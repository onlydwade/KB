<template>
    <div class="comp_loadding">
        <a-spin tip="资源加载中..."></a-spin>
    </div>
</template>
<script setup>
import api from '@/api/index';
onMounted(() => {
})
</script>
<style scoped lang="less">
.comp_loadding{
   text-align    : center;
   background    : #fff;
   border-radius : 4px;
   padding       : 48px;
}
</style>