<template>
  <nav2 />
  <div class="search-bottom">
    <!-- 背景图片 -->
    <!-- <img src="@/assets/images/blog/female-medical-worker-2021-04-03-03-56-15-utc.jpg" alt="医疗工作者" class="background-image"> -->
  </div>
  <!-- 搜索提示信息 -->
  <div class="search-prompt">
    <p>没有找到你想要的结果吗？</p>
    <h2>搜索一下</h2>
    <p>智慧医药为您提供疾病查询,常见疾病大全,帮您全面了解疾病病因症状、检查治疗、饮食护理等信息,是您疾病查询的好帮手。</p>
  </div>

  <!-- 搜索框 -->
  <div class="search-bar">
    <input type="text" placeholder="搜索..." v-model="searchQuery" @keyup.enter="handleSearch">
    <button @click="handleSearch"><van-icon name="search" /></button>
  </div>

  <!-- 搜索结果 -->
  <div v-if="searchResults.length" class="search-results">
    <h3>搜索结果:</h3>
    <ul>
      <li v-for="result in searchResults" :key="result.id" @click="showIllnessDetails(result.id)">
        {{ result.illnessName }} - {{ result.illnessSymptom }}
      </li>
    </ul>
  </div>

  <!-- 疾病详细信息模态框 -->
  <el-dialog v-model="showIllnessDialog" title="疾病与药品详情" width="70%">
    <el-form :model="illnessForm" label-width="150px">
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
        <div v-for="medicine in illnessForm.medicines" :key="medicine.id">
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
    <div class="categories">
      <span>分类：</span>
      <template v-if="categories.length > 10">
        <a href="#" v-for="category in categories.slice(0, 10)" :key="category">{{ category }}</a>
        <!-- <span class="ellipsis" :title="categories.slice(10).join(', ')">...</span> -->
      </template>
    </div>
    <div class="categories">
      <a href="#" v-for="category in categories.slice(11, 20)" :key="category">{{ category }}</a>
    </div>
    <div class="categories">
      <a href="#" v-for="category in categories.slice(21, 30)" :key="category">{{ category }}</a>
    </div>
    <div class="categories">
      <a href="#" v-for="category in categories.slice(31, 80)" :key="category">{{ category }}</a>
    </div>
    <div class="history">
      <span>搜索历史：</span>
      <a href="#" v-for="history in searchHistory" :key="history">{{ history }}</a>
    </div>
  </div>

  <!-- 回到顶部按钮 -->
  <div class="back-to-top" @click="scrollToTop">
    <img src="@/assets/images/blog/image.png" alt="回到顶部" />
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { illnessKindPage } from '@/api/admin/illnessKind.js';
import { ElMessage } from 'element-plus';
import { illnessPage } from '@/api/admin/illness.js';
import { illnessMedicinePage } from '@/api/admin/illnessMedicine.js';
import { medicinePage } from '@/api/admin/medicine.js';
import nav2 from '@/components/nav2.vue'; // 引入nav1组件

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
  medicines: []
});

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
  })
  .finally(() => {
    // 加载完成后执行的逻辑
  });

illnessPage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      illnessPageData.value = res.data.data;
      console.log('illnessPageData.value5555555', illnessPageData.value);
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('请求失败，请稍后再试');
  });

// 获取疾病药品关系数据
illnessMedicinePage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      illnessMedicinePageData.value = res.data.data;
      console.log('illnessMedicinePageData.value', illnessMedicinePageData.value);
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('请求失败，请稍后再试');
  })
  .finally(() => {
    // 加载完成后执行的逻辑
  });

// 获取药品数据
medicinePage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      medicinePageData.value = res.data.data;
      console.log('medicinePageData.value', medicinePageData.value);
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error('请求失败，请稍后再试');
  })
  .finally(() => {
    // 加载完成后执行的逻辑
  });

// 处理搜索逻辑
const handleSearch = () => {
  if (searchQuery.value) {
    console.log('搜索关键词:', searchQuery.value);
    searchResults.value = illnessPageData.value.filter(illness =>
      illness.illnessName.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
    router.push({ path: '/search', query: { q: searchQuery.value } });
  }
};

// 根据药品id查找对应的药品名字
const getMedicineNameById = (medicineId) => {
  if (!medicinePageData.value || medicinePageData.value.length === 0) return '未知药物';
  const medicine = medicinePageData.value.find(item => item.id === medicineId);
  return medicine ? medicine.medicineName : '未知药物';
};

// 根据疾病id查找对应的所有药品
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

// 根据药品id查找对应的药品信息
const getMedicineInfoById = (medicineId) => {
  if (!medicinePageData.value || medicinePageData.value.length === 0) return {};
  const medicine = medicinePageData.value.find(item => item.id === medicineId);
  return medicine ? medicine : {};
};

// 显示疾病详细信息
const showIllnessDetails = (illnessId) => {
  // 获取疾病信息
  const illness = illnessPageData.value.find(item => item.id === illnessId);
  if (!illness) {
    ElMessage.error('获取疾病信息失败');
    return;
  }

  // 获取药品信息
  const medicines = getMedicinesByIllnessId(illnessId);

  // 填充疾病详细信息表单
  illnessForm.illnessName = illness.illnessName;
  illnessForm.illnessSymptom = illness.illnessSymptom;
  illnessForm.specialSymptom = illness.specialSymptom;
  illnessForm.medicines = medicines;

  // 显示疾病详细信息模态框
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
.search-bottom {
  position: relative;
  background-image: url("http://www.xumuren.com/data/attachment/forum/202409/24/104003qrfvz2d7gll22zp2.jpg");
  background-size: cover;
  background-position: center;
  background-attachment: fixed; /* 固定背景图片 */
  height: 120px; /* 设置背景图片的高度 */
  filter: brightness(70%);
}

.search-prompt {
  text-align: center;
  margin-top: 20px;
}

.search-prompt h2 {
  color: #008c69;
}

.search-bar {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.search-bar input {
  padding: 10px;
  width: 300px;
  border: 1px solid #ccc;
  border-radius: 4px 0 0 4px;
}

.search-bar button {
  background-color: #008c69;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 0 4px 4px 0;
}

.categories-history {
  text-align: center;
  margin-top: 20px;
}

.categories a, .history a {
  margin: 0 5px;
  color: #008c69;
  text-decoration: none;
}

.categories .ellipsis {
  margin: 0 5px;
  color: #008c69;
  cursor: help;
}

.back-to-top {
  background-color: transparent;
  position: fixed;
  bottom: 20px;
  right: 20px;
  cursor: pointer;
}

.back-to-top img {
  background-color: transparent;
  width: 40px;
}

.search-results {
  margin-top: 20px;
}

.search-results h3 {
  color: #008c69;
}

.search-results ul {
  list-style-type: none;
  padding: 0;
}

.search-results li {
  margin: 5px 0;
  cursor: pointer;
}

.search-results li:hover {
  background-color: #f0f0f0;
}

.medicine-link {
  color: #409EFF; /* 蓝色 */
  text-decoration: underline; /* 添加下划线 */
  cursor: pointer; /* 改变鼠标指针为手型 */
}

.medicine-link:hover {
  color: #66B1FF; /* 悬停时颜色加深 */
  text-decoration: none; /* 悬停时去掉下划线 */
}
</style>