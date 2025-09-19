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
            <!-- 编辑行 -->
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
                    v-if="column.editType == 'text'">
                    <a-input v-if="editData[index]" allowClear v-model:value="editData[index][column.dataIndex]"
                        placeholder="请输入" />
                    <template v-else>
                        {{ text }}{{ editData[index] }}
                    </template>
                </a-form-item>
                <template v-if="column.key === 'action'">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small"
                            @click="moveUp(record, index)">上移</a-button>
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
            <!-- 新增行 -->
            <template #summary v-if="!readOnly && addVisible">
                <a-table-summary>
                    <a-table-summary-row>
                        <template v-for="(item, index) in columns" :key="index">
                            <a-table-summary-cell v-if="item.key != 'action'">
                                <a-form-item label="" :required="item.required" :name="['add', item.dataIndex]">
                                    <a-input-number v-if="item.editType == 'number'"
                                        v-model:value="editData['add'][item.dataIndex]" :min="0" class="w_full"
                                        placeholder="请输入" />
                                    <a-input v-if="item.editType == 'text'" allowClear
                                        v-model:value="editData['add'][item.dataIndex]" placeholder="请输入" />
                                    <a-select  :dropdownMatchSelectWidth="false"  v-if="item.editType == 'select'"
                                        mode="tags"
                                        v-model:value="editData['add'][item.dataIndex]" placeholder="请选择"
                                        @change="handleChange">
                                        <a-select-option v-for="el in proOptions" :key="el.projectId"
                                            :value="el.projectId">{{ el.projectName }}</a-select-option>
                                    </a-select>
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
    <div class="add_btn" @click="addPro" v-if="!readOnly && !addVisible">
        <plus-circle-outlined style="margin-right:8px;" />
        新增项目
    </div>
</template>
<script setup>
import api from '@/api/index';
import { useCorrelation } from './correlationWinBidder';
import { watchArray } from '@vueuse/core'
const props = defineProps({
    modelValue: {
        type: String,
        default: null,
    },
    customerId: Number,
    readOnly: {
        type: Boolean,
        default: false,
    },
    id: Number,
    deptId: Number,
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
    moveUp,
    editSubmit,
    editCancel,
    del,
    proOptions,
    columns
} = useCorrelation(props.customerId, 'customerContacts');
watchArray(list, (newList) => {
    emit('update:modelValue', newList.length > 0 ? 'finished' : null);
}, { immediate: true, deep: true })


const handleChange = (val) => {
    debugger;
    if(val.length == 0){
        return;
    }
    if(val.length > 1){
        editData['add'].projectId = val[0];
        editData['add'].projectName = editData['add'].projectName[0];
        return;
    }
    let findItem = proOptions.value.find(el => el.projectId === val[0])
    if (findItem) {
        editData['add'].projectId = findItem.projectId
        editData['add'].projectName = findItem.projectName
        editData['add'].bidedAmount = findItem.bidedAmount
        editData['add'].deptId = findItem.deptId
        editData['add'].deptName = findItem.deptName
        editData['add'].year = findItem.year
    }
}

const addPro = () => {
    columns.value[0].editType = 'select'
    let postData = {
        desc: ["createTime"],
        params: {},
    };
    if(props.deptId){
        postData.params['companyId'] = props.deptId;
    }
    api.workBrief.winBidderGetList(postData).then(res => {
        if (res.code == 200) {
            proOptions.value = res.data;
        }
    });
    addVisible.value = true
}
onMounted(() => {
    if (!props.readOnly) {
        columns.value.push(
            {
                title: '操作',
                key: 'action',
                width: 180,
                fixed: 'right'
            }
        )
    }
    if (!props.id) {
        // getList();
    }
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
