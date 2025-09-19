<template>
  <div class="page-project content-inner">
    <div class="bread_box">
      <a-breadcrumb>
        <a-breadcrumb-item><a href="#/index">首页</a></a-breadcrumb-item>
        <a-breadcrumb-item>实时商机</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <page-filter v-model="filterForm" @submit="seacrh" />
    <div class="content-box_full">
      <Title title="实时商机库" />
      <div class="percentage">
        <div>实时标讯解锁占比 {{ lockData.unlockBussinessCount }}/{{ lockData.unlockBussinessAllCount }}</div>
        <div>企业库解锁占比 {{ lockData.unlockCompanyCount }}/{{ lockData.unlockCompanyAllCount }}</div>
      </div>
      <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">
        <template #bodyCell="{ column, text, record, index }">
          <template v-if="column.key === 'extractProjName'">
             <a-tooltip placement="top">
              <template #title>
                {{ record.extractProjName }}
              </template>
              <div class="extractProjName" @click="toDetail(record)">
                {{ record.extractProjName }}
              </div>
            </a-tooltip>  
          </template>
          <template v-if="column.key === 'areaName'">
            {{ record.areaName }}
          </template>
          <template v-if="column.key === 'tenderUnit'"> 
              {{ record.tenderUnit }} 
          </template>
          <template v-if="column.key === 'url'">
            <a-button type="link" color="#eea245" @click="getURL(record)">查看</a-button>
          </template>
          <template v-if="column.key === 'biddingType'">
            {{ record.biddingTypeStr }}
          </template>
          <template v-if="column.key === 'biddingBudget'">
            {{ record.biddingBudget }}
          </template>
          <template v-if="column.key === 'bidsDeadline'">
            {{ record.bidsDeadline }}
          </template>
          <template v-if="column.key === 'updateTime'">
            {{ record.updateTime }}
          </template>
          <template v-if="column.key === 'lockStatusStr'">
            {{ record.lockStatusStr }}
          </template>
          <template v-if="column.key === 'isCreated'">
            <span v-if="record.isCreated === 'SHI'">是</span>
            <span v-else>否</span>
          </template>
          <template v-if="column.key === 'action'">
            <a-button type="link" color="#eea245" v-if="record.hasCustomer === false&&hasPermission(['biz:realTime:addCustomer'])"
              @click="createCustomer(record)">添加客户</a-button>
            <a-button type="link" color="#eea245" v-if="record.isCreated === 'FOU' &&hasPermission(['biz:realTime:addProject'])"
              @click="createProject(record)">创建项目</a-button>
          </template>
        </template>
      </FullTable>
      <div class="pagination_box">
        <a-pagination showSizeChanger show-quick-jumper v-model:current="filterForm.pageNo"
          v-model:pageSize="filterForm.pageSize" :show-total="(total) => `共 ${total} 条数据`" size="small" @change="getPage()"
          @showSizeChange="filterForm.pageNo = 1" :total="data.total" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router'
import api from "@/api/index";
import { message, Modal } from 'ant-design-vue';
import PageFilter from './components/PageFilter.vue'
import { hasPermission } from '@/utils/tools';

const router = useRouter()
const lockData = ref({
  unlockBussinessCount: 0,
  unlockStockCount: 0,
  unlockCompanyCount: 0,
  unlockBussinessAllCount: 0,
  unlockStockAllCount: 0,
  unlockCompanyAllCount: 0
});
const filterForm = ref({
  pageNo: 1,
  pageSize: 10,
})

const loadding = ref(false)

const data = reactive({
  list: [],
  columns: [
    {
      title: "项目名称",
      key: "extractProjName",
      width: 500,
      ellipsis:false
    },
    {
      title: "业务地区",
      dataIndex: "areaName",
      width: 100,
    },
    {
      title: "业主单位",
      key: "tenderUnit",
      width: 250,
    },
    {
      title: "赢标宝详情页",
      key: "url",
      width: 100,
    },
    {
      title: "招标方式",
      key: "biddingType",
      width: 180,
    },
    {
      title: "招标预算",
      key: "biddingBudget",
      width: 180,
    },
    {
      title: "招标截止时间",
      key: "bidsDeadline",
      width: 130,
    },
    {
      title: "商机发布时间",
      dataIndex: "updateTime",
      width: 180,
    },
    {
      title: "是否解锁",
      dataIndex: "lockStatusStr",
      width: 150,
    },
    {
      title: "是否创建项目",
      key: "isCreated",
      width: 120,
    },
    {
      title: "操作",
      key: "action",
      width: 240,
      fixed: "right",
    },
  ],
  total: 0,
});

const seacrh = () => {
  loadding.value = true
  api.customer.getQlmRealTimeList(filterForm.value).then(res => {
    loadding.value = false
    if (res.code === 200) {
      data.list = res.data.list
      data.total = res.data.totalSize
      // //如果后端返回的页数小于点击的页面则设置默认从第一页重新开始请求
      // if (res.data.totalPage != 0 && res.data.totalPage < filterForm.value.pageNo) {
      //   filterForm.value.pageNo=1;        
      //   seacrh()
      // }
    }
  })
}

const getPage = () => {
  seacrh()
}

const toDetail = (record) => {
  router.push({ name: 'realTimeDetail', 
  query:{
    opportunityId:record.opportunityId, 
    extractProjName:record.extractProjName,
    areaName:record.areaName,
    tenderUnit:record.tenderUnit,
    biddingBudget:record.biddingBudget,
    bidsDeadline:record.bidsDeadline,
    updateTime:record.updateTime,
    lockStatus:record.lockStatus}
  })
}
const getInfo = (record) => {
	api.customer.getQlmRealTimeDetail(record.opportunityId, record.lockStatus).then(res => {
    if (res.code == 200 ) {
      message.success('解锁成功'); 
      seacrh();
      getUnlockData();
		}
	})
}

const getURL = (record) => {   
  if(record.lockStatus=="0"){
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
              //计算+1
              getInfo(record);
              goToQlmDetail(record); 
          }
      });
    }else{
      goToQlmDetail(record);
    }
 
}

const goToQlmDetail=(record)=>{
  api.customer.getQlmToken( record.opportunityId).then(res=>{  
    if(res.code==200 && res.data==""){
      //message.warn('千里马无法获取token，请联系管理员');
      //return;
    } 
    let  linkUrl=record.url +  res.data;
    console.log("千里马跳转连接:"+ linkUrl);
    window.open(linkUrl, '_blank')
  });
}

const getUnlockData = () => {
  api.customer.getUnlockData().then(res => {
    lockData.value = res.data
  });
}

const createCustomer = (record) => {
  api.customer.getQlmIsUnLockCompany(record.tenderUnit).then(res => {
    if (res.code == 200 && res.data == false) {
      getUnlockData();
      Modal.confirm({
        title: '操作确认',
        content: '添加客户信息需要消耗解锁次数，是否继续？',
        onOk() {
             if( lockData.value.unlockCompanyCount >=lockData.value.unlockCompanyAllCount){
            message.warning('解锁次数已经超过上限，不允许解锁'); 
            return;
          }
          getQlmInfo(record.tenderUnit);
          //message.success('解锁成功');
        }
      });
    } else {
      // message.warning('已存在客户，不允许解锁'); 
      // return;
      // router.push('/innerPage/customerEdit?isQlm=1&companyName=中国农业银行股份有限公司')
      router.push({ path: '/innerPage/customerEdit', query: { companyName: record.tenderUnit, isQlm: 1 } })
    }
  })

}

 
//获取千里马客户信息
const getQlmInfo  = (companyName)=>{
    api.customer.getQlmCompany(companyName).then(res=>{
        if(res.code==200  ){
            message.success('解锁成功');
            seacrh();
  getUnlockData();
            router.push({ path: '/innerPage/customerEdit', query: { companyName: companyName, isQlm: 1 } })
            //formData.value            = res.data;
        }else if(res.code==500  ){
          message.success(msg);
        }else{
          message.success('赢标宝请求失败，请联系管理员');
        }
    })
}
const createProject = (record) => {
  if (record.lockStatus == "0") {

    Modal.confirm({
      title: '操作确认',
      content: '是否解锁该商机数据？',
      onOk() {
        if( lockData.value.unlockBussinessCount >=lockData.value.unlockBussinessAllCount){
              message.warning('解锁次数已经超过上限，不允许解锁'); 
              return;
            }
            if(!hasPermission(['biz:realTime:unlock']) ){
              message.warning('不允许解锁，请联系管理员开通解锁权限'); 
              return;
            }
            seacrh();
  getUnlockData();
        message.success('解锁成功');
        router.push({ path: '/innerPage/projectEdit', query: { realtimeOpportunityId: record.opportunityId, lockStatus: record.lockStatus } })
      }
    });
  } else {
    router.push({ path: '/innerPage/projectEdit', query: { realtimeOpportunityId: record.opportunityId, lockStatus: record.lockStatus } })
  }
}
onMounted(() => {
  seacrh();
  getUnlockData();
})
</script>
<style lang="less" scoped>
.percentage {
  display: flex;
  align-items: center;
  font-size: 1.3rem;
  padding: 2rem;
  margin: 1rem 1rem 0rem;
  background-color: #fffaf0;

  div {
    margin-right: 2rem;
  }
}

.extractProjName {
  width: 480px;
  // text-overflow: ellipsis;
  // overflow: hidden;
  // text-wrap: nowrap;
  cursor: pointer;
  color: #eea245;
}
</style>