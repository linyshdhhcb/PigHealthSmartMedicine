<template>
  <el-card class="p-0">
    <h1>用户管理模块</h1>
    <!-- 数据列表 -->
    <el-row class="h-full flex flex-col overflow-x-auto overflow-y-hidden">
      <!-- 查询条件 -->
      <div class="w-full">
        <!-- 查询条件在同一行 -->
        <el-row :gutter="10"  class="w-full"  v-if="showSearchRow">
          <el-col :span="6">
            <el-form :model="searchForm"  inline label-position="left" >
              <el-form-item label="用户名">
                <el-input v-model="searchForm.userAccount" placeholder="请输入用户名" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-form :model="searchForm"  >
              <el-form-item label="真实姓名">
                <el-input v-model="searchForm.userName" placeholder="请输入真实姓名" />
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>

        <!-- 查询、重置、添加、刷新、收缩/展开在同一行 -->
        <el-row :gutter="10" class="w-full mt-3">
          <el-col :span="12">
            <el-button type="primary" @click="getPageList(false)">
              <template #icon>
                <el-icon><Search /></el-icon>
              </template>
              查询
            </el-button>
            <el-button @click="getPageList(true)">
              <template #icon>
                <el-icon><Refresh /></el-icon>
              </template>
              重置
            </el-button>
          </el-col>
          <el-col :span="12" style="text-align: right; display: flex; justify-content: flex-end;">
            <!-- 添加 -->
            <el-button type="primary" @click="addBtnClick()">
              <template #icon>
                <el-icon><Plus /></el-icon>
              </template>
              添加用户
            </el-button>

            <!-- 刷新 -->
            <el-button shape="circle" @click="getPageList(false)">
              <template #icon>
                <el-icon><Refresh /></el-icon>
              </template>
            </el-button>

            <!-- 收缩/展开 -->
            <el-button shape="circle" @click="showSearchRow = !showSearchRow">
              <template #icon>
                <el-icon v-if="showSearchRow">
                  <ArrowDown />
                </el-icon>
                <el-icon v-else>
                  <ArrowUp />
                </el-icon>
              </template>
            </el-button>
          </el-col>
        </el-row>

        <el-divider v-if="showSearchRow" class="mt-2" />

        <!-- 数据展示区 -->
        <el-row class="w-full flex-1 mt-3 overflow-y-auto" style="margin: 10px auto;">
          <el-table
            class="w-full"
            :data="datatable.records"
            :loading="datatable.loading"
            style="width: 100%; table-layout: fixed; height: calc(100vh - 350px);"
            :fit="true"
            :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
          >
            <el-table-column prop="userAccount" label="用户名" min-width="150" align="center" />
            <el-table-column prop="userName" label="真实姓名" min-width="150" align="center" />
            <el-table-column prop="userAge" label="年龄" min-width="100" align="center" />
            <el-table-column prop="userSex" label="性别" min-width="100" align="center" />
            <el-table-column prop="userEmail" label="邮箱" min-width="200" align="center" />
            <el-table-column label="操作" min-width="160" fixed="right" align="center">
              <template #default="scope">
                <el-space>
                  <el-button type="text"  @click="updateBtnClick(scope.row.id)">
                    <template #icon>
                      <el-icon><Edit /></el-icon>
                    </template>
                    修改
                  </el-button>
                  <el-popconfirm content="确认要删除吗?" @confirm="deleteBtnOkClick(scope.row.id)">
                    <template #reference>
                      <el-button type="text"  status="danger">
                        <template #icon>
                          <el-icon><Delete /></el-icon>
                        </template>
                        删除
                      </el-button>
                    </template>
                  </el-popconfirm>
                  <el-button type="text"  @click="detailBtnClick(scope.row.id)">
                    <template #icon>
                      <el-icon><View /></el-icon>
                    </template>
                    详情
                  </el-button>
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
    <el-dialog v-model="modal.visible" fullscreen :esc-close="true" :modal="true" draggable :footer="false">
      <template #header>
        <span>{{ modal.title }}</span>
      </template>
      <component :is="modal.component" :params="modal.params" @ok="onOk" @cancel="onCancel" v-if="modal.visible" />
    </el-dialog>

    <!-- 详情 模态框 -->
    <el-dialog v-model="detailModal.visible" fullscreen :esc-close="true" :modal="true" draggable :footer="false">
      <template #header>
        <span>{{ detailModal.title }}</span>
      </template>
      <component :is="detailModal.component" :params="detailModal.params" @cancel="detailOnCancel" v-if="detailModal.visible" />
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, shallowRef } from 'vue';
import { userPage, userDelete } from '@/api/user.js';
import UserEdit from './UserEdit.vue';
import UserDetail from './UserDetail.vue';
import { ElMessage } from 'element-plus';
import { Search, Refresh, Plus, ArrowUp, ArrowDown, Edit, Delete, View } from '@element-plus/icons-vue';

// 获取全局实例
const { proxy } = getCurrentInstance();

// 是否展示搜索区域
const showSearchRow = ref(true);
// 查询参数表单
const searchForm = reactive({
  userAccount: null,
  userName: null,
  pageNum: 1,
  pageSize: 100,
});
// 数据列表配置
const datatable = reactive({
  loading: false,
  records: [],
  total: 0,
});

// 加载中
const spinLoading = ref(false);

// 查询数据列表
const getPageList = (isReset = false) => {
  if (isReset) {
    searchForm.userAccount = null;
    searchForm.userName = null;
    searchForm.pageNum = 1;
    searchForm.pageSize = 100;
  }
  datatable.loading = true;
  spinLoading.value = true; // 设置加载状态

  // 调用分页接口
  userPage(searchForm)
    .then(res => {
      datatable.records = res.records;
      datatable.total = res.total;
    })
    .finally(() => {
      datatable.loading = false;
      spinLoading.value = false; // 关闭加载状态
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
  title: '用户管理',
  params: {},
  component: null,
});

// 详情模态框
const detailModal = reactive({
  visible: false,
  title: '用户详情',
  params: {},
  component: null,
});

// 添加按钮点击事件
const addBtnClick = () => {
  modal.visible = true;
  modal.title = '添加用户';
  modal.params = { operationType: 'add' }; // 传递 operationType
  modal.component = shallowRef(UserEdit);
};

// 表格行"修改"按钮点击事件
const updateBtnClick = (id) => {
  modal.visible = true;
  modal.title = '修改用户';
  modal.params = { operationType: 'update', id }; // 传递 id 和 operationType
  modal.component = shallowRef(UserEdit);
};

// 表格行"删除"按钮点击事件
const deleteBtnOkClick = (id) => {
  userDelete(id)
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
  detailModal.title = '用户详情';
  detailModal.params = { id };
  detailModal.component = shallowRef(UserDetail);
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
</script>

<style scoped>

</style>