<!-- src/pages/history/HistoryEdit.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 用户ID -->
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <!-- 搜索关键字 -->
        <el-form-item label="搜索关键字" prop="keyword">
          <el-input v-model="form.keyword" placeholder="请输入搜索关键字" />
        </el-form-item>
        <!-- 操作类型 -->
        <el-form-item label="操作类型" prop="operateType">
          <el-select v-model="form.operateType" placeholder="请选择操作类型">
            <el-option label="搜索" value="1" />
            <el-option label="科目" value="2" />
            <el-option label="药品" value="3" />
          </el-select>
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
import { historyAdd, historyUpdate, historyUpdate_1 } from '@/api/history.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（operationType: 'add' 或 'update'，以及操作日志ID）
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
  keyword: '',
  operateType: null
});

// 表单校验规则
const rules = {
  userId: [{ required: true, message: '用户ID不能为空', trigger: 'submit' }],
  keyword: [{ required: true, message: '搜索关键字不能为空', trigger: 'submit' }],
  operateType: [{ required: true, message: '操作类型不能为空', trigger: 'submit' }]
};

// 是否为添加操作
const isAddOperation = ref(false);

// 根据操作日志ID加载数据（编辑时调用）
const loadHistoryInfo = (id) => {
  spinLoading.value = true;
  historyUpdate_1(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
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
      historyAdd(form)
        .then(() => {
          ElMessage.success('新增操作记录成功');
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
      historyUpdate(form)
        .then(() => {
          ElMessage.success('更新操作记录成功');
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
    loadHistoryInfo(newVal.id);
  } else {
    isAddOperation.value = true;
    // 重置表单
    form.id = null;
    form.userId = '';
    form.keyword = '';
    form.operateType = null;
  }
}, { immediate: true, deep: true });
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>