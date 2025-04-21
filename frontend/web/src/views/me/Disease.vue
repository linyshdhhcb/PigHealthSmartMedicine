<template>
  <div class="disease-management">
    <h3>全部疾病</h3>
    <p>共收集 {{ illnessPageData.length }} 条疾病信息。你可以继续<a @click="addNewDisease">新增疾病</a>。</p>

    <!-- 疾病列表 -->
    <el-table :data="illnessPageData" style="width: 100%">
      <el-table-column prop="kindId" label="分类" width="80"></el-table-column>
      <el-table-column prop="illnessName" label="名称" width="120"></el-table-column>
      <el-table-column prop="illnessSymptom" label="症状"></el-table-column>
      <el-table-column prop="specialSymptom" label="特殊症状"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="150"></el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="handleView(scope.row)">
            <el-icon><View /></el-icon>
          </el-button>
          <el-button size="small" @click="handleEdit(scope.row)">
            <el-icon><Edit /></el-icon>
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">
            <el-icon><Delete /></el-icon>
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 30, 40]"
      @size-change="fetchIllnessData"
      @current-change="fetchIllnessData"
    />


    <!-- 查看疾病详情模态框 -->
    <el-dialog v-model="showViewDialog" title="疾病详情" width="50%">
      <el-form :model="viewForm" label-width="100px">
        <el-form-item label="ID">
          <el-input v-model="viewForm.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="viewForm.illnessName" disabled></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="viewForm.kindId" disabled></el-input>
        </el-form-item>
        <el-form-item label="症状">
          <el-input v-model="viewForm.illnessSymptom" disabled></el-input>
        </el-form-item>
        <el-form-item label="特殊症状">
          <el-input v-model="viewForm.specialSymptom" disabled></el-input>
        </el-form-item>
        <el-form-item label="更新时间">
          <el-input v-model="viewForm.updateTime" disabled></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showViewDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑疾病模态框 -->
    <el-dialog v-model="showEditDialog" title="修改疾病" width="50%">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="ID">
          <el-input v-model="editForm.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="editForm.illnessName"></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="editForm.kindId"></el-input>
        </el-form-item>
        <el-form-item label="症状">
          <el-input v-model="editForm.illnessSymptom"></el-input>
        </el-form-item>
        <el-form-item label="特殊症状">
          <el-input v-model="editForm.specialSymptom"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmEdit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 新增疾病模态框 -->
    <el-dialog v-model="showAddDialog" title="新增疾病" width="50%">
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="名称">
          <el-input v-model="addForm.illnessName"></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="addForm.kindId"></el-input>
        </el-form-item>
        <el-form-item label="症状">
          <el-input v-model="addForm.illnessSymptom"></el-input>
        </el-form-item>
        <el-form-item label="特殊症状">
          <el-input v-model="addForm.specialSymptom"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmAdd">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { illnessPage, getillnessInfo, illnessUpdate, illnessDelete, illnessAdd } from '@/api/admin/illness.js';

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const illnessPageData = ref([]);
const fetchIllnessData = () => {
  illnessPage({ pageNum: currentPage.value, pageSize: pageSize.value })
  .then(res => {
    if (res.code === 200) {
      illnessPageData.value = res.data.data;
      total.value = res.data.total;//确保后端返回的总条数为total
      console.log('illnessPageData.value', illnessPageData.value);
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试');
    }
  })
  .catch(err => {
    console.error(err);
    ElMessage.error(err.message || '请求失败，请稍后再试');
  })
  .finally(() => {
    // 加载完成后执行的逻辑
  });
}
fetchIllnessData();

// 新增疾病
const showAddDialog = ref(false);
const addForm = ref({});

const addNewDisease = () => {
  showAddDialog.value = true;
  addForm.value = {}; // 清空表单
};

const confirmAdd = () => {
  illnessAdd(addForm.value)
    .then(res => {
      if (res.code === 200) {
        illnessPageData.value.push(res.data);
        showAddDialog.value = false;
        ElMessage.success('新增成功！');
      } else {
        ElMessage.error(res.message || '新增失败');
      }
    })
    .catch(err => {
      console.error(err);
      ElMessage.error('新增失败');
    });
};

// 查看疾病详情
const showViewDialog = ref(false);
const viewForm = ref({});

const handleView = (row) => {
  getillnessInfo(row.id)
    .then(res => {
      if (res.code === 200) {
        viewForm.value = res.data;
        showViewDialog.value = true;
      } else {
        ElMessage.error(res.message || '获取疾病详情失败');
      }
    })
    .catch(err => {
      console.error(err);
      ElMessage.error('获取疾病详情失败');
    });
};

// 编辑疾病
const showEditDialog = ref(false);
const editForm = ref({});

const handleEdit = (row) => {
  editForm.value = { ...row };
  showEditDialog.value = true;
};

const confirmEdit = () => {
  illnessUpdate(editForm.value)
    .then(res => {
      if (res.code === 200) {
        const index = illnessPageData.value.findIndex(item => item.id === editForm.value.id);
        if (index !== -1) {
          illnessPageData.value[index] = { ...editForm.value };
        }
        showEditDialog.value = false;
        ElMessage.success('修改成功！');
      } else {
        ElMessage.error(res.message || '修改失败');
      }
    })
    .catch(err => {
      console.error(err);
      ElMessage.error('修改失败');
    });
};

// 删除疾病
const handleDelete = (row) => {
  ElMessageBox.confirm('此操作将永久删除该疾病, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    illnessDelete(row.id)
      .then(res => {
        if (res.code === 200) {
          const index = illnessPageData.value.findIndex(item => item.id === row.id);
          if (index !== -1) {
            illnessPageData.value.splice(index, 1);
          }
          ElMessage.success('删除成功！');
        } else {
          ElMessage.error(res.message || '删除失败');
        }
      })
      .catch(err => {
        console.error(err);
        ElMessage.error('删除失败');
      });
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
};
</script>

<style scoped>
.disease-management {
  padding: 20px;
}

h3 {
  margin-bottom: 10px;
}

p {
  margin-bottom: 20px;
}

a {
  color: #409eff;
  cursor: pointer;
}
</style>