<!-- src/pages/user/UserDetail.vue -->
<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 用户名 -->
        <el-form-item label="用户名">
          <el-input v-model="form.userAccount" placeholder="请输入用户名" readonly />
        </el-form-item>
        <!-- 真实姓名 -->
        <el-form-item label="真实姓名">
          <el-input v-model="form.userName" placeholder="请输入真实姓名" readonly />
        </el-form-item>
        <!-- 年龄 -->
        <el-form-item label="年龄">
          <el-input-number v-model="form.userAge" placeholder="请输入年龄" readonly />
        </el-form-item>
        <!-- 性别 -->
        <el-form-item label="性别">
          <el-input v-model="userSexName" placeholder="性别" readonly />
        </el-form-item>
        <!-- 邮箱 -->
        <el-form-item label="邮箱">
          <el-input v-model="form.userEmail" placeholder="请输入邮箱" readonly />
        </el-form-item>
        <!-- 手机号 -->
        <el-form-item label="手机号">
          <el-input v-model="form.userTel" placeholder="请输入手机号" readonly />
        </el-form-item>
        <!-- 角色状态 -->
        <el-form-item label="角色状态">
          <el-input v-model="roleStatusName" placeholder="角色状态" readonly />
        </el-form-item>
        <!-- 用户头像 -->
        <el-form-item label="用户头像">
          <el-image v-if="form.imgPath" :src="form.imgPath" style="width: 100px; height: 100px;" />
          <span v-else>无头像</span>
        </el-form-item>
      </div>
    </el-form>
    <el-divider class="mt-0" />
    <div class="flex justify-end">
      <el-space>
        <el-button @click="cancelBtnClick">关闭</el-button>
      </el-space>
    </div>
  </el-card>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, watch } from 'vue';
import { getuserInfo } from '@/api/user.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（用户ID）
const props = defineProps({
  params: {
    type: Object,
    default: () => ({}),
  },
});

// 父组件函数
const emits = defineEmits(['cancel']);

// 加载中
const spinLoading = ref(false);
// 表单数据，初始化字段，可根据需要扩展
const form = reactive({
  id: null,
  userAccount: '',
  userName: '',
  userAge: null,
  userSex: '',
  userEmail: '',
  userTel: '',
  roleStatus: null,
  imgPath: '',
});

// 性别名称
const userSexName = ref('');

// 角色状态名称
const roleStatusName = ref('');

// 根据用户ID加载数据
const loadUserInfo = (id) => {
  spinLoading.value = true;
  getuserInfo(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
        userSexName.value = getUserSexName(res.data.userSex);
        roleStatusName.value = getRoleStatusName(res.data.roleStatus);
      } else {
        ElMessage.error('获取用户信息失败');
      }
    })
    .catch(error => {
      console.error('获取用户信息失败', error);
      ElMessage.error('获取用户信息失败');
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 获取性别名称
const getUserSexName = (sex) => {
  switch (sex) {
    case '男':
      return '男';
    case '女':
      return '女';
    default:
      return '未知';
  }
};

// 获取角色状态名称
const getRoleStatusName = (status) => {
  switch (status) {
    case 1:
      return '管理员';
    case 0:
      return '普通用户';
    default:
      return '未知';
  }
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    loadUserInfo(newVal.id);
  } else {
    // 重置表单
    form.id = null;
    form.userAccount = '';
    form.userName = '';
    form.userAge = null;
    form.userSex = '';
    form.userEmail = '';
    form.userTel = '';
    form.roleStatus = null;
    form.imgPath = '';
    userSexName.value = '';
    roleStatusName.value = '';
  }
}, { immediate: true, deep: true });

// 关闭按钮点击事件
const cancelBtnClick = () => {
  emits('cancel');
};
</script>

<style scoped>
/* 根据需要添加样式 */
</style>