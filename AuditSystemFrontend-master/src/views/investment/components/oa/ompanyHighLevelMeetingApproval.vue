
<template>
    <div class="menu_inner">
        <a-form layout="vertical" :model="formData" ref="formRef">
            <Title class="titlebar">
                <template #title>
                    信息填写
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-row :gutter="24">
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item label="审批发起人" name="approvalUserId">
                        <UserSelect v-model="formData.approvalUserId" :users="formData.approvalUser" disabled/>
                    </a-form-item>
                </a-col>
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item label="审批发起日期">
                        <a-input disabled allowClear :value="formData.createTime || nowDate"/>
                    </a-form-item>
                </a-col>
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item label="企业名称" name="companyName" v-if="companyId">
                        <a-input disabled allowClear :value="formData.companyName"/>
                    </a-form-item>
                    <a-form-item required label="企业名称" name="companyId" v-else>
                        <DataSelect
                            :disabled="type!='ADD'"
                            v-model="formData.companyId"
                            modeName="company"
                            :options="{
                                key   : 'id',
                                label : 'name'
                            }"
                            :concats="{
                                name:formData.companyName,
                                id:formData.companyId
                            }"/>
                    </a-form-item>
                </a-col>
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item required label="会议标题" name="title">
                        <a-input :disabled="disabled" allowClear v-model:value="formData.title"/>
                    </a-form-item>
                </a-col>
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item required label="会议类型" name="type">
                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.type"
                            class="w_full"
                            placeholder="请选择"
                            :options="dict.options('SAN_HUI_HUI_YI_LEI_XING')">
                        </a-select>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-form-item label="内容描述" name="content">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.content" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            <Title class="titlebar" title="文件上传">
                <a-form-item name="fileOk" 
                :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                </a-form-item>
            </Title>
            <CompanyDocuments v-model="formData.fileOk" ref="documentRef" :companyId="companyId" :menuId="53" :recordId="formData.id" :readOnly="disabled"/>
        </a-form>
    </div>
    <FooterBarL>
        <CompanyOaBtn
            ref="odBtnRef"
            mode="COMPANY_SAN_HUI_SHEN_PI"
            placement="topLeft"
            v-if="formData.id"
            :companyId="companyId" :recordId="formData.id"
            @submit="startOa">
        </CompanyOaBtn>
        <a-button size="large" @click="handleOk">暂存</a-button>
    </FooterBarL>
</template>
<script setup>
import api              from '@/api/index';
import moment           from 'moment'
import { message }      from 'ant-design-vue';
import { mainStore }    from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict  = useDictStore();
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
        api.investment.correlationEdit(formData.value,'ompanyHighLevelMeetingApproval').then(async res=>{
            if(res.code==200){
                if(action=='OA'){
                    let subject = formData.value.companyName +'-'+ formData.value.title +'-三会审批';
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
const companyName = inject('getAutoParams')('companyName');
const getInfo     = ()=>{
    api.investment.correlationGet(props.recordId,'ompanyHighLevelMeetingApproval').then(res=>{
        if(res.code==200){
            formData.value = res.data || {};
        }
    })
}
onMounted(() => {
    getInfo();
})

const nowDate = ref(moment(new Date()).format('YYYY-MM-DD'));
</script>
<style scoped lang="less">

</style>
