<template>
  <el-card class="p-0" style="height: 100%;">
    <h1>疾病-药品管理模块</h1>

    <!-- 数据列表 -->
    <el-row class="w-full h-full flex flex-col overflow-x-auto overflow-y-hidden">
      <!-- 查询条件 -->
      <div class="w-full" v-if="showSearchRow">
        <!-- 查询条件在同一行 -->
        <el-row :gutter="10" class="w-full">
          <el-col :span="5">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="疾病ID">
                <el-input v-model="searchForm.illnessId" placeholder="请输入疾病ID" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="5">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="药品ID">
                <el-input v-model="searchForm.medicineId" placeholder="请输入药品ID" />
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>

        <!-- 查询、重置、添加、刷新、收缩/展开在同一行 -->
        <el-row :gutter="10" class="w-full mt-3">
          <el-col :span="12">
            <el-button type="primary" @click="getPageList(false)">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="getPageList(true)">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-col>
          <el-col :span="12" style="text-align: right; display: flex; justify-content: flex-end;">
            <!-- 添加 -->
            <el-button type="primary" @click="addBtnClick">
              <el-icon><Plus /></el-icon>
              添加疾病-药品
            </el-button>

            <!-- 刷新 -->
            <el-button shape="circle" @click="getPageList(false)">
              <el-icon><Refresh /></el-icon>
            </el-button>

            <!-- 收缩/展开 -->
            <el-button shape="circle" @click="showSearchRow = !showSearchRow">
              <el-icon>
                <ArrowUp v-if="showSearchRow" />
                <ArrowDown v-else />
              </el-icon>
            </el-button>
          </el-col>
        </el-row>

        <el-divider v-if="showSearchRow" class="mt-2" />

        <!-- 数据展示区 -->
        <el-row class="w-full flex-1 mt-3 overflow-y-auto">
          <div class="table-container">
            <el-table
              style="width: 100%; min-width: 1200px; height: calc(100vh - 350px);"
              border
              :data="datatable.records"
              :loading="datatable.loading"
              :header-cell-style="{ background: '#f5f7fa', fontWeight: '600' }"
            >
              <el-table-column prop="illnessId" label="疾病ID" align="center" min-width="150" />
              <el-table-column prop="medicineId" label="药品ID" align="center" min-width="150" />
              <el-table-column prop="illnessName" label="疾病名称" align="center" min-width="300" />
              <el-table-column prop="medicineName" label="药品名称" align="center" min-width="300" />
              <el-table-column label="操作" align="center" min-width="300" fixed="right" class-name="fixed-column">
                <template #default="scope">
                  <div class="acticon-button">
                    <el-space>
                      <!-- 查看详情 -->
                      <el-button type="info" @click="detailBtnClick(scope.row.id)">
                        <el-icon><View /></el-icon>
                        详情
                      </el-button>
                      <!-- 修改 -->
                      <el-button type="primary" @click="updateBtnClick(scope.row.id)">
                        <el-icon><Edit /></el-icon>
                        修改
                      </el-button>
                      <!-- 删除 -->
                      <el-popconfirm title="确认要删除吗?" @confirm="deleteBtnOkClick(scope.row.id)">
                        <template #reference>
                          <el-button type="danger">
                            <el-icon><Delete /></el-icon>
                            删除
                          </el-button>
                        </template>
                      </el-popconfirm>
                    </el-space>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-row>

        <!-- 分页 -->
        <el-row class="w-full flex justify-end mt-2" style="margin: 10px auto;">
          <el-pagination
            v-if="datatable.total > 0"
            v-model:current-page="searchForm.pageNum"
            v-model:page-size="searchForm.pageSize"
            :total="datatable.total"
            :page-sizes="[10, 20, 50, 100, 200]"
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
          />
        </el-row>
      </div>
    </el-row>

    <!-- 添加/修改 模态框 -->
    <el-dialog v-model="modal.visible" fullscreen :close-on-click-modal="true" :close-on-press-escape="true" draggable>
      <template #header>{{ modal.title }}</template>
      <component :is="modal.component" :params="modal.params" @ok="onOk" @cancel="onCancel" v-if="modal.visible" />
    </el-dialog>

    <!-- 详情 模态框 -->
    <el-dialog v-model="detailModal.visible" fullscreen :close-on-click-modal="true" :close-on-press-escape="true" draggable>
      <template #header>{{ detailModal.title }}</template>
      <component :is="detailModal.component" :params="detailModal.params" @cancel="detailOnCancel" v-if="detailModal.visible" />
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, shallowRef, onMounted } from 'vue';
import { illnessMedicineAdd, illnessMedicinePage, illnessMedicineDelete, illnessMedicineUpdate, medicinePage, illnessPage } from '@/api/illnessMedicine.js';
import illnessMedicineEdit from '@/pages/illnessMedicine/illnessMedicineEdit.vue';
import illnessMedicineDetail from '@/pages/illnessMedicine/illnessMedicineDetail.vue';
import { ElMessage } from 'element-plus';
import { Search, Refresh, Plus, ArrowUp, ArrowDown, Edit, Delete, View } from '@element-plus/icons-vue';

// 是否展示搜索区域
const showSearchRow = ref(true);
// 查询参数表单
const searchForm = reactive({
  pageNum: 1,
  pageSize: 10,
  illnessId: null,
  medicineId: null,
});
// 数据列表配置
const datatable = reactive({
  records: [],
  loading: false,
  total: 0
});

// 查询数据列表
const getPageList = (isReset = false) => {
  if (isReset) {
    searchForm.illnessId = null;
    searchForm.medicineId = null;
    searchForm.pageNum = 1;
    searchForm.pageSize = 10;
  }
  datatable.loading = true;

  // 调用分页接口
  illnessMedicinePage(searchForm)
    .then(res => {
      if (res.code === 200) {
        const illnessMedicineData = res.data.data;

        // 获取所有需要的 medicineId 和 illnessId
        const medicineIds = Array.from(new Set(illnessMedicineData.map(item => item.medicineId)));
        const illnessIds = Array.from(new Set(illnessMedicineData.map(item => item.illnessId)));

        // 调用 medicinePage 和 illnessPage 获取详细信息
        return Promise.all([
          medicinePage({ ids: medicineIds.join(',') }),
          illnessPage({ ids: illnessIds.join(',') })
        ]).then(([medicineRes, illnessRes]) => {
          if (medicineRes.code === 200 && illnessRes.code === 200) {
            const medicineMap = medicineRes.data.data.reduce((map, item) => {
              map[item.id] = item.medicineName;
              return map;
            }, {});

            const illnessMap = illnessRes.data.data.reduce((map, item) => {
              map[item.id] = item.illnessName;
              return map;
            }, {});

            // 关联数据
            const records = illnessMedicineData.map(item => ({
              ...item,
              medicineName: medicineMap[item.medicineId] || '未知药品',
              illnessName: illnessMap[item.illnessId] || '未知疾病'
            }));

            datatable.records = records;
            datatable.total = res.data.total;
          } else {
            ElMessage.error(medicineRes.message || illnessRes.message || '获取详细信息失败');
          }
        });
      } else {
        ElMessage.error(res.message || '获取疾病-药品列表失败');
      }
    })
    .catch(error => {
      console.error('获取疾病-药品列表失败', error);
      ElMessage.error('获取疾病-药品列表失败');
    })
    .finally(() => {
      datatable.loading = false;
    });
};

const handlePageChange = (pageNum) => {
  searchForm.pageNum = pageNum;
  getPageList();
};
const handleSizeChange = (pageSize) => {
  searchForm.pageSize = pageSize;
  getPageList();
};

// 公共模态框
const modal = reactive({
  visible: false,
  title: '疾病-药品管理',
  params: {},
  component: null
});

// 详情模态框
const detailModal = reactive({
  visible: false,
  title: '疾病-药品详情',
  params: {},
  component: null
});

// 添加按钮点击事件
const addBtnClick = () => {
  modal.visible = true;
  modal.title = '添加疾病-药品';
  modal.params = { operationType: 'add' };
  modal.component = shallowRef(illnessMedicineEdit);
};

// 表格行"修改"按钮点击事件
const updateBtnClick = (id) => {
  modal.visible = true;
  modal.title = '修改疾病-药品';
  modal.params = { operationType: 'update', id };
  modal.component = shallowRef(illnessMedicineEdit);
};

// 表格行"删除"按钮点击事件
const deleteBtnOkClick = (id) => {
  illnessMedicineDelete(id)
    .then(response => {
      if (response.data) {
        ElMessage.success('删除成功');
        getPageList();
      } else {
        ElMessage.error(response.message || '删除失败');
      }
    })
    .catch(error => {
      console.error('删除操作失败', error);
      ElMessage.error('删除操作失败');
    });
};

// 表格行"详情"按钮点击事件
const detailBtnClick = (id) => {
  detailModal.visible = true;
  detailModal.title = '疾病-药品详情';
  detailModal.params = { id };
  detailModal.component = shallowRef(illnessMedicineDetail);
};

// 模态框确认回调
const onOk = () => {
  modal.visible = false;
  getPageList();
};

// 模态框取消回调
const onCancel = () => {
  modal.visible = false;
};

// 详情模态框取消回调
const detailOnCancel = () => {
  detailModal.visible = false;
};

// 初始查询数据列表
getPageList();
</script>

<style scoped>
/* 表格内容溢出时显示省略号 */
.ellipsis {
  display: inline-block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 表格容器样式 */
.table-container {
  overflow-x: auto;  /* 关键3：容器开启横向滚动 */
  position: relative;
}

/* 操作按钮容器 */
.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
  white-space: nowrap;
}

/* 保持表头固定 */
:deep(.el-table__header-wrapper) {
  position: sticky;
  top: 0;
  z-index: 3;
}

/* 横向滚动条样式 */
:deep(.el-table__body-wrapper)::-webkit-scrollbar {
  height: 8px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-track {
  background: #f1f1f1;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

/* 优化列宽设置 */
:deep(.el-table__body) td {
  white-space: nowrap;
}

:deep(.el-table__body) .cell {
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 新增关键样式 */
.table-container {
  overflow-x: auto;  /* 关键3：容器开启横向滚动 */
  position: relative;
}

/* 优化固定列样式 */
:deep(.fixed-column) {
  position: sticky !important;
  right: 0;
  z-index: 100;
  background: #fff;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.08);
  transition: box-shadow 0.3s;
}

/* 保证表头固定 */
:deep(.el-table__header-wrapper) {
  position: sticky;
  top: 0;
  z-index: 101;
  background: #fff;
}

/* 优化滚动条样式 */
.table-container::-webkit-scrollbar {
  height: 8px;
  width: 8px;
}

.table-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.table-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
  transition: background 0.3s;
}

.table-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 强制表格列不换行 */
:deep(.el-table__body) td .cell {
  white-space: nowrap;
}

/* 修复表头对齐问题 */
:deep(.el-table__header) {
  width: auto !important;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .table-container {
    min-width: 100%;
    overflow-x: scroll;
  }
  
  :deep(.fixed-column) {
    position: static !important;
    box-shadow: none;
  }
}

:deep(.el-table__fixed-right) {
  position: sticky !important;
  right: 0;
  z-index: 2;
  background: #fff;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
}
</style>