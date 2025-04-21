import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { createPinia } from 'pinia';
import { useUserStore } from '@/stores/user.js';

// 整体导入 ElementPlus 组件库
import ElementPlus from 'element-plus'; // 导入 ElementPlus 组件库的所有模块和功能
import 'element-plus/dist/index.css'; // 导入 ElementPlus 组件库所需的全局 css 样式
import * as ElementPlusIconsVue from '@element-plus/icons-vue'; // 导入 ElementPlus 组件库中的所有图标
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'; // 导入 ElementPlus 组件库的中文语言包
import '@fortawesome/fontawesome-free/css/all.css';

const pinia = createPinia();
const app = createApp(App);

// 将 ElementPlus 插件注册到 Vue 应用中，并设置区域语言为中文简体
app.use(ElementPlus, {
  locale: zhCn
});

// 注册 ElementPlus 组件库中的所有图标到全局 Vue 应用中
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(pinia);
app.use(router);

// 从 localStorage 中恢复用户信息
const userStore = useUserStore();
const storedUserInfo = localStorage.getItem('userInfo');
if (storedUserInfo) {
  userStore.setUserInfo(JSON.parse(storedUserInfo));
}

app.mount('#app');
