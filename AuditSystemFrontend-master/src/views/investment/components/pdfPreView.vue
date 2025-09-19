<template>
  <a-drawer :maskClosable="false" width="70%" @close="handleClose" destroyOnClose :visible="visible">
    <template #extra>
      <a-space :size="16">
        <a-button size="large" type="primary" @click="getPdf" :loading="loading">
          导出pdf
        </a-button>
      </a-space>
    </template>
    <a-spin :spinning="loading">
      <div class="pdf-preview" id="pdfDom">
        <a-form :label-col="{ style: { width: '220px' } }" labelAlign="left">
          <Title class="titlebar" title="信息内容"></Title>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="企业名称:">{{ formData.companyName }}</a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="报告名称:">{{ formData.name }}</a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="报告期:">{{ formData.year }}年 - {{ formData.cycleTypeStr }} / {{ formData.cycleStr
              }}</a-form-item>
            </a-col>
            <a-col :span="12" v-if="companyIdEnd">
              <a-form-item label="报告方式:">{{ formData.reportMethodStr }}</a-form-item>
            </a-col>
            <a-col :span="22">
              <a-form-item label="报告说明:"> {{ formData.description }} </a-form-item>
            </a-col>
          </a-row>
          <div v-if="formData.reportMethod == 'XIAN_SHANG_JI_LU'">
            <Title class="titlebar" title="公司基本情况"></Title>
            <a-row :gutter="24">
              <a-col :span="12">
                <a-form-item label="公司及子公司股权结构:">{{ formData.ownershipStructure }}</a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="公司内部组织架构:">{{ formData.organization }}</a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="公司核心团队情况:">{{ formData.coreTeam }}</a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="公司职员情况及员工激励政策:">{{ formData.officeWorker }}</a-form-item>
              </a-col>
              <a-col :span="22">
                <OperateExecutivesPDF :readOnly="true" :companyId="companyIdEnd" :reportId="formData.id"
                  v-model="formData.executivesList" />
              </a-col>
              <a-col :span="22" style="margin-top: 10px;">
                <a-form-item label="公司其他情况:">{{ formData.basicOther }}</a-form-item>
              </a-col>
            </a-row>
            <Title class="titlebar" title="公司业务情况"></Title>
            <a-row :gutter="24">
              <a-col :span="22">
                <a-form-item label="行业基本情况及变化:">{{ formData.industryInformation }}</a-form-item>
              </a-col>
              <a-col :span="22">
                <a-form-item label="经营分析:">{{ formData.operateAnalysis }}</a-form-item>
              </a-col>
              <a-col :span="22">
                <a-form-item label="供应商和客户、研发和投资情况与变化" class="long-label">{{ formData.externalSituation }}</a-form-item>
              </a-col>
            </a-row>
            <Title class="titlebar" title="财务分析"></Title>
            <a-row :gutter="24">
              <a-col :span="22">
                <a-form-item label="财务分析整体描述:">{{ formData.financialSituation }}</a-form-item>
              </a-col>
              <a-col :span="22">
                <a-form-item label="税务情况:">{{ formData.taxSituation }}</a-form-item>
              </a-col>
              <a-col :span="22">
                <a-form-item label="整体交易情况:">{{ formData.accountingFirm }}</a-form-item>
              </a-col>
            </a-row>
            <Title class="titlebar" title="其他供应商"></Title>
            <a-row :gutter="24">
              <a-col :span="22">
                <a-form-item label="会计师事务所:">{{ formData.accountingFirm }}</a-form-item>
              </a-col>
              <a-col :span="22">
                <a-form-item label="律师事务所:">{{ formData.lawFirm }}</a-form-item>
              </a-col>
              <a-col :span="22">
                <a-form-item label="咨询机构:">{{ formData.advisoryBody }}</a-form-item>
              </a-col>
              <a-col :span="22">
                <a-form-item label="其他:">{{ formData.supplierOther }}</a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="22">
                <OperateItemPDF :readOnly="true" :companyId="companyIdEnd" :reportId="formData.id"
                  v-model="formData.executivesList" />
              </a-col>
            </a-row>
            <Title class="titlebar" title="其他信息"></Title>
            <a-row :gutter="24">
              <a-col :span="22">
                <a-form-item label="公司履约情况、投后义务、
            风险处置情况总结及
            后续跟进计划：" class="long-label">{{ formData.performance }}</a-form-item>
              </a-col>
              <a-col :span="22">
                <a-form-item label="投后服务（本报告期为项目企业提供的投后服务情况总结）:" class="long-label">{{ formData.investmentAfter
                }}</a-form-item>
              </a-col>
            </a-row>
          </div>
        </a-form>
      </div>
    </a-spin>
  </a-drawer>
</template>
<script setup>
import OperateExecutivesPDF from './correlation/OperateExecutivesPDF.vue';
import OperateItemPDF from './correlation/OperateItemPDF.vue'
import htmlToPdf from '@/utils/htmlToPdf';
const props = defineProps({
  formData: {
    type: Object,
    default: {},
  },
  companyId: {
    type: Number,
    default: 0,
  },
})
const butVisible = ref(true)
const visible = ref(false)
const loading = ref(false)
const companyIdEnd = computed(() => {
  return props.companyId || props.formData.companyId;
})
const open = () => {
  visible.value = true

}
const getPdf = () => {
  loading.value = true;
  nextTick(() => {

    // 调用htmlToPdf工具函数
    htmlToPdf.getPdf('经营报告', false, 'pdfDom');
    butVisible.value = false
    // 定时器模拟按钮loading动画的时间
    setTimeout(() => {
      loading.value = false;
    }, 1000);
  })
}
const handleClose = () => {
  visible.value = false;
  butVisible.value = true
}
defineExpose({ open })
</script>
<style scoped lang="less">
.long-label :v-deep .ant-form-item-label > label {
  height: auto;
  white-space: pre-line;
}

.pdf-preview {
  :v-deep .ant-form-item {
    margin-bottom: 6px !important;
  }

}

.titlebar {
  margin: 16px -16px;

  &:first-child {
    margin-top: -16px;
  }
}

.title_t {
  margin-bottom: 10px;
  color: #000;
  font-size: 16px;
}

.title_f {
  margin-bottom: 10px;
  color: #000;
  font-size: 16px;
  font-weight: bold;
}
</style>
