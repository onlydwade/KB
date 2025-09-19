
<template>
    <a-modal v-model:visible="visible" title="财务报告选择" destroyOnClose width="1024px">
        <a-tabs v-model:activeKey="pageType" @change="filterSubmit" style="margin-top:-16px;">
            <a-tab-pane :key="57" tab="资产负债表"/>
            <a-tab-pane :key="58" tab="利润信息表"/>
            <a-tab-pane :key="59" tab="现金流信息表"/>
            <template #rightExtra>
                <a-space>
                    <a-input allowClear style="width:200px" v-model:value="filterForm.name" placeholder="请输入报告名称搜索" />
                    <a-range-picker v-model:value="filterForm.months" picker="month" :placeholder="['报告月份','范围筛选']"/>
                    <a-button @click="filterSubmit" type="primary">查询</a-button>
                </a-space>
            </template>
        </a-tabs>
        <a-table style="margin-bottom:24px;" :columns="columns" :loading="loadding" :data-source="data.list" :row-selection="rowSelection" rowKey="id" :pagination="false" :scroll="{ x: '100%',y:400 }">
            <template #bodyCell="{ column,text,record,index }">
                <template v-if="column.key === 'documentTemplateList'">
                    <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                        <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                    </template>
                </template>
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
        <template #footer>
            <a-button key="back" @click="batchIds=[]">重置</a-button>
            <a-button key="submit" type="primary" @click="handleOk"  :disabled="batchIds.length==0">确认</a-button>
      </template>
    </a-modal>
</template>
<script setup>
import api                from '@/api/index';
import { useBatchHandle } from './batchHandle';
const emit     = defineEmits(['select'])
const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
})
const visible  = ref(false);
const pageType = ref(57);
const {
    data,
    batchIds,
    rowSelection,
    batchDownLoad,
} = useBatchHandle();
const loadding   = ref(false);
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 5,
    months   : []
})
const column  = { xxl: 3, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 };
const columns = ref([
    {
        title     : '名称',
        dataIndex : 'name',
        width     : 360,
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
        postData.params.name = filterForm.name;
    }
    if(filterForm.months&&filterForm.months.length==2){
        postData.geParams.createTime = filterForm.months[0]+' 00:00:00';
        postData.leParams.createTime = filterForm.months[1]+' 23:59:59';
    }
    
    loadding.value = true;
    api.investment.correlationPage(postData,'companyAppraise',pageType.value).then(res=>{
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

const handleOk = ()=>{
    let files = [];
    let arr   = data.list.filter(item=>batchIds.value.includes(item.id));
    files     = arr.reduce((acc, curr) => acc.concat(curr.documentTemplateList),[]).reduce((acc, curr) => acc.concat(curr.projectCompanyDocumentList),[])
    emit('select',files);
    batchIds.value = [];
    visible.value  = false;
}
const open     = (data,type,index)=>{
    filterSubmit();
    visible.value = true;
}
defineExpose({open})
</script>
<style scoped lang="less">
</style>
