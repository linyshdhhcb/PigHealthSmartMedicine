<template>
  <view class="tab-bar">
    <view
      v-for="item in list"
      :key="item.pagePath"
      class="tab-item"
      @click="switchTab(item)"
    >
      <image
        class="tab-icon"
        :src="current === item.pagePath ? item.selectedIconPath : item.iconPath"
      />
      <text class="tab-text" :class="{ active: current === item.pagePath }">
        {{ item.text }}
      </text>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const list = [
  {
    text: '首页',
    pagePath: '/pages/index/index',
    iconPath: '/static/images/hom-h.png',
    selectedIconPath: '/static/images/home.png'
  },
  {
    text: 'ai兽医',
    pagePath: '/pages/AIDoctor/AIDoctor',
    iconPath: '/static/images/message.png',
    selectedIconPath: '/static/images/message-h.png'
  },
  {
    text: '疾病',
    pagePath: '/pages/SearchIllness/SearchIllness',
    iconPath: '/static/images/search.png',
    selectedIconPath: '/static/images/search-h.png'
  },
  {
    text: '知识',
    pagePath: '/pages/Knowledge/Knowledge',
    iconPath: '/static/images/book.png',
    selectedIconPath: '/static/images/book-h.png'
  },
  {
    text: '我的',
    pagePath: '/pages/user/user',
    iconPath: '/static/images/user.png',
    selectedIconPath: '/static/images/user-h.png'
  }
]

const current = computed(() => route.path)

function switchTab(item) {
  uni.switchTab({ url: item.pagePath })
}
</script>

<style scoped>
.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50px;
  background: #fff;
  border-top: 1px solid #eee;
  display: flex;
  z-index: 999;
}
.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.tab-icon {
  width: 24px;
  height: 24px;
}
.tab-text {
  font-size: 12px;
  color: #9799a5;
}
.tab-text.active {
  color: #28b389;
}
</style>