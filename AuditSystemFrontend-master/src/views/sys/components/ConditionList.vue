
<template>
    <div  class="condition_box">
        <div class="condition_item" v-for="(item,index) in conditions" :key="index">
            <a-row :gutter="16" type="flex" align="middle">
                <a-col :span="3">
                    <a-select :getPopupContainer="trigger => trigger.parentNode"
                      v-model:value="item.fieldType"
                      class="w_full"
                      placeholder="请选择"
                      @change="(val,option)=>{
                          item.fieldName = null;
                          item.conditionValue = null;
                          if(codeToStatus(val,ruleDict[modeName])==8){
                              item.condition = '3';
                          }
                      }"
                      :options="ruleDict[modeName]">
                    </a-select>
                </a-col>
                <a-col :span="4">
                    <a-select :getPopupContainer="trigger => trigger.parentNode"
                      v-model:value="item.fieldName"
                      class="w_full"
                      @change="(val,option)=>{
                          item.conditionValue = null;
                      }"
                      placeholder="请选择"
                      notFoundContent="请先选择条件类型"
                      :options="ruleDict[item.fieldType]">
                    </a-select>
                </a-col>
                <a-col :span="2" class="text-center">
                    <a-select :getPopupContainer="trigger => trigger.parentNode"
                      v-if="codeToStatus(item.fieldType,ruleDict[modeName])==7 || codeToStatus(item.fieldName,ruleDict[item.fieldType])==10"
                      v-model:value="item.condition"
                      class="w_full"
                      placeholder="请选择"
                      :options="dict.options('GUI_ZE_FU_HAO')">
                    </a-select>
                    <a-select v-else :getPopupContainer="trigger => trigger.parentNode"
                      v-model:value="item.condition"
                      class="w_full"
                      placeholder="请选择">
                      <a-select-option value="3">=</a-select-option>
                      <a-select-option value="7">!=</a-select-option>
                    </a-select>
                </a-col>
                <a-col :span="3">
                    <a-select :getPopupContainer="trigger => trigger.parentNode"
                      :value="codeToStatus(item.fieldType,ruleDict[modeName])"
                      disabled
                      class="w_full">
                        <a-select-option :value="7">时间</a-select-option>
                        <a-select-option :value="8">
                            {{codeToStatus(item.fieldName,ruleDict[item.fieldType])==10?'数值':'选项'}}
                        </a-select-option>
                    </a-select>
                </a-col>
                <a-col :span="4">
                    <template v-if="codeToStatus(item.fieldType,ruleDict[modeName])==7">
                        <a-input-number v-model:value="item.conditionValue"  class="w_full"/>
                    </template>
                </a-col>
                <a-col :span="4">
                    <template v-if="codeToStatus(item.fieldType,ruleDict[modeName])==7">
                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                            v-model:value="item.unit"
                            class="w_full">
                            <a-select-option value="NIAN">年</a-select-option>
                            <a-select-option value="YUE">月</a-select-option>
                            <a-select-option value="TIAN">天</a-select-option>
                    </a-select>
                        <!-- <a-input-number v-if="props.modeName=='XIANG_MU'" v-model:value="item.conditionValue" addon-after="天"  class="w_full"/>
                        <a-input-number v-else v-model:value="item.conditionValue" addon-after="年"  class="w_full"/> -->
                    </template>
                    <template v-else>
                        <DeptSelect v-if="codeToStatus(item.fieldName,ruleDict[item.fieldType])==9" :leaf="false" v-model="item.conditionValue"/>
                        <a-input-number v-else-if="codeToStatus(item.fieldName,ruleDict[item.fieldType])==10" v-model:value="item.conditionValue" :min="0" class="w_full"/>
                        <TagSelect v-else v-model="item.conditionValue" mode="multiple" :maxTagCount="1" :options="ruleDict[item.fieldName]"/>
                    </template>
                </a-col>
                <a-col :span="4">
                    <a-button type="text" class="color-primary" size="small" @click="del(index)" v-if="conditions.length>1">删除</a-button>
                    <a-button type="text" class="color-primary" size="small" @click="add" v-if="index==conditions.length-1">增加条件</a-button>
                </a-col>
            </a-row>
        </div>
        <div class="color-danger" v-if="!spinning&&!validateField">
            请完善表单信息！
        </div>
        <div class="spinning_tip" v-if="spinning">
            请先选择规则对象类型
        </div>
    </div>
</template>
<script setup>
import api              from '@/api/index';
import { useDictStore } from '@/store/dict';
const dict  = useDictStore();
const props = defineProps({
    modelValue : {
        type    :Array,
        default : [],
    },
    readOnly:{
        type    : Boolean,
        default : false
    },
    ruleDict:{
        type    : Object,
        default : {}
    },
    modeName:{
        type    : String,
        default : ''
    }
})
const spinning   = computed(()=>props.modeName=='');
const emit       = defineEmits(['update:modelValue','update:validateField']);
const defaultVal = (val)=>{
    return val.length>0?val:[{condition:'3'}];
}
const conditions = ref(defaultVal(props.modelValue));
watch(()=> props.modelValue,() => {
    conditions.value = defaultVal(props.modelValue)
})
watch(()=> props.modeName,() => {
    conditions.value = [{condition:'3'}];
})

//表单完善状态
const getValidateField = (arr) => {
  const len = arr.length;
  let reg = true;
  for (let i = 0; i < len; i++) {
    const item = arr[i];
    const isValid = ['XIANG_MU_YE_WU' , 'TOUTUO_OPERATE_YE_WU_TOUTUO','CUSTOMER_YE_WU_CUSTOMER','OA_TODO_REMIND_YE_WU','OA_TODO_REMIND_XI_TONG'].includes(item.fieldType)
      ? !item.fieldType || !item.fieldName || !item.condition || !(item.conditionValue || item.conditionValue === 0)
      : !item.fieldType || !item.fieldName || !item.condition || !(item.conditionValue || item.conditionValue === 0) || !item.unit;
    if (isValid) {
      reg = false;
      break;
    }
  }
  return reg;
}
const validateField    = ref(getValidateField(conditions.value));

watch(conditions,
    (newVal, oldVal) => {
        validateField.value = getValidateField(newVal);
        emit('update:validateField', validateField.value)
        emit('update:modelValue',newVal);
    },
    {deep: true}
)


const del = (index)=>{
    conditions.value.splice(index,1);
}
const add = ()=>{
    conditions.value.push({condition:'3'});
}

const codeToStatus = (code,parent)=>{
    let status = null;
    (parent || []).forEach((item, i) => {
        if(item.value==code){
            status = item.status;
        }
    });
    return status;
}
</script>
<style scoped lang="less">
.condition_box{
    padding: 16px 0;
    position:relative;
    .spinning_tip{
        position         : absolute;
        height           : 100%;
        width            : 100%;
        top              : 0;
        left             : 0;
        background-color : rgba(255,255,255,0.7);
        display          : flex;
        justify-content  : center;
        align-items      : center;
        color            : @primary-color;
    }
}
.condition_item{
    margin-bottom : 16px;
}
</style>
