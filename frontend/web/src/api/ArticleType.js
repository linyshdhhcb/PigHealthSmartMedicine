import axios from '@/axios';



/** 1
 * 分页查询文章类型
 * @param {object} params 文章类型查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {string} params.typeName 文章类型名称
 * @returns
 */
export function articleTypesPage(params) {
  return axios.post(`/articleTypes/articleTypesPage`, params);
}

/** 2
 * 新增文章类型
 * @param {object} params 文章类型新增实体
 * @param {string} params.typeName 文章类型名称
 * @returns
 */
export function articleTypesAdd(params) {
  return axios.post(`/articleTypes/articleTypesAdd`, params);
}


/** 3
 * 根据主键ID删除文章类型
 * @param {string} id 
 * @returns
 */
export function articleTypesDelete(id) {
  return axios.delete(`/articleTypes/articleTypesDelete?id=${id}`);
}


/** 4
 * 根据主键ID批量删除文章类型
 * @param {string} ids 
 * @returns
 */
export function articleTypesListDelete(ids) {
  return axios.delete(`/articleTypes/articleTypesListDelete?ids=${ids}`);
}

/** 5
 * 根据主键ID修改文章类型
 * @param {object} params 文章类型修改实体
 * @param {object} params.id 
 * @param {string} params.typeName 文章类型名称
 * @returns
 */
export function articleTypesUpdate(params) {
  return axios.put(`/articleTypes/articleTypesUpdate`, params);
}

/** 6
 * 根据主键ID查询文章类型
 * @param {string} id 
 * @returns
 */
export function articleTypesUpdate_1(id) {
  return axios.get(`/articleTypes/getInfo?id=${id}`);
}