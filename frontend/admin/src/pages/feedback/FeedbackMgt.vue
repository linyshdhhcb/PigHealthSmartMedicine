<template>
  <el-card class="p-0" style="height: 100%;">
    <h1>反馈管理模块</h1>

    <!-- 数据列表 -->
    <el-row class="w-full h-full flex flex-col overflow-x-auto overflow-y-hidden">
      <!-- 查询条件 -->
      <div class="w-full" v-if="showSearchRow">
        <!-- 查询条件在同一行 -->
        <el-row :gutter="10" class="w-full">
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="反馈用户">
                <el-input v-model="searchForm.name" placeholder="请输入反馈用户" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="邮箱地址">
                <el-input v-model="searchForm.email" placeholder="请输入邮箱地址" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="反馈标题">
                <el-input v-model="searchForm.title" placeholder="请输入反馈标题" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="反馈内容">
                <el-input v-model="searchForm.content" placeholder="请输入反馈内容" />
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
              添加反馈
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
        <div class="table-container overflow-x-auto">
          <el-table
            class="min-w-full"
            :data="datatable.records"
            :loading="datatable.loading"
            style="height: calc(100vh - 350px);"
            border
            :header-cell-style="{ background: '#f5f7fa', fontWeight: '600' }"
          >
            <el-table-column fixed prop="name" label="反馈用户" align="center" min-width="180" />
            <el-table-column prop="email" label="邮箱地址" align="center" min-width="220" />
            <el-table-column prop="title" label="反馈标题" align="center" min-width="200" />
            <el-table-column prop="content" label="反馈内容" align="center" min-width="300">
              <template #default="scope">
                <el-tooltip effect="dark" :content="scope.row.content" placement="top">
                  <span class="ellipsis">{{ scope.row.content }}</span>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="280" fixed="right" class-name="fixed-column">
              <template #default="scope">
                <div class="action-buttons">
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
import { feedbackAdd, feedbackPage, feedbackDelete, feedbackUpdate } from '@/api/feedback.js';
import FeedbackEdit from '@/pages/feedback/FeedbackEdit.vue';
import FeedbackDetail from '@/pages/feedback/FeedbackDetail.vue';
import { ElMessage } from 'element-plus';
import { Search, Refresh, Plus, ArrowUp, ArrowDown, Edit, Delete, View } from '@element-plus/icons-vue';

// 是否展示搜索区域
const showSearchRow = ref(true);
// 查询参数表单
const searchForm = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null,
  email: null,
  title: null,
  content: null,
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
    searchForm.name = null;
    searchForm.email = null;
    searchForm.title = null;
    searchForm.content = null;
    searchForm.pageNum = 1;
    searchForm.pageSize = 10;
  }
  datatable.loading = true;

  feedbackPage(searchForm)
    .then(res => {
      if (res.code === 200) {
        datatable.records = res.data.data;
        datatable.total = res.data.total;
      } else {
        ElMessage.error(res.message || '获取反馈列表失败');
      }
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
  title: '反馈管理',
  params: {},
  component: null
});

// 详情模态框
const detailModal = reactive({
  visible: false,
  title: '反馈详情',
  params: {},
  component: null
});

// 添加按钮点击事件
const addBtnClick = () => {
  modal.visible = true;
  modal.title = '添加反馈';
  modal.params = { operationType: 'add' };
  modal.component = shallowRef(FeedbackEdit);
};

// 修改按钮点击事件
const updateBtnClick = (id) => {
  modal.visible = true;
  modal.title = '修改反馈';
  modal.params = { operationType: 'update', id };
  modal.component = shallowRef(FeedbackEdit);
};

// 删除按钮点击事件
const deleteBtnOkClick = (id) => {
  feedbackDelete(id)
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

// 详情按钮点击事件
const detailBtnClick = (id) => {
  detailModal.visible = true;
  detailModal.title = '反馈详情';
  detailModal.params = { id };
  detailModal.component = shallowRef(FeedbackDetail);
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

// 初始查询数据
getPageList();
</script>

<style scoped>
/* 继承 filesMgt.vue 样式规范 */
.ellipsis {
  display: inline-block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.table-container {
  width: 100%;
  overflow-x: auto;
}

:deep(.fixed-column) {
  position: sticky !important;
  right: 0;
  z-index: 100;
  background: #fff;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.08);
}

:deep(.el-table__header-wrapper) {
  position: sticky;
  top: 0;
  z-index: 101;
  background: #f5f7fa;
}

:deep(.el-table__body) td {
  white-space: nowrap;
}

:deep(.el-table__body .cell) {
  overflow: hidden;
  text-overflow: ellipsis;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
  white-space: nowrap;
}

/* 滚动条样式 */
:deep(.table-container::-webkit-scrollbar) {
  height: 8px;
  width: 8px;
}

:deep(.table-container::-webkit-scrollbar-track) {
  background: #f1f1f1;
  border-radius: 4px;
}

:deep(.table-container::-webkit-scrollbar-thumb) {
  background: #c1c1c1;
  border-radius: 4px;
}

:deep(.table-container::-webkit-scrollbar-thumb:hover) {
  background: #a8a8a8;
}

.w-full{
  width: 100%;
}
</style>