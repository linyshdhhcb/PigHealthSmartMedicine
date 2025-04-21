import axios from "@/axios";

/** 
 * 根据主键ID查询疾病种类
 * @param {string} id 
  * @returns
 */
export function getillnessKindInfo(id) {
  return axios.get(`/illnessKind/getInfo?id=${id}`);
}


/** 
 * 新增疾病种类
 * @param {object} params 疾病种类新增实体
 * @param {string} params.name 分类名称
 * @param {string} params.info 描述
 * @returns
 */
export function illnessKindAdd(params) {
  return axios.post(`/illnessKind/illnessKindAdd`, params);
}


/** 
 * 根据主键ID删除疾病种类
 * @param {string} id 
  * @returns
 */
export function illnessKindDelete(id) {
  return axios.delete(`/illnessKind/illnessKindDelete?id=${id}`);
}


/** 
 * 根据主键ID批量删除疾病种类
 * @param {string} ids 
  * @returns
 */
export function illnessKindListDelete(ids) {
  return axios.delete(`/illnessKind/illnessKindListDelete?ids=${ids}`);
}


/** 
 * 分页查询疾病种类
 * @param {object} params 疾病种类查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {string} params.name 分类名称
 * @param {string} params.info 描述
 * @returns
 */
export function illnessKindPage(params) {
  return axios.post(`/illnessKind/illnessKindPage`, params);
}


/** 
 * 根据主键ID修改疾病种类
 * @param {object} params 疾病种类修改实体
 * @param {object} params.id 
 * @param {string} params.name 分类名称
 * @param {string} params.info 描述
 * @returns
 */
export function illnessKindUpdate(params) {
  return axios.put(`/illnessKind/illnessKindUpdate`, params);
}