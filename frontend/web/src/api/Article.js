import axios from '@/axios';

export const getArticlesPage = (params) => {
  return axios.post('/articles/articlesPage', params, {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
    }
  });
};

export const addArticle = (article) => {
  return axios.post('/articles/addArticle', article, {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
    }
  });
};