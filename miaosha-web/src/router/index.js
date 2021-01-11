import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/components/layout'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: ()=>import('@/views/login')
    },
    {
      path: '/register',
      name: 'Register',
      component: ()=>import('@/views/register')
    },
    {
      path: '/market',
      name: 'Market',
      component: ()=>import('@/views/market')
    },
    {
      path: '/home',
      name: 'Home',
      component: ()=>import('@/views/home')
    },
    {
      path: '/',
      name: '',
      redirect: '/home',
    },
    {
      path: '/admin',
      name:'Admin',
      component: ()=>import('@/views/admin')
    }
  ]
})
