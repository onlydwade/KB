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
            <a-form-item label="报告说明" name="description">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.description" placeholder="请输入(1000字以内)" show-count :maxlength="1000"/>
            </a-form-item>

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
                    <Title class="titlebar" title="公司基本情况"></Title>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item required label="公司及子公司股权结构" name="ownershipStructure">
                                <a-input :disabled="disabled" allowClear v-model:value="formData.ownershipStructure"/>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item required label="公司内部组织架构" name="organization">
                                <a-input :disabled="disabled" allowClear v-model:value="formData.organization"/>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item required label="公司核心团队情况" name="coreTeam">
                                <a-input :disabled="disabled" allowClear v-model:value="formData.coreTeam"/>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item required label="公司职员情况及员工激励政策" name="officeWorker">
                                <a-input :disabled="disabled" allowClear v-model:value="formData.officeWorker"/>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-form-item label="公司董事会人员及变化情况" name="executivesList">
                        <OperateExecutives :readOnly="disabled" v-if="visible" :companyId="companyIdEnd" :reportId="formData.id" v-model="formData.executivesList" />
                    </a-form-item>
                    <a-form-item label="公司其他情况（包括公司业务经营范围及实际从事的主要业务是否出现变化，是否存在诉讼、仲裁，是否签署重大合同，知识产权情况，环保安全情况）" name="basicOther">
                        <a-textarea :disabled="disabled" allowClear :rows="5" type="textarea" :maxlength="1000"  v-model:value="formData.basicOther" placeholder="请输入" show-count/>
                    </a-form-item>

                    <Title class="titlebar" title="公司业务情况"></Title>
                    <a-form-item required label="行业基本情况及变化" name="industryInformation">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.industryInformation" placeholder="请输入(1000字以内)" show-count :maxlength="1000"/>
                    </a-form-item>
                    <a-form-item required label="经营分析（结合投资报告中对业务的预测，阐述企业主要业务发展情况）" name="operateAnalysis">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.operateAnalysis" placeholder="请输入(1000字以内)" show-count :maxlength="1000"/>
                    </a-form-item>
                    <a-form-item label="供应商和客户、研发和投资情况与变化" name="externalSituation">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.externalSituation" placeholder="请输入(1000字以内)" show-count :maxlength="1000"/>
                    </a-form-item>

                    <Title class="titlebar" title="财务分析"></Title>
                    <a-form-item label="财务分析整体描述" name="financialSituation">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.financialSituation" placeholder="请输入(1000字以内)" show-count :maxlength="1000"/>
                    </a-form-item>
                    <a-form-item label="税务情况" name="taxSituation">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.taxSituation" placeholder="请输入(1000字以内)" show-count :maxlength="1000"/>
                    </a-form-item>
                    <a-form-item label="整体交易情况" name="transactionSituation">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.transactionSituation" placeholder="请输入(100字以内)" show-count :maxlength="1000"/>
                    </a-form-item>

                    <CompanyDocuments v-if="visible" ref="documentRef2" type="CAIWU_FENXI" :companyId="companyIdEnd" :menuId="menuId" :recordId="formData.id" :readOnly="disabled"/>
                    <Title class="titlebar" title="其他供应商"></Title>
                    <a-form-item label="会计师事务所" name="accountingFirm">
                        <a-textarea :disabled="disabled" allowClear :rows="1" type="textarea" v-model:value="formData.accountingFirm" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                    </a-form-item>
                    <a-form-item label="律师事务所" name="lawFirm">
                        <a-textarea :disabled="disabled" allowClear :rows="1" type="textarea" v-model:value="formData.lawFirm" placeholder="请输入(50字以内)" show-count :maxlength="50"/>
                    </a-form-item>
                    <a-form-item label="咨询机构" name="advisoryBody">
                        <a-textarea :disabled="disabled" allowClear :rows="1" type="textarea" v-model:value="formData.advisoryBody" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                    </a-form-item>
                    <a-form-item label="其他" name="supplierOther">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.supplierOther" placeholder="请输入(1000字以内)" show-count :maxlength="1000"/>
                    </a-form-item>

                    <CompanyDocuments v-if="visible" ref="documentRef3" type="CHENGUO_FUJIAN" :companyId="companyIdEnd" :menuId="menuId" :recordId="formData.id" :readOnly="disabled"/>
                    <Title class="titlebar" title="本报告期出现重大事项情况"></Title>
                    <a-form-item label="" name="itemList">
                        <OperateItem :readOnly="disabled" v-if="visible" :companyId="companyIdEnd" :reportId="formData.id" v-model="formData.itemList" />
                    </a-form-item>

                    <Title class="titlebar" title="其他信息"></Title>
                    <a-form-item label="公司履约情况、投后义务、风险处置情况总结及后续跟进计划" name="performance">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.performance" placeholder="请输入(1000字以内)" show-count :maxlength="1000"/>
                    </a-form-item>
                    <a-form-item label="投后服务（本报告期为项目企业提供的投后服务情况总结）" name="investmentAfter">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.investmentAfter" placeholder="请输入(1000字以内)" show-count :maxlength="1000"/>
                    </a-form-item>
                </template>
            </template>
            <a-alert :show-icon="false" banner v-else>
                <template #message>
                    选择企业后完善更多信息！！！
                </template>
            </a-alert>
        </a-form>
    </a-drawer>
</template>
<script setup>
import api               from '@/api/index';
import { message  }      from 'ant-design-vue';
import OperateExecutives from './correlation/OperateExecutives.vue';
import OperateItem       from './correlation/OperateItem.vue'
import { mainStore }     from '@/store';
import htmlToPdf from '@/utils/htmlToPdf';
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
        default : 44,
    }
})
const loading = ref(false)
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
const documentRef2   = ref(null);
const documentRef3   = ref(null);
const handleOk      = ()=>{
    formRef.value.validateFields().then(vRes=>{
        let apiKey               = type.value=='ADD'?'correlationAdd':'correlationEdit';
        formData.value.companyId = companyIdEnd.value;

        api.investment[apiKey](formData.value,'companyOperateReport').then(async res=>{
            if(res.code==200){
                if(type.value=='ADD'){
                    if(formData.value.reportMethod == 'SHANG_CHAUNG_BAO_GAO'){
                        let uploadResult = await documentRef.value.batchUpLoad(res.data.id);
                    }
                    if(formData.value.reportMethod == 'XIAN_SHANG_JI_LU'){
                        let uploadResult2 = await documentRef2.value.batchUpLoad(res.data.id);
                        let uploadResult3 = await documentRef3.value.batchUpLoad(res.data.id);
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
    title.value   = '经营报告' + titleObj[type.value];
    if(key=='ADD'){
        formData.value.reportMethod = 'SHANG_CHAUNG_BAO_GAO';
        if(companyName){
            formData.value.companyName  = companyName;
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
