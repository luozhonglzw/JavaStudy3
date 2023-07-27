<script setup>
import {reactive, ref} from "vue";
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";

//设定在重置的什么位置
const active=ref(0)
//设置两个表单内容
const form = reactive({
  email:'',
  code:'',
  password:'',
  password_repeat: '',
})
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
const rules={
  email:[
    { required: true, message: '请输入邮件地址', trigger: 'blur' },
    {
      type: 'email', message: '请输入合法的邮箱地址', trigger: ['blur', 'change'],
    }
  ],
  //这里设定验证码的输入框
  code:[
    { required: true, message: '请输入验证码', trigger: 'blur' },
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
}
//设定一个冷却的默认值
const coldTime =ref(0)
//有个方法验证之前的表单是否通过 判断邮箱是否有效 一开始默认无效 这里的ref有多个选择
const isEmailValid =  ref(false)
// 这里要设置值 但是不用给
const formRef =ref()
const validateEmail =()=>{
  //这里只有三个值 url,data,success
  //import {post} from "@/net"; 这里记得返回自己的post 这里写了 axios 就可以与后端交互
  post('/api/auth/valid-reset-email',{
    email: form.email
  },(message)=>{
    // 发送信息 弹窗型 这个
    ElMessage.success(message)
    // 这里设置冷却的时间 每次点击后都设置冷却的时间都为60
    coldTime.value=60
    setInterval(()=>coldTime.value--,1000)//每秒钟设置定时器 每次都减1
  })
}
const onValidate =(prop,isValid)=>{
  if(prop==='email'){
    isEmailValid.value=isValid
  }
}
//这里必须要填写完整个表单
const startRest =()=>{
  formRef.value.validate((isValid)=>{
    if(isValid){
      post('/api/auth/start-rest',{
        email:form.email,
        code:form.code
      },()=>{
        active.value++
      })
    }else {
      ElMessage.warning("请完整填写上面信息")
    }
  })

}
const doRset=()=>{
  formRef.value.validate((isValid)=>{
    if(isValid){
      post('/api/auth/do-reset',{
        password:form.password,
      },(message)=>{
        ElMessage.success(message)
        router.push('/')
      })
    }else {
      ElMessage.warning("请填写新的密码")
    }
  })
}
</script>
<!-- 进度条 居中 0开始 默认0开始-->
<template>
  <div style="margin: 30px 20px">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="验证电子邮件" />
      <el-step title="重置设定密码" />
    </el-steps>
  </div>
  <div style="text-align: center;margin: 0 20px" v-if="active===0">
    <div style="margin-top: 100px">
      <div style="font-size: 25px;font-weight: bold">重置密码</div>
      <div style="font-size: 14px;color: grey">请填写需要更改密码的邮箱地址</div>
    </div>
    <div style="margin-top: 50px">
      <!--      这里用el自带的表单 可以绑定前端的页面 如果不符合的话自动报错 绑定form表单 加入rules规则 自己写 这里要绑定事件 这里要拿到它的函数应用-->
        <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
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
                <el-input v-model="form.code" :maxlength="6" type="text" placeholder="请输入验证码">
                  <template #prefix>
                    <el-icon>
                      <EditPen/>
                    </el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="5">
                <!--              这里如果 获得邮箱是无效的话 按钮也失效 disabled失效 这里要改内容-->
                <el-button type="success" @click="validateEmail"
                           :disabled="!isEmailValid ||coldTime>0">
                  {{coldTime>0?'请稍后 '+coldTime+' 秒':'获得验证码'}}</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
    <div style="margin-top: 70px;">
<!--      点击 进入注册的第二个页面-->
      <el-button @click="startRest()" style="width: 270px;" type="danger" plain>开始重置密码</el-button>
    </div>
  </div>
  <div style="text-align: center;margin: 0 20px" v-if="active===1">
    <div style="margin-top: 100px">
      <div style="font-size: 25px;font-weight: bold">重置密码</div>
      <div style="font-size: 14px;color: grey">请填写新密码</div>
    </div>
    <div style="margin-top: 50px">
      <!--      这里用el自带的表单 可以绑定前端的页面 如果不符合的话自动报错 绑定form表单 加入rules规则 自己写 这里要绑定事件 这里要拿到它的函数应用-->
      <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
        <el-form-item prop="password">
          <el-input v-model="form.password" :maxlength="16" type="password" placeholder="新密码" >
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" :maxlength="16" type="password" placeholder="重复新密码" >
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>

        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 70px;">
      <!--      点击 进入注册的第二个页面-->
      <el-button @click="doRset()" style="width: 270px;" type="danger" plain>立即重置密码</el-button>
    </div>
  </div>
</template>

<style scoped>

</style>