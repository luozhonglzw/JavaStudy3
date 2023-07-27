<script setup>
import {get} from "@/net";
import {ElMessage} from "element-plus";
import {useStore} from "@/stores";
import router from "@/router";
//如果用户请求的话 存到这里 没有存的话请求一次
const store = useStore()
if(store.auth.user==null){
  //这里写通知 如果是登录的话 不用返回登录页面
  get('/api/user/me',(message)=>{
    store.auth.user=message
    router.push('/index')//如果已经登录了自动去index 登录成功把 user拿到 否则继续为空
  },()=>{
    store.auth.user=null
  })
}

</script>
<!--<div/>  走路由 路由模块?-->
<template>
  <router-view/>
</template>

<style scoped>

</style>
