<!-- 疾病详细信息对话框 -->
<template>
  <van-dialog
    v-model:show="showDialog"
    width="80%"
    class="detail-dialog"
    :show-cancel-button="false"
    :show-confirm-button="false"
    close-on-click-overlay
  >
    <!-- 自定义标题栏 -->
    <template #title>
      <div class="dialog-title">
        <span>疾病详细信息</span>
        <van-icon name="cross" class="dialog-close" @click="closeDialog" />
      </div>
    </template>
    <!-- 详细内容 -->
    <div class="detail-content">
      <p><strong>疾病ID：</strong>{{ detailForm.id }}</p>
      <p><strong>疾病名称：</strong>{{ detailForm.illnessName }}</p>
      
      <!-- 诱发因素 -->
      <p><strong>诱发因素：</strong></p>
      <div v-html="detailForm.includeReason" class="detail-body"></div>
      
      <!-- 疾病症状 -->
      <p><strong>疾病症状：</strong></p>
      <div v-html="detailForm.illnessSymptom" class="detail-body"></div>
      
      <!-- 特殊症状 -->
      <p><strong>特殊症状：</strong></p>
      <div v-html="detailForm.specialSymptom" class="detail-body"></div>
      
      <p><strong>创建时间：</strong>{{ detailForm.createTime }}</p>
      <p><strong>更新时间：</strong>{{ detailForm.updateTime }}</p>
    </div>
  </van-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  detailForm: {
    type: Object,
    default: () => ({})
  }
});

const emit = defineEmits(['update:show']);

const showDialog = ref(props.show);

watch(() => props.show, (newValue) => {
  showDialog.value = newValue;
});

watch(showDialog, (newValue) => {
  emit('update:show', newValue);
});

const closeDialog = () => {
  showDialog.value = false;
};
</script>

<style scoped>
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
  max-height: 80vh;
}

.detail-content p {
  margin: 8px 0;
  color: #606266;
  line-height: 1.6;
}

.detail-body {
  margin-top: 15px;
  line-height: 1.8;
  overflow-x: auto;
}

.detail-body h3, .detail-body h4, .detail-body h5 {
  margin: 15px 0;
  padding-bottom: 5px;
  border-bottom: 1px solid #eee;
}

.detail-body ul, .detail-body ol {
  padding-left: 20px;
  margin: 10px 0;
}

.detail-body li {
  margin-bottom: 5px;
}

.detail-body p {
  margin: 8px 0;
}

.detail-body table {
  width: 100%;
  border-collapse: collapse;
  margin: 15px 0;
}

.detail-body th, .detail-body td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.detail-body th {
  background-color: #f5f7fa;
}
</style>