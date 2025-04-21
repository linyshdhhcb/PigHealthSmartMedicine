<template>
  <div ref="navBarRef" :class="['main-nav', { 'visible': isNavVisible }]" style="width: 100%; height: 80px; background: rgb(255, 255, 255); display: flex; align-items: center; border-bottom: 1px solid #e8e8e8; transition: all 0.3s ease;">
    <div style="padding-left: 10px; width: 220px; height: 50px; align-items: center;">
      <i class="fas fa-piggy-bank fa-2x" style="color: #12b48b;"></i> <!-- 使用 Font Awesome 图标 -->
      <span style="margin-left: 10px; font-size: 20px; color: #12b48b;">Smart Pig</span>
    </div>
    <div style="flex: 1; display: flex; justify-content: center; align-items: center;">
      <ul style="list-style: none; margin: 0; padding: 0; display: flex;">
        <li v-for="(item, index) in navItems" :key="index" :class="{'active': activeIndex === index}">
          <router-link :to="navRoutes[index]" @click="handleNavClick(index)">
            {{ item }}
          </router-link>
        </li>
      </ul>
    </div>
    <div v-if="userStore.userInfo" style="display: flex; align-items: center;">
      <!-- 用户头像 -->
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="el-dropdown-link">
          <!-- <span>欢迎你：{{ userStore.userInfo.userName }}</span> -->
          <img :src="userStore.userInfo.imgPath" alt="User Avatar" style="width: 30px; height: 30px; border-radius: 50%; margin-right: 10px;">
          
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="myInfo"><i class="el-icon-user"></i> 用户中心</el-dropdown-item>
            <el-dropdown-item command="changePassword"><i class="el-icon-edit"></i> 修改密码</el-dropdown-item>
            <el-dropdown-item command="logout"><i class="el-icon-switch-button"></i> 退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <div v-else style="display: flex; align-items: center;">
      <el-button type="success" @click="handleLogin">登录</el-button>
      <router-link to="/register" style="margin-left: 10px;">
        <el-button type="success">注册</el-button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { ElMessageBox, ElMessage } from 'element-plus';
import { logout, savePassword } from '@/api/admin/user';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

// 导航菜单
const navItems = ['首页','新闻资讯','猪病百科', '猪猪药品', '智能兽医', '猪猪文章','搜索'];
const navRoutes = ['/home','/news','/findIllness', '/findMedicines', '/doctor', '/article','/serachBottom'];
const activeIndex = ref(0);
const navBarRef = ref(null);
const isNavVisible = ref(true);

// 根据当前路由设置 activeIndex
const setActiveIndexByRoute = () => {
  const currentPath = route.path;
  for (let i = 0; i < navRoutes.length; i++) {
    if (currentPath.startsWith(navRoutes[i])) {
      activeIndex.value = i;
      break;
    }
  }
};

// 监听路由变化，更新 activeIndex
watch(route, () => {
  setActiveIndexByRoute();
});

// 初始化时设置 activeIndex
onMounted(() => {
  setActiveIndexByRoute();
  window.addEventListener('scroll', handleScroll);
});

// 在组件销毁时移除事件监听
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});

// 处理导航点击事件
const handleNavClick = (index) => {
  activeIndex.value = index;
};

// 处理下拉菜单命令
const handleCommand = (command) => {
  switch (command) {
    case 'myInfo':
      goToMyInfo();
      break;
    case 'changePassword':
      changePassword();
      break;
    case 'logout':
      confirmLogout();
      break;
    default:
      break;
  }
};

// 跳转到用户中心
const goToMyInfo = () => {
  router.push('/user');
};

// 修改密码
const changePassword = () => {
  // 调用 savePassword 方法
  ElMessageBox.prompt('请输入旧密码', '修改密码', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'password',
    inputPlaceholder: '请输入旧密码',
    inputPattern: /[\S]{6,}/,
    inputErrorMessage: '密码长度至少为6位',
  })
    .then(({ value: oldPass }) => {
      ElMessageBox.prompt('请输入新密码', '修改密码', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'password',
        inputPlaceholder: '请输入新密码',
        inputPattern: /[\S]{6,}/,
        inputErrorMessage: '密码长度至少为6位',
      })
        .then(({ value: newPass }) => {
          savePassword(oldPass, newPass).then(() => {
            ElMessage.success('密码修改成功！');
          }).catch((error) => {
            ElMessage.error('密码修改失败，请重试');
          });
        })
        .catch(() => {});
    })
    .catch(() => {});
};

// 确认退出登录
const confirmLogout = () => {
  ElMessageBox.confirm('您确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      logout().then(() => {
        // 清除用户信息
        userStore.setUserInfo(null);
        localStorage.removeItem('userInfo');
        localStorage.removeItem('token');
        ElMessage.success('退出登录成功！');
        router.push('/login');
      }).catch((error) => {
        ElMessage.error('退出登录失败，请重试');
      });
    })
    .catch(() => {});
};

// 登录按钮点击事件
const handleLogin = () => {
  router.push('/login');
};

// 滚动事件处理函数
const handleScroll = () => {
  const scrollPosition = window.pageYOffset;
  // 当滚动超过20px时显示导航栏
  isNavVisible.value = scrollPosition > 0;
};

</script>

<style scoped>
.visible {
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 100;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.active {
  color: black;
  border-bottom: 2px solid #12b48b; /* 主题色下划线 */
}

li {
  color: black;
  font-size: 16px;
  padding: 10px;
  cursor: pointer;
}

a {
  text-decoration: none;
  color: inherit;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
}
</style>