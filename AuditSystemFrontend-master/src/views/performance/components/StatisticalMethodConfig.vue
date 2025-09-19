<template>
    <a-drawer :maskClosable="false" 
    :width="1224"
    title="考核指标统计方式配置"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
        <template #extra>
            <a-space :size="16">
                <a-button size="large" @click="handleClose">取消</a-button>
                <a-button size="large" type="primary" :disabled="filteryear<new Date().getFullYear()"  @click="handleOk()" :loading="submitLoading" >保存</a-button>
               
            </a-space>
        </template>
        <a-row :gutter="24">

                <a-col :span="8">
                    <a-form-item>
                        <a-date-picker 
                        v-model:value="filteryear" 
                        picker="year" 
                        valueFormat="YYYY"
                        format="YYYY"
                        @change="getConfigList()"
                        style="width:200px"/>
                    </a-form-item>
                </a-col>
            </a-row>
        <a-form layout="vertical" :model="formData" ref="formRef">           
            <div class="table_box">
                <a-table bordered :columns="columns" :data-source="formData" rowKey="id" :pagination="false" :scroll="{x:'max-content'}">
                    <template #bodyCell="{ column,text,record,index }">
                        <template v-if="column.key === 'fieldName'"> 
                            {{ record.fieldKey == 'HTZJE' ? '合同总金额' : '合同年度金额' }}
                        </template>
                        <template v-else> 
                            <a-checkbox :disabled="filteryear<new Date().getFullYear()"  :checked="text" size="large" @change="select(record,column,text)" ></a-checkbox>        
                        </template>
                    </template>                    
                </a-table>
            </div>
        </a-form>
    </a-drawer>
</template>
<script setup>
import api                        from '@/api/index';
import { message , notification } from 'ant-design-vue';
// import {numFixed , amountUnit}    from '@/utils/tools';
const emit  = defineEmits(['success'])
const props = defineProps({
    title:{
        type    : String,
        default : '',
    },
    type:{
        type    : String,
        default : 'In',
    },
    budgetYear:{
        type    : Number,
        default : 2024,
    }
})
const columns = [
  {
    key: 'fieldName',
    title: "指标类型",
    dataIndex: "fieldName",
    width: 120,
    editType: "input",
  },
  {
    title: "单一投标-增量拓展",
    dataIndex: "yesIncremental",
    width: 150,
    editType: "check",
  },
  {
    title: "单一投标-存量保盘",
    dataIndex: "yesKeep",
    width: 150,
    editType: "check",
  },
  {
    title: "单一投标-存量新增",
    dataIndex: "yesAdd",
    width: 150,
    editType: "check",
  },
  {
    title: "单一非投标-增量拓展",
    dataIndex: "noIncremental",
    width: 150,
    editType: "check",
  },
  {
    title: "单一非投标-存量保盘",
    dataIndex: "noKeep",
    width: 150,
    editType: "check",
  },
  {
    title: "单一非投标-存量新增",
    dataIndex: "noAdd",
    width: 150,
    editType: "check",
  },
];
const filteryear =    ref(2024);
 
 
const editStatus = ref(false);

const visible       = ref(false);
const formData      = ref([]);
const formRef       = ref(null);
const submitLoading = ref(false); 


const handleClose = ()=>{ 
    visible.value = false;
}

const handleOk      = (callBack)=>{
    let postData={configs:[]};
    postData.configs = formData.value;
    api.performance.saveConfigList(postData).then(res=>{
        visible.value= false;
        message.success('操作成功');
        emit('success');        
      })
    // api.performance.saveConfigList(filteryear).then(res=>{
    //     if(res.code==200){
    //             emit('success');
    //             if(callBack){
    //                 callBack();
    //             }else{
    //                 message.success('操作成功');
    //             }                
    //         }
    // })    
}


const open = (data,year)=>{    
    filteryear.value = props.budgetYear;
    getConfigList();
    editStatus.value = false;
    visible.value    = true;
}
defineExpose({open})

const select =  (record,column,text) => {
    if(column.dataIndex==='yesIncremental'){
        record.yesIncremental=!text;
    }
    if(column.dataIndex==='yesKeep'){
        record.yesKeep=!text;
    }
    if(column.dataIndex==='yesAdd'){
        record.yesAdd=!text;
    }
    if(column.dataIndex==='noIncremental'){
        record.noIncremental=!text;
    }
    if(column.dataIndex==='noKeep'){
        record.noKeep=!text;
    }
    if(column.dataIndex==='noAdd'){
        record.noAdd=!text;
    }

}

const getConfigList = ()=>{
    api.performance.getConfigList(filteryear.value).then(res=>{
        if(res.code==200){
            formData.value       = res.data;
        }
    })
}
 
</script>
<style scoped lang="less">
.titlebar{
    margin : 16px -16px;
    &:first-child{
        margin-top : 0;
    }
}
</style>
