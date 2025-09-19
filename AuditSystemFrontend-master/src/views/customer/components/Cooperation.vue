
<template>
    <div class="content-box_full">
        <Title title="合作协议及证明上传"></Title>
        <FullTable :columns="data.columns" :dataSource="data.list">
            <template #bodyCell="{ column,record }">
                <template v-if="column.key === 'name'">
                    <span class="color-danger" v-if="record.required">*</span>
                    {{record.name}}
                </template>
                <template v-if="column.key === 'status'">
                    <check-circle-outlined v-if="record[record.attr].length>0" class="color-success"/>
                    <clock-circle-outlined v-else  class="color-gray" />
                </template>
                <template v-if="column.key === 'upload'">
                    <Upload v-model="record[record.attr]" :limit="record.limit" @change="fileUploadChange($event,record)" :readOnly="readOnly" :canDel="canDel"/>
                </template>
            </template>
        </FullTable>
        
        <!-- <FooterBar v-if="!readOnly">
            <template #left>
                <a-button size="large" type="primary">发起OA审批</a-button>
            </template>
        </FooterBar> -->
    </div>
</template>
<script setup>
import api         from '@/api/index';
import { message } from 'ant-design-vue';
import {hasPermission} from '@/utils/tools';
const props = defineProps({
    recordId:{
        type    : Number,
        default : 0,
    },
    readOnly:{
        type    : Boolean,
        default : false,
    }
})

const data     = reactive({
    list    : [],
    columns : [
        {
            title : '名称',
            key   : 'name',
            width : 150,
        },
        {
            title : '上传状态',
            key   : 'status',
            width : 80,
        },
        {
            title : '文件上传',
            key   : 'upload',
            width : 180,
        },
    ],
})
const canDel = computed(()=>{
    if(hasPermission(['biz:customer:cooperationFileDel'])){
        return 1;
    }else{
        return 2
    }
    
})
const baseData = [
    {name:'合作协议（盖章版）上传',required:true,attr:'cooperationAgreementDocument',limit:1},
    {name:'专项会议纪要上传',   required:false,attr:'meetingMinutesDocument',limit:0},
    {name:'其他文件上传',      required:false,attr:'otherDocument',limit:0},
]
const getInfo  = ()=>{
    api.customer.cooperationGet(props.recordId).then(res=>{
        if(res.code==200){
            let resData = res.data || {};
            if(resData.id){
                cooperationId.value = resData.id;
            }
            data.list           = [];
            baseData.forEach((item, i) => {
                let obj = {
                    name     : item.name,
                    required : item.required,
                    attr     : item.attr,
                    limit    : item.limit
                }
                obj[item.attr] = JSON.parse(resData[item.attr] || '[]');
                data.list.push(obj);
            });
        }
    })
}
const cooperationId    = ref(null);
const fileUploadChange = (val,record)=>{
    let postData = {
        id         : cooperationId.value,
        customerId : props.recordId
    }
    data.list.forEach((item, i) => {
        postData[item.attr] = item[item.attr].length>0?JSON.stringify(item[item.attr]):'[]';
    });
    
    let apiKey = cooperationId.value?'cooperationEdit':'cooperationAdd';
    api.customer[apiKey](postData).then(res=>{
        if(res.code==200){
            cooperationId.value = res.data.id;
            if(record.attr=='cooperationAgreementDocument' && postData['cooperationAgreementDocument']!='[]'){
                markKeyWords();
            }
        }
    })
}
onMounted(() => {
    getInfo();
})

const infoData = inject('getAutoParams')();
const markKeyWords = ()=>{
    if(infoData.value.keywords.indexOf('战略合作')==-1){
        let postData = {
            id       : infoData.value.id,
            keywords : infoData.value.keywords?(infoData.value.keywords+',战略合作'):'战略合作'
        }
        api.customer.customerEdit(postData).then(res=>{
            if(res.code==200){
                infoData.value.keywords = postData.keywords;
            }
        });
    }
    
}
</script>
<style scoped lang="less">
.content-box{

}
</style>
