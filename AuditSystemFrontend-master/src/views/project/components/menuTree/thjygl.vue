
<template>
    <div class="menu_inner padding_box">
        <Operation v-if="businessTypeStr =='住宅'" :projectId="projectId" :projectName="projectName" :menuId="menuId" :businessTypeStr="businessTypeStr" :companyName="companyName" :menuInfo="menuInfo" type="TUO" v-model="correlation" :showStr="showStr" :readOnly="disabled"/>
        <MisOperation v-else :projectId="projectId" :projectName="projectName" :menuId="menuId" :businessTypeStr="businessTypeStr" :companyName="companyName" :menuInfo="menuInfo" type="TUO" v-model="correlation" :showStr="showStr"  :readOnly="disabled"/>
    </div>
</template>
<script setup>
import Operation from '../correlation/Operation.vue'
import MisOperation from '../correlation/MisOperation.vue'
// import { useMenuTree }  from './menu';
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
// const {
//     disabled
// } = useMenuTree(props.projectId,toRefs(props).menuInfo);
const formData      = ref({});
const route      = useRoute();
const businessTypeStr = ref(route.query.businessTypeStr)
const showStr = ref(route.query.show)
const companyName = ref(route.query.companyName)
const projectName = ref(route.query.projectName)
const roleKeys      = inject('getAutoParams')('roleKeys'); 
const serviceStatus = inject('getAutoParams')('serviceStatus'); 
const expire        = inject('getAutoParams')('expire'); 
const disabled      = computed(()=>{
    return
    // || [1,2,5,8].includes(props.menuInfo.approvalStatus) || ['ZAI_GUAN','YI_ZHONG_ZHI'].includes(serviceStatus.value) || expire.value=='YI_SHI_XIAO'
})

const refreshMenuTree = inject('refreshMenuTree');
const correlation = ref(null);
watch(()=>correlation.value,(val)=>{
    // let status = val=='finished'?1:0;
    if(props.menuInfo.status!=1){
        refreshMenuTree(props.menuId,1);
    }
})
</script>
<style scoped lang="less">
</style>
