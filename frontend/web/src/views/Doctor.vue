<template>
  <div>
    <nav2 />
    <div class="ai-vet-container">

      <!-- 左侧历史对话侧边栏 -->
      <div class="history-sidebar">
        <div class="sidebar-header">
          <img src="@/assets/images/icons/ys.png" class="brand-logo" alt="AI兽医">
          <h1>AI Pet Care</h1>
        </div>
        <div class="conversation-list">
          <div v-for="(conv, index) in conversations" :key="index" class="conversation-item"
            :class="{ active: activeConv === index }" @click="activeConv = index">
            <span class="pet-icon">🐶</span>
            <div class="conv-preview">
              <h4>{{ conv.petName || '新对话' }}</h4>
              <!-- <p>{{ lastMessagePreview(conv.messages) }}</p> -->
            </div>
          </div>
        </div>
        <button class="new-chat-btn" @click="startNewConversation">
          <span class="plus-icon">+</span> 新对话
        </button>
      </div>

      <!-- 右侧聊天主区域 -->
      <div class="chat-main">
        <div class="chat-header">
          <div class="ai-info">
            <div class="ai-avatar">
              <img :src="aiAvatar" alt="AI兽医助手(ai头像)"> <!-- ✏️ 使用导入的 aiAvatar -->
              <div class="status-indicator"></div>
            </div>
            <div>
              <h2>Dr.Paw 智能兽医</h2>
              <p class="ai-specialty">宠物健康顾问 | 24小时在线</p>
            </div>
          </div>
          <div class="tool-buttons">
            <button class="tool-btn">
              <img src="@/assets/images/svg/jkbk.svg" alt="健康报告">
            </button>
            <button class="tool-btn">
              <img src="@/assets/images/svg/jjqz.svg" alt="紧急求助">
            </button>
          </div>
        </div>

        <div class="chat-messages" ref="messagesContainer">
          <div v-for="(msg, index) in currentMessages" :key="index" class="message-bubble" :class="msg.sender">
            <div class="message-content">
              <div class="message-header">
                <img :src="msg.sender === 'user' ? userInfo.avatar : aiAvatar" class="sender-avatar"
                  :alt="msg.sender === 'user' ? '用户头像' : 'AI头像'">
                <span class="sender-name">{{ msg.sender === 'user' ? userInfo.name : 'Dr.Paw' }}</span>
              </div>
              <div class="message-text" v-html="msg.content"></div>
              <div v-if="msg.loading" class="typing-indicator">
                <div class="dot"></div>
                <div class="dot"></div>
                <div class="dot"></div>
              </div>
            </div>
          </div>
        </div>

        <div class="input-area">
          <div class="quick-actions">
            <button class="action-btn">
              <img src="@/assets/images/svg/sctp.svg" alt="上传图片">
            </button>
            <button class="action-btn">
              <img src="@/assets/images/svg/zzzz.svg" alt="症状自查">
            </button>
          </div>
          <div class="message-input">
            <textarea v-model="inputMessage" placeholder="描述宠物症状，例如：我的狗狗最近食欲不振..."
              @keyup.enter="sendMessage"></textarea>
            <button class="send-btn" @click="sendMessage">
              <img src="@/assets/images/svg/fasong.svg" alt="发送">
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import nav2 from '@/components/nav2.vue'
import {
  ref,
  reactive,
  onMounted,
  computed,
  nextTick
} from 'vue'
import { getOllama, getHistoryNum, sessionList, sessionCreate, listBySession } from '@/api/admin/conversation'
import DOMPurify from 'dompurify'

// ✏️ 修正：正确导入图片文件
import doctorAvatar from '@/assets/images/icons/ys.png'
import userAvatarSrc from '@/assets/images/icons/avatar.jpg' // ✏️ 假设这是用户头像路径

// 用户信息
const userInfo = reactive({
  name: '毛孩子家长',
  avatar: userAvatarSrc // ✏️ 使用导入的用户头像路径
})

// 会话列表
const conversations = ref([])
const currentSessionId = ref(null)

const activeConv = ref(0)
const inputMessage = ref('')
const messagesContainer = ref(null)

// ✏️ 修正：使用导入的医生头像路径
const aiAvatar = doctorAvatar

const currentMessages = computed(() => {
  return conversations.value[activeConv.value]?.messages || []
})

// 将 Markdown 文本渲染为安全 HTML
const escapeHtml = (str = '') => str
  .replace(/&/g, '&amp;')
  .replace(/</g, '&lt;')
  .replace(/>/g, '&gt;')
  .replace(/"/g, '&quot;')
  .replace(/'/g, '&#39;')

const renderMarkdown = (md = '') => {
  if (!md) return ''
  let html = md

  // 代码块 ```...```
  html = html.replace(/```([\s\S]*?)```/g, (m, p1) => {
    return `<pre><code>${escapeHtml(p1)}</code></pre>`
  })
  // 行内代码 `...`
  html = html.replace(/`([^`]+)`/g, (m, p1) => `<code>${escapeHtml(p1)}</code>`)
  // 标题（支持 H1-H6，先匹配更深级别以避免被更浅级别吞掉）
  html = html
    .replace(/^######\s+(.*)$/gm, '<h6>$1</h6>')
    .replace(/^#####\s+(.*)$/gm, '<h5>$1</h5>')
    .replace(/^####\s+(.*)$/gm, '<h4>$1</h4>')
    .replace(/^###\s+(.*)$/gm, '<h3>$1</h3>')
    .replace(/^##\s+(.*)$/gm, '<h2>$1</h2>')
    .replace(/^#\s+(.*)$/gm, '<h1>$1</h1>')
  // 粗体与斜体
  html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
  // 链接 [text](url)
  html = html.replace(/\[([^\]]+)\]\((https?:[^\s)]+)\)/g, '<a href="$2" target="_blank" rel="noopener noreferrer">$1</a>')
  // 无序列表
  html = html.replace(/^(?:\s*[-*]\s+.*(?:\n|$))+?/gm, (block) => {
    const items = block.trim().split(/\n/).map(line => line.replace(/^\s*[-*]\s+/, ''))
    return `<ul>${items.map(it => `<li>${it}</li>`).join('')}</ul>`
  })
  // 有序列表 1. 2. 3.
  html = html.replace(/^(?:\s*\d+\.\s+.*(?:\n|$))+?/gm, (block) => {
    const items = block.trim().split(/\n/).map(line => line.replace(/^\s*\d+\.\s+/, ''))
    return `<ol>${items.map(it => `<li>${it}</li>`).join('')}</ol>`
  })
  // 引用块 >
  html = html.replace(/^(?:>\s?.*(?:\n|$))+?/gm, (block) => {
    const lines = block.trim().split(/\n/).map(line => line.replace(/^>\s?/, ''))
    return `<blockquote>${lines.join('<br>')}</blockquote>`
  })
  // 段落换行
  html = html.replace(/\n/g, '<br>')

  return DOMPurify.sanitize(html)
}

const isSending = ref(false)

const sendMessage = async () => {
  const content = inputMessage.value.trim()
  if (!content || isSending.value) return

  // 用户消息
  currentMessages.value.push({
    sender: 'user',
    content,
    timestamp: new Date().toLocaleTimeString()
  })
  inputMessage.value = ''
  scrollToBottom()

  // AI “打字”中指示
  const loadingMsg = { sender: 'ai', content: '', loading: true }
  currentMessages.value.push(loadingMsg)
  scrollToBottom()

  isSending.value = true
  try {
    const res = await getOllama(content, null, currentSessionId.value)
    const reply = res && res.code === 200 && res.data ? (res.data.aiResponse || '抱歉，我没有获取到有效回复。') : '抱歉，服务繁忙，请稍后重试。'
    // 替换 loading 为真正回复
    currentMessages.value.pop()
    currentMessages.value.push({
      sender: 'ai',
      content: renderMarkdown(reply),
      timestamp: new Date().toLocaleTimeString()
    })
    if (res && res.data && res.data.sessionId) {
      currentSessionId.value = res.data.sessionId
    }
  } catch (e) {
    currentMessages.value.pop()
    currentMessages.value.push({ sender: 'ai', content: '网络异常或服务器错误，请稍后再试。', timestamp: new Date().toLocaleTimeString() })
  } finally {
    isSending.value = false
    scrollToBottom()
  }
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const startNewConversation = async () => {
  const created = await sessionCreate('新对话', undefined)
  if (created && created.code === 200 && created.data) {
    conversations.value.unshift({ petName: created.data.title || '新对话', sessionId: created.data.id, messages: [] })
    activeConv.value = 0
    currentSessionId.value = created.data.id
  } else {
    conversations.value.unshift({ petName: '新对话', sessionId: null, messages: [] })
    activeConv.value = 0
  }
}

onMounted(async () => {
  // 先加载会话列表
  try {
    const s = await sessionList()
    if (s && s.code === 200 && Array.isArray(s.data)) {
      conversations.value = s.data.map(item => ({ petName: item.title || '新对话', sessionId: item.id, messages: [] }))
      if (conversations.value.length > 0) {
        activeConv.value = 0
        currentSessionId.value = conversations.value[0].sessionId
        const h = await listBySession(currentSessionId.value)
        if (h && h.code === 200 && Array.isArray(h.data)) {
          conversations.value[0].messages = h.data.flatMap(m => {
            const arr = []
            if (m.userInput) arr.push({ sender: 'user', content: DOMPurify.sanitize(escapeHtml(m.userInput)).replace(/\n/g, '<br>'), timestamp: '' })
            if (m.aiResponse) arr.push({ sender: 'ai', content: renderMarkdown(m.aiResponse), timestamp: '' })
            return arr
          })
        }
      } else {
        await startNewConversation()
      }
    }
  } catch (e) {
    // 退化为最近记录
    try {
      const res = await getHistoryNum(10)
      if (res && res.code === 200 && Array.isArray(res.data)) {
        conversations.value = [{ petName: '新对话', sessionId: null, messages: [] }]
        const historyMessages = []
        res.data.forEach(item => {
          if (item.userInput) historyMessages.push({ sender: 'user', content: DOMPurify.sanitize(escapeHtml(item.userInput)).replace(/\n/g, '<br>'), timestamp: '' })
          if (item.aiResponse) historyMessages.push({ sender: 'ai', content: renderMarkdown(item.aiResponse), timestamp: '' })
        })
        conversations.value[0].messages = historyMessages
      }
    } catch { }
  }
  scrollToBottom()
})
</script>

<style lang="scss" scoped>
.ai-vet-container {
  margin-top: 10px;
  display: flex;
  height: 100vh;
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f4ff 100%);
  font-family: 'Segoe UI', system-ui, sans-serif;
  margin-top: 70px;
}

.history-sidebar {
  width: 280px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-right: 1px solid rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;

  .sidebar-header {
    padding: 24px;
    background: linear-gradient(45deg, #00c6a7, #00a8c5);
    color: white;

    .brand-logo {
      width: 60px;
      height: 60px;
      border-radius: 16px;
      margin-bottom: 16px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    h1 {
      font-size: 1.5rem;
      font-weight: 600;
      margin: 0;
    }
  }

  .conversation-list {
    flex: 1;
    overflow-y: auto;
    padding: 16px;

    .conversation-item {
      padding: 12px;
      margin-bottom: 8px;
      border-radius: 12px;
      background: white;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
      transition: all 0.2s;
      cursor: pointer;
      display: flex;
      gap: 12px;

      &:hover {
        transform: translateX(4px);
        box-shadow: 0 4px 12px rgba(0, 200, 167, 0.15);
      }

      &.active {
        background: #e6f9f5;
        border-left: 4px solid #00c6a7;
      }

      .pet-icon {
        font-size: 24px;
      }

      .conv-preview {
        h4 {
          margin: 0 0 4px;
          font-size: 14px;
          color: #333;
        }

        p {
          margin: 0;
          font-size: 12px;
          color: #666;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
    }
  }

  .new-chat-btn {
    margin: 16px;
    padding: 12px 24px;
    background: #00c6a7;
    color: white;
    border: none;
    border-radius: 8px;
    font-weight: 500;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    transition: all 0.2s;

    &:hover {
      background: #00b496;
      transform: scale(1.02);
    }

    .plus-icon {
      font-size: 1.2rem;
    }
  }
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: url('@/assets/images/pig/pig2.jpg') repeat;
  background-size: 400px;

  .chat-header {
    padding: 20px 32px;
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(8px);
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    .ai-info {
      display: flex;
      align-items: center;
      gap: 16px;

      .ai-avatar {
        position: relative;
        width: 48px;
        height: 48px;
        border-radius: 50%;
        overflow: hidden;
        box-shadow: 0 4px 12px rgba(0, 198, 167, 0.2);

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }

        .status-indicator {
          position: absolute;
          bottom: 2px;
          right: 2px;
          width: 12px;
          height: 12px;
          background: #00c6a7;
          border-radius: 50%;
          border: 2px solid white;
        }
      }

      h2 {
        margin: 0;
        font-size: 1.25rem;
        color: #333;
      }

      .ai-specialty {
        margin: 4px 0 0;
        font-size: 0.875rem;
        color: #666;
      }
    }

    .tool-buttons {
      display: flex;
      gap: 12px;

      .tool-btn {
        width: 40px;
        height: 40px;
        border: none;
        border-radius: 8px;
        background: rgba(0, 198, 167, 0.1);
        display: grid;
        place-items: center;
        transition: all 0.2s;

        &:hover {
          background: rgba(0, 198, 167, 0.2);
          transform: scale(1.05);
        }

        img {
          width: 24px;
          height: 24px;
        }
      }
    }
  }

  .chat-messages {
    flex: 1;
    padding: 24px 32px;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 24px;

    .message-bubble {
      max-width: 75%;
      animation: messageAppear 0.3s ease-out;

      &.user {
        align-self: flex-end;

        .message-content {
          background: linear-gradient(45deg, #00c6a7, #00a8c5);
          color: white;
          border-radius: 16px 16px 4px 16px;
        }
      }

      &.ai {
        .message-content {
          background: white;
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
          border-radius: 16px 16px 16px 4px;
        }
      }

      .message-content {
        padding: 16px;
        position: relative;

        .message-header {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 12px;

          .sender-avatar {
            width: 32px;
            height: 32px;
            border-radius: 50%;
          }

          .sender-name {
            font-size: 0.875rem;
            font-weight: 500;
          }
        }

        .message-text {
          line-height: 1.6;
          white-space: pre-wrap;
        }

        .typing-indicator {
          display: flex;
          gap: 4px;
          padding: 12px 0 4px;

          .dot {
            width: 8px;
            height: 8px;
            background: #ddd;
            border-radius: 50%;
            animation: typing 1.4s infinite;

            &:nth-child(2) {
              animation-delay: 0.2s
            }

            &:nth-child(3) {
              animation-delay: 0.4s
            }
          }
        }
      }
    }
  }

  .input-area {
    padding: 24px 32px;
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(8px);
    box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.03);

    .quick-actions {
      display: flex;
      gap: 8px;
      margin-bottom: 12px;

      .action-btn {
        width: 36px;
        height: 36px;
        border: none;
        border-radius: 8px;
        background: rgba(0, 198, 167, 0.1);
        display: grid;
        place-items: center;
        transition: all 0.2s;

        &:hover {
          background: rgba(0, 198, 167, 0.2);
          transform: scale(1.05);
        }

        img {
          width: 20px;
          height: 20px;
        }
      }
    }

    .message-input {
      display: flex;
      gap: 12px;
      align-items: center;

      textarea {
        flex: 1;
        padding: 14px 20px;
        border: none;
        border-radius: 16px;
        background: white;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        resize: none;
        font-size: 1rem;
        line-height: 1.5;

        &:focus {
          outline: none;
          box-shadow: 0 0 0 2px #00c6a7;
        }
      }

      .send-btn {
        width: 48px;
        height: 48px;
        border: none;
        border-radius: 50%;
        background: linear-gradient(45deg, #00c6a7, #00a8c5);
        display: grid;
        place-items: center;
        transition: all 0.2s;

        &:hover {
          transform: scale(1.05);
          box-shadow: 0 4px 12px rgba(0, 198, 167, 0.3);
        }

        img {
          width: 24px;
          height: 24px;
          filter: brightness(0) invert(1);
        }
      }
    }
  }
}

@keyframes messageAppear {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes typing {

  0%,
  60%,
  100% {
    transform: translateY(0)
  }

  30% {
    transform: translateY(-4px)
  }
}
</style>