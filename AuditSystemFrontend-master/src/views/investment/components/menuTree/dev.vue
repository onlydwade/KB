
<template>
    <div class="menu_inner">
        <a-result status="403" title="开发中" sub-title="敬请期待..."></a-result>
    </div>
</template>
<script setup>
const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    }
})
</script>
<style scoped lang="less">

</style>
