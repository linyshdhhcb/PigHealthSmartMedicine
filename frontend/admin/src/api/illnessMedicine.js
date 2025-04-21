import axios from "@/axios";



/** 
 * 根据主键ID查询疾病-药物
 * @param {string} id 
  * @returns
 */
export function illnessMedicineUpdate_1(id) {
  return axios.get(`/illnessMedicine/getInfo?id=${id}`);
}


/** 
 * 新增疾病-药物
 * @param {object} params 疾病-药物新增实体
 * @param {number} params.illnessId 病id
 * @param {number} params.medicineId 药品id
 * @returns
 */
export function illnessMedicineAdd(params) {
  return axios.post(`/illnessMedicine/illnessMedicineAdd`, params);
}


/** 
 * 根据主键ID删除疾病-药物
 * @param {string} id 
  * @returns
 */
export function illnessMedicineDelete(id) {
  return axios.delete(`/illnessMedicine/illnessMedicineDelete?id=${id}`);
}


/** 
 * 根据主键ID批量删除疾病-药物
 * @param {string} ids 
  * @returns
 */
export function illnessMedicineListDelete(ids) {
  return axios.delete(`/illnessMedicine/illnessMedicineListDelete?ids=${ids}`);
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
 * 根据主键ID修改疾病-药物
 * @param {object} params 疾病-药物修改实体
 * @param {object} params.id 
 * @param {number} params.illnessId 病id
 * @param {number} params.medicineId 药品id
 * @returns
 */
export function illnessMedicineUpdate(params) {
  return axios.put(`/illnessMedicine/illnessMedicineUpdate`, params);
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