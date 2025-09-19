
<template>
    <div class="menu_inner">
        <a-tabs v-model:activeKey="handleType" v-if="showHandle">
            <a-tab-pane key="FENG_XIAN_JIE_CHU" tab="风险解除"/>
            <a-tab-pane key="FENG_XIAN_CHU_LI" tab="风险处理"/>
        </a-tabs>
        <a-form layout="vertical" :model="formData" ref="formRef">
            <Title class="titlebar">
                <template #title>
                    信息填写
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-row :gutter="24">
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item label="审批发起人" name="processApprovalUserId">
                        <UserSelect v-model="formData.processApprovalUserId" :users="formData.processApprovalUser" disabled/>
                    </a-form-item>
                </a-col>
                <a-col :lg="8" :sm="12" :xs="24">
                    <a-form-item label="审批发起日期">
                        <a-input disabled allowClear :value="dateFormat(formData.processApprovalSendTime,'YYYY-MM-DD')  ||  dateFormat(nowDate)"/>
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
                <template v-if="handleType=='FENG_XIAN_CHU_LI'">
                    <a-col :lg="8" :sm="12" :xs="24">
                        <a-form-item required label="风险节点名称" name="name">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.name"/>
                        </a-form-item>
                    </a-col>
                    <a-col :lg="8" :sm="12" :xs="24">
                        <a-form-item required label="风险提出人" name="presenter">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.presenter"/>
                        </a-form-item>
                    </a-col>
                    <a-col :lg="8" :sm="12" :xs="24">
                        <a-form-item required label="风险类型" name="type" >
                            <a-select
                                :disabled="disabled"
                                v-model:value="formData.type"
                                class="w_full"
                                placeholder="请选择"
                                :options="dict.options('FENG_XIAN_LEI_XING')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                </template>
            </a-row>
            
            <a-form-item required label="风险解除描述" name="schemeContent" v-if="handleType=='FENG_XIAN_JIE_CHU'">
                <a-textarea :disabled="disabled2" allowClear :rows="3" type="textarea" v-model:value="formData.schemeContent" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            <a-form-item label="风险描述" name="content" v-if="handleType=='FENG_XIAN_CHU_LI'">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.content" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            <a-form-item label="风险解决方案" name="scheme" v-if="handleType=='FENG_XIAN_CHU_LI'">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.scheme" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            
            <template v-if="companyIdEnd">
                <Title class="titlebar" title="附件上传">
                    <a-form-item name="fileOk" 
                    :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                    </a-form-item>
                </Title>
                <CompanyDocuments v-model="formData.fileOk" v-if="handleType=='FENG_XIAN_JIE_CHU'" type="JIE_CHU" ref="documentRef" :companyId="companyIdEnd" :menuId="46" :recordId="formData.id" :readOnly="disabled2"/>
                <CompanyDocuments v-model="formData.fileOk" v-if="handleType=='FENG_XIAN_CHU_LI'" type="CHU_LI" ref="documentRef" :companyId="companyIdEnd" :menuId="46" :recordId="formData.id" :readOnly="disabled"/>
            </template>
            <a-alert :show-icon="false" banner v-else>
                <template #message>
                    选择企业后完善更多信息！！！
                </template>
            </a-alert>
        </a-form>
    </div>
    <FooterBarL>
        <CompanyOaBtn
            v-if="handleType=='FENG_XIAN_CHU_LI'"
            ref="odBtnRef"
            mode="COMPANY_FX_CHU_LI"
            placement="topLeft"
            tempName="风险处理OA审批"
            :companyId="companyIdEnd" :recordId="formData.id" action="Process"
            @submit="startOa">
        </CompanyOaBtn>
        <CompanyOaBtn
            v-if="handleType=='FENG_XIAN_JIE_CHU'"
            ref="odBtnRef"
            mode="COMPANY_FX_JIE_CHU"
            placement="topLeft"
            tempName="风险解除OA审批"
            :companyId="companyIdEnd" :recordId="formData.id" action="Relieve"
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

const companyIdEnd = computed(()=>{
    return props.companyId || formData.value.companyId;
})
const disabled    = computed(()=>[1,2,5].includes(formData.value.processApprovalStatus) || props.mode=='COMPANY_FX_JIE_CHU');
const disabled2   = computed(()=>[1,2,5].includes(formData.value.relieveApprovalStatus) || props.mode=='COMPANY_FX_CHU_LI');
const title       = ref('');
const formData    = ref({});
const formRef     = ref(null);
const documentRef = ref(null);
const odBtnRef    = ref(null);
const startOa     = (oaType)=>{
    handleOk('OA',oaType)
}
const handleOk = (action,oaType)=>{
    formRef.value.validateFields().then(vRes=>{
        let postData             = JSON.parse(JSON.stringify(formData.value));
        if(handleType.value=='FENG_XIAN_JIE_CHU'){
            delete postData.processApprovalStatus;
            postData.relieveApprovalStatus = 0;
        }
        if(handleType.value=='FENG_XIAN_CHU_LI'){
            delete postData.relieveApprovalStatus;
        }
        api.investment.correlationEdit(postData,'companyrisk').then(async res=>{
            if(res.code==200){
                if(action=='OA'){
                    let subject = formData.value.companyName +'-'+ formData.value.name +'-'+ (handleType.value=='FENG_XIAN_JIE_CHU'?'风险解除审批':'风险处理审批');
                    odBtnRef.value.oaSubmit(oaType,subject,formData.value.companyId,formData.value.id || res.data.id,()=>{
                        message.success('操作成功');
                        getInfo();
                    })
                }else{
                    message.success('操作成功');
                    getInfo();
                }
            }
        })
    }).catch(err=>{
        message.warning('请完善必填信息！');
    })
}

const handleType = ref('FENG_XIAN_JIE_CHU');
const showHandle = computed(()=>{
    return formData.value.relieveApprovalStatus || formData.value.relieveApprovalStatus==0  || props.mode=='COMPANY_FX_JIE_CHU';
})

const companyName = inject('getAutoParams')('companyName');
const getInfo     = ()=>{
    api.investment.correlationGet(props.recordId,'companyrisk').then(res=>{
        if(res.code==200){
            formData.value = res.data || {};
        }
    })
}
onMounted(() => {
    handleType.value = props.mode=='COMPANY_FX_CHU_LI'?'FENG_XIAN_CHU_LI':'FENG_XIAN_JIE_CHU';
    getInfo();
})

const nowDate = ref(moment(new Date()).format('YYYY-MM-DD'));
</script>
<style scoped lang="less">

</style>
