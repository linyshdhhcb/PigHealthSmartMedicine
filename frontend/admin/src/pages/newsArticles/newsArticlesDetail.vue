<!-- src/pages/newsArticles/newsArticlesDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 新闻标题 -->
        <el-form-item label="新闻标题">
          <el-input v-model="form.title" placeholder="请输入新闻标题" readonly />
        </el-form-item>
        <!-- 作者 -->
        <el-form-item label="作者">
          <el-input v-model="form.author" placeholder="请输入作者" readonly />
        </el-form-item>
        <!-- 新闻来源 -->
        <el-form-item label="新闻来源">
          <el-input v-model="form.source" placeholder="请输入新闻来源" readonly />
        </el-form-item>
        <!-- 新闻摘要 -->
        <el-form-item label="新闻摘要">
          <el-input v-model="form.summary" type="textarea" :rows="5" placeholder="请输入新闻摘要" readonly />
        </el-form-item>
        <!-- 发布时间 -->
        <el-form-item label="发布时间">
          <el-date-picker v-model="form.publishTime" type="datetime" placeholder="选择发布时间" readonly />
        </el-form-item>
        <!-- 转载URL -->
        <el-form-item label="转载URL">
          <el-input v-model="form.url" placeholder="请输入转载URL" readonly />
        </el-form-item>
        <!-- 新闻内容 -->
        <el-form-item label="新闻内容">
          <el-input v-model="form.content" type="textarea" :rows="10" placeholder="请输入新闻内容" readonly />
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
import { newsArticlesGetInfo } from '@/api/newsArticles.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（新闻资讯ID）
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
  title: '',
  author: '',
  source: '',
  summary: '',
  publishTime: null,
  url: '',
  content: ''
});

// 根据新闻资讯ID加载数据
const loadNewsInfo = (id) => {
  spinLoading.value = true;
  newsArticlesGetInfo(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
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

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    loadNewsInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.title = '';
    form.author = '';
    form.source = '';
    form.summary = '';
    form.publishTime = null;
    form.url = '';
    form.content = '';
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