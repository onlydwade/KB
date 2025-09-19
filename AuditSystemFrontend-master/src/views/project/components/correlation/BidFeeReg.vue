
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
                <a-form-item label="" validateTrigger="['change', 'blur']" :required="column.required"
                    :name="editData[index] ? [index, (editData[index].freeType.includes('QI_TA') ? 'freeTypeOther' : 'freeType')] : ''"
                    v-if="column.editType == 'freeType'">
                    <template v-if="editData[index]">
                        <a-input v-model:value="editData[index]['freeTypeOther']"
                            v-if="(editData[index][column.editType] || '').includes('QI_TA')" placeholder="请输入">
                            <template #addonBefore>
                                <a-select v-model:value="editData[index][column.editType]" style="width: 90px"
                                    :dropdownMatchSelectWidth="false" :options="freeTypes">
                                </a-select>
                            </template>
                        </a-input>
                        <a-select v-else v-model:value="editData[index][column.editType]" placeholder="请选择" class="w_full"
                            :options="freeTypes">
                        </a-select>
                    </template>
                    <template v-else>
                        {{ (record.freeType || '').includes('QI_TA') ? record.freeTypeOther : text }}
                    </template>
                </a-form-item>

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
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.dataIndex] : ''"
                    v-if="column.editType == 'recoverStatus'">
                    <a-select v-if="editData[index]" v-model:value="editData[index][column.dataIndex]" class="w_full"
                        placeholder="请选择" :options="dict.options('QIAN_KUAN_ZHUANG_TAI')">
                    </a-select>
                    <template v-else>
                        <span v-if="text == 'YI_GUI_HUAN'">已归还</span>
                        <span v-if="text == 'WEI_GUI_HUAN'">未归还</span>
                    </template>
                </a-form-item>
                <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.dataIndex] : ''"
                    v-if="column.editType == 'deduct'">
                    <a-select v-if="editData[index]" v-model:value="editData[index][column.dataIndex]" class="w_full"
                        placeholder="请选择" :options="dict.options('HUI_KUAN_ZHUANG_TAI')">
                    </a-select>
                    <template v-else>
                        <span v-if="text == 'WEI_TUI_HUI'">未退回</span>
                        <span v-if="text == 'YI_TUI_HUI_QUAN_FEI_KUAN'">已退回非全款</span>
                        <span v-if="text == 'YI_TUI_HUI_QUAN_KUAN'">已退回全款</span>
                    </template>
                </a-form-item>
                <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.dataIndex] : ''"
                    v-if="column.editType == 'time'">
                    <a-date-picker v-if="editData[index]" v-model:value="editData[index][column.dataIndex]" class="w_full"
                        valueFormat="YYYY-MM-DD 00:00:00" format="YYYY-MM-DD" placeholder="请选择" />
                    <template v-else>
                        {{ dateFormat(text, 'YYYY-MM-DD') }}
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
                                <a-form-item validateTrigger="['change', 'blur']" :required="item.required" label=""
                                    v-if="item.editType == 'freeType'"
                                    :name="['add', (editData['add'][item.editType] || '').includes('QI_TA') ? 'freeTypeOther' : 'freeType']">
                                    <a-input v-model:value="editData['add']['freeTypeOther']"
                                        v-if="(editData['add'][item.editType] || '').includes('QI_TA')" placeholder="请输入">
                                        <template #addonBefore>
                                            <a-select v-model:value="editData['add'][item.editType]" style="width: 90px"
                                                :dropdownMatchSelectWidth="false" :options="freeTypes">
                                            </a-select>
                                        </template>
                                    </a-input>
                                    <a-select v-else v-model:value="editData['add'][item.editType]" placeholder="请选择"
                                        class="w_full" :options="freeTypes">
                                    </a-select>
                                </a-form-item>
                                <a-form-item :required="item.required" v-else :name="['add', item.dataIndex]">
                                    <a-input-number v-if="item.editType == 'number'"
                                        v-model:value="editData['add'][item.dataIndex]" :min="0" class="w_full"
                                        placeholder="请输入" />
                                    <a-input v-if="item.editType == 'text'" allowClear
                                        v-model:value="editData['add'][item.dataIndex]" placeholder="请输入" />
                                    <a-select v-if="item.editType == 'recoverStatus'"
                                        v-model:value="editData['add'][item.dataIndex]" class="w_full" placeholder="请选择"
                                        :options="dict.options('QIAN_KUAN_ZHUANG_TAI')">
                                    </a-select>
                                    <a-select v-if="item.editType == 'deduct'"
                                        v-model:value="editData['add'][item.dataIndex]" class="w_full" placeholder="请选择"
                                        :options="dict.options('HUI_KUAN_ZHUANG_TAI')">
                                    </a-select>
                                    <a-date-picker v-if="item.editType == 'time'"
                                        v-model:value="editData['add'][item.dataIndex]" class="w_full"
                                        valueFormat="YYYY-MM-DD 00:00:00" format="YYYY-MM-DD" placeholder="请选择" />
                                </a-form-item>
                            </a-table-summary-cell>
                        </template>
                        <a-table-summary-cell :index="13">
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
} = useCorrelation(props.projectId, 'projectBidFeeRegistration');
watchArray(list, (newList) => {
    emit('update:modelValue', newList.length > 0 ? 'finished' : null);
}, { immediate: true, deep: true })

const columns = [
    {
        title: '费用名称',
        dataIndex: 'freeTypeStr',
        required: true,
        width: 220,
        editType: 'freeType'
    },
    {
        title: '费用金额(元)',
        dataIndex: 'freeAmount',
        required: true,
        width: 150,
        editType: 'number'
    },
    {
        title: '支付时间',
        dataIndex: 'paymentTime',
        required: true,
        width: 180,
        editType: 'time'
    },
    {
        title: '实际缴纳单位',
        dataIndex: 'paymentCompany',
        required: true,
        width: 180,
        editType: 'text'
    },
    {
        title: '收款单位',
        dataIndex: 'payeeCompany',
        required: true,
        width: 150,
        editType: 'text'
    },
    {
        title: '欠款单位',
        dataIndex: 'arrearageCompany',
        width: 150,
        editType: 'text'
    },
    {
        title: '欠款状态',
        dataIndex: 'recoverStatus',
        width: 120,
        editType: 'recoverStatus'
    },
    // {
    //     title     : '是否需追回',
    //     dataIndex : 'recover',
    //     width     : 120,
    //     editType  : 'radio'
    // },
    {
        title: '回款金额（元）',
        dataIndex: 'deductAmount',
        width: 150,
        editType: 'number'
    },
    {
        title: '回款状态',
        dataIndex: 'deduct',
        width: 160,
        editType: 'deduct'
    },
    {
        title: '回款时间',
        dataIndex: 'recoverTime',
        width: 180,
        editType: 'time'
    },
    // {
    //     title     : '扣除流向',
    //     dataIndex : 'deductFlow',
    //     width     : 150,
    //     editType  : 'text'
    // },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 150,
        editType: 'text'
    },
    {
        title: '登记人',
        dataIndex: ['createUser', 'realname'],
        width: 150,
    },
    {
        title: '登记时间',
        dataIndex: 'createTime',
        width: 180,
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

const freeTypes = computed(() => {
    let selects = list.value.map((item, i) => {
        return item.freeType;
    });
    for (let key in editData) {
        let type = editData[key].freeType;
        if (!selects.includes(type)) {
            selects.push(type);
        }
    }
    return dict.options('TOU_BIAO_FEI_YONG_LEI_XING').map(item => {
        return {
            value: item.value,
            label: item.label,
            disabled: selects.includes(item.value) && item.value != 'TOU_BIAO_FEI_YONG_QI_TA'
        }
    })
})
const radioShiFou = [{ value: 1, label: '是' }, { value: 0, label: '否' }]
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
