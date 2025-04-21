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
          <img :src="message.avatar" alt="用户头像" class="message-avatar">
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
import { getApiLLM,getOllama } from '@/api/admin/conversation.js'; // 导入 getApiLLM 接口

// 导入头像图片
import doctorAvatar from '@/assets/images/team/user-2.jpg';
import userAvatar from '@/assets/images/team/user-4.jpg';

// 消息列表
const messages = ref([
  {
    type: 'doctor',
    avatar: doctorAvatar,
    text: '管理员你好，我是您的智能专属医生薛伟，身体不舒服或者有任何需要咨询的问题，都可以向我提问，我会全心全意为您解答！'
  }
]);

// 新消息输入框绑定的变量
const newMessage = ref('');

// 发送消息的方法
const sendMessage = async () => {
  if (newMessage.value.trim() === '') return;

  // 添加用户的消息
  messages.value.push({
    type: 'user',
    avatar: userAvatar,
    text: newMessage.value
  });

  // 调用 AI 接口获取回复
  try {
    // const response = await getApiLLM(newMessage.value); // 使用 getApiLLM 接口
    const response = await getOllama(newMessage.value); // 使用 getOllama 接口

    // 添加医生的回复
    messages.value.push({
      type: 'doctor',
      avatar: doctorAvatar,
      text: response.data // 假设 AI 回复在 response.data 中
    });
  } catch (error) {
    console.error('获取 AI 回复失败', error);
    messages.value.push({
      type: 'doctor',
      avatar: doctorAvatar,
      text: '抱歉，获取回复失败，请稍后再试。'
    });
  }

  // 清空输入框
  newMessage.value = '';
};
</script>

<style scoped>
.smart-doctor {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.doctor-header {
  padding: 16px;
  border-bottom: 1px solid #ebebeb;
  display: flex;
  align-items: center;
}

.doctor-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  margin-right: 16px;
}

.doctor-info h3 {
  margin: 0;
  font-size: 18px;
}

.doctor-info p {
  margin: 4px 0 0;
  color: #999;
}

.chat-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.message {
  margin-bottom: 16px;
}

.message-item {
  display: flex;
  align-items: flex-start;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  margin-right: 8px;
}

.message-text {
  background-color: #f0f0f0;
  padding: 8px 12px;
  border-radius: 4px;
  max-width: 70%;
}

.message.user .message-text {
  background-color: #d9ecff;
  align-self: flex-end;
}

.input-area {
  display: flex;
  padding: 16px;
  border-top: 1px solid #ebebeb;
}

textarea {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  resize: none;
}

button {
  margin-left: 8px;
  padding: 8px 16px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
</style>