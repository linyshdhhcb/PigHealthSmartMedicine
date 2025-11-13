import axios from "@/axios";

/** 
 * 分页查询疾病
 * @param {object} params 疾病查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {number} params.kindId 疾病分类ID
 * @param {string} params.illnessName 疾病名字
 * @param {string} params.includeReason 诱发因素
 * @param {string} params.illnessSymptom 疾病症状
 * @param {string} params.specialSymptom 特殊症状
 * @returns
 */
export function illnessPage(params) {
  return axios.post(`/illness/illnessPage`, params);
}

/** 
 * 根据主键ID查询浏览量
 * @param {string} id 
  * @returns
 */
export function pageviewUpdate_1(id) {
  return axios.get(`/pageview/getInfo?id=${id}`);
}


/** 
 * 新增浏览量
 * @param {object} params 浏览量新增实体
 * @param {number} params.pageviews 浏览量
 * @param {number} params.illnessId 病的id
 * @returns
 */
export function pageviewAdd(params) {
  return axios.post(`/pageview/pageviewAdd`, params);
}


/** 
 * 根据主键ID删除浏览量
 * @param {string} id 
  * @returns
 */
export function pageviewDelete(id) {
  return axios.delete(`/pageview/pageviewDelete?id=${id}`);
}


/** 
 * 根据主键ID批量删除浏览量
 * @param {string} ids 
  * @returns
 */
export function pageviewListDelete(ids) {
  return axios.delete(`/pageview/pageviewListDelete?ids=${ids}`);
}


/** 
 * 分页查询浏览量
 * @param {object} params 浏览量查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {number} params.pageviewsMax 浏览量
 * @param {number} params.pageviewsMin 浏览量
 * @param {number} params.illnessId 病的id
 * @returns
 */
export function pageviewPage(params) {
  return axios.post(`/pageview/pageviewPage`, params);
}


/** 
 * 根据主键ID修改浏览量
 * @param {object} params 浏览量修改实体
 * @param {object} params.id 
 * @param {number} params.pageviews 浏览量
 * @param {number} params.illnessId 病的id
 * @returns
 */
export function pageviewUpdate(params) {
  return axios.put(`/pageview/pageviewUpdate`, params);
}


/** 
 * 分页查询文章
 * @param {object} params 文章查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {string} params.title 文章标题
 * @param {string} params.content 文章内容
 * @param {string} params.author 作者
 * @param {number} params.typeId 文章类型ID，外键关联article_types表
 * @returns
 */
export function articlesPage(params) {
  return axios.post(`/articles/articlesPage`, params);
}