<!-- src/pages/feedback/FeedbackDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 反馈用户 -->
        <el-form-item label="反馈用户">
          <el-input v-model="form.name" placeholder="请输入反馈用户" readonly />
        </el-form-item>
        <!-- 邮箱地址 -->
        <el-form-item label="邮箱地址">
          <el-input v-model="form.email" placeholder="请输入邮箱地址" readonly />
        </el-form-item>
        <!-- 反馈标题 -->
        <el-form-item label="反馈标题">
          <el-input v-model="form.title" placeholder="请输入反馈标题" readonly />
        </el-form-item>
        <!-- 反馈内容 -->
        <el-form-item label="反馈内容">
          <el-input v-model="form.content" type="textarea" :rows="5" placeholder="请输入反馈内容" readonly />
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
import { feedbackUpdate_1 } from '@/api/feedback.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（反馈ID）
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
  email: '',
  title: '',
  content: ''
});

// 根据反馈ID加载数据
const loadFeedbackInfo = (id) => {
  spinLoading.value = true;
  feedbackUpdate_1(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
      } else {
        ElMessage.error('获取反馈信息失败');
      }
    })
    .catch(error => {
      console.error('获取反馈信息失败', error);
      ElMessage.error('获取反馈信息失败');
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 关闭按钮点击事件
const cancelBtnClick = () => {
  emits('cancel');
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    loadFeedbackInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.name = '';
    form.email = '';
    form.title = '';
    form.content = '';
  }
}, { immediate: true, deep: true });
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>