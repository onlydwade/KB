
<template>
    <div class="menu_inner padding_box">
        <Title title="基础信息"></Title>
        <div class="description_box">
            <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                <!-- <a-descriptions-item label="项目名称">
                  <EllipsisTooltipSlot>
                      <span>  {{infoData.projectName || '-'}}</span>
                  </EllipsisTooltipSlot>
                </a-descriptions-item> -->
                <a-descriptions-item label="项目名称">{{infoData.projectName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="所属大区">{{infoData.regionName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="归属单位">{{infoData.companyName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="归属人">{{(infoData.attributorUser || {}).realname}}</a-descriptions-item>
                <a-descriptions-item label="项目优先级">{{infoData.projectLevelStr || '-'}}</a-descriptions-item>
                <a-descriptions-item label="项目类型">{{infoData.projectTypeStr || '-'}}</a-descriptions-item>

                <template v-if="infoData.projectType=='GU_QUAN_HE_ZUO_XIANG_MU'">
                    <a-descriptions-item label="目标公司名称">
                        <EllipsisTooltipSlot>
                            <span>{{infoData.targetCompanyName || '-'}}</span>
                        </EllipsisTooltipSlot>
                    </a-descriptions-item>
                    <a-descriptions-item label="合作模式">
                        {{infoData.cooperationTypeStr || ''}}
                        <span v-if="(infoData.cooperationType || '').includes('QI_TA') && infoData.cooperationTypeOther">-{{infoData.cooperationTypeOther}}</span>
                    </a-descriptions-item>
                    <a-descriptions-item label="目标公司社会统一信用代码">
                      <EllipsisTooltipSlot>
                         <span>{{infoData.targetCompanyNo || '-'}}</span>
                      </EllipsisTooltipSlot>
                    </a-descriptions-item>
                    <a-descriptions-item label="目标公司法定代表人">{{infoData.targetLegalRepresentative || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="目标公司类型">{{infoData.targetCompanyTypeStr || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="目标公司注册资本(万元)">{{infoData.targetRegisteredCapital || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="目标公司所属业态">{{infoData.targetBusinessTypeStr || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="目标公司成立日期">{{infoData.targetIncorporationTime || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="合作人员规模">{{infoData.targetPersonnel || '-'}}</a-descriptions-item>
                </template>
                <template v-else>
                    <a-descriptions-item label="拓展模式">{{infoData.expansionModeStr || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="是否招标">{{infoData.bidedStr || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="合作方式">{{infoData.cooperationModeStr || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="是否存量项目">{{infoData.inStockStr || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="业态">{{infoData.businessTypeStr || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="服务内容">
                        {{infoData.serviceContentStr || ''}}
                        <span v-if="infoData.serviceContentOther">,{{infoData.serviceContentOther}}</span>
                    </a-descriptions-item>
                    <a-descriptions-item label="关联客户">{{(infoData.customer || {}).name}}</a-descriptions-item>
                    <a-descriptions-item label="建筑面积（㎡）">{{infoData.constructionArea || '-'}}</a-descriptions-item>
                </template>

                <a-descriptions-item label="省份">{{infoData.provinceName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="城市">{{infoData.cityName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="区/县">{{infoData.areaName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="街道">{{infoData.streetName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="详细地址">
                  <EllipsisTooltipSlot>
                         <span>{{infoData.address || '-'}}</span>
                  </EllipsisTooltipSlot>
                </a-descriptions-item>
                <a-descriptions-item label="预计进场时间">{{dateFormat(infoData.approachTime,'YYYY-MM-DD')}}</a-descriptions-item>
                <a-descriptions-item label="项目预估金额（元）">{{infoData.bidingBudget || '-'}}</a-descriptions-item>
                <a-descriptions-item label="备注">{{infoData.remark || '-'}}</a-descriptions-item>
            </a-descriptions>
        </div>

        <template v-if="infoData.projectType=='DAN_YI_TOU_BIAO_XIANG_MU'">
            <Title title="招投标信息"></Title>
            <div class="description_box">
                <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                    <a-descriptions-item label="招标编号">{{infoData.bidingNo || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="招标类型">{{infoData.bidingModeStr || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="发布日期">{{dateFormat(infoData.bidingPublishtime,'YYYY-MM-DD')}}</a-descriptions-item>
                    <a-descriptions-item label="招标单位">{{infoData.bidingCompany || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="招标单位联系人">{{infoData.bidingCompanyContact || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="招标单位联系方式">{{infoData.bidingCompanyPhone || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="招标代理机构">{{infoData.bidingAgency || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="代理机构联系人">{{infoData.bidingAgencyContact || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="代理机构联系方式">{{infoData.bidingAgencyPhone || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="投标截止时间">{{infoData.bidingEndtime || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="开标时间">{{dateFormat(infoData.bidingOpentime,'YYYY-MM-DD')}}</a-descriptions-item>
                    <a-descriptions-item label="招标网址">{{infoData.bidingWebsite || '-'}}</a-descriptions-item>
                 </a-descriptions>
            </div>
        </template>

        <template v-if="infoData.projectType!='GU_QUAN_HE_ZUO_XIANG_MU'">
            <Title title="合同信息"></Title>
            <div class="description_box">
                <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                    <a-descriptions-item label="甲方单位名称">{{infoData.firstResponsibleCompany || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="合同总金额（元）">{{infoData.contractAmount || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="合同年度额（元）">{{infoData.contractAnnualAmount || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="当年转化金额（元）">{{infoData.annualConversionAmount || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="测算利润率">{{infoData.calculateProfitRate || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="签约日期">{{dateFormat(infoData.signTime,'YYYY-MM-DD')}}</a-descriptions-item>
                    <a-descriptions-item label="服务开始日期">{{dateFormat(infoData.serviceBeginTime,'YYYY-MM-DD')}}</a-descriptions-item>
                    <a-descriptions-item label="合同到期日期">{{dateFormat(infoData.serviceEndTime,'YYYY-MM-DD')}}</a-descriptions-item>
                    <a-descriptions-item label="拟服务期限（月）">{{infoData.proposedServicePeriod || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="合同服务内容">{{infoData.serviceContentStr || '-'}}</a-descriptions-item>
                    <a-descriptions-item label="服务面积（㎡）">{{infoData.constructionArea || '-'}}</a-descriptions-item>
                 </a-descriptions>
            </div>
        </template>
        <Title title="拓后交接信息"></Title>
        <div class="description_box">
            <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                <a-descriptions-item label="归属单位">{{infoData.companyName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="拓展负责人">{{(infoData.attributorUser || {}).realname || '-'}}</a-descriptions-item>
                <a-descriptions-item label="甲方单位名称">{{infoData.firstResponsibleCompany || '-'}}</a-descriptions-item>
                <a-descriptions-item label="运营团队">{{infoData.operationTeamName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="拓后负责人">{{(infoData.principal || {}).realname || '-'}}</a-descriptions-item>
                <a-descriptions-item label="实际进场时间">{{dateFormat(infoData.enterTime,'YYYY-MM-DD')}}</a-descriptions-item>
             </a-descriptions>
        </div>
        <Title title="特殊说明"></Title>
        <div class="description_box">
            <a-descriptions :column="{ xxl: 1 }">
                <a-descriptions-item label="优惠承诺">{{infoData.discountsDesc || '-'}}</a-descriptions-item>
                <a-descriptions-item label="服务承诺">{{infoData.serviceDesc || '-'}}</a-descriptions-item>
                <a-descriptions-item label="主要条款">{{infoData.mainClause || '-'}}</a-descriptions-item>
                <a-descriptions-item label="其他风险提示">{{infoData.riskWarning || '-'}}</a-descriptions-item>
             </a-descriptions>
        </div>
        <Title title="商务关系维护"></Title>
        <div class="description_box">
            <a-descriptions :column="{ xxl: 1,xl: 1, lg: 1 }">
                <a-descriptions-item label="拓展人员是否把甲方对接人微信推送给运营负责人，并完成好友添加">{{infoData.wechatFriends =='SHI' ?'是':'否' ||  '-'}}</a-descriptions-item>
                <a-descriptions-item label="拓展人员是否带运营负责人完成项目现场的走访熟悉  ，交代风险点">{{infoData.interviewed =='SHI' ?'是':'否' || '-'}}</a-descriptions-item>
                <a-descriptions-item label="其他补充">{{infoData.otherDesc ||  '-'}}</a-descriptions-item>
             </a-descriptions>
        </div>
        <Title title="拓后处理信息"></Title>
        <div class="description_box">
            <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                <a-descriptions-item label="处理状态">{{infoData.processState == 0 ? '未处理' : infoData.processState == 1 ? '审批中' : infoData.processState == 2 ? '已处理' : infoData.processState == 3 ? '处理被驳回' : '-' }}</a-descriptions-item>
                <a-descriptions-item label="处理方式">{{infoData.processMode == 0 ? '未续签' : infoData.processMode == 1 ? '续签' : infoData.processMode == 2 ? '重新投标' : infoData.processMode == 3 ? '退场' : '-'}}</a-descriptions-item>
                <a-descriptions-item label="关联新建项目">{{infoData.relevanceProjectNo || '-'}}</a-descriptions-item>
                <a-descriptions-item label="退场描述">{{infoData.exitDescription || '-'}}</a-descriptions-item>
                <a-descriptions-item label="退场日期">{{infoData.processMode == 3 ? infoData.approvalSponsorTime:'-'}}</a-descriptions-item>
                <a-descriptions-item label="处理人">{{infoData.processPerson || '-'}}</a-descriptions-item>
                <a-descriptions-item label="处理时间">{{infoData.disposeTime || '-'}}</a-descriptions-item>
                <a-descriptions-item label="对应OA审批流程">
                  <span
                    class="color-link"
                    @click="toOaLink(infoData.approvalProcessOA)">{{infoData.approvalProcessOA || '-'}}
                  </span>
                </a-descriptions-item>
             </a-descriptions>
        </div>
        <Title title="系统信息"></Title>
        <div class="description_box">
            <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                <a-descriptions-item label="创建人">{{(infoData.createUser || {}).realname || '-'}}</a-descriptions-item>
                <a-descriptions-item label="创建时间">{{infoData.createTime || '-'}}</a-descriptions-item>
                <a-descriptions-item label="最后修改人">{{(infoData.updateUser || {}).realname || '-'}}</a-descriptions-item>
                <a-descriptions-item label="最后修改时间">{{infoData.updateTime || '-'}}</a-descriptions-item>
                <a-descriptions-item label="最新跟进时间">{{infoData.followTime || '-'}}</a-descriptions-item>
             </a-descriptions>
        </div>
    </div>
</template>
<script setup>
import api from "@/api/index";
import { mainStore } from '@/store'
const store = mainStore()
const infoData = inject('getAutoParams')();
const toOaLink = (link)=>{
    api.common.getSsoToken({
        mobile:store.userInfo.phonenumber
    }).then(res=>{
        if(res.code==200 && res.data){
            window.open(link+'&access_token='+res.data);
        }
    })
}
</script>
<style scoped lang="less">
.description_box{
    margin         : 16px;
    padding-bottom : 24px;
    margin-bottom  : 24px;
    border-bottom  : 1px solid @border-color-base;
    &:last-child{
        border-bottom  : none;
        // padding-bottom : 0;
        margin-bottom  : 0;
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
