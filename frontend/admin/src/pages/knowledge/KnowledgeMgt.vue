<template>
    <el-card class="p-0">
        <h1>RAG 知识库</h1>

        <el-tabs v-model="activeTab" type="border-card">
            <el-tab-pane label="知识库" name="base">
                <el-row :gutter="10" style="width: 100%; margin-top: 12px;">
                    <el-col :span="12">
                        <el-input v-model="kbSearch.name" placeholder="知识库名称" style="width: 200px; margin-right: 8px;" />
                        <el-button type="primary" @click="getKbList(false)">查询</el-button>
                        <el-button @click="getKbList(true)">重置</el-button>
                    </el-col>
                    <el-col :span="12" style="text-align: right;">
                        <el-button type="primary" @click="onAddKb">新增知识库</el-button>
                    </el-col>
                </el-row>
                <el-divider />
                <div class="table-container">
                    <el-table style="width: 100%;" border :data="kbData.records" :loading="kbData.loading">
                        <el-table-column prop="id" label="ID" align="center" min-width="80" />
                        <el-table-column prop="name" label="知识库名称" align="center" min-width="180" />
                        <el-table-column prop="embeddingModel" label="嵌入模型" align="center" min-width="200" />
                        <el-table-column prop="collectionName" label="Milvus Collection" align="center" min-width="220" />
                        <el-table-column prop="createdBy" label="创建人" align="center" min-width="100" />
                        <el-table-column prop="createTime" label="创建时间" align="center" min-width="180" />
                        <el-table-column label="操作" align="center" min-width="280" fixed="right">
                            <template #default="scope">
                                <el-space>
                                    <el-button type="primary" size="small" @click="viewDocs(scope.row)">文档</el-button>
                                    <el-button type="info" size="small" @click="onEditKb(scope.row)">编辑</el-button>
                                    <el-popconfirm title="确认删除? 将同时删除关联文档和向量数据" @confirm="onDeleteKb(scope.row.id)">
                                        <template #reference>
                                            <el-button type="danger" size="small">删除</el-button>
                                        </template>
                                    </el-popconfirm>
                                </el-space>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <el-row style="display: flex; justify-content: center; margin-top: 16px;">
                    <el-pagination :current-page="kbSearch.pageNum" :page-size="kbSearch.pageSize"
                        :total="kbData.total" :page-sizes="[10, 20, 50, 100]"
                        layout="total, sizes, prev, pager, next, jumper" background
                        @current-change="(p) => { kbSearch.pageNum = p; getKbList(); }"
                        @size-change="(s) => { kbSearch.pageSize = s; getKbList(); }" />
                </el-row>
            </el-tab-pane>

            <el-tab-pane label="文档管理" name="doc" :disabled="!currentKb">
                <div v-if="currentKb" style="margin-bottom: 12px;">
                    <el-tag type="info" size="large">当前知识库：{{ currentKb.name }} (ID: {{ currentKb.id }})</el-tag>
                    <el-button size="small" style="margin-left: 8px;" @click="currentKb = null; activeTab = 'base'">返回</el-button>
                </div>
                <el-row :gutter="10" style="width: 100%;">
                    <el-col :span="12">
                        <el-input v-model="docSearch.fileName" placeholder="文件名" style="width: 200px; margin-right: 8px;" />
                        <el-button type="primary" @click="getDocList(false)">查询</el-button>
                        <el-button @click="getDocList(true)">重置</el-button>
                    </el-col>
                    <el-col :span="12" style="text-align: right;">
                        <el-upload :show-file-list="false" :http-request="onUpload" :before-upload="beforeUpload"
                            accept=".pdf,.txt,.md,.doc,.docx">
                            <el-button type="primary">上传文件</el-button>
                        </el-upload>
                    </el-col>
                </el-row>
                <el-divider />
                <div class="table-container">
                    <el-table style="width: 100%;" border :data="docData.records" :loading="docData.loading">
                        <el-table-column prop="id" label="ID" align="center" min-width="80" />
                        <el-table-column prop="fileName" label="文件名" align="center" min-width="220" />
                        <el-table-column prop="fileType" label="类型" align="center" min-width="120" />
                        <el-table-column prop="fileSize" label="大小(B)" align="center" min-width="100" />
                        <el-table-column prop="status" label="状态" align="center" min-width="100">
                            <template #default="scope">
                                <el-tag :type="scope.row.status === 'ready' ? 'success' : 'warning'" size="small">
                                    {{ scope.row.status }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column prop="chunkCount" label="分块数" align="center" min-width="80" />
                        <el-table-column prop="createTime" label="上传时间" align="center" min-width="180" />
                        <el-table-column label="操作" align="center" min-width="220" fixed="right">
                            <template #default="scope">
                                <el-space>
                                    <el-button type="primary" size="small" @click="viewChunks(scope.row)">分块</el-button>
                                    <el-popconfirm title="确认删除?" @confirm="onDeleteDoc(scope.row.id)">
                                        <template #reference>
                                            <el-button type="danger" size="small">删除</el-button>
                                        </template>
                                    </el-popconfirm>
                                </el-space>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <el-row style="display: flex; justify-content: center; margin-top: 16px;">
                    <el-pagination :current-page="docSearch.pageNum" :page-size="docSearch.pageSize"
                        :total="docData.total" :page-sizes="[10, 20, 50, 100]"
                        layout="total, sizes, prev, pager, next, jumper" background
                        @current-change="(p) => { docSearch.pageNum = p; getDocList(); }"
                        @size-change="(s) => { docSearch.pageSize = s; getDocList(); }" />
                </el-row>
            </el-tab-pane>

            <el-tab-pane label="分块详情" name="chunk" :disabled="!currentDoc">
                <div v-if="currentDoc" style="margin-bottom: 12px;">
                    <el-tag type="info" size="large">当前文档：{{ currentDoc.fileName }} (ID: {{ currentDoc.id }})</el-tag>
                    <el-button size="small" style="margin-left: 8px;" @click="currentDoc = null; activeTab = 'doc'">返回</el-button>
                </div>
                <div class="table-container">
                    <el-table style="width: 100%;" border :data="chunkData.records" :loading="chunkData.loading">
                        <el-table-column prop="id" label="ID" align="center" min-width="80" />
                        <el-table-column prop="chunkIndex" label="分块序号" align="center" min-width="80" />
                        <el-table-column prop="content" label="内容" align="center" min-width="400">
                            <template #default="scope">
                                <el-tooltip effect="dark" :content="scope.row.content" placement="top">
                                    <span class="ellipsis">{{ scope.row.content }}</span>
                                </el-tooltip>
                            </template>
                        </el-table-column>
                        <el-table-column prop="charCount" label="字符数" align="center" min-width="80" />
                        <el-table-column prop="tokenCount" label="Token数" align="center" min-width="80" />
                        <el-table-column prop="contentHash" label="内容哈希" align="center" min-width="160">
                            <template #default="scope">
                                <span style="font-size: 12px; font-family: monospace;">{{ scope.row.contentHash }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" min-width="100" fixed="right">
                            <template #default="scope">
                                <el-popconfirm title="确认删除该分块?" @confirm="onDeleteChunk(scope.row.id)">
                                    <template #reference>
                                        <el-button type="danger" size="small">删除</el-button>
                                    </template>
                                </el-popconfirm>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <el-row style="display: flex; justify-content: center; margin-top: 16px;">
                    <el-pagination :current-page="chunkSearch.pageNum" :page-size="chunkSearch.pageSize"
                        :total="chunkData.total" :page-sizes="[10, 20, 50, 100]"
                        layout="total, sizes, prev, pager, next, jumper" background
                        @current-change="(p) => { chunkSearch.pageNum = p; getChunkList(); }"
                        @size-change="(s) => { chunkSearch.pageSize = s; getChunkList(); }" />
                </el-row>
            </el-tab-pane>
        </el-tabs>

        <el-dialog v-model="kbDialog.visible" :title="kbDialog.isEdit ? '编辑知识库' : '新增知识库'" width="500px">
            <el-form :model="kbDialog" label-width="100px">
                <el-form-item label="名称">
                    <el-input v-model="kbDialog.name" placeholder="请输入知识库名称" />
                </el-form-item>
                <el-form-item label="嵌入模型">
                    <el-input v-model="kbDialog.embeddingModel" placeholder="如 nomic-embed-text" />
                </el-form-item>
                <el-form-item label="Collection">
                    <el-input v-model="kbDialog.collectionName" placeholder="Milvus Collection 名称" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="kbDialog.visible = false">取消</el-button>
                <el-button type="primary" @click="submitKb">保存</el-button>
            </template>
        </el-dialog>
    </el-card>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { knowledgePage, knowledgeUpload, knowledgeDelete } from '@/api/knowledge.js';
import { knowledgeBasePage, knowledgeBaseAdd, knowledgeBaseUpdate, knowledgeBaseDelete } from '@/api/knowledgeBase.js';
import { knowledgeChunkPage, knowledgeChunkDelete } from '@/api/knowledgeChunk.js';

const activeTab = ref('base');
const currentKb = ref(null);
const currentDoc = ref(null);

const kbSearch = reactive({ name: null, pageNum: 1, pageSize: 10 });
const kbData = reactive({ records: [], total: 0, loading: false });

const docSearch = reactive({ fileName: null, fileType: null, pageNum: 1, pageSize: 10, kbId: null });
const docData = reactive({ records: [], total: 0, loading: false });

const chunkSearch = reactive({ pageNum: 1, pageSize: 10, kbId: null, docId: null });
const chunkData = reactive({ records: [], total: 0, loading: false });

const kbDialog = reactive({ visible: false, isEdit: false, id: null, name: '', embeddingModel: '', collectionName: '' });

const getKbList = (reset = false) => {
    if (reset) { kbSearch.name = null; kbSearch.pageNum = 1; kbSearch.pageSize = 10; }
    kbData.loading = true;
    knowledgeBasePage(kbSearch).then(res => {
        if (res.code === 200) { kbData.records = res.data.data; kbData.total = res.data.total; }
        else { ElMessage.error(res.message || '获取知识库列表失败'); }
    }).finally(() => { kbData.loading = false; });
};

const onAddKb = () => {
    kbDialog.visible = true; kbDialog.isEdit = false;
    kbDialog.id = null; kbDialog.name = ''; kbDialog.embeddingModel = ''; kbDialog.collectionName = '';
};

const onEditKb = (row) => {
    kbDialog.visible = true; kbDialog.isEdit = true;
    kbDialog.id = row.id; kbDialog.name = row.name; kbDialog.embeddingModel = row.embeddingModel; kbDialog.collectionName = row.collectionName;
};

const submitKb = async () => {
    const params = { name: kbDialog.name, embeddingModel: kbDialog.embeddingModel, collectionName: kbDialog.collectionName };
    const res = kbDialog.isEdit
        ? await knowledgeBaseUpdate({ ...params, id: kbDialog.id })
        : await knowledgeBaseAdd(params);
    if (res.code === 200) { ElMessage.success('保存成功'); kbDialog.visible = false; getKbList(); }
    else { ElMessage.error(res.message || '保存失败'); }
};

const onDeleteKb = async (id) => {
    const res = await knowledgeBaseDelete(id);
    if (res.code === 200) { ElMessage.success('删除成功'); getKbList(); }
    else { ElMessage.error(res.message || '删除失败'); }
};

const viewDocs = (row) => {
    currentKb.value = row;
    docSearch.kbId = row.id;
    docSearch.pageNum = 1;
    activeTab.value = 'doc';
    getDocList();
};

const getDocList = (reset = false) => {
    if (reset) { docSearch.fileName = null; docSearch.fileType = null; docSearch.pageNum = 1; docSearch.pageSize = 10; }
    docData.loading = true;
    knowledgePage(docSearch).then(res => {
        if (res.code === 200) { docData.records = res.data.data; docData.total = res.data.total; }
        else { ElMessage.error(res.message || '获取文档列表失败'); }
    }).finally(() => { docData.loading = false; });
};

const beforeUpload = (file) => {
    const lower = (file.name || '').toLowerCase();
    const ok = lower.endsWith('.txt') || lower.endsWith('.md') || lower.endsWith('.pdf') || lower.endsWith('.doc') || lower.endsWith('.docx');
    if (!ok) { ElMessage.error('仅支持上传 txt、md、pdf、word(doc/docx) 文件'); }
    return ok;
};

const onUpload = async (options) => {
    const form = new FormData();
    form.append('file', options.file);
    form.append('kbId', currentKb.value.id);
    try {
        const res = await knowledgeUpload(form);
        if (res.code === 200) { ElMessage.success('上传成功'); getDocList(); }
        else { ElMessage.error(res.message || '上传失败'); }
    } catch (e) { ElMessage.error('上传异常'); }
};

const onDeleteDoc = async (id) => {
    const res = await knowledgeDelete(id);
    if (res.code === 200) { ElMessage.success('删除成功'); getDocList(); }
    else { ElMessage.error(res.message || '删除失败'); }
};

const viewChunks = (row) => {
    currentDoc.value = row;
    chunkSearch.kbId = row.kbId;
    chunkSearch.docId = row.id;
    chunkSearch.pageNum = 1;
    activeTab.value = 'chunk';
    getChunkList();
};

const getChunkList = () => {
    chunkData.loading = true;
    knowledgeChunkPage(chunkSearch).then(res => {
        if (res.code === 200) { chunkData.records = res.data.data; chunkData.total = res.data.total; }
        else { ElMessage.error(res.message || '获取分块列表失败'); }
    }).finally(() => { chunkData.loading = false; });
};

const onDeleteChunk = async (id) => {
    const res = await knowledgeChunkDelete(id);
    if (res.code === 200) { ElMessage.success('删除成功'); getChunkList(); }
    else { ElMessage.error(res.message || '删除失败'); }
};

getKbList();
</script>

<style scoped>
.table-container {
    overflow-x: auto;
    position: relative;
}
.ellipsis {
    display: inline-block;
    width: 100%;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}
</style>
