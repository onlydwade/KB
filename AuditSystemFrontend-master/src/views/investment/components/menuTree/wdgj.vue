
<template>
    <div class="menu_inner">
        <div class="left_box">
            <a-tabs v-model:activeKey="pageType" @change="getTree">
                <a-tab-pane key="Project" tab="投前"/>
                <a-tab-pane key="Company" tab="投后"/>
            </a-tabs>
            <DocTree v-if="pageType=='Project'" v-model="documentId_project" :treeData="documentTree_project"/>
            <DocTree v-if="pageType=='Company'" v-model="documentId_company" :treeData="documentTree_company"/>
        </div>
        <div class="right_content">
            <ProjectDocuments v-if="pageType=='Project'" :menuId="documentId_project" :projectId="projectId" :readOnly="true"/>
            <CompanyDocPage v-if="pageType=='Company'" :menuId="documentId_company" :companyId="companyId" :readOnly="true"/>
        </div>
    </div>
</template>
<script setup>
import api              from '@/api/index';
import { message }      from 'ant-design-vue';
import DocTree          from '../DocTree.vue'
import ProjectDocuments from '../correlation/ProjectDocuments.vue'
import CompanyDocPage   from '../correlation/CompanyDocPage.vue'
const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    }
})
const pageType = ref('Project');

const projectId            = inject('getAutoParams')('projectId');
const documentTree_project = ref([]);
const documentId_project   = ref(null);
const getTree_project      = ()=>{
    if(documentTree_project.value.length==0){
        api.project.treeDocumentByType('GU_QUAN_HE_ZUO_XIANG_MU').then(res=>{
            if(res.code==200){
                const addKey = (treeData)=>{
                    return treeData.map(item => ({
                        ...item,
                        disabled : item.children?.length>0 ? true : false,
                        children : item.children?addKey(item.children) : []
                    }))
                }            
                documentTree_project.value = addKey(res.data);
                if(documentTree_project.value.length>0){
                    documentId_project.value = getFirstId(documentTree_project.value[0]);
                }
            }
        })
    }
}

const documentTree_company = ref([]);
const documentId_company   = ref(null);
const getTree_company      = ()=>{
    if(documentTree_company.value.length==0){
        api.investment.stepTree().then(res=>{
            if(res.code==200){
                const addKey               = (treeData)=>{
                    return treeData.map(item => ({
                        ...item,
                        disabled : item.children?.length>0 ? true : false,
                        children : item.children?addKey(item.children) : []
                    }))
                }            
                documentTree_company.value = addKey(res.data.filter(item=>item.isShow==1));
                if(documentTree_company.value.length>0){
                    documentId_company.value = getFirstId(documentTree_company.value[0]);
                }
            }
        })
    }
}
const getTree = ()=>{
    if(pageType.value=='Project'){
        getTree_project();
    }
    if(pageType.value=='Company'){
        getTree_company();
    }
}

const getFirstId = (item)=>{
    let documentId = null;
    function doArr(obj){
        if(obj.children&&obj.children.length>0){
            doArr(obj.children[0]);
        }else{
            documentId =  obj.id;
        }
    }
    doArr(item);
    return documentId;
}
onMounted(() => {
    getTree();
})

</script>
<style scoped lang="less">
.menu_inner{
    flex           : 1;
    display        : flex;
    .left_box{
        border-right: 1px solid #ddd;
    }
    .right_content{
        margin-left    : 16px;
        flex           : 1;
        width          : 0;
        height         : 100%;
        display        : flex;
        flex-direction : column;
    }
}
</style>
