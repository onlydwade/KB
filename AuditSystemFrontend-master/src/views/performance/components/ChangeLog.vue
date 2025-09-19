
<template>
<a-drawer
    :width="660"
    title="变更记录"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
    <template #extra>
        <a-space>
            <a-date-picker 
            v-model:value="year" 
            picker="year"
            @change="getList"
            valueFormat="YYYY"
            format="YYYY"
            size="large"
            style="width:160px"/>
            <a-button size="large" @click="handleClose">关闭</a-button>
        </a-space>
    </template>
    <div class="log_content">
        <a-steps direction="vertical" :current="-1">
            <a-step v-for="(item,index) in logList" :key="index">
                <template #icon>
                    <UserBox :data="{userId:item.updateUserId, realname:item.updateUserName}" single />
                </template>
                <template #title>
                    <a-space class="title">
                        <span class="sub_title">
                            {{item.updateTime}}
                        </span>
                    </a-space>
                </template>
                <template #description>
                    <div class="html_content" v-html="updateLogParse(item.updateLog)"></div>
                </template>
            </a-step>
        </a-steps>
        <a-empty v-if="logList.length==0" description="暂无变更记录" />
    </div>
</a-drawer>
</template>
<script setup>
import api from '@/api/index';
const props = defineProps({
    recordId:{
        type    : Number,
        default : false,
    },
    level:{
        type    : Number,
        default : false,
    },
    moduleName:{
        type    : String,
        default : 'Lead',
    },
})

const year         = ref(null);
const logStartTime = computed(()=>{
    return year.value+'-01-01 00:00:00';
})
const logEndTime = computed(()=>{
    return year.value+'-12-31 23:59:59';
})

const logList  = ref([]);
const getList = ()=>{
    let postData = {
        desc          : ['updateTime'],
        pageNo        : 1,
        pageSize      : 1000,
        params        : {},
        geParams:{
            updateTime : logStartTime.value
        },
        leParams:{
            updateTime : logEndTime.value
        }
    }
    api.common.operLogPage(props.moduleName,props.recordId,postData).then(res=>{
        if(res.code==200){
            logList.value = res.data.records;
        }
    })
}
onMounted(() => {
})
const updateLogParse = (str)=>{
    let json = [];
    let html = '';
    try {
        json = JSON.parse(str);
    } catch (e) {
        json = [];
    }
    if(typeof json=='object' && typeof json.length != 'number'){
        json =  [json];
    }
    json.forEach((item, i) => {
        html += '<p>';
        html += item.fieldName || ' ';
        if((item.valueBefore&&item.valueBefore!='NULL')||(item.valueAfter&&item.valueAfter!='NULL')){
            html += ' [ '
        }
        if(item.valueBefore&&item.valueBefore!='NULL'){
            html += item.valueBefore
        }
        if((item.valueBefore&&item.valueBefore!='NULL')&&(item.valueAfter&&item.valueAfter!='NULL')){
            html += ' => '
        }
        if(item.valueAfter&&item.valueAfter!='NULL'){
            html += '<span class="color-success">'+item.valueAfter+'</span>'
        }
        if((item.valueBefore&&item.valueBefore!='NULL')||(item.valueAfter&&item.valueAfter!='NULL')){
            html += ' ] '
        }
        html += '</p>';
        
    });
    return html;
}


const visible     = ref(false);
const handleClose = ()=>{
    visible.value = false;
}
const open = (date)=>{
    logList.value = [];
    year.value    = date;
    visible.value = true;
    getList();
}
defineExpose({open})
</script>
<style scoped lang="less">
.log_content{
    padding: 0 16px;
    .title{
        color: @primary-color;
    }
    .sub_title{
        font-size: 14px;
        color: @text-color;
    }
    .html_content{
        color: @text-color-secondary;
    }
    :deep(.ant-steps-item-container){
        cursor: auto!important;
    }
}
</style>
