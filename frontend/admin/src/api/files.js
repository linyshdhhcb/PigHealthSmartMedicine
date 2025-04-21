import axios from "@/axios";

/** 
 * 新增文件信息
 * @param {object} params 文件信息新增实体
 * @param {string} params.fileName 文件名
 * @param {string} params.filePath 文件在 MinIO 中的路径
 * @param {number} params.fileSize 文件大小，单位为字节
 * @param {string} params.contentType 文件的 MIME 类型
 * @param {string} params.url 文件的url
 * @param {string} params.bucketName 文件存储的 MinIO 桶名称
 * @returns
 */
export function filesAdd(params) {
  return axios.post(`/files/filesAdd`, params);
}


/** 
 * 根据主键ID删除文件信息
 * @param {string} id 
  * @returns
 */
export function filesDelete(id) {
  return axios.delete(`/files/filesDelete?id=${id}`);
}


/** 
 * 根据主键ID批量删除文件信息
 * @param {string} ids 
  * @returns
 */
export function filesListDelete(ids) {
  return axios.delete(`/files/filesListDelete?ids=${ids}`);
}


/** 
 * 分页查询文件信息
 * @param {object} params 文件信息查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {string} params.fileName 文件名
 * @param {string} params.filePath 文件在 MinIO 中的路径
 * @param {number} params.fileSizeMin 文件大小，单位为字节
 * @param {number} params.fileSizeMax 文件大小，单位为字节
 * @param {string} params.contentType 文件的 MIME 类型
 * @param {string} params.url 文件的url
 * @param {string} params.bucketName 文件存储的 MinIO 桶名称
 * @returns
 */
export function filesPage(params) {
  return axios.post(`/files/filesPage`, params);
}


/** 
 * 根据主键ID修改文件信息
 * @param {object} params 文件信息修改实体
 * @param {object} params.id 
 * @param {string} params.fileName 文件名
 * @param {string} params.filePath 文件在 MinIO 中的路径
 * @param {number} params.fileSize 文件大小，单位为字节
 * @param {string} params.contentType 文件的 MIME 类型
 * @param {string} params.url 文件的url
 * @param {string} params.bucketName 文件存储的 MinIO 桶名称
 * @returns
 */
export function filesUpdate(params) {
  return axios.put(`/files/filesUpdate`, params);
}


/** 
 * 根据主键ID查询文件信息
 * @param {string} id 
  * @returns
 */
export function filesUpdate_1(id) {
  return axios.get(`/files/getInfo?id=${id}`);
}

/** 
 * 上传文件并生成临时url（限时时间）
 * @param {string} fileName 
  * @returns
 */
export function policy(fileName) {
  return axios.get(`/files/policy?fileName=${fileName}`);
}


/** 
 * 文件上传
 * @param {string} file 
  * @returns
 */
export function upload(file) {
  return axios.post(`/files/upload?file=${file}`);
}


/** 
 * 获取文件上传的预签名URL
 * @param {string} fileName 
  * @returns
 */
export function uploadUrl(fileName) {
  return axios.get(`/files/uploadUrl?fileName=${fileName}`);
}


/** 
 * 获取文件的URL(7天)
 * @param {string} fileName 
  * @returns
 */
export function getUrl(fileName) {
  return axios.get(`/files/url?fileName=${fileName}`);
}