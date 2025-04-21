<template>
  <div class="my-info">
    <!-- 用户资料部分 -->
    <div class="user-profile">
      <h3>我的资料</h3>
      <div class="upload-avatar">
        <img :src="userInfo.imgPath" alt="用户头像" class="avatar">
        <span @click="handleUpload">点击上传</span>
      </div>

      <el-form :model="userInfo" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账户名称">
              <el-input v-model="userInfo.userAccount" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名">
              <el-input v-model="userInfo.userName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="userInfo.userSex" placeholder="请选择性别">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄">
              <el-input v-model.number="userInfo.userAge"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="userInfo.userEmail"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号">
              <el-input v-model="userInfo.userTel"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="注册时间">
          <el-input v-model="userInfo.createTime" disabled></el-input>
        </el-form-item>
        <el-button type="primary" @click="saveChanges">保存修改</el-button>
      </el-form>
    </div>

    <!-- 修改登录密码部分 -->
    <div class="change-password">
      <h3>修改登录密码</h3>
      <el-form :model="passwordForm" label-width="100px">
        <el-form-item label="当前密码">
          <el-input v-model="passwordForm.currentPassword" type="password"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordForm.newPassword" type="password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="passwordForm.confirmPassword" type="password"></el-input>
        </el-form-item>
        <el-button type="primary" @click="changePassword">修改密码</el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/stores/user';
import { saveProfile, savePassword } from '@/api/admin/user.js';

const userStore = useUserStore();

// 用户信息数据
const userInfo = ref({
  id: 0,
  userAccount: '',
  userName: '',
  userPwd: '',
  userAge: 0,
  userSex: '',
  userEmail: '',
  userTel: '',
  roleStatus: 0,
  imgPath: '',
  createTime: '',
  updateTime: ''
});

// 密码修改表单数据
const passwordForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// 初始化时从 Pinia 中获取用户信息
watch(
  () => userStore.userInfo,
  (newUserInfo) => {
    if (newUserInfo) {
      userInfo.value = newUserInfo;
    }
  },
  { immediate: true }
);

// 处理上传头像
const handleUpload = () => {
  // 实现头像上传逻辑
};

// 保存修改
const saveChanges = async () => {
  try {
    // 调用 saveProfile API 保存用户信息
    const response = await saveProfile(userInfo.value);
    if (response.code === 200) {
      // 更新 Pinia 中的用户信息
      userStore.setUserInfo(response.data);
      ElMessage.success('保存成功！');
    } else {
      ElMessage.error(response.message || '保存失败，请稍后再试');
    }
  } catch (error) {
    console.error('保存失败:', error);
    ElMessage.error('保存失败，请重试');
  }
};

// 修改密码
const changePassword = async () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    ElMessage.error('新密码与确认密码不一致！');
    return;
  }

  try {
    // 调用 savePassword API 修改密码
    const response = await savePassword(passwordForm.value.currentPassword, passwordForm.value.newPassword);
    if (response.code === 200) {
      ElMessage.success('密码修改成功！');
    } else {
      ElMessage.error(response.message || '密码修改失败，请稍后再试');
    }
  } catch (error) {
    console.error('修改密码失败:', error);
    ElMessage.error('修改密码失败，请重试');
  }
};
</script>

<style scoped>
.my-info {
  padding: 20px;
}

.user-profile,
.change-password {
  margin-bottom: 20px;
}

.upload-avatar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-right: 10px;
}

.upload-avatar span {
  cursor: pointer;
  color: #409eff;
}

.el-form-item {
  margin-bottom: 15px;
}
</style>