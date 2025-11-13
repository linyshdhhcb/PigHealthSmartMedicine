<template>
  <el-card class="p-0" style="height: 100%;">
    <h1>操作日志管理模块</h1>

    <!-- 数据列表 -->
    <el-row class="w-full h-full flex flex-col overflow-x-auto overflow-y-hidden">
      <!-- 查询条件 -->
      <div class="w-full">
        <!-- 查询条件在同一行 -->
        <el-row :gutter="10" class="w-full" v-if="showSearchRow">
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="用户ID">
                <el-input v-model="searchForm.userId" placeholder="请输入用户ID" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="搜索关键字">
                <el-input v-model="searchForm.keyword" placeholder="请输入搜索关键字" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="操作类型">
                <el-select style="width: 200px;" v-model="searchForm.operateType" placeholder="请选择操作类型">
                  <el-option label="搜索" value="1" />
                  <el-option label="科目" value="2" />
                  <el-option label="药品" value="3" />
                </el-select>
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
              添加操作记录
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
        <el-row class="w-full flex-1 mt-3 overflow-y-auto"  style="width: 100%;">
          <div class="table-container"   style="width: 100%;">
            <el-table
              style="width: 100%; min-width: 900px; height: calc(100vh - 350px);"
              border
              :data="datatable.records"
              :loading="datatable.loading"
              :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
            >
              <el-table-column prop="userId" label="用户ID" align="center" min-width="150" />
              <el-table-column prop="keyword" label="搜索关键字" align="center" min-width="400">
                <template #default="scope">
                  <el-tooltip effect="dark" :content="scope.row.keyword" placement="top">
                    <span class="ellipsis">{{ scope.row.keyword }}</span>
                  </el-tooltip>
                </template>
              </el-table-column>
              <el-table-column prop="operateType" label="操作类型" align="center" min-width="300">
                <template #default="scope">
                  {{ getOperateTypeName(scope.row.operateType.toString()) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" min-width="280" fixed="right" class-name="fixed-column">
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
import { historyPage, historyDelete } from '@/api/history.js';
import HistoryEdit from '@/pages/history/HistoryEdit.vue';
import HistoryDetail from '@/pages/history/HistoryDetail.vue';
import { ElMessage } from 'element-plus';
import { Search, Refresh, Plus, ArrowUp, ArrowDown, Edit, Delete, View } from '@element-plus/icons-vue';


// 是否展示搜索区域
const showSearchRow = ref(true);
// 查询参数表单
const searchForm = reactive({
  userId: null,
  keyword: null,
  operateType: null,
  pageNum: 1,
  pageSize: 10
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
    searchForm.userId = null;
    searchForm.keyword = null;
    searchForm.operateType = null;
    searchForm.pageNum = 1;
    searchForm.pageSize = 10;
  }
  datatable.loading = true;

  // 调用分页接口
  historyPage(searchForm)
    .then(res => {
      if (res.code === 200) {
        datatable.records = res.data.data;
        datatable.total = res.data.total;
      } else {
        ElMessage.error(res.message || '获取操作日志列表失败');
      }
    })
    .finally(() => {
      datatable.loading = false;
    });
};

// 处理页码变化
const handlePageChange = (pageNum) => {
  searchForm.pageNum = pageNum;
  getPageList();
};
// 处理每页显示条数变化
const handleSizeChange = (pageSize) => {
  searchForm.pageSize = pageSize;
  getPageList();
};

// 公共模态框
const modal = reactive({
  visible: false,
  title: '操作日志管理',
  params: {},
  component: null
});

// 详情模态框
const detailModal = reactive({
  visible: false,
  title: '操作日志详情',
  params: {},
  component: null
});

// 添加按钮点击事件
const addBtnClick = () => {
  modal.visible = true;
  modal.title = '添加操作记录';
  modal.params = { operationType: 'add' };
  modal.component = shallowRef(HistoryEdit);
};

// 表格行"修改"按钮点击事件
const updateBtnClick = (id) => {
  modal.visible = true;
  modal.title = '修改操作记录';
  modal.params = { operationType: 'update', id };
  modal.component = shallowRef(HistoryEdit);
};

// 表格行"删除"按钮点击事件
const deleteBtnOkClick = (id) => {
  historyDelete(id)
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
  detailModal.title = '操作日志详情';
  detailModal.params = { id };
  detailModal.component = shallowRef(HistoryDetail);
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

// 获取操作类型名称
const getOperateTypeName = (type) => {
  switch (type) {
    case '1':
      return '搜索';
    case '2':
      return '科目';
    case '3':
      return '药品';
    default:
      return '未知';
  }
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
  background: #fff;
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


.w-full{
  width: 100%;
}
</style>