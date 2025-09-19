
<template>
    <a-form ref="formDetailRef" layout="vertical" :model="editData">
        <Title title="工作进展" style="margin-left: -16px;"></Title>
        <a-table :columns="columns" :data-source="value" :pagination="false" :scroll="{ x: '100%' }">
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
                    v-if="column.editType == 'number'">
                    <a-input-number v-if="editData[index]" v-model:value="editData[index][column.dataIndex]" :min="0"
                        class="w_full" placeholder="请输入" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>

                <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.dataIndex] : ''"
                    v-if="column.editType == 'taskStatus'">
                    <a-select v-if="editData[index]" v-model:value="editData[index][column.dataIndex]" class="w_full"
                        placeholder="请选择" :options="taskStatusList">
                    </a-select>
                    <template v-else>
                        {{ text == "CHI_XUN_GEN_JIN" ? '持续跟进' : (text == "TING_ZHI" ? '停止' : '结束跟进') }}
                    </template>
                </a-form-item>

                <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.dataIndex] : ''"
                    v-if="column.editType == 'text'">
                    <a-input v-if="editData[index]" allowClear v-model:value="editData[index][column.dataIndex]"
                        placeholder="请输入" />
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
                                <a-form-item label="" :required="item.required" :name="['add', item.dataIndex]">
                                    <a-input-number v-if="item.editType == 'number'"
                                        v-model:value="editData['add'][item.dataIndex]" :min="0" class="w_full"
                                        placeholder="请输入" />
                                    <a-select v-if="item.editType == 'taskStatus'"
                                        v-model:value="editData['add'][item.dataIndex]" class="w_full" placeholder="请选择"
                                        :options="taskStatusList">
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
    <!-- 
    <p class="follow_text"  v-for="(detail,fileIndex) in detailList" readOnly :key="fileIndex" :fileData="detail">
                           {{fileIndex+1}}: {{detail.workSummary}}|{{detail.taskStatus}}
                            <br/>
                            {{detail.followStatus}}
                        </p> -->
</template>
<script setup>
import api from '@/api/index';
import { useCorrelation } from './correlation';
import { useDictStore } from '@/store/dict';
import { watchArray } from '@vueuse/core'
const dict = useDictStore();
const props = defineProps({
    // detailList: {
    //     type: Array,
    //     default: [],
    // },
    value: {
        type: Array,
        default: [],
    },
    recordId: Number,
    readOnly: {
        type: Boolean,
        default: false,
    }
})


const emit = defineEmits(['update:modelValue'])
const {
    formDetailRef,
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
} = useCorrelation(props.recordId, 'followLog');

watchArray(list, (newList) => {
    emit('update:modelValue', newList);
}, { immediate: true, deep: true })

watchArray(() => props.value, () => {
    getList(props.value)
}, { immediate: true, deep: true })

const columns = [
    {
        title: '工作摘要',
        dataIndex: 'workSummary',
        required: true,
        editType: 'text'
    },
    {
        title: '任务情况',
        dataIndex: 'taskStatus',
        required: true,
        editType: 'taskStatus'
    },
    {
        title: '推进状态',
        dataIndex: 'followStatus',
        required: true,
        editType: 'text'
    },
    {
        title: '专班建立',
        dataIndex: 'teamEstablish',
        required: true,
        editType: 'text'
    },
    {
        title: '负责人',
        dataIndex: 'head',
        required: true,
        editType: 'text'
    },
]

const taskStatusList = computed(() => {
    return dict.options('REN_WU_QING_KUANG');
})

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
    getList(props.value)
})

defineExpose({ list })
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
