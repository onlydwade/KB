
<template>
    <div class="menu_inner">
        <a-form
        ref="formRef"
        layout="vertical"
        :model="formData">
            <Title>
                <template #title>
                    信息填写
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-row :gutter="24">
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item label="企业名称" name="companyName">
                        <a-input disabled :value="formData.companyName"/>
                    </a-form-item>
                </a-col>
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item label="申请内容">
                        <a-input disabled :value="formData.approvalContent"/>
                    </a-form-item>
                </a-col>
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item required label="发起人" name="approvalUserId">
                        <UserSelect v-model="formData.approvalUserId" :users="formData.approvalUser" :disabled="disabled"/>
                    </a-form-item>
                </a-col>
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item required label="发起日期" name="approvalSendTime">
                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.approvalSendTime"
                            class="w_full"
                            valueFormat="YYYY-MM-DD 00:00:00"
                            format="YYYY-MM-DD"
                            placeholder="请选择" />
                    </a-form-item>
                </a-col>
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item required label="项目退出日期" name="exitTime">
                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.exitTime"
                            class="w_full"
                            valueFormat="YYYY-MM-DD 00:00:00"
                            format="YYYY-MM-DD"
                            placeholder="请选择" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-form-item required label="退出描述" name="content">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.content" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            
            <Title class="titlebar" title="文件上传">
                <a-form-item name="fileOk" 
                :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                </a-form-item>
            </Title>
            <CompanyDocuments v-model="formData.fileOk" ref="documentRef" :recordId="-1" :companyId="companyId" :menuId="50" :readOnly="disabled"/>
        </a-form>
    </div>
    <FooterBarL>
        <CompanyOaBtn
            ref="odBtnRef"
            mode="COMPANY_PROJECT_STOP"
            placement="topLeft"
            v-if="formData.id"
            :companyId="companyId" :recordId="formData.id"
            @submit="startOa">
        </CompanyOaBtn>
        <a-button size="large" @click="handleOk">暂存</a-button>
    </FooterBarL>
</template>
<script setup>
import api           from '@/api/index';
import { message }   from 'ant-design-vue';
import { mainStore } from '@/store';
const store = mainStore();
const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    recordId : {
        type    : Number,
        default : 0,
    },
    mode:{
        type    : String,
        default : ''
    }
})

const formRef     = ref(null);
const formData    = ref({});
const documentRef = ref(null);
const odBtnRef    = ref(null);
const startOa     = (oaType)=>{
    handleOk('OA',oaType)
}
const handleOk = (action,oaType)=>{
    formRef.value.validateFields().then(vRes=>{
        api.investment.correlationEdit(formData.value,'companyExit').then(async res=>{
            if(res.code==200){
                if(action=='OA'){
                    let subject = formData.value.companyName + '-项目退出审批';
                    odBtnRef.value.oaSubmit(oaType,subject,formData.value.companyId,formData.value.id || res.data.id,()=>{
                        getInfo();
                        message.success('操作成功');
                    })
                }else{
                    getInfo();
                    message.success('操作成功');
                }
            }
        })
    }).catch(err=>{
        message.warning('请完善必填信息！');
    })
}

const disabled    = computed(()=>false);
const getInfo     = ()=>{
    api.investment.correlationGet(props.recordId,'companyExit').then(res=>{
        if(res.code==200){
            formData.value = res.data || {};
        }
    })
}
onMounted(() => {
    getInfo();
})
</script>
<style scoped lang="less">

</style>
