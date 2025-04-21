import axios from "@/axios";

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
 * 根据主键ID查询疾病
 * @param {string} id 
  * @returns
 */
export function getillnessInfo(id) {
  return axios.get(`/illness/getInfo?id=${id}`);
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
 * 分页查询疾病-药物
 * @param {object} params 疾病-药物查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {number} params.illnessId 病id
 * @param {number} params.medicineId 药品id
 * @returns
 */
export function illnessMedicinePage(params) {
  return axios.post(`/illnessMedicine/illnessMedicinePage`, params);
}


/** 
 * 分页查询药品
 * @param {object} params 药品查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {string} params.medicineName 药的名字
 * @param {string} params.keyword 关键字搜索
 * @param {string} params.medicineEffect 药的功效
 * @param {string} params.medicineBrand 药的品牌
 * @param {string} params.interaction 药的相互作用
 * @param {string} params.taboo 禁忌
 * @param {string} params.usAge 用法用量
 * @param {number} params.medicineType 药的类型，0西药，1中药，2中成药
 * @param {number} params.medicinePriceMin 药的价格最低
 * @param {number} params.medicinePriceMax 药的价格最高
 * @returns
 */
export function medicinePage(params) {
  return axios.post(`/medicine/medicinePage`, params);
}