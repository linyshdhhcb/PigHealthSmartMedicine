<!-- src/views/FineMedicine.vue -->
<template>
  <div class="medicine-page">
    <div class="nav2">
      <nav2 />
    </div>

    <div class="container">
      <!-- 左侧 tag 过滤 -->
      <aside class="left-panel" :class="{ collapsed: collapsedTags }">
        <div class="tags-header">
          <h3>分类筛选</h3>
          <button class="collapse-btn" @click="collapsedTags = !collapsedTags">
            {{ collapsedTags ? '展开' : '收起' }}
          </button>
        </div>
        <div class="tag-list">
          <button
            class="tag"
            :class="{ active: currentTag === '' }"
            @click="selectTag('')"
            tabindex="0"
          >全部</button>

          <button
            v-for="t in allTags"
            :key="t"
            class="tag"
            :class="{ active: currentTag === t }"
            @click="selectTag(t)"
            tabindex="0"
          >{{ t }}</button>
        </div>
      </aside>

      <!-- 右侧 主区 -->
      <main class="right-panel">
        <!-- 搜索栏 -->
        <div class="toolbar">
          <el-input
            v-model="searchText"
            placeholder="搜索药品名 / 适应症 / 关键词"
            clearable
            @clear="onSearchClear"
            @input="onSearchInput"
            class="search-input"
          >
            <template #prefix>
              <i class="iconfont icon-search" />
            </template>
          </el-input>
        </div>

        <!-- 列表区 -->
        <section class="list-area">
          <div v-if="loading" class="skeleton-wrap">
            <!-- 简单骨架占位 -->
            <div class="skeleton-card" v-for="n in 6" :key="n"></div>
          </div>

          <div v-else>
            <div v-if="filteredList.length === 0" class="no-result">
              没有找到符合条件的药品。试试清除筛选或更换关键词。
            </div>

            <div class="grid">
              <article
                v-for="medicine in pagedList"
                :key="medicine.id"
                class="card"
                tabindex="0"
                role="button"
                @click="openDetail(medicine.id)"
                @keydown.enter="openDetail(medicine.id)"
              >
                <div class="thumb-wrap">
                  <!-- 懒加载并设置占位尺寸 -->
                  <img
                    :src="formatImagePath(medicine.imgPath)"
                    alt="封面"
                    class="thumb"
                    loading="lazy"
                    width="220"
                    height="220"
                  />
                </div>

                <div class="card-body">
                  <header class="card-head">
                    <h4 class="title">{{ medicine.medicineName }}</h4>
                    <div class="price">¥{{ Number(medicine.medicinePrice).toFixed(2) }}</div>
                  </header>

                  <p class="desc">{{ medicine.medicineEffect }}</p>
                  <span class="desc ">禁忌：<div class="taboo">{{ medicine.taboo }}</div></span>
                  <span class="desc  ">药效：<div class="keyword">{{ medicine.keyword }}</div></span>
                  <div class="meta">
                    <div class="medicine-box">
                      <span class="brand">品牌：{{ medicine.medicineBrand }}</span>
                      
                    </div>
                    <span
                        class="type"
                        :class="getTypeClass(medicine.medicineType)"
                      >{{ getMedicineTypeName(medicine.medicineType) }}
                   </span>
                  </div>

                  <div class="card-footer">
                    <span class="usage">用法：{{ shorten(medicine.usAge, 30) }}</span>
                    <span class="tag-warning">⚠️ 切忌乱用</span>
                  </div>
                </div>
              </article>
            </div>
          </div>
        </section>

        <!-- 分页 -->
        <footer class="pagination-wrap">
          <el-pagination
            v-if="filteredList.length > 0"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="filteredList.length"
            layout="total, sizes, prev, pager, next, jumper"
            :page-sizes="[4, 8, 12, 16]"
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
          />
        </footer>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, shallowRef } from 'vue'
import { useRouter } from 'vue-router'
import nav2 from '@/components/nav2.vue'
import { medicinePage, getmedicineInfo } from '@/api/admin/medicine.js'
import debounce from 'lodash/debounce' // 若项目未安装 lodash，可使用自写防抖

// 状态
const router = useRouter()
const collapsedTags = ref(false)
const currentTag = ref('')
const searchText = ref('')
const loading = ref(true)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(4)
// 使用 shallowRef 来减少深度响应开销
const medicinePageData = shallowRef([]) // 当前页后端返回的数据
const allItemsCache = shallowRef([]) // 可选：完整数据缓存（若后端支持一次性拉取）
const medicines = ref([])

// 获取列表（后端分页）
const fetchData = async (page = currentPage.value, size = pageSize.value) => {
  loading.value = true
  try {
    const res = await medicinePage({ pageNum: 1, pageSize: 999 })
    // 你的后端接口可能包裹在 res.data 或直接 res，按实际调整
    if (res && (res.code === 200 || res.data)) {
      // 兼容多种返回：优先用 res.data.data
      const data = res.data.data 
      medicinePageData.value = data
      total.value = res.data?.total ?? res.total ?? (data?.length || 0)
      medicines.value = res.data.data
      // 可选：维护全量缓存（用于客户端过滤/快速搜索，不适合大数据）
      // allItemsCache.value = allItemsCache.value.concat(data)
    } else {
      // 错误处理
      medicinePageData.value = []
      total.value = 0
    }
  } catch (e) {
    console.error('fetch medicine error', e)
    medicinePageData.value = []
  } finally {
    loading.value = false
  }
}

// 初始加载
onMounted(() => {
  fetchData()
})

// 搜索防抖：300ms
const debouncedSearch = debounce((val) => {
  // 优先做后端搜索（如果接口支持），否则在客户端过滤
  // 假设后端支持传关键字：medicinePage({pageNum:1,pageSize,keyword:val})
  currentPage.value = 1
  fetchData(1, pageSize.value)
}, 300)

function onSearchInput() {
  debouncedSearch(searchText.value)
}
function onSearchClear() {
  searchText.value = ''
  currentPage.value = 1
  fetchData(1, pageSize.value)
}

// Tag 选择
function selectTag(tag) {
  currentTag.value = tag
  currentPage.value = 1
  fetchData(1, pageSize.value)
}




// 分页逻辑
const displayedList = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredList.value.slice(start, end)
})


// 切换页
const handlePageChange = (page) => {
  currentPage.value = page
}

// 切换分页大小
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

// 跳详情页（路由）
function openDetail(id) {
  router.push({ name: 'MedicineDetail', params: { id } })
}

// 计算 allTags（若后端未提供 tags，可由当前页或全量缓存生成）
const allTags = computed(() => {
  const set = new Set()
  // 优先用 full cache，否则用当前页数据提取
  const source = allItemsCache.value.length ? allItemsCache.value : (medicinePageData.value || [])
  source.forEach((it) => {
    (it.keyword || '').split(',').forEach(k => {
      const s = k.trim()
      if (s) set.add(s)
    })
  })
  return Array.from(set)
})

// filteredList 与分页：如果你使用后端分页并且后端已做过滤，则 medicinePageData 就是最终数据；
// 否则在客户端做过滤（下面示例做客户端过滤 on current page items）
const filteredList = computed(() => {
  const list = medicinePageData.value || [];
  const q = searchText.value.trim().toLowerCase();
  return list.filter(item => {
    const hitTag = !currentTag.value || (item.keyword || '').split(',').map(s => s.trim()).includes(currentTag.value);
    const hitKeyword = !q ||
      (item.medicineName || '').toLowerCase().includes(q) ||
      (item.medicineEffect || '').toLowerCase().includes(q) ||
      (item.keyword || '').toLowerCase().includes(q);
    return hitTag && hitKeyword;
  });
});

// pagedList 显示当前页（filteredList 已基于 current backend page；如果需要 client pagination 再切分）
const pagedList = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredList.value.slice(start, end);
});

// small utilities
const getMedicineTypeName = (t) => {
  switch (+t) {
    case 0: return '西药'
    case 1: return '中药'
    case 2: return '中成药'
    default: return '未知'
  }
}
const getTypeClass = (t) => {
  if (t === 0) return 'western'
  if (t === 1) return 'chinese'
  return 'neutral'
}
const formatImagePath = (p) => (p || '').replace(/[<>]/g, '')
const shorten = (str = '', len = 40) => (str.length > len ? str.slice(0, len) + '…' : str)
</script>

<style scoped>
/* 整体容器 */
.container {
  display: flex;
  gap: 24px;
  margin: 18px;
}
.nav2{
  height: 70px;
}

/* 左侧 tag 面板 */
.left-panel {
  width: 220px;
  min-width: 160px;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 6px 18px rgba(16, 24, 40, 0.06);
}
.left-panel.collapsed {
  width: 56px;
  overflow: hidden;
  padding-left: 10px;
}
.tags-header {
  display:flex;
  justify-content:space-between;
  align-items:center;
  margin-bottom: 12px;
}
.tag-list {
  display:flex;
  flex-direction:column;
  gap:8px;
}
.tag {
  border: none;
  background: #f0f6ff;
  color: #2c6bbe;
  padding: 8px 12px;
  border-radius: 999px;
  text-align: center;
  cursor: pointer;
  transition: transform .15s ease, background .15s;
}
.tag:focus { outline: none; box-shadow: 0 0 0 3px rgba(64,158,255,0.08); }
.tag.active { background: #16a34a; color: #fff; transform: translateX(4px); }

/* 右侧内容区域 */
.right-panel {
  flex: 1;
  display:flex;
  flex-direction:column;
  gap:16px;
}

/* 工具条 */
.toolbar {
  display:flex;
  align-items:center;
  justify-content: center;
}
.search-input { 
  max-width: 640px; 
  width:100%; 
  background:gray;
}

.grid{
  margin: 30px 30px;
  width: 75vw;
  justify-content: center;
  border: 1px solid #eee;
}
.card {
  display:flex;
  gap: 100px;
  background: #fff;
  padding: 14px;
  border-radius: 122px;
  box-shadow: 0 6px 18px rgba(16,24,40,0.04);
  cursor:pointer;
  transition: transform .18s ease, box-shadow .18s;
  border-bottom: 1px solid #eee;
}
.card:focus { outline: none; box-shadow: 0 10px 30px rgba(16,24,40,0.08); transform: translateY(-4px); }
.card:hover { transform: translateY(-6px); box-shadow: 0 14px 32px rgba(16,24,40,0.06); }

.thumb-wrap { width: 220px; height: 220px; flex-shrink:0; border-radius:8px; overflow:hidden; }
.thumb { width: 100%; height:100%; object-fit: cover; display:block; }

/* 卡片内容 */
.card-body { flex:1; display:flex; flex-direction:column; gap:8px; }
.card-head { display:flex; justify-content:space-between; align-items:center; }
.title { font-size: 16px; font-weight: 700; color: #222; margin: 0; }
.price { 
  color: #f56c6c; 
  font-weight: 700; 
  font-size: 24px;
  margin-right: 20px;
}

.desc {display: flex; color:#555; font-size:14px; margin:0; line-height:1.5; max-height:3em; overflow:hidden; text-overflow:ellipsis; }
.keyword{
  color: #16a34a;
}
.taboo{
  color: rgb(146, 20, 20);
}
.meta { 
  display:flex; 
  gap:15px; 
  align-items:center; 
  font-size:14px;
   color:#666;
   }
.medicine-box{
  display: flex;
}
.type.western { color:#409eff; }
.type.chinese { color:#67c23a; }
.type.neutral { color:#e6a23c; }

.card-footer { display:flex; justify-content:space-between; align-items:center; margin-top:auto; font-size:12px; color:#888; }
.tag-warning { color: #f56c6c; font-weight:700; }

/* pagination */
.pagination-wrap { 
  display:flex; 
  justify-content:center; 
  margin-top:18px;
 }

/* skeleton 占位 */
.skeleton-wrap { display:grid; grid-template-columns: repeat(auto-fill, minmax(260px, 1fr)); gap:18px; }
.skeleton-card { height: 220px; background: linear-gradient(90deg,#f3f5f8,#eef1f4,#f3f5f8); border-radius:12px; }
</style>
