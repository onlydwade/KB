<template>
    <a-result status="500" title="登录失败" :sub-title="message">
            <template #extra>
                <a-button type="primary" href="#/flower">重新登录</a-button>
            </template>
      </a-result>
</template>
<script setup>
const router  = useRouter();
const route   = useRoute();
const message = ref(route.query.message || '请重新登录');
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