<template>
    <div class="page-project content-inner">
        <a-page-header :ghost="false"
            :breadcrumb="{ routes }">
            <template #footer>
                <a-tabs v-model:activeKey="pageType" @change="filterSubmit">
                    <a-tab-pane key="DOCUMENT_RECORD" tab="“三会”文档记录"/>
                    <a-tab-pane key="APPROVE_RECORD" tab="“三会”审批记录"/>
                </a-tabs>
            </template>
        </a-page-header>
        <governanceFilter v-model="filterForm" :type="pageType" @submit="filterSubmit" @dataExport="dataExport"/>
        <div class="content-box_full">
            <Title title="“三会”文档记录" v-if="pageType=='DOCUMENT_RECORD'">
                <template #right>
                    <a-space>
                        <a-button @click="batchDownLoad" :disabled="batchIds.length==0" v-permission="['biz:file:downloadBatch']">批量下载</a-button>
                        <a-button type="primary" @click="drawer('ADD')" v-permission="['biz:projectCompany:add']">
                            <template #icon>
                                <plus-outlined />
                            </template>
                            新建
                        </a-button>
                    </a-space>
                </template>
            </Title>
            <FullTableAuto v-if="pageType=='DOCUMENT_RECORD'" @tableReady="tableReady">
                <a-table :columns="columns[pageType]" :loading="loadding" :data-source="data.list" :row-selection="rowSelection" rowKey="id"  :pagination="false" :scroll="{y: tableHeight }">
                    <template #bodyCell="{ column,text,record,index }">
                        <template v-if="column.key === 'documentTemplateList'">
                            <!-- <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                                <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                            </template> -->
                            <a-popconfirm placement="topLeft">
                              <template #icon>
                                  <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                                      <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                                  </template>
                              </template>
                              <template #cancelButton>
                              </template>
                              <template #okButton>
                              </template>
                              <a-bottom type="text" class="color-link" v-if="hasFile(record.documentTemplateList)">查看文件</a-bottom>
                            </a-popconfirm>
                        </template>
                        <template v-if="column.key === 'action'">
                            <actionBtn :actions="actions(record,index)"/>
                        </template>
                        <EllipsisTooltip v-if="column.ellipsis" :content="text"/>
                    </template>
                </a-table>
            </FullTableAuto>

            <Title title="“三会”审批记录" v-if="pageType=='APPROVE_RECORD'">
                <template #right>
                    <a-space>
                        <a-button type="primary" @click="drawer('ADD')" v-permission="['biz:projectCompany:add']">
                            <template #icon>
                                <plus-outlined />
                            </template>
                            发起三会审批
                        </a-button>
                    </a-space>
                </template>
            </Title>
            <FullTable v-if="pageType=='APPROVE_RECORD'" :loadding="loadding" :columns="columns[pageType]" :dataSource="data.list">
                <template #bodyCell="{ column,text,record,index }">
                    <template v-if="column.key === 'approvalUrl'">
                        <!-- <OaLink :link="record.approvalUrl"/> -->
                        <div class="flex_box color-link" @click="toOaLink(record.approvalUrl)">
                          <EllipsisTooltip class="flex_full" :content="record.approvalUrl"/>
                        </div>
                    </template>
                    <template v-if="column.key === 'documentTemplateList'">
                        <!-- <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                            <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                        </template> -->
                        <a-popconfirm placement="topLeft" >
                              <template #icon>
                                  <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                                      <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                                  </template>
                              </template>
                              <template #cancelButton>
                              </template>
                              <template #okButton>
                              </template>
                              <a-bottom type="text" class="color-link" v-if="hasFile(record.documentTemplateList)">查看文件</a-bottom>
                        </a-popconfirm>
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

        <threeMeetingDoc ref="drawerDocRef" :menuId="menuId"  @success="getPage"  />
        <threeMeetingApprove ref="drawerApproveRef" :menuId="menuId" @success="getPage"/>
    </div>
</template>
<script setup>
import api                                       from '@/api/index';
import { message,Modal }                         from 'ant-design-vue';
import governanceFilter                          from './components/governanceFilter.vue';
import threeMeetingDoc                           from './components/threeMeetingDoc.vue'
import threeMeetingApprove                       from './components/threeMeetingApprove.vue'
import { useBatchHandle }                        from './components/batchHandle';
import { dataToFile , hasPermission,dateFormat ,hasFile} from '@/utils/tools';
import { mainStore }                             from '@/store';
import { ref } from 'vue';
const store  = mainStore();
const router = useRouter();
const routes     = [
    {
        breadcrumbName: '投后管理',
    },
    {
        breadcrumbName: '公司治理',
    },
];

import { useRecordFileDel } from './components/recordFileDel';
const {
    delRecordFile
} = useRecordFileDel();

const {
    data,
    batchIds,
    rowSelection,
    batchDownLoad,
} = useBatchHandle();

const pageType   = ref('DOCUMENT_RECORD');
const menuId = ref(null)
const loadding   = ref(false);
const tableHeight = ref(0);
const tableReady  = (h)=>{
    tableHeight.value = h;
}
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
const columns = ref({
    'DOCUMENT_RECORD' : [
        {
            title        : '记录日期',
            dataIndex    : 'createTime',
            width        : 140,
            customRender : ({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '公司编号',
            dataIndex : 'companyBizNo',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '企业名称',
            dataIndex : 'companyName',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '会议标题',
            dataIndex : 'title',
            width     : 150,
        },
        {
            title     : '会议类型',
            dataIndex : 'typeStr',
            width     : 150,
        },
        {
          title     : '会议时间',
          dataIndex : 'meetingTime',
          width     : 180,
        },
        {
            title     : '文件类型',
            dataIndex : 'documentTypeStr',
            width     : 150,
        },
        {
            title : '文件内容',
            key   : 'documentTemplateList',
            width : 120,
        },
        {
            title : '操作',
            key   : 'action',
            width : 180,
            fixed : 'right'
        },

    ],
    'APPROVE_RECORD' : [
        {
            title        : '发起日期',
            dataIndex    : 'createTime',
            width        : 140,
            customRender : ({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '公司编号',
            dataIndex : 'companyBizNo',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '企业名称',
            dataIndex : 'companyName',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '会议标题',
            dataIndex : 'title',
            width     : 150,
        },
        {
            title     : '会议类型',
            dataIndex : 'typeStr',
            width     : 150,
        },
        {
          title     : '会议时间',
          dataIndex : 'meetingTime',
          width     : 180,
        },
        {
            title     : '记录人',
            dataIndex : ['createUser','realname'],
            width     : 180,
        },
        {
            title     : '内容描述',
            dataIndex : 'content',
            width     : 180,
            ellipsis  : true,
        },
        {
            title : '相关附件',
            key   : 'documentTemplateList',
            width : 120,
        },
        {
            title        : '审批结果',
            key          : 'approvalStatus',
            width        : 120,
            customRender : ({text})=>{
                return oaStatus.value[text.approvalStatus] || '草稿'
            }
        },
        {
            title : 'OA审批流程',
            key   : 'approvalUrl',
            width : 250,
        },
        {
            title : '操作',
            key   : 'action',
            width : 180,
            fixed : 'right'
        },

    ],
})

const apiKey = computed(()=>pageType.value=='APPROVE_RECORD'?'ompanyHighLevelMeetingApproval':'ompanyHighLevelMeeting');
const getPage = ()=>{
    let postData = builderFilter();
    loadding.value = true;
    api.investment.correlationPage(postData,apiKey.value,pageType.value=='APPROVE_RECORD'?53:52).then(res=>{
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
const dataExport = ()=>{
    let postData = builderFilter();
    store.spinChange(1);
    api.project.projectExport(postData).then(res=>{
        store.spinChange(-1);
        let timestamp = (new Date).getTime();
        dataToFile(res,'子公司-'+timestamp+'.xlsx');
    })
}
const builderFilter = ()=>{
    let postData = {
        desc          : [],
        pageNo        : filterForm.pageNo,
        pageSize      : filterForm.pageSize,
        params        : {},
        geParams      : {},
        leParams      : {},
        inParams      : {},
        likeParams    : {}
    }

    let params     = ['type'];
    let likeParams = ['company_companyBizNo','company_name','title'];
    let rangParams = [];

    if(pageType.value=='DOCUMENT_RECORD'){
        params.push('documentType')
        postData.desc = ['meeting.createTime']
        rangParams.push('meeting_meetingTime')
    }else{
        postData.desc = ['approval.createTime']
        rangParams.push('approval_meetingTime')
    }

    params.forEach(key => {
        if(filterForm[key]&&filterForm[key]!=-1){
            if(key.includes('_')){
                postData.params[key.replace('_','.')] = filterForm[key];
            }else{
                postData.params[key] = filterForm[key];
            }
        }
    });
    likeParams.forEach(key => {
        if(filterForm[key]){
            if(key.includes('_')){
                postData.likeParams[key.replace('_','.')] = filterForm[key];
            }else{
                postData.likeParams[key] = filterForm[key];
            }
        }
    });
    rangParams.forEach(key => {
        if(filterForm[key]&&filterForm[key].length==2){
            if(key.includes('_')){
                postData.geParams[key.replace('_','.')] = filterForm[key][0]+' 00:00:00';
                postData.leParams[key.replace('_','.')] = filterForm[key][1]+' 23:59:59';
            }else{
                postData.geParams[key] = filterForm[key][0]+' 00:00:00';
                postData.leParams[key] = filterForm[key][1]+' 23:59:59';
            }
        }
    });
    return postData;
}

const actions = (record,index)=>{
    return [
        {
            text  : '查看',
            show  : true,
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
            show  : hasPermission(['biz:projectCompany:delete']),
            click : ()=>{
                del(record);
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
const drawerDocRef     = ref(null);
const drawerApproveRef = ref(null);
const drawer           = (type,record)=>{
    menuId.value = pageType.value==='APPROVE_RECORD'?53:52
    if(pageType.value=='DOCUMENT_RECORD'){
        drawerDocRef.value.open(type,record || {});
    }
    if(pageType.value=='APPROVE_RECORD'){
        drawerApproveRef.value.open(type,record || {});
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
<script>export default{name:'homeInvestmentInvestment/governance'}</script>
<style scoped lang="less">

</style>
