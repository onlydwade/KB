
<template>
    <div class="menu_inner">
        <Title title="基础信息"></Title>
        <div class="description_box">
            <a-descriptions :column="column">
                <a-descriptions-item label="公司编号">
                  <EllipsisTooltipSlot>
                      <span style="width: 100% !important;float: left !important;overflow: hidden !important;text-overflow: ellipsis !important;white-space: normal !important;">  {{infoData.companyBizNo || '-'}}</span>
                  </EllipsisTooltipSlot>
                </a-descriptions-item>
                <a-descriptions-item label="企业名称">
                  <EllipsisTooltipSlot>
                      <span style="width: 100% !important;float: left !important;overflow: hidden !important;text-overflow: ellipsis !important;white-space: normal !important;">   {{infoData.name || '-'}}</span>
                  </EllipsisTooltipSlot>
                </a-descriptions-item>
                <a-descriptions-item label="投后负责人">{{(infoData.principal || {}).realname}}</a-descriptions-item>
                <a-descriptions-item label="投前项目名称">
                    <router-link :to="'/innerPage/projectInfo?id='+infoData.projectId" v-if="infoData.projectId" class="color-link">
                        <EllipsisTooltipSlot>
                            <span style="width: 100% !important;float: left !important;overflow: hidden !important;text-overflow: ellipsis !important;white-space: normal !important;"> {{infoData.projectName}}</span>
                        </EllipsisTooltipSlot>
                    </router-link>
                    <span v-else>-</span>
                </a-descriptions-item>
                <a-descriptions-item label="省份">{{infoData.provinceName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="城市">{{infoData.cityName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="区/县">{{infoData.areaName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="街道">{{infoData.streetName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="注册地址"> 
                    {{infoData.incorporationAddress || '-'}}  
                </a-descriptions-item>
                <a-descriptions-item label="业务所属部门">{{infoData.businessDeptName || '-'}}</a-descriptions-item>
                <a-descriptions-item label="注册资本（万元）">{{infoData.registeredCapital || '-'}}</a-descriptions-item>
                <a-descriptions-item label="行业分类">{{infoData.industryTypeStr || '-'}}</a-descriptions-item>
                <a-descriptions-item label="成立日期">{{dateFormat(infoData.incorporationTime,'YYYY-MM-DD')}}</a-descriptions-item>
                <a-descriptions-item label="社会统一信用代码(注册编码) ">
                  <EllipsisTooltipSlot>
                    <span style="width: 100% !important;float: left !important;overflow: hidden !important;text-overflow: ellipsis !important;white-space: normal !important;">  {{infoData.companyNo || '-'}}</span>
                  </EllipsisTooltipSlot>
                </a-descriptions-item>
                <a-descriptions-item label="法定代表人">{{infoData.legalRepresentative || '-'}}</a-descriptions-item>
                <a-descriptions-item label="董事长/执行董事">{{infoData.chairman || '-'}}</a-descriptions-item>
                <a-descriptions-item label="是否属于高新技术企业">{{infoData.hightechCompanyStr || '-'}}</a-descriptions-item>
                <a-descriptions-item label="是否享受国家财税政策">{{infoData.taxPolicyStr || '-'}}</a-descriptions-item>
                <a-descriptions-item label="是否属于中小企业">{{infoData.smallCompanyStr || '-'}}</a-descriptions-item>
                <a-descriptions-item label="投资类型">{{infoData.investmentTypeStr || '-'}}</a-descriptions-item>
                <a-descriptions-item label="企业人数（合同员工）">{{infoData.personnel || '-'}}</a-descriptions-item>
                <a-descriptions-item label="持股比例">{{infoData.shareholdingRatio || '-'}}%</a-descriptions-item>
                <a-descriptions-item label="是否实缴">{{infoData.paidCapitalStatusStr || '否'}}</a-descriptions-item>
                <a-descriptions-item label="实缴资金（元）">{{infoData.paidCapital || '-'}}</a-descriptions-item>

                <a-descriptions-item label="企业概况" :span="column">{{infoData.enterpriseProfile || '-'}}</a-descriptions-item>
                <a-descriptions-item label="经营范围" :span="column">{{infoData.managementRange || '-'}}</a-descriptions-item>
                <a-descriptions-item label="主营业务" :span="column">{{infoData.mainBusiness || '-'}}</a-descriptions-item>
                <a-descriptions-item label="核心竞争力描述" :span="column">{{infoData.coreCompetitiveness || '-'}}</a-descriptions-item>
                <a-descriptions-item label="重点关注" :span="column">{{infoData.importantFollow || '-'}}</a-descriptions-item>
                <a-descriptions-item label="备注" :span="column">{{infoData.remark || '-'}}</a-descriptions-item>
            </a-descriptions>
        </div>
        <a-collapse ghost expandIconPosition="right">
            <a-collapse-panel>
                <template #header>
                    <h5 class="title_single">董监高信息</h5>
                </template>
                <Executives :companyId="companyId" :readOnly="true"/>
            </a-collapse-panel>

            <a-collapse-panel>
                <template #header>
                    <h5 class="title_single">章程及工商执照</h5>
                </template>
                <CompanyDocuments :companyId="companyId" :menuId="37" :recordId="companyId" type="GONGSHAN_XINXI" :readOnly="true">
                    <template #linkDoc_0>
                        <FileZhangchen :projectId="infoData.projectId" v-if="infoData.projectId" />
                    </template>
                    <template #linkDoc_1>
                        <CompanyFileLink :companyId="companyId" :documentTemplateId="2"/>
                    </template>
                    <template #linkDoc_2>
                        <CompanyFileLink :companyId="companyId" :documentTemplateId="3"/>
                    </template>
                </CompanyDocuments>
            </a-collapse-panel>

            <a-collapse-panel v-show="existProjectTeam">
                <template #header>
                    <h5 class="title_single">信息变更</h5>
                </template>
                <CompanyDocuments :companyId="companyId" :menuId="37" :recordId="companyId" type="XINXI_BIANGEN" :readOnly="true"/>
            </a-collapse-panel>

            <a-collapse-panel>
                <template #header>
                    <h5 class="title_single">子项目</h5>
                </template>
                <SubProject :companyId="companyId" :readOnly="true"/>
            </a-collapse-panel>

            <a-collapse-panel>
                <template #header>
                    <h5 class="title_single">系统信息</h5>
                </template>
                <div class="description_box">
                    <a-descriptions :column="column">
                        <a-descriptions-item label="创建人">{{(infoData.createUser || {}).realname || '-'}}</a-descriptions-item>
                        <a-descriptions-item label="创建时间">{{infoData.createTime || '-'}}</a-descriptions-item>
                        <a-descriptions-item label="最后修改人">{{(infoData.updateUser || {}).realname || '-'}}</a-descriptions-item>
                        <a-descriptions-item label="最后修改时间">{{infoData.updateTime || '-'}}</a-descriptions-item>
                        <a-descriptions-item label="最新跟进时间">{{infoData.followTime || '-'}}</a-descriptions-item>
                     </a-descriptions>
                </div>
            </a-collapse-panel>
        </a-collapse>
    </div>
</template>
<script setup>
import Executives    from '../correlation/Executives.vue'
import SubProject    from '../correlation/SubProject.vue'
import FileZhangchen from '../FileZhangchen.vue'
const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    },
    existProjectTeam:{
        type    : Boolean,
        default : false
    }
})
const column = { xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 };
const infoData = inject('getAutoParams')();
</script>
<style scoped lang="less">
.menu_inner{
    :deep(.ant-collapse-item){
        margin     : 0 -16px;
        padding    : 16px 0;
        border-top : 1px solid #f1f1f1;
    }
}
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
