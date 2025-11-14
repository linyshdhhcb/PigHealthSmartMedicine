<template>
    <view class="article-detail">
        <!-- 文章内容 -->
        <view class="article-content" v-if="article">
            <view class="article-header">
                <text class="article-title">{{ article.title }}</text>
                <view class="article-meta">
                    <text class="article-author">{{ article.author || '未知作者' }}</text>
                    <text class="article-date">{{ formatDate(article.createTime) }}</text>
                </view>
            </view>

            <view class="article-body">
                <!-- 使用rich-text组件渲染HTML内容 -->
                <rich-text :nodes="article.content"></rich-text>
            </view>
        </view>

        <!-- 加载状态 -->
        <view class="loading-state" v-if="loading">
            <text class="loading-text">加载中...</text>
        </view>

        <!-- 错误状态 -->
        <view class="error-state" v-if="error">
            <text class="error-text">{{ error }}</text>
            <button class="retry-btn" @click="getArticleDetail">重试</button>
        </view>
    </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getArticleById } from '../../api/articles.js'

/* 响应式数据 */
const articleId = ref('')
const article   = ref(null)
const loading   = ref(true)
const error     = ref('')

/* 生命周期 */
onLoad((options) => {
  if (options?.id) {
    articleId.value = options.id
    getArticleDetail()
  } else {
    error.value   = '文章ID不存在'
    loading.value = false
  }
})

/* 方法 */
async function getArticleDetail() {
  loading.value = true
  error.value   = ''

  try {
    const res = await getArticleById(articleId.value)
    if (res?.data) {
      article.value = Array.isArray(res.data) ? res.data[0] : res.data
    } else {
      error.value = '获取文章详情失败'
    }
  } catch {
    error.value = '获取文章详情失败'
  } finally {
    loading.value = false
  }
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
    d.getDate()
  ).padStart(2, '0')}`
}
</script>
<style>
.article-detail {
    padding: 30rpx;
    background-color: #fff;
    min-height: 100vh;
}

.article-header {
    margin-bottom: 40rpx;
}

.article-title {
    font-size: 40rpx;
    font-weight: bold;
    color: #333;
    line-height: 1.4;
    margin-bottom: 20rpx;
    display: block;
}

.article-meta {
    display: flex;
    justify-content: space-between;
    font-size: 24rpx;
    color: #999;
}

.article-body {
    margin-bottom: 60rpx;
}

/* 富文本内容样式 */
.article-body>>>p {
    margin-bottom: 20rpx;
    line-height: 1.8;
    font-size: 30rpx;
    color: #333;
}

.article-body>>>a {
    color: #07c160;
    text-decoration: none;
}

.article-body>>>img {
    max-width: 100%;
    height: auto;
    margin: 20rpx 0;
}

.loading-state,
.error-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 100rpx 0;
}

.loading-text,
.error-text {
    font-size: 28rpx;
    color: #999;
    margin-bottom: 30rpx;
}

.retry-btn {
    width: 200rpx;
    height: 70rpx;
    background-color: #07c160;
    color: white;
    border-radius: 35rpx;
    font-size: 28rpx;
    display: flex;
    align-items: center;
    justify-content: center;
}
</style>