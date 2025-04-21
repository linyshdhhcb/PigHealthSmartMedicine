<!-- src/pages/medicine/medicineDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 药品名称 -->
        <el-form-item label="药品名称">
          <el-input v-model="form.medicineName" placeholder="请输入药品名称" readonly />
        </el-form-item>
        <!-- 关键字 -->
        <el-form-item label="关键字">
          <el-input v-model="form.keyword" placeholder="请输入关键字" readonly />
        </el-form-item>
        <!-- 药品功效 -->
        <el-form-item label="药品功效">
          <el-input v-model="form.medicineEffect" type="textarea" :rows="5" placeholder="请输入药品功效" readonly />
        </el-form-item>
        <!-- 药品品牌 -->
        <el-form-item label="药品品牌">
          <el-input v-model="form.medicineBrand" placeholder="请输入药品品牌" readonly />
        </el-form-item>
        <!-- 药品类型 -->
        <el-form-item label="药品类型">
          <el-input v-model="medicineTypeName" placeholder="药品类型" readonly />
        </el-form-item>
        <!-- 相关图片路径 -->
        <el-form-item label="相关图片路径">
          <el-input v-model="form.imgPath" placeholder="请输入相关图片路径" readonly />
        </el-form-item>
        <!-- 用法用量 -->
        <el-form-item label="用法用量">
          <el-input v-model="form.usAge" type="textarea" :rows="5" placeholder="请输入用法用量" readonly />
        </el-form-item>
        <!-- 禁忌 -->
        <el-form-item label="禁忌">
          <el-input v-model="form.taboo" type="textarea" :rows="5" placeholder="请输入禁忌" readonly />
        </el-form-item>
        <!-- 药品相互作用 -->
        <el-form-item label="药品相互作用">
          <el-input v-model="form.interaction" type="textarea" :rows="5" placeholder="请输入药品相互作用" readonly />
        </el-form-item>
        <!-- 药品价格 -->
        <el-form-item label="药品价格">
          <el-input-number v-model="form.medicinePrice" :precision="2" :step="0.01" placeholder="请输入药品价格" readonly />
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
import { medicineUpdate_1 } from '@/api/medicine.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（药品ID）
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
  medicineName: '',
  keyword: '',
  medicineEffect: '',
  medicineBrand: '',
  medicineType: null,
  imgPath: '',
  usAge: '',
  taboo: '',
  interaction: '',
  medicinePrice: null
});

// 药品类型名称
const medicineTypeName = ref('');

// 根据药品ID加载数据
const loadMedicineInfo = (id) => {
  spinLoading.value = true;
  medicineUpdate_1(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
        medicineTypeName.value = getMedicineTypeName(res.data.medicineType);
      } else {
        ElMessage.error('获取药品信息失败');
      }
    })
    .catch(error => {
      console.error('获取药品信息失败', error);
      ElMessage.error('获取药品信息失败');
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 获取药品类型名称
const getMedicineTypeName = (type) => {
  switch (type) {
    case '0':
      return '西药';
    case '1':
      return '中药';
    case '2':
      return '中成药';
    default:
      return '未知';
  }
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    loadMedicineInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.medicineName = '';
    form.keyword = '';
    form.medicineEffect = '';
    form.medicineBrand = '';
    form.medicineType = null;
    form.imgPath = '';
    form.usAge = '';
    form.taboo = '';
    form.interaction = '';
    form.medicinePrice = null;
    medicineTypeName.value = '';
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