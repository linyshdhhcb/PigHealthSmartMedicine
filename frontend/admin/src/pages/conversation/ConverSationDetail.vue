<!-- src/pages/conversation/ConverSationDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 用户ID -->
        <el-form-item label="用户ID">
          <el-input v-model="form.userId" placeholder="请输入用户ID" readonly />
        </el-form-item>
        <!-- 用户输入 -->
        <el-form-item label="用户输入">
          <el-input v-model="form.userInput" placeholder="请输入用户输入" readonly />
        </el-form-item>
        <!-- AI回复 -->
        <el-form-item label="AI回复">
          <el-input v-model="form.aiResponse" type="textarea" :rows="5" placeholder="请输入AI回复" readonly />
        </el-form-item>
        <!-- 对话时间 -->
        <el-form-item label="对话时间">
          <el-date-picker v-model="form.conversationTime" type="datetime" placeholder="选择对话时间" readonly />
        </el-form-item>
        <!-- AI模型名称 -->
        <el-form-item label="AI模型名称">
          <el-input v-model="form.modelName" placeholder="请输入AI模型名称" readonly />
        </el-form-item>
        <!-- AI响应时间（秒） -->
        <el-form-item label="AI响应时间（秒）">
          <el-input-number v-model="form.responseTime" :precision="2" :step="0.01" placeholder="请输入AI响应时间（秒）" readonly />
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
import { conversationUpdate_1 } from '@/api/conversation.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（对话ID）
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
  userInput: '',
  aiResponse: '',
  conversationTime: null,
  modelName: '',
  responseTime: null
});

// 根据对话ID加载数据
const loadConversationInfo = (id) => {
  spinLoading.value = true;
  conversationUpdate_1(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
      } else {
        ElMessage.error('获取对话信息失败');
      }
    })
    .catch(error => {
      console.error('获取对话信息失败', error);
      ElMessage.error('获取对话信息失败');
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    loadConversationInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.userId = '';
    form.userInput = '';
    form.aiResponse = '';
    form.conversationTime = null;
    form.modelName = '';
    form.responseTime = null;
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