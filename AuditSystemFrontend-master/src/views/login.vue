<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">
                <img class="logo" src="/images/logo_f.png" alt="">
                投拓管理平台
            </div>
            <a-form v-if="loginType=='normal'" :model="formData" :rules="formRules" size="large" label-width="0px" class="ms-content" @finish="submitForm">
                <a-form-item name="username">
                    <a-input v-model:value.trim="formData.username" size="large" placeholder="用户名"/>
                </a-form-item>
                <a-form-item name="password">
                    <a-input type="password"  v-model:value.trim="formData.password" size="large" placeholder="密码"/>
                </a-form-item>
                <div class="login-btn">
                    <a-button type="primary" html-type="submit" size="large">登录</a-button>
                </div>
                <!-- <p class="login-tips">400-88888888</p> -->
            </a-form>
            <a-result v-if="!loginType" status="403" title="登录通道暂时关闭" sub-title="请先从OA系统登录后，从OA系统跳转投拓平台！">
            </a-result> 
        </div>
    </div>
</template>

<script setup>
    import api                           from '@/api/index';
    import { mainStore }                 from '@/store';
    import { createRouter,removeRouter } from '@/utils/tools';    
    import md5 from "js-md5";
    const { proxy } = getCurrentInstance();
    const store     = mainStore();
    const router    = useRouter();
    const route     = useRoute();
    const formData  = reactive({
        username : "",
        password : "",
    });
    const formRules = {
        username : [{required:true,message: "请输入用户名",trigger: "blur"}],
        password : [{ required:true,message: "请输入密码", trigger: "blur"}],
    };
    const submitForm = () => {
        store.spinChange(1);
        let postData = {
            username : formData.username,
            password : md5(formData.password),
        }
        api.common.login(postData).then(async (res)=>{
            if(res.code==200){
                store.setToken(res.data.access_token);
                getMenu();
            }else{
                store.spinChange(-1);
            }
        }).catch(err=>{
            store.spinChange(-1);
        })
    };
    const getMenu = async ()=>{
        let res = await api.sys.menuGetByAccess();
        if(res.code==200){
            store.setMenuRouters(res.data);
            createRouter(router,res.data);
            
            let backPath = sessionStorage.getItem('backPath') || '/index';
            router.push(backPath);
            
            sessionStorage.removeItem('backPath'); 
        }
        store.spinChange(-1);
    }
    const loginType = ref('');
    onMounted(() => {
        removeRouter(router);
        let urlToken      = route.query.token;
        let ssoToken      = route.query.ssoToken;
        let type          = route.query.type;
        let error_message = route.query.error_message;
        let access_token  = route.query.access_token;
        
        if(error_message){
            router.push('/error?message='+error_message);
        }
        else if(access_token){
            loginType.value = 'byAccessToken';
            sessionStorage.setItem('backPath','/index');
            window.location.href = GLOBAL_PATH.tokenAuth+route.query.access_token;
            return;
        }else if(urlToken){
		loginType.value = 'byToken';
            store.spinChange(1);
            store.setToken(urlToken,ssoToken);
            getMenu();
        }else if(type=='account' || !GLOBAL_PATH.auth){
            loginType.value = 'normal';
            //暂时啥都不干
        }else{
            //window.location.href = GLOBAL_PATH.auth;
        }
    })
</script>

<style scoped lang="less">
.login-wrap {
    position        : relative;
    width           : 100%;
    height          : 100%;
    background      : #f0f2f5 url(@/assets/images/login_bg.svg) no-repeat 50%;
    background-size : 100%;
}

.ms-login {
    position      : absolute;
    left          : 50%;
    top           : 50%;
    width         : 440px;
    border-radius : 8px;
    padding       : 16px;
    background    : rgba(0, 0, 0, 0);
    overflow      : hidden;
    transform     : translate(-50%,-50%);
}

.ms-title {
    text-align      : center;
    font-size       : 20px;
    color           : @text-color;
    display         : flex;
    align-items     : center;
    justify-content : center;
    margin-bottom   : 16px;
    .logo{
        height       : 60px;
        margin-right : 16px;
    }
}
.ms-content {
    padding : 16px;
}
.login-btn{
    display    : flex;
    margin-top : 16px;
    button{
        display : block;
        flex    : 1;
    }
}
.login-tips {
    margin-top  : 16px;
    font-size   : 12px;
    color       : @text-color-secondary;
    font-weight : 300;
    text-align  : center;
}
</style>