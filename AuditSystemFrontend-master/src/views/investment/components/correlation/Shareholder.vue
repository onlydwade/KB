
<template>
    <a-form
    ref="formRef"
    layout="vertical"
    :model="editData">
        <a-table :columns="columns" :data-source="list" :pagination="false" :scroll="{ x: '100%' }">
            <template #headerCell="{ column }">
                <template v-if="column.required">
                    {{column.title}}
                    <span class="color-danger" v-if="!readOnly">*</span>
                </template>
            </template>
            <template #emptyText>
                暂无数据，请添加
            </template>
            <template #bodyCell="{ column, text, record,index}">
                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='type'">
                    <a-select
                        v-if="editData[index]"
                        v-model:value="editData[index][column.dataIndex]"
                        class="w_full"
                        placeholder="请选择"
                        :options="[{label:'企业法人',value:1},{label:'自然人',value:2}]">
                    </a-select>
                    <template v-else>
                        {{ text==1?'企业法人':'自然人' }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='currencyType'">
                    <a-select
                        v-if="editData[index]"
                        v-model:value="editData[index][column.dataIndex]"
                        class="w_full"
                        placeholder="请选择"
                       :options="[{label:'货币',value:1},{label:'非货币',value:2}]">
                    </a-select>
                    <template v-else>
                        {{ {1:'货币',2:'非货币'}[text] }}
                    </template>
                </a-form-item>

                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='persent'">
                    <a-input-number v-if="editData[index]" v-model:value="editData[index][column.dataIndex]" :min="0" :max="100" class="w_full" placeholder="请输入"/>
                    <template v-else>
                        {{ text }}%
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='number'">
                    <a-input-number v-if="editData[index]" v-model:value="editData[index][column.dataIndex]" :min="0" class="w_full" placeholder="请输入"/>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>

                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='text'">
                    <a-input v-if="editData[index]" allowClear v-model:value="editData[index][column.dataIndex]" placeholder="请输入"/>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='companyNo'"
                    :rules="[{required:true,validator: (editData[index]?editData[index]['type']:1) ==1?companyNoRule:companyNoRuleID}]">
                    <a-input v-if="editData[index]" allowClear v-model:value="editData[index][column.dataIndex]" placeholder="请输入"/>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='time'">
                    <a-date-picker v-if="editData[index]"
                        v-model:value="editData[index][column.dataIndex]"
                        class="w_full"
                        valueFormat="YYYY-MM-DD 00:00:00"
                        :disabled-date="disabledDate"
                        format="YYYY-MM-DD"
                        placeholder="请选择" />
                    <template v-else>
                        {{ dateFormat(text,'YYYY-MM-DD') }}
                    </template>
                </a-form-item>
                <template v-if="column.key === 'action'">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="edit(record,index)" v-permissionInvestment="['biz:projectCompany:edit']">编辑</a-button>
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="del(record,index)" v-permissionInvestment="['biz:projectCompany:delete']">删除</a-button>
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="clone(record,index)" v-permissionInvestment="['biz:projectCompany:add']">克隆</a-button>
                    </template>
                    <a-space v-else>
                        <a-button type="primary" shape="circle" @click="editSubmit(record,index)" size="small">
                            <template #icon><check-outlined /></template>
                        </a-button>
                        <a-button shape="circle" @click="editCancel(record,index)" size="small">
                            <template #icon><close-outlined /></template>
                        </a-button>
                    </a-space>
                </template>
            </template>
            <template #summary v-if="!readOnly&&addVisible">
                <a-table-summary>
                    <a-table-summary-row>
                        <template v-for="(item,index) in columns" :key="index">
                            <a-table-summary-cell v-if="item.key != 'action'">
                                <a-form-item v-if="item.editType=='companyNo'" :required="item.required" :name="['add',item.dataIndex]" :rules="[{required:true,validator:editData['add']['type']==1?companyNoRule:companyNoRuleID}]">
                                    <a-input allowClear v-model:value="editData['add'][item.dataIndex]" placeholder="请输入"/>
                                </a-form-item>
                                <a-form-item v-else :required="item.required" :name="['add',item.dataIndex]">
                                    <a-select
                                        v-if="item.editType=='type'"
                                        v-model:value="editData['add'][item.dataIndex]"
                                        class="w_full"
                                        placeholder="请选择"
                                        :options="[{label:'企业法人',value:1},{label:'自然人',value:2}]">
                                    </a-select>
                                    <a-select
                                        v-if="item.editType=='currencyType'"
                                        v-model:value="editData['add'][item.dataIndex]"
                                        class="w_full"
                                        placeholder="请选择"
                                        :options="[{label:'货币',value:1},{label:'非货币',value:2}]">
                                    </a-select>

                                    <a-input-number v-if="item.editType=='persent'" v-model:value="editData['add'][item.dataIndex]" :min="0" :max="100"  class="w_full" placeholder="请输入"/>
                                    <a-input-number v-if="item.editType=='number'" v-model:value="editData['add'][item.dataIndex]" :min="0" class="w_full" placeholder="请输入"/>

                                    <a-input v-if="item.editType=='text'" allowClear v-model:value="editData['add'][item.dataIndex]" placeholder="请输入"/>

                                    <a-date-picker v-if="item.editType=='time'"
                                        v-model:value="editData['add'][item.dataIndex]"
                                        class="w_full"
                                        :disabled-date="disabledDate"
                                        valueFormat="YYYY-MM-DD 00:00:00"
                                        format="YYYY-MM-DD"
                                        placeholder="请选择" />
                                </a-form-item>
                            </a-table-summary-cell>
                        </template>
                        <a-table-summary-cell :index="9">
                            <a-space>
                                <a-button type="primary" shape="circle" @click="addSubmit" size="small">
                                    <template #icon><check-outlined /></template>
                                </a-button>
                                <a-button shape="circle" @click="addCancel" size="small">
                                    <template #icon><close-outlined /></template>
                                </a-button>
                            </a-space>
                        </a-table-summary-cell>
                    </a-table-summary-row>
                </a-table-summary>
            </template>
        </a-table>
    </a-form>
    <div class="add_btn" @click="addVisible=true" v-if="!readOnly&&!addVisible" v-permissionInvestment="['biz:projectCompany:add']">
        <plus-circle-outlined style="margin-right:8px;"/>
        新增
    </div>
</template>
<script setup>
import api                from '@/api/index';
import { useCorrelation } from './correlation';
import { useDictStore }   from '@/store/dict';
import { watchArray }     from '@vueuse/core'
import dayjs from 'dayjs';
const dict  = useDictStore();
const props = defineProps({
    modelValue : {
        type    : String,
        default : null,
    },
    projectId : Number,
    companyId : {
        type    : Number,
        default : 0,
    },
    readOnly : {
        type    : Boolean,
        default : false,
    },
    columnsType :{
      type    : Number,
      default : 0,
    }
})
const emit = defineEmits(['update:modelValue'])
const {
    formRef,
    list,
    getList,
    addVisible,
    addSubmit,
    addCancel,
    editData,
    edit,
    editSubmit,
    editCancel,
    del,
    clone
} = useCorrelation(props.projectId,'projectCompanyShareholder',{id:props.companyId,attr:'companyId'},0,false,[{name:'统一信用代码',key:'companyNo'}]);
watchArray(list, (newList) => {
    emit('update:modelValue',newList.length>0?'finished':null);
},{immediate:true,deep: true})
const columns = props.companyId==0 && props.columnsType==0?[
        {
            title     : '股东名称',
            dataIndex : 'name',
            required  : true,
            width     : 150,
            editType  : 'text'
        },
        {
            title     : '股东类型',
            dataIndex : 'type',
            required  : true,
            width     : 150,
            editType  : 'type'
        },
        {
            title     : '持股比例',
            dataIndex : 'rate',
            required  : true,
            width     : 150,
            editType  : 'persent'
        },
        {
            title     : '认缴出资金额（元）',
            dataIndex : 'investmentAmount',
            required  : true,
            width     : 180,
            editType  : 'number'
        },
    ]:[
    {
        title     : '股东名称',
        dataIndex : 'name',
        required  : true,
        width     : 150,
        editType  : 'text'
    },
    {
        title     : '股东类型',
        dataIndex : 'type',
        required  : true,
        width     : 150,
        editType  : 'type'
    },
    {
        title     : '统一信用代码',
        dataIndex : 'companyNo',
        required  : true,
        width     : 200,
        editType  : 'companyNo'
    },
    {
        title     : '出资方式',
        dataIndex : 'currencyType',
        required  : true,
        width     : 150,
        editType  : 'currencyType'
    },
    {
        title     : '持股比例',
        dataIndex : 'rate',
        required  : true,
        width     : 150,
        editType  : 'persent'
    },
    {
        title     : '认缴出资金额（元）',
        dataIndex : 'investmentAmount',
        required  : true,
        width     : 180,
        editType  : 'number'
    },
    {
        title     : '出资截止日期',
        dataIndex : 'investmentEndTime',
        required  : true,
        width     : 150,
        editType  : 'time'
    },
    {
        title     : '实缴出资金额（元）',
        dataIndex : 'actualInvestmentAmount',
        required  : true,
        width     : 180,
        editType  : 'number'
    },
    {
        title     : '备注',
        dataIndex : 'remark',
        width     : 180,
        editType  : 'text'
    },
]
const disabledDate = (current) => {
  return current && current < dayjs().startOf('day');
};
onMounted(() => {
    if(!props.readOnly){
        columns.push(
            {
                title : '操作',
                key   : 'action',
                width : 180,
                fixed : 'right'
            }
        )
    }
    getList();
})

const companyNoRule = (rule, value, callback) => {
  const regex = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;
  if (rule.required && !regex.test(value)) {
    return new Promise((resolve, reject) => {
      reject('请输入有效社会信用代码(18位)');
    });
  }
  return Promise.resolve();
};

const companyNoRuleID = (rule, value, callback)=>{
  let pattern = /^[1-9]\d{5}(?:18|19|20)\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\d|30|31)\d{3}[\dXx]$/
  if(rule.required && !pattern.test(value) ){ 
    return new Promise((resolve, reject) => {
      reject('请输入有效的身份证号')
    });
  }
  return Promise.resolve();
}
</script>
<style scoped lang="less">
.add_btn{
    display         : flex;
    align-items     : center;
    justify-content : center;
    font-size       : 16px;
    cursor          : pointer;
    margin-top      : 8px;
    height          : 48px;
    // box-shadow      : 0 0 8px rgb(0 21 41 / 8%);
    border:1px solid #eee;
    border-radius   : 4px;

    &:hover{
        color            : @primary-color;
        background-color : #fffaf0;
    }
}
</style>
