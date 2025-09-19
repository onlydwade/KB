<template>
  <div class="menu_inner">
    <div class="card_box">
      <van-cell-group title="项目信息">
        <van-cell title="项目名称" :value="formData.projectName || '-'" />
        <van-cell title="归属单位" :value="formData.companyName || '-'" />
        <van-cell title="报价金额（元）" :value="parseFormatNum(formData.bidingAmount) || '-'" />
        <van-cell title="测算利润率(%)" :value="formData.calculateProfitRate || '-'" />
        <van-cell title="优先级" :value="formData.projectLevelStr || '-'" />
        <van-cell v-if="formData.projectLevel === 'ZHONG_DIAN_GUAN_ZHU_XIANG_MU'" title="重点项目类型" :value="formData.keyFocusProjectStr || '-'" />
        <van-cell v-if="formData.keyFocusProject && formData.keyFocusProject.includes('6')" title="其他特殊情况" :value="formData.otherDescription || '-'" />
      </van-cell-group>
    </div>
    <FileList :menuId="menuId" :projectId="projectId" />
  </div>
</template>
<script setup>
import { useMenuTree } from "./menu";
import { parseFormatNum } from "@/utils/tools";
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

const {
  formData,
  getInfo,
} = useMenuTree(props.projectId, toRefs(props).menuInfo);

onMounted(() => {
  getInfo();
});
</script>
<style scoped lang="less">
.menu_inner {
  .card_box {
    padding-bottom: 20px;
    border-radius: 12px;
    background: #fff;
  }

  .van-cell {
    color: #969799;
  }

  :v-deep .van-cell__value {
    color: #000;
  }

  :v-deep .van-cell-group__title {
    color: #000;
    font-weight: bold;
  }
}
</style>
