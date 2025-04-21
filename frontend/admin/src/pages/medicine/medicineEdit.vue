<!-- src/pages/medicine/medicineEdit.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 药品名称 -->
        <el-form-item label="药品名称" prop="medicineName">
          <el-input v-model="form.medicineName" placeholder="请输入药品名称" />
        </el-form-item>
        <!-- 关键字 -->
        <el-form-item label="关键字" prop="keyword">
          <el-input v-model="form.keyword" placeholder="请输入关键字" />
        </el-form-item>
        <!-- 药品功效 -->
        <el-form-item label="药品功效" prop="medicineEffect">
          <el-input v-model="form.medicineEffect" type="textarea" :rows="5" placeholder="请输入药品功效" />
        </el-form-item>
        <!-- 药品品牌 -->
        <el-form-item label="药品品牌" prop="medicineBrand">
          <el-input v-model="form.medicineBrand" placeholder="请输入药品品牌" />
        </el-form-item>
        <!-- 药品类型 -->
        <el-form-item label="药品类型" prop="medicineType">
          <el-select v-model="form.medicineType" placeholder="请选择药品类型">
            <el-option label="西药" value="0" />
            <el-option label="中药" value="1" />
            <el-option label="中成药" value="2" />
          </el-select>
        </el-form-item>
        <!-- 相关图片路径 -->
        <el-form-item label="相关图片路径" prop="imgPath">
          <el-input v-model="form.imgPath" placeholder="请输入相关图片路径" />
        </el-form-item>
        <!-- 用法用量 -->
        <el-form-item label="用法用量" prop="usAge">
          <el-input v-model="form.usAge" type="textarea" :rows="5" placeholder="请输入用法用量" />
        </el-form-item>
        <!-- 禁忌 -->
        <el-form-item label="禁忌" prop="taboo">
          <el-input v-model="form.taboo" type="textarea" :rows="5" placeholder="请输入禁忌" />
        </el-form-item>
        <!-- 药品相互作用 -->
        <el-form-item label="药品相互作用" prop="interaction">
          <el-input v-model="form.interaction" type="textarea" :rows="5" placeholder="请输入药品相互作用" />
        </el-form-item>
        <!-- 药品价格 -->
        <el-form-item label="药品价格" prop="medicinePrice">
          <el-input-number v-model="form.medicinePrice" :precision="2" :step="0.01" placeholder="请输入药品价格" />
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
import { medicineAdd, medicineUpdate, medicineUpdate_1 } from '@/api/medicine.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（operationType: 'add' 或 'update'，以及药品ID）
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

// 表单校验规则
const rules = {
  medicineName: [{ required: true, message: '药品名称不能为空', trigger: 'submit' }],
  keyword: [{ required: true, message: '关键字不能为空', trigger: 'submit' }],
  medicineEffect: [{ required: true, message: '药品功效不能为空', trigger: 'submit' }],
  medicineBrand: [{ required: true, message: '药品品牌不能为空', trigger: 'submit' }],
  medicineType: [{ required: true, message: '药品类型不能为空', trigger: 'submit' }],
  usAge: [{ required: true, message: '用法用量不能为空', trigger: 'submit' }],
  taboo: [{ required: true, message: '禁忌不能为空', trigger: 'submit' }],
  interaction: [{ required: true, message: '药品相互作用不能为空', trigger: 'submit' }],
  medicinePrice: [{ required: true, message: '药品价格不能为空', trigger: 'submit' }]
};

// 是否为添加操作
const isAddOperation = ref(false);

// 根据药品ID加载数据（编辑时调用）
const loadMedicineInfo = (id) => {
  spinLoading.value = true;
  medicineUpdate_1(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
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
      medicineAdd(form)
        .then(() => {
          ElMessage.success('新增药品成功');
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
      medicineUpdate(form)
        .then(() => {
          ElMessage.success('更新药品成功');
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
    loadMedicineInfo(newVal.id);
  } else {
    isAddOperation.value = true;
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
  }
}, { immediate: true, deep: true });
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>