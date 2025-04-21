<!-- src/views/Search.vue -->
<template>
  <div class="container" style="background-color: rgb(241,241,241);">
    <nav2 />

    <div style="width: 100%; text-align: center; align-items: center;">
      <h2 style="color: rgb(32, 64, 102); margin-top: 5px; height: 100%;">搜索结果</h2>
    </div>
    <div style="width: 81%; margin: 0 auto; margin-top: 20px;">
      <van-row gutter="24">
        <van-col span="8" v-for="(item, index) in displayedItems" :key="index">
          <van-card 
            style="background-color: rgb(255,255,255);" 
            class="custom-card"
            :to="`/illness/${item.id}`"
          >
            <template #desc>
              <div class="card-description special-symptom" style="text-align: center; font-size: 16px;">{{ item.illnessName }}</div>
              <div class="card-description symptom"> 疾病症状：{{ item.illnessSymptom }}</div>
              <div class="card-description cause"> 诱发因素：{{ item.includeReason }}</div>
              <div class="card-description special-symptom"> 特殊症状：{{ item.specialSymptom }}</div>
              <div class="card-description medicine">
                <span v-for="(medicine, index) in getMedicinesByIllnessId(item.id)" :key="index" class="medicine-link" @click="handleViewCombinedInfo(item.id, medicine.id)">
                  {{ medicine.name }}
                  <span v-if="index < getMedicinesByIllnessId(item.id).length - 1">, </span>
                </span>
              </div>
              <div class="card-description">
                <div style="display: flex; justify-content: space-between; align-items: center;">
                  <span>{{ item.createTime }}  |  <span class="success">{{ getCategoryNameById(item.kindId) }}</span></span> 
                  <span><van-icon name="eye" /> {{ getCategoryPageviewById(item.id) }}</span>
                </div>
              </div>
            </template>
          </van-card>
        </van-col>
      </van-row>
      <div style="text-align: center; margin-top: 20px;">
        <van-pagination v-model="currentPage" :total-items="totalItems" :items-per-page="pageSize" @change="handlePageChange" />
      </div>
    </div>

    <!-- 综合信息模态框 -->
    <el-dialog v-model="showCombinedDialog" title="疾病与药品详情" width="70%">
      <el-form :model="combinedForm" label-width="150px">
        <el-form-item label="疾病名称">
          <el-input v-model="combinedForm.illnessName" disabled></el-input>
        </el-form-item>
        <el-form-item label="疾病症状">
          <el-input v-model="combinedForm.illnessSymptom" type="textarea" :rows="2" disabled></el-input>
        </el-form-item>
        <el-form-item label="特殊症状">
          <el-input v-model="combinedForm.specialSymptom" type="textarea" :rows="2" disabled></el-input>
        </el-form-item>
        <el-form-item label="对症药品">
          <el-input v-model="combinedForm.medicineName" disabled></el-input>
        </el-form-item>
        <el-form-item label="作用">
          <el-input v-model="combinedForm.keyword" disabled></el-input>
        </el-form-item>
        <el-form-item label="药品功效">
          <el-input v-model="combinedForm.medicineEffect" type="textarea" :rows="2" disabled></el-input>
        </el-form-item>
        <el-form-item label="药品品牌">
          <el-input v-model="combinedForm.medicineBrand" disabled></el-input>
        </el-form-item>
        <el-form-item label="用法用量">
          <el-input v-model="combinedForm.usAge" type="textarea" :rows="2" disabled></el-input>
        </el-form-item>
        <el-form-item label="禁忌">
          <el-input v-model="combinedForm.taboo" type="textarea" :rows="2" disabled></el-input>
        </el-form-item>
        <el-form-item label="药品相互作用">
          <el-input v-model="combinedForm.interaction" type="textarea" :rows="2" disabled></el-input>
        </el-form-item>
        <el-form-item label="药品价格">
          <el-input-number v-model="combinedForm.medicinePrice" :precision="2" :step="0.01" disabled></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCombinedDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router';
import nav2 from '@/components/nav2.vue';
import { illnessPage } from '@/api/admin/illness.js'
import { illnessKindPage } from '@/api/admin/illnessKind.js'
import { pageviewPage } from '@/api/admin/pageview.js'
import { ElMessage } from 'element-plus';
import { illnessMedicinePage } from '@/api/admin/illnessMedicine.js';
import { medicinePage } from '@/api/admin/medicine.js'

const route = useRoute();

const illnesses = ref([]);
const illnessKindData = ref([]);
const pageviewPageData = ref([]);
const illnessMedicinePageData = ref([]);
const medicinePageData = ref([]);

const pageSize = 9;
const currentPage = ref(1);
const totalItems = computed(() => illnesses.value.length);
const displayedItems = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return illnesses.value.slice(start, end);
});

const searchQuery = ref(route.query.q || '');

// 获取页面浏览量数据
pageviewPage({pageNum:1, pageSize:100})
.then(res => {
  if(res.code === 200) {
    pageviewPageData.value = res.data.data;
    console.log('pageviewPageData.value', pageviewPageData.value);
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

// 获取疾病种类数据
illnessKindPage({pageNum:1, pageSize:100})
.then(res => {
  if(res.code === 200){
    illnessKindData.value = res.data.data;
    console.log('illnessKindData.value', illnessKindData.value);
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

// 获取疾病数据
illnessPage({pageNum:1, pageSize:100, illnessName: searchQuery.value})
.then(res => {
  illnesses.value = res.data.data;
  console.log('illnesses.value', illnesses.value);
})
.catch(err => {
  ElMessage.error('请求失败，请稍后再试');
})
.finally(() => {
  // 加载完成后执行的逻辑
});

// 获取疾病药品关系数据
illnessMedicinePage({pageNum:1, pageSize:100})
.then(res => {
  if(res.code === 200){
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
medicinePage({pageNum:1, pageSize:100})
.then(res => {
  if(res.code === 200){
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

const handlePageChange = () => {
  // 分页改变时的逻辑
};

// 根据 kindId 查找对应的疾病种类名字
const getCategoryNameById = (kindId) => {
  if (!illnessKindData.value || illnessKindData.value.length === 0) return '未知种类';
  const category = illnessKindData.value.find(item => item.id === kindId);
  return category ? category.name : '未知种类';
};

// 根据疾病id查找对应疾病的浏览量
const getCategoryPageviewById = (id) => {
  if (!pageviewPageData.value || pageviewPageData.value.length === 0) return '未知浏览数';
  const pageview = pageviewPageData.value.find(item => item.illnessId === id);
  return pageview ? pageview.pageviews : '未知浏览数';
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
      id: item.medicineId
    }));
  return medicines;
};

// 新增综合信息模态框相关变量
const showCombinedDialog = ref(false);
const combinedForm = ref({});

// 处理点击事件，打开综合信息模态框
const handleViewCombinedInfo = (illnessId, medicineId) => {
  // 获取疾病信息
  const illness = illnesses.value.find(item => item.id === illnessId);
  if (!illness) {
    ElMessage.error('获取疾病信息失败');
    return;
  }

  // 获取药品信息
  const medicine = medicinePageData.value.find(item => item.id === medicineId);
  if (!medicine) {
    ElMessage.error('获取药品信息失败');
    return;
  }

  // 填充综合信息表单
  combinedForm.value = {
    illnessName: illness.illnessName,
    illnessSymptom: illness.illnessSymptom,
    specialSymptom: illness.specialSymptom,
    medicineName: medicine.medicineName,
    keyword: medicine.keyword,
    medicineEffect: medicine.medicineEffect,
    medicineBrand: medicine.medicineBrand,
    usAge: medicine.usAge,
    taboo: medicine.taboo,
    interaction: medicine.interaction,
    medicinePrice: medicine.medicinePrice,
  };

  // 显示综合信息模态框
  showCombinedDialog.value = true;
};
</script>

<style scoped>
.custom-card {
  height: 300px; /* 调整卡片高度 */
  width: 90%; /* 卡片宽度占满列宽 */
  box-sizing: border-box; /* 确保 padding 不影响盒子尺寸 */
  background-color: rgb(255,255,255);
  margin: 5px auto;
}

.van-card__content {
  text-align: left;
  height: 100%; /* 内容区域高度占满卡片高度 */
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 垂直方向上均匀分布内容 */
}

.card-description {
  color: #7a7a7a;
  font-size: 16px;
  margin: 15px 5px 0 5px;
  flex-grow: 1; /* 让描述区域占据剩余空间 */
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box; /* 兼容 WebKit 浏览器 */
  display: box; /* 标准属性 */
  -webkit-box-orient: vertical; /* 兼容 WebKit 浏览器 */
  box-orient: vertical; /* 标准属性 */
  -webkit-line-clamp: 4; /* 控制描述行数 (WebKit) */
  line-clamp: 4; /* 标准属性 */
}

.card-description.symptom {
  color: #67C23A; /* 绿色 */
}

.card-description.cause {
  color: #E6A23C; /* 橙色 */
}

.card-description.special-symptom {
  color: #F56C6C; /* 红色 */
}

.card-description.medicine {
  color: #409EFF; /* 蓝色 */
}

.van-card__footer {
  padding-top: 10px; /* 调整底部内容与描述之间的间距 */
}

.warning {
  color: rgb(255, 151, 106);
  font-size: 16px;
}

.primary {
  color: rgb(25, 137, 250);
  font-size: 16px;
}

.success {
  color: rgb(7, 193, 96);
  font-size: 16px;
}

.danger {
  color: rgb(238, 10, 36);
  font-size: 16px;
}

/* 添加药品链接样式 */
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