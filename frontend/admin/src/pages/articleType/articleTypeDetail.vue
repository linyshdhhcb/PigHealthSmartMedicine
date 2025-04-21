<!-- src/pages/articleType/articleTypeDetail.vue -->
<template>
  <el-card class="p-0" :body-style="{ height: 'calc(100vh - 125px)' }">
    <h1>文章类型详情</h1>

    <el-skeleton class="w-full" :size="35" :loading="spinLoading" tip="正在加载文章类型详情...">
      <el-row class="w-full h-full flex flex-col overflow-x-auto overflow-y-hidden">
        <el-row class="w-full">
          <el-descriptions title="文章类型详情" :column="1" border>
            <el-descriptions-item label="文章类型名称">
              {{ form.typeName }}
            </el-descriptions-item>
          </el-descriptions>
        </el-row>
      </el-row>
    </el-skeleton>
  </el-card>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, watch } from 'vue';
import { getarticleTypesInfo } from '@/api/articleType.js';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 加载状态
const spinLoading = ref(false);

// 接收父组件参数（例如：传入 articleTypeId）
const props = defineProps({
  params: {
    type: Object,
    default: () => ({}),
  }
});

// 表单数据（根据接口返回数据字段，可自行调整字段名称）
const form = reactive({
  id: null,
  typeName: ''
});

// 加载文章类型详细信息
const loadArticleTypeInfo = (id) => {
  spinLoading.value = true;
  getarticleTypesInfo(id)
    .then(res => {
      if (res) {
        // 将返回数据赋值到 form 中
        for (let key in res) {
          if (form.hasOwnProperty(key)) {
            form[key] = res[key];
          }
        }
        console.log('res', res);
      }
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.articleTypeId) {
    loadArticleTypeInfo(newVal.articleTypeId);
  }
}, { immediate: true, deep: true });
</script>

<style scoped>
/* 根据需要添加样式 */
</style>