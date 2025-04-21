import { createRouter, createWebHistory } from 'vue-router'
import ArticleType from '../views/ArticleType.vue';


const routes = [
  {
    path: '/',
    name: 'Home',
    component: ()=> import('@/views/Home.vue')
  },
  {
    path: '/home',
    name: 'HomeAlt',
    component: ()=> import('@/views/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: ()=> import('@/views/Login.vue')
  },
  {
    path: '/news',
    name: 'news',
    component: () => import('@/views/news.vue')
  },
  {
    path: '/serachBottom',
    name: 'serachBottom',
    component: () => import('@/components/serachBottom.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/findIllness',
    name: 'FindIllness',
    component: ()=> import('@/views/FindIllness/FindIllness.vue')
  },
  {
    path: '/Search',
    name: 'Search',
    component: () => import('@/views/Search.vue')
  },
  {
    path: '/findMedicines',
    name: 'FindMedicines',
    component: ()=> import('@/views/FindMedicines.vue')
  },
  {
    path:'/article',
    name:'Article',
    component: () => import('@/views/Article.vue')
  },
  {
    path: '/doctor',
    name: 'Doctor',
    component: () => import('@/views/Doctor.vue'),
    children: [
      {
        path: 'myInfo',
        name: 'myInfo',
        component: ()=> import('@/views/me/myInfo.vue')
      },
      {
        path: 'smartDoctor',
        name: 'SmartDoctor',
        component: () => import('@/views/me/smartDoctor.vue'),// 确保这里使用正确的组件
        
      },
      {
        path: 'Disease',
        name: 'Disease',
        component: ()=> import('@/views/me/Disease.vue')
      },
      {
        path: 'Medicine',
        name: 'Medicine',
        component: () => import('@/views/me/Medicine.vue')
      },
      {
        path: 'Feedback',
        name: 'Feedback',
        component: () => import('@/views/me/Feedback.vue')
      }
    ]
  },
  {
    path:'/user',
    name:'user',
    component: () => import('@/views/user/user.vue')
  },
  {
    path: '/searchBottom',
    name: 'searchBottom',
    component: () => import('@/components/serachBottom.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router