
<template>
    <div class="left_filter">
        <div class="project_search">
            <a-select
              v-model:value="projectId"
              class="w_full"
              allowClear
              placeholder="请搜索选择项目"
              showSearch
              :filter-option="false"
              @select="projectSelect"
              :options="dataList"
              @search="fetchProject">
              <template #notFoundContent>
                    <a-spin v-if="fetching" size="small" />
                    <div class="empty" v-else>
                        按关键词搜索后选择项目
                    </div>
              </template>
            </a-select>
        </div>
        <AScrollbar>
            <div class="padding_box" style="padding-left:8px;">
                <a-tree
                    v-if="!loading&&treeList.length>0"
                    showLine
                    defaultExpandAll
                    v-model:selectedKeys="treeId"
                    selectable
                    :field-names="{
                        children: 'children',
                        title: 'name',
                        key: 'id',
                    }"
                    :tree-data="treeList">
                    <template #title="item">
                        <div class="tree_node">
                            <div class="name">
                                <EllipsisTooltip class="flex_full" :content="item.name"/>
                            </div>
                        </div>
                    </template>
                </a-tree>
            </div>
        </AScrollbar>
    </div>
</template>
<script setup>
import api from '@/api/index';
const emit  = defineEmits(['update:modelValue','update:projectId','update:projectType','change'])
const props = defineProps({
    modelValue : {
        type    : Number,
        default : null,
    },
    projectId : {
        type    : Number,
        default : null,
    },
    projectType:{
        type    : String,
        default : null,
    }
})
const loading       = ref(false);
const typeByProject = ref(false);
const treeId        = computed({
    get : () => [props.modelValue],
    set : (val) => {
        emit('update:modelValue',...val);
        emit('change',...val);
    }
})
const treeList     = ref([]);
const getTree      = async ()=>{
    loading.value = true;
    let menuRes   = await api.project.treeDocumentByType(props.projectType);
    if(menuRes.code==200){
        treeList.value     = menuRes.data;
    }
    loading.value = false;
}
watch(()=>props.projectType, (val) => {
    getTree();
    if(typeByProject.value){
        typeByProject.value = false;
    }else{
        emit('update:projectId',null);
    }
    emit('change');
},{immediate:true})


onMounted(() => {
    getTree();
})
const projectId = computed({
    get : () => [props.projectId],
    set : (val) => {
        emit('update:projectId',val);
        emit('change');
    }
})
const dataList  = ref([]);
const fetching  = ref(false);
const timerFun  = ref(null);
const fetchProject = (name)=>{
    window.clearTimeout(timerFun.value);
    if(name){
        timerFun.value = setTimeout(()=>{
           searchData(name);
       },500)
    }
}
const searchData = async (name)=>{
    fetching.value = true;
    let postData = {
        pageNo     : 1,
        pageSize   : 500,
        likeParams : {
            projectName:name
        }
    }
    let res = await api.project.projectPage(postData);
    if(res.code==200){
        dataList.value = res.data.records.map(item=>{
            return {
                label       : item.projectName,
                value       : item.id,
                projectType : item.projectType,
            }
        });
        fetching.value = false;
    }
}
const projectSelect = (val,option)=>{
    typeByProject.value = true;
    emit('update:projectType',option.projectType);
}
</script>
<style scoped lang="less">
.left_filter{
    height           : 100%;
    box-sizing       : border-box;
    background-color : #fff;
    border-radius    : 4px;
    margin-right     : 16px;
    width            : 250px;
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
    
    .project_search{
        padding    : 16px;
        box-shadow : 0 2px 2px rgb(0 21 41 / 4%);
        z-index: 10;
    }
}

.tree_node{
    display         : flex;
    margin-bottom   : 4px;
    justify-content : space-between;
    width           : 100%;
    align-items     : center;
    .name{
        max-width: 100px
    }
}
</style>
