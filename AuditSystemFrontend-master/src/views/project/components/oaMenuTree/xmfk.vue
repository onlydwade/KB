
<template>
  <div class="menu_inner">
    <div class="card_box">
      <van-cell-group title="公司实缴注册资本">
        <van-cell title="首次出资金额（元）" :value="parseFormatNum(formData.firstCapitalAmount, 2) || '-'" />
        <van-cell title="首次出资时间" :value="dateFormat(formData.firstCapitalTime, 'YYYY-MM-DD') || '-'" />
        <van-cell title="情况说明" :value="formData.conditionExplain || '-'" />
      </van-cell-group>
    </div>
    <FileList :menuId="menuId" :projectId="projectId"
      :stepLink="{ 1031: { stepMenuId: 23, id: 1025 }, 1032: { stepMenuId: 23, id: 1026 } }" />
  </div>
</template>
<script setup>
import { useMenuTree } from './menu';
import { useDictStore } from '@/store/dict';
import { parseFormatNum, dateFormat } from '@/utils/tools';
const dict = useDictStore();
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
  }
})

const formAttrs = ['id', 'ddMode', 'ddTeamType', 'ddAmount'];
const {
  formRef,
  documentsRef,
  formData,
  getInfo,
  submit,
  save,
  disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs, false);
onMounted(() => {
  getInfo();
})
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
}</style>
