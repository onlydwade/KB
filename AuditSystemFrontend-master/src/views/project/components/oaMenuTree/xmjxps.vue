<template>
    <div class="menu_inner">
        <div class="card_box">
            <van-cell-group title="营收信息">
                <van-cell title="预计年营收（万元）" :value="parseFormatNum(formData.revenueInfo, 2)" />
                <van-cell title="年平均利润率（%）" :value="formData.annualAvgProfitMargin || '-'" />
            </van-cell-group>
        </div>
        <div class="card_box">
            <van-cell-group title="项目信息">
                <van-cell title="项目名称" :value="formData.projectName || '-'" />
                <van-cell title="运营模式" :value="formData.operationModeStr || '-'" />
                <van-cell v-if="formData.operationMode === 'WAI_BAO' || formData.operationMode === 'ZI_YIN_WAI_BAO'" title="外包单位名称"
                    :value="formData.outsourcingUnitName || '-'" />
            </van-cell-group>
        </div>
        <!-- <div class="card_box">
            <van-cell-group title="业绩信息">
                <van-cell title="测算利润率（%）" :value="formData.calculateProfitRate || '-'" />
            </van-cell-group>
        </div> -->
        <ContractSatisYd   :title="formData.operationMode === 'ZI_YIN_WAI_BAO'?'预估成本/外包费用': formData.operationMode === 'WAI_BAO' ? '外包费用' : '预估成本' "
            :list="formData.projectEstimatedCosts" />

        <IndicatorsYd :projectId="projectId" />
        <collectionRateYd :formData="formData" />
        <FileList :menuId="menuId" :projectId="projectId" />
    </div>
</template>
<script setup>
import { useMenuTree } from "./menu";
import IndicatorsYd from "./components/IndicatorsYd.vue";
import ContractSatisYd from "./components/ContractSatisYd.vue";
import collectionRateYd from "./components/collectionRateYd.vue";
import { parseFormatNum } from '@/utils/tools';
import { ref } from "vue";

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

const { formData, getInfo } = useMenuTree(props.projectId, toRefs(props).menuInfo);

onMounted(() => {
    getInfo((res) => {
    formData.value.projectEstimatedCosts = res.projectEstimatedCosts || []
    });
});

</script>
<style scoped lang="less">
.menu_inner {
    .card_box {
        margin-bottom: 20px;
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
