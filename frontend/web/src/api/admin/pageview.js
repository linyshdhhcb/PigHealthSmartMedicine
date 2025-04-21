import axios from "@/axios";

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