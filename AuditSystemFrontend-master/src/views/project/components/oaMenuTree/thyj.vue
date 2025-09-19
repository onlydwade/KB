<template>
    <div class="menu_inner">
        <div class="card_box">
            <van-cell-group title=" 项目基础信息">
                <van-cell title="项目名称" :value="formData.projectName || '-'" />
                <van-cell title="省/市/区/县/街道">
                    <template #value>
                        {{ formData.provinceName }}
                        {{ formData.cityName }}
                        {{ formData.areaName }}
                        {{ formData.streetName }}
                    </template>
                </van-cell>
                <van-cell title="合作方式" :value="formData.cooperationModeStr || '-'" />
                <van-cell title="业态" :value="formData.businessTypeStr || '-'" />
                <van-cell title="服务内容" :value="formData.serviceContentStr || '-'" />
                <van-cell title="合同总金额（元）" :value="parseFormatNum(formData.contractAmount, 2) || '-'" />
                <van-cell title="建筑面积（m²）" :value="parseFormatNum(formData.constructionArea, 2) || '-'" />
            </van-cell-group>
            <van-cell-group title="拓展信息">
                <van-cell title="归属单位" :value="formData.companyName || '-'" />
                <van-cell title="拓展归属人" :value="(formData.attributorUser || {}).realname || '-'" />
                <van-cell title="甲方单位名称" :value="formData.firstResponsibleCompany || '-'" />
                <van-cell title="运营团队"
                    :value="getNodeById(store.getDeptWithOutRoot, formData.operationTeamId, 'deptId') || '-'" />
                <van-cell title="拓后负责人" :value="(formData.principal || {}).realname || '-'" />
                <van-cell title="实际进场时间" :value="dateFormat(formData.enterTime, 'YYYY-MM-DD') || '-'" />
            </van-cell-group>
        </div>
        <!-- <IndicatorsYd :projectId="projectId" />
        <collectionRateYd :formData="formData" /> -->
        <van-cell-group title="特殊说明">
            <van-cell title="优惠承诺" :value="formData.discountsDesc || '-'" />
            <van-cell title="服务承诺" :value="formData.serviceDesc || '-'" />
            <van-cell title="主要条款" :value="formData.mainClause || '-'" />
            <van-cell title="其他风险提示" :value="formData.riskWarning || '-'" />
        </van-cell-group>
        <van-cell-group title="商务关系维护">
            <van-cell title="拓展人员是否把甲方对接人微信推送给运营负责人，并完成好友添加？" :value="SHIFOUFormat(formData.wechatFriends) || '-'" />
            <van-cell title="拓展人员是否带运营负责人完成项目现场的走访熟悉  ，交代风险点？" :value="SHIFOUFormat(formData.interviewed) || '-'" />
            <van-cell title="其他补充" :value="formData.otherDesc || '-'" />
        </van-cell-group>
        <FileList :menuId="menuId" :projectId="projectId" />
    </div>
</template>
<script setup>
import { useMenuTree } from './menu';
import { SHIFOUFormat, parseFormatNum, dateFormat, getNodeById } from '@/utils/tools';
// import IndicatorsYd from "./components/IndicatorsYd.vue";
// import collectionRateYd from "./components/collectionRateYd.vue";
import { mainStore } from '@/store';
const store = mainStore();
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

const formAttrs = ['id', 'operationTeamId', 'principalId', 'enterTime', 'collectionRateFirst', 'collectionRateSecond', 'collectionRateThird', 'discountsDesc', 'serviceDesc', 'mainClause', 'riskWarning', 'wechatFriends', 'interviewed', 'otherDesc'];
const {
    formData,
    getInfo,
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs);
onMounted(() => {
    getInfo()
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