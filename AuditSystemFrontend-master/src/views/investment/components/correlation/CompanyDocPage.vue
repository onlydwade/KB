
<template>
    <Title title="文档列表">
        <template #right>
            <a-space v-if="menuId==52 || menuId==53">
                <a-input allowClear style="width:200px" v-model:value="filterForm.documentName" placeholder="请输入会议标题搜索" />
                <a-range-picker 
                    v-model:value="filterForm.createTime"
                    valueFormat="YYYY-MM-DD"
                    format="YYYY-MM-DD"
                    style="width:240px"
                    :placeholder="['记录日期','范围筛选']" />
                <a-select
                  v-if="menuId==52"
                  v-model:value="filterForm.documentType"
                  style="width:130px"
                  placeholder="文件类型"
                  :options="dict.optionsAll('HUI_YI_WEN_DANG_LEI_XING')">
                </a-select>
                <a-select
                  v-model:value="filterForm.type"
                  class="w_full"
                  style="width:130px"
                  placeholder="会议类型"
                  :options="dict.optionsAll('SAN_HUI_HUI_YI_LEI_XING')">
                </a-select>
                <a-button type="primary" @click="filterSubmit">查询</a-button>
                <a-button @click="reset">重置</a-button>
            </a-space>
            <a-space v-else>
                <a-input allowClear style="width:200px" v-model:value="filterForm.documentName" placeholder="输入文件名称搜索" />
                <a-range-picker 
                    v-model:value="filterForm.createTime"
                    valueFormat="YYYY-MM-DD"
                    format="YYYY-MM-DD"
                    style="width:240px"
                    :placeholder="['上传日期','范围筛选']" />
                <a-button type="primary" @click="filterSubmit">查询</a-button>
            </a-space>
        </template>
    </Title>
    <a-table 
    :columns="columns[menuId] || columns.common" 
    :loading="loadding" 
    :data-source="data.list"
    rowKey="id" 
    :pagination="false"
     :scroll="{x:'100%'}">
        <template #bodyCell="{ column,text,record,index }">
            <template v-if="column.key === 'status'">
                <check-circle-outlined class="color-success"/>
            </template>
            <template v-if="column.key === 'docmentObject'">
                <FileItem :fileData="record.docmentObject" :readOnly="readOnly"/>
            </template>
            <template v-if="column.key === 'approvalUrl'">
                <OaLink :link="record.approvalUrl"/>
            </template>
            <template v-if="column.key === 'documentTemplateList'">
                <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                    <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="readOnly" />
                </template>
            </template>
            <EllipsisTooltip v-if="column.ellipsis" :content="text"/>
        </template>
    </a-table>
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
</template>
<script setup>
import api              from '@/api/index';
import { message }      from 'ant-design-vue';
import { dateFormat }   from '@/utils/tools';
import { useDictStore } from '@/store/dict';
const dict  = useDictStore();
const props = defineProps({
    companyId:{
        type    : Number,
        default : 0,
    },
    menuId: {
        type    : Number,
        default : 0,
    },
    readOnly: {
        type    : Boolean,
        default : true,
    },
})
const columns    = ref({
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
    }],
    common : [{
        title     : '节点',
        dataIndex : 'stepMenuName',
        width     : 200,
        ellipsis  : true,
    },
    {
        title     : '名称',
        dataIndex : 'documentName',
        width     : 200,
        ellipsis  : true,
    },
    {
        title : '文件',
        key   : 'docmentObject',
        width : 480,
    }]
})
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
    
})

const loadding  = ref(false);
const data = reactive({
    list  : [],
    total : 0,
})
const getPage   = async ()=>{
    if(!props.menuId){
        return;
    }
    loadding.value = true;
    let postData = {
        desc       : ['createTime'],
        pageNo     : filterForm.pageNo,
        pageSize   : filterForm.pageSize,
        params     : {},
        likeParams : {},
        geParams   : {},
        leParams   : {}
    }
    
    let res = null;
    if([52,53].includes(props.menuId)){
        postData.params.companyId =  props.companyId;
        if(filterForm.documentName){
            postData.likeParams.title = filterForm.documentName;
        }
        if(filterForm.type&&filterForm.type!=-1){
            postData.params.type = filterForm.type;
        }
        if(props.menuId==52 && filterForm.documentType && filterForm.documentType!=-1){
            postData.params.documentType = filterForm.documentType;
        }
        if(props.menuId==52&&filterForm.createTime&&filterForm.createTime.length==2){
            postData.geParams['meeting.createTime'] = filterForm.createTime[0]+' 00:00:00';
            postData.leParams['meeting.createTime'] = filterForm.createTime[1]+' 23:59:59';
        }
        if(props.menuId==53&&filterForm.createTime&&filterForm.createTime.length==2){
            postData.geParams['approval.createTime'] = filterForm.createTime[0]+' 00:00:00';
            postData.leParams['approval.createTime'] = filterForm.createTime[1]+' 23:59:59';
        }
        res = await api.investment.correlationPage(postData,props.menuId==52?'ompanyHighLevelMeeting':'ompanyHighLevelMeetingApproval',props.menuId);
    }else{
        postData.params['d.company_id']   = props.companyId;
        postData.params['d.step_menu_id'] = props.menuId;
        if(filterForm.documentName){
            postData.likeParams['d.document_name'] = filterForm.documentName;
        }
        if(filterForm.createTime&&filterForm.createTime.length==2){
            postData.geParams['d.createTime'] = filterForm.createTime[0]+' 00:00:00';
            postData.leParams['d.createTime'] = filterForm.createTime[1]+' 23:59:59';
        }
        
        res = await api.investment.documentPage(postData);
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

const reset = ()=>{
    filterForm.documentName = null;
    filterForm.createTime   = null;
    filterForm.documentType = null;
    filterForm.type         = null;
    filterSubmit();
}


watch(
    () => props.menuId,(newValue, oldValue) => {
        filterSubmit();
    }
)
const fileDel = (index,list,item)=>{
    api.project.delDocument(item.id).then(res=>{
        if(res.code==200){
            list.splice(index,1);
        }
    })
}
onMounted(() => {
    filterSubmit();
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
</script>
<style scoped lang="less">
.pagination_box{
    padding     : 0;
    padding-top : 16px;
}
</style>
