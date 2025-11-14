<!-- src/views/MedicineDetail.vue -->
<template>
  <div class="detail-page">
    <nav2 />

    <main class="detail-container">
      <div v-if="loading" class="center">加载中…</div>

      <div v-else-if="error" class="center">
        <p>获取失败</p>
        <el-button type="primary" @click="fetchInfo">重试</el-button>
      </div>

      <article v-else class="content-card">
        <!-- 返回按钮 -->
         <div style="margin-bottom:12px;">
          <el-button type="info"  circle @click="$router.back()" size="large">
            <el-icon><Back /></el-icon>
          </el-button>
         </div>
        <div class="hero-wrap">
          <img :src="formatImagePath(medicine.imgPath)" alt="hero" class="hero" loading="lazy" />
        </div>

        <section class="head">
          <div>
            <h1 class="name">{{ medicine.medicineName }}</h1>
            <p class="brand">{{ medicine.medicineBrand }}</p>
          </div>
          <div class="price">¥{{ Number(medicine.medicinePrice).toFixed(2) }}</div>
        </section>

        <section class="info-grid">
          <div class="info-block">
            <h3>功效</h3>
            <p>{{ medicine.medicineEffect }}</p>
          </div>

          <div class="info-block">
            <h3>用法</h3>
            <p>{{ medicine.usAge }}</p>
          </div>

          <div class="info-block">
            <h3>禁忌</h3>
            <p class="danger">{{ medicine.taboo }}</p>
          </div>

          <div class="info-block">
            <h3>相互作用</h3>
            <p>{{ medicine.interaction }}</p>
          </div>

          <div class="meta-row">
            <span>关键词：{{ medicine.keyword }}</span>
            <span>创建时间：{{ medicine.createTime }}</span>
            <span>更新时间：{{ medicine.updateTime }}</span>
          </div>
        </section>
      </article>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import nav2 from '@/components/nav2.vue'
import { getmedicineInfo } from '@/api/admin/medicine.js'

const route = useRoute()
const router = useRouter()
const id = route.params.id

const loading = ref(true)
const error = ref(false)
const medicine = ref({
  medicineName: '',
  medicineBrand: '',
  medicineEffect: '',
  usAge: '',
  taboo: '',
  interaction: '',
  medicinePrice: 0,
  imgPath: '',
  keyword: '',
  createTime: '',
  updateTime: ''
})

async function fetchInfo() {
  loading.value = true
  error.value = false
  try {
    const res = await getmedicineInfo(id)
    // 根据接口适配 res.data 或 res
    const data = res.data ?? res
    medicine.value = data
  } catch (err) {
    console.error('fetch detail error', err)
    error.value = true
  } finally {
    loading.value = false
  }
}

function formatImagePath(p) { return (p || '').replace(/[<>]/g, '') }

onMounted(() => {
  if (!id) {
    // 无 id 时导航回列表
    router.replace({ name: 'FineMedicine' })
    return
  }
  fetchInfo()
})
</script>

<style scoped>
.detail-container {
    margin-top: 50px;
  padding: 22px;
  display:flex;
  justify-content:center;
}
.content-card {
  width: 100%;
  max-width: 1000px;
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 10px 30px rgba(16,24,40,0.06);
}
.hero-wrap { width:100%; height: 420px; overflow:hidden; border-radius:8px; margin-bottom:16px; }
.hero { width:100%; height:100%; object-fit:cover; display:block; }

.head { display:flex; justify-content:space-between; align-items:flex-end; margin-bottom:16px; }
.name { font-size:28px; margin:0; font-weight:800; color:#222; }
.brand { color:#666; margin-top:6px; }
.price { color:#f56c6c; font-size:24px; font-weight:800; }

.info-grid { display:grid; grid-template-columns: 1fr; gap:18px; }
.info-block h3 { display:inline-block; background: rgba(13,115,119,0.08); color:#0d7377; padding:6px 12px; border-radius:8px; font-size:14px; margin:0 0 8px 0;}
.info-block p { margin:0; color:#333; line-height:1.8; font-size:15px; }

.danger { color:#e63946; font-weight:600; }

.meta-row { display:flex; gap:18px; margin-top:16px; color:#888; font-size:13px; flex-wrap:wrap; }
.center { text-align:center; padding:40px 0; color:#999; }
</style>
