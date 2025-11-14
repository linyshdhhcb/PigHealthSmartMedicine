<template>
	
	<view class="layout">
		<uni-search-bar placeholder="搜索药品名称或适应症..." v-model="keyword" @input="onSearch"  ></uni-search-bar>
		
		
		<scroll-view scroll-x class="tag-scroll">
			<view class="tag-item" :class="{active :currentTag === ''}" @click="switchTag('')">全部</view>
				<view v-for="t in allTags" :key="t" class="tag-item" :class="{active:currentTag === t}" @click="switchTag(t)">
				{{t}}
				</view>
		</scroll-view>
		
		<scroll-view class="drug-list" scroll-y="true">
			<view v-for="item in filteredList" :key="item.id" class="drug-card"  @click="gotoDetail(item)">
			<image :src="item.imgPath" mode="aspectFill" class="drug-img"></image>
			<view class="drug-info">
				<view class="box1">
					<view class="left">
						<view class="name">{{item.medicineName}}</view>
						<view class="factory">{{item.medicineBrand}}</view>
					</view>
					<view class="price right">￥{{item.medicinePrice.toFixed(2)}}</view>
				</view>
				<view class="effect">✔️适应症:{{item.medicineEffect}}</view>
				<view class="usage">ℹ️用法:{{item.usAge}}</view>
				<view class="taboo">⚠️禁忌{{item.taboo}}</view>
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
	console.log('e',e);
	uni.navigateTo({
		url:`/pages/medicine/medicineDetail?id=${e.id}`
	})
}

// 1. 定义响应式数据
const MedicineList = ref([])
const keyword = ref('')
const currentTag = ref('')

// 2. 定义计算属性（必须在组件实例作用域内）
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

// 3. 异步获取数据的函数
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
    console.log('MedicineList.value', MedicineList.value);
  } catch {
    uni.showToast({ title: '获取药品列表失败', icon: 'none' })
  }
}

// 4. 切换标签和搜索的方法
function switchTag(tag) {
  currentTag.value = tag
}

function onSearch() {
  // keyword 已双向绑定，自动触发 filteredList 计算属性
}

// 5. 页面加载时调用接口
getmedicine()
</script>

<style lang="scss" scoped>

.tag-scroll{
	white-space: nowrap;
	padding: 20rpx 0 0 24rpx;
	height: 100rpx;
	.tag-item{
		display: inline-block;
		padding: 12rpx 28rpx;
		margin-right: 20rpx;
		background: #e6f7ff;
		color: #1890ff;
		border-radius: 30rpx;
		font-size: 26rpx;
		&.active{
			background: rgb(22, 163, 74);
			color: #fff;
		}
	}
}

.drug-list{
	flex:1;
	padding: 0 24rpx 30rpx;
	.drug-card{
		display: flex;
		background: #fff;
		border-radius: 16rpx;
		padding: 24rpx;
		margin-top: 24rpx;
		box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);
		.drug-img{
			width: 160rpx;
			height: 160rpx;
			border-radius: 12rpx;
			margin-right: 24rpx;
			flex-shrink: 0;
		}
		.drug-info{
			flex: 1;
			line-height: 1.6;
			.name{
				font-size: 32rpx;
				font-weight: 600;
				color: #333;
			}
			.factory{
				font-size: 26rpx;
				color: #666;
				margin: 4rpx 0;
			}
			.box1{
				display: flex;
				justify-content: space-between;
				.right{
					margin-right: 30rpx;
					font-size: 36rpx;
				}
			}
			.price{
				font-size: 30rpx;
				color: #ff4d4f;
				margin: 6rpx 0;
			}
			.effect,
			.usage,
			.taboo{
				font-size: 24rpx;
				color: #555;
				margin-top: 6rpx;
			}
		}
	}
	
}



</style>
