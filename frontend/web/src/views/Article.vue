<template>
  <nav2 />
  <div class="article-page">
    <!-- Sidebar for article types -->
    <aside class="sidebar">
      <ul class="type-list">
        <!-- 全部按钮 -->
        <li
          :class="{ active: selectedTypeId === null }"
          @click="selectType(null)">
          全部
        </li>
        <!-- 各类型按钮 -->
        <li
          v-for="type in articleTypes"
          :key="type.typeId"
          :class="{ active: selectedTypeId === type.typeId }"
          @click="selectType(type.typeId)">{{ type.typeName }}</li>
      </ul>
    </aside>

    <!-- Main content area with grid layout -->
    <main class="content-area">
      <van-row gutter="20">
        <van-col
          v-for="article in articles"
          :key="article.id"
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6">
          <div class="card-wrapper">
            <van-card class="article-card" :hoverable="true">
              <!-- Title slot: show type tag, title and author -->
              <template #title>
                <div class="card-header">
                  <van-tag type="info" class="type-tag">
                    {{ typeMap[article.typeId] || '未知类型' }}
                  </van-tag>
                  <div class="title-block">
                    <h3 class="title">{{ article.title }}</h3>
                    <p class="author">作者：{{ article.author }}</p>
                  </div>
                </div>
              </template>

              <!-- Description preview -->
              <template #desc>
                <p class="desc">
                  {{ article.content.replace(/<[^>]+>/g, '').slice(0, 100) + '...' }}
                </p>
              </template>

              <!-- Footer with timestamps and view button -->
              <template #footer>
                <div class="footer">
                  <span class="time">创建：{{ article.createTime }}</span>
                  <van-button size="small" type="primary" @click="viewDetail(article)">查看</van-button>
                </div>
              </template>
            </van-card>
          </div>
        </van-col>
      </van-row>

      <!-- Pagination -->
      <van-pagination
        v-model:current-page="currentPage"
        :total-items="totalArticles"
        :items-per-page="pageSize"
        @change="onPageChange" />

      <!-- Article detail dialog with close icon -->
      <van-dialog
        v-model:show="showDetail"
        width="80%"
        class="detail-dialog"
        :show-cancel-button="false"
        :show-confirm-button="false"
        close-on-click-overlay>
        <!-- Custom title with close button -->
        <template #title>
          <div class="dialog-title">
            <span>{{ detail.title }}</span>
            <van-icon name="cross" class="dialog-close" @click="closeDetail" />
          </div>
        </template>
        
        <!-- Detail content -->
        <div class="detail-content">
          <p><strong>作者：</strong>{{ detail.author }} <strong>类型：</strong>{{ typeMap[detail.typeId] || '未知类型' }}</p>
          <p><strong>创建：</strong>{{ detail.createTime }} <strong>更新：</strong>{{ detail.updateTime }}</p>
          <div v-html="detail.content" class="detail-body"></div>
        </div>
      </van-dialog>
    </main>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import nav2 from '@/components/nav2.vue';
import { articlesPage, getArticleInfo } from '@/api/admin/articles.js';
import { articleTypesPage } from '@/api/admin/articleType.js';
import { ElMessage } from 'element-plus';
import { Icon } from 'vant';

// State
const articleTypes = ref([]);
const articles = ref([]);
const typeMap = ref({});
const selectedTypeId = ref(null);
const currentPage = ref(1);
const pageSize = ref(8);
const totalArticles = ref(0);
const showDetail = ref(false);
const detail = ref({});

// Fetch types and build map
async function loadTypes() {
  try {
    const res = await articleTypesPage({ pageNum: 1, pageSize: 100 });
    if (res.code === 200) {
      articleTypes.value = res.data.data;
      typeMap.value = articleTypes.value.reduce((map, t) => {
        map[t.typeId] = t.typeName;
        return map;
      }, {});
    }
  } catch (e) {
    ElMessage.error('类型加载失败');
  }
}

// Fetch articles with optional type filter
async function loadArticles() {
  try {
    const params = { pageNum: currentPage.value, pageSize: pageSize.value };
    if (selectedTypeId.value !== null) {
      params.typeId = selectedTypeId.value;
    }
    const res = await articlesPage(params);
    if (res.code === 200) {
      articles.value = res.data.data;
      totalArticles.value = res.data.total;
    }
  } catch (e) {
    ElMessage.error('文章加载失败');
  }
}

// Fetch article detail by ID
async function fetchArticleDetail(articleId) {
  try {
    const res = await getArticleInfo(articleId);
    if (res.code === 200) {
      return res.data;
    }
  } catch (e) {
    ElMessage.error('文章详情加载失败');
  }
  return null;
}

// Select type and reset paging
function selectType(typeId) {
  selectedTypeId.value = typeId;
  currentPage.value = 1;
}

// Watch filters and paging for reload
watch([selectedTypeId, currentPage], loadArticles);

// Page change handler
function onPageChange(page) {
  currentPage.value = page;
}

// Show detail dialog
async function viewDetail(article) {
  const articleDetail = await fetchArticleDetail(article.id);
  if (articleDetail) {
    detail.value = articleDetail;
    showDetail.value = true;
  }
}

// Close detail dialog
function closeDetail() {
  showDetail.value = false;
}

// Initial load
onMounted(async () => {
  await loadTypes();
  await loadArticles();
});
</script>

<style scoped>
.article-page {
  display: flex;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.sidebar {
  width: 120px;
  background: #fff;
  padding: 10px;
  border-right: 1px solid #e0e6ed;
}

.type-list {
  list-style: none;
  padding: 0;
}

.type-list li {
  padding: 8px;
  margin-bottom: 8px;
  cursor: pointer;
  border-radius: 4px;
  text-align: center;
  transition: background 0.3s;
}

.type-list li.active,
.type-list li:hover {
  background: #409eff;
  color: #fff;
}

.content-area {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.card-wrapper {
  display: flex;
  justify-content: center;
}

.article-card {
  max-width: 80%;
  width: 100%;
  margin: 10px 0;
  transition: transform 0.3s;
}

.article-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

.card-header {
  display: flex;
  align-items: center;
  padding: 8px;
}

.type-tag {
  font-size: 12px;
  margin-right: 8px;
  background-color: #409eff !important;
  color: #ffffff !important;
  padding: 0 6px;
  border-radius: 4px;
}

.title-block {
  flex: 1;
}

.title {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

.author {
  font-size: 12px;
  color: #909399;
  margin: 4px 0 0;
}

.desc {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin: 10px 0;
}

.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  font-size: 12px;
  color: #909399;
}

/* Detail dialog styles */
.detail-dialog .van-dialog__body {
  padding: 0;
}

.dialog-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #f5f7fa;
}

.dialog-close {
  font-size: 20px;
  cursor: pointer;
  color: #606266;
}

.detail-content {
  padding: 20px;
  overflow-y: auto;
  max-height: 80vh; /* 设置最大高度并添加滚动条 */
}

.detail-content p {
  margin: 8px 0;
  color: #606266;
  line-height: 1.6;
}

.detail-body {
  margin-top: 15px;
  line-height: 1.8;
}
</style>