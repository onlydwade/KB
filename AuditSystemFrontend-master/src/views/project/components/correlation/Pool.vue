
<template>
    <a-form ref="formRef" layout="vertical" :model="editData">
        <a-table :columns="columns" :data-source="list" :pagination="false" :scroll="{ x: '100%' }">
            <template #headerCell="{ column }">
                <template v-if="column.required">
                    {{ column.title }}
                    <span class="color-danger">*</span>
                </template>
            </template>
            <template #emptyText>
                暂无数据，请添加
            </template>
            <template #bodyCell="{ column, text, record, index }">
                <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.dataIndex] : ''"
                    v-if="column.editType == 'text'">
                    <a-input v-if="editData[index]" allowClear v-model:value="editData[index][column.dataIndex]"
                        placeholder="请输入" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'investmentType'">
                    <a-select v-if="editData[index]" v-model:value="editData[index][column.editType]" class="w_full"
                        placeholder="请选择" :options="dict.options('TOU_ZI_LEI_XING')">
                    </a-select>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>

                <template v-if="column.key === 'action'">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small"
                            @click="edit(record, index)">编辑</a-button>
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small"
                            @click="del(record, index)">删除</a-button>
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small"
                            @click="clone(record, index)">克隆</a-button>
                    </template>
                    <a-space v-else>
                        <a-button type="primary" shape="circle" @click="editSubmit(record, index)" size="small">
                            <template #icon><check-outlined /></template>
                        </a-button>
                        <a-button shape="circle" @click="editCancel(record, index)" size="small">
                            <template #icon><close-outlined /></template>
                        </a-button>
                    </a-space>
                </template>
            </template>
            <template #summary v-if="!readOnly && addVisible">
                <a-table-summary>
                    <a-table-summary-row>
                        <template v-for="(item, index) in columns" :key="index">
                            <a-table-summary-cell v-if="item.key != 'action'">
                                <a-form-item :required="item.required" label=""
                                    :name="['add', item.editType == 'investmentType' ? item.editType : item.dataIndex]">
                                    <a-select v-if="item.editType == 'investmentType'"
                                        v-model:value="editData['add'][item.editType]" class="w_full" placeholder="请选择"
                                        :options="dict.options('TOU_ZI_LEI_XING')">
                                    </a-select>
                                    <a-input v-if="item.editType == 'text'" allowClear
                                        v-model:value="editData['add'][item.dataIndex]" placeholder="请输入" />
                                </a-form-item>
                            </a-table-summary-cell>
                        </template>
                        <a-table-summary-cell :index="2">
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
    <div class="add_btn" @click="addVisible = true" v-if="!readOnly && !addVisible">
        <plus-circle-outlined style="margin-right:8px;" />
        新增
    </div>
</template>
<script setup>
import api from '@/api/index';
import { useCorrelation } from './correlation';
import { useDictStore } from '@/store/dict';
import { watchArray } from '@vueuse/core'
const dict = useDictStore();
const props = defineProps({
    modelValue: {
        type: String,
        default: null,
    },
    projectId: Number,
    companyId: {
        type: Number,
        default: 0,
    },
    readOnly: {
        type: Boolean,
        default: false,
    },
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
} = useCorrelation(props.projectId, 'projectPool', { id: props.companyId, attr: 'companyId' });
watchArray(list, (newList) => {
    emit('update:modelValue', newList.length > 0 ? 'finished' : null);
}, { immediate: true, deep: true })

const columns = [
    {
        title: '项目全称',
        dataIndex: 'name',
        required: true,
        width: 150,
        editType: 'text'
    },
    {
        title: '项目进程',
        dataIndex: 'process',
        width: 150,
        editType: 'text'
    },
    {
        title: '项目所属部门',
        dataIndex: 'dept',
        width: 150,
        editType: 'text'
    },
    {
        title: '投资类型',
        dataIndex: 'investmentTypeStr',
        width: 150,
        editType: 'investmentType'
    },
    {
        title: '负责人',
        dataIndex: 'principal',
        width: 150,
        editType: 'text'
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        width: 170,
    },
]

onMounted(() => {
    if (!props.readOnly) {
        columns.push(
            {
                title: '操作',
                key: 'action',
                width: 180,
                fixed: 'right'
            }
        )
    }
    getList();
})
</script>
<style scoped lang="less">
.add_btn {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    cursor: pointer;
    margin-top: 8px;
    height: 48px;
    // box-shadow      : 0 0 8px rgb(0 21 41 / 8%);
    border: 1px solid #eee;
    border-radius: 4px;

    &:hover {
        color: @primary-color;
        background-color: #fffaf0;
    }
}
</style>
