
<template>
<a-spin :spinning="uploading">
    <a-upload-dragger
        name="file"
        v-if="!disabled && (limit==0 || limit>fileList.length)"
        :multiple="true"
        :disabled="readOnly || disabled"
        :accept="accept"
        :headers = "{
            'Authorization':'Bearer '+ token
        }"
        :showUploadList="false"
        :action="uploadPath"
        @change="handleChange($event,index)">
        <p class="ant-upload-drag-icon">
          <inbox-outlined></inbox-outlined>
        </p>
        <p class="ant-upload-text">点击或将文件拖拽到这里上传</p>
        <p class="ant-upload-hint" v-if="accept" style="font-size:11px;">
            支持格式：{{acceptStr || accept}} 
        </p>
    </a-upload-dragger>
</a-spin>
<FileItem v-for="(item,index) in fileList" :key="index" :fileData="item" :readOnly="disabled || readOnly" @fileDel="del(index)" :canDel="canDel"/>
</template>
<script setup>
import api         from '@/api/index';
import { message } from 'ant-design-vue';
const props = defineProps({
    modelValue : {
        type    : Array,
        default : [],
    },
    disabled:{
        type    : Boolean,
        default : false,
    },
    recordId:{
        type    : Number,
        default : 0,
    },
    modeName:{
        type    : String,
        default : 'project',
    },
    accept:{
        type    : String,
        default : '.jpg, .jpeg, .png, .gif, .rar, .zip, .doc, .docx, .pdf, .xls, .xlsx, .xlsm',
    },
    acceptStr:{
        type    : String,
        default : '',
    },
    limit:{
        type    : Number,
        default : 0,
    },
    readOnly:{
        type    : Boolean,
        default : false,
    },
    canDel:{
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 0,
    }
})
const uploadPath = GLOBAL_PATH.api + '/system/upload/'+props.modeName;
const token      = localStorage.getItem('token');
const emit       = defineEmits(['update:modelValue', 'change']);
const fileList   = computed({
    get : () => props.modelValue,
    set : (val) => {
        emit('update:modelValue',val);
    }
})

const uploading = ref(false);
const handleChange = (info,index)=>{
    const status   = info.file.status;
    const response = info.file.response;
    if(status==='uploading'){
        uploading.value = true;
    }
    if (status === 'done') {
        uploading.value = false;
        if(response&&response.success){
            response.data.time = Date.now();
            let ext = response.data.name.substring(response.data.name.lastIndexOf(".")+1);
            response.data.ext = ext;
            if(props.menuId){
                response.data.stepMenuId         = props.menuId;
                response.data.documentTemplateId = 1038;
            }
            fileList.value.push(response.data);
            emit('change',fileList.value);
        }
    } else if (status === 'error') {
        uploading.value = false;
        message.error(`${info.file.name} 上传失败.`);
    }
    if(response&&response.code&&response.code==500){
        message.error(response.msg);
    }
}
const del = (index)=>{
    fileList.value.splice(index,1);
    emit('change',fileList.value);
}
</script>
<style scoped lang="less">

</style>
