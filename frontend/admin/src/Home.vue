<template>
  <a-layout has-sider>
    <a-layout-sider
      v-model:collapsed="collapsed"
      :trigger="null"
      collapsible
      :style="{ overflow: 'auto', height: '100vh', position: 'fixed', left: 0, top: 0, bottom: 0 }"
    >
      <div class="logo" />
      <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="inline">
        <!-- 主页 -->
        <a-menu-item key="1">
          <HomeOutlined />
          <span class="nav-text">主页</span>
        </a-menu-item>

        <!-- 用户管理 -->
        <a-sub-menu key="sub1">
          <template #title>
            <span>
              <UserOutlined />
              <span>用户管理</span>
            </span>
          </template>
          <a-menu-item key="2">
            <router-link to="/StudentMgt">考生管理</router-link>
          </a-menu-item>
          <a-menu-item key="3">
            <router-link to="/admin-management">管理员管理</router-link>
          </a-menu-item>
          <a-menu-item key="4">
            <router-link to="/reviewer-management">评阅老师管理</router-link>
          </a-menu-item>
        </a-sub-menu>

        <!-- 试卷管理 -->
        <a-sub-menu key="sub2">
          <template #title>
            <span>
              <FileTextOutlined />
              <span>试卷管理</span>
            </span>
          </template>
          <a-menu-item key="5">
            <router-link to="/exam-list">试卷列表</router-link>
          </a-menu-item>
          <a-menu-item key="6">
            <router-link to="/exam-edit">试卷编辑</router-link>
          </a-menu-item>
          <a-menu-item key="7">
            <router-link to="/question-list">题目列表</router-link>
          </a-menu-item>
          <a-menu-item key="11">
            <router-link to="/question-edit">题目编辑</router-link>
          </a-menu-item>
        </a-sub-menu>

        <!-- 套餐管理 -->
        <a-sub-menu key="sub3">
          <template #title>
            <span>
              <GiftOutlined />
              <span>套餐管理</span>
            </span>
          </template>
          <a-menu-item key="8">
            <router-link to="/package-list">套餐列表</router-link>
          </a-menu-item>
        </a-sub-menu>

        <!-- 教育管理 -->
        <a-sub-menu key="sub4">
          <template #title>
            <span>
              <BookOutlined />
              <span>教育管理</span>
            </span>
          </template>
          <a-menu-item key="9">
            <router-link to="/subject-list">学科列表</router-link>
          </a-menu-item>
        </a-sub-menu>

        <!-- 成绩管理 -->
        <a-sub-menu key="sub5">
          <template #title>
            <span>
              <BarChartOutlined />
              <span>成绩管理</span>
            </span>
          </template>
          <a-menu-item key="10">
            <router-link to="/answer-sheet-list">答卷列表</router-link>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout :style="{ marginLeft: collapsed ? '80px' : '200px' }">
      <a-layout-header :style="{ background: '#fff', padding: 0 }">
        <MenuUnfoldOutlined
          v-if="collapsed"
          class="trigger"
          @click="() => (collapsed = !collapsed)"
        />
        <MenuFoldOutlined v-else class="trigger" @click="() => (collapsed = !collapsed)" />
      </a-layout-header>
      <a-layout-content :style="{ margin: '24px 16px 0', overflow: 'initial' }">
        <router-view></router-view>
      </a-layout-content>
      <a-layout-footer :style="{ textAlign: 'center' }">
        Copyright ©2019-2024 仲恺农业技术工程学院 版权所有
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script setup>
import { ref } from 'vue';


const selectedKeys = ref(['1']);
const collapsed = ref(false);
</script>

<style scoped>
#components-layout-demo-fixed-sider .logo {
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px;
}
.site-layout .site-layout-background {
  background: #fff;
}

[data-theme='dark'] .site-layout .site-layout-background {
  background: #141414;
}

.trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}

.trigger:hover {
  color: #1890ff;
}
</style>