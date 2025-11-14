 <template>
   <!-- 加载 / 错误 -->
   <view v-if="loading" class="center">加载中…</view>
   <view v-else-if="error" class="center">
     <text>获取失败</text>
     <button class="retry" @click="getmedicineGetinfo">重试</button>
   </view>
   <!-- 主内容 -->
   <scroll-view v-else scroll-y class="page">
     <!-- 大图 -->
     <image class="hero" :src="medicineInfo.imgPath" mode="aspectFill" @click="preview" />
 <!-- 核心头 -->
 <view class="header">
   <view class="left">
     <text class="name">{{ medicineInfo.keyword }}</text>
     <text class="brand">{{ medicineInfo.medicineBrand }}</text>
   </view>
   <text class="price">¥{{ medicineInfo.medicinePrice }}</text>
 </view>
 
 <!-- 信息组 -->
 <view class="section">
   <text class="tag">功效</text>
   <text class="content">{{ medicineInfo.medicineEffect }}</text>
 </view>
 
 <view class="section">
   <text class="tag">用法</text>
   <text class="content">{{ medicineInfo.usAge }}</text>
 </view>
 
 <view class="section">
   <text class="tag">禁忌</text>
   <text class="content danger">{{ medicineInfo.taboo }}</text>
 </view>
 
 <view class="section">
   <text class="tag">相互作用</text>
   <text class="content">{{ medicineInfo.interaction }}</text>
 </view>
 
 <!-- 底部时间 -->
 <view class="footer">创建时间：{{ medicineInfo.updateTime }}</view>
 
  </scroll-view>
</template>
<script setup>
import { medicineGetinfo } from '@/api/articles.js'
import { onLoad } from '@dcloudio/uni-app'
import { ref } from 'vue'

const medicineId = ref('')
const medicineInfo = ref(null)
const loading = ref(true)
const error = ref(false)

async function getmedicineGetinfo() {
  loading.value = true
  error.value = false
  try {
    const res = await medicineGetinfo({ id: medicineId.value })
    medicineInfo.value = res.data
  } catch {
    error.value = true
  } finally {
    loading.value = false
  }
}

function preview() {
  uni.previewImage({ urls: [medicineInfo.value.imgPath] })
}

onLoad((option) => {
  medicineId.value = option.id
  getmedicineGetinfo()
})
</script>
<style lang="scss" scoped>
/* 变量 */
$cyan: #0d7377;
$warning: #f9c74f;
$radius: 24rpx;

.page {
  height: 100vh;
  background: #ffffff;
}
.center {
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 30rpx;
  color: #999;
  .retry {
    margin-top: 32rpx;
    padding: 12rpx 48rpx;
    font-size: 28rpx;
    color: #fff;
    background: $cyan;
    border-radius: 50rpx;
  }
}

/* 大图 */
.hero {
  width: 100%;
  height: 400rpx;
}

/* 头部 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  padding: 40rpx 40rpx 0;
  .left {
    flex: 1;
    .name {
      font-size: 48rpx;
      font-weight: 700;
      color: #222;
      display: block;
    }
    .brand {
      font-size: 28rpx;
      color: #666;
      margin-top: 8rpx;
    }
  }
  .price {
    font-size: 56rpx;
    color: $warning;
    font-weight: 800;
  }
}

/* 分段信息 */
.section {
  padding: 40rpx;
  border-bottom: 1rpx solid #f2f2f2;
  .tag {
    display: inline-block;
    font-size: 26rpx;
    color: $cyan;
    background: rgba($cyan, 0.08);
    padding: 6rpx 16rpx;
    border-radius: 8rpx;
    margin-right: 16rpx;
  }
  .content {
    font-size: 32rpx;
    line-height: 1.6;
    color: #333;
    &.danger {
      color: #e63946;
    }
  }
}

/* 底部时间 */
.footer {
  text-align: center;
  font-size: 24rpx;
  color: #bbb;
  padding: 40rpx 0 60rpx;
}
</style>