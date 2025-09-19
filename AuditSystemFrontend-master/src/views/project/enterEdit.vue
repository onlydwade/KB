<template>
  <div class="content-inner">
    <a-page-header :ghost="false" :breadcrumb="{ routes }">
      <template #title>
        <EllipsisTooltip
          style="width: 800px"
          class="flex_full"
          :content="projectId ? formData.projectName : '新增项目补录'"
        />
      </template>
    </a-page-header>
    <AScrollbar style="margin: -16px -16px 0 -16px">
      <div class="padding_box">
        <a-form layout="vertical" :model="formData" ref="formRef">
          <a-collapse
            ghost
            expandIconPosition="right"
            v-model:activeKey="collapseKey"
            class="crl-collapse"
          >
            <a-collapse-panel key="base">
              <template #header>
                <h5 class="title_single">基础信息</h5>
                <span class="color-danger">*</span>
              </template>
              <a-row :gutter="24">
<!--                <a-col :xxl="6" :lg="8" :sm="12">-->
<!--                  <a-form-item required label="公司标识" name="corporateIdentity">-->
<!--                    <a-select :getPopupContainer="trigger => trigger.parentNode"-->
<!--                              v-model:value="formData.corporateIdentity"-->
<!--                              class="w_full"-->
<!--                              placeholder="请选择"-->
<!--                              :options="dict.options('GONG_SI_BIAO_SHI')">-->
<!--                    </a-select>-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="项目名称" name="projectName">
                    <a-input
                      allowClear
                      v-model:value="formData.projectName"
                      :maxlength="50"
                      placeholder="请输入"
                      :disabled="isView"
                    />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12" v-if="projectId">
                  <a-form-item label="项目编号" name="projectNo">
                    <a-input allowClear v-model:value="formData.projectNo" disabled />
                  </a-form-item>
                </a-col>
<!--                <a-col :xxl="6" :lg="8" :sm="12" v-if="projectId">-->
<!--                  <a-form-item label="状态" name="serviceStatus">-->
<!--                    <a-input allowClear v-model:value="formData.backfillStatusStr" disabled />-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="归属人" name="attributorUserId">
                    <UserSelect
                      v-model="formData.attributorUserId"
                      :users="formData.attributorUser"
                      @change="userSelect"
                      :disabled="isView"
                    />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="归属单位" name="companyId">
                    <DeptSelect
                      v-model="formData.companyId"
                      :noRoot="true"
                      :disabled="isView"
                      @change="
                        (val, node) => {
                          formData.regionId = getSetRegionId(node.deptType, val, node.parentId);
                        }
                      "
                    />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="所属大区" name="regionId">
                    <DeptRegionSelect v-model="formData.regionId" :noRoot="true"  :disabled="isView" />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="项目优先级" name="projectLevel">
                    <a-select
                      :getPopupContainer="trigger => trigger.parentNode"
                      v-model:value="formData.projectLevel"
                      :disabled="formData.isXmps || isView"
                      class="w_full"
                      placeholder="请选择"
                      :options="dict.options('XIANG_MU_YOU_XIAN_JI')"
                    ></a-select>
                  </a-form-item>
                </a-col>

                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="项目类型" name="projectType">
                    <a-select
                      :getPopupContainer="trigger => trigger.parentNode"
                      v-model:value="formData.projectType"
                      class="w_full"
                      :disabled="isView"
                      placeholder="请选择"
                      @change="
                        val => {
                          formData.bided =
                            val == 'GU_QUAN_HE_ZUO_XIANG_MU'
                              ? null
                              : val == 'DAN_YI_FEI_TOU_BIAO_XIANG_MU'
                              ? 'FOU'
                              : 'SHI';
                          formData.expansionMode = null;
                          formData.bidingBudget = null;
                          formData.businessType = null;
                          formData.targetBusinessType = null;
                        }
                      "
                      :options="projectTypeOptions"
                    ></a-select>
                  </a-form-item>
                </a-col>

                <template v-if="formData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="关联客户" name="customerId">
                      <div class="flex_box">
                        <DataSelect
                          v-model="formData.customerId"
                          modeName="customer"
                          class="flex_full"
                          :options="{
                            key: 'id',
                            label: 'customerName',
                          }"
                          :concats="formData.customer"
                           :disabled="isView"
                        />
                        <AddBaseData @addSuccess="customerAddOk"  :disabled="isView"/>
                      </div>
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="目标公司名称" name="targetCompanyName">
                      <a-input
                        allowClear
                        v-model:value="formData.targetCompanyName"
                        placeholder="请输入"
                        :disabled="isView"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item label="合作模式" name="cooperationType">
                      <a-input
                        v-model:value="formData.cooperationTypeOther"
                        v-if="(formData.cooperationType || '').includes('QI_TA')"
                        placeholder="请输入"
                        :disabled="isView"
                      >
                        <template #addonBefore>
                          <a-select
                            v-model:value="formData.cooperationType"
                            style="width: 90px"
                            :getPopupContainer="trigger => trigger.parentNode"
                            :dropdownMatchSelectWidth="false"
                            :options="dict.options('HE_ZUO_MO_SHI')"
                            :disabled="isView"
                          ></a-select>
                        </template>
                      </a-input>
                      <a-select
                        v-else
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.cooperationType"
                        placeholder="请选择"
                        class="w_full"
                        :options="dict.options('HE_ZUO_MO_SHI')"
                        :disabled="isView"
                      ></a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item
                      required
                      label="目标公司社会统一信用代码"
                      name="targetCompanyNo"
                      :rules="[{ required: true, validator: companyNoRule }]"
                    >
                      <a-input
                        allowClear
                        v-model:value="formData.targetCompanyNo"
                        placeholder="请输入"
                        :disabled="isView"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item
                      required
                      label="目标公司法定代表人"
                      name="targetLegalRepresentative"
                    >
                      <a-input
                        allowClear
                        v-model:value="formData.targetLegalRepresentative"
                        placeholder="请输入"
                        :disabled="isView"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="目标公司类型" name="targetCompanyType">
                      <a-select
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.targetCompanyType"
                        class="w_full"
                        placeholder="请选择"
                        :options="dict.options('GONG_SI_LEI_XING')"
                        :disabled="isView"
                      ></a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item
                      required
                      label="目标公司注册资本(万元)"
                      name="targetRegisteredCapital"
                    >
                      <a-input-number
                        v-model:value="formData.targetRegisteredCapital"
                        :min="0"
                        class="w_full"
                        placeholder="请输入"
                        :disabled="isView"
                      />
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
                      <YetaiSelect v-model="formData.targetBusinessType" :multiple="false" :disabled="isView" />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="目标公司成立日期" name="targetIncorporationTime">
                      <a-date-picker
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.targetIncorporationTime"
                        class="w_full"
                        valueFormat="YYYY-MM-DD 00:00:00"
                        format="YYYY-MM-DD"
                        placeholder="请选择"
                        :disabled="isView"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item label="合作人员规模" name="targetPersonnel">
                      <a-input-number
                        v-model:value="formData.targetPersonnel"
                        :min="0"
                        class="w_full"
                        placeholder="请输入"
                        :disabled="isView"
                      />
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
                      <a-select
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.expansionMode"
                        class="w_full"
                        placeholder="请选择"
                        :options="dict.options(formData.projectType)"
                        :disabled="isView"
                      ></a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="是否招标" name="bided">
                      <a-select
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.bided"
                        class="w_full"
                        :disabled="!!formData.bided || isView"
                        placeholder="请选择"
                        :options="dict.options('SHI_FOU')"
                      ></a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="合作方式" name="cooperationMode">
                      <a-select
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.cooperationMode"
                        class="w_full"
                        placeholder="请选择"
                        :options="dict.options('HE_ZUO_FANG_SHI')"
                        :disabled="isView"
                      ></a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="是否为续签项目" name="inStock">
                      <a-select
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.inStock"
                        class="w_full"
                        :disabled="formData.renewBid || isView"
                        placeholder="请选择"
                        :options="dict.options('SHI_FOU')"
                      ></a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="业态" name="businessType">
                      <YetaiSelect v-model="formData.businessType" :multiple="false" :disabled="isView"/>
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="服务内容" name="serviceContent">
                      <MultipleDicts v-model="formData.serviceContent" dictKey="FU_WU_NEI_RONG" :disabled="isView" />
                    </a-form-item>
                  </a-col>
                  <a-col
                    :xxl="6"
                    :lg="8"
                    :sm="12"
                    v-if="(formData.serviceContent || '').includes('QI_TA')"
                  >
                    <a-form-item label="其它服务内容" name="serviceContentOther">
                      <a-input
                        v-model:value="formData.serviceContentOther"
                        placeholder="请输入其它服务内容"
                        :disabled="isView"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="关联客户" name="customerId">
                      <div class="flex_box">
                        <DataSelect
                          v-model="formData.customerId"
                          modeName="customer"
                          class="flex_full"
                          :options="{
                            key: 'id',
                            label: 'customerName',
                          }"
                          :concats="formData.customer"
                          :disabled="isView"
                        />
                        <AddBaseData @addSuccess="customerAddOk" :disabled="isView" />
                      </div>
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="建筑面积（m²）" name="constructionArea">
                      <a-input-number
                        v-model:value="formData.constructionArea"
                        :min="0"
                        class="w_full"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"
                        :addon-after="amountUnit(formData.constructionArea)"
                        :disabled="isView"
                      />
                    </a-form-item>
                  </a-col>
                </template>

                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="省市区" name="areaCode">
                    <AddressSelect
                      @change="addressChange"
                      v-model:provinceCode="formData.provinceCode"
                      v-model:cityCode="formData.cityCode"
                      v-model:areaCode="formData.areaCode"
                      mode="area"
                      :disabled="isView"
                    />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="所属街道" name="streetCode">
                    <a-select
                      :getPopupContainer="trigger => trigger.parentNode"
                      v-model:value="formData.streetCode"
                      class="w_full"
                      :placeholder="formData.areaCode ? '请选择街道' : '请先选择省市区'"
                      :options="streetList"
                      :disabled="isView"
                    ></a-select>
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item label="详细地址" name="address">
                    <a-input allowClear v-model:value="formData.address" placeholder="请输入" :disabled="isView"/>
                  </a-form-item>
                </a-col>
<!--                <a-col :xxl="6" :lg="8" :sm="12">-->
<!--                  <a-form-item label="预计进场时间" name="approachTime">-->
<!--                    <a-date-picker-->
<!--                      :getPopupContainer="trigger => trigger.parentNode"-->
<!--                      v-model:value="formData.approachTime"-->
<!--                      class="w_full"-->
<!--                      valueFormat="YYYY-MM-DD 00:00:00"-->
<!--                      format="YYYY-MM-DD"-->
<!--                      placeholder="请选择"-->
<!--                      :disabled="isView"-->
<!--                    />-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col-->
<!--                  :xxl="6"-->
<!--                  :lg="8"-->
<!--                  :sm="12"-->
<!--                  v-if="formData.projectType != 'GU_QUAN_HE_ZUO_XIANG_MU'"-->
<!--                >-->
<!--                  <a-form-item required label="项目预估金额（元)" name="bidingBudget">-->
<!--                    <a-input-number-->
<!--                      v-model:value="formData.bidingBudget"-->
<!--                      :min="0"-->
<!--                      class="w_full"-->
<!--                      :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"-->
<!--                      :parser="value => value.replace(/\\s?|(,*)/g, '')"-->
<!--                      :addon-after="amountUnit(formData.bidingBudget)"-->
<!--                      :disabled="isView"-->
<!--                    />-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item label="关键词" name="keywords">
                    <KeyWords v-model="formData.keywords" :type="3" :disabled="isView" />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item label="项目来源" name="sourceName">
                    <a-input disabled v-model:value="formData.sourceName" :disabled="isView" />
                  </a-form-item>
                </a-col>
              </a-row>
              <a-form-item label="备注/介绍" name="remark">
                <a-textarea
                  allowClear
                  :rows="3"
                  type="textarea"
                  v-model:value="formData.remark"
                  placeholder="请输入(200字以内)"
                  show-count
                  :maxlength="200"
                  :disabled="isView"
                />
              </a-form-item>
            </a-collapse-panel>
            <a-collapse-panel
              key="zhaobiao"
              v-if="formData.projectType == 'DAN_YI_TOU_BIAO_XIANG_MU'"
            >
              <template #header>
                <h5 class="title_single">招标信息</h5>
                <span class="color-danger">*</span>
              </template>
              <a-row :gutter="24">
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="招标编号" name="bidingNo">
                    <a-input allowClear v-model:value="formData.bidingNo" :disabled="isView" />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="招标类型" name="bidingMode">
                    <a-select
                      :getPopupContainer="trigger => trigger.parentNode"
                      v-model:value="formData.bidingMode"
                      class="w_full"
                      placeholder="请选择"
                      :options="dict.options('ZHAO_BIAO_LEI_XING')"
                      :disabled="isView"
                    ></a-select>
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="发布日期" name="bidingPublishtime">
                    <a-date-picker
                      :getPopupContainer="trigger => trigger.parentNode"
                      v-model:value="formData.bidingPublishtime"
                      class="w_full"
                      valueFormat="YYYY-MM-DD 00:00:00"
                      format="YYYY-MM-DD"
                      placeholder="请选择"
                      :disabled="isView"
                    />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="招标单位" name="bidingCompany">
                    <a-input allowClear v-model:value="formData.bidingCompany" :disabled="isView" />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item label="招标单位联系人" name="bidingCompanyContact">
                    <a-input allowClear v-model:value="formData.bidingCompanyContact" :disabled="isView" />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item label="招标单位联系方式" name="bidingCompanyPhone">
                    <a-input allowClear v-model:value="formData.bidingCompanyPhone" :disabled="isView" />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item label="招标代理机构" name="bidingAgency">
                    <a-input allowClear v-model:value="formData.bidingAgency" :disabled="isView" />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item label="代理机构联系人" name="bidingAgencyContact">
                    <a-input allowClear v-model:value="formData.bidingAgencyContact" :disabled="isView" />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item label="代理机构联系方式" name="bidingAgencyPhone">
                    <a-input allowClear v-model:value="formData.bidingAgencyPhone" :disabled="isView" />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="投标截止时间" name="bidingEndtime">
                    <a-date-picker
                      :getPopupContainer="trigger => trigger.parentNode"
                      v-model:value="formData.bidingEndtime"
                      class="w_full"
                      valueFormat="YYYY-MM-DD 23:59:59"
                      format="YYYY-MM-DD"
                      placeholder="请选择"
                      :disabled="isView"
                    />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item required label="开标时间" name="bidingOpentime">
                    <a-date-picker
                      :getPopupContainer="trigger => trigger.parentNode"
                      v-model:value="formData.bidingOpentime"
                      class="w_full"
                      valueFormat="YYYY-MM-DD 00:00:00"
                      format="YYYY-MM-DD"
                      placeholder="请选择"
                      :disabled="isView"
                    />
                  </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12">
                  <a-form-item label="招标网址" name="bidingWebsite">
                    <a-input allowClear v-model:value="formData.bidingWebsite" :disabled="isView"/>
                  </a-form-item>
                </a-col>
              </a-row>
            </a-collapse-panel>
            <a-collapse-panel key="ht">
              <template #header>
                <h5 class="title_single">合同信息录入</h5>
                <span class="color-danger">*</span>
              </template>
              <div class="padding_box">
                <a-row :gutter="24">
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="甲方单位名称" name="firstResponsibleCompany">
                      <a-input
                        :disabled="isView"
                        v-model:value="formData.firstResponsibleCompany"
                        placeholder="请输入"
                        
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="合同总金额（元）" name="contractAmount">
                      <a-input-number
                        :disabled="isView"
                        v-model:value="formData.contractAmount"
                        :min="0"
                        class="w_full"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"
                        :addon-after="amountUnit(formData.contractAmount)"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="合同年度金额（元）" name="contractAnnualAmount">
                      <a-input-number
                        :disabled="isView"
                        v-model:value="formData.contractAnnualAmount"
                        :min="0"
                        class="w_full"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"
                        :addon-after="amountUnit(formData.contractAnnualAmount)"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item label="当年转化金额 (元)">
                      <a-input-number
                        :value="formData.annualConversionAmount"
                        :min="0"
                        class="w_full"
                        disabled
                        placeholder="自动计算"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"
                        :addon-after="amountUnit(formData.annualConversionAmount)"
                      />
                    </a-form-item>
                  </a-col>

                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="乙方单位名称" name="secondParty">
                      <a-input
                        :disabled="isView"
                        v-model:value="formData.secondParty"
                        placeholder="请输入"
                      />
                    </a-form-item>
                  </a-col>
                  <!-- <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="测算利润率(%)" name="calculateProfitRate">
                                        <a-input-number :disabled="disabled" v-model:value="formData.calculateProfitRate" :min="0"
                                            class="w_full" />
                                    </a-form-item>
                                </a-col> -->
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="签约日期" name="signTime">
                      <a-date-picker
                        :disabled="isView"
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.signTime"
                        class="w_full"
                        valueFormat="YYYY-MM-DD 00:00:00"
                        format="YYYY-MM-DD"
                        placeholder="请选择"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="服务开始日期" name="serviceBeginTime">
                      <a-date-picker
                        :disabled="isView"
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.serviceBeginTime"
                        class="w_full"
                        valueFormat="YYYY-MM-DD 00:00:00"
                        format="YYYY-MM-DD"
                        placeholder="请选择"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="合同到期日期" name="serviceEndTime">
                      <a-date-picker
                       :disabled="isView"
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.serviceEndTime"
                        class="w_full"
                        valueFormat="YYYY-MM-DD 23:59:59"
                        format="YYYY-MM-DD"
                        placeholder="请选择"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="拟服务期限（月）" name="proposedServicePeriod">
                      <a-input-number
                        :disabled="isView"
                        v-model:value="formData.proposedServicePeriod"
                        :min="0"
                        class="w_full"
                      />
                    </a-form-item>
                  </a-col>
                  <!-- <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="服务内容" name="serviceContent">
                      <MultipleDicts
                        :disabled="isView"
                        v-model="formData.serviceContent"
                        dictKey="FU_WU_NEI_RONG"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col
                    :xxl="6"
                    :lg="8"
                    :sm="12"
                    v-if="(formData.serviceContent || '').includes('QI_TA')"
                  >
                    <a-form-item label="其它服务内容" name="serviceContentOther">
                      <a-input
                        :disabled="isView"
                        v-model:value="formData.serviceContentOther"
                        placeholder="请输入其它服务内容"
                      />
                    </a-form-item>
                  </a-col>

                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="建筑面积（㎡）" name="constructionArea">
                      <a-input-number
                        ::disabled="isView"
                        v-model:value="formData.constructionArea"
                        :min="0"
                        class="w_full"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item label="拓展模式" name="expansionModeStr">
                      <a-input disabled v-model:value="formData.expansionModeStr" :disabled="isView" />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item label="业态" name="businessTypeStr">
                      <a-input disabled v-model:value="formData.businessTypeStr" :disabled="isView" />
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item label="是否为续签项目">
                      <a-select
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.inStock"
                        :disabled="formData.renewBid"
                        class="w_full"
                        placeholder="请选择"
                        :options="dict.options('SHI_FOU')"
                        :disabled="isView"
                      ></a-select>
                    </a-form-item>
                  </a-col> -->
                  <a-col v-if="formData.inStock === 'SHI'" :xxl="6" :lg="8" :sm="12">
                    <a-form-item label="是否有增量业绩确认">
                      <a-select
                        :getPopupContainer="trigger => trigger.parentNode"
                        :disabled="isView"
                        v-model:value="formData.isPerformanceIncrement"
                        class="w_full"
                        placeholder="请选择"
                        :options="dict.options('SHI_FOU')"
                      ></a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="是否计算业绩" name="isCountPerformance">
                      <a-select
                        :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.isCountPerformance"
                        :disabled="formData.renewBid || isView"
                        class="w_full"
                        placeholder="请选择"
                        :options="dict.options('SHI_FOU')"
                      ></a-select>
                    </a-form-item>
                  </a-col>
                </a-row>

                <template
                  v-if="formData.inStock === 'SHI' && formData.isPerformanceIncrement === 'SHI'"
                >
                  <Title style="margin-top: 32px">
                    <template #title>增量业绩确认信息</template>
                    <!-- <a-form-item name="correlation" :rules="{ required: true, message: '( 请完善业绩分配后提交!!! )', }">
                                </a-form-item> -->
                  </Title>
                  <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                      <a-form-item required label="合同总金额(元) " name="contractAmounts">
                        <a-input-number
                          :disabled="isView"
                          v-model:value="formData.contractAmounts"
                          :min="0"
                          placeholder="请输入"
                          class="w_full"
                          :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                          :parser="value => value.replace(/\\s?|(,*)/g, '')"
                          :addon-after="amountUnit(formData.contractAmounts)"
                        />
                      </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                      <a-form-item required label="合同年度金额(元) " name="contractAnnualAmounts">
                        <a-input-number
                         :disabled="isView"
                          v-model:value="formData.contractAnnualAmounts"
                          placeholder="请输入"
                          :min="0"
                          class="w_full"
                          :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                          :parser="value => value.replace(/\\s?|(,*)/g, '')"
                          :addon-after="amountUnit(formData.contractAnnualAmounts)"
                        />
                      </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                      <a-form-item
                        required
                        label="当年转化金额(元) "
                        name="annualConversionAmounts"
                      >
                        <a-input-number
                          v-model:value="formData.annualConversionAmounts"
                          :min="0"
                          class="w_full"
                          disabled
                          placeholder="自动计算"
                          :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                          :parser="value => value.replace(/\\s?|(,*)/g, '')"
                          :addon-after="amountUnit(formData.annualConversionAmounts)"
                        />
                      </a-form-item>
                    </a-col>
                  </a-row>
                </template>
              </div>
            </a-collapse-panel>
            <a-collapse-panel key="yj">
              <template #header>
                <h5 class="title_single">
                  业绩分配
                  <span class="color-danger">*</span>
                </h5>
              </template>
              <div class="form-message-tip" style="margin-top: -32px">
                <a-form-item
                  name="correlation"
                  :rules="{ required: true, message: '( 请完善业绩分配后提交!!! )' }"
                ></a-form-item>
              </div>

              <EnterAchievement
                :projectId="projectId"
                v-model="formData.correlation"
                v-model:dataList="formData.projectBacklogAchievement"
                :readOnly="isView"
              />
            </a-collapse-panel>
            <a-collapse-panel key="fj">
              <template #header>
                <h5 class="title_single">
                  附件上传
                  <span class="color-danger">*</span>
                </h5>
              </template>
              <div class="form-message-tip" style="margin-top: -32px">
                <a-form-item
                  name="fileOk"
                  class="form-message-tip"
                  :rules="{ required: true, message: '( 请上传必传文件后提交!!! )' }"
                ></a-form-item>
              </div>
              <ProjectEnterDocumentsList
                v-model="formData.fileOk"
                v-model:FileList="formData.projectBacklogFiles"
                ref="documentsRef"
                :readOnly="isView"
              />
            </a-collapse-panel>
            <div v-if="formData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU' && !projectId">
              <a-alert
                message="基础信息提交保存后再完善“股权”，“成员”，“项目”等信息"
                closable
                type="warning"
              />
            </div>

            <template v-if="formData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU' && projectId">
              <a-collapse-panel key="guquan">
                <template #header>
                  <h5 class="title_single">股权信息</h5>
                </template>
                <Shareholder :projectId="projectId"  :disabled="isView"/>
              </a-collapse-panel>
              <a-collapse-panel key="gaoguan">
                <template #header>
                  <h5 class="title_single">董监高信息</h5>
                </template>
                <Executives :projectId="projectId" :disabled="isView"/>
              </a-collapse-panel>
              <a-collapse-panel key="pool">
                <template #header>
                  <h5 class="title_single">目标公司项目信息</h5>
                </template>
                <Pool :projectId="projectId" :disabled="isView" />
              </a-collapse-panel>
            </template>
          </a-collapse>
        </a-form>
      </div>
    </AScrollbar>
    <!-- <FooterBar>
      <a-button size="large" @click="router.back()">取消</a-button>
      <a-button size="large" type="primary" @click="submit" :loading="submitLoading">提交</a-button>
    </FooterBar> -->
    <ProjectEnterActions
      @submit="submit"
      @staging="staging"
      :menuInfo="enterInfo"
      ref="actionRef"
    ></ProjectEnterActions>
  </div>
</template>
<script setup>
import api from "@/api/index";
import { amountUnit, numFixed, throttle ,dateFormat } from "@/utils/tools";
import { Modal, message } from "ant-design-vue";
import { useDictStore } from "@/store/dict";
import Shareholder from "./components/correlation/Shareholder.vue";
import Executives from "./components/correlation/Executives.vue";
import Pool from "./components/correlation/Pool.vue";
import EnterAchievement from "./components/correlation/EnterAchievement.vue";
import YjqrspModal from "./components/correlation/YjqrspModal.vue";
import { mainStore } from "@/store";
const store = mainStore();
const dict = useDictStore();
const router = useRouter();
const route = useRoute();
const projectId = ref(Number(route.query.id || 0));
const realtimeOpportunityId = ref(route.query.realtimeOpportunityId || "");
const lockStatus = ref(route.query.lockStatus || "");
const stockOpportunityId = ref(route.query.stockOpportunityId || "");
const bus = inject("bus");
const routes = [
  {
    path: "expand",
    breadcrumbName: "在管项目库",
  },
  {
    breadcrumbName: "项目补录",
  },
];
const collapseKey = ref(["base", "ht", "yj", "fj", "zhaobiao"]);
const formData = ref(
    {
      roleKeys: [] ,
      isCountPerformance: null
    }
);
const formRef = ref(null);
const submitLoading = ref(false);
const homePageReload = inject("homePageReload");
const disabled = ref(false);
const projectTypeOptions = computed(() => {
  let arr = dict.options('XIANG_MU_LEI_XING');
  console.log("arr", arr);
  
  arr = arr.filter(item => {
    return item.value !== 'GU_QUAN_HE_ZUO_XIANG_MU1' && item.value !== 'GU_QUAN_HE_ZUO_XIANG_MU';
  });
  return arr;
});

const enterInfo = {
  type: Object,
  default: {},
};
const isView = ref(Number(route.query.isView || ""));
const getCheckOaBacklog = () => {
  api.project.getCheckOaBacklog().then(res => {
    if (res.code == 200) {
      enterInfo.checkoa = res.data;
    }
  });
};
const submit = (type, temp) => {
  console.log("22222", type, temp);
  formRef.value
    .validateFields()
    .then(vRes => {
      formData.value.customerId = formData.value.customerId || 0;
      // submitLoading.value = true;
      if (formData.value.projectType == "GU_QUAN_HE_ZUO_XIANG_MU") {
        formData.value.businessType = formData.value.targetBusinessType;
      }
      // formData.value.businessType = formData.value.businessType[1]
      // formData.value.targetBusinessType = formData.value.targetBusinessType[1]
      api.project.projectBacklogSubmit(formData.value).then(res => {
        submitLoading.value = false;
        if (res.code == 200) {
          message.success('操作成功');
          //router.back();
          //oaStart(type, temp);
          enterInfo.id = res.data.id;
          projectId.value = res.data.id;
          if (type == 0 || type == -1) {
            oaStart(type, temp);
          } else {
            router.push({ path: '/expand/extension2', query: { tabIndex: 1 } });
          }
          //debugger;
          if (route.query == undefined || route.query.id == undefined) {
            router.push({ path: '/expand/extension2', query: { tabIndex: 1 } });
          }
        }
      });
    })
    .catch(err => {
      collapseKey.value = ["base", "ht", "yj", "fj", "zhaobiao"];
      message.warning("请完善信息！");
    });
};
const oaStart = (type, temp) => {
  let postData = {
    subject: formData.value.projectName + "-项目补录",
    recordId: projectId.value,
    subRecordId: null,
    templateId: enterInfo.templateId,
    moduleName: "ProjectBacklog",
    approvalStatus: 5,
    stepCode: null,
    mainProcess: 1,
    detailUrl: `${window.location.origin}/#/checkBacklog?id=${projectId.value}&isView=1`,
    //detailUrl: `${window.location.origin}/#/mobile/workReport?id=${briefId.value}&read=1`,
    //https://tt-sit.gem-flower.com/#/mobile/workReport?id=%s&read=1
  };
  let apiKey = "oaAdd";
  if (type == -1) {
    apiKey = "oaUpdate";
    postData.id = temp.oaId;
  } else {
    postData.submitTime = dateFormat(new Date());
    postData.createTime = dateFormat(new Date());
    postData.submitUserId = temp.userId;
  }
  api.common[apiKey](postData).then(async res => {
    if (res.code == 200) {
      message.success("操作成功！");
      if (type != 2) {
        bus.emit("workgetlist");
      }
      if (temp.mainProcess && menuInfo.value.status == 0) {
        await api.project.stepStatusUpdate({
          projectId: projectId,
          stepMenuId: menuInfo.value.id,
          approvalStatus: 5,
        });
      }
    }
  });
};
const staging = () => {
  formData.value.customerId = formData.value.customerId || 0;
  // submitLoading.value = true;
  let apiAttr =  "projectBacklogAdd";
  if (formData.value.projectType == "GU_QUAN_HE_ZUO_XIANG_MU") {
    formData.value.businessType = formData.value.targetBusinessType;
  }
  // formData.value.businessType = formData.value.businessType[1]
  // formData.value.targetBusinessType = formData.value.targetBusinessType[1]
  api.project[apiAttr](formData.value).then(res => {
    submitLoading.value = false;
    if (res.code == 200) {
      message.success("操作成功");
      router.push({ path: '/expand/extension2', query: { tabIndex: 1 } });
    } 
  });
};
const getQlmRealTimeDetail = () => {
  api.customer.getQlmRealTimeDetail(realtimeOpportunityId.value, lockStatus.value).then(res => {
    if (res.code == 200) {
      let resp = res.data[0];
      formData.value.projectName = resp.extractProjName;
      formData.value.bidingCompany = resp.tenderUnit;
      formData.value.bidingCompanyContact = resp.tenderRelationName;
      formData.value.projectType = "DAN_YI_TOU_BIAO_XIANG_MU";
      formData.value.bidingCompanyPhone = resp.tenderRelationWay;
      formData.value.bidingAgency = resp.agentUnit;
      formData.value.bidingAgencyContact = resp.agentRelationName;
      formData.value.bidingAgencyPhone = resp.agentRelationWay;
      formData.value.contractAmount = resp.biddingAmountNum;
      formData.value.bidingBudget = resp.biddingBudgetNum;
      formData.value.bidingNo = resp.xmNumber;
      debugger;
      if (resp.bidsDeadline != null && resp.bidsDeadline != "") {
        formData.value.bidingEndtime = resp.bidsDeadline + " 23:59:59";
      }
      if (resp.updateTime != null && resp.updateTime != "") {
        formData.value.bidingPublishtime = resp.updateTime + " 00:00:00";
      }

      getBusinessTypeByName(resp.propertyTypeList);
      getBidingModeByName(resp.biddingTypeStr);
      getServiceContentOther(resp.industryTagCodeList);
      //实时商机列表和明细没有建筑面积，存量商机只有服务面积
      // formData.value.constructionArea=res.data.serviceArea;
      formData.value.sourceName = "赢标宝";

      saveQlmCompanyCustomer();
    }
  });
};

const getServiceContentOther = industryTagCodeStr => {
  formData.value.serviceContent = "QI_TA";
  if (industryTagCodeStr == null) {
    return;
  }

  let industryTagCodeList = JSON.parse(industryTagCodeStr);
  formData.value.serviceContentOther = industryTagCodeList.join(",");
};

const getBidingModeByName = item => {
  switch (item) {
    case "公开招标":
      formData.value.bidingMode = "GONG_KAI_ZHAO_BIAO";
      break;
    case "竞争性谈判或竞争性磋商":
      formData.value.bidingMode = "JING_ZHENG_XING_TAN_PAN";
      break;
    case "邀请招标":
      formData.value.bidingMode = "YAO_QING_ZHAO_BIAO";
      break;
    case "单一来源":
      formData.value.bidingMode = "DAN_YI_LAI_YUAN";
      break;
    case "询价":
      formData.value.bidingMode = "XUN_JIA";
      break;
  }
};

const getBusinessTypeByName = propertyTypeStr => {
  if (propertyTypeStr == null) {
    return;
  }
  let propertyTypeList = JSON.parse(propertyTypeStr);
  for (var j = 0; j < propertyTypeList.length; j++) {
    let item = propertyTypeList[j];
    switch (item) {
      case "城市服务":
        formData.value.businessType = "SHI _ZHENG _HUAN _WEI";
        return;
      case "医院":
        formData.value.businessType = "YI _YUAN";
        return;
      case "学校":
        formData.value.businessType = "XUE _XIAO";
        return;
      case "政府办公楼":
        formData.value.businessType = "BAN _GONG";
        return;
      case "商务办公楼":
        formData.value.businessType = "SHANG _XIE";
        return;
      case "旅游景区":
        formData.value.businessType = "JING _QU _GUAN _LI";
        return;
      case "场馆":
        formData.value.businessType = "CHANG _GUAN1";
        return;
      case "产业园区":
        formData.value.businessType = "CHAN _YE _YUAN _QU";
        return;
      case "酒店":
        formData.value.businessType = "JIU_DIAN_WU_YE";
        return;
      case "住宅":
        formData.value.businessType = "ZHU _ZHAI";
        return;
    }
  }
};

const getQlmStockDetail = () => {
  api.customer.getQlmStockDetail(stockOpportunityId.value, lockStatus.value).then(res => {
    if (res.code == 200) {
      let resp = res.data[0];
      formData.value.projectName = resp.extractProjName;
      formData.value.bidingCompany = resp.tenderUnit;
      formData.value.bidingCompanyContact = resp.tenderRelationName;
      formData.value.projectType = "DAN_YI_TOU_BIAO_XIANG_MU";
      formData.value.bidingCompanyPhone = resp.tenderRelationWay;
      formData.value.bidingAgency = resp.agentUnit;
      formData.value.bidingAgencyContact = resp.agentRelationName;
      formData.value.bidingAgencyPhone = resp.agentRelationWay;
      formData.value.contractAmount = resp.biddingAmountNum;
      formData.value.bidingBudget = resp.biddingAmountNum;
      formData.value.bidingNo = resp.xmNumber;

      getBusinessTypeByName(resp.propertyTypeList);
      getBidingModeByName(resp.biddingTypeStr);
      getServiceContentOther(resp.industryTagCodeList);
      //实时商机列表和明细没有建筑面积，存量商机只有服务面积
      //formData.value.constructionArea=resp.serviceArea;
      formData.value.sourceName = "赢标宝";

      saveQlmCompanyCustomer();
    }
  });
};
//获取千里马客户信息
const saveQlmCompanyCustomer = () => {
  debugger;
  api.customer.saveQlmCompanyCustomer(formData.value.bidingCompany).then(res => {
    if (res.code == 200 || res.code == 500) {
      //message.success('解锁成功');
      formData.value.customerId = res.data.id;
      formData.value.customer = {
        id: res.data.id,
        name: res.data.customerName,
      };
    }
  });
};
//是否解锁招标单位
const getQlmisUnLockCompany = isTip => {
  debugger;
  api.customer.getQlmIsUnLockCompany(formData.value.bidingAgency).then(res => {
    if (res.code == 200 && res.data == false) {
      if (isTip) {
        Modal.confirm({
          title: "操作确认",
          content: "是否解锁该客户数据？",
          onOk() {
            //router.push('/innerPage/customerEdit?isQlm=1&companyName=中国农业银行股份有限公司')
          },
        });
      }
    } else {
      //router.push('/innerPage/customerEdit?isQlm=1&companyName=中国农业银行股份有限公司')
    }
  });
};

const getInfo = async () => {
  let res = await api.project.projectBacklogInfo(projectId.value);
  if (res.code == 200) {
    formData.value = res.data;
    enterInfo.approveStatus = res.data.approveStatus || 0;
    enterInfo.isAdministrators = res.data.isAdministrators ;
    formData.value.provinceCode = Number(res.data.provinceCode);
    formData.value.cityCode = Number(res.data.cityCode);
    formData.value.areaCode = Number(res.data.areaCode);
    formData.value.streetCode = Number(res.data.streetCode);
    if (res.data.areaCode) {
      getStreet(res.data.areaCode);
    }
  }
};

watch(
  () => [
    formData.value.proposedServicePeriod,
    formData.value.contractAmount,
    formData.value.contractAnnualAmount,
    formData.value.signTime,
    formData.value.serviceBeginTime,
    formData.value.serviceEndTime,
    formData.value.contractAmounts,
    formData.value.contractAnnualAmounts,
  ],
  () => {
    if (
      formData.value.proposedServicePeriod &&
      (formData.value.signTime || formData.value.serviceBeginTime || formData.value.serviceEndTime)
    ) {
      if (
        (formData.value.contractAmounts || formData.value.contractAnnualAmounts) &&
        formData.value.inStock == "SHI" &&
        formData.value.isPerformanceIncrement == "SHI"
      ) {
        //增量为是，需要计算增量金额
        getAnnualConversionAmountCalculate("SHI");
      }
      if (formData.value.contractAmount || formData.value.contractAnnualAmount) {
        //如果为否，需要清除增量业绩数据
        if (formData.value.inStock == "FOU" || formData.value.isPerformanceIncrement == "FOU") {
          formData.value.isPerformanceIncrement = "FOU";
          formData.value.contractAmounts = 0;
          formData.value.contractAnnualAmounts = 0;
          formData.value.annualConversionAmounts = 0;
        }
        //无论续签是还是否，都要计算当年转化金额 (元)
        getAnnualConversionAmountCalculate("FOU");
      }
    }
  },
  { deep: true }
);
const getAnnualConversionAmountCalculate = throttle(isIncrement => {
  const arr =
    isIncrement === "SHI"
      ? [
          "id",
          "proposedServicePeriod",
          "contractAmounts",
          "contractAnnualAmounts",
          "signTime",
          "serviceBeginTime",
          "serviceEndTime",
          "projectType",
          "inStock",
          "isPerformanceIncrement",
        ]
      : [
          "id",
          "proposedServicePeriod",
          "contractAmount",
          "contractAnnualAmount",
          "signTime",
          "serviceBeginTime",
          "serviceEndTime",
          "projectType",
          "inStock",
          "isPerformanceIncrement",
          "contractAmounts",
          "contractAnnualAmounts",
          "annualConversionAmounts",
        ];
  const postData = arr.reduce((prev, key) => ({ ...prev, [key]: formData.value[key] }), {
    isIncrement,
    annualConversionAmount: 0,
  });
  api.project.backlogAnnualConversionAmountCalculate(postData).then(res => {
    if (res.code == 200) {
      if (isIncrement === "SHI") {
        formData.value.annualConversionAmounts = parseFloat(res.data) || null;
      } else {
        formData.value.annualConversionAmount = res.data || null;
      }
    }
  });
}, 20);
onMounted(() => {
  enterInfo.id = projectId.value;
  enterInfo.templateId = "19229580f0f022c2f3e00fd406cbfefd";
  enterInfo.isView = isView.value;
  getCheckOaBacklog();
  if (projectId.value) {
    getInfo();
  } else {
    formData.value.serviceStatus = "LI_XIANG_ZHONG";
    formData.value.cooperationMode = "QUAN_WEI_XIANG_MU";
    formData.value.expire = "YOU_XIAO" ;
    formData.value.sourceName = "项目补录新增";
    // //当存在商机ID参数
    // if ("" != realtimeOpportunityId.value) {
    //   formData.value.sourceName = "赢标宝";
    //   formData.value.projectType = "DAN_YI_TOU_BIAO_XIANG_MU";
    //   formData.value.opportunityId = realtimeOpportunityId.value;
    //   getQlmRealTimeDetail();
    // }
    // //存在存量商机
    // if ("" != stockOpportunityId.value) {
    //   formData.value.sourceName = "赢标宝";
    //   formData.value.projectType = "DAN_YI_TOU_BIAO_XIANG_MU";
    //   formData.value.opportunityId = stockOpportunityId.value;
    //   getQlmStockDetail();
    // }
  }
});

const addressChange = areaCode => {
  if (areaCode) {
    getStreet(areaCode);
  }
  formData.value.streetCode = null;
  formData.value.streetZipcode = null;
};
const streetList = ref([]);
const getStreet = areaCode => {
  api.common.listStreetByArea(areaCode).then(res => {
    if (res.code == 200) {
      streetList.value = res.data.map(item => {
        return {
          label: item.name + " (" + item.zipCode + ")",
          value: item.areaCode,
          zipCode: item.zipCode,
        };
      });
    }
  });
};

const userSelect = async user => {
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
    if (role.deptType == "CENG_JI") {
      formData.value.companyId = role.deptId;
    } else {
      formData.value.companyId = role.parentId;
    }
    let obj = await api.sys.regionInfo(role.deptId);
    if (obj.code == 200 && obj.data) {
      formData.value.regionId = obj.data.deptId;
    }
  }
};
const hasChildren = deptId => {
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
  return hasChild;
};
const getCompanyId = (deptId, parentId) => {
  console.log(hasChildren(deptId));
  return hasChildren(deptId) ? deptId : parentId;
};
const getSetRegionId = async (deptType, deptId, parentId) => {
  if (deptType == "CENG_JI") {
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
};

const customerAddOk = res => {
  if (!formData.value.customerId) {
    formData.value.customerId = res.id;
    formData.value.customer = {
      id: res.id,
      name: res.customerName,
    };
  } else {
    //当为新增项目时
    //当存在商机ID参数
    if ("" != realtimeOpportunityId.value && "" != formData.value.bidingAgency) {
      getQlmisUnLockCompany(true);
    }
    //存在存量商机
    if ("" != stockOpportunityId.value && "" != formData.value.bidingAgency) {
      getQlmisUnLockCompany(true);
    }
  }
};

const companyNoRule = (rule, value, callback) => {
  const regex = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;
  if (rule.required && !regex.test(value)) {
    return new Promise((resolve, reject) => {
      reject("请输入有效社会信用代码(18位)");
    });
  }
  return Promise.resolve();
};
</script>
<style scoped lang="less">
.form-message-tip-tip {
  .ant-form-item {
    margin-bottom: 0 !important;
    .ant-form-item-control {
      .ant-form-item-control-inp {
        min-height: 0 !important;
      }
    }
  }
}
</style>
