<template>
  <el-card class="w-[100%]" :size="35" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="100px">
      <div class="flex flex-wrap">
        <!-- 文章标题 -->
        <el-form-item label="文章标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入文章标题" />
        </el-form-item>
        <!-- 作者 -->
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者" />
        </el-form-item>
        <!-- 文章内容（TinyMCE 富文本） -->
        <el-form-item label="文章内容" prop="content">
          <Editor
            v-model="form.content"
            :api-key="tinyApiKey"
            :init="editorOptions"
            class="w-full"
          />
        </el-form-item>
        <!-- 文章类型 -->
        <el-form-item label="文章类型" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择文章类型">
            <el-option
              v-for="item in articleTypes"
              :key="item.typeId"
              :label="item.typeName"
              :value="item.typeId"
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
import Editor from '@tinymce/tinymce-vue'; // 引入 TinyMCE 编辑器
import { articlesAdd, getArticleInfo, articlesUpdate, articleTypesPage } from '@/api/articles.js';
import { ElMessage } from 'element-plus';

// 配置 TinyMCE
const tinyApiKey = '902amriqsdr8l27hbbj3kq9m6j64jfroimothc6ag8dnepyp';
const editorOptions = reactive({
  menubar: false, // 隐藏菜单栏
  plugins: 'advlist autolink lists link image charmap preview anchor searchreplace visualblocks code fullscreen insertdatetime media table code help wordcount',
  toolbar: 'undo redo | blocks | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image media table | code fullscreen preview | forecolor backcolor | help',
  content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
  height: 500,
  images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {
    // 这里可以添加图片上传逻辑
    reject('需要配置图片上传服务');
  })
});

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（operationType: 'add' 或 'update'，以及文章ID）
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
  content: '',
  author: '',
  typeId: null // 默认类型
});

// 文章类型列表
const articleTypes = ref([]);

// 表单校验规则
const rules = {
  title: [{ required: true, message: '文章标题不能为空', trigger: 'submit' }],
  author: [{ required: true, message: '作者不能为空', trigger: 'submit' }],
  content: [{ required: true, message: '文章内容不能为空', trigger: 'submit' }],
  typeId: [{ required: true, message: '文章类型不能为空', trigger: 'submit' }]
};

// 是否为添加操作
const isAddOperation = ref(false);

// 确定按钮点击事件
const okBtnClick = () => {
  formRef.value.validate((valid) => {
    if (!valid) return;

    spinLoading.value = true;
    
    // 创建提交数据副本
    const submitData = { 
      ...form,
      content: form.content 
        ? form.content.trim() // 去除空白
        : '' 
    };

    // 检查真实内容是否为空（防止只有HTML标签的情况）
    const isEmptyContent = !submitData.content || 
      /^<p>\s*<\/p>$/i.test(submitData.content);

    if (isEmptyContent) {
      ElMessage.warning('文章内容不能为空');
      spinLoading.value = false;
      return;
    }

    // 使用 submitData 提交
    const apiPromise = isAddOperation.value 
      ? articlesAdd(submitData)
      : articlesUpdate(submitData);

    apiPromise
      .then(() => {
        ElMessage.success(isAddOperation.value ? '新增成功' : '更新成功');
        emits('ok');
      })
      .catch(error => {
        console.error('操作失败', error);
        ElMessage.error(error.message || '操作失败');
      })
      .finally(() => {
        spinLoading.value = false;
      });
  });
};

// 取消按钮点击事件
const cancelBtnClick = () => {
  emits('cancel');
};

// 获取文章类型列表
const getArticleTypes = () => {
  const params = {
    pageNum: 1,
    pageSize: 100 // 获取足够多的数据
  };
  articleTypesPage(params)
    .then(res => {
      if (res.code === 200) {
        articleTypes.value = res.data.data;
      }
    })
    .catch(error => {
      console.error('获取文章类型失败', error);
    });
};

// 根据文章ID加载数据（编辑时调用）
const loadArticleInfo = (id) => {
  spinLoading.value = true;
  getArticleInfo(id)
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
    loadArticleInfo(newVal.id);
  } else {
    isAddOperation.value = true;
    // 重置表单
    form.id = null;
    form.title = '';
    form.content = '';
    form.author = '';
    form.typeId = null;
  }
}, { immediate: true, deep: true });

// 在组件挂载时获取文章类型列表
onMounted(() => {
  getArticleTypes();
});
</script>

<style scoped>
/* 调整编辑器容器样式 */
:deep(.tox-tinymce) {
  border-radius: 4px;
  border: 1px solid #DCDFE6;
}
</style>