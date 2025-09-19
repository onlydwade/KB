import axios from 'axios';
import router from '@/router'
import { message } from 'ant-design-vue';
import { mainStore } from '@/store';
import { isDingTalkEnv } from '@/utils/dingtalk-jsapi'

const store = mainStore();
const service = axios.create({
    baseURL: GLOBAL_PATH.api,
    timeout: 12000000,
    headers: {}
});
service.interceptors.request.use(
    config => {
        let token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        // store.spinChange(1);
        config.url = decodeURI(encodeURI(config.url).replace(/%E2%80%8B/g, ""))
        return config;
    },
    error => {
        // store.spinChange(-1);
        message.error('发生未知错误啦！请稍后再试！！');
        return Promise.reject();
    }
);
service.interceptors.response.use(
    res => {
        // store.spinChange(-1);
        if (res.status == 200) {
            if (res.data && res.data.code != 200 && !res.config.filePath) {
                if (res.data.code == 401) {
                    message.destroy()
                    message.error('登录失效，请重新登录！');
                    store.clearToken();
                    sessionStorage.setItem('backPath', window.location.href.replace(window.location.origin + '/#', ''))
                    if (router.currentRoute.value.path.includes('/mobile') && isDingTalkEnv()) {
                        router.push('/ddLogin')
                    } else if (GLOBAL_PATH.logOut) {
                        window.location.href = GLOBAL_PATH.logOut;
                    } else {
                        window.location.href = "#/flower"
                    }
                }  else {
                    message.error(res.data.msg || '发生未知错误啦！请稍后再试！！');
                }
            }
            return Promise.resolve(res.data);
        } else {
            message.destroy()
            message.error('发生未知错误啦！请稍后再试！！');
            return Promise.reject(res.data)
        }
    },
    error => {
        // store.spinChange(-1);
        message.destroy()
        message.error('发生未知错误啦！请稍后再试！！');
        return Promise.reject();
    }
);

export default service;
