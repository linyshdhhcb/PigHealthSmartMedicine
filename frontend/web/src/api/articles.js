import axios from 'axios';

const authHeaders = () => ({
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
});

export const conversationKnowledgeAsk = (params) => {
  return axios.get('/api/conversation/getOllama', { params, headers: authHeaders() });
};
