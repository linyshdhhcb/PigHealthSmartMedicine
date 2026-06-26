<template>
  <view class="tab-bar">
    <view
      v-for="item in list"
      :key="item.pagePath"
      class="tab-item"
      :class="{ active: current === item.pagePath }"
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
    text: 'AI兽医',
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
  height: 56px;
  background: #fff;
  border-top: 1px solid #f0f0f0;
  display: flex;
  z-index: 999;
  padding-bottom: env(safe-area-inset-bottom);
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.03);
}
.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}
.tab-icon {
  width: 24px;
  height: 24px;
  transition: transform 0.2s ease;
}
.tab-item.active .tab-icon {
  transform: scale(1.1);
}
.tab-text {
  font-size: 11px;
  color: #9799a5;
  margin-top: 2px;
  transition: color 0.2s ease;
}
.tab-text.active {
  color: #2e7d32;
  font-weight: 600;
}
</style>
