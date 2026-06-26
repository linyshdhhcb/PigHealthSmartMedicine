<template>
    <view class="article-detail">
        <view class="article-content" v-if="article">
            <view class="article-header">
                <text class="article-title">{{ article.title }}</text>
                <view class="article-meta">
                    <view class="meta-left">
                        <view class="author-avatar">
                            <uni-icons type="person" size="16" color="#43a047" />
                        </view>
                        <text class="article-author">{{ article.author || '未知作者' }}</text>
                    </view>
                    <text class="article-date">{{ formatDate(article.createTime) }}</text>
                </view>
            </view>

            <view class="divider"></view>

            <view class="article-body">
                <rich-text :nodes="article.content"></rich-text>
            </view>
        </view>

        <view class="loading-state" v-if="loading">
            <view class="loading-spinner"></view>
            <text class="loading-text">加载中...</text>
        </view>

        <view class="error-state" v-if="error">
            <uni-icons type="closeempty" size="60" color="#ddd" />
            <text class="error-text">{{ error }}</text>
            <button class="retry-btn" @click="getArticleDetail">重试</button>
        </view>
    </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getArticleById } from '../../api/articles.js'

const articleId = ref('')
const article   = ref(null)
const loading   = ref(true)
const error     = ref('')

onLoad((options) => {
  if (options?.id) {
    articleId.value = options.id
    getArticleDetail()
  } else {
    error.value   = '文章ID不存在'
    loading.value = false
  }
})

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

<style lang="scss" scoped>
.article-detail {
    min-height: 100vh;
    background: #f7f8fa;
}

.article-content {
    background: #fff;
    margin: 24rpx;
    border-radius: 24rpx;
    padding: 36rpx 32rpx;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.article-header {
    margin-bottom: 24rpx;
}

.article-title {
    font-size: 40rpx;
    font-weight: 700;
    color: #222;
    line-height: 1.4;
    margin-bottom: 20rpx;
    display: block;
}

.article-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.meta-left {
    display: flex;
    align-items: center;
}

.author-avatar {
    width: 48rpx;
    height: 48rpx;
    border-radius: 50%;
    background: rgba(67, 160, 71, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 12rpx;
}

.article-author {
    font-size: 26rpx;
    color: #666;
}

.article-date {
    font-size: 24rpx;
    color: #bbb;
}

.divider {
    height: 1rpx;
    background: #f0f0f0;
    margin-bottom: 28rpx;
}

.article-body {
    margin-bottom: 40rpx;
}

.article-body ::v-deep p {
    margin-bottom: 20rpx;
    line-height: 1.8;
    font-size: 30rpx;
    color: #444;
}

.article-body ::v-deep a {
    color: #43a047;
    text-decoration: none;
}

.article-body ::v-deep img {
    max-width: 100%;
    height: auto;
    margin: 20rpx 0;
    border-radius: 12rpx;
}

.loading-state,
.error-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 200rpx 0;
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

.loading-text,
.error-text {
    font-size: 28rpx;
    color: #999;
    margin-bottom: 20rpx;
}

.retry-btn {
    padding: 16rpx 56rpx;
    background: linear-gradient(135deg, #2e7d32, #43a047);
    color: white;
    border-radius: 40rpx;
    font-size: 28rpx;
}
</style>
