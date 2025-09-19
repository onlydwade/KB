
<template>
    <a-space :size="16">
        <a-divider type="vertical" style="width: 2px; background-color: #f99c34" v-if="placement=='bottomRight'"/>
        
        <a-button v-if="oaNewest.approvalStatus==5" size="large" type="primary" :disabled="oaNewest.approvalStatus==5">审批待确认</a-button>
        <a-button v-if="oaNewest.approvalStatus==1" size="large" type="primary" :disabled="oaNewest.approvalStatus==1">审批中...</a-button>
        <a-button v-if="oaNewest.approvalStatus==2" size="large" type="primary" :disabled="oaNewest.approvalStatus==2">审批通过</a-button>
        
        <template v-if="hasPermission(['biz:projectCompany:createOa'],'investment') || myProject">
            <a-button :loading="loadding" :disabled="btnDisabled" v-if="!oaNewest.approvalStatus || oaNewest.approvalStatus==0" size="large" type="primary" @click="startOa(0)">发起{{tempName}}</a-button>
            <a-button :loading="loadding" :disabled="btnDisabled" v-if="oaNewest.approvalStatus==10" size="large" type="primary" @click="startOa(0)">{{tempName}}已删除，重新发起</a-button>
            <a-button :loading="loadding" :disabled="btnDisabled" v-if="oaNewest.approvalStatus==3" size="large" type="primary" @click="startOa(-1)">{{tempName}}已驳回，重新发起</a-button>
            <a-button :loading="loadding" :disabled="btnDisabled" v-if="oaNewest.approvalStatus==4" size="large" type="primary" @click="startOa(0)">{{tempName}}已废弃，重新发起</a-button>
        </template>
        <template v-else>
            <a-button size="large" type="primary" :disabled="btnDisabled" v-if="![1,2,5].includes(oaNewest.approvalStatus)">无“{{tempName}}”权限</a-button>
        </template>

        <a-popconfirm title="" style="z-index:10" v-model:visible="createNewOa">
            <template #icon>
                <div style="font-size:16px;padding-bottom:16px;">
                    确认OA审批发起人(可搜索切换)：
                </div>
                <UserSelect v-model="oaUserId" :users="oaUser" style="width:280px;" size="large"/>
            </template>
            <template #okButton>
                <a-button :loading="loadding" size="large" type="primary" @click="startOa(0)">确认提交</a-button>
            </template>
            <template #cancelButton>
                <a-button @click="createNewOa = false" size="large">取消</a-button>
            </template>
            <a-button  v-if="oaNewest.approvalStatus&&hasPermission(['biz:projectCompany:createNewOa'],'investment')" size="large" type="primary">创建新{{tempName}}流程</a-button>
        </a-popconfirm>

        <a-popconfirm title="" :placement="placement" v-if="oaNewest.id">
            <template #icon>
                <div class="" style="width:780px;margin:0 -16px">
                    <Title title="最新审批信息"></Title>
                    <div style="padding:16px;">
                        <a-descriptions :column="1">
                            <a-descriptions-item label="审批编号">{{oaNewest.approvalNo || '-'}}</a-descriptions-item>
                            <a-descriptions-item label="审批状态">{{status[oaNewest.approvalStatus]}}</a-descriptions-item>
                            <a-descriptions-item label="OA审批详情" v-if="oaNewest.approvalUrl">
                                <a class="color-link" @click="toOaLink(oaNewest.approvalUrl)">
                                    {{oaNewest.approvalUrl}}
                                </a>
                            </a-descriptions-item>
                        </a-descriptions>
                    </div>
                    <Title title="审批记录"></Title>
                    <div class="" style="padding:16px;">
                        <a-table :columns="columns" :data-source="oaList" :pagination="false" :scroll="{ x: '100%',y:360 }" bordered>
                            <template #bodyCell="{ column, record }">
                                <template v-if="column.key === 'approvalStatus'">
                                    {{status[record.approvalStatus]}}
                                </template>
                                <template v-if="column.key === 'action'">
                                    <a class="color-link" @click="toOaLink(record.approvalUrl)" v-if="record.approvalUrl">
                                        查看OA详情
                                    </a>
                                </template>
                            </template>
                        </a-table>
                    </div>
                </div>
            </template>
            <template #cancelButton>
            </template>
            <template #okButton>
            </template>
            <a-button size="large" v-if="oaNewest.id" class="arrow_left_btn">
                <span class="arrow_before"></span>
                <template #icon><more-outlined style="color:#f99c34"/></template>
                审批详情
            </a-button>
         </a-popconfirm>

         <a-divider type="vertical" style="width: 2px; background-color: #f99c34" v-if="placement=='topLeft'"/>
    </a-space>
</template>
<script setup>
import api               from '@/api/index';
import {oaIds}           from '@/utils/baseData';
import { message }       from 'ant-design-vue';
import { hasPermission } from '@/utils/tools';
import { dateFormat }    from '@/utils/tools';
import { mainStore }     from '@/store';
const store = mainStore();
const props = defineProps({
    disabled:{
        type    : Boolean,
        default : false
    },
    // projectId:{
    //     type    : Number,
    //     default : 0,
    // },
    companyId:{
        type    : Number,
        default : 0,
    },
    recordId:{
        type    : Number,
        default : 0,
    },
    tempName:{
        type    : String,
        default : 'OA审批',
    },
    action:{
        type    : String,
        default : '',
    },
    placement:{
        type    : String,
        default : 'bottomRight',
    },
    mode:{
        type    : String,
        default : '',
    },
})
const status = ref({
    0:'待发起审批',
    1:'审批中',
    2:'审批通过',
    3:'已驳回',
    4:'已废弃',
    5:'待确认',
    8:'线下审批通过',
    9:'无需审批',
})
const emit    = defineEmits(['submit']);
const oaList  = ref([]);
const columns = ref([
    {
        title     : '审批编号',
        dataIndex : 'approvalNo',
        width     : 320,
        ellipsis  : true,
    },
    {
        title : '审批状态',
        key   : 'approvalStatus',
        width : 120,
    },
    {
        title        : '发起时间',
        dataIndex    : 'createTime',
        width        : 180,
    },
    {
        title     : '发起人',
        dataIndex : ['submitUser','realname'],
        width     : 180,
    },
    {
        title : '操作',
        key   : 'action',
        width : 120,
        fixed : 'right'
    },
])
const oaNewest  = computed(()=>{
    return oaList.value.length>0?oaList.value[0]:{};
});
const getList   = (type,companyId,recordId)=>{
    let postData = {
        desc     : ['createTime'],
        pageNo   : 1,
        pageSize : 500,
        params   : {
            moduleName  : 'Company',
            recordId    : companyId || props.companyId,
            subRecordId : recordId || props.recordId,
            templateId  : oaIds[props.mode].tempId
        }
    }
    api.common.oaPage(postData).then(res=>{
        if(res.code==200){
            oaList.value    = res.data.records || []
            if(type!='first'&&oaList.value.length>0&&oaList.value[0].approvalUrl){
                toOaLink(oaList.value[0].approvalUrl);
            }
        }
    })
}
onMounted(() => {
    if(props.companyId){
        isInTeam();
    }
    if(props.recordId){
        getList('first');
    }

    oaUserId.value = store.userInfo.userId;
    oaUser.value   = {
        userId   : store.userInfo.userId,
        realname : store.userInfo.realname
    }
})


const attributorUserId = ref(null);
const createUserId     = ref(null);
const myProject        = computed(()=>{
  return store.userInfo.userId==attributorUserId.value || store.userInfo.userId==createUserId.value || inTeam.value;
})
const btnDisabled = computed(()=>{
   if(hasPermission(['biz:projectCompany:createOa']) || myProject.value){
    return false
   }
   return true
})
const createNewOa = ref(false);
const oaUserId    = ref(0);
const oaUser      = ref({});
const inTeam      = ref(false);
const isInTeam    = async ()=>{
    let pId = null;
    let res = await api.investment.correlationGet(props.companyId,'projectCompany');
    if(res.code==200){
        pId                    = res.data.projectId;
        createUserId.value     = res.data.createUserId;
        attributorUserId.value = res.data.attributorUserId;
    }
    api.investment.inProject(pId).then(res=>{
        if(res.code==200){
            inTeam.value = res.data || false;
        }
    })

}

const loadding = ref(false);
const startOa = (type)=>{
    if(loadding.value){
        return;
    }
    loadding.value = true;
    setTimeout(()=>{
        loadding.value = false;
    },2000)
    emit('submit',type);
}
const oaSubmit = (type,subject,companyId,recordId,callBack)=>{
    let postData = {
        subject        : subject,
        recordId       : companyId || props.companyId,
        subRecordId    : recordId || props.recordId,
        templateId     : oaIds[props.mode].tempId,
        moduleName     : 'Company',
        subModuleName  : oaIds[props.mode].module,
        approvalStatus : 5,
        mainProcess    : true,
        detailUrl      : window.location.origin+'/#/companyOaInfo?id='+recordId+'&mode='+props.mode
    }
    if(props.action){
        postData.subRecordAction = props.action;
    }
    let apiKey = 'oaAdd';
    if(type==-1){
        apiKey      = 'oaUpdate';
        postData.id = oaNewest.value.id;
    }else{
        // postData.submitTime   = dateFormat(new Date());
        // postData.createTime   = dateFormat(new Date())
        // postData.submitDeptId = store.deptPost.deptId || store.userInfo.deptId;
        postData.submitUserId = oaUserId.value || store.userInfo.userId;
        // postData.submitUser = {
        //     id       : store.userInfo.userId,
        //     realname : store.userInfo.realname
        // };
    }
    api.common[apiKey](postData).then(async res=>{
        if(res.code==200){
            getList('reLoad',companyId,recordId);
            callBack && callBack();
        }
    })

}
defineExpose({oaSubmit,oaUserId})

const toOaLink = (link)=>{
    api.common.getSsoToken({
        mobile:store.userInfo.phonenumber
    }).then(res=>{
        if(res.code==200 && res.data){
            window.open(link+'&access_token='+res.data);
        }
    })
}
</script>
<style scoped lang="less">
:deep(.arrow_left_btn){
    position: relative;
    margin-left: -4px;
    margin-right: -4px;
    &::after,.arrow_before{
        border   : solid transparent;
        content  : ' ';
        height   : 0;
        top      : 0;
        bottom   : 0;
        margin   : auto;
        right    : 100%;
        position : absolute;
        width    : 0px;
    }
    &::after{
        border-width:6px;
        border-right-color: #fff!important;
        z-index: 50;
    }
    .arrow_before{
        border-width:7px;
        border-right-color: #ccc;
        opacity: 0.8;
        z-index: 49;
    }
    &:hover,&:focus{
        .arrow_before{
            border-width:8px;
            border-right-color: @primary-color;
        }
    }
}
</style>
