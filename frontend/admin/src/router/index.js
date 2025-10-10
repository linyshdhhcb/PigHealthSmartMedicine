// src/router/index.js
import { createRouter, createWebHistory } from "vue-router";
import Login from "@/pages/Login.vue"; // 登录页面路由
import Layout from "@/pages/Layout.vue";

const routes = [
  { path: "/", redirect: "/login" }, // 默认路由重定向到登录页面
  { path: "/login", component: Login }, // 登录页面路由
  {
    path: "/",
    component: Layout,
    children: [
      // { path: '/user-mgt', component: () => import('../pages/user/UserMgt.vue') },
      { path: "/UserMgt", component: () => import("@/pages/user/UserMgt.vue") },
      {
        path: "/newsArticlesMgt",
        component: () => import("@/pages/newsArticles/newsArticlesMgt.vue"),
      },
      {
        path: "/articleTypeMgt",
        component: () => import("@/pages/articleType/articleTypeMgt.vue"),
      },
      {
        path: "/ArticleMgt",
        component: () => import("@/pages/Article/ArticleMgt.vue"),
      },
      {
        path: "/filesMgt",
        component: () => import("@/pages/files/filesMgt.vue"),
      },
      {
        path: "/pageviewMgt",
        component: () => import("@/pages/pageview/pageviewMgt.vue"),
      },
      {
        path: "/illnessKindMgt",
        component: () => import("@/pages/illnessKind/illnessKindMgt.vue"),
      },
      {
        path: "/illnessMgt",
        component: () => import("@/pages/illness/illnessMgt.vue"),
      },
      {
        path: "/illnessMedicineMgt",
        component: () =>
          import("@/pages/illnessMedicine/illnessMedicineMgt.vue"),
      },
      {
        path: "/medicineMgt",
        component: () => import("@/pages/medicine/medicineMgt.vue"),
      },
      {
        path: "/FeedbackMgt",
        component: () => import("@/pages/feedback/FeedbackMgt.vue"),
      },
      {
        path: "/CoverSationMgt",
        component: () => import("@/pages/conversation/CoverSationMgt.vue"),
      },
      {
        path: "/HistoryMgt",
        component: () => import("@/pages/history/HistoryMgt.vue"),
      },
      {
        path: "/KnowledgeMgt",
        component: () => import("@/pages/knowledge/KnowledgeMgt.vue"),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
