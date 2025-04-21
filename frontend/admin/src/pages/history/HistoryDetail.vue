<!-- src/pages/history/HistoryDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 用户ID -->
        <el-form-item label="用户ID">
          <el-input v-model="form.userId" placeholder="请输入用户ID" readonly />
        </el-form-item>
        <!-- 搜索关键字 -->
        <el-form-item label="搜索关键字">
          <el-input v-model="form.keyword" placeholder="请输入搜索关键字" readonly />
        </el-form-item>
        <!-- 操作类型 -->
        <el-form-item label="操作类型">
          <el-input v-model="operateTypeName" placeholder="操作类型" readonly />
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
import { historyUpdate_1 } from '@/api/history.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（操作日志ID）
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
  userId: '',
  keyword: '',
  operateType: null
});

// 操作类型名称
const operateTypeName = ref('');

// 根据操作日志ID加载数据
const loadHistoryInfo = (id) => {
  spinLoading.value = true;
  historyUpdate_1(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
        operateTypeName.value = getOperateTypeName(res.data.operateType);
      } else {
        ElMessage.error('获取操作日志信息失败');
      }
    })
    .catch(error => {
      console.error('获取操作日志信息失败', error);
      ElMessage.error('获取操作日志信息失败');
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 获取操作类型名称
const getOperateTypeName = (type) => {
  switch (type) {
    case 1:
      return '搜索';
    case 2:
      return '科目';
    case 3:
      return '药品';
    default:
      return '未知';
  }
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    loadHistoryInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.userId = '';
    form.keyword = '';
    form.operateType = null;
    operateTypeName.value = '';
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