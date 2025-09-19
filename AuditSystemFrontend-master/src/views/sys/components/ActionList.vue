
<template>
    <div class="action_box">
        <a-row :gutter="24" v-if="!readOnly">
            
            <a-col :span="12" v-for="(item,index) in ruleDict['GUI_ZE_GUAN_LI_DONG_ZUO']">
                <div class="add_btn" v-if="props.modeName=='XIANG_MU' && item.groupName =='XIANG_MU'"  @click="add(item)" :class="{'add_disabled':item.value=='BIAN_GENG_ZHI'}">
                    <plus-circle-outlined style="margin-right:8px;"/>
                    {{item.label}}
                </div>
                <div class="add_btn" v-if="props.modeName =='CUSTOMER' && item.groupName =='CUSTOMER'" @click="add(item)" :class="{'add_disabled':item.value=='BIAN_GENG_ZHI'}">
                    <plus-circle-outlined style="margin-right:8px;"/>
                    {{item.label}}
                </div>
                <div class="add_btn" v-else-if="props.modeName =='TOUTUO_OPERATE' && item.groupName =='TOUTUO_OPERATE'" @click="add(item)" :class="{'add_disabled':item.value=='BIAN_GENG_ZHI'}">
                    <plus-circle-outlined style="margin-right:8px;"/>
                    {{item.label}}
                </div>
                <div class="add_btn" v-if="props.modeName =='OA_TODO_REMIND' && item.groupName =='OA_TODO_REMIND'" @click="add(item)" :class="{'add_disabled':item.value=='BIAN_GENG_ZHI'}">
                    <plus-circle-outlined style="margin-right:8px;"/>
                    {{item.label}}
                </div>
            </a-col>
        </a-row>
        <a-form
        ref="formRef"
        layout="vertical"   >
            <a-collapse ghost expandIconPosition="right" class="crl-collapse" v-model:activeKey="collapseKey">
                <template v-for="(item,index) in actionList">
                    <a-collapse-panel :key="index" v-if="actionTypeStatus(item.actionType)==5">
                        <template #header>
                            <h3>发送消息通知</h3>
                        </template>
                        <template #extra>
                            <a-button type="text" class="color-primary" size="small" @click="del(index)" v-if="actionList.length>1">删除</a-button>
                        </template>
                        <a-row :gutter="24">
                            <a-col :span="12">
                                <a-form-item required label="发送对象">
                                    <a-select :getPopupContainer="trigger => trigger.parentNode"
                                      v-model:value="item.sendObjects"
                                      class="w_full"
                                      mode="multiple"
                                      placeholder="请选择"
                                      :options="ruleDict[item.actionType]">
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :span="12">
                                <a-form-item required label="发送渠道（可多选）">
                                    <a-select :getPopupContainer="trigger => trigger.parentNode"
                                      v-model:value="item.sendChannels"
                                      class="w_full"
                                      mode="multiple"
                                      placeholder="请选择"
                                      :options="dict.options('GUI_ZE_FA_SONG_QU_DAO')">
                                    </a-select>
                                </a-form-item>
                            </a-col>
                        </a-row>
                        <a-form-item required label="频次类型">
                            <a-row :gutter="24"  type="flex" align="middle">
                                <a-col>
                                    <a-radio-group v-model:value="item.sendType" name="sendType">
                                        <a-radio :value="1">一次性发送</a-radio>
                                        <a-radio :value="2">按周期发送</a-radio>
                                    </a-radio-group>
                                </a-col>
                                <a-col flex="120px" v-if="item.sendType==2">
                                    <a-input-number v-model:value="item.sendTime" class="w_full" :min="0" />
                                </a-col>
                                <a-col flex="120px" v-if="item.sendType==2">
                                    <a-select :getPopupContainer="trigger => trigger.parentNode"
                                      v-model:value="item.sendUnit"
                                      class="w_full"
                                      :options="dict.options('SHI_JIAN_ZHOU_QI')">
                                    </a-select>
                                </a-col>
                                <a-col flex="auto" v-if="item.sendType==2">
                                    / 次
                                </a-col>

                
                            </a-row>
                            <a-row :gutter="24"  type="flex" align="middle">
                                <a-col flex="220px" v-if="item.sendType==2">
                                    <!-- <a-form-item required label="任务开始时间" name="startTime">
                                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="formData.startTime"
                                            class="w_full"
                                            valueFormat="YYYY-MM-DD HH:mm:ss"
                                            format="YYYY-MM-DD HH:mm:ss"
                                            placeholder="请选择" />
                                    </a-form-item>
                                    <a-input-number v-model:value="item.startTime" class="w_full" :min="0" /> -->
                                                        
                                    <a-form-item  required label="任务开始时间">
                                        <a-date-picker show-time :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="item.startTime"
                                            class="w_full"
                                            valueFormat="YYYY-MM-DD HH:mm:ss"
                                            format="YYYY-MM-DD HH:mm:ss"
                                            placeholder="请选择" />
                                    </a-form-item>
                                </a-col>
                            </a-row>
                        </a-form-item>

                        <a-form-item required label="消息标题">
                            <a-input allowClear v-model:value="item.messageTitle"/>
                        </a-form-item>
                        <a-form-item required label="消息正文">
                            <a-textarea allowClear :rows="3" type="textarea" v-model:value="item.messageContent" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                        </a-form-item>
                    </a-collapse-panel>
                    <a-collapse-panel :key="index" v-if="actionTypeStatus(item.actionType)==6">
                        <template #header>
                            <h3>变更枚举值</h3>
                        </template>
                        <template #extra>
                            <a-button type="text" class="color-primary" size="small" @click="del(index)" v-if="actionList.length>1">删除</a-button>
                        </template>
                        <a-row :gutter="16" type="flex" align="middle">
                            <a-col :span="6">
                                <a-select :getPopupContainer="trigger => trigger.parentNode"
                                  v-model:value="item.updateField"
                                  class="w_full"
                                  placeholder="请选择"
                                  :options="ruleDict[modeName]">
                                </a-select>
                            </a-col>
                            <a-col :span="2" class="text-center">
                                变更为
                            </a-col>
                            <a-col :span="6">
                                <a-select :getPopupContainer="trigger => trigger.parentNode"
                                  v-model:value="item.updateValue"
                                  class="w_full"
                                  placeholder="请选择"
                                  notFoundContent="请先选择变更字段类型"
                                  :options="ruleDict[item.updateField]">
                                </a-select>
                            </a-col>
                        </a-row>
                    </a-collapse-panel>
                </template>
            </a-collapse>
        </a-form>
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
const actionList = ref(props.modelValue);
watch(()=> props.modelValue,() => {
    actionList.value = props.modelValue;
    collapseKey.value = props.modelValue.length>0 ? [0]:[];
})
watch(()=> props.modeName,() => {
    console.log('12'+props.modeName)
    actionList.value  = [];
    // collapseKey.value = [];
})
watch(actionList,
    (newVal, oldVal) => {
        validateField.value = getValidateField(newVal);
        emit('update:validateField', validateField.value)
        emit('update:modelValue',newVal);
    },
    {deep: true}
)
//表单完善状态
const getValidateField = (arr) => {
  const len = arr.length;
  if(len==0) return false
  let reg = true;
  for (let i = 0; i < len; i++) {
    const item = arr[i];
    const isValid =  !item.sendChannels.length || !item.sendObjects.length || !item.messageTitle  || !item.messageContent;
    if (isValid) {
      reg = false;
      break;
    }
  }
  return reg;
}
const validateField    = ref(getValidateField(actionList.value));
const del = (index)=>{
    actionList.value.splice(index,1);
}
const collapseKey = ref([0]);
const add         = (item)=>{
    if(item.value=='BIAN_GENG_ZHI'){
        return;
    }
    let obj = {
        actionType   : item.value,
    }
    if(item.status==5){
        obj.sendType     = 1;
        obj.sendTime     = 7;
        obj.sendUnit     = 'TIAN';
        obj.sendChannels = [];
        obj.sendObjects  = [];
    }
    actionList.value.push(obj);
    collapseKey.value.push(actionList.value.length-1);
}
const actionTypeStatus = (type)=>{
    let status = null;
    props.ruleDict['GUI_ZE_GUAN_LI_DONG_ZUO'].forEach((item, i) => {
        if(item.value==type){
            status = item.status; 
        }
    });
    return status;
}

</script>
<style scoped lang="less">
.action_box{
    background-color : #f0f2f5;
    padding          : 16px;
    border-radius    : 4px;
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
.add_btn{
    flex            : 1;
    display         : flex;
    align-items     : center;
    justify-content : center;
    font-size       : 16px;
    cursor          : pointer;
    height          : 48px;
    margin-bottom: 16px;
    background-color : #fff;
    // box-shadow      : 0 0 8px rgb(0 21 41 / 8%);
    border          : 1px solid #eee;
    border-radius   : 4px;
    
    &:hover{
        color            : @primary-color;
        background-color : #fffaf0;
    }
}
.add_disabled{
    background-color : #f7f7f7!important;
    color            : #999!important;
    cursor           : not-allowed!important;
}
</style>
