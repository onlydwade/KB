<template>
    <a-drawer :maskClosable="false"
    :width="1024"
    :title="title"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
        <template #extra>
            <a-space :size="16" v-if="type=='SHOW'">
                <a-button size="large" @click="handleClose">关闭窗口</a-button>
            </a-space>
            <a-space :size="16" v-else>
                <a-button size="large" @click="handleClose">关闭窗口</a-button>
                <a-button size="large" @click="handleOk">暂存</a-button>

                <CompanyOaBtn
                    v-if="visible"
                    ref="odBtnRef"
                    mode="COMPANY_SAN_HUI_SHEN_PI"
                    :companyId="companyIdEnd" :recordId="formData.id"
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
                    <a-form-item label="审批发起人" name="approvalUserId">
                        <UserSelect v-model="formData.approvalUserId" :users="formData.approvalUser" disabled/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="审批发起日期">
                        <a-input disabled allowClear :value="nowDate"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="企业名称" name="companyName" v-if="companyId || type!='ADD'">
                        <a-input disabled allowClear :value="formData.companyName"/>
                    </a-form-item>
                    <a-form-item required label="企业名称" name="companyId" v-else>
                        <DataSelect
                            v-model="formData.companyId"
                            @change="change"
                            modeName="company"
                            :options="{
                                key   : 'id',
                                label : 'name'
                            }"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="会议标题" name="title">
                        <a-input :disabled="disabled" allowClear v-model:value="formData.title"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
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
                <a-col :span="12">
                  <a-form-item required label="会议时间" name="meetingTime">
                    <a-date-picker v-model:value="formData.meetingTime"
                                   :disabled="disabled"
                                   show-time
                                   placeholder="会议时间"
                                   class="w_full"
                                   valueFormat="YYYY-MM-DD HH:mm:ss"
                                   format="YYYY-MM-DD HH:mm:ss" />
                  </a-form-item>
                </a-col>
            </a-row>
            <a-form-item label="内容描述" name="content">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.content" placeholder="请输入(200字以内) ！" show-count :maxlength="200"/>
            </a-form-item>
            <template v-if="companyIdEnd">
                <Title class="titlebar" title="文件上传">
                    <a-form-item name="fileOk"
                    :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                    </a-form-item>
                </Title>
                <CompanyDocuments v-model="formData.fileOk" v-if="visible" ref="documentRef" :companyId="companyIdEnd" :menuId="menuId" :recordId="formData.id" :readOnly="disabled"/>
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
        default : 53,
    }
})

const companyIdEnd = computed(()=>{
    return props.companyId || formData.value.companyId;
})
const emit        = defineEmits(['success'])
const visible     = ref(false);
const type        = ref('ADD');
const disabled    = computed(()=>type.value=='SHOW');
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
        let apiKey = type.value=='ADD'?'correlationAdd':'correlationEdit';
        formData.value.companyId = companyIdEnd.value;
        api.investment[apiKey](formData.value,'ompanyHighLevelMeetingApproval').then(async res=>{
            if(res.code==200){
                if(type.value=='ADD'){
                    let uploadResult = await documentRef.value.batchUpLoad(res.data.id);
                }
                if(action=='OA'){
                    let subject = formData.value.companyName +'-'+ formData.value.title +'-三会审批';
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
const change = (id,data) =>{
 if(id){
  formData.value.companyName = data.label
 }else{
  formData.value.companyName =''
 }
}
const handleClose = ()=>{
    visible.value = false;
}
const titleObj = {ADD:'发起',EDIT:'修改',SHOW:'查看'};
const open     = (key,data,companyName)=>{
    type.value    = key;
    try {
        formData.value = JSON.parse(JSON.stringify(data));
    } catch (e) {
        type.value     = 'ADD';
        formData.value = {};
    }
    title.value   =  titleObj[type.value] + ' “三会” 审批';
    if(key=='ADD'){
        formData.value.approvalUserId = store.userInfo.userId;
        formData.value.approvalUser   = {
            userId   : store.userInfo.userId,
            realname : store.userInfo.realname
        }
        if(companyName){
            formData.value.companyName  = companyName;
        }
    }
    visible.value = true;
}
defineExpose({open})

const nowDate = ref(moment(new Date()).format('YYYY-MM-DD'));
</script>
<style scoped lang="less">
.titlebar{
    margin : 16px -16px;
    &:first-child{
        margin-top : -16px;
    }
}
</style>
