
<template>
    <a-tooltip :title="title">
        <a-button type="primary" @click="add" style="margin-left:8px;" :disabled="disabled">
            <template #icon><plus-outlined /></template>
        </a-button>
    </a-tooltip>
    <a-drawer :maskClosable="false" 
    :width="960"
    :title="title"
    destroyOnClose
    :bodyStyle="{backgroundColor:'#f0f2f5'}"
    @close="handleClose"
    :visible="visible">
        <template #extra>
            <a-space :size="16">
                <a-button size="large" @click="handleClose">取消</a-button>
                <a-button size="large" type="primary" @click="handleOk" :loading="submitLoading">保存</a-button>
            </a-space>
        </template>
        <component ref="editFormRef" :lockData="lockData" :is="components[props.modeName]" @success="submitSuccess"></component>
    </a-drawer>
</template>
<script setup>
import api              from '@/api/index';
import { message }      from 'ant-design-vue';
import LoadingComponent from '@/components/LoadingComponent.vue'
const props = defineProps({
    modeName:{
        type    : String,
        default : 'customer',
    },
    componentName:{
        type    : String,
        default : 'EditForm',
    },
    title:{
        type    : String,
        default : '添加客户',
    },
    
    lockData:{
        type    : Object,
        default : null,
    },
    disabled:{
        type    : Boolean,
        default : false,
    }
})
const visible     = ref(false);
const handleClose = ()=>{
    visible.value = false;
}
const modules    = import.meta.glob("../**/*.vue");
const components = {};
const add        = ()=>{
    components[props.modeName] = defineAsyncComponent({
        loader           : modules['../views/'+props.modeName+'/components/'+props.componentName+'.vue'],
        loadingComponent : LoadingComponent,
    });
    visible.value = true;
}

const submitLoading = ref(false);
const editFormRef   = ref(null);
const handleOk      = ()=>{
    editFormRef.value.submit();
}

const emit      = defineEmits(['addSuccess'])
const submitSuccess = (data)=>{
    visible.value = false;
    emit('addSuccess',data);
    message.success('添加成功!');
}
</script>
<style scoped lang="less">

</style>
