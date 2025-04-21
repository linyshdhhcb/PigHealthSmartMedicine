<!-- src/pages/articleType/articleTypeEdit.vue -->
<template>
  <el-form :model="form" label-width="120px">
    <el-form-item label="文章类型名称">
      <el-input v-model="form.typeName" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">保存</el-button>
      <el-button @click="onCancel">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, reactive, watch } from 'vue';
import { articleTypesUpdate, getarticleTypesInfo } from '@/api/articleType.js';
import { ElMessage } from 'element-plus';

const props = defineProps({
  params: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['ok', 'cancel']);

const form = reactive({
  id: null,
  typeName: ''
});

// 定义 getArticleTypeInfo 函数
const getArticleTypeInfo = (id) => {
  getarticleTypesInfo(id)
    .then(response => {
      if (response.data) {
        form.typeName = response.data.typeName;
      } else {
        ElMessage.error('获取文章类型信息失败');
      }
    })
    .catch(error => {
      console.error('获取文章类型信息失败', error);
      ElMessage.error('获取文章类型信息失败');
    });
};

// 监听 params 变化
watch(
  () => props.params,
  (newParams) => {
    if (newParams.operationType === 'update') {
      console.log('newParams.id111', newParams.id);
      form.id = newParams.id;
      // 调用 getArticleTypeInfo 函数
      getArticleTypeInfo(newParams.id);
    } else {
      form.id = null;
      form.typeName = '';
    }
  },
  { immediate: true }
);

const onSubmit = () => {
  articleTypesUpdate(form)
    .then(response => {
      console.log('修改响应:', response); // 打印响应
      if (response.data) { // 假设响应包含 data 属性
        ElMessage.success('修改成功');
        emit('ok');
      } else {
        ElMessage.error(response.message || '修改失败');
      }
    })
    .catch(error => {
      console.error('修改操作失败', error);
      ElMessage.error('修改操作失败');
    });
};

const onCancel = () => {
  emit('cancel');
};
</script>

<style scoped>
/* 根据需要添加样式 */
</style>