<template>
  <scroll-view scroll-y="true" class="scroll">
    <view class="container">
      <view class="search-box">
        <uni-search-bar bgColor="#f0f2f5" class="search-input" placeholder="搜索知识库..." v-model="searchKeyword" @input="handleSearch" />
      </view>

      <scroll-view scroll-x class="type-scroll">
        <view class="type-tag" :class="{ active: currentLevel === 'base' }" @tap="switchLevel('base')">知识库</view>
        <view class="type-tag" :class="{ active: currentLevel === 'doc' }" @tap="switchLevel('doc')" v-if="currentKb">文档</view>
        <view class="type-tag" :class="{ active: currentLevel === 'chunk' }" @tap="switchLevel('chunk')" v-if="currentDoc">分块</view>
      </scroll-view>

      <scroll-view class="list-area" scroll-y @scrolltolower="loadMore" refresher-enabled :refresher-triggered="isRefreshing" @refresherrefresh="onRefresh">
        <view v-if="currentLevel === 'base'">
          <view class="kb-item" v-for="kb in kbList" :key="kb.id" @tap="enterKb(kb)">
            <view class="kb-icon">
              <uni-icons type="shop" size="24" color="#43a047" />
            </view>
            <view class="kb-content">
              <view class="kb-name">{{ kb.name }}</view>
              <view class="kb-meta">
                <text class="kb-model">{{ kb.embeddingModel }}</text>
                <text class="kb-time">{{ formatDate(kb.createTime) }}</text>
              </view>
            </view>
            <uni-icons type="right" size="18" color="#ccc" />
          </view>
        </view>

        <view v-if="currentLevel === 'doc' && currentKb">
          <view class="breadcrumb">
            <text class="bc-link" @tap="goBack('base')">返回知识库</text>
            <text class="bc-sep">/</text>
            <text class="bc-current">{{ currentKb.name }}</text>
          </view>
          <view class="doc-item" v-for="doc in docList" :key="doc.id" @tap="enterDoc(doc)">
            <view class="doc-icon" :class="doc.status === 'ready' ? 'icon-ready' : 'icon-pending'">
              <uni-icons :type="doc.status === 'ready' ? 'checkboxFilled' : 'spinner'" size="20" :color="doc.status === 'ready' ? '#43a047' : '#ff9800'" />
            </view>
            <view class="doc-content">
              <view class="doc-name">{{ doc.fileName }}</view>
              <view class="doc-meta">
                <text :class="['doc-status', doc.status === 'ready' ? 'status-ready' : 'status-pending']">{{ doc.status }}</text>
                <text class="doc-chunks">{{ doc.chunkCount || 0 }} 分块</text>
                <text class="doc-time">{{ formatDate(doc.createTime) }}</text>
              </view>
            </view>
          </view>
        </view>

        <view v-if="currentLevel === 'chunk' && currentDoc">
          <view class="breadcrumb">
            <text class="bc-link" @tap="goBack('doc')">返回文档</text>
            <text class="bc-sep">/</text>
            <text class="bc-current">{{ currentDoc.fileName }}</text>
          </view>
          <view class="chunk-item" v-for="chunk in chunkList" :key="chunk.id">
            <view class="chunk-header">
              <view class="chunk-index-wrap">
                <text class="chunk-index">#{{ chunk.chunkIndex }}</text>
              </view>
              <text class="chunk-count">{{ chunk.charCount }}字 / {{ chunk.tokenCount || '-' }}token</text>
            </view>
            <text class="chunk-content">{{ chunk.content }}</text>
          </view>
        </view>

        <view class="loading-state" v-if="loading">
          <view class="loading-spinner"></view>
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
.container { display: flex; flex-direction: column; height: 100vh; background-color: #f7f8fa; }
.search-box { padding: 20rpx 24rpx; background-color: #fff; position: sticky; top: 0; z-index: 100; border-bottom: 1rpx solid #f0f0f0; }
.search-input { border-radius: 60rpx; flex: 1; height: 70rpx; font-size: 28rpx; }
.type-scroll { white-space: nowrap; padding: 20rpx 24rpx; background: #fff; }
.type-tag { display: inline-block; padding: 12rpx 28rpx; margin-right: 16rpx; font-size: 28rpx; color: #666; border-radius: 32rpx; background: #f0f2f5; transition: all 0.3s ease; }
.type-tag.active { background: linear-gradient(135deg, #2e7d32, #43a047); color: #fff; box-shadow: 0 4rpx 12rpx rgba(46, 125, 50, 0.25); }
.list-area { flex: 1; padding: 20rpx 24rpx; }

.breadcrumb { display: flex; align-items: center; padding: 16rpx 0; margin-bottom: 16rpx; }
.bc-link { color: #43a047; font-size: 28rpx; }
.bc-sep { color: #ccc; margin: 0 12rpx; font-size: 28rpx; }
.bc-current { font-size: 28rpx; color: #333; font-weight: 600; }

.kb-item {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 20rpx;
  margin-bottom: 16rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
  transition: transform 0.2s ease;

  &:active { transform: scale(0.98); }
}

.kb-icon {
  width: 72rpx;
  height: 72rpx;
  border-radius: 20rpx;
  background: linear-gradient(135deg, #e8f5e9, #c8e6c9);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.kb-content { flex: 1; }
.kb-name { font-size: 30rpx; font-weight: 600; color: #222; margin-bottom: 8rpx; }
.kb-meta { display: flex; gap: 16rpx; font-size: 24rpx; color: #999; }
.kb-model { color: #43a047; }

.doc-item {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 20rpx;
  margin-bottom: 16rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
  transition: transform 0.2s ease;

  &:active { transform: scale(0.98); }
}

.doc-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.icon-ready { background: linear-gradient(135deg, #e8f5e9, #c8e6c9); }
.icon-pending { background: linear-gradient(135deg, #fff3e0, #ffe0b2); }

.doc-content { flex: 1; }
.doc-name { font-size: 30rpx; font-weight: 600; color: #222; margin-bottom: 8rpx; }
.doc-meta { display: flex; gap: 16rpx; align-items: center; font-size: 24rpx; }

.doc-status { padding: 4rpx 16rpx; border-radius: 20rpx; font-size: 22rpx; }
.status-ready { background: #e8f5e9; color: #43a047; }
.status-pending { background: #fff3e0; color: #ff9800; }
.doc-chunks { color: #666; }

.chunk-item {
  background-color: #fff;
  border-radius: 20rpx;
  margin-bottom: 16rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.chunk-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12rpx; }
.chunk-index-wrap { display: inline-flex; }
.chunk-index { font-size: 26rpx; font-weight: 700; color: #43a047; background: rgba(67, 160, 71, 0.08); padding: 4rpx 16rpx; border-radius: 8rpx; }
.chunk-count { font-size: 24rpx; color: #999; }
.chunk-content { font-size: 26rpx; color: #666; line-height: 1.6; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 4; overflow: hidden; }

.loading-state { display: flex; flex-direction: column; align-items: center; padding: 40rpx 0; }
.loading-spinner { width: 40rpx; height: 40rpx; border: 3rpx solid #e0e0e0; border-top-color: #43a047; border-radius: 50%; animation: spin 0.8s linear infinite; margin-bottom: 12rpx; }
@keyframes spin { to { transform: rotate(360deg); } }
.loading-text { font-size: 24rpx; color: #999; }
</style>
