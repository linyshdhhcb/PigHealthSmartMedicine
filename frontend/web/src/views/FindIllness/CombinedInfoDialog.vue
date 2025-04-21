<!-- 疾病与药品详情对话框 -->
<template>
  <el-dialog v-model="showDialog" title="疾病与药品详情" width="70%">
    <el-form :model="combinedForm" label-width="150px">
      <el-form-item label="疾病名称">
        <el-input v-model="combinedForm.illnessName" disabled></el-input>
      </el-form-item>
      <el-form-item label="疾病症状">
        <el-input v-model="combinedForm.illnessSymptom" type="textarea" :rows="2" disabled></el-input>
      </el-form-item>
      <el-form-item label="特殊症状">
        <el-input v-model="combinedForm.specialSymptom" type="textarea" :rows="2" disabled></el-input>
      </el-form-item>
      <el-form-item label="对症药品">
        <el-input v-model="combinedForm.medicineName" disabled></el-input>
      </el-form-item>
      <el-form-item label="作用">
        <el-input v-model="combinedForm.keyword" disabled></el-input>
      </el-form-item>
      <el-form-item label="药品功效">
        <el-input v-model="combinedForm.medicineEffect" type="textarea" :rows="2" disabled></el-input>
      </el-form-item>
      <el-form-item label="药品品牌">
        <el-input v-model="combinedForm.medicineBrand" disabled></el-input>
      </el-form-item>
      <el-form-item label="用法用量">
        <el-input v-model="combinedForm.usAge" type="textarea" :rows="2" disabled></el-input>
      </el-form-item>
      <el-form-item label="禁忌">
        <el-input v-model="combinedForm.taboo" type="textarea" :rows="2" disabled></el-input>
      </el-form-item>
      <el-form-item label="药品相互作用">
        <el-input v-model="combinedForm.interaction" type="textarea" :rows="2" disabled></el-input>
      </el-form-item>
      <el-form-item label="药品价格">
        <el-input-number v-model="combinedForm.medicinePrice" :precision="2" :step="0.01" disabled></el-input-number>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="closeDialog">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  combinedForm: {
    type: Object,
    default: () => ({})
  }
});

const emit = defineEmits(['update:show']);

const showDialog = ref(props.show);

watch(() => props.show, (newValue) => {
  showDialog.value = newValue;
});

watch(showDialog, (newValue) => {
  emit('update:show', newValue);
});

const closeDialog = () => {
  showDialog.value = false;
};
</script>