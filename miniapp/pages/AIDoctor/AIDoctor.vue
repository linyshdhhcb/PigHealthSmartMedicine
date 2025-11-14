<!-- src/pages/ai/AIDoctor.vue -->
<template>
  <view class="chat-page">
    <!-- 顶部标题栏 -->
    <view class="header">
      <text class="title">🐷 AI 兽医助手</text>
      <text class="subtitle">智能诊断 · 在线问诊</text>
    </view>

    <!-- 会话选择 -->
   <!-- <scroll-view class="session-bar" scroll-x>
      <view
        v-for="item in sessions"
        :key="item.sessionId"
        :class="['session-tab', currentSessionId === item.sessionId ? 'active' : '']"
        @click="switchSession(item.sessionId)"
      >
        会话 {{ item.sessionId }}
      </view>
      <view class="session-tab new" @click="createSession">＋ 新建</view>
    </scroll-view> -->

    <!-- 聊天消息区 -->
    <scroll-view
      class="chat-box"
      scroll-y
      :scroll-top="scrollTop"
      :scroll-with-animation="true"
    >
      <view v-for="(msg, i) in messages" :key="i" :class="['message-row', msg.role]">
        <image
          v-if="msg.role === 'ai'"
          class="avatar"
          src="/static/images/doctor.png"
          mode="aspectFill"
        />
        <view class="bubble">
          <text class="time">{{ msg.time }}</text>
          <text class="content">{{ msg.content }}</text>
        </view>
        <image
          v-if="msg.role === 'user'"
          class="avatar"
          src="/static/images/user.jpg"
          mode="aspectFill"
        />
		
      </view>

      <!-- AI 加载中效果 -->
      <view v-if="loadingAI" class="loading-ai">
        <image src="/static/images/doctor.png" class="avatar" />
        <text class="loading-text">AI 正在思考中...</text>
      </view>
    </scroll-view>

    <!-- 输入框区域 -->
    <view class="input-area">
      <input
        v-model="inputText"
        class="input"
        placeholder="请描述猪只症状..."
        confirm-type="send"
        @confirm="sendMessage"
      />
      <button class="send-btn" :disabled="loadingAI" @click="sendMessage">
        {{ loadingAI ? '发送中...' : '发送' }}
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import {
  conversationPage,
  conversationAdd,
  getOllama,
  listBySession
} from '@/api/articles.js'

const userId = uni.getStorageSync('userId')  // 本地存储用户ID
const sessions = ref([]) // 所有会话
const currentSessionId = ref(null) // 当前会话
const messages = ref([]) // 聊天记录
const inputText = ref('')
const scrollTop = ref(0)
const loadingAI = ref(false)

// 自动滚动
const scrollToBottom = () => {
  nextTick(() => {
    scrollTop.value = messages.value.length * 999
  })
}

// 初始化：加载分页会话数据
onMounted(async () => {
  await loadSessions()
})

// 加载分页数据
async function loadSessions() {
  try {
    const params = { pageNum: 1, pageSize: 10, sortField: 'createTime', sortOrder: 'desc' }
    const res = await conversationPage(params)
    sessions.value = res.data?.data || []
	console.log('sessions.value',sessions.value);
	
    // 默认加载第一个会话
    if (sessions.value.length > 0) {
      currentSessionId.value = sessions.value[0].sessionId
      await loadHistory(currentSessionId.value)
    }
  } catch (err) {
    uni.showToast({ title: '加载会话失败', icon: 'none' })
  }
}



// 切换会话
async function switchSession(id) {
  if (loadingAI.value) return
  currentSessionId.value = id
  await loadHistory(id)
}

// 创建新会话
async function createSession() {
  try {
    const res = await conversationAdd({
      userId,
      sessionId: 0,
      userInput: '',
      aiResponse: '',
      conversationTime: '',
      modelName: '',
      responseTime: 0
    })
    if (res.code === 200 && res.data === true) {
      uni.showToast({ title: '新建会话成功', icon: 'success' })
      await loadSessions()
    }
  } catch {
    uni.showToast({ title: '创建会话失败', icon: 'none' })
  }
}

// 加载会话历史记录
async function loadHistory(sessionId) {
  try {
    const res = await listBySession({ sessionId })
    const list = res.data || []
	
	//展平，一条记录-变成两条记录
	messages.value = list.flatMap(item => [
		//用户消息
		{
			role:'user',
			content:item.userInput,
			time:item.conversationTime  ? item.conversationTime.slice(11,19) : ''
		},
		//ai消息
		{
			role:'ai',
			content:item.aiResponse,
			time:item.conversationTime ? item.conversationTime.slice(11,19) :''
		}
	])
	

	
	
    scrollToBottom()
  } catch {
    uni.showToast({ title: '加载历史失败', icon: 'none' })
  }
}

// 发送消息
async function sendMessage() {
  const text = inputText.value.trim()
  if (!text || !currentSessionId.value) return

  // 添加用户消息
  const now = new Date()
  const timeStr = now.toTimeString().slice(0, 8)
  messages.value.push({ role: 'user', content: text, time: timeStr })
  inputText.value = ''
  scrollToBottom()

  loadingAI.value = true

  try {
    const res = await getOllama({
      prompt: text,
      sessionId: currentSessionId.value
    })

    if (res.code === 200 && res.data) {
      messages.value.push({
        role: 'ai',
        content: res.data.aiResponse || 'AI暂时无法回复，请稍后再试～',
        time: res.data.conversationTime
          ? res.data.conversationTime.slice(11, 19)
          : new Date().toTimeString().slice(0, 8)
      })
    } else {
      messages.value.push({
        role: 'ai',
        content: 'AI响应异常，请稍后重试。',
        time: new Date().toTimeString().slice(0, 8)
      })
    }
  } catch (err) {
    uni.showToast({ title: 'AI响应失败', icon: 'none' })
  } finally {
    loadingAI.value = false
    scrollToBottom()
  }
}

</script>

<style lang="scss" scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: linear-gradient(180deg, #f8fbff 0%, #eef3f8 100%);
}

/* 顶部 */
.header {
  text-align: center;
  padding: 40rpx 0 20rpx 0;
  background: linear-gradient(135deg, #4fc3f7, #81d4fa);
  color: #fff;
  border-bottom-left-radius: 40rpx;
  border-bottom-right-radius: 40rpx;
  box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.1);

  .title {
    font-size: 40rpx;
    font-weight: 700;
  }
  .subtitle {
    font-size: 26rpx;
    opacity: 0.9;
  }
}

/* 会话选择栏 */
.session-bar {
  display: flex;
  flex-direction: row;
  padding: 16rpx 20rpx;
  background: #fff;
  border-bottom: 1px solid #eee;

  .session-tab {
    padding: 10rpx 26rpx;
    border-radius: 40rpx;
    margin-right: 20rpx;
    font-size: 26rpx;
    color: #555;
    border: 1px solid #ddd;
    background: #f9f9f9;
    transition: all 0.3s;
    &.active {
      background: linear-gradient(135deg, #43a047, #81c784);
      color: white;
      border: none;
      box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.2);
    }
    &.new {
      background: #fff;
      color: #007aff;
      border: 1px dashed #007aff;
    }
  }
}

/* 聊天区 */
.chat-box {
  flex: 1;
  padding: 30rpx;
  overflow-y: auto;
  background: #f6f8fa;
}

.message-row {
  display: flex;
  align-items: flex-end;
  margin-bottom: 28rpx;

  &.ai {
    justify-content: flex-start;
    .bubble {
      background: #fff;
      border: 1px solid #e0e0e0;
    }
  }

  &.user {
    justify-content: flex-end;
    .bubble {
      background: linear-gradient(135deg, #a5d6a7, #66bb6a);
      color: #fff;
    }
  }

  .avatar {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    margin: 0 16rpx;
    box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.15);
  }

  .bubble {
    max-width: 70%;
    padding: 20rpx 26rpx;
    border-radius: 30rpx;
    box-shadow: 0 3rpx 8rpx rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: column;
    animation: fadeIn 0.3s ease;

    .time {
      font-size: 22rpx;
      color: #aaa;
      align-self: flex-end;
      margin-bottom: 6rpx;
    }

    .content {
      font-size: 30rpx;
      line-height: 1.6;
      white-space: pre-wrap;
    }
  }
}

/* AI加载中动画 */
.loading-ai {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 16rpx;
  padding: 10rpx 30rpx;

  .avatar {
    width: 60rpx;
    height: 60rpx;
    border-radius: 50%;
  }

  .loading-text {
    font-size: 26rpx;
    color: #777;
    animation: pulse 1.5s infinite;
  }
}

@keyframes pulse {
  0% {
    opacity: 0.6;
  }
  50% {
    opacity: 1;
  }
  100% {
    opacity: 0.6;
  }
}

/* 输入框 */
.input-area {
	
	/* #ifdef H5 */
	margin-bottom: 100px;
	/* #endif */
  display: flex;
  align-items: center;
  padding: 20rpx;
  background: #fff;
  border-top: 1px solid #ddd;

  .input {
    flex: 1;
    padding: 16rpx 26rpx;
    border: 1px solid #ccc;
    border-radius: 50rpx;
    font-size: 30rpx;
    margin-right: 16rpx;
  }

  .send-btn {
    background: linear-gradient(135deg, #43a047, #66bb6a);
    color: #fff;
    border: none;
    border-radius: 50rpx;
    padding: 16rpx 40rpx;
    font-size: 30rpx;
    font-weight: 600;
    transition: all 0.3s;
  }

  .send-btn:active {
    opacity: 0.8;
  }

  .send-btn:disabled {
    opacity: 0.6;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
