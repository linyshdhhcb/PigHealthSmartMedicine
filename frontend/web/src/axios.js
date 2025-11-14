import axios from "axios";
const service = axios.create({
    // 基础URL
    baseURL: 'http://117.72.101.170:19999',
    // 请求超时5秒
    timeout: 60000,
    // 允许发送和接收 Cookie
    withCredentials: true
});


//请求拦截器
service.interceptors.request.use(
    config => {
        // 对请求参数做点什么
        const token = localStorage.getItem('token')  //拿到登录后存储的token
        if(token){
            //后端要求请求头里有个satoken
            config.headers['satoken'] = token
        }
        return config;
    },
    err => Promise.reject(err)
)

//响应拦截器
service.interceptors.response.use(
    res => res.data,
    err => {
        console.error('请求失败:', err.response?.data || '请求失败');
        if(err.response?.status === 401 || err.response?.data?.code === 500){
            //token 失效
            localStorage.clear()
            location.replace('/login')
        }
        return Promise.reject(err)
    }
)



export default service;
