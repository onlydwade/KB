<template>
    <div class="content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a  href="#/investment">投后管理</a></a-breadcrumb-item>
                <a-breadcrumb-item>经营报告</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <operationalFilter v-model="filterForm" @submit="filterSubmit"/>
        <div class="content-box_full">
            <Title title="经营报告列表">
                <template #right>
                    <a-space>
                        <a-button @click="batchDownLoad" :disabled="batchIds.length==0" v-permission="['biz:file:downloadBatch']">批量下载</a-button>
                        <a-button type="primary" @click="drawer('ADD')" v-permission="['biz:projectCompany:add']">
                            <template #icon>
                                <plus-outlined />
                            </template>
                            新增经营报告
                        </a-button>
                    </a-space>
                </template>
            </Title>
            <FullTableAuto @tableReady="tableReady">
                <a-table :columns="columns" :loading="loadding" :data-source="data.list" :row-selection="rowSelection" rowKey="id"  :pagination="false" :scroll="{y: tableHeight }">
                    <template #bodyCell="{ column,text,record,index }">
                        <template v-if="column.key === 'companyBizNo'">
                            <router-link :to="'/innerPage/subsidiaryInfo?id='+record.companyId" class="color-link">
                                {{record.companyBizNo || '-'}}
                            </router-link>
                        </template>
                        <template v-if="column.key === 'cycle'">
                            {{record.cycleTypeStr}}-{{record.cycleStr}}
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

        <operationalDrawer ref="drawerRef"  @success="getPage"/>
        <pdfPreView :formData="formData"  ref="pdfPreViewRef"></pdfPreView>
    </div>
</template>
<script setup>
import api                                       from '@/api/index';
import { message }                               from 'ant-design-vue';
import operationalFilter                         from './components/operationalFilter.vue'
import operationalDrawer                         from './components/operationalDrawer.vue'
import { useBatchHandle }                        from './components/batchHandle';
import { dataToFile , hasPermission,dateFormat,hasFile } from '@/utils/tools';
import { mainStore }                             from '@/store';
import pdfPreView from "./components/pdfPreView.vue";
const store       = mainStore();
const router      = useRouter();

const {
    data,
    batchIds,
    rowSelection,
    batchDownLoad,
} = useBatchHandle();

const loadding    = ref(false);
const tableHeight = ref(0);
const tableReady  = (h)=>{
    tableHeight.value = h;
}
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})

const columns = ref([
    {
        title : '公司编号',
        key   : 'companyBizNo',
        width : 170,
    },
    {
        title     : '企业名称',
        dataIndex : 'companyName',
        width     : 170,
        ellipsis  : true,
    },
    {
        title     : '报告名称',
        dataIndex : 'name',
        width     : 170,
        ellipsis  : true,
    },
    {
        title     : '报告所属年份',
        dataIndex : 'year',
        width     : 170,
    },
    {
        title : '报告期',
        key   : 'cycle',
        width : 170,
    },
    {
        title     : '报告方式',
        dataIndex : 'reportMethodStr',
        width     : 170,
    },
    {
        title : '文件',
        key   : 'documentTemplateList',
        width : 120,
    },
    {
        title     : '创建人',
        dataIndex : ['createUser','realname'],
        width     : 180,
    },
    {
        title        : '创建时间',
        dataIndex    : 'createTime',
        width        : 180,
    },
    {
        title : '操作',
        key   : 'action',
        width : 200,
        fixed : 'right'
    },
])
const getPage = ()=>{
    let postData = builderFilter();
    loadding.value = true;
    api.investment.correlationPage(postData,'companyOperateReport',44).then(res=>{
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
const builderFilter = ()=>{
    let postData = {
        desc          : ['createTime'],
        pageNo        : filterForm.pageNo,
        pageSize      : filterForm.pageSize,
        params        : {},
        geParams      : {},
        leParams      : {},
        inParams      : {},
        likeParams    : {}
    }

    let params     = ['year','cycleType','cycle'];
    let likeParams = ['company_name','report_name','companyName'];
    params.forEach(key => {
        if(filterForm[key]&&filterForm[key]!=-1){
            postData.params[key] = filterForm[key];
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
    return postData;
}

const actions = (record,index)=>{
    return [
        {
            text  : '查看详情',
            show  : true,
            click : ()=>{
                drawer('SHOW',record);
            }
        },
        {
            text  : '预览',
            show  : true,
            click : ()=>{
              pdfOpen(record);
            }
        },
        {
            text  : '编辑',
            show  : hasPermission(['biz:projectCompany:edit']),
            click : ()=>{
                drawer('EDIT',record);
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

//drawer
const drawerRef = ref(null);
const drawer    = (type,record)=>{
    drawerRef.value.open(type,record || {});
}
const pdfPreViewRef = ref(null)
const formData = ref({})
const pdfOpen = (record) => {
  formData.value = record
  pdfPreViewRef.value.open()
}
</script>
<script>export default{name:'homeInvestmentInvestment/operational'}</script>
<style scoped lang="less">

</style>
