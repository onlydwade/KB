
<template>
    <div class="content-box_full">
        <Title title="关联项目"></Title>
        <FullTable :columns="data.columns" :loading="loadding" :data-source="data.list">
            <template #bodyCell="{ column,record,index,text }">
                <template v-if="column.key === 'projectNo'">
                    <LinkTo modelName="project" :link="'/innerPage/projectInfo?id='+record.id" :dataId="record.id">
                        {{record.projectNo}}
                    </LinkTo>
                </template>
                <template v-if="column.key === 'projectStatus'">
                    <projectStatus :projectStatus="record.serviceStatus" />
                </template>
                <EllipsisTooltip v-if="column.ellipsis" :content="text"/>
            </template>
        </FullTable>
        <div class="pagination_box">
            <a-pagination showSizeChanger show-quick-jumper
                v-model:current="data.pageNo" 
                v-model:pageSize="data.pageSize"  
                :show-total="total => `共 ${total} 条数据`" 
                size="small" 
                @change="getPage"
                @showSizeChange="data.pageNo==1"
                :total="data.total" />
        </div>
    </div>
</template>
<script setup>
import api from '@/api/index';
const props = defineProps({
    recordId:{
        type    : Number,
        default : false,
    },
})
const loadding = ref(false);
const data     = reactive({
    list    : [],
    columns : [
        {
            title     : '关联项目编号',
            key       : 'projectNo',
            width     : 150,
        },
        {
            title     : '关联项目名称',
            dataIndex : 'projectName',
            width     : 150,
            ellipsis  : true,
        },
        {
            title     : '项目状态',
            key       : 'projectStatus',
            width     : 130,
        },
        {
            title     : '归属单位',
            dataIndex : 'companyName',
            width     : 150,
            ellipsis  : true,
        },
        {
            title     : '项目归属人',
            dataIndex : ['attributorUser','realname'],
            width     : 180,
        },
        {
            title        : '关联时间',
            dataIndex    : 'createTime',
            width        : 180
        },
    ],
    pageNo   : 1,
    pageSize : 10,
    total    : 0,
})
const getPage = ()=>{
    let postData = {
        desc          : ['createTime'],
        pageNo        : data.pageNo,
        pageSize      : data.pageSize,
        params        : {
            customerId : props.recordId
        },
    }
    loadding.value = true;
    api.project.projectPage(postData).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
onMounted(() => {
    getPage();
})
</script>
<style scoped lang="less">

</style>
