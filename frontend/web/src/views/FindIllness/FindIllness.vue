<template>
  <div class="container">
    <nav2 />
    <div class="main-content-container">
      <div class="main-content">
        <!-- 左侧疾病分类导航栏 -->
        <aside class="sidebar">
          <ul class="type-list">
            <li
              :class="{ active: selectedKindId === null }"
              @click="filterByKind(null)"
              class="type-item"
            >
              全部疾病
            </li>
            <li
              v-for="kind in illnessKindData"
              :key="kind.id"
              :class="{ active: selectedKindId === kind.id }"
              @click="filterByKind(kind.id)"
              class="type-item"
            >
              {{ kind.name }}
            </li>
          </ul>
        </aside>
        
        <!-- 右侧疾病卡片区域 -->
        <main class="content-area">
          <div class="card-grid">
            <div
              v-for="(item, index) in displayedItems"
              :key="index"
              class="card-wrapper"
            >
              <van-card
                class="custom-card"
                :to="`/illness/${item.id}`"
                @mouseenter="hoverCard = item.id"
                @mouseleave="hoverCard = null"
              >
                <template #desc>
                  <div class="card-header">
                    <h3 class="card-title">{{ item.illnessName }}</h3>
                    <div class="card-stats">
                      <span>{{ item.createTime }} | {{ getCategoryNameById(item.kindId) }}</span>
                      <span><van-icon name="eye" /> {{ getCategoryPageviewById(item.id) }}</span>
                    </div>
                  </div>
                  
                  <div class="card-section symptoms">
                    <div class="label">疾病症状：</div>
                    <div class="card-description symptom" v-html="getShortHtml(item.illnessSymptom, 40)"></div>
                  </div>
                  
                  <div class="card-section causes">
                    <div class="label">诱发因素：</div>
                    <div class="card-description cause" v-html="getShortHtml(item.includeReason, 35)"></div>
                  </div>
                  
                  <div class="card-section special-symptoms">
                    <div class="label">特殊症状：</div>
                    <div class="card-description special-symptom" v-html="getShortHtml(item.specialSymptom, 40)"></div>
                  </div>
                  
                  <div class="card-section medicines">
                    <div class="label">对应药物：</div>
                    <span v-for="(medicine, idx) in getMedicinesByIllnessId(item.id)" :key="idx" class="medicine-link" @click="handleViewCombinedInfo(item.id, medicine.id)">
                      {{ medicine.name }}
                      <span v-if="idx < getMedicinesByIllnessId(item.id).length - 1">, </span>
                    </span>
                  </div>
                  
                  <!-- 查看按钮 -->
                  <div class="card-action">
                    <el-button type="primary" @click="handleViewIllnessInfo(item.id)">查看详情</el-button>
                  </div>
                </template>
              </van-card>
            </div>
          </div>
          
          <!-- 分页 -->
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
        </main>
      </div>
    </div>

    <!-- 综合信息模态框 -->
    <CombinedInfoDialog 
      :show="showCombinedDialog" 
      :combinedForm="combinedForm" 
      @update:show="showCombinedDialog = $event" 
    />
    
    <!-- 疾病详细信息模态框 -->
    <DetailInfoDialog 
      :show="showDetailDialog" 
      :detailForm="detailForm" 
      @update:show="showDetailDialog = $event" 
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRoute } from 'vue-router'
import nav2 from '@/components/nav2.vue'
import CombinedInfoDialog from './CombinedInfoDialog.vue'
import DetailInfoDialog from './DetailInfoDialog.vue'

import { ElMessage } from 'element-plus'

import { illnessPage, illnessKindPage, pageviewPage, illnessMedicinePage, medicinePage, getillnessInfo, pageviewUpdate } from '@/api/admin/FindIllness'

import DOMPurify from 'dompurify'

const router = useRoute()

const illnesses = ref([])
const illnessKindData = ref([])
const pageviewPageData = ref([])
const illnessMedicinePageData = ref([])
const medicinePageData = ref([])
const selectedKindId = ref(null)
const hoverCard = ref(null)

// 获取页面浏览量数据
pageviewPage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      pageviewPageData.value = res.data.data
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试')
    }
  })
  .catch(err => {
    console.error(err)
    ElMessage.error('请求失败，请稍后再试')
  })

// 获取疾病种类数据
illnessKindPage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      illnessKindData.value = res.data.data
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试')
    }
  })
  .catch(err => {
    console.error(err)
    ElMessage.error('请求失败，请稍后再试')
  })

// 获取疾病数据
illnessPage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    illnesses.value = res.data.data.map(item => ({
      ...item,
      illnessSymptom: DOMPurify.sanitize(item.illnessSymptom),
      includeReason: DOMPurify.sanitize(item.includeReason),
      specialSymptom: DOMPurify.sanitize(item.specialSymptom)
    }))
  })
  .catch(err => {
    ElMessage.error('请求失败，请稍后再试')
  })

// 获取疾病药品关系数据
illnessMedicinePage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      illnessMedicinePageData.value = res.data.data
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试')
    }
  })
  .catch(err => {
    console.error(err)
    ElMessage.error('请求失败，请稍后再试')
  })

// 获取药品数据
medicinePage({ pageNum: 1, pageSize: 100 })
  .then(res => {
    if (res.code === 200) {
      medicinePageData.value = res.data.data
    } else {
      ElMessage.error(res.message || '请求失败，请稍后再试')
    }
  })
  .catch(err => {
    console.error(err)
    ElMessage.error('请求失败，请稍后再试')
  })

const pageSize = ref(10)
const currentPage = ref(1)

const filteredItems = computed(() => {
  if (selectedKindId.value) {
    return illnesses.value.filter(item => item.kindId === selectedKindId.value)
  } else {
    return illnesses.value
  }
})

const displayedItems = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredItems.value.slice(start, end)
})

// 根据 kindId 查找对应的疾病种类名字
const getCategoryNameById = (kindId) => {
  if (!illnessKindData.value || illnessKindData.value.length === 0) return '未知种类'
  const category = illnessKindData.value.find(item => item.id === kindId)
  return category ? category.name : '未知种类'
}

// 根据疾病id查找对应的浏览量记录
const getPageViewById = (illnessId) => {
  return pageviewPageData.value.find(item => item.illnessId === illnessId)
}

// 根据疾病id查找对应疾病的浏览量
const getCategoryPageviewById = (id) => {
  const pageview = getPageViewById(id)
  return pageview ? pageview.pageviews : '未知浏览数'
}

// 根据药品id查找对应的药品名字
const getMedicineNameById = (medicineId) => {
  if (!medicinePageData.value || medicinePageData.value.length === 0) return '未知药物'
  const medicine = medicinePageData.value.find(item => item.id === medicineId)
  return medicine ? medicine.medicineName : '未知药物'
}

// 根据疾病id查找对应的所有药品
const getMedicinesByIllnessId = (illnessId) => {
  if (!illnessMedicinePageData.value || illnessMedicinePageData.value.length === 0) return []
  const medicines = illnessMedicinePageData.value
    .filter(item => item.illnessId === illnessId)
    .map(item => ({
      name: getMedicineNameById(item.medicineId),
      id: item.medicineId
    }))
  return medicines
}

// 新增综合信息模态框相关变量
const showCombinedDialog = ref(false)
const combinedForm = ref({})

// 处理点击事件，打开综合信息模态框
const handleViewCombinedInfo = (illnessId, medicineId) => {
  // 获取疾病信息
  const illness = illnesses.value.find(item => item.id === illnessId)
  if (!illness) {
    ElMessage.error('获取疾病信息失败')
    return
  }

  // 获取药品信息
  const medicine = medicinePageData.value.find(item => item.id === medicineId)
  if (!medicine) {
    ElMessage.error('获取药品信息失败')
    return
  }

  // 填充综合信息表单
  combinedForm.value = {
    illnessName: illness.illnessName,
    illnessSymptom: illness.illnessSymptom,
    specialSymptom: illness.specialSymptom,
    medicineName: medicine.medicineName,
    keyword: medicine.keyword,
    medicineEffect: medicine.medicineEffect,
    medicineBrand: medicine.medicineBrand,
    usAge: medicine.usAge,
    taboo: medicine.taboo,
    interaction: medicine.interaction,
    medicinePrice: medicine.medicinePrice,
  }

  // 显示综合信息模态框
  showCombinedDialog.value = true
}

// 新增查看详细信息模态框相关变量
const showDetailDialog = ref(false)
const detailForm = ref({})

// 处理点击事件，打开详细信息模态框并更新浏览量
const handleViewIllnessInfo = async (illnessId) => {
  const pageView = getPageViewById(illnessId)
  
  if (pageView) {
    try {
      // 更新浏览量
      const updatedPageViews = pageView.pageviews + 1
      const updateParams = {
        id: pageView.id,
        pageviews: updatedPageViews,
        illnessId: illnessId
      }
      
      const updateRes = await pageviewUpdate(updateParams)
      
      if (updateRes.code === 200) {
        // 更新本地数据
        const pageIndex = pageviewPageData.value.findIndex(item => item.id === pageView.id)
        if (pageIndex !== -1) {
          pageviewPageData.value[pageIndex].pageviews = updatedPageViews
          // ElMessage.success('浏览量更新成功')
        }
      } else {
        ElMessage.error(updateRes.message || '更新浏览量失败，请稍后再试')
        return
      }
    } catch (err) {
      console.error(err)
      ElMessage.error('更新浏览量失败，请稍后再试')
      return
    }
  }

  getillnessInfo(illnessId)
    .then(res => {
      if (res.code === 200) {
        // 使用 DOMPurify 清理富文本内容，防止XSS攻击
        detailForm.value = {
          ...res.data,
          includeReason: DOMPurify.sanitize(res.data.includeReason),
          illnessSymptom: DOMPurify.sanitize(res.data.illnessSymptom),
          specialSymptom: DOMPurify.sanitize(res.data.specialSymptom)
        }
        showDetailDialog.value = true
      } else {
        ElMessage.error(res.message || '请求失败，请稍后再试')
      }
    })
    .catch(err => {
      console.error(err)
      ElMessage.error('请求失败，请稍后再试')
    })
}

// 根据分类ID筛选疾病
const filterByKind = (kindId) => {
  selectedKindId.value = kindId
  currentPage.value = 1
}

// 截取文本并添加省略号
const getShortText = (text, maxLength) => {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.slice(0, maxLength) + '...'
}

// 解析 HTML 并截取文本
const getShortHtml = (html, maxLength) => {
  if (!html) return ''

  // 清理 HTML，防止XSS攻击
  const cleanHtml = DOMPurify.sanitize(html)
  
  // 解析 HTML
  const parser = new DOMParser()
  const doc = parser.parseFromString(cleanHtml, 'text/html')
  
  // 获取纯文本
  const text = doc.body.textContent || ''
  
  // 截取文本
  if (text.length <= maxLength) {
    return cleanHtml
  } else {
    const shortenedText = text.slice(0, maxLength) + '...'
    
    // 创建一个新的 HTML 文档
    const newDoc = parser.parseFromString(cleanHtml, 'text/html')
    newDoc.body.textContent = shortenedText
    
    return newDoc.body.innerHTML
  }
}

// 分页变化处理
const handlePageChange = (page) => {
  currentPage.value = page
}

// 分页大小变化处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}
</script>

<style scoped>
.container {
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
  padding: 20px;
}

.main-content-container {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.main-content {
  display: flex;
  width: 100%;
}

/* 左侧导航栏样式 */
.sidebar {
  width: 240px;
  background: linear-gradient(to bottom, #f8f9fa, #e9ecef);
  padding: 20px;
  border-right: 1px solid #e0e6ed;
}

.type-list {
  list-style: none;
  padding: 0;
}

.type-item {
  padding: 12px 16px;
  margin-bottom: 8px;
  cursor: pointer;
  border-radius: 8px;
  text-align: left;
  transition: all 0.3s;
  font-weight: 500;
  color: #495060;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
}

.type-item:before {
  content: "";
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: transparent;
  margin-right: 12px;
  transition: all 0.3s ease;
}

.type-item:hover:before,
.type-item.active:before {
  background-color: #409eff;
}

.type-item.active,
.type-item:hover {
  background: linear-gradient(135deg, #e9f5ff, #d0eeff);
  color: #409eff;
  transform: translateX(4px);
}

.type-item.active {
  box-shadow: 0 4px 8px rgba(64, 158, 255, 0.2);
}

/* 右侧内容区域样式 */
.content-area {
  flex: 1;
  padding: 24px;
  background: linear-gradient(to bottom, #ffffff, #f8f9fa);
}

/* 卡片容器 */
.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.card-wrapper {
  transition: transform 0.3s ease;
}

/* 卡片样式 */
.custom-card {
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  height: 100%;
}

.custom-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  transform: translateY(-5px);
}

.van-card__content {
  padding: 20px;
}

/* 卡片头部 */
.card-header {
  margin-bottom: 20px;
}

.card-title {
  font-size: 18px;
  color: #333;
  margin-bottom: 12px;
  font-weight: 600;
}

.card-stats {
  display: flex;
  justify-content: space-between;
  color: #7a7a7a;
  font-size: 13px;
}

/* 卡片内容区块 */
.card-section {
  margin-bottom: 16px;
}

.label {
  color: #7a7a7a;
  font-size: 14px;
  margin-bottom: 8px;
  font-weight: 500;
}

.card-description {
  color: #495060;
  font-size: 14px;
  line-height: 1.6;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

/* 不同内容类型的颜色 */
.card-description.symptom {
  color: #4caf50;
}

.card-description.cause {
  color: #ff9800;
}

.card-description.special-symptom {
  color: #f44336;
}

/* 药物列表 */
.medicines .medicine-link {
  color: #409eff;
  cursor: pointer;
  display: inline-block;
  margin-right: 10px;
  transition: all 0.3s ease;
}

.medicines .medicine-link:hover {
  color: #66bb6a;
  transform: translateY(-2px);
}

/* 查看按钮 */
.card-action {
  text-align: center;
  margin-top: 20px;
}

.el-button {
  width: 100%;
  border-radius: 8px;
  font-weight: 500;
}

/* 分页样式 */
.pagination-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .main-content {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
    border-right: none;
    border-bottom: 1px solid #e0e6ed;
    height: auto;
    overflow-y: hidden;
  }
  
  .content-area {
    margin-left: 0;
    margin-top: 20px;
  }
  
  .card-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .card-grid {
    grid-template-columns: 1fr;
  }
  
  .pagination-container {
    padding: 0 10px;
  }
}
</style>