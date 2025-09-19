
<template>
    <div class="menu_inner padding_box">
        <FollowList :recordId="projectId" moduleName="Project" :menuId="menuId" :readOnly="disabled"/>
    </div>
</template>
<script setup>
import api from '@/api/index';
import { useMenuTree }  from './menu';
const props = defineProps({
    projectId: {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    }
})
const {
    disabled
} = useMenuTree(props.projectId,toRefs(props).menuInfo);

onMounted(() => {
    
})
</script>
<style scoped lang="less">
</style>
