<template>
  <div class="illness-detail-page">
    <!-- 顶部导航 -->
    <nav2 />
    

    <!-- 加载中 -->
    <div v-if="loading" class="center">
      <el-icon class="loading-icon"><ChatSquare /></el-icon>
      <p class="loading-text">正在调取病历...</p>
    </div>

    <!-- 错误提示 -->
    <div v-else-if="error" class="center">
      <p class="error-text">病历调取失败</p>
      <el-button type="primary" @click="loadAll">重新调取</el-button>
    </div>

    <!-- 主体内容 -->
    <div v-else class="content">
        
      <!-- 头部信息 -->
      <el-card class="record-header">
        <!-- 返回按钮 -->
         <div style="margin-left:-90%;">
          <el-button type="info"  circle @click="$router.back()" size="large">
            <el-icon><Back /></el-icon>
          </el-button>
         </div>
        <div class="header-left">
            
          <img class="icon" :src="getStableImage(illnessData.id)" alt="疾病图标" />
        </div>
        <div class="header-center">
          <h2 class="name">{{ illnessData.illnessName }}</h2>
          <p class="id">病历编号：{{ illnessData.id }}</p>
        </div>
        <div class="header-right">
          <p class="update-time">最近更新：{{ formatDate(illnessData.updateTime) }}</p>
        </div>
      </el-card>

      <!-- 病因 / 诱因 -->
      <el-card class="block">
        <div class="block-header">
          <el-icon :size="28"><WarningFilled /></el-icon>
          <span class="title">病因 / 诱因</span>
        </div>
        <div class="block-body" v-html="illnessData.includeReason"></div>
      </el-card>

      <!-- 常见症状 -->
      <el-card class="block">
        <div class="block-header">
          <el-icon :size="28"><DocumentCopy /></el-icon>
          <span class="title">常见症状</span>
        </div>
        <div class="block-body" v-html="illnessData.illnessSymptom"></div>
      </el-card>

      <!-- 特殊症状 -->
      <el-card class="block">
        <div class="block-header">
          <el-icon :size="28"><WarnTriangleFilled /></el-icon>
          <span class="title">特殊症状</span>
        </div>
        <div class="block-body" v-html="illnessData.specialSymptom"></div>
      </el-card>

      <!-- 推荐治疗药物 -->
      <el-card class="block drug-block">
        <div class="block-header">
          <el-icon :size="28"><Select /></el-icon>
          <span class="title">推荐治疗方案</span>
          <span class="sub">共 {{ medicineList.length }} 种药物</span>
        </div>

        <div class="drug-list">
          <el-row :gutter="20">
            <el-col
              v-for="m in medicineList"
              :key="m.id"
              :xs="24"
              :sm="12"
              :md="8"
              :lg="6"
            >
              <el-card class="drug-card" @click="toDrugDetail(m.id)">
                <img class="drug-img" :src="m.imgPath || defaultDrugImg" alt="药物图片" />
                <div class="drug-info">
                  <div class="drug-name">{{ m.medicineName }}</div>
                  <div class="drug-price">¥{{ m.medicinePrice || '-' }}</div>
                  <div class="drug-brand">{{ m.medicineBrand }}</div>
                  <div class="drug-effect">{{ m.medicineEffect }}</div>
                  <div class="tags">
                    <span class="tag">用法：{{ m.usAge || '暂无' }}</span>
                    <span class="tag danger">禁忌：{{ m.taboo || '无' }}</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <!-- 底部时间 -->
      <div class="footer">
        <p>创建时间：{{ formatDate(illnessData.createTime) }}</p>
        <p>最后更新：{{ formatDate(illnessData.updateTime) }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import nav2 from '@/components/nav2.vue';
import DOMPurify from 'dompurify';
import { ElMessage } from 'element-plus';

// ==== API导入 ====
import {
  getillnessInfo,
  illnessMedicinePage,
  medicinePage
} from '@/api/admin/FindIllness';

// ==== 路由 ====
const route = useRoute();
const router = useRouter();

// ==== 状态 ====
const illnessId = ref(route.query.id || null);
const illnessData = ref({});
const medicineList = ref([]);
const loading = ref(true);
const error = ref(false);
const defaultDrugImg = new URL('@/assets/images/medicine-default.jpg', import.meta.url).href;

// ==== 方法 ====
async function loadAll() {
  if (!illnessId.value) {
    error.value = true;
    loading.value = false;
    return;
  }
  loading.value = true;
  error.value = false;

  try {
    // 1️⃣ 获取疾病详情
    const res = await getillnessInfo(illnessId.value);
    if (res?.code !== 200) throw new Error('获取病历失败');
    illnessData.value = {
      ...res.data,
      includeReason: DOMPurify.sanitize(res.data.includeReason || ''),
      illnessSymptom: DOMPurify.sanitize(res.data.illnessSymptom || ''),
      specialSymptom: DOMPurify.sanitize(res.data.specialSymptom || '')
    };

    // 2️⃣ 获取疾病对应药物
    const imRes = await illnessMedicinePage({ pageNum: 1, pageSize: 500 });
    const allRelations = imRes?.data?.data || [];
    const relatedMedicineIds = allRelations
      .filter(r => r.illnessId === Number(illnessId.value))
      .map(r => r.medicineId);

    // 3️⃣ 获取药物数据
    const mRes = await medicinePage({ pageNum: 1, pageSize: 500 });
    const allMedicines = mRes?.data?.data || [];
    medicineList.value = allMedicines.filter(m => relatedMedicineIds.includes(m.id));

  } catch (err) {
    console.error(err);
    error.value = true;
    ElMessage.error('加载失败，请稍后重试');
  } finally {
    loading.value = false;
  }
}

// // 跳转到药品详情页（你可以后续创建 medicineDetail.vue）
// function toDrugDetail(medicineId) {
//   router.push({ path: '/medicine/:id', query: { id: medicineId } });
// }
// 跳详情页（路由）
function toDrugDetail(medicineId) {
  router.push({ name: 'MedicineDetail', params: {  id: medicineId } })
}


function formatDate(dt) {
  if (!dt) return '';
  try {
    const d = new Date(dt);
    if (isNaN(d)) return dt;
    return d.toLocaleDateString();
  } catch {
    return dt;
  }
}

// 猪病图片（与 FindIllness 保持一致）
const pigImages = [
  new URL('@/assets/images/illnessImg/pig1.jpg', import.meta.url).href,
  new URL('@/assets/images/illnessImg/pig2.jpg', import.meta.url).href,
  new URL('@/assets/images/illnessImg/pig3.jpg', import.meta.url).href,
  new URL('@/assets/images/illnessImg/pig4.jpg', import.meta.url).href,
  new URL('@/assets/images/illnessImg/pig5.jpg', import.meta.url).href
];
function getStableImage(id) {
  const s = String(id || '');
  let hash = 0;
  for (let i = 0; i < s.length; i++) hash += s.charCodeAt(i);
  return pigImages[hash % pigImages.length];
}

// === 挂载 ===
onMounted(() => {
  loadAll();
});
</script>

<style scoped>
.illness-detail-page {
  background: #f8fafc;
  min-height: 100vh;
  padding-bottom: 40px;
}
.center {
  text-align: center;
  margin-top: 120px;
}
.loading-text,
.error-text {
  font-size: 18px;
  color: #666;
}
.loading-icon {
  font-size: 40px;
  margin-bottom: 12px;
}
.content {
  max-width: 900px;
  margin: 85px auto;
  padding: 0 20px;
}
.record-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-radius: 14px;
}
.header-left .icon {
  width: 250px;
  height: 250px;
  border-radius: 12px;
  object-fit: cover;
  justify-content: center;
}

::v-deep .el-card__body {
  margin: 0 auto ;
}
.header-center {
  flex: 1;
  margin-left: 20px;
}
.name {
  font-size: 24px;
  font-weight: 700;
  color: #222;
}
.id {
  color: #777;
  font-size: 14px;
}
.header-right {
  color: #666;
  font-size: 14px;
}

/* 内容块 */
.block {
  margin-top: 20px;
  border-radius: 14px;
  overflow: hidden;
}
.block-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 12px;
}
.block-body {
  font-size: 15px;
  color: #444;
  line-height: 1.8;
  word-break: break-word;
}

/* 药品部分 */
.drug-list {
  margin-top: 16px;
}
.drug-card {
  cursor: pointer;
  transition: all 0.2s ease;
}
.drug-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.06);
}
.drug-img {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 10px;
}
.drug-info {
  margin-top: 10px;
}
.drug-name {
  font-weight: 700;
  font-size: 16px;
  color: #222;
}
.drug-price {
  color: #e84343;
  font-size: 14px;
  margin-bottom: 4px;
}
.drug-brand {
  font-size: 13px;
  color: #666;
}
.drug-effect {
  color: #444;
  font-size: 13px;
  margin: 4px 0;
}
.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}
.tag {
  background: #eef5ff;
  padding: 2px 6px;
  border-radius: 6px;
  font-size: 12px;
  color: #409eff;
}
.tag.danger {
  background: #ffeaea;
  color: #e74c3c;
}
.footer {
  text-align: center;
  margin-top: 30px;
  font-size: 13px;
  color: #777;
  line-height: 1.8;
}
</style>
