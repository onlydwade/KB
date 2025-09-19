<template>
  <div class="page-project content-inner">
    <a-page-header :ghost="false"
            :breadcrumb="{ routes }">
            <template #footer>
              <span style="font-weight:bold;font-size: 18px;">在管项目库</span>
            </template>
    </a-page-header>
    <div class="menu_tab">
            <div class="tab_item"
            v-for="(item,index) in menuList"
            :key="index"
            :class="{'tab_item_active':tabIndex==index}"
            @click="tabChange(index)" v-show="item.show">
                <span>
                    {{item.name}}
                </span>
            </div>
      </div>
    <PageFilter v-model="filterForm" @submit="filterSubmit" @dataExport="dataExport"
      @exportProjectAchievement="exportProjectAchievement" v-show="tabIndex==0" />
    <PageEnterFilter v-model="filterEnterForm" @submit="filterSubmit" v-show="tabIndex==1" />  
    <div class="content-box_full" v-show="tabIndex==0">
      <Title title="项目列表">
<!--        <template #right>-->
<!--          <a-space>-->
<!--            <NodeConfig />-->
<!--            <a-button type="primary" @click="router.push('/innerPage/projectEdit')" v-permission="['biz:project:add']">-->
<!--              <template #icon>-->
<!--                <plus-outlined />-->
<!--              </template>-->
<!--              新建项目-->
<!--            </a-button>-->
<!--          </a-space>-->
<!--        </template>-->
      </Title>

      <div>
        <a-row :gutter="24" style="margin-top: 12px;">
          <a-col :span="24">
            <div class="dashboard_inner">
              <div class="container_main" style="background-color: rgba(255, 250, 240, 1);">
                <div class="container_font2">
                  <div class="titleValue">
                    <span :style="{'color':filterForm.totalType == 0?'#f99c34':'black'}">项目数：</span> <span :style="{'color':filterForm.totalType == 0?'#f99c34':'black'}" class="num">{{ formData.projectTotal || 0 }} 个</span>
                    <span @click="showClickList(1)"  :style="{'color':filterForm.totalType == 1?'#f99c34':'black'}">续签数：</span> <span :style="{'color':filterForm.totalType == 1?'#f99c34':'black'}" @click="showClickList(1)" class="num">{{ formData.renewTotal || 0 }} 个</span>
                    <span @click="showClickList(2)"  :style="{'color':filterForm.totalType == 2?'#f99c34':'black'}">重新投标数：</span><span :style="{'color':filterForm.totalType == 2?'#f99c34':'black'}" @click="showClickList(2)" class="num">{{ formData.newBidTotal || 0 }} 个</span>
                    <span @click="showClickList(3)"  :style="{'color':filterForm.totalType == 3?'#f99c34':'black'}">完结数：</span> <span :style="{'color':filterForm.totalType == 3?'#f99c34':'black'}" @click="showClickList(3)" class="num">{{ formData.finishTotal || 0 }} 个</span>
                    <span @click="showClickList(4)"  :style="{'color':filterForm.totalType == 4?'#f99c34':'black'}">退场数：</span> <span :style="{'color':filterForm.totalType == 4?'#f99c34':'black'}" @click="showClickList(4)" class="num">{{ formData.exitTotal || 0 }} 个</span>

                    <!-- <span>项目数：</span> <a-button :plain="false" type="text">重置</a-button>

                    <span>续签数：</span> <span @click="showClickList(1)" class="num">{{ formData.renewTotal || 0 }} 个</span>
                    <span>重新投标数：</span><span @click="showClickList(2)" class="num">{{ formData.newBidTotal || 0 }} 个</span>
                    <span>完结数：</span> <span @click="showClickList(3)" class="num">{{ formData.finishTotal || 0 }} 个</span>
                    <span>退场数：</span> <span @click="showClickList(4)" class="num">{{ formData.exitTotal || 0 }} 个</span> -->


                    <!-- <span>到期数：</span> <span class="num">{{ formData.expireTotal || 0 }} 个</span> -->
                    <!-- <span>项目保留率：</span> <span class="num">{{ formData.keepRate || '0.00%' }}</span> -->
                  </div>
                </div>
              </div>
            </div>
          </a-col>
        </a-row>
      </div>

      <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">
        <template #bodyCell="{ column, text, record, index }">
          <template v-if="column.key === 'projectNo'">
            <router-link target="_blank" :to="'/innerPage/projectInfo?id=' + record.id" class="color-link">
              {{ record.projectNo }}
            </router-link>
          </template>
          <template v-if="column.key === 'projectName'">
            <EllipsisTooltip class="flex_full" :content="record.projectName" />
          </template>
          <template v-if="column.key === 'serviceStatus'">
            <projectStatus :projectStatus="record.serviceStatus" />
          </template>
          <template v-if="column.key === 'isSyncOperationStr'">
            {{ record.isSyncOperationStr }}
          </template>
          <template v-if="column.key === 'customerName'">
            <router-link v-if="record.customer" :to="'/innerPage/customerInfo?id=' + record.customerId"
              class="color-link">
              {{ record.customer.name || "-" }}
            </router-link>
            <span v-else>-</span>
          </template>
          <template v-if="column.key === 'sourceName'">
            <router-link v-if="record.relevanceProjectId != null"
              :to="'/innerPage/projectInfo?id=' + record.relevanceProjectId" class="color-link">
              {{ record.sourceName }}
            </router-link>
            <span v-else>{{ record.sourceName || "-" }}</span>
          </template>

          <template v-if="column.key === 'serviceContent'">
            <div class="flex_box">
              <EllipsisTooltip class="flex_full"
                :content="(record.serviceContentStr || '') + (record.serviceContentOther ? ',' + record.serviceContentOther : '')" />
            </div>
          </template>
          <template v-if="column.key === 'bidedStr'">
            <span v-if="record.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">-</span>
            <span v-else>{{ record.bidedStr }}</span>
          </template>
          <template v-if="column.key === 'cooperationTypeStr'">
            <span>{{ record.cooperationTypeStr }}</span>
            <span v-if="(record.cooperationType || '').includes('QI_TA') && record.cooperationTypeOther">-{{
              record.cooperationTypeOther }}</span>
          </template>

          <template v-if="column.key === 'attributorUser'">
            <UserBox :data="record.attributorUser || {}" single />
          </template>
          <template v-if="column.key === 'businessTypeStr'">
            {{ record.businessTypeStr }}
          </template>
          <template v-if="column.key === 'action'">
            <actionBtn :actions="actions(record, index)" />
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
    <div class="content-box_full" v-show="tabIndex==1">
      <Title title="项目补录列表">
        <template #right>
          <a-space>
            <a-button style="margin-left: 10px;" v-permission="['biz:projectEnter:checkConfig']" type="primary" @click="getCheckOaBacklog()">
                <template #icon>
                  <EditOutlined />
                </template>
                审批设置
            </a-button>
            <a-button type="primary"  @click="router.push('/innerPage/projectEnterEdit')" v-permission="['biz:projectEnter:add']">
              <template #icon>
                <plus-outlined />
              </template>
              新建
            </a-button>
          </a-space>
        </template>
      </Title>
      <FullTable :loadding="loadding" :columns="enterData.columns" :dataSource="enterData.list">
        <template #bodyCell="{ column, text, record, index }">
          <template v-if="column.key === 'projectNo'">
            <router-link target="_blank" :to="'/innerPage/projectEnterEdit?id=' + record.id + '&isView=1'" class="color-link">
              {{ record.projectNo }}
            </router-link>
          </template>
          <template v-if="column.key === 'projectName'">
            <EllipsisTooltip class="flex_full" :content="record.projectName" />
          </template>
          <template v-if="column.key === 'serviceStatus'">
            <projectStatus :projectStatus="record.serviceStatus" />
          </template>
          <template v-if="column.key === 'isSyncOperationStr'">
            {{ record.isSyncOperationStr }}
          </template>
          <template v-if="column.key === 'customerName'">
            <router-link v-if="record.customer" :to="'/innerPage/customerInfo?id=' + record.customerId"
              class="color-link">
              {{ record.customer.name || "-" }}
            </router-link>
            <span v-else>-</span>
          </template>
          <template v-if="column.key === 'sourceName'">
            <router-link v-if="record.relevanceProjectId != null"
              :to="'/innerPage/projectInfo?id=' + record.relevanceProjectId" class="color-link">
              {{ record.sourceName }}
            </router-link>
            <span v-else>{{ record.sourceName || "-" }}</span>
          </template>

          <template v-if="column.key === 'serviceContent'">
            <div class="flex_box">
              <EllipsisTooltip class="flex_full"
                :content="(record.serviceContentStr || '') + (record.serviceContentOther ? ',' + record.serviceContentOther : '')" />
            </div>
          </template>
          <template v-if="column.key === 'bidedStr'">
            <span v-if="record.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">-</span>
            <span v-else>{{ record.bidedStr }}</span>
          </template>
          <template v-if="column.key === 'cooperationTypeStr'">
            <span>{{ record.cooperationTypeStr }}</span>
            <span v-if="(record.cooperationType || '').includes('QI_TA') && record.cooperationTypeOther">-{{
              record.cooperationTypeOther }}</span>
          </template>

          <template v-if="column.key === 'attributorUser'">
            <UserBox :data="record.attributorUser || {}" single />
          </template>
          <template v-if="column.key === 'businessTypeStr'">
            {{ record.businessTypeStr }}
          </template>
          <template v-if="column.key === 'action'">
            <!-- <a-button type="text" class="color-primary" size="small" @click="handleEnterEdit(record, index)">编辑</a-button> -->
             <!-- <a-button type="text" class="color-primary" size="small" @click="handleEnterDel(record, index)">删除</a-button> -->
             <actionBtn :actions="actionsEnter(record, index)" />
          </template>
          <EllipsisTooltip v-if="column.ellipsis" :content="text" />
        </template>
      </FullTable>
      <div class="pagination_box">
        <a-pagination showSizeChanger show-quick-jumper v-model:current="filterEnterForm.pageNo"
          v-model:pageSize="filterEnterForm.pageSize" :show-total="(total) => `共 ${total} 条数据`" size="small" @change="projectBacklogPage"
          @showSizeChange="filterEnterForm.pageNo = 1" :total="enterData.total" />
      </div>
      
    </div>
    <BaseHandle ref="baseHandleRef" @success="getPage" />
    <ChangeBelonger ref="changeBelongerRef" @success="getPage" />
    <StopDrawer ref="stopDrawerRef" @success="getPage" />
    <a-modal v-model:visible="visible" :width="660" :height="1000" destroyOnClose>
                <template #title>
                    <span>流程配置</span>
                </template>
                <a-switch v-model:checked="checkoa" :checkedValue="1" checked-children="开启" :unCheckedValue="0"
                    un-checked-children="关闭" />
                <template #footer>
                    <a-button @click="visible = false">
                        关闭
                    </a-button>
                    <a-button type="primary" @click="setCheckOaBacklog()">
                        保存
                    </a-button>
                </template>
    </a-modal>
  </div>
</template>
<script setup>
import api from "@/api/index";
import { message, Modal } from "ant-design-vue";
import PageEnterFilter from "./components/PageEnterFilter.vue";
import PageFilter from "./components/extensFilter2.vue";
//import NodeConfig from "./components/NodeConfig.vue";
import BaseHandle from "./components/BaseHandle.vue";
import ChangeBelonger from "./components/ChangeBelonger.vue";
import StopDrawer from "./components/StopDrawer.vue";
import { dataToFile, hasPermission, dateFormat } from "@/utils/tools";
import { mainStore } from "@/store";
import { ref } from "vue";

const formData = ref({})
const store = mainStore();
const router = useRouter();

const route = useRoute();


const loadding = ref(false);
const pageType = ref('')
const routes     = [
    {
        breadcrumbName: '拓展管理',
    },
    {
        breadcrumbName: '在管项目库',
    },
];
const menuList = [
  {
    name: '在管项目库列表',
    show: 1,
    key: 0,
  },
  {
    name: '项目补录列表',
    show: hasPermission(['biz:projectEnter:enterList']),
    key: 1,
  },
]
const filterForm = reactive({
  pageNo: 1,
  pageSize: 10,
  serviceStatus: ["ZAI_GUAN"],
  processState: [
    "0"
  ],
  expandServiceStatus: ['YI_DAO_QI'],
  expire: 'YOU_XIAO',
  expandTwoServiceStatus: ['YI_TUI_CHANG', 'YI_WAN_JIE'],
  totalType:0
});
const filterEnterForm = reactive({
  pageNo: 1,
  pageSize: 10,
});
const data = reactive({
  list: [],
  columns: [
    // {
    //   title: "公司标识",
    //   dataIndex: "corporateIdentityStr",
    //   width: 140,
    //   ellipsis: true,
    // },
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
      title: "项目编号",
      key: "projectNo",
      width: 150,
    },
    {
      title: "项目名称",
      key: "projectName",
      width: 300,
    },
    {
      title: "关联客户",
      key: "customerName",
      width: 180,
    },
    {
      title: "项目状态",
      key: "serviceStatus",
      width: 130,
    },
    {
      title: "是否同步拓后运营",
      key: "isSyncOperationStr",
      width: 150,
    },

    {
      title: "是否有效",
      dataIndex: "expireStr",
      width: 180,
    },
    {
      title: "项目类型",
      dataIndex: "projectTypeStr",
      width: 150,
    },
    {
      title: '业务板块',
      dataIndex: 'businessSegmentsStr',
      width: 150,
    },
    {
      title: "业态",
      key: "businessTypeStr",
      width: 120,
    },
    {
      title: "服务内容",
      key: "serviceContent",
      width: 180,
    },
    {
      title: "项目优先级",
      dataIndex: "projectLevelStr",
      width: 150,
    },
    {
      title: "项目预估金额(元)",
      dataIndex: "bidingBudget",
      width: 150,
    },
    {
      title: "是否为续签项目",
      dataIndex: "inStockStr",
      width: 120,
    },
    {
      title: "归属人",
      key: "attributorUser",
      width: 80,
    },
    {
      title: "拓展模式",
      dataIndex: "expansionModeStr",
      width: 150,
    },
    {
      title: "合作模式",
      key: "cooperationTypeStr",
      width: 120,
    },
    {
      title: "是否招标",
      key: "bidedStr",
      width: 80,
    },
    {
      title: "招标类型",
      dataIndex: "bidingModeStr",
      width: 120,
    },
    {
      title: "合作方式",
      dataIndex: "cooperationModeStr",
      width: 120,
    },
    {
      title: "项目来源",
      key: "sourceName",
      width: 120,
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
      title: "区/县",
      dataIndex: "areaName",
      width: 120,
    },
    {
      title: "街道",
      dataIndex: "streetName",
      width: 120,
    },
    {
      title: "详细地址",
      dataIndex: "address",
      width: 120,
      ellipsis: true,
    },
    {
      title: "备注",
      dataIndex: "remark",
      width: 120,
      ellipsis: true,
    },
    {
      title: "业绩确认时间",
      dataIndex: "performanceConfirmTime",
      width: 180,
    },
    {
      title: "最新跟进时间",
      dataIndex: "followTime",
      width: 180,
    },
    {
      title: "失效原因",
      dataIndex: "expireReason",
      width: 180,
      ellipsis: true,
    },
    {
      title: "失效时间",
      dataIndex: "expireTime",
      width: 180,
    },
    {
      title: "创建人",
      dataIndex: ["createUser", "realname"],
      width: 180,
    },
    {
      title: "创建时间",
      dataIndex: "createTime",
      width: 180,
    },
    {
      title: "最后修改人",
      dataIndex: ["updateUser", "realname"],
      width: 180,
    },
    {
      title: "最后修改时间",
      dataIndex: "updateTime",
      width: 180,
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
const enterData = reactive({
  list: [],
  columns: [
    // {
    //   title: "公司标识",
    //   dataIndex: "corporateIdentityStr",
    //   width: 140,
    //   ellipsis: true,
    // },
    {
      title: "是否计算业绩",
      dataIndex: "isCountPerformanceStr",
      width: 120,
    },
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
      title: "项目编号",
      key: "projectNo",
      width: 150,
    },
    {
      title: "项目名称",
      key: "projectName",
      width: 300,
    },
    {
      title: "关联客户",
      key: "customerName",
      width: 180,
    },
    {
      title: "补录状态",
      dataIndex: "backfillStatusStr",
      width: 130,
    },
    // {
    //   title: "是否有效",
    //   dataIndex: "validityStatusStr",
    //   width: 180,
    // },
    {
      title: "项目类型",
      dataIndex: "projectTypeStr",
      width: 150,
    },
    {
      title: '业务板块',
      dataIndex: 'businessSegmentsStr',
      width: 150,
    },
    {
      title: "业态",
      key: "businessTypeStr",
      width: 120,
    },
    {
      title: "招标类型",
      dataIndex: "bidingModeStr",
      width: 120,
    },
    {
      title: "服务内容",
      key: "serviceContent",
      width: 180,
    },
    {
      title: "项目优先级",
      dataIndex: "projectLevelStr",
      width: 150,
    },
    {
      title: "项目预估金额(元)",
      dataIndex: "bidingBudget",
      width: 150,
    },
    {
      title: "是否为续签项目",
      dataIndex: "inStockStr",
      width: 120,
    },
    {
      title: "归属人",
      key: "attributorUser",
      width: 80,
    },
    {
      title: "拓展模式",
      dataIndex: "expansionModeStr",
      width: 150,
    },
    // {
    //   title: "合作模式",
    //   key: "cooperationTypeStr",
    //   width: 120,
    // },
    {
      title: "是否招标",
      key: "bidedStr",
      width: 80,
    },
    // {
    //   title: "招标类型",
    //   dataIndex: "bidingModeStr",
    //   width: 120,
    // },
    {
      title: "合作方式",
      dataIndex: "cooperationModeStr",
      width: 120,
    },
    {
      title: "项目来源",
      key: "sourceName",
      width: 120,
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
      title: "区/县",
      dataIndex: "areaName",
      width: 120,
    },
    {
      title: "街道",
      dataIndex: "streetName",
      width: 120,
    },
    {
      title: "详细地址",
      dataIndex: "address",
      width: 120,
      ellipsis: true,
    },
    {
      title: "备注",
      dataIndex: "remark",
      width: 120,
      ellipsis: true,
    },
    {
      title: "最新跟进时间",
      dataIndex: "followTime",
      width: 180,
    },
    {
      title: "创建人",
      dataIndex: ["createUser", "realname"],
      width: 180,
    },
    {
      title: "创建时间",
      dataIndex: "createTime",
      width: 180,
    },
    {
      title: "最后修改人",
      dataIndex: ["updateUser", "realname"],
      width: 180,
    },
    {
      title: "最后修改时间",
      dataIndex: "updateTime",
      width: 180,
    },
    {
      title: "操作",
      key: "action",
      width: 140,
      fixed: "right",
    },
  ],
  total: 0,
})
const tabIndex = ref(0);

const tabChange = (index) => {
  localStorage.setItem('tabIndex',index)
  pageType.value = menuList[index].key;
  tabIndex.value = index;
  if(index == 0){
    getPage();
  }else{
    projectBacklogPage();
  }
}
const getPage = () => {
  let postData = builderFilter();
  loadding.value = true;
  api.project.projectExtensionPage(postData).then((res) => {
    if (res.code == 200) {
      data.list = res.data.records;
      data.total = res.data.total;
      //如果后端返回的页数小于点击的页面则设置默认从第一页重新开始请求
      if (res.data.pages != 0 && res.data.pages < filterForm.pageNo) {
        filterSubmit();
      }
      projectTotal();
    }
    loadding.value = false;
  });
};
const projectBacklogPage = ()=>{
  let postData = builderEnterFilter();
  loadding.value = true;
  api.project.projectBacklogList(postData).then((res) => {
    if (res.code == 200) {
      enterData.list = res.data.records;
      enterData.total = res.data.total;
      //如果后端返回的页数小于点击的页面则设置默认从第一页重新开始请求
      if (res.data.pages != 0 && res.data.pages < filterForm.pageNo) {
        filterSubmit();
      }
    }
    loadding.value = false;
})
}
const visible = ref(false);
const checkoa = ref(1);
const getCheckOaBacklog = () => {
    api.project.getCheckOaBacklog().then(res => {
        if (res.code == 200) {
            visible.value = true;
            checkoa.value = res.data;
        }
    })
}
const setCheckOaBacklog = () => {
    api.project.setCheckOaBacklog(checkoa.value).then(res => {
        if (res.code == 200) {
            message.success('操作成功');
            visible.value = false;
        }
    })
}
const handleEnterEdit = (record) => {
  //跳转编辑页面
  router.push('/innerPage/projectEnterEdit?id=' + record.id)
}
const  handleEnterDel = (record) => {
  
}
const projectTotal = () => {
  let postData = builderFilter();
  api.project.expandProjectTotal(postData).then((res) => {
    if (res.code == 200) {
      formData.value = { ...res.data }
    }
  })
}
const filterSubmit = () => {
  filterForm.pageNo = 1;
  filterEnterForm.pageNo = 1;
  if(tabIndex.value == 0){
    getPage();
  }else{
    projectBacklogPage();
  }
  
};
const dataExport = () => {
  let postData = builderFilter();
  store.spinChange(1);
  api.project.projectTQxport(postData).then((res) => {
    store.spinChange(-1);
    let timestamp = new Date().getTime();
    dataToFile(res, "项目-" + timestamp + ".xlsx");
  });
};
const exportProjectAchievement = () => {
  let postData = builderFilter();
  store.spinChange(1);
  api.project.exportProjectAchievement(postData).then((res) => {
    store.spinChange(-1);
    let timestamp = new Date().getTime();
    dataToFile(res, "业绩台账-" + timestamp + ".xlsx");
  });
};

const showClickList  = (data) => {
  //alert(data);

  if(data == filterForm.totalType){
    filterForm.totalType = 0;
  }else{
    filterForm.totalType = data;
  }
  getPage();
};
const builderFilter = () => {
  let postData = {
    desc: ["createTime"],
    pageNo: filterForm.pageNo,
    pageSize: filterForm.pageSize,
    params: {},
    geParams: {},
    leParams: {},
    inParams: {},
    likeParams: {},
  };
  // if(filterForm.moreFilter){
  let params = [
    "projectLevel",
    "bided",
    "attributorUserId",
    "provinceCode",
    "cityCode",
    "areaCode",
    "streetCode",
    "customerId",
    "inStock",
    "expire",
    "totalType",
    "isSyncOperation"
  ];
  let likeParams = ["projectName", "projectNo"];
  let inParams = [
    "serviceStatus",    
    "expandServiceStatus",
    "businessType",
    "companyId",
    "expansionMode",
    "projectType",
    "processState",
    "expandTwoServiceStatus"
  ];
  params.forEach((key) => {
    if (filterForm[key] && filterForm[key] != -1) {
      postData.params[key] = filterForm[key];
    }
  });
  likeParams.forEach((key) => {
    if (filterForm[key]) {
      postData.likeParams[key] = filterForm[key];
    }
  });
  inParams.forEach((key) => {
    // 业态级联筛选
    // if(filterForm['businessType']&&filterForm['businessType'].length>0) {
    //     let arr =''
    //     filterForm.businessType.forEach((item)=>{
    //         arr += item[1]+ ",";
    //     })
    //     arr = arr.substring(0, arr.length-1);
    //     arr = arr.split(",");
    //     postData.inParams['businessType'] = arr;
    // }
    if (filterForm["businessType"] && filterForm["businessType"].length) {
      postData.inParams["businessType"] = filterForm["businessType"];
    }

    if (filterForm[key] && filterForm[key].length > 0) {
      postData.inParams[key] = filterForm[key];
    }
  });
  // }

  if (!filterForm.moreFilter && filterForm.searchKey) {
    postData.content = filterForm.searchKey;
    postData.contentColumns = ["projectName", "projectNo"];
  }
  if (filterForm.year) {
    postData.geParams.updateStatusDate = filterForm.year + "-01-01 00:00:00";
    postData.leParams.updateStatusDate = filterForm.year + "-12-31 23:59:59";
  }
  return postData;
};
const builderEnterFilter = () => {
  let postData = {
    desc: ["createTime"],
    pageNo: filterEnterForm.pageNo,
    pageSize: filterEnterForm.pageSize,
    params: {},
    geParams: {},
    leParams: {},
    inParams: {},
    likeParams: {},
  };
  // if(filterForm.moreFilter){
  let params = [
    "projectLevel",
    "bided",
    "attributorUserId",
    "provinceCode",
    "cityCode",
    "areaCode",
    "streetCode",
    "customerId",
    "inStock",
    "expire",
    "totalType",
    "isSyncOperation"
  ];
  let likeParams = ["projectName", "projectNo"];
  let inParams = [
    "serviceStatus",
    "expandServiceStatus",
    "businessType",
    "companyId",
    "expansionMode",
    "projectType",
    "backfillStatus",
    "processState",
    "expandTwoServiceStatus"
  ];
  params.forEach((key) => {
    if (filterEnterForm[key] && filterEnterForm[key] != -1) {
      postData.params[key] = filterEnterForm[key];
    }
  });
  likeParams.forEach((key) => {
    if (filterEnterForm[key]) {
      postData.likeParams[key] = filterEnterForm[key];
    }
  });
  inParams.forEach((key) => {
    // 业态级联筛选
    // if(filterForm['businessType']&&filterForm['businessType'].length>0) {
    //     let arr =''
    //     filterForm.businessType.forEach((item)=>{
    //         arr += item[1]+ ",";
    //     })
    //     arr = arr.substring(0, arr.length-1);
    //     arr = arr.split(",");
    //     postData.inParams['businessType'] = arr;
    // }
    if (filterEnterForm["businessType"] && filterEnterForm["businessType"].length) {
      postData.inParams["businessType"] = filterEnterForm["businessType"];
    }

    if (filterEnterForm[key] && filterEnterForm[key].length > 0) {
      postData.inParams[key] = filterEnterForm[key];
    }
  });
  // }

  if (!filterEnterForm.moreFilter && filterEnterForm.searchKey) {
    postData.content = filterEnterForm.searchKey;
    postData.contentColumns = ["projectName", "projectNo"];
  }
  
  if (filterEnterForm.year) {
    postData.geParams.createTime = filterEnterForm.year + "-01-01 00:00:00";
    postData.leParams.createTime = filterEnterForm.year + "-12-31 23:59:59";
  }

  return postData;
};
const actions = (record, index) => {
  return [
    {
      text: '续签',
      show: (record.serviceStatus == 'YI_DAO_QI' || record.serviceStatus == 'ZAI_GUAN') && (record.show == true ||record.attributorOrCreateShow == true || hasPermission(['biz:projectExtension:renew'])) &&!(record.processMode=='3' && ([1, 2].includes(record.processState)))  ,//&& [0, 3].includes(record.processState),
      click: () => {
        handleComponent('renewal', record, index)
      }
    },
    {
      text: '重新投标',
      show: (record.serviceStatus == 'YI_DAO_QI' || record.serviceStatus == 'ZAI_GUAN') &&(record.show == true ||record.attributorOrCreateShow == true || hasPermission(['biz:projectExtension:newBid'])) &&!(record.processMode=='3' && ([1, 2].includes(record.processState)))  ,//&& [0, 3].includes(record.processState),
      click: () => {
        handleComponent('rebid', record, index)
      }
    },
    {
      text: '同步到运营',
      show: (record.show == true ||record.principalShow == true || hasPermission(['biz:projectExtension:IsInManagement'])) && record.isSyncOperation !== 'SHI' && record.serviceStatus != 'YI_WAN_JIE' ,
      click: () => {
        handleComponent('syncOper', record, index)
      }
    },
    {
      text: '退场',
      show: (record.show == true ||record.principalShow == true || hasPermission(['biz:projectExtension:exit'])) && record.serviceStatus != 'YI_WAN_JIE',
      click: () => {
        router.push('/innerPage/extensionInfo?id=' + record.id + '&code=thxmxx' + '&show=' + record.principalShow)
      }
    },
    {
      text: '完结',
      show: (record.show == true ||record.attributorOrCreateShow == true || hasPermission(['biz:project:finish'])) &&  (record.serviceStatus == 'YI_DAO_QI' || record.serviceStatus == 'ZAI_GUAN') &&!(record.processMode=='3' && ([1, 2].includes(record.processState))) ,
      click: () => {
        handleComponent('finish', record, index)
      }
    },
    {
      text: '撤销',
      show: (record.serviceStatus == 'YI_TUI_CHANG' || record.serviceStatus == 'YI_WAN_JIE' || record.serviceStatus == 'CLOSE_FILE') && hasPermission(['biz:projectExtension:rollback'])  ,//&& [0, 3].includes(record.processState),
      click: () => {
        handleComponent('rollback', record, index)
      }
    },
  ]
}
const actionsEnter = (record, index) => {
  return [
    {
      text: '编辑',
      show: ((record.backfillStatus === 0 || record.backfillStatus === 3 || record.backfillStatus === 5|| record.backfillStatus === 4|| record.backfillStatus === 10 || record.backfillStatus == null) && hasPermission(["biz:projectEnter:edit"])) || record.isAdministrators,
      click: () => {
        router.push('/innerPage/projectEnterEdit?id=' + record.id)
     }
   }
  ]
}
const baseHandleRef = ref(null);
const changeBelongerRef = ref(null);
const stopDrawerRef = ref(null);
const handleComponent = (component, row, index) => {
  switch (component) {
    case 'renewal': //续签
      Modal.confirm({
        title: '项目发起续签确认',
        content: '请问是否确定发起"' + row.projectName + '"的续签流程',
        onOk() {
          // api.project.projectExtensionRenew(row.id).then((res) => {
          //   if (res.code == 200) {
          //     message.success('操作成功')
          //     updateRow(res.data, index)
          //   }
          // })
          router.push(`/innerPage/renewalEdit?id=${row.id}&type=1`)
        }
      })
      break
    case 'rebid': // 重新投标
      Modal.confirm({
        title: '项目重新投标确认',
        content: `请问是否确定重新投标"${row.projectName}"项目`,
        onOk() {
          // api.project.projectExtensionNewBid(row.id).then((res) => {
          //   if (res.code == 200) {
          //     message.success('操作成功')
          //     updateRow(res.data, index)
          //   }
          // })
          router.push(`/innerPage/renewalEdit?id=${row.id}&type=2`)
        }
      })
      break
    case 'finish':
      Modal.confirm({
        title: '项目完结确认',
        content: `当前项目到期时间为${dateFormat(row.serviceEndTime, 'YYYY-MM-DD')}，是否确定需要完结？`,
        onOk() {
          api.project.projectEdit({ id: row.id, serviceStatus: 'YI_WAN_JIE' }).then((res) => {
            if (res.code == 200) {
              message.success('操作成功')
              updateRow(res.data, index)
            }
          })
        }
      })
      break
    case 'syncOper':
      Modal.confirm({
        title: '项目同步拓后运营确认',
        content: `请问是否确定同步"${row.projectName}"项目到拓后运营？`,
        onOk() {
          api.project.syncOperationProject(row.id).then((res) => {
            if (res.code == 200) {
              message.success('操作成功')
              updateRow(res.data, index)
            }
          })
        }
      })
      break
    case 'rollback':
      Modal.confirm({
        title: '回退',
        content: `请问是否确定回退"${row.projectName}"项目？`,
        onOk() {
          api.project.rollbackProject(row.id).then((res) => {
            if (res.code == 200) {
              message.success('操作成功')
              updateRow(res.data, index)
            }
          })
        }
      })
      break
    default:

  }
}

const updateRow = (res, index) => {
  getPage();
}

onMounted(() => {
  if(route.query.tabIndex == 1){
    tabIndex.value = 1
  }
  if(tabIndex.value == 0){
    getPage();
  }else{
    projectBacklogPage();
  }
});
onActivated(() => {
  getPage();
});
</script>
<style scoped lang="less"></style>

<style scoped lang="less">
.dashboard_inner {
  width: 98%;
  margin-left: 15px;

  .container_main {
    display: flex;
    flex-wrap: wrap;
    height: 80px;
    min-height: 80px;

    .container_font {
      padding: 20px;
      border-radius: 10px;
      color: rgba(0, 0, 0, 0.6470588235294118);

      .numValue {
        font-size: 16px;
        line-height: 20px;
      }

      .titleValue {
        font-size: 14px;
        font-weight: 400;
        line-height: 35px;
      }
    }

    .container_font2 {
      padding: 25px;
      border-radius: 10px;
      color: rgba(0, 0, 0, 0.6470588235294118);

      .numValue {
        font-size: 24px;
        line-height: 35px;
      }

      .titleValue {
        font-size: 14px;
        font-weight: 400;
        line-height: 35px;
      }
    }

    .num {
      margin-right: 40px;
      cursor: pointer;

      // &:hover {
      //   color: #f99c34;
      // }
    }

    .box_color {
      background-color: #f99c34;
    }
  }
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 8px;
}
.menu_tab{
    display : flex;
    .tab_item{
        font-size        : 16px;
        padding          : 6px 16px;
        border-radius    : 6px 6px 0 0;
        background-color : #fffaf0;
        color            : @primary-color;
        margin-right     : 4px;
        cursor           : pointer;
        box-shadow       : 0 -4px 4px rgba(249,156,52,0.1) inset;
        position         : relative;
        overflow         : hidden;
        span{
            z-index    : 2;
            position   : relative;
            transition : all 0.3s;
        }
        &:hover{
            opacity: 0.85;
        }
        &::after{
            content          : '';
            height           : 0;
            width            : 100%;
            background-color : @primary-color;
            border-radius    : 1px;
            position         : absolute;
            bottom           : 0;
            left             : 0;
            transition       : all 0.3s;
            z-index          : 1;
        }
    }
    .tab_item_active{
        span{
            color            : #fff;
        }
        &::after{
            height : 100%;
        }
    }
    .tab_item_disabled{
        cursor           : not-allowed;
        background-color : #ccc;
        color            : #555;

        &:hover{
            opacity: 1;
        }
    }
}
</style>
