<template>
  <van-dialog
    v-model:show="showDialog"
    width="95%"
    class="detail-dialog"
    :show-cancel-button="false"
    :show-confirm-button="false"
    close-on-click-overlay
  >
    <!-- 自定义标题栏 -->
    <template #title>
      <div class="dialog-title">
        <h1 class="title-main">{{ detailForm.illnessName }}</h1>
        <p class="title-time">{{ formatTime(detailForm.createTime) }}</p>
        <van-icon name="cross" class="dialog-close" @click="closeDialog" />
      </div>
    </template>
    <!-- 详细内容 -->
    <div class="detail-content">
      <!-- 诱发因素 -->
      <h5 class="subtitle">诱发因素</h5>
      <div v-html="detailForm.includeReason" class="detail-body"></div>
      
      <!-- 疾病症状 -->
      <h5 class="subtitle">疾病症状</h5>
      <div v-html="detailForm.illnessSymptom" class="detail-body"></div>
      
      <!-- 特殊症状 -->
      <h5 class="subtitle">特殊症状</h5>
      <div v-html="detailForm.specialSymptom" class="detail-body"></div>
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

// 格式化时间
const formatTime = (timeStr) => {
  return timeStr.replace(' ', '   ');
};
</script>

<style scoped>
.detail-dialog .van-dialog__body {
  padding: 0;
}

.dialog-title {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
  background-color: #f5f7fa;
  position: relative;
}

.dialog-close {
  position: absolute;
  top: 15px;
  right: 15px;
  font-size: 20px;
  cursor: pointer;
  color: #606266;
}

.title-main {
  font-size: 22px;
  font-weight: bold;
  margin: 0;
  text-align: center;
}

.title-time {
  font-size: 14px;
  color: #999;
  margin: 5px 0 0;
  text-align: right;
  width: 100%;
}

.detail-content {
  padding: 20px;
  overflow-y: auto;
  max-height: 80vh;
}

.subtitle {
  font-size: 18px;
  font-weight: bold;
  margin: 20px 0 10px;
  text-align: center;
  padding-bottom: 8px;
  border-bottom: 2px solid #eee;
}

.detail-body {
  margin-top: 10px;
  line-height: 1.8;
  overflow-x: auto;
}

.detail-body h5, 
.detail-body h4, 
.detail-body h5 {
  margin: 15px 0;
  padding-bottom: 5px;
  border-bottom: 1px solid #eee;
}

.detail-body ul,
.detail-body ol {
  padding-left: 20px;
  margin: 10px 0;
}

.detail-body li {
  margin-bottom: 8px;
}

.detail-body p {
  margin: 8px 0;
  text-align: center;
}

.detail-body table {
  width: 100%;
  border-collapse: collapse;
  margin: 15px 0;
}

.detail-body th,
.detail-body td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.detail-body th {
  background-color: #f5f7fa;
}
</style>