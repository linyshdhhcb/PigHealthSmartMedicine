// articles.js - 文章相关API
import request from "./request.js";

/**
 * 分页查询文章
 * @param {object} params 文章查询实体
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页大小
 * @param {string} params.sortField 排序字段
 * @param {string} params.sortOrder 排序方式
 * @param {string} params.title 文章标题
 * @param {string} params.content 文章内容
 * @param {string} params.author 作者
 * @param {number} params.typeId 文章类型ID，外键关联article_types表
 * @returns {Promise} 文章列表
 */
export function articlesPage(params) {
  return request.post("/articles/articlesPage", params);
}

/**
 * 根据ID获取文章详情
 * @param {string} id 文章ID
 * @returns {Promise} 文章详情
 */
export function getArticleById(id) {
  return request.get(`/articles/getInfo?id=${id}`);
}


//药品分页接口
export function medicinePage(params){
	return request.post("/medicine/medicinePage",params)
}


//分页疾病接口
export function illnessPage(params){
	return request.post("/illness/illnessPage",params)
}

//疾病-药物分页模块
export function illnessMedicinePage(params){
	return request.post("/illnessMedicine/illnessMedicinePage",params)
}


//疾病种类关联模块
export function illnessKindPage(params){
	return request.post("/illnessKind/illnessKindPage",params)
}

//分页查询浏览量
export function pageviewPage(params){
	return request.post("/pageview/pageviewPage",params)
}


//分页查询文章类型
export function articleTypesPage(params){
	return request.post("/articleTypes/articleTypesPage",params)
}



//分页查询新闻资讯
export function newsArticlesPage(params){
	return request.post("/newsArticles/newsArticlesPage",params)
}


//分页查询知识库文件
export function knowledgePage(params){
	return request.post("/knowledge/page",params)
}


//根据主键id查询药品
export function medicineGetinfo(params){
	return request.get("/medicine/getInfo",params)
}

//根据主键id查询疾病

export function illnessGetinfo(params){
	return request.get("/illness/getInfo",params)
}


//获取对话分页数据
export function conversationPage(params){
	return request.post("/conversation/conversationPage",params)
}


//新增对话数据
export function conversationAdd(params){
	return request.post("/conversation/conversationAdd",params)
}

//通过ollama调用大模型
export function getOllama(params){
	return request.get("/conversation/getOllama",params)
}

//通过会话ID获取历史记录
export function listBySession(params){
	return request.get("/conversation/listBySession",params)
}


//新增浏览量
export function pageviewAdd(params){
	return request.post("/pageview/pageviewAdd",params)
}


