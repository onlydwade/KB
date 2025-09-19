
<template>
    <div class="menu_inner">
        <a-form
        ref="formRef"
        layout="vertical"
        :model="formData">
            <div class="padding_box">
                <Title title="文件上传">
                    <span class="color-danger"> * </span>
                    <a-form-item name="fileOk" 
                    :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                    </a-form-item>
                </Title>
                <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval" :menuId="menuId" :projectId="projectId" v-model="formData.fileOk" @uploadResult="uploadResult" ref="documentsRef" :readOnly="disabled"/>
            </div>
        </a-form>
        
        <ProjectActions @submit="submit" :menuInfo="menuInfo" offLineTitle="已线下审批，上传会议记录标记完成">
            <a-button size="large" @click="submit(2)" v-if="menuInfo.oaApproval!=1&&menuInfo.status==0&&[0,9].includes(menuInfo.approvalStatus)&&!disabled">标记完成</a-button>
        </ProjectActions>
    </div>
</template>
<script setup>
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
    menuInfo : {
        type    : Object,
        default : {},
    }
})
const {
    formRef,
    documentsRef,
    formData,
    submit,
    disabled
} = useMenuTree(props.projectId,toRefs(props).menuInfo);

const uploadResult = (result)=>{
    //是否有开启审批  如果没有 根据结果自动标记
}
</script>
<style scoped lang="less">
</style>
