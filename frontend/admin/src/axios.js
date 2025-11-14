import axios from "axios";
const service = axios.create({
    // 基础URL
    baseURL: 'http://127.0.0.1:9999',
    // 请求超时50秒
    timeout: 500000,
    // 允许发送和接收 Cookie
    withCredentials: true
});



//请求拦截器
service.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token') //登录后存储的token
        if(token){
            //后端要求请求头携带satoken
            config.headers['satoken'] = token
        }
        return config
    },
    err => Promise.reject(err)
)



//响应拦截器
service.interceptors.response.use(
    res => res.data,
    err => {
        console.error('请求失败',err.response?.data || err.message)
        if(err.response?.status === 401 || err.response?.data?.code === 500){
            //token失效
            localStorage.clear()
            location.replace('/login')
        }
    }
)

export default service;
