<template>
  <div class="page-project content-inner">
    <div class="bread_box">
      <a-breadcrumb>
        <a-breadcrumb-item><a href="#/index">业绩管理</a></a-breadcrumb-item>
        <a-breadcrumb-item>实际签约汇总</a-breadcrumb-item>
      </a-breadcrumb>
    </div>

    <PageFilterSummary :is="view" v-model="filterForm" @submit="filterSubmit" @dataExport="dataExport"/>

    <div class="content-box_full">
      <Title title="实际签约汇总列表">
      </Title>
      <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">
        <template #bodyCell="{ column, text, record, index }">

          <template v-if="column.key === 'projectName'">
            <EllipsisTooltip class="flex_full" :content="record.projectName" />
          </template>
          <template v-if="column.key === 'regionName'">
            <EllipsisTooltip class="flex_full" :content="record.regionName" />
          </template>
          <template v-if="column.key === 'companyName'">
            <EllipsisTooltip class="flex_full" :content="record.companyName" />
          </template>
          <template v-if="column.key === 'firstResponsibleCompany'">
            <EllipsisTooltip class="flex_full" :content="record.firstResponsibleCompany" />
          </template>
          <template v-if="column.key === 'secondParty'">
            <EllipsisTooltip class="flex_full" :content="record.secondParty" />
          </template>
          
          <template v-if="column.key === 'downloadPath'">
          <span>{{ record.downloadPath || "-" }}</span>
          </template>
          <template v-if="column.key === 'businessTypeStr'">
          <span>{{ record.businessTypeStr || "-" }}</span>
          </template>
          
       
          <template v-if="column.key === 'detail'">     
            <a-button type="link" color="#eea245" @click="openAchievement(record)">查看</a-button>
          </template> 

          <template v-if="column.key === 'serviceContent'">
            <div class="flex_box">
              <EllipsisTooltip class="flex_full"
                :content="(record.serviceContentStr || '') + (record.serviceContentOther ? ',' + record.serviceContentOther : '')" />
            </div>
          </template>
  

          <EllipsisTooltip v-if="column.ellipsis" :content="text" />
        </template>
      </FullTable>
      <div class="pagination_box">
        <a-pagination showSizeChanger show-quick-jumper v-model:current="filterForm.pageNo"
          v-model:pageSize="filterForm.pageSize" :show-total="(total) => `共 ${total} 条数据`" size="small" @change="getPage"
          @showSizeChange="filterForm.pageNo = 1" :total="data.total" />
      </div>
    </div>

    <a-modal v-model:visible="visible" title="业绩分配信息" destroyOnClose="true" :footer="null"  :width="660">
        <Achievement  :projectId="projectId" readOnly />
    </a-modal>

  </div>
</template>
<script setup>
import api from "@/api/index";
import { message, Modal } from "ant-design-vue";
import Achievement from "./components/correlation/Achievement.vue";
import PageFilterSummary from "./components/PageFilterSummary.vue";
import { dataToFile, hasPermission, dateFormat } from "@/utils/tools";
import { mainStore } from "@/store";

const store = mainStore();
const router = useRouter();

const loadding = ref(false);

const visible = ref(false);

const projectId = ref(0);


const filterForm = reactive({
  pageNo: 1,
  pageSize: 10
});
const data = reactive({
  list: [],
  columns: [
    {
      title: "大区",
      dataIndex: "regionName",
      width: 140,
      ellipsis: true,
    },
    {
      title: "归属单位",
      dataIndex: "companyName",
      width: 200,
      ellipsis: true,
    },
 
    {
      title: "项目名称",
      key: "projectName",
      width: 300,
    },
    {
      title: "业务板块",
      dataIndex: "businessSegmentsStr",
      width: 150,
    },
    {
      title: "拓展模式",
      dataIndex: "expansionModeStr",
      width: 150,
    },
    {
      title: "招标类型",
      dataIndex: "bidingModeStr",
      width: 120,
    },
    {
      title: "业态",
      key: "businessTypeStr",
      width: 220,
    },
    {
      title: "是否为续签项目",
      dataIndex: "inStockStr",
      width: 120,
    },
    {
      title: "是否计算业绩",
      dataIndex: "isCountPerformanceStr",
      width: 120,
    },
    {
      title: "合同总金额(元)",
      dataIndex: "contractAmount",
      width: 200,
    },
    {
      title: "合同年度金额(元)",
      dataIndex: "contractAnnualAmount",
      width: 200,
    },
    {
      title: "当年转化收入(元)",
      dataIndex: "annualConversionAmount",
      width: 200,
    },

    {
      title: "增量合同总金额(元)",
      dataIndex: "contractAmounts",
      width: 200,
    },
    {
      title: "增量合同年度金额(元)",
      dataIndex: "contractAnnualAmounts",
      width: 200,
    },
    {
      title: "增量当年转化收入(元)",
      dataIndex: "annualConversionAmounts",
      width: 200,
    },
    {
      title: "建筑面积 (㎡)",
      dataIndex: "constructionArea",
      width: 120,
    },
    {
      title: "签约日期",
      dataIndex: "signTime",
      width: 140,
      customRender: ({ text }) => {
        return dateFormat(text, "YYYY-MM-DD");
      },
    },
    {
      title: "服务开始日期",
      dataIndex: "serviceBeginTime",
      width: 140,
      customRender: ({ text }) => {
        return dateFormat(text, "YYYY-MM-DD");
      },
    },
    {
      title: "合同到期日期",
      dataIndex: "serviceEndTime",
      width: 140,
      customRender: ({ text }) => {
        return dateFormat(text, "YYYY-MM-DD");
      },
    },
    {
      title: "拟服务期限(月)",
      dataIndex: "proposedServicePeriod",
      width: 170,
    },
    {
      title: "查看明细",
      key: "detail",
      width: 120,
    },
    {
      title: "甲方单位名称",
      key: "firstResponsibleCompany",
      width: 250,
    },
    {
      title: "乙方单位名称",
      key: "secondParty",
      width: 250,
    },
    {
      title: "服务内容",
      key: "serviceContent",
      width: 280,
    },
    {
      title: "省份",
      dataIndex: "provinceName",
      width: 120,
    },    
    {
      title: "城市",
      dataIndex: "cityName",
      width: 120,
    },
    {
      title: "合同下载地址",
      key: "downloadPath",
      width: 1280,
    }
  
  ],
  total: 0,
});

const getPage = () => {
  let postData = builderFilter();  
  loadding.value = true;
  api.performance.actualInSummaryPage(postData).then((res) => {
    if (res.code == 200) {
      data.list = res.data.records;
      data.total = res.data.total;
      //如果后端返回的页数小于点击的页面则设置默认从第一页重新开始请求
      if (res.data.pages != 0 && res.data.pages < filterForm.pageNo) {
        filterSubmit();
      }
    }
    loadding.value = false;
  });
};
const filterSubmit = () => {
  filterForm.pageNo = 1;
  getPage();
};
const dataExport = () => {
  let postData = builderFilter();
  store.spinChange(1);
  api.performance.actualInSummaryExport(postData).then((res) => {
    store.spinChange(-1);
    let timestamp = new Date().getTime();
    dataToFile(res, "实际签约汇总-" + timestamp + ".xlsx");
  });
};

const builderFilter = () => {

  
    var companyIds = [];
    var expandCompanyIds = [];
  

    if(filterForm.companyId!= undefined){
    for(var i =0;i<filterForm.companyId.length;i++){
        companyIds.push(filterForm.companyId[i].value)
    }}

    if(filterForm.companyId!= undefined){
    for(var i =0;i<filterForm.expandCompanyId.length;i++){
        expandCompanyIds.push(filterForm.expandCompanyId[i].value)
    }
  }
    // formData.value.companyId = companyIds;
    // formData.value.expandCompanyId = expandCompanyIds;
    


  let dto={        
    pageNo: filterForm.pageNo,
    pageSize: filterForm.pageSize,
    // deptIds:filterForm.deptIds,
    companyIds: companyIds,
    expandCompanyIds:expandCompanyIds, 
    inStock:filterForm.inStock,
    hasExpandCompanys:filterForm.hasExpandCompanys,
    isRenewalTender:filterForm.isRenewalTender
  }
  if (filterForm.year) {
    dto.start = filterForm.year + "-01-01 00:00:00";
    dto.end = filterForm.year + "-12-31 23:59:59";
  }
  return dto;
 
};
 

const openAchievement = (record, index) => {
    if(record.id){
      projectId.value=record.id;
      visible.value=true;      
    }
}
 

onMounted(() => {
  getPage();
});
onActivated(() => {
  getPage();
});
</script>
<style scoped lang="less"></style>
