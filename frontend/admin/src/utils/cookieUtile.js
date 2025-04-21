// src/utils/cookieUtils.js

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