<template>
	<view class="layout">
		<scroll-view class="main-scroll" scroll-y="true">
			<view class="hero">
				<view class="hero-bg"></view>
				<view class="hero-content">
					<view class="hero-top">
						<image class="hero-avatar" src="/static/images/logo.jpg" mode="aspectFill" />
						<view class="hero-greeting">
							<text class="greeting-text">欢迎回来</text>
							<text class="greeting-sub">智慧猪医 · 专业生猪健康管理</text>
						</view>
						<view class="hero-actions">
							<view class="action-btn">
								<uni-icons type="notification" size="22" color="#fff" />
							</view>
						</view>
					</view>
					<view class="stats-row">
						<view class="stat-item">
							<text class="stat-num">1000+</text>
							<text class="stat-label">疾病库</text>
						</view>
						<view class="stat-divider"></view>
						<view class="stat-item">
							<text class="stat-num">500+</text>
							<text class="stat-label">药品库</text>
						</view>
						<view class="stat-divider"></view>
						<view class="stat-item">
							<text class="stat-num">10000+</text>
							<text class="stat-label">用户</text>
						</view>
					</view>
				</view>
			</view>

			<view class="content-area">
				<view class="section-header">
					<text class="section-title">快捷功能</text>
				</view>
				<view class="feature-grid">
					<view class="feature-card" @click="gotoDoctor">
						<view class="feature-icon-wrap icon-green">
							<image class="feature-icon" src="/static/images/item-doctor.png" mode="aspectFill" />
						</view>
						<text class="feature-name">AI兽医</text>
						<text class="feature-desc">智能诊断</text>
					</view>
					<view class="feature-card" @click="gotoIllness">
						<view class="feature-icon-wrap icon-blue">
							<image class="feature-icon" src="/static/images/search-h.png" mode="aspectFill" />
						</view>
						<text class="feature-name">疾病查询</text>
						<text class="feature-desc">症状诊断</text>
					</view>
					<view class="feature-card" @click="gotoMedecine">
						<view class="feature-icon-wrap icon-orange">
							<image class="feature-icon" src="/static/images/item-medicine.png" mode="aspectFill" />
						</view>
						<text class="feature-name">药品管理</text>
						<text class="feature-desc">安全用药</text>
					</view>
					<view class="feature-card" @click="gotoKnowledge">
						<view class="feature-icon-wrap icon-purple">
							<image class="feature-icon" src="/static/images/item-book.png" mode="aspectFill" />
						</view>
						<text class="feature-name">知识库</text>
						<text class="feature-desc">养殖资讯</text>
					</view>
				</view>

				<view class="section-header">
					<text class="section-title">最近动态</text>
					<text class="section-more">查看全部</text>
				</view>
				<view class="activity-list">
					<view class="activity-item" v-for="(item, index) in activityList" :key="index">
						<view class="activity-left">
							<view class="activity-dot" :class="`dot-${item.color}`"></view>
							<view v-if="index < activityList.length - 1" class="activity-line"></view>
						</view>
						<view class="activity-content">
							<text class="activity-text">{{ item.text }}</text>
							<text class="activity-time">{{ item.time }}</text>
						</view>
					</view>
				</view>
			</view>
		</scroll-view>

		<!-- #ifdef H5 -->
		<TabBar />
		<!-- #endif -->
	</view>
</template>

<script setup>
import TabBar from '@/components/TabBar.vue'
import { ref } from 'vue'

const activityList = ref([
  { text: 'AI诊断：疑似猪瘟症状', time: '2小时前', color: 'green' },
  { text: '查看了青霉素使用说明', time: '1天前', color: 'blue' },
  { text: '阅读了《猪病预防指南》', time: '3天前', color: 'purple' },
])

const gotoDoctor = () => {
	uni.switchTab({ url: '/pages/AIDoctor/AIDoctor' })
}

const gotoIllness = () => {
	uni.switchTab({ url: '/pages/SearchIllness/SearchIllness' })
}

const gotoKnowledge = () => {
	uni.switchTab({ url: '/pages/Knowledge/Knowledge' })
}

const gotoMedecine = () => {
	uni.navigateTo({ url: '/pages/medicine/medicine' })
}
</script>

<style lang="scss" scoped>
.layout {
	height: 100%;
	overflow: hidden;
}

.main-scroll {
	height: 100vh;
}

.hero {
	position: relative;
	min-height: 340rpx;
	overflow: hidden;
}

.hero-bg {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 30%, #43a047 60%, #66bb6a 100%);
}

.hero-content {
	position: relative;
	z-index: 1;
	padding: 40rpx 32rpx 36rpx;
}

.hero-top {
	display: flex;
	align-items: center;
	margin-bottom: 40rpx;
}

.hero-avatar {
	width: 72rpx;
	height: 72rpx;
	border-radius: 50%;
	border: 4rpx solid rgba(255, 255, 255, 0.4);
	margin-right: 20rpx;
}

.hero-greeting {
	flex: 1;
}

.greeting-text {
	display: block;
	font-size: 36rpx;
	font-weight: 700;
	color: #fff;
}

.greeting-sub {
	display: block;
	font-size: 24rpx;
	color: rgba(255, 255, 255, 0.75);
	margin-top: 4rpx;
}

.hero-actions {
	display: flex;
}

.action-btn {
	width: 72rpx;
	height: 72rpx;
	border-radius: 50%;
	background: rgba(255, 255, 255, 0.15);
	display: flex;
	align-items: center;
	justify-content: center;
}

.stats-row {
	display: flex;
	align-items: center;
	justify-content: space-around;
	background: rgba(255, 255, 255, 0.12);
	border-radius: 20rpx;
	padding: 28rpx 0;
}

.stat-item {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.stat-num {
	font-size: 40rpx;
	font-weight: 800;
	color: #fff;
}

.stat-label {
	font-size: 24rpx;
	color: rgba(255, 255, 255, 0.7);
	margin-top: 4rpx;
}

.stat-divider {
	width: 1rpx;
	height: 60rpx;
	background: rgba(255, 255, 255, 0.2);
}

.content-area {
	padding: 0 32rpx 120rpx;
	margin-top: -20rpx;
	position: relative;
	z-index: 2;
}

.section-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin: 36rpx 0 20rpx;
}

.section-title {
	font-size: 34rpx;
	font-weight: 700;
	color: #222;
}

.section-more {
	font-size: 26rpx;
	color: #43a047;
}

.feature-grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 20rpx;
}

.feature-card {
	background: #fff;
	border-radius: 24rpx;
	padding: 32rpx 24rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
	transition: transform 0.2s ease, box-shadow 0.2s ease;

	&:active {
		transform: scale(0.97);
	}
}

.feature-icon-wrap {
	width: 96rpx;
	height: 96rpx;
	border-radius: 24rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 16rpx;
}

.icon-green {
	background: linear-gradient(135deg, #e8f5e9, #c8e6c9);
}

.icon-blue {
	background: linear-gradient(135deg, #e3f2fd, #bbdefb);
}

.icon-orange {
	background: linear-gradient(135deg, #fff3e0, #ffe0b2);
}

.icon-purple {
	background: linear-gradient(135deg, #f3e5f5, #e1bee7);
}

.feature-icon {
	width: 56rpx;
	height: 56rpx;
}

.feature-name {
	font-size: 30rpx;
	font-weight: 600;
	color: #222;
	margin-bottom: 4rpx;
}

.feature-desc {
	font-size: 24rpx;
	color: #999;
}

.activity-list {
	background: #fff;
	border-radius: 24rpx;
	padding: 28rpx 32rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.activity-item {
	display: flex;
	align-items: flex-start;
	min-height: 80rpx;
}

.activity-left {
	width: 40rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
	flex-shrink: 0;
	padding-top: 6rpx;
}

.activity-dot {
	width: 18rpx;
	height: 18rpx;
	border-radius: 50%;
	flex-shrink: 0;
}

.dot-green {
	background: #43a047;
	box-shadow: 0 0 0 6rpx rgba(67, 160, 71, 0.15);
}

.dot-blue {
	background: #2196f3;
	box-shadow: 0 0 0 6rpx rgba(33, 150, 243, 0.15);
}

.dot-purple {
	background: #9c27b0;
	box-shadow: 0 0 0 6rpx rgba(156, 39, 176, 0.15);
}

.activity-line {
	width: 2rpx;
	flex: 1;
	background: #eee;
	margin-top: 8rpx;
}

.activity-content {
	flex: 1;
	padding-left: 16rpx;
	padding-bottom: 24rpx;
}

.activity-text {
	display: block;
	font-size: 28rpx;
	color: #333;
	line-height: 1.5;
}

.activity-time {
	display: block;
	font-size: 24rpx;
	color: #bbb;
	margin-top: 4rpx;
}
</style>
