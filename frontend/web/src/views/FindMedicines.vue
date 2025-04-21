<template>
  <div class="medicine-list">
    <nav2 />

    <van-row gutter="20" class="medicine-container">
      <van-col
        span="8"
        v-for="(medicine, index) in medicinePageData"
        :key="index"
        class="medicine-col"
      >
        <van-card
          :title="medicine.medicineName"
          :desc="medicine.medicineEffect"
          :thumb="formatImagePath(medicine.imgPath)"
          class="medicine-card"
          @click="openMedicineDetail(medicine.id)"
        >
          <template #tags>
            <div class="medicine-tags">
              <span
                :class="[
                  medicine.medicineType === 2 ? 'neutral-tag' : '',
                  medicine.medicineType === 0 ? 'western-tag' : '',
                  medicine.medicineType === 1 ? 'chinese-medicine-tag' : '',
                ]"
                >{{ getMedicineTypeName(medicine.medicineType) }}</span
              >
              <span class="warning">是药三分毒，切忌不要乱吃药</span>
            </div>
            <div class="medicine-info">
              <p>
                <span style="color: black; font-weight: bold; font-size: 14px;">品牌:</span>
                <span class="primary">{{ medicine.medicineBrand }}</span>
              </p>
              <p>
                <span style="color: black; font-weight: bold; font-size: 14px;">用法:</span>
                <span class="warning">{{ medicine.usAge }}</span>
              </p>
              <p>
                <span style="color: black; font-weight: bold; font-size: 14px;">禁忌:</span>
                <span class="danger">{{ medicine.taboo }}</span>
              </p>
              <p>
                <span style="color: black; font-weight: bold; font-size: 14px;">相互作用:</span>
                <span class="success">{{ medicine.interaction }}</span>
              </p>
              <p>
                <span style="color: black; font-weight: bold; font-size: 14px;">价格:</span>
                <span class="price">{{ medicine.medicinePrice }} 元</span>
              </p>
            </div>
          </template>
        </van-card>
      </van-col>
    </van-row>

    <!-- 分页组件 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[9, 18, 27, 36]"
        @size-change="fetchMedicineData"
        @current-change="fetchMedicineData"
      >
        <template #prev>
          <span>上一页</span>
        </template>
        <template #next>
          <span>下一页</span>
        </template>
        <template #pager="{ page, isActive }">
          <span :class="{ active: isActive }">{{ page }}</span>
        </template>
      </el-pagination>
    </div>

    <!-- 药品详情模态框 -->
    <van-dialog
      v-model:show="showDetail"
      width="60%"
      class="detail-dialog"
      :show-cancel-button="false"
      :show-confirm-button="false"
      close-on-click-overlay
    >
      <!-- 标题栏：药品名称 + 关闭 -->
      <template #title>
        <div class="dialog-title">
          <span>{{ medicineDetail.medicineName }}</span>
          <van-icon name="cross" class="dialog-close" @click="showDetail = false" />
        </div>
      </template>

      <!-- 内容：图片 + 详细字段，滚动显示 -->
      <div class="detail-content">
        <img
          :src="formatImagePath(medicineDetail.imgPath)"
          alt="封面"
          class="detail-thumb"
        />
        <div class="info-block">
          <p><strong>关键词：</strong>{{ medicineDetail.keyword }}</p>
          <p><strong>功效：</strong>{{ medicineDetail.medicineEffect }}</p>
          <p><strong>品牌：</strong>{{ medicineDetail.medicineBrand }}</p>
          <p><strong>用法：</strong>{{ medicineDetail.usAge }}</p>
          <p><strong>禁忌：</strong>{{ medicineDetail.taboo }}</p>
          <p><strong>相互作用：</strong>{{ medicineDetail.interaction }}</p>
          <p><strong>价格：</strong>¥{{ medicineDetail.medicinePrice.toFixed(2) }}</p>
          <p><strong>创建时间：</strong>{{ medicineDetail.createTime }}</p>
          <p><strong>更新时间：</strong>{{ medicineDetail.updateTime }}</p>
        </div>
      </div>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import nav2 from '@/components/nav2.vue';
import { medicinePage, getmedicineInfo } from '@/api/admin/medicine.js';
import { ElMessage } from 'element-plus';

const currentPage = ref(1);
const pageSize = ref(9);
const total = ref(0);
const medicinePageData = ref([]);
const showDetail = ref(false);
const medicineDetail = ref({
  medicineName: '',
  keyword: '',
  medicineEffect: '',
  medicineBrand: '',
  interaction: '',
  taboo: '',
  usAge: '',
  medicinePrice: 0,
  createTime: '',
  updateTime: '',
});

const fetchMedicineData = () => {
  medicinePage({ pageNum: currentPage.value, pageSize: pageSize.value })
    .then((res) => {
      if (res.code === 200) {
        medicinePageData.value = res.data.data;
        total.value = res.data.total;
      } else {
        ElMessage.error(res.message || '请求失败，请稍后再试');
      }
    })
    .catch((err) => {
      console.error(err);
      ElMessage.error('请求失败，请稍后再试');
    });
};

fetchMedicineData();

// 根据medicineType获取药品类型名称
const getMedicineTypeName = (medicineType) => {
  switch (medicineType) {
    case 0:
      return '西药';
    case 1:
      return '中药';
    case 2:
      return '中成药';
    default:
      return '未知';
  }
};

// 格式化图片路径
const formatImagePath = (imgPath) => {
  return imgPath.replace(/[<>]/g, '');
};

// 打开药品详情
const openMedicineDetail = (medicineId) => {
  getmedicineInfo(medicineId)
    .then((res) => {
      if (res.code === 200) {
        medicineDetail.value = res.data;
        showDetail.value = true;
      } else {
        ElMessage.error(res.message || '获取药品详情失败');
      }
    })
    .catch((err) => {
      console.error(err);
      ElMessage.error('获取药品详情失败');
    });
};
</script>

<style scoped>
.medicine-list {
  margin: 20px;
  padding: 20px;
  background-color: #f5f7fa;
}

.medicine-container {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -10px;
}

.medicine-col {
  padding: 0 10px;
  margin-bottom: 20px;
}

.medicine-card {
  width: 100%;
  padding: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  transition: all 0.3s;
  cursor: pointer;
}

.medicine-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.neutral-tag {
  background-color: #e6a23c;
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  margin-right: 8px;
  font-size: 12px;
}

.western-tag {
  background-color: #409eff;
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  margin-right: 8px;
  font-size: 12px;
}

.chinese-medicine-tag {
  background-color: #67c23a;
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  margin-right: 8px;
  font-size: 12px;
}

.medicine-tags {
  margin-bottom: 10px;
}

.medicine-info {
  margin-top: 10px;
}

.medicine-info p {
  margin: 5px 0;
  font-size: 14px;
}

.warning {
  color: #f56c6c;
}

.primary {
  color: #409eff;
}

.success {
  color: #67c23a;
}

.danger {
  color: #f56c6c;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.el-pagination {
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-pagination ::v-deep(.el-pagination__total) {
  margin-right: 10px;
}

.el-pagination ::v-deep(.el-pagination__sizes) {
  margin: 0 10px;
}

.el-pagination ::v-deep(.el-pagination__jump) {
  margin-left: 10px;
}

.el-pagination ::v-deep(.el-pager) {
  display: flex;
}

.el-pagination ::v-deep(.el-pager li) {
  margin: 0 5px;
  width: 30px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  border-radius: 4px;
  background-color: #fff;
  border: 1px solid #dcdfe6;
  color: #606266;
  cursor: pointer;
  transition: all 0.3s;
}

.el-pagination ::v-deep(.el-pager li.active) {
  background-color: #409eff;
  color: #fff;
  border-color: #409eff;
}

.el-pagination ::v-deep(.el-pager li:hover) {
  color: #409eff;
}

/* 模态框样式 */
.detail-dialog {
  border-radius: 12px;
  overflow: hidden;
  padding: 0;
}

.dialog-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  padding: 12px 20px;
  border-bottom: 1px solid #ebeef5;
}

.dialog-close {
  font-size: 20px;
  cursor: pointer;
  color: #606266;
}

.detail-content {
  display: flex;
  padding: 20px;
  max-height: 60vh;
  overflow-y: auto;
}

.detail-thumb {
  width: 40%;
  border-radius: 4px;
  object-fit: cover;
}

.info-block {
  flex: 1;
  margin-left: 20px;
  font-size: 14px;
  color: #606266;
}

.info-block p {
  margin: 8px 0;
}

.info-block p strong {
  color: #303133;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}
</style>