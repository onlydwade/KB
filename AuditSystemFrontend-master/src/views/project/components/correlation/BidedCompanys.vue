
<template>
    <a-form ref="formRef" layout="vertical" :model="editData">
        <a-table :columns="columns" :data-source="sortsList" :pagination="false" :scroll="{ x: '100%' }">
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
                <template v-if="column.key === 'type'">
                    {{ record.type == 1 ? '我方' : '竞方' }}
                </template>
                <template v-if="column.key === 'selected'">
                    <a-checkbox :disabled="readOnly" :checked="record.win" size="large"
                        @click="select(record,index)"></a-checkbox>
                </template>
                <a-form-item :rules="{ required: column.required, message: '请输入' }"
                    :name="editData[index] ? [index, column.dataIndex] : ''" v-if="column.editType == 'number'">
                    <a-input-number v-if="editData[index]"                          
                        v-model:value="editData[index][column.dataIndex]" :min="0" class="w_full" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :rules="{ required: column.required, message: '请输入' }"
                    :name="editData[index] ? [index, column.dataIndex] : ''" v-if="column.editType == 'text'">
                    <a-input v-if="editData[index]"
                        :disabled="record.type == 1 && ['company', 'bidingAmount'].includes(column.dataIndex)" allowClear
                        v-model:value="editData[index][column.dataIndex]" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <template v-if="column.key === 'action'">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small"
                            @click="edit(record, index)">编辑</a-button>
                        <a-button type="text" class="color-primary" size="small" @click="del(record, index)"
                            v-if="record.type == 2 && !readOnly">删除</a-button>
                        <a-button type="text" class="color-primary" size="small" @click="clone(record, index)"
                            v-if="record.type == 2 && !readOnly">克隆</a-button>
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
                                <a-form-item :rules="{ required: item.required, message: '请输入' }" :required="item.required"
                                    :name="['add', item.dataIndex]">
                                    <a-input-number v-if="item.editType == 'number'"
                                        v-model:value="editData['add'][item.dataIndex]" :min="0" class="w_full" />
                                    <a-input v-if="item.editType == 'text'" allowClear
                                        v-model:value="editData['add'][item.dataIndex]" />
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
    <div class="add_btn" @click="add" v-if="!readOnly && !addVisible">
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
    readOnly: {
        type: Boolean,
        default: false,
    },
})
const emit = defineEmits(['update:modelValue', 'change'])
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
} = useCorrelation(props.projectId, 'projectBid');
const add = () => {
    editData['add'].type = 2;
    addVisible.value = true;
}
const rowSpan = reactive({
    start: 0,
    span0: 1,
    span1: 1,
})
const getrowSpan = () => {
    let type0 = 0;
    let type1 = 0;
    list.value.forEach((item, i) => {
        if (item.type == 1) {
            type1++;
        } else {
            type0++;
        }
    });
    rowSpan.start = type1;
    rowSpan.span0 = type0;
    rowSpan.span1 = type1;
}

watchArray(list, (newList) => {
    emit('update:modelValue', newList.length > 0 ? 'finished' : null);
    for (var i = 0; i < newList.length; i++) {
        if (newList[i].win == 1) {
            emit('change', newList[i]);
            break;
        }
    }
    getrowSpan();
}, { immediate: true, deep: true })

const sortsList = computed(() => {
    return list.value.sort((a, b) => {
        return a.type - b.type;
    })
})
const columns = [
    {
        title: '竞标方',
        key: 'type',
        width: 150,
        customCell: (_, index) => ({
            rowSpan: index == 0 ? (rowSpan.span1 == 0 ? rowSpan.span0 : rowSpan.span1) : index == rowSpan.start ? rowSpan.span0 : 0
        }),
    },
    {
        title: '中标单位',
        key: 'selected',
        align: 'center',
        width: 120,
    },
    {
        title: '参标单位',
        dataIndex: 'company',
        required: true,
        editType: 'text'
    },
    {
        title: '投标报价(元)',
        dataIndex: 'bidingAmount',
        required: true,
        editType: 'number'
    },
    {
        title: '投标排名',
        dataIndex: 'sorts',
        required: true,
        editType: 'number'
    },
    {
        title: '投标得分',
        dataIndex: 'score',
        editType: 'number'
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


const select = async (row,index) => {
    if (row.win || props.readOnly) {
        return;
    }
    list.value.forEach((item, i) => {
        if (item.win) {
            item.win = false;
            api.project.correlationEdit(item, 'projectBid');
        }
    });
    row.win = true;
    let res2 = await api.project.correlationEdit(row, 'projectBid');
    emit('change', row);
}
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
