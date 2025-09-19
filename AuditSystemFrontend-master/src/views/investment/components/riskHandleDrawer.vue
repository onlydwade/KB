<template>
    <a-drawer :maskClosable="false" 
    :width="1324"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
        <template #title>
            <a-radio-group v-model:value="handleType" size="large"  v-if="showHandle">
                <a-radio-button value="FENG_XIAN_JIE_CHU">风险解除</a-radio-button>
                <a-radio-button value="FENG_XIAN_CHU_LI">风险处理</a-radio-button>
            </a-radio-group>
            <span v-else>{{title}}</span>
        </template>
        <template #extra>
            <a-space :size="16" v-if="type=='SHOW'">
                <a-button size="large" @click="handleClose">关闭窗口</a-button>
            </a-space>
            <a-space :size="16" v-else>
                <a-button size="large" @click="handleClose">关闭窗口</a-button>
                <a-button size="large" @click="handleOk">暂存</a-button>

                <CompanyOaBtn
                    v-if="visible && handleType=='FENG_XIAN_CHU_LI' && companyIdEnd"
                    ref="odBtnRef"
                    mode="COMPANY_FX_CHU_LI"
                    tempName="风险处理OA审批"
                    :companyId="companyIdEnd" :recordId="formData.id" action="Process"
                    @submit="startOa">
                </CompanyOaBtn>
                <CompanyOaBtn
                    v-if="visible && handleType=='FENG_XIAN_JIE_CHU' && companyIdEnd"
                    ref="odBtnRef"
                    mode="COMPANY_FX_JIE_CHU"
                    tempName="风险解除OA审批"
                    :companyId="companyIdEnd" :recordId="formData.id" action="Relieve"
                    @submit="startOa">
                </CompanyOaBtn>
            </a-space>
        </template>

        <a-form layout="vertical" :model="formData" ref="formRef">
            <Title class="titlebar">
                <template #title>
                    信息填写
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item label="审批发起人" name="processApprovalUserId">
                        <UserSelect v-model="formData.processApprovalUserId" :users="formData.processApprovalUser" disabled/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="审批发起日期">
                        <a-input disabled allowClear :value="dateFormat(formData.processApprovalSendTime,'YYYY-MM-DD')  ||  dateFormat(nowDate) "/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="企业名称" name="companyName" v-if="companyId || type!='ADD'">
                        <a-input disabled allowClear :value="formData.companyName"/>
                    </a-form-item>
                    <a-form-item required label="企业名称" name="companyId" v-else>
                        <DataSelect
                            v-model="formData.companyId"
                            modeName="company"
                            :options="{
                                key   : 'id',
                                label : 'name'
                            }"/>
                    </a-form-item>
                </a-col>
                <template v-if="handleType=='FENG_XIAN_CHU_LI'">
                    <a-col :span="12">
                        <a-form-item required label="风险节点名称" name="name">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.name" show-count :maxlength="100"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item required label="风险提出人" name="presenter">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.presenter"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
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
                <CompanyDocuments v-model="formData.fileOk" v-if="visible && handleType=='FENG_XIAN_JIE_CHU'" type="JIE_CHU" ref="documentRef" :companyId="companyIdEnd" :menuId="menuId" :recordId="formData.id" :readOnly="disabled2"/>
                <CompanyDocuments v-model="formData.fileOk" v-if="visible && handleType=='FENG_XIAN_CHU_LI'" type="CHU_LI" ref="documentRef" :companyId="companyIdEnd" :menuId="menuId" :recordId="formData.id" :readOnly="disabled"/>
            </template>
            <a-alert :show-icon="false" banner v-else>
                <template #message>
                    选择企业后完善更多信息！！！
                </template>
            </a-alert>
        </a-form>
    </a-drawer>
</template>
<script setup>
import api              from '@/api/index';
import moment           from 'moment'
import { message  }     from 'ant-design-vue';
import { mainStore }    from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict = useDictStore();

const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 46,
    }
})

const companyIdEnd = computed(()=>{
    return props.companyId || formData.value.companyId;
})
const emit        = defineEmits(['success'])
const visible     = ref(false);
const type        = ref('ADD');
const disabled    = computed(()=>type.value=='SHOW' || [1,2,5].includes(formData.value.processApprovalStatus));
const disabled2   = computed(()=>type.value=='SHOW' || [1,2,5].includes(formData.value.relieveApprovalStatus));
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
        let apiKey               = type.value=='ADD'?'correlationAdd':'correlationEdit';
        formData.value.processApprovalSendTime =  type.value=='ADD' ?  nowDate :  formData.value.processApprovalSendTime
        formData.value.companyId = companyIdEnd.value;
        let postData             = JSON.parse(JSON.stringify(formData.value));

        if(handleType.value=='FENG_XIAN_JIE_CHU'){
            delete postData.processApprovalStatus;
            postData.relieveApprovalStatus = 0;
        }
        if(handleType.value=='FENG_XIAN_CHU_LI'){
            delete postData.relieveApprovalStatus;
        }
        postData.relieveApprovalUserId = odBtnRef.value.oaUserId
        api.investment[apiKey](postData,'companyrisk').then(async res=>{
            if(res.code==200){
                if(type.value=='ADD'){
                    let uploadResult = await documentRef.value.batchUpLoad(res.data.id);
                }
                if(action=='OA'){
                    let subject = formData.value.companyName +'-'+ formData.value.name +'-'+ (handleType.value=='FENG_XIAN_JIE_CHU'?'风险解除审批':'风险处理审批');
                    odBtnRef.value.oaSubmit(oaType,subject,formData.value.companyId,formData.value.id || res.data.id,()=>{
                        emit('success');
                        message.success('操作成功');
                        visible.value = false;
                    })
                }else{
                    emit('success');
                    message.success('操作成功');
                    visible.value = false;
                }
            }
        })
    }).catch(err=>{
        message.warning('请完善必填信息！');
    })
}

const handleClose = ()=>{
    visible.value = false;
}
const handleType = ref('FENG_XIAN_JIE_CHU');
const titleObj = {ADD:'新增',EDIT:'修改',SHOW:'查看',HANDLE:'解除'};
const showHandle = computed(()=>{
    return formData.value.relieveApprovalStatus || formData.value.relieveApprovalStatus==0 || type.value=='HANDLE';
})
const open     = (key,data,companyName)=>{
    type.value = key;
    try {
        formData.value = JSON.parse(JSON.stringify(data));
    } catch (e) {
        type.value     = 'ADD';
        formData.value = {};
    }
    title.value   =  '风险处理' + titleObj[type.value];
    if(key=='ADD'){
        handleType.value                     = 'FENG_XIAN_CHU_LI';
        formData.value.processApprovalUserId = store.userInfo.userId;
        formData.value.processApprovalUser   = {
            userId   : store.userInfo.userId,
            realname : store.userInfo.realname
        }
        if(companyName){
            formData.value.companyName  = companyName;
        }
    }else{
        if(formData.value.relieveApprovalStatus || formData.value.relieveApprovalStatus==0 || type.value=='HANDLE'){
            handleType.value = 'FENG_XIAN_JIE_CHU'
        }else{
            handleType.value = 'FENG_XIAN_CHU_LI'
        }
    }
    visible.value = true;
}
defineExpose({open})

const nowDate = ref(moment(new Date()).format('YYYY-MM-DD HH:mm:ss'));
</script>
<style scoped lang="less">
.titlebar{
    margin : 16px -16px;
    &:first-child{
        margin-top : -16px;
    }
}
</style>
