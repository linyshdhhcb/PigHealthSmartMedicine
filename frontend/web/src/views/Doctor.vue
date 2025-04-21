<template>
<nav2 />
  <div class="ai-vet-container">
    
    <!-- 左侧历史对话侧边栏 -->
    <div class="history-sidebar">
      <div class="sidebar-header">
        <img src="@/assets/images/icons/ys.png" class="brand-logo" alt="AI兽医">
        <h1>AI Pet Care</h1>
      </div>
      <div class="conversation-list">
        <div 
          v-for="(conv, index) in conversations" 
          :key="index"
          class="conversation-item"
          :class="{active: activeConv === index}"
          @click="activeConv = index"
        >
          <span class="pet-icon">🐶</span>
          <div class="conv-preview">
            <h4>{{ conv.petName || '未命名对话' }}</h4>
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
        <div 
          v-for="(msg, index) in currentMessages" 
          :key="index"
          class="message-bubble"
          :class="msg.sender"
        >
          <div class="message-content">
            <div class="message-header">
              <img 
                :src="msg.sender === 'user' ? userInfo.avatar : aiAvatar" 
                class="sender-avatar"
                :alt="msg.sender === 'user' ? '用户头像' : 'AI头像'"
              >
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
          <textarea
            v-model="inputMessage"
            placeholder="描述宠物症状，例如：我的狗狗最近食欲不振..."
            @keyup.enter="sendMessage"
          ></textarea>
          <button class="send-btn" @click="sendMessage">
            <img src="@/assets/images/svg/fasong.svg" alt="发送">
          </button>
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

// ✏️ 修正：正确导入图片文件
import doctorAvatar from '@/assets/images/icons/ys.png'
import userAvatarSrc from '@/assets/images/icons/avatar.jpg' // ✏️ 假设这是用户头像路径

// 用户信息
const userInfo = reactive({
  name: '毛孩子家长',
  avatar: userAvatarSrc // ✏️ 使用导入的用户头像路径
})

// 会话列表
const conversations = ref([
  {
    petName: '豆豆（金毛，3岁）',
    messages: [
      { sender: 'user', content: '医生，我家狗狗最近总是抓耳朵', timestamp: '10:30' },
      { sender: 'ai',   content: '可能是耳螨感染，建议先用宠物专用洗耳液清洁...', timestamp: '10:32' }
    ]
  },
  {
    petName: '咪咪（布偶猫，2岁）',
    messages: [
      { sender: 'user', content: '猫咪突然呕吐怎么办？', timestamp: '09:15' },
      { sender: 'ai',   content: '请描述呕吐物的颜色和形态，是否伴有其他症状...', timestamp: '09:16' }
    ]
  }
])

const activeConv = ref(0)
const inputMessage = ref('')
const messagesContainer = ref(null)

// ✏️ 修正：使用导入的医生头像路径
const aiAvatar = doctorAvatar

const currentMessages = computed(() => {
  return conversations.value[activeConv.value]?.messages || []
})

const sendMessage = () => {
  if (!inputMessage.value.trim()) return

  // 用户消息
  currentMessages.value.push({
    sender: 'user',
    content: inputMessage.value,
    timestamp: new Date().toLocaleTimeString()
  })

  // AI “打字”中指示
  const loadingMsg = { sender: 'ai', content: '', loading: true }
  currentMessages.value.push(loadingMsg)

  setTimeout(() => {
    // 替换 loading 为真正回复
    currentMessages.value.pop()
    currentMessages.value.push({
      sender: 'ai',
      content: '及时请兽医检查，明确病因后针对性治疗，如肠胃炎可用阿莫西林等抗生素、胃复安止吐，严格按指导用药，不可盲目喂药。',
      timestamp: new Date().toLocaleTimeString()
    })
    scrollToBottom()
  }, 1500)

  inputMessage.value = ''
  scrollToBottom()
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const startNewConversation = () => {
  conversations.value.unshift({ petName: '', messages: [] })
  activeConv.value = 0
}

onMounted(() => {
  // 页面加载后可自动滚动
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

            &:nth-child(2) { animation-delay: 0.2s }
            &:nth-child(3) { animation-delay: 0.4s }
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
  0%, 60%, 100% { transform: translateY(0) }
  30% { transform: translateY(-4px) }
}
</style>