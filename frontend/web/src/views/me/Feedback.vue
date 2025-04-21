<template>
  <div class="feedback-management">
    <h3>反馈信息</h3>
    <p>集思广益，广纳贤才。共收集 {{ feedbackPageData.length }} 条反馈信息。</p>

    <el-button type="primary" @click="showAddForm = true"  >新增反馈</el-button>

    <!-- 反馈列表 -->
    <el-table :data="feedbackPageData" style="width: 100%">
      <el-table-column prop="id" label="ID" width="50"></el-table-column>
      <el-table-column prop="name" label="姓名" width="120"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
      <el-table-column prop="title" label="标题" width="150"></el-table-column>
      <el-table-column prop="content" label="内容"></el-table-column>
      <el-table-column prop="createTime" label="反馈时间" width="200"></el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          <el-button size="small" type="info" @click="handleUpdate(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加反馈表单 -->
    <div v-if="showAddForm" class="add-feedback-form">
      <h4>新增反馈</h4>
      <el-form :model="newFeedback" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="newFeedback.name"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="newFeedback.email"></el-input>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="newFeedback.title"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="newFeedback.content" type="textarea" :rows="4"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitFeedback">提交反馈</el-button>
          <el-button @click="showAddForm = false">取消</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 修改反馈模态框 -->
    <el-dialog v-model="showUpdateDialog" title="修改反馈" width="50%">
      <el-form :model="updateForm" label-width="100px">
        <el-form-item label="ID">
          <el-input v-model="updateForm.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="updateForm.name"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="updateForm.email"></el-input>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="updateForm.title"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="updateForm.content"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showUpdateDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmUpdate">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { feedbackPage, feedbackDelete, feedbackUpdate, feedbackAdd } from '@/api/admin/feedback.js';

// 反馈列表数据
const feedbackPageData = ref([]);

// 新增反馈表单数据
const newFeedback = ref({
  name: '',
  email: '',
  title: '',
  content: ''
});

// 修改反馈表单数据
const updateForm = ref({});
const showUpdateDialog = ref(false);

const showAddForm = ref(false)

// 获取反馈列表
feedbackPage({ pageNum: 1, pageSize: 100 })
  .then((res) => {
    if (res.code === 200) {
      feedbackPageData.value = res.data.data;
    } else {
      ElMessage.error('获取反馈信息失败！');
    }
  })
  .catch((err) => {
    console.log(err);
    ElMessage.error('获取反馈信息失败！');
  });

// 删除反馈
const handleDelete = (index, row) => {
  ElMessageBox.confirm('此操作将永久删除该反馈, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    feedbackDelete(row.id)
      .then((res) => {
        if (res.code === 200) {
          feedbackPageData.value.splice(index, 1);
          ElMessage.success('删除成功！');
        } else {
          ElMessage.error('删除失败！');
        }
      })
      .catch((err) => {
        console.log(err);
        ElMessage.error('删除失败！');
      });
  });
};

// 修改反馈
const handleUpdate = (row) => {
  updateForm.value = { ...row };
  showUpdateDialog.value = true;
};

const confirmUpdate = () => {
  feedbackUpdate(updateForm.value)
    .then((res) => {
      if (res.code === 200) {
        const index = feedbackPageData.value.findIndex((item) => item.id === updateForm.value.id);
        if (index !== -1) {
          feedbackPageData.value[index] = { ...updateForm.value };
        }
        showUpdateDialog.value = false;
        ElMessage.success('修改成功！');
      } else {
        ElMessage.error('修改失败！');
      }
    })
    .catch((err) => {
      console.log(err);
      ElMessage.error('修改失败！');
    });
};

// 提交反馈
const submitFeedback = () => {
  feedbackAdd(newFeedback.value)
    .then((res) => {
      if (res.code === 200) {
        // 清空表单
        newFeedback.value = {
          name: '',
          email: '',
          title: '',
          content: ''
        };
        // 重新加载反馈列表
        feedbackPage({ pageNum: 1, pageSize: 100 })
          .then((res) => {
            if (res.code === 200) {
              feedbackPageData.value = res.data.data;
              ElMessage.success('反馈提交成功！');
            } else {
              ElMessage.error('获取反馈信息失败！');
            }
          })
          .catch((err) => {
            console.log(err);
            ElMessage.error('获取反馈信息失败！');
          });
      } else {
        ElMessage.error('反馈提交失败！');
      }
    })
    .catch((err) => {
      console.log(err);
      ElMessage.error('反馈提交失败！');
    });
};
</script>

<style scoped>
.feedback-management {
  padding: 20px;
}

h3 {
  margin-bottom: 10px;
}

p {
  margin-bottom: 20px;
}

.add-feedback-form {
  margin-top: 20px;
}

.el-form-item {
  margin-bottom: 15px;
}
</style>