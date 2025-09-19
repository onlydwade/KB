<template>
    <div class="page-common">
        <a-result status="403" title="开发中" sub-title="敬请期待...">
            <template #extra>
                <a-button type="primary" href="#/home">回到首页</a-button>
            </template>
        </a-result>
    </div>
</template>
<script setup>
import api from '@/api/index';
onMounted(() => {
})
</script>
<style scoped lang="less">

</style>