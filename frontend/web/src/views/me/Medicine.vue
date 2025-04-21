<template>
  <div class="medicine-management">
    <h3>全部药品</h3>
    <p>共收集 {{ total }} 条药品信息。你可以继续<a @click="addNewMedicine">新增药品</a>。</p>

    <!-- 药品列表 -->
    <el-table :data="medicinePageData" style="width: 100%">
      <el-table-column prop="medicineName" label="名称" width="200"></el-table-column>
      <el-table-column prop="medicineBrand" label="品牌" width="150"></el-table-column>
      <el-table-column prop="medicineType" label="类型" width="150"></el-table-column>
      <el-table-column prop="medicinePrice" label="价格" width="100"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="200"></el-table-column>
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
    <!-- 分页组件 -->
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 30, 40]"
      @size-change="fetchMedicineData"
      @current-change="fetchMedicineData"
    />

    <!-- 查看药品详情模态框 -->
    <el-dialog v-model="showViewDialog" title="药品详情" width="50%">
      <el-form :model="viewForm" label-width="120px">
        <el-form-item label="药品名称">
          <el-input v-model="viewForm.medicineName" placeholder="请输入药品名称" readonly />
        </el-form-item>
        <el-form-item label="关键字">
          <el-input v-model="viewForm.keyword" placeholder="请输入关键字" readonly />
        </el-form-item>
        <el-form-item label="药品功效">
          <el-input v-model="viewForm.medicineEffect" type="textarea" :rows="5" placeholder="请输入药品功效" readonly />
        </el-form-item>
        <el-form-item label="药品品牌">
          <el-input v-model="viewForm.medicineBrand" placeholder="请输入药品品牌" readonly />
        </el-form-item>
        <el-form-item label="药品类型">
          <el-input v-model="medicineTypeName" placeholder="药品类型" readonly />
        </el-form-item>
        <el-form-item label="相关图片路径">
          <el-input v-model="viewForm.imgPath" placeholder="请输入相关图片路径" readonly />
        </el-form-item>
        <el-form-item label="用法用量">
          <el-input v-model="viewForm.usAge" type="textarea" :rows="5" placeholder="请输入用法用量" readonly />
        </el-form-item>
        <el-form-item label="禁忌">
          <el-input v-model="viewForm.taboo" type="textarea" :rows="5" placeholder="请输入禁忌" readonly />
        </el-form-item>
        <el-form-item label="药品相互作用">
          <el-input v-model="viewForm.interaction" type="textarea" :rows="5" placeholder="请输入药品相互作用" readonly />
        </el-form-item>
        <el-form-item label="药品价格">
          <el-input-number v-model="viewForm.medicinePrice" :precision="2" :step="0.01" placeholder="请输入药品价格" readonly />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showViewDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑药品模态框 -->
    <el-dialog v-model="showEditDialog" title="修改药品" width="50%">
      <el-form :model="editForm" label-width="120px">
        <el-form-item label="药品名称">
          <el-input v-model="editForm.medicineName" placeholder="请输入药品名称" />
        </el-form-item>
        <el-form-item label="关键字">
          <el-input v-model="editForm.keyword" placeholder="请输入关键字" />
        </el-form-item>
        <el-form-item label="药品功效">
          <el-input v-model="editForm.medicineEffect" type="textarea" :rows="5" placeholder="请输入药品功效" />
        </el-form-item>
        <el-form-item label="药品品牌">
          <el-input v-model="editForm.medicineBrand" placeholder="请输入药品品牌" />
        </el-form-item>
        <el-form-item label="药品类型">
          <el-select v-model="editForm.medicineType" placeholder="请选择药品类型">
            <el-option label="西药" :value="0" />
            <el-option label="中药" :value="1" />
            <el-option label="中成药" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="相关图片路径">
          <el-input v-model="editForm.imgPath" placeholder="请输入相关图片路径" />
        </el-form-item>
        <el-form-item label="用法用量">
          <el-input v-model="editForm.usAge" type="textarea" :rows="5" placeholder="请输入用法用量" />
        </el-form-item>
        <el-form-item label="禁忌">
          <el-input v-model="editForm.taboo" type="textarea" :rows="5" placeholder="请输入禁忌" />
        </el-form-item>
        <el-form-item label="药品相互作用">
          <el-input v-model="editForm.interaction" type="textarea" :rows="5" placeholder="请输入药品相互作用" />
        </el-form-item>
        <el-form-item label="药品价格">
          <el-input-number v-model="editForm.medicinePrice" :precision="2" :step="0.01" placeholder="请输入药品价格" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmEdit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 新增药品模态框 -->
    <el-dialog v-model="showAddDialog" title="新增药品" width="50%">
      <el-form :model="addForm" label-width="120px">
        <el-form-item label="药品名称">
          <el-input v-model="addForm.medicineName" placeholder="请输入药品名称" />
        </el-form-item>
        <el-form-item label="关键字">
          <el-input v-model="addForm.keyword" placeholder="请输入关键字" />
        </el-form-item>
        <el-form-item label="药品功效">
          <el-input v-model="addForm.medicineEffect" type="textarea" :rows="5" placeholder="请输入药品功效" />
        </el-form-item>
        <el-form-item label="药品品牌">
          <el-input v-model="addForm.medicineBrand" placeholder="请输入药品品牌" />
        </el-form-item>
        <el-form-item label="药品类型">
          <el-select v-model="addForm.medicineType" placeholder="请选择药品类型">
            <el-option label="西药" :value="0" />
            <el-option label="中药" :value="1" />
            <el-option label="中成药" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="相关图片路径">
          <el-input v-model="addForm.imgPath" placeholder="请输入相关图片路径" />
        </el-form-item>
        <el-form-item label="用法用量">
          <el-input v-model="addForm.usAge" type="textarea" :rows="5" placeholder="请输入用法用量" />
        </el-form-item>
        <el-form-item label="禁忌">
          <el-input v-model="addForm.taboo" type="textarea" :rows="5" placeholder="请输入禁忌" />
        </el-form-item>
        <el-form-item label="药品相互作用">
          <el-input v-model="addForm.interaction" type="textarea" :rows="5" placeholder="请输入药品相互作用" />
        </el-form-item>
        <el-form-item label="药品价格">
          <el-input-number v-model="addForm.medicinePrice" :precision="2" :step="0.01" placeholder="请输入药品价格" />
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
import { ref, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { medicinePage, getmedicineInfo, medicineUpdate, medicineDelete, medicineAdd } from '@/api/admin/medicine.js';

const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

const medicinePageData = ref([]);

const fetchMedicineData = () => {
  medicinePage({ pageNum: currentPage.value, pageSize: pageSize.value })
    .then(res => {
      if (res.code === 200) {
        medicinePageData.value = res.data.data;
        total.value = res.data.total; // 假设后端返回的总条数字段为 total
        console.log('medicinePageData.value', medicinePageData.value);
      } else {
        ElMessage.error(res.message || '请求失败，请稍后再试');
      }
    })
    .catch(err => {
      console.error(err);
      ElMessage.error('请求失败，请稍后再试');
    })
    .finally(() => {
      // 加载完成后执行的逻辑
    });
};

fetchMedicineData();

// 新增药品
const showAddDialog = ref(false);
const addForm = ref({});

const addNewMedicine = () => {
  showAddDialog.value = true;
  addForm.value = {}; // 清空表单
};

const confirmAdd = () => {
  medicineAdd(addForm.value)
    .then(res => {
      if (res.code === 200) {
        medicinePageData.value.push(res.data);
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

// 查看药品详情
const showViewDialog = ref(false);
const viewForm = ref({});

const handleView = (row) => {
  getmedicineInfo(row.id)
    .then(res => {
      if (res.code === 200) {
        viewForm.value = res.data;
        showViewDialog.value = true;
      } else {
        ElMessage.error(res.message || '获取药品详情失败');
      }
    })
    .catch(err => {
      console.error(err);
      ElMessage.error('获取药品详情失败');
    });
};

// 编辑药品
const showEditDialog = ref(false);
const editForm = ref({});

const handleEdit = (row) => {
  editForm.value = { ...row };
  showEditDialog.value = true;
};

const confirmEdit = () => {
  medicineUpdate(editForm.value)
    .then(res => {
      if (res.code === 200) {
        const index = medicinePageData.value.findIndex(item => item.id === editForm.value.id);
        if (index !== -1) {
          medicinePageData.value[index] = { ...editForm.value };
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

// 删除药品
const handleDelete = (row) => {
  ElMessageBox.confirm('此操作将永久删除该药品, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    medicineDelete(row.id)
      .then(res => {
        if (res.code === 200) {
          const index = medicinePageData.value.findIndex(item => item.id === row.id);
          if (index !== -1) {
            medicinePageData.value.splice(index, 1);
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

// 计算药品类型名称
const medicineTypeName = computed(() => {
  const typeMap = {
    0: '西药',
    1: '中药',
    2: '中成药'
  };
  return typeMap[viewForm.value.medicineType] || '未知';
});
</script>

<style scoped>
.medicine-management {
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