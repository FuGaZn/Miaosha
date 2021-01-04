import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: ()=>import('@/views/login')
    },
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
      path: '/home',
      name: 'Home',
      component: ()=>import('@/views/home')
    }
  ]
})
