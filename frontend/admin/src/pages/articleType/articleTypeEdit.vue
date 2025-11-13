<!-- src/pages/articleType/articleTypeEdit.vue -->
<template>
  <el-card class="p-4 rounded-2xl shadow-md bg-gradient-to-br from-white to-gray-50">
    <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
      <el-form-item label="文章类型名称" prop="typeName">
        <el-input v-model="form.typeName" placeholder="请输入文章类型名称" clearable />
      </el-form-item>
      <el-form-item class="flex justify-end">
        <el-button type="primary" @click="onSubmit">保存</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, reactive, watch } from 'vue';
import { articleTypesAdd, articleTypesUpdate, getarticleTypesInfo } from '@/api/articleType.js';
import { ElMessage } from 'element-plus';

const props = defineProps({ params: Object });
const emit = defineEmits(['ok', 'cancel']);
const formRef = ref(null);

const form = reactive({ id: null, typeName: '' });
const rules = {
  typeName: [{ required: true, message: '请输入文章类型名称', trigger: 'blur' }]
};

// 加载数据
const loadInfo = async (id) => {
  const res = await getarticleTypesInfo(id);
  if (res.code === 200 && res.data) form.typeName = res.data.typeName;
  else ElMessage.error('获取文章类型信息失败');
};

watch(() => props.params, async (val) => {
  if (!val) return;
  if (val.operationType === 'update' && val.id) {
    form.id = val.id;
    await loadInfo(val.id);
  } else {
    form.id = null;
    form.typeName = '';
  }
}, { immediate: true });

// 保存
const onSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    const api = props.params.operationType === 'add' ? articleTypesAdd : articleTypesUpdate;
    const res = await api(form);
    if (res.code === 200) {
      ElMessage.success(props.params.operationType === 'add' ? '添加成功' : '修改成功');
      emit('ok');
    } else {
      ElMessage.error(res.message || '操作失败');
    }
  });
};

const onCancel = () => emit('cancel');
</script>

<style scoped>
.el-card {
  transition: all 0.3s ease;
}
.el-card:hover {
  box-shadow: 0 6px 20px rgba(0,0,0,0.1);
}
</style>