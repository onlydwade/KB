<template>
  <a-modal
    v-model:visible="visible"
    title="客户删除"
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
          <a-col :span="7" class="more_col">
            <a-form-item label="客户全称" name="customerName">
              <a-input allowClear v-model:value="filterForm.customerName" placeholder="请输入" />
            </a-form-item>
          </a-col>
          <a-col :span="7" class="more_col">
            <a-form-item label="客户编码" name="customerNo">
              <a-input allowClear v-model:value="filterForm.customerNo" placeholder="请输入" />
            </a-form-item>
          </a-col>
          <a-col :span="6" class="more_col">
            <a-form-item label="客户类型" name="customerType">
              <a-select
                v-model:value="filterForm.customerType"
                class="w_full"
                placeholder="请选择"
                :options="dict.optionsAll('KE_HU_FEN_LEI')"
              ></a-select>
            </a-form-item>
          </a-col>
          <a-col :span="4" class="more_col">
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
        <template v-if="column.key === 'customerNo'">
          <router-link  target="_blank" :to="'/innerPage/customerInfo?id=' + record.id" class="color-link">
            {{ record.customerNo }}
          </router-link>
        </template>
        <template v-if="column.key === 'followUser'">
          <UserBox :data="record.followUserVO || {}" single />
        </template>
        <template v-if="column.key === 'maintenanceUser'">
          <UserBox :data="record.maintenanceUserVO || {}" single />
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
import { useDictStore } from "@/store/dict";
import { message, Modal } from "ant-design-vue";
const dict = useDictStore();
const emit = defineEmits(["success"]);
const visible = ref(false);
const formRef = ref(null);
const disabled = ref(true);
const loadding = ref(false);

const data = reactive({
  list: [],
  columns : [
        {
            title     : '客户编码',
            key       : 'customerNo',
            width     : 170,
            fixed     : "left",
        },
        {
            title     : '客户全称',
            dataIndex : 'customerName',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '跟进类型',
            dataIndex : 'customerLevelStr',
            width     : 120,
        },
        {
            title     : '客户类型',
            dataIndex : 'customerTypeStr',
            width     : 120,
        },
        {
            title     : '企业类型',
            dataIndex : 'companyTypeStr',
            width     : 120,
        },
        {
            title     : '合作类型',
            dataIndex : 'cooperationTypeStr',
            width     : 120,
        },
        {
            title     : '所属行业',
            dataIndex : 'customerIndustryStr',
            width     : 120,
        },
        {
            title     : '客户标签',
            dataIndex : 'keywords',
            width     : 150,
            ellipsis  : true,
        },
        {
            title     : '统一社会信用代码',
            dataIndex : 'customerCompanyNo',
            width     : 200,
        },
    ],
  total: 0,
});
const filterForm = reactive({
  pageNo: 1,
  pageSize: 10,
  serviceStatus: ["WEI_JIE_XIANG"],
});
const open = () => {
  visible.value = true;
};
/* 监听visible变化 清空batchIds*/
watch(
  () => visible.value,
  (newVal, oldVal) => {
    batchIds.value = [];
  }
);
const filterSubmit = () => {
  filterForm.pageNo = 1;
  getPage();
};
const reset = () => {
  formRef.value.resetFields();
  filterSubmit();
};
const handleDelete = () => {
  if(batchIds.value.length == 0) return message.warning("请先选择要删除的客户");
  Modal.confirm({
    title: "操作确认",
    content: "是否确认删除选中的客户？",
    async onOk() {
      api.customer.deleteCustomer({ids: batchIds.value}).then(res => {
        if (res.code == 200) {
          message.success("删除成功");
          batchIds.value = [];
          emit("success");
          reset();
        }
      });
    },
  });
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
    "followUserId",
    "maintenanceUserId",
    "customerType",
    "customerLevel",
    "customerIndustry",
    "provinceCode",
    "cityCode",
    "areaCode",
    "cooperationType",
  ];
  let likeParams = ["customerName", "customerNo", "address", "keywords"];
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

  return postData;
};
const getPage = () => {
  let postData = builderFilter();
  loadding.value = true;
  api.customer.customerPage(postData).then(res => {
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
