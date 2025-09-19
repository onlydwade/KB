
<template>
    <a-table 
    :columns="columns" 
    :loading="loadding" 
    :data-source="tableList"
    v-if="tableList.length>0"
    rowKey="id" 
    :pagination="false"
     :scroll="{x:'100%'}">
        <template #bodyCell="{ column,record,index }">
            <template v-if="column.key === 'operName'">
                <span class="color-danger" v-if="record.required==1">*</span>
                {{record.operName}}
            </template>
            <template v-if="column.key === 'status'">
                <check-circle-outlined v-if="record.projectCompanyDocumentList.length>0" class="color-success"/>
                <clock-circle-outlined v-else  class="color-gray" />
            </template>
            <template v-if="column.key === 'projectCompanyDocumentList'">
                <a-spin :spinning="uploading==index">
                    <a-upload-dragger
                        :class="{'upload-disabled': record.disabled!==0}"
                        v-if="!readOnly"
                        name="file"
                        :multiple="true"
                        :accept="accept"
                        :headers = "{
                            'Authorization':'Bearer '+ token
                        }"
                        :disabled="record.disabled!==0"
                        :showUploadList="false"
                        :action="uploadPath"
                        @change="handleChange($event,index)">
                        <p class="ant-upload-drag-icon">
                          <inbox-outlined></inbox-outlined>
                        </p>
                        <template v-if="record.disabled===0">
                            <p class="ant-upload-text" >点击或将文件拖拽到这里上传</p>
                            <p class="ant-upload-hint" style="font-size:11px;">
                                支持：{{accept}}
                            </p>
                        </template>
                        <p v-else class="ant-upload-text" >无需上传，自动带入</p>
                    </a-upload-dragger>
                    <slot></slot>
                    <FileItem v-for="(item,index) in record.projectCompanyDocumentList" :readOnly="readOnly" :key="index" :fileData="item.docmentObject" @fileDel="fileDel(index,record.projectCompanyDocumentList,item)"/>
                </a-spin>
            </template>
        </template>
    </a-table>
    
</template>
<script setup>
import api           from '@/api/index';
import { message }   from 'ant-design-vue';
import { mainStore } from '@/store';
const store  = mainStore();
const props = defineProps({
    modelValue : {
        type    : String,
        default : null,
    },
    menuId: {
        type    : Number,
        default : 0,
    },
    projectId: {
        type    : Number,
        default : 0,
    },
    companyId:{
        type    : Number,
        default : 0,
    },
    recordId:{
        type    : Number,
        default : 0,
    },
    
    proType:{
        type    : String,
        default : '',
    },
    accept : {
        type    : String,
        default : '.jpg, .jpeg, .png, .gif, .rar, .zip, .doc, .docx, .pdf, .xls, .xlsx, .xlsm',
    },
    readOnly: {
        type    : Boolean,
        default : false,
    },
})
const emit = defineEmits(['update:modelValue','uploadResult'])

const uploadPath = GLOBAL_PATH.api + '/system/upload/company';
const token      = localStorage.getItem('token');

const columns    = [
    {
        title : '名称',
        key   : 'operName',
    },
    {
        title : '状态',
        key   : 'status',
        align : 'center',
        width : 100,
    },
    {
        title : '文件上传',
        key   : 'projectCompanyDocumentList',
        width : 480,
    },
]
const loadding  = ref(false);
const tableList = ref([]);

const getList   = ()=>{
    loadding.value = true;
    api.project.expansionDocumentList(props.projectId,props.menuId,props.recordId).then(res=>{
        if(res.code==200){
            tableList.value = res.data.filter(item=>{
                console.log(props.proType)
                if(props.proType){
                    return item.type == props.proType;
                }else{
                    return !props.type || item.type == props.type;
                }
            });
            getUploadResult('init');
        }
        loadding.value = false;
    })
}

const uploading = ref(-1);
const handleChange = (info,index)=>{
    const status   = info.file.status;
    const response = info.file.response;
    if(status==='uploading'){
        uploading.value = index;
    }
    if (status === 'done') {
        uploading.value = -1;
        if(response&&response.success){
            response.data.time = Date.now();
            addDocument(response.data,index)
        }
    } else if (status === 'error') {
        uploading.value = -1;
        message.error(`${info.file.name} 上传失败.`);
    }
    if(response&&response.code&&response.code==500){
        message.error(response.msg);
    }
}
const addDocument = (fileData,index)=>{
    let ext = fileData.name.substring(fileData.name.lastIndexOf(".")+1);
    let postData = {
        companyId          : props.companyId,
        stepMenuId         : props.menuId,
        documentTemplateId : tableList.value[index].id,
        docmentObject      : JSON.stringify(fileData),
        documentExt        : ext,
        documentName       : fileData.name.replace('.'+ext,''),
        projectId          : props.projectId,
        
        recordId : 0,
    }
    if(props.recordId){
        postData.recordId = props.recordId;
        api.project.addExpansionDocument(postData).then(res=>{
            if(res.code==200){
                tableList.value[index].projectCompanyDocumentList.push(res.data);
                getUploadResult();
            }
        })
    }else{
        tableList.value[index].projectCompanyDocumentList.push(postData);
        getUploadResult();
    }
}

const fileDel = (index,list,item)=>{
    if(item.id){
        api.project.delExpansionDocument(item.id).then(res=>{
            if(res.code==200){
                list.splice(index,1);
                getUploadResult();
            }
        })
    }else{
        list.splice(index,1);
        getUploadResult();
    }
}

onMounted(() => {
    if(props.menuId){
        getList();
    }
})
watch(
    () => props.menuId,(newValue, oldValue) => {
        if(props.menuId){
            getList();
        }
    }
)

const uploadResult    = ref(false);
const getUploadResult = (type)=>{
    let done = true;
    for (var i = 0; i < tableList.value.length; i++) {
        let item = tableList.value[i];
        if((item.projectCompanyDocumentList || []).length==0 && !!item.required) {
            done = false;
            break;
        }
    }
    if(type!='init'){
        if(uploadResult.value!=done){
            uploadResult.value = done;
            emit('uploadResult',done);
        }
    }
    emit('update:modelValue',done?'upload_finish':null);
}

const batchUpLoad = (recordId)=>{
    store.spinChange(1);
    return new Promise(function(resolve, reject) {
        let promises = [];
        tableList.value.forEach((item, i) => {
            item.projectCompanyDocumentList.forEach((file, k) => {
                file.recordId = recordId;
                let pro = api.project.addExpansionDocument(file).then(res=>{
                    if(res.code==200){
                        tableList.value[i].projectCompanyDocumentList[k] = res.data;
                    }
                })
                promises.push(pro);
            });
        });
        if(promises.length>0){
            Promise.all(promises).then(posts=>{
                store.spinChange(-1);
                resolve('upload_success');
            }).catch(reason=>{
                store.spinChange(-1);
                reject('upload_filed');
            });
        }else{
            store.spinChange(-1);
            resolve('upload_none');
        }
    });
}
defineExpose({batchUpLoad})
</script>
<style scoped lang="less">
</style>
