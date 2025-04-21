<!-- src/pages/pageview/pageviewDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 浏览量 -->
        <el-form-item label="浏览量">
          <el-input-number v-model="form.pageviews" :precision="0" :step="1" placeholder="请输入浏览量" readonly />
        </el-form-item>
        <!-- 病的ID -->
        <el-form-item label="病的ID">
          <el-input v-model="form.illnessId" placeholder="请输入病的ID" readonly />
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
import { pageviewUpdate_1 } from '@/api/pageview.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（浏览量ID）
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
  pageviews: null,
  illnessId: null
});

// 根据浏览量ID加载数据
const loadPageviewInfo = (id) => {
  spinLoading.value = true;
  pageviewUpdate_1(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
      } else {
        ElMessage.error('获取浏览量信息失败');
      }
    })
    .catch(error => {
      console.error('获取浏览量信息失败', error);
      ElMessage.error('获取浏览量信息失败');
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    loadPageviewInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.pageviews = null;
    form.illnessId = null;
  }
}, { immediate: true, deep: true });

// 关闭按钮点击事件
const cancelBtnClick = () => {
  emits('cancel');
};
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>