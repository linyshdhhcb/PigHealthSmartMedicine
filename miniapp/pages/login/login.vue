<template>
    <view class="login-container">
        <view class="bg-top"></view>
        <view class="logo-box">
            <view class="logo-wrap">
                <image class="logo" src="/static/images/logo.jpg" mode="aspectFill" />
            </view>
            <text class="app-name">猪病智能医疗</text>
            <text class="app-slogan">专业的生猪健康管理平台</text>
        </view>

        <view class="form-box">
            <view class="input-group">
                <text class="input-label">账号</text>
                <view class="input-wrap">
                    <uni-icons type="person" size="20" color="#999" />
                    <input class="input" type="text" v-model="userAccount" placeholder="请输入账号" />
                </view>
            </view>

            <view class="input-group">
                <text class="input-label">密码</text>
                <view class="input-wrap">
                    <uni-icons type="locked" size="20" color="#999" />
                    <input class="input" type="password" v-model="password" placeholder="请输入密码" password />
                </view>
            </view>

            <button class="login-btn" @click="handleLogin">
                <text class="btn-text">登 录</text>
            </button>

            <view class="tips">
                <text class="tips-text">还没有账号？请联系管理员</text>
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
<style lang="scss" scoped>
.login-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    min-height: 100vh;
    background: #f7f8fa;
    position: relative;
    overflow: hidden;
}

.bg-top {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 520rpx;
    background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 40%, #43a047 70%, #66bb6a 100%);
    border-radius: 0 0 60rpx 60rpx;
}

.logo-box {
    position: relative;
    z-index: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 100rpx;
    margin-bottom: 60rpx;
}

.logo-wrap {
    width: 160rpx;
    height: 160rpx;
    border-radius: 40rpx;
    background: rgba(255, 255, 255, 0.2);
    padding: 16rpx;
    margin-bottom: 24rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.15);
}

.logo {
    width: 128rpx;
    height: 128rpx;
    border-radius: 28rpx;
}

.app-name {
    font-size: 40rpx;
    font-weight: 700;
    color: #fff;
    margin-bottom: 8rpx;
}

.app-slogan {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.75);
}

.form-box {
    position: relative;
    z-index: 1;
    width: calc(100% - 64rpx);
    background-color: #fff;
    border-radius: 32rpx;
    padding: 48rpx 40rpx;
    box-shadow: 0 8rpx 40rpx rgba(0, 0, 0, 0.08);
}

.input-group {
    margin-bottom: 36rpx;
}

.input-label {
    font-size: 28rpx;
    color: #555;
    font-weight: 600;
    margin-bottom: 16rpx;
    display: block;
}

.input-wrap {
    display: flex;
    align-items: center;
    background: #f7f8fa;
    border-radius: 20rpx;
    padding: 0 28rpx;
    height: 96rpx;
    border: 2rpx solid #eee;
    transition: border-color 0.3s ease;
}

.input {
    flex: 1;
    height: 96rpx;
    font-size: 30rpx;
    margin-left: 16rpx;
}

.login-btn {
    width: 100%;
    height: 96rpx;
    background: linear-gradient(135deg, #2e7d32, #43a047);
    color: white;
    border-radius: 20rpx;
    font-size: 34rpx;
    margin-top: 48rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 8rpx 24rpx rgba(46, 125, 50, 0.3);
    transition: transform 0.2s ease, box-shadow 0.2s ease;

    &:active {
        transform: scale(0.98);
        box-shadow: 0 4rpx 12rpx rgba(46, 125, 50, 0.2);
    }
}

.btn-text {
    font-weight: 600;
    letter-spacing: 8rpx;
}

.tips {
    margin-top: 36rpx;
    text-align: center;
}

.tips-text {
    font-size: 24rpx;
    color: #bbb;
}
</style>
