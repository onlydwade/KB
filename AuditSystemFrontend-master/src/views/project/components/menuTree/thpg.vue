
<template>
    <div class="menu_inner padding_box">
        <Title title="评估记录">
            <template #right>
                <a-button type="primary" v-permission="['biz:projectAssess:add']" @click="drawer('ADD')">
                    新增项目评估记录
                </a-button>
            </template>
        </Title>
        <a-table :columns="data.columns" :loading="loadding" :data-source="data.list" rowKey="id"  :pagination="false" :scroll="{ x: '100%' }">
            <template #bodyCell="{ column,text,record,index }">
                <template v-if="column.key === 'sort'">
                    {{(filterForm.pageNo-1)*filterForm.pageSize + index +1}}
                </template>
                <template v-if="column.key === 'assessTime'">
                    {{dateFormat(record.assessTime,'YYYY-MM-DD')}}
                </template>
                <template v-if="column.key === 'assessDeadline'">
                    {{dateFormat(record.assessDeadline,'YYYY-MM-DD')}}
                </template>
                <template v-if="column.key === 'transmitDeadline'">
                    {{dateFormat(record.transmitDeadline,'YYYY-MM-DD')}}
                </template>
                <template v-if="column.key === 'assessStage' && record.assessYear !=null">
                    {{(record.assessYear).substr(2, 2)+'年' + (record.assessStageDetailsStr ==null ? '' : record.assessStageDetailsStr)|| ''}}
                </template>
                <template v-if="column.key === 'assessState'">
                    <span :style="{color:statesColor(record.assessState)}">
                        {{assessState(record.assessState)}}
                    </span>
                </template>
                <template v-if="column.key === 'transmitState'">
                    {{interventionPlan(record.transmitState)}}
                </template>
                <template v-if="column.key === 'transmitAlreadyState'">
                    {{interventionPlan(record.transmitAlreadyState)}}
                </template>
                <template v-if="column.key === 'action'">
                    <actionBtn :actions="actions(record,index)"/>
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
    
    <!-- <XmfkDrawer ref="drawerRef" :companyId="companyId" :menuId="menuInfo.id" @success="getPage"/> -->
    <EstimateDrawer ref="drawerRef" :projectId="projectId" :companyId="companyId" :menuId="menuInfo.id" @success="getPage" />
</template>
<script setup>
import api                 from '@/api/index';
// import XmfkDrawer          from '../correlation/XmfkDrawer.vue';
import EstimateDrawer  from '../../components/EstimateDrawer.vue'
import { dateFormat }      from '@/utils/tools';
import { message , Modal } from 'ant-design-vue';
import {hasPermission} from '@/utils/tools';
const props = defineProps({
    projectId : {
        type    : Number,
        default : 0,
    },
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
const route     = useRoute();
const projectId = ref(Number(route.query.id || 0));
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})
const data = reactive({
    list : [],
    columns : [
        {
            title     : '评估日期',
            key       : 'assessTime',
            width     : 150,
            required  : true
        },
        {
            title     : '经营评估期限',
            key : 'assessDeadline',
            width     : 150,
        },
        {
            title     : '评估人',
            dataIndex : 'assessUserName',
            width     : 150,
        },
        {
            title     : '评估标题',
            dataIndex : 'assessTitle',
            width     : 150,
        },
        {
            title     : '评估年份',
            dataIndex : 'assessYear',
            width     : 150,
        },
        {
            title     : '评估阶段',
            key : 'assessStage',
            width     : 150,
        },
        {
            title     : '状态',
            key : 'assessState',
            width     : 150,
        },
        {
            title     : '是否需下达干预方案',
            key : 'transmitState',
            width     : 200,
        },
        {
            title     : '方案下达期限',
            key : 'transmitDeadline',
            width     : 150,
        },
        {
            title     : '是否已下达干预方案',
            key : 'transmitAlreadyState',
            width     : 160,
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
            projectId : projectId.value
        }
    }
    loadding.value = true;
    api.project.projectAssessPage(postData,'projectAssess',49).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            console.log(data.list)
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
const summary = ref({});
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
            show:  hasPermission(['biz:projectAssess:show']),
            click : ()=>{
                drawer('SHOW',record);
            }
        },
        {
            text  : '编辑',
            show:  hasPermission(['biz:projectAssess:edit']),
            click : ()=>{
                drawer('EDIT',record);
            }
        },
        {
            text  : '删除',
            show:  hasPermission(['biz:projectAssess:delete']),
            click : ()=>{
                del(record);
            }
        },
    ];
}
const del = (data)=>{
    Modal.confirm({
        title   : '操作确认',
        content : '是否确认删除该条评估记录？',
        onOk() {
            api.project.projectAssessDel(data.id).then(res=>{
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
    let dfaultType = data.total==0
    // ?'SHOU_CI_CHU_ZI':'ZHUI_JIA_SHI_JIAO_ZHU_CE_ZI_BEN';
    drawerRef.value.open(type,record || {});
}
const assessState = (val) => {
    if(val === 0) {
        return '草稿'
    }
    if(val === 1) {
        return '已完成'
    }
}
const interventionPlan = (val) => {
    if(val === 0) {
        return '否'
    }
    if(val === 1) {
        return '是'
    }
}
const assessStage = (val) => {
    if(val === '1') {
        return '月度'
    }
    if(val === '2') {
        return '季度'
    }
    if(val === '3') {
        return '半季度'
    }
    if(val === '4') {
        return '年度'
    }
}
const statesColor = (val) =>{
    let colorStr = ""
    switch(val) {
        case 0:
            colorStr= '#000000b3'
            break;
        case 1:
            colorStr= '#63a103'
            break;
        default:
            colorStr= ''
    }
    return colorStr
}
</script>

<style scoped lang="less">

</style>
