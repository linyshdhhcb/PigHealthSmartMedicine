<!-- src/pages/user/UserEdit.vue -->
<template>
  <el-card class="w-full" :body-style="{ height: 'calc(100vh - 125px)' }">
    <el-loading class="w-full" :size="35" :loading="spinLoading" tip="正在处理，请稍候...">
      <el-form :model="form" ref="formRef" :rules="rules" label-width="100px" auto-label-width>
        <el-row class="w-full">
          <!-- 用户名 -->
          <el-form-item label="用户名" prop="userAccount" required>
            <el-input v-model="form.userAccount" placeholder="请输入用户名" />
          </el-form-item>
          <!-- 真实姓名 -->
          <el-form-item label="真实姓名" prop="userName" required>
            <el-input v-model="form.userName" placeholder="请输入真实姓名" />
          </el-form-item>
          <!-- 年龄 -->
          <el-form-item label="年龄" prop="userAge" required>
            <el-input-number v-model="form.userAge" placeholder="请输入年龄" />
          </el-form-item>
          <!-- 性别 -->
          <el-form-item label="性别" prop="userSex" required>
            <el-select v-model="form.userSex" placeholder="请选择性别" clearable>
              <el-option v-for="(item, index) in sexOptions" :key="index" :value="item.value" :label="item.label" />
            </el-select>
          </el-form-item>
          <!-- 邮箱 -->
          <el-form-item label="邮箱" prop="userEmail" required>
            <el-input v-model="form.userEmail" placeholder="请输入邮箱" />
          </el-form-item>
          <!-- 手机号 -->
          <el-form-item label="手机号" prop="userTel" required>
            <el-input v-model="form.userTel" placeholder="请输入手机号" />
          </el-form-item>
          <!-- 密码 -->
          <el-form-item label="密码" prop="userPwd" required v-if="isAddOperation">
            <el-input-password v-model="form.userPwd" placeholder="请输入密码" />
          </el-form-item>
          <!-- 确认密码 -->
          <el-form-item label="确认密码" prop="userPwds" required v-if="isAddOperation">
            <el-input-password v-model="form.userPwds" placeholder="请确认密码" />
          </el-form-item>
        </el-row>
      </el-form>
      <el-divider class="mt-0" />
      <div class="flex justify-end">
        <el-space>
          <el-button @click="cancelBtnClick">取消</el-button>
          <el-button type="primary" @click="okBtnClick">确定</el-button>
        </el-space>
      </div>
    </el-loading>
  </el-card>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, watch } from 'vue';
import { userAdd, userUpdate, getuserInfo } from '@/api/user.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 接收父组件参数（operationType: 'add' 或 'update'，以及用户ID）
const props = defineProps({
  params: {
    type: Object,
    default: () => ({}),
  },
});

// 父组件函数
const emits = defineEmits(['ok', 'cancel']);

// 加载中
const spinLoading = ref(false);
// 表单引用
const formRef = ref(null);
// 表单数据，初始化字段，可根据需要扩展
const form = reactive({
  id: null,
  userAccount: '',
  userName: '',
  userAge: null,
  userSex: '',
  userEmail: '',
  userTel: '',
  userPwd: '',
  userPwds: '',
});

// 表单校验规则
const rules = {
  userAccount: [{ required: true, message: '用户名不能为空', trigger: 'submit' }],
  userName: [{ required: true, message: '真实姓名不能为空', trigger: 'submit' }],
  userAge: [{ required: true, message: '年龄不能为空', trigger: 'submit' }],
  userSex: [{ required: true, message: '性别不能为空', trigger: 'submit' }],
  userEmail: [{ required: true, message: '邮箱不能为空', trigger: 'submit' }],
  userTel: [{ required: true, message: '手机号不能为空', trigger: 'submit' }],
  userPwd: [{ required: true, message: '密码不能为空', trigger: 'submit' }, { validator: validatePassword }],
  userPwds: [{ required: true, message: '确认密码不能为空', trigger: 'submit' }, { validator: validateConfirmPassword }],
};

// 性别下拉选项
const sexOptions = [
  { value: '男', label: '男' },
  { value: '女', label: '女' },
];

// 是否为添加操作
const isAddOperation = ref(false);

// 密码校验
const validatePassword = (rule, value, callback) => {
  if (isAddOperation.value && !value) {
    callback(new Error('密码不能为空'));
  } else if (isAddOperation.value && value !== form.userPwds) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

// 确认密码校验
const validateConfirmPassword = (rule, value, callback) => {
  if (isAddOperation.value && !value) {
    callback(new Error('确认密码不能为空'));
  } else if (isAddOperation.value && value !== form.userPwd) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

// 确定按钮点击事件
const okBtnClick = () => {
  formRef.value.validate((valid) => {
    if (!valid) {
      console.log('表单验证失败');
      return;
    }
    console.log('表单验证通过');

    spinLoading.value = true;

    if (isAddOperation.value) {
      // 新增操作
      userAdd({
        userAccount: form.userAccount,
        userName: form.userName,
        userAge: form.userAge,
        userSex: form.userSex,
        userEmail: form.userEmail,
        userTel: form.userTel,
        userPwd: form.userPwd,
        userPwds: form.userPwds,
      })
        .then(() => {
          ElMessage.success('新增用户成功');
          emits('ok');
        })
        .catch(error => {
          console.error('新增操作失败', error);
          ElMessage.error('新增操作失败');
        })
        .finally(() => {
          spinLoading.value = false;
        });
    } else {
      // 更新操作
      userUpdate({
        id: form.id,
        userAccount: form.userAccount,
        userName: form.userName,
        userAge: form.userAge,
        userSex: form.userSex,
        userEmail: form.userEmail,
        userTel: form.userTel,
      })
        .then(() => {
          ElMessage.success('更新用户成功');
          emits('ok');
        })
        .catch(error => {
          console.error('更新操作失败', error);
          ElMessage.error('更新操作失败');
        })
        .finally(() => {
          spinLoading.value = false;
        });
    }
  });
};

// 取消按钮点击事件
const cancelBtnClick = () => {
  emits('cancel');
};

// 根据用户ID加载数据（编辑时调用）
const loadUserInfo = (id) => {
  spinLoading.value = true;
  getuserInfo(id)
    .then(res => {
      if (res) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res);
      }
    })
    .finally(() => {
      spinLoading.value = false;
    });
};

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    isAddOperation.value = false;
    loadUserInfo(newVal.id);
  } else {
    isAddOperation.value = true;
    // 重置表单
    form.id = null;
    form.userAccount = '';
    form.userName = '';
    form.userAge = null;
    form.userSex = '';
    form.userEmail = '';
    form.userTel = '';
    form.userPwd = '';
    form.userPwds = '';
  }
}, { immediate: true, deep: true });
</script>

<style scoped>
/* 根据需要添加样式 */
</style>