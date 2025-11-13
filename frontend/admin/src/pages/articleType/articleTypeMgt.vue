<template>
  <el-card class="p-0" style="height: 100%;">
    <h1>文章类型管理模块</h1>

    <!-- 数据列表 -->
    <el-row class="el-row">
      <!-- 查询条件 -->
      <div class="w-full-box">
        <!-- 查询条件在同一行 -->
        <el-row :gutter="10" class="search" v-if="showSearchRow">
          <el-col :span="6">
            <el-form class="search-bar" :model="searchForm" inline label-position="left">
              <el-form-item label="文章类型名称">
                <el-input v-model="searchForm.typeName" placeholder="请输入文章类型名称" />
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>

        <!-- 查询、重置、添加、刷新、收缩/展开在同一行 -->
        <el-row :gutter="10" class="w-full mt-3">
          <el-col :span="12">
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="resetSearch">
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
          <div class="table-container">
            <el-table
              style="width: 100%; min-width: 800px; height: calc(100vh - 350px);"
              border
              :data="datatable.records"
              :loading="datatable.loading"
              :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
            >
              <el-table-column prop="typeName" label="文章类型名称" align="center" min-width="800" />
              <el-table-column label="操作" align="center" min-width="380" fixed="right" class-name="fixed-column">
                <template #default="scope">
                  <div class="acticon-button">
                    <el-space>
                      <!-- 查看详情 -->
                      <el-button type="info" @click="detailBtnClick(scope.row.typeId)">
                        <el-icon><View /></el-icon>
                        详情
                      </el-button>
                      <!-- 修改 -->
                      <el-button type="primary" @click="updateBtnClick(scope.row.typeId)">
                        <el-icon><Edit /></el-icon>
                        修改
                      </el-button>
                      <!-- 删除 -->
                      <el-popconfirm title="确认要删除吗?" @confirm="deleteBtnOkClick(scope.row.typeId)">
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
    <el-dialog 
      v-model="modal.visible" 
      :title="modal.title"
      width="600px" 
      draggable
      :close-on-click-modal="false"
    >
      <template #header>{{ modal.title }}</template>
      <component 
      :is="modal.component" 
      :params="modal.params" 
      @ok="onOk"
       @cancel="onCancel" 
       v-if="modal.visible" />
    </el-dialog>

    <!-- 详情 模态框 -->
    <!-- 详情对话框 -->
        <el-dialog 
          v-model="detailModal.visible" 
          :title="detailModal.title"
          width="600px" 
          draggable
        >

      <template #header>{{ detailModal.title }}</template>
      <component :is="detailModal.component" :params="detailModal.params" @cancel="detailOnCancel" v-if="detailModal.visible" />
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, shallowRef,onMounted  } from 'vue';
import { articleTypesAdd, articleTypesPage, articleTypesDelete } from '@/api/articleType.js';
import articleTypeEdit from '@/pages/articleType/articleTypeEdit.vue';
import articleTypeDetail from '@/pages/articleType/articleTypeDetail.vue';
import { ElMessage } from 'element-plus';
import { Search, Refresh, Plus, ArrowUp, ArrowDown, Edit, Delete, View } from '@element-plus/icons-vue';

const allData = ref([]); // 保存所有类型数据

// --- 模糊查询 ---
const handleSearch = () => {
  const q = searchForm.typeName?.trim().toLowerCase();
  if (!q) {
    ElMessage.info('请输入搜索关键词');
    return;
  }

  // 本地模糊过滤
  datatable.records = allData.value.filter(item =>
    item.typeName && item.typeName.toLowerCase().includes(q)
  );

  datatable.total = datatable.records.length;
};

const resetSearch = () => {
  searchForm.typeName = null;
  datatable.records = allData.value; // 恢复完整数据
  datatable.total = allData.value.length;
};



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

// 查询数据列表,相当于刷新
const getPageList = (isReset = false) => {
  if (isReset) {
    searchForm.typeName = null;
    searchForm.pageNum = 1;
    searchForm.pageSize = 10;
  }
  datatable.loading = true;

  // 调用分页接口
  articleTypesPage(searchForm)
    .then(res => {
      if (res.code === 200) {
        datatable.records = res.data.data;
        allData.value = res.data.data; // ✅ 备份完整数据
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

// 详情模态框
const detailModal = reactive({
  visible: false,
  title: '文章类型详情',
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

// 表格行"详情"按钮点击事件
const detailBtnClick = (typeId) => {
  detailModal.visible = true;
  detailModal.title = '文章类型详情';
  detailModal.params = { id: typeId };
  detailModal.component = shallowRef(articleTypeDetail);
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
onMounted(() => {
  getPageList();
});
</script>

<style scoped>

.el-row{
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}
.w-full-box{
  width: 100%;
 
}

.search{
  display: flex;
   /* background: skyblue; */
}
.search-bar{
  /* background: gray; */
  margin-left: -150%;
}

</style>