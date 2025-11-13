<template>
  <el-card class="p-0" style="height: 100%;">
    <h1>新闻资讯管理模块</h1>

    <!-- 数据列表 -->
    <el-row class="w-full h-full flex flex-col overflow-x-auto overflow-y-hidden">
      <!-- 查询条件 -->
      <div class="w-full" v-if="showSearchRow">
        <!-- 新闻标题、作者、新闻来源查询 -->
        <el-row :gutter="10" class="w-full">
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="新闻标题">
                <el-input 
                  v-model="searchForm.title" 
                  placeholder="请输入新闻标题" 
                  style="width: 100%;"
                />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="作者">
                <el-input 
                  v-model="searchForm.author" 
                  placeholder="请输入作者" 
                  style="width: 100%;"
                />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="新闻来源">
                <el-input 
                  v-model="searchForm.source" 
                  placeholder="请输入新闻来源" 
                  style="width: 100%;"
                />
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>

        <!-- 操作按钮区 -->
        <el-row :gutter="10" class="w-full mt-3">
          <el-col :span="12">
            <el-button 
              type="primary" 
              @click="getPageList(false)"
              class="mr-2"
            >
              <el-icon><Search /></el-icon> 查询
            </el-button>
            <el-button 
              @click="getPageList(true)"
            >
              <el-icon><Refresh /></el-icon> 重置
            </el-button>
          </el-col>
          <el-col :span="12" style="text-align: right;">
            <el-button 
              type="primary" 
              @click="addBtnClick"
              class="mr-2"
            >
              <el-icon><Plus /></el-icon> 新增新闻
            </el-button>
            <el-button 
              shape="circle" 
              @click="getPageList(false)"
              class="mx-1"
            >
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button 
              shape="circle" 
              @click="showSearchRow = !showSearchRow"
              class="mx-1"
            >
              <el-icon>
                <ArrowUp v-if="showSearchRow" />
                <ArrowDown v-else />
              </el-icon>
            </el-button>
          </el-col>
        </el-row>

        <el-divider v-if="showSearchRow" class="mt-2 border-dashed" />

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
            <el-table-column 
              prop="title" 
              label="新闻标题" 
              align="center" 
              min-width="200"
            />
            <el-table-column 
              prop="author" 
              label="作者" 
              align="center" 
              min-width="100"
            />
            <el-table-column 
              prop="source" 
              label="新闻来源" 
              align="center" 
              min-width="200"
            />
            <el-table-column 
              prop="summary" 
              label="新闻摘要" 
              align="center" 
              min-width="200"
            >
              <template #default="scope">
                <el-tooltip 
                  effect="dark" 
                  :content="scope.row.summary" 
                  placement="top"
                >
                  <span class="ellipsis">{{ scope.row.summary }}</span>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column 
              prop="publishTime" 
              label="发布时间" 
              align="center" 
              min-width="180"
            />
            <el-table-column 
              label="操作" 
              align="center" 
              width="280" 
              fixed="right" 
              class-name="fixed-column"
            >
              <template #default="scope">
                <div class="action-buttons">
                  <el-space size="small">
                    <el-button 
                      type="info" 
                      @click="detailBtnClick(scope.row.id)"
                    >
                      <el-icon><View /></el-icon> 详情
                    </el-button>
                    <el-button 
                      type="primary" 
                      @click="updateBtnClick(scope.row.id)"
                    >
                      <el-icon><Edit /></el-icon> 修改
                    </el-button>
                    <el-popconfirm 
                      title="确认删除该新闻资讯？" 
                      @confirm="deleteBtnOkClick(scope.row.id)"
                    >
                      <template #reference>
                        <el-button type="danger">
                          <el-icon><Delete /></el-icon> 删除
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
        <el-row class="w-full flex justify-end mt-2">
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
    <el-dialog 
      v-model="modal.visible" 
      fullscreen 
      :close-on-click-modal="false" 
      draggable
    >
      <template #header>
        <div class="flex justify-between items-center">
          <span>{{ modal.title }}</span>
          <el-button 
            type="text" 
            @click="onCancel"
          >
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
      </template>
      <component 
        :is="modal.component" 
        :params="modal.params" 
        @ok="onOk" 
        @cancel="onCancel" 
        v-if="modal.visible" 
      />
    </el-dialog>

    <!-- 详情 模态框 -->
    <el-dialog 
      v-model="detailModal.visible" 
      fullscreen 
      :close-on-click-modal="false" 
      draggable
    >
      <template #header>
        <div class="flex justify-between items-center">
          <span>{{ detailModal.title }}</span>
          <el-button 
            type="text" 
            @click="detailOnCancel"
          >
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
      </template>
      <component 
        :is="detailModal.component" 
        :params="detailModal.params" 
        @cancel="detailOnCancel" 
        v-if="detailModal.visible" 
      />
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, shallowRef, onMounted } from 'vue';
import { 
  newsArticlesAdd, 
  newsArticlesPage, 
  newsArticlesDelete, 
  newsArticlesUpdate 
} from '@/api/newsArticles.js';
import newsArticlesEdit from '@/pages/newsArticles/newsArticlesEdit.vue';
import newsArticlesDetail from '@/pages/newsArticles/newsArticlesDetail.vue';
import { ElMessage } from 'element-plus';
import { 
  Search, Refresh, Plus, ArrowUp, ArrowDown, 
  Edit, Delete, View, Close 
} from '@element-plus/icons-vue';

// 是否展示搜索区域
const showSearchRow = ref(true);
// 查询参数表单
const searchForm = reactive({
  pageNum: 1,
  pageSize: 10,
  title: null,
  author: null,
  source: null
});
// 数据列表配置
const datatable = reactive({
  records: [],
  loading: false,
  total: 0
});
// 公共模态框
const modal = reactive({
  visible: false,
  title: '新闻资讯管理',
  params: {},
  component: null
});
// 详情模态框
const detailModal = reactive({
  visible: false,
  title: '新闻资讯详情',
  params: {},
  component: null
});

// 查询数据列表
const getPageList = (isReset = false) => {
  if (isReset) {
    searchForm.title = null;
    searchForm.author = null;
    searchForm.source = null;
    searchForm.pageNum = 1;
    searchForm.pageSize = 10;
  }
  datatable.loading = true;

  newsArticlesPage(searchForm)
    .then(res => {
      if (res.code === 200) {
        datatable.records = res.data.data;
        datatable.total = res.data.total;
      } else {
        ElMessage.error(res.message || '获取新闻资讯列表失败');
      }
    })
    .finally(() => datatable.loading = false);
};

// 分页处理
const handlePageChange = (pageNum) => {
  searchForm.pageNum = pageNum;
  getPageList();
};

const handleSizeChange = (pageSize) => {
  searchForm.pageSize = pageSize;
  getPageList();
};

// 按钮事件
const addBtnClick = () => {
  modal.visible = true;
  modal.title = '新增新闻资讯';
  modal.component = shallowRef(newsArticlesEdit);
  modal.params = { operationType: 'add' };
};

const updateBtnClick = (id) => {
  modal.visible = true;
  modal.title = '修改新闻资讯';
  modal.component = shallowRef(newsArticlesEdit);
  modal.params = { operationType: 'update', id };
};

const deleteBtnOkClick = (id) => {
  newsArticlesDelete(id)
    .then(response => {
      if (response.code === 200) {
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

const detailBtnClick = (id) => {
  detailModal.visible = true;
  detailModal.component = shallowRef(newsArticlesDetail);
  detailModal.params = { id };
};

// 模态框回调
const onOk = () => {
  modal.visible = false;
  getPageList();
};

const onCancel = () => {
  modal.visible = false;
};

const detailOnCancel = () => {
  detailModal.visible = false;
};

// 初始化
onMounted(() => {
  getPageList();
});
</script>

<style scoped>
/* 继承文件管理模块基础样式 */
/* @import './filesMgt.css'; */

/* 自定义样式 */
.ellipsis {
  display: inline-block;
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

:deep(.fixed-column) {
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.08);
  background: #ffffff;
}

:deep(.el-table__header th) {
  padding: 16px 8px;
  font-size: 14px;
  color: #303133;
}

:deep(.el-table__body td) {
  padding: 12px 8px;
  vertical-align: middle;
}

/* 响应式调整 */
@media (max-width: 1440px) {
  .el-col {
    flex: 0 0 48% !important;
    max-width: 48% !important;
  }
}


.w-full{
  width: 100%;
}
</style>