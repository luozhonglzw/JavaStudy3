<script setup xmlns:el-col="http://www.w3.org/1999/html">

import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive} from "vue";//记得导入
// 这里写数据绑定 设置表单中的东西
const form=reactive({
  username:'',
  password:'',
  password_repeat:'',//重复密码 前端做
  email:'',
  code:''
})
//写完后 记得与输入框的数据相对应
</script>

<template>


  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 100px">
      <div style="font-size: 25px;font-weight: bold">注册</div>
      <div style="font-size: 14px;color: grey">在进入学习系统前请填入相关信息</div>
    </div>
    <div style="margin-top: 50px">
      <el-input v-model="form.username" type="text" placeholder="用户名">
        <template #prefix>
          <el-icon>
            <User/>
          </el-icon>
        </template>
      </el-input>
      <el-input v-model="form.password" type="password" placeholder="密码" style="margin-top: 10px">
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>
      <el-input v-model="form.password_repeat" type="password" placeholder="重复密码" style="margin-top: 10px">
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>
      <el-input v-model="form.email" type="email" placeholder="电子邮件地址" style="margin-top: 10px">
        <!--        导入图标-->
        <template #prefix>
          <el-icon>
            <Message/>
          </el-icon>
        </template>
      </el-input>
    </div>
    <div style="margin-top: 10px">
      <el-row :gutter="10" style="width: 100%">
        <el-col :span="17">
          <el-input v-model="form.code" type="text" placeholder="请输入验证码">
            <template #prefix>
              <el-icon>
                <EditPen/>
              </el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="5">
          <el-button type="success">获取验证码</el-button>
        </el-col>
      </el-row>
    </div>
    <div style="margin-top: 80px">
      <el-button style="width: 270px" type="warning">立即注册</el-button>
    </div>
    <div style=" margin-top: 20px">
      <span style="font-size: 14px;line-height: 15px ; color: grey">已有账号? </span>
<!--      一根斜杠回到登录页面 el-link有下沉 要自己调-->
      <el-link type="primary" style="translate: 0 -2px" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>
</template>

<style scoped>

</style>