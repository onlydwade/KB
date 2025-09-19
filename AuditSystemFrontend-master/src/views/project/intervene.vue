<template>
  <div class="content-inner">
    <div class="bread_box">
      <a-breadcrumb>
        <a-breadcrumb-item>拓后运营</a-breadcrumb-item>
        <a-breadcrumb-item>项目干预</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <interveneFilter v-model="filterForm" @submit="filterSubmit" />
    <div class="content-box_full">
      <Title title="项目干预列表"></Title>
      <FullTable
        :loadding="loadding"
        :dataSource="data.list"
        :columns="data.columns"
      >
        <template #bodyCell="{ record, column, index }">
          <template v-if="column.key === 'projectNo'">
            <router-link
              :to="'/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&to=thgy'+ '&show=' + record.show"
              class="color-link"
            >
              {{ record.projectNo }}
            </router-link>
          </template>
          <template v-if="column.key === 'projectName'">
            <router-link
              :to="'/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&to=thgy'+ '&show=' + record.show"
              class="color-link"
            >
              <EllipsisTooltip
                class="flex_full"
                :content="record.projectName"
              />
            </router-link>
          </template>
          <template v-if="column.key === 'transmitSchemeTime'">
            {{ dateFormat(record.transmitSchemeTime, "YYYY-MM-DD") }}
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
          <template v-if="column.key === 'isNotTransmitInterveneScheme'">
            {{ interventionPlan(record.isNotTransmitInterveneScheme) }}
          </template>
          <template v-if="column.key === 'interveneState'">
            {{ executionStatus(record.interveneState) }}
          </template>
          <template v-if="column.key === 'transmitSchemeUser'">
            <UserBox :data="record.transmitSchemeUser || {}" single />
          </template>
          <template v-if="column.key === 'attributorUser'">
            <UserBox :data="record.attributorUser || {}" single />
          </template>
          <template v-if="column.key === 'principal'">
            <UserBox :data="record.principal || {}" single />
          </template>
          <template v-if="column.key === 'action'">
            <actionBtn :actions="actions(record, index)"></actionBtn>
          </template>
        </template>
      </FullTable>
      <div class="pagination_box">
        <a-pagination
          showSizeChanger
          showQuickJumper
          v-model:current="filterForm.pageNo"
          v-model:pageSize="filterForm.pageSize"
          :show-total="(total) => `共${total}条数据`"
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
import interveneFilter from "./components/interveneFilter.vue";
export default {
  components: { interveneFilter },
  setup() {
    const router = useRouter();
    const loadding = ref(false);
    const data = reactive({
      list: [],
      columns: [
        {
          title: "归属单位",
          dataIndex: "companyName",
          width: 200,
        },
        {
          title: "项目编号",
          key: "projectNo",
          width: 150,
        },
        {
          title: "项目名称",
          key: "projectName",
          width: 150,
        },
        {
          title: "是否已下达干预方案",
          key: "isNotTransmitInterveneScheme",
          width: 150,
        },
        {
          title: "最新干预方案名称",
          dataIndex: "interveneSchemeName",
          width: 150,
        },
        {
          title: "最新方案下达人",
          key: "transmitSchemeUser",
          width: 150,
        },
        {
          title: "最新方案下达日期",
          key: "transmitSchemeTime",
          width: 140,
        },
        {
          title: "最新方案执行状态",
          key: "interveneState",
          width: 150,
        },
        {
          title: "项目优先级",
          dataIndex: "projectLevelStr",
          width: 150,
        },
        {
          title: "拓展模式",
          dataIndex: "expansionModeStr",
          width: 150,
        },
        {
          title: "业态",
          dataIndex: "businessTypeStr",
          width: 120,
        },
        {
          title: "服务内容",
          key: "serviceContent",
          width: 180,
        },
        {
          title: "拓后负责人",
          key: "principal",
          width: 120,
        },
        {
          title: "项目状态",
          key: "serviceStatus",
          width: 120,
        },
        {
          title: "所属大区",
          dataIndex: "regionName",
          width: 200,
        },
        {
          title: "项目归属人",
          key: "attributorUser",
          width: 200,
        },
        {
          title: "运营团队",
          dataIndex: "operationTeamName",
          width: 150,
        },
        {
          title: "是否为续签项目",
          dataIndex: "inStockStr",
          width: 150,
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
          title: "操作",
          key: "action",
          width: 100,
          fixed: "right",
        },
      ],
    });
    const filterForm = reactive({
      pageNo: 1,
      pageSize: 10,
    });
    const actions = (record, index) => {
      return [
        {
          text: "查看",
          show: true,
          click: () => {
            router.push('/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&show=' + record.show + '&to=thgy');
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
          "isNotTransmitInterveneScheme",
          "interveneState",
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
        let inParams = ["businessType", "serviceContent","regionId","companyId","operationTeamId"];

        params.forEach((key) => {
          if (filterForm['interveneState'] && filterForm['interveneState'] != -1) {
            filterData.params['intervene.interveneState'] = filterForm['interveneState'];
          }
          if (filterForm[key] && filterForm[key] != -1&& key != 'interveneState') {
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
          if(filterForm[key]&&filterForm[key].length>0) {
            filterData.inParams[key] = filterForm[key]
          }
        })
      }
      if (!filterForm.moreFilter && filterForm.projectName) {
        filterData.likeParams.projectName = filterForm.projectName;
      }
      if (!filterForm.moreFilter && filterForm.isNotTransmitInterveneScheme) {
        filterData.params.isNotTransmitInterveneScheme =
          filterForm.isNotTransmitInterveneScheme;
      }
      if (filterForm.year) {
        filterData.geParams.performanceConfirmTime = filterForm.year + "-01-01 00:00:00";
        filterData.leParams.performanceConfirmTime = filterForm.year + "-12-31 23:59:59";
      }

      loadding.value = true;
      api.project.projectInterveneListPage(filterData).then((res) => {
        if (res.code == 200) {
          data.list = res.data.records;
          data.total = res.data.total;
        }
      });
      loadding.value = false;
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
    const interventionPlan = (val) => {
      if (val === 'FOU') {
        return "否";
      }
      if (val === 'SHI') {
        return "是";
      }
    };
    const executionStatus = (val) => {
      if (val === 0) {
        return "干预待执行";
      }
      if (val === 1) {
        return "待检查";
      }
      if (val === 2) {
        return "执行不通过";
      }
      if (val === 3) {
        return "已完成";
      }
    };
    return {
      router,
      loadding,
      data,
      filterForm,
      actions,
      filterSubmit,
      getPage,
      interventionPlan,
      executionStatus,
    };
  },
};
</script>


