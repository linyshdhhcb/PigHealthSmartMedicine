<template>
	<view v-if="loading" class="center">
		<view class="loading-spinner"></view>
		<text class="loading-text">正在调取病历...</text>
	</view>

	<view v-else-if="error" class="center">
		<uni-icons type="closeempty" size="60" color="#ddd" />
		<text class="error-text">病历调取失败</text>
		<button class="retry" @click="loadAll">重新调取</button>
	</view>

	<scroll-view v-else scroll-y class="page">
		<view class="record-header">
			<view class="header-bg"></view>
			<view class="header-content">
				<view class="header-top">
					<view class="icon-wrap">
						<uni-icons type="staff" size="28" color="#fff" />
					</view>
					<view class="title-wrap">
						<text class="name">{{ illnessData.illnessName }}</text>
						<text class="id">编号：{{ illnessData.id }}</text>
					</view>
				</view>
				<text class="update">更新于 {{ illnessData.updateTime }}</text>
			</view>
		</view>

		<view class="content-area">
			<view class="block">
				<view class="block-hd">
					<view class="hd-dot dot-orange"></view>
					<text class="hd-txt">病因 / 诱因</text>
				</view>
				<view class="block-bd">
					<mp-html :content="illnessData.includeReason" />
				</view>
			</view>

			<view class="block">
				<view class="block-hd">
					<view class="hd-dot dot-red"></view>
					<text class="hd-txt">常见症状</text>
				</view>
				<view class="block-bd">
					<mp-html :content="illnessData.illnessSymptom" />
				</view>
			</view>

			<view class="block">
				<view class="block-hd">
					<view class="hd-dot dot-purple"></view>
					<text class="hd-txt">特殊症状</text>
				</view>
				<view class="block-bd">
					<mp-html :content="illnessData.specialSymptom" />
				</view>
			</view>

			<view class="block" v-if="medicineList.length">
				<view class="block-hd">
					<view class="hd-dot dot-green"></view>
					<text class="hd-txt">推荐治疗方案</text>
					<text class="hd-sub">{{ medicineList.length }} 种药物</text>
				</view>

				<view class="drug-list">
					<view class="drug-card" v-for="m in medicineList" :key="m.id" @click="toDrugDetail(m.id)">
						<image class="drug-img" :src="m.imgPath" mode="aspectFill" />
						<view class="drug-info">
							<view class="info-hd">
								<text class="name">{{ m.medicineName }}</text>
								<text class="price">¥{{ m.medicinePrice }}</text>
							</view>
							<text class="brand">{{ m.medicineBrand }}</text>
							<text class="effect">{{ m.medicineEffect }}</text>
							<view class="tags">
								<text class="tag tag-blue">用法：{{ m.usAge }}</text>
								<text class="tag tag-red">禁忌：{{ m.taboo }}</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="footer">
			<text class="footer-text">创建：{{ illnessData.createTime }} · 更新：{{ illnessData.updateTime }}</text>
		</view>
	</scroll-view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import {
  illnessGetinfo,
  illnessMedicinePage,
  medicineGetinfo
} from '@/api/articles.js'

const loading = ref(true)
const error = ref(false)
const illnessData = ref({})
const medicineList = ref([])
const illnessId = ref('')

onLoad((opt) => {
  illnessId.value = opt.id
  loadAll()
})

async function loadAll() {
  loading.value = true
  error.value = false
  try {
    await getIllnessInfo()
    await getMedicineList()
  } catch {
    error.value = true
  } finally {
    loading.value = false
  }
}

async function getIllnessInfo() {
  const res = await illnessGetinfo(illnessId.value)
  illnessData.value = res.data
}

async function getMedicineList() {
  const rel = await illnessMedicinePage({
    pageNum: 1,
    pageSize: 100
  })

  const ids = rel.data.data
    .filter(i => i.illnessId === illnessData.value.id)
    .map(i => i.medicineId)

  if (!ids.length) return

  const tasks = ids.map(id => medicineGetinfo(id))
  const resArr = await Promise.all(tasks)
  medicineList.value = resArr.map(r => r.data)
}

function toDrugDetail(id) {
  uni.navigateTo({ url: `/pages/medicine/medicineDetail?id=${id}` })
}
</script>

<style lang="scss" scoped>
.page {
	min-height: 100vh;
	background: #f7f8fa;
}

.center {
	height: 100vh;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
}

.loading-spinner {
	width: 48rpx;
	height: 48rpx;
	border: 4rpx solid #e0e0e0;
	border-top-color: #43a047;
	border-radius: 50%;
	animation: spin 0.8s linear infinite;
	margin-bottom: 20rpx;
}

@keyframes spin {
	to { transform: rotate(360deg); }
}

.loading-text {
	font-size: 28rpx;
	color: #999;
}

.error-text {
	font-size: 28rpx;
	color: #999;
	margin: 20rpx 0;
}

.retry {
	margin-top: 16rpx;
	padding: 16rpx 56rpx;
	font-size: 28rpx;
	color: #fff;
	background: linear-gradient(135deg, #2e7d32, #43a047);
	border-radius: 40rpx;
}

.record-header {
	position: relative;
	overflow: hidden;
}

.header-bg {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 40%, #43a047 70%, #66bb6a 100%);
}

.header-content {
	position: relative;
	z-index: 1;
	padding: 48rpx 32rpx 40rpx;
}

.header-top {
	display: flex;
	align-items: center;
	margin-bottom: 16rpx;
}

.icon-wrap {
	width: 72rpx;
	height: 72rpx;
	border-radius: 20rpx;
	background: rgba(255, 255, 255, 0.15);
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 20rpx;
}

.title-wrap {
	flex: 1;
}

.name {
	display: block;
	font-size: 40rpx;
	font-weight: 700;
	color: #fff;
}

.id {
	display: block;
	font-size: 24rpx;
	color: rgba(255, 255, 255, 0.7);
	margin-top: 4rpx;
}

.update {
	font-size: 24rpx;
	color: rgba(255, 255, 255, 0.6);
}

.content-area {
	padding: 24rpx 24rpx 0;
	margin-top: -16rpx;
	position: relative;
	z-index: 1;
}

.block {
	background: #fff;
	border-radius: 24rpx;
	padding: 28rpx 32rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.block-hd {
	display: flex;
	align-items: center;
	margin-bottom: 20rpx;
}

.hd-dot {
	width: 12rpx;
	height: 12rpx;
	border-radius: 50%;
	margin-right: 12rpx;
}

.dot-orange { background: #ff9800; box-shadow: 0 0 0 6rpx rgba(255, 152, 0, 0.15); }
.dot-red { background: #e63946; box-shadow: 0 0 0 6rpx rgba(230, 57, 70, 0.15); }
.dot-purple { background: #9c27b0; box-shadow: 0 0 0 6rpx rgba(156, 39, 176, 0.15); }
.dot-green { background: #43a047; box-shadow: 0 0 0 6rpx rgba(67, 160, 71, 0.15); }

.hd-txt {
	font-size: 32rpx;
	font-weight: 700;
	color: #222;
}

.hd-sub {
	margin-left: auto;
	font-size: 24rpx;
	color: #999;
}

.drug-list {
	display: flex;
	flex-direction: column;
	gap: 16rpx;
	margin-top: 16rpx;
}

.drug-card {
	display: flex;
	background: #f7f8fa;
	border-radius: 20rpx;
	padding: 20rpx;
	gap: 20rpx;
	transition: transform 0.2s ease;

	&:active {
		transform: scale(0.98);
	}
}

.drug-img {
	width: 140rpx;
	height: 140rpx;
	border-radius: 16rpx;
	flex-shrink: 0;
}

.drug-info {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.info-hd {
	display: flex;
	justify-content: space-between;
	align-items: baseline;
}

.name {
	font-size: 32rpx;
	font-weight: 700;
	color: #222;
}

.price {
	font-size: 32rpx;
	color: #e63946;
	font-weight: 800;
}

.brand {
	font-size: 24rpx;
	color: #999;
	margin-top: 4rpx;
}

.effect {
	font-size: 26rpx;
	color: #555;
	margin-top: 8rpx;
	line-height: 1.5;
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 2;
	overflow: hidden;
}

.tags {
	margin-top: 12rpx;
	display: flex;
	flex-wrap: wrap;
	gap: 8rpx;
}

.tag {
	font-size: 22rpx;
	padding: 4rpx 12rpx;
	border-radius: 6rpx;
}

.tag-blue {
	color: #1565c0;
	background: rgba(21, 101, 192, 0.08);
}

.tag-red {
	color: #c62828;
	background: rgba(198, 40, 40, 0.08);
}

.footer {
	text-align: center;
	padding: 32rpx 0 80rpx;
}

.footer-text {
	font-size: 24rpx;
	color: #bbb;
}
</style>
