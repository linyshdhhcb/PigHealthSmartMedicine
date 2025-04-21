<!-- src/pages/illness/illnessDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 疾病名称 -->
        <el-form-item label="疾病名称">
          <el-input v-model="form.illnessName" placeholder="请输入疾病名称" readonly />
        </el-form-item>
        <!-- 诱发因素 -->
        <el-form-item label="诱发因素">
          <el-input v-model="form.includeReason" placeholder="请输入诱发因素" readonly />
        </el-form-item>
        <!-- 疾病症状 -->
        <el-form-item label="疾病症状">
          <el-input v-model="form.illnessSymptom" type="textarea" :rows="5" placeholder="请输入疾病症状" readonly />
        </el-form-item>
        <!-- 特殊症状 -->
        <el-form-item label="特殊症状">
          <el-input v-model="form.specialSymptom" type="textarea" :rows="5" placeholder="请输入特殊症状" readonly />
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
import { getillnessInfo } from '@/api/illness.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（疾病ID）
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
  illnessName: '',
  includeReason: '',
  illnessSymptom: '',
  specialSymptom: ''
});

// 根据疾病ID加载数据
const loadIllnessInfo = (id) => {
  spinLoading.value = true;
  getillnessInfo(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
      } else {
        ElMessage.error('获取疾病信息失败');
      }
    })
    .catch(error => {
      console.error('获取疾病信息失败', error);
      ElMessage.error('获取疾病信息失败');
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    loadIllnessInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.illnessName = '';
    form.includeReason = '';
    form.illnessSymptom = '';
    form.specialSymptom = '';
  }
}, { immediate: true, deep: true });

// 关闭按钮点击事件
const cancelBtnClick = () => {
  emits('cancel');
};
</script>

<style scoped>
/* 可根据需要添加样式 */
.ellipsis {
  display: inline-block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>