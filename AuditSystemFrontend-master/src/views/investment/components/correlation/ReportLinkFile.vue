
<template>
    <template v-for="(item,index) in linkFile" key="index">
        <FileItem v-for="(file,indexf) in item.projectCompanyDocumentList" :readOnly="true" :key="index+'_'+indexf" :fileData="file.docmentObject"/>
    </template>
</template>
<script setup>
import api from '@/api/index';
const props  = defineProps({
    companyId : {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 0,
    },
    year:{
        type    : String,
        default : '',
    },
    cycleType:{
        type    : String,
        default : '',
    },
    cycle:{
        type    : String,
        default : '',
    }
})
const linkObj = computed(()=>{
    return ({
        42:{
            key  : 'companyCoordination',
            type : 'DIAO_YAN',
        },
        44:{
            key   : 'companyOperateReport',
        },
    })[props.menuId] || {}
})
const linkFile    = ref([]);
const getLinkFile = async (projectId)=>{
    let postData = {
        desc     : ['createTime'],
        pageNo   : 1,
        pageSize : 1000,
        params   : {
            companyId : props.companyId,
        }
    }
    if(props.menuId==42){
        postData.params.type            = linkObj.value.type;
        postData.params.surveyCycleType = props.cycleType;
        postData.params.surveyYear      = props.year;
    }else{
        postData.params['report.cycle_type'] = props.cycleType;
        postData.params['report.year']       = props.year;
    }
    if(props.cycleType!='DIAO_YAN_NIAN_DU'){
        if(props.menuId==42){
            postData.params.surveyCycle = props.cycle;
        }else{
            postData.params['report.cycle'] = props.cycle;
        }
    }
    api.investment.correlationPage(postData,linkObj.value.key,props.menuId).then(res=>{
        if(res.code==200){
            linkFile.value = (res.data.records || []).reduce((acc, curr) => acc.concat(curr.documentTemplateList),[]);
        }
    })
}

watch([()=>props.year,()=>props.cycleType,()=>props.cycle], (val) => {
    if(props.year && ((props.cycleType&&props.cycle) || props.cycleType=='DIAO_YAN_NIAN_DU')){
        getLinkFile();
    }else{
        linkFile.value = [];
    }
},{immediate:true})
</script>
<style scoped lang="less">
</style>
