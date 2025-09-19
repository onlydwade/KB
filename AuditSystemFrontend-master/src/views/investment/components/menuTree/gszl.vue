<template>
    <div class="menu_inner">
        <a-tabs v-model:activeKey="pageType" @change="filterSubmit">
            <a-tab-pane key="DOCUMENT_RECORD" tab="“三会”文档记录"/>
            <a-tab-pane key="APPROVE_RECORD" tab="“三会”审批记录"/>
            <template #rightExtra>
                <a-space>
                    <a-select
                      v-if="pageType=='DOCUMENT_RECORD'"
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

                    <a-input allowClear style="width:150px" v-model:value="filterForm.title" placeholder="会议标题搜索" />
                    <a-range-picker
                        v-model:value="filterForm.meetingTime"
                        allowClear
                        valueFormat="YYYY-MM-DD"
                        format="YYYY-MM-DD"
                        style="width:240px"
                        :placeholder="['会议时间','范围筛选']" />

                    <a-button @click="filterSubmit">查询</a-button>
                    <a-button @click="reset">重置</a-button>
                    <a-button type="primary" @click="drawer('ADD')" v-permissionInvestment="['biz:projectCompany:add']">
                        <template #icon>
                            <plus-outlined />
                        </template>
                        {{pageType=='DOCUMENT_RECORD'?'新建':'发起三会审批'}}
                    </a-button>
                </a-space>
            </template>
        </a-tabs>
        <a-table :columns="data[pageType]" :loading="loadding" :data-source="data.list" rowKey="id" :pagination="false" :scroll="{ x: '100%' }">
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
        <div class="pagination_table">
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
    <threeMeetingDoc ref="drawerRecordRef" :companyId="companyId" :menuId="52" @success="getPage"/>
    <threeMeetingApprove ref="drawerApproveRef" :companyId="companyId" :menuId="53" @success="getPage"/>
</template>
<script setup>
import api                 from '@/api/index';
import threeMeetingDoc     from '../threeMeetingDoc.vue'
import threeMeetingApprove from '../threeMeetingApprove.vue'
import { hasPermission,dateFormat,hasFile }      from '@/utils/tools';
import { message , Modal } from 'ant-design-vue';
import { useDictStore }    from '@/store/dict';
const dict = useDictStore();

import { useRecordFileDel } from '../recordFileDel';
const {
    delRecordFile
} = useRecordFileDel();

const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    }
})
const pageType   = ref('DOCUMENT_RECORD');
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
    'DOCUMENT_RECORD' : [
        {
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
          title     : '会议时间',
          dataIndex : 'meetingTime',
          width     : 180,
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
            width : 200,
        },
        {
            title : '操作',
            key   : 'action',
            width : 180,
            fixed : 'right'
        },
    ],
    total : 0,
})
const apiKey = computed(()=>pageType.value=='APPROVE_RECORD'?'ompanyHighLevelMeetingApproval':'ompanyHighLevelMeeting');
const getPage = ()=>{
    let postData = {
        desc     : ['createTime'],
        pageNo   : filterForm.pageNo,
        pageSize : filterForm.pageSize,

        geParams      : {},
        leParams      : {},
        inParams      : {},
        likeParams    : {}
    }


    if(pageType.value=='DOCUMENT_RECORD'){
        if(filterForm.documentType && filterForm.documentType!=-1){
            postData.params['documentType'] = filterForm.documentType;
        }
        if(filterForm.meetingTime&&filterForm.meetingTime.length==2){
            postData.geParams['meeting.meetingTime'] = filterForm.meetingTime[0]+' 00:00:00';
            postData.leParams['meeting.meetingTime'] = filterForm.meetingTime[1]+' 23:59:59';
        }
      postData.params={"meeting.companyId":props.companyId}
    }else{
        if(filterForm.meetingTime&&filterForm.meetingTime.length==2){
            postData.geParams['approval.meetingTime'] = filterForm.meetingTime[0]+' 00:00:00';
            postData.leParams['approval.meetingTime'] = filterForm.meetingTime[1]+' 23:59:59';
        }
      postData.params={"approval.companyId":props.companyId}
    }
    if(filterForm.title){
        postData.likeParams['title'] = filterForm.title;
    }
    if(filterForm.type&&filterForm.type!=-1){
        postData.params['type'] = filterForm.type;
    }

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
const reset = ()=>{
    filterForm.documentType = null;
    filterForm.type         = null;
    filterForm.title        = null;
    filterForm.createTime   = null;
    filterForm.meetingTime   = null;
    filterSubmit();
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
            show  : hasPermission(['biz:projectCompany:edit'],'investment'),
            click : ()=>{
                drawer('EDIT',record);
            }
        },
        {
            text  : '删除',
            show  : hasPermission(['biz:projectCompany:delete'],'investment'),
            click : ()=>{
                del(record);
            }
        },
    ];
}
onMounted(() => {
    filterSubmit();
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
const companyName    = inject('getAutoParams')('name');
const drawerRecordRef = ref(null);
const drawerApproveRef = ref(null);
const drawer         = (editType,record)=>{
    if(pageType.value=='DOCUMENT_RECORD'){
        drawerRecordRef.value.open(editType,record || {},companyName.value);
    }
    if(pageType.value=='APPROVE_RECORD'){
        drawerApproveRef.value.open(editType,record || {},companyName.value);
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
<style scoped lang="less">

</style>
