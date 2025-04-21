<!-- src/pages/files/filesDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 文件名 -->
        <el-form-item label="文件名">
          <el-input v-model="form.fileName" placeholder="请输入文件名" readonly />
        </el-form-item>
        <!-- 文件类型 -->
        <el-form-item label="文件类型">
          <el-input v-model="form.contentType" placeholder="请输入文件类型" readonly />
        </el-form-item>
        <!-- 文件大小 -->
        <el-form-item label="文件大小">
          <el-input v-model="formattedFileSize" placeholder="请输入文件大小（字节）" readonly />
        </el-form-item>
        <!-- 文件路径 -->
        <el-form-item label="文件路径">
          <el-input v-model="form.filePath" placeholder="请输入文件路径" readonly />
        </el-form-item>
        <!-- 文件URL -->
        <el-form-item label="文件URL">
          <el-link :href="form.url" target="_blank">{{ form.url }}</el-link>
        </el-form-item>
        <!-- MinIO桶名称 -->
        <el-form-item label="MinIO桶名称">
          <el-input v-model="form.bucketName" placeholder="请输入MinIO桶名称" readonly />
        </el-form-item>
      </div>
    </el-form>
    <el-divider class="mt-0" />
    <div class="flex justify-end">
      <el-space>
        <el-button @click="cancelBtnClick">关闭</el-button>
      </el-space>
    </div>
  </el-card>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, watch } from 'vue';
import { filesUpdate_1 } from '@/api/files.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（文件ID）
const props = defineProps({
  params: {
    type: Object,
    default: () => ({}),
  },
});

// 父组件函数
const emits = defineEmits(['cancel']);

// 加载中
const spinLoading = ref(false);
// 表单数据，初始化字段，可根据需要扩展
const form = reactive({
  id: null,
  fileName: '',
  contentType: '',
  fileSize: null,
  filePath: '',
  url: '',
  bucketName: ''
});

// 格式化文件大小
const formattedFileSize = ref('');

// 根据文件ID加载数据
const loadFileInfo = (id) => {
  spinLoading.value = true;
  filesUpdate_1(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
        formattedFileSize.value = formatFileSize(res.data.fileSize);
      } else {
        ElMessage.error('获取文件信息失败');
      }
    })
    .catch(error => {
      console.error('获取文件信息失败', error);
      ElMessage.error('获取文件信息失败');
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 格式化文件大小
const formatFileSize = (size) => {
  if (size < 1024) {
    return `${size} B`;
  } else if (size < 1024 * 1024) {
    return `${(size / 1024).toFixed(2)} KB`;
  } else if (size < 1024 * 1024 * 1024) {
    return `${(size / (1024 * 1024)).toFixed(2)} MB`;
  } else {
    return `${(size / (1024 * 1024 * 1024)).toFixed(2)} GB`;
  }
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    loadFileInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.fileName = '';
    form.contentType = '';
    form.fileSize = null;
    form.filePath = '';
    form.url = '';
    form.bucketName = '';
    formattedFileSize.value = '';
  }
}, { immediate: true, deep: true });

// 关闭按钮点击事件
const cancelBtnClick = () => {
  emits('cancel');
};
</script>

<style scoped>
/* 可根据需要添加样式 */
.ellipsis {
  display: inline-block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>