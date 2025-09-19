
<template>
    <div class="menu_inner">
        <Title title="关联企业"></Title>
        <LinkCompany :companyId="companyId"/>
    </div>
</template>
<script setup>
import LinkCompany from '../correlation/LinkCompany.vue'
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
