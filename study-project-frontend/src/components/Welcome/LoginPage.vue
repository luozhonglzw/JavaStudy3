<script setup>

import {Lock, User} from "@element-plus/icons-vue";
import  {reactive} from "vue";
import {ElMessage} from "element-plus";
import {get, post} from "@/net";
import router from "@/router";
import {useStore} from "@/stores";
const store = useStore()
const form=reactive({
  username:'',
  password:'',
  remember:false,
})
const login=()=>{
  if(!form.username||!form.password){
    ElMessage.warning('请填写用户名或密码')
  }else {
    post('/api/auth/login',{
      username:form.username,
      password:form.password,
      remember:form.remember
    },(message)=>{
      ElMessage.success(message)//这里登录成功后要获得用户的信息
      get('/api/user/me',(message)=>{
        store.auth.user=message
        router.push('/index')//如果已经登录了自动去index 登录成功把 user拿到 否则继续为空
      },()=>{
        store.auth.user=null
      })
    })
  }
}
</script>

<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold">登录</div>
      <div style="font-size: 14px;color: grey">在进入系统前请先输入用户名和密码进行登录</div>
    </div>
    <div style="margin-top: 30px">
      <el-input v-model="form.username" type="text" placeholder="用户名或者邮箱">
        <template #prefix>
          <el-icon>
            <User/>
          </el-icon>
        </template>
      </el-input>
      <el-input v-model="form.password" type="password" style="margin-top: 10px" placeholder="密码">
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>
    </div>
    <el-row style="margin-top: 5px">
      <el-col :span="12" style="text-align: left">
        <el-checkbox v-model="form.remember" label="记住我"></el-checkbox>
      </el-col>
      <el-col :span="12" style="text-align: right">
        <el-link @click="router.push('/forget')">忘记密码？</el-link>
      </el-col>
    </el-row>
    <div style="margin-top: 40px">
      <el-button @click="login()" style="width: 270px" type="success" plain>立即登录</el-button>
    </div>
    <div>
      <el-divider>
        <span style="color: grey;font-size: 13px">没有账号</span>
      </el-divider>
    </div>
    <div>
<!--      加一个路由链接 去注册页面-->
      <el-button style="width: 270px" @click="router.push('/register')" type="warning" plain>注册账号</el-button>
    </div>
  </div>
</template>

<style scoped>

</style>