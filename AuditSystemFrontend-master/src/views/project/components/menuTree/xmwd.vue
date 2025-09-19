
<template>
    <div class="menu_inner">
        <DocTree v-model="documentId" :treeData="documentTree"/>
        <div class="right_content">
            <ProjectTreeDocuments 
          :offlineApproval="menuInfo.offlineApproval" :menuId="documentId" :projectId="projectId" :type="1" :readOnly="true"/>
        </div>
    </div>
</template>
<script setup>
import api         from '@/api/index';
import { message } from 'ant-design-vue';
import DocTree     from '../DocTree.vue'
// import { useMenuTree }  from './menu';
const props = defineProps({
    projectId: {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    }
})
// const {
//     disabled
// } = useMenuTree(props.projectId,toRefs(props).menuInfo);

const projectType = inject('getAutoParams')('projectType');
const documentTree = ref([]);
const documentId   = ref(null);
const getTree      = ()=>{
    api.project.treeDocumentByType(projectType.value || 'DAN_YI_FEI_TOU_BIAO_XIANG_MU').then(res=>{
        
        if(res.code==200){
            const addKey = (treeData)=>{
                return treeData.map(item => ({
                    ...item,
                    disabled : item.children?.length>0 ? true : false,
                    children : item.children?addKey(item.children) : []
                }))
            }            
            documentTree.value = addKey(res.data);
            if(documentTree.value.length>0){
                documentId.value = getFirstId(documentTree.value[0]);
            }
        }
    })
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
    padding : 16px;
    flex    : 1;
    display : flex;
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
