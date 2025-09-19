<template>
    <a-drawer :maskClosable="false" 
    :width="1024"
    :title="title"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
        <template #extra>
            <a-space :size="16" v-if="type=='SHOW'">
                <a-button size="large" @click="handleClose">关闭</a-button>
            </a-space>
            <a-space :size="16" v-else>
                <a-button size="large" @click="handleClose">取消</a-button>
                <a-button size="large" type="primary" @click="handleOk">保存</a-button>
            </a-space>
        </template>
        <a-form layout="vertical" :model="formData" ref="formRef">
            <Title class="titlebar">
                <template #title>
                    信息填写
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item required label="变更人" name="changeUserId">
                        <UserSelect v-model="formData.changeUserId" :users="formData.changeUser" :disabled="disabled"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="变更日期" name="changeTime">
                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.changeTime"
                            class="w_full"
                            valueFormat="YYYY-MM-DD 00:00:00"
                            format="YYYY-MM-DD"
                            placeholder="请选择" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="变更项" name="changeType">
                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.changeType"
                            class="w_full"
                            placeholder="请选择"
                            :options="dict.options('GU_QUAN_BIAN_GEN_LEI_XING')">
                        </a-select>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-form-item label="变更前" name="changeBefore">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.changeBefore" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            <a-form-item label="变更后" name="changeAfter">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.changeAfter" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            <Title class="titlebar" title="附件上传">
                <a-form-item name="fileOk" 
                :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                </a-form-item>
            </Title>
            <CompanyDocuments v-model="formData.fileOk" v-if="visible" ref="documentRef" :companyId="companyId" :menuId="menuId" :recordId="formData.id" :readOnly="disabled"/>
        </a-form>
    </a-drawer>
</template>
<script setup>
import api          from '@/api/index';
import { message  } from 'ant-design-vue';
import { mainStore } from '@/store';
const store = mainStore();
import { useDictStore } from '@/store/dict';
const dict = useDictStore();

const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 38,
    }
})

const emit          = defineEmits(['success'])
const visible       = ref(false);
const type          = ref('ADD');
const disabled      = computed(()=>type.value=='SHOW');
const title         = ref('');
const formData      = ref({});
const formRef       = ref(null);
const documentRef   = ref(null);
const handleOk      = ()=>{
    formRef.value.validateFields().then(vRes=>{
        let apiKey               = type.value=='ADD'?'correlationAdd':'correlationEdit';
        formData.value.companyId = props.companyId;
        api.investment[apiKey](formData.value,'projectCompanyShareholderLog').then(async res=>{
            if(res.code==200){
                if(type.value=='ADD'){
                    let uploadResult = await documentRef.value.batchUpLoad(res.data.id);
                }
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
const titleObj = {ADD:'新增',EDIT:'修改',SHOW:'查看'};
const open     = (key,data)=>{
    type.value    = key;
    try {
        formData.value = JSON.parse(JSON.stringify(data));
    } catch (e) {
        type.value     = 'ADD';
        formData.value = {};
    }
    title.value   = titleObj[type.value] +'股权信息变更';
    if(key=='ADD'){
        formData.value.changeUserId = store.userInfo.userId;
        formData.value.changeUser = {
            userId   : store.userInfo.userId,
            realname : store.userInfo.realname
        }
    }
    
    visible.value = true;
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
