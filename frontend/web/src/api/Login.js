import axios from '@/axios'

export function login(userAccount, password) {
  const params = new URLSearchParams()
  params.append('userAccount', userAccount)
  params.append('password', password)

  return axios.post('/user/login', params, {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  })
}

export function getuserInfo(id) {
  return axios.get('/user/getInfo', { params: { id } })
}

export function logout() {
  return axios.post('/user/logout')
}