
<template>
    <a-form ref="formRef" layout="vertical" :model="editData">
        <Title title="风险记录"></Title>
        <a-table :columns="columns" :data-source="list" :pagination="false" :scroll="{ x: '100%' }">
            <template #emptyText>
                暂无数据，请添加
            </template>
            <template #bodyCell="{ column, text, record, index }">
                <template v-if="column.key === 'documentTemplateList'">
                    <template v-for="(item, tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                        <FileItem v-for="(file, findex) in (item.projectCompanyDocumentList || [])"
                            :key="index + '_' + tindex + '_' + findex" :fileData="file.docmentObject" :readOnly="true" :canDel=1
                            @fileDel="fileDel(index, record.projectCompanyDocumentList, item)" />
                    </template>
                </template>
                <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'riskName'">
                    <a-input placeholder="请输入" v-if="editData[index]" v-model:value="editData[index][column.editType]" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                    v-if="column.editType == 'riskDescribe'">
                    <a-input placeholder="请输入" v-if="editData[index]" v-model:value="editData[index][column.editType]" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <template v-if="column.key === 'action'">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary"
                            v-if="(!readOnly && hasPermission(['biz:projectRisk:edit'])) || (!readOnly && showStr == 'true')"
                            size="small" @click="edit(record, index)">编辑</a-button>
                        <a-button type="text" class="color-primary"
                            v-if="(!readOnly && hasPermission(['biz:projectRisk:delete'])) || (!readOnly && showStr == 'true')"
                            size="small" @click="del(record, index)">删除</a-button>
                        <UploadBtn
                            v-if="(!readOnly && tempId != 0 && hasPermission(['biz:projectRisk:upload'])) || (!readOnly && tempId != 0 && showStr == 'true')"
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
                                <a-form-item label=""> <!-- :required="item.required" :name="['add',item.editType]" -->
                                    <a-input placeholder="请输入" v-if="item.editType == 'riskName'"
                                        v-model:value="editData['add'][item.editType]" />
                                    <a-input placeholder="请输入" v-if="item.editType == 'riskDescribe'"
                                        v-model:value="editData['add'][item.editType]" />
                                </a-form-item>
                            </a-table-summary-cell>
                        </template>
                        <a-table-summary-cell :index="7">
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
        v-if="(!readOnly && !addVisible && hasPermission(['biz:projectRisk:add'])) || (!readOnly && !addVisible && showStr == 'true')">
        <plus-circle-outlined style="margin-right:8px;" />
        新增
    </div>
</template>
<script setup>
import api from '@/api/index';
import { useCorrelation } from './riskfx';
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
    menuId: {
        type: Number,
        default: {},
    },
    readOnly: {
        type: Boolean,
        default: false,
    },
    type: {
        type: String,
        default: 'TOU',
    },
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
} = useCorrelation(props.projectId, 'riskfx');
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
        title: '风险名称',
        dataIndex: 'riskName',
        required: true,
        editType: 'riskName',
        width: 150
    },
    {
        title: '风险描述',
        dataIndex: 'riskDescribe',
        editType: 'riskDescribe',
        width: 200
    },
    {
        title: '相关附件',
        key: 'documentTemplateList',
        width: 300
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
const userChange = (dept, obj) => {
    if (dept && dept.deptId) {
        obj.deptId = dept.deptId;
        obj.deptName = dept.deptName;
    }
    // api.common.getUser(dept.deptId).then(res=>{
    //     if(res.code==200){
    //
    //     }
    // })
}
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
}</style>
