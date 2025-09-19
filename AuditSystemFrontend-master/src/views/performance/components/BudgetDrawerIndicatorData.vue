<template>
    <a-drawer :maskClosable="false" 
    :width="1224"
    title="业绩目标设置"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
        <template #extra>
            <a-space :size="16">
                <a-button size="large" @click="handleClose">关闭</a-button>
                <a-button size="large" type="primary" v-if="!filterData.locked&&editStatus"   @click="handleOk()" :loading="submitLoading" >保存</a-button>
                <a-button size="large" v-if="!filterData.locked&&!editStatus" @click="editStatus=true" v-permission="['biz:budgetIn:update']">变更</a-button>
                
                <a-button size="large" type="primary" v-if="!filterData.locked" @click="locked" v-permission="['biz:budgetIn:update']">锁定</a-button>
                <a-button size="large" type="primary" v-if="filterData.locked&&!editStatus"  @click="unlocked" v-permission="['biz:budgetIn:unlocked']">解锁</a-button>
            </a-space>
        </template>
        <a-form layout="vertical" :model="formData" ref="formRef">
            <Title title="基本信息" class="titlebar"></Title>
            <a-row :gutter="24">
                <a-col :span="16">
                    <a-form-item>
                        <a-input allowClear disabled v-model:value="filterData.companyName" placeholder="请输入"/>
                    </a-form-item>
                </a-col>
                <a-col :span="8">
                    <a-form-item>
                        <a-date-picker  disabled
                        v-model:value="filterData.year"        
                        picker="year" 
                        valueFormat="YYYY"
                        format="YYYY"
                        style="width:200px"/>
                    </a-form-item>
                </a-col>
            </a-row>
          
            <Title title="年度目标设置" class="titlebar"></Title>
            <div class="table_box" >
                <table> 
                <thead>
                    <tr>  
                        <th style="float: left;width: 170px;padding-top: 8px">
                            <span>考核项</span>
                        </th>        
                        <th  v-for="header in headers" :key="header.code">
                        <a-input style="float: left;width: 176px;margin-right: 8px;margin-bottom: 4px;" :key="header.code"  type="text"  v-model:value="header.name" :disabled="editDisabled" />  
                        </th> 
                        <th  >
                            <a-button type="primary" @click="addColumn()" :disabled="editDisabled">
                                <template #icon>
                                    <plus-outlined />
                                </template>
                            </a-button>
                        </th>
                    </tr>  
                    </thead>  
                    <tbody>  
                    <tr class="ant-table-row"  v-for="(row, rowIndex) in rows" :key="rowIndex">  
                        <td  style="float: left;width: 170px;padding-top: 8px;">{{row.label}}</td>  
                        <td   v-for="(data, dataIndex) in row.dataList" :key="dataIndex">
                            <a-input  style="float: left;width: 176px;margin-right: 8px;margin-bottom: 4px" :key="data.id"  type="text"  :disabled="editDisabled"
                            v-model:value="data.amount"  :min="0" maxlength="12" class="w_full"
                                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                        :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                 
                                        />                          
        
                        </td>  
                        <td></td>                  
                    </tr>  
                    </tbody>  
                </table>  
            </div>
        </a-form>
    </a-drawer>
</template>
<script setup>
import {numFixed , amountUnit}    from '@/utils/tools';
import api                        from '@/api/index';
import { message , notification } from 'ant-design-vue';
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
    recordId:{
        type    : Number,
        default : 0,
    },
    companyName:{
        type    : String,
        default : '',
    },
})

const columns    = ref([]);
const filterData = reactive({
    recordId   : null,
    companyName      : '',
    year       : '2024',
})

const editStatus = ref(false);
const rows      = ref([]);
const rowNew  = ref({
    amount:null,
    fieldSort:10,
    fieldName:0,
    fieldYear:'',
    companyId:"",    
    code:"new"
})
const headers      = ref([]);
const headerNew      = ref({
    fieldYear:'',
    code:"",
    name:"",
    sort:10
});

const visible       = ref(false);
const formData      = ref({budgetDatas:[],configList:[]});
const formRef       = ref(null);
const submitLoading = ref(false);
const handleOk      = (callBack)=>{
    console.log("rows 数据 ");
    console.log(rows.value);
    console.log(headers.value);
    //debugger
    let isNull=false;
    headers.value.forEach((item, i) => {
        if(item.name===undefined || item.name===""){
            isNull=true;
            return;
        }
    })
    if(isNull){
        message.warn('表头名称不能为空');
        return;
    }

    submitLoading.value      = true; 
    let postData={companyId:null,fieldYear:null,configList:[],dataVoList:[]};
    postData.configList=headers.value;
    postData.dataVoList=rows.value;
    postData.companyId=props.recordId;
    postData.fieldYear=filterData.year;
    api.performance.saveTargetIndicator(postData).then(res=>{
        if(res.code==200){
            emit('success');
            if(callBack){
                callBack();
            }else{
                message.success('操作成功');
            }
            editStatus.value = false;
            handleClose() ;
        }
        submitLoading.value = false;
    })
    
}
 

//
const addColumn=()=>{

    console.log("addColumn")
    let timestamp  =Date.now();
    let sort= parseInt( headers.value.length) + 1; 
    let code= "new"+timestamp;
    headerNew.code=code;
    headerNew.sort=sort; 
    headers.value.push( JSON.parse(JSON.stringify(headerNew) ));
    rows.value.forEach((item, i) => {
        if (item.dataList&&item.dataList.length>0) {
            rowNew.fieldSort=  sort;
            rowNew.code= "new"+timestamp;
            //rowNew.id=i+timestamp;
            //rowNew.amount=0;            
            item.dataList.push(JSON.parse(JSON.stringify(rowNew)))
        }
    });
}



const editDisabled = computed(()=>{
    return !editStatus.value || filterData.locked;
})
const locked = ()=>{
    if(submitLoading.value)return;
    submitLoading.value      = true; 
    handleOk(()=>{
        let apiKey = props.type=='In'?'budgetInLockIndicator':'budgetOutLockIndicator';
        api.performance[apiKey](props.recordId || 0,filterData.year).then(res=>{
            if(res.code==200){
                emit('success');
                message.success('锁定成功');
                filterData.locked = true;
                editStatus.value = false;
                handleClose() ;
            }
            submitLoading.value = false;
        })
    })
}
const unlocked = ()=>{
    let apiKey = props.type=='In'?'budgetInUnlockedIndicator':'budgetOutUnlockedIndicator';
    if(submitLoading.value)return;
    submitLoading.value      = true; 
    api.performance[apiKey](props.recordId || 0,filterData.year).then(res=>{
        if(res.code==200){
            emit('success');
            message.success('解锁成功');
            filterData.locked = false;
            editStatus.value = false;
                handleClose() ;
        }
        submitLoading.value = false;
    })
}
const handleClose = ()=>{ 
    debugger
    visible.value = false;
}
const open = (data,locked,year)=>{
    filterData.recordId   = props.recordId;
    filterData.companyName      = props.companyName;
    filterData.locked=locked;
    filterData.year       = year;
    getconfigList();
    getList();
    editStatus.value = false;
    visible.value    = true;
}
defineExpose({open})
const getconfigList = ()=>{
    api.performance.getTargetIndicatorConfig(filterData.year).then(res=>{
        if(res.code==200){
            headers.value       = res.data; 
             
        }
    })
}

const getList = ()=>{
    // let apiKey = props.type=='In'?'budgetInGet':'budgetOutGet';
    console.log("record")
    console.log(filterData)
    api.performance.getTargetIndicatorDataList(filterData.year,props.recordId).then(res=>{
        if(res.code==200){
            formData.value       = res.data;
            rows.value=res.data; 
            // if(res.data){
            //     formData.value.locked =res.data[0].locked
            // }
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
