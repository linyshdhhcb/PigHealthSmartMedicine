<template>
      <nav1 />
      <nav2 />
  <div class="news-container">

    <div class="news-grid">
      <div v-for="news in newsList" :key="news.id" class="news-card">
        <h2 class="news-title" v-html="news.title"></h2>
        <div class="news-meta">
          <span class="news-author">{{ news.author }}</span>
          <span class="news-source">{{ news.source }}</span>
          <span class="news-publish-time">{{ news.publishTime }}</span>
        </div>
        <p class="news-summary" v-html="news.summary"></p>
        <div class="action-buttons">
          <a :href="news.url" class="news-link" target="_blank">阅读全文</a>
          <el-button type="success" @click="openNewsDetail(news.id)">查看详情</el-button>
        </div>
      </div>
    </div>
    <div class="pagination">
      <el-pagination
        v-if="pagination.total > 0"
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100, 200]"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>

    <!-- 新闻详情模态框 -->
    <div v-if="showDetail" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeDetail">&times;</span>
        <h2 v-html="detailNews.title"></h2>
        <div class="detail-meta">
          <span class="detail-author">{{ detailNews.author }}</span>
          <span class="detail-source">{{ detailNews.source }}</span>
          <span class="detail-publish-time">{{ detailNews.publishTime }}</span>
        </div>
        <div class="detail-content" v-html="detailNews.content"></div>
      </div>
    </div>
  </div>
  <buttom2 />
</template>

<script setup>
import nav1 from '@/components/nav1.vue';
import nav2 from '@/components/nav2.vue';
import buttom2 from '@/components/buttom2.vue';
import { ref, onMounted } from 'vue';
import { newsArticlesPage, newsArticlesGetInfo } from '@/api/admin/newsArticles.js';

const newsList = ref([]);
const showDetail = ref(false);
const detailNews = ref({});
const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
});

onMounted(async () => {
  fetchNewsList();
});

const fetchNewsList = async () => {
  try {
    const params = {
      pageNum: pagination.value.pageNum,
      pageSize: pagination.value.pageSize,
    };
    const response = await newsArticlesPage(params);
    if (response.code === 200) {
      newsList.value = response.data.data;
      pagination.value.total = response.data.total;
    }
  } catch (error) {
    console.error('获取新闻列表失败:', error);
  }
};

const openNewsDetail = async (id) => {
  try {
    const response = await newsArticlesGetInfo(id);
    if (response.code === 200) {
      detailNews.value = response.data;
      showDetail.value = true;
    }
  } catch (error) {
    console.error('获取新闻详情失败:', error);
  }
};

const closeDetail = () => {
  showDetail.value = false;
};

const handlePageChange = (currentPage) => {
  pagination.value.pageNum = currentPage;
  fetchNewsList();
};

const handleSizeChange = (pageSize) => {
  pagination.value.pageSize = pageSize;
  fetchNewsList();
};
</script>

<style scoped>
.news-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.news-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.news-card:hover {
  transform: translateY(-5px);
}

.news-title {
  font-size: 18px;
  margin-bottom: 10px;
  color: #333;
}

.news-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 12px;
  color: #777;
}

.news-summary {
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 15px;
  color: #555;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}

.news-link {
  display: inline-block;
  color: #1890ff;
  text-decoration: none;
  font-size: 14px;
  padding: 8px 15px;
  background-color: #f0f7ff;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.news-link:hover {
  background-color: #e0f0ff;
  text-decoration: none;
}

.el-button {
  padding: 8px 15px;
  border-radius: 4px;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

/* 模态框样式 */
.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
}

.modal-content {
  background-color: #fefefe;
  margin: auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
  border-radius: 8px;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
}

.detail-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 12px;
  color: #777;
}

.detail-content {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
}
</style>