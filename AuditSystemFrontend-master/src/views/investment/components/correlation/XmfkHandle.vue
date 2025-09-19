<template>
    <a-drawer :maskClosable="false" 
    :width="1024"
    :title="title"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
        <template #extra>
            <a-space :size="16">
                <a-button size="large" @click="handleClose">取消</a-button>
                <a-button size="large" type="primary" @click="handleOk">标记已处理</a-button>
            </a-space>
        </template>
        <a-form layout="vertical" :model="formData" ref="formRef">
            <Title class="titlebar">
                <template #title>
                    处理信息
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-form-item required label="处理描述" name="processDescription">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.processDescription" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            
            <Title class="titlebar" title="文件上传">
                <a-form-item name="fileOk" 
                :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                </a-form-item>
            </Title>
            <CompanyDocuments v-model="formData.fileOk" v-if="visible" ref="documentRef" type="CHULI_WENJIAN" :companyId="companyId" :menuId="menuId" :recordId="formData.id" :readOnly="disabled"/>
        </a-form>
    </a-drawer>
</template>
<script setup>
import api          from '@/api/index';
import { message  } from 'ant-design-vue';
import { mainStore } from '@/store';
const store = mainStore();
const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 43,
    }
})

const emit          = defineEmits(['success'])
const visible       = ref(false);
const disabled      = computed(()=>false);
const title         = ref('');
const formData      = ref({});
const formRef       = ref(null);
const documentRef   = ref(null);
const handleOk      = ()=>{
    formRef.value.validateFields().then(vRes=>{
        let postData = {
            id                 : formData.value.id,
            processStatus      : 'SHI',
            processUserId      : store.userInfo.userId,
            processDescription : formData.value.processDescription
        }
        api.investment.correlationProcess(postData,'companyPayment').then(res=>{
            if(res.code==200){
                emit('success');
                message.success('操作成功');
                visible.value = false;
            }
        })
    }).catch(err=>{
        message.warning('请完善必填信息！');
    })
}

const handleClose = ()=>{
    visible.value = false;
}
const open = (data)=>{
    formData.value = JSON.parse(JSON.stringify(data));
    title.value    = data.typeStr;
    visible.value  = true;
}
defineExpose({open})

</script>
<style scoped lang="less">
.titlebar{
    margin : 16px -16px;
    &:first-child{
        margin-top : -16px;
    }
}
</style>
