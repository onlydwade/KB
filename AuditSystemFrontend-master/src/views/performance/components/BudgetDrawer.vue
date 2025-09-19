<template>
    <a-drawer :maskClosable="false" 
    :width="1024"
    title="业绩目标设置"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
        <template #extra>
            <a-space :size="16">
                <a-button size="large" @click="handleClose">取消</a-button>
                <a-button size="large" type="primary" v-if="!formData.locked&&editStatus" @click="handleOk()" :loading="submitLoading" v-permission="['biz:budgetIn:update']">保存</a-button>
                <a-button size="large" v-if="!formData.locked&&!editStatus" @click="editStatus=true" v-permission="['biz:budgetIn:update']">变更</a-button>
                
                <a-button size="large" type="primary" v-if="!formData.locked" @click="locked" v-permission="['biz:budgetIn:update']">锁定</a-button>
                <a-button size="large" type="primary" v-if="formData.locked&&!editStatus" @click="unlocked" v-permission="['biz:budgetIn:unlocked']">解锁</a-button>
            </a-space>
        </template>
        <a-form layout="vertical" :model="formData" ref="formRef">
            <Title title="基本信息" class="titlebar"></Title>
            <a-row :gutter="24">
                <a-col :span="16">
                    <a-form-item>
                        <a-input allowClear disabled v-model:value="formData.label" placeholder="请输入"/>
                    </a-form-item>
                </a-col>
                <a-col :span="8">
                    <a-form-item>
                        <a-date-picker 
                        v-model:value="filterData.year" 
                        disabled
                        picker="year" 
                        valueFormat="YYYY"
                        format="YYYY"
                        style="width:200px"/>
                    </a-form-item>
                </a-col>
            </a-row>
            <Title :title="'年度目标'+title" class="titlebar"></Title>
            <a-row :gutter="24">
                <a-col :span="8" v-for="(item,index) in formData.budgetDatas" :key="index">
                    <a-form-item :name="['budgetDatas',index,'value']" :required="item.required">
                        <template #label>
                            {{item.label}} 
                            <a-tooltip placement="top" :title="item.desc" v-if="item.desc">
                                <question-circle-filled />
                            </a-tooltip>
                        </template>
                        <a-input-number allowClear v-model:value="item.value" placeholder="请输入" :precision="0" class="w_full"
                        :disabled="editDisabled || item.readonly"
                        @change="inputChange($event,item.key)"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"
                        :addon-after="amountUnit(item.value)"/>
                        <p class="color-warning" v-if="item.value&&item.defaultValue&&item.value>item.defaultValue">
                            金额已超出默认值
                        </p>
                    </a-form-item>
                </a-col>
            </a-row>
            <!-- <div class="rules_box" v-for="(item,index) in (formData.rules || [{},{}])" :key="index">
                <a-alert v-if="!item.trigger" :message="item.content" style="margin-bottom:16px;" type="error" closable></a-alert>
            </div> -->
            <a-form-item>
                <a-button type="primary" :disabled="editDisabled" @click="amountEqual">一键平分目标额</a-button>
            </a-form-item>
            
            <Title title="目标细分（按月度分解目标）" class="titlebar"></Title>
            <div class="table_box">
                <a-table bordered :columns="columns" :data-source="formData.budgetDatas" rowKey="id" :pagination="false" :scroll="{x:'max-content'}">
                    <template #bodyCell="{ column,record,index }">
                        <template v-if="column.amount">
                            <a-input-number 
                            allowClear
                            :disabled="editDisabled || record.list[column.key].readonly"
                            v-model:value="record.list[column.key].value" :min="0" class="w_full"/>
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
import {numFixed , amountUnit}    from '@/utils/tools';
const emit  = defineEmits(['success'])
const props = defineProps({
    title:{
        type    : String,
        default : '',
    },
    type:{
        type    : String,
        default : 'In',
    }
})

const columns    = ref([]);
const filterData = reactive({
    recordId   : null,
    level      : 0,
    year       : '2022',
})

const editStatus = ref(false);

const visible       = ref(false);
const formData      = ref({budgetDatas:[],rules:[]});
const formRef       = ref(null);
const submitLoading = ref(false);
const handleOk      = (callBack)=>{
    formRef.value.validateFields().then(vRes=>{
        //金额校验
        let amountReg = getAmountReg();
        if(!amountReg){
            message.warning('年度金额与月份细分金额之和不相等，请检查！！！');
            return;
        }
        submitLoading.value      = true;
        let apiKey = props.type=='In'?'budgetInUpdate':'budgetOutUpdate';
        api.performance[apiKey](filterData.level,filterData.recordId || 0,filterData.year,formData.value).then(res=>{
            if(res.code==200){
                emit('success');
                if(callBack){
                    callBack();
                }else{
                    message.success('操作成功');
                }
                editStatus.value = false;
            }
            submitLoading.value = false;
        })
    }).catch(err=>{
        message.warning('请完善必填信息！');
    })
}

const editDisabled = computed(()=>{
    return !editStatus.value || formData.locked;
})
const locked = ()=>{
    if(submitLoading.value)return;
    handleOk(()=>{
        let apiKey = props.type=='In'?'budgetInLock':'budgetOutLock';
        api.performance[apiKey](filterData.recordId || 0,filterData.year).then(res=>{
            if(res.code==200){
                message.success('操作成功');
                formData.value.locked = true;
            }
        })
    })
}
const unlocked = ()=>{
    let apiKey = props.type=='In'?'budgetInUnlocked':'budgetOutUnlocked';
    api.performance[apiKey](filterData.recordId || 0,filterData.year).then(res=>{
        if(res.code==200){
            message.success('操作成功');
            formData.value.locked = false;
        }
    })
}
const handleClose = ()=>{
    notification.close();
    notificationRemove();
    visible.value = false;
}
const open = (data,year)=>{
    filterData.recordId   = data.parentId;
    filterData.level      = data.level;
    filterData.year       = year;
    getList();
    editStatus.value = false;
    visible.value    = true;
}
defineExpose({open})

const getList = ()=>{
    let apiKey = props.type=='In'?'budgetInGet':'budgetOutGet';
    api.performance[apiKey](filterData.level,filterData.recordId || 0,filterData.year).then(res=>{
        if(res.code==200){
            formData.value       = res.data;
            formData.value.rules = formData.value.rules || [];
            tableDataBuild();
        }
    })
}

//数值集合
const valPool     = ref({});
const inputChange = (val,valueKey)=>{
    valPool.value[valueKey] = val;
}
const tableDataBuild = ()=>{
    let rowSpan = formData.value.budgetDatas.length || 1;
    columns.value = [
        {
            title     : '月份',
            dataIndex : 'label',
            fixed     : 'left',
            width     : 190,
        },
    ]
    formData.value.months.forEach((item, i) => {
        columns.value.push({
            title  : item,
            key    : item,
            amount : true,
            width  : 150,
        })
    });
    formData.value.budgetDatas.forEach((item, i) => {
        item.value = numFixed(item.value,0) || null;
        valPool.value[item.key] = item.value;
        item.valueComputed      = computed(()=>{
            let total = null;
            Object.keys(item.list).forEach((key)=>{
                total += item.list[key].value || 0;
            })
            return numFixed(total,0) || null;
        })
        watch(()=>item.valueComputed,(newVal,oldVal)=>{
            item.value              = newVal;
            valPool.value[item.key] = newVal;
        })
        // if(i==2){            //如果最后一项是合计
        //     Object.keys(item.list).forEach((key)=>{
        //         item.list[key].value = computed({
        //             get:()=>{
        //                 let total = 0;
        //                 for (var i = 0; i < formData.value.budgetDatas.length-1; i++) {
        //                     total += formData.value.budgetDatas[i].list[key].value || 0
        //                 }
        //                 return numFixed(total,0) || null;
        //             },
        //             set:()=>{
        // 
        //             }
        //         })
        //     })
        // }
    });
    
    formData.value.rules.forEach((item,index) => {
        let type     = item.formulaRight.type;
        item.trigger = computed(()=>{
            let total = 0;
            item.formulaRight.fields.forEach((field,i) => {
                let fieldVal = field.type=='field'?(valPool.value[field.name] || 0):field.name;
                if(i==0){
                    total = fieldVal;
                }else{
                    if(type=='SUM'){
                        total += fieldVal;
                    }
                    if(type=='MULTIP'){
                        total = total*fieldVal;
                    }
                }
            });
            if(item.symbol==1){
                return (item.valueLeft || 0)<total;
            }
            if(item.symbol==2){
                return (item.valueLeft || 0)>total;
            }
            return false;
        })
        watch(()=>item.trigger,(newVal,oldVal)=>{
            if(newVal&&!oldVal){
                notification.warning({
                    key         : 'notification'+index,
                    message     : '提示',
                    duration    : 0,
                    description : item.wraningMsg,
                    placement   : 'bottomLeft',
                    class       : 'color-danger',
                });
            }
            if(!newVal&&oldVal){
                try {
                    notification.close('notification'+index);
                } catch (e) {
                    
                }
            }
        })
    });
}
const notificationRemove = ()=>{
    formData.value.rules.forEach((item,index) => {
        try {
            notification.close('notification'+index);
        } catch (e) {}
    })
}

const amountEqual = ()=>{
    let len = formData.value.months.length;
    formData.value.budgetDatas.forEach((item, i) => {
        let equal = (item.value || 0)/len;
        Object.keys(item.list).forEach((key)=>{
            item.list[key].value = numFixed(equal,0) || null;
        })
    });
}

const getAmountReg = ()=>{
    let res = true;
    for (var i = 0; i < formData.value.budgetDatas.length; i++) {
        let total = 0;
        let item = formData.value.budgetDatas[i];
        Object.keys(item.list).forEach((key)=>{
            total += item.list[key].value || 0;
        })
        if(total!=item.value){
            res = false;
            break;
        }
    }
    return res;
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
