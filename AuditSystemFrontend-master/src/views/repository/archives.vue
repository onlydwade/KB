<template>
    <div class="content-inner">
        <a-page-header :ghost="false"
            :breadcrumb="{ routes }">
            <template #footer>
                <a-tabs v-model:activeKey="pageType" @change="getTree">
                    <a-tab-pane v-for="(item,index) in dict.options('XIANG_MU_LEI_XING')" :key="item.value" :tab="item.label"/>
                </a-tabs>
            </template>
        </a-page-header>
        <div class="left_filter_content">
            <div class="left_box">
                <a-tabs v-model:activeKey="subType" @change="getTree" v-if="pageType=='GU_QUAN_HE_ZUO_XIANG_MU'" style="padding:0 16px;margin-bottom:-16px">
                    <a-tab-pane key="Project" tab="投前"/>
                    <a-tab-pane key="Company" tab="投后"/>
                </a-tabs>
                <a-tabs v-model:activeKey="thirdType" @change="getTree" v-else style="padding:0 16px;margin-bottom:-16px">
                    <a-tab-pane key="Before" tab="拓前"/>
                    <a-tab-pane key="After" tab="拓后"/>
                </a-tabs>

                <AScrollbar>
                    <DocTree v-if="pageType=='DAN_YI_FEI_TOU_BIAO_XIANG_MU' && thirdType=='Before'" v-model="documentId1" :treeData="documentTree1" @change="filterSubmit"/>
                    <DocTree v-if="pageType=='DAN_YI_TOU_BIAO_XIANG_MU' && thirdType=='Before'" v-model="documentId2" :treeData="documentTree2" @change="filterSubmit"/>

                    <DocTree v-if="pageType=='GU_QUAN_HE_ZUO_XIANG_MU' && subType=='Project'" v-model="documentId3" :treeData="documentTree3" @change="filterSubmit"/>
                    <DocTree v-if="pageType=='GU_QUAN_HE_ZUO_XIANG_MU' && subType=='Company'" v-model="documentId4" :treeData="documentTree4" @change="filterSubmit"/>

                    <DocTree v-if="pageType!='GU_QUAN_HE_ZUO_XIANG_MU' && thirdType=='After'" v-model="documentId5" :treeData="documentTree5" @change="filterSubmit"/>
                </AScrollbar>
            </div>

            <div class="right_content">
                <ArchivesFilter v-model="filterForm" @submit="filterSubmit" :subType="subType" :menuId="documentId4"/>
                <div class="content-box_full">
                    <Title title="档案列表">
                        <template #right>
                            <a-button type="primary" :disabled="batchIds.length==0" @click="batchDownLoad" v-permission="['biz:file:downloadBatch']">
                                批量下载
                            </a-button>
                        </template>
                    </Title>
                    <FullTableAuto @tableReady="tableReady">
                        <a-table :columns="columns[columnsKey]" :loading="loadding" :row-selection="rowSelection" :data-source="data.list" rowKey="id"  :pagination="false" :scroll="{y: tableHeight }">
                            <template #bodyCell="{ column,text, record,index }">
                                <template v-if="column.key === 'status'">
                                    <check-circle-outlined class="color-success"/>
                                </template>
                                <template v-if="column.key === 'docmentObject'">
                                    <FileItem :fileData="record.docmentObject" :readOnly="true"/>
                                </template>
                                <template v-if="column.key === 'approvalUrl'">
                                    <OaLink :link="record.approvalUrl"/>
                                </template>
                                <template v-if="column.key === 'documentTemplateList'">
                                    <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                                        <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                                    </template>
                                </template>
                                <template v-if="column.key === 'createTime'">
                                    <div class="">
                                        <span v-if="record.createTime">{{record.createTime}}</span>
                                        <span v-else>
                                            {{record.docmentObject.time?dateFormat(record.docmentObject.time):'-'}}
                                        </span>
                                    </div>
                                </template>
                                <template v-if="column.key === 'action'">
                                    <a-button type="text" class="color-primary" size="small" @click="downLoad(record)">下载</a-button>
                                </template>
                                <template v-if="column.key === 'actionOperation'">
                                    <a-button type="text" class="color-primary" size="small" @click="batchDownLoad(record,true)">下载</a-button>
                                </template>
                                <EllipsisTooltip v-if="column.ellipsis" :content="text"/>
                            </template>
                        </a-table>
                    </FullTableAuto>
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
            </div>
        </div>
    </div>
</template>
<script setup>
import api                       from '@/api/index';
import { dataToFile,dateFormat } from '@/utils/tools';
import { message }               from 'ant-design-vue';
import ArchivesFilter            from './components/ArchivesFilter.vue'
import DocTree                   from './components/DocTree.vue'
import { useBatchHandle }        from './components/batchHandle';
import { useDictStore }          from '@/store/dict';
import { mainStore }             from '@/store';
const store       = mainStore();
const dict        = useDictStore();
const loadding    = ref(false);
const tableHeight = ref(0);
const tableReady  = (h)=>{
    tableHeight.value = h;
}
const routes     = [
    {
        path: 'index',
        breadcrumbName: '首页',
    },
    {
        breadcrumbName: '档案管理',
    },
];

const {
    data,
    batchIds,
    rowSelection,
    batchDownLoad,
} = useBatchHandle();

const pageType   = ref('DAN_YI_FEI_TOU_BIAO_XIANG_MU');
const subType    = ref('Project');
const thirdType  = ref('Before');
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})
const columnsKey = computed(()=>{
    return (pageType.value=='GU_QUAN_HE_ZUO_XIANG_MU' && subType.value=='Company' && [52,53].includes(documentId4.value))?documentId4.value:'common';
})
const columns = ref({
    53     : [{
        title     : '记录日期',
        dataIndex : 'createTime',
        width     : 140,
        customRender:({text})=>{
            return dateFormat(text,'YYYY-MM-DD')
        }
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
        title     : '文件类型',
        dataIndex : 'documentTypeStr',
        width     : 150,
    },
    {
        title : '文件内容',
        key   : 'documentTemplateList',
        width : 360,
    },{
        title : '操作',
        key   : 'actionOperation',
        width : 60,
        fixed : 'right'
    }],
    52     : [{
        title     : '发起日期',
        dataIndex : 'createTime',
        width     : 140,
        customRender:({text})=>{
            return dateFormat(text,'YYYY-MM-DD')
        }
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
        width : 360,
    },
    {
        title        : '审批结果',
        key          : 'approvalStatus',
        width        : 120,
        customRender : ({text})=>{
            return oaStatus.value[text] || '草稿'
        }
    },{
        title : '操作',
        key   : 'actionOperation',
        width : 60,
        fixed : 'right'
    }],
    common : [
    {
        title     : computed(()=>subType.value=='Project' || pageType.value!='GU_QUAN_HE_ZUO_XIANG_MU'?'项目名称':'企业名称'),
        dataIndex : computed(()=>subType.value=='Project' || pageType.value!='GU_QUAN_HE_ZUO_XIANG_MU'?'projectName':'companyName'),
        width     : 200,
        ellipsis  : true,
    },
    {
        title     : '节点名称',
        dataIndex : 'stepMenuName',
        width     : 200,
        ellipsis  : true,
    },
    {
        title : '文件',
        key   : 'docmentObject',
        width : 480,
    },
    {
        title     : '上传人',
        dataIndex : ['createUser','realname'],
        width     : 180,
    },
    {
        title : '上传时间',
        key   : 'createTime',
        width : 170,
    },{
        title : '操作',
        key   : 'action',
        width : 60,
        fixed : 'right'
    }]
})
const getPage = async ()=>{
    batchIds.value = [];
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
    loadding.value = true;
    let res = null;

    if(thirdType.value=='After' && pageType.value!='GU_QUAN_HE_ZUO_XIANG_MU'){
        //拓后
         postData.params.projectType = pageType.value;

        postData.params.stepMenuId  = documentId5.value;

        if(filterForm.name){
            postData.likeParams.projectName = filterForm.name;
        }
        if(filterForm.stepMenuName){
            postData.likeParams.stepMenuName = filterForm.stepMenuName;
        }
        if(filterForm.documentName){
            postData.likeParams.documentName = filterForm.documentName;
        }
        if(filterForm.createTime&&filterForm.createTime.length==2){
            postData.geParams.createTime = filterForm.createTime[0]+' 00:00:00';
            postData.leParams.createTime = filterForm.createTime[1]+' 23:59:59';
        }

        res = await api.project.correlationPage(postData,'projectExpansionDocument');

    }else{
        //拓前   投前 投后  包含公司治理的带业务展示
        if(pageType.value=='DAN_YI_FEI_TOU_BIAO_XIANG_MU' || pageType.value=='DAN_YI_TOU_BIAO_XIANG_MU' || (pageType.value=='GU_QUAN_HE_ZUO_XIANG_MU' && subType.value=='Project')){
            postData.params.projectType = pageType.value;
            postData.params.stepMenuId  = pageType.value=='DAN_YI_FEI_TOU_BIAO_XIANG_MU'?documentId1.value:pageType.value=='DAN_YI_TOU_BIAO_XIANG_MU'?documentId2.value:documentId3.value;

            if(filterForm.name){
                postData.likeParams.projectName = filterForm.name;
            }
            if(filterForm.stepMenuName){
                postData.likeParams.stepMenuName = filterForm.stepMenuName;
            }
            if(filterForm.documentName){
                postData.likeParams.documentName = filterForm.documentName;
            }
            if(filterForm.createTime&&filterForm.createTime.length==2){
                postData.geParams.createTime = filterForm.createTime[0]+' 00:00:00';
                postData.leParams.createTime = filterForm.createTime[1]+' 23:59:59';
            }
            // res = await api.project.documentPageTwo(postData);
            res = await api.project.correlationPage(postData,'projectDocument');
        }else{
            if([52,53].includes(documentId4.value)){
                if(filterForm.type&&filterForm.type!=-1){
                    postData.params.type = filterForm.type;
                }
                if(documentId4.value==52&&filterForm.documentType&&filterForm.documentType!=-1){
                    postData.params.documentType = filterForm.documentType;
                }
                if(filterForm.name){
                    postData.likeParams['company.name'] = filterForm.name;
                }
                if(filterForm.title){
                    postData.likeParams.title = filterForm.title;
                }
                if(documentId4.value==52&&filterForm.createTime&&filterForm.createTime.length==2){
                    postData.geParams['meeting.createTime'] = filterForm.createTime[0]+' 00:00:00';
                    postData.leParams['meeting.createTime'] = filterForm.createTime[1]+' 23:59:59';
                }
                if(documentId4.value==53&&filterForm.createTime&&filterForm.createTime.length==2){
                    postData.geParams['approval.createTime'] = filterForm.createTime[0]+' 00:00:00';
                    postData.leParams['approval.createTime'] = filterForm.createTime[1]+' 23:59:59';
                }
                res = await api.investment.correlationPage(postData,documentId4.value==52?'ompanyHighLevelMeeting':'ompanyHighLevelMeetingApproval',documentId4.value);
            }else{
                postData.params['d.step_menu_id'] = documentId4.value;

                if(filterForm.name){
                    postData.likeParams['d.company_name'] = filterForm.name;
                }
                if(filterForm.stepMenuName){
                    postData.likeParams['d.step_menu_name'] = filterForm.stepMenuName;
                }
                if(filterForm.documentName){
                    postData.likeParams['d.document_name'] = filterForm.documentName;
                }
                if(filterForm.createTime&&filterForm.createTime.length==2){
                    postData.geParams['d.create_time'] = filterForm.createTime[0]+' 00:00:00';
                    postData.leParams['d.create_time'] = filterForm.createTime[1]+' 23:59:59';
                }

                res = await api.investment.correlationPage(postData,'companyDocument');
            }
        }
    }



    if(res.code==200){
        data.list  = res.data.records;
        data.total = res.data.total;
    }
    loadding.value = false;
}
const filterSubmit = ()=>{
    data.list         = [];
    filterForm.pageNo = 1;
    getPage();
}

const documentId1   = ref(null);
const documentId2   = ref(null);
const documentId3   = ref(null);
const documentId4   = ref(null);
const documentId5   = ref(null);
const documentTree1 = ref([]);
const documentTree2 = ref([]);
const documentTree3 = ref([]);
const documentTree4 = ref([]);
const documentTree5 = ref([]);
const getTree = ()=>{
    if(pageType.value=='DAN_YI_FEI_TOU_BIAO_XIANG_MU' && thirdType.value=='Before' && documentTree1.value.length==0){
        getProjectTree(documentTree1,documentId1);
        return;
    }
    if(pageType.value=='DAN_YI_TOU_BIAO_XIANG_MU' && thirdType.value=='Before' && documentTree2.value.length==0){
        getProjectTree(documentTree2,documentId2);
        return;
    }
    if(pageType.value=='GU_QUAN_HE_ZUO_XIANG_MU' && subType.value=='Project' && documentTree3.value.length==0){
        getProjectTree(documentTree3,documentId3);
        return;
    }
    if(pageType.value=='GU_QUAN_HE_ZUO_XIANG_MU' && subType.value=='Company' && documentTree4.value.length==0){
        getCompanyTree(documentTree4,documentId4);
        return;
    }
    if(pageType.value!='GU_QUAN_HE_ZUO_XIANG_MU' && thirdType.value=='After' && documentTree5.value.length==0){
        getProjectTreeAfter(documentTree5,documentId5);
        return;
    }
    filterSubmit();
}
const getProjectTree = (tree,menuId)=>{
    api.project.treeDocumentByType(pageType.value).then(res=>{
        if(res.code==200){
            const addKey = (treeData)=>{
                return treeData.map(item => ({
                    ...item,
                    disabled : item.children?.length>0 ? true : false,
                    children : item.children?addKey(item.children) : []
                }))
            }
            tree.value = addKey(res.data);
            menuId.value= getFirstId(tree.value[0]);
            filterSubmit();
        }
    })
}
const getProjectTreeAfter = (tree,menuId)=>{
    api.project.treeByProjectId(0).then(res=>{
        if(res.code==200){
            const addKey = (treeData)=>{
                return treeData.map(item => {
                    let isShow = (item.children || []).length==0 || item.children.filter(f=>f.isDocument==1).length>0
                    return {
                        ...item,
                        isShow   : isShow,
                        disabled : item.children?.length>0 ? true : false,
                        children : item.children?addKey(item.children.filter(f=>f.isDocument==1)) : []
                    }
                })
            }
            tree.value = addKey(res.data).filter(f=>f.isShow);
            menuId.value= getFirstId(tree.value[0]);
            filterSubmit();
        }
    })
}
const getCompanyTree = (tree,menuId)=>{
    api.investment.stepTree().then(res=>{
        if(res.code==200){
            const addKey = (treeData)=>{
                return treeData.map(item => ({
                    ...item,
                    disabled : item.children?.length>0 ? true : false,
                    children : item.children?addKey(item.children) : []
                }))
            }
            tree.value   = addKey(res.data.filter(item=>item.isShow==1));
            menuId.value = getFirstId(tree.value[0]);
            filterSubmit();
        }
    })
}
onMounted(() => {
    getTree();
})

const getFirstId = (item)=>{
    let documentId = null;
    function doArr(obj){
        if(obj.children&&obj.children.length>0){
            doArr(obj.children[0]);
        }else{
            documentId =  obj.id;
        }
    }
    doArr(item);
    return documentId;
}
const filePreview = (row)=>{
    api.common.tokenFile({fileId:row.id}).then(res=>{
        if(res.status&&res.status=='SUCCESS'){
            let fileToken = res.token;
            window.open(GLOBAL_PATH.oss + row.docmentObject.previewurl+'&f_token='+fileToken);
        }
    })
}
const downLoad = (row)=>{
    store.spinChange(1);

    let parseData = JSON.parse(row.docmentObject)
    let postData = {
        ossPath : parseData.ossPath,
        name    : parseData.name
    }
    api.common.downloadFile(postData).then(res=>{
        store.spinChange(-1);
        dataToFile(res,parseData.name);
    })
}

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
</script>
<style scoped lang="less">
.left_filter_content{
    flex    : 1;
    display : flex;
    height  : 0;
    .left_box{
        background-color : #fff;
        border-radius    : 8px;
        margin-right     : 16px;
        display          : flex;
        height           : 100%;
        flex-direction   : column;
    }
    .right_content{
        flex           : 1;
        width          : 0;
        height         : 100%;
        display        : flex;
        flex-direction : column;
    }
}
</style>
