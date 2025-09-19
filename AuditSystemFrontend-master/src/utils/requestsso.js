import axios       from 'axios';
import { message } from 'ant-design-vue';
const service = axios.create({
    timeout : 12000000,
    transformRequest: [
        function(data) {
            let ret = '';
            for (let it in data) {
                ret += (ret?'&':'')+encodeURIComponent(it) + '=' + encodeURIComponent(data[it]);
            }
            return ret;
        }
    ],
    headers : {
        'Content-Type': 'application/x-www-form-urlencoded'
    }
});
service.interceptors.request.use(
    config => {
        if(config.authorization){
            config.headers['Authorization']=config.authorization;
        }
        return config;
    },  
    error => {
        console.log('error1',error)
        message.error('登录错误，请检查登录信息后重新登录！！');
        return Promise.reject();
    }
);
service.interceptors.response.use(
    res => {
        console.log(res)
        if(res.status==200){
            return Promise.resolve(res.data);
        }else{
            message.destroy()
            message.error('登录错误，请检查登录信息后重新登录！！');
            return Promise.reject(res.data)
        }
    },
    error => {
        console.log('error',error)
        message.destroy()
        message.error('登录错误，请检查登录信息后重新登录！！');
        return Promise.reject();
    }
);
export default service;
