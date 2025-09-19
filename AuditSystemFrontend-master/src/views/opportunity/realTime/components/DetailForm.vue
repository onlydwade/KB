<template>
	<div class="page-project content-inner">
		<div class="bread_box">
			<a-breadcrumb>
				<a-breadcrumb-item><a href="#/index">首页</a></a-breadcrumb-item>
				<a-breadcrumb-item><a  @click="router.back()">实时商机</a></a-breadcrumb-item>
				<a-breadcrumb-item>商机库详情页面</a-breadcrumb-item>
			</a-breadcrumb>

		</div>
 
			<a-collapse ghost expandIconPosition="right" v-model:activeKey="collapseKey" class="crl-collapse">
				<a-collapse-panel key="base">
					<template #header>
						<h5 class="title_single">基础信息</h5>  						
					</template>
					<a-row :gutter="[24, 40]">
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">项目名称：</span>{{ formData.extractProjName || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">业务地区：</span>{{ formData.areaName || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">招标预算：</span>{{ formData.biddingBudget || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">招标截止时间：</span>{{ formData.bidsDeadline || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">商机发布时间：</span>{{ formData.updateTime || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">业主单位：</span>{{ formData.tenderUnit || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12" v-if="formData.lockStatus === 1">
							<span class="title">业主类型：</span>{{ formData.tenderFirstCategory || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12" v-if="formData.lockStatus === 1">
							<span class="title">服务类型：</span>{{ JSON.parse(formData.industryTagCodeList)?.join('、') || '-' }}
						</a-col>
					</a-row>
				</a-collapse-panel>
				<a-collapse-panel key="bidding" >
					<template #header>
						<h5 class="title_single">招标信息</h5>
					</template>
					<a-row :gutter="[24, 40]" v-if="formData.lockStatus === 1">
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">招标单位：</span>{{ formData.tenderUnit || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">招标单位联系人：</span>{{ formData.tenderRelationName || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">招标单位联系方式：</span>{{ formData.tenderRelationWay || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">中标单位：</span>{{ formData.bidderUnit || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">中标单位联系人：</span>{{ formData.bidderRelationName || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">中标单位联系方式：</span>{{ formData.bidderRelationWay || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">代理机构：</span>{{ formData.agentUnit || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">代理机构联系人：</span>{{ formData.agentRelationName || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">代理机构联系方式：</span>{{ formData.agentRelationWay || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">业务省份：</span>{{ formData.provinceStr || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">业务城市：</span>{{ formData.cityStr || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">合同到期时间：</span>{{ formData.expirationDate || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">当前合同金额：</span>{{ formData.biddingAmount || '-' }}
						</a-col>
						<a-col :xxl="6" :lg="8" :sm="12">
							<span class="title">当前合同周期：</span>{{ formData.extractPeriod || '-' }}
						</a-col>
						<a-col :xxl="12" :lg="12" :sm="12">
							<span class="title">在管企业名称：</span>{{ formData.tenderUnit || '-' }}
						</a-col>
						<a-col :xxl="24" :lg="24" :sm="24">
							<span class="title">项目地址：</span>{{ formData.tenderAddress || '-' }}
						</a-col>
 
	
					</a-row>
					<a-divider v-else class="divider">商机未解锁，无法查看，请点击<span class="unlock" @click="unLock()">解锁</span></a-divider>
				</a-collapse-panel>
				<div ><a-button  size="large"   style="float: right;margin-right: 40px;margin-bottom: 20px;"  @click="router.back()">返回</a-button></div> 
			</a-collapse>
			
 
		
	</div>
</template>
<script setup>
import { useRoute } from 'vue-router';
import api from '@/api/index';
import { message, Modal } from 'ant-design-vue';
import { hasPermission } from '@/utils/tools';
const router = useRouter();
const route = useRoute()
const id = route.query.opportunityId
const lockStatus = route.query.lockStatus
const collapseKey = ref(['base', 'bidding'])
const lockData = ref({
  unlockBussinessCount: 0,
  unlockStockCount: 0,
  unlockCompanyCount: 0,
  unlockBussinessAllCount: 0,
  unlockStockAllCount: 0,
  unlockCompanyAllCount: 0
});
const formData = ref({});
const getInfo = () => {
	api.customer.getQlmRealTimeDetail(id, lockStatus).then(res => {
		if (res.code == 200 ) {
			formData.value = res.data[0];
			formData.value.lockStatus=1;
		}
	})
}

const getUnlockData = () => {
  api.customer.getUnlockData().then(res => {
    lockData.value = res.data
  });
}

const unLock = () => {
	if(formData.value.lockStatus=="0"){
	  getUnlockData()
      Modal.confirm({
          title   : '操作确认',
          content : '是否解锁该商机数据？',
          onOk() {
			if( lockData.value.unlockBussinessCount >=lockData.value.unlockBussinessAllCount){
				message.warning('解锁次数已经超过上限，不允许解锁'); 
				return;
			}
			if(!hasPermission(['biz:realTime:unlock']) ){
              message.warning('不允许解锁，请联系管理员开通解锁权限'); 
              return;
            }
              message.success('解锁成功'); 
              getInfo();
          }
      });
    }else{
		getInfo()
    }
}

onMounted(() => {	 
 	formData.value.opportunityId =route.query.opportunityId;
 	formData.value.extractProjName =route.query.extractProjName;
	formData.value.areaName =route.query.areaName;
	formData.value.tenderUnit =route.query.tenderUnit;
	formData.value.biddingBudget =route.query.biddingBudget;
	formData.value.bidsDeadline =route.query.bidsDeadline;
	formData.value.updateTime =route.query.updateTime;
	formData.value.lockStatus =route.query.lockStatus;
	debugger
	if("1"==route.query.lockStatus){
		getInfo()
	}

});
 
</script>
<style scoped lang="less">
.crl-collapse {
	background-color: #ffffff !important;

	.title {
		font-weight: bold;
	}

	.divider {
		font-weight: bold;
		color: #929292;

		.unlock {
			cursor: pointer;
			color: #a4c0d5;
		}
	}
}
</style>