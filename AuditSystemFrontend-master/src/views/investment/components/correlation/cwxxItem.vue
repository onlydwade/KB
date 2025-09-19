<template>
    <Title :title="cwObj.title"></Title>
    <a-descriptions :column="column">
        <a-descriptions-item v-for="(item,index) in cwObj.form" :label="item.label">{{reportData[item.key] || '-'}}</a-descriptions-item>
    </a-descriptions>
    <Title class="title_sub">
        <span class="sub_title">{{cwObj.title}}附件</span>
        <template #right>
            <a-space>
                <a-input allowClear style="width:200px" v-model:value="filterForm.name" placeholder="请输入报告名称搜索" />
                <a-range-picker v-model:value="filterForm.months" valueFormat="YYYY-MM" picker="month" :placeholder="['报告月份','范围筛选']"/>
                <a-button @click="filterSubmit" type="primary">查询</a-button>
                <a-button @click="batchDownLoad" :disabled="batchIds.length==0" v-permission="['biz:file:downloadBatch']">批量下载报告</a-button>
            </a-space>
        </template>
    </Title>
    <a-table :columns="columns" :loading="loadding" :data-source="data.list" :row-selection="rowSelection" rowKey="id" :pagination="false" :scroll="{ x: '100%' }">
        <template #bodyCell="{ column,text,record,index }">
            <template v-if="column.key === 'name'">
                {{companyName}}{{dateFormat(record.beginTime,'YYYY年MM月')}}-{{dateFormat(record.endTime,'YYYY年MM月')}}累计{{cwObj.title}}
            </template>
            <template v-if="column.key === 'documentTemplateList'">
                <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                    <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                </template>
            </template>
        </template>
    </a-table>
    <div class="pagination_box" style="margin:16px 0 24px 0;">
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
import api                from '@/api/index';
import { useBatchHandle } from '../batchHandle';
import { dateFormat } from '@/utils/tools';
const props = defineProps({
    reportData:{
        type    : Object,
        default : {},
    },
    companyId : {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 41,
    }
})
const cwObj = computed(()=>{
    return ({
        57:{
            title : '资产负债信息',
            key   : 'companyFinanceAssets',
            form  : [{label:'总资产（万元）',key:'totalAssets'},{label:'净资产（万元）',key:'netAssets'}]
        },
        58:{
            title : '利润信息',
            key   : 'companyFinanceProfit',
            form  : [{label:'利润总额（万元）',key:'totalProfit'},{label:'净利润（万元）',key:'netProfit'},{label:'营业收入本年累计',key:'accumulatedOperating'}]
        },
        59:{
            title : '现金流信息',
            key   : 'companyFinanceCashFlow',
            form  : [{label:'期初现金与现金等价物余额（万元）',key:'beginCashFlow'},{label:'期末现金与现金等价物余额（万元）',key:'endCashFlow'}]
        }
    })[props.menuId] || {}
})
const {
    data,
    batchIds,
    rowSelection,
    batchDownLoad,
} = useBatchHandle();

const bus        = inject('bus');
const loadding   = ref(false);
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 5,
    months   : []
})
const companyName = inject('getAutoParams')('name');
const column  = { xxl: 3, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 };
const columns = ref([
    {
        title : '名称',
        key   : 'name',
        width : 360,
    },
    {
        title : '文件',
        key   : 'documentTemplateList',
        width : 360,
    },
])
const getPage = ()=>{
    let postData = {
        desc       : ['createTime'],
        pageNo     : filterForm.pageNo,
        pageSize   : filterForm.pageSize,
        params     : {
            companyId : props.companyId
        },
        geParams   : {},
        leParams   : {},
        inParams   : {},
        likeParams : {}
    }
    if(filterForm.name){
        postData.likeParams.name = filterForm.name;
    }
    if(filterForm.months&&filterForm.months.length==2){
        postData.geParams.createTime = filterForm.months[0]+'-01 00:00:00';
        let year = filterForm.months[1].split('-')[0];
        let month = filterForm.months[1].split('-')[1];
        postData.leParams.createTime = filterForm.months[1]+ '-' + getMonthLastDay(year,month)+ ' 23:59:59';
    }

    loadding.value = true;
    api.investment.correlationPage(postData,cwObj.value.key,props.menuId).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
const getMonthLastDay = (year,month)=>{
  let dateObj = new Date(year,month,0);

  let theMonthDay = dateObj.getDate();
  return theMonthDay;
}
const filterSubmit = ()=>{
    filterForm.pageNo = 1;
    getPage();
}
onMounted(() => {
    getPage();
    bus.on('reloadPageData',filterSubmit);
})
onUnmounted(() => {
    bus.off('reloadPageData',filterSubmit);
})
defineExpose({filterSubmit})
</script>
<style scoped lang="less">
.title_sub{
    padding-top: 0!important;
}
.sub_title{
    font-size   : 14px;
    margin-left : -16px;
    font-weight : bold;
}
</style>
