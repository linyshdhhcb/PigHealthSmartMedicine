// import { createStore } from 'vuex';
import { illnessKindPage } from "@/api/admin/illnessKind.js";

import { createApp } from "vue";
import { createPinia } from "pinia";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import App from "./App.vue";
// import 'tailwindcss/tailwind.css'
import router from "./router";
import "vant/lib/index.css";
import Vant from "vant";
import { useUserStore } from "@/stores/user.js";
import zhCn from "element-plus/dist/locale/zh-cn.mjs"; // 导入 ElementPlus 组件库的中文语言包
import "@fortawesome/fontawesome-free/css/all.css";

//导入element plus 图标组件
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

const pinia = createPinia();
const app = createApp(App);

app.use(router);
app.use(pinia);
// 将 ElementPlus 插件注册到 Vue 应用中，并设置区域语言为中文简体
app.use(ElementPlus, {
  locale: zhCn,
});

// 从 localStorage 中恢复用户信息
const userStore = useUserStore();
const storedUserInfo = localStorage.getItem("userInfo");
if (storedUserInfo) {
  try {
    const userInfo = JSON.parse(storedUserInfo);
    userStore.setUserInfo(userInfo);
  } catch (error) {
    console.error("解析用户信息失败:", error);
    localStorage.removeItem("userInfo");
  }
}

// 注册所有图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

//全局注册 Vant UI 组件
app.use(Vant);

app.mount("#app");
