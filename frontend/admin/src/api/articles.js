
import axios from '@/axios';

/** 
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

/** 
 * 新增文章
 * @param {object} params 文章新增实体
 * @param {string} params.title 文章标题
 * @param {string} params.content 文章内容
 * @param {string} params.author 作者
 * @param {number} params.typeId 文章类型ID，外键关联article_types表
 * @returns
 */
export function articlesAdd(params) {
  return axios.post(`/articles/articlesAdd`, params);
}

/** 
 * 根据主键ID删除文章
 * @param {string} id 
  * @returns
 */
export function articlesDelete(id) {
  return axios.delete(`/articles/articlesDelete?id=${id}`);
}

/** 
 * 根据主键ID批量删除文章
 * @param {string} ids 
  * @returns
 */
export function articlesListDelete(ids) {
  return axios.delete(`/articles/articlesListDelete?ids=${ids}`);
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

/** 
 * 根据主键ID修改文章
 * @param {object} params 文章修改实体
 * @param {object} params.id 
 * @param {string} params.title 文章标题
 * @param {string} params.content 文章内容
 * @param {string} params.author 作者
 * @param {number} params.typeId 文章类型ID，外键关联article_types表
 * @returns
 */
export function articlesUpdate(params) {
  return axios.put(`/articles/articlesUpdate`, params);
}

/** 
 * 根据主键ID查询文章
 * @param {string} id 
  * @returns
 */
export function getArticleInfo(id) {
  return axios.get(`/articles/getInfo?id=${id}`);
}