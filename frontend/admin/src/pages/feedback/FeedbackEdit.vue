<!-- src/pages/feedback/FeedbackEdit.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 反馈用户 -->
        <el-form-item label="反馈用户" prop="name">
          <el-input v-model="form.name" placeholder="请输入反馈用户" />
        </el-form-item>
        <!-- 邮箱地址 -->
        <el-form-item label="邮箱地址" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱地址" />
        </el-form-item>
        <!-- 反馈标题 -->
        <el-form-item label="反馈标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入反馈标题" />
        </el-form-item>
        <!-- 反馈内容 -->
        <el-form-item label="反馈内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="5" placeholder="请输入反馈内容" />
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
import { feedbackAdd, feedbackUpdate, feedbackUpdate_1 } from '@/api/feedback.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（operationType: 'add' 或 'update'，以及反馈ID）
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
  name: '',
  email: '',
  title: '',
  content: ''
});

// 表单校验规则
const rules = {
  name: [{ required: true, message: '反馈用户不能为空', trigger: 'submit' }],
  email: [{ required: true, message: '邮箱地址不能为空', trigger: 'submit' }],
  title: [{ required: true, message: '反馈标题不能为空', trigger: 'submit' }],
  content: [{ required: true, message: '反馈内容不能为空', trigger: 'submit' }]
};

// 是否为添加操作
const isAddOperation = ref(false);

// 根据反馈ID加载数据（编辑时调用）
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
      feedbackAdd(form)
        .then(() => {
          ElMessage.success('新增反馈成功');
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
      feedbackUpdate(form)
        .then(() => {
          ElMessage.success('更新反馈成功');
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
    loadFeedbackInfo(newVal.id);
  } else {
    isAddOperation.value = true;
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