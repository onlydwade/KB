
<template>
    <a-modal v-model:visible="visible" :title="handleTitles[handleType].title" @ok="handleOk" destroyOnClose width="580px">
        <a-form layout="vertical" ref="formRef" :model="formData">
            <a-form-item label="项目终止原因" name="terminationReason" required v-if="handleType=='close'">
                <a-input allowClear v-model:value="formData.terminationReason" placeholder="请输入" />
            </a-form-item>
            <a-form-item label="项目失效原因" name="expireReason" required v-if="handleType=='expire'">
                <a-input allowClear v-model:value="formData.expireReason" placeholder="请输入" />
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script setup>
    import api         from '@/api/index';
    import { message } from 'ant-design-vue';
    const emit     = defineEmits(['success'])
    const visible  = ref(false);
    const formData = reactive({
        id             : null,
    })
    const formRef  = ref(null);
    const handleOk = ()=>{
        formRef.value.validateFields().then(values=>{
            
            api.project[handleTitles[handleType.value].apiKey](formData).then(res=>{
                if(res.code==200){
                    emit('success',res.data || {},handleIndex.value,handleType.value);
                    visible.value = false;
                    message.success('操作成功');
                }
            })
        })
    }
    const handleIndex = ref(-1);
    const handleType  = ref('close');
    const open        = (data,type,index)=>{
        formData.id       = data.id;
        formData.remark  = '';
        handleIndex.value = index;
        handleType.value  = type;
        visible.value     = true;
        nextTick(()=>{
            formRef.value.resetFields();
        })
    }
    const handleTitles = reactive({
        'close'  : {
            title  : '终止项目',
            apiKey : 'projectClose'
        },
        'expire'  : {
            title  : '标记失效',
            apiKey : 'projectExpire'
        }
    })
    defineExpose({open})
</script>
<style scoped lang="less">
</style>
