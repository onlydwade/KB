
<template>
    <a-space :size="16" v-if="oaReady">
        <a-button v-if="oaNewest.approvalStatus==5" size="large" type="primary" disabled>审批待确认</a-button>
        <a-button v-if="oaNewest.approvalStatus==1" size="large" type="primary" disabled>审批中...</a-button>
        <a-button v-if="oaNewest.approvalStatus==2" size="large" type="primary" disabled>审批通过</a-button>
        
        <template v-if="hasPermission(['biz:project:createOa']) || myProject">
            <a-button :loading="loadding" v-if="!oaNewest.approvalStatus || oaNewest.approvalStatus==0" size="large" type="primary" @click="startOa(0)">发起OA审批</a-button>
            <a-button :loading="loadding" v-if="oaNewest.approvalStatus==10" size="large" type="primary" @click="startOa(0)">{{temp.shortName}}已删除，重新发起</a-button>
            <a-button :loading="loadding" v-if="oaNewest.approvalStatus==3" size="large" type="primary" @click="startOa(-1)">终止审批已驳回，再次发起</a-button>
            <a-button :loading="loadding" v-if="oaNewest.approvalStatus==4" size="large" type="primary" @click="startOa(0)">终止审批已废弃，重新发起</a-button>
        </template>
        <template v-else>
            <a-button size="large" type="primary" disabled v-if="![1,2,5].includes(oaNewest.approvalStatus)">无发起OA审批权限</a-button>
        </template>
        <a-popconfirm title="" v-model:visible="createNewOa">
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
            <a-button v-if="oaNewest.approvalStatus&&!terminationStatus" size="large" type="primary" v-permission="['biz:project:createNewOa']">发起新OA审批</a-button>
        </a-popconfirm>
        
        <a-popconfirm title="" :placement="place" v-if="oaNewest.id">
            <template #icon>
                <div class="" style="width:780px;margin:0 -16px">
                    <Title :title="'[终止审批] 最新审批信息'"></Title>
                    <div style="padding:16px;">
                        <a-descriptions :column="1">
                            <a-descriptions-item label="审批编号">{{oaNewest.approvalNo || '-'}}</a-descriptions-item>
                            <a-descriptions-item label="审批状态">{{status[oaNewest.approvalStatus]}}</a-descriptions-item>
                            <!-- <a-descriptions-item label="审批结果">{{resoutParse(oaNewest.approvalResult)}}</a-descriptions-item> -->
                            <a-descriptions-item label="OA审批详情" v-if="oaNewest.approvalUrl">
                                <a class="color-link" @click="toOaLink(oaNewest.approvalUrl)">
                                    {{oaNewest.approvalUrl}}
                                </a>
                            </a-descriptions-item>
                        </a-descriptions>
                    </div>
                    <Title :title="'[终止审批] 审批记录'"></Title>
                    <div class="" style="padding:16px;">
                        <a-table :columns="columns" :data-source="oaList" :pagination="false" :scroll="{ x: '100%',y:360 }" bordered>
                            <template #bodyCell="{ column, record }">
                                <template v-if="column.key === 'approvalStatus'">
                                    {{status[record.approvalStatus]}}
                                </template>
                                <!-- <template v-if="column.key === 'approvalResult'">
                                    {{resoutParse(record.approvalResult)}}
                                </template> -->
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
         
         <a-divider type="vertical" style="width: 2px; background-color: #f99c34"/>
    </a-space>
</template>
<script setup>
import api               from '@/api/index';
import { message }       from 'ant-design-vue';
import { hasPermission } from '@/utils/tools';
import { mainStore }     from '@/store';
const store = mainStore();
const props = defineProps({
    projectId:{
        type    : Number,
        default : 0,
    },
    menuInfo:{
        type    : Object,
        default : {},
    },
    projectInfo:{
        type    : Object,
        default : {},
    },
    oaTempId:{
        type    : String,
        default : '',
    },
    place:{
        type    : String,
        default : 'bottomRight',
    },
    terminationStatus:{
      type      :Boolean,
      default   :false
    }
    
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
const bus       = inject('bus');
const emit      = defineEmits(['submit','getOaStatus']);
const oaList    = ref([]);
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
    // {
    //     title : 'OA审批结果内容',
    //     key   : 'approvalResult',
    //     width : 180,
    // },
    // {
    //     title     : 'OA审批备注',
    //     dataIndex : 'remark',
    //     width     : 180,
    // },
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
const oaReady   = ref(false);
const oaNewest  = computed(()=>{
    return oaList.value.length>0?oaList.value[0]:{};
});
const getList   = (type)=>{
    let postData = {
        desc     : ['createTime'],
        pageNo   : 1,
        pageSize : 500,
        params   : {
            recordId   : props.projectId,
            subRecordId :props.menuInfo.id,
            templateId : props.oaTempId
        }
    }
    api.common.oaPage(postData).then(res=>{
        if(res.code==200){
            oaList.value  = res.data.records || []
            oaReady.value = true;
            if(type!='first'&&oaList.value.length>0&&oaList.value[0].approvalUrl){
                toOaLink(oaList.value[0].approvalUrl);
            }
            if(oaList.value.length>0){    
                emit('getOaStatus',[1,2,8].includes(oaList.value[0].approvalStatus));
            }else{
                emit('getOaStatus',false);
            }
            
            createNewOa.value = false;
        }
    })
}


const myProject = computed(()=>{
    return store.userInfo.userId==props.projectInfo.attributorUserId || store.userInfo.userId==props.projectInfo.createUserId || inTeam.value;
})

const createNewOa = ref(false);
const oaUserId    = ref(0);
const oaUser      = ref({});
const inTeam      = ref(false);
const isInTeam    = ()=>{
    api.project.inTeam(props.projectId).then(res=>{
        if(res.code==200){
            inTeam.value = res.data || false;
        }
    })
}

onMounted(() => {
    getList('first');
    bus.on('oaHasSubmit', getList);
    isInTeam();
    oaUserId.value = store.userInfo.userId;
    oaUser.value   = {
        userId   : store.userInfo.userId,
        realname : store.userInfo.realname
    }
})
onUnmounted(() => {
    bus.off('oaHasSubmit',getList);
})

const loadding = ref(false);
const startOa = (type)=>{
    if(loadding.value){
        return;
    }
    loadding.value = true;
    setTimeout(()=>{
        loadding.value = false;
    },2000)
    emit('submit',type,{templateId:props.oaTempId,oaId:oaNewest.value.id,userId:oaUserId.value || store.userInfo.userId});
}

const toOaLink = (link)=>{
    api.common.getSsoToken({
        mobile:store.userInfo.phonenumber
    }).then(res=>{
        if(res.code==200 && res.data){
            window.open(link+'&access_token='+res.data);
        }
    })
}
const resoutParse = (str)=>{
    try {
        return JSON.parse(str).docStatusName || '-'
    } catch (e) {
        return str || '-'
    }
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
