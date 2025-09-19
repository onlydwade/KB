<template>
  <work-report :class="{ isPC: isDingTalkEnv() === false }" v-if="report.id" :report="report" />
</template>

<script setup>
import { useRoute } from 'vue-router';
import api from '@/api/index'
import WorkReport from '@/components/workReport.vue'
import { showLoadingToast, closeToast } from 'vant'
import { isDingTalkEnv } from '@/utils/dingtalk-jsapi'

const route = useRoute()
const id = ref(route.query.id)
const read = ref(route.query.read)

const report = ref({})



onMounted(() => {

  showLoadingToast({
  message: '正在加载...',
  forbidClick: true,
  duration: 0
});

if (route.query.access_token) {
    sessionStorage.setItem('backPath', route.fullPath.replace('&access_token=' + route.query.access_token, ''))
    window.location.href = GLOBAL_PATH.tokenAuth + route.query.access_token;
    return;
  }
  
  api.workBrief.workBriefViewRead(id.value, read.value).then(res => {
    if (res.code === 200) {
      report.value = res.data
    }
    closeToast(true)
  })
})
</script>

<style lang="less" scoped>
.isPC {
  width: 500px;
  margin: 0rem auto;
}
</style>
