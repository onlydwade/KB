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
                    <a-form-item required label="付款类型" name="type">
                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="type!='ADD' || oldType!='SHOU_CI_CHU_ZI'"
                            v-model:value="formData.type"
                            class="w_full"
                            placeholder="请选择"
                            :options="dict.options('XIANG_MU_FU_KUAN_LEI_XIN')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="出资申请人" name="takeId">
                        <UserSelect v-model="formData.takeId" :users="formData.take" :disabled="disabled"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="出资申请日期" name="applyTime">
                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.applyTime"
                            class="w_full"
                            valueFormat="YYYY-MM-DD 00:00:00"
                            format="YYYY-MM-DD"
                            placeholder="请选择" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="出资金额（元）" name="amount">
                        <a-input-number v-model:value="formData.amount" :min="0" class="w_full"
                        :disabled="disabled"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"
                        :addon-after="amountUnit(formData.amount)"/>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-form-item label="出资描述" name="description">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.description" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            
            <Title class="titlebar" title="文件上传">
                <a-form-item name="fileOk" 
                :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                </a-form-item>
            </Title>
            <CompanyDocuments v-model="formData.fileOk" v-if="visible" ref="documentRef" :type="type=='SHOW'?'':'SHENPI_PINZHEN'" :companyId="companyId" :menuId="menuId" :recordId="formData.id" :readOnly="disabled"/>
        </a-form>
    </a-drawer>
</template>
<script setup>
import api           from '@/api/index';
import { message  }  from 'ant-design-vue';
import {amountUnit}  from '@/utils/tools';
import { mainStore } from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict = useDictStore();

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
        
        api.investment[apiKey](formData.value,'companyPayment').then(async res=>{
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
const oldType  = ref('')
const open     = (key,data,dfaultType)=>{
    oldType.value = dfaultType;
    type.value       = key;
    try {
        formData.value = JSON.parse(JSON.stringify(data));
    } catch (e) {
        type.value     = 'ADD';
        formData.value = {};
    }
    title.value   = titleObj[type.value] +'项目付款记录';
    if(key=='ADD'){
        formData.value.type          = dfaultType;
        formData.value.processStatus = 'FOU';
        
        formData.value.takeId = store.userInfo.userId;
        formData.value.take = {
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
