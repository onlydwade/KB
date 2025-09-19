<template>
  <a-modal
    v-model:visible="visible"
    title="项目删除"
    destroyOnClose
    :width="1400"
    style="z-index: 10000"
  >
    <template #footer>
      <a-button @click="visible = false">取消</a-button>
      <a-button class="bnt-delete" @click="handleDelete">删除</a-button>
    </template>
    <div class="filter-box">
      <a-form :label-col="{ style: { width: '110px' } }" ref="formRef" :model="filterForm">
        <a-row :class="{ show_more_col: true }">
          <a-col :span="8" class="more_col">
            <a-form-item label="项目名称" name="projectName">
              <a-input allowClear v-model:value="filterForm.projectName" placeholder="请输入" />
            </a-form-item>
          </a-col>
          <a-col :span="8" class="more_col">
            <a-form-item label="项目编号" name="projectNo">
              <a-input allowClear v-model:value="filterForm.projectNo" placeholder="请输入" />
            </a-form-item>
          </a-col>

          <a-col :span="8" class="more_col">
            <a-form-item label="项目状态" name="serviceStatus">
              <a-select
                mode="multiple"
                max-tag-count="responsive"
                v-model:value="filterForm.serviceStatus"
                class="w_full"
                placeholder="请选择"
                allowClear
                :options="serviceStatusOptions"
              ></a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8" class="more_col">
            <a-form-item label="关联客户" name="customerId">
              <DataSelect
                v-model="filterForm.customerId"
                modeName="customer"
                :options="{
                  key: 'id',
                  label: 'customerName',
                }"
              />
            </a-form-item>
          </a-col>
          <a-col :span="8" class="more_col">
            <a-button
              type="primary"
              @click="filterSubmit"
              style="margin-left: 40px; margin-right: 10px"
            >
              查询
            </a-button>
            <a-button @click="reset">重置</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <a-table
      :loadding="loadding"
      rowKey="id"
      :columns="data.columns"
      :dataSource="data.list"
      :row-selection="rowSelection"
      :scroll="{ x: '100%', y: 400 }"
      :pagination="false"
    >
      <template #bodyCell="{ column, text, record, index }">
        <template v-if="column.key === 'projectNo'">
          <router-link
            target="_blank"
            :to="'/innerPage/projectInfo?id=' + record.id"
            class="color-link"
          >
            {{ record.projectNo }}
          </router-link>
        </template>
        <template v-if="column.key === 'projectName'">
          <EllipsisTooltip class="flex_full" :content="record.projectName" />
        </template>
        <template v-if="column.key === 'serviceStatus'">
          <projectStatus :projectStatus="record.serviceStatus" />
        </template>
        <template v-if="column.key === 'customerName'">
          <router-link
            v-if="record.customer"
            :to="'/innerPage/customerInfo?id=' + record.customerId"
            class="color-link"
          >
            {{ record.customer.name || "-" }}
          </router-link>
          <span v-else>-</span>
        </template>
        <template v-if="column.key === 'sourceName'">
          <router-link
            v-if="record.relevanceProjectId != null"
            :to="'/innerPage/projectInfo?id=' + record.relevanceProjectId"
            class="color-link"
          >
            {{ record.sourceName }}
          </router-link>
          <span v-else>{{ record.sourceName || "-" }}</span>
        </template>

        <template v-if="column.key === 'stepName'">
          <span>{{ record.stepName || "-" }}</span>
        </template>

        <template v-if="column.key === 'serviceContent'">
          <div class="flex_box">
            <EllipsisTooltip
              class="flex_full"
              :content="
                (record.serviceContentStr || '') +
                (record.serviceContentOther ? ',' + record.serviceContentOther : '')
              "
            />
          </div>
        </template>
        <template v-if="column.key === 'bidedStr'">
          <span v-if="record.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">-</span>
          <span v-else>{{ record.bidedStr }}</span>
        </template>
        <template v-if="column.key === 'cooperationTypeStr'">
          <span>{{ record.cooperationTypeStr }}</span>
          <span
            v-if="(record.cooperationType || '').includes('QI_TA') && record.cooperationTypeOther"
          >
            -{{ record.cooperationTypeOther }}
          </span>
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
    </a-table>
    <div class="pagination_box" style="margin-top: 10px">
      <a-pagination
        showSizeChanger
        show-quick-jumper
        v-model:current="filterForm.pageNo"
        v-model:pageSize="filterForm.pageSize"
        :show-total="total => `共 ${total} 条数据`"
        size="small"
        @change="getPage"
        @showSizeChange="filterForm.pageNo = 1"
        :total="data.total"
      />
    </div>
  </a-modal>
</template>

<script setup>
import api from "@/api/index";
import { amountUnit, parseFormatNum, dataToFile } from "@/utils/tools";
import { ref } from "vue";
import { message, Modal } from "ant-design-vue";
import { useDictStore } from "@/store/dict";
const dict = useDictStore();
const emit = defineEmits(["success"]);
const visible = ref(false);
const formRef = ref(null);
const disabled = ref(true);
const loadding = ref(false);
//项目第一项状态
const serviceStatusOptions = computed(() => {
  let arr = [
    {
      label: "全部未结项",
      value: "WEI_JIE_XIANG",
    },
  ];
  let newArr = getServiceStatusDic() || [];
  return [...arr, ...newArr];
});
// 补上从枚举读取的状态
const getServiceStatusDic = () => {
  let dictData = dict.options("XIANG_MU_ZHUANG_TAI");
  return dictData.map(item => ({
    label: item.label,
    value: item.value,
  }));
};

const data = reactive({
  list: [],
  columns: [
    {
      title: "项目名称",
      key: "projectName",
      width: 300,
      fixed: "left",
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
      title: "是否为续签项目",
      dataIndex: "inStockStr",
      width: 140,
    },
    {
      title: "业态",
      key: "businessTypeStr",
      width: 150,
    },
    {
      title: "服务内容",
      key: "serviceContent",
      width: 180,
    },
  ],
  total: 0,
});
const filterForm = reactive({
  pageNo: 1,
  pageSize: 10,
  serviceStatus: [],
});
/* 监听visible变化 清空batchIds*/
watch(
  () => visible.value,
  (newVal, oldVal) => {
    batchIds.value = [];
  }
);
const open = () => {
  visible.value = true;
};
const filterSubmit = () => {
  filterForm.pageNo = 1;
  getPage();
};
const reset = () => {
  formRef.value.resetFields();
  filterSubmit();
};

//批量操作
const batchIds = ref([]);
const rowSelection = ref({
  checkStrictly: false,
  selectedRowKeys: batchIds,
  onChange: (selectedRowKeys, selectedRows) => {
    batchIds.value = selectedRowKeys;
  },
});
const handleDelete = () => {
  if(batchIds.value.length == 0) return message.warning("请先选择要删除的项目");
  Modal.confirm({
    title: "操作确认",
    content: "是否确认删除选中的项目？",
    async onOk() {
      api.project.deleteProject(batchIds.value).then(res => {
        if (res.code == 200) {
          message.success("删除成功");
          batchIds.value = [];
          reset()
          emit("success");
        }
      });
    },
  });
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
    "isRenewalTender",
  ];
  let likeParams = ["sourceName", "projectName", "projectNo"];
  let inParams = [
    "serviceStatus",
    "expandServiceStatus",
    "businessType",
    "companyId",
    "expandCompanyId",
    "expansionMode",
    "projectType",
    "finishStepId",
  ];
  params.forEach(key => {
    if (filterForm[key] && filterForm[key] != -1) {
      postData.params[key] = filterForm[key];
    }
  });
  likeParams.forEach(key => {
    if (filterForm[key]) {
      postData.likeParams[key] = filterForm[key];
    }
  });
  inParams.forEach(key => {
    if (filterForm["businessType"] && filterForm["businessType"].length) {
      postData.inParams["businessType"] = filterForm["businessType"];
    }

    if (filterForm[key] && filterForm[key].length > 0) {
      postData.inParams[key] = filterForm[key];
    }
  });
  return postData;
};
const getPage = () => {
  let postData = builderFilter();
  loadding.value = true;
  api.project.projectPageAll(postData).then(res => {
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
onMounted(() => {
  getPage();
});
defineExpose({ open });
</script>
<style scoped lang="less">
.bnt-delete {
  background: #f5222d;
  color: #fff;
}
</style>
