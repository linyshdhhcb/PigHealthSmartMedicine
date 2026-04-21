<template>
  <div class="smart-doctor">
    <div class="doctor-header">
      <img src="@/assets/images/team/user-2.jpg" alt="智能医生头像" class="doctor-avatar">
      <div class="doctor-info">
        <h3>智能在线医生</h3>
        <p>支持按知识库问答，返回命中文本与标准化结论</p>
      </div>
    </div>

    <div class="toolbar">
      <label class="kb-label">知识库 ID</label>
      <input v-model="kbId" type="number" min="1" placeholder="请输入 kbId" />
    </div>

    <div class="chat-content" ref="chatRef">
      <div v-for="(message, index) in messages" :key="index" class="message">
        <div :class="['message-item', message.type]">
          <img v-if="message.type === 'user'" :src="userAvatar" alt="用户头像" class="message-avatar">
          <img v-else src="@/assets/images/team/user-2.jpg" alt="医生头像" class="message-avatar">
          <div class="message-text">
            <p class="message-title">{{ message.title }}</p>
            <p class="message-body">{{ message.text }}</p>
            <div v-if="message.chunks && message.chunks.length" class="chunk-list">
              <div v-for="chunk in message.chunks" :key="chunk.id" class="chunk-item">
                <div class="chunk-meta">
                  <span>docId: {{ chunk.docId }}</span>
                  <span>chunkIndex: {{ chunk.chunkIndex }}</span>
                  <span>fileName: {{ chunk.fileName || '未知文件' }}</span>
                </div>
                <p class="chunk-content">{{ chunk.content }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="input-area">
      <textarea
        v-model="newMessage"
        placeholder="输入要咨询的内容..."
        @keydown.enter.exact.prevent="sendMessage"
      ></textarea>
      <button :disabled="loading" @click="sendMessage">{{ loading ? '发送中...' : '发送' }}</button>
    </div>
  </div>
</template>

<script setup>
import { nextTick, ref } from 'vue';
import userAvatar from '@/assets/images/icons/avatar.jpg';
import { conversationKnowledgeAsk } from '@/api/articles.js';

const kbId = ref('');
const messages = ref([
  {
    type: 'doctor',
    title: '系统提示',
    text: '请输入 kbId 后提问，系统将返回标准化问答结果。',
    chunks: []
  }
]);
const newMessage = ref('');
const loading = ref(false);
const chatRef = ref(null);

const scrollToBottom = async () => {
  await nextTick();
  if (chatRef.value) {
    chatRef.value.scrollTop = chatRef.value.scrollHeight;
  }
};

const sendMessage = async () => {
  const question = newMessage.value.trim();
  const resolvedKbId = Number(kbId.value);
  if (!question || !resolvedKbId) return;

  messages.value.push({
    type: 'user',
    title: '用户提问',
    text: question,
    chunks: []
  });
  newMessage.value = '';
  loading.value = true;
  await scrollToBottom();

  try {
    const res = await conversationKnowledgeAsk({
      prompt: question,
      kbId: resolvedKbId,
      sessionId: 1
    });

    const data = res.data?.data || {};
    messages.value.push({
      type: 'doctor',
      title: '智能问答结果',
      text: data.aiResponse || '暂无可用回答',
      chunks: data.chunks || []
    });
  } catch (error) {
    messages.value.push({
      type: 'doctor',
      title: '系统提示',
      text: '问答请求失败，请稍后重试。',
      chunks: []
    });
  } finally {
    loading.value = false;
    await scrollToBottom();
  }
};
</script>
