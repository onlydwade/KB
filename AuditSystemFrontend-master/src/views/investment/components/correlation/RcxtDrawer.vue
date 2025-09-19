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
                    <a-form-item required label="事项类型" name="type">
                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.type"
                            class="w_full"
                            placeholder="请选择"
                            :options="dict.options('SHI_XIANG_LEI_XING')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="记录人" name="takeId">
                        <UserSelect v-model="formData.takeId" :users="formData.take" :disabled="disabled"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="标题" name="title">
                        <a-input :disabled="disabled" allowClear v-model:value="formData.title" show-count :maxlength="100"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12"></a-col>
                <template v-if="formData.type=='BAI_FANG_JI_LU'">
                    <a-col :span="12">
                        <a-form-item required label="拜访日期" name="visitTime">
                            <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                                :disabled="disabled"
                                v-model:value="formData.visitTime"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item required label="拜访对象" name="interviewee">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.interviewee"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item required label="拜访地点" name="visitLocaltion">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.visitLocaltion"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item required label="拜访方式" name="visitMethod">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.visitMethod"/>
                        </a-form-item>
                    </a-col>
                </template>
                <template v-if="formData.type=='DIAO_YAN'">
                    <a-col :span="12">
                        <a-form-item required label="调研年份" name="surveyYear">
                            <a-date-picker
                            :disabled="disabled"
                            v-model:value="formData.surveyYear"
                            picker="year"
                            :disabled-date="disabledDate"
                            class="w_full"
                            valueFormat="YYYY"
                            format="YYYY"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="调研周期" :name="formData.surveyCycleType=='DIAO_YAN_NIAN_DU'?'surveyCycleType':'surveyCycle'">
                            <div class="flex_box">
                                <a-select
                                    v-model:value="formData.surveyCycleType"
                                    :disabled="disabled"
                                    class="flex_full"
                                    placeholder="请选择"
                                    @change="()=>{
                                        formData.surveyCycle = null
                                    }"
                                    :options="dict.options('BAO_GAO_ZHOU_QI_LEI_XING')">
                                </a-select>
                                <a-form-item-rest>
                                    <a-input class="flex_full" disabled v-if="formData.surveyCycleType=='DIAO_YAN_NIAN_DU'" :value="formData.surveyYear" />
                                    <a-select
                                        v-else
                                        :disabled="disabled"
                                        v-model:value="formData.surveyCycle"
                                        class="flex_full"
                                        style="margin-left:4px;"
                                        placeholder="请选择"
                                        :options="dict.options(formData.surveyCycleType)">
                                    </a-select>
                                    
                                </a-form-item-rest>
                            </div>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item required label="调研日期" name="visitTime">
                            <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                                :disabled="disabled"
                                v-model:value="formData.visitTime"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item required label="调研组人员" name="investigator">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.investigator"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item required label="调研对象" name="targetInvestigator">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.targetInvestigator"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item required label="调研方式" name="visitMethod">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.visitMethod"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item required label="调研地点" name="visitLocaltion">
                            <a-input :disabled="disabled" allowClear v-model:value="formData.visitLocaltion"/>
                        </a-form-item>
                    </a-col>
                </template>
            </a-row>
            
            <a-form-item required label="内容描述" name="content">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.content" placeholder="请输入(1000字以内)" show-count :maxlength="1000"/>
            </a-form-item>

            <Title class="titlebar" title="文件上传">
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
        default : 42,
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
        
        api.investment[apiKey](formData.value,'companyCoordination').then(async res=>{
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
const open     = (key,data,dfaultType)=>{
    type.value    = key;
    try {
        formData.value = JSON.parse(JSON.stringify(data));
    } catch (e) {
        type.value     = 'ADD';
        formData.value = {};
    }
    title.value   = titleObj[type.value] +'日常协调信息';
    if(key=='ADD'){
        formData.value.type=dfaultType;
        
        formData.value.takeId = store.userInfo.userId;
        formData.value.take = {
            userId   : store.userInfo.userId,
            realname : store.userInfo.realname
        }
    }
    
    visible.value = true;
}
defineExpose({open})


const disabledDate = (current) => {
  return current && current > new Date();
};
</script>
<style scoped lang="less">
.titlebar{
    margin : 16px -16px;
    &:first-child{
        margin-top : -16px;
    }
}
</style>
