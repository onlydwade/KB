<template>
    <div class="page-common content-inner">
        <a-page-header :ghost="false" :breadcrumb="{ routes }" >
            <template #footer>
                <a-row>
                    <a-col :span="8">
                      <a-tree-select
                          v-model:value="deptFilter.deptId"
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
                          :tree-data="deptFilter.tree"
                          :disabled="notChange"
                      >
                      </a-tree-select>
                    </a-col>
                    <a-col :span="16" class="text-right">
                        <a-space>
                            <!-- <a-radio-group
                            v-model:value="dateFilter.dateType"
                            @change="dateTypeChange"
                            button-style="solid">
                                <a-radio-button value="month">按月查看</a-radio-button>
                                <a-radio-button value="year">按年查看</a-radio-button>
                            </a-radio-group> -->

                            <a-date-picker
                            :allowClear="false"
                            v-model:value="dateFilter.dateVal"
                            :picker="dateFilter.dateType"
                            :valueFormat="dateFilter.dateFormat"
                            :format="dateFilter.dateFormat"
                            :disabledDate="dateFilter.disabledDate"
                            :disabled="notChange"
                            />
                        </a-space>
                        <a-space style="margin-left: 10px;" v-if="!notChange">
                            <a-radio-button :disabled="notChange" @click="openSendTodoModal" style="background-color: #f99c34;color: white;" >发送数据看板</a-radio-button>
                        </a-space>

                    </a-col>
                </a-row>
            </template>
        </a-page-header>
        <AScrollbar style="margin:-16px;" v-if="deptFilter.tree.length>0">
            <div class="dashboard_main">
                <div class="dashboard_item" style="width:40%;">
                    <Summary :dateType="dateFilter.dateType" :dateVal="dateFilter.dateVal" :level="deptFilter.level" :deptId="deptFilter.deptId"/>
                </div>
                <div class="dashboard_item" style="width:60%;">
                    <div style="width:100%;height:50%; ">
                        <ProjectPerformance :dateType="dateFilter.dateType" :dateVal="dateFilter.dateVal" :level="deptFilter.level" :deptId="deptFilter.deptId"  />
                    </div>
                    <div style="width:100%;height:50%;padding-top : 16px">
                        <extendedMode :dateType="dateFilter.dateType" :dateVal="dateFilter.dateVal" :level="deptFilter.level" :deptId="deptFilter.deptId"  />
                    </div>
                </div>

                <div class="dashboard_item" style="width:40%;">
                    <ProjectYetai :dateType="dateFilter.dateType" :dateVal="dateFilter.dateVal" :level="deptFilter.level" :deptId="deptFilter.deptId"/>
                </div>
                <div class="dashboard_item" style="width:60%;">
                    <ProjectMap :dateType="dateFilter.dateType" :dateVal="dateFilter.dateVal" :level="deptFilter.level" :deptId="deptFilter.deptId"/>
                </div>

                <div class="dashboard_item" style="width:65%;">
                    <bidding :dateType="dateFilter.dateType" :dateVal="dateFilter.dateVal" :level="deptFilter.level" :deptId="deptFilter.deptId"/>
                </div>
                <div class="dashboard_item" style="width:35%;">
                    <FunnelAnalysis :dateType="dateFilter.dateType" :dateVal="dateFilter.dateVal" :level="deptFilter.level" :deptId="deptFilter.deptId"/>
                </div>
                <div class="dashboard_item" style="width:100%;">
                    <OpportunityCondition :dateType="dateFilter.dateType" :dateVal="dateFilter.dateVal" :level="deptFilter.level" :deptId="deptFilter.deptId"/>
                </div>

<!--                <div class="dashboard_item" style="width:100%;">-->
<!--                    <CompositeBar :dateType="dateFilter.dateType" :dateVal="dateFilter.dateVal" :level="deptFilter.level" :deptId="deptFilter.deptId"/>-->
<!--                </div>-->
            </div>
            <SendTodoModal ref="sendTodoModal" />
        </AScrollbar>
        <div class="empty" v-else>
            <a-empty description="当前账号暂无可查询主体"/>
        </div>
    </div>


</template>
<script setup>
import moment               from 'moment'
import api                  from '@/api/index';
import { useRoute } from 'vue-router';
import Summary              from './components/dashboard/Summary.vue';   //整体项目情况
import ProjectMap           from './components/dashboard/ProjectMap.vue';   //城市分布图
import ProjectYetai         from './components/dashboard/ProjectYetai.vue';   //项目业态占比
import ProjectPerformance   from './components/dashboard/ProjectPerformance.vue';   //项目业绩达成情况
import ExtendedMode         from './components/dashboard/ExtendedMode.vue';   //扩展模式占比
import Bidding              from './components/dashboard/Bidding.vue';   //投标情况
import OpportunityCondition from './components/dashboard/OpportunityCondition.vue';   //实际签约情况
import FunnelAnalysis       from './components/dashboard/FunnelAnalysis.vue';   //漏斗分析
import CompositeBar         from './components/dashboard/CompositeBar.vue';   //签约趋势及排名
import SendTodoModal from './components/dashboard/correlation/SendTodoModal.vue';

const sendTodoModal = ref(null);
const route = useRoute()
const routeYear = ref(route.query.year)
const routeDeptId = ref(route.query.deptId)
const routeTodoId = ref(route.query.todoId)
let notChange = false

const routes = [
    {
        path: 'analysis',
        breadcrumbName: '数据分析'
    },
    {
        breadcrumbName: '数据看板'
    }
]
const deptFilter = reactive({
    deptId : null,
    level  : null,
    tree   : []
});

const props = defineProps({
    dateType:{
        type    : String,
        default : 'year',
    },
    dateVal:{
        type    : String,
        default : 1,
    },
    level:{
        type    : Number,
        default : 2,
    },
    deptId:{
        type    : Number,
        default : 3,
    },
})

const openSendTodoModal = () => {
    sendTodoModal.value.open(props.level,props.deptId,props.dateVal)
};

const getDept  = async ()=>{
  let res = await api.performance.actualInTree();
  if(res.code==200&&res.data){
    let tree = [];
    tree = getDeptWithOutRoot([res.data]);
    // if(res.data.children && res.data.children.length>0){
    //   tree = getDeptWithOutRoot([res.data]);
    // }else{
    //   let dept = await api.sys.deptInfo(res.data.parentId);
    //   tree = [{
    //     id       : dept.data.deptId,
    //     name     : dept.data.deptName,
    //     level    : dept.data.level,
    //     parentId : dept.data.parentId,
    //     children : []
    //   }];
    // }

    deptFilter.tree = tree;
      if(isNotEmpty(routeDeptId)){
          deptFilter.deptId = routeDeptId
      }else {
          deptFilter.deptId = [tree[0].id];
      }
    deptFilter.level = [tree[0].level];

  }
}
const deptSelect = (val,option)=>{
    deptFilter.deptId = option.id;
    deptFilter.level  = option.level;
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

const dateFilter = reactive({
    dateType   : 'year',
    dateVal    : moment(new Date()).format('YYYY'),
    dateFormat : computed(() => {
        return dateFilter.dateType == 'month' ? 'YYYY-MM' : 'YYYY'
    }),
    disabledDate: (current) => {
        return current && current > moment().endOf('day')
    }
})
const dateTypeChange = ()=>{
    dateFilter.dateVal = dateFilter.dateType == 'year'?moment(new Date()).format('YYYY'):moment(new Date()).format('YYYY-MM');
}

// 一个计算属性或方法来检查 值 是否为空或空字符串
const isNotEmpty = (data) => {
    return data.value !== null && data.value !== '' && data.value !== undefined;
};

onMounted(() => {
    getDept();

    if(isNotEmpty(routeYear)){
        dateFilter.dateVal = routeYear.value
        notChange = true
    }
    if(isNotEmpty(routeYear)){
        dateFilter.routeDeptId = routeYear.value
        notChange = true
    }
})

</script>
<style scoped lang="less">
.dashboard_main {
    padding   : 8px;
    display   : flex;
    flex-wrap : wrap;
}
.dashboard_item{
    width   : 50%;
    padding : 8px;

    :deep(.dashboard_box){
        background-color : #fff;
        border-radius    : 8px;
        height           : 100%;
        .ant-spin-nested-loading{
            height : 100%;
            .ant-spin-container{
                height         : 100%;
                display        : flex;
                flex-direction : column;
            }
        }
        .title_box{
            padding-bottom : 0px;
            padding-top    : 0;
            min-height     : 53px;
            border-bottom  : 1px solid #eee;
        }
        .dashboard_inner{
            padding    : 16px;
            min-height : 230px;
        }
    }
}
.empty{
    flex            : 1;
    display         : flex;
    align-items     : center;
    justify-content : center;
}
</style>
