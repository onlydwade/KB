
<template>
    <a-modal v-model:visible="visible" title="归属人变更" @ok="handleOk" destroyOnClose width="580px">
        <a-form layout="vertical" ref="formRef" :model="formData">
            <a-form-item name="attributorUserId" required label="变更后人员">
                <UserSelect v-model="formData.attributorUserId"/>
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
        attributorUserId : null
    })
    const formRef  = ref(null);
    const handleOk = ()=>{
        formRef.value.validateFields().then(values=>{
            api.project.changeAttributor(dataId.value,formData.attributorUserId).then(res=>{
                if(res.code==200){
                    emit('success',{},handleIndex.value);
                    visible.value = false;
                    message.success('操作成功');
                }
            })
        })
    }
    const dataId    = ref(null);
    const handleIndex = ref(-1);
    const open        = (data,index)=>{
        formData.attributorUserId = null;
        dataId.value              = data.id;
        handleIndex.value         = index;
        visible.value             = true;
    }
    defineExpose({open})
</script>
<style scoped lang="less">

</style>
