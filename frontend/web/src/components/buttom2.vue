<template>
  <footer class="footer">
    <div class="container">
      <div class="section">
        <h3>友情链接</h3>
        <ul>
          <li><a href="http://120.nxin.com" target="_blank">猪病通（农信互联）</a></li>
          <li><a href="http://veterinary.scau.xk.hnlat.com/" target="_blank">华南农业大学动物医学院</a></li>
          <li><a href="http://www.hvri.ac.cn" target="_blank">中国农业科学院哈尔滨兽医研究所</a></li>
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
          <li>生猪 AI 智能诊疗系统，解析 80+ 常见猪病，智能生成诊疗方案，让猪病防控更科学、更精准！</li>
        </ul>
      </div>
    </div>
    <div class="bottom-text">© 2025 Smart Pig 智慧生猪诊疗系统</div>
  </footer>
</template>

<style scoped>
.footer {
    background: linear-gradient(180deg, #0b223d 0%, #091a31 100%);
  color: #e8f1ff;
  padding: 10px 0 10px;
  font-family: 'Segoe UI', Roboto, sans-serif;
}

.container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
  gap: 40px;
  width: 90%;
  margin: 0 auto;
}

.section h3 {
  font-size: 18px;
  margin-bottom: 15px;
  color: #12b48b;
  position: relative;
}
.section h3::after {
  content: '';
  position: absolute;
  bottom: -6px;
  left: 0;
  width: 35px;
  height: 2px;
  background: #12b48b;
  border-radius: 2px;
}

.section ul {
  list-style: none;
  padding: 0;
}
.section ul li {
  margin: 8px 0;
  font-size: 14px;
  color: #dbe6ff;
  transition: all 0.3s ease;
}
.section ul li:hover {
  color: #00c6ff;
  transform: translateX(4px);
}
.section ul li a {
  color: #dbe6ff;
  text-decoration: none;
  transition: color 0.3s ease;
}
.section ul li a:hover {
  color: #00c6ff;
}

.bottom-text {
  text-align: center;
  font-size: 13px;
  color: #a4b8d4;
  margin-top: 50px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding-top: 20px;
}

@media (max-width: 768px) {
  .container {
    grid-template-columns: 1fr 1fr;
    gap: 25px;
  }
  .section h3 {
    font-size: 16px;
  }
}
@media (max-width: 500px) {
  .container {
    grid-template-columns: 1fr;
  }
  .bottom-text {
    font-size: 12px;
  }
}
</style>

