<template>
  <el-card class="p-0">
    <h1>文章类型管理模块</h1>

    <!-- 数据列表 -->
    <el-row class="w-full h-full flex flex-col overflow-x-auto overflow-y-hidden">
      <!-- 查询条件 -->
      <div class="w-full" >
        <!-- 查询条件在同一行 -->
        <el-row :gutter="10" class="w-full" v-if="showSearchRow">
          <el-col :span="6">
            <el-form :model="searchForm" inline label-position="left" >
              <el-form-item label="文章类型名称">
                <el-input v-model="searchForm.typeName" placeholder="请输入文章类型名称" />
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
              添加文章类型
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
            <el-table-column prop="typeName" label="文章类型名称" align="center" min-width="200" />
            <el-table-column label="操作" align="center" min-width="160" fixed="right">
              <template #default="scope">
                <el-space>
                  <!-- 修改 -->
                  <el-button type="primary"  @click="updateBtnClick(scope.row.typeId)">
                    <el-icon><Edit /></el-icon>
                    修改
                  </el-button>
                  <!-- 删除 -->
                  <el-popconfirm title="确认要删除吗?" @confirm="deleteBtnOkClick(scope.row.typeId)">
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
  </el-card>
</template>

<script setup>
import { ref, reactive, shallowRef } from 'vue';
import { articleTypesAdd, articleTypesPage, articleTypesDelete, articleTypesUpdate } from '@/api/articleType.js';
import articleTypeEdit from '@/pages/articleType/articleTypeEdit.vue';
import { ElMessage } from 'element-plus';
import { Search, Refresh, Plus, ArrowUp, ArrowDown, Edit, Delete } from '@element-plus/icons-vue';

// 是否展示搜索区域
const showSearchRow = ref(true);
// 查询参数表单
const searchForm = reactive({
  typeName: null,
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
    searchForm.typeName = null;
    searchForm.pageNum = 1;
    searchForm.pageSize = 100;
  }
  datatable.loading = true;

  // 调用分页接口
  articleTypesPage(searchForm)
    .then(res => {
      if (res.code === 200) {
        datatable.records = res.data.data;
        datatable.total = res.data.total;
      } else {
        ElMessage.error(res.message || '获取文章类型列表失败');
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
  title: '文章类型管理',
  params: {},
  component: null
});

// 添加按钮点击事件
const addBtnClick = () => {
  modal.visible = true;
  modal.title = '添加文章类型';
  modal.params = { operationType: 'add' };
  modal.component = shallowRef(articleTypeEdit);
};

// 表格行"修改"按钮点击事件
const updateBtnClick = (typeId) => {
  modal.visible = true;
  modal.title = '修改文章类型';
  modal.params = { operationType: 'update', id: typeId };
  modal.component = shallowRef(articleTypeEdit);
};

// 表格行"删除"按钮点击事件
const deleteBtnOkClick = (typeId) => {
  articleTypesDelete(typeId)
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

// 模态框确认回调
const onOk = () => {
  modal.visible = false;
  getPageList();
};

// 模态框取消回调
const onCancel = () => {
  modal.visible = false;
};

// 初始查询数据列表
getPageList();
</script>

<style scoped>
/* 根据需要添加样式 */
</style>