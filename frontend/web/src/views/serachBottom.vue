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
          <li v-for="result in searchResults" :key="result.id" @click="showIllnessDetails(result.id)"
            class="search-result-item">
            {{ result.illnessName }} - {{ result.illnessSymptom }}
          </li>
        </ul>
      </div>

      <!-- 疾病详细信息模态框 -->
      <el-dialog v-model="showIllnessDialog" title="疾病与药品详情" width="70%" class="illness-dialog">
        <el-form :model="illnessForm" label-width="120px" label-position="left">
          <el-form-item label="疾病名称">
            <el-input v-model="illnessForm.illnessName" disabled></el-input>
          </el-form-item>
          <el-form-item label="疾病症状">
            <el-input v-model="illnessForm.illnessSymptom" type="textarea" :rows="2" disabled></el-input>
          </el-form-item>
          <el-form-item label="特殊症状">
            <el-input v-model="illnessForm.specialSymptom" type="textarea" :rows="2" disabled></el-input>
          </el-form-item>
          <el-form-item label="对症药品">
            <div>
              <span v-for="(medicine, index) in illnessForm.medicines" :key="medicine.id" class="medicine-link">
                {{ medicine.name }}
                <span v-if="index < illnessForm.medicines.length - 1">, </span>
              </span>
            </div>
          </el-form-item>
          <el-form-item label="药品详情">
            <div v-for="medicine in illnessForm.medicines" :key="medicine.id" class="medicine-detail-card">
              <h4>{{ medicine.name }}</h4>
              <el-form-item label="作用">
                <el-input v-model="medicine.keyword" disabled></el-input>
              </el-form-item>
              <el-form-item label="药品功效">
                <el-input v-model="medicine.medicineEffect" type="textarea" :rows="2" disabled></el-input>
              </el-form-item>
              <el-form-item label="药品品牌">
                <el-input v-model="medicine.medicineBrand" disabled></el-input>
              </el-form-item>
              <el-form-item label="用法用量">
                <el-input v-model="medicine.usAge" type="textarea" :rows="2" disabled></el-input>
              </el-form-item>
              <el-form-item label="禁忌">
                <el-input v-model="medicine.taboo" type="textarea" :rows="2" disabled></el-input>
              </el-form-item>
              <el-form-item label="药品相互作用">
                <el-input v-model="medicine.interaction" type="textarea" :rows="2" disabled></el-input>
              </el-form-item>
              <el-form-item label="药品价格">
                <el-input-number v-model="medicine.medicinePrice" :precision="2" :step="0.01" disabled></el-input-number>
              </el-form-item>
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
              <a href="#" v-for="category in row" :key="category">{{ category }}</a>
            </div>
          </div>
        </div>
        <div class="history-container">
          <div class="history-title">搜索历史：</div>
          <div class="history-list">
            <a href="#" v-for="history in searchHistory" :key="history">{{ history }}</a>
          </div>
        </div>
      </div>

      <!-- 回到顶部按钮 -->
      <div class="back-to-top" @click="scrollToTop">
        <img src="@/assets/images/goTop.png" alt="回到顶部" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { useRouter } from 'vue-router';
import { illnessKindPage, illnessPage, illnessMedicinePage, medicinePage } from '@/api/admin/FindIllness';
import { ElMessage } from 'element-plus';
import nav2 from '@/components/nav2.vue';

const router = useRouter();

const searchHistory = ['猪肺炎1', '猪肺炎2', '猪肺炎3', '水肿病1', '水肿病2', '嗜血杆菌病1', '白痢病3', '白痢病2'];
const searchQuery = ref('');
const categories = ref([]);
const illnessPageData = ref([]);
const searchResults = ref([]);
const illnessMedicinePageData = ref([]);
const medicinePageData = ref([]);
const showIllnessDialog = ref(false);
const illnessForm = reactive({
  illnessName: '',
  illnessSymptom: '',
  specialSymptom: '',
  medicines: [],
});

// 行数为 4 的分类数组
const categoryRows = computed(() => {
  const rows = [];
  const chunkSize = Math.ceil(categories.value.length / 4);
  for (let i = 0; i < 4; i++) {
    rows.push(categories.value.slice(i * chunkSize, (i + 1) * chunkSize));
  }
  return rows;
});

// 获取分类数据
illnessKindPage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      categories.value = res.data.data.map(item => item.name);
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('请求失败，请稍后再试');
  });

illnessPage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      illnessPageData.value = res.data.data;
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('请求失败，请稍后再试');
  });

illnessMedicinePage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      illnessMedicinePageData.value = res.data.data;
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('请求失败，请稍后再试');
  });

medicinePage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      medicinePageData.value = res.data.data;
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('请求失败，请稍后再试');
  });

// 处理搜索逻辑
const handleSearch = () => {
  if (searchQuery.value) {
    searchResults.value = illnessPageData.value.filter(illness =>
      illness.illnessName.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
    router.push({ path: '/search', query: { q: searchQuery.value } });
  }
};

// 根据药品 id 查找对应的药品名字
const getMedicineNameById = (medicineId) => {
  if (!medicinePageData.value || medicinePageData.value.length === 0) return '未知药物';
  const medicine = medicinePageData.value.find(item => item.id === medicineId);
  return medicine ? medicine.medicineName : '未知药物';
};

// 根据疾病 id 查找对应的所有药品
const getMedicinesByIllnessId = (illnessId) => {
  if (!illnessMedicinePageData.value || illnessMedicinePageData.value.length === 0) return [];
  const medicines = illnessMedicinePageData.value
    .filter(item => item.illnessId === illnessId)
    .map(item => ({

      name: getMedicineNameById(item.medicineId),
      id: item.medicineId,
      keyword: getMedicineInfoById(item.medicineId).keyword,
      medicineEffect: getMedicineInfoById(item.medicineId).medicineEffect,
      medicineBrand: getMedicineInfoById(item.medicineId).medicineBrand,
      usAge: getMedicineInfoById(item.medicineId).usAge,
      taboo: getMedicineInfoById(item.medicineId).taboo,
      interaction: getMedicineInfoById(item.medicineId).interaction,
      medicinePrice: getMedicineInfoById(item.medicineId).medicinePrice,
    }));
  return medicines;
};

// 根据药品 id 查找对应的药品信息
const getMedicineInfoById = (medicineId) => {
  if (!medicinePageData.value || medicinePageData.value.length === 0) return {};
  const medicine = medicinePageData.value.find(item => item.id === medicineId);
  return medicine ? medicine : {};
};

// 显示疾病详细信息
const showIllnessDetails = (illnessId) => {
  const illness = illnessPageData.value.find(item => item.id === illnessId);
  if (!illness) {
    ElMessage.error('获取疾病信息失败');
    return;
  }

  const medicines = getMedicinesByIllnessId(illnessId);

  illnessForm.illnessName = illness.illnessName;
  illnessForm.illnessSymptom = illness.illnessSymptom;
  illnessForm.specialSymptom = illness.specialSymptom;
  illnessForm.medicines = medicines;

  showIllnessDialog.value = true;
};

// 回到顶部功能
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });
};
</script>

<style scoped>
.search-container {
  position: relative;
  width: 100%;
  min-height: 100vh;
  overflow: hidden;
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
  display: flex;
  justify-content: space-between;
  margin-top: 40px;
}

.categories-container, .history-container {
  flex: 1;
}

.categories-title, .history-title {
  font-size: 18px;
  color: #333;
  margin-bottom: 15px;
  font-weight: bold;
}

.categories-list, .history-list {
  display: flex;
  flex-wrap: wrap;
}

.categories-row, .history-list {
  margin-bottom: 10px;
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
}

.categories-row a:hover, .history-list a:hover {
  color: #008c69;
  background-color: #e8f4f1;
  transform: translateY(-2px);
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
</style>