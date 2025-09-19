
<template>
    <a-modal v-model:visible="visible" :title="handleTitles[handleType].title" @ok="handleOk" destroyOnClose width="580px">
        <a-form layout="vertical" ref="formRef" :model="formData">
            <a-form-item label="删除原因" name="remark" required v-if="handleType=='delete'">
                <a-input allowClear v-model:value="formData.remark" placeholder="请输入" />
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
        remark        : null,
    })
    const formRef  = ref(null);
    const handleOk = ()=>{
        formRef.value.validateFields().then(values=>{
            api.customer[handleTitles[handleType.value].apiKey](formData).then(res=>{
                if(res.code==200){
                    emit('success',res.data || {},handleIndex.value,handleType.value);
                    visible.value = false;
                    message.success('操作成功');
                }
            })
        })
    }
    const leadId      = ref(null);
    const handleIndex = ref(-1);
    const handleType  = ref('delete');
    const open        = (data,type,index)=>{
        leadId.value      = data.id;
        formData.id       = data.id;
        handleIndex.value = index;
        handleType.value  = type;
        visible.value     = true;
    }
    const handleTitles = reactive({
        'delete'  : {
            title  : '删除客户',
            apiKey : 'customerDel'
        },
    })
    defineExpose({open})
</script>
<style scoped lang="less">
</style>
