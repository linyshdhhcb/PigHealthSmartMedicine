import request from "./request.js";

export function knowledgeChunkPage(params) {
  return request.post("/knowledgeChunk/page", params);
}

export function knowledgeChunkGetInfo(id) {
  return request.get(`/knowledgeChunk/getInfo?id=${id}`);
}

export function knowledgeChunkDelete(id) {
  return request.delete(`/knowledgeChunk/delete?id=${id}`);
}
