<script setup>

</script>

<template>
  <!--  <div> 这个是主页的样子 分两边左大填满 右小</div>-->
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex: 1;">
      <el-image style="width: 100%;height: 100%" fit="cover"
                src="https://tse4.mm.bing.net/th?id=OIP.fioTTGr0zW16ZEDRLwaJwAHaEo&pid=ImgDet&rs=1"></el-image>
    </div>
<!--    <div></div> 右下角的小字 -->
    <div class="welcome-title">
        <div style="font-weight: bold;font-size: 30px">欢迎来到我们的学习平台</div>
        <div style="margin-top: 10px">在这里你可以学习如何使用Java,如何搭建网络</div>
        <div style="margin-top: 5px">坚持继续学习</div>
    </div>
    <!--    <div> 这里写登录用户名密码输入框 注册 </div>-->
    <div style="width: 400px;background-color: white;">
        <router-view/>
    </div>
  </div>
</template>
<!--< css: 绝对布局 距离左边30px 白色 阴影-->
<style scoped>
.welcome-title{
    position: absolute;
    bottom: 30px;
    left: 30px;
    color: white;
    text-shadow: 0 0 10px 0 black;
}
</style>