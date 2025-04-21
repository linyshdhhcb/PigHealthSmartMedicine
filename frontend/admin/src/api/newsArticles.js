import axios from "@/axios";

/** 
 * 根据主键ID查询新闻资讯
 * @param {string} id 
  * @returns
 */
export function newsArticlesGetInfo(id) {
  return axios.get(`/newsArticles/getInfo?id=${id}`);
}


/** 
 * 新增新闻资讯
 * @param {object} params 新闻资讯新增实体
 * @param {string} params.url 转载url
 * @param {string} params.title 新闻标题
 * @param {string} params.content 新闻内容
 * @param {string} params.author 作者
 * @param {object} params.publishTime 发布时间，默认为当前时间
 * @param {string} params.source 新闻来源
 * @param {string} params.summary 新闻摘要
 * @returns
 */
export function newsArticlesAdd(params) {
  return axios.post(`/newsArticles/newsArticlesAdd`, params);
}

/** 
 * 根据主键ID删除新闻资讯
 * @param {string} id 
  * @returns
 */
export function newsArticlesDelete(id) {
  return axios.delete(`/newsArticles/newsArticlesDelete?id=${id}`);
}


/** 
 * 根据主键ID批量删除新闻资讯
 * @param {string} ids 
  * @returns
 */
export function newsArticlesListDelete(ids) {
  return axios.delete(`/newsArticles/newsArticlesListDelete?ids=${ids}`);
}


/** 
 * 分页查询新闻资讯
 * @param {object} params 新闻资讯查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {string} params.url 转载url
 * @param {string} params.title 新闻标题
 * @param {string} params.content 新闻内容
 * @param {string} params.author 作者
 * @param {object} params.startPublishTime 发布时间，默认为当前时间
 * @param {object} params.endPublishTime 发布时间，默认为当前时间
 * @param {string} params.source 新闻来源
 * @param {string} params.summary 新闻摘要
 * @returns
 */
export function newsArticlesPage(params) {
  return axios.post(`/newsArticles/newsArticlesPage`, params);
}


/** 
 * 根据主键ID修改新闻资讯
 * @param {object} params 新闻资讯修改实体
 * @param {object} params.id 
 * @param {string} params.url 转载url
 * @param {string} params.title 新闻标题
 * @param {string} params.content 新闻内容
 * @param {string} params.author 作者
 * @param {object} params.publishTime 发布时间，默认为当前时间
 * @param {string} params.source 新闻来源
 * @param {string} params.summary 新闻摘要
 * @returns
 */
export function newsArticlesUpdate(params) {
  return axios.put(`/newsArticles/newsArticlesUpdate`, params);
}