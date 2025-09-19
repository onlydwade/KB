<template>
    <div class="dashboard_box">
        <a-spin :spinning="loadding">
            <Title title="xxxx">
            </Title>
            <div class="dashboard_inner">
            </div>
        </a-spin>
    </div>
</template>
<script setup>
import api from '@/api/index';

const props = defineProps({
    dateType:{
        type    : String,
        default : 'year',
    },
    dateVal:{
        type    : String,
        default : null,
    },
    level:{
        type    : Number,
        default : null,
    },
    deptId:{
        type    : Number,
        default : null,
    },
})
const loadding = ref(true);
const getData = ()=>{
    loadding.value = true;
    setTimeout(()=>{
        loadding.value = false;
    },500)
}

watch([()=>props.dateType,()=>props.dateVal,()=>props.level,()=>props.deptId], (val) => {
    if(props.dateType&&props.dateVal&&props.level&&props.deptId){
        getData();
    }
},{immediate:true})
</script>

<style scoped lang="less">

</style>
