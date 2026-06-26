<template>
  <view class="page">
    <view class="top-bar">
      <view class="top-bar-inner">
        <image class="top-icon" src="/static/images/doctor.png" mode="aspectFill" />
        <view class="top-info">
          <text class="top-title">AI 兽医助手</text>
          <text class="top-sub">基于知识库的智能猪病问答</text>
        </view>
      </view>
      <picker :range="kbList" range-key="name" @change="onKbChange">
        <view class="kb-selector">
          <text class="kb-text">{{ currentKb ? currentKb.name : '选择知识库' }}</text>
          <text class="kb-arrow">▼</text>
        </view>
      </picker>
    </view>

    <scroll-view class="chat-area" scroll-y :scroll-top="scrollTop" :scroll-with-animation="true">
      <view v-if="messages.length === 0" class="welcome">
        <image class="welcome-img" src="/static/images/doctor.png" mode="aspectFill" />
        <text class="welcome-title">你好，我是 AI 兽医助手</text>
        <text class="welcome-desc">请描述猪只的症状，我将基于知识库为你提供专业建议</text>
        <view class="quick-tags">
          <view class="quick-tag" @tap="quickAsk('猪感冒发烧怎么治疗？')">🤒 猪感冒发烧</view>
          <view class="quick-tag" @tap="quickAsk('母猪产后不吃食怎么办？')">🍽️ 产后不食</view>
          <view class="quick-tag" @tap="quickAsk('仔猪拉稀怎么处理？')">💧 仔猪拉稀</view>
          <view class="quick-tag" @tap="quickAsk('猪皮肤出现红斑是什么病？')">🔴 皮肤红斑</view>
        </view>
      </view>

      <view v-for="(msg, i) in messages" :key="i" class="msg-wrap">
        <text class="msg-time">{{ msg.time }}</text>
        <view :class="['msg-row', msg.role === 'user' ? 'msg-right' : 'msg-left']">
          <image v-if="msg.role === 'ai'" class="msg-avatar" src="/static/images/doctor.png" mode="aspectFill" />
          <view :class="['msg-bubble', msg.role === 'user' ? 'bubble-user' : 'bubble-ai']">
            <text v-if="msg.role === 'user'" class="msg-text">{{ msg.content }}</text>
            <mp-html v-else :content="msg.html" :tag-style="'p{margin:8rpx 0;} li{margin:4rpx 0;}'" />
          </view>
          <image v-if="msg.role === 'user'" class="msg-avatar" src="/static/images/user.png" mode="aspectFill" />
        </view>

        <view v-if="msg.role === 'ai' && msg.chunks && msg.chunks.length" class="ref-section">
          <view class="ref-header" @tap="toggleRef(i)">
            <text class="ref-title">📚 引用知识 ({{ msg.chunks.length }})</text>
            <text class="ref-arrow">{{ refOpen[i] ? '收起 ▲' : '展开 ▼' }}</text>
          </view>
          <view v-if="refOpen[i]" class="ref-list">
            <view v-for="(chunk, ci) in msg.chunks" :key="chunk.id || ci" class="ref-item">
              <view class="ref-meta">
                <text class="ref-file">📄 {{ chunk.fileName || '未知文件' }}</text>
                <text class="ref-idx">片段 #{{ chunk.chunkIndex }}</text>
              </view>
              <text class="ref-content">{{ chunk.content }}</text>
            </view>
          </view>
        </view>
      </view>

      <view v-if="loadingAI" class="msg-wrap">
        <text class="msg-time">{{ currentTime }}</text>
        <view class="msg-row msg-left">
          <image class="msg-avatar" src="/static/images/doctor.png" mode="aspectFill" />
          <view class="msg-bubble bubble-ai">
            <view class="typing">
              <view class="typing-dot" />
              <view class="typing-dot" />
              <view class="typing-dot" />
            </view>
          </view>
        </view>
      </view>

      <view style="height: 20rpx;" />
    </scroll-view>

    <view class="input-bar">
      <view class="input-wrap">
        <input
          v-model="inputText"
          class="input-field"
          placeholder="描述猪只症状..."
          confirm-type="send"
          @confirm="sendMessage"
        />
      </view>
      <view :class="['send-btn', (loadingAI || !inputText.trim()) ? 'send-disabled' : '']" @tap="sendMessage">
        <text class="send-text">{{ loadingAI ? '...' : '发送' }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, nextTick, onMounted } from 'vue'
import { conversationKnowledgeAsk } from '@/api/articles.js'
import { knowledgeBasePage } from '@/api/knowledgeBase.js'

function renderMarkdown(md) {
  if (!md) return ''
  try {
    let html = md
    html = html.replace(/```(\w*)\n([\s\S]*?)```/g, '<pre style="background:#f5f5f5;padding:12rpx;border-radius:8rpx;overflow-x:auto;"><code>$2</code></pre>')
    html = html.replace(/`([^`]+)`/g, '<code style="background:#f0f0f0;padding:2rpx 8rpx;border-radius:4rpx;">$1</code>')
    html = html.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    html = html.replace(/\*(.+?)\*/g, '<em>$1</em>')
    html = html.replace(/^### (.+)$/gm, '<h4 style="margin:16rpx 0 8rpx;font-size:28rpx;">$1</h4>')
    html = html.replace(/^## (.+)$/gm, '<h3 style="margin:16rpx 0 8rpx;font-size:30rpx;">$1</h3>')
    html = html.replace(/^# (.+)$/gm, '<h2 style="margin:16rpx 0 8rpx;font-size:32rpx;">$1</h2>')
    html = html.replace(/^[-*] (.+)$/gm, '<li style="margin-left:24rpx;">$1</li>')
    html = html.replace(/^\d+\. (.+)$/gm, '<li style="margin-left:24rpx;">$1</li>')
    html = html.replace(/\n{2,}/g, '</p><p>')
    html = html.replace(/\n/g, '<br/>')
    html = '<p>' + html + '</p>'
    return html
  } catch (e) {
    console.error('renderMarkdown error:', e)
    return '<p>' + md.replace(/\n/g, '<br/>') + '</p>'
  }
}

const kbList = ref([])
const currentKb = ref(null)
const messages = ref([])
const inputText = ref('')
const scrollTop = ref(0)
const loadingAI = ref(false)
const refOpen = reactive({})
const currentTime = ref('')

const scrollToBottom = () => {
  nextTick(() => {
    scrollTop.value = messages.value.length * 999 + 100
  })
}

const now = () => {
  const d = new Date()
  return `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

onMounted(() => {
  loadKbList()
})

async function loadKbList() {
  try {
    const res = await knowledgeBasePage({ pageNum: 1, pageSize: 100 })
    kbList.value = res.data.data || []
    if (kbList.value.length > 0) {
      currentKb.value = kbList.value[0]
    }
  } catch (e) {
    console.error('加载知识库列表失败', e)
  }
}

function onKbChange(e) {
  currentKb.value = kbList.value[e.detail.value]
}

function toggleRef(i) {
  refOpen[i] = !refOpen[i]
}

function quickAsk(text) {
  inputText.value = text
  sendMessage()
}

async function sendMessage() {
  const text = inputText.value.trim()
  if (!text || loadingAI.value) return
  if (!currentKb.value) {
    uni.showToast({ title: '请先选择知识库', icon: 'none' })
    return
  }

  messages.value.push({
    role: 'user',
    content: text,
    time: now(),
    chunks: []
  })
  inputText.value = ''
  currentTime.value = now()
  scrollToBottom()
  loadingAI.value = true

  try {
    const res = await conversationKnowledgeAsk({
      prompt: text,
      kbId: currentKb.value.id,
      sessionId: Date.now()
    })
    const data = res.data || {}
    const aiText = data.aiResponse || '抱歉，暂时无法回答该问题。'
    messages.value.push({
      role: 'ai',
      content: aiText,
      html: renderMarkdown(aiText),
      time: now(),
      chunks: data.chunks || []
    })
  } catch (err) {
    messages.value.push({
      role: 'ai',
      content: '请求失败，请检查网络后重试。',
      html: '<p>请求失败，请检查网络后重试。</p>',
      time: now(),
      chunks: []
    })
  } finally {
    loadingAI.value = false
    scrollToBottom()
  }
}
</script>

<style scoped lang="scss">
.page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f6fa;
}

.top-bar {
  padding: 20rpx 24rpx 16rpx;
  background: linear-gradient(135deg, #43a047, #66bb6a);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.top-bar-inner {
  display: flex;
  align-items: center;
}
.top-icon {
  width: 56rpx;
  height: 56rpx;
  border-radius: 50%;
  border: 3rpx solid rgba(255,255,255,0.5);
  margin-right: 16rpx;
}
.top-info {
  display: flex;
  flex-direction: column;
}
.top-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #fff;
}
.top-sub {
  font-size: 22rpx;
  color: rgba(255,255,255,0.85);
  margin-top: 4rpx;
}
.kb-selector {
  background: rgba(255,255,255,0.2);
  border-radius: 30rpx;
  padding: 10rpx 24rpx;
  display: flex;
  align-items: center;
}
.kb-text {
  font-size: 24rpx;
  color: #fff;
  margin-right: 8rpx;
  max-width: 180rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.kb-arrow {
  font-size: 20rpx;
  color: rgba(255,255,255,0.8);
}

.chat-area {
  flex: 1;
  padding: 0 24rpx;
}

.welcome {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 80rpx;
}
.welcome-img {
  width: 140rpx;
  height: 140rpx;
  border-radius: 50%;
  border: 6rpx solid #e8f5e9;
  margin-bottom: 24rpx;
}
.welcome-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 12rpx;
}
.welcome-desc {
  font-size: 26rpx;
  color: #999;
  margin-bottom: 40rpx;
}
.quick-tags {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 16rpx;
}
.quick-tag {
  background: #fff;
  border: 2rpx solid #e0e0e0;
  border-radius: 32rpx;
  padding: 14rpx 28rpx;
  font-size: 24rpx;
  color: #43a047;
}

.msg-wrap {
  margin-top: 24rpx;
}
.msg-time {
  display: block;
  text-align: center;
  font-size: 22rpx;
  color: #bbb;
  margin-bottom: 12rpx;
}
.msg-row {
  display: flex;
  align-items: flex-start;
}
.msg-left {
  justify-content: flex-start;
}
.msg-right {
  justify-content: flex-end;
}
.msg-avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  flex-shrink: 0;
}
.msg-bubble {
  max-width: 520rpx;
  padding: 24rpx 28rpx;
  border-radius: 20rpx;
  font-size: 28rpx;
  line-height: 1.7;
  word-break: break-all;
}
.bubble-ai {
  background: #fff;
  color: #333;
  margin-left: 16rpx;
  border-top-left-radius: 4rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.06);
  padding: 20rpx 24rpx;
}
.bubble-user {
  background: #43a047;
  color: #fff;
  margin-right: 16rpx;
  border-top-right-radius: 4rpx;
}
.msg-text {
  font-size: 28rpx;
  line-height: 1.7;
}

.ref-section {
  margin-left: 88rpx;
  margin-top: 12rpx;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.04);
}
.ref-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 24rpx;
  background: #f9fbf9;
}
.ref-title {
  font-size: 24rpx;
  color: #666;
  font-weight: bold;
}
.ref-arrow {
  font-size: 22rpx;
  color: #999;
}
.ref-list {
  padding: 0 24rpx 16rpx;
}
.ref-item {
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.ref-item:last-child {
  border-bottom: none;
}
.ref-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8rpx;
}
.ref-file {
  font-size: 22rpx;
  color: #43a047;
}
.ref-idx {
  font-size: 22rpx;
  color: #bbb;
}
.ref-content {
  font-size: 24rpx;
  color: #888;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
}

.typing {
  display: flex;
  align-items: center;
  gap: 8rpx;
  padding: 8rpx 0;
}
.typing-dot {
  width: 14rpx;
  height: 14rpx;
  border-radius: 50%;
  background: #c8e6c9;
  animation: typing-bounce 1.2s infinite;
}
.typing-dot:nth-child(2) { animation-delay: 0.2s; }
.typing-dot:nth-child(3) { animation-delay: 0.4s; }
@keyframes typing-bounce {
  0%, 60%, 100% { transform: translateY(0); opacity: 0.4; }
  30% { transform: translateY(-10rpx); opacity: 1; }
}

.input-bar {
  display: flex;
  align-items: center;
  padding: 16rpx 24rpx;
  padding-bottom: calc(16rpx + env(safe-area-inset-bottom));
  background: #fff;
  border-top: 1rpx solid #eee;
}
.input-wrap {
  flex: 1;
  background: #f5f6fa;
  border-radius: 36rpx;
  padding: 0 28rpx;
  height: 76rpx;
  display: flex;
  align-items: center;
}
.input-field {
  flex: 1;
  font-size: 28rpx;
  height: 76rpx;
}
.send-btn {
  margin-left: 16rpx;
  width: 120rpx;
  height: 76rpx;
  background: linear-gradient(135deg, #43a047, #66bb6a);
  border-radius: 38rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
.send-disabled {
  opacity: 0.5;
}
.send-text {
  font-size: 28rpx;
  color: #fff;
  font-weight: bold;
}
</style>
