<template>
  <div class="container"></div>
</template>
  
<script setup>
import { useRouter } from 'vue-router'
import { mainStore } from '@/store';
import api from '@/api/index'
import { showLoadingToast, closeToast } from 'vant'
import { requestAuthCode } from '@/utils/dingtalk-jsapi'

const router = useRouter()
const store = mainStore()

showLoadingToast({
  message: '正在登录...',
  forbidClick: true,
  duration: 0
});

api.common.getDingCorpId().then(res => {
  if (res.code === 200) {
    requestAuthCode(res.data).then(res => {
      api.common.ddLogin({ code: res.code }).then(res => {
        if (res.code === 200) {
          store.setToken(res.data.access_token);
          api.common.loginUser().then((res) => {
            if (res.code == 200) {
              store.setUserInfo(res);
              closeToast(true)
              router.replace('/dashboard')
            }
          })
        }
      })
    })
  }
})

</script>
<style scoped lang="less"></style>
