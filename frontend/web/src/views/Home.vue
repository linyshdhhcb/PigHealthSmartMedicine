<template>
  <div class="home-container">
    <nav1 />
    <nav2 />

    <div class="main-content">
      <!-- 顶部AI诊断区域 -->
      <section class="hero-section">
        <div class="hero-overlay">
          <img src="@/assets/images/pig/AIdoctor.png" alt="AI诊断" class="ai-icon" @click="goToDoctor" />
          <h1>智能兽医诊断</h1>
          <p class="ai-desc">AI 智能分析 · 精准诊断 · 在线问诊</p>
        </div>
      </section>

      <!-- 功能卡片 -->
      <section class="feature-section">
        <div class="card card-illness" @click="router.push('/findIllness')">
          <img class="card-image" src="@/assets/images/pig/illness.jpg" loading="lazy" />
          <div class="card-content">
            <h2>疾病管理</h2>
            <p>实时掌握猪只疾病动态，科学分析病情趋势</p>
            <div class="card-footer">
              <span>查看更多</span>
              <el-icon><Right /></el-icon>
            </div>
          </div>
        </div>

        <div class="card card-medicine" @click="router.push('/findMedicines')">
          <div class="card-content">
            <h2>药品管理</h2>
            <p>智能管理药品库存，精准推荐配药方案</p>
            <div class="card-footer">
              <span>立即进入</span>
              <el-icon><Right /></el-icon>
            </div>
          </div>
          <img class="card-image" src="@/assets/images/pig/medicine.jpg" loading="lazy" />
        </div>
      </section>

      <!-- 文章推荐 -->
      <section class="article-section">
        <h2>养殖知识 · 精选推荐</h2>
        <div class="article-list">
          <div class="article-card" v-for="(item, i) in articles" :key="i" @click="router.push('/news')">
            <img :src="item.img" loading="lazy" />
            <div class="article-info">
              <h3>{{ item.title }}</h3>
              <p>{{ item.desc }}</p>
              <div class="article-meta">
                <span>{{ item.year }}</span><span>{{ item.time }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>

    <buttom2 />
  </div>
</template>

<script setup>
import nav1 from '@/components/nav1.vue'
import nav2 from '@/components/nav2.vue'
import buttom2 from '@/components/buttom2.vue'
import { useRouter } from 'vue-router'
import { Right } from '@element-plus/icons-vue'

const router = useRouter()
const goToDoctor = () => router.push('/doctor')

const articles = [
  {
    title: 'A 养殖医文章文式',
    desc: '畜禽防疫体系完善中，规范用药提升成活率。',
    img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMaSaRTpLFV6w6Kods_2pvoGn8b5esZb0smQ&s',
    year: '2019',
    time: '20:17',
  },
  {
    title: '生畜饲料配制与营养搭配',
    desc: '科学喂养是养殖成功的关键，提高饲料利用率。',
    img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQQp2iJ3iFi2GFulg0dthLXtv6Bc-SLuUpYg&s',
    year: '2024',
    time: '20:16',
  },
  {
    title: '养殖知识航文专论',
    desc: '智能化养殖：数据赋能助力精准决策。',
    img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-RtENh7N6z0BJ55fyvSFoP6RmXM2eExG9DA&s',
    year: '2025',
    time: '20:25',
  },
  {
    title: '生猪健康管理与智慧医疗系统',
    desc: 'AI助力猪场疾病早预警与健康档案追踪。',
    img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQrA_iWzL1ZTFQzOp2MZ6FsBpa4Yi_a4AeBfw&s',
    year: '2024',
    time: '20:56',
  },
]
</script>

<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: linear-gradient(180deg, #f9fbfd 0%, #eef3f8 100%);
}

/* 顶部区域 */
.hero-section {
  height: 80vh;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  background: url('@/assets/images/pig/background.png') no-repeat center center / cover;
}

.hero-overlay h1 {
  font-size: 40px;
  color: #0a2440;
  margin: 20px 0 10px;
  font-weight: 700;
}

.ai-desc {
  font-size: 18px;
  color: #555;
}

.ai-icon {
  width: 220px;
  height: 220px;
  border-radius: 50%;
  cursor: pointer;
  background: linear-gradient(135deg, #12b48b, #00b4ff);
  padding: 15px;
  box-shadow: 0 0 35px rgba(18, 180, 139, 0.5);
  animation: pulse 2.8s infinite ease-in-out;
}
@keyframes pulse {
  0%,100% { transform: scale(1); box-shadow: 0 0 35px rgba(18,180,139,0.4); }
  50% { transform: scale(1.05); box-shadow: 0 0 55px rgba(18,180,139,0.6); }
}

/* 功能卡片 */
.feature-section {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 60px;
  margin-top: -100px;
  padding: 40px 20px;
}
.card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
  overflow: hidden;
}
.card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 40px rgba(0, 180, 255, 0.25);
}
.card-image {
  width: 220px;
  height: 220px;
  object-fit: cover;
}
.card-content {
  padding: 25px;
}
.card-content h2 {
  color: #007aff;
  margin-bottom: 10px;
}
.card-footer {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #12b48b;
}

/* 文章区 */
.article-section {
  width: 90%;
  margin: 80px auto;
}
.article-section h2 {
  text-align: center;
  font-size: 28px;
  color: #0a2440;
  margin-bottom: 30px;
}
.article-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 25px;
}
.article-card {
  background: linear-gradient(145deg, #0b223d, #0a192f);
  color: #fff;
  border-radius: 15px;
  overflow: hidden;
  transition: 0.3s;
}
.article-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 0 30px rgba(0, 200, 255, 0.4);
}
.article-card img {
  width: 100%;
  height: 160px;
  object-fit: cover;
}
.article-info {
  padding: 15px;
}
.article-info p {
  color: #cfdfff;
  font-size: 13px;
  margin-bottom: 8px;
}
.article-meta {
  display: flex;
  justify-content: space-between;
  color: #aaa;
  font-size: 12px;
}
</style>
