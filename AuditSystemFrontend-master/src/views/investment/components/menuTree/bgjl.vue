
<template>
    <div class="menu_inner">
        <OperLog :recordId="companyId" moduleName="ProjectCompany"/>
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
