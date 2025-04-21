// Login.js
import axios from 'axios';

export const login = (userAccount, password) => {
  return axios.post('http://117.72.101.170:9999/user/login', {
    userAccount: userAccount,
    password: password
  }, {
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
  });
};