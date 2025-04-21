<template>
  <el-card class="w-full" :loading="spinLoading" tip="正在处理，请稍候...">
    <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
      <div class="flex flex-wrap">
        <!-- 疾病种类 -->
        <el-form-item label="疾病种类" prop="kindId">
          <el-select v-model="form.kindId" placeholder="请选择疾病种类">
            <el-option
              v-for="kind in illnessKindList"
              :key="kind.id"
              :label="kind.name"
              :value="kind.id"
            />
          </el-select>
        </el-form-item>
        <!-- 疾病名称 -->
        <el-form-item label="疾病名称" prop="illnessName">
          <el-input v-model="form.illnessName" placeholder="请输入疾病名称" />
        </el-form-item>
        <!-- 诱发因素 -->
        <el-form-item label="诱发因素" prop="includeReason">
          <Editor
            v-model="form.includeReason"
            :api-key="tinyApiKey"
            :init="editorOptions"
            class="w-full"
          />
        </el-form-item>
        <!-- 疾病症状 -->
        <el-form-item label="疾病症状" prop="illnessSymptom">
          <Editor
            v-model="form.illnessSymptom"
            :api-key="tinyApiKey"
            :init="editorOptions"
            class="w-full"
          />
        </el-form-item>
        <!-- 特殊症状 -->
        <el-form-item label="特殊症状" prop="specialSymptom">
          <Editor
            v-model="form.specialSymptom"
            :api-key="tinyApiKey"
            :init="editorOptions"
            class="w-full"
          />
        </el-form-item>
      </div>
    </el-form>
    <el-divider class="mt-0" />
    <div class="flex justify-end">
      <el-space>
        <el-button @click="cancelBtnClick">取消</el-button>
        <el-button type="primary" @click="okBtnClick">确定</el-button>
      </el-space>
    </div>
  </el-card>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, watch } from 'vue';
import Editor from '@tinymce/tinymce-vue'; // 引入 TinyMCE 编辑器
import { illnessAdd, illnessUpdate, getillnessInfo } from '@/api/illness.js';
import { illnessKindPage } from '@/api/illnessKind.js';
import { ElMessage } from 'element-plus';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 配置 TinyMCE
const tinyApiKey = '902amriqsdr8l27hbbj3kq9m6j64jfroimothc6ag8dnepyp';
const editorOptions = reactive({
  menubar: false, // 隐藏菜单栏
  plugins: 'advlist autolink lists link image charmap preview anchor searchreplace visualblocks code fullscreen insertdatetime media table code help wordcount',
  toolbar: 'undo redo | blocks | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image media table | code fullscreen preview | forecolor backcolor | help',
  content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
  height: 500,
  images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {
    // 这里可以添加图片上传逻辑
    reject('需要配置图片上传服务');
  })
});

// 接收父组件参数（operationType: 'add' 或 'update'，以及疾病ID和kindId）
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
  kindId: null,
  illnessName: '',
  includeReason: '',
  illnessSymptom: '',
  specialSymptom: ''
});

// 表单校验规则
const rules = {
  kindId: [{ required: true, message: '疾病种类不能为空', trigger: 'submit' }],
  illnessName: [{ required: true, message: '疾病名称不能为空', trigger: 'submit' }],
  includeReason: [{ required: true, message: '诱发因素不能为空', trigger: 'submit' }],
  illnessSymptom: [{ required: true, message: '疾病症状不能为空', trigger: 'submit' }],
  specialSymptom: [{ required: true, message: '特殊症状不能为空', trigger: 'submit' }]
};

// 是否为添加操作
const isAddOperation = ref(false);

// 疾病种类列表
const illnessKindList = ref([]);

// 获取疾病种类列表
const getIllnessKindList = () => {
  illnessKindPage({ pageNum: 1, pageSize: 1000 })
    .then(res => {
      if (res.code === 200) {
        illnessKindList.value = res.data.data;
      } else {
        ElMessage.error(res.message || '获取疾病种类列表失败');
      }
    })
    .catch(error => {
      console.error('获取疾病种类列表失败', error);
      ElMessage.error('获取疾病种类列表失败');
    });
};

// 根据疾病ID加载数据（编辑时调用）
const loadIllnessInfo = (id) => {
  spinLoading.value = true;
  getillnessInfo(id)
    .then(res => {
      if (res.data) {
        // 将返回的数据赋值给form，假设返回数据字段与form字段一致
        Object.assign(form, res.data);
      } else {
        ElMessage.error('获取疾病信息失败');
      }
    })
    .catch(error => {
      console.error('获取疾病信息失败', error);
      ElMessage.error('获取疾病信息失败');
    })
    .finally(() => {
      spinLoading.value = false;
    });
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
      illnessAdd(form)
        .then(() => {
          ElMessage.success('新增疾病成功');
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
      illnessUpdate(form)
        .then(() => {
          ElMessage.success('更新疾病成功');
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

// 监听父组件传入参数变化
watch(() => props.params, (newVal) => {
  if (newVal && newVal.id) {
    isAddOperation.value = false;
    loadIllnessInfo(newVal.id);
    form.kindId = newVal.kindId; // 设置 kindId
  } else {
    isAddOperation.value = true;
    // 重置表单
    form.id = null;
    form.kindId = newVal.kindId || null; // 设置 kindId
    form.illnessName = '';
    form.includeReason = '';
    form.illnessSymptom = '';
    form.specialSymptom = '';
  }
}, { immediate: true, deep: true });

// 初始化获取疾病种类列表
getIllnessKindList();
</script>

<style scoped>
/* 可根据需要添加样式 */
:deep(.tox-tinymce) {
  border-radius: 4px;
  border: 1px solid #DCDFE6;
}
</style>