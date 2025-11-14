<template>
  <nav2 />
  <div class="search-container">
    <!-- 背景图片 -->
    <div class="background-container">
      <img src="@/assets/images/pig/pig4.jpg" alt="医疗工作者" class="background-image">
    </div>

    <!-- 搜索框区域 -->
    <div class="search-area">
      <div class="search-prompt">
        <p>没有找到你想要的结果吗？</p>
        <h2>搜索一下</h2>
        <p>智慧医药为您提供疾病查询，常见疾病大全，帮您全面了解疾病病因症状、检查治疗、饮食护理等信息，是您疾病查询的好帮手。</p>
      </div>

      <div class="search-bar">
        <input type="text" placeholder="搜索疾病、症状或药品..." v-model="searchQuery" @keyup.enter="handleSearch">
        <button @click="handleSearch"><van-icon name="search" /></button>
      </div>
    </div>

    <!-- 搜索栏下方的虚线 -->
    <div class="divider"></div>

    <!-- 搜索框下方信息 -->
    <div class="bottom-info">
      <!-- 搜索结果 -->
      <div v-if="searchResults.length" class="search-results">
        <h3>搜索结果：</h3>
        <ul>
          <li v-for="result in searchResults" :key="result.id" @click="handleResultClick(result)" class="search-result-item">
            {{ result.illnessName }} - <span v-if="stripHtml(result.illnessSymptom)">{{ stripHtml(result.illnessSymptom).slice(0, 60) }}</span>
          </li>
        </ul>
      </div>

      <!-- 疾病详细信息模态框 -->
      <el-dialog v-model="showIllnessDialog" title="疾病与药品详情" width="70%" class="illness-dialog">
        <el-form :model="illnessForm" label-width="120px" label-position="left">
          <el-form-item label="疾病名称">
            <el-input v-model="illnessForm.illnessName" disabled></el-input>
          </el-form-item>

          <el-form-item label="疾病概述（病因/包含原因）">
            <!-- 富文本渲染 -->
            <div class="rich-text" v-if="illnessForm.includeReason" v-html="illnessForm.includeReason"></div>
            <div v-else class="no-data">无</div>
          </el-form-item>

          <el-form-item label="疾病症状">
            <div class="rich-text" v-if="illnessForm.illnessSymptom" v-html="illnessForm.illnessSymptom"></div>
            <div v-else class="no-data">无</div>
          </el-form-item>

          <el-form-item label="特殊症状">
            <div class="rich-text" v-if="illnessForm.specialSymptom" v-html="illnessForm.specialSymptom"></div>
            <div v-else class="no-data">无</div>
          </el-form-item>

          <el-form-item label="对症药品">
            <div>
              <span v-for="(medicine, index) in illnessForm.medicines" :key="medicine.id" class="medicine-link" @click="handleMedicineClick(medicine.id)">
                {{ medicine.name }}
                <span v-if="index < illnessForm.medicines.length - 1">, </span>
              </span>
            </div>
          </el-form-item>

          <el-form-item label="药品详情">
  <div
    v-for="medicine in illnessForm.medicines"
    :key="medicine.id"
    class="medicine-detail-card"
  >
    <!-- 药品图片展示 -->
    <div class="medicine-header">
      <img
        :src="medicine.imgPath || 'https://image.jianke.com/upload/prodimage/201605wm/2016519151858698.jpg'"
        alt="药品图片"
        class="medicine-img"
        loading="lazy"
      />
      <div class="medicine-info">
        <h4>{{ medicine.name }}</h4>
        <p class="medicine-brand">{{ medicine.medicineBrand || '品牌未知' }}</p>
      </div>
    </div>

    <!-- 药品详细内容 -->
    <div class="medicine-body">
      <div class="medicine-item">
        <span class="label">作用：</span>
        <span>{{ medicine.keyword || '无' }}</span>
      </div>
      <div class="medicine-item">
        <span class="label">功效：</span>
        <span>{{ medicine.medicineEffect || '暂无说明' }}</span>
      </div>
      <div class="medicine-item">
        <span class="label">用法用量：</span>
        <span>{{ medicine.usAge || '暂无信息' }}</span>
      </div>
      <div class="medicine-item">
        <span class="label">禁忌：</span>
        <span>{{ medicine.taboo || '暂无信息' }}</span>
      </div>
      <div class="medicine-item">
        <span class="label">相互作用：</span>
        <span>{{ medicine.interaction || '暂无信息' }}</span>
      </div>
      <div class="medicine-item">
        <span class="label">价格：</span>
        <span class="price">￥{{ medicine.medicinePrice?.toFixed(2) || '0.00' }}</span>
      </div>
    </div>
  </div>
</el-form-item>

        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="showIllnessDialog = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 分类和搜索历史 -->
      <div class="categories-history">
        <div class="categories-container">
          <div class="categories-title">分类：</div>
          <div class="categories-list">
            <div class="categories-row" v-for="(row, index) in categoryRows" :key="index">
              <a href="javascript:;" v-for="category in row" :key="category.id" @click.prevent="handleCategoryClick(category)">
                {{ category.name }}
              </a>
            </div>
          </div>
        </div>
        <div class="history-container">
          <div class="history-title">搜索历史：</div>
          
          <div class="history-list">
            <a href="javascript:;" v-for="history in searchHistory" :key="history" @click.prevent="handleHistoryClick(history)">{{ history }}</a>
          </div>
          
          <span class="clear-history" v-if="searchHistory" @click="clearHistory">清空</span>
        </div>
      </div>

      <!-- 回到顶部按钮 -->
      <div class="back-to-top" @click="scrollToTop">
        <img src="@/assets/images/goTop.png" alt="回到顶部" />
      </div>
    </div>

    <buttom2 />
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { useRouter } from 'vue-router';
import { illnessKindPage, illnessPage, illnessMedicinePage, medicinePage, getillnessInfo } from '@/api/admin/FindIllness';
import { ElMessage } from 'element-plus';
import nav2 from '@/components/nav2.vue';
import buttom2 from '@/components/buttom2.vue';
import { closeDialog } from 'vant';

const router = useRouter();

// --- 搜索历史：从 localStorage 读取，若无则空数组 ---
const searchHistory = ref(JSON.parse(localStorage.getItem('searchHistory') || '[]'));

// --- 保存搜索历史到 localStorage ---
const saveSearchHistory = () => {
  // 限制最多 10 条
  if (searchHistory.value.length > 10) {
    searchHistory.value = searchHistory.value.slice(0, 10);
  }
  localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value));
};

// --- 处理搜索 ---
const handleSearch = () => {
  if (searchQuery.value && searchQuery.value.trim() !== '') {
    const q = searchQuery.value.trim().toLowerCase();

    // 模糊匹配
    searchResults.value = illnessPageData.value.filter(illness =>
      (illness.illnessName && illness.illnessName.toLowerCase().includes(q)) ||
      (illness.illnessSymptom && stripHtml(illness.illnessSymptom).toLowerCase().includes(q)) ||
      (illness.specialSymptom && stripHtml(illness.specialSymptom).toLowerCase().includes(q))
    );

    // --- 保存真实搜索记录 ---
    const existingIndex = searchHistory.value.findIndex(h => h === searchQuery.value);
    if (existingIndex !== -1) {
      // 已存在的搜索项提前到最前
      searchHistory.value.splice(existingIndex, 1);
    }
    searchHistory.value.unshift(searchQuery.value);
    saveSearchHistory();

  } else {
    ElMessage.info('请输入搜索关键词');
  }
};

const searchQuery = ref('');
const categories = ref([]); // 存储为 { id, name }
const illnessPageData = ref([]); // 疾病分页数据 (数组 of objects)
const searchResults = ref([]);
const illnessMedicinePageData = ref([]);
const medicinePageData = ref([]);
const showIllnessDialog = ref(false);
const loading = ref(false);
const illnessForm = reactive({
  id: null,
  illnessName: '',
  includeReason: '',
  illnessSymptom: '',
  specialSymptom: '',
  medicines: [],
});


//清空搜索历史：
const clearHistory = () => {
  searchHistory.value = [];
  localStorage.removeItem('searchHistory');
  ElMessage.success('已清空搜索历史');
};


// 分类按行分割显示（4 行）
const categoryRows = computed(() => {
  const rows = [];
  const total = categories.value.length;
  if (total === 0) {
    return [[], [], [], []];
  }
  const chunkSize = Math.ceil(total / 4);
  for (let i = 0; i < 4; i++) {
    rows.push(categories.value.slice(i * chunkSize, (i + 1) * chunkSize));
  }
  return rows;
});

// --- 初始化：拉取分类、疾病、疾病-药物、药物分页 ---
illnessKindPage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      // 保存为对象数组，保留 id 与 name
      categories.value = (res.data.data || []).map(item => ({ id: item.id, name: item.name }));
    } else {
      ElMessage.error(res.message || '获取分类失败');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('获取分类失败，请稍后再试');
  });

illnessPage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      illnessPageData.value = res.data.data || [];
    } else {
      ElMessage.error(res.message || '请求疾病列表失败，请稍后再试');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('请求疾病列表失败，请稍后再试');
  });

illnessMedicinePage({ pageNum: 1, pageSize: 200 })
  .then(res => {
    if (res.code === 200) {
      illnessMedicinePageData.value = res.data.data || [];
    } else {
      ElMessage.error(res.message || '获取疾病-药品映射失败');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('获取疾病-药品映射失败，请稍后再试');
  });

medicinePage({ pageNum: 1, pageSize: 200 })
  .then(res => {
    if (res.code === 200) {
      medicinePageData.value = res.data.data || [];
    } else {
      ElMessage.error(res.message || '获取药品数据失败');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('获取药品数据失败，请稍后再试');
  });

// --- 工具函数: 根据药品 id 查找对应药品信息 ---
const getMedicineInfoById = (medicineId) => {
  if (!medicinePageData.value || medicinePageData.value.length === 0) return {};
  const medicine = medicinePageData.value.find(item => item.id === medicineId);
  return medicine ? medicine : {};
};

// --- 根据疾病 id 查找对应的所有药品 (用于弹窗) ---
const getMedicinesByIllnessId = (illnessId) => {
  if (!illnessMedicinePageData.value || illnessMedicinePageData.value.length === 0) return [];
  const medicines = illnessMedicinePageData.value
    .filter(item => item.illnessId === illnessId)
    .map(item => {
      const info = getMedicineInfoById(item.medicineId);
      return {
        name: info.medicineName || '未知药物',
        id: item.medicineId,
        keyword: info.keyword || '',
        medicineEffect: info.medicineEffect || '',
        medicineBrand: info.medicineBrand || '',
        usAge: info.usAge || '',
        taboo: info.taboo || '',
        interaction: info.interaction || '',
        medicinePrice: info.medicinePrice || 0,
        imgPath:info.imgPath || '',
      };
    });
  return medicines;
};

// --- 调用后端 getillnessInfo 查询疾病详细信息，并填充弹窗数据 ---
const fetchIllnessById = async (id) => {
  if (!id) {
    ElMessage.error('无效的疾病 id');
    return;
  }
  loading.value = true;
  try {
    const res = await getillnessInfo(id);
    if (res && res.code === 200 && res.data) {
      const data = res.data;
      console.log('datadatadata2222',res.data);
      
      illnessForm.id = data.id || id;
      illnessForm.illnessName = data.illnessName || '';
      // 后端字段包含富文本 html（includeReason / illnessSymptom / specialSymptom）
      illnessForm.includeReason = data.includeReason || '';
      illnessForm.illnessSymptom = data.illnessSymptom || '';
      illnessForm.specialSymptom = data.specialSymptom || '';
      // 根据 illnessId 去映射药品信息（使用 illnessMedicinePage 和 medicinePage）
      illnessForm.medicines = getMedicinesByIllnessId(id);
      showIllnessDialog.value = true;
      console.log('datadatadata2222',illnessForm.medicines);

    } else {
      ElMessage.error(res.message || '获取疾病详情失败');
    }
  } catch (err) {
    console.error(err);
    ElMessage.error('获取疾病详情失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// --- 点击搜索结果时（result 有 id） ---
const handleResultClick = (result) => {
  if (result && result.id) {
    // 直接通过 id 调用后端详细接口，保证数据最新
    fetchIllnessById(result.id);
  } else if (result && result.illnessName) {
    // 兜底：按名称查找 id 再请求
    const found = illnessPageData.value.find(i => i.illnessName === result.illnessName || (i.illnessName && i.illnessName.includes(result.illnessName)));
    if (found) {
      fetchIllnessById(found.id);
    } else {
      ElMessage.warning('未找到对应疾病 ID');
    }
  }
};

// --- 点击搜索历史：按名称匹配疾病并请求详情 ---
const handleHistoryClick = (historyName) => {
  if (!historyName) return;
  // 先更新搜索框
  searchQuery.value = historyName;
  // 在本地疾病列表里查找同名或包含的记录
  const found = illnessPageData.value.find(i => i.illnessName === historyName || (i.illnessName && i.illnessName.includes(historyName)));
  if (found && found.id) {
    fetchIllnessById(found.id);
  } else {
    ElMessage.warning('未在本地疾病数据中找到对应项，尝试按名称搜索后再点详情');
    // 仍然进行模糊搜索展示在结果列表（可进一步点击）
    searchResults.value = illnessPageData.value.filter(illness =>
      illness.illnessName && illness.illnessName.toLowerCase().includes(historyName.toLowerCase())
    );
  }
};

// --- 点击分类：找到该分类下的第一个疾病并请求详情（示例行为，必要可调整为跳转到分类列表） ---
const handleCategoryClick = (category) => {
  if (!category || !category.id) return;
  // 在疾病数据里寻找 kindId === category.id 的项
  const found = illnessPageData.value.find(i => i.kindId === category.id);
  if (found && found.id) {
    fetchIllnessById(found.id);
  } else {
    ElMessage.warning('该分类下暂未找到疾病数据');
    // 可选择跳转到分类搜索页或展示空列表
  }
};


// 点击药品名可以跳转到药品详情页（如果你有路由），这里示例仅提示
const handleMedicineClick = (medicineId) => {
  const med = getMedicineInfoById(medicineId);
  if (med && med.id) {
    // 如果你有药品详情页，可以 router.push 到对应页面；否则用提示或弹窗
    // router.push({ path: `/medicine/${med.id}` });
    ElMessage.info(`药品：${med.medicineName || '未知'} (示例：请实现药品详情页跳转)`);
  } else {
    ElMessage.warning('未找到药品信息');
  }
};

// 回到顶部
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });
};

// strip HTML 简单工具（用于搜索或预览）
const stripHtml = (html) => {
  if (!html) return '';
  return html.replace(/<[^>]*>/g, '').replace(/\s+/g, ' ').trim();
};
</script>

<style scoped>
.search-container {
  position: relative;
  width: 100%;
  min-height: 100vh;
  overflow: hidden;
  margin-top: 70px;
}

.background-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 400px;
  overflow: hidden;
  z-index: -1;
}

.background-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: brightness(70%);
  transform: scale(1.1);
}

.search-area {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 20px 40px;
  text-align: center;
  position: relative;
  z-index: 1;
}

.search-prompt {
  color: white;
  margin-bottom: 30px;
}

.search-prompt h2 {
  font-size: 36px;
  margin: 15px 0;
  color: #ffffff;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.search-prompt p:first-child {
  font-size: 18px;
}

.search-prompt p:last-child {
  font-size: 16px;
  max-width: 800px;
  margin: 0 auto;
  line-height: 1.6;
}

.search-bar {
  display: flex;
  justify-content: center;
  max-width: 600px;
  margin: 0 auto;
  background-color: white;
  border-radius: 50px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.search-bar input {
  flex: 1;
  padding: 15px 20px;
  border: none;
  outline: none;
  font-size: 16px;
}

.search-bar button {
  padding: 15px 25px;
  background-color: #008c69;
  color: white;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-bar button:hover {
  background-color: #006c52;
}

.divider {
  height: 1px;
  background: linear-gradient(to right, transparent, rgba(255, 255, 255, 0.7), transparent);
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.bottom-info {
  max-width: 1200px;
  margin: 40px auto 0;
  padding: 0 20px;
  position: relative;
  z-index: 1;
}

.search-results {
  background-color: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  margin-bottom: 30px;
}

.search-results h3 {
  color: #008c69;
  margin-bottom: 15px;
  font-size: 20px;
  border-left: 4px solid #008c69;
  padding-left: 10px;
}

.search-results ul {
  list-style: none;
  padding: 0;
}

.search-result-item {
  padding: 12px 15px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
  border-radius: 5px;
}

.search-result-item:hover {
  background-color: #f5f5f5;
  transform: translateY(-2px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.search-result-item:last-child {
  border-bottom: none;
}

.illness-dialog ::v-deep(.el-dialog__header) {
  background-color: #008c69;
  padding: 15px 20px;
}

.illness-dialog ::v-deep(.el-dialog__title) {
  color: white;
  font-size: 20px;
}

.illness-dialog ::v-deep(.el-dialog__headerbtn) {
  color: white;
}

.illness-dialog ::v-deep(.el-dialog__body) {
  padding: 20px;
}

.illness-dialog ::v-deep(.el-form-item__label) {
  color: #444;
  font-weight: bold;
}

.illness-dialog ::v-deep(.el-form-item__content) {
  background-color: #f9f9f9;
  border-radius: 5px;
  padding: 8px 12px;
}

.medicine-detail-card {
  border: 1px solid #eaeaea;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  background-color: #fafafa;
}

.medicine-detail-card h4 {
  color: #008c69;
  margin-top: 0;
  margin-bottom: 10px;
  padding-bottom: 5px;
  border-bottom: 1px solid #eaeaea;
}

.medicine-link {
  color: #409eff;
  text-decoration: underline;
  cursor: pointer;
}

.medicine-link:hover {
  color: #66b1ff;
}

.categories-history {
  justify-content: space-between;
  margin-top: 40px;
  display: flex;
}

.categories-container, .history-container {
  display: flex;
  flex: 1;
}

.categories-title, .history-title {
  font-size: 18px;
  color: #333;
  margin-bottom: 15px;
  font-weight: bold;
  margin-left: 30px;
}

.categories-list, .history-list {
  display: flex;
  flex-wrap: wrap;
}

.categories-row, .history-list {
  margin-bottom: 10px;
   height: 50px ;
}

.categories-row a, .history-list a {
  color: #666;
  text-decoration: none;
  margin-right: 15px;
  margin-bottom: 8px;
  padding: 3px 8px;
  border-radius: 15px;
  background-color: #f5f5f5;
  transition: all 0.2s;
   height: 30px ;
}

.categories-row a:hover, .history-list a:hover {
  color: #008c69;
  background-color: #e8f4f1;
  transform: translateY(-2px);
   height: 50px ;
}

.back-to-top {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 50px;
  height: 50px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
  z-index: 10;
}

.back-to-top:hover {
  background-color: #008c69;
  transform: translateY(-5px);
}

.back-to-top img {
  width: 25px;
  height: 25px;
}

/* 富文本渲染样式 */
.rich-text {
  background-color: #fff;
  padding: 12px;
  border-radius: 6px;
  max-height: 360px;
  overflow: auto;
}

/* 无数据提示 */
.no-data {
  color: #999;
  padding: 8px;
}

@media (max-width: 768px) {
  .search-prompt h2 {
    font-size: 28px;
  }

  .search-bar {
    flex-direction: column;
    border-radius: 10px;
  }

  .search-bar input {
    width: 100%;
    border-radius: 10px 10px 0 0;
  }

  .search-bar button {
    width: 100%;
    border-radius: 0 0 10px 10px;
  }

  .categories-history {
    flex-direction: column;
  }

  .categories-container, .history-container {
    margin-bottom: 30px;
  }
}

/* ===== 全局色调与基础样式优化 ===== */
:root {
  --primary-color: #00a884;
  --text-color: #333;
  --subtext-color: #777;
  --card-bg: #ffffff;
  --shadow-light: 0 4px 15px rgba(0, 0, 0, 0.08);
  --shadow-hover: 0 8px 25px rgba(0, 0, 0, 0.12);
}

.illness-dialog ::v-deep(.el-dialog__header) {
  background: linear-gradient(135deg, #00a884, #00bfa6);
  padding: 18px 20px;
  border-radius: 8px 8px 0 0;
}

.illness-dialog ::v-deep(.el-dialog__title) {
  color: #fff;
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 1px;
}

.illness-dialog ::v-deep(.el-dialog__headerbtn) {
  color: #fff;
}

.illness-dialog ::v-deep(.el-dialog__body) {
  background: #f8faf9;
  border-radius: 0 0 8px 8px;
}

/* ===== 药品详情卡 ===== */
.medicine-detail-card {
  background: var(--card-bg);
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 25px;
  box-shadow: var(--shadow-light);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  overflow: hidden;
}

.medicine-detail-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-hover);
}

/* 药品图片头部 */
.medicine-header {
  display: flex;
  align-items: center;
  margin-bottom: 18px;
}

.medicine-img {
  width: 100px;
  height: 100px;
  border-radius: 12px;
  object-fit: cover;
  margin-right: 20px;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s;
}

.medicine-img:hover {
  transform: scale(1.05);
}

.medicine-info h4 {
  color: var(--primary-color);
  font-size: 20px;
  margin: 0;
}

.medicine-brand {
  color: var(--subtext-color);
  font-size: 14px;
  margin-top: 4px;
}

/* 药品主体信息 */
.medicine-body {
  background: #f9fdfc;
  border-radius: 12px;
  padding: 15px 18px;
  line-height: 1.7;
}

.medicine-item {
  display: flex;
  margin-bottom: 8px;
  font-size: 15px;
  color: var(--text-color);
}

.medicine-item .label {
  width: 90px;
  font-weight: 600;
  color: var(--primary-color);
  flex-shrink: 0;
}

.medicine-item .price {
  font-weight: 700;
  color: #e46a0a;
}

/* ===== 富文本内容区域 ===== */
.rich-text {
  background-color: #ffffff;
  padding: 15px;
  border-radius: 10px;
  max-height: 360px;
  overflow-y: auto;
  line-height: 1.7;
  color: var(--text-color);
  box-shadow: var(--shadow-light);
}

.no-data {
  color: #aaa;
  padding: 10px;
  font-style: italic;
}

/* ===== 小屏优化 ===== */
@media (max-width: 768px) {
  .medicine-detail-card {
    padding: 15px;
  }

  .medicine-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .medicine-img {
    width: 100%;
    height: auto;
    border-radius: 10px;
    margin: 0 0 10px 0;
  }

  .medicine-info h4 {
    font-size: 18px;
  }

  .medicine-body {
    padding: 12px;
  }
}


.clear-history {
  color: #ccc;
  font-size: 14px;
  cursor: pointer;
  margin-left: 10px;
  transition: color 0.2s;
  margin-top: 5px;
}
.clear-history:hover {
  color: #00a884;
 
}

</style>
