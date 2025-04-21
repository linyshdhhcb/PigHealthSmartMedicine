<template>
  <div class="footer" style="width: 100%;height: 150px;">
    <div class="container">
      <div class="section">
        <h3>友情链接:</h3>
        <ul>
          <li>
              <a href="http://120.nxin.com">
                  <p style="color: rgb(18, 180, 139);">猪病通（农信互联）</p>
              </a>
          </li>
          <li>
              <a href="https://veterinary.scau.edu.cn">
                  <p style="color: rgb(18, 180, 139);">华南农业大学动物医学院</p>
              </a>
          </li>
          <li>
              <a href="http://www.hvri.ac.cn">
                  <p style="color: rgb(18, 180, 139);">中国农业科学院哈尔滨兽医研究所</p>
              </a>
          </li>
        </ul>
      </div>
      <div class="section">
        <h3>联系我们</h3>
        <ul>
          <li>地址：XXX XXX</li>
          <li>官网：www.xx.com</li>
          <li>电话：8888 8666</li>
  
        </ul>
      </div>
      <div class="section">
        <h3>便捷通道</h3>
        <ul>
          <li>查看疾病百科</li>
          <li>猪猪药品</li>
          <li>智能兽医</li>
          <li>新闻资讯</li>
        </ul>
      </div>
      
      <div class="section">
        <h3>网站理念</h3>
        <ul>
          <li>生猪 AI 智能诊疗系统，猪病精准诊断专家！，解析 80 + 常见猪病，智能生成诊疗方案（检测推荐 + 用药配比 + 疗程管理 + 生物安全防控），让猪病防控更科学、更高效、更精准！</li>
  
        </ul>
      </div>
    </div>
    <!-- <div class="bottom-text">© 2025 智慧医药系统</div> -->
  </div>
</template>

<script setup>
import { ref } from 'vue';

const isDarkMode = ref(false);
</script>

<style scoped>
.footer {
  background-color: rgb(11, 34, 61);
  color: #fff;
  padding: 20px 0;
  font-size: 14px; /* 基础字体大小 */
  /* max-height: 300px; */
}

.container {
  display: flex;
  justify-content: space-between;
  margin: 0 auto;
  max-width: 1200px;
  padding: 0 20px;
}

.section {
  flex: 1;
  margin-right: 20px;
  /* background-color: white; */
}

.section:last-child {
  margin-right: 0;
}

.section h3 {
  font-size: 18px; /* 调整 h3 字体大小 */
  margin-bottom: 10px;
}

.section ul {
  list-style: none;
  padding: 0;
}

.section li {
  margin-bottom: 5px;
  font-size: 14px; /* 调整 li 字体大小 */
}

.bottom-text {
  text-align: center;
  margin-top: 20px;
  font-size: 14px; /* 调整底部文本字体大小 */
}

.dark {
  background-color: #222;
  color: #ddd;
}

/* 友情链接部分的样式 */
.section:nth-last-child(1) p {
  font-size: 16px; /* 调整友情链接字体大小 */
  color: red; /* 设置字体颜色为红色 */
}
</style>