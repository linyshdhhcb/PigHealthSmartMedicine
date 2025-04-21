import axios from "@/axios";

/**
 * 用户登录
 * @param {object} params 登录参数
 * @param {string} params.userAccount 用户名
 * @param {string} params.password 密码
 * @returns
 */
export function login(params) {
  return axios.post('/user/login', params, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&';
      }
      return ret;
    }]
  })
  .then(response => {
    console.log('登录响应数据:', response.data);
    return response.data;
  })
  .catch(error => {
    console.error('登录失败:', error);
    throw error;
  });
}


/**
 * 从 document.cookie 中获取指定名称的 Cookie 值
 * @param {string} satoken - Cookie 名称
 * @returns {string|null} - Cookie 值或 null（如果未找到）
 */
export function getCookie(satoken) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${satoken}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
}