import { createRouter, createWebHistory } from 'vue-router'

// 配置路由 定义两层路由
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path:'/',
      name: 'welcome',
      component:()=>import('@/views/WelcomeView.vue'),
      children:[{
        path:'',
        name:'welcome-login',
        component:()=>import('@/components/Welcome/LoginPage.vue')
      },{
        path:'register',
        name:'welcome-register',
        // 这里路由链接@/components/Welcome/RegisterRage.vue 页面
        component:()=>import('@/components/Welcome/RegisterRage.vue')
      },{
        path:'forget',
        name:'welcome-forget',
        // 这里路由链接@/components/Welcome/RegisterRage.vue 页面
        component:()=>import('@/components/Welcome/ForgetPage.vue')
      }]
    },{
        path:'/index',
        name:'index',
        component:()=>import('@/views/IndexView.vue')
    }
  ]
})

export default router
