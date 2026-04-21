// articles.js - 文章相关API
import request from "./request.js";

export function conversationKnowledgeAsk(params){
	return request.get("/conversation/getOllama", { params })
}
