import { createApp }   from 'vue'
import { createPinia } from 'pinia'
import App             from './App.vue'
import router          from './router'
import mitt            from 'mitt'

import '@/assets/font/iconfont.css'
import '@/assets/css/layout.less'
import '@/assets/css/page.less'

//ant  非组件模块 样式引入
import 'ant-design-vue/es/message/style/css'; 
import 'ant-design-vue/es/notification/style/css'; 
import 'ant-design-vue/es/modal/style/css'; 

import '@/components/scrollbar/css/scrollbar.less';
import AScrollbar from '@/components/scrollbar';

import baseMixin       from '@/utils/mixin'  //建议按需引入
import * as directives from '@/utils/directive'

import * as echarts from 'echarts'
import Vant from 'vant'
import 'vant/lib/index.css'
import 'remixicon/fonts/remixicon.css'
const app = createApp(App)

// import VConsole from 'vconsole';

// const vConsole = new VConsole();

app.provide('bus', new mitt())
app.use(Vant)
Object.keys(directives).forEach(key => {
    app.directive(key,directives[key]);
})

app.use(createPinia())
    .use(router)
    .mixin(baseMixin)
    .use(AScrollbar)
    .mount('#app')
   

