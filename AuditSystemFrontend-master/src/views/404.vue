<template>
    <a-result status="404" title="404" sub-title="抱歉，您访问的页面不存在">
            <template #extra>
                <a-button type="primary" href="#/home">回到主页</a-button>
            </template>
      </a-result>
</template>
<script setup>
import api           from '@/api/index';
import { mainStore } from '@/store';
const store = mainStore();
onMounted(() => {
    // api.common.getUserInfo().then(res=>{
    // 
    // })
})

</script>
<style scoped lang="less">
.page-dashboard{
    display        : flex;
    flex-direction : column;
    .content-box{
        flex : 1;
        &:not(&:first-child){
            margin-top: 16px;
        }
    }
}
</style>