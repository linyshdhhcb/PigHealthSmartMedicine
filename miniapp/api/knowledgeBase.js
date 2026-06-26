import request from "./request.js";

export function knowledgeBasePage(params) {
  return request.post("/knowledgeBase/page", params);
}

export function knowledgeBaseGetInfo(id) {
  return request.get(`/knowledgeBase/getInfo?id=${id}`);
}

export function knowledgeBaseAdd(params) {
  return request.post("/knowledgeBase/add", params);
}

export function knowledgeBaseUpdate(params) {
  return request.put("/knowledgeBase/update", params);
}

export function knowledgeBaseDelete(id) {
  return request.delete(`/knowledgeBase/delete?id=${id}`);
}
