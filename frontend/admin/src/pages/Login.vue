<template>
  <div class="login-container">
    <!-- 背景图片 -->
    <div class="background-image">
      <img src="@/assets/images/pig/pig3.jpg" alt="背景图片">
    </div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <!-- 内部图片 -->
      <div class="inner-image">
        <img src="@/assets/images/pig/pig1.jpg" alt="内部图片">
      </div>

      <!-- 登录表单 -->
      <div class="login-form">
        <h2 class="login-title">欢迎登录 生猪智慧医药系统</h2>
        <p class="login-subtitle">专为生猪养殖行业设计的综合性医药管理平台</p>

        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="userAccount">账号:</label>
            <input type="text" id="userAccount" v-model="userAccount" required placeholder="请输入账号">
          </div>
          <div class="form-group">
            <label for="password">密码:</label>
            <input type="password" id="password" v-model="password" required placeholder="请输入密码">
          </div>
          <div class="form-group captcha-group">
            <label for="captcha">验证码:</label>
            <div class="captcha-wrapper">
              <input type="text" id="captcha" placeholder="请输入验证码" v-model="captchaInput">
              <div class="captcha-display" @click="generateCaptcha">{{ captcha }}</div>
            </div>
          </div>
          <div class="remember-password">
            <input type="checkbox" id="remember">
            <label for="remember">记住密码</label>
          </div>
          <button type="submit" class="login-button">登录</button>
        </form>

        <!-- 其他登录方式 -->
        <div class="other-login-methods">
          <p class="other-login-title">其他方式登录</p>
          <div class="social-login">
            <img src="@/assets/images/icons/wechat.png" alt="微信登录">
            <img src="@/assets/images/icons/alipay.png" alt="支付宝登录">
            <img src="@/assets/images/icons/douyin.png" alt="抖音登录">
            <img src="@/assets/images/icons/qq.png" alt="QQ登录">
            <img src="@/assets/images/icons/sina.png" alt="微博登录">
            <img src="@/assets/images/icons/github.png" alt="GitHub登录">
            <img src="@/assets/images/icons/google.png" alt="Google登录">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login, getuserInfo } from '@/api/user.js';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/stores/user';

const userAccount = ref('admin');
const password = ref('123456');
const captchaInput = ref('');
const captcha = ref('');

// 生成随机验证码
const generateCaptcha = () => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  for (let i = 0; i < 6; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  captcha.value = result;
};

// 初始化生成验证码
generateCaptcha();

const router = useRouter();
const userStore = useUserStore();

const handleLogin = async () => {
  // 验证码验证
  if (captchaInput.value.toLowerCase() !== captcha.value.toLowerCase()) {
    ElMessage.error('验证码错误');
    generateCaptcha(); // 刷新验证码
    return;
  }

  try {
    const response = await login(userAccount.value, password.value);
    if (response.code === 200) {
      ElMessage.success('登录成功');
      // 存储 token 到本地存储
      localStorage.setItem('token', response.data.tokenValue);
      // 获取 loginId
      const loginId = response.data.loginId;
      // 调用 getuserInfo 接口获取用户信息
      const userInfoResponse = await getuserInfo(loginId);
      if (userInfoResponse.code === 200) {
        // 将用户信息存储到 Pinia 中
        userStore.setUserInfo(userInfoResponse.data);
        // 将用户信息存储到 localStorage 中
        localStorage.setItem('userInfo', JSON.stringify(userInfoResponse.data));
      }
      // 登录成功跳转到首页
      router.push('/articleTypeMgt');
    } else {
      ElMessage.error(response.message || '请求失败，请稍后再试');
    }
  } catch (error) {
    console.error('登录失败:', error);
    ElMessage.error('登录失败，请重试');
  }
};
</script>

<style scoped>
/* 登录容器 */
.login-container {
  height: 100vh;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
}

/* 背景图片 */
.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}

.background-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: brightness(0.85);
}

/* 登录卡片 */
.login-card {
  display: flex;
  width: 900px;
  max-width: 95%;
  height: 750px;
  background-color: white;
  border-radius: 15px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

/* 内部图片区域 */
.inner-image {
  flex: 1;
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, rgba(85, 100, 112, 0.8), rgba(107, 142, 35, 0.8));
}

.inner-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

/* 登录表单区域 */
.login-form {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  background-color: rgba(255, 255, 255, 0.85); /* 添加半透明效果 */
}

/* 登录标题 */
.login-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
  color: #333;
}

/* 登录副标题 */
.login-subtitle {
  font-size: 14px;
  color: #666;
  margin-bottom: 30px;
  background: linear-gradient(90deg, #4CAF50, #2196F3);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* 表单分组 */
.form-group {
  margin-bottom: 20px;
}

/* 标签样式 */
label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #555;
}

/* 输入框样式 */
input[type="text"],
input[type="password"],
input[type="captcha"] {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
}

input[type="text"]:focus,
input[type="password"]:focus,
input[type="captcha"]:focus {
  border-color: #4CAF50;
  outline: none;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
}

/* 验证码区域 */
.captcha-group {
  margin-top: 10px;
}

.captcha-wrapper {
  display: flex;
  align-items: center;
}

.captcha-wrapper input {
  width: 60%;
  margin-right: 10px;
}

.captcha-display {
  height: 38px;
  width: auto;
  cursor: pointer;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f0f0;
  color: #333;
  font-weight: bold;
  letter-spacing: 2px;
  user-select: none;
}

/* 记住密码 */
.remember-password {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.remember-password input {
  margin-right: 8px;
}

.remember-password label {
  font-size: 13px;
  color: #666;
}

/* 登录按钮 */
.login-button {
  width: 100%;
  padding: 12px;
  background: linear-gradient(90deg, #4CAF50, #2196F3);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(76, 175, 80, 0.4);
}

/* 其他登录方式 */
.other-login-methods {
  margin-top: auto;
  margin-top: 30px;
}

.other-login-title {
  text-align: center;
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
}

.social-login {
  display: flex;
  justify-content: space-between;
}

.social-login img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #f5f5f5;
  padding: 5px;
  cursor: pointer;
  transition: transform 0.3s;
}

.social-login img:hover {
  transform: scale(1.1);
}
</style>
