
<template>
    <div class="log_content">
        <a-steps direction="vertical" :current="-1">
            <a-step v-for="(item,index) in data.list" :key="index">
                <template #icon>
                    <UserBox :data="{userId:item.updateUserId, realname:item.updateUserName}" single />
                </template>
                <template #title>
                    <a-space class="title">
                        <span class="sub_title">
                            {{item.updateDeptName || '-'}}
                            {{item.updatePostName}}
                        </span>
                        <a-divider type="vertical" />
                        {{item.updateTime}}
                    </a-space>
                </template>
                <template #description>
                    <div class="html_content" v-html="updateLogParse(item.updateLog)"></div>
                </template>
            </a-step>
        </a-steps>
        <a-empty v-if="data.list.length==0" description="暂无变更记录" />
        <div class="pagination_box">
            <a-pagination showSizeChanger show-quick-jumper
                v-model:current="data.pageNo"
                v-model:pageSize="data.pageSize"
                :show-total="total => `共 ${total} 条数据`"
                size="small"
                @change="getList"
                @showSizeChange="data.pageNo=1"
                :total="data.total" />
        </div>
    </div>
</template>
<script setup>
import api from '@/api/index';
const props = defineProps({
    recordId:{
        type    : Number,
        default : false,
    },
    moduleName:{
        type    : String,
        default : 'Lead',
    },
})

const loadding = ref(false);
const data     = reactive({
    pageNo   : 1,
    pageSize : 20,
    total    : 0,
    list     : []
})
const getList = ()=>{
    let postData = {
        desc          : ['updateTime'],
        pageNo        : data.pageNo,
        pageSize      : data.pageSize,
        params        : {},
        gtParams      : {'updateUserId':0}
    }
    loadding.value = true;
    api.common.operLogPage(props.moduleName,props.recordId,postData).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
onMounted(() => {
    getList();
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
</script>
<style scoped lang="less">
.log_content{
    padding:16px;
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
