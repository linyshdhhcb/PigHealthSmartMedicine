<template>
  <view class="chat-page">
    <view class="header">
      <text class="title">AI 兽医助手</text>
      <text class="subtitle">按知识库问答，返回命中文本与标准化结论</text>
    </view>

    <view class="kb-bar">
      <text class="kb-label">知识库 ID</text>
      <input v-model="kbId" class="kb-input" type="number" placeholder="请输入 kbId" />
    </view>

    <scroll-view class="chat-box" scroll-y :scroll-top="scrollTop" :scroll-with-animation="true">
      <view v-for="(msg, i) in messages" :key="i" :class="['message-row', msg.role]">
        <image v-if="msg.role === 'ai'" class="avatar" src="/static/images/doctor.png" mode="aspectFill" />
        <view class="bubble">
          <text class="time">{{ msg.time }}</text>
          <text class="title-line">{{ msg.title }}</text>
          <text class="content">{{ msg.content }}</text>

          <view v-if="msg.chunks && msg.chunks.length" class="chunk-list">
            <view v-for="chunk in msg.chunks" :key="chunk.id" class="chunk-item">
              <view class="chunk-meta">
                <text>docId: {{ chunk.docId }}</text>
                <text>chunkIndex: {{ chunk.chunkIndex }}</text>
                <text>fileName: {{ chunk.fileName || '未知文件' }}</text>
              </view>
              <text class="chunk-content">{{ chunk.content }}</text>
            </view>
          </view>
        </view>
        <image v-if="msg.role === 'user'" class="avatar" src="/static/images/user.jpg" mode="aspectFill" />
      </view>

      <view v-if="loadingAI" class="loading-ai">
        <image src="/static/images/doctor.png" class="avatar" mode="aspectFill" />
        <text class="loading-text">AI 正在思考中...</text>
      </view>
    </scroll-view>

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
import { conversationKnowledgeAsk } from '@/api/articles.js'

const kbId = ref('')
const messages = ref([
  {
    role: 'ai',
    title: '系统提示',
    content: '请输入 kbId 后提问，系统将返回标准化问答结果。',
    time: '' ,
    chunks: []
  }
])
const inputText = ref('')
const scrollTop = ref(0)
const loadingAI = ref(false)

const scrollToBottom = () => {
  nextTick(() => {
    scrollTop.value = messages.value.length * 999
  })
}

onMounted(() => {
  scrollToBottom()
})

async function sendMessage() {
  const text = inputText.value.trim()
  const resolvedKbId = Number(kbId.value)
  if (!text || !resolvedKbId || loadingAI.value) return

  const timeStr = new Date().toTimeString().slice(0, 8)
  messages.value.push({
    role: 'user',
    title: '用户提问',
    content: text,
    time: timeStr,
    chunks: []
  })
  inputText.value = ''
  scrollToBottom()

  loadingAI.value = true

  try {
    const res = await conversationKnowledgeAsk({
      prompt: text,
      kbId: resolvedKbId,
      sessionId: 1
    })
    const data = res.data?.data || {}
    messages.value.push({
      role: 'ai',
      title: '智能问答结果',
      content: data.aiResponse || '暂无可用回答',
      time: new Date().toTimeString().slice(0, 8),
      chunks: data.chunks || []
    })
  } catch (err) {
    messages.value.push({
      role: 'ai',
      title: '系统提示',
      content: '问答请求失败，请稍后重试。',
      time: new Date().toTimeString().slice(0, 8),
      chunks: []
    })
  } finally {
    loadingAI.value = false
    scrollToBottom()
  }
}
</script>
