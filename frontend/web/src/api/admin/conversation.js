import axios from "@/axios";

/**
 * 新增对话
 * @param {object} params 对话新增实体
 * @param {number} params.userId 用户ID
 * @param {string} params.userInput 用户输入
 * @param {string} params.aiResponse AI回复
 * @param {object} params.conversationTime 对话时间
 * @param {string} params.modelName AI模型名称
 * @param {number} params.responseTime AI响应时间（秒）
 * @returns
 */
export function conversationAdd(params) {
  return axios.post(`/conversation/conversationAdd`, params);
}

/**
 * 根据主键ID删除对话
 * @param {string} id
 * @returns
 */
export function conversationDelete(id) {
  return axios.delete(`/conversation/conversationDelete?id=${id}`);
}

/**
 * 根据主键ID批量删除对话
 * @param {string} ids
 * @returns
 */
export function conversationListDelete(ids) {
  return axios.delete(`/conversation/conversationListDelete?ids=${ids}`);
}

/**
 * 分页查询对话
 * @param {object} params 对话查询实体
 * @param {number} params.pageNum
 * @param {number} params.pageSize
 * @param {string} params.sortField
 * @param {string} params.sortOrder
 * @param {number} params.id 对话ID
 * @param {number} params.userId 用户ID
 * @param {string} params.userInput 用户输入
 * @param {string} params.aiResponse AI回复
 * @param {object} params.startConversationTime 对话时间
 * @param {object} params.endConversationTime 对话时间
 * @param {string} params.modelName AI模型名称
 * @param {number} params.responseTime AI响应时间（秒）
 * @returns
 */
export function conversationPage(params) {
  return axios.post(`/conversation/conversationPage`, params);
}

/**
 * 根据主键ID修改对话
 * @param {object} params 对话修改实体
 * @param {object} params.id
 * @param {number} params.userId 用户ID
 * @param {string} params.userInput 用户输入
 * @param {string} params.aiResponse AI回复
 * @param {object} params.conversationTime 对话时间
 * @param {string} params.modelName AI模型名称
 * @param {number} params.responseTime AI响应时间（秒）
 * @returns
 */
export function conversationUpdate(params) {
  return axios.put(`/conversation/conversationUpdate`, params);
}

/**
 * 调用大模型API
 * @param {string} prompt
 * @returns
 */
export function getApiLLM(prompt) {
  return axios.get(`/conversation/getApiLLM?prompt=${prompt}`);
}

/**
 * 要根据几次历史对话记录
 * @param {string} num
 * @returns
 */
export function getHistoryNum(num) {
  return axios.get(`/conversation/getHistory?num=${num}`);
}

/**
 * 根据主键ID查询对话
 * @param {string} id
 * @returns
 */
export function conversationUpdate_1(id) {
  return axios.get(`/conversation/getInfo?id=${id}`);
}

/**
 * 通过ollama调用大模型
 * @param {string} prompt
 * @returns
 */
export function getOllama(prompt, sessionId) {
  const s = sessionId ? `&sessionId=${sessionId}` : "";
  return axios.get(
    `/conversation/getOllama?prompt=${encodeURIComponent(prompt)}${s}`
  );
}

/**
 * 通过会话ID获取历史消息
 */
export function listBySession(sessionId) {
  return axios.get(`/conversation/listBySession?sessionId=${sessionId}`);
}

/**
 * 会话主表：列表、创建、关闭
 */
export function sessionList() {
  return axios.get(`/conversation/session/list`);
}
export function sessionCreate(title, modelName) {
  const t = title ? `&title=${encodeURIComponent(title)}` : "";
  const m = modelName ? `&modelName=${encodeURIComponent(modelName)}` : "";
  return axios.post(`/conversation/session/create?1=1${t}${m}`);
}
export function sessionClose(sessionId) {
  return axios.post(`/conversation/session/close?sessionId=${sessionId}`);
}
