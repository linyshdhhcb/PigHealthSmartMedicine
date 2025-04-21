<template>
  <div class="smart-doctor">
    <!-- 智能医生头部 -->
    <div class="doctor-header">
      <img src="@/assets/images/team/user-2.jpg" alt="智能医生头像" class="doctor-avatar">
      <div class="doctor-info">
        <h3>智能在线医生</h3>
        <p>你的专属医生，随时为您服务！</p>
      </div>
    </div>

    <!-- 聊天内容区域 -->
    <div class="chat-content">
      <div v-for="(message, index) in messages" :key="index" class="message">
        <div :class="['message-item', message.type]">
          <img :src="userAvatar" alt="用户头像" class="message-avatar">
          <div class="message-text">
            <p>{{ message.text }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="input-area">
      <textarea placeholder="输入要咨询的内容..." v-model="newMessage"></textarea>
      <button @click="sendMessage">发送</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
// import userAvatar from '@/assets/images/icons/avatar.png';
import userAvatar from '@/assets/images/icons/avatar.jpg';
// 消息列表
const messages = ref([
  {
    type: 'doctor',
    avatar: '@/assets/images/team/user-2.jpg',
    text: '管理员你好，我是您的智能专属医生薛伟，身体不舒服或者有任何需要咨询的问题，都可以向我提问，我会全心全意为您解答！'
  }
]);

// 新消息输入框绑定的变量
const newMessage = ref('');

// 发送消息的方法
const sendMessage = () => {
  if (newMessage.value.trim() === '') return;

  // 添加用户的消息
  messages.value.push({
    type: 'user',
    avatar: '@/assets/images/team/user-4.jpg',
    text: newMessage.value
  });

  // 模拟AI回复
  if (newMessage.value.includes('小猪老是呕吐，食欲不振怎么办')) {
    messages.value.push({
      type: 'doctor',
      avatar: '@/assets/images/team/user-2.jpg',
      text: '及时请兽医检查，明确病因后针对性治疗，如肠胃炎可用阿莫西林等抗生素、胃复安止吐，严格按指导用药，不可盲目喂药。'
    });
  } else {
    messages.value.push({
      type: 'doctor',
      avatar: '@/assets/images/team/user-2.jpg',
      text: '这是一个示例回复，请根据实际情况提出您的问题。'
    });
  }

  // 清空输入框
  newMessage.value = '';
};
</script>

<style scoped>
/* 整体布局 */
.smart-doctor {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f7fa;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  overflow: hidden;
}

/* 医生头部样式 */
.doctor-header {
  background: linear-gradient(135deg, #61dafb, #3498db);
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.doctor-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  margin-right: 16px;
  border: 3px solid #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.doctor-info h3 {
  margin: 0;
  font-size: 1.5rem;
  color: #fff;
  font-weight: bold;
}

.doctor-info p {
  margin: 4px 0 0;
  color: #fff;
  font-size: 0.9rem;
}

/* 聊天内容区域 */
.chat-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: #fff;
  scroll-behavior: smooth;
}

.chat-content::-webkit-scrollbar {
  width: 8px;
}

.chat-content::-webkit-scrollbar-thumb {
  background-color: #bfbfbf;
  border-radius: 4px;
}

.chat-content::-webkit-scrollbar-track {
  background-color: #f1f1f1;
  border-radius: 4px;
}

.message {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
}

.message-item {
  display: flex;
  max-width: 80%;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
  object-fit: cover;
}

.message-text {
  background-color: #f1f1f1;
  padding: 12px 16px;
  border-radius: 18px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
  font-size: 0.95rem;
  line-height: 1.4;
  word-break: break-word;
}

.message.user {
  justify-content: flex-end;
  margin-left: auto;
}

.message.user .message-text {
  background-color: #e3f2fd;
  color: #0d47a1;
}

.message.doctor {
  justify-content: flex-start;
}

.message.doctor .message-text {
  background-color: #f1f8e9;
  color: #33691e;
}

/* 输入区域样式 */
.input-area {
  display: flex;
  padding: 15px;
  background-color: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}

textarea {
  flex: 1;
  height: 60px;
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  resize: none;
  font-size: 0.95rem;
  transition: border 0.3s;
  outline: none;
}

textarea:focus {
  border-color: #3498db;
}

button {
  margin-left: 10px;
  padding: 0 20px;
  height: 40px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

button:hover {
  background: linear-gradient(135deg, #2980b9, #3498db);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .doctor-header {
    padding: 15px;
  }

  .doctor-avatar {
    width: 50px;
    height: 50px;
  }

  .doctor-info h3 {
    font-size: 1.2rem;
  }
}
</style>