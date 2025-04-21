<!-- src/pages/conversation/ConverSationEdit.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 用户ID -->
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <!-- 用户输入 -->
        <el-form-item label="用户输入" prop="userInput">
          <el-input v-model="form.userInput" placeholder="请输入用户输入" />
        </el-form-item>
        <!-- AI回复 -->
        <el-form-item label="AI回复" prop="aiResponse">
          <el-input v-model="form.aiResponse" type="textarea" :rows="5" placeholder="请输入AI回复" />
        </el-form-item>
        <!-- 对话时间 -->
        <el-form-item label="对话时间" prop="conversationTime">
          <el-date-picker v-model="form.conversationTime" type="datetime" placeholder="选择对话时间" />
        </el-form-item>
        <!-- AI模型名称 -->
        <el-form-item label="AI模型名称" prop="modelName">
          <el-input v-model="form.modelName" placeholder="请输入AI模型名称" />
        </el-form-item>
        <!-- AI响应时间（秒） -->
        <el-form-item label="AI响应时间（秒）" prop="responseTime">
          <el-input-number v-model="form.responseTime" :precision="2" :step="0.01" placeholder="请输入AI响应时间（秒）" />
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
import { conversationAdd, conversationUpdate, conversationUpdate_1 } from '@/api/conversation.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（operationType: 'add' 或 'update'，以及对话ID）
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
  userId: '',
  userInput: '',
  aiResponse: '',
  conversationTime: null,
  modelName: '',
  responseTime: null
});

// 表单校验规则
const rules = {
  userId: [{ required: true, message: '用户ID不能为空', trigger: 'submit' }],
  userInput: [{ required: true, message: '用户输入不能为空', trigger: 'submit' }],
  aiResponse: [{ required: true, message: 'AI回复不能为空', trigger: 'submit' }],
  conversationTime: [{ required: true, message: '对话时间不能为空', trigger: 'submit' }],
  modelName: [{ required: true, message: 'AI模型名称不能为空', trigger: 'submit' }],
  responseTime: [{ required: true, message: 'AI响应时间不能为空', trigger: 'submit' }]
};

// 是否为添加操作
const isAddOperation = ref(false);

// 根据对话ID加载数据（编辑时调用）
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
      conversationAdd(form)
        .then(() => {
          ElMessage.success('新增对话成功');
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
      conversationUpdate(form)
        .then(() => {
          ElMessage.success('更新对话成功');
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
    loadConversationInfo(newVal.id);
  } else {
    isAddOperation.value = true;
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
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>