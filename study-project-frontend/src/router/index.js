import { createRouter, createWebHistory } from 'vue-router'
import {useStore} from "@/stores";

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
//这里加路由指挥
router.beforeEach((to, from, next)=>{
  const store = useStore()
  if(store.auth.user!=null&&to.name.startsWith('welcome-')){//已经登录 并且想去的地方是以welcome开头的地方
    next('/index')
  }else if(store.auth.user==null&&to.fullPath.startsWith('/index')){//如果未登录 且想去的地方为index的话
    next('/')//直接去登录
  }
  else if(to.matched.length===0){
    next('/index')//如果没有权限的话 会继续丢到/ 这里要注意是matched
  }
  else {
    next()//其他情况 直接去
  }
})
export default router
