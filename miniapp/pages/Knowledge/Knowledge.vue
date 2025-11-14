<template>
	<scroll-view scroll-y="true" class="scroll">
		<view class="container">
			<!-- 顶部搜索栏 -->
			<view class="search-box">
				<view class="search-input-box">
					<!-- <text class="iconfont icon-search"></text> -->
					<!-- <input class="search-input" type="text" v-model="searchKeyword" placeholder="搜索文章"
						@confirm="handleSearch" /> -->
					<uni-search-bar bgColor="#f5f5f5" class="search-input" placeholder="搜索文章..." v-model="searchKeyword" @input="handleSearch"  ></uni-search-bar>
				</view>
			</view>
			
			<scroll-view scroll-x class="type-scroll">
				<view 
				class="type-tag" 
				:key="t.typeId" 
				v-for="t in articleTypeList"
				:class="{active:currentType === t.typeId}" 
				@tap="selectType(t.typeId)">{{t.typeName}}</view>
			</scroll-view>
		
			<!-- 文章列表 -->
			<scroll-view class="article-list" scroll-y @scrolltolower="loadMoreArticles" refresher-enabled
				:refresher-triggered="isRefreshing" @refresherrefresh="onRefresh">
				<!-- 空状态 -->
				<!-- <view class="empty-state" v-if="articles.length === 0 && !loading">
					<image class="empty-image" src="/static/empty.png"></image>
					<text class="empty-text">暂无文章</text>
				</view> -->
		
				<!-- 文章列表项 -->
				<view class="article-item" v-for="(article, index) in articles" :key="article.id"
					@click="goToDetail(article.id)">
					<view class="article-info">
						<view class="article-title">
							<view class="left">{{ article.title }}</view>
							<view class="right">卡德加</view>
						</view>
						<view class="article-desc">{{ formatContent(article.content) }}</view>
						<view class="article-meta">
							<!-- <view class="article-author image-father">
							  <image class="image" src="/static/images/pageviews.png" mode="aspectFill" />
							  {{ article.pageviews || 0 }}
							</view> -->
							<text class="article-date">{{ formatDate(article.createTime) }}</text>
						</view>
					</view>
				</view>
		
				<!-- 加载状态 -->
				<view class="loading-state" v-if="loading">
					<text class="loading-text">加载中...</text>
				</view>
		
				<!-- 加载完成 -->
				<view class="loading-state" v-if="!hasMore && articles.length > 0">
					<text class="loading-text">没有更多了</text>
				</view>
				
				<!-- #ifdef H5 -->
					<TabBar />
				<!-- #endif -->
			</scroll-view>
		</view>
	</scroll-view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { articlesPage,medicinePage,illnessPage,illnessMedicinePage,illnessKindPage,articleTypesPage,pageviewPage } from '../../api/articles.js'
import { checkLogin } from '../../api/user.js'
import TabBar from '@/components/TabBar.vue'

/* 响应式数据 */
const searchKeyword   = ref('')
const articles        = ref([])
const pageNum         = ref(1)
const pageSize        = 10
const total           = ref(0)
const hasMore         = ref(true)
const loading         = ref(false)
const isRefreshing    = ref(false)
const articleTypeList = ref([])
const currentType = ref(0)
const  pageviewList = ref([])

// 1. 一进入页面就先检查登录
onLoad(() => checkLoginStatus())

// 2. 登录检查函数
async function checkLoginStatus() {
  const token = uni.getStorageSync('token')
  if (!token) return redirectToLogin()   // 无 token 直接踢走
  const res = await checkLogin()         // 有 token 再校验一次后端
  res.data ? getArticles() : redirectToLogin()
}
 


function redirectToLogin() {
  uni.reLaunch({ url: '/pages/login/login' })
}


/* // 3. 拿文章列表（同时支持“下拉刷新”和“上拉加载更多”）
 async function getArticles(isRefresh = false) {
  if (isRefresh) {              // 下拉刷新时归零
    pageNum.value = 1
    articles.value = []
  }
  if (!hasMore.value && !isRefresh) return   // 没有更多数据就停手

  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize,
      sortField: 'createTime',
      sortOrder: 'desc',
      ...(searchKeyword.value && { title: searchKeyword.value }), // 搜索关键字
      ...(currentType.value && { typeId: currentType.value })    // 分类 id
    }
    const res = await articlesPage(params)
    const list = res.data.data || []          // 兼容后端两层/三层结构
    articles.value = isRefresh ? list : [...articles.value, ...list]
    hasMore.value = list.length === pageSize  // 本次拿满说明可能还有
    pageNum.value++
  } finally {
    loading.value = false
    isRefreshing.value = false
  }
} */


/* //测试药品分页数据能否拿到
async function getmedicine(){
	try{
		const CSparams = {
			pageNum:1,
			pageSize:100,
			sortField: 'createTime',
			sortOrder: 'desc',
		}
		const res = await medicinePage(CSparams)
		console.log('getmedicine',res);
		
		
	}catch {
    uni.showToast({ title: '获取文章列表失败', icon: 'none' })
  } finally {
    loading.value      = false
    isRefreshing.value = false
  }
}

//测试药品分页数据能否拿到
async function getillnessPage(){
	try{
		const illparams = {
			pageNum:1,
			pageSize:100,
			sortField: 'createTime',
			sortOrder: 'desc',
		}
		const res = await illnessPage(illparams)
		console.log('疾病分页接口数据',res);
		
		
	}catch {
    uni.showToast({ title: '获取文章列表失败', icon: 'none' })
  } finally {
    loading.value      = false
    isRefreshing.value = false
  }
}

//测试药品-疾病分页数据能否拿到
async function getillnessMedicinePage(){
	try{
		const illparams = {
			pageNum:1,
			pageSize:100,
			sortField: 'createTime',
			sortOrder: 'desc',
		}
		const res = await illnessMedicinePage(illparams)
		console.log('疾病-药物分页接口数据',res);
		
		
	}catch {
    uni.showToast({ title: '获取文章列表失败', icon: 'none' })
  } finally {
    loading.value      = false
    isRefreshing.value = false
  }
}

//测试疾病种类分页数据能否拿到
async function getillnessKindPage(){
	try{
		const illparams = {
			pageNum:1,
			pageSize:100,
			sortField: 'createTime',
			sortOrder: 'desc',
		}
		const res = await illnessKindPage(illparams)
		console.log('疾病种类分页接口数据',res);
		
		
	}catch {
    uni.showToast({ title: '获取文章列表失败', icon: 'none' })
  } finally {
    loading.value      = false
    isRefreshing.value = false
  }
} */



//测试文章类型分页数据能否拿到
async function getarticleTypesPage(){
	try{
		const params = {
			pageNum:1,
			pageSize:100,
			sortField: 'createTime',
			sortOrder: 'desc',
		}
		const res = await articleTypesPage(params)
		
		articleTypeList.value = [
			{typeId:0,typeName:'全部'},
			...(res.data.data || [])
		]
		console.log('文章类型分页数据articleTypeList.value',articleTypeList.value);
		
		
	}catch {
    uni.showToast({ title: '获取文章类型分页数据失败', icon: 'none' })
  } finally {

  }
}

// //测试文章浏览量分页数据能否拿到
// async function getpageviewPage(){
// 	try{
// 		const params = {
// 			pageNum:1,
// 			pageSize:100,
// 			sortField: 'createTime',
// 			sortOrder: 'desc',
// 		}
// 		const res = await pageviewPage(params)
// 		 pageviewList.value = res.data.data
		
// 		console.log('文章浏览量分页数据',pageviewList.value);
		
		
// 	}catch {
//     uni.showToast({ title: '获取文章浏览量分页数据', icon: 'none' })
//   } finally {

//   }
// }

/* 1. 新增一个 map 缓存浏览量 */
const pvMap = ref(new Map())

/* 2. 把浏览量列表转成 map，方便查询 */
function buildPvMap(list) {
  const m = new Map()
  list.forEach(item => m.set(item.id, item.pageviews))
  pvMap.value = m
}

/* 3. 获取文章后，把浏览量写回文章 */
async function getArticles(isRefresh = false) {
  if (isRefresh) {
    pageNum.value = 1
    articles.value = []
  }
  if (!hasMore.value && !isRefresh) return

  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize,
      sortField: 'createTime',
      sortOrder: 'desc',
      ...(searchKeyword.value && { title: searchKeyword.value }),
      ...(currentType.value && { typeId: currentType.value })
    }
    const res = await articlesPage(params)
    const list = res.data.data || []

    /* 关键：把浏览量写回每条文章 */
    list.forEach(art => {
      art.pageviews = pvMap.value.get(art.id) || 0
    })

    articles.value = isRefresh ? list : [...articles.value, ...list]
    hasMore.value = list.length === pageSize
    pageNum.value++
  } finally {
    loading.value = false
    isRefreshing.value = false
  }
}

/* 4. 获取浏览量后一次性转成 map，并重新渲染当前页 */
async function getpageviewPage() {
  try {
    const res = await pageviewPage({
      pageNum: 1,
      pageSize: 100,
      sortField: 'createTime',
      sortOrder: 'desc'
    })
    buildPvMap(res.data.data || [])
    /* 已有文章时，把浏览量刷一遍 */
    if (articles.value.length) {
      articles.value.forEach(art => {
        art.pageviews = pvMap.value.get(art.id) || 0
      })
    }
  } catch {
    uni.showToast({ title: '获取浏览量失败', icon: 'none' })
  }
}	
	
	
// 4. 分类切换
function selectType(typeId) {
  currentType.value = typeId
  getArticles(true)   //  true = 重新从第一页拿
}

// 5. 触底加载更多
function loadMoreArticles() {
  if (!loading.value && hasMore.value) getArticles()
}


// 6. 下拉刷新
function onRefresh() {
  isRefreshing.value = true
  getArticles(true)
}

// 7. 实时搜索（防抖可再包一层）
function handleSearch() {
  getArticles(true)
}


function goToDetail(id) {
  uni.navigateTo({ url: `/pages/article/article-detail?id=${id}` })
}
function formatContent(html) {
  if (!html) return '暂无内容'
  const txt = html.replace(/<[^>]+>/g, '')
  return txt.length > 100 ? txt.slice(0, 100) + '...' : txt
}
function formatDate(dateStr) {
  if (!dateStr) return ''
  // ⚙️ iOS 日期兼容：统一格式为 yyyy/MM/dd HH:mm:ss
  const safeStr = dateStr.replace(/-/g, '/')
  const d = new Date(safeStr)
  if (isNaN(d.getTime())) return dateStr  // 防止解析失败显示原文
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
    d.getDate()
  ).padStart(2, '0')}`
}

// getmedicine()
// getillnessPage()
// getillnessMedicinePage()
// getillnessKindPage()
getarticleTypesPage()
getpageviewPage()
</script>

<style scoped lang="scss">
	.scroll{
		height: 100vh -5vh;
	}
.container {
	display: flex;
	flex-direction: column;
	height: 100vh;
	background-color: #f8f8f8;
}

.search-box {
	padding: 20rpx;
	background-color: #fff;
	position: sticky;
	top: 0;
	z-index: 100;
}

.search-input-box {
	display: flex;
	align-items: center;
	/* background-color: #f5f5f5; */
	border-radius: 60rpx;
	padding: 0 20rpx;
	height: 70rpx;
}

.icon-search {
	font-size: 28rpx;
	color: #999;
	margin-right: 10rpx;
}

.search-input {
	border-radius: 60rpx;
	flex: 1;
	height: 70rpx;
	font-size: 28rpx;
}

.article-list {
	flex: 1;
	padding: 20rpx;
}

.article-item {
	background-color: #fff;
	border-radius: 12rpx;
	margin-bottom: 20rpx;
	padding: 30rpx;
	box-shadow: 0 2rpx 10rpx 0 rgba(0, 0, 0, 0.05);
}

.article-title {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
	margin-bottom: 16rpx;
	line-height: 1.4;
	display: flex;
	justify-content: space-between;
	.right{
		color: #999;
		font-size: 25rpx;
	}
}

.article-desc {
	font-size: 28rpx;
	color: #666;
	margin-bottom: 20rpx;
	line-height: 1.5;
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 3;
	overflow: hidden;
}

.article-meta {
	display: flex;
	justify-content: space-between;
	align-items: center;
	font-size: 24rpx;
	color: #999;
		/* 浏览量那一坨 */
		.image-father {
		  display: inline-flex;      /* 关键①：行内弹性盒子 */
		  align-items: center;       /* 关键②：交叉轴居中（垂直方向） */
		  gap: 6rpx;                 /* 图标和数字间距，随意调 */
		  font-size: 24rpx;
		  color: #999;
		  /* 图标大小 */
		  .image{
		  	width: 40rpx;
		  	height: 40rpx;
			/* 如果你还想更精细，再补一句 */
			 vertical-align: middle; 
		  }
		}

	
	
}

.empty-state {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 100rpx 0;
}

.empty-image {
	width: 200rpx;
	height: 200rpx;
	margin-bottom: 20rpx;
}

.empty-text {
	font-size: 28rpx;
	color: #999;
}

.loading-state {
	text-align: center;
	padding: 30rpx 0;
}

.loading-text {
	font-size: 24rpx;
	color: #999;
}


/* 横向滚动分类 - 标准 WXSS */
.type-scroll {
  white-space: nowrap;
  padding: 20rpx 0 10rpx;
  background: #fff;
}
.type-tag {
  display: inline-block;
  padding: 10rpx 24rpx;
  margin-left: 20rpx;
  font-size: 28rpx;
  color: #333;
  border-radius: 30rpx;
  background: #f2f2f2;
}
/* 激活状态单独写，不要嵌套 */
.type-tag.active {
  /* background: #007aff; */
  background: linear-gradient(135deg, #43a047, #81c784);
  color: #fff;
}
</style>