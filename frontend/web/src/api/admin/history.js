import axios from "@/axios";

/** 
 * 根据主键ID查询操作记录
 * @param {string} id 
  * @returns
 */
export function historyUpdate_1(id) {
  return axios.get(`/history/getInfo?id=${id}`);
}


/** 
 * 新增操作记录
 * @param {object} params 操作记录新增实体
 * @param {number} params.userId 用户ID
 * @param {string} params.keyword 搜索关键字
 * @param {number} params.operateType 类型：1搜索，2科目，3药品
 * @returns
 */
export function historyAdd(params) {
  return axios.post(`/history/historyAdd`, params);
}


/** 
 * 根据主键ID删除操作记录
 * @param {string} id 
  * @returns
 */
export function historyDelete(id) {
  return axios.delete(`/history/historyDelete?id=${id}`);
}


/** 
 * 根据主键ID批量删除操作记录
 * @param {string} ids 
  * @returns
 */
export function historyListDelete(ids) {
  return axios.delete(`/history/historyListDelete?ids=${ids}`);
}


/** 
 * 分页查询操作记录
 * @param {object} params 操作记录查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {number} params.userId 用户ID
 * @param {string} params.keyword 搜索关键字
 * @param {number} params.operateType 类型：1搜索，2科目，3药品
 * @returns
 */
export function historyPage(params) {
  return axios.post(`/history/historyPage`, params);
}


/** 
 * 根据主键ID修改操作记录
 * @param {object} params 操作记录修改实体
 * @param {object} params.id 
 * @param {number} params.userId 用户ID
 * @param {string} params.keyword 搜索关键字
 * @param {number} params.operateType 类型：1搜索，2科目，3药品
 * @returns
 */
export function historyUpdate(params) {
  return axios.put(`/history/historyUpdate`, params);
}