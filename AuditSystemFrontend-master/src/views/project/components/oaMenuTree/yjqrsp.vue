<template>
  <div class="menu_inner">
    <div class="card_box">
      <van-cell-group title="业绩确认信息" >
        <van-cell title="查看上个版本合同信息" v-if="formData.relevanceProjectId"  is-link  @click="gotoOldVersion()" />
        <van-cell title="甲方单位名称" :value="formData.firstResponsibleCompany || '-'" />
        <van-cell title="合同总金额（元）" :value="parseFormatNum(formData.contractAmount) || '-'" />
        <van-cell title="合同年度金额（元）" :value="parseFormatNum(formData.contractAnnualAmount) || '-'" />
        <van-cell title="当年转化金额 (元)" :value="parseFormatNum(formData.annualConversionAmount) || '-'" />
        <!-- <van-cell title="测算利润率(%)" :value="formData.calculateProfitRate || '-'" /> -->
        <van-cell title="签约日期" :value="dateFormat(formData.signTime, 'YYYY-MM-DD') || '-'" />
        <van-cell title="服务开始日期" :value="dateFormat(formData.serviceBeginTime, 'YYYY-MM-DD') || '-'" />
        <van-cell title="合同到期日期" :value="dateFormat(formData.serviceEndTime, 'YYYY-MM-DD') || '-'" />
        <van-cell title="拟服务期限（月）" :value="formData.proposedServicePeriod || '-'" />
        <van-cell title="服务内容" :value="formData.serviceContentStr || '-'" />
        <van-cell title="其它服务内容" :value="formData.serviceContentOther || '-'"
          v-if="(formData.serviceContent || '').includes('QI_TA')" />
        <van-cell title="建筑面积（㎡）" :value="formData.constructionArea || '-'" />
        <van-cell title="业务板块" :value="formData.businessSegmentsStr || '-'" />
        <van-cell title="拓展模式" :value="formData.expansionModeStr || '-'" /> 
        <van-cell title="业态" :value="formData.businessTypeStr || '-'" />         
        <van-cell title="是否为续签项目" :value="formData.inStockStr || '-'" />
        <van-cell title="是否有增量业绩确认" v-if="formData.inStock === 'SHI'" :value="formData.isPerformanceIncrementStr || '-'" /> 
      </van-cell-group>
    </div>
    <div v-if="formData.isPerformanceIncrement === 'SHI' && formData.inStock === 'SHI'" class="card_box">
      <van-cell-group title="增量业绩信息">
        <van-cell title="合同总金额（元）" :value="parseFormatNum(formData.contractAmounts) || '-'" />
        <van-cell title="合同年度金额（元）" :value="parseFormatNum(formData.contractAnnualAmounts) || '-'" />
        <van-cell title="当年转化金额 (元)" :value="parseFormatNum(formData.annualConversionAmounts) || '-'" />
      </van-cell-group>
    </div>
    <AchievementYd v-if="formData.id" :projectId="projectId" />
    <FileList :menuId="menuId" :projectId="projectId" />
  </div>
</template>

<script setup>
import api from "@/api/index";
import { amountUnit, parseFormatNum } from "@/utils/tools";
import AchievementYd from "./components/AchievementYd.vue";
import yjqrspOldVersion from "./yjqrspOldVersion.vue";

import { useMenuTree } from "./menu";
const router        = useRouter();
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
});
const formAttrs = [
  "id",
  "firstResponsibleCompany",
  "contractAmount",
  "contractAnnualAmount",
  "calculateProfitRate",
  "signTime",
  "serviceBeginTime",
  "serviceEndTime",
  "proposedServicePeriod",
  "serviceContent",
  "serviceContentOther",
  "constructionArea",
];
const { formData, getInfo } = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs);

onMounted(() => {
  getInfo(res => {
    formData.value.firstResponsibleCompany = res.firstResponsibleCompany || res.bidingCompany;
    formData.value.annualConversionAmount = formData.value.annualConversionAmount || null;
  });
});

watch(
  () => [
    formData.value.proposedServicePeriod,
    formData.value.contractAmount,
    formData.value.contractAnnualAmount,
    formData.value.signTime,
    formData.value.serviceBeginTime,
    formData.value.serviceEndTime,
  ],
  () => {
    if (
      formData.value.proposedServicePeriod &&
      (formData.value.contractAmount || formData.value.contractAnnualAmount) &&
      (formData.value.signTime || formData.value.serviceBeginTime || formData.value.serviceEndTime)
    ) {
      getAnnualConversionAmountCalculate();
    }
  },
  { deep: true }
);
const getAnnualConversionAmountCalculate = (isIncrement = 'FOU') => {
  const arr = [
    "id",
    "proposedServicePeriod",
    "contractAmount",
    "contractAnnualAmount",
    "signTime",
    "serviceBeginTime",
    "serviceEndTime",
    "projectType",
  ];
  const postData = arr.reduce((prev, key) => ({ ...prev, [key]: formData.value[key] }), {
    isIncrement,
    annualConversionAmount: 0
  })
  api.project.getAnnualConversionAmountCalculate(postData).then(res => {
    if (res.code == 200) {
      formData.value.annualConversionAmount = res.data || null;
    }
  });
};

const gotoOldVersion=()=>{
  let relevanceProjectId =formData.value.relevanceProjectId;
  router.push("/mobile/yjqrspOldVersion?id=" + relevanceProjectId+"&to=yjqrsp");
}

</script>
<style scoped lang="less">
.menu_inner {
  :v-deep .van-cell__value {
    color: #000;
  }

  :v-deep .van-cell-group__title {
    color: #000;
    font-weight: bold;
  }

  .card_box {
    margin-bottom: 20px;
  }

  .bm {
    margin: 10px;
  }
}
</style>
