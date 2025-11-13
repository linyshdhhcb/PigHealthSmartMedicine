<template>
  <el-card class="p-0">
    <h1>用户管理模块</h1>

    <!-- 数据列表 -->
    <el-row class="w-full h-full flex flex-col overflow-x-auto overflow-y-hidden">
      <!-- 查询条件 -->
      <div class="w-full">
        <!-- 查询条件在同一行 -->
        <el-row :gutter="10" class="w-full" v-if="showSearchRow">
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="用户名">
                <el-input v-model="searchForm.userAccount" placeholder="请输入用户名" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left">
              <el-form-item label="真实姓名">
                <el-input v-model="searchForm.userName" placeholder="请输入完整姓名" />
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
            <el-button type="primary" @click="addBtnClick()">
              <el-icon><Plus /></el-icon>
              添加用户
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
              fixed
              prop="userAccount" 
              label="用户名" 
              align="center" 
              min-width="200"
            />
            <el-table-column 
              prop="userName" 
              label="真实姓名" 
              align="center" 
              min-width="180"
            />
            <el-table-column 
              prop="userAge" 
              label="年龄" 
              align="center" 
              min-width="100"
            />
            <el-table-column 
              prop="userSex" 
              label="性别" 
              align="center" 
              min-width="100"
            />
            <el-table-column 
              prop="userEmail" 
              label="邮箱" 
              align="center" 
              min-width="240"
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
                      type="primary" 
                      @click="updateBtnClick(scope.row.id)"
                    >
                      <el-icon><Edit /></el-icon> 修改
                    </el-button>
                    <el-popconfirm 
                      title="确认删除该用户？" 
                      @confirm="deleteBtnOkClick(scope.row.id)"
                    >
                      <template #reference>
                        <el-button type="danger">
                          <el-icon><Delete /></el-icon> 删除
                        </el-button>
                      </template>
                    </el-popconfirm>
                    <el-button 
                      type="info" 
                      @click="detailBtnClick(scope.row.id)"
                    >
                      <el-icon><View /></el-icon> 详情
                    </el-button>
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
import { ref, reactive, getCurrentInstance, shallowRef,onMounted } from 'vue';
import { userPage, userDelete } from '@/api/user.js';
import UserEdit from './UserEdit.vue';
import UserDetail from './UserDetail.vue';
import { ElMessage } from 'element-plus';
import { 
  Search, Refresh, Plus, ArrowUp, ArrowDown, 
  Edit, Delete, View, Close 
} from '@element-plus/icons-vue';

const { proxy } = getCurrentInstance();
const showSearchRow = ref(true);
const searchForm = reactive({
  userAccount: null,
  userName: null,
  pageNum: 1,
  pageSize: 100,
});
const datatable = reactive({
  loading: false,
  records: [],
  total: 0,
});

const getPageList = (isReset = false) => {
  if (isReset) {
    searchForm.userAccount = null;
    searchForm.userName = null;
    searchForm.pageNum = 1;
    searchForm.pageSize = 100;
  }
  datatable.loading = true;
  userPage(searchForm)
    .then(res => {
      datatable.records = res.data.data;
      datatable.total = res.data.total;
    })
    .finally(() => datatable.loading = false);
};

const handlePageChange = (pageNum) => {
  searchForm.pageNum = pageNum;
  getPageList();
};

const handleSizeChange = (pageSize) => {
  searchForm.pageSize = pageSize;
  getPageList();
};

const modal = reactive({
  visible: false,
  title: '用户管理',
  params: {},
  component: null,
});

const detailModal = reactive({
  visible: false,
  title: '用户详情',
  params: {},
  component: null,
});

const addBtnClick = () => {
  modal.visible = true;
  modal.title = '添加用户';
  modal.component = shallowRef(UserEdit);
  modal.params = { operationType: 'add' };
};

const updateBtnClick = (id) => {
  modal.visible = true;
  modal.title = '修改用户';
  modal.component = shallowRef(UserEdit);
  modal.params = { operationType: 'update', id };
};

const deleteBtnOkClick = (id) => {
  userDelete(id)
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
  detailModal.component = shallowRef(UserDetail);
  detailModal.params = { id };
};

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

/* 用户管理自定义样式 */
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

.w-full{
  width: 100%;
}
</style>