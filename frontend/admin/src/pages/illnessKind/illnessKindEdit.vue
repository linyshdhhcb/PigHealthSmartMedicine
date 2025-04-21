<!-- src/pages/illnessKind/illnessKindEdit.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="100px">
      <div class="flex flex-wrap">
        <!-- 分类名称 -->
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <!-- 描述 -->
        <el-form-item label="描述" prop="info">
          <el-input v-model="form.info" type="textarea" :rows="10" placeholder="请输入描述" />
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
import { illnessKindAdd, getillnessKindInfo, illnessKindUpdate } from '@/api/illnessKind.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（operationType: 'add' 或 'update'，以及疾病种类ID）
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
  info: ''
});

// 表单校验规则
const rules = {
  name: [{ required: true, message: '分类名称不能为空', trigger: 'submit' }],
  info: [{ required: true, message: '描述不能为空', trigger: 'submit' }]
};

// 是否为添加操作
const isAddOperation = ref(false);

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
      illnessKindAdd(form)
        .then(() => {
          ElMessage.success('新增疾病种类成功');
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
      illnessKindUpdate(form)
        .then(() => {
          ElMessage.success('更新疾病种类成功');
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

// 根据疾病种类ID加载数据（编辑时调用）
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
    isAddOperation.value = false;
    loadIllnessKindInfo(newVal.id);
  } else {
    isAddOperation.value = true;
    // 重置表单
    form.id = null;
    form.name = '';
    form.info = '';
  }
}, { immediate: true, deep: true });
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>