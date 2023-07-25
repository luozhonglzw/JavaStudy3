

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
//这里按需导入 节约项目的内存
import 'element-plus/dist/index.css';
import axios from "axios";
const app = createApp(App)

axios.defaults.baseURL='http://localhost:8080'

app.use(createPinia())
app.use(router)

app.mount('#app')
