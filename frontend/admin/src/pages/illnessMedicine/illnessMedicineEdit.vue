<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 疾病名称 -->
        <el-form-item label="疾病名称" prop="illnessId">
          <el-select v-model="form.illnessId" placeholder="请选择疾病">
            <el-option
              v-for="illness in illnessOptions"
              :key="illness.id"
              :label="illness.illnessName"
              :value="illness.id"
            />
          </el-select>
        </el-form-item>
        <!-- 药品名称 -->
        <el-form-item label="药品名称" prop="medicineId">
          <el-select v-model="form.medicineId" placeholder="请选择药品">
            <el-option
              v-for="medicine in medicineOptions"
              :key="medicine.id"
              :label="medicine.medicineName"
              :value="medicine.id"
            />
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
import { ref, reactive, getCurrentInstance, watch, onMounted } from 'vue';
import { illnessMedicineAdd, illnessMedicineUpdate, illnessMedicineUpdate_1, medicinePage, illnessPage } from '@/api/illnessMedicine.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（operationType: 'add' 或 'update'，以及疾病-药品ID）
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
  illnessId: '',
  medicineId: ''
});

// 疾病选项列表
const illnessOptions = ref([]);
// 药品选项列表
const medicineOptions = ref([]);

// 表单校验规则
const rules = {
  illnessId: [{ required: true, message: '疾病名称不能为空', trigger: 'submit' }],
  medicineId: [{ required: true, message: '药品名称不能为空', trigger: 'submit' }]
};

// 是否为添加操作
const isAddOperation = ref(false);

// 获取疾病列表
const getIllnessList = () => {
  illnessPage({ pageNum: 1, pageSize: 100 })
    .then(res => {
      if (res.code === 200) {
        illnessOptions.value = res.data.data;
      }
    })
    .catch(error => {
      console.error('获取疾病列表失败', error);
    });
};

// 获取药品列表
const getMedicineList = () => {
  medicinePage({ pageNum: 1, pageSize: 100 })
    .then(res => {
      if (res.code === 200) {
        medicineOptions.value = res.data.data;
      }
    })
    .catch(error => {
      console.error('获取药品列表失败', error);
    });
};

// 根据疾病-药品ID加载数据（编辑时调用）
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
      illnessMedicineAdd(form)
        .then(() => {
          ElMessage.success('新增疾病-药品成功');
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
      illnessMedicineUpdate(form)
        .then(() => {
          ElMessage.success('更新疾病-药品成功');
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
    loadIllnessMedicineInfo(newVal.id);
  } else {
    isAddOperation.value = true;
    // 重置表单
    form.id = null;
    form.illnessId = '';
    form.medicineId = '';
  }
}, { immediate: true, deep: true });

// 在组件挂载时获取疾病和药品列表
onMounted(() => {
  getIllnessList();
  getMedicineList();
});
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>