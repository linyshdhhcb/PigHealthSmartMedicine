<template>
  <el-card class="article-detail-card" v-loading="loading">
    <div v-if="article">
      <!-- 标题部分 -->
      <div class="header">
        <h1 class="title">{{ article.title }}</h1>
        <div class="meta">
          <span class="author">作者：{{ article.author || '佚名' }}</span>
          <el-tag type="success" class="type-tag">{{ typeName }}</el-tag>
          <span class="time">{{ formatTime(article.createTime) }}</span>
        </div>
      </div>

      <el-divider />

      <!-- 内容部分 -->
      <div class="content" v-html="article.content"></div>

      <el-divider />

      <!-- 底部操作 -->
      <div class="footer">
        <el-button type="primary" @click="$emit('cancel')">
          <el-icon><Back /></el-icon>
          返回
        </el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getArticleInfo, articleTypesPage } from '@/api/articles.js';
import { ElMessage } from 'element-plus';

const props = defineProps({
  params: {
    type: Object,
    default: () => ({ id: null }),
  },
});

const article = ref(null);
const loading = ref(true);
const typeName = ref('');

// 获取文章详情
const fetchArticle = async () => {
  try {
    const res = await getArticleInfo(props.params.id);
    if (res.code === 200 && res.data) {
      article.value = res.data;
      await fetchTypeName(res.data.typeId);
    } else {
      ElMessage.error(res.message || '获取文章详情失败');
    }
  } catch (error) {
    console.error('加载文章详情失败:', error);
    ElMessage.error('加载失败，请稍后再试');
  } finally {
    loading.value = false;
  }
};

// 获取类型名称
const fetchTypeName = async (typeId) => {
  try {
    const res = await articleTypesPage({ pageNum: 1, pageSize: 100 });
    if (res.code === 200 && res.data?.data) {
      const type = res.data.data.find(t => t.typeId === typeId);
      typeName.value = type ? type.typeName : '未知类型';
    }
  } catch (error) {
    console.error('获取类型失败', error);
  }
};

// 时间格式化
const formatTime = (time) => {
  if (!time) return '';
  const d = new Date(time);
  return d.toLocaleDateString() + ' ' + d.toLocaleTimeString();
};

// 生命周期：加载数据
onMounted(() => {
  fetchArticle();
});
</script>

<style scoped>
.article-detail-card {
  max-width: 900px;
  margin: 20px auto;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  background: #fff;
  animation: fadeIn 0.4s ease;
}

/* 动画 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(8px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 标题 */
.title {
  font-size: 28px;
  font-weight: bold;
  color: #222;
  margin-bottom: 10px;
  text-align: center;
  line-height: 1.4;
}

/* 作者与类型信息 */
.meta {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 25px;
  color: #666;
  font-size: 14px;
  flex-wrap: wrap;
}

.type-tag {
  font-size: 12px;
  border-radius: 6px;
  padding: 2px 8px;
}

/* 时间 */
.time {
  color: #999;
}

/* 内容部分 */
.content {
  margin-top: 30px;
  line-height: 1.9;
  color: #333;
  font-size: 16px;
  word-break: break-word;
}

.content img {
  max-width: 100%;
  height: auto;
  margin: 15px 0;
  border-radius: 8px;
}

.content p {
  margin-bottom: 16px;
  text-indent: 2em;
}

.content h1, .content h2, .content h3 {
  margin-top: 24px;
  color: #222;
  border-left: 4px solid #409EFF;
  padding-left: 8px;
}

/* 底部 */
.footer {
  margin-top: 40px;
  text-align: center;
}
</style>
