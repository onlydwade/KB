<template>
  <div class="menu_inner">
    <div class="card_box">
      <van-cell-group title="基础信息" >
<!--        <van-cell title="公司标识" :value="formData.corporateIdentityStr || '-'" />-->
        <van-cell title="甲方单位名称" :value="formData.projectName || '-'" />
        <van-cell title="项目编号" :value="formData.projectNo || '-'" />
        <van-cell title="归属人" :value="(formData.attributorUser || {}).realname || '-'" />
        <van-cell title="归属单位" :value="formData.companyName || '-'" />
        <van-cell title="所属大区" :value="formData.regionName || '-'" />
        <van-cell title="项目优先级" :value="formData.projectLevelStr || '-'" />
        <van-cell title="项目类型" :value="formData.projectTypeStr || '-'" />
        <van-cell title="业务板块" :value="formData.businessSegmentsStr || '-'" />
        <van-cell title="拓展模式" :value="formData.expansionModeStr || '-'" />
        <van-cell title="是否招标" :value="formData.bidedStr || '-'" />
        <van-cell title="合作方式" :value="formData.cooperationModeStr || '-'" />
        <van-cell title="是否为续签项目" :value="formData.inStockStr || '-'" />
        <van-cell title="业态" :value="formData.businessTypeStr || '-'" />
        <van-cell title="服务内容" :value="formData.serviceContentStr || '-'" />
        <van-cell title="其它服务内容"  :value="formData.serviceContentOther || '-'" v-if="(formData.serviceContent || '').includes('QI_TA')"/>
        <van-cell title="关联客户" :value="(formData.customer || {}).name || '-'" />
        <van-cell title="建筑面积（m²）" :value="formData.constructionArea || '-'" />
        <van-cell title="省/市/区/县/街道">
          <template #value>
            {{ formData.provinceName }}
            {{ formData.cityName }}
            {{ formData.areaName }}
            {{ formData.streetName }}
          </template>
        </van-cell>
        <van-cell title="详细地址" :value="formData.address || '-'" />
        <van-cell title="关键词" :value="formData.keywords || '-'" />
        <van-cell title="项目来源" :value="formData.sourceName || '-'" />
        <van-cell title="备注/介绍" :value="formData.remark || '-'" />
      </van-cell-group>
    </div>
    <div class="card_box" v-if="formData.projectType == 'DAN_YI_TOU_BIAO_XIANG_MU'">
      <van-cell-group title="招标信息" >
        <van-cell title="招标编号" :value="formData.bidingNo || '-'" />
        <van-cell title="招标类型" :value="formData.bidingModeStr || '-'" />
        <van-cell title="发布日期" :value="dateFormat(formData.bidingPublishtime, 'YYYY-MM-DD') || '-'"/>
        <van-cell title="招标单位" :value="formData.bidingCompany || '-'" />
        <van-cell title="招标单位联系人" :value="formData.bidingCompanyContact || '-'" />
        <van-cell title="招标单位联系方式" :value="formData.bidingCompanyPhone || '-'" />
        <van-cell title="招标代理机构" :value="formData.bidingAgency || '-'" />
        <van-cell title="代理机构联系人" :value="formData.bidingAgencyContact || '-'" />
        <van-cell title="代理机构联系方式" :value="formData.bidingAgencyPhone || '-'" />
        <van-cell title="投标截止时间" :value="dateFormat(formData.bidingEndtime, 'YYYY-MM-DD') || '-'"/>
        <van-cell title="开标时间" :value="dateFormat(formData.bidingOpentime, 'YYYY-MM-DD') || '-'"/>
        <van-cell title="招标网址" :value="formData.bidingWebsite || '-'" />
      </van-cell-group>
    </div>
    <div class="card_box">
      <van-cell-group title="合同信息录入" >
        <van-cell title="甲方单位名称" :value="formData.firstResponsibleCompany || '-'" />
        <van-cell title="合同总金额（元）" :value="parseFormatNum(formData.contractAmount) || '-'" />
        <van-cell title="合同年度金额（元）" :value="parseFormatNum(formData.contractAnnualAmount) || '-'" />
        <van-cell title="当年转化金额 (元)" :value="parseFormatNum(formData.annualConversionAmount) || '-'" />
        <van-cell title="签约日期" :value="dateFormat(formData.signTime, 'YYYY-MM-DD') || '-'" />
        <van-cell title="服务开始日期" :value="dateFormat(formData.serviceBeginTime, 'YYYY-MM-DD') || '-'" />
        <van-cell title="合同到期日期" :value="dateFormat(formData.serviceEndTime, 'YYYY-MM-DD') || '-'" />
        <van-cell title="拟服务期限（月）" :value="formData.proposedServicePeriod || '-'" />
        <van-cell title="是否有增量业绩确认" :value="formData.isPerformanceIncrementStr || '-'" />
        <van-cell title="是否计算业绩" :value="formData.isCountPerformanceStr || '-'" />
      </van-cell-group>
    </div>
    <div v-if="formData.isPerformanceIncrement === 'SHI' && formData.inStock === 'SHI'" class="card_box">
      <van-cell-group title="增量业绩信息">
        <van-cell title="合同总金额（元）" :value="parseFormatNum(formData.contractAmounts) || '-'" />
        <van-cell title="合同年度金额（元）" :value="parseFormatNum(formData.contractAnnualAmounts) || '-'" />
        <van-cell title="当年转化金额 (元)" :value="parseFormatNum(formData.annualConversionAmounts) || '-'" />
      </van-cell-group>
    </div>
    <AchievementBacklogYd v-if="formData.id" :projectId="projectId" />
    <div class="card_box" v-for="(item,index) in listData" :key="index">
      <div class="title">
        {{ item.operName }}
      </div>
      <div class="list_box" v-for="(t,idx) in item.projectBacklogDocumentList" :key="idx">
        <a-row>
          <a-col :span="18">
            <div class="name"><EllipsisTooltip :content="t.documentName" /></div>
            <div class="time">{{dateFormat(t.createTime,'YYYY-MM-DD HH:mm:ss') }}</div>
          </a-col>
          <a-col :span="6" align="middle" class="btn">
            <span  class="color-primary" @click="fileDownload(t.documentObject)">点击下载</span>
          </a-col>
        </a-row>
      </div>
    </div>
  </div>
</template>
<script setup>
import api from "@/api/index";
import AchievementBacklogYd from "./components/AchievementBacklogYd.vue";
import { dataToFile,dateFormat ,parseFormatNum} from "@/utils/tools";
import { showLoadingToast, closeToast } from 'vant'
const route = useRoute();
const projectId = ref(Number(route.query.id || 0));
const listData = ref([])
const formData = ref(
    {
      roleKeys: [] ,
    }
);

const getInfo = async () => {
  let res = await api.project.projectBacklogInfo(projectId.value);
  if (res.code == 200) {
    formData.value = res.data;
    listData.value = res.data.projectBacklogFilesGroup;
  }
  closeToast(true);
};

const fileDownload = (data)=>{
  let jsData = JSON.parse(data)
  let postData = {
    ossPath : jsData.ossPath,
    name    : jsData.name
  }
  console.log(isMobile)
  if(isMobile){
    api.common.getFilePath(postData).then(res=>{
      if(res.code==200){
        // window.open(res.data,'_blank');

        // 创建a标签
        const link    = document.createElement('a');
        link.href     = res.data;
        link.download = postData.name;
        // 将a标签添加到DOM中并触发点击事件
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }
    })
  }else{
    api.common.downloadFile(postData).then(res=>{
      dataToFile(res,postData.name);
    })
  }
}

onMounted(() => {

   showLoadingToast({
     message: '正在加载...',
     forbidClick: true,
     duration: 0
  });

  if (route.query.access_token) {
    sessionStorage.setItem('backPath', route.fullPath.replace('&access_token=' + route.query.access_token, ''))
    window.location.href = GLOBAL_PATH.tokenAuth + route.query.access_token;
    return;
  }
  getInfo();
});

</script>
<style scoped lang="less">

.card_box {
  margin: 20px 0;
  padding: 10px;
}
.title {
  color: #000;
  font-weight: bold;
  line-height: 40px;
}

.list_box_my {
  // border: 1px solid #f0f2f5;
//   background: #fffaf0;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 8px;
.item_my{
    background: #f99c34;
}
  .name {
    font-size: 15px;
  }

  .simple {
    line-height: 30px;
    color: #969799;
  }
}


.list_box {
  // border: 1px solid #f0f2f5;
//   background: #fffaf0;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 8px;
.item_my{
    background: #f99c34;
}
  .name {
    font-size: 15px;
  }

  .simple {
    line-height: 30px;
    color: #969799;
  }
}


.menu_inner {
  .card_box {
    padding-bottom: 20px;
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