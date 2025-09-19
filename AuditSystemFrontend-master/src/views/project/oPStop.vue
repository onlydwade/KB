<template>
    <div class="page-os_stop">
      <div v-if="isMobileDevice">
          <div class="card_box" >
            <van-cell-group title="终止项目信息">
                    <van-cell title="项目名称" :value="formData.projectName || '-'"/>
                    <van-cell title="申请内容" value="终止项目"/>
                    <van-cell title="发起人" :value="formData.terminationSponsor || '-'"/>
                    <van-cell title="发起日期" :value="dateFormat(formData.terminationTime,'YYYY-MM-DD') || '-'"/>
                    <van-cell title="发起人" :value="formData.terminationSponsor || '-'"/>
                    <van-cell title="项目终止原因" :value="formData.terminationReason || '-'"/>
            </van-cell-group>
          </div>
          <FileListVal :list="formData.terminationDocumentArr"/>
      </div>

      <div class="os_stop_in" v-else>
            <div class="padding_box">
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
                        <Upload :disabled="disabled" modeName="follow" v-model="formData.terminationDocumentArr"/>
                    </a-form-item>
                </a-form>
            </div>

            <FooterBarL>
                <StopOaBtn @submit="handleOk" :projectId="formData.id" :oaTempId="oaTempId" place="topLeft" @getOaStatus="getOaStatus" :projectInfo="infoData"></StopOaBtn>
                <a-button size="large" @click="save(1)">暂存</a-button>
            </FooterBarL>
      </div>

    </div>
</template>
<script setup>
import api                                              from '@/api/index';
import { handleTree , TpWatermark , RemoveTpWatermark,dateFormat ,isMobile} from '@/utils/tools';
import { mainStore }                                    from '@/store';
const router    = useRouter();
const route     = useRoute();
const projectId = ref(Number(route.query.id || 0));
const store     = mainStore();
const isMobileDevice  = isMobile()
onMounted(() => {
    if(route.query.access_token){
        sessionStorage.setItem('backPath',route.fullPath.replace('&access_token='+route.query.access_token,''))
        window.location.href = GLOBAL_PATH.tokenAuth+route.query.access_token;
        return;
    }
    getInfo(()=>{
        getDept();
    });
})

const infoData = ref({});
const getInfo  = (callBack)=>{
    api.project.projectInfo(projectId.value).then(res=>{
        if(res.code==200){
            infoData.value = res.data;
            open(res.data)
            callBack && callBack();
        }
    })
}
const getDept = ()=>{
    api.sys.deptList().then(res=>{
        if(res.code==200){
            store.setDeptTree(handleTree(res.data,"deptId"));
        }
    })
}

import StopOaBtn     from './components/StopOaBtn.vue';
const formData = ref({});
const formRef  = ref(null);
const handleOk = (type,temp)=>{
    formRef.value.validateFields().then(values=>{
        save(type,temp);
    })
}
const save = (type,temp)=>{
    formData.value.terminationDocument = JSON.stringify(formData.value.terminationDocumentArr);
    api.project.projectClose(formData.value).then(res=>{
        if(res.code==200){
            if(type==1){
                message.success('操作成功');
            }else{
                oaSubmit(type,temp)
            }
        }
    })
}
const oaTempId = ref('');
const open        = (data,index)=>{
    formData.value = {
        id                     : data.id,
        projectName            : data.projectName,
        terminationSponsor     : (data.terminationSponsor || store.userInfo.realname),
        terminationTime        : data.terminationTime,
        terminationReason      : data.terminationReason,
        terminationDocumentArr : JSON.parse(data.terminationDocument || '[]')
    }
    oaTempId.value    = data.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'?'1867201364c97da9575926e4cf896ea9':'18671ffc27c58db73c92ec94410806e7';
}
const bus      = inject('bus');
const oaSubmit = (type,temp)=>{
    let postData = {
        subject        : formData.value.projectName+'-终止审批',
        recordId       : formData.value.id,
        subRecordId    : formData.value.id,
        templateId     : temp.templateId,
        moduleName     : 'Project',
        approvalStatus : 1,
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
    disabled.value = res;
}
</script>
<style scoped lang="less">
.page-os_stop{
    display        : flex;
    flex-direction : column;
    height         : 100%;
    padding        : 16px;
    :v-deep .van-cell__value {
    color: #000;
  }
  :v-deep .van-cell-group__title {
    color: #000;
    font-weight: bold;
  }
}
.os_stop_in{
    flex             : 1;
    background-color : #fff;
    border-radius    : 8px;
    overflow-y       :auto ;
}
</style>