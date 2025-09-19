
<template>
    <a-modal v-model:visible="visible" title="追踪动态编辑" @ok="handleOk" :width="560">
            <a-form
            ref="formRef"
            :model="formData"
            layout="vertical">
                <a-form-item label="追踪内容描述" required name="followContent">
                    <a-mentions :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.followContent"
                        rows="3"
                        placeholder="请输入追踪内容">
                    </a-mentions>
                </a-form-item>
                <a-form-item label="附件" name="followDocumentArr">
                    <Upload modeName="follow" v-model="formData.followDocumentArr" :menuId="menuId"/>
                </a-form-item>
            </a-form>
        </a-modal>
</template>
<script setup>
import api           from '@/api/index';
import { deepClone } from '@/utils/tools';
import { message }   from 'ant-design-vue';
const props = defineProps({
    menuId:{
        type    : Number,
        default : 0,
    }
})
const emit = defineEmits(['success'])

const visible  = ref(false);
const formData = ref({});
const formRef  = ref(null);
const handleOk = ()=>{
    formRef.value.validateFields().then(vRes=>{
        formData.value.followDocument = JSON.stringify(formData.value.followDocumentArr);
        api.common.followEdit(formData.value).then(res=>{
            if(res.code==200){
                message.success('操作成功');
                emit('success');
                visible.value = false;
            }
        })
    }).catch(err=>{
        message.warning('请完善必填信息！');
    })
}

const open = (data)=>{
    formData.value                   = deepClone(data);
    formData.value.followDocumentArr = JSON.parse(formData.value.followDocument || '[]');
    visible.value                    = true;
}
defineExpose({open})
</script>
<style scoped lang="less">

</style>
