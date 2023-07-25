import axios from "axios";
import {ElMessage} from "element-plus";

//这里前端用来获得后端的数据

const defaultError=()=>ElMessage.error('发生错误')
const defaultFailure=(message)=>ElMessage.warning(message)
function post(url,data,success,failure=defaultFailure,error=defaultError){
    axios.post(url,data,{
        headers:{
            //改成表单
            'Content-Type':'application/x-www-form-urlencoded'
        },
        //发起请求的时候 是否携带cookie
        withCredentials:true
    }).then(({data})=>{
            if(data.success){
                success(data.message,data.status)
            }
            else {
                failure(data.message,data.status)
            }
    }).catch(error)
}
function get(url,success,failure=defaultFailure,error=defaultError){
    axios.get(url,{
        //发起请求的时候 是否携带cookie
        withCredentials:true
    }).then(({data})=>{
            if(data.success){
                success(data.message,data.status)
            }
            else {
                failure(data.message,data.status)
            }
    }).catch(error)
}
//封装好后 暴露数据;
export {get,post}