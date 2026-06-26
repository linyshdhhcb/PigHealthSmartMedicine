<template>
  <scroll-view scroll-y="true" class="scroll">
    <view class="container">
      <view class="search-box">
        <uni-search-bar bgColor="#f5f5f5" class="search-input" placeholder="搜索知识库..." v-model="searchKeyword" @input="handleSearch" />
      </view>

      <scroll-view scroll-x class="type-scroll">
        <view class="type-tag" :class="{ active: currentLevel === 'base' }" @tap="switchLevel('base')">知识库</view>
        <view class="type-tag" :class="{ active: currentLevel === 'doc' }" @tap="switchLevel('doc')" v-if="currentKb">文档</view>
        <view class="type-tag" :class="{ active: currentLevel === 'chunk' }" @tap="switchLevel('chunk')" v-if="currentDoc">分块</view>
      </scroll-view>

      <scroll-view class="list-area" scroll-y @scrolltolower="loadMore" refresher-enabled :refresher-triggered="isRefreshing" @refresherrefresh="onRefresh">
        <view v-if="currentLevel === 'base'">
          <view class="kb-item" v-for="kb in kbList" :key="kb.id" @tap="enterKb(kb)">
            <view class="kb-name">{{ kb.name }}</view>
            <view class="kb-meta">
              <text class="kb-model">{{ kb.embeddingModel }}</text>
              <text class="kb-time">{{ formatDate(kb.createTime) }}</text>
            </view>
          </view>
        </view>

        <view v-if="currentLevel === 'doc' && currentKb">
          <view class="breadcrumb">
            <text class="bc-link" @tap="goBack('base')">← 返回知识库</text>
            <text class="bc-current">{{ currentKb.name }}</text>
          </view>
          <view class="doc-item" v-for="doc in docList" :key="doc.id" @tap="enterDoc(doc)">
            <view class="doc-name">{{ doc.fileName }}</view>
            <view class="doc-meta">
              <text :class="['doc-status', doc.status === 'ready' ? 'status-ready' : 'status-pending']">{{ doc.status }}</text>
              <text class="doc-chunks">分块: {{ doc.chunkCount || 0 }}</text>
              <text class="doc-time">{{ formatDate(doc.createTime) }}</text>
            </view>
          </view>
        </view>

        <view v-if="currentLevel === 'chunk' && currentDoc">
          <view class="breadcrumb">
            <text class="bc-link" @tap="goBack('doc')">← 返回文档</text>
            <text class="bc-current">{{ currentDoc.fileName }}</text>
          </view>
          <view class="chunk-item" v-for="chunk in chunkList" :key="chunk.id">
            <view class="chunk-header">
              <text class="chunk-index">#{{ chunk.chunkIndex }}</text>
              <text class="chunk-count">{{ chunk.charCount }}字 / {{ chunk.tokenCount || '-' }}token</text>
            </view>
            <text class="chunk-content">{{ chunk.content }}</text>
          </view>
        </view>

        <view class="loading-state" v-if="loading">
          <text class="loading-text">加载中...</text>
        </view>
        <view class="loading-state" v-if="!hasMore && dataList.length > 0">
          <text class="loading-text">没有更多了</text>
        </view>

        <!-- #ifdef H5 -->
        <TabBar />
        <!-- #endif -->
      </scroll-view>
    </view>
  </scroll-view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { knowledgeBasePage } from '@/api/knowledgeBase.js'
import { knowledgePage } from '@/api/knowledge.js'
import { knowledgeChunkPage } from '@/api/knowledgeChunk.js'
import { checkLogin } from '@/api/user.js'
import TabBar from '@/components/TabBar.vue'

const searchKeyword = ref('')
const currentLevel = ref('base')
const currentKb = ref(null)
const currentDoc = ref(null)
const loading = ref(false)
const isRefreshing = ref(false)
const hasMore = ref(true)
const pageNum = ref(1)
const pageSize = 10

const kbList = ref([])
const docList = ref([])
const chunkList = ref([])

const dataList = computed(() => {
  if (currentLevel.value === 'base') return kbList.value
  if (currentLevel.value === 'doc') return docList.value
  return chunkList.value
})

onLoad(() => checkLoginStatus())

async function checkLoginStatus() {
  const token = uni.getStorageSync('token')
  if (!token) return redirectToLogin()
  const res = await checkLogin()
  res.data ? loadList() : redirectToLogin()
}

function redirectToLogin() {
  uni.reLaunch({ url: '/pages/login/login' })
}

function switchLevel(level) {
  currentLevel.value = level
  pageNum.value = 1
  loadList()
}

function enterKb(kb) {
  currentKb.value = kb
  currentLevel.value = 'doc'
  pageNum.value = 1
  docList.value = []
  loadList()
}

function enterDoc(doc) {
  currentDoc.value = doc
  currentLevel.value = 'chunk'
  pageNum.value = 1
  chunkList.value = []
  loadList()
}

function goBack(level) {
  if (level === 'base') {
    currentKb.value = null
    currentDoc.value = null
  } else if (level === 'doc') {
    currentDoc.value = null
  }
  currentLevel.value = level
  pageNum.value = 1
  loadList()
}

async function loadList(isRefresh = false) {
  if (isRefresh) { pageNum.value = 1; hasMore.value = true }
  if (!hasMore.value && !isRefresh) return
  loading.value = true
  try {
    let res
    if (currentLevel.value === 'base') {
      res = await knowledgeBasePage({ pageNum: pageNum.value, pageSize, name: searchKeyword.value || undefined })
      kbList.value = isRefresh ? (res.data.data || []) : [...kbList.value, ...(res.data.data || [])]
    } else if (currentLevel.value === 'doc') {
      res = await knowledgePage({ pageNum: pageNum.value, pageSize, kbId: currentKb.value.id, fileName: searchKeyword.value || undefined })
      docList.value = isRefresh ? (res.data.data || []) : [...docList.value, ...(res.data.data || [])]
    } else {
      res = await knowledgeChunkPage({ pageNum: pageNum.value, pageSize, kbId: currentKb.value.id, docId: currentDoc.value.id })
      chunkList.value = isRefresh ? (res.data.data || []) : [...chunkList.value, ...(res.data.data || [])]
    }
    const list = res.data.data || []
    hasMore.value = list.length === pageSize
    pageNum.value++
  } catch {
    uni.showToast({ title: '加载失败', icon: 'none' })
  } finally {
    loading.value = false
    isRefreshing.value = false
  }
}

function loadMore() {
  if (!loading.value && hasMore.value) loadList()
}

function onRefresh() {
  isRefreshing.value = true
  loadList(true)
}

function handleSearch() {
  if (currentLevel.value === 'base') {
    kbList.value = []
  } else if (currentLevel.value === 'doc') {
    docList.value = []
  }
  loadList(true)
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const safeStr = dateStr.replace(/-/g, '/')
  const d = new Date(safeStr)
  if (isNaN(d.getTime())) return dateStr
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}
</script>

<style scoped lang="scss">
.scroll { height: 95vh; }
.container { display: flex; flex-direction: column; height: 100vh; background-color: #f8f8f8; }
.search-box { padding: 20rpx; background-color: #fff; position: sticky; top: 0; z-index: 100; }
.search-input { border-radius: 60rpx; flex: 1; height: 70rpx; font-size: 28rpx; }
.type-scroll { white-space: nowrap; padding: 20rpx 0 10rpx; background: #fff; }
.type-tag { display: inline-block; padding: 10rpx 24rpx; margin-left: 20rpx; font-size: 28rpx; color: #333; border-radius: 30rpx; background: #f2f2f2; }
.type-tag.active { background: linear-gradient(135deg, #43a047, #81c784); color: #fff; }
.list-area { flex: 1; padding: 20rpx; }

.breadcrumb { display: flex; align-items: center; padding: 16rpx 0; margin-bottom: 16rpx; }
.bc-link { color: #43a047; font-size: 28rpx; margin-right: 16rpx; }
.bc-current { font-size: 28rpx; color: #333; font-weight: bold; }

.kb-item, .doc-item, .chunk-item { background-color: #fff; border-radius: 12rpx; margin-bottom: 20rpx; padding: 30rpx; box-shadow: 0 2rpx 10rpx 0 rgba(0,0,0,0.05); }
.kb-name, .doc-name { font-size: 32rpx; font-weight: bold; color: #333; margin-bottom: 12rpx; }
.kb-meta, .doc-meta { display: flex; justify-content: space-between; font-size: 24rpx; color: #999; }
.kb-model { color: #43a047; }

.doc-status { padding: 4rpx 16rpx; border-radius: 20rpx; font-size: 22rpx; }
.status-ready { background: #e8f5e9; color: #43a047; }
.status-pending { background: #fff3e0; color: #ff9800; }
.doc-chunks { color: #666; }

.chunk-header { display: flex; justify-content: space-between; margin-bottom: 12rpx; }
.chunk-index { font-size: 28rpx; font-weight: bold; color: #43a047; }
.chunk-count { font-size: 24rpx; color: #999; }
.chunk-content { font-size: 26rpx; color: #666; line-height: 1.6; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 4; overflow: hidden; }

.loading-state { text-align: center; padding: 30rpx 0; }
.loading-text { font-size: 24rpx; color: #999; }
</style>
