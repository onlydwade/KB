
<template>
    <div class="oa_content" :style="{paddingTop:oaList.length>0?'32px':'0'}"> 
        <a-steps progress-dot direction="vertical" :current="-1">
            <a-step v-for="(item,index) in oaList" :key="index">
                <template #title>
                    <div class="step_status">
                        <check-circle-outlined v-if="item.approvalStatus==1" class="color-success"/>
                        <close-circle-outlined v-if="item.approvalStatus==2" class="color-danger"/>
                        <clock-circle-outlined v-if="item.approvalStatus==9" class="color-primary" />
                        {{statusMap[item.approvalStatus] || '未知状态'}}
                    </div>
                </template>
                <template #description>
                    <div class="step_desc">
                        <a-space class="params">
                            {{item.submitTime}}
                            <a-divider type="vertical" />
                            {{item.submitDeptName || ''}} {{item.submitUser.realname || ''}}
                            <a-divider type="vertical" />
                            {{item.approvalNo}}
                        </a-space>
                        <p class="content" v-if="item.remark">
                            提交审批说明：{{item.remark}}
                        </p>
                        <p class="content" v-if="item.approvalResult">
                            审批说明：{{item.approvalResult}}
                        </p>
                    </div>
                </template>
            </a-step>
        </a-steps>
    </div>
</template>
<script setup>
import api from '@/api/index';
const props = defineProps({
    projectId: {
        type    : Number,
        default : 0,
    },
    moduleName:{
        type    : String,
        default : 'LI_XIANG_SHENG_PI',
    },
})
const statusMap = {
    0:'数据尚未提交审核',
    1:'审核通过',
    2:'审核驳回',
    9:'审核中'
}
const oaList  = ref([]);
const getList = ()=>{
    api.project.oaApprovalList(props.moduleName,props.projectId).then(res=>{
        if(res.code==200){
            oaList.value = res.data || [];
        }
    })
}
onMounted(() => {
    getList();
})
</script>
<style scoped lang="less">
.oa_content{
    .step_status{
        color            : @text-color
    }
    .step_desc{
        background-color : #f0f2f5;
        border-radius    : 4px;
        padding          : 16px;
        margin-top       : 8px;
        max-width        : 600px;
        margin-bottom    : 16px;
        .params{
            color: @text-color-secondary;
        }
        .content{
            font-size  : 16px;
            color      : @text-color;
            margin-top : 8px;
        }
        
    }
    :deep(.ant-steps-item-container){
        cursor: auto!important;
    }
}
</style>
