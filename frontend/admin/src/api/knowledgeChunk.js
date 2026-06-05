import axios from "@/axios";

export function knowledgeChunkPage(params) {
  return axios.post(`/knowledgeChunk/page`, params);
}

export function knowledgeChunkGetInfo(id) {
  return axios.get(`/knowledgeChunk/getInfo?id=${id}`);
}

export function knowledgeChunkDelete(id) {
  return axios.delete(`/knowledgeChunk/delete?id=${id}`);
}
