<script setup>
import {get} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";
import {useStore} from "@/stores";
const logout=()=>{
  //退出登录后 返回登录页面
  get('/api/auth/logout',(message)=>{
    ElMessage.success(message)
    store.auth.user=null//这里清空user 会直接去登录页面
    router.push('/')
  })
}
const store = useStore()//这里发现退出登录不行 可以理解为全局的变量吗？
</script>

<template>
    <div>
      欢迎{{store.auth.user.username}}进入学习平台
    </div>
    <div>
        <el-button @click="logout()" type="danger" plain> 退出登录</el-button>
    </div>
</template>

<style scoped>

</style>