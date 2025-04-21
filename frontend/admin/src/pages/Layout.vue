<template>
  <el-container class="layout-container">
    <el-aside class="layout-aside" width="250px">
      <div class="logo">
        <i class="fas fa-piggy-bank fa-2x"></i>
        <span>Smart Pig</span>
      </div>
      <el-menu :default-active="activeMenu" class="el-menu-vertical-demo" @select="handleSelect">
        <el-menu-item index="/UserMgt">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/newsArticlesMgt">
          <el-icon><Document /></el-icon>
          <span>新闻信息管理</span>
        </el-menu-item>
        <el-menu-item index="/articleTypeMgt">
          <el-icon><Folder /></el-icon>
          <span>文章类型管理</span>
        </el-menu-item>
        <el-menu-item index="/ArticleMgt">
          <el-icon><Edit /></el-icon>
          <span>文章管理</span>
        </el-menu-item>
        <el-menu-item index="/filesMgt">
          <el-icon><FolderOpened /></el-icon>
          <span>文件信息管理</span>
        </el-menu-item>
        <el-menu-item index="/pageviewMgt">
          <el-icon><View /></el-icon>
          <span>浏览量管理</span>
        </el-menu-item>
        <el-menu-item index="/illnessKindMgt">
          <el-icon><FirstAidKit /></el-icon>
          <span>疾病分类管理</span>
        </el-menu-item>
        <el-menu-item index="/illnessMgt">
          <el-icon><Memo /></el-icon>
          <span>疾病管理</span>
        </el-menu-item>
        <el-menu-item index="/illnessMedicineMgt">
          <el-icon><Document /></el-icon>
          <span>疾病药品管理</span>
        </el-menu-item>
        <el-menu-item index="/medicineMgt">
          <el-icon><Document /></el-icon>
          <span>药品管理</span>
        </el-menu-item>
        <el-menu-item index="/FeedbackMgt">
          <el-icon><Message /></el-icon>
          <span>反馈管理</span>
        </el-menu-item>
        <el-menu-item index="/CoverSationMgt">
          <el-icon><ChatDotSquare /></el-icon>
          <span>对话管理</span>
        </el-menu-item>
        <el-menu-item index="/HistoryMgt">
          <el-icon><Clock /></el-icon>
          <span>搜索日志</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container class="layout-content">
      <el-header class="layout-header">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ activeMenuLabel }}</el-breadcrumb-item>
        </el-breadcrumb>
      </el-header>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, watch, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {
  User,
  Document,
  Folder,
  Edit,
  FolderOpened,
  View,
  FirstAidKit,
  Memo,
  Message,
  ChatDotSquare,
  Clock
} from '@element-plus/icons-vue';

const router = useRouter();
const route = useRoute();
const activeMenu = ref(route.path);
const activeMenuLabel = ref('');

const menuItems = [
  { path: '/UserMgt', label: '用户管理' },
  { path: '/newsArticlesMgt', label: '新闻信息管理' },
  { path: '/articleTypeMgt', label: '文章类型管理' },
  { path: '/ArticleMgt', label: '文章管理' },
  { path: '/filesMgt', label: '文件信息管理' },
  { path: '/pageviewMgt', label: '浏览量管理' },
  { path: '/illnessKindMgt', label: '疾病分类管理' },
  { path: '/illnessMgt', label: '疾病管理' },
  { path: '/illnessMedicineMgt', label: '疾病药品管理' },
  { path: '/medicineMgt', label: '药品管理' },
  { path: '/FeedbackMgt', label: '反馈管理' },
  { path: '/CoverSationMgt', label: '对话管理' },
  { path: '/HistoryMgt', label: '搜索日志' }
];

watch(
  () => route.path,
  (newPath) => {
    activeMenu.value = newPath;
    const item = menuItems.find(item => item.path === newPath);
    activeMenuLabel.value = item ? item.label : '';
  },
  { immediate: true }
);

const handleSelect = (index) => {
  router.push(index);
};
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  background: linear-gradient(135deg, #f3f4f6, #e5e7eb);
}

.layout-aside {
  background: linear-gradient(135deg, #3a3f47, #2c2f33);
  color: #fff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  padding: 20px;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.logo i {
  margin-right: 10px;
}

.el-menu-vertical-demo {
  background: transparent;
  border-right: none;
}

.el-menu-item {
  color: #fff;
  font-size: 14px;
}

.el-menu-item.is-active {
  background: #409eff;
  color: #fff;
  border-radius: 4px;
}

.layout-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.layout-header {
  background: #fff;
  padding: 10px 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.layout-main {
  flex: 1;
  padding: 20px;
  background: #fff;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  overflow: hidden;
}
</style>