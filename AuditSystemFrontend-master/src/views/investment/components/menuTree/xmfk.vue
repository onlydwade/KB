
<template>
    <div class="menu_inner">
        <Title title="项目付款记录">
            <template #right>
                <a-button type="primary" @click="drawer('ADD')" v-permissionInvestment="['biz:projectCompany:add']">
                    新增记录
                </a-button>
            </template>
        </Title>
        <a-alert :show-icon="false" banner style="margin-bottom:16px;">
            <template #message>
                统计：当前共出资 <span class="color-primary">{{summary.count || 0}} </span>次；出资总金额 <span class="color-primary">{{summary.amount || 0}}</span> 元。
            </template>
        </a-alert>
        <a-table :columns="data.columns" :loading="loadding" :data-source="data.list" rowKey="id"  :pagination="false" :scroll="{ x: '100%' }" class="ant-table-striped" :row-class-name="(_record, index) => (_record.type=='SHOU_CI_CHU_ZI' ? 'table-striped' : null)" >
            <template #bodyCell="{ column,text,record,index }">
                <template v-if="column.key === 'sort'">
                    {{(filterForm.pageNo-1)*filterForm.pageSize + index +1}}
                </template>
                <template v-if="column.key === 'action' && record.type!='SHOU_CI_CHU_ZI'">
                    <actionBtn  :actions="actions(record,index)"/>
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

    <XmfkDrawer ref="drawerRef" :companyId="companyId" :menuId="menuInfo.id" @success="getPage"/>
    <XmfkHandle ref="drawerRef2" :companyId="companyId" :menuId="menuInfo.id" @success="getPage"/>
</template>
<script setup>
import api                 from '@/api/index';
import XmfkDrawer          from '../correlation/XmfkDrawer.vue';
import XmfkHandle          from '../correlation/XmfkHandle.vue';
import { hasPermission,dateFormat }      from '@/utils/tools';
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

const loadding   = ref(false);
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})
const data = reactive({
    list : [],
    columns : [
        {
            title    : '序号',
            key      : 'sort',
            width    : 80,
        },
        {
            title     : '付款类型',
            dataIndex : 'typeStr',
            width     : 150,
        },
        {
            title     : '出资申请人',
            dataIndex : ['take','realname'],
            width     : 150,
        },
        {
            title     : '出资申请日期',
            dataIndex : 'applyTime',
            width     : 140,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '出资金额 (元)',
            dataIndex : 'amount',
            width     : 160,
        },
        {
            title     : '是否已处理',
            dataIndex : 'processStatus',
            width     : 120,
            customRender:({text, record, index, column})=>{
                return record.type === 'SHOU_CI_CHU_ZI'?'否':(text === 'SHI'?'是':'否')
            }
        },
        {
            title     : '处理描述',
            dataIndex : 'processDescription',
            width     : 250,
            ellipsis  : true,
        },
        {
            title     : '记录人',
            dataIndex : ['createUser','realname'],
            width     : 180,
        },
        {
            title     : '记录日期',
            dataIndex : 'createTime',
            width     : 140,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '处理人',
            dataIndex : ['processUser','realname'],
            width     : 180,
        },
        {
            title     : '处理日期',
            dataIndex : 'processTime',
            width     : 140,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title : '操作',
            key   : 'action',
            width : 200,
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
    api.investment.correlationPage(postData,'companyPayment',43).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })

    getTotal();
}
const summary = ref({});
const getTotal = ()=>{
    api.investment.companyPaymentTotal(props.companyId).then(res=>{
        if(res.code==200){
            summary.value = res.data || {};
        }
    })
}
const filterSubmit = ()=>{
    filterForm.pageNo = 1;
    getPage();
}
onMounted(() => {
    filterSubmit();
})
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
            disabled:record.type=='SHOU_CI_CHU_ZI',
            show  : record.type!='SHOU_CI_CHU_ZI' && hasPermission(['biz:projectCompany:edit'],'investment'),
            click : ()=>{
                drawer('EDIT',record);
            }
        },
        {
            text  : '标记已处理',
            disabled:record.type=='SHOU_CI_CHU_ZI',
            show  : record.processStatus!='SHI' && hasPermission(['biz:projectCompany:edit'],'investment'),
            click : ()=>{
                doneMark(record);
            }
        },
        {
            text  : '删除',
            disabled:record.type=='SHOU_CI_CHU_ZI',
            show  : record.type!='SHOU_CI_CHU_ZI' && hasPermission(['biz:projectCompany:delete'],'investment'),
            click : ()=>{
                del(record);
            }
        },
    ];
}

const drawerRef2 = ref(null);
const doneMark   = (data)=>{
    drawerRef2.value.open(data);
}
const del = (data)=>{
    Modal.confirm({
        title   : '操作确认',
        content : '是否确认删除该条付款记录？',
        async onOk() {
            if(data.documentTemplateList&&data.documentTemplateList.length>0){
                await delRecordFile(data.documentTemplateList);
            }
            api.investment.correlationDel(data.id,'companyPayment').then(res=>{
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
    let dfaultType = data.total==0?'SHOU_CI_CHU_ZI':'ZHUI_JIA_SHI_JIAO_ZHU_CE_ZI_BEN';
    drawerRef.value.open(type,record || {},dfaultType);
}
</script>
<style scoped lang="less">
.ant-table-striped :deep(.table-striped) td {
  background-color: #CDCDCD;
}

</style>
