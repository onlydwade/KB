<template>
  <div class="content-inner">
    <div class="bread_box">
      <a-breadcrumb>
        <a-breadcrumb-item>拓后运营</a-breadcrumb-item>
        <a-breadcrumb-item>承接查验</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <inspectionFilter v-model="filterForm" @submit="filterSubmit" />
    <div class="content-box_full">
      <Title title="承接查验列表"></Title>
      <FullTable
        :loadding="loadding"
        :columns="data.columns"
        :dataSource="data.list"
      >
        <template #bodyCell="{ record, column, index }">
          <template v-if="column.key === 'projectNo'">
            <router-link
              :to="'/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&to=thcy'+ '&show=' + record.show"
              class="color-link"
            >
              {{ record.projectNo }}
            </router-link>
          </template>
          <template v-if="column.key === 'projectName'">
            <router-link
              :to="'/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&to=thcy'+ '&show=' + record.show"
              class="color-link"
            >
              <!-- {{ record.projectName }} -->
              <EllipsisTooltip
                class="flex_full"
                :content="record.projectName"
              />
            </router-link>
          </template>
          <template v-if="column.key === 'serviceStatus'">
            <projectStatus :projectStatus="record.serviceStatus" />
          </template>
          <template v-if="column.key === 'serviceContent'">
            <div class="flex_box">
              <EllipsisTooltip
                class="flex_full"
                :content="
                  (record.serviceContentStr || '') +
                  (record.serviceContentOther
                    ? '-' + record.serviceContentOther
                    : '')
                "
              />
            </div>
          </template>
          <template v-if="column.key === 'checkState'">
            <!-- <span v-if="record.checkState=='SHI'">是</span>
            <span v-if="record.checkState=='FOU'">否</span> -->
            {{ checkState(record.checkState) }}
          </template>
          <template v-if="column.key === 'attributorUser'">
            <!-- {{ record.attributorUser ? record.attributorUser.realname : "--" }} -->
            <UserBox :data="record.attributorUser || {}" single />
          </template>
          <template v-if="column.key === 'principal'">
            <!-- {{ record.principal ? record.principal.realname : "--" }} -->
            <UserBox :data="record.principal || {}" single />
          </template>
          <template v-if="column.key === 'serviceEndTime'">
            {{ dateFormat(record.serviceEndTime, "YYYY-MM-DD") }}
          </template>
          <template v-if="column.key === 'action'">
            <actionBtn :actions="actions(record, index)" />
            <!-- <a-button type="text" @click="lookover(record, index)"查看</a-button> -->
          </template>
        </template>
      </FullTable>
      <div class="pagination_box">
        <a-pagination
          showSizeChanger
          showQuickJumper
          v-model:current="filterForm.pageNo"
          v-model:pageSize="filterForm.pageSize"
          :show-total="(total) => `共${total} 条数据`"
          size="small"
          @change="getPage"
          @showSizeChange="filterForm.pageNo = 1"
          :total="data.total"
        ></a-pagination>
      </div>
    </div>
  </div>
</template>
<script>
import api from "@/api/index";
import { ref, reactive, onMounted, onActivated } from "vue";
import inspectionFilter from "./components/inspectionFilter.vue";
export default {
  components: { inspectionFilter },
  setup() {
    const router = useRouter();
    const loadding = ref(false);
    const data = reactive({
      list: [],
      columns: [
        {
          title: "归属单位",
          width: 200,
          key: "companyName",
          dataIndex: "companyName",
        },
        {
          title: "项目编号",
          width: 150,
          key: "projectNo",
        },
        {
          title: "项目名称",
          width: 200,
          key: "projectName",
        },
        {
          title: "是否已承接查验",
          width: 150,
          key: "checkState",
        },
        {
          title: "项目优先级",
          width: 150,
          dataIndex: "projectLevelStr",
        },
        {
          title: "拓展模式",
          width: 150,
          dataIndex: "expansionModeStr",
        },
        {
          title: "业态",
          width: 120,
          dataIndex: "businessTypeStr",
        },
        {
          title: "服务内容",
          width: 180,
          key: "serviceContent",
        },
        {
          title: "拓后负责人",
          width: 120,
          key: "principal",
        },
        {
          title: "项目状态",
          width: 120,
          key: "serviceStatus",
        },
        {
          title: "合同到期日期",
          width: 140,
          key: "serviceEndTime",
        },
        {
          title: "所属大区",
          width: 200,
          dataIndex: "regionName",
        },
        {
          title: "项目归属人",
          width: 200,
          key: "attributorUser",
        },
        {
          title: "运营团队",
          width: 150,
          dataIndex: "operationTeamName",
        },
        {
          title: "是否为续签项目",
          width: 150,
          dataIndex: "inStockStr",
        },
        {
          title: "省份",
          width: 120,
          dataIndex: "provinceName",
        },
        {
          title: "城市",
          width: 120,
          dataIndex: "cityName",
        },
        {
          title: "区/县",
          width: 120,
          dataIndex: "areaName",
        },
        {
          title: "街道",
          width: 120,
          dataIndex: "streetName",
        },
        {
          title: "操作",
          width: 100,
          key: "action",
          fixed: "right",
        },
      ],
    });
    const filterForm = reactive({
      pageNo: 1,
      pageSize: 10,
    });
    const lookover = (record, index) => {
      console.log("record", record);
      // console.log('index',index)
      // router.push('/index')
    };
    const actions = (record, index) => {
      return [
        {
          text: "查看",
          show: true,
          click: () => {
            router.push('/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&show=' + record.show + '&to=thcy');
          },
        },
      ];
    };
    const getPage = () => {
      let filterData = {
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize,
        // desc: ["createTime"],
        asc: ['serviceEndTime'],
        params: {},
        geParams: {},
        leParams: {},
        inParams: {},
        likeParams: {},
      };
      if (filterForm.moreFilter) {
        let params = [
          "projectLevel",
          "serviceStatus",
          "expansionMode",
          // "businessType",
          // "serviceContent",
          // "serviceEndTime",
          "checkState",
          "provinceCode",
          "cityCode",
          "areaCode",
          "streetCode",
          // "regionId",
          // "companyId",
          // "operationTeamId",
          "principalId",
          "attributorUserId",
        ];
        let likeParams = ["projectNo", "projectName"];
        let inParams = [
          "businessType",
          "serviceContent",
          "regionId",
          "companyId",
          "operationTeamId"
        ];
        let geParams = ["serviceEndTime"];
        let leParams = ["serviceEndTime"];

        params.forEach((key) => {
          if (filterForm[key] && filterForm[key] != -1) {
            filterData.params[key] = filterForm[key];
          }
        });
        likeParams.forEach((key) => {
          if (filterForm[key]) {
            filterData.likeParams[key] = filterForm[key];
          }
        });
        inParams.forEach((key) => {
          if(filterForm['serviceContent']&&filterForm['serviceContent'].length>0){
            filterData.inParams['serviceContent'] = filterForm['serviceContent']
          }
          if(filterForm['businessType']&&filterForm['businessType'].length>0) {
            filterData.inParams['businessType'] = filterForm['businessType']
          }
          if (filterForm[key] && filterForm[key].length > 0) {
            filterData.inParams[key] = filterForm[key];
          }
        });
        geParams.forEach((key) => {
          if (filterForm[key]) {
            filterData.geParams[key] = filterForm[key] + " 00:00:00";
          }
        });
        leParams.forEach((key) => {
          if (filterForm[key]) {
            filterData.leParams[key] = filterForm[key] + " 23:59:59";
          }
        });
      }
      if (!filterForm.moreFilter && filterForm.projectName) {
        filterData.likeParams.projectName = filterForm.projectName;
      }
      if (!filterForm.moreFilter && filterForm.checkState) {
        filterData.params.checkState = filterForm.checkState;
      }
      if (filterForm.year) {
        filterData.geParams.performanceConfirmTime = filterForm.year + "-01-01 00:00:00";
        filterData.leParams.performanceConfirmTime = filterForm.year + "-12-31 23:59:59";
      }

      loadding.value = true;
      api.project.projectCheckPage(filterData).then((res) => {
        if (res.code == 200) {
          data.list = res.data.records;
          data.total = res.data.total;
        }
        loadding.value = false;
      });
    };
    const checkState = (val) => {
      if (val === "SHI") {
        return "是";
      }
      if (val === "FOU") {
        return "否";
      }
    };
    const filterSubmit = () => {
      filterForm.pageNo = 1;
      getPage();
    };
    onMounted(() => {
      getPage();
    });
    onActivated(() => {
      getPage();
    });
    return {
      router,
      loadding,
      data,
      filterForm,
      lookover,
      actions,
      filterSubmit,
      getPage,
      checkState,
    };
  },
};
</script>

