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
                    <a-form-item required label="报告名称" name="name">
                        <a-input :disabled="disabled" allowClear v-model:value="formData.name" show-count :maxlength="100"/>
                    </a-form-item>
                </a-col>
                
                <a-col :span="12">
                    <a-form-item required label="报告所属年份" name="year">
                        <a-date-picker
                        :disabled="disabled"
                        v-model:value="formData.year"
                        picker="year"
                        :disabled-date="disabledDate"
                        class="w_full"
                        valueFormat="YYYY"
                        format="YYYY"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="报告期" :name="formData.cycleType=='DIAO_YAN_NIAN_DU'?'cycleType':'cycle'" validateTrigger="['change', 'blur']">
                        <div class="flex_box">
                            <a-select
                                v-model:value="formData.cycleType"
                                :disabled="disabled"
                                class="flex_full"
                                placeholder="请选择"
                                @change="()=>{
                                    formData.cycle = null
                                }"
                                :options="dict.options('BAO_GAO_ZHOU_QI_LEI_XING')">
                            </a-select>
                            <a-form-item-rest>
                                <a-input class="flex_full" disabled v-if="formData.cycleType=='DIAO_YAN_NIAN_DU'" :value="formData.year" />
                                <a-select
                                    v-else
                                    :disabled="disabled"
                                    v-model:value="formData.cycle"
                                    class="flex_full"
                                    style="margin-left:4px;"
                                    placeholder="请选择"
                                    :options="dict.options(formData.cycleType)">
                                </a-select>
                            </a-form-item-rest>
                        </div>
                    </a-form-item>
                </a-col>
            </a-row>
            
            <template v-if="companyIdEnd">
                <Title class="titlebar" title="报告方式"></Title>
                <a-form-item label="报告方式" name="reportMethod">
                    <a-radio-group v-model:value="formData.reportMethod" name="radioGroup" :disabled="disabled">
                        <a-radio v-for="(item,index) in dict.options('BAO_GAO_FANG_SHI')" :key="index" :value="item.value">{{item.label}}</a-radio>
                    </a-radio-group>
                </a-form-item>
                
                <template v-if="formData.reportMethod=='SHANG_CHAUNG_BAO_GAO'">
                    <Title class="titlebar" title="报告上传">
                        <a-form-item name="fileOk" 
                        :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                        </a-form-item>
                    </Title>
                    <CompanyDocuments v-model="formData.fileOk" v-if="visible" ref="documentRef" type="BAOGAO_SHANGCHUAN" :companyId="companyIdEnd" :menuId="menuId" :recordId="formData.id" :readOnly="disabled"/>
                </template>
                
                <template v-if="formData.reportMethod=='XIAN_SHANG_JI_LU'">
                    <Title class="titlebar" title="财务情况">
                        <a-form-item name="fileOk2" 
                        :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                        </a-form-item>
                        <template #right>
                            <!-- <a-button type="primary" @click="showCwSelect">
                                选择系统财务报告
                            </a-button> -->
                        </template>
                    </Title>
                    <CompanyDocuments v-model="formData.fileOk2" v-if="visible" ref="documentRef2" type="CAIWU_BAOGAO" :companyId="companyIdEnd" :menuId="menuId" :recordId="formData.id" :readOnly="disabled"/>
                    
                    <Title class="titlebar" title="调研情况">
                        <a-form-item name="fileOk3" 
                        :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                        </a-form-item>
                    </Title>
                    <CompanyDocuments v-model="formData.fileOk3" v-if="visible" ref="documentRef3" type="DIAOYAN_BAOGAO" :companyId="companyIdEnd" :menuId="menuId" :recordId="formData.id" :readOnly="disabled">
                        <template #linkDoc_0>
                            <ReportLinkFile :year="formData.year" :cycle="formData.cycle" :cycleType="formData.cycleType" :menuId="42" :companyId="companyIdEnd" v-if="visible" />
                        </template>
                    </CompanyDocuments>
                    <Title class="titlebar" title="经营情况">
                        <a-form-item name="fileOk4" 
                        :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                        </a-form-item>
                    </Title>
                    <CompanyDocuments v-model="formData.fileOk4" v-if="visible" ref="documentRef4" type="JINYIN_BAOGAO" :companyId="companyIdEnd" :menuId="menuId" :recordId="formData.id" :readOnly="disabled">
                        <template #linkDoc_0>
                            <ReportLinkFile :year="formData.year" :cycle="formData.cycle" :cycleType="formData.cycleType" :menuId="44" :companyId="companyIdEnd" v-if="visible" />
                        </template>
                    </CompanyDocuments>
                </template>
            </template>
            <a-alert :show-icon="false" banner v-else>
                <template #message>
                    选择企业后完善更多信息！！！
                </template>
            </a-alert>
        </a-form>
    </a-drawer>
    
    <cwSelect :companyId="companyId" ref="cwRef" @select="cwSelectRes"/>
</template>
<script setup>
import api            from '@/api/index';
import { message  }   from 'ant-design-vue';
import cwSelect       from './cwSelect.vue'
import ReportLinkFile from './correlation/ReportLinkFile.vue'
import { mainStore }  from '@/store';
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
        default : 45,
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
const documentRef   = ref(null);
const documentRef2  = ref(null);
const documentRef3  = ref(null);
const documentRef4  = ref(null);
const handleOk      = ()=>{
    formRef.value.validateFields().then(vRes=>{
        let apiKey               = type.value=='ADD'?'correlationAdd':'correlationEdit';
        formData.value.companyId = companyIdEnd.value;
        
        api.investment[apiKey](formData.value,'companyAppraise').then(async res=>{
            if(res.code==200){
                if(type.value=='ADD'){
                    if(formData.value.reportMethod == 'SHANG_CHAUNG_BAO_GAO'){
                        let uploadResult = await documentRef.value.batchUpLoad(res.data.id);
                    }
                    if(formData.value.reportMethod == 'XIAN_SHANG_JI_LU'){
                        let uploadResult2 = await documentRef2.value.batchUpLoad(res.data.id);
                        let uploadResult3 = await documentRef3.value.batchUpLoad(res.data.id);
                        let uploadResult4 = await documentRef4.value.batchUpLoad(res.data.id);
                    }
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
const titleObj = {ADD:'上传',EDIT:'修改',SHOW:'查看'};
const open     = (key,data,companyName)=>{
    type.value    = key;
    try {
        formData.value = JSON.parse(JSON.stringify(data));
    } catch (e) {
        type.value     = 'ADD';
        formData.value = {};
    }
    title.value   = '评价报告' + titleObj[type.value];
    if(key=='ADD'){
        formData.value.reportMethod = 'SHANG_CHAUNG_BAO_GAO';
        if(companyName){
            formData.value.companyName  = companyName;
        }
    }
    visible.value = true;
}
defineExpose({open})


//选择财务报告
const cwRef        = ref(null);
const showCwSelect = ()=>{
    cwRef.value.open();
}
const cwSelectRes = (files)=>{
    console.log(files)
    files.forEach((item, i) => {
        documentRef2.value.addDocument(JSON.parse(item.docmentObject),0);
    });
}
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
