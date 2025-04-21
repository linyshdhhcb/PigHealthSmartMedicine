<!-- src/pages/illnessMedicine/illnessMedicineDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 疾病ID -->
        <el-form-item label="疾病ID">
          <el-input v-model="form.illnessId" placeholder="请输入疾病ID" readonly />
        </el-form-item>
        <!-- 药品ID -->
        <el-form-item label="药品ID">
          <el-input v-model="form.medicineId" placeholder="请输入药品ID" readonly />
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
import { illnessMedicineUpdate_1 } from '@/api/illnessMedicine.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（疾病-药品ID）
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
  illnessId: '',
  medicineId: ''
});

// 根据疾病-药品ID加载数据
const loadIllnessMedicineInfo = (id) => {
  spinLoading.value = true;
  illnessMedicineUpdate_1(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
      } else {
        ElMessage.error('获取疾病-药品信息失败');
      }
    })
    .catch(error => {
      console.error('获取疾病-药品信息失败', error);
      ElMessage.error('获取疾病-药品信息失败');
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
    loadIllnessMedicineInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.illnessId = '';
    form.medicineId = '';
  }
}, { immediate: true, deep: true });
</script>

<style scoped>

</style>