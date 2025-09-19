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
                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='text'">
                    <a-input v-if="editData[index]" allowClear v-model:value="editData[index][column.dataIndex]" placeholder="请输入"/>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.editType]:''" v-if="column.editType=='type'">
                    <a-select
                        v-if="editData[index]"
                        v-model:value="editData[index][column.editType]"
                        class="w_full"
                        placeholder="请选择"
                       :options="dict.options('ZHONG_DA_SHI_XIANG_LEI_XING')">
                    </a-select>
                    <a-select
                        v-else
                        :value="record.type"
                        disabled
                        class="w_full"
                        placeholder="请选择"
                       :options="dict.options('ZHONG_DA_SHI_XIANG_LEI_XING')">
                    </a-select>
                </a-form-item>
                <template v-if="column.key === 'action'">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="edit(record,index)" v-permissionInvestment="['biz:projectCompany:edit']">编辑</a-button>
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="del(record,index)" v-permissionInvestment="['biz:projectCompany:delete']">删除</a-button>
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
                                <a-form-item :required="item.required" :name="['add',item.editType=='text'?item.dataIndex:item.editType]">
                                    <a-input v-if="item.editType=='text'" allowClear v-model:value="editData['add'][item.dataIndex]" placeholder="请输入"/>
                                    <a-select
                                        v-if="item.editType=='type'"
                                        v-model:value="editData['add'][item.editType]"
                                        class="w_full"
                                        placeholder="请选择"
                                       :options="dict.options('ZHONG_DA_SHI_XIANG_LEI_XING')">
                                    </a-select>
                                </a-form-item>
                            </a-table-summary-cell>
                        </template>
                        <a-table-summary-cell :index="3">
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
import { watchArray }     from '@vueuse/core';

const dict  = useDictStore();
const emit  = defineEmits(['update:modelValue']);
const props = defineProps({
    modelValue : {
        type    : Array,
        default : [],
    },
    companyId : {
        type    : Number,
        default : 0,
    },
    reportId:{
        type    : Number,
        default : 0,
    },
    readOnly : {
        type    : Boolean,
        default : false,
    },
})
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
} = useCorrelation(props.companyId,'companyOperateReportItem',{attr:'reportId',id:props.reportId},0,props.reportId?false:true);

watchArray(list, (newList) => {
    emit('update:modelValue',newList);
},{deep: true})

const columns = [
    {
        title     : '类型',
        dataIndex : 'type',
        required  : true,
        editType  : 'type'
    },
    {
        title     : '事项明细',
        dataIndex : 'detail',
        editType  : 'text'
    },
    {
        title     : '事项描述',
        dataIndex : 'description',
        editType  : 'text'
    },
    {
        title     : '处理建议',
        dataIndex : 'processPropose',
        editType  : 'text'
    },
]
onMounted(() => {
    if(!props.readOnly){
        columns.push(
            {
                title : '操作',
                key   : 'action',
                width : 120,
                fixed : 'right'
            }
        )
    }
    if(props.reportId){
        getList();
    }
})
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
