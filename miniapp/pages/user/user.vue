<template>
	<view class="layout">
		
		<view class="top">
			<view class="left">
				<image src="https://tse4-mm.cn.bing.net/th/id/OIP-C.Xd88RmKtrH3ORAfPnL3gwAAAAA?w=168&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7" mode=""></image>
				<view class="text">欢迎回来</view>
			</view>
			<view class="right">
				<uni-icons class="icon icon1" type="notification" size="25"></uni-icons>
				<uni-icons class="icon icon2" type="gear" size="25"></uni-icons>
			</view>
		</view>
		
		<view class="center">
			<view class="modal modal1">
				<!-- <uni-icons class="icon" type="email" size="20"></uni-icons> -->
				<image class="icon"  src="/static/images/user1.png" mode="aspectFill"></image>
				<view class="view">12</view>
				<view class="futitle">资讯次数</view>
			</view>
			<view class="modal modal2">
				<!-- <uni-icons class="icon" type="email" size="20"></uni-icons> -->
				<image class="icon"  src="/static/images/user2.png" mode="aspectFill"></image>
				<view class="view">8</view>
				<view class="futitle">收藏文章</view>
			</view>
			<view class="modal modal3">
				<!-- <uni-icons class="icon" type="email" size="20"></uni-icons> -->
				<image class="icon"  src="/static/images/user3.png" mode="aspectFill"></image>
				<view class="view">25</view>
				<view class="futitle">查询记录</view>
			</view>
			<view class="modal modal4">
				<!-- <uni-icons class="icon" type="email" size="20"></uni-icons> -->
				<image class="icon"  src="/static/images/user4.png" mode="aspectFill"></image>
				<view class="view">45</view>
				<view class="futitle">使用天数</view>
			</view>
		</view>
		
		<view class="bottom">
			<view class="box">
				
				<view class="item ">
					<view class="bottom-left">
						<image src="/static/images/user1.png" mode=""></image>
						<view class="bottom-center">
							<view class="bottom-title">我的咨询</view>
							<view class="bottom-futitle">查看历史咨询记录</view>
						</view>
					</view>
					<view class="bottom-right">
						<uni-icons type="right" size="20"></uni-icons>
					</view>
				</view>
				
				<view class="item ">
					<view class="bottom-left">
						<image src="/static/images/user2.png" mode=""></image>
						<view class="bottom-center">
							<view class="bottom-title">我的收藏</view>
							<view class="bottom-futitle">收藏的文章和咨询</view>
						</view>
					</view>
					<view class="bottom-right">
						<uni-icons type="right" size="20"></uni-icons>
					</view>
				</view>
				<view class="item ">
					<view class="bottom-left">
						<image src="/static/images/user3.png" mode=""></image>
						<view class="bottom-center">
							<view class="bottom-title">使用记录</view>
							<view class="bottom-futitle">疾病查询和药品使用记录</view>
						</view>
					</view>
					<view class="bottom-right">
						<uni-icons type="right" size="20"></uni-icons>
					</view>
				</view>
				<view class="item ">
					<view class="bottom-left">
						<image src="/static/images/user4.png" mode=""></image>
						<view class="bottom-center">
							<view class="bottom-title">消息通知</view>
							<view class="bottom-futitle">系统消息和提醒</view>
						</view>
					</view>
					<view class="bottom-right">
						<uni-icons type="right" size="20"></uni-icons>
					</view>
				</view>
				<view class="item ">
					<view class="bottom-left">
						<image src="/static/images/user5.png" mode=""></image>
						<view class="bottom-center">
							<view class="bottom-title">隐私设置</view>
							<view class="bottom-futitle">个人信息和隐私保护</view>
						</view>
					</view>
					<view class="bottom-right">
						<uni-icons type="right" size="20"></uni-icons>
					</view>
				</view>
				
				
			</view>
		</view>
		
		<!-- #ifdef H5 -->
			<TabBar />
		<!-- #endif -->
		
	</view>
</template>

<script setup>
import TabBar from '@/components/TabBar.vue'
</script>

<style lang="scss" scoped>
.layout{
	// background:
	// linear-gradient(to bottom,transparent,#fff 400rpx),
	// linear-gradient(to right,#81C784 20%,#beecd8 );
	height: 100%;
	overflow: hidden;
	.top{
		
		display: flex;
		margin-top: 5rpx;
		padding: 10rpx 10rpx;
		justify-content: space-between;
		align-items: center;
		height: 5vh;
		border-bottom: 1px solid #eee;
		margin-bottom: 10rpx;
		.left{
			display: flex;
			height: 100%;
			image{
				width: 45rpx;
				height: 45rpx;
				border-radius: 50%;
				margin: auto 10rpx;
			}
			.text{
				margin-left: 14rpx;
				justify-content: center;
				align-items: center;
				font-weight: 800;
				margin: auto 0;
			}
		}
		.right{
			display: flex;
			.icon{
				border-radius: 50%;
				
				
			}
			.icon1{
				margin-right: 12rpx
			}
		}
	}
	
	.center{
	
		padding: 60rpx 0;
		margin: 15rpx 15rpx;
		display: flex;
		justify-content: space-between;
		border: 1px solid #eee;
		.modal{
			
			
			margin: 15rpx 15rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			.view{
				font-weight: 900;
				font-size: 36rpx;
			}
			.icon{
				width: 30px;
				height: 30px;
			}
			.futitle{
				font-weight: 400;
				color: dimgray;
			}
		}
		.modal4{
			margin-right: 20rpx;
		}
	}
	
	
	.bottom{
		margin: 30rpx 30rpx;
		height: 1000rpx;
		background: rgba(249, 250, 251,0.9);
		.box{
			.item{
				height: 100rpx;
				margin: 30rpx 0rpx;
				background: ghostwhite;
				display: flex;
				justify-content: space-between;
				.bottom-left{
					display: flex;
					align-items: center;
					image{
						margin-left: 30rpx;
						
						width: 50rpx;
						height: 50rpx;
					}
					.bottom-center{
						margin-left: 20rpx;
						.bottom-title{
							color: black;
							font-size: 30rpx;
						}
						.bottom-futitle{
							color: dimgray;
						}
					}
				}
			}
		}
	}
}
</style>
