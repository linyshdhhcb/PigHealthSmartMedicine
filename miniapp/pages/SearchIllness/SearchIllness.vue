<template>
  <scroll-view scroll-y class="scroll">
    <view class="illness-page">
      <view class="search-area">
        <uni-search-bar
          class="search-bar"
          placeholder="搜索疾病名称或症状..."
          v-model="keyword"
          @input="onSearch"
          bgColor="#f0f2f5"
        />
      </view>

      <scroll-view scroll-x class="kind-scroll">
        <view
          v-for="k in kindIllnessList"
          :key="k.id"
          class="kind-tag"
          :class="{ active: activeKind === k.id }"
          @tap="selectKind(k.id)"
        >
          {{ k.name }}
        </view>
      </scroll-view>

      <view class="card-list">
        <view
          v-for="item in showList"
          :key="item.id"
          class="card"
          @click="gotoDetail(item)"
        >
          <image class="pic" :src="item.imageUrl" mode="aspectFill" />
          <view class="info">
            <view class="top">
              <text class="name">{{ item.illnessName }}</text>
              <view class="views-wrap">
                <uni-icons type="eye" size="14" color="#bbb" />
                <text class="views">{{ item.pageviews || 0 }}</text>
              </view>
            </view>
            <view class="kind-tag-row" v-if="item.kindName">
              <text class="kind-badge">{{ item.kindName }}</text>
              <text class="kind-info" v-if="item.kindInfo">{{ item.kindInfo }}</text>
            </view>
            <view class="desc">
              <view class="desc-item">
                <text class="desc-label">诱发原因</text>
                <text class="desc-value" v-if="item.illnessSymptom.length > 100">免疫力太弱</text>
                <text class="desc-value" v-else>{{ shortText(item.includeReason, 30) }}</text>
              </view>
              <view class="desc-item">
                <text class="desc-label">主要症状</text>
                <text class="desc-value" v-if="item.illnessSymptom.length > 100">高热，食欲不振，呼吸困难</text>
                <text class="desc-value" v-else>{{ shortText(item.illnessSymptom, 30) }}</text>
              </view>
              <view class="desc-item">
                <text class="desc-label">特殊症状</text>
                <text class="desc-value" v-if="item.specialSymptom.length > 100">抽搐，脱水，休克</text>
                <text class="desc-value" v-else>{{ shortText(item.specialSymptom, 30) }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- #ifdef H5 -->
      <TabBar />
      <!-- #endif -->
    </view>
  </scroll-view>
</template>

<script setup>
import TabBar from '@/components/TabBar.vue'
import { illnessPage, illnessKindPage, illnessMedicinePage, pageviewPage, pageviewAdd } from '@/api/articles.js'
import { ref, computed } from 'vue'

const kindIllnessList = ref([])
const IllnessList = ref([])
const keyword = ref('')
const activeKind = ref(0)
const IllnessMedicineList = ref([])
const pageviewList = ref([])

const pigImages = [
  '/static/images/pig1.jpg',
  '/static/images/pig2.jpg',
  '/static/images/pig3.jpg',
  '/static/images/pig4.jpg',
  '/static/images/pig5.jpg'
]

function getStableRandomImage(id) {
  const hash = Array.from(String(id))
    .reduce((acc, ch) => acc + ch.charCodeAt(0), 0)
  return pigImages[hash % pigImages.length]
}

const gotoDetail = async (item) => {
	try {
		await pageviewAdd({
			illnessId: item.id,
			pageviews: (item.pageviews || 0) + 1
		})
		item.pageviews = (item.pageviews || 0) + 1
	} catch (e) {
		console.error('浏览量+1失败', e)
	}

  uni.navigateTo({
    url: `/pages/SearchIllness/IllnessDetail/IllnessDetail?id=${item.id}`
  })
}

async function getpageviewPage() {
  try {
    const params = {
      pageNum: 1,
      pageSize: 200,
      sortField: 'createTime',
      sortOrder: 'desc'
    }
    const res = await pageviewPage(params)
    pageviewList.value = res.data.data || []
  } catch {
    uni.showToast({ title: '获取浏览量失败', icon: 'none' })
  }
}

async function getillnessMedicinePage() {
  try {
    const params = { pageNum: 1, pageSize: 100 }
    const res = await illnessMedicinePage(params)
    IllnessMedicineList.value = res.data.data
  } catch {
    uni.showToast({ title: '获取疾病-药物列表失败', icon: 'none' })
  }
}

async function getKind() {
  try {
    const res = await illnessKindPage({ pageNum: 1, pageSize: 100 })
    kindIllnessList.value = [{ id: 0, name: '全部' }, ...res.data.data]
  } catch {
    uni.showToast({ title: '获取分类失败', icon: 'none' })
  }
}

async function getList() {
  try {
    const res = await illnessPage({ pageNum: 1, pageSize: 200 })
    IllnessList.value = res.data.data
  } catch {
    uni.showToast({ title: '获取疾病失败', icon: 'none' })
  }
}

function mergeAll(list1, kinds, views) {
  const kindMap = new Map(kinds.map(k => [k.id, { name: k.name, info: k.info }]))
  const viewMap = new Map()

  views.forEach(v => {
    viewMap.set(v.illnessId, (viewMap.get(v.illnessId) || 0) + v.pageviews)
  })

  return list1.map(it => {
    const { name = '', info = '' } = kindMap.get(it.kindId) || {}
    return {
      ...it,
      kindName: name,
      kindInfo: info,
      pageviews: viewMap.get(it.id) || 0,
      imageUrl: getStableRandomImage(it.id)
    }
  })
}

const onSearch = () => {}
function selectKind(id) {
  activeKind.value = id
}

const showList = computed(() => {
  let list = IllnessList.value
  if (activeKind.value) list = list.filter(i => i.kindId === activeKind.value)
  if (keyword.value.trim()) {
    const key = keyword.value.trim().toLowerCase()
    list = list.filter(
      i =>
        i.kindName?.toLowerCase().includes(key) ||
        i.illnessName?.toLowerCase().includes(key) ||
        i.illnessSymptom?.toLowerCase().includes(key)
    )
  }
  return list
})

function shortText(text, maxLen = 30) {
  if (!text) return ''
  return text.length > maxLen ? text.slice(0, maxLen) + '...' : text
}

Promise.all([getKind(), getpageviewPage(), getList()]).then(() => {
  IllnessList.value = mergeAll(IllnessList.value, kindIllnessList.value, pageviewList.value)
})
getillnessMedicinePage()
</script>

<style lang="scss" scoped>
.scroll {
  height: 100vh;
}

.illness-page {
  background: #f7f8fa;
  min-height: 100vh;
  padding-bottom: 80rpx;
}

.search-area {
  background: #fff;
  padding: 16rpx 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.search-bar {
  background: white;
}

.kind-scroll {
  white-space: nowrap;
  padding: 20rpx 24rpx;
  background: #fff;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.03);
}

.kind-tag {
  display: inline-block;
  padding: 12rpx 28rpx;
  margin-right: 16rpx;
  font-size: 28rpx;
  color: #555;
  border-radius: 32rpx;
  background: #f0f2f5;
  transition: all 0.3s ease;

  &.active {
    background: linear-gradient(135deg, #2e7d32, #43a047);
    color: #fff;
    box-shadow: 0 4rpx 12rpx rgba(46, 125, 50, 0.25);
  }
}

.card-list {
  padding: 24rpx;
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.card {
  display: flex;
  background: #fff;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
  overflow: hidden;
  transition: transform 0.2s ease;

  &:active {
    transform: scale(0.98);
  }

  .pic {
    width: 200rpx;
    height: 200rpx;
    object-fit: cover;
    flex-shrink: 0;
  }

  .info {
    flex: 1;
    padding: 20rpx 24rpx;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .top {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .name {
      font-size: 32rpx;
      font-weight: 700;
      color: #222;
    }

    .views-wrap {
      display: flex;
      align-items: center;
      gap: 4rpx;
    }

    .views {
      font-size: 24rpx;
      color: #bbb;
    }
  }

  .kind-tag-row {
    display: flex;
    align-items: center;
    gap: 8rpx;
    margin: 8rpx 0;
  }

  .kind-badge {
    font-size: 22rpx;
    color: #43a047;
    background: rgba(67, 160, 71, 0.08);
    padding: 4rpx 12rpx;
    border-radius: 6rpx;
  }

  .kind-info {
    font-size: 24rpx;
    color: #999;
  }

  .desc {
    font-size: 24rpx;
    color: #444;
    display: flex;
    flex-direction: column;
    gap: 6rpx;

    .desc-item {
      display: flex;
      line-height: 1.5;

      .desc-label {
        color: #999;
        width: 120rpx;
        flex-shrink: 0;
        font-size: 24rpx;
      }

      .desc-value {
        flex: 1;
        color: #555;
        font-size: 24rpx;
      }
    }
  }
}
</style>
