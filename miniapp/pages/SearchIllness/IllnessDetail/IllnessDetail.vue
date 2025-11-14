<template>
  <!-- ======================== 加载与错误状态 ======================== -->
  <!-- 如果数据正在加载，显示加载提示 -->
  <view v-if="loading" class="center">
    <text class="loading-text">正在调取病历…</text>
  </view>

  <!-- 如果加载失败，显示错误提示和重试按钮 -->
  <view v-else-if="error" class="center">
    <text class="error-text">病历调取失败</text>
    <button class="retry" @click="loadAll">重新调取</button>
  </view>

  <!-- ======================== 正文部分 ======================== -->
  <!-- 当不处于加载或错误状态时，显示病历内容 -->
  <scroll-view v-else scroll-y class="page">
    
    <!-- 病历头部信息 -->
    <view class="record-header">
      <!-- 左侧图标 -->
      <image class="icon" src="/static/images/pig.jpg" mode="aspectFit" />
      
      <!-- 中间部分显示病名与编号 -->
      <view class="title-wrap">
        <text class="name">{{ illnessData.illnessName }}</text>
        <text class="id">病历编号：{{ illnessData.id }}</text>
      </view>

      <!-- 右侧更新时间 -->
      <text class="update">{{ illnessData.updateTime }} 更新</text>
    </view>

    <!-- ======================== 病因 / 诱因 ======================== -->
    <view class="block">
      <view class="block-hd">
        <image class="hd-icon" src="/static/images/pig.jpg" />
        <text class="hd-txt">病因 / 诱因</text>
      </view>
      <view class="block-bd">
        <!-- 使用 mp-html 组件来解析并渲染富文本（HTML内容） -->
        <mp-html :content="illnessData.includeReason"></mp-html>
      </view>
    </view>

    <!-- ======================== 常见症状 ======================== -->
    <view class="block">
      <view class="block-hd">
        <image class="hd-icon" src="/static/images/pig.jpg" />
        <text class="hd-txt">常见症状</text>
      </view>
      <view class="block-bd">
        <mp-html :content="illnessData.illnessSymptom"></mp-html>
      </view>
    </view>

    <!-- ======================== 特殊症状 ======================== -->
    <view class="block">
      <view class="block-hd">
        <image class="hd-icon" src="/static/images/pig.jpg" />
        <text class="hd-txt">特殊症状</text>
      </view>
      <view class="block-bd">
        <mp-html :content="illnessData.specialSymptom"></mp-html>
      </view>
    </view>

    <!-- ======================== 治疗药物 ======================== -->
    <view class="block drug-block">
      <view class="block-hd">
        <image class="hd-icon" src="/static/images/pig.jpg" />
        <text class="hd-txt">推荐治疗方案</text>
        <text class="hd-sub">共 {{ medicineList.length }} 种药物</text>
      </view>

      <!-- 药物列表 -->
      <view class="drug-list">
        <!-- 遍历药品数组 -->
        <view
          class="drug-card"
          v-for="m in medicineList"
          :key="m.id"
          @click="toDrugDetail(m.id)"
        >
          <!-- 左侧药物图片 -->
          <image class="drug-img" :src="m.imgPath" mode="aspectFill" />
          
          <!-- 右侧药物信息 -->
          <view class="drug-info">
            <!-- 药名与价格 -->
            <view class="info-hd">
              <text class="name">{{ m.medicineName }}</text>
              <text class="price">¥{{ m.medicinePrice }}</text>
            </view>

            <!-- 品牌与功效 -->
            <text class="brand">{{ m.medicineBrand }}</text>
            <text class="effect">{{ m.medicineEffect }}</text>

            <!-- 用法与禁忌标签 -->
            <view class="tags">
              <text class="tag-small">用法：{{ m.usAge }}</text>
              <text class="tag-small danger">禁忌：{{ m.taboo }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- ======================== 底部时间戳 ======================== -->
    <view class="footer">
      <text>创建时间：{{ illnessData.createTime }}</text>
      <text>最后更新：{{ illnessData.updateTime }}</text>
    </view>
  </scroll-view>
</template>

<script setup>
// ======================================================
// 1️⃣ 依赖导入区
// ======================================================
import { ref } from 'vue'  // Vue 3 响应式 API
import { onLoad } from '@dcloudio/uni-app' // uni-app 的页面生命周期钩子
import {
  illnessGetinfo,         // 获取病历详情的接口
  illnessMedicinePage,     // 获取病历与药物关联表的接口
  medicineGetinfo          // 获取药品详情的接口
} from '@/api/articles.js' // 引入API模块

// ======================================================
// 2️⃣ 响应式数据定义
// ======================================================
const loading = ref(true)        // 是否正在加载
const error = ref(false)         // 是否出现错误
const illnessData = ref({})      // 存储病历数据
const medicineList = ref([])     // 存储药品列表
const illnessId = ref('')        // 当前病历ID

// ======================================================
// 3️⃣ 页面加载时执行（uni-app生命周期）
// ======================================================
onLoad((opt) => {
  illnessId.value = opt.id     // 从页面参数中取出病历id
  loadAll()                    // 调用统一加载函数
})

// ======================================================
// 4️⃣ 核心函数区
// ======================================================

/**
 * 统一加载所有数据：病历信息 + 药品列表
 */
async function loadAll() {
  loading.value = true
  error.value = false
  try {
    await getIllnessInfo()    // 获取病历详情
    await getMedicineList()   // 获取药品列表
  } catch {
    error.value = true        // 任何一步失败都显示错误
  } finally {
    loading.value = false     // 不论成功失败，都结束加载
  }
}

/**
 * 获取病历信息
 */
async function getIllnessInfo() {
  const res = await illnessGetinfo({ id: illnessId.value })
  // 将接口返回的数据存储到 illnessData
  illnessData.value = res.data
}

/**
 * 获取该病对应的药品列表
 */
async function getMedicineList() {
  // 调用接口，获取所有病历-药品关联信息
  const rel = await illnessMedicinePage({
    pageNum: 1,
    pageSize: 100
  })

  // 过滤出当前病历关联的药品ID
  const ids = rel.data.data
    .filter(i => i.illnessId === illnessData.value.id)
    .map(i => i.medicineId)

  // 如果没有药物，直接返回
  if (!ids.length) return

  // 根据药品ID数组，发起多个并行请求
  const tasks = ids.map(id => medicineGetinfo({ id }))

  // Promise.all 同时等待所有请求完成
  const resArr = await Promise.all(tasks)

  // 把每个药品的返回数据提取出来存入数组
  medicineList.value = resArr.map(r => r.data)
}

/**
 * 跳转到药品详情页
 */
function toDrugDetail(id) {
  uni.navigateTo({ url: `/pages/medicine/medicineDetail?id=${id}` })
}
</script>

<style lang="scss" scoped>
/* ======================== 全局样式变量 ======================== */
$primary: #0d7377;    /* 主色调（青绿色） */
$danger: #e63946;     /* 危险色（红色） */
$radius: 24rpx;       /* 圆角大小 */
$shadow: 0 6rpx 24rpx rgba(0, 0, 0, 0.06); /* 阴影 */

/* ======================== 页面布局 ======================== */
.page {
  background: #f8f9fa;
  min-height: 100vh;
}

/* ======================== 加载 / 错误页 ======================== */
.center {
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  .loading-text,
  .error-text {
    font-size: 32rpx;
    color: #999;
  }

  .retry {
    margin-top: 32rpx;
    padding: 12rpx 56rpx;
    font-size: 30rpx;
    color: #fff;
    background: $primary;
    border-radius: 50rpx;
  }
}

/* ======================== 病历头部 ======================== */
.record-header {
  display: flex;
  align-items: center;
  background: $primary;
  color: #fff;
  padding: 40rpx;

  .icon {
    width: 80rpx;
    height: 80rpx;
    margin-right: 24rpx;
  }

  .title-wrap {
    flex: 1;
    .name {
      display: block;
      font-size: 44rpx;
      font-weight: 700;
    }
    .id {
      display: block;
      font-size: 24rpx;
      opacity: 0.85;
      margin-top: 6rpx;
    }
  }

  .update {
    font-size: 24rpx;
    opacity: 0.85;
  }
}

/* ======================== 通用内容块 ======================== */
.block {
  margin: 24rpx 24rpx 0;
  background: #fff;
  border-radius: $radius;
  box-shadow: $shadow;
  padding: 32rpx;

  &-hd {
    display: flex;
    align-items: center;
    margin-bottom: 24rpx;

    .hd-icon {
      width: 40rpx;
      height: 40rpx;
      margin-right: 12rpx;
    }

    .hd-txt {
      font-size: 34rpx;
      font-weight: 600;
      color: #222;
    }

    .hd-sub {
      margin-left: auto;
      font-size: 26rpx;
      color: #999;
    }
  }

  &-bd {
    .content {
      font-size: 32rpx;
      line-height: 1.6;
      color: #444;
      &.special {
        color: $danger;
        font-weight: 600;
      }
    }
  }
}

/* ======================== 药品列表 ======================== */
.drug-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
  margin-top: 24rpx;
}

.drug-card {
  display: flex;
  background: #f8f9fa;
  border-radius: $radius;
  padding: 24rpx;
  gap: 24rpx;

  .drug-img {
    width: 160rpx;
    height: 160rpx;
    border-radius: 12rpx;
    flex-shrink: 0;
  }

  .drug-info {
    flex: 1;
    display: flex;
    flex-direction: column;

    .info-hd {
      display: flex;
      justify-content: space-between;
      align-items: baseline;

      .name {
        font-size: 36rpx;
        font-weight: 700;
        color: #222;
      }
      .price {
        font-size: 40rpx;
        color: $danger;
        font-weight: 800;
      }
    }

    .brand {
      font-size: 28rpx;
      color: #666;
      margin-top: 6rpx;
    }

    .effect {
      font-size: 30rpx;
      color: #555;
      margin-top: 12rpx;
      line-height: 1.5;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }

    .tags {
      margin-top: 16rpx;
      display: flex;
      flex-wrap: wrap;
      gap: 12rpx;

      .tag-small {
        font-size: 24rpx;
        color: $primary;
        background: rgba($primary, 0.06);
        padding: 6rpx 16rpx;
        border-radius: 8rpx;

        &.danger {
          color: $danger;
          background: rgba($danger, 0.06);
        }
      }
    }
  }
}

/* ======================== 底部时间 ======================== */
.footer {
  text-align: center;
  font-size: 24rpx;
  color: #aaa;
  padding: 40rpx 0 60rpx;

  text {
    display: block;
    margin-top: 8rpx;
  }
}
</style>
