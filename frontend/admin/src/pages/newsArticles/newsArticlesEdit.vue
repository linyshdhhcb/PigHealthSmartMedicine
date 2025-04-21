<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 新闻标题 -->
        <el-form-item label="新闻标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入新闻标题" />
        </el-form-item>
        <!-- 作者 -->
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者" />
        </el-form-item>
        <!-- 新闻来源 -->
        <el-form-item label="新闻来源" prop="source">
          <el-input v-model="form.source" placeholder="请输入新闻来源" />
        </el-form-item>
        <!-- 新闻摘要（富文本） -->
        <el-form-item label="新闻摘要" prop="summary">
          <Editor
            v-model="form.summary"
            :api-key="tinyApiKey"
            :init="summaryEditorOptions"
            class="w-full"
          />
        </el-form-item>
        <!-- 发布时间 -->
        <el-form-item label="发布时间" prop="publishTime">
          <el-date-picker v-model="form.publishTime" type="datetime" placeholder="选择发布时间" />
        </el-form-item>
        <!-- 转载URL -->
        <el-form-item label="转载URL" prop="url">
          <el-input v-model="form.url" placeholder="请输入转载URL" />
        </el-form-item>
        <!-- 新闻内容（富文本） -->
        <el-form-item label="新闻内容" prop="content">
          <Editor
            v-model="form.content"
            :api-key="tinyApiKey"
            :init="contentEditorOptions"
            class="w-full"
          />
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
import Editor from '@tinymce/tinymce-vue'; // 引入 TinyMCE 编辑器
import { newsArticlesAdd, newsArticlesUpdate, newsArticlesGetInfo } from '@/api/newsArticles.js';
import { ElMessage } from 'element-plus';
import dayjs from 'dayjs';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（operationType: 'add' 或 'update'，以及新闻资讯ID）
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
  title: '',
  author: '',
  source: '',
  summary: '',
  publishTime: null,
  url: '',
  content: ''
});

// 表单校验规则
const rules = {
  title: [{ required: true, message: '新闻标题不能为空', trigger: 'submit' }],
  author: [{ required: true, message: '作者不能为空', trigger: 'submit' }],
  source: [{ required: true, message: '新闻来源不能为空', trigger: 'submit' }],
  summary: [{ required: true, message: '新闻摘要不能为空', trigger: 'submit' }],
  publishTime: [{ required: true, message: '发布时间不能为空', trigger: 'submit' }],
  url: [{ required: true, message: '转载URL不能为空', trigger: 'submit' }],
  content: [{ required: true, message: '新闻内容不能为空', trigger: 'submit' }]
};

// TinyMCE 配置
const tinyApiKey = '902amriqsdr8l27hbbj3kq9m6j64jfroimothc6ag8dnepyp';

// 新闻摘要编辑器配置
const summaryEditorOptions = reactive({
  menubar: false, // 隐藏菜单栏
  plugins: 'advlist autolink lists link image charmap preview anchor searchreplace visualblocks code fullscreen insertdatetime media table code help wordcount',
  toolbar: 'undo redo | bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image media | code fullscreen preview | help',
  content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
  height: 200,
  images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {
    // 这里可以添加图片上传逻辑
    reject('需要配置图片上传服务');
  })
});

// 新闻内容编辑器配置
const contentEditorOptions = reactive({
  menubar: false, // 隐藏菜单栏
  plugins: 'advlist autolink lists link image charmap preview anchor searchreplace visualblocks code fullscreen insertdatetime media table code help wordcount',
  toolbar: 'undo redo | blocks | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image media table | code fullscreen preview | forecolor backcolor | help',
  content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
  height: 400,
  images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {
    // 这里可以添加图片上传逻辑
    reject('需要配置图片上传服务');
  })
});

// 是否为添加操作
const isAddOperation = ref(false);

// 根据新闻资讯ID加载数据（编辑时调用）
const loadNewsInfo = (id) => {
  spinLoading.value = true;
  newsArticlesGetInfo(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
        // 格式化时间publishTime
        form.publishTime = dayjs(res.data.publishTime).format('YYYY-MM-DD HH:mm:ss');
      } else {
        ElMessage.error('获取新闻资讯信息失败');
      }
    })
    .catch(error => {
      console.error('获取新闻资讯信息失败', error);
      ElMessage.error('获取新闻资讯信息失败');
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

    // 格式化 publishTime
    form.publishTime = dayjs(form.publishTime).format('YYYY-MM-DD HH:mm:ss');

    if (isAddOperation.value) {
      // 新增操作
      newsArticlesAdd(form)
        .then(() => {
          ElMessage.success('新增新闻资讯成功');
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
      newsArticlesUpdate(form)
        .then(() => {
          ElMessage.success('更新新闻资讯成功');
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
    loadNewsInfo(newVal.id);
  } else {
    isAddOperation.value = true;
    // 重置表单
    form.id = null;
    form.title = '';
    form.author = '';
    form.source = '';
    form.summary = '';
    form.publishTime = dayjs().format('YYYY-MM-DD HH:mm:ss'); // 设置当前时间为默认发布时间
    form.url = '';
    form.content = '';
  }
}, { immediate: true, deep: true });
</script>

<style scoped>
/* 调整编辑器容器样式 */
:deep(.tox-tinymce) {
  border-radius: 4px;
  border: 1px solid #DCDFE6;
}
</style>