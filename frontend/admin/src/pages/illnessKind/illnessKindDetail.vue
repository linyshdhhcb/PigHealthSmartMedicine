<!-- src/pages/illnessKind/illnessKindDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="100px">
      <div class="flex flex-wrap">
        <!-- 分类名称 -->
        <el-form-item label="分类名称">
          <el-input v-model="form.name" placeholder="请输入分类名称" readonly />
        </el-form-item>
        <!-- 描述 -->
        <el-form-item label="描述">
          <el-input v-model="form.info" type="textarea" :rows="10" placeholder="请输入描述" readonly />
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
import { getillnessKindInfo } from '@/api/illnessKind.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（疾病种类ID）
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
  name: '',
  info: ''
});

// 根据疾病种类ID加载数据
const loadIllnessKindInfo = (id) => {
  spinLoading.value = true;
  getillnessKindInfo(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
      }
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    loadIllnessKindInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.name = '';
    form.info = '';
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