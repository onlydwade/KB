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
                    <a-form-item label="企业名称" name="companyName" v-if="companyId || type!='ADD'">
                        <a-input disabled allowClear :value="formData.companyName"/>
                    </a-form-item>
                    <a-form-item required label="企业名称" name="companyId" v-else>
                        <DataSelect
                            v-model="formData.companyId"
                            modeName="company"
                            :options="{
                                key   : 'id',
                                label : 'name'
                            }"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="风险节点名称" name="node">
                        <a-input :disabled="disabled" allowClear v-model:value="formData.node" show-count :maxlength="100"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="里程碑" name="milepostTime">
                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.milepostTime"
                            class="w_full"
                            valueFormat="YYYY-MM-DD 00:00:00"
                            format="YYYY-MM-DD"
                            placeholder="请选择" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="提醒日期" name="reminderTime">
                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.reminderTime"
                            class="w_full"
                            valueFormat="YYYY-MM-DD 00:00:00"
                            format="YYYY-MM-DD"
                            placeholder="请选择" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-form-item required label="提醒内容" name="content">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.content" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
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
        default : 46,
    }
})

const companyIdEnd = computed(()=>{
    return props.companyId || formData.value.companyId;
})
const emit          = defineEmits(['success'])
const visible       = ref(false);
const type          = ref('ADD');
const disabled      = computed(()=>type.value=='SHOW');
const title         = ref('');
const formData      = ref({});
const formRef       = ref(null);
const handleOk      = ()=>{
    formRef.value.validateFields().then(vRes=>{
        let apiKey               = type.value=='ADD'?'correlationAdd':'correlationEdit';
        formData.value.companyId = companyIdEnd.value;
        
        api.investment[apiKey](formData.value,'companyRiskInspection').then(async res=>{
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
const titleObj = {ADD:'上传',EDIT:'修改',SHOW:'查看'};
const open     = (key,data,companyName)=>{
    type.value    = key;
    try {
        formData.value = JSON.parse(JSON.stringify(data));
    } catch (e) {
        type.value     = 'ADD';
        formData.value = {};
    }
    title.value   =  titleObj[type.value] + '风险预警';
    if(key=='ADD'){
        
        if(companyName){
            formData.value.companyName  = companyName;
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
