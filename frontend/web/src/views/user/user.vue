<template>
  <div class="user-center-container">
    <!-- 顶部导航栏 -->
    <nav2 />

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧导航栏 -->
      <div class="left-sidebar">
        <div class="user-info">
          <div class="user-avatar">
            <img :src="userStore.userInfo?.imgPath || '@/assets/images/icons/avatar.jpg'" alt="用户头像">
          </div>
          <div class="user-name">{{ userStore.userInfo?.userName || '用户名' }}</div>
        </div>

        <el-menu
          default-active="1"
          class="left-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="1">
            <el-icon><User /></el-icon>
            <span>用户中心</span>
          </el-menu-item>
          <el-menu-item index="2">
            <el-icon><Lock /></el-icon>
            <span>修改密码</span>
          </el-menu-item>
          <el-menu-item index="3">
            <el-icon><ChatDotRound /></el-icon>
            <span>反馈管理</span>
          </el-menu-item>
          <el-menu-item index="4">
            <el-icon><SwitchButton /></el-icon>
            <span>退出登录</span>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 右侧内容区域 -->
      <div class="right-content">
        <div class="content-header">
          <h2 v-if="activeMenu === '1'">用户中心</h2>
          <h2 v-else-if="activeMenu === '2'">修改密码</h2>
          <h2 v-else-if="activeMenu === '3'">反馈管理</h2>
          <h2 v-else-if="activeMenu === '4'">退出登录</h2>
        </div>

        <div class="content-body">
          <!-- 用户中心内容 -->
          <div v-if="activeMenu === '1'">
            <!-- <div class="user-stats">
              <div class="stat-item">
                <div class="stat-value">0</div>
                <div class="stat-label">文章</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">0</div>
                <div class="stat-label">粉丝</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">0</div>
                <div class="stat-label">关注</div>
              </div>
            </div> -->

            <div class="user-profile">
              <div class="profile-section">
                <h3>个人信息</h3>
                <div class="profile-content">
                  <div class="form-group">
                    <label>用户名</label>
                    <input type="text" :value="userStore.userInfo?.userName || '用户名'" disabled>
                  </div>
                  <div class="form-group">
                    <label>邮箱</label>
                    <input type="email" :value="userStore.userInfo?.userEmail || 'user@example.com'" disabled>
                  </div>
                  <div class="form-group">
                    <label>手机号</label>
                    <input type="tel" :value="userStore.userInfo?.userTel || '13800138000'" disabled>
                  </div>
                </div>
              </div>

              <div class="profile-section">
                <h3>账户设置</h3>
                <div class="profile-content">
                  <div class="form-group">
                    <label>语言偏好</label>
                    <select>
                      <option value="zh">中文</option>
                      <option value="en">English</option>
                    </select>
                  </div>
                  <!-- <div class="form-group">
                    <label>通知设置</label>
                    <div class="switch-group">
                      <span>邮件通知</span>
                      <el-switch v-model="emailNotification" />
                    </div>
                    <div class="switch-group">
                      <span>站内信通知</span>
                      <el-switch v-model="siteNotification" />
                    </div>
                  </div> -->
                </div>
              </div>
            </div>
          </div>

          <!-- 修改密码内容 -->
          <div v-if="activeMenu === '2'">
            <h3>修改密码</h3>
            <div class="password-form">
              <div class="form-group">
                <label>当前密码</label>
                <input type="password" v-model="passwordForm.oldPass">
              </div>
              <div class="form-group">
                <label>新密码</label>
                <input type="password" v-model="passwordForm.newPass">
              </div>
              <div class="form-group">
                <label>确认新密码</label>
                <input type="password" v-model="passwordForm.confirmPass">
              </div>
              <button class="update-button" @click="handlePasswordUpdate">更新密码</button>
            </div>
          </div>

          <!-- 反馈管理内容 -->
          <div v-if="activeMenu === '3'">
            <h3>反馈管理</h3>
            <div class="feedback-header-actions">
              <el-button type="primary" @click="showFeedbackDialog = true">新增反馈</el-button>
            </div>
            <div class="feedback-list">
              <div v-for="feedback in feedbacks" :key="feedback.id" class="feedback-item">
                <div class="feedback-header">
                  <span class="feedback-title">{{ feedback.title }}</span>
                  <span :class="['feedback-status', feedback.status === '已处理' ? '' : 'pending']">{{ feedback.status }}</span>
                </div>
                <div class="feedback-content">{{ feedback.content }}</div>
                <div class="feedback-footer">
                  <span class="feedback-date">{{ formatDate(feedback.createTime) }}</span>
                  <el-button type="text" size="small">查看详情</el-button>
                </div>
              </div>
              <div v-if="feedbacks.length === 0" class="no-feedbacks">
                暂无反馈记录
              </div>
            </div>
            <div class="pagination">
              <el-pagination
                v-model:current-page="feedbackParams.pageNum"
                v-model:page-size="feedbackParams.pageSize"
                :total="feedbackTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @current-change="getFeedbacks"
                @size-change="getFeedbacks"
              />
            </div>
          </div>

          <!-- 新增反馈对话框 -->
          <el-dialog v-model="showFeedbackDialog" title="新增反馈" width="50%">
            <div class="form-group">
              <label>标题</label>
              <input type="text" v-model="feedbackForm.title">
            </div>
            <div class="form-group">
              <label>内容</label>
              <textarea v-model="feedbackForm.content" rows="4"></textarea>
            </div>
            <div class="form-actions">
              <el-button @click="showFeedbackDialog = false">取消</el-button>
              <el-button type="primary" @click="addFeedback">提交</el-button>
            </div>
          </el-dialog>

          <!-- 退出登录内容 -->
          <div v-if="activeMenu === '4'">
            <h3>确认退出</h3>
            <div class="logout-content">
              <p>确定要退出当前账号吗？</p>
              <div class="logout-buttons">
                <el-button @click="activeMenu = '1'">取消</el-button>
                <el-button type="primary" @click="handleLogout">确认退出</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <buttom2 />
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import buttom2 from '@/components/buttom2.vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { User, Lock, ChatDotRound, SwitchButton } from '@element-plus/icons-vue';
import nav2 from '@/components/nav2.vue';
import { logout, feedbackAdd, feedbackPage, savePassword } from '@/api/admin/user.js';
import { useUserStore } from '@/stores/user';

const router = useRouter();
const userStore = useUserStore();
const emailNotification = ref(true);
const siteNotification = ref(true);
const activeMenu = ref('1');
const showFeedbackDialog = ref(false);
const passwordForm = reactive({
  oldPass: '',
  newPass: '',
  confirmPass: ''
});
const feedbackForm = reactive({
  title: '',
  content: ''
});
const feedbacks = ref([]);
const feedbackTotal = ref(0);
const feedbackParams = reactive({
  pageNum: 1,
  pageSize: 5,
  sortField: '',
  sortOrder: '',
  name: '',
  email: '',
  title: '',
  content: ''
});

// 初始化加载用户信息
onMounted(() => {
  getFeedbacks();
});

// 获取反馈列表
const getFeedbacks = async () => {
  try {
    const response = await feedbackPage(feedbackParams);
    if (response.code === 200) {
      feedbacks.value = response.data.data;
      feedbackTotal.value = response.data.total;
    }
  } catch (error) {
    console.error('获取反馈列表失败:', error);
    ElMessage.error('获取反馈列表失败，请重试');
  }
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

// 处理菜单选择
const handleMenuSelect = (index) => {
  activeMenu.value = index;
};

// 处理退出登录
const handleLogout = async () => {
  try {
    const response = await logout();
    if (response.code === 200 && response.data) {
      ElMessage.success('已成功退出登录');
      userStore.clearUserInfo();
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
      router.push({ name: 'Login' });
    } else {
      ElMessage.error(response.message || '退出登录失败');
    }
  } catch (error) {
    console.error('退出登录失败:', error);
    ElMessage.error('退出登录失败，请重试');
  }
};

// 处理密码更新
const handlePasswordUpdate = async () => {
  if (!passwordForm.oldPass || !passwordForm.newPass || !passwordForm.confirmPass) {
    ElMessage.error('请填写完整的密码信息');
    return;
  }
  if (passwordForm.newPass !== passwordForm.confirmPass) {
    ElMessage.error('新密码和确认密码不一致');
    return;
  }
  try {
    const response = await savePassword(passwordForm.oldPass, passwordForm.newPass);
    if (response.code === 200) {
      ElMessage.success('密码更新成功');
      passwordForm.oldPass = '';
      passwordForm.newPass = '';
      passwordForm.confirmPass = '';
    } else {
      ElMessage.error(response.message || '密码更新失败');
    }
  } catch (error) {
    console.error('密码更新失败:', error);
    ElMessage.error('密码更新失败，请重试');
  }
};

// 添加反馈
const addFeedback = async () => {
  if (!feedbackForm.title || !feedbackForm.content) {
    ElMessage.error('请填写完整的反馈信息');
    return;
  }
  try {
    const params = {
      name: userStore.userInfo?.userName || '匿名用户',
      email: userStore.userInfo?.userEmail || '',
      title: feedbackForm.title,
      content: feedbackForm.content
    };
    const response = await feedbackAdd(params);
    if (response.code === 200) {
      ElMessage.success('反馈提交成功');
      showFeedbackDialog.value = false;
      feedbackForm.title = '';
      feedbackForm.content = '';
      getFeedbacks();
    } else {
      ElMessage.error(response.message || '反馈提交失败');
    }
  } catch (error) {
    console.error('反馈提交失败:', error);
    ElMessage.error('反馈提交失败，请重试');
  }
};
</script>

<style scoped>
/* 整体布局 */
.user-center-container {
  height: 100vh;
  background-color: #f5f7fa;
}

/* 主要内容区域 */
.main-content {
  display: flex;
  height: calc(100vh - 60px); /* 减去顶部导航栏高度 */
  padding: 20px;
  box-sizing: border-box;
}

/* 左侧导航栏 */
.left-sidebar {
  width: 240px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-right: 20px;
  display: flex;
  flex-direction: column;
}

/* 用户信息 */
.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 20px;
}

.user-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  margin-bottom: 10px;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

/* 左侧菜单 */
.left-menu {
  flex: 1;
  border-right: none !important;
}

.left-menu :deep(.el-menu-item) {
  height: auto;
  margin-bottom: 5px;
  border-radius: 8px;
  padding: 12px 15px !important;
}

.left-menu :deep(.el-menu-item.is-active) {
  background-color: #e1f5fe;
  color: #0288d1;
}

.left-menu :deep(.el-menu-item:hover) {
  background-color: #f0f7ff;
}

/* 右侧内容区域 */
.right-content {
  flex: 1;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 25px;
  overflow-y: auto;
}

/* 内容标题 */
.content-header {
  margin-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 15px;
}

.content-header h2 {
  font-size: 22px;
  color: #333;
  font-weight: 600;
}

/* 用户统计 */
.user-stats {
  display: flex;
  justify-content: space-around;
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 25px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #0288d1;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 个人资料表单 */
.profile-section {
  margin-bottom: 25px;
}

.profile-section h3 {
  font-size: 18px;
  margin-bottom: 15px;
  color: #333;
  display: flex;
  align-items: center;
}

.profile-section h3::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 18px;
  background-color: #4CAF50;
  margin-right: 8px;
  border-radius: 2px;
}

.profile-content {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}

.switch-group {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.update-button {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.update-button:hover {
  background-color: #388e3c;
}

/* 修改密码表单 */
.password-form {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 25px;
}

/* 反馈列表 */
.feedback-header-actions {
  margin-bottom: 20px;
}

.feedback-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.feedback-item {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
}

.feedback-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.feedback-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.feedback-status {
  font-size: 14px;
  color: #4CAF50;
  padding: 3px 8px;
  background-color: #e1f5fe;
  border-radius: 4px;
}

.feedback-status.pending {
  color: #FF9800;
  background-color: #FFF3E0;
}

.feedback-content {
  margin-bottom: 15px;
  color: #666;
  line-height: 1.6;
}

.feedback-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #999;
}

.no-feedbacks {
  padding: 30px;
  color: #999;
  text-align: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 注销内容 */
.logout-content {
  text-align: center;
  padding: 50px 0;
}

.logout-content p {
  margin-bottom: 30px;
  font-size: 16px;
  color: #666;
}

.logout-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

:deep(.el-switch__core) {
  height: 24px !important;
}

:deep(.el-switch__core::before) {
  width: 12px !important;
  height: 12px !important;
}
</style>