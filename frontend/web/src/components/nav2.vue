<template>
  <div class="main-nav" ref="navBarRef">
    <!-- Logo区域 -->
    <div class="logo-box">
      <i class="fas fa-piggy-bank fa-2x logo-icon"></i>
      <span class="logo-text">Smart Pig</span>
    </div>

    <!-- 导航项 -->
    <ul class="nav-list">
      <li
        v-for="(item, index) in navItems"
        :key="index"
        :class="{ active: activeIndex === index }"
        @click="handleNavClick(index)"
      >
        <router-link :to="navRoutes[index]">{{ item }}</router-link>
      </li>
    </ul>

    <!-- 登录状态 -->
    <div v-if="userStore.isLoggedIn" class="user-section">
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="el-dropdown-link">
          <span class="user-name">欢迎你：{{ userStore.userInfo.userName || userStore.userInfo.userAccount }}</span>
          <img
            v-if="userStore.userInfo.imgPath"
            :src="userStore.userInfo.imgPath"
            class="user-avatar"
            alt="User Avatar"
          />
          <i v-else class="fas fa-user-circle default-avatar"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="myInfo">用户中心</el-dropdown-item>
            <el-dropdown-item command="changePassword">修改密码</el-dropdown-item>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <div v-else class="auth-section">
      <el-button type="success" @click="handleLogin">登录</el-button>
      <router-link to="/register">
        <el-button type="success" plain>注册</el-button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox, ElMessage } from 'element-plus'
import { logout } from '@/api/admin/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const navItems = ['首页', '新闻资讯', '猪病百科', '猪猪药品', '智能兽医', '猪猪文章', '搜索']
const navRoutes = ['/home', '/news', '/findIllness', '/findMedicines', '/doctor', '/article', '/serachBottom']
const activeIndex = ref(0)
const navBarRef = ref(null)

const setActiveIndexByRoute = () => {
  const currentPath = route.path
  const foundIndex = navRoutes.findIndex(r => currentPath.startsWith(r))
  if (foundIndex !== -1) activeIndex.value = foundIndex
}
watch(route, setActiveIndexByRoute)
onMounted(() => {
  setActiveIndexByRoute()
  window.addEventListener('scroll', handleScroll)
})
onUnmounted(() => window.removeEventListener('scroll', handleScroll))

const handleNavClick = (i) => (activeIndex.value = i)

const handleCommand = (cmd) => {
  if (cmd === 'myInfo') router.push('/user')
  if (cmd === 'changePassword') router.push('/user')
  if (cmd === 'logout') confirmLogout()
}

const confirmLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    logout().finally(() => {
      userStore.setUserInfo(null)
      localStorage.clear()
      ElMessage.success('已退出登录')
      router.push('/login')
    })
  })
}

const handleLogin = () => router.push('/login')

const handleScroll = () => {
  if (!navBarRef.value) return
  navBarRef.value.classList.toggle('with-shadow', window.scrollY > 10)
}
</script>

<style scoped>
.main-nav {
  position: fixed;
  top: 0;
  left: 0;
  height: 75px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(200, 200, 200, 0.2);
  transition: all 0.3s ease;
  z-index: 999;
}
.with-shadow {
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

/* logo */
.logo-box {
  display: flex;
  align-items: center;
  padding-left: 25px;
}
.logo-icon {
  color: #12b48b;
}
.logo-text {
  font-size: 22px;
  font-weight: 600;
  margin-left: 10px;
  color: #12b48b;
}

/* 导航栏 */
.nav-list {
  display: flex;
  list-style: none;
  gap: 25px;
}
.nav-list li {
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}
.nav-list li a {
  text-decoration: none;
  color: #333;
  font-weight: 500;
  font-size: 16px;
  padding: 4px 0;
  transition: color 0.3s ease;
}

/* hover 效果 */
.nav-list li:hover a {
  color: #12b48b;
}

/* 下划线动画 */
.nav-list li::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 50%;
  width: 0;
  height: 2px;
  background: #12b48b;
  transform: translateX(-50%);
  transition: width 0.3s ease;
}
.nav-list li:hover::after {
  width: 60%;
}

/* ✅ 激活状态高亮 */
.nav-list li.active a {
  color: #12b48b;
  font-weight: 600;
}
.nav-list li.active::after {
  width: 60%;
}

/* 用户区 */
.user-section,
.auth-section {
  display: flex;
  align-items: center;
  padding-right: 25px;
  align-items: center;
}
.user-name {
  margin-right: 10px;
  color: #333;
}
::v-deep(.el-dropdown-link) {
  display: flex;
  align-items: center;
}
.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  transition: 0.3s;
}
.user-avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 0 12px rgba(18, 180, 139, 0.6);
}
.default-avatar {
  font-size: 34px;
  color: #12b48b;
}
</style>
