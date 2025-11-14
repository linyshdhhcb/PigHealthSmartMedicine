// user.js - 用户相关API
import request from "./request.js";

/**
 * 账号密码登录
 * @param {string} userAccount 用户账号
 * @param {string} password 密码
 * @returns {Promise} 登录结果
 */
export function login(userAccount, password) {
  return request.post(
    `/user/login?userAccount=${userAccount}&password=${password}`
  );
}

/**
 * 退出登录
 * @returns {Promise} 退出结果
 */
export function logout() {
  return request.post("/user/logout");
}

/**
 * 检查登录状态
 * @returns {Promise} 登录状态
 */
export function checkLogin() {
  return request.get("/user/checkLogin");
}

/**
 * 获取用户信息
 * @param {string} id 用户ID
 * @returns {Promise} 用户信息
 */
export function getUserInfo(id) {
  return request.get(`/user/getInfo?id=${id}`);
}
