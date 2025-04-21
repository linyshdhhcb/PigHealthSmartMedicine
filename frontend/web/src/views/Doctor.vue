<template>
  <nav2 />
  <div class="doctor-page">
    <!-- 左侧导航栏 -->
    <div class="sidebar">
      <img src="@/assets/images/team/user-4.jpg" alt="管理员头像" class="admin-avatar">
      <div class="admin-info">
        <h3>管理员</h3>
        <p><van-icon name="phone"/>17746678954</p>
        <p><van-icon name="envelop-o"/>2678788262@qq.com</p>
      </div>
      <van-sidebar v-model="activeKey" class="sidebar-content">
        <van-sidebar-item title="我的资料" @click="showMyInfo">
          <template #icon>
            <van-icon name="contact" />
          </template>
        </van-sidebar-item>
        <van-sidebar-item title="智能医生" @click="showSmartDoctor">
          <template #icon>
            <van-icon name="fire-o"  />
          </template>
        </van-sidebar-item>
        <van-sidebar-item title="疾病管理" @click="showDiseaseManagement">
          <template #icon>
            <van-icon name="bars" />
          </template>
        </van-sidebar-item>
        <van-sidebar-item title="药品管理" @click="showMedicineManagement">
          <template #icon>
            <van-icon name="bars" />
          </template>
        </van-sidebar-item>
        <van-sidebar-item title="反馈管理" @click="showFeedbackManagement">
          <template #icon>
            <van-icon name="bars" />
          </template>
        </van-sidebar-item>
        <van-sidebar-item title="退出登录" @click="Userlogout">
          <template #icon>
            <van-icon name="setting" />
          </template>
        </van-sidebar-item>
      </van-sidebar>
    </div>

    <!-- 右侧内容区 -->
    <div class="content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import nav2 from '@/components/nav2.vue'; // 引入nav1组件
import { ElMessageBox,ElMessage } from 'element-plus';
import { logout } from '@/api/admin/user.js';

const router = useRouter();

// 当前激活的导航项
const activeKey = ref(0); // 默认选中我的资料

// 导航项点击事件处理函数
const showMyInfo = () => {
  router.push({ name: 'myInfo' });
};
const showSmartDoctor = () => {
  router.push({ name: 'SmartDoctor' });
};
const showDiseaseManagement = () => {
  router.push({ name: 'Disease' });
};
const showMedicineManagement = () => {
  router.push({ name: 'Medicine' });
};
const showFeedbackManagement = () => {
  router.push({ name: 'Feedback' });
};

// 退出登录
const Userlogout = () => {
  ElMessageBox.confirm('您确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    // 调用退出登录API
    logout().then(() => {
      // 清除用户信息（如果有的话）
      // 例如：清除Vuex中的用户信息
      // store.commit('clearUserInfo');

      // 例如：清除本地存储中的用户信息
      // localStorage.removeItem('userInfo');

      // 跳转到登录页面
      router.push({ path: '/login' });
    }).catch(() => {
      ElMessage.error('退出登录失败，请稍后再试');
    });
  }).catch(() => {
    // 取消退出登录
  });
};

// 初始化时跳转到智能医生页面
onMounted(() => {
  showMyInfo();
});
</script>

<style scoped>
.sidebar-content {
  /* margin-left: 150px; */
  width: 100%;
}

::v-deep .van-sidebar-item {
  background: white;
  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中 */
  justify-content: flex-start; /* 水平左对齐 */
  padding-left: 20px; /* 左侧内边距 */
}

/* 添加选中状态样式 */
::v-deep .van-sidebar-item--select {
  background-color: #006baa;
  color: white;
}

.doctor-page {
  margin: 30px 220px;
  display: flex;
}

.sidebar {
  width: 250px;
  padding: 20px;
  border-right: 1px solid #ebebeb;
  display: flex;
  flex-direction: column;
  align-items: center; /* 使整个侧边栏内容居中 */
}

.admin-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 10px;
}

.admin-info {
  text-align: center;
  margin-bottom: 20px;
}

.content {
  flex: 1;
  padding: 20px;
}

.doctor-chat {
  background-color: #fff;
  border: 1px solid #ebebeb;
  padding: 20px;
  height: 500px;
}

.chat-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.doctor-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.chat-content {
  margin-bottom: 20px;
}

.message {
  display: flex;
  margin-bottom: 10px;
}

.message-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
}

.message-text {
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 4px;
}

.input-area {
  display: flex;
  margin-top: 280px;
}

.input-area textarea {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px 0 0 4px;
 
}

.input-area button {
  background-color: #008c69;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 0 4px 4px 0;
}
</style>