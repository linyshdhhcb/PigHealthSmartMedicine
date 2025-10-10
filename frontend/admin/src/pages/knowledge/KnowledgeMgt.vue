<template>
    <el-card class="p-0" style="height: 100%;">
        <h1>RAG 知识库</h1>
        <el-row :gutter="10" class="w-full mt-3">
            <el-col :span="12">
                <el-input v-model="searchForm.fileName" placeholder="文件名" style="width: 200px; margin-right: 8px;" />
                <el-input v-model="searchForm.fileType" placeholder="MIME类型" style="width: 200px; margin-right: 8px;" />
                <el-button type="primary" @click="getPageList(false)">查询</el-button>
                <el-button @click="getPageList(true)">重置</el-button>
            </el-col>
            <el-col :span="12" style="text-align: right;">
                <el-upload :show-file-list="false" :http-request="onUpload" :before-upload="beforeUpload"
                    accept=".pdf,.txt,.md,.doc,.docx">
                    <el-button type="primary">上传文件</el-button>
                </el-upload>
            </el-col>
        </el-row>

        <el-divider class="mt-2" />

        <div class="table-container">
            <el-table style="width: 100%; min-width: 900px;" border :data="datatable.records"
                :loading="datatable.loading">
                <el-table-column prop="id" label="ID" align="center" min-width="80" />
                <el-table-column prop="fileName" label="文件名" align="center" min-width="220" />
                <el-table-column prop="fileType" label="类型" align="center" min-width="160" />
                <el-table-column prop="fileSize" label="大小(B)" align="center" min-width="120" />
                <el-table-column prop="filePath" label="相对路径" align="center" min-width="280" />
                <el-table-column prop="createTime" label="上传时间" align="center" min-width="180" />
                <el-table-column label="操作" align="center" min-width="220" fixed="right">
                    <template #default="scope">
                        <el-space>
                            <el-popconfirm title="确认删除?" @confirm="onDelete(scope.row.id)">
                                <template #reference>
                                    <el-button type="danger" size="small">删除</el-button>
                                </template>
                            </el-popconfirm>
                            <el-button type="primary" size="small" @click="onEditRemark(scope.row)">备注</el-button>
                        </el-space>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <el-row class="w-full flex justify-end mt-2" style="margin: 10px auto;">
            <el-pagination v-if="datatable.total > 0" :current-page="searchForm.pageNum"
                :page-size="searchForm.pageSize" :total="datatable.total" :page-sizes="[10, 20, 50, 100, 200]"
                layout="total, sizes, prev, pager, next, jumper" @current-change="handlePageChange"
                @size-change="handleSizeChange" />
        </el-row>

        <el-dialog v-model="remarkDialog.visible" title="编辑备注" width="500px">
            <el-input type="textarea" v-model="remarkDialog.remark" rows="4" placeholder="请输入备注" />
            <template #footer>
                <el-button @click="remarkDialog.visible = false">取消</el-button>
                <el-button type="primary" @click="submitRemark">保存</el-button>
            </template>
        </el-dialog>
    </el-card>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { knowledgePage, knowledgeUpload, knowledgeDelete, knowledgeUpdateRemark } from '@/api/knowledge.js';

const searchForm = reactive({
    fileName: null,
    fileType: null,
    pageNum: 1,
    pageSize: 10
});

const datatable = reactive({
    records: [],
    total: 0,
    loading: false
});

const remarkDialog = reactive({
    visible: false,
    id: null,
    remark: ''
});

const getPageList = (reset = false) => {
    if (reset) {
        searchForm.fileName = null;
        searchForm.fileType = null;
        searchForm.pageNum = 1;
        searchForm.pageSize = 10;
    }
    datatable.loading = true;
    knowledgePage(searchForm).then(res => {
        if (res.code === 200) {
            datatable.records = res.data.data;
            datatable.total = res.data.total;
        } else {
            ElMessage.error(res.message || '获取列表失败');
        }
    }).finally(() => { datatable.loading = false; });
};

const handlePageChange = (p) => { searchForm.pageNum = p; getPageList(); };
const handleSizeChange = (s) => { searchForm.pageSize = s; getPageList(); };

const beforeUpload = (file) => {
    const lower = (file.name || '').toLowerCase();
    const ok = lower.endsWith('.txt') || lower.endsWith('.md') || lower.endsWith('.pdf') || lower.endsWith('.doc') || lower.endsWith('.docx');
    if (!ok) {
        ElMessage.error('仅支持上传 txt、md、pdf、word(doc/docx) 文件');
    }
    return ok;
};

const onUpload = async (options) => {
    const form = new FormData();
    form.append('file', options.file);
    try {
        const res = await knowledgeUpload(form);
        if (res.code === 200) {
            ElMessage.success('上传成功');
            getPageList();
        } else {
            ElMessage.error(res.message || '上传失败');
        }
    } catch (e) {
        ElMessage.error('上传异常');
    }
};

const onDelete = async (id) => {
    try {
        const res = await knowledgeDelete(id);
        if (res.code === 200 && res.data) {
            ElMessage.success('删除成功');
            getPageList();
        } else {
            ElMessage.error(res.message || '删除失败');
        }
    } catch (e) {
        ElMessage.error('删除异常');
    }
};

const onEditRemark = (row) => {
    remarkDialog.visible = true;
    remarkDialog.id = row.id;
    remarkDialog.remark = row.remark || '';
};

const submitRemark = async () => {
    try {
        const res = await knowledgeUpdateRemark({ id: remarkDialog.id, remark: remarkDialog.remark });
        if (res.code === 200 && res.data) {
            ElMessage.success('保存成功');
            remarkDialog.visible = false;
            getPageList();
        } else {
            ElMessage.error(res.message || '保存失败');
        }
    } catch (e) {
        ElMessage.error('保存异常');
    }
};

getPageList();
</script>

<style scoped>
.table-container {
    overflow-x: auto;
    position: relative;
}
</style>
