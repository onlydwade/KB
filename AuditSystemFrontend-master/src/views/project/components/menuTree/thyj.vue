<template>
  <div class="menu_inner">
    <a-form ref="formRef" layout="vertical" :model="formData">
      <div class="padding_box">
        <Title>
          <template #title> 项目基础信息 </template>
        </Title>
        <a-row :gutter="24">
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="项目名称">
              <a-input disabled :value="formData.projectName" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="省份/城市/区/县">
              <AddressSelect disabled v-model:provinceCode="formData.provinceCode" v-model:cityCode="formData.cityCode"
                v-model:areaCode="formData.areaCode" mode="area" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="街道">
              <a-input disabled :value="formData.streetName || ''" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="合作方式">
              <a-input disabled :value="formData.cooperationModeStr || ''" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="业态">
              <a-input disabled :value="formData.businessTypeStr || ''" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="服务内容">
              <a-input disabled :value="formData.serviceContentStr || ''" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="合同总金额（元）">
              <a-input-number disabled :value="formData.contractAmount" class="w_full" :formatter="(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                " :parser="(value) => value.replace(/\\s?|(,*)/g, '')"
                :addon-after="amountUnit(formData.contractAmount)" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="建筑面积（m²）">
              <a-input-number disabled :value="formData.constructionArea" class="w_full" :formatter="(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                " :parser="(value) => value.replace(/\\s?|(,*)/g, '')" />
            </a-form-item>
          </a-col>
        </a-row>

        <Title>
          <template #title> 拓展信息 </template>
        </Title>
        <a-row :gutter="24">
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="归属单位">
              <a-input disabled :value="formData.companyName" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="拓展归属人">
              <a-input disabled :value="(formData.attributorUser || {}).realname" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item label="甲方单位名称">
              <a-input disabled :value="formData.firstResponsibleCompany" placeholder="" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item required label="运营团队" name="operationTeamId">
              <DeptSelect :noRoot="true" :disabled="disabled" v-model="formData.operationTeamId" @change="change" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item required label="拓后负责人" name="principalId">
              <UserSelect :disabled="disabled" v-model="formData.principalId" :users="formData.principal" />
            </a-form-item>
          </a-col>
          <a-col :xxl="6" :lg="8" :sm="12">
            <a-form-item required label="实际进场时间" name="enterTime">
              <a-date-picker :disabled="disabled" :getPopupContainer="(trigger) => trigger.parentNode"
                v-model:value="formData.enterTime" class="w_full" valueFormat="YYYY-MM-DD 00:00:00" format="YYYY-MM-DD"
                placeholder="请选择" />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- <Title>
                    <template #title>
                        经营指标信息
                    </template>
                </Title>
                <Indicators :projectId="projectId" :readOnly="disabled"/>

                <Title style="margin-top:32px;">
                    <template #title>
                        立项收缴率指标信息 <span v-if="formData.projectType !='GU_QUAN_HE_ZUO_XIANG_MU'">(仅住宅类项目填报)</span>
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :span="6">
                        <a-form-item  label="指标">
                            立项收缴率指标（%）
                        </a-form-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-item label="第一年（%）" name="collectionRateFirst">
                            <a-input-number :disabled="disabled" v-model:value="formData.collectionRateFirst" class="w_full" :min="0" :max="100"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-item label="第二年（%）" name="collectionRateSecond">
                            <a-input-number :disabled="disabled" v-model:value="formData.collectionRateSecond" class="w_full" :min="0" :max="100"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-item label="第三年（%）" name="collectionRateThird">
                            <a-input-number :disabled="disabled" v-model:value="formData.collectionRateThird" class="w_full" :min="0" :max="100"/>
                        </a-form-item>
                    </a-col>
                </a-row> -->

        <Title>
          <template #title> 特殊说明 </template>
        </Title>
        <a-form-item label="优惠承诺" name="discountsDesc">
          <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.discountsDesc"
            placeholder="如空置费优惠等给甲方的优惠条款，如有，由移交人自行补充描述" show-count />
        </a-form-item>
        <a-form-item label="服务承诺" name="serviceDesc">
          <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.serviceDesc"
            placeholder="如设备完好率、三年创优等服务质量承诺，如有，由移交人自行补充描述" show-count />
        </a-form-item>
        <a-form-item label="主要条款" name="mainClause">
          <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.mainClause"
            placeholder="请输入签约合同的主要条款" show-count />
        </a-form-item>
        <a-form-item label="其他风险提示" name="riskWarning">
          <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.riskWarning"
            placeholder="如有，由移交人自行补充描述" show-count />
        </a-form-item>
        <Title>
          <template #title> 商务关系维护 </template>
        </Title>
        <a-form-item label="拓展人员是否把甲方对接人微信推送给运营负责人，并完成好友添加？" name="wechatFriends">
          <a-switch :disabled="disabled" v-model:checked="formData.wechatFriends" checkedValue="SHI" checked-children="是"
            unCheckedValue="FOU" un-checked-children="否" />
        </a-form-item>
        <a-form-item label="拓展人员是否带运营负责人完成项目现场的走访熟悉  ，交代风险点？" name="interviewed">
          <a-switch :disabled="disabled" v-model:checked="formData.interviewed" checkedValue="SHI" checked-children="是"
            unCheckedValue="FOU" un-checked-children="否" />
        </a-form-item>
        <a-form-item label="其他补充" name="otherDesc">
          <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.otherDesc"
            placeholder="如有，由移交人自行补充描述" show-count />
        </a-form-item>

        <Title title="附件上传">
          <a-form-item name="fileOk" :rules="{ required: true, message: '( 请上传必传文件后提交!!! )' }">
          </a-form-item>
        </Title>
        <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval" v-if="formData.id" :menuId="menuId"
          :projectId="projectId" v-model="formData.fileOk" ref="documentsRef" :readOnly="disabled"  />
      </div>
    </a-form>
    <ProjectActions @submit="submit" :menuInfo="menuInfo" offLineTitle="已线下审批，上传会议纪要标记完成" ref="ProjectActionsRef">
      <a-button size="large" @click="submit(2)"
        v-if="menuInfo.oaApproval != 1 && menuInfo.status == 0 && [0, 9].includes(menuInfo.approvalStatus) && !disabled">标记完成</a-button>
      <a-button size="large" @click="save"
        v-if="(menuInfo.status == 0 && [0, 5, 9].includes(menuInfo.approvalStatus)) || !disabled">暂存</a-button>
    </ProjectActions>
  </div>
</template>
<script setup>
// import Indicators      from '../correlation/Indicators.vue'
import { amountUnit } from "@/utils/tools";
import { useMenuTree } from "./menu";
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
  "operationTeamId",
  "principalId",
  "enterTime",
  "discountsDesc",
  "serviceDesc",
  "mainClause",
  "riskWarning",
  "wechatFriends",
  "interviewed",
  "otherDesc",
];

const {
  formRef,
  documentsRef,
  formData,
  getInfo,
  submit,
  save,
  disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs);

const ProjectActionsRef = ref(null);
const change = (e) => {
  ProjectActionsRef.value.getList(e);
};
onMounted(() => {
  getInfo();
});
</script>
<style scoped lang="less"></style>
