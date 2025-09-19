
<template>
    <div class="menu_inner">
        <OperLog :recordId="projectId" moduleName="Project"/>
    </div>
</template>
<script setup>
const props = defineProps({
    projectId: {
        type    : Number,
        default : 0,
    }
})
</script>
<style scoped lang="less">
</style>
