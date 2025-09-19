<template>
    <div class="page-project content-inner">
        <a-page-header :ghost="false"
            :breadcrumb="{ routes }">
            <template #footer>
                <a-tabs v-model:activeKey="pageType" @change="filterSubmit">
                    <a-tab-pane key="FENG_XIAN_YU_JING" tab="风险预警"/>
                    <a-tab-pane key="FENG_XIAN_CHU_LI" tab="风险处理"/>
                </a-tabs>
            </template>
        </a-page-header>
        <riskFilter v-model="filterForm" @submit="filterSubmit" :mode="pageType" />
        <div class="content-box_full">
            <Title :title="pageType=='FENG_XIAN_YU_JING'?'风险预警':'风险处理'">
                <template #right>
                    <a-space>
                        <a-button type="primary" @click="drawer('ADD')" v-permission="['biz:projectCompany:add']">
                            <template #icon>
                                <plus-outlined />
                            </template>
                            新增
                        </a-button>
                    </a-space>
                </template>
            </Title>
            <FullTable :loadding="loadding" :columns="data[pageType]" :dataSource="data.list">
                <template #bodyCell="{ column,text,record,index }">
                    <template v-if="column.key === 'companyName'">
                        <router-link :to="'/innerPage/subsidiaryInfo?id='+record.companyId" class="color-link flex_box">
                            <EllipsisTooltip class="flex_full" :content="record.companyName"/>
                        </router-link>
                    </template>
                    <template v-if="column.key === 'sort'">
                        {{(filterForm.pageNo-1)*filterForm.pageSize + index +1}}
                    </template>
                    <template v-if="column.key === 'processApprovalUrl'">
                        <!-- <OaLink :link="record.processApprovalUrl"/> -->
                        <div class="flex_box color-link" @click="toOaLink(record.processApprovalUrl)">
                          <EllipsisTooltip class="flex_full" :content="record.processApprovalUrl"/>
                        </div>
                    </template>
                    <template v-if="column.key === 'relieveApprovalUrl'">
                        <!-- <OaLink :link="record.relieveApprovalUrl"/> -->
                        <div class="flex_box color-link" @click="toOaLink(record.relieveApprovalUrl)">
                          <EllipsisTooltip class="flex_full" :content="record.relieveApprovalUrl"/>
                        </div>
                    </template>
                    <template v-if="column.key === 'expired'">
                      <div v-if="record.expired" class="status_box">
                        <span class="dot" :style="'background-color:#f5222d'"></span>
                        <span class="name text_no_warp">
                            {{ record.expired }}
                        </span>
                        <slot></slot>
                      </div>
                    </template>
                    <template v-if="column.key === 'action'">
                        <actionBtn :actions="actions(record,index)"/>
                    </template>
                    <EllipsisTooltip v-if="column.ellipsis" :content="text"/>
                </template>
            </FullTable>
            <div class="pagination_box">
                <a-pagination showSizeChanger show-quick-jumper
                    v-model:current="filterForm.pageNo"
                    v-model:pageSize="filterForm.pageSize"
                    :show-total="total => `共 ${total} 条数据`"
                    size="small"
                    @change="getPage"
                    @showSizeChange="filterForm.pageNo=1"
                    :total="data.total" />
            </div>
        </div>

        <riskWarningDrawer ref="drawerWarningRef" @success="getPage"/>
        <riskHandleDrawer ref="drawerHandleRef" @success="getPage"/>
    </div>
</template>
<script setup>
import api                          from '@/api/index';
import { message , Modal }          from 'ant-design-vue';
import riskFilter                   from './components/riskFilter.vue';
import riskWarningDrawer            from './components/riskWarningDrawer.vue';
import riskHandleDrawer             from './components/riskHandleDrawer.vue';
import { hasPermission,dateFormat } from '@/utils/tools';
import { mainStore }                from '@/store';

import { useRecordFileDel } from './components/recordFileDel';
const {
    delRecordFile
} = useRecordFileDel();

const store  = mainStore();
const router = useRouter();
const routes = [
    {
        breadcrumbName: '投后管理',
    },
    {
        breadcrumbName: '风险管理',
    },
];
const pageType   = ref('FENG_XIAN_YU_JING');
const loadding   = ref(false);
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})
const oaStatus = ref({
    0:'待发起审批',
    1:'审批中',
    2:'审批通过',
    3:'已驳回',
    4:'已废弃',
    5:'待确认',
    8:'线下审批通过',
    9:'无需审批',
})
const data = reactive({
    list                : [],
    'FENG_XIAN_YU_JING' : [
        {
            title : '企业名称',
            key   : 'companyName',
            width : 200,
        },
        {
            title     : '风险节点名称',
            dataIndex : 'node',
            width     : 180,
            ellipsis  : true,
        },
        {
            title     : '里程碑',
            dataIndex : 'milepostTime',
            width     : 140,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '提醒时间',
            dataIndex : 'reminderTime',
            width     : 140,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '提醒内容',
            dataIndex : 'content',
            width     : 250,
            ellipsis  : true,
        },
        {
          title     : '状态',
          key       : 'expired',
          width     : 140
        },
        {
            title : '操作',
            key   : 'action',
            width : 120,
            fixed : 'right'
        },
    ],
    'FENG_XIAN_CHU_LI' : [
        {
            title    : '序号',
            key      : 'sort',
            width    : 80,
        },
        {
            title : '企业名称',
            key   : 'companyName',
            width : 200,
        },
        {
            title     : '风险名称',
            dataIndex : 'name',
            width     : 180,
            ellipsis  : true,
        },
        {
            title     : '风险类型',
            dataIndex : 'typeStr',
            width     : 180,
        },
        {
            title     : '风险状态',
            dataIndex : 'processApprovalStatus',
            width     : 180,
            customRender : ({text})=>{
                return oaStatus.value[text] || '草稿'
            }
        },
        {
            title     : '风险处理发起人',
            dataIndex : ['processApprovalUser','realname'],
            width     : 180,
        },
        {
            title        : '风险处理发起日期',
            dataIndex    : 'processApprovalSendTime',
            width        : 160,
            customRender : ({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title : '风险处理OA审批流',
            key   : 'processApprovalUrl',
            width : 250,
        },
        {
            title     : '风险解除发起人',
            dataIndex : ['relieveApprovalUser','realname'],
            width     : 180,
        },
        {
            title        : '风险解除发起日期',
            dataIndex    : 'relieveApprovalSendTime',
            width        : 160,
            customRender : ({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title : '风险解除审批流',
            key   : 'relieveApprovalUrl',
            width : 250,
        },
        {
            title : '操作',
            key   : 'action',
            width : 240,
            fixed : 'right'
        },
    ],
    total : 0,
})
const apiKey = computed(()=>pageType.value=='FENG_XIAN_CHU_LI'?'companyrisk':'companyRiskInspection');
const getPage = ()=>{
    let postData = builderFilter();
    loadding.value = true;
    api.investment.correlationPage(postData,apiKey.value,pageType.value=='FENG_XIAN_CHU_LI'?46:null).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
const filterSubmit = ()=>{
    data.list         = [];
    filterForm.pageNo = 1;
    getPage();
}

const builderFilter = ()=>{
    let postData = {
        desc       : ['createTime'],
        pageNo     : filterForm.pageNo,
        pageSize   : filterForm.pageSize,
        params     : {},
        geParams   : {},
        leParams   : {},
        inParams   : {},
        likeParams : {}
    }
    let likeParams = ['company_name']
    if(pageType.value=='FENG_XIAN_CHU_LI'){
        likeParams.push('approval_name');
    }else{
        likeParams.push('node');
    }
    likeParams.forEach(key => {
        if(filterForm[key]){
            if(key.includes('_')){
                postData.likeParams[key.replace('_','.')] = filterForm[key];
            }else{
                postData.likeParams[key] = filterForm[key];
            }
        }
    });
    return postData;
}

const actions = (record,index)=>{
    return [
        {
            text  : '查看',
            show  : pageType.value=='FENG_XIAN_CHU_LI',
            click : ()=>{
                drawer('SHOW',record);
            }
        },
        {
            text  : '编辑',
            show  : hasPermission(['biz:projectCompany:edit']),
            click : ()=>{
                drawer('EDIT',record);
            }
        },
        {
            text  : '删除',
            show  : pageType.value=='FENG_XIAN_YU_JING' && hasPermission(['biz:projectCompany:delete']),
            click : ()=>{
                del(record);
            }
        },
        {
            text  : '风险解除审批',
            show  : pageType.value=='FENG_XIAN_CHU_LI' && !record.relieveApprovalStatus,
            click : ()=>{
                drawer('HANDLE',record);
            }
        },
    ];
}

onMounted(() => {
    getPage();
})
onActivated(() => {
    getPage();
})

const del = (data)=>{
    Modal.confirm({
        title   : '操作确认',
        content : '是否确认删除该条数据？',
        async onOk() {
            if(data.documentTemplateList&&data.documentTemplateList.length>0){
                await delRecordFile(data.documentTemplateList);
            }
            api.investment.correlationDel(data.id,apiKey.value).then(res=>{
                if(res.code==200){
                    getPage();
                }
            })
        }
    });
}

//drawer
const drawerWarningRef = ref(null);
const drawerHandleRef  = ref(null);
const drawer           = (type,record)=>{
    if(pageType.value=='FENG_XIAN_YU_JING'){
        drawerWarningRef.value.open(type,record || {});
    }
    if(pageType.value=='FENG_XIAN_CHU_LI'){
        drawerHandleRef.value.open(type,record || {});
    }
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
</script>
<script>export default{name:'homeInvestmentInvestment/risk'}</script>
<style scoped lang="less">
.status_box {
  display     : inline-flex;
  align-items : center;
  .dot{
    width         : 8px;
    height        : 8px;
    border-radius : 50%;
    margin-right  : 8px;
  }
}
</style>
