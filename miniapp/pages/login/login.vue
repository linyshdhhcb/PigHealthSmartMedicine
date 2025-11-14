<template>
    <view class="login-container">
        <view class="logo-box">
            <image class="logo" src="/static/images/logo.jpg"></image>
            <text class="app-name">猪病智能医疗</text>
        </view>

        <view class="form-box">
            <view class="input-group">
                <text class="input-label">账号</text>
                <input class="input" type="text" v-model="userAccount" placeholder="请输入账号" />
            </view>

            <view class="input-group">
                <text class="input-label">密码</text>
                <input class="input" type="password" v-model="password" placeholder="请输入密码" password />
            </view>

            <button class="login-btn" @click="handleLogin">登录</button>

            <view class="tips">
                <text>还没有账号？请联系管理员</text>
            </view>
        </view>
    </view>
</template>

<script setup>
import { ref } from 'vue'
import { login } from '../../api/user.js'

const userAccount = ref('admin')
const password    = ref('123456')

async function handleLogin() {
  if (!userAccount.value.trim()) {
    return uni.showToast({ title: '请输入账号', icon: 'none' })
  }
  if (!password.value.trim()) {
    return uni.showToast({ title: '请输入密码', icon: 'none' })
  }

  try {
    uni.showLoading({ title: '登录中...' })
    const res = await login(userAccount.value, password.value)
    if (res?.data) {
      uni.setStorageSync('token', res.data.tokenValue)
      uni.setStorageSync('userId', res.data.loginId)
      uni.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => {
        uni.reLaunch({ url: '/pages/index/index' })
      }, 1500)
    }
  } catch {
    uni.showToast({ title: '账号或密码错误', icon: 'none' })
  } finally {
    uni.hideLoading()
  }
}
</script>
<style>
.login-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 40rpx;
    height: 100vh;
    background-color: #f8f8f8;
}

.logo-box {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 80rpx;
    margin-bottom: 60rpx;
}

.logo {
    width: 180rpx;
    height: 180rpx;
    margin-bottom: 20rpx;
}

.app-name {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
}

.form-box {
    width: 100%;
    background-color: #fff;
    border-radius: 20rpx;
    padding: 40rpx;
    box-shadow: 0 2rpx 12rpx 0 rgba(0, 0, 0, 0.1);
}

.input-group {
    margin-bottom: 30rpx;
}

.input-label {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 10rpx;
    display: block;
}

.input {
    width: 100%;
    height: 90rpx;
    background-color: #f5f5f5;
    border-radius: 45rpx;
    padding: 0 30rpx;
    font-size: 28rpx;
}

.login-btn {
    width: 100%;
    height: 90rpx;
    background-color: #07c160;
    color: white;
    border-radius: 45rpx;
    font-size: 32rpx;
    margin-top: 50rpx;
    display: flex;
    align-items: center;
    justify-content: center;
}

.tips {
    margin-top: 30rpx;
    text-align: center;
    font-size: 24rpx;
    color: #999;
}
</style>
