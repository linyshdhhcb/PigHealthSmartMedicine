<!-- src/pages/articleType/articleTypeDetail.vue -->
<template>
  <el-card class="p-6 rounded-2xl shadow-md bg-gradient-to-br from-white to-gray-50">
    <h2 class="text-xl font-semibold mb-4 text-gray-700">文章类型详情</h2>
    <el-skeleton :loading="loading" animated>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="类型 ID">{{ form.typeId }}</el-descriptions-item>
        <el-descriptions-item label="类型名称">{{ form.typeName }}</el-descriptions-item>
      </el-descriptions>
    </el-skeleton>
  </el-card>
</template>

<script setup>
import { ref, reactive, watch } from 'vue';
import { getarticleTypesInfo } from '@/api/articleType.js';
import { ElMessage } from 'element-plus';

const props = defineProps({ params: Object });
const loading = ref(false);
const form = reactive({ id: '', typeName: '' });

const loadInfo = async (id) => {
  loading.value = true;
  try {
    const res = await getarticleTypesInfo(id);
    if (res.code === 200 && res.data) Object.assign(form, res.data);
    else ElMessage.error('获取详情失败');
  } finally {
    loading.value = false;
  }
};

watch(() => props.params, (val) => {
  if (val?.id) loadInfo(val.id);
}, { immediate: true });
</script>

<style scoped>
.el-card {
  transition: all 0.3s ease;
}
.el-card:hover {
  box-shadow: 0 6px 20px rgba(0,0,0,0.1);
}
</style>