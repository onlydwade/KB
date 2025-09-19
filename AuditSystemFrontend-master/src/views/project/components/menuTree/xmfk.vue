
<template>
    <div class="menu_inner">
        <a-form
        ref="formRef"
        layout="vertical"
        :model="formData">
            <div class="padding_box">
                <Title>
                    <template #title>
                        公司实缴注册资本
                        <span class="color-danger"> * </span>
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="首次出资金额（元）" name="firstCapitalAmount">
                            <a-input-number :disabled="disabled" v-model:value="formData.firstCapitalAmount" :min="0" class="w_full"
                            :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                            :parser="value => value.replace(/\\s?|(,*)/g, '')"
                            :addon-after="amountUnit(formData.firstCapitalAmount)"/>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="首次出资时间" name="firstCapitalTime">
                            <a-date-picker :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.firstCapitalTime"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-form-item label="情况说明" name="conditionExplain">
                    <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.conditionExplain" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                </a-form-item>
                <Title title="文件上传">
                    <span class="color-danger"> * </span>
                    <a-form-item name="fileOk" 
                    :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                    </a-form-item>
                </Title>
                <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval" v-if="formData.id" :menuId="menuId" :projectId="projectId" v-model="formData.fileOk" ref="documentsRef" :readOnly="disabled" :stepLink="{1031:{stepMenuId:23,id:1025},1032:{stepMenuId:23,id:1026}}"/>
            </div>
        </a-form>
        
        <ProjectActions @submit="submit" :menuInfo="menuInfo" offLineTitle="NC审批,标记完成">
            <a-button size="large" @click="save" v-if="(menuInfo.status==0&&[0,5,9].includes(menuInfo.approvalStatus))||!disabled">暂存</a-button>
            <a-button size="large" @click="submit(3)" v-if="menuInfo.status==0&&[0,9].includes(menuInfo.approvalStatus)&&!disabled">跳过此环节</a-button>
        </ProjectActions>
    </div>
</template>
<script setup>
import BidedCompanys   from '../correlation/BidedCompanys.vue'
import { amountUnit }  from '@/utils/tools';
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

const formAttrs = ['id','firstCapitalAmount','firstCapitalTime','conditionExplain'];
const {
    formRef,
    documentsRef,
    formData,
    getInfo,
    submit,
    save,
    disabled
} = useMenuTree(props.projectId,toRefs(props).menuInfo,formAttrs,true);
onMounted(() => {
    getInfo();
})
</script>
<style scoped lang="less">
</style>
