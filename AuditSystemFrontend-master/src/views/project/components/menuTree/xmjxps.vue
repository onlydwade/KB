<template>
  <div class="menu_inner">
    <a-form ref="formRef" layout="vertical" :model="formData">
      <div class="padding_box">
        <Title style="margin-top: 32px">
          <template #title>
            营收信息
          </template>
        </Title>
        <a-row :gutter="24">
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item required label="预计年营收（万元）" name="revenueInfo">
              <a-input-number :disabled="disabled" v-model:value="formData.revenueInfo" class="w_full" :min="0" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item required label="年平均利润率（%）" name="annualAvgProfitMargin">
              <a-input-number :disabled="disabled" v-model:value="formData.annualAvgProfitMargin"  class="w_full"
                :min="0" />
            </a-form-item>
          </a-col>
        </a-row>
       


        <!-- <Title>
          <template #title>
            业绩信息
          </template>
        </Title>
        <a-col :xxl="6" :lg="8" :sm="12">
          <a-form-item label="测算利润率(%)" name="calculateProfitRate">
            <a-input-number disabled v-model:value="formData.calculateProfitRate" class="w_full" :min="0" />
          </a-form-item>
        </a-col> -->

        <Title>
          <template #title> 经营指标信息 </template>
        </Title>
        <Indicators :projectId="projectId" :readOnly="disabled" />
              
        <Title>
          <template #title>
            结项信息
          </template>
        </Title>
        <a-row :gutter="24">
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item required label="运营模式" name="operationMode">
              <a-select :disabled="disabled" v-model:value="formData.operationMode" class="w_full" placeholder="请选择"
                :options="dict.options('YUN_YING_MO_SHI')">
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12" v-if="formData.operationMode === 'WAI_BAO' || formData.operationMode === 'ZI_YIN_WAI_BAO'">
            <a-form-item :required="formData.operationMode === 'WAI_BAO' || formData.operationMode === 'ZI_YIN_WAI_BAO'" label="外包单位名称" name="outsourcingUnitName">
              <a-input :disabled="disabled" v-model:value="formData.outsourcingUnitName" autocomplete="off"
                placeholder="请输入" />
            </a-form-item>
          </a-col>
        </a-row>

        <Title> 
          <template #title>
            {{ formData.operationMode === 'ZI_YIN_WAI_BAO'?'预估成本/外包费用': formData.operationMode === 'WAI_BAO' ? '外包费用' : '预估成本' }}
            <span class="color-danger"> * </span>
          </template>
          <a-form-item name="projectEstimatedCosts" :rules="{ required: true, message: formData.operationMode === 'WAI_BAO' ? '请完善外包费用后提交!!!' : '请完善预估成本后提交!!!' }">
          </a-form-item>
        </Title>
        <EstimatedCosts :projectId="projectId" v-model="formData.projectEstimatedCosts" :readOnly="disabled" />


        <Title style="margin-top: 32px">
          <template #title>
            收缴率指标信息
            <span v-if="formData.projectType != 'GU_QUAN_HE_ZUO_XIANG_MU'">(仅住宅类项目填报)</span>
          </template>
        </Title>
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="指标"> 收缴率指标（%） </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="第一年（%）" name="collectionRateFirst">
              <a-input-number :disabled="disabled" v-model:value="formData.collectionRateFirst" class="w_full" :min="0"
                :max="100" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="第二年（%）" name="collectionRateSecond">
              <a-input-number :disabled="disabled" v-model:value="formData.collectionRateSecond" class="w_full" :min="0"
                :max="100" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="第三年（%）" name="collectionRateThird">
              <a-input-number :disabled="disabled" v-model:value="formData.collectionRateThird" class="w_full" :min="0"
                :max="100" />
            </a-form-item>
          </a-col>
        </a-row>        
        <Title title="附件上传">
          <a-form-item name="fileOk" :rules="{ required: true, message: '( 请上传必传文件后提交!!! )', }">
          </a-form-item>
        </Title>
        <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval"
          :requiredIds="formData.operationMode !== 'WAI_BAO' ? [] : [1042]" v-if="formData.id" :menuId="menuId"
          :projectId="projectId" v-model="formData.fileOk" ref="documentsRef" :readOnly="disabled" />
      </div>
    </a-form> 
    <ProjectActions @submit="submit" :menuInfo="menuInfo" offLineTitle="已线下审批，上传会议纪要标记完成" ref="ProjectActionsRef">
      <a-button size="large" @click="submit(2)"
        v-if="menuInfo.oaApproval !== 1 && menuInfo.status === 0 && [0, 9].includes(menuInfo.approvalStatus) && !disabled && formData.serviceStatus != 'YI_FEI_ZHI'">标记完成</a-button>
      <a-button size="large" @click="save"
        v-if="(menuInfo.status == 0 && [0, 5, 9].includes(menuInfo.approvalStatus)  && formData.serviceStatus != 'YI_FEI_ZHI' )|| !disabled ">暂存</a-button>
        <!-- <a-button size="large" @click="abolish()"
        v-if="menuInfo.status == 0 && [0, 5, 9].includes(menuInfo.approvalStatus) && !disabled && formData.serviceStatus != 'YI_FEI_ZHI'">项目废止（测算及方案不通过）</a-button> -->
      </ProjectActions>

  </div>
</template>

<script setup>
import { useMenuTree } from './menu';
import { ref } from 'vue';
import { useDictStore } from '@/store/dict';
import Indicators from '../correlation/Indicators.vue'
import EstimatedCosts from '../correlation/EstimatedCosts.vue'
import {Modal } from 'ant-design-vue';
const dict = useDictStore();
const workInfo = {
    type: Object,
    default: {},
}
const ProjectActionsRef = ref(null);

const props = defineProps({
  projectId: {
    type: Number,
    default: 0,
  },
  menuId: {
    type: Number,
    default: 0,
  },
  menuInfo: {
    type: Object,
    default: {},
  },
})

const formAttrs = [
  'id',
  'operationMode',
  'outsourcingUnitName',
  'projectedProfitMargin',
  "collectionRateFirst",
  "collectionRateSecond",
  "collectionRateThird",
  'projectEstimatedCosts',
  'annualAvgProfitMargin',
  'contractAnnualAmount',
  'revenueInfo',
];

const {
  formRef,
  documentsRef,
  formData,
  getInfo,
  submit,
  save,
  disabled,
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs)

// 监听formData.operationMode的变化
watch(() => [formData.value.operationMode], () => {
  if (formData.value.operationMode !== 'WAI_BAO' && formData.value.operationMode !== 'ZI_YIN_WAI_BAO') {
    formData.value.outsourcingUnitName = ''; // 清空申请类型
  }
}, { deep: true });

const abolish = ()=>{
    Modal.confirm({
        title   : '操作确认',
        content : '确认废止项目吗?',
        onOk() {
          save(5);
        }
    });
}
const revenueInfo = null;
onMounted(() => {

  workInfo.id = 1626;
  workInfo.templateId = '18d1d8239b978f959e9b6ce4bce853ab';
  workInfo.isView = '';

  getInfo((res) => {
    formData.value.revenueInfo =  (res.revenueInfo==null||res.revenueInfo==''||res.revenueInfo==0)?(res.contractAnnualAmount/10000).toFixed(6):Number(res.revenueInfo).toFixed(6);
    formData.value.projectEstimatedCosts = res.projectEstimatedCosts || [];
    formData.value.annualAvgProfitMargin = res.annualAvgProfitMargin==null? res.calculateProfitRate :res.annualAvgProfitMargin;
  });
})
</script>

<style scoped lang="less"></style>