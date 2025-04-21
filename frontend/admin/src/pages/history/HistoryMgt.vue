<template>
  <el-card class="p-0">
    <h1>操作日志管理模块</h1>

    <!-- 数据列表 -->
    <el-row class="w-full h-full flex flex-col overflow-x-auto overflow-y-hidden">
      <!-- 查询条件 -->
      <div class="w-full" >
        <!-- 查询条件在同一行 -->
        <el-row :gutter="10" class="w-full" v-if="showSearchRow">
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left" >
              <el-form-item label="用户ID">
                <el-input v-model="searchForm.userId" placeholder="请输入用户ID" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left" >
              <el-form-item label="搜索关键字">
                <el-input v-model="searchForm.keyword" placeholder="请输入搜索关键字" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left" >
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
        <el-row class="w-full flex-1 mt-3 overflow-y-auto">
          <el-table
            class="w-full"
            :data="datatable.records"
            :loading="datatable.loading"
            style="width: 100%; table-layout: fixed; height: calc(100vh - 350px);"
            :fit="true"
            :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
          >
            <el-table-column prop="userId" label="用户ID" align="center" min-width="150" />
            <el-table-column prop="keyword" label="搜索关键字" align="center" min-width="200">
              <template #default="scope">
                <el-tooltip effect="dark" :content="scope.row.keyword" placement="top">
                  <span class="ellipsis">{{ scope.row.keyword }}</span>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="operateType" label="操作类型" align="center" min-width="150">
              <template #default="scope">
                {{ getOperateTypeName(scope.row.operateType) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="200" fixed="right">
              <template #default="scope">
                <el-space>
                  <!-- 查看详情 -->
                  <el-button type="info"  @click="detailBtnClick(scope.row.id)">
                    <el-icon><View /></el-icon>
                    详情
                  </el-button>
                  <!-- 修改 -->
                  <el-button type="primary"  @click="updateBtnClick(scope.row.id)">
                    <el-icon><Edit /></el-icon>
                    修改
                  </el-button>
                  <!-- 删除 -->
                  <el-popconfirm title="确认要删除吗?" @confirm="deleteBtnOkClick(scope.row.id)">
                    <template #reference>
                      <el-button type="danger" >
                        <el-icon><Delete /></el-icon>
                        删除
                      </el-button>
                    </template>
                  </el-popconfirm>
                </el-space>
              </template>
            </el-table-column>
          </el-table>

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
import { historyAdd, historyPage, historyDelete, historyUpdate } from '@/api/history.js';
import HistoryEdit from '@/pages/history/HistoryEdit.vue';
import HistoryDetail from '@/pages/history/HistoryDetail.vue';
import { ElMessage } from 'element-plus';
import { Search, Refresh, Plus, ArrowUp, ArrowDown, Edit, Delete, View } from '@element-plus/icons-vue';

// 是否展示搜索区域
const showSearchRow = ref(true);
// 查询参数表单
const searchForm = reactive({
  pageNum: 1,
  pageSize: 10,
  sortField: "",
  sortOrder: "",
  userId: null,
  keyword: null,
  operateType: null,
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
        datatable.records = res.data.data; // 将操作日志列表赋值给 datatable.records
        datatable.total = res.data.total; // 设置总记录数
      } else {
        ElMessage.error(res.message || '获取操作日志列表失败');
      }
    })
    .finally(() => {
      datatable.loading = false;
    });
};

// 处理页码变化
const handlePageChange = (val) => {
  searchForm.pageNum = val;
  getPageList();
};

// 处理每页显示条数变化
const handleSizeChange = (val) => {
  searchForm.pageSize = val;
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
  modal.params = { operationType: 'add' }; // 传递 operationType
  modal.component = shallowRef(HistoryEdit);
};

// 表格行"修改"按钮点击事件
const updateBtnClick = (id) => {
  modal.visible = true;
  modal.title = '修改操作记录';
  modal.params = { operationType: 'update', id }; // 传递 id 和 operationType
  modal.component = shallowRef(HistoryEdit);
};

// 表格行"删除"按钮点击事件
const deleteBtnOkClick = (id) => {
  historyDelete(id)
    .then(response => {
      console.log('删除响应:', response); // 打印响应
      if (response.data) { // 假设响应包含 data 属性
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
  // 刷新列表
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
    case 1:
      return '搜索';
    case 2:
      return '科目';
    case 3:
      return '药品';
    default:
      return '未知';
  }
};

// 初始查询数据列表
getPageList();

// 获取操作日志种类列表
onMounted(() => {
  // 如果需要操作日志种类列表，可以在这里获取
});
</script>

<style scoped>
/* 根据需要添加样式 */
.ellipsis {
  display: inline-block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>