import service from '@/axios'   // 上面配置好的实例

/* 登录 */
export function login(userAccount, password) {
  const params = new URLSearchParams()
  params.append('userAccount', userAccount)
  params.append('password', password)

  return service.post('/user/login', params, {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  })
}

/* 拿用户信息 */
export function getuserInfo(id) {
  return service.get('/user/getInfo', { params: { id } })
}

/* 其余接口示例 */
export function logout() {
  return service.post('/user/logout')
}