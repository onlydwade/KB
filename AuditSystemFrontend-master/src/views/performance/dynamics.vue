<template>
    <div class="page-dynamics content-inner">
        <div class="filter-box">
            <a-space>
                <!-- <a-tree-select
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
                </a-tree-select> -->

                <a-tree-select
                          v-model:value="treeData.treeIds"
                          show-search
                          style="width:500px"
                          :dropdown-style="{ maxHeight: '500px', overflow: 'auto' }"
                          placeholder="请选择查询主体"
                          tree-default-expand-all
                          treeNodeFilterProp="name"
                          :dropdownStyle = '{
                             whiteSpace: "nowrap"
                          }'
                           @select="deptSelect"
                          :field-names="{
                              children: 'children',
                              label: 'name',
                              value: 'id',
                          }"
                          :tree-data="treeData.list"
                      >
                      </a-tree-select>


                <a-date-picker
                :allowClear="false"
                v-model:value="filterForm.year"
                picker="year"
                valueFormat="YYYY"
                format="YYYY"
                style="width:160px"/>
                <a-button type="primary" @click="filterSubmit" :disabled="treeData.treeIds  == null">查询</a-button>
                <a-button :disabled="treeData.treeIds  == null" @click="dataExport" v-permission="['biz:actualInAchievement:export']">导出</a-button>
            </a-space>
        </div>
        <div class="content-box_full">
            <template v-if="treeData.treeIds !=null">
                <Title title="业绩动态表"></Title>
                <FullTable :loadding="loadding" :pagination="false"  bordered :columns="columns" :dataSource="data.list">
                    <template #bodyCell="{ column,text,record,index }">
                        <template v-if="column.key === 'rate' && record.key  !== 'HTDNZHSR'">
                            <span >业绩达成率 </span>
                            <span class="color-primary">{{record.rate || '-'}}</span>
                            <span class="color-primary"> %</span>
                        </template>
                        <template v-if="column.key === 'rate' && record.key  === 'HTDNZHSR'">
                            <span >{{record.label}}</span>
                        </template>
                        <template v-if="column.key === 'value'">
                            {{amountFormat(record.value)}}
                        </template>
                    </template>
                </FullTable>
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
    treeIds  : 100,
    list     : [],
    level  : 1,
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
        treeData.treeIds = tree[0].id;
        treeData.level = tree[0].level;
        filterSubmit();
    }
}

const deptSelect = (val,option)=>{
    treeData.treeIds = option.id;
    treeData.level  = option.level;
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
    api.performance.actualInAchievementList(postData).then(res=>{
        if(res.code==200){
            tableDataBuild3(res.data);
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
        desc       : ['createTime'],
        pageNo     : filterForm.pageNo,
        pageSize   : filterForm.pageSize,
        deptId    : treeData.treeIds,
        level : treeData.level
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
    api.performance.actualInAchievementExport(postData).then(res=>{
        store.spinChange(-1);
        let timestamp = (new Date).getTime();
        dataToFile(res,'业绩动态表-'+timestamp+'.xlsx');
    })
}

const tableDataBuild = (res)=>{
    let rowSpan = [2,0,2,0,2,0,1];
    columns.value = [
        {
            title      : '费项',
            dataIndex  : 'name',
            width      : 200,
            fixed      : 'left',
            customCell: (_, index) => ({
                rowSpan : rowSpan[index]==0?0:(rowSpan[index] || 1)
            }),
        },
        {
            title : '统计',
            key   : 'rate',
            width : 180,
            fixed : 'left',
            customCell: (_, index) => ({
                rowSpan : rowSpan[index]==0?0:(rowSpan[index] || 1)
            }),
        },
        {
            title     : '类型',
            dataIndex : 'typeStr',
            width     : 140,
            fixed     : 'left',
        },
        {
            title : '全年',
            key   : 'value',
            width : 150,
            fixed : 'left',
        },
    ]
    res.months.forEach((item, i) => {
        columns.value.push({
            title  : item,
            key    : item,
            amount : true,
            width  : 130,
        },)
    });
    data.list = [];
    function doArr(arr,parent,attr){
        arr.forEach((item, i) => {
            let obj      = item;
            obj.name     = parent.label;
            obj.tableKey = item.key+'_'+i;
            obj.typeStr  = item.label;
            obj.rate     = parent.achievementRate;
            data[attr].push(obj);
        });
    }
    data.list = [];
    (res.achievementVOs || []).forEach((item, i) => {
        doArr(item.budgetDatas || [],item,'list');
    });
}

const tableDataBuild2 = (res)=>{
    let rowSpan = [2,0,2,0,2,0,1];
    columns.value = [
        {
            title      : '费项',
            dataIndex  : 'name',
            width      : 200,
            fixed      : 'left',
            customCell: (_, index) => ({
                rowSpan : rowSpan[index]==0?0:(rowSpan[index] || 1)
            }),
        },
        {
            title : '统计',
            key   : 'rate',
            width : 180,
            fixed : 'left',
            customCell: (_, index) => ({
                rowSpan : rowSpan[index]==0?0:(rowSpan[index] || 1)
            }),
        },
        {
            title     : '类型',
            dataIndex : 'typeStr',
            width     : 140,
            fixed     : 'left',
        },
        {
            title : '全年',
            key   : 'value',
            width : 150,
            fixed : 'left',
        },
    ]
    res.months.forEach((item, i) => {
        columns.value.push({
            title  : item,
            dataIndex  : item,
            amount : true,
            width  : 130,
            customRender : (text)=>{
                return amountFormat(text.text)
            }
        },)
    });

    data.list = [];

    function doArr(arr,attr){
        let obj = {}
        obj.name     = arr.label;
        obj.typeStr  = arr.cnType;
        obj.rate     = arr.percent;
        obj.value    = arr.value
        let monthList = arr.mouthList
        monthList.forEach((item)=>{
            obj[item.mouth] = item.value
        })
        data[attr].push(obj);
    }
    doArr(res.total, 'list')
    doArr(res.sjTotal, 'list')
    doArr(res.year, 'list')
    doArr(res.sjYear, 'list')
    doArr(res.tb, 'list')
    doArr(res.sjTb, 'list')
    doArr(res.sjChange, 'list')

}

const tableDataBuild3 = (res)=>{

    data.list = res;

    let rowSpan = []
    res.forEach((item, i) => {
        if(item.isMerge == '2'){
            rowSpan.push(2)
        }
        else if(item.isMerge == '1'){
            rowSpan.push(1)
        }else {
            rowSpan.push(0)
        }
    });

    console.log(rowSpan)

    columns.value = [
        {
            title      : '费项',
            dataIndex  : 'label',
            width      : 200,
            customCell: (_, index) => ({
                rowSpan : rowSpan[index]
            }),
        },
        {
            title : '统计',
            key   : 'rate',
            width : 180,
            customCell: (_, index) => ({
                rowSpan : rowSpan[index]
            }),
        },
        {
            title     : '类型',
            dataIndex : 'type',
            width     : 140,
        },
        {
            title : '全年',
            key   : 'value',
            width : 150,
        },
    ]



    // function doArr(arr,attr){
    //     let obj = {}
    //     obj.name     = arr.label;
    //     obj.typeStr  = arr.cnType;
    //     obj.rate     = arr.percent;
    //     obj.value    = arr.value
    //     data[attr].push(obj);
    // }
    // doArr(res.total, 'list')
    // doArr(res.sjTotal, 'list')
    // doArr(res.year, 'list')
    // doArr(res.sjYear, 'list')
    // doArr(res.tb, 'list')
    // doArr(res.sjTb, 'list')
    // doArr(res.sjChange, 'list')

}
</script>
<style scoped lang="less">
.page-dynamics{
    .filter-box{
        display         : flex;
        justify-content : space-between;
        align-items     : center;
        padding-bottom  : 16px;
    }
}
</style>
