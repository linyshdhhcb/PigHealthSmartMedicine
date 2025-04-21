<!-- src/views/ArticleType.vue -->
<template>
  <div style="height: 100vh; padding: 20px; display: flex;">
    <div class="sidebar">
      <h2>文章类型</h2>
      <ul>
        <li
          v-for="articleType in articleTypes"
          :key="articleType.typeId"
          :class="{ active: selectedTypeId === articleType.typeId }"
          @click="selectArticleType(articleType.typeId)"
        >
          {{ articleType.typeName }}
        </li>
      </ul>
    </div>
    <div class="content">
      <Article :typeId="selectedTypeId" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
// import { getArticleTypesPage } from '@/api/ArticleType';
// import Article from './Article.vue';
// import Article from '@/api/Article.js';

const articleTypes = ref([]);
const selectedTypeId = ref(null);

const fetchArticleTypes = async () => {
  try {
    const response = await getArticleTypesPage({
      pageNum: 0,
      pageSize: 100,
      sortField: '',
      sortOrder: '',
      typeName: ''
    });
    if (response.data.code === 0) {
      articleTypes.value = response.data.data.data;
      if (articleTypes.value.length > 0) {
        selectedTypeId.value = articleTypes.value[0].typeId;
      }
    } else {
      alert('获取文章类型失败: ' + response.data.message);
    }
  } catch (error) {
    console.error('获取文章类型失败:', error);
    alert('获取文章类型失败，请重试');
  }
};

const selectArticleType = (typeId) => {
  selectedTypeId.value = typeId;
};

onMounted(() => {
  fetchArticleTypes();
});
</script>

<style scoped>
.sidebar {
  width: 250px;
  border-right: 1px solid #ccc;
  padding-right: 20px;
}

.sidebar h2 {
  margin-bottom: 20px;
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
}

.sidebar li {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

.sidebar li.active {
  background-color: #f0f0f0;
}

.content {
  flex-grow: 1;
  padding-left: 20px;
}
</style>