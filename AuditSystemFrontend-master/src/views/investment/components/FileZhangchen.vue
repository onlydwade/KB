
<template>
    <FileItem v-for="(item,index) in linkFile" :readOnly="true" :key="index" :fileData="item.docmentObject"/>
</template>
<script setup>
import api from '@/api/index';
const props  = defineProps({
    projectId : {
        type    : Number,
        default : 0,
    },
})
const linkFile    = ref([]);
const getLinkFile = async (projectId)=>{
    if(!props.projectId){
        return;
    }
    //带入  盖章版章程数据
    let linkRes = await api.project.documentList(props.projectId,23);
    linkRes.data.forEach((item, i) => {
        if(item.id==1026){
            linkFile.value = item.projectDocumentList || [];
        }
    });
}
onMounted(() => {
    getLinkFile();
})
</script>
<style scoped lang="less">
</style>
