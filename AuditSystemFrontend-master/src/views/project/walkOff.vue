<template>
  <div class="content-inner">
    <div class="bread_box">
      <a-breadcrumb>
        <a-breadcrumb-item>拓后运营</a-breadcrumb-item>
        <a-breadcrumb-item>退场管理</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <walkOffFilter v-model="filterForm" @submit="filterSubmit" />
    <div class="content-box_full">
      <Title title="项目退场列表" />
      <FullTable
        :loadding="loadding"
        :dataSource="data.list"
        :columns="data.columns"
      >
        <template #bodyCell="{ record, column, index }">
          <template v-if="column.key === 'projectNo'">
            <router-link
              :to="'/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&to=thtc' +'&show=' + record.show"
              class="color-link"
              >{{ record.projectNo }}</router-link
            >
          </template>
          <template v-if="column.key === 'projectName'">
            <router-link
              :to="'/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&to=thtc'+'&show=' + record.show"
              class="color-link"
            >
              <EllipsisTooltip
                class="flex_full"
                :content="record.projectName"
              />
            </router-link>
          </template>
          <template v-if="column.key === 'serviceStatus'">
            <projectStatus :projectStatus="record.serviceStatus" />
          </template>
          <template v-if="column.key === 'exitDescription'">
            <EllipsisTooltip
                  class="flex_full"
                  :content="
                  record.exitDescription
                  "
                />
          </template>
          <template v-if="column.key === 'approvalProcessOA'">
            <EllipsisTooltip
                class="color-link"
                @click="toOaLink(record.approvalProcessOA)"
                :content="
                 record.approvalProcessOA
                "
              />
          </template>
          <template v-if="column.key === 'processState'">
            {{ processState(record.processState) }}
          </template>
          <template v-if="column.key === 'processMode'">
            {{ processMode(record.processMode) }}
          </template>
          <template v-if="column.key === 'serviceEndTime'">
            {{ dateFormat(record.serviceEndTime, "YYYY-MM-DD") }}
          </template>
          <template v-if="column.key === 'contractExpireTime'">
            <span v-if="record.processState ==2 || record.processMode == 1 || record.processMode == 2 || record.processMode == 3"></span>
            <span v-else>
              <ExpireTimes :expireTime="record.contractExpireTime" :contractIsNotExpire="record.contractIsNotExpire" />
            </span>
          </template>
          <template v-if="column.key === 'disposeTime'">
            {{ dateFormat(record.disposeTime, "YYYY-MM-DD") }}
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
          <template v-if="column.key === 'attributorUser'">
            <UserBox :data="record.attributorUser || {}" single />
          </template>
          <template v-if="column.key === 'principal'">
            <UserBox :data="record.principal || {}" single />
          </template>
          <template v-if="column.key === 'updateUser'">
            <UserBox :data="record.updateUser || {}" single />
          </template>
          <template v-if="column.key === 'action'">
            <actionBtn :actions="actions(record, index)" />
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
import { onActivated, onMounted, reactive, ref } from "vue";
import walkOffFilter from "./components/walkOffFilter.vue";
import { mainStore } from '@/store'
export default {
  components: { walkOffFilter },
  setup() {
    const router = useRouter();
    const store = mainStore()
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
          title: "合同到期倒计时",
          width: 150,
          key: "contractExpireTime",
        },
        {
          title: "处理状态",
          width: 120,
          key: "processState",
        },
        {
          title: "处理方式",
          width: 100,
          key: "processMode",
        },
        {
          title: "处理日期",
          width: 120,
          key: "disposeTime",
        },
        {
          title: "处理人",
          width: 120,
          dataIndex: 'processPerson',
        },
        {
          title: "退场描述",
          width: 200,
          key: "exitDescription",
        },
        {
          title: "对应OA审批流程",
          width: 300,
          key: "approvalProcessOA",
        },
        {
          title: "项目优先级",
          width: 120,
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
          title: "所属大区",
          width: 200,
          dataIndex: "regionName",
        },
        {
          title: "项目归属人",
          width: 120,
          key: "attributorUser",
        },
        {
          title: "运营团队",
          width: 150,
          dataIndex: "operationTeamName",
        },
        {
          title: "是否为续签项目",
          width: 130,
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
    const actions = (record, index) => {
      return [
        {
          text: "处理",
          show: true,
          click: () => {
            router.push('/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&show=' + record.show + '&to=thtc');
          },
        },
      ];
    };
    const toOaLink = (link)=>{
        api.common.getSsoToken({
            mobile:store.userInfo.phonenumber
        }).then(res=>{
            if(res.code==200 && res.data){
                window.open(link+'&access_token='+res.data);
            }
        })
    }
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
          "processState",
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
        let geParams = ["serviceEndTime"];
        let leParams = ["serviceEndTime"];

        params.forEach((key) => {
          if (filterForm[key] && filterForm[key] != -1 && key !='processState') {
            filterData.params[key] = filterForm[key];
          }
          if (filterForm['processState'] && filterForm['processState'] != -1) {
            filterData.params['extension.processState'] = filterForm['processState'];
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
          if (filterForm["businessType"] && filterForm["businessType"].length > 0) {
            filterData.inParams["businessType"] = filterForm["businessType"];
          }
          if(filterForm[key]&&filterForm[key].length>0) {
            filterData.inParams[key] = filterForm[key]
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
      if (!filterForm.moreFilter && filterForm.processState) {
        filterData.params.processState = filterForm.processState;
      }
      if (filterForm.year) {
        filterData.geParams.performanceConfirmTime = filterForm.year + "-01-01 00:00:00";
        filterData.leParams.performanceConfirmTime = filterForm.year + "-12-31 23:59:59";
      }

      loadding.value = true;
      api.project.projectExtensionExitListPage(filterData).then((res) => {
        if (res.code == 200) {
          data.list = res.data.records;
          data.total = res.data.total;
        }
        loadding.value = false;
      });
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
    const processState = (val) => {
      if (val === 0) {
        return "未处理";
      }
      if (val === 1) {
        return "审批中";
      }
      if (val === 2) {
        return "已处理";
      }
      if (val === 3) {
        return "处理被驳回";
      }
    };
    const processMode = (val) => {
      if (val === 0) {
        return "未续签";
      }
      if (val === 1) {
        return "续签";
      }
      if (val === 2) {
        return "重新投标";
      }
      if (val === 3) {
        return "退场";
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
      processState,
      processMode,
      toOaLink
    };
  },
};
</script>

