<template>
    <div class="page-contract_summary content-inner">
        <div class="filter-box">
            <a-space>
                <a-tree-select
                    v-model:value="treeData.treeIds"
                    show-search
                    style="width:500px"
                    :dropdown-style="{ maxHeight: '500px', overflow: 'auto' }"
                    placeholder="请选择查询主体"
                    allow-clear
                    tree-checkable
                    maxTagCount="responsive"
                    show-checked-strategy="SHOW_PARENT"
                    :dropdownMatchSelectWidth="false"
                    tree-default-expand-all
                    :dropdownStyle = '{
                        whiteSpace: "nowrap"
                    }'
                    :field-names="{
                        children: 'children',
                        label: 'name',
                        value: 'id',
                    }"
                    :tree-data="treeData.list">
                </a-tree-select>
                <a-date-picker
                :allowClear="false"
                v-model:value="filterForm.year"
                picker="year"
                valueFormat="YYYY"
                :disabled="treeData.treeIds.length==0"
                format="YYYY"
                style="width:160px"/>
                <a-button type="primary" @click="filterSubmit" :disabled="treeData.treeIds.length==0">查询</a-button>
                <a-button @click="dataExport" v-permission="['biz:actualIn:export']">导出</a-button>
            </a-space>
        </div>
        <div class="content-box_full">
            <template v-if="treeData.treeIds.length>0">
                <Title title="合同收入列表">
                    <template #right>
                        <a-space>
                            <a-tag color="#f99c34" style="font-size:14px;padding:4px 6px;">当年有效信息提报量：{{data.submitTotal}}</a-tag>
                        </a-space>
                    </template>
                </Title>
                <FullTable :loadding="loadding" bordered :columns="columns" :dataSource="data.list">
                    <template #bodyCell="{ column,text,record,index }">
                        <template v-if="column.key === 'value'">
                            {{amountFormat(record.value)}}
                        </template>
                        <template v-if="column.amount">
                            {{amountFormat(record.datas[column.key])}}
                        </template>
                    </template>
                    <template #summary>
                        <a-table-summary-row v-for="(record,index) in data.summary">
                            <a-table-summary-cell :col-span="3" :index="0" :row-span="data.summary.length" v-if="index==0">合计</a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" :row-span="data.summary.length" v-if="index==0"></a-table-summary-cell>
                            <a-table-summary-cell>
                                {{ record.label }}
                            </a-table-summary-cell>
                            <a-table-summary-cell>
                                {{amountFormat(record.value)}}
                            </a-table-summary-cell>
                            <a-table-summary-cell v-for="(val,key) in record.datas">
                                {{amountFormat(val)}}
                            </a-table-summary-cell>
                        </a-table-summary-row>
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
            </template>
            <div class="empty padding_box" v-else>
                <a-empty description="请选择查询主体后开始查询"/>
            </div>
        </div>

    </div>
</template>
<script setup>
import api            from '@/api/index';
import moment         from 'moment';
import {amountFormat,dataToFile} from '@/utils/tools';
import { mainStore } from '@/store';
const store = mainStore();
const treeData = reactive({
    loadding : false,
    treeIds  : [],
    list     : [],
});
const getTree = async ()=>{
    treeData.loadding = true;
    let res = await api.performance.actualInTree();
    treeData.loadding = false;
    if(res.code==200&&res.data){
        let tree = [];
        tree = getDeptWithOutRoot([res.data]);
        // if(res.data.children && res.data.children.length>0){
        //     tree = getDeptWithOutRoot([res.data]);
        // }else{
        //     let dept = await api.sys.deptInfo(res.data.parentId);
        //     tree = [{
        //         id       : dept.data.deptId,
        //         name     : dept.data.deptName,
        //         level    : dept.data.level,
        //         parentId : dept.data.parentId,
        //         children : []
        //     }];
        // }

        treeData.list = tree;

        treeData.treeIds       = [tree[0].id];
        filterSubmit();
    }
}
const getDeptWithOutRoot = (tree)=>{
    let arr = [];
    function doArr(treeData,parentChild){
      if (treeData){
        treeData.forEach((item, i) => {
            let obj = {
                ...item,
                children : []
            }

            if(item.deptType === 'CENG_JI'|| (item.children&&item.children.length>0)){
                doArr(item.children,obj.children);
                parentChild.push(obj)
            }
        });
      }
    }
    doArr(tree,arr);
    return arr;
}

const loadding   = ref(false);
const filterForm = reactive({
    year     : moment(new Date).format('YYYY'),
    pageNo   : 1,
    pageSize : 10,
})
const columns = ref([]);
const data    = reactive({
    list        : [],
    summary     : [],
    submitTotal : 0,
    total       : 0,
})
const getPage = ()=>{
    let postData   = builderFilter();
    loadding.value = true;
    api.performance.actualInPage(postData).then(res=>{
        if(res.code==200){
            data.total = res.data.total;
            tableDataBuild(res.data.records || [])
        }
        loadding.value = false;
    })
}
const getTotal = (callBack)=>{
    let postData   = builderFilter();
    api.performance.actualInTotal(postData).then(res=>{
        if(res.code==200){
            data.summary = res.data;
            tableColumnsBuild(res.data);
        }
        callBack&&callBack();
    })
    api.performance.actualInSubmitTotal(postData).then(res=>{
        if(res.code==200){
            let count =  0;
            res.data.forEach((item, i) => {
                count += item.count;
            });
            data.submitTotal = count;
        }
    })
}
const filterSubmit = ()=>{
    filterForm.pageNo = 1;

    getTotal(()=>{
        getPage();
    });
}
const builderFilter = ()=>{
    let postData = {
        desc       : ['createTime'],
        pageNo     : filterForm.pageNo,
        pageSize   : filterForm.pageSize,
        deptIds    : treeData.treeIds,
    }
    postData.start = filterForm.year + '-01-01 00:00:00';
    postData.end   = filterForm.year + '-12-31 23:59:59';
    return postData;
}

onMounted(() => {
    getTree();
})

const dataExport = ()=>{
    let postData = builderFilter();
    store.spinChange(1);
    api.performance.actualInExport(postData).then(res=>{
        store.spinChange(-1);
        let timestamp = (new Date).getTime();
        dataToFile(res,'实际签约汇总-'+timestamp+'.xlsx');
    })
}

const tableDataBuild = (resData)=>{
    data.list = [];
    resData.forEach((item, i) => {
        item.datas.forEach((subItem, k) => {
            let obj = {}
            for (let i in subItem.list) {
                obj[i] = subItem.list[i].value
            }
            data.list.push({
                id               : item.projectId+'_'+i,
                regionName       : item.regionName,
                companyName      : item.companyName,
                projectName      : item.projectName,
                signDate         : item.signDate,
                serviceDate      : item.serviceDate,
                serviceMonth     : item.serviceMonth,
                enterTime        : item.enterTime,
                constructionArea : item.constructionArea,
                label            : subItem.label,
                value            : subItem.value,
                datas            : obj
            })
        });
    });
}
const tableColumnsBuild = (resData)=>{
    let rowSpan = (resData || []).length || 1;
    columns.value = [
        {
            title      : '大区',
            dataIndex  : 'regionName',
            width      : 180,
            fixed      : 'left',
            customCell : (_, index) => ({
                rowSpan : index%rowSpan==0 ? rowSpan : 0,
            }),
        },
        {
            title      : '单位',
            dataIndex  : 'companyName',
            width      : 180,
            fixed      : 'left',
            customCell : (_, index) => ({
                rowSpan : index%rowSpan==0 ? rowSpan : 0,
            }),
        },
        {
            title      : '项目',
            dataIndex  : 'projectName',
            width      : 180,
            fixed      : 'left',
            customCell : (_, index) => ({
                rowSpan : index%rowSpan==0 ? rowSpan : 0,
            }),
        },
        {
            title      : '合同签约时间',
            dataIndex  : 'signDate',
            width      : 190,
            customCell : (_, index) => ({
                rowSpan : index%rowSpan==0 ? rowSpan : 0,
            }),
        },
        {
            title      : '合同服务周期',
            dataIndex  : 'serviceDate',
            width      : 190,
            customCell : (_, index) => ({
                rowSpan : index%rowSpan==0 ? rowSpan : 0,
            }),
        },
        {
            title      : '拟服务期限',
            dataIndex  : 'serviceMonth',
            width      : 190,
            customCell : (_, index) => ({
                rowSpan : index%rowSpan==0 ? rowSpan : 0,
            }),
        },
        {
            title      : '实际进场时间',
            dataIndex  : 'enterTime',
            width      : 190,
            customCell : (_, index) => ({
                rowSpan : index%rowSpan==0 ? rowSpan : 0,
            }),
        },
        {
            title      : '建筑面积 (㎡)',
            dataIndex  : 'constructionArea',
            width      : 190,
            customCell : (_, index) => ({
                rowSpan : index%rowSpan==0 ? rowSpan : 0,
            }),
        },
        {
            title     : '实际值',
            dataIndex : 'label',
            width     : 200,
        },
        {
            title : '小计',
            key   : 'value',
            width : 180,
        },
    ]
    for (let i in resData[0].datas) {
        columns.value.push({
            title  : i,
            key    : i,
            amount : true,
            width  : 150,
        })
    }
}
</script>
<style scoped lang="less">
.page-contract_summary{
    .filter-box{
        display         : flex;
        justify-content : space-between;
        align-items     : center;
        padding-bottom  : 16px;
    }
}
</style>
