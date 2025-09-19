
<template>
    <a-form ref="formRef" layout="vertical" :model="editData">
        <Title title="服务满意度信息"></Title>
        <a-table :columns="columns" :data-source="list" :pagination="false" :scroll="{ x: '100%' }">
            <template #emptyText>
                暂无数据，请添加
            </template>
            <template #bodyCell="{ column, text, record, index }">
                <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'satisfactionExplain'">
                    <a-input placeholder="请输入" v-if="editData[index]" v-model:value="editData[index][column.editType]" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'year'">
                    <a-date-picker v-if="editData[index]" v-model:value="editData[index][column.editType]" class="w_full"
                        picker="year" valueFormat="YYYY" />
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
                        <!-- <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="clone(record,index)">下载文件</a-button> -->
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
                                <a-form-item label=""> <!-- :required="item.required" :name="['add',item.editType]" -->
                                    <a-date-picker v-if="item.editType == 'year'"
                                        v-model:value="editData['add'][item.editType]" class="w_full" picker="year"
                                        valueFormat="YYYY" />
                                    <a-input placeholder="请输入" v-if="item.editType == 'satisfactionExplain'"
                                        v-model:value="editData['add'][item.editType]" />
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
    <div class="add_btn" @click="addVisible = true" v-if="!readOnly && !addVisible">
        <plus-circle-outlined style="margin-right:8px;" />
        新增
    </div>
</template>
<script setup>
import api from '@/api/index';
import { useCorrelation } from './serviceSatis';
import { useDictStore } from '@/store/dict';
import { watchArray } from '@vueuse/core'
const dict = useDictStore();
const emit = defineEmits(['update:modelValue']);
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
    type: {
        type: String,
        default: 'TOU',
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
} = useCorrelation(props.projectId, 'operation');
watchArray(list, (newList) => {
    emit('update:modelValue', newList.length > 0 ? 'finished' : null);
}, { immediate: true, deep: true })

const roleTypeDict = computed(() => {
    return dict.options('XIANG_MU_JUE_SE_LEI_XING').filter(item => {
        return item.value.startsWith(props.type);
    })
})
const columns = [
    {
        title: '年份',
        dataIndex: 'year',
        required: true,
        editType: 'year'
    },
    {
        title: '合同相对方满意度',
        dataIndex: 'satisfactionExplain',
        required: true,
        editType: 'satisfactionExplain'
    }
]
onMounted(() => {
    if (!props.readOnly) {
        columns.push(
            {
                title: '操作',
                key: 'action',
                width: 220,
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
