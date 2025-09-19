
<template>
    <div class="budget_content">
        <div class="left_tree_box">
            <AScrollbar class="left_filter">
                <a-spin :spinning="treeData.loadding">
                    <div class="padding_box" style="padding-left:4px;">
                        <a-tree
                            v-if="treeData.list.length>0"
                            showLine
                            defaultExpandAll
                            v-model:selectedKeys="treeData.treeId"
                            selectable
                            @select="treeSelect"
                            :field-names="{
                                children: 'children',
                                title: 'name',
                                key: 'id',
                            }"
                            :tree-data="treeData.list">
                            <template #title="item">
                                <div class="tree_node">
                                    <div class="name">
                                        <EllipsisTooltip class="flex_full" :content="item.name"/>
                                    </div>
                                </div>
                            </template>
                        </a-tree>
                        <a-empty v-if="!treeData.loadding&&treeData.list.length==0"/>
                    </div>
                </a-spin>
            </AScrollbar>
        </div>
        <div class="right_content">
            <div class="filter-box">
                <h3>{{treeData.treeSelected.name}}</h3>
                <a-space>
                    <a-date-picker
                    :allowClear="false"
                    v-model:value="data.year"
                    :disabled="treeData.treeSelected.id==0"
                    picker="year"
                    @change="getTableData()"
                    valueFormat="YYYY"
                    format="YYYY"
                    style="width:160px"/>
                    <a-button @click="showLog" :disabled="treeData.treeSelected.id==0">查看变更记录</a-button>
                </a-space>
            </div>
            <div class="content-box_full" v-if="treeData.list.length>0">
                <Title :title="levels(treeData.treeSelected.level)+'年度业绩目标'">
                  <template #right>
                    <!-- <a-button style="margin-right:6px;" class="color-danger"  @click="calc()"  v-if="treeData.treeSelected.level == 1" v-permission="['biz:performance:config']">
                        考核计算
                    </a-button> -->

                    <a-button style="margin-right:6px;"  @click="showMethodConfig()"  v-if="treeData.treeSelected.level == 1" v-permission="['biz:performance:config']">
                        考核指标统计方式配置
                    </a-button>
                    <a-button  @click="showConfig()"  v-if="treeData.treeSelected.level == 1" v-permission="['biz:performance:config']">
                        考核指标项配置
                    </a-button>
                </template>
                </Title>
                <div class="table_box" style="overflow: auto;">
                <table class="target-table"   style="width: 100%; min-width: 100%; table-layout: fixed;" > 
                <thead class="ant-table-thead">
                    <tr>  
                        <th class="ant-table-cell"  style="width: 170px;">

                            <span>层级</span>
                        </th>
                        <th class="ant-table-cell" style="width: 170px;">

                            <span>考核项</span>
                        </th>        
                        <th style="width: 130px;" class="ant-table-cell" v-for="header in headers" :key="header.code">
                                {{ header.name }}
                        </th>   
                    </tr>  
                    </thead>  
                    <tbody class="ant-table-tbody">  
                    <tr class="ant-table-row" v-for="(row, rowIndex) in rows" :key="rowIndex">  
                        <td class="ant-table-cell" v-if="rowIndex==0" :rowspan="rowCount" style="width: 170px;" >

                            <a-button type="text" class="color-primary" size="small" key="budgetDrawerKey"  @click="edit(row)">{{treeData.treeSelected.name}}</a-button>
                        </td>
                        <td style="width: 170px;" class="ant-table-cell" >{{row.label}}</td>  
                        <td style="width: 130px;" class="ant-table-cell"  v-for="(data, dataIndex) in row.dataList" :key="dataIndex">                            
                            {{data.amount==null?"-":data.amount}} 
                        </td>                    
                    </tr>  
                    </tbody>  
                 </table>  
        
                </div> 
                <!--    <a-table bordered :columns="columns" :loading="data.loadding" :data-source="data.list" rowKey="tableKey" :pagination="false" :scroll="{x:'100%'}">
                        <template #bodyCell="{ column,record,index }">
                            <template v-if="column.key === 'name'">
                                <a-button type="text" class="color-primary" size="small" @click="edit(record)">{{record.name}}</a-button>
                            </template>
                            <template v-if="column.key === 'value'">
                                {{amountFormat(record.value)}}
                            </template>
                            <template v-if="column.amount">
                                {{amountFormat(record.list[column.key].value)}}
                            </template>
                        </template>
        </a-table>-->

            </div>
            <div class="content-box_full" v-else>
                <a-empty style="padding-top:60px;"/>
            </div>
        </div>
        <BudgetDrawerIndicatorData ref="budgetDrawerIndicatorDataRef" title="业绩目标设置" type="In"  :companyName="treeData.treeSelected.name"  :recordId="treeData.treeSelected.id" :key="budgetDrawerKey" @success="getTableData"/>
        <ChangeLog ref="changeLogRef" moduleName="BudgetIn" :level="treeData.treeSelected.level" :recordId="treeData.treeSelected.id"/>
        <assessmentConfigDrawer ref="assessmentConfigRef"  @success="getTargetIndicatorDataList"/>
        <StatisticalMethodConfig ref="statisticalMethodConfigRef" :budgetYear="data.year"   @success="getTargetIndicatorDataList"/>
    </div>
</template>
<script setup>
import api            from '@/api/index';
import moment         from 'moment';
import {amountFormat} from '@/utils/tools';
import BudgetDrawerIndicatorData   from './components/BudgetDrawerIndicatorData.vue';
import ChangeLog      from './components/ChangeLog.vue';
import assessmentConfigDrawer      from './components/assessmentConfigDrawer.vue';
import StatisticalMethodConfig      from './components/StatisticalMethodConfig.vue';
import { message, Modal } from "ant-design-vue";
const treeData = reactive({
    loadding     : false,
    treeId       : [],
    treeSelected : {
        id    : 0,
        level : 1,
        name  : '-'
    },
    list    : [],
});




const levels = (level,sub)=>{
    let str = sub?'下级单位':'单位'
    switch (level) {
        case 1:
            str = '总部';
            break;
            case 2:
                str = '大区';
                break;
                case 3:
                    str = '单位';
                    break;
        default:
    }
    return str;
}
const getTree = async ()=>{
    treeData.loadding = true;
    let res = await api.performance.budgetInTree();

    treeData.loadding = false;
    if(res.code==200&&res.data){
        let tree = [];
        tree = getDeptWithOutRoot([res.data]);
        treeData.list = tree;
        treeData.treeId       = [tree[0].id];
        treeData.treeSelected = {
            id    : tree[0].id,
            level : tree[0].level,
            name  : tree[0].name
        }
        // getList();
        getconfigList();
        getTargetIndicatorDataList();
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
const treeSelect = (selectedKeys,selectedRows)=>{
    if(selectedKeys.length==0){
        return;
    }
    treeData.treeSelected = {
        id    : selectedRows.selectedNodes[0].id,
        level : selectedRows.selectedNodes[0].level,
        name  : selectedRows.selectedNodes[0].name
    }
    //getList();
    getconfigList();
    getTargetIndicatorDataList();
}
const getTableData=()=>{
    getconfigList();
    getTargetIndicatorDataList();
}

// const treeVisible = computed(()=>{
//     return treeData.list.length>1 || (treeData.list.length==1 && treeData.list[0].children && treeData.list[0].children.length>0);
// })
// const columns = ref([]);
const data    = reactive({
    year     : moment(new Date).format('YYYY'),
    list     : [],
    subList  : [],
    loadding : false,
})
// const getList = ()=>{
//     data.loadding = true;
//     api.performance.budgetInGetWithSub(treeData.treeSelected.level,treeData.treeSelected.id,data.year).then(res=>{
//         if(res.code==200){
//             tableDataBuild(res.data);
//         }
//         data.loadding = false;
//     })
// }
const headers      = ref([]);
const getconfigList = ()=>{
    api.performance.getTargetIndicatorConfig(data.year).then(res=>{
        if(res.code==200){
            headers.value       = res.data; 
        }
    })
}

const rows      = ref([]);
const rowCount= ref(0);
const getTargetIndicatorDataList = ()=>{
    data.loadding = true;
    api.performance.getTargetIndicatorDataList(data.year,treeData.treeSelected.id).then(res=>{
        if(res.code==200){ 
            rows.value=res.data;
            indicatorTableDataBuild(res.data);
        }
        data.loadding = false;
    })
}
const calc = ()=>{
    data.loadding = true;
    api.performance.calcPerformanceAllocationDataAll(data.year).then(res=>{
        if(res.code==200){
            message.success("操作成功");
        }
        data.loadding = false;
    })
}

const budgetDrawerKey = ref(0);
const budgetDrawerIndicatorDataRef = ref(null);
const edit = (row)=>{
    budgetDrawerKey.value += 1;
    nextTick(()=>{
        budgetDrawerIndicatorDataRef.value.open( treeData.treeSelected.id,row.locked,data.year);
    })
}

const changeLogRef = ref(null);
const showLog = ()=>{
    changeLogRef.value.open(data.year);
}
const assessmentConfigRef = ref(null)
const showConfig = () =>{
  assessmentConfigRef.value.open()
}


const statisticalMethodConfigRef = ref(null)
const showMethodConfig = () =>{
    statisticalMethodConfigRef.value.open()
}

onMounted(() => {
    getTree();
})
const indicatorTableDataBuild = (res)=>{
    rowCount.value=res.length
}
 
</script>
<style scoped lang="less">
.budget_content{
    flex    : 1;
    display : flex;
    .right_content{
        flex           : 1;
        height         : 100%;
        width          : 0;
        min-width      : 0;
        display        : flex;
        flex-direction : column;
        padding        : 16px;
        .content-box{
            height         : 100%;
            display        : flex;
            flex-direction : column;
        }
    }
    .filter-box{
        display         : flex;
        justify-content : space-between;
        align-items: center;
        padding-bottom: 16px;
    }
    .table_box{
        padding: 16px;
    }
}

.left_tree_box{
    height        : 100%;
    width         : 280px;
    padding       : 16px;
    padding-right : 0;
}
.left_filter{
    box-sizing       : border-box;
    background-color : #fff;
    border-radius    : 4px;
    display          : flex;
    flex-direction   : column;

    :deep(.ant-tree .ant-tree-node-content-wrapper.ant-tree-node-selected){
        background-color : rgba(0,0,0,0);
        color            : @primary-color;
        font-weight      : bold;
        position: relative;
        &::after{
            content          : '';
            position         : absolute;
            width            : 100%;
            height           : 2px;
            background-color : @primary-color;
            bottom           : 0;
            left             : 0;
            border-radius    : 1px;
        }
    }
}

.tree_node{
    display         : flex;
    margin-bottom   : 4px;
    justify-content : space-between;
    width           : 100%;
    align-items     : center;
    .name{
        max-width: 150px
    }
}

button.color-primary{
    word-wrap: break-word;
    overflow-wrap: break-word;
    white-space: normal;

}


// .target-table {
//     border-collapse: collapse;
//     width: 100%;
//     border: 1px solid #ddd;
// }

// .target-table tr {
//     border: 1px solid #ddd;
//     padding: 8px;
//     text-align: left;
// }

// .target-table th {
//     background-color: #f2f2f2;
// }


</style>
