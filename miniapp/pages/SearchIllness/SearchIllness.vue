<template>
  <scroll-view scroll-y class="scroll">
    <view class="illness-page">
      <!-- 搜索栏 -->
      <uni-search-bar
        class="search-bar"
        placeholder="搜索疾病名称或症状..."
        v-model="keyword"
        @input="onSearch"
      ></uni-search-bar>

      <!-- 分类横向滚动 -->
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

      <!-- 疾病卡片列表 -->
      <view class="card-list">
        <view
          v-for="item in showList"
          :key="item.id"
          class="card"
          @click="gotoDetail(item)"
        >
          <!-- 左侧疾病图 -->
          <image class="pic" :src="item.imageUrl" mode="aspectFill" />

          <!-- 右侧疾病信息 -->
          <view class="info">
            <view class="top">
              <view class="name">{{ item.illnessName }}</view>
              <view class="views">
                👁️ {{ item.pageviews || 0 }}
              </view>
            </view>

            <view class="kind">{{ item.kindName }} · {{ item.kindInfo }}</view>

            <view class="desc">
              <view class="desc-item">
                <text class="desc-label">诱发原因：</text>
                <text class="desc-value" v-if="item.illnessSymptom.length > 100">免疫力太弱</text>
                <text class="desc-value" v-else>{{ shortText(item.includeReason, 40) }}</text>
              </view>
              <view class="desc-item">
                <text class="desc-label">主要症状：</text>
                <text class="desc-value" v-if="item.illnessSymptom.length > 100">高热，食欲不振，呼吸困难，皮肤发红</text>
				<text class="desc-value" v-else>{{ shortText(item.illnessSymptom, 40) }}</text>
              </view>
              <view class="desc-item">
                <text class="desc-label">特殊症状：</text>
                <text class="desc-value" v-if="item.specialSymptom.length > 100">抽搐，脱水，休克</text>
				<text class="desc-value" v-else>{{ shortText(item.specialSymptom, 40) }}</text>
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
import { illnessPage, illnessKindPage, illnessMedicinePage, pageviewPage,pageviewAdd } from '@/api/articles.js'
import { ref, computed } from 'vue'

/* ---------- 基础变量 ---------- */
const kindIllnessList = ref([])
const IllnessList = ref([])
const keyword = ref('')
const activeKind = ref(0)
const IllnessMedicineList = ref([])
const pageviewList = ref([])

/* ---------- 图片资源随机绑定 ---------- */
const pigImages = [
  '/static/images/pig1.jpg',
  '/static/images/pig2.jpg',
  '/static/images/pig3.jpg',
  '/static/images/pig4.jpg',
  '/static/images/pig5.jpg'
]

// 稳定随机：利用疾病id生成固定索引
function getStableRandomImage(id) {
  const hash = Array.from(String(id))
    .reduce((acc, ch) => acc + ch.charCodeAt(0), 0)
  return pigImages[hash % pigImages.length]
}

/* ---------- 导航 ---------- */
const gotoDetail = async (item) => {
	//1.浏览量加1
	try{
		await pageviewAdd({
			illnessId:item.id,
			pageviews:(item.pageviews || 0) +1
		})
		//本地也先加1，防止用户秒回列表看到数字没变化
		item.pageviews = (item.pageviews || 0) +1
	} catch(e){
		//即使失败也不阻塞跳转
		console.error('浏览量+1失败',e)
	}
	
	//2.跳转详情
  uni.navigateTo({
    url: `/pages/SearchIllness/IllnessDetail/IllnessDetail?id=${item.id}`
  })
}

/* ---------- 获取疾病浏览量分页数据 ---------- */
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

/* ---------- 疾病、分类、药物数据 ---------- */
async function getillnessMedicinePage() {
  try {
    const params = {
      pageNum: 1,
      pageSize: 100
    }
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

/* ---------- 合并kindName / 浏览量 / 图片 ---------- */
function mergeAll(list1, kinds, views) {
  const kindMap = new Map(kinds.map(k => [k.id, { name: k.name, info: k.info }]))
  const viewMap = new Map()

  // 累加相同 illnessId 的浏览量
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

/* ---------- 搜索 & 筛选 ---------- */
const onSearch = () => {}
function selectKind(id) {
  activeKind.value = id
}

/* ---------- 最终展示 ---------- */
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

/* ---------- 辅助函数 ---------- */
function shortText(text, maxLen = 50) {
  if (!text) return ''
  return text.length > maxLen ? text.slice(0, maxLen) + '...' : text
}

/* ---------- 初始化 ---------- */
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

.search-bar {
  background: white;
  padding: 10rpx 0;
  border-bottom: 1rpx solid #eee;
}

.kind-scroll {
  white-space: nowrap;
  padding: 20rpx 0 10rpx;
  background: #fff;
  box-shadow: 0 4rpx 10rpx rgba(0, 0, 0, 0.05);
}
.kind-tag {
  display: inline-block;
  padding: 10rpx 30rpx;
  margin-left: 20rpx;
  font-size: 28rpx;
  color: #444;
  border-radius: 40rpx;
  background: #f1f3f5;
  transition: all 0.3s ease;
  &.active {
    background: linear-gradient(135deg, #43a047, #81c784);
    color: #fff;
    box-shadow: 0 6rpx 12rpx rgba(67, 160, 71, 0.3);
  }
}

.card-list {
  padding: 24rpx;
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.card {
  display: flex;
  background: #fff;
  border-radius: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  &:hover {
    transform: translateY(-4rpx);
    box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.08);
  }
  .pic {
    width: 200rpx;
    height: 200rpx;
    object-fit: cover;
    flex-shrink: 0;
  }
  .info {
    flex: 1;
    padding: 20rpx;
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
    .views {
      font-size: 26rpx;
      color: #999;
    }
  }
  .kind {
    font-size: 26rpx;
    color: #666;
    margin: 6rpx 0 10rpx;
  }
  .desc {
    font-size: 26rpx;
    color: #444;
    display: flex;
    flex-direction: column;
    gap: 6rpx;
    .desc-item {
      display: flex;
      flex-wrap: wrap;
      line-height: 1.5;
      .desc-label {
        color: #888;
        width: 150rpx;
        flex-shrink: 0;
      }
      .desc-value {
        flex: 1;
        color: #333;
      }
    }
  }
}
</style>
