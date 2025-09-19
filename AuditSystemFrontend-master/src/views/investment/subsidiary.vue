<template>
    <div class="content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a  href="#/investment">投后管理</a></a-breadcrumb-item>
                <a-breadcrumb-item>子公司信息库</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <subsidiaryFilter v-model="filterForm" @submit="filterSubmit" @dataExport="dataExport"/>
        <div class="content-box_full">
            <Title title="子公司列表"></Title>
            <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">
                <template #bodyCell="{ column,text,record,index }">
                    <template v-if="column.key === 'companyBizNo'">
                        <router-link :to="'/innerPage/subsidiaryInfo?id='+record.id" class="color-link">
                            {{record.companyBizNo}}
                        </router-link>
                    </template>
                    <template v-if="column.key === 'name'">
                        <router-link :to="'/innerPage/subsidiaryInfo?id='+record.id" class="color-link flex_box">
                          {{record.name}}
<!--                            <EllipsisTooltip class="flex_full" :content="record.name"/>-->
                        </router-link>
                    </template>
                    <template v-if="column.key === 'projectName'">
                        <router-link v-if="record.projectId" :to="'/innerPage/projectInfo?id='+record.projectId" class="color-link flex_box">
                            <EllipsisTooltip class="flex_full" :content="record.projectName"/>
                        </router-link>
                        <span v-else>-</span>
                    </template>
                    <template v-if="column.key === 'paidCapitalStatusStr'">
                     {{record.paidCapital>0?'是':'否'}}
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
    </div>
</template>
<script setup>
import api                                       from '@/api/index';
import { message }                               from 'ant-design-vue';
import subsidiaryFilter                          from './components/subsidiaryFilter.vue'
import { dataToFile , hasPermission , dateFormat } from '@/utils/tools';
import { mainStore }                             from '@/store';
const store  = mainStore();
const router = useRouter();

const loadding   = ref(false);
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})
const data = reactive({
    list : [],
    columns : [
        {
            title : '公司编号',
            key   : 'companyBizNo',
            width : 170,
        },
        {
            title : '企业名称',
            key   : 'name',
            width : 250,
        },
        {
            title     : '状态',
            dataIndex : 'serviceStatusStr',
            width     : 80,
        },
        {
            title     : '投资类型',
            dataIndex : 'investmentTypeStr',
            width     : 100,
        },
        {
            title     : '注册资本（万元)',
            dataIndex : 'registeredCapital',
            width     : 200,
        },
        {
            title     : '持股比例',
            dataIndex : 'shareholdingRatio',
            width     : 100,
            customRender:({text})=>{
                return (text || 0)+'%'
            }
        },
        {
            title     : '是否实缴',
            key : 'paidCapitalStatusStr',
            width     : 100,
        },
        {
            title     : '实缴资金（元）',
            dataIndex : 'paidCapital',
            width     : 140,
        },
        {
            title     : '省份',
            dataIndex : 'provinceName',
            width     : 140,
        },
        {
            title     : '投前项目归属人',
            dataIndex : ['attributorUser','realname'],
            width     : 180,
        },
        {
            title : '投前项目名称',
            key   : 'projectName',
            width : 170,
        },
        {
            title     : '投后负责人',
            dataIndex : ['principal','realname'],
            width     : 180,
        },
        {
            title     : '成立日期',
            dataIndex : 'incorporationTime',
            width     : 140,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title        : '创建时间',
            dataIndex    : 'createTime',
            width        : 180,
        },
        {
            title     : '最后修改人',
            dataIndex : ['updateUser','realname'],
            width     : 180,
        },
        {
            title : '操作',
            key   : 'action',
            width : 240,
            fixed : 'right'
        },

    ],
    total : 0,
})
const getPage = ()=>{
    let postData = builderFilter();
    loadding.value = true;
    api.investment.correlationPage(postData,'projectCompany').then(res=>{
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
const dataExport = ()=>{
    let postData = builderFilter();
    store.spinChange(1);
    api.investment.projectCompanyExport(postData).then(res=>{
        store.spinChange(-1);
        let timestamp = (new Date).getTime();
        dataToFile(res,'子公司-'+timestamp+'.xlsx');
    })
}
const builderFilter = ()=>{
    let postData = {
        desc          : ['createTime'],
        pageNo        : filterForm.pageNo,
        pageSize      : filterForm.pageSize,
        params        : {},
        geParams      : {
            'company.principalId' : 1
        },
        leParams      : {},
        inParams      : {},
        likeParams    : {}
    }

    let params     = ['company_principalId','company_serviceStatus','company_investmentType','company_attributorUserId','company_provinceCode','company_paidCapitalStatus'];
    let likeParams = ['company_name','company_companyBizNo','company_companyNo','company_projectName'];
    let rangParams = ['company_incorporationTime','company_shareholdingRatio'];
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
               if(filterForm[key][1]){
                postData.geParams[key.replace('_','.')] = filterForm[key][0];
                postData.leParams[key.replace('_','.')] = filterForm[key][1];
               }
            }else{
                postData.geParams[key] = filterForm[key][0];
                postData.leParams[key] = filterForm[key][1];
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
                router.push('/innerPage/subsidiaryInfo?id='+record.id);
            }
        },
        {
            text  : '投资进程回顾',
            show  : true,
            click : ()=>{
                router.push('/innerPage/projectInfo?id='+record.projectId+'&code=lxsp');
            }
        },
        {
            text  : '企业工商信息变更',
            show  : hasPermission(['biz:projectCompany:edit']),
            click : ()=>{
                router.push('/innerPage/subsidiaryEdit?id='+record.id);
            }
        },
        {
            text  : '项目退出',
            show  : hasPermission(['biz:projectCompany:changeExit']),
            click : ()=>{
                router.push('/innerPage/subsidiaryInfo?id='+record.id+'&code=xmtc');
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
</script>
<script>export default{name:'homeInvestmentInvestment/subsidiary'}</script>
<style scoped lang="less">

</style>
