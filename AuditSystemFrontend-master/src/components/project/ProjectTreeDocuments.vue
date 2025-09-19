
<template>
    <a-table :columns="columns" :loading="loadding" :data-source="onLine" v-if="onLine.length > 0" rowKey="id"
        :pagination="false" :scroll="{ x: '100%' }">
        <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'operName'">
                <span class="color-danger" v-if="record.required == 1 || requiredIds.includes(record.id)">*</span>
                {{ record.operName }}
            </template>
            <template v-if="column.key === 'status'">
                <check-circle-outlined v-if="record.projectDocumentList.length > 0 || (record.linkDocuments || []).length > 0"
                    class="color-success" />
                <clock-circle-outlined v-else class="color-gray" />
            </template>
            <!-- <template v-if="column.key === 'remark'">
                <div class="" v-html="record.remark.replaceAll('\n','<br/>')"></div>
            </template> -->
            <template v-if="column.key === 'projectDocumentList'">
                <a-spin :spinning="uploading == record.index">
                    <a-upload-dragger :class="{ 'upload-disabled': record.disabled !== 0 }" v-if="!readOnly" name="file"
                        :multiple="true" :accept="accept" :headers="{
                            'Authorization': 'Bearer ' + token
                        }" :disabled="record.disabled !== 0" :showUploadList="false" :action="uploadPath"
                        @change="handleChange($event, record.index)">
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
                    <FileItem v-for="(item, index) in record.linkDocuments" :readOnly="true" :key="index"
                        :fileData="item.docmentObject" />
                    <FileItem v-for="(item, index) in record.projectDocumentList" :readOnly="readOnly" :key="index"
                        :fileData="item.docmentObject" @fileDel="fileDel(index, record.projectDocumentList, item)" />
                </a-spin>
            </template>
        </template>
    </a-table>
    <TenderReviewMatters v-if="menuId==37"/>
    <template v-if="offlineApproval == 1 || hasOffLineDoc">
        <Title title="线下审批需上传文件" style="margin-top:32px;margin-bottom:8px;" v-if="offLine.length > 0"></Title>
        <a-table :columns="columns" :loading="loadding" :data-source="offLine" v-if="offLine.length > 0" rowKey="id"
            :pagination="false" :scroll="{ x: '100%' }">
            <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'operName'">
                    <span class="color-danger" v-if="record.required == 1 || requiredIds.includes(record.id)">*</span>
                    {{ record.operName }}
                </template>
                <template v-if="column.key === 'status'">
                    <check-circle-outlined
                        v-if="record.projectDocumentList.length > 0 || (record.linkDocuments || []).length > 0"
                        class="color-success" />
                    <clock-circle-outlined v-else class="color-gray" />
                </template>
                <!-- <template v-if="column.key === 'remark'">
                    <div class="" v-html="record.remark.replaceAll('\n','<br/>')"></div>
                </template> -->
                <template v-if="column.key === 'projectDocumentList'">
                    <a-spin :spinning="uploading == record.index">
                        <a-upload-dragger :class="{ 'upload-disabled': record.disabled !== 0 }" v-if="!readOnly" name="file"
                            :multiple="true" :accept="accept" :headers="{
                                'Authorization': 'Bearer ' + token
                            }" :disabled="record.disabled !== 0" :showUploadList="false" :action="uploadPath"
                            @change="handleChange($event, record.index)">
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
                        <FileItem v-for="(item, index) in record.linkDocuments" :readOnly="true" :key="index"
                            :fileData="item.docmentObject" />
                        <FileItem v-for="(item, index) in record.projectDocumentList" :readOnly="readOnly" :key="index"
                            :fileData="item.docmentObject" @fileDel="fileDel(index, record.projectDocumentList, item)" />
                    </a-spin>
                </template>
            </template>
        </a-table>
    </template>
</template>
<script setup>
import api from '@/api/index';
import { message } from 'ant-design-vue';
import { watchArray } from '@vueuse/core'
const props = defineProps({
    modelValue: {
        type: String,
        default: null,
    },
    projectId: {
        type: Number,
        default: 0,
    },
    menuId: {
        type: Number,
        default: 0,
    },
    stepLink: {
        type: Object,
        default: {},
    },
    requiredIds: {
        type: Array,
        default: [],
    },
    accept: {
        type: String,
        default: '.jpg, .jpeg, .png, .gif, .rar, .zip, .doc, .docx, .pdf, .xls, .xlsx, .xlsm',
    },
    readOnly: {
        type: Boolean,
        default: false,
    },
    offlineApproval: {
        type: Number,
        default: 1,
    }
})
const emit = defineEmits(['update:modelValue', 'uploadResult'])

const uploadPath = GLOBAL_PATH.api + '/system/upload/project';
const token = localStorage.getItem('token');

const projectType = inject('getAutoParams')('projectType');
const expansionMode = inject('getAutoParams')('expansionMode');

const columns = [
    {
        title: '名称',
        key: 'operName',
    },
    {
        title: '状态',
        key: 'status',
        align: 'center',
        width: 100,
    },
    // {
    //     title : '说明',
    //     key   : 'remark',
    //     width : 200,
    // },
    {
        title: '文件上传',
        key: 'projectDocumentList',
        width: 480,
    },
]
const loadding = ref(false);
const tableList = ref([]);

const onLine = computed(() => {
    return tableList.value.filter(item => {
        return item.isOnline == 1;
    })
})
const offLine = computed(() => {
    return tableList.value.filter(item => {
        return item.isOnline == 0;
    })
})
const hasOffLineDoc = ref(false);
const getList = () => {
    if (!props.menuId) {
        return;
    }
    loadding.value = true;
    api.project.documentList(props.projectId, props.menuId).then(async res => {
        if (res.code == 200) {
            tableList.value = (res.data || []).filter(item => {
                return (!item.projectType || !projectType.value || item.projectType.indexOf(projectType.value) > -1) && (!item.expansionMode || !expansionMode.value || item.expansionMode.indexOf(expansionMode.value) > -1);
            });
            tableList.value.sort((a, b) => {
                return a.sorts - b.sorts;
            })
            tableList.value = tableList.value.map((item, index) => {
                item.index = index;
                if (item.isOnline == 0 && (item.projectDocumentList || []).length > 0) {
                    hasOffLineDoc.value = true;
                }
                return item;
            })
            for (const item of tableList.value) {
                if (props.stepLink[item.id]) {
                    let linkRes = await api.project.documentList(props.projectId, props.stepLink[item.id].stepMenuId);
                    linkRes.data.forEach((linkItem, k) => {
                        if (linkItem.id == props.stepLink[item.id].id) {
                            item.linkDocuments = linkItem.projectDocumentList;
                        }
                    });
                }
            }
            getUploadResult('init');
        }
        loadding.value = false;
    })
}

watch(
    () => props.menuId, (newValue, oldValue) => {
        getList();
    }
)

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
        stepMenuId: props.menuId,
        projectType: projectType.value,
        documentTemplateId: tableList.value[index].id,
        docmentObject: JSON.stringify(fileData),
        documentExt: ext,
        documentName: fileData.name.replace('.' + ext, ''),
    }
    api.project.addDocument(postData).then(res => {
        if (res.code == 200) {
            tableList.value[index].projectDocumentList.push(res.data);
            getUploadResult();
        }
    })
}

watchArray(() => props.requiredIds, (newList) => {
    getUploadResult();
}, { deep: true })

const uploadResult = ref(false);
const getUploadResult = (type) => {
    let done = true;
    for (var i = 0; i < tableList.value.length; i++) {
        let item = tableList.value[i];
        if (((item.projectDocumentList || []).length == 0 && (item.linkDocuments || []).length == 0) && (!!item.required || props.requiredIds.includes(item.id))) {
            done = false;
            break;
        }
    }
    if (type != 'init') {
        if (uploadResult.value != done) {
            uploadResult.value = done;
            emit('uploadResult', done);
        }
    }
    emit('update:modelValue', done ? 'upload_finish' : null);
}
const fileDel = (index, list, item) => {
    api.project.delDocument(item.id).then(res => {
        if (res.code == 200) {
            list.splice(index, 1);
            getUploadResult();
        }
    })
}

onMounted(() => {
    getList();
})
const JsonParse = (jsonStr) => {
    return JSON.parse(jsonStr);
}

const getAssignStatus = (assignId) => {
    let result = 'success';
    for (var i = 0; i < tableList.value.length; i++) {
        let item = tableList.value[i];
        if ((item.projectDocumentList || []).length == 0 && item.id == assignId) {
            result = '请上传文件:' + item.operName;
            break;
        }
    }
    return result;
}

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
defineExpose({ getAssignStatus, getOfflineStatus })
</script>
<style scoped lang="less"></style>
