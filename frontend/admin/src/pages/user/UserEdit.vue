<!-- src/pages/user/UserEdit.vue -->
<template>
  <el-card class="user-edit-card" :body-style="{ height: 'calc(100vh - 120px)' }">
    <div class="user-edit-container">
      <!-- 左侧表单 -->
      <div class="form-section">
        <h2 class="section-title">👤 用户信息</h2>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="120px"
          label-position="left"
          size="default"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="userAccount">
                <el-input v-model="form.userAccount" placeholder="请输入用户名" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="真实姓名" prop="userName">
                <el-input v-model="form.userName" placeholder="请输入真实姓名" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="年龄" prop="userAge">
                <el-input-number v-model="form.userAge" :min="0" :max="150" style="width:100%" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="性别" prop="userSex">
                <el-select v-model="form.userSex" placeholder="请选择性别">
                  <el-option label="男" value="男" />
                  <el-option label="女" value="女" />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="邮箱" prop="userEmail">
                <el-input v-model="form.userEmail" placeholder="请输入邮箱" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="手机号" prop="userTel">
                <el-input v-model="form.userTel" placeholder="请输入手机号" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="角色状态" prop="roleStatus">
                <el-select v-model="form.roleStatus" placeholder="请选择角色">
                  <el-option label="管理员" :value="1" />
                  <el-option label="普通用户" :value="0" />
                </el-select>
              </el-form-item>
            </el-col>

            <template v-if="isAddOperation">
              <el-col :span="12">
                <el-form-item label="密码" prop="userPwd">
                  <el-input type="password" v-model="form.userPwd" placeholder="请输入密码" show-password />
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="确认密码" prop="userPwds">
                  <el-input type="password" v-model="form.userPwds" placeholder="请再次输入密码" show-password />
                </el-form-item>
              </el-col>
            </template>
          </el-row>
        </el-form>
      </div>

      <!-- 右侧头像上传 -->
      <div class="avatar-section">
        <h2 class="section-title">🖼 用户头像</h2>
        <div class="avatar-container">
          <img
            v-if="form.imgPath"
            :src="form.imgPath"
            alt="用户头像"
            class="avatar-img"
            loading="lazy"
          />
          <div v-else class="avatar-placeholder">暂无头像</div>

          <label class="upload-btn">
            选择头像
            <input type="file" accept="image/*" @change="handleAvatarUpload" />
          </label>
        </div>
        <p class="hint-text">图片将自动上传，支持 JPG/PNG ≤ 2MB</p>
      </div>
    </div>

    <div class="footer-actions">
      <el-button size="large"   @click="cancelBtnClick">取消</el-button>
      <el-button size="large"   type="primary" :loading="loading" @click="okBtnClick">确定</el-button>
    </div>
  </el-card>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { userAdd, userUpdate, getuserInfo, uploadFile } from '@/api/user.js'

const props = defineProps({
  params: { type: Object, default: () => ({}) }
})
const emits = defineEmits(['ok', 'cancel'])

const formRef = ref(null)
const loading = ref(false)
const isAddOperation = ref(true)

const form = reactive({
  id: null,
  userAccount: '',
  userName: '',
  userAge: null,
  userSex: '',
  userEmail: '',
  userTel: '',
  roleStatus: 0,
  userPwd: '',
  userPwds: '',
  imgPath: ''
})

// 表单校验规则
const rules = {
  userAccount: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
  userName: [{ required: true, message: '真实姓名不能为空', trigger: 'blur' }],
  userSex: [{ required: true, message: '请选择性别', trigger: 'change' }],
  userEmail: [{ required: true, message: '邮箱不能为空', trigger: 'blur' }],
  userTel: [{ required: true, message: '手机号不能为空', trigger: 'blur' }],
  roleStatus: [{ required: true, message: '请选择角色', trigger: 'change' }],
  userPwd: [
    {
      required: true,
      trigger: 'blur',
      validator: (rule, value, callback) => {
        if (isAddOperation.value && (!value || value.length < 6)) {
          callback(new Error('密码至少6位'))
        } else callback()
      }
    }
  ],
  userPwds: [
    {
      required: true,
      trigger: 'blur',
      validator: (rule, value, callback) => {
        if (isAddOperation.value && value !== form.userPwd) {
          callback(new Error('两次输入的密码不一致'))
        } else callback()
      }
    }
  ]
}

// 监听 props 判断是否是新增/编辑
watch(
  () => props.params,
  async (nv) => {
    if (nv && nv.id) {
      isAddOperation.value = false
      const res = await getuserInfo(nv.id)
      const data = res.data?.data || res.data || res
      Object.assign(form, data)
    } else {
      isAddOperation.value = true
      Object.assign(form, {
        id: null,
        userAccount: '',
        userName: '',
        userAge: null,
        userSex: '',
        userEmail: '',
        userTel: '',
        roleStatus: 0,
        userPwd: '',
        userPwds: '',
        imgPath: ''
      })
    }
  },
  { immediate: true, deep: true }
)

// 📸 头像上传逻辑
const handleAvatarUpload = async (e) => {
  const file = e.target.files[0]
  if (!file) return

  if (!file.type.startsWith('image/')) {
    ElMessage.error('请上传图片文件')
    return
  }
  if (file.size / 1024 / 1024 > 2) {
    ElMessage.error('图片大小不能超过2MB')
    return
  }

  try {
    const resp = await uploadFile(file)
    if (resp.code === 200 ) {
      form.imgPath = resp.data.url
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(resp.message || '上传失败 else部分')
    }
  } catch (err) {
    console.error('上传失败', err)
    ElMessage.error('上传失败 catch部分，请稍后重试')
  }
}

// 🧾 提交表单
const okBtnClick = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return ElMessage.error('请检查表单信息')

    loading.value = true
    try {
      const payload = { ...form }

      if (isAddOperation.value) {
        await userAdd(payload)
        ElMessage.success('新增用户成功')
      } else {
        await userUpdate(payload)
        ElMessage.success('更新用户成功')
      }
      emits('ok')
    } catch (err) {
      console.error(err)
      ElMessage.error('提交失败')
    } finally {
      loading.value = false
    }
  })
}

const cancelBtnClick = () => emits('cancel')
</script>

<style scoped>
.user-edit-card {
  padding: 20px;
  background: linear-gradient(180deg, #fdfdfd, #f6f9fc);
  border-radius: 14px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.05);
}

.user-edit-container {
  display: flex;
  justify-content: space-between;
  gap: 40px;
  height: 100%;
}

.form-section {
  flex: 1.4;
  background: #fff;
  padding: 24px;
  border-radius: 14px;
  box-shadow: 0 4px 18px rgba(0, 0, 0, 0.04);
}

.avatar-section {
  flex: 0.7;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #fff;
  padding: 24px;
  border-radius: 14px;
  box-shadow: 0 4px 18px rgba(0, 0, 0, 0.04);
  text-align: center;
}

.section-title {
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 20px;
  color: #333;
}

.avatar-container {
  position: relative;
}

.avatar-img {
  width: 160px;
  height: 160px;
  border-radius: 50%;
  object-fit: cover;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.25s ease;
}
.avatar-img:hover {
  transform: scale(1.05);
}

.avatar-placeholder {
  width: 160px;
  height: 160px;
  border-radius: 50%;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 14px;
  border: 1px dashed #ddd;
}

.upload-btn {
  margin-top: 14px;
  display: inline-block;
  padding: 8px 14px;
  background: #409eff;
  color: white;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
  transition: 0.25s;
}
.upload-btn:hover {
  background: #66b1ff;
}
.upload-btn input {
  display: none;
}

.hint-text {
  margin-top: 10px;
  font-size: 12px;
  color: #888;
}

.footer-actions {
  margin-top: -400px;
  text-align: center;

}

</style>
