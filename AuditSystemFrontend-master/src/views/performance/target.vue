
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
                    @change="getList"
                    valueFormat="YYYY"
                    format="YYYY"
                    style="width:160px"/>
                    <a-button @click="showLog" :disabled="treeData.treeSelected.id==0">查看变更记录</a-button>
                </a-space>
            </div>
            <div class="content-box_full" v-if="treeData.list.length>0">
                <Title :title="levels(treeData.treeSelected.level)+'年度业绩目标'">
                  <template #right>
                    <a-button style="margin-right:6px;" class="color-danger"  @click="calc()"  v-if="treeData.treeSelected.level == 1" v-permission="['biz:performance:config']">
                        考核计算
                    </a-button>

                    <a-button style="margin-right:6px;"  @click="showMethodConfig()"  v-if="treeData.treeSelected.level == 1" v-permission="['biz:performance:config']">
                        考核指标统计方式配置
                    </a-button>
                    <a-button  @click="showConfig()"  v-if="treeData.treeSelected.level == 1" v-permission="['biz:performance:config']">
                        考核指标项配置
                    </a-button>
                </template>
                </Title>
                <div class="table_box">
                    <a-table bordered :columns="columns" :loading="data.loadding" :data-source="data.list" rowKey="tableKey" :pagination="false" :scroll="{x:'100%'}">
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
                    </a-table>
                </div>

                <!-- <Title v-if="data.subList.length>0||data.loadding" title="下级单位/部门年度业绩目标"></Title>
                <FullTable v-if="data.subList.length>0||data.loadding" bordered :loadding="data.loadding" :columns="columns" :dataSource="data.subList" rowKey="tableKey">
                    <template #bodyCell="{ column,record,index }">
                        <template v-if="column.key === 'name'">
                            <a-button type="text" class="color-primary" size="small" @click="edit(record)" v-if="!record.readonly">{{record.name}}</a-button>
                            <span v-else>{{record.name}}</span>
                        </template>
                        <template v-if="column.key === 'value'">
                            {{amountFormat(record.value)}}
                        </template>
                        <template v-if="column.amount">
                            {{amountFormat(record.list[column.key].value)}}
                        </template>
                    </template>
                </FullTable> -->

                <Title v-if="data.subList.length>0||data.loadding" :title="'下级各'+levels(treeData.treeSelected.level+1)+'年度业绩目标填报总计'"></Title>
                <FullTable v-if="data.subList.length>0||data.loadding" bordered :loadding="data.loadding" :columns="columns" :dataSource="data.subList" rowKey="tableKey">
                    <template #bodyCell="{ column,record,index }">
                        <template v-if="column.key === 'name'">
                            <span>下级各{{levels(treeData.treeSelected.level+1)}}填报总计</span>
                        </template>
                        <template v-if="column.key === 'value'">
                            {{amountFormat(record.value)}}
                        </template>
                        <template v-if="column.amount">
                            {{amountFormat(record.list[column.key] != null ? record.list[column.key].value : null)}}
                        </template>
                    </template>
                </FullTable>
            </div>
            <div class="content-box_full" v-else>
                <a-empty style="padding-top:60px;"/>
            </div>
        </div>
        <!-- <BudgetDrawer ref="budgetDrawerRef" title="业绩目标设置" type="In" :key="budgetDrawerKey" @success="getList"/> -->
        <BudgetDrawerIndicatorData ref="budgetDrawerIndicatorDataRef" title="业绩目标设置" type="In" :key="budgetDrawerKey" @success="getList"/>
        <ChangeLog ref="changeLogRef" moduleName="BudgetIn" :level="treeData.treeSelected.level" :recordId="treeData.treeSelected.id"/>
        <assessmentConfigDrawer ref="assessmentConfigRef"  @success="getList"/>
        <StatisticalMethodConfig ref="statisticalMethodConfigRef"  @success="getList"/>
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
        treeData.treeId       = [tree[0].id];
        treeData.treeSelected = {
            id    : tree[0].id,
            level : tree[0].level,
            name  : tree[0].name
        }
        getList();
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
    getList();
}
const treeVisible = computed(()=>{
    return treeData.list.length>1 || (treeData.list.length==1 && treeData.list[0].children && treeData.list[0].children.length>0);
})
const columns = ref([]);
const data    = reactive({
    year     : moment(new Date).format('YYYY'),
    list     : [],
    subList  : [],
    loadding : false,
})
const getList = ()=>{
    data.loadding = true;
    api.performance.budgetInGetWithSub(treeData.treeSelected.level,treeData.treeSelected.id,data.year).then(res=>{
        if(res.code==200){
            tableDataBuild(res.data);
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
        budgetDrawerIndicatorDataRef.value.open(row || treeData.treeSelected,data.year);
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
const tableDataBuild = (res)=>{
    let rowSpan = (res.budgetDatas || []).length || 1;
    columns.value = [
        {
            title : '层级',
            key   : 'name',
            width : 180,
            fixed : 'left',
            customCell: (_, index) => ({
                rowSpan : index%rowSpan==0 ? rowSpan : 0,
            }),
        },
        {
            title     : '目标额',
            dataIndex : 'label',
            fixed     : 'left',
            width     : 190,
        },
        {
            title : '全年',
            key   : 'value',
            width : 180,
        },
    ]
    res.months.forEach((item, i) => {
        columns.value.push({
            title  : item,
            key    : item,
            amount : true,
            width  : 150,
        },)
    });
    data.list = [];
    data.subList = [];
    function doArr(arr,parent){
        arr.forEach((item, i) => {
            let obj      = item;
            obj.name     = parent.label ||  parent.id;
            obj.level    = parent.level;
            obj.parentId = parent.id;
            obj.locked   = parent.locked;
            obj.readonly = parent.readonly;
            obj.tableKey = item.key+'_'+i;
            data.list.push(obj);


            let subObj = {
                name     : parent.label ||  parent.id,
                label    : item.label,
                level    : parent.level+1,
                tableKey : item.key+'_'+i,
                value    : 0,
                list     : null
            }
            if(parent.childBuudgets && parent.childBuudgets.length>0){
                parent.childBuudgets.forEach((itemChild, k) => {
                    itemChild.budgetDatas.forEach((item3, x) => {
                        if(item3.key==item.key){
                            subObj.value += (item3.value || 0);
                            if(subObj.list){
                                for (let y in item3.list) {
                                    subObj.list[y].value += (item3.list[y].value || 0);
                                }
                            }else{
                                subObj.list = JSON.parse(JSON.stringify(item3.list));
                            }
                        }
                    });
                });
                data.subList.push(subObj)
            }else{
                if(parent.level<3){
                    subObj.list = {}
                    data.subList.push(subObj)
                }
            }
        });
    }
    doArr(res.budgetDatas || [],res);
    // function doArr(arr,parent,attr){
    //     arr.forEach((item, i) => {
    //         let obj      = item;
    //         obj.name     = parent.label ||  parent.id;
    //         obj.level    = parent.level;
    //         obj.parentId = parent.id;
    //         obj.locked   = parent.locked;
    //         obj.readonly = parent.readonly;
    //         obj.tableKey = item.key+'_'+i;
    //         data[attr].push(obj);
    //     });
    // }
    // doArr(res.budgetDatas || [],res,'list');
    //
    // data.subList = [];
    // (res.childBuudgets || []).forEach((item, i) => {
    //     doArr(item.budgetDatas || [],item,'subList');
    // });
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

</style>
