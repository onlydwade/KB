<template>
    <div class="content-inner">
        <a-page-header :ghost="false" :breadcrumb="{ routes }">
            <template #title>
                <EllipsisTooltip style="width:800px" class="flex_full" :content="title" />
            </template>
        </a-page-header>
        <AScrollbar style="margin:-16px -16px 0 -16px;">
            <div class="padding_box">
                <a-form layout="vertical" :model="formData" ref="formRef">
                    <a-collapse ghost expandIconPosition="right" v-model:activeKey="collapseKey" class="crl-collapse">
                        <a-collapse-panel key="base">
                            <template #header>
                                <h5 class="title_single">基础信息</h5>
                                <span class="color-danger">*</span>
                            </template>
                            <a-row :gutter="24">
<!--                                <a-col :xxl="6" :lg="8" :sm="12">-->
<!--                                  <a-form-item required label="公司标识" name="corporateIdentity">-->
<!--                                    <a-select :getPopupContainer="trigger => trigger.parentNode"-->
<!--                                              v-model:value="formData.corporateIdentity"-->
<!--                                              class="w_full"-->
<!--                                              placeholder="请选择"-->
<!--                                              :options="dict.options('GONG_SI_BIAO_SHI')">-->
<!--                                    </a-select>-->
<!--                                  </a-form-item>-->
<!--                                </a-col>-->
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="项目名称" name="projectName">
                                        <a-input allowClear v-model:value="formData.projectName" :maxlength="50"
                                            placeholder="请输入" />
                                    </a-form-item>
                                </a-col>
                                <!-- <a-col :xxl="6" :lg="8" :sm="12" v-if="projectId">
                                    <a-form-item label="项目编号" name="projectNo">
                                        <a-input allowClear v-model:value="formData.projectNo" disabled/>
                                    </a-form-item>
                                </a-col> -->
                                <!-- <a-col :xxl="6" :lg="8" :sm="12" v-if="projectId">
                                    <a-form-item label="状态" name="serviceStatus">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                          v-model:value="formData.serviceStatus"
                                          class="w_full"
                                          disabled
                                           :options="dict.options('XIANG_MU_ZHUANG_TAI')">
                                        </a-select>
                                    </a-form-item>
                                </a-col> -->
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="归属人" name="attributorUserId">
                                        <UserSelect v-model="formData.attributorUserId" :users="formData.attributorUser"
                                            @change="userSelect" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="归属单位" name="companyId">
                                        <DeptSelect v-model="formData.companyId" :noRoot="true" @change="(val, node) => {
            formData.regionId = getSetRegionId(node.deptType, val, node.parentId)
        }" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="所属大区" name="regionId">
                                        <DeptRegionSelect v-model="formData.regionId" :noRoot="true" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="项目优先级" name="projectLevel">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="formData.projectLevel" class="w_full" placeholder="请选择"
                                            :options="dict.options('XIANG_MU_YOU_XIAN_JI')">
                                        </a-select>
                                    </a-form-item>
                                </a-col>

                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="项目类型" name="projectType">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="formData.projectType" class="w_full" :disabled="!!projectId"
                                            placeholder="请选择" @change="(val) => {
            formData.bided = val == 'GU_QUAN_HE_ZUO_XIANG_MU' ? null : val == 'DAN_YI_FEI_TOU_BIAO_XIANG_MU' ? 'FOU' : 'SHI'
            formData.expansionMode = null
            formData.bidingBudget = null
            formData.businessType = null
            formData.targetBusinessType = null
        }" :options="dict.options('XIANG_MU_LEI_XING')">
                                        </a-select>
                                    </a-form-item>
                                </a-col>

                                <template v-if="formData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="关联客户" name="customerId">
                                            <div class="flex_box">
                                                <DataSelect v-model="formData.customerId" modeName="customer"
                                                    class="flex_full" :options="{
            key: 'id',
            label: 'customerName'
        }" :concats="formData.customer" />
                                                <AddBaseData @addSuccess="customerAddOk" />
                                            </div>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="目标公司名称" name="targetCompanyName">
                                            <a-input allowClear v-model:value="formData.targetCompanyName"
                                                placeholder="请输入" />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item label="合作模式" name="cooperationType">
                                            <a-input v-model:value="formData.cooperationTypeOther"
                                                v-if="(formData.cooperationType || '').includes('QI_TA')"
                                                placeholder="请输入">
                                                <template #addonBefore>
                                                    <a-select v-model:value="formData.cooperationType"
                                                        style="width: 90px"
                                                        :getPopupContainer="trigger => trigger.parentNode"
                                                        :dropdownMatchSelectWidth="false"
                                                        :options="dict.options('HE_ZUO_MO_SHI')">
                                                    </a-select>
                                                </template>
                                            </a-input>
                                            <a-select v-else :getPopupContainer="trigger => trigger.parentNode"
                                                v-model:value="formData.cooperationType" placeholder="请选择"
                                                class="w_full" :options="dict.options('HE_ZUO_MO_SHI')">
                                            </a-select>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="目标公司社会统一信用代码" name="targetCompanyNo"
                                            :rules="[{ required: true, validator: companyNoRule }]">
                                            <a-input allowClear v-model:value="formData.targetCompanyNo"
                                                placeholder="请输入" />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="目标公司法定代表人" name="targetLegalRepresentative">
                                            <a-input allowClear v-model:value="formData.targetLegalRepresentative"
                                                placeholder="请输入" />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="目标公司类型" name="targetCompanyType">
                                            <a-select :getPopupContainer="trigger => trigger.parentNode"
                                                v-model:value="formData.targetCompanyType" class="w_full"
                                                placeholder="请选择" :options="dict.options('GONG_SI_LEI_XING')">
                                            </a-select>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="目标公司注册资本(万元)" name="targetRegisteredCapital">
                                            <a-input-number v-model:value="formData.targetRegisteredCapital" :min="0"
                                                class="w_full" placeholder="请输入" />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="目标公司所属业态" name="targetBusinessType">
                                            <!-- <a-select :getPopupContainer="trigger => trigger.parentNode"
                                              v-model:value="formData.targetBusinessType"
                                              class="w_full"
                                              placeholder="请选择"
                                              :options="dict.options('XIANG_MU_YE_TAI')">
                                            </a-select> -->
                                            <YetaiSelect v-model="formData.targetBusinessType" :multiple="false" />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="目标公司成立日期" name="targetIncorporationTime">
                                            <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                                                v-model:value="formData.targetIncorporationTime" class="w_full"
                                                valueFormat="YYYY-MM-DD 00:00:00" format="YYYY-MM-DD"
                                                placeholder="请选择" />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item label="合作人员规模" name="targetPersonnel">
                                            <a-input-number v-model:value="formData.targetPersonnel" :min="0"
                                                class="w_full" placeholder="请输入" />
                                        </a-form-item>
                                    </a-col>
                                </template>
                                <template v-else>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                      <a-form-item required label="业务板块" name="businessSegments">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                                  v-model:value="formData.businessSegments"
                                                  class="w_full"
                                                  placeholder="请选择"
                                                  :options="dict.options('YE_WU_BAN_KUAI')">
                                        </a-select>
                                      </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="拓展模式" name="expansionMode">
                                            <a-select :getPopupContainer="trigger => trigger.parentNode"
                                                v-model:value="formData.expansionMode" class="w_full" placeholder="请选择"
                                                :options="dict.options(formData.projectType)">
                                            </a-select>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="是否招标" name="bided">
                                            <a-select :getPopupContainer="trigger => trigger.parentNode"
                                                v-model:value="formData.bided" class="w_full" placeholder="请选择"
                                                :options="dict.options('SHI_FOU')">
                                            </a-select>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="合作方式" name="cooperationMode">
                                            <a-select :getPopupContainer="trigger => trigger.parentNode"
                                                v-model:value="formData.cooperationMode" class="w_full"
                                                placeholder="请选择" :options="dict.options('HE_ZUO_FANG_SHI')">
                                            </a-select>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="是否为续签项目" name="inStock">
                                            <a-select :getPopupContainer="trigger => trigger.parentNode"
                                                v-model:value="formData.inStock" :disabled="true" class="w_full"
                                                placeholder="请选择" :options="dict.options('SHI_FOU')">
                                            </a-select>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="业态" name="businessType">
                                            <YetaiSelect v-model="formData.businessType" :multiple="false" />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="服务内容" name="serviceContent">
                                            <MultipleDicts v-model="formData.serviceContent" dictKey="FU_WU_NEI_RONG" />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12"
                                        v-if="(formData.serviceContent || '').includes('QI_TA')">
                                        <a-form-item label="其它服务内容" name="serviceContentOther">
                                            <a-input v-model:value="formData.serviceContentOther"
                                                placeholder="请输入其它服务内容" />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="关联客户" name="customerId">
                                            <div class="flex_box">
                                                <DataSelect v-model="formData.customerId" modeName="customer"
                                                    class="flex_full" :options="{
            key: 'id',
            label: 'customerName'
        }" :concats="formData.customer" />
                                                <AddBaseData @addSuccess="customerAddOk" />
                                            </div>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :xxl="6" :lg="8" :sm="12">
                                        <a-form-item required label="建筑面积（m²）" name="constructionArea">
                                            <a-input-number v-model:value="formData.constructionArea" :min="0"
                                                class="w_full"
                                                :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                                :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                                :addon-after="amountUnit(formData.constructionArea)" />
                                        </a-form-item>
                                    </a-col>
                                </template>

                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="省市区" name="areaCode">
                                        <AddressSelect @change="addressChange"
                                            v-model:provinceCode="formData.provinceCode"
                                            v-model:cityCode="formData.cityCode" v-model:areaCode="formData.areaCode"
                                            mode="area" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="所属街道" name="streetCode">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="formData.streetCode" class="w_full"
                                            :placeholder="formData.areaCode ? '请选择街道' : '请先选择省市区'" :options="streetList">
                                        </a-select>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="详细地址" name="address">
                                        <a-input allowClear v-model:value="formData.address" placeholder="请输入" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="预计进场时间" name="approachTime">
                                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="formData.approachTime" class="w_full"
                                            valueFormat="YYYY-MM-DD 00:00:00" format="YYYY-MM-DD" placeholder="请选择" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12" v-if="formData.projectType != 'GU_QUAN_HE_ZUO_XIANG_MU'">
                                    <a-form-item required label="项目预估金额（元)" name="bidingBudget">
                                        <a-input-number v-model:value="formData.bidingBudget" :min="0" class="w_full"
                                            :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                            :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                            :addon-after="amountUnit(formData.bidingBudget)" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="关键词" name="keywords">
                                        <KeyWords v-model="formData.keywords" :type="3" />
                                    </a-form-item>
                                </a-col>
                                <!-- <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="项目来源" name="sourceName">
                                        <a-input disabled v-model:value="formData.sourceName"/>
                                    </a-form-item>
                                </a-col> -->
                            </a-row>
                            <a-form-item label="备注/介绍" name="remark">
                                <a-textarea allowClear :rows="3" type="textarea" v-model:value="formData.remark"
                                    placeholder="请输入(200字以内)" show-count :maxlength="200" />
                            </a-form-item>
                        </a-collapse-panel>

                        <div v-if="formData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU' && !projectId">
                            <a-alert message="基础信息提交保存后再完善“股权”，“成员”，“项目”等信息" closable type="warning" />
                        </div>

                        <template v-if="formData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU' && projectId">
                            <a-collapse-panel key="guquan">
                                <template #header>
                                    <h5 class="title_single">股权信息</h5>
                                </template>
                                <Shareholder :projectId="projectId" />
                            </a-collapse-panel>
                            <a-collapse-panel key="gaoguan">
                                <template #header>
                                    <h5 class="title_single">董监高信息</h5>
                                </template>
                                <Executives :projectId="projectId" />
                            </a-collapse-panel>
                            <a-collapse-panel key="pool">
                                <template #header>
                                    <h5 class="title_single">目标公司项目信息</h5>
                                </template>
                                <Pool :projectId="projectId" />
                            </a-collapse-panel>
                        </template>

                        <a-collapse-panel key="zhaobiao" v-if="formData.projectType == 'DAN_YI_TOU_BIAO_XIANG_MU'">
                            <template #header>
                                <h5 class="title_single">招标信息</h5>
                                <span class="color-danger">*</span>
                            </template>
                            <a-row :gutter="24">
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="招标编号" name="bidingNo">
                                        <a-input allowClear v-model:value="formData.bidingNo" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="招标类型" name="bidingMode">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="formData.bidingMode" class="w_full" placeholder="请选择"
                                            :options="dict.options('ZHAO_BIAO_LEI_XING')">
                                        </a-select>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="发布日期" name="bidingPublishtime">
                                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="formData.bidingPublishtime" class="w_full"
                                            valueFormat="YYYY-MM-DD 00:00:00" format="YYYY-MM-DD" placeholder="请选择" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="招标单位" name="bidingCompany">
                                        <a-input allowClear v-model:value="formData.bidingCompany" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="招标单位联系人" name="bidingCompanyContact">
                                        <a-input allowClear v-model:value="formData.bidingCompanyContact" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="招标单位联系方式" name="bidingCompanyPhone">
                                        <a-input allowClear v-model:value="formData.bidingCompanyPhone" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="招标代理机构" name="bidingAgency">
                                        <a-input allowClear v-model:value="formData.bidingAgency" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="代理机构联系人" name="bidingAgencyContact">
                                        <a-input allowClear v-model:value="formData.bidingAgencyContact" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="代理机构联系方式" name="bidingAgencyPhone">
                                        <a-input allowClear v-model:value="formData.bidingAgencyPhone" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="投标截止时间" name="bidingEndtime">
                                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="formData.bidingEndtime" class="w_full"
                                            valueFormat="YYYY-MM-DD 23:59:59" format="YYYY-MM-DD" placeholder="请选择" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="开标时间" name="bidingOpentime">
                                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="formData.bidingOpentime" class="w_full"
                                            valueFormat="YYYY-MM-DD 00:00:00" format="YYYY-MM-DD" placeholder="请选择" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="招标网址" name="bidingWebsite">
                                        <a-input allowClear v-model:value="formData.bidingWebsite" />
                                    </a-form-item>
                                </a-col>
                            </a-row>
                        </a-collapse-panel>
                    </a-collapse>
                </a-form>
            </div>
        </AScrollbar>
        <FooterBar>
            <a-button size="large" @click="router.back()">取消</a-button>
            <a-button size="large" type="primary" @click="submit" :loading="submitLoading">提交</a-button>
        </FooterBar>
    </div>
</template>
<script setup>
import api from '@/api/index';
import { amountUnit, numFixed } from '@/utils/tools';
import { Modal, message } from 'ant-design-vue';
import { useDictStore } from '@/store/dict';
import Shareholder from './components/correlation/Shareholder.vue';
import Executives from './components/correlation/Executives.vue'
import Pool from './components/correlation/Pool.vue'
import { mainStore } from '@/store';
const store = mainStore();
const dict = useDictStore();
const router = useRouter();
const route = useRoute();
const projectId = ref(Number(route.query.id || 0))
const operateType = ref(Number(route.query.type || 0))
const realtimeOpportunityId = ref(route.query.realtimeOpportunityId || '')
const lockStatus = ref(route.query.lockStatus || '')
const stockOpportunityId = ref(route.query.stockOpportunityId || '')
const title = ref('');
const routes = [
    {
        path: 'expand',
        breadcrumbName: '项目库',
    },
    {
        breadcrumbName: route.query.id ? '项目编辑' : '新增项目',
    },
];
const collapseKey = ref(['base', 'zhaobiao']);
const formData = ref({ roleKeys: [] });
const formRef = ref(null);
const submitLoading = ref(false);
const homePageReload = inject('homePageReload');
const submit = () => {
    console.log('22222', formData.value.regionId)
    formRef.value.validateFields().then(vRes => {
        formData.value.customerId = formData.value.customerId || 0;
        formData.value.operateType = operateType.value;
        // submitLoading.value = true;
        let apiAttr = 'saveRenewal';
        api.project[apiAttr](formData.value).then(res => {
            submitLoading.value = false;
            if (res.code == 200) {
                if (projectId.value) {
                    router.push(`/expand/list`);
                    message.success('操作成功');
                }
            }
        })
    }).catch(err => {
        collapseKey.value = ['base', 'zhaobiao'];
        message.warning('请完善信息！');
    })
}


const getInfo = async () => {
    let res = await api.project.projectInfo(projectId.value);
    if (res.code == 200) {

        title.value = res.data.projectName + '-' + (operateType.value == 1 ? '续签' : '重新投标');
        //res.data.sourceName = title.value;
        res.data.sourceId = projectId.value;
        res.data.inStock = 'SHI';

        if ((res.data.projectType == 'DAN_YI_FEI_TOU_BIAO_XIANG_MU' && operateType.value == 1 ) || (res.data.projectType == 'DAN_YI_TOU_BIAO_XIANG_MU' && operateType.value == 2)) {
            console.log('非投标项目进行续签；投标项目进行重新投标');
        } else {
            res.data.expansionMode = '';
        }

        res.data.projectType = operateType.value == 1 ? 'DAN_YI_FEI_TOU_BIAO_XIANG_MU' : 'DAN_YI_TOU_BIAO_XIANG_MU';
        res.data.relevanceProjectId = projectId.value;

        res.data.bided = operateType.value == 2 ? 'SHI': res.data.bided;

        res.data.projectName = '';
        res.data.projectNo = '';
        res.data.serviceStatus = '';
        res.data.serviceContentOther = '';
        res.data.approachTime = '';
        res.data.bidingBudget = '';
        //res.data.sourceName = '';
        res.data.bidingNo = '';
        res.data.bidingPublishtime = '';
        res.data.remark = '';
        // res.data.bidingCompany = '';
        res.data.bidingCompanyContact = '';
        res.data.bidingCompanyPhone = '';
        res.data.bidingAgency = '';
        res.data.bidingAgencyContact = '';
        res.data.bidingAgencyPhone = '';
        res.data.bidingEndtime = '';
        res.data.bidingOpentime = '';
        res.data.bidingWebsite = '';

        formData.value = res.data;
        if (res.data.areaCode) {
            getStreet(res.data.areaCode);
        }
    }
}
onMounted(() => {
    if (projectId.value) {
        getInfo();
    }
})


const addressChange = (areaCode) => {
    if (areaCode) {
        getStreet(areaCode);
    }
    formData.value.streetCode = null;
    formData.value.streetZipcode = null;
}
const streetList = ref([]);
const getStreet = (areaCode) => {
    api.common.listStreetByArea(areaCode).then(res => {
        if (res.code == 200) {
            streetList.value = res.data.map(item => {
                return {
                    label: item.name + " (" + item.zipCode + ")",
                    value: item.areaCode,
                    zipCode: item.zipCode,
                }
            });
        }
    })
}

const userSelect = async (user) => {
    if (formData.value.companyId) {
        return;
    }
    let res = null;
    let role = {};
    if (user.deptId) {
        let res = await api.sys.deptInfo(user.deptId);
        if (res.code == 200 && res.data) {
            role = res.data || {};
        }
    } else {
        res = await api.sys.userDeptPostList(user.value);
        if (res.data && res.data.length > 0) {
            role = res.data[0];
        }
    }
    /*if(role.deptId){
        if(role.level<3){
            if(hasChildren(role.deptId)){
                formData.value.companyId = role.deptId;
                formData.value.regionId  = role.deptId;
            }else{
                formData.value.companyId = role.parentId;
                formData.value.regionId  = role.parentId;
            }
        }else{
           formData.value.companyId = getCompanyId(role.deptId,role.parentId);
           formData.value.regionId  = getSetRegionId(role.level,role.deptId,role.parentId);
        }
    }*/
    if (role.deptId) {
        if (role.deptType == 'CENG_JI') {
            formData.value.companyId = role.deptId;
        } else {
            formData.value.companyId = role.parentId;
        }
        let obj = await api.sys.regionInfo(role.deptId);
        if (obj.code == 200 && obj.data) {
            formData.value.regionId = obj.data.deptId;
        }
    }

}
const hasChildren = (deptId) => {
    let hasChild = false;
    function doArr(arr, id) {
        for (var i = 0; i < arr.length; i++) {
            let item = arr[i];
            if (item.deptId == deptId) {
                hasChild = item.children && item.children.length > 0;
                break;
            } else {
                if (item.children && item.children.length > 0) {
                    doArr(item.children);
                }
            }
        }
    }
    doArr(store.deptTree);
    return hasChild
}
const getCompanyId = (deptId, parentId) => {
    console.log(hasChildren(deptId))
    return hasChildren(deptId) ? deptId : parentId;
}
const getSetRegionId = async (deptType, deptId, parentId) => {
    if (deptType == 'CENG_JI') {
        formData.value.companyId = deptId;
    } else {
        formData.value.companyId = parentId;
    }
    let obj = await api.sys.regionInfo(deptId);
    if (obj.code == 200 && obj.data) {
        formData.value.regionId = obj.data.deptId;
    }
    /* if(level<3){
         if(hasChildren(deptId)){
             return deptId;
         }else{
             return parentId;
         }
     }else if(level==3){
         return parentId;
     }else{
         let regionId = null;
         function doArr(arr,id){
             for (var i = 0; i < arr.length; i++) {
                 let item = arr[i];
                 if(item.deptId==parentId){
                     regionId = id;
                     console.log('oooo',id,store.deptTree[0].children)
                     break;
                 }else{
                     if(item.children&&item.children.length>0){
                         doArr(item.children,id || item.deptId);
                     }
                 }
             }
         }
         doArr(store.deptTree[0].children);
         return regionId;
     }*/
}

const customerAddOk = (res) => {
    if (!formData.value.customerId) {
        formData.value.customerId = res.id;
        formData.value.customer = {
            id: res.id,
            name: res.customerName
        }
    } else {
        //当为新增项目时
        //当存在商机ID参数
        if ('' != realtimeOpportunityId.value && "" != formData.value.bidingAgency) {
            getQlmisUnLockCompany()
        }
        //存在存量商机
        if ('' != stockOpportunityId.value && "" != formData.value.bidingAgency) {
            getQlmisUnLockCompany()
        }

    }
}

const companyNoRule = (rule, value, callback) => {
    const regex = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;
    if (rule.required && !regex.test(value)) {
        return new Promise((resolve, reject) => {
            reject('请输入有效社会信用代码(18位)');
        });
    }
    return Promise.resolve();
};
</script>
<style scoped lang="less"></style>
