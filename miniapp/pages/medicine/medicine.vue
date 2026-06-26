<template>
	<view class="layout">
		<view class="search-area">
			<uni-search-bar placeholder="搜索药品名称或适应症..." v-model="keyword" @input="onSearch" bgColor="#f0f2f5" />
		</view>

		<scroll-view scroll-x class="tag-scroll">
			<view class="tag-item" :class="{ active: currentTag === '' }" @click="switchTag('')">全部</view>
			<view v-for="t in allTags" :key="t" class="tag-item" :class="{ active: currentTag === t }" @click="switchTag(t)">
				{{ t }}
			</view>
		</scroll-view>

		<scroll-view class="drug-list" scroll-y="true">
			<view v-for="item in filteredList" :key="item.id" class="drug-card" @click="gotoDetail(item)">
				<image :src="item.imgPath" mode="aspectFill" class="drug-img" />
				<view class="drug-info">
					<view class="info-top">
						<view class="info-left">
							<text class="name">{{ item.medicineName }}</text>
							<text class="factory">{{ item.medicineBrand }}</text>
						</view>
						<text class="price">¥{{ item.medicinePrice.toFixed(2) }}</text>
					</view>
					<view class="info-tags">
						<text class="tag tag-green">适应症: {{ shortText(item.medicineEffect, 20) }}</text>
						<text class="tag tag-blue">用法: {{ shortText(item.usAge, 15) }}</text>
						<text class="tag tag-red">禁忌: {{ shortText(item.taboo, 15) }}</text>
					</view>
				</view>
			</view>
		</scroll-view>
	</view>

	<!-- #ifdef H5 -->
	<TabBar />
	<!-- #endif -->
</template>

<script setup>
import TabBar from '@/components/TabBar.vue'
import { ref, computed } from 'vue'
import { medicinePage } from "@/api/articles.js"

const gotoDetail = (e) => {
	uni.navigateTo({
		url: `/pages/medicine/medicineDetail?id=${e.id}`
	})
}

const MedicineList = ref([])
const keyword = ref('')
const currentTag = ref('')

const filteredList = computed(() => MedicineList.value.filter(item => {
  const hitTag = !currentTag.value || item.keyword.split(',').some(k => k.trim() === currentTag.value)
  const hitKeyword = !keyword.value || item.medicineName.includes(keyword.value) || item.medicineEffect.includes(keyword.value)
  return hitTag && hitKeyword
}))

const allTags = computed(() => {
  const set = new Set()
  MedicineList.value.forEach(item =>
    item.keyword.split(',').forEach(k => set.add(k.trim()))
  )
  return Array.from(set)
})

async function getmedicine() {
  try {
    const CSparams = {
      pageNum: 1,
      pageSize: 100,
      sortField: 'createTime',
      sortOrder: 'desc',
    }
    const res = await medicinePage(CSparams)
    MedicineList.value = res.data.data
  } catch {
    uni.showToast({ title: '获取药品列表失败', icon: 'none' })
  }
}

function switchTag(tag) {
  currentTag.value = tag
}

function onSearch() {}

function shortText(text, maxLen = 15) {
	if (!text) return ''
	return text.length > maxLen ? text.slice(0, maxLen) + '...' : text
}

getmedicine()
</script>

<style lang="scss" scoped>
.layout {
	min-height: 100vh;
	background: #f7f8fa;
	display: flex;
	flex-direction: column;
}

.search-area {
	padding: 16rpx 24rpx;
	background: #fff;
}

.tag-scroll {
	white-space: nowrap;
	padding: 20rpx 24rpx;
	background: #fff;
	border-bottom: 1rpx solid #f0f0f0;
}

.tag-item {
	display: inline-block;
	padding: 12rpx 28rpx;
	margin-right: 16rpx;
	background: #f0f2f5;
	color: #666;
	border-radius: 32rpx;
	font-size: 26rpx;
	transition: all 0.3s ease;

	&.active {
		background: linear-gradient(135deg, #2e7d32, #43a047);
		color: #fff;
		box-shadow: 0 4rpx 12rpx rgba(46, 125, 50, 0.25);
	}
}

.drug-list {
	flex: 1;
	padding: 24rpx;
}

.drug-card {
	display: flex;
	background: #fff;
	border-radius: 24rpx;
	padding: 24rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
	transition: transform 0.2s ease;

	&:active {
		transform: scale(0.98);
	}
}

.drug-img {
	width: 160rpx;
	height: 160rpx;
	border-radius: 20rpx;
	margin-right: 24rpx;
	flex-shrink: 0;
}

.drug-info {
	flex: 1;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
}

.info-top {
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
}

.info-left {
	flex: 1;
}

.name {
	font-size: 32rpx;
	font-weight: 700;
	color: #222;
	display: block;
}

.factory {
	font-size: 24rpx;
	color: #999;
	margin-top: 4rpx;
	display: block;
}

.price {
	font-size: 34rpx;
	font-weight: 800;
	color: #e63946;
	flex-shrink: 0;
}

.info-tags {
	display: flex;
	flex-direction: column;
	gap: 8rpx;
	margin-top: 16rpx;
}

.tag {
	font-size: 24rpx;
	padding: 6rpx 16rpx;
	border-radius: 8rpx;
	display: inline-block;
	line-height: 1.5;
}

.tag-green {
	color: #2e7d32;
	background: rgba(46, 125, 50, 0.08);
}

.tag-blue {
	color: #1565c0;
	background: rgba(21, 101, 192, 0.08);
}

.tag-red {
	color: #c62828;
	background: rgba(198, 40, 40, 0.08);
}
</style>
