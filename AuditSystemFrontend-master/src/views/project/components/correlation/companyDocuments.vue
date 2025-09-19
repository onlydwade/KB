
<template>
    <a-table :columns="columns" :loading="loadding" :data-source="tableList" rowKey="documentTemplateId" :pagination="false"
        :scroll="{ x: '100%' }">
        <template #bodyCell="{ column, record, index }">
            <template v-if="column.key === 'operName'">
                <span class="color-danger" v-if="record.required == 1">*</span>
                {{ record.operName }}
            </template>
            <template v-if="column.key === 'status'">
                <check-circle-outlined v-if="record.projectDocumentList.length > 0" class="color-success" />
                <clock-circle-outlined v-else class="color-gray" />
            </template>
            <template v-if="column.key === 'projectDocumentList'">
                <a-spin :spinning="uploading == index">
                    <a-upload-dragger :class="{ 'upload-disabled': record.disabled !== 0 }" v-if="!readOnly" name="file"
                        :multiple="true" :accept="accept" :headers="{ 'Authorization': 'Bearer ' + token }"
                        :disabled="record.disabled !== 0" :showUploadList="false" :action="uploadPath"
                        @change="handleChange($event, index)">
                        <p class="ant-upload-drag-icon">
                            <inbox-outlined></inbox-outlined>
                        </p>
                        <template v-if="record.disabled === 0">
                            <p class="ant-upload-text">点击或将文件拖拽到这里上传</p>
                            <p class="ant-upload-hint" style="font-size:11px;">
                                支持：{{ accept }}
                            </p>
                        </template>
                        <p v-else class="ant-upload-text">无需上传，自动带入</p>
                    </a-upload-dragger>
                    <FileItem v-for="(item, index) in record.projectDocumentList"
                        :readOnly="readOnly || record.disabled == 1" :key="index" :fileData="item.docmentObject"
                        @fileDel="fileDel(index, record.projectDocumentList, item)" />
                </a-spin>
            </template>
        </template>
    </a-table>
</template>
<script setup>
import api from '@/api/index';
import { message } from 'ant-design-vue';
// import { watchArray } from '@vueuse/core'
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
    accept: {
        type: String,
        default: '.jpg, .jpeg, .png, .gif, .rar, .zip, .doc, .docx, .pdf, .xls, .xlsx, .xlsm',
    },
    readOnly: {
        type: Boolean,
        default: false,
    },
})
const emit = defineEmits(['update:modelValue'])

const uploadPath = GLOBAL_PATH.api + '/system/upload/project';
const token = localStorage.getItem('token');

// const projectType = inject('getAutoParams')('projectType');

const columns = [
    {
        title: '名称',
        key: 'operName',
    },
    {
        title: '状态',
        key: 'status',
    },
    {
        title: '文件上传',
        key: 'projectDocumentList',
        width: 480,
    },
]
const baseData = [
    // {operName:'盖章版章程上传',required:true,disabled:0,documentTemplateId:1},
    { operName: '工商执照上传', required: true, disabled: 0, documentTemplateId: 2 },
    { operName: '其他文件上传', required: false, disabled: 0, documentTemplateId: 3 },
]

const loadding = ref(false);
const tableList = ref([]);

const getList = async () => {
    loadding.value = true;
    let list = []
    //带入  盖章版章程数据
    let linkRes = await api.project.documentList(props.projectId, 23);
    linkRes.data.forEach((item, i) => {
        if (item.id == 1026) {

            item.disabled = 1
            list.push(item)
        }
    });


    let postData = {
        pageNo: 1,
        pageSize: 1000,
        params: {
            companyId: props.companyId,
        },
        inParams: {
            documentTemplateId: [2, 3]
        }
    }
    const res = await api.project.correlationPage(postData, 'companyDocument');
    if (res.code == 200) {
        baseData.forEach((temp, i) => {
            let obj = {
                operName: temp.operName,
                required: temp.required,
                disabled: temp.disabled,
                documentTemplateId: temp.documentTemplateId,
                projectDocumentList: [],
            }
            res.data.records.forEach(item => {
                if (item.companyId == props.companyId && temp.documentTemplateId == item.documentTemplateId) {
                    obj.projectDocumentList.push(item);
                }
            });
            list.push(obj);
        });
        tableList.value = list;
        getUploadResult();
    }
    loadding.value = false;
}

const uploading = ref(-1);
const handleChange = (info, index) => {
    const status = info.file.status;
    const response = info.file.response;
    if (status === 'uploading') {
        uploading.value = index;
    }
    if (status === 'done') {
        uploading.value = -1;
        if (response && response.success) {
            response.data.time = Date.now();
            addDocument(response.data, index)
        }
    } else if (status === 'error') {
        uploading.value = -1;
        message.error(`${info.file.name} 上传失败.`);
    }
    if (response && response.code && response.code == 500) {
        message.error(response.msg);
    }
}
const addDocument = (fileData, index) => {
    let ext = fileData.name.substring(fileData.name.lastIndexOf(".") + 1);
    let postData = {
        projectId: props.projectId,
        companyId: props.companyId,
        documentTemplateId: tableList.value[index].documentTemplateId,
        docmentObject: JSON.stringify(fileData),
        documentExt: ext,
        documentName: fileData.name.replace('.' + ext, ''),
    }
    api.project.correlationAdd(postData, 'projectCompanyDocument').then(res => {
        if (res.code == 200) {
            getList();
        }
    })
}

const getUploadResult = (type) => {
    let done = true;
    for (var i = 0; i < tableList.value.length; i++) {
        let item = tableList.value[i];
        if ((item.projectDocumentList || []).length == 0 && !!item.required) {
            done = false;
            break;
        }
    }
    emit('update:modelValue', done ? 'upload_finish' : null);
}
const fileDel = (index, list, item) => {
    api.project.correlationDel(item.id, 'projectCompanyDocument').then(res => {
        if (res.code == 200) {
            list.splice(index, 1);
            getUploadResult();
        }
    })
}

onMounted(() => {
    getList();
})
// const JsonParse = (jsonStr) => {
//     return JSON.parse(jsonStr);
// }

const getOfflineStatus = () => {
    let result = 'success';
    for (var i = 0; i < tableList.value.length; i++) {
        let item = tableList.value[i];
        if ((item.projectDocumentList || []).length == 0 && item.isOnline == 0) {
            result = '请上传文件:' + item.operName;
            break;
        }
    }
    return result;
}
defineExpose({ getOfflineStatus, tableList })
</script>
<style scoped lang="less"></style>
