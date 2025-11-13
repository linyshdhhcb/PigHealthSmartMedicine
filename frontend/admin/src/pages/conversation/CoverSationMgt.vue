<template>
  <el-card class="p-0">
    <h1>对话管理模块</h1>

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
              <el-form-item label="用户输入">
                <el-input v-model="searchForm.userInput" placeholder="请输入用户输入" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left" >
              <el-form-item label="AI回复">
                <el-input v-model="searchForm.aiResponse" placeholder="请输入AI回复" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left" >
              <el-form-item label="AI模型名称">
                <el-input v-model="searchForm.modelName" placeholder="请输入AI模型名称" />
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
              添加对话
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
            <el-table-column prop="userInput" label="用户输入" align="center" min-width="200">
              <template #default="scope">
                <el-tooltip effect="dark" :content="scope.row.userInput" placement="top">
                  <span class="ellipsis">{{ scope.row.userInput }}</span>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="aiResponse" label="AI回复" align="center" min-width="300">
              <template #default="scope">
                <el-tooltip effect="dark" :content="scope.row.aiResponse" placement="top">
                  <span class="ellipsis">{{ scope.row.aiResponse }}</span>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="modelName" label="AI模型名称" align="center" min-width="200" />
            <el-table-column prop="responseTime" label="AI响应时间（秒）" align="center" min-width="150" />
            <el-table-column label="操作" align="center" min-width="200" fixed="right">
              <template #default="scope">
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
import { conversationAdd, conversationPage, conversationDelete, conversationUpdate } from '@/api/conversation.js';
import ConverSationEdit from '@/pages/conversation/ConverSationEdit.vue';
import ConverSationDetail from '@/pages/conversation/ConverSationDetail.vue';
import { ElMessage } from 'element-plus';
import { Search, Refresh, Plus, ArrowUp, ArrowDown, Edit, Delete, View } from '@element-plus/icons-vue';

// 是否展示搜索区域
const showSearchRow = ref(true);
// 查询参数表单
const searchForm = reactive({
  pageNum: 1,
  pageSize: 1000,
  sortField: "",
  sortOrder: "",
  userId: null,
  userInput: null,
  aiResponse: null,
  modelName: null,
});
// 数据列表配置
const datatable = reactive({
  records: [],
  loading: false,
  total: 0
});

// 查询数据列表
// 查询数据列表（带模糊查询）
const getPageList = (isReset = false) => {
  if (isReset) {
    searchForm.userId = null;
    searchForm.userInput = null;
    searchForm.aiResponse = null;
    searchForm.modelName = null;
    searchForm.pageNum = 1;
    searchForm.pageSize = 100;
  }

  datatable.loading = true;

  // 调用分页接口
  conversationPage(searchForm)
    .then(res => {
      if (res.code === 200) {
        let dataList = res.data.data || [];

        // 模糊查询过滤逻辑
        const keywordUserId = searchForm.userId?.toString().trim().toLowerCase() || '';
        const keywordUserInput = searchForm.userInput?.trim().toLowerCase() || '';
        const keywordAI = searchForm.aiResponse?.trim().toLowerCase() || '';
        const keywordModel = searchForm.modelName?.trim().toLowerCase() || '';

        // 执行前端模糊过滤
        if (keywordUserId || keywordUserInput || keywordAI || keywordModel) {
          dataList = dataList.filter(item => {
            const userIdMatch =
              !keywordUserId || item.userId?.toString().toLowerCase().includes(keywordUserId);
            const inputMatch =
              !keywordUserInput || item.userInput?.toLowerCase().includes(keywordUserInput);
            const aiMatch =
              !keywordAI || item.aiResponse?.toLowerCase().includes(keywordAI);
            const modelMatch =
              !keywordModel || item.modelName?.toLowerCase().includes(keywordModel);
            return userIdMatch && inputMatch && aiMatch && modelMatch;
          });
        }

        datatable.records = dataList;
        datatable.total = dataList.length; // 更新总数
      } else {
        ElMessage.error(res.message || '获取对话列表失败');
      }
    })
    .finally(() => {
      datatable.loading = false;
    });
};


// 公共模态框
const modal = reactive({
  visible: false,
  title: '对话管理',
  params: {},
  component: null
});

// 详情模态框
const detailModal = reactive({
  visible: false,
  title: '对话详情',
  params: {},
  component: null
});

// 添加按钮点击事件
const addBtnClick = () => {
  modal.visible = true;
  modal.title = '添加对话';
  modal.params = { operationType: 'add' }; // 传递 operationType
  modal.component = shallowRef(ConverSationEdit);
};

// 表格行"修改"按钮点击事件
const updateBtnClick = (id) => {
  modal.visible = true;
  modal.title = '修改对话';
  modal.params = { operationType: 'update', id }; // 传递 id 和 operationType
  modal.component = shallowRef(ConverSationEdit);
};

// 表格行"删除"按钮点击事件
const deleteBtnOkClick = (id) => {
  conversationDelete(id)
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
  detailModal.title = '对话详情';
  detailModal.params = { id };
  detailModal.component = shallowRef(ConverSationDetail);
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

// 初始查询数据列表
getPageList();

// 获取对话种类列表
onMounted(() => {
  // 如果需要对话种类列表，可以在这里获取
});



const handlePageChange = (pageNum) => {
  searchForm.pageNum = pageNum;
  getPageList();
};

const handleSizeChange = (pageSize) => {
  searchForm.pageSize = pageSize;
  getPageList();
};

</script>

<style scoped>
.el-row{
  width: 1500px;
}
/* 根据需要添加样式 */
.ellipsis {
  display: inline-block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.w-full{
  width: 100%;
}
</style>