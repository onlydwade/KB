
<template>
    <a-upload
        name="file"
        :disabled="disabled"
        :accept="accept"
        :headers = "{
            'Authorization':'Bearer '+ token
        }"
        :showUploadList="false"
        :action="uploadPath"
        @change="handleChange($event,index)">
        <slot></slot>
    </a-upload>
</template>
<script setup>
import api         from '@/api/index';
import { message } from 'ant-design-vue';
const props = defineProps({
    disabled:{
        type    : Boolean,
        default : false,
    },
    modeName:{
        type    : String,
        default : 'flower',
    },
    accept:{
        type    : String,
        default : '.jpg, .jpeg, .png, .gif, .rar, .zip, .doc, .docx, .pdf, .xls, .xlsx, .xlsm',
    },
    acceptStr:{
        type    : String,
        default : '',
    },
})
const uploadPath = GLOBAL_PATH.api + '/system/upload/'+props.modeName;
const token      = localStorage.getItem('token');
const emit       = defineEmits(['change']);

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
            emit('change',response.data);
        }
    } else if (status === 'error') {
        uploading.value = false;
        message.error(`${info.file.name} 上传失败.`);
    }
    if(response&&response.code&&response.code==500){
        message.error(response.msg);
    }
}
</script>
<style scoped lang="less">

</style>
