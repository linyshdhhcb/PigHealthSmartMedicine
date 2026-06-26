<template>
	<view class="layout">
		<scroll-view scroll-y class="scroll">
			<view class="header">
				<view class="header-bg"></view>
				<view class="header-content">
					<view class="profile">
						<image class="avatar" src="/static/images/logo.jpg" mode="aspectFill" />
						<view class="profile-info">
							<text class="username">欢迎回来</text>
							<text class="user-role">生猪健康管理平台</text>
						</view>
					</view>
					<view class="stats-row">
						<view class="stat-item">
							<text class="stat-num">12</text>
							<text class="stat-label">咨询次数</text>
						</view>
						<view class="stat-item">
							<text class="stat-num">8</text>
							<text class="stat-label">收藏文章</text>
						</view>
						<view class="stat-item">
							<text class="stat-num">25</text>
							<text class="stat-label">查询记录</text>
						</view>
						<view class="stat-item">
							<text class="stat-num">45</text>
							<text class="stat-label">使用天数</text>
						</view>
					</view>
				</view>
			</view>

			<view class="menu-area">
				<view class="menu-group">
					<view class="menu-item" v-for="item in menuList1" :key="item.title">
						<view class="menu-left">
							<view class="menu-icon" :class="item.color">
								<image class="icon-img" :src="item.icon" mode="aspectFill" />
							</view>
							<view class="menu-text">
								<text class="menu-title">{{ item.title }}</text>
								<text class="menu-desc">{{ item.desc }}</text>
							</view>
						</view>
						<uni-icons type="right" size="18" color="#ccc" />
					</view>
				</view>

				<view class="menu-group">
					<view class="menu-item" v-for="item in menuList2" :key="item.title">
						<view class="menu-left">
							<view class="menu-icon" :class="item.color">
								<image class="icon-img" :src="item.icon" mode="aspectFill" />
							</view>
							<view class="menu-text">
								<text class="menu-title">{{ item.title }}</text>
								<text class="menu-desc">{{ item.desc }}</text>
							</view>
						</view>
						<uni-icons type="right" size="18" color="#ccc" />
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

const menuList1 = [
	{ title: '我的咨询', desc: '查看历史咨询记录', icon: '/static/images/user1.png', color: 'bg-green' },
	{ title: '我的收藏', desc: '收藏的文章和咨询', icon: '/static/images/user2.png', color: 'bg-blue' },
	{ title: '使用记录', desc: '疾病查询和药品使用记录', icon: '/static/images/user3.png', color: 'bg-orange' },
]

const menuList2 = [
	{ title: '消息通知', desc: '系统消息和提醒', icon: '/static/images/user4.png', color: 'bg-purple' },
	{ title: '隐私设置', desc: '个人信息和隐私保护', icon: '/static/images/user5.png', color: 'bg-red' },
]
</script>

<style lang="scss" scoped>
.layout {
	height: 100%;
	overflow: hidden;
}

.scroll {
	height: 100vh;
}

.header {
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

.profile {
	display: flex;
	align-items: center;
	margin-bottom: 36rpx;
}

.avatar {
	width: 96rpx;
	height: 96rpx;
	border-radius: 50%;
	border: 4rpx solid rgba(255, 255, 255, 0.4);
	margin-right: 24rpx;
}

.profile-info {
	flex: 1;
}

.username {
	display: block;
	font-size: 36rpx;
	font-weight: 700;
	color: #fff;
}

.user-role {
	display: block;
	font-size: 24rpx;
	color: rgba(255, 255, 255, 0.7);
	margin-top: 4rpx;
}

.stats-row {
	display: flex;
	background: rgba(255, 255, 255, 0.12);
	border-radius: 20rpx;
	padding: 28rpx 0;
}

.stat-item {
	flex: 1;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.stat-num {
	font-size: 36rpx;
	font-weight: 800;
	color: #fff;
}

.stat-label {
	font-size: 22rpx;
	color: rgba(255, 255, 255, 0.7);
	margin-top: 4rpx;
}

.menu-area {
	padding: 24rpx 32rpx 120rpx;
}

.menu-group {
	background: #fff;
	border-radius: 24rpx;
	padding: 8rpx 0;
	margin-bottom: 24rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.menu-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 28rpx 32rpx;
	transition: background 0.2s ease;

	&:active {
		background: #f7f8fa;
	}
}

.menu-left {
	display: flex;
	align-items: center;
	flex: 1;
}

.menu-icon {
	width: 72rpx;
	height: 72rpx;
	border-radius: 20rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 24rpx;
}

.bg-green {
	background: linear-gradient(135deg, #e8f5e9, #c8e6c9);
}

.bg-blue {
	background: linear-gradient(135deg, #e3f2fd, #bbdefb);
}

.bg-orange {
	background: linear-gradient(135deg, #fff3e0, #ffe0b2);
}

.bg-purple {
	background: linear-gradient(135deg, #f3e5f5, #e1bee7);
}

.bg-red {
	background: linear-gradient(135deg, #fce4ec, #f8bbd0);
}

.icon-img {
	width: 44rpx;
	height: 44rpx;
}

.menu-text {
	flex: 1;
}

.menu-title {
	display: block;
	font-size: 30rpx;
	font-weight: 600;
	color: #333;
}

.menu-desc {
	display: block;
	font-size: 24rpx;
	color: #999;
	margin-top: 4rpx;
}
</style>
