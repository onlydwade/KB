
<template>
    <div class="menu_inner">
        <div class="padding_box">
            <a-alert message="温馨提示：请跟踪投标费用的追回情况并将最新情况更新录入系统中" closable type="warning" />
            <Title>
                <template #title>
                    投标费用登记
                </template>
            </Title>
            <BidFeeReg :projectId="projectId" :readOnly="disabledM"/>
            
            <Title title="附件上传" style="margin-top:36px;"></Title>
            <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval" :menuId="menuId" :projectId="projectId" ref="documentsRef" :readOnly="disabled"/>
        </div>
        
        <ProjectActions @submit="submit" :menuInfo="menuInfo">
            <a-button size="large" @click="submit(2)" v-if="menuInfo.oaApproval!=1&&menuInfo.status==0&&[0,9].includes(menuInfo.approvalStatus)&&!disabled">标记完成</a-button>
        </ProjectActions>
    </div>
</template>
<script setup>
import BidFeeReg       from '../correlation/BidFeeReg.vue'
import { useMenuTree } from './menu';
const props = defineProps({
    projectId: {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 0,
    },
    menuInfo:{
        type    : Object,
        default : {},
    }
})
const {
    documentsRef,
    submit,
    disabled
} = useMenuTree(props.projectId,toRefs(props).menuInfo);

const roleKeys      = inject('getAutoParams')('roleKeys'); 
const serviceStatus = inject('getAutoParams')('serviceStatus'); 
const expire        = inject('getAutoParams')('expire'); 
const disabledM      = computed(()=>{
    return !roleKeys.value.includes(1) || ['ZAI_GUAN','YI_ZHONG_ZHI'].includes(serviceStatus.value) || expire.value=='YI_SHI_XIAO';
})
</script>
<style scoped lang="less">
</style>
