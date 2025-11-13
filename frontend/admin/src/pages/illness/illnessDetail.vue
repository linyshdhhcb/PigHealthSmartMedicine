<!-- src/pages/illness/illnessDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候..." style="width: 100%;">
    <el-form :model="form" label-width="120px" style="width: 100%;">
      <div class="flex flex-wrap" style="width: 100%;">
        <!-- 疾病名称 -->
        <el-form-item label="疾病名称" style="width: 100%;">
          <el-input v-model="form.illnessName" placeholder="请输入疾病名称" readonly />
        </el-form-item>
        <!-- 诱发因素 -->
        <!-- <el-form-item label="诱发因素">
          <el-input v-model="form.includeReason" placeholder="请输入诱发因素" readonly />
        </el-form-item> -->
        <!-- 诱发因素 -->
        <el-form-item label="诱发因素" prop="includeReason">
          <Editor
            v-model="form.includeReason"
            :api-key="tinyApiKey"
            :init="editorOptions"
            class="w-full"
          />
        </el-form-item>
        <!-- 疾病症状 -->
        <!-- <el-form-item label="疾病症状">
          <el-input v-model="form.illnessSymptom" type="textarea" :rows="5" placeholder="请输入疾病症状" readonly />
        </el-form-item> -->
        <el-form-item label="疾病症状" prop="illnessSymptom">
          <Editor
            v-model="form.illnessSymptom"
            :api-key="tinyApiKey"
            :init="editorOptions"
          />
        </el-form-item>
        <!-- 特殊症状 -->
        <!-- <el-form-item label="特殊症状">
          <el-input v-model="form.specialSymptom" type="textarea" :rows="5" placeholder="请输入特殊症状" readonly />
        </el-form-item> -->
        <el-form-item label="特殊症状" prop="specialSymptom">
          <Editor
            v-model="form.specialSymptom"
            :api-key="tinyApiKey"
            :init="editorOptions"
            class="w-full"
          />
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
import Editor from '@tinymce/tinymce-vue'; // 引入 TinyMCE 编辑器
// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（疾病ID）
const props = defineProps({
  params: {
    type: Object,
    default: () => ({}),
  },
});


// 配置 TinyMCE
const tinyApiKey = '902amriqsdr8l27hbbj3kq9m6j64jfroimothc6ag8dnepyp';
const editorOptions = reactive({
  menubar: false, // 隐藏菜单栏
  plugins: 'advlist autolink lists link image charmap preview anchor searchreplace visualblocks code fullscreen insertdatetime media table code help wordcount',
  toolbar: 'undo redo | blocks | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image media table | code fullscreen preview | forecolor backcolor | help',
  content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
  height: 500,
  width: '80%',
  images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {
    // 这里可以添加图片上传逻辑
    reject('需要配置图片上传服务');
  })
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

.w-full{
  width: 100%;
}
</style>