
<template>
    <div class="menu_inner padding_box">
        <Title title="基础信息"></Title>
        <div class="description_box">
            <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
<!--                <a-descriptions-item label="公司标识">{{ infoData.corporateIdentityStr || '-' }}</a-descriptions-item>-->
                <a-descriptions-item label="项目名称">{{ infoData.projectName || '-' }}</a-descriptions-item>
                <a-descriptions-item label="所属大区">{{ infoData.regionName || '-' }}</a-descriptions-item>
                <a-descriptions-item label="归属单位">{{ infoData.companyName || '-' }}</a-descriptions-item>
                <a-descriptions-item label="归属人">{{ (infoData.attributorUser || {}).realname }}</a-descriptions-item>
                <a-descriptions-item label="项目优先级">{{ infoData.projectLevelStr || '-' }}</a-descriptions-item>
                <a-descriptions-item label="项目类型">{{ infoData.projectTypeStr || '-' }}</a-descriptions-item>

                <template v-if="infoData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">
                    <a-descriptions-item label="目标公司名称">{{ infoData.targetCompanyName || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="合作模式">
                        {{ infoData.cooperationTypeStr || '' }}
                        <span v-if="(infoData.cooperationType || '').includes('QI_TA') && infoData.cooperationTypeOther">-{{
                            infoData.cooperationTypeOther }}</span>
                    </a-descriptions-item>
                    <a-descriptions-item label="目标公司社会统一信用代码">{{ infoData.targetCompanyNo || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="目标公司法定代表人">
                        {{ infoData.targetLegalRepresentative || '-' }}
                    </a-descriptions-item>
                    <a-descriptions-item label="目标公司类型">
                        {{ infoData.targetCompanyTypeStr || '-' }}
                    </a-descriptions-item>
                    <a-descriptions-item label="目标公司注册资本(万元)">
                        {{ infoData.targetRegisteredCapital || '-' }}
                    </a-descriptions-item>
                    <a-descriptions-item label="目标公司所属业态">{{ infoData.targetBusinessTypeStr || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="目标公司成立日期">
                        {{ infoData.targetIncorporationTime || '-' }}
                    </a-descriptions-item>
                    <a-descriptions-item label="合作人员规模">{{ infoData.targetPersonnel || '-' }}</a-descriptions-item>
                </template>
                <template v-else>
                    <a-descriptions-item label="业务板块">{{ infoData.businessSegmentsStr || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="拓展模式">{{ infoData.expansionModeStr || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="是否招标">{{ infoData.bidedStr || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="合作方式">{{ infoData.cooperationModeStr || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="是否为续签项目">{{ infoData.inStockStr || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="业态">{{ infoData.businessTypeStr || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="服务内容">{{ infoData.serviceContentStr || '' }}</a-descriptions-item>
                    <template v-if="(infoData.serviceContent || '').includes('QI_TA')">
                      <a-descriptions-item label="其他服务内容">{{ infoData.serviceContentOther || '-' }}</a-descriptions-item>
                    </template>
                    <a-descriptions-item label="关联客户">{{ (infoData.customer || {}).name }}</a-descriptions-item>
                    <a-descriptions-item label="建筑面积（㎡）">{{ infoData.constructionArea || '-' }}</a-descriptions-item>
                </template>

                <a-descriptions-item label="省份">{{ infoData.provinceName || '-' }}</a-descriptions-item>
                <a-descriptions-item label="城市">{{ infoData.cityName || '-' }}</a-descriptions-item>
                <a-descriptions-item label="区/县">{{ infoData.areaName || '-' }}</a-descriptions-item>
                <a-descriptions-item label="街道">{{ infoData.streetName || '-' }}</a-descriptions-item>
                <a-descriptions-item label="详细地址">{{ infoData.address || '-' }}</a-descriptions-item>
                <a-descriptions-item label="预计进场时间">
                    {{ dateFormat(infoData.approachTime, 'YYYY-MM-DD') }}
                </a-descriptions-item>
                <a-descriptions-item label="项目预估金额（元）">{{ infoData.bidingBudget || '-' }}</a-descriptions-item>
                <a-descriptions-item label="备注">{{ infoData.remark || '-' }}</a-descriptions-item>
            </a-descriptions>
        </div>

        <template v-if="infoData.projectType == 'DAN_YI_TOU_BIAO_XIANG_MU'">
            <Title title="招投标信息"></Title>
            <div class="description_box">
                <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                    <a-descriptions-item label="招标编号">{{ infoData.bidingNo || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="招标类型">{{ infoData.bidingModeStr || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="发布日期">
                        {{ dateFormat(infoData.bidingPublishtime, 'YYYY-MM-DD') }}
                    </a-descriptions-item>
                    <a-descriptions-item label="招标单位">{{ infoData.bidingCompany || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="招标单位联系人">{{ infoData.bidingCompanyContact || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="招标单位联系方式">{{ infoData.bidingCompanyPhone || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="招标代理机构">{{ infoData.bidingAgency || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="代理机构联系人">{{ infoData.bidingAgencyContact || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="代理机构联系方式">{{ infoData.bidingAgencyPhone || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="投标截止时间">
                        {{ dateFormat(infoData.bidingEndtime, 'YYYY-MM-DD') }}
                    </a-descriptions-item>
                    <a-descriptions-item label="开标时间">
                        {{ dateFormat(infoData.bidingOpentime, 'YYYY-MM-DD') }}
                    </a-descriptions-item>
                    <a-descriptions-item label="招标网址">{{ infoData.bidingWebsite || '-' }}</a-descriptions-item>
                </a-descriptions>
            </div>
        </template>

        <template v-if="infoData.projectType != 'GU_QUAN_HE_ZUO_XIANG_MU'">
            <Title title="合同信息"></Title>
            <div class="description_box">
                <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                    <a-descriptions-item label="合同总金额（元）">{{ infoData.contractAmount || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="合同年度额（元）">{{ infoData.contractAnnualAmount || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="当年转化金额（元）">
                        {{ infoData.annualConversionAmount || '-' }}
                    </a-descriptions-item>
                    <a-descriptions-item label="签约日期">
                        {{ dateFormat(infoData.signTime, 'YYYY-MM-DD') }}
                    </a-descriptions-item>
                    <a-descriptions-item label="服务开始日期">
                        {{ dateFormat(infoData.serviceBeginTime, 'YYYY-MM-DD') }}
                    </a-descriptions-item>
                    <a-descriptions-item label="合同到期日期">
                        {{ dateFormat(infoData.serviceEndTime, 'YYYY-MM-DD') }}
                    </a-descriptions-item>
                    <a-descriptions-item label="拟服务期限（月）">{{ infoData.proposedServicePeriod || '-' }}</a-descriptions-item>
                    <a-descriptions-item label="合同服务内容">{{ infoData.serviceContentStr || '-' }}</a-descriptions-item>
                </a-descriptions>
            </div>
        </template>
        <Title title="系统信息"></Title>
        <div class="description_box">
            <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                <a-descriptions-item label="创建人">{{ (infoData.createUser || {}).realname || '-' }}</a-descriptions-item>
                <a-descriptions-item label="创建时间">{{ infoData.createTime || '-' }}</a-descriptions-item>
                <a-descriptions-item label="最后修改人">{{ (infoData.updateUser || {}).realname || '-' }}</a-descriptions-item>
                <a-descriptions-item label="最后修改时间">{{ infoData.updateTime || '-' }}</a-descriptions-item>
                <a-descriptions-item label="最新跟进时间">{{ infoData.followTime || '-' }}</a-descriptions-item>
            </a-descriptions>
        </div>
    </div>
</template>
<script setup>
const infoData = inject('getAutoParams')();
</script>
<style scoped lang="less">
.description_box {
    margin: 16px;
    padding-bottom: 24px;
    margin-bottom: 24px;
    border-bottom: 1px solid @border-color-base;

    &:last-child {
        border-bottom: none;
        // padding-bottom : 0;
        margin-bottom: 0;
    }

    :v-deep .ant-descriptions-item-content {
        display: table-cell;
        flex: 1;
        color: rgba(0, 0, 0, 0.7);
        font-size: 14px;
        line-height: 1.5715;
        word-break: break-word;
        overflow-wrap: break-word;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }
}
</style>
