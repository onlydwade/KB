<template>
    <div class="page-mag">
        <div class="title color-text">
            {{msgInfo.title || '标题'}}
        </div>
        <p class="time color-info">
            {{msgInfo.createTime || '时间'}}
        </p>
        <div class="info color-info">
          请进投拓系统查看详情
<!--            {{msgInfo.message || '内容'}}-->
        </div>
    </div>
</template>
<script setup>
import api from '@/api/index';
const router  = useRouter();
const route   = useRoute();
const msgId   = ref(Number(route.query.id || 0))
const msgInfo = ref({});
const getInfo = ()=>{
    api.sys.messageInfo(msgId.value).then(res=>{
        if(res.code==200){
            msgInfo.value = res.data || {};
        }
    })
}
onMounted(() => {
    if(msgId.value){
        getInfo();
    }
})
</script>
<style scoped lang="less">
.page-mag{
    padding          : 16px;
    height           : 100%;
    overflow-y       : auto;
    background-color : #fff;
    box-sizing       : border-box;
    .title{
        font-size : 16px;
    }
    .time{
        border-bottom  : 1px solid #ddd;
        font-size      : 12px;
        margin-bottom  : 8px;
        padding-bottom : 8px;
    }
    .info{
        font-size : 14px;
    }
}
</style>
