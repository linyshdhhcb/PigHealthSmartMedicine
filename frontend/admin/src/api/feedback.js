import axios from "@/axios";

/** 
 * 新增反馈
 * @param {object} params 反馈新增实体
 * @param {string} params.name 反馈用户
 * @param {string} params.email 邮箱地址
 * @param {string} params.title 反馈标题
 * @param {string} params.content 反馈内容
 * @returns
 */
export function feedbackAdd(params) {
  return axios.post(`/feedback/feedbackAdd`, params);
}


/** 
 * 根据主键ID删除反馈
 * @param {string} id 
  * @returns
 */
export function feedbackDelete(id) {
  return axios.delete(`/feedback/feedbackDelete?id=${id}`);
}


/** 
 * 根据主键ID批量删除反馈
 * @param {string} ids 
  * @returns
 */
export function feedbackListDelete(ids) {
  return axios.delete(`/feedback/feedbackListDelete?ids=${ids}`);
}


/** 
 * 分页查询反馈
 * @param {object} params 反馈查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {string} params.name 反馈用户
 * @param {string} params.email 邮箱地址
 * @param {string} params.title 反馈标题
 * @param {string} params.content 反馈内容
 * @returns
 */
export function feedbackPage(params) {
  return axios.post(`/feedback/feedbackPage`, params);
}


/** 
 * 根据主键ID修改反馈
 * @param {object} params 反馈修改实体
 * @param {object} params.id 
 * @param {string} params.name 反馈用户
 * @param {string} params.email 邮箱地址
 * @param {string} params.title 反馈标题
 * @param {string} params.content 反馈内容
 * @returns
 */
export function feedbackUpdate(params) {
  return axios.put(`/feedback/feedbackUpdate`, params);
}


/** 
 * 根据主键ID查询反馈
 * @param {string} id 
  * @returns
 */
export function feedbackUpdate_1(id) {
  return axios.get(`/feedback/getInfo?id=${id}`);
}