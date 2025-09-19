
<template>
    <a-drawer :maskClosable="false"
    :width="1024"
    title="终止项目申请"
    destroyOnClose
    @close="visible=false"
    :visible="visible">
        <template #extra>
            <a-space :size="16">
                <!-- <a-button size="large" type="primary" @click="handleOk">保存</a-button> -->
                <StopOaBtn @submit="handleOk" :projectId="formData.id" :oaTempId="oaTempId" @getOaStatus="getOaStatus" :projectInfo="formData" :terminationStatus="terminationStatus"></StopOaBtn>
                <a-button :disabled="disabled" size="large" @click="save(1)">暂存</a-button>
            </a-space>
        </template>
        <a-form layout="vertical" ref="formRef" :model="formData">
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item label="项目名称">
                        <a-input disabled :value="formData.projectName"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="申请内容">
                        <a-input disabled value="终止项目"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="发起人" name="terminationSponsor">
                        <a-input :disabled="disabled" v-model:value="formData.terminationSponsor" placeholder="请输入"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="发起日期" name="terminationTime">
                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.terminationTime"
                            class="w_full"
                            valueFormat="YYYY-MM-DD 00:00:00"
                            format="YYYY-MM-DD"
                            placeholder="请选择" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-form-item name="terminationReason" required label="项目终止原因">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.terminationReason" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            <a-form-item name="terminationDocumentArr" label="附件上传">
                <Upload v-if="uploadVisible" modeName="follow" v-model="formData.terminationDocumentArr" :disabled="disabled"/>
                <Upload v-else modeName="follow" v-model="formData.terminationDocumentArr"  @change="uploadChange"/>
            </a-form-item>
        </a-form>
    </a-drawer>
</template>
<script setup>
import api           from '@/api/index';
import { message }   from 'ant-design-vue';
import { mainStore } from '@/store';
import StopOaBtn     from './StopOaBtn.vue';
import { dateFormat } from '@/utils/tools';
const store    = mainStore();
const emit     = defineEmits(['success'])
const visible  = ref(false);
const formData = ref({});
const formRef  = ref(null);
const uploadVisible = ref(true)
const terminationStatus = computed(()=>{
  return formData.value.expire=='YI_SHI_XIAO' || ['YI_ZHONG_ZHI'].includes(formData.value.serviceStatus)
})
const handleOk = (type,temp)=>{
    formRef.value.validateFields().then(values=>{
        save(type,temp);
    })
}
const save = (type,temp)=>{
    formData.value.terminationDocument = JSON.stringify(formData.value.terminationDocumentArr);
    api.project.projectClose(formData.value).then(res=>{
        if(res.code==200){
            emit('success',{},handleIndex.value);
            if(type==1){
                message.success('操作成功');
            }else{
                oaSubmit(type,temp)
            }
        }
    })
}
const handleIndex = ref(-1);
const oaTempId = ref('');
const open        = (data,index)=>{
    formData.value = {
        id                     : data.id,
        projectName            : data.projectName,
        terminationSponsor     : (data.terminationSponsor || store.userInfo.realname),
        terminationTime        : data.terminationTime,
        terminationReason      : data.terminationReason,
        terminationDocumentArr : JSON.parse(data.terminationDocument || '[]'),

        attributorUserId : data.attributorUserId,
        createUserId     : data.createUserId,
        expire           :data.expire,
        serviceStatus    :data.serviceStatus
    }
    oaTempId.value    = data.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'?'1867201364c97da9575926e4cf896ea9':'18671ffc27c58db73c92ec94410806e7';
    handleIndex.value = index;
    visible.value     = true;
}
defineExpose({open})
const bus      = inject('bus');
const oaSubmit = (type,temp)=>{
    let postData = {
        subject        : formData.value.projectName+'-终止审批',
        recordId       : formData.value.id,
        subRecordId    : formData.value.id,
        templateId     : temp.templateId,
        moduleName     : 'Project',
        approvalStatus : 5,
        mainProcess    : true,
        detailUrl      : window.location.origin+'/#/projectOaStop?id='+formData.value.id,
        approvalData   : 'YI_ZHONG_ZHI'
    }
    let apiKey = 'oaAdd';
    if(type==-1){
        apiKey      = 'oaUpdate';
        postData.id = temp.oaId;
    }else{
        postData.submitTime   = dateFormat(new Date());
        postData.createTime   = dateFormat(new Date())
        postData.submitUserId = temp.userId;
    }
    api.common[apiKey](postData).then(res=>{
        if(res.code==200){
            message.success('操作成功！');
            bus.emit('oaHasSubmit');
        }
    })
}

const disabled = ref(false);
const getOaStatus = (res)=>{
    console.log('res',res)
    uploadChangeStatus.value = res
    const postId = import.meta.env.VITE_ADMINISTRATOR_POST_ID;
    if (parseInt(postId) === store.deptPost.postId) {
      uploadVisible.value = false
    }
    if(terminationStatus.value){
      disabled.value = terminationStatus.value;
    }else{
      disabled.value = res;
    }

}
const uploadChangeStatus = ref(false)
const uploadChange = ()=>{
  if(uploadChangeStatus.value){
    handleOk(1)
  }
}
</script>
<style scoped lang="less">

</style>
