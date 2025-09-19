
<template>
    <Title title="文档列表">
    </Title>
    <a-table 
    :columns="columns" 
    :loading="loadding" 
    :data-source="tableList"
    v-if="tableList.length>0"
    rowKey="id" 
    :pagination="false"
     :scroll="{x:'100%'}">
        <template #bodyCell="{ column,record,index }">
            <template v-if="column.key === 'operName'">
                <span class="color-danger" v-if="record.required==1">*</span>
                {{record.operName}}
            </template>
            <template v-if="column.key === 'status'">
                <check-circle-outlined v-if="record.projectDocumentList.length>0" class="color-success"/>
                <clock-circle-outlined v-else  class="color-gray" />
            </template>
            <template v-if="column.key === 'projectDocumentList'">
                <FileItem v-for="(item,index) in record.projectDocumentList" :readOnly="readOnly" :key="index" :fileData="item.docmentObject" @fileDel="fileDel(index,record.projectDocumentList,item)"/>
            </template>
        </template>
    </a-table>
</template>
<script setup>
import api         from '@/api/index';
import { message } from 'ant-design-vue';
const props = defineProps({
    projectId: {
        type    : Number,
        default : 0,
    },
    menuId: {
        type    : Number,
        default : 0,
    },
    readOnly: {
        type    : Boolean,
        default : false,
    },
    projectType:{
        type    : String,
        default : 'GU_QUAN_HE_ZUO_XIANG_MU',
    }
})
const columns    = [
    {
        title : '名称',
        key   : 'operName',
    },
    {
        title : '状态',
        key   : 'status',
        align : 'center',
        width : 100,
    },
    {
        title : '文件',
        key   : 'projectDocumentList',
        width : 480,
    },
]
const loadding  = ref(false);
const tableList = ref([]);
const getList   = ()=>{
    if(!props.menuId){
        return;
    }
    loadding.value = true;
    api.project.documentList(props.projectId,props.menuId).then(async res=>{
        if(res.code==200){
            tableList.value = (res.data || []).filter(item=>{
                return !item.projectType || item.projectType.indexOf(props.projectType)>-1;
            });
            tableList.value.sort((a,b)=>{
                return a.sorts-b.sorts;
            })
        }
        loadding.value = false;
    })
}

watch(
    () => props.menuId,(newValue, oldValue) => {
        getList();
    }
)
const fileDel = (index,list,item)=>{
    api.project.delDocument(item.id).then(res=>{
        if(res.code==200){
            list.splice(index,1);
        }
    })
}
onMounted(() => {
    getList();
})
</script>
<style scoped lang="less">
</style>
