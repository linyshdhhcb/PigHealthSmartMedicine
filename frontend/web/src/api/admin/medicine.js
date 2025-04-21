
import axios from '@/axios';



/** 
 * 根据主键ID查询药品
 * @param {string} id 
  * @returns
 */
export function getmedicineInfo(id) {
  return axios.get(`/medicine/getInfo?id=${id}`);
}

/** 
 * 新增药品
 * @param {object} params 药品新增实体
 * @param {string} params.medicineName 药的名字
 * @param {string} params.keyword 关键字搜索
 * @param {string} params.medicineEffect 药的功效
 * @param {string} params.medicineBrand 药的品牌
 * @param {string} params.interaction 药的相互作用
 * @param {string} params.taboo 禁忌
 * @param {string} params.usAge 用法用量
 * @param {number} params.medicineType 药的类型，0西药，1中药，2中成药
 * @param {string} params.imgPath 相关图片路径
 * @param {number} params.medicinePrice 药的价格
 * @returns
 */
export function medicineAdd(params) {
  return axios.post(`/medicine/medicineAdd`, params);
}

/** 
 * 根据主键ID删除药品
 * @param {string} id 
  * @returns
 */
export function medicineDelete(id) {
  return axios.delete(`/medicine/medicineDelete?id=${id}`);
}


/** 
 * 根据主键ID批量删除药品
 * @param {string} ids 
  * @returns
 */
export function medicineListDelete(ids) {
  return axios.delete(`/medicine/medicineListDelete?ids=${ids}`);
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
 * 根据主键ID修改药品
 * @param {object} params 药品修改实体
 * @param {object} params.id 
 * @param {string} params.medicineName 药的名字
 * @param {string} params.keyword 关键字搜索
 * @param {string} params.medicineEffect 药的功效
 * @param {string} params.medicineBrand 药的品牌
 * @param {string} params.interaction 药的相互作用
 * @param {string} params.taboo 禁忌
 * @param {string} params.usAge 用法用量
 * @param {number} params.medicineType 药的类型，0西药，1中药，2中成药
 * @param {string} params.imgPath 相关图片路径
 * @param {number} params.medicinePrice 药的价格
 * @returns
 */
export function medicineUpdate(params) {
  return axios.put(`/medicine/medicineUpdate`, params);
}