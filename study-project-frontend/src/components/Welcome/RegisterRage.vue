<script setup xmlns:el-col="http://www.w3.org/1999/html">

import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
//记得导入
// 这里写数据绑定 设置表单中的东西
const form = reactive({
  username: '',
  password: '',
  password_repeat: '',//重复密码 前端做
  email: '',
  code: ''
})
//自定义验证 username是否合法 用正则表达式
//这里要自上而下 和c语言一样否则识别不了
const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if(!/^[a-zA-z0-9\u4e00-\u9fa5]+$/.test(value)){
    //如果用户名不符合表达式 只有中文和英文
    callback(new Error('用户名不能包含特殊字符,只能是中英文'))
  }else {
    callback()//验证通过就直接callback
  }
}

//这里重复密码设置限定 因为这里是js 不能用ts的语法rule: any, value: any, callback: any
const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    //这里用来与form表单中的password检验 不一致返回报错
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}
// 写rules规则 包括所有 比如用户名不能有特殊符号 写上规则
//写完后 记得与输入框的数据相对应
const rules ={

  username:[
      //如果为空的话 显示字 el-form-item中要写上规则的名字 修改或者聚焦
    { validator: validateUsername , trigger: ['blur','change']},
    { min: 2, max: 8, message: '用户名的长度2~8字符之间', trigger: ['blur','change']},
  ],
  //这里限制密码 上面一定要有密码
  password:[
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 16, message: '密码的长度6~16字符之间', trigger: ['blur','change']},
  ],
  //这里限制重复密码的框
  password_repeat:[
    { validator: validatePassword , trigger: ['blur','change']},
  ],
  //这里限制email输入框 别忘了和el-form-item对应 prop写上规则名字
  email:[
    { required: true, message: '请输入邮件地址', trigger: 'blur' },
    {
      type: 'email', message: '请输入合法的邮箱地址', trigger: ['blur', 'change'],
    }
  ],
  //这里设定验证码的输入框
  code:[
    { required: true, message: '请输入验证码', trigger: 'blur' },
  ]
}
//有个方法验证之前的表单是否通过 判断邮箱是否有效 一开始默认无效 这里的ref有多个选择
const isEmailValid =  ref(false)
// 这里要设置值 但是不用给
const formRef =ref()
//这里写判断 更新值 自带的 一个是表单事件 就是上面写的 当输入合法的时候isValid改为isEmailValid.value的值true 记得绑定事件
const onValidate =(prop,isValid)=>{
  if(prop==='email'){
    isEmailValid.value=isValid
  }
}
// 这里写一个 form表单对所有的输入框进行判断 如果都通过了就允许注册 这是自带的 不用自己写 ref="formRef"这里要设置 绑定全部的form表单 isValid如果都通过了返回true
// 这个函数设定在form表单之外 formRef 这里外链接表单
const register=()=>{
  formRef.value.validate((isValid)=>{
    if(isValid){

    }else {
      ElMessage.warning("请完整填写上面信息")
    }
  })
}
</script>

<template>


  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 100px">
      <div style="font-size: 25px;font-weight: bold">注册</div>
      <div style="font-size: 14px;color: grey">在进入学习系统前请填入相关信息</div>
    </div>
    <div style="margin-top: 50px">
<!--      这里用el自带的表单 可以绑定前端的页面 如果不符合的话自动报错 绑定form表单 加入rules规则 自己写 这里要绑定事件 这里要拿到它的函数应用-->
      <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" type="text" placeholder="用户名">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" >
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" type="password" placeholder="重复密码" >
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>

        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" type="email" placeholder="电子邮件地址" >
            <!--        导入图标-->
            <template #prefix>
              <el-icon>
                <Message/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
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
<!--              这里如果 获得邮箱是无效的话 按钮也失效 disabled失效-->
              <el-button type="success" :disabled="!isEmailValid">获取验证码</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 80px">
      <el-button style="width: 270px" @click="register" type="warning">立即注册</el-button>
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