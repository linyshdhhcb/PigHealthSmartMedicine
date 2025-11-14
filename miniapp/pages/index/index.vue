<template>
	<view class="layout">
		<view class="user">
			<view class="left">
				<image src="https://tse4-mm.cn.bing.net/th/id/OIP-C.Xd88RmKtrH3ORAfPnL3gwAAAAA?w=168&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7" mode=""></image>
				<view class="text">欢迎回来</view>
			</view>
			<view class="right">
				<uni-icons class="icon icon1" type="notification" size="25"></uni-icons>
				<uni-icons class="icon icon2" type="gear" size="25"></uni-icons>
			</view>
		</view>
		
		
		<scroll-view class="main-scroll" scroll-y="true">
			<view class="pingpai">
				<view class="text">
					<view class="text1">智慧猪医</view>
					<view class="text2">专业的生猪健康管理平台</view>
					<view class="number">
						<view class="number1">
							<view class="n11">1000+</view>
							<view class="n12">疾病率</view>
						</view>
						<view class="number2">
							<view class="n21">500+</view>
							<view class="n22">药品库</view>
						</view>
						<view class="number3">
							<view class="n31">10000+</view>
							<view class="n32">用户</view>
						</view>
					</view>
				</view>
			</view>
			
			<view class="main">
					<view  class="item" @click="gotoDoctor">
						<image src="/static/images/item-doctor.png" mode="aspectFill"></image>
						<view class="title">AI兽医</view>
						<view class="futitle">智能诊断,快速准确</view>
					</view>
				
				<view class="item" @click="gotoIllness">
					<image src="/static/images/search-h.png" mode="aspectFill"></image>
					<view class="title">疾病查询</view>
					<view class="futitle">疾病信息，症状诊断</view>
				</view>
				<view class="item" @click="gotoMedecine">
					<image src="/static/images/item-medicine.png" mode=""></image>
					<view class="title">药品管理</view>
					<view class="futitle">用药指导，安全用药</view>
				</view>
				<view class="item" @click="gotoKnowledge">
					<image src="/static/images/item-book.png" mode=""></image>
					<view class="title">知识库</view>
					<view class="futitle">养殖知识，最新资讯</view>
				</view>
			</view>
			
			<view class="bottom">
				<view class="news-title">最近活动</view>
				<view class="news" v-for="(item,index) in activityList" :key="index">
					<view class="activity-dot" :class="`dot-${item.color}`"></view>
					<view class="activity-content">
						<view class="activity-text">{{item.text}}</view>
						<view class="activity-time">{{item.time}}</view>
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
import { ref } from 'vue';

const activityList = ref([
  { text: 'AI诊断：疑似猪瘟症状', time: '2小时前', color: 'green' },
  { text: '查看了青霉素使用说明', time: '1天前', color: 'blue' },
  { text: '阅读了《猪病预防指南》', time: '3天前', color: 'purple' },
]);

const gotoDoctor = () => {
	uni.switchTab({
		url:"/pages/AIDoctor/AIDoctor"
	})
}


const gotoIllness = () => {
	uni.switchTab({
		url:"/pages/SearchIllness/SearchIllness"
	})
}

const gotoKnowledge = () => {
	uni.switchTab({
		url:"/pages/Knowledge/Knowledge"
	})
}

const gotoMedecine = () => {
	uni.navigateTo({
		url:"/pages/medicine/medicine"
	})
}

</script>

<style lang="scss" scoped>
.layout{
	
	height: 100%;
	overflow: hidden;
	.user{
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
	
	.main-scroll {
	  height: calc(100vh  ); // 减去头部、中间模块等的高度，确保有剩余可滚动区域
	   // height: 100%;
	}
	
	
	.pingpai{
			background: 
			// linear-gradient(to bottom,transparent,#fff 400rpx),
			linear-gradient(to right, #2E7D32,#81C784);
			min-height: 30vh;
		.text{
			// color: #eee;
			width: 100%;
			height: 100%;
			.text1{
				color: #eee;
				font-size: 40rpx;
				padding: 20rpx 20rpx;
				font-weight: 900;
			}
			.text2{
				color: #eee;
				padding-left: 20rpx;
				font-weight: 500;
				font-size: 30rpx;
			}
			.number{
				display: flex;
				padding: 30rpx 20rpx;
				.number1{
					.n11{
						font-weight: 900;
						font-size: 40rpx;
						color: #eee;
					}
					.n12{
						color: lightgray;
					}
				}
				.number2{
					margin-left: 30rpx;
					.n21{
						font-weight: 900;
						font-size: 40rpx;
						color: #eee;
					}
					.n22{
						color: lightgray;
					}
				}
				.number3{
					margin-left: 30rpx;
					.n31{
						font-weight: 900;
						font-size: 40rpx;
						color: #eee;
					}
					.n32{
						color: lightgray;
					}
				}
			}
		}
	}
	
	.main{
		padding-top: 20rpx;
		display: grid;
		height: 35vh;
		gap: 10rpx;
		grid-template-columns: 1fr 1fr;
		.item{
			border: 1px solid #eee;
			background-color: rgb(239, 246, 255);
			height: 100%;
			width: 100%;
			image{
				width: 100rpx;
				height: 100rpx;
				margin-left: 110rpx;
				margin-right: 110rpx;
				justify-content: center;
				align-items: center;
			}
			.title{
				font-weight: 700;
				margin-left: 110rpx;
				margin-right: 110rpx;
			}
			.futitle{
				color: gray;
				white-space: nowrap;
				margin-left: 10%;
			}
		}
	}
	
	.bottom{
		.news-title{
			margin-top: 30rpx;
			font-size: 38rpx;
			font-weight: bold;
			margin-bottom: 15rpx;
		}
		.news{
			margin-bottom: 12px;
			  display: flex;
			  align-items: flex-start;
		}
		.activity-dot {
		  width: 10px;
		  height: 10px;
		  border-radius: 50%;
		  margin-right: 8px;
		  margin-top: 3px;
		}
		
		.dot-green {
		  background-color: #4CAF50;
		}
		
		.dot-blue {
		  background-color: #2196F3;
		}
		
		.dot-purple {
		  background-color: #9C27B0;
		}
	
		
		.activity-text {
		  font-size: 14px;
		  margin-bottom: 2px;
		}
		
		.activity-time {
		  font-size: 12px;
		  color: #666;
		}
	}
	
	
}
</style>
