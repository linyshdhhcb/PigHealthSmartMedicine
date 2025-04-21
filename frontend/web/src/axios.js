import axios from "axios";
const service = axios.create({
    // 基础URL
    baseURL: 'http://117.72.101.170:9999',
    // 请求超时5秒
    timeout: 5000,
    // 允许发送和接收 Cookie
    withCredentials: true
});

// 请求拦截器
service.interceptors.request.use(function (config) {
    // config.headers['Cookie'] =  'satoken=ec60e835-1002-4dbb-82b4-c6b62184eb37';
    // console.log('请求配置:', config);
    return config;
}, function (err) {
    // 请求错误
    return Promise.reject(err);
});

// 响应拦截器
service.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    return response.data;
}, function (res) {
    // 响应错误
    console.error('请求失败:', res.response.data || '请求失败');
    return Promise.reject(res);
});

export default service;