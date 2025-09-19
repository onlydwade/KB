<template>
  <div class="menu_inner">
    <div class="card_box">
      <van-cell-group title="基础信息">
        <van-cell title="项目名称">
          <template #value>
            {{ formData.projectName || '-' }}
            <CheckListYd :data="formData" />
          </template>
        </van-cell>
        <van-cell title="所属大区" :value="formData.regionName || '-'" />
        <van-cell title="归属单位" :value="formData.companyName || '-'" />
        <van-cell title="归属人" :value="(formData.attributorUser || {}).realname" />
        <van-cell title="项目优先级" :value="formData.projectLevelStr || '-'" />
        <van-cell title="业务板块" :value="formData.businessSegmentsStr || '-'" />
        <van-cell title="拓展模式" :value="formData.expansionModeStr || '-'" />
        <div v-if="formData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">
          <van-cell title="项目类型" :value="formData.projectTypeStr || '-'" />
          <van-cell title="目标公司名称" :value="formData.targetCompanyName || '-'" />
          <van-cell title="目标公司社会统一信用代码" :value="formData.targetCompanyNo || '-'" />
          <van-cell title="目标公司法定代表人" :value="formData.targetLegalRepresentative || '-'" />
          <van-cell title="目标公司类型" :value="formData.targetCompanyTypeStr || '-'" />
          <van-cell title="目标公司注册资本(万元)" :value="formData.targetRegisteredCapital || '-'" />
          <van-cell title="目标公司所属业态" :value="formData.targetBusinessTypeStr || '-'" />
          <van-cell title="目标公司成立日期" :value="formData.targetIncorporationTime || '-'" />
          <van-cell title="合作人员规模" :value="formData.targetPersonnel || '-'" />
          <van-cell title="合作模式" :value="(formData.cooperationTypeStr || '') + (formData.cooperationTypeOther || '')" />
        </div>
        <div v-else>
          <van-cell title="是否招标" :value="formData.bidedStr || '-'" />
          <van-cell title="合作方式" :value="formData.cooperationModeStr || '-'" />
          <van-cell title="是否为续签项目" :value="formData.inStockStr || '-'" />
          <van-cell title="业态" :value="formData.businessTypeStr || '-'" />
          <van-cell title="服务内容" :value="formData.serviceContentStr || '-'" />
          <template v-if="(formData.serviceContent || '').includes('QI_TA')">
            <van-cell title="服务内容" :value="formData.serviceContentOther || '-'" />
          </template>
          <van-cell title="关联客户" :value="(formData.customer || {}).name || '-'" />
          <van-cell title="建筑面积（m²）" :value="formData.constructionArea || '-'" />
          <van-cell title="预计进场时间" :value="dateFormat(formData.approachTime, 'YYYY-MM-DD')" />
        </div>
        <van-cell title="省/市/区/县/街道">
          <template #value>
            {{ formData.provinceName }}
            {{ formData.cityName }}
            {{ formData.areaName }}
            {{ formData.streetName }}
          </template>
        </van-cell>
        <van-cell title="详细地址" :value="formData.address || '-'" />
        <van-cell title="项目预估金额（元）" :value="parseFormatNum(formData.bidingBudget)" />
        <van-cell title="项目来源" :value="formData.sourceName || '-'" />
      </van-cell-group>
    </div>
    <div v-if="projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">
      <ShareholderYd :projectId="projectId" />
      <ExecutivesYd :projectId="projectId" />
      <PoolYd :projectId="projectId" />
    </div>
    <div class="card_box" v-if="projectType == 'DAN_YI_TOU_BIAO_XIANG_MU'">
      <van-cell-group title="投标报名">
        <van-cell title="参标单位" :value="formData.bidPartInCompany || '-'" />
        <van-cell title="参标主体（人力组织）" :value="getHrOrgName(formData.hrOrg) " />        
        <van-cell title="标书购买金额（元）" :value="parseFormatNum(formData.tenderAmount) || '-'" />
        <van-cell title="是否推送报销系统" :value="formData.reimbursementStr || '-'" />
        <van-cell title="招标编号" :value="formData.bidingNo || '-'" />
        <van-cell title="招标类型" :value="formData.bidingModeStr || '-'" />
        <van-cell title="发布日期" :value="dateFormat(formData.bidingPublishtime, 'YYYY-MM-DD')" />
        <van-cell title="招标单位" :value="formData.bidingCompany || '-'" />
        <van-cell title="招标单位联系人" :value="formData.bidingCompanyContact || '-'" />
        <van-cell title="招标单位联系方式" :value="formData.bidingCompanyPhone || '-'" />
        <van-cell title="招标代理机构" :value="formData.bidingAgency || '-'" />
        <van-cell title="代理机构联系人" :value="formData.bidingAgencyContact || '-'" />
        <van-cell title="代理机构联系方式" :value="formData.bidingAgencyPhone || '-'" />
        <van-cell title="投标截止时间" :value="dateFormat(formData.bidingEndtime, 'YYYY-MM-DD')" />
        <van-cell title="开标时间" :value="dateFormat(formData.bidingOpentime, 'YYYY-MM-DD')" />
        <van-cell title="招标网址" :value="formData.bidingWebsite || '-'" />
      </van-cell-group>
      <div class="bm"><span style="font-weight: bold;">碧拓报名</span> <span>(此处由大区成员记录)</span></div>
      <a-form-item required label="报名已通过" name="btApply" :rules="[{ required: true, validator: btApplyRule }]"
        style="margin-left:10px;">
        <a-switch v-model:checked="formData.btApply" :disabled="disabled || store.deptPost.level != 2" checkedValue="SHI"
          checked-children="是" unCheckedValue="FOU" un-checked-children="否" />
      </a-form-item>
    </div>
    <FileList :menuId="menuId" :projectId="projectId" />
  </div>
</template>
<script setup>
import { amountUnit, dateFormat, parseFormatNum } from "@/utils/tools";
import { useMenuTree } from "./menu";
import { useDictStore } from "@/store/dict";
import { mainStore } from "@/store";
import ShareholderYd from "./components/ShareholderYd.vue";
import ExecutivesYd from "./components/ExecutivesYd.vue";
import PoolYd from "./components/PoolYd.vue";
import CheckListYd from "./components/CheckListYd.vue";
import api               from '@/api/index';
import AchievementBacklogYd from "@/views/project/components/oaMenuTree/components/AchievementBacklogYd.vue";
const store = mainStore();
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
  },
});
const hrOrgName=ref('');
const projectType = inject("getAutoParams")("projectType");
const formAttrs = computed(() => {
  return projectType.value == "DAN_YI_TOU_BIAO_XIANG_MU"
    ? ["id", "bidPartInCompany", "tenderAmount", "btApply"]
    : null;
});
const { formRef, documentsRef, formData, getInfo, disabled } = useMenuTree(
  props.projectId,
  toRefs(props).menuInfo,
  formAttrs.value
);

const btApplyRule = (rule, value, callback) => {
  if (rule.required && value != 'SHI') {
    return new Promise((resolve, reject) => {
      reject('勾选此项后方可继续操作！')
    });
  }
  return Promise.resolve();
}



onMounted(() => {
  getInfo();
  getHrOrg();
});


const mainDeptHrTree = ref([]);
const getHrOrgName=(code)=>{
  let name=''
  mainDeptHrTree.value.forEach(element => {                
    if(code==element.code){      
        name=element.desc2;
      }
  });

  return name;
}
const getHrOrg = ()=>{ 
    api.sys.hrOrgTree().then(res => {
        if (res.code == 200) {  
            if(res.data){
              let ele= res.data[0];
              mainDeptHrTree.value.push({code:ele.code,desc2:ele.desc2});
              if(formData.hrOrg==ele.code){
                hrOrgName.value=ele.desc2;
              }
              ele.childs.forEach(element => {                
                  mainDeptHrTree.value.push({code:element.code,desc2:element.desc2});
              });
            }
        }
        return hrOrgName.value;
    })  
}
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
}
</style>
