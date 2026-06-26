import request from "./request.js";

export function knowledgePage(params) {
  return request.post("/knowledge/page", params);
}

export function knowledgeUpload(form) {
  return request.post("/knowledge/upload", form);
}

export function knowledgeDelete(id) {
  return request.delete(`/knowledge/delete?id=${id}`);
}

export function knowledgeQaByKbId(params) {
  return request.post("/knowledge/qaByKbId", params);
}
