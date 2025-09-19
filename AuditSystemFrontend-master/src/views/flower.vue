<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">
                <img class="logo" src="/images/logo_f.png" alt="">
                投拓管理平台
            </div>
            <a-form  :model="formData" :rules="formRules" size="large" label-width="0px" class="ms-content" @finish="submitForm">
                <a-form-item name="username">
                    <a-input v-model:value.trim="formData.username" size="large" placeholder="用户名/手机号"/>
                </a-form-item>
                <a-form-item name="password">
                    <a-input type="password" v-model:value.trim="formData.password" size="large" placeholder="密码"/>
                </a-form-item>
                <a-form-item name="code">
                    <div class="flex_box">
                        <a-input v-model:value.trim="formData.code" size="large" placeholder="验证码" style="flex:1;"/>
                        <div style="width:100px;margin-left:8px">
                            <img :src="codeUrl" alt="" style="height:40px;width:100px;cursor: pointer;" v-if="codeVisible" @click="refreshCode">
                        </div>
                    </div>
                </a-form-item>
                <div class="login-btn">
                    <a-button type="primary" html-type="submit" size="large">登录</a-button>
                </div>
                <!-- <p class="login-tips">400-88888888</p> -->
            </a-form>
        </div>
    </div>
</template>

<script setup>
    import api           from '@/api/index';
    import { encryption} from '@/utils/aes.js'
    const { proxy } = getCurrentInstance();
    const formData  = reactive({
        username : "",
        password : "",
        code     : ""
    });
    const formRules = {
        username : [{required:true,message: "请输入用户名",trigger: "blur"}],
        password : [{ required:true,message: "请输入密码", trigger: "blur"}],
        code     : [{ required:true,message: "请输入验证码", trigger: "blur"}],
    };
    const time = ref((new Date()).getTime());
    const submitForm = () => {
        const user = encryption({
            data  : {
                username : formData.username,
                password : formData.password,
            },
            key   : 'dadich,roc2cloud',
            param : ['password']
        })
        let params = {
            code       : formData.code,
            randomStr  : randomStr.value,
            grant_type : 'password',
            scope      : 'server'
        }
        let postData = {
            username   : user.username,
            password   : user.password,
        }
        api.common.ssoLogin(postData,params,"Basic "+window.btoa('web:web')).then(async (res)=>{
            if(res.access_token){
                sessionStorage.setItem('backPath','/index');
                window.location.href = GLOBAL_PATH.tokenAuth+res.access_token;
            }else{
                refreshCode();
            }
        }).catch(err=>{
            refreshCode();
        })
    };

    const randomStr = ref('');
    const codeUrl = computed(()=>{
        return GLOBAL_PATH.ssoUrl+'/bsh-unify-api/code?randomStr='+randomStr.value+'&time='+time.value;
    })
    const codeVisible = ref(false);
    const refreshCode = ()=>{
        time.value = (new Date()).getTime();
        codeVisible.value = false;
        setTimeout(()=>{
            codeVisible.value = true;
        },300)
    }
    onMounted(() => {
        let randomLocal = sessionStorage.getItem('randomStr') || '';
        if(!randomLocal){
            randomLocal = generateRandomNumber();
            sessionStorage.setItem('randomStr' , randomLocal);
        }
        randomStr.value = randomLocal;
        setTimeout(()=>{
            codeVisible.value = true;
        },100)
    })
    
    const generateRandomNumber = ()=>{
      const array = new Uint8Array(16)
      crypto.getRandomValues(array)
      let hex = ''
      for (let i = 0; i < array.length; i++) {
        hex += array[i].toString(16).padStart(2, '0')
      }
      return hex
    }
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