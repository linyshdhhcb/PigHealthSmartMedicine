import axios from "@/axios";

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
 * 根据主键ID查询疾病
 * @param {string} id 
  * @returns
 */
export function getillnessInfo(id) {
  return axios.get(`/illness/getInfo?id=${id}`);
}


/** 
 * 新增疾病
 * @param {object} params 疾病新增实体
 * @param {number} params.kindId 疾病分类ID
 * @param {string} params.illnessName 疾病名字
 * @param {string} params.includeReason 诱发因素
 * @param {string} params.illnessSymptom 疾病症状
 * @param {string} params.specialSymptom 特殊症状
 * @returns
 */
export function illnessAdd(params) {
  return axios.post(`/illness/illnessAdd`, params);
}


/** 
 * 根据主键ID删除疾病
 * @param {string} id 
  * @returns
 */
export function illnessDelete(id) {
  return axios.delete(`/illness/illnessDelete?id=${id}`);
}


/** 
 * 根据主键ID批量删除疾病
 * @param {string} ids 
  * @returns
 */
export function illnessListDelete(ids) {
  return axios.delete(`/illness/illnessListDelete?ids=${ids}`);
}


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
 * 根据主键ID修改疾病
 * @param {object} params 疾病修改实体
 * @param {object} params.id 
 * @param {number} params.kindId 疾病分类ID
 * @param {string} params.illnessName 疾病名字
 * @param {string} params.includeReason 诱发因素
 * @param {string} params.illnessSymptom 疾病症状
 * @param {string} params.specialSymptom 特殊症状
 * @returns
 */
export function illnessUpdate(params) {
  return axios.put(`/illness/illnessUpdate`, params);
}