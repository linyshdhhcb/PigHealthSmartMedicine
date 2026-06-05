import axios from "@/axios";

export function knowledgeBasePage(params) {
  return axios.post(`/knowledgeBase/page`, params);
}

export function knowledgeBaseGetInfo(id) {
  return axios.get(`/knowledgeBase/getInfo?id=${id}`);
}

export function knowledgeBaseAdd(params) {
  return axios.post(`/knowledgeBase/add`, params);
}

export function knowledgeBaseUpdate(params) {
  return axios.put(`/knowledgeBase/update`, params);
}

export function knowledgeBaseDelete(id) {
  return axios.delete(`/knowledgeBase/delete?id=${id}`);
}
