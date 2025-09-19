
<template>
    <div class="menu_inner">
        <a-tabs v-model:activeKey="type" @change="filterSubmit">
            <a-tab-pane key="BAI_FANG_JI_LU" tab="拜访记录"></a-tab-pane>
            <a-tab-pane key="DIAO_YAN" tab="调研记录"></a-tab-pane>
            <a-tab-pane key="XIE_TONG_SHI_XIANG" tab="协同事项"></a-tab-pane>
            <template #rightExtra>
                <a-button type="primary" @click="drawer('ADD')" v-permissionInvestment="['biz:projectCompany:add']">
                    <template #icon>
                        <plus-outlined />
                    </template>
                    新建
                </a-button>
            </template>
        </a-tabs>
        
        <a-table :columns="data[type]" :loading="loadding" :data-source="data.list" rowKey="id" :pagination="false" :scroll="{ x: '100%' }"> 
            <template #bodyCell="{ column,text,record,index }">
                <template v-if="column.key === 'action'">
                    <a-button type="text" class="color-primary" size="small" @click="drawer('SHOW',record)">查看</a-button>
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
    
    <RcxtDrawer ref="drawerRef" :companyId="companyId" :menuId="menuInfo.id" @success="getPage"/>
</template>
<script setup>
import api                 from '@/api/index';
import RcxtDrawer          from '../correlation/RcxtDrawer.vue';
import { dateFormat }      from '@/utils/tools';
import { message , Modal } from 'ant-design-vue';

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
const type = ref('BAI_FANG_JI_LU')
const loadding   = ref(false);
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})
const data = reactive({
    list             : [],
    'BAI_FANG_JI_LU' : [
        {
            title     : '拜访日期',
            dataIndex : 'visitTime',
            width     : 140,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '标题',
            dataIndex : 'title',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '内容描述',
            dataIndex : 'content',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '拜访人员',
            dataIndex : 'interviewee',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '拜访地点',
            dataIndex : 'visitLocaltion',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '拜访方式',
            dataIndex : 'visitMethod',
            width     : 150,
        },
        {
            title : '操作',
            key   : 'action',
            width : 170,
            fixed : 'right'
        },
    ],
    'DIAO_YAN' : [
        {
            title     : '调研日期',
            dataIndex : 'visitTime',
            width     : 140,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '标题',
            dataIndex : 'title',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '内容描述',
            dataIndex : 'content',
            width     : 200,
            ellipsis  : true,
        },
        {
            title : '操作',
            key   : 'action',
            width : 170,
            fixed : 'right'
        },
    ],
    'XIE_TONG_SHI_XIANG' : [
        {
            title     : '记录日期',
            dataIndex : 'createTime',
            width     : 140,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '标题',
            dataIndex : 'title',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '内容描述',
            dataIndex : 'content',
            width     : 200,
            ellipsis  : true,
        },
        {
            title : '操作',
            key   : 'action',
            width : 170,
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
            companyId : props.companyId,
            type      : type.value
        }
    }
    loadding.value = true;
    api.investment.correlationPage(postData,'companyCoordination',42).then(res=>{
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
onMounted(() => {
    filterSubmit();
})

const del = (data)=>{
    Modal.confirm({
        title   : '操作确认',
        content : '是否确认删除该条记录？',
        async onOk() {
            if(data.documentTemplateList&&data.documentTemplateList.length>0){
                await delRecordFile(data.documentTemplateList);
            }
            api.investment.correlationDel(data.id,'companyCoordination').then(res=>{
                if(res.code==200){
                    getPage();
                }
            })
        }
    });
}
//drawer
const drawerRef = ref(null);
const drawer    = (editType,record)=>{
    drawerRef.value.open(editType,record || {},type.value);
}
</script>
<style scoped lang="less">

</style>
