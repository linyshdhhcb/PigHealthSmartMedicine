<!-- src/pages/files/filesEdit.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 文件名 -->
        <el-form-item label="文件名" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件名" />
        </el-form-item>
        <!-- 文件类型 -->
        <el-form-item label="文件类型" prop="contentType">
          <el-input v-model="form.contentType" placeholder="请输入文件类型" />
        </el-form-item>
        <!-- 文件大小 -->
        <el-form-item label="文件大小" prop="fileSize">
          <el-input-number v-model="form.fileSize" :precision="0" :step="1" placeholder="请输入文件大小（字节）" />
        </el-form-item>
        <!-- 文件路径 -->
        <el-form-item label="文件路径" prop="filePath">
          <el-input v-model="form.filePath" placeholder="请输入文件路径" />
        </el-form-item>
        <!-- 文件URL -->
        <el-form-item label="文件URL" prop="url">
          <el-input v-model="form.url" placeholder="请输入文件URL" />
        </el-form-item>
        <!-- MinIO桶名称 -->
        <el-form-item label="MinIO桶名称" prop="bucketName">
          <el-input v-model="form.bucketName" placeholder="请输入MinIO桶名称" />
        </el-form-item>
      </div>
    </el-form>
    <el-divider class="mt-0" />
    <div class="flex justify-end">
      <el-space>
        <el-button @click="cancelBtnClick">取消</el-button>
        <el-button type="primary" @click="okBtnClick">确定</el-button>
      </el-space>
    </div>
  </el-card>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, watch } from 'vue';
import { filesAdd, filesUpdate, filesUpdate_1 } from '@/api/files.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（operationType: 'add' 或 'update'，以及文件ID）
const props = defineProps({
  params: {
    type: Object,
    default: () => ({}),
  },
});

// 父组件函数
const emits = defineEmits(['ok', 'cancel']);

// 加载中
const spinLoading = ref(false);
// 表单引用
const formRef = ref(null);
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

// 表单校验规则
const rules = {
  fileName: [{ required: true, message: '文件名不能为空', trigger: 'submit' }],
  contentType: [{ required: true, message: '文件类型不能为空', trigger: 'submit' }],
  fileSize: [{ required: true, message: '文件大小不能为空', trigger: 'submit' }],
  filePath: [{ required: true, message: '文件路径不能为空', trigger: 'submit' }],
  url: [{ required: true, message: '文件URL不能为空', trigger: 'submit' }],
  bucketName: [{ required: true, message: 'MinIO桶名称不能为空', trigger: 'submit' }]
};

// 是否为添加操作
const isAddOperation = ref(false);

// 根据文件ID加载数据（编辑时调用）
const loadFileInfo = (id) => {
  spinLoading.value = true;
  filesUpdate_1(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
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

// 确定按钮点击事件
const okBtnClick = () => {
  formRef.value.validate((valid) => {
    if (!valid) {
      console.log('表单验证失败');
      return;
    }
    console.log('表单验证通过');

    spinLoading.value = true;

    if (isAddOperation.value) {
      // 新增操作
      filesAdd(form)
        .then(() => {
          ElMessage.success('新增文件成功');
          emits('ok');
        })
        .catch(error => {
          console.error('新增操作失败', error);
          ElMessage.error('新增操作失败');
        })
        .finally(() => {
          spinLoading.value = false;
        });
    } else {
      // 更新操作
      filesUpdate(form)
        .then(() => {
          ElMessage.success('更新文件成功');
          emits('ok');
        })
        .catch(error => {
          console.error('更新操作失败', error);
          ElMessage.error('更新操作失败');
        })
        .finally(() => {
          spinLoading.value = false;
        });
    }
  });
};

// 取消按钮点击事件
const cancelBtnClick = () => {
  emits('cancel');
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    isAddOperation.value = false;
    loadFileInfo(newVal.id);
  } else {
    isAddOperation.value = true;
    // 重置表单
    form.id = null;
    form.fileName = '';
    form.contentType = '';
    form.fileSize = null;
    form.filePath = '';
    form.url = '';
    form.bucketName = '';
  }
}, { immediate: true, deep: true });
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>