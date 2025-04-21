<template>
  <el-card class="p-0" style="height: 100vh; display: flex; flex-direction: column;">
    <h1 style="margin-bottom: 20px;">文章管理模块</h1>

    <!-- 数据列表 -->
    <el-row class="w-full flex flex-col flex-1 overflow-x-auto overflow-y-auto">
      <!-- 查询条件 -->
      <div class="w-full">
        <!-- 分类名称和描述在同一行 -->
        <el-row :gutter="10" class="w-full"  v-if="showSearchRow">
          <el-col :span="5">
            <el-form :model="searchForm" inline label-position="left" >
              <el-form-item label="文章标题">
                <el-input v-model="searchForm.title" placeholder="请输入文章标题" />
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="5">
            <el-form :model="searchForm" inline label-position="left" >
              <el-form-item label="作者">
                <el-input v-model="searchForm.author" placeholder="请输入作者" />
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
              添加文章
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
        <el-row class="w-full flex-1 mt-3 overflow-y-auto" style="margin: 0 auto;">
          <el-table
            class="w-full"
            :data="datatable.records"
            :loading="datatable.loading"
           style="width: 100%; table-layout: fixed; height: calc(100vh - 350px);"
            :fit="true"
            :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
          >
            <el-table-column prop="title" label="文章标题" align="center" min-width="200" />
            <!-- 修改 el-table-column 的 content 部分 -->
            <el-table-column prop="content" label="文章内容" align="center" min-width="200">
              <template #default="scope">
                <el-tooltip
                  effect="dark"
                  :content="stripHtml(scope.row.content)"
                  placement="top"
                  :disabled="!scope.row.content || stripHtml(scope.row.content).length <= 30"
                >
                  <span class="content-cell">
                    {{ truncateText(stripHtml(scope.row.content), 30) }}
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="author" label="作者" align="center" min-width="150" />
            <!-- 修改类型id显示为类型名称 -->
            <el-table-column label="类型名称" align="center" min-width="150">
              <template #default="scope">
                {{ getTypeName(scope.row.typeId) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="160" fixed="right">
              <template #default="scope">
                <el-space>
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
  </el-card>
</template>

<script setup>
import { ref, reactive, shallowRef, onMounted } from 'vue';
import {
  articlesAdd,
  articlesPage,
  articlesDelete,
  getArticleInfo,
  articlesUpdate,
  articleTypesPage
} from '@/api/articles.js';
import ArticleEdit from '@/pages/Article/ArticleEdit.vue';
import { ElMessage } from 'element-plus';
import { Search, Refresh, Plus, ArrowUp, ArrowDown, Edit, Delete } from '@element-plus/icons-vue';

// 工具方法
const stripHtml = (html) => {
  if (!html) return '';
  return html.replace(/<[^>]*>?/gm, '').trim();
};

const truncateText = (text, maxLength) => {
  if (!text) return '';
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
};

// 是否展示搜索区域
const showSearchRow = ref(true);

// 查询参数表单
const searchForm = reactive({
  title: null,
  author: null,
  pageNum: 1,
  pageSize: 10
});

// 数据列表配置
const datatable = reactive({
  records: [],
  loading: false,
  total: 0
});

// 文章类型映射
const articleTypeMap = ref({});

// 获取文章类型列表
const getArticleTypes = () => {
  const params = {
    pageNum: 1,
    pageSize: 100
  };
  articleTypesPage(params)
    .then((res) => {
      if (res.code === 200) {
        const data = res.data.data || [];
        data.forEach((item) => {
          articleTypeMap.value[item.typeId] = item.typeName;
        });
      }
    })
    .catch((error) => {
      console.error('获取文章类型失败', error);
    });
};

// 根据类型ID获取类型名称
const getTypeName = (typeId) => {
  return articleTypeMap.value[typeId] || '未知类型';
};

// 查询数据列表
const getPageList = (isReset = false) => {
  if (isReset) {
    searchForm.title = null;
    searchForm.author = null;
    searchForm.pageNum = 1;
    searchForm.pageSize = 10;
  }
  datatable.loading = true;

  articlesPage(searchForm)
    .then((res) => {
      if (res.code === 200) {
        datatable.records = res.data.data;
        datatable.total = res.data.total;
      } else {
        ElMessage.error(res.message || '获取文章列表失败');
      }
    })
    .finally(() => {
      datatable.loading = false;
    });
};

// 分页处理
const handlePageChange = (currentPage) => {
  searchForm.pageNum = currentPage;
  getPageList();
};

const handleSizeChange = (pageSize) => {
  searchForm.pageSize = pageSize;
  getPageList();
};

// 模态框控制
const modal = reactive({
  visible: false,
  title: '文章管理',
  params: {},
  component: null
});

// 按钮点击事件
const addBtnClick = () => {
  modal.visible = true;
  modal.title = '添加文章';
  modal.params = { operationType: 'add' };
  modal.component = shallowRef(ArticleEdit);
};

const updateBtnClick = (id) => {
  modal.visible = true;
  modal.title = '修改文章';
  modal.params = { operationType: 'update', id };
  modal.component = shallowRef(ArticleEdit);
};

const deleteBtnOkClick = (id) => {
  articlesDelete(id)
    .then((response) => {
      if (response.data) {
        ElMessage.success('删除成功');
        getPageList();
      } else {
        ElMessage.error(response.message || '删除失败');
      }
    })
    .catch((error) => {
      console.error('删除操作失败', error);
      ElMessage.error('删除操作失败');
    });
};

// 模态框回调
const onOk = () => {
  modal.visible = false;
  getPageList();
};

const onCancel = () => {
  modal.visible = false;
};

// 组件挂载
onMounted(() => {
  getArticleTypes();
  getPageList();
});
</script>

<style scoped>
.ellipsis {
  display: inline-block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.content-cell {
  display: inline-block;
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>