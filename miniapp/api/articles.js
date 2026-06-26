import request from "./request.js";

export function conversationKnowledgeAsk({ prompt, kbId, sessionId }) {
	return request.get(`/conversation/getOllama?prompt=${encodeURIComponent(prompt)}&kbId=${kbId || ''}&sessionId=${sessionId || ''}`)
}

export function getArticleById(id) {
	return request.get(`/articles/getInfo?id=${id}`)
}

export function getArticlesPage(params) {
	return request.post("/articles/articlesPage", params)
}

export function illnessPage(params) {
	return request.post("/illness/illnessPage", params)
}

export function illnessGetinfo(id) {
	return request.get(`/illness/getInfo?id=${id}`)
}

export function illnessKindPage(params) {
	return request.post("/illnessKind/illnessKindPage", params)
}

export function illnessMedicinePage(params) {
	return request.post("/illnessMedicine/illnessMedicinePage", params)
}

export function medicineGetinfo(id) {
	return request.get(`/medicine/getInfo?id=${id}`)
}

export function medicinePage(params) {
	return request.post("/medicine/medicinePage", params)
}

export function pageviewPage(params) {
	return request.post("/pageview/pageviewPage", params)
}

export function pageviewAdd(data) {
	return request.post("/pageview/pageviewAdd", data)
}
