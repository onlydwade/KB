
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
            <a-col :xxl="6" :lg="8" :sm="12">
                <a-form-item label="企业名称" name="companyName">
                    <a-input disabled :value="companyName"/>
                </a-form-item>
            </a-col>
            <a-col :xxl="6" :lg="8" :sm="12">
                <a-form-item label="申请内容">
                    <a-input disabled :value="formData.approvalContent"/>
                </a-form-item>
            </a-col>
            <a-col :xxl="6" :lg="8" :sm="12">
                <a-form-item required label="发起人" name="approvalUserId">
                    <UserSelect v-model="formData.approvalUserId" :users="formData.approvalUser" :disabled="disabled"/>
                </a-form-item>
            </a-col>
            <a-col :xxl="6" :lg="8" :sm="12">
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
            <a-col :xxl="6" :lg="8" :sm="12">
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
        <CompanyDocuments  :key="companyKey" v-model="formData.fileOk" ref="documentRef" :recordId="-1" :companyId="companyId" :menuId="menuInfo.id" :readOnly="disabled"/>
    </a-form>
</div>
<FooterBarL>
    <CompanyOaBtn
        ref="odBtnRef"
        mode="COMPANY_PROJECT_STOP"
        placement="topLeft"
        v-if="type=='ADD' || formData.id"
        :companyId="companyId" :recordId="formData.id"
        action="signOut"
        @submit="startOa">
    </CompanyOaBtn>
    <a-button :loading="bool" size="large" @click="handleOk" v-permissionInvestment="['biz:projectCompany:edit']">暂存</a-button>
</FooterBarL>
</template>
<script setup>
import api           from '@/api/index';
import { message }   from 'ant-design-vue';
import { mainStore } from '@/store';
import {nextTick} from "vue";
const store = mainStore();
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

const formRef     = ref(null);
const formData    = ref({
  fileOk:''
});
const companyKey = ref(0)
const bool = ref(false)
const documentRef = ref(null);
const odBtnRef    = ref(null);
const companyName = inject('getAutoParams')('name');
const startOa     = (oaType)=>{
    handleOk('OA',oaType)
}
const handleOk = (action,oaType)=>{
    bool.value = true
    formRef.value.validateFields().then(vRes=>{
        let apiKey = type.value=='ADD'?'correlationAdd':'correlationEdit';

        formData.value.companyId = props.companyId;
        formData.value.companyName = companyName
        api.investment[apiKey](formData.value,'companyExit').then(async res=>{
            if(res.code==200){
                if(type.value=='ADD'){
                    // let uploadResult = await documentRef.value.batchUpLoad(res.data.id);
                }
                if(action=='OA'){
                    let subject = companyName.value + '-项目退出审批';
                    odBtnRef.value.oaSubmit(oaType,subject,formData.value.companyId,formData.value.id || res.data.id,()=>{
                        getInfo();
                        message.success('操作成功');
                    })
                }else{
                    getInfo();
                    message.success('操作成功');
                }
            }
            bool.value = false
        })
    }).catch(err=>{
        bool.value = false
        message.warning('请完善必填信息！');
    })
}



const type        = ref('EDIT');
const disabled    = computed(()=>false);
const getInfo     = ()=>{
    let postData = {
        pageNo   : 1,
        pageSize : 1,
        params   : {
            companyId : props.companyId
        }
    }
    companyKey.value++
    api.investment.correlationPage(postData,'companyExit').then(res=>{
        if(res.code==200&&res.data&&res.data.records.length>0){
            type.value     = 'EDIT';
            formData.value =Object.assign(res.data.records[0], formData.value);
        }else{
            type.value                    = 'ADD';
            formData.value.approvalUserId = store.userInfo.userId;
            formData.value.approvalUser   = {
                userId   : store.userInfo.userId,
                realname : store.userInfo.realname
            }
            formData.value.approvalContent = '项目退出'
        }
    })
}
onMounted(() => {
    getInfo();
})
</script>
<style scoped lang="less">

</style>
