
import axios from "@/axios";

/** 
 * 修改密码
 * @param {string} oldPass 
  * @param {string} newPass 
  * @returns
 */
export function savePassword(oldPass, newPass) {
  return axios.post(`/user/savePassword?oldPass=${oldPass}&newPass=${newPass}`);
}

/** 
 * 退出登录
 * @returns
 */
export function logout() {
  return axios.post(`/user/logout`);
}


/** 
 * 分页查询反馈
 * @param {object} params 反馈查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {string} params.name 反馈用户
 * @param {string} params.email 邮箱地址
 * @param {string} params.title 反馈标题
 * @param {string} params.content 反馈内容
 * @returns
 */
export function feedbackPage(params) {
  return axios.post(`/feedback/feedbackPage`, params);
}

/** 
 * 新增反馈
 * @param {object} params 反馈新增实体
 * @param {string} params.name 反馈用户
 * @param {string} params.email 邮箱地址
 * @param {string} params.title 反馈标题
 * @param {string} params.content 反馈内容
 * @returns
 */
export function feedbackAdd(params) {
  return axios.post(`/feedback/feedbackAdd`, params);
}

/** 
 * 登录校验
 * @returns
 */
export function logintoken() {
  return axios.get(`/user/checkLogin`);
}

/** 
 * 邮箱密码登录
 * @param {string} email 
  * @param {string} password 
  * @returns
 */
export function emailLogin(email, password) {
  return axios.post(`/user/emailLogin?email=${email}&password=${password}`);
}


/** 
 * 根据主键ID查询用户
 * @param {string} id 
  * @returns
 */
export function getuserInfo(id) {
  return axios.get(`/user/getInfo?id=${id}`);
}


/** 
 * 账号密码登录
 * @param {string} userAccount 
  * @param {string} password 
  * @returns
 */
export function login(userAccount, password) {
  return axios.post(`/user/login?userAccount=${userAccount}&password=${password}`);
}




/** 
 * 注册
 * @param {object} params 注册实体
 * @param {string} params.code 验证码
 * @param {string} params.key 验证码key
 * @param {string} params.userAccount 用户账号
 * @param {string} params.userPwd 用户密码
 * @param {string} params.userPwds 用户密码确认
 * @param {string} params.userEmail 用户邮箱
 * @returns
 */
export function register(params) {
  return axios.post(`/user/register`, params);
}



/** 
 * 修改个人信息
 * @param {object} params 用户自己修改信息
 * @param {object} params.id 
 * @param {string} params.userName 用户的真实名字
 * @param {number} params.userAge 用户年龄
 * @param {string} params.userSex 用户性别
 * @param {string} params.userEmail 用户邮箱
 * @param {string} params.userTel 手机号
 * @param {string} params.imgPath 用户头像
 * @returns
 */
export function saveProfile(params) {
  return axios.put(`/user/saveProfile`, params);
}


/** 
 * 发送邮箱验证码
 * @param {string} email 
  * @returns
 */
export function sendEmailCode(email) {
  return axios.post(`/user/sendEmailCode?email=${email}`);
}


/** 
 * 新增用户
 * @param {object} params 用户新增实体
 * @param {string} params.userAccount 用户账号
 * @param {string} params.userName 用户的真实名字
 * @param {string} params.userPwd 用户密码
 * @param {number} params.userAge 用户年龄
 * @param {string} params.userSex 用户性别
 * @param {string} params.userEmail 用户邮箱
 * @param {string} params.userTel 手机号
 * @param {number} params.roleStatus 角色状态，1管理员，0普通用户
 * @param {string} params.imgPath 用户头像
 * @returns
 */
export function userAdd(params) {
  return axios.post(`/user/userAdd`, params);
}


/** 
 * 根据主键ID删除用户
 * @param {string} id 
  * @returns
 */
export function userDelete(id) {
  return axios.delete(`/user/userDelete?id=${id}`);
}


/** 
 * 根据主键ID批量删除用户
 * @param {string} ids 
  * @returns
 */
export function userListDelete(ids) {
  return axios.delete(`/user/userListDelete?ids=${ids}`);
}


/** 
 * 分页查询用户
 * @param {object} params 用户查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.sortField 
 * @param {string} params.sortOrder 
 * @param {string} params.userAccount 用户账号
 * @param {string} params.userName 用户的真实名字
 * @param {number} params.userAgeMax 用户年龄
 * @param {number} params.userAgeMin 用户年龄
 * @param {string} params.userSex 用户性别
 * @param {string} params.userEmail 用户邮箱
 * @param {string} params.userTel 手机号
 * @param {number} params.roleStatus 角色状态，1管理员，0普通用户
 * @returns
 */
export function userPage(params) {
  return axios.post(`/user/userPage`, params);
}



/** 
 * 根据主键ID修改用户
 * @param {object} params 用户修改实体
 * @param {object} params.id 
 * @param {string} params.userAccount 用户账号
 * @param {string} params.userName 用户的真实名字
 * @param {number} params.userAge 用户年龄
 * @param {string} params.userSex 用户性别
 * @param {string} params.userEmail 用户邮箱
 * @param {string} params.userTel 手机号
 * @param {number} params.roleStatus 角色状态，1管理员，0普通用户
 * @param {string} params.imgPath 用户头像
 * @returns
 */
export function userUpdate(params) {
  return axios.put(`/user/userUpdate`, params);
}


/** 
 * 上传文件
 * @param {File} file - 要上传的文件对象
 * @returns
 */
export function uploadFile(file){
  const formData = new FormData();
  formData.append('file',file);

  return axios.post('/files/upload',formData,)
}
