<template>
     <!-- 导航栏 -->
     <nav1 />
    <nav2 />
  <div class="home-container">
 

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 背景图片 -->
      <div class="background-image">
        <img src="@/assets/images/pig/pig2.jpg" alt="背景图片">
      </div>

      <!-- 内容覆盖层 -->
      <div class="content-overlay">
        <div class="content-wrapper">
          <!-- <div class="tag-line">智诊生猪</div> -->
          <h1 class="main-title">智诊猪疾，护航安康</h1>
          <h2 class="subtitle">生猪健康诊断与咨询领航者</h2>
          <p class="description">AI Swine Health Empire—Reconstructing Disease Diagnosis and Treatment Paradigms with Quantum-Level Intelligence, Redefining the New Cosmos of Livestock Health</p>
          <div class="button-container">
            <el-button type="success" @click="handleClick">
              了解更多
              <el-icon><Right /></el-icon>
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <buttom2 />
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import nav1 from '@/components/nav1.vue'; // 引入nav1组件
import nav2 from '@/components/nav2.vue'; // 引入nav2组件
import buttom2 from '@/components/buttom2.vue'; // 引入buttom2组件
import { Right } from '@element-plus/icons-vue';

const handleClick = () => {
  // 这里可以添加了解更多按钮的逻辑
  console.log('了解更多');
}
</script>

<style scoped>
/* 全局样式 */
.home-container {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

/* 背景图片样式 */
.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}

.background-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: brightness(0.7); /* 调暗背景图片，使文字更清晰 */
}

/* 内容覆盖层样式 */
.content-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    135deg,
    rgba(0, 0, 0, 0.6) 0%,
    rgba(0, 0, 0, 0.4) 100%
  );
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  box-sizing: border-box;
}

/* 内容包装样式 */
.content-wrapper {
  max-width: 800px;
  text-align: center;
  color: #fff;
  padding: 2rem;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px); /* 模糊效果 */
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

/* 标签样式 */
.tag-line {
  font-size: 1.2rem;
  font-weight: 500;
  color: #4CAF50; /* 绿色标签 */
  margin-bottom: 0.5rem;
}

/* 主标题样式 */
.main-title {
  color: green;
   font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  border: 1px solid gray;
  background: rgb(101, 87, 90);
  /* background: linear-gradient(to right, #4CAF50, #2196F3); */
  /* -webkit-background-clip: text; */
  /* -webkit-text-fill-color: transparent;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);  */
}

/* 副标题样式 */
.subtitle {
  font-size: 1.8rem;
  font-weight: 400;
  margin-bottom: 1.5rem;
  color: #e0e0e0;
}

/* 描述文字样式 */
.description {
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 2rem;
  color: #e0e0e0;
}

/* 按钮容器样式 */
.button-container {
  margin-top: 2rem;
}

/* 按钮样式 */
.el-button {
  font-size: 1.1rem;
  padding: 0.8rem 2rem;
  border-radius: 50px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.el-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-title {
    font-size: 2.5rem;
  }

  .subtitle {
    font-size: 1.3rem;
  }

  .content-wrapper {
    padding: 1.5rem;
  }
}

@media (max-width: 480px) {
  .main-title {
    font-size: 2rem;
  }

  .subtitle {
    font-size: 1.1rem;
  }

  .content-wrapper {
    padding: 1rem;
  }
}
</style>