
<template>
    <div class="menu_inner">
        <Title title="股权信息"></Title>
        <Shareholder :companyId="companyId"/>
        
        <Title title="股权变更记录" style="margin-top:16px">
            <template #right>
                <a-button type="primary" @click="drawer('ADD')" v-permissionInvestment="['biz:projectCompany:add']">
                    录入股权变更信息
                </a-button>
            </template>
        </Title>
        <a-table :columns="data.columns" :loading="loadding" :data-source="data.list" rowKey="id"  :pagination="false" :scroll="{ x: '100%' }">
            <template #bodyCell="{ column,text,record,index }">
                <template v-if="column.key === 'documentTemplateList'">
                    <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                        <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                    </template>
                </template>
                
                <template v-if="column.key === 'action'">
                    <a-button type="text" class="color-primary" size="small" @click="drawer('EDIT',record)" v-permissionInvestment="['biz:projectCompany:edit']">编辑</a-button>
                    <a-button type="text" class="color-primary" size="small" @click="del(record)" v-permissionInvestment="['biz:projectCompany:delete']">删除</a-button>
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
    
    <ShareholderChangeDrawer ref="drawerRef" :companyId="companyId" :menuId="menuInfo.id" @success="getPage"/>
</template>
<script setup>
import api                     from '@/api/index';
import Shareholder             from '../correlation/Shareholder.vue';
import ShareholderChangeDrawer from '../correlation/ShareholderChangeDrawer.vue';
import { dateFormat }          from '@/utils/tools';
import { message , Modal }     from 'ant-design-vue';

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

const loadding   = ref(false);
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})
const data = reactive({
    list : [],
    columns : [
        {
            title     : '变更日期',
            dataIndex : 'changeTime',
            width     : 140,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '变更项',
            dataIndex : 'changeTypeStr',
            width     : 200,
        },
        {
            title     : '变更前',
            dataIndex : 'changeBefore',
            width     : 200,
        },
        {
            title     : '变更后',
            dataIndex : 'changeAfter',
            width     : 200,
        },
        {
            title : '相关附件',
            key   : 'documentTemplateList',
            width : 360,
        },
        {
            title     : '变更人',
            dataIndex : ['changeUser','realname'],
            width     : 180,
        },
        {
            title : '操作',
            key   : 'action',
            width : 120,
            fixed : 'right'
        },
    ],
    total : 0,
})
const getPage = ()=>{
    let postData = {
        desc     : ['createTime'],
        pageNo   : filterForm.pageNo,
        pageSize : filterForm.pageSize,
        params   : {
            companyId : props.companyId
        }
    }
    loadding.value = true;
    api.investment.correlationPage(postData,'projectCompanyShareholderLog',props.menuInfo.id).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
const filterSubmit = ()=>{
    filterForm.pageNo = 1;
    getPage();
}
onMounted(() => {
    filterSubmit();
})

const del = (data)=>{
    Modal.confirm({
        title   : '操作确认',
        content : '是否确认删除该条股权变更记录？',
        async onOk() {
            if(data.documentTemplateList&&data.documentTemplateList.length>0){
                await delRecordFile(data.documentTemplateList);
            }
            api.investment.correlationDel(data.id,'projectCompanyShareholderLog').then(res=>{
                if(res.code==200){
                    getPage();
                }
            })
        }
    });
}
//drawer
const drawerRef = ref(null);
const drawer    = (type,record)=>{
    drawerRef.value.open(type,record || {});
}

</script>
<style scoped lang="less">

</style>
