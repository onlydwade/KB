
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
                <span class="color-danger" v-if="record.required==1 && !readOnly">*</span>
                {{record.operName}}
            </template>
            <template v-if="column.key === 'status'">
                <check-circle-outlined v-if="record.projectCompanyDocumentList.length>0 || height[index]>5" class="color-success"/>
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
                    <div :ref="'ref'+index">
                        <slot :name="'linkDoc_'+index"></slot>
                    </div>
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
import { useRoute } from 'vue-router'
import { useElementSize } from '@vueuse/core'
const route = useRoute()
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
    companyId:{
        type    : Number,
        default : 0,
    },
    recordId:{
        type    : Number,
        default : 0,
    },
    
    type:{
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
        width : 200,
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

const { proxy } = getCurrentInstance()
const height    = ref({})

watch(()=>height.value,()=>{
    getUploadResult();
},{deep:true})
const getList  = ()=>{
  let recordId = route.path =='/companyOaInfo' ? route.query.id:props.recordId
    loadding.value = true;
    api.investment.documentList(props.companyId,props.menuId,recordId).then(res=>{
        if(res.code==200){
            tableList.value = res.data.filter(item=>{
                return !props.type || item.type == props.type;
            });
            getUploadResult('init');

            nextTick(()=>{
                tableList.value.forEach((item, i) => {
                    height.value[i] =  useElementSize(proxy.$refs['ref'+i]).height;
                });
            })
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
        
        recordId : 0,
    }
    if(props.recordId){
        postData.recordId = props.recordId;
        api.investment.documentAdd(postData).then(res=>{
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
        api.investment.documentDel(item.id).then(res=>{
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
        if((item.projectCompanyDocumentList || []).length==0 && !!item.required && (height.value[i] || 0) <5) {
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

const batchUpLoad = (recordId,type)=>{
    store.spinChange(1);
    return new Promise((resolve, reject)=> {
        let promises = [];
        tableList.value.forEach((item, i) => {
            if(!type || type==item.type){
                item.projectCompanyDocumentList.forEach((file, k) => {
                    file.recordId = recordId;
                    let pro = api.investment.documentAdd(file).then(res=>{
                        if(res.code==200){
                            tableList.value[i].projectCompanyDocumentList[k] = res.data;
                        }
                    })
                    promises.push(pro);
                });
            }
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
const getTypeDocNum = (type)=>{
    for (var i = 0; i < tableList.value.length; i++) {
        if(tableList.value[i].type==type){
            return tableList.value[i].projectCompanyDocumentList.length || 0;
            break;
        }
    }
    return 0;
}
defineExpose({batchUpLoad,getTypeDocNum,addDocument})
</script>
<style scoped lang="less">
</style>
