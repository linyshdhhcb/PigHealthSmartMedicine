// request.js - 网络请求封装
const baseURL = "http://127.0.0.1:19999";

// 请求拦截器
const httpRequest = (options) => {
  return new Promise((resolve, reject) => {
    // 获取存储的token
    const token = uni.getStorageSync("token");

    uni.request({
      url: baseURL + options.url,
      method: options.method || "GET",
      data: options.data || {},
      header: {
        "content-type": options.contentType || "application/json",
        satoken: token || "",
      },
      success: (res) => {
        console.log("请求响应:", res);
        // 请求成功
        if (res.statusCode === 200) {
          // 业务逻辑成功
          if (res.data.code === 200) {
            resolve(res.data);
          } else {
            // 业务逻辑失败
            console.error("业务逻辑失败:", res.data);
            uni.showToast({
              title: res.data.message || "请求失败",
              icon: "none",
            });
            reject(res.data);
          }
        } else {
          // HTTP状态码错误
          console.error("HTTP状态码错误:", res.statusCode);
          uni.showToast({
            title: "网络请求错误：" + res.statusCode,
            icon: "none",
          });
          reject(res);
        }
      },
      fail: (err) => {
        // 请求失败
        uni.showToast({
          title: "网络请求失败",
          icon: "none",
        });
        reject(err);
      },
    });
  });
};

// 封装常用请求方法
const request = {
  get: (url, data) => {
    return httpRequest({
      url,
      method: "GET",
      data,
    });
  },
  post: (url, data) => {
    return httpRequest({
      url,
      method: "POST",
      data,
    });
  },
  put: (url, data) => {
    return httpRequest({
      url,
      method: "PUT",
      data,
    });
  },
  delete: (url, data) => {
    return httpRequest({
      url,
      method: "DELETE",
      data,
    });
  },
};

export default request;
