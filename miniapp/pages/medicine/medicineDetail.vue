<template>
	<view v-if="loading" class="center">
		<view class="loading-spinner"></view>
		<text class="loading-text">加载中...</text>
	</view>

	<view v-else-if="error" class="center">
		<uni-icons type="closeempty" size="60" color="#ddd" />
		<text class="error-text">获取失败</text>
		<button class="retry" @click="getmedicineGetinfo">重新加载</button>
	</view>

	<scroll-view v-else scroll-y class="page">
		<view class="hero-section">
			<image class="hero-img" :src="medicineInfo.imgPath" mode="aspectFill" @click="preview" />
			<view class="hero-overlay"></view>
		</view>

		<view class="main-card">
			<view class="card-header">
				<view class="header-left">
					<text class="medicine-name">{{ medicineInfo.medicineName }}</text>
					<text class="medicine-brand">{{ medicineInfo.medicineBrand }}</text>
				</view>
				<view class="price-wrap">
					<text class="price-symbol">¥</text>
					<text class="price-num">{{ medicineInfo.medicinePrice }}</text>
				</view>
			</view>

			<view class="keyword-tag" v-if="medicineInfo.keyword">
				<text class="keyword-text">{{ medicineInfo.keyword }}</text>
			</view>
		</view>

		<view class="info-section">
			<view class="section-item">
				<view class="section-label label-green">
					<text class="label-text">功效</text>
				</view>
				<text class="section-content">{{ medicineInfo.medicineEffect }}</text>
			</view>

			<view class="section-item">
				<view class="section-label label-blue">
					<text class="label-text">用法</text>
				</view>
				<text class="section-content">{{ medicineInfo.usAge }}</text>
			</view>

			<view class="section-item">
				<view class="section-label label-red">
					<text class="label-text">禁忌</text>
				</view>
				<text class="section-content content-danger">{{ medicineInfo.taboo }}</text>
			</view>

			<view class="section-item">
				<view class="section-label label-orange">
					<text class="label-text">相互作用</text>
				</view>
				<text class="section-content">{{ medicineInfo.interaction }}</text>
			</view>
		</view>

		<view class="footer">
			<text class="footer-text">更新时间：{{ medicineInfo.updateTime }}</text>
		</view>
	</scroll-view>
</template>

<script setup>
import { medicineGetinfo } from '@/api/articles.js'
import { onLoad } from '@dcloudio/uni-app'
import { ref } from 'vue'

const medicineId = ref('')
const medicineInfo = ref({})
const loading = ref(true)
const error = ref(false)

async function getmedicineGetinfo() {
  loading.value = true
  error.value = false
  try {
    const res = await medicineGetinfo(medicineId.value)
    medicineInfo.value = res.data
  } catch {
    error.value = true
  } finally {
    loading.value = false
  }
}

function preview() {
  uni.previewImage({ urls: [medicineInfo.value.imgPath] })
}

onLoad((option) => {
  medicineId.value = option.id
  getmedicineGetinfo()
})
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
	padding: 16rpx 56rpx;
	font-size: 28rpx;
	color: #fff;
	background: linear-gradient(135deg, #2e7d32, #43a047);
	border-radius: 40rpx;
}

.hero-section {
	position: relative;
	height: 400rpx;
	overflow: hidden;
}

.hero-img {
	width: 100%;
	height: 100%;
}

.hero-overlay {
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
	height: 120rpx;
	background: linear-gradient(transparent, rgba(0, 0, 0, 0.3));
}

.main-card {
	background: #fff;
	margin: -40rpx 24rpx 0;
	border-radius: 24rpx;
	padding: 32rpx;
	position: relative;
	z-index: 1;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
}

.card-header {
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
}

.header-left {
	flex: 1;
}

.medicine-name {
	font-size: 40rpx;
	font-weight: 700;
	color: #222;
	display: block;
}

.medicine-brand {
	font-size: 26rpx;
	color: #999;
	margin-top: 8rpx;
	display: block;
}

.price-wrap {
	display: flex;
	align-items: baseline;
	flex-shrink: 0;
}

.price-symbol {
	font-size: 28rpx;
	color: #e63946;
	font-weight: 700;
}

.price-num {
	font-size: 48rpx;
	color: #e63946;
	font-weight: 800;
}

.keyword-tag {
	margin-top: 20rpx;
	display: inline-flex;
}

.keyword-text {
	font-size: 24rpx;
	color: #43a047;
	background: rgba(67, 160, 71, 0.08);
	padding: 8rpx 20rpx;
	border-radius: 20rpx;
}

.info-section {
	margin: 24rpx;
	background: #fff;
	border-radius: 24rpx;
	overflow: hidden;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.section-item {
	padding: 28rpx 32rpx;
	border-bottom: 1rpx solid #f5f5f5;

	&:last-child {
		border-bottom: none;
	}
}

.section-label {
	display: inline-flex;
	padding: 6rpx 16rpx;
	border-radius: 8rpx;
	margin-bottom: 12rpx;
}

.label-text {
	font-size: 24rpx;
	font-weight: 600;
}

.label-green {
	background: rgba(46, 125, 50, 0.08);
	.label-text { color: #2e7d32; }
}

.label-blue {
	background: rgba(21, 101, 192, 0.08);
	.label-text { color: #1565c0; }
}

.label-red {
	background: rgba(198, 40, 40, 0.08);
	.label-text { color: #c62828; }
}

.label-orange {
	background: rgba(230, 81, 0, 0.08);
	.label-text { color: #e65100; }
}

.section-content {
	font-size: 28rpx;
	line-height: 1.7;
	color: #444;
	display: block;
}

.content-danger {
	color: #c62828;
	font-weight: 500;
}

.footer {
	text-align: center;
	padding: 40rpx 0 80rpx;
}

.footer-text {
	font-size: 24rpx;
	color: #bbb;
}
</style>
