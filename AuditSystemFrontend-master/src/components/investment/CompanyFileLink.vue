
<template>
    <FileItem v-for="(item,index) in linkFile" :readOnly="true" :key="index" :fileData="item.docmentObject"/>
</template>
<script setup>
import api from '@/api/index';
const props  = defineProps({
    companyId : {
        type    : Number,
        default : 0,
    },
    documentTemplateId:{
        type    : Number,
        default : 0,
    }
})
const linkFile    = ref([]);
const getLinkFile = async (projectId)=>{
    let postData = {
        pageNo   : 1,
        pageSize : 1000,
        params   : {
            companyId          : props.companyId,
            documentTemplateId : props.documentTemplateId
        }
    }
    const res  = await api.investment.correlationPage(postData,'companyDocument');
    if(res.code==200){
        linkFile.value = res.data.records || []
    }
}
onMounted(() => {
    getLinkFile();
})
</script>
<style scoped lang="less">
</style>
