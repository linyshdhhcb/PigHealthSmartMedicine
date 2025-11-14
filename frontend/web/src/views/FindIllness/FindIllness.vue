<template>
  <div class="find-illness-root">
    <!-- 顶部导航占位（你已有 nav2 组件） -->
    <div class="nav2">
      <nav2 />
    </div>

    <div class="container">
      <!-- 左侧固定侧边栏（保留现状） -->
      <aside class="fixed-sidebar">
        <ul class="type-list">
          <li :class="{ active: selectedKindId === null }" @click="filterByKind(null)">
            全部疾病
          </li>
          <li
            v-for="kind in illnessKindData"
            :key="kind.id"
            :class="{ active: selectedKindId === kind.id }"
            @click="filterByKind(kind.id)"
          >
            {{ kind.name }}
          </li>
        </ul>
      </aside>

      <!-- 主要内容区域 -->
      <main class="content-area">
        <!-- 顶部搜索行 -->
        <div class="top-controls">
          <el-input
            v-model="searchQuery"
            placeholder="搜索疾病名称 / 症状 / 药物..."
            clearable
            @input="handleSearchInput"
            @clear="handleClear"
            class="search-input"
            prefix-icon="el-icon-search"
          />
          <div class="spacer" />
          <div class="stats" v-if="totalCount !== null">共 {{ totalCount }} 条结果</div>
        </div>

        <!-- 卡片网格 -->
        <div class="card-grid" ref="cardGrid">
          <transition-group name="list" tag="div" class="card-grid-inner">
            <div
              v-for="(item, index) in displayedItems"
              :key="item.id"
              class="card-wrapper"
              @mouseenter="hoverCard = item.id"
              @mouseleave="hoverCard = null"
            >
              <van-card class="custom-card" :hover-class="'no-hover'">
                <template #desc>
                  <div class="card-body" @click.stop="gotoDetail(item)">
                    <!-- 左侧图片 -->
                    <div class="card-left">
                      <img class="illness-img" :src="getStableImage(item.id)" :alt="item.illnessName" />

                    </div>

                    <!-- 右侧信息 -->
                    <div class="card-right">
                      <div class="card-top">
                        <h3 class="card-title">{{ item.illnessName }}</h3>
                        <div class="card-views">
                          <van-icon name="eye" /> {{ item.pageviews || 0 }}
                        </div>
                      </div>

                      <div class="card-meta">
                        <span class="kind-name">{{ item.kindName || getCategoryNameById(item.kindId) }}</span>
                        <span class="create-time"> · {{ formatDate(item.createTime) }}</span>
                      </div>

                      <div class="card-descs">
                        <div class="desc-row">
                          <div class="label">诱发原因</div>
                          <div class="value" v-html="shortHtml(item.includeReason, 80)"></div>
                        </div>
                        <div class="desc-row">
                          <div class="label">主要症状</div>
                          <div class="value" v-html="shortHtml(item.illnessSymptom, 80)"></div>
                        </div>
                        <div class="desc-row">
                          <div class="label">特殊症状</div>
                          <div class="value" v-html="shortHtml(item.specialSymptom, 60)"></div>
                        </div>

                        <div class="desc-row medicines" v-if="getMedicinesByIllnessId(item.id).length">
                          <div class="label">对应药物</div>
                          <div class="value medicine-list">
                            <span
                              v-for="(m, idx) in getMedicinesByIllnessId(item.id)"
                              :key="m.id"
                              class="medicine-link"
                              @click.stop="handleViewCombinedInfo(item.id, m.id)"
                            >
                              {{ m.name }}<span v-if="idx < getMedicinesByIllnessId(item.id).length - 1">, </span>
                            </span>
                          </div>
                        </div>
                      </div>

                      <div class="card-actions">
                        <el-button type="primary" plain @click.stop="gotoDetail(item)">查看详情</el-button>
                      </div>
                    </div>
                  </div>
                </template>
              </van-card>
            </div>
          </transition-group>
        </div>

        <!-- 分页（保持现状） -->
        <div class="pagination-container">
          <el-pagination
            v-if="filteredItems.length > 0"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="filteredItems.length"
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
            background
          />
        </div>

        <!-- 弹窗组件（保持调用） -->
        <CombinedInfoDialog
          :show="showCombinedDialog"
          :combinedForm="combinedForm"
          @update:show="showCombinedDialog = $event"
        />
        <DetailInfoDialog
          :show="showDetailDialog"
          :detailForm="detailForm"
          @update:show="showDetailDialog = $event"
        />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick,watch  } from 'vue';
import { useRouter } from 'vue-router';
import nav2 from '@/components/nav2.vue';
import CombinedInfoDialog from './CombinedInfoDialog.vue';
import DetailInfoDialog from './DetailInfoDialog.vue';
import DOMPurify from 'dompurify';
import { ElMessage } from 'element-plus';

// API - 保留你现有的命名空间/导出
import {
  illnessPage,
  illnessKindPage,
  pageviewPage,
  illnessMedicinePage,
  medicinePage,
  getillnessInfo,
  pageviewAdd
} from '@/api/admin/FindIllness';

// router
const router = useRouter();

// 数据源
const illnesses = ref([]);
const illnessKindData = ref([]);
const pageviewPageData = ref([]);
const illnessMedicinePageData = ref([]);
const medicinePageData = ref([]);

// 交互状态
const searchQuery = ref('');
let searchTimer = null;
const selectedKindId = ref(null);
const hoverCard = ref(null);
const pageSize = ref(10);
const currentPage = ref(1);
const showCombinedDialog = ref(false);
const combinedForm = ref({});
const showDetailDialog = ref(false);
const detailForm = ref({});
const totalCount = ref(null);

// 用于懒加载图片
const lazyObserver = ref(null);
const lazyImgs = ref([]);

// ---------- 初始化 API 数据 ----------
async function loadAllData() {
  try {
    // 并行请求主要数据，控制 pageSize 较大以便前端分页 / 搜索
    const [kRes, pRes, iRes, imRes, mRes] = await Promise.all([
      illnessKindPage({ pageNum: 1, pageSize: 200 }),
      pageviewPage({ pageNum: 1, pageSize: 1000 }),
      illnessPage({ pageNum: 1, pageSize: 1000 }),
      illnessMedicinePage({ pageNum: 1, pageSize: 1000 }),
      medicinePage({ pageNum: 1, pageSize: 1000 })
    ]);

    if (kRes?.code === 200) illnessKindData.value = kRes.data.data || [];
    if (pRes?.code === 200) pageviewPageData.value = pRes.data.data || [];
    if (iRes?.code === 200) {
      illnesses.value = (iRes.data.data || []).map(it => ({
        ...it,
        illnessSymptom: DOMPurify.sanitize(it.illnessSymptom || ''),
        includeReason: DOMPurify.sanitize(it.includeReason || ''),
        specialSymptom: DOMPurify.sanitize(it.specialSymptom || '')
      }));
    }
    if (imRes?.code === 200) illnessMedicinePageData.value = imRes.data.data || [];
    if (mRes?.code === 200) medicinePageData.value = mRes.data.data || [];

    // 合并辅助字段（kindName, pageviews）
    mergeAuxFields();

    // 总数
    totalCount.value = illnesses.value.length;
  } catch (err) {
    console.error(err);
    ElMessage.error('初始化数据失败，请稍后重试');
  } finally {
    // 延迟挂载懒加载观察者
    await nextTick();
    initLazyLoad();
  }
}

// 在 mounted 时加载
onMounted(() => {
  loadAllData();
});

// ---------- 辅助：合并类别名和浏览量到疾病列表（不改变原结构） ----------
function mergeAuxFields() {
  const kindMap = new Map((illnessKindData.value || []).map(k => [k.id, k]));
  const viewMap = new Map();
  (pageviewPageData.value || []).forEach(v => {
    // 累加相同 illnessId 的浏览量（兼容历史结构）
    viewMap.set(v.illnessId, (viewMap.get(v.illnessId) || 0) + (v.pageviews || 0));
  });

  illnesses.value = illnesses.value.map(it => {
    const kind = kindMap.get(it.kindId) || {};
    return {
      ...it,
      kindName: kind.name || '',
      kindInfo: kind.info || '',
      pageviews: viewMap.get(it.id) || it.pageviews || 0
    };
  });
}

// ---------- 搜索 / 过滤 / 分页 ----------
const filteredItems = computed(() => {
  const q = searchQuery.value.trim().toLowerCase();
  let list = illnesses.value;

  if (selectedKindId.value) {
    list = list.filter(i => i.kindId === selectedKindId.value);
  }

  if (q) {
    // 以病名、症状、分类名和对应药物名做模糊匹配
    list = list.filter(i => {
      const name = (i.illnessName || '').toLowerCase();
      const symptom = (stripText(i.illnessSymptom) || '').toLowerCase();
      const reason = (stripText(i.includeReason) || '').toLowerCase();
      const kindName = (i.kindName || getCategoryNameById(i.kindId) || '').toLowerCase();

      // 药物名集合
      const meds = getMedicinesByIllnessId(i.id).map(m => (m.name || '').toLowerCase()).join(' ');

      return (
        name.includes(q) ||
        symptom.includes(q) ||
        reason.includes(q) ||
        kindName.includes(q) ||
        meds.includes(q)
      );
    });
  }
  return list;
});

const displayedItems = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredItems.value.slice(start, end);
});

// ---------- 分页处理 ----------
function handlePageChange(page) {
  currentPage.value = page;
  // 视图变换后让卡片网格滚回顶部（提升 UX）
  const node = document.querySelector('.card-grid');
  if (node) node.scrollIntoView({ behavior: 'smooth' });
}
function handleSizeChange(size) {
  pageSize.value = size;
  currentPage.value = 1;
}



// 分类切换
function filterByKind(kindId) {
  selectedKindId.value = kindId
  currentPage.value = 1
}



// ---------- 搜索节流 ----------
function handleSearchInput() {
  if (searchTimer) clearTimeout(searchTimer);
  searchTimer = setTimeout(() => {
    currentPage.value = 1;
  }, 300);
}
function handleClear() {
  searchQuery.value = '';
  currentPage.value = 1;
}

// ---------- 视图 / 文本处理 ----------
function stripText(html) {
  if (!html) return '';
  const parser = new DOMParser();
  const doc = parser.parseFromString(DOMPurify.sanitize(html), 'text/html');
  return doc.body.textContent || '';
}
function shortHtml(html, maxLength = 80) {
  const text = stripText(html);
  if (!text) return '';
  const trimmed = text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
  // 用 <span> 包裹，保持安全
  return `<span>${escapeHtml(trimmed)}</span>`;
}
function escapeHtml(s) {
  return (s || '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}
function formatDate(dt) {
  if (!dt) return '';
  try {
    const d = new Date(dt);
    if (isNaN(d)) return dt;
    return d.toLocaleDateString();
  } catch {
    return dt;
  }
}



// ---------- 分类 / 药物相关 ----------
// 根据 kindId 查找名字（后备）
function getCategoryNameById(kindId) {
  const k = illnessKindData.value.find(x => x.id === kindId);
  return k ? k.name : '未知种类';
}

// 支持通过 illnessMedicinePage 与 medicinePage 关联
function getMedicinesByIllnessId(illnessId) {
  if (!illnessMedicinePageData.value || !medicinePageData.value) return [];
  const rels = illnessMedicinePageData.value.filter(r => r.illnessId === illnessId);
  return rels.map(r => {
    const m = medicinePageData.value.find(x => x.id === r.medicineId) || {};
    return { id: r.medicineId, name: m.medicineName || m.name || '未知药物' };
  });
}

// ---------- 查看综合信息（保留原逻辑） ----------
function handleViewCombinedInfo(illnessId, medicineId) {
  const illness = illnesses.value.find(i => i.id === illnessId);
  const medicine = medicinePageData.value.find(m => m.id === medicineId);
  if (!illness || !medicine) {
    ElMessage.error('获取综合信息失败');
    return;
  }
  combinedForm.value = {
    illnessName: illness.illnessName,
    illnessSymptom: illness.illnessSymptom,
    specialSymptom: illness.specialSymptom,
    medicineName: medicine.medicineName || medicine.name,
    keyword: medicine.keyword,
    medicineEffect: medicine.medicineEffect,
    medicineBrand: medicine.medicineBrand,
    usAge: medicine.usAge,
    taboo: medicine.taboo,
    interaction: medicine.interaction,
    medicinePrice: medicine.medicinePrice
  };
  showCombinedDialog.value = true;
}

// ---------- 跳转详情并增加浏览量（使用 pageviewAdd，失败不阻塞跳转） ----------
async function gotoDetail(item) {
  try {
    // 1) 尝试调用 pageviewAdd（后端可能处理为新增或累加）
    await pageviewAdd({
      illnessId: item.id,
      pageviews: (item.pageviews || 0) + 1
    });
    // 本地同步显示
    const idx = illnesses.value.findIndex(x => x.id === item.id);
    if (idx !== -1) illnesses.value[idx].pageviews = (illnesses.value[idx].pageviews || 0) + 1;
  } catch (e) {
    // 若失败，仅在本地加 1（不阻塞）
    console.warn('pageviewAdd failed', e);
    const idx = illnesses.value.findIndex(x => x.id === item.id);
    if (idx !== -1) illnesses.value[idx].pageviews = (illnesses.value[idx].pageviews || 0) + 1;
  } finally {
    // 跳转到约定的路径： /illnessDetail?id=xxx
    router.push({ path: '/illnessDetail', query: { id: String(item.id) } });
  }
}

// ---------- 详情弹窗（备用，保持和原逻辑） ----------
async function handleViewIllnessInfo(illnessId) {
  try {
    const res = await getillnessInfo(illnessId);
    if (res?.code === 200) {
      detailForm.value = {
        ...res.data,
        includeReason: DOMPurify.sanitize(res.data.includeReason || ''),
        illnessSymptom: DOMPurify.sanitize(res.data.illnessSymptom || ''),
        specialSymptom: DOMPurify.sanitize(res.data.specialSymptom || '')
      };
      showDetailDialog.value = true;
    } else {
      ElMessage.error(res.message || '获取详情失败');
    }
  } catch (err) {
    console.error(err);
    ElMessage.error('获取详情失败');
  }
}

// ---------- 图片：稳定映射到你的五张图片 + 懒加载 ----------
const pigImages = [
  new URL('@/assets/images/illnessImg/pig1.jpg', import.meta.url).href,
  new URL('@/assets/images/illnessImg/pig2.jpg', import.meta.url).href,
  new URL('@/assets/images/illnessImg/pig3.jpg', import.meta.url).href,
  new URL('@/assets/images/illnessImg/pig4.jpg', import.meta.url).href,
  new URL('@/assets/images/illnessImg/pig5.jpg', import.meta.url).href
];


function getStableImage(id) {
  // 保证 id 可用
  const s = String(id || '');
  let hash = 0;
  for (let i = 0; i < s.length; i++) hash += s.charCodeAt(i);
  return pigImages[hash % pigImages.length];
}

// 初始化懒加载观察者
function initLazyLoad() {
  // 兼容性判断
  if (!('IntersectionObserver' in window)) {
    // 直接把所有 data-src 赋予 src
    document.querySelectorAll('img[data-src]').forEach(img => {
      img.setAttribute('src', img.getAttribute('data-src'));
    });
    return;
  }

  lazyObserver.value = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        const img = entry.target;
        const src = img.getAttribute('data-src');
        if (src) {
          img.src = src;
          img.removeAttribute('data-src');
        }
        lazyObserver.value.unobserve(img);
      }
    });
  }, { rootMargin: '150px 0px' });

  // 遍历当前已渲染的图片节点
  document.querySelectorAll('img[data-src]').forEach(img => {
    lazyObserver.value.observe(img);
  });
}

// 在每次 displayedItems 更新后，尝试重新挂载懒加载
// 使用简单 watcher via nextTick (手动)
watch(() => displayedItems.value, async () => {
  await nextTick();
  initLazyLoad();
});

// ---------- 实用：截断纯文本（保留HTML安全） ----------
function escapeHtmlText(s) {
  return (s || '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}
</script>

<style scoped>
/* Container */
.nav2{
  height: 60px;
}
.find-illness-root {
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* layout */
.container {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

/* fixed sidebar (left) */
.fixed-sidebar {
  position: fixed;
  left: 20px;
  top: 80px;
  bottom: 20px;
  width: 240px;
  background: #fff;
  border-radius: 12px;
  padding: 10px;
  box-shadow: 0 8px 24px rgba(14, 30, 37, 0.06);
  overflow-y: auto;
  z-index: 50;
}
.type-list {
  list-style: none;
  padding: 8px;
  margin: 0;
}
.type-list li {
  padding: 10px 12px;
  margin-bottom: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all .2s;
  text-align: center;
  color: #444;
}
.type-list li:hover {
  transform: translateX(6px);
  background: #f0f7ff;
}
.type-list li.active {
  background: linear-gradient(90deg,#e6f0ff,#f3fbff);
  color: #16a34a;
  font-weight: 600;
  box-shadow: 0 6px 18px rgba(64,158,255,0.12);
  border-radius: 50px;
}

/* content area */
.content-area {
  margin-left: 280px; /* accommodate fixed sidebar */
  width: calc(100% - 280px);
  padding-bottom: 40px;
}

/* top controls */
.top-controls {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 18px;
  margin-bottom: 14px;
}
.search-input {
  flex: 1;
}
.spacer {
  width: 8px;
}
.stats {
  color: #778;
  font-size: 14px;
}

/* card grid */
.card-grid {
  margin-top: 6px;
}
.card-grid-inner {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(720px, 1fr));
  gap: 18px;
}

/* wrapper + card */
.card-wrapper {
  transition: transform .2s ease, box-shadow .2s ease;
}
.custom-card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 8px 28px rgba(14, 30, 37, 0.05);
  padding: 0;
}
.card-body {
  display: flex;
  gap: 18px;
  align-items: stretch;
  cursor: pointer;
  padding: 14px;
}

/* left image */
.card-left {
  flex: 0 0 200px;
  width: 200px;
  height: 140px;
  border-radius: 12px;
  overflow: hidden;
  background: linear-gradient(180deg,#fafafa,#f3f5f7);
  display: flex;
  align-items: center;
  justify-content: center;
}
.illness-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform .35s ease;
}
.card-wrapper:hover .illness-img {
  transform: scale(1.03);
}

/* right info */
.card-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.card-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-title {
  font-size: 18px;
  margin: 0;
  color: #222;
  font-weight: 700;
}
.card-views {
  color: #888;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 6px;
}

/* meta row */
.card-meta {
  color: #889;
  font-size: 13px;
  margin-bottom: 6px;
}

/* descriptions */
.card-descs {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.desc-row {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}
.label {
  min-width: 90px;
  color: #7a7a7a;
  font-weight: 600;
}
.value {
  color: #444;
  font-size: 14px;
  line-height: 1.6;
  max-height: 3.6em;
  overflow: hidden;
}

/* medicines */
.medicine-list .medicine-link {
  color: #409eff;
  cursor: pointer;
  margin-right: 6px;
}
.medicine-list .medicine-link:hover {
  text-decoration: underline;
}

/* actions */
.card-actions {
  margin-top: 8px;
  display: flex;
  gap: 8px;
  align-items: center;
}
.el-button {
  border-radius: 8px;
}

/* pagination */
.pagination-container {
  margin-top: 26px;
  display: flex;
  justify-content: center;
}

/* responsiveness */
@media (max-width: 1100px) {
  .card-grid-inner {
    grid-template-columns: 1fr;
  }
  .content-area {
    margin-left: 20px;
    width: calc(100% - 40px);
  }
  .fixed-sidebar {
    display: none;
  }
}

/* small screens */
@media (max-width: 720px) {
  .card-body {
    flex-direction: column;
    gap: 12px;
  }
  .card-left {
    width: 100%;
    height: 180px;
  }
}
</style>
