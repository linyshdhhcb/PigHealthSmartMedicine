import axios from "@/axios";

export function knowledgeUpload(formData) {
  return axios.post(`/knowledge/upload`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
}

export function knowledgePage(params) {
  return axios.post(`/knowledge/page`, params);
}

export function knowledgeGetInfo(id) {
  return axios.get(`/knowledge/getInfo?id=${id}`);
}

export function knowledgeDelete(id) {
  return axios.delete(`/knowledge/delete?id=${id}`);
}

export function knowledgeBatchDelete(ids) {
  return axios.delete(`/knowledge/deleteBatch?ids=${ids}`);
}

export function knowledgeUpdateRemark(params) {
  return axios.put(`/knowledge/updateRemark`, params);
}
