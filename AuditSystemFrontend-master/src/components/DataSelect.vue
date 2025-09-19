
<template>
    <a-select :getPopupContainer="trigger => trigger.parentNode"
      v-model:value="selectId"
      :mode="mode"
      class="w_full"
      allowClear
      placeholder="请搜索选择"
      showSearch
      @change="change"
      :filter-option="false"
      :options="dataList"
      @search="fetchData">
      <template #notFoundContent>
            <a-spin v-if="fetching" size="small" />
            <div class="empty" v-else>
                按关键词搜索后选择
            </div>
      </template>
    </a-select>
</template>
<script setup>
import api from '@/api/index';
const props = defineProps({
    modelValue : {
        type    : [Number,String,Array],
        default : null,
    },
    mode       : {
        type    : String,
        default : '',
    },
    modeName:{
        type    : String,
        default : 'customer',
    },
    concats:{
        type    : [Object,Array],
        default : [],
    },
    options:{
        type    : Object,
        default : {
            key   : 'id',
            label : 'name'
        },
    },
    filter:{
        type    : Object,
        default : {
            key : 'customerId',
            val : 0
        },
    },
})
const emit     = defineEmits(['update:modelValue','change']);
const selectId = computed({
    get : () => props.modelValue || (props.mode?[]:null),
    set : (val) => emit('update:modelValue',val)
})
const dataList  = ref([]);
const fetching  = ref(false);
const timerFun  = ref(null);
const fetchData = (name)=>{
    window.clearTimeout(timerFun.value);
    if(name){
        timerFun.value = setTimeout(()=>{
           searchData(name);
       },500)
    }
}
const searchData = async (name)=>{
    fetching.value = true;
    dataDefault();
    //暂时固定几个场景  后续考虑更多适配 
    let postData = {
        pageNo        : 1,
        pageSize      : 500,
        content       : name,
        contentColumn : props.options.label,
        params        : {}
    }
    if(props.filter&&props.filter.val){
        postData.params[props.filter.key] = props.filter.val;
    }
    let res = null;
    
    if(props.modeName=='customer'){
        res = await api.customer.customerPage(postData);
    }
    if(props.modeName=='contact'){
        res = await api.customer.customerContactPage(postData);
    }
    if(props.modeName=='competitor'){
        res = await api.competitor.competitorPage(postData);
    }
    if(props.modeName=='realty'){
        res = await api.common.realtyPage(postData);
    }
    if(props.modeName=='project'){
        res = await api.project.projectPage(postData);
    }
    if(props.modeName=='company'){
        res = await api.investment.correlationPage(postData,'projectCompany');
    }
    if(res.code==200){
        let datas = res.data&&res.data.records?(res.data.records || []):(res.data || []);
        datas.forEach((item, i) => {
            let row = {
                label    : item[props.options.label],
                value    : item[props.options.key],
                customer : item.customer || {}
            }
            let hasData = dataList.value.some(oldData => {
                return row.value == oldData.value && row.label == oldData.label
            })
            if(!hasData){
                dataList.value.push(row);
            }
        });
    }
    fetching.value = false;
}
const change = (val,option)=>{
    emit('change',val,option)
}

const concatsWacth = ref(true);
watch(() => props.concats,
    (newVal, oldVal) => {
        dataDefault();
        // if((props.mode&&(oldVal || []).length>0) || (!props.mode&&oldVal)){
        // 
        // }
        // if(concatsWacth.value || (oldVal || []).length>0){
        // 
        // }
    },
    {deep: true}
)
watch(()=>props.filter,(val,oldVal)=>{
    if(!val.val){
        dataDefault();
    }else if(val.val==oldVal.val){
        
    }else{
        searchData();
    }
})
const dataDefault = ()=>{
    if(!props.mode){
        if(props.concats&&props.concats.id){
            concatsWacth.value = false;
            dataList.value = [{
                label : props.concats.name || '未定义',
                value : props.concats.id
            }]
        }else{
            dataList.value = [];
        }
    }else{
        let datas = props.concats || [];
        if(datas.length>0){
            concatsWacth.value = false;
            dataList.value.length = [];
            datas.forEach((item, i) => {
                let row = {
                    label    : item[props.options.label],
                    value    : item[props.options.key],
                    customer : item.customer || {}
                }
                let hasData = dataList.value.some(oldData => {
                    return row.value == oldData.value && row.label == oldData.label
                })
                if(!hasData){
                    dataList.value.push(row);
                }
            });
            
            // dataList.value = datas.map(item=>{
            //     return {
            //         label    : item[props.options.label] || '未定义',
            //         value    : item.id,
            //         customer : item.customer || {}
            //     }
            // })
        }else{
            dataList.value = [];
        }
    }
}
onMounted(() => {
    dataDefault();
});
    
</script>
<style scoped lang="less">
</style>
