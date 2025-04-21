<template>
  <div style="width: 100%; height: 55px;margin-left: -20px;padding-left: 200px; background: rgb(32, 64, 102); color: white;display: flex;align-items: center;">
    <p style="font-size: 16px; line-height: 55px;text-align: left;">丹心擘划宏图展，医界绽奇葩。【欢迎来到智慧医药系统】</p>
    <div>
      
      <el-button type="primary" style="margin-left: 600px;" @click="handleClick"><el-icon><Search /></el-icon>搜索一下</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElButton, ElIcon } from 'element-plus'
import 'element-plus/dist/index.css'


const handleClick = () => {
  // 搜索功能
}
</script>

<style scoped>
.el-icon {
  color: white;
  font-size: 18px;
}
</style>