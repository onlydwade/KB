
<template>
    <a-form ref="formRef" layout="vertical" :model="editData">
        <Title title="经营收支情况"></Title>
        <a-row :gutter="24">
            <a-col :xxl="8" :lg="8" :sm="12">
                <a-form-item required label="项目名称">
                    <a-input disabled v-model:value="paramsData.projectName" placeholder="请输入" />
                </a-form-item>
            </a-col>
            <a-col :xxl="8" :lg="8" :sm="12">
                <a-form-item label="项目业态" name="title">
                    <a-input v-model:value="paramsData.businessTypeStr" disabled placeholder="请输入" />
                </a-form-item>
            </a-col>
        </a-row>
        <a-table :columns="columns" :data-source="list" :pagination="false" :scroll="{ x: '100%' }">
            <template #emptyText>
                暂无数据，请添加
            </template>
            <template #bodyCell="{ column, text, record, index }">
                <template v-if="column.key === 'documentTemplateList'">
                    <template v-for="(item, tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                        <FileItem v-for="(file, findex) in (item.projectCompanyDocumentList || [])"
                            :key="index + '_' + tindex + '_' + findex" :fileData="file.docmentObject" :readOnly="true"
                            :canDel=1 @fileDel="fileDel(index, record.projectCompanyDocumentList, item)" />
                    </template>
                </template>
                <a-form-item :required="column.required" :label="column.title"
                    :name="editData[index] ? [index, column.editType] : ''" v-if="column.editType == 'operateYear'">
                    <a-date-picker v-if="editData[index]" v-model:value="editData[index][column.editType]"
                        @change="() => { editData[index].roleKey = null; }" class="w_full" picker="year"
                        valueFormat="YYYY" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" label="" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'closeRate'">
                    <a-input placeholder="请输入" v-if="editData[index]" v-model:value="editData[index][column.editType]" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'practicalCloseMoney'">
                    <a-input placeholder="请输入" v-if="editData[index]" v-model:value="editData[index][column.editType]" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'outerBusinessIncome'">
                    <a-input placeholder="请输入" v-if="editData[index]" v-model:value="editData[index][column.editType]" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'cost'">
                    <a-input placeholder="请输入" v-if="editData[index]" v-model:value="editData[index][column.editType]" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'profit'">
                    <a-input placeholder="请输入" v-if="editData[index]" v-model:value="editData[index][column.editType]" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'roleType'">
                    <a-select v-if="editData[index]" v-model:value="editData[index][column.editType]" @change="() => {
                        editData[index].roleKey = null;
                    }" class="w_full" placeholder="请选择" :options="roleTypeDict">
                    </a-select>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'quarterStage'">
                    <a-select v-if="editData[index]" v-model:value="editData[index][column.editType]" @change="() => {
                        editData[index].roleKey = null;
                    }" class="w_full" placeholder="请选择"
                        :options="[{ label: '第一季度', value: 1 }, { label: '第二季度', value: 2 }, { label: '第三季度', value: 3 }, { label: '第四季度', value: 4 }]">
                    </a-select>
                    <template v-else>
                        {{ record.quarterStage == 1 ? '第一季度' : record.quarterStage == 2 ? '第二季度' : record.quarterStage == 3
                            ? '第三季度' : record.quarterStage == 4 ? '第四季度' : '' }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'roleKey'">
                    <a-select v-if="editData[index]" v-model:value="editData[index][column.editType]" class="w_full"
                        placeholder="请选择" :options="dict.options(editData[index].roleType)">
                    </a-select>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <template v-if="column.key === 'action'">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary"
                            v-if="(!readOnly && hasPermission(['biz:projecOperation:edit'])) || (!readOnly && showStr == 'true')"
                            size="small" @click="edit(record, index)">编辑</a-button>
                        <a-button type="text" class="color-primary"
                            v-if="(!readOnly && hasPermission(['biz:projecOperation:delete'])) || (!readOnly && showStr == 'true')"
                            size="small" @click="del(record, index)">删除</a-button>
                        <UploadBtn
                            v-if="(!readOnly && tempId != 0 && hasPermission(['biz:projecOperation:upload'])) || (!readOnly && tempId != 0 && showStr == 'true')"
                            @change="uploadChange($event, record.id)">
                            <a-button type="text" class="color-primary" size="small">上传文件</a-button>
                        </UploadBtn>
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
                                <a-form-item label="">
                                    <!-- :required="item.required" :name="['add',item.editType]" -->
                                    <!-- <UserSelect v-if="item.editType=='operateYear'" v-model="editData['add'][item.editType]"  @change="userChange($event,editData['add'])"  :users="editData['add'].user"/> -->
                                    <a-date-picker v-if="item.editType == 'operateYear'"
                                        v-model:value="editData['add'][item.editType]" class="w_full" picker="year"
                                        valueFormat="YYYY" />
                                    <a-input placeholder="请输入" v-if="item.editType == 'closeRate'"
                                        v-model:value="editData['add'][item.editType]" />
                                    <a-input placeholder="请输入" v-if="item.editType == 'practicalCloseMoney'"
                                        v-model:value="editData['add'][item.editType]" />
                                    <a-input placeholder="请输入" v-if="item.editType == 'outerBusinessIncome'"
                                        v-model:value="editData['add'][item.editType]" />
                                    <a-input placeholder="请输入" v-if="item.editType == 'cost'"
                                        v-model:value="editData['add'][item.editType]" />
                                    <a-input placeholder="请输入" v-if="item.editType == 'profit'"
                                        v-model:value="editData['add'][item.editType]" />
                                    <a-select v-if="item.editType == 'roleType'"
                                        v-model:value="editData['add'][item.editType]" class="w_full" @change="() => {
                                            editData['add'].roleKey = null;
                                        }" placeholder="请选择" :options="roleTypeDict">
                                    </a-select>
                                    <a-select v-if="item.editType == 'quarterStage'"
                                        v-model:value="editData['add'][item.editType]" class="w_full" @change="() => {
                                            editData['add'].roleKey = null;
                                        }" placeholder="请选择"
                                        :options="[{ label: '第一季度', value: 1 }, { label: '第二季度', value: 2 }, { label: '第三季度', value: 3 }, { label: '第四季度', value: 4 }]">
                                    </a-select>
                                    <a-select v-if="item.editType == 'roleKey'"
                                        v-model:value="editData['add'][item.editType]" class="w_full" placeholder="请选择"
                                        :options="dict.options(editData['add'].roleType)">
                                    </a-select>
                                </a-form-item>
                            </a-table-summary-cell>
                        </template>
                        <a-table-summary-cell :index="11">
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
    <div class="add_btn" @click="addVisible = true"
        v-if="(!readOnly && !addVisible && hasPermission(['biz:projecOperation:add'])) || (!readOnly && !addVisible && showStr == 'true')">
        <plus-circle-outlined style="margin-right:8px;" />
        新增
    </div>
</template>
<script setup>
import api from '@/api/index';
import { useCorrelation } from './operation';
import { useDictStore } from '@/store/dict';
import { watchArray } from '@vueuse/core';
import { hasPermission } from '@/utils/tools';
const dict = useDictStore();
const emit = defineEmits(['update:modelValue']);
const props = defineProps({
    modelValue: {
        type: String,
        default: null,
    },
    projectId: Number,
    projectName: String,
    readOnly: {
        type: Boolean,
        default: false,
    },
    type: {
        type: String,
        default: 'TOU',
    },
    menuInfo: {
        type: Object,
        default: {},
    },
    menuId: {
        type: Number,
        default: {},
    },
    companyName: String,
    businessTypeStr: String,
    showStr: Boolean
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
        dataIndex: 'operateYear',
        required: true,
        editType: 'operateYear',
        width: 120
    },
    {
        title: '季度',
        key: 'quarterStage',
        editType: 'quarterStage',
        width: 150
    },
    {
        title: '结算率（%）',
        dataIndex: 'closeRate',
        editType: 'closeRate',
        width: 150
    },
    {
        title: '实际结算金额（元）',
        dataIndex: 'practicalCloseMoney',
        editType: 'practicalCloseMoney',
        width: 250,
    },
    {
        title: '成本（元）',
        dataIndex: 'cost',
        editType: 'cost',
        width: 150
    },
    {
        title: '利润（元）',
        dataIndex: 'profit',
        editType: 'profit',
        width: 150
    },
    {
        title: '附件',
        key: 'documentTemplateList',
        width: 300
        // editType  : 'roleKey'
    },
    {
        title: '创建人',
        dataIndex: ['createUser', 'realname'],
        width: 150
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        width: 150
    },
    {
        title: '最后更新人',
        dataIndex: ['updateUser', 'realname'],
        width: 150
    },
    {
        title: '最后更新时间',
        dataIndex: 'updateTime',
        width: 150
    }
]
const uploadChange = (fileData, recordId) => {
    console.log(fileData)
    let ext = fileData.name.substring(fileData.name.lastIndexOf(".") + 1);
    let postData = {
        companyId: props.companyId,
        stepMenuId: props.menuId,
        documentTemplateId: props.tempId,
        docmentObject: JSON.stringify(fileData),
        documentExt: ext,
        documentName: fileData.name.replace('.' + ext, ''),
        recordId: recordId,
        projectId: props.projectId,
    }
    api.project.addExpansionDocument(postData).then(res => {
        if (res.code == 200) {
            getList();
        }
    })
}
const fileDel = (index, list, item) => {
    if (item.projectCompanyDocumentList[index].id) {
        api.project.delExpansionDocument(item.projectCompanyDocumentList[index].id).then(res => {
            if (res.code == 200) {
                item.projectCompanyDocumentList.splice(index, 1);
                getList();
            }
        })
    } else {
        list.splice(index, 1);
        getList();
    }
}
// 首页-其它通知跳转到拓后详情-经营管理
const paramsData = reactive([])
const businessManagement = (callBack) => {
    if (props.businessTypeStr == undefined || props.companyName == undefined || props.projectName == undefined) {
        api.project.projectExtensionInfo(props.projectId).then((res) => {
            if (res.code == 200) {
                paramsData.businessTypeStr = res.data.businessTypeStr
                paramsData.projectName = res.data.projectName
                callBack && callBack();
                console.log('paramsData1', paramsData)
            }
        })
    }
    else {
        paramsData.businessTypeStr = props.businessTypeStr
        paramsData.projectName = props.projectName
    }
}
onBeforeMount(() => {
    businessManagement(() => { });
})
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
    console.log(props.showStr)
    // businessManagement(()=>{});
});
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
