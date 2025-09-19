<template>
  <div class="page-common content-inner">
    <a-page-header :ghost="false" :breadcrumb="{ routes }">
      <template #footer>
        <span style="font-weight: bold; font-size: 18px">系统日志</span>
      </template>
    </a-page-header>
    <div class="menu_tab">
      <div
        class="tab_item"
        v-for="(item, index) in menuList"
        :key="index"
        :class="{ tab_item_active: tabIndex == index }"
        @click="tabChange(index)"
        v-show="item.show"
      >
        <span>
          {{ item.name }}
        </span>
      </div>
    </div>
    <div class="filter-box">
      <a-form :label-col="{ style: { width: '100px' } }" ref="filterFormRef" :model="filterForm">
        <a-row v-if="tabIndex == 0">
          <a-col :span="8">
            <a-form-item label="项目名称" name="projectName">
              <a-input allowClear v-model:value="filterForm.projectName" placeholder="请输入" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="通告类型" name="notifyType">
              <a-select
                v-model:value="filterForm.notifyType"
                class="w_full"
                placeholder="请选择"
                :options="[
                  { label: '全部', value: -1 },
                  {
                    label: '项目合同到期未及时处理',
                    value: 'XIANG_MU_HE_TONG_DAO_QI_WEI_JI_SHI_CHU_LI',
                  },
                  {
                    label: '项目未进行标书合规评审',
                    value: 'XIANG_MU_WEI_JIN_XING_BIAO_SHU_HE_GUI_PING_SHEN',
                  },
                  { label: '项目开标未及时复盘', value: 'XIANG_MU_KAI_BIAO_WEI_JI_SHI_FU_PAN' },
                ]"
              ></a-select>
            </a-form-item>
          </a-col>
          <a-col :span="10" class="text-right">
            <a-space>
              <a-button type="primary" @click="filterSubmit">查询</a-button>
              <a-button @click="reset">重置</a-button>
            </a-space>
          </a-col>
        </a-row>
        <a-row v-if="tabIndex == 1">
          <a-col :span="6">
            <a-form-item label="操作类型" name="recordType">
              <a-select
                v-model:value="filterForm.recordType"
                class="w_full"
                placeholder="请选择"
                :options="[
                  { label: '全部', value: -1 },
                  {
                    label: '项目补录',
                    value: 'XIANG_MU_BU_LU',
                  },
                  {
                    label: '项目删除',
                    value: 'PROJECT_DELETE',
                  },
                  { label: '客户删除', value: 'COMPANY_DELETE' },
                ]"
              ></a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="项目名称" name="projectName">
              <a-input allowClear v-model:value="filterForm.projectName" placeholder="请输入" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="项目编号" name="projectNo">
              <a-input allowClear v-model:value="filterForm.projectNo" placeholder="请输入" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="客户名称" name="customerName">
              <a-input allowClear v-model:value="filterForm.customerName" placeholder="请输入" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="客户编号" name="customerNo">
              <a-input allowClear v-model:value="filterForm.customerNo" placeholder="请输入" />
            </a-form-item>
          </a-col>
          <a-col :span="10" style="margin-left: 30px">
            <a-space>
              <a-button type="primary" @click="filterSubmit">查询</a-button>
              <a-button @click="reset">重置</a-button>
            </a-space>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="content-box_full">
      <FullTable :columns="data.columns[tabIndex]" :dataSource="data.list" rowKey="id"  :loadding="loadding">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'isReset' && tabIndex == 1">
           <span  v-if="record.recordType == 'PROJECT_DELETE'  || record.recordType == 'COMPANY_DELETE'">
            {{record.isReset == '0' ? '未恢复' : '已恢复'}}
           </span>
          </template>
          <template v-if="column.key === 'action' && tabIndex == 0">
            <a-button
              type="text"
              class="color-primary"
              size="small"
              @click="restore(record.id)"
              v-permission="['biz:revokeLog:restore']"
            >
              恢复
            </a-button>
          </template>
          <template v-if="column.key === 'action' && tabIndex == 1">
            <a-button
              type="text"
              class="color-primary"
              size="small"
              v-if="record.recordType == 'PROJECT_DELETE' && record.isReset == 0 || record.recordType == 'COMPANY_DELETE' && record.isReset == 0"
              @click="projectClientRecovery(record)"
              v-permission="['biz:projectClient:restore']"
            >
              恢复
            </a-button>
          </template>
        </template>
      </FullTable>
      <div class="pagination_box">
        <a-pagination
          showSizeChanger
          show-quick-jumper
          v-model:current="filterForm.pageNo"
          v-model:pageSize="filterForm.pageSize"
          :show-total="total => `共 ${total} 条数据`"
          size="small"
          @change="changePage"
          @showSizeChange="filterForm.pageNo = 1"
          :total="data.total"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import api from "@/api/index";
import { mainStore } from "@/store";
import { message, Modal } from "ant-design-vue";
const store = mainStore();
import { hasPermission } from "@/utils/tools";
const loadding = ref(false);
const routes = [
  {
    breadcrumbName: "系统管理",
  },
  {
    breadcrumbName: "系统日志",
  },
];
const menuList = [
  {
    name: "通告撤销日志",
    status: 1,
    show: hasPermission(['biz:revokeLog:list']),
    key: 0,
  },
  {
    name: "项目客户操作日志",
    status: 1,
    show: hasPermission(['biz:projectClient:list']),
    key: 1,
  },
];
onMounted(() => {
  filterForm.notifyType = -1;
  filterForm.recordType = -1;
  getPage();
});
const tabIndex = ref(0);
const tabChange = index => {
  tabIndex.value = index;
  if(index == 0){
    getPage();
  }else{
    getProjectLogPage()
  }
};

const data = reactive({
  list: [],
  columns: {
    0: [
      {
        title: "归属单位",
        dataIndex: "deptName",
        width: 250,
      },
      {
        title: "项目名称",
        dataIndex: "projectName",
        key: "projectName",
        width: 300,
      },
      {
        title: "通告类型",
        dataIndex: "notifyTypeName",
        width: 180,
      },
      {
        title: "项目负责人",
        dataIndex: "attributorUserName",
        width: 180,
      },
      {
        title: "逾期天数",
        dataIndex: "overdueTime",
        width: 100,
      },
      {
        title: "发布时间",
        dataIndex: "pushTime",
        width: 180,
      },
      {
        title: "撤销人",
        dataIndex: "updateUserName",
        width: 180,
      },
      {
        title: "操作",
        key: "action",
        width: 100,
        align: "center",
        fixed: "right",
      },
    ],
    1: [
      {
        title: "操作类型",
        dataIndex: "recordTypeName",
        width: 180,
      },
      {
        title: "项目编号/统一社会信用代码",
        dataIndex: "recordCode",
        width: 220,
      },
      {
        title: "项目名称/客户名称",
        dataIndex: "recordName",
        width: 300,
      },
      {
        title: "操作人",
        dataIndex : ['createUser','realname'],
        width: 180,
      },
      {
        title: "操作日期",
        dataIndex: "createTime",
        width: 180,
      },
      {
        title: "是否恢复",
        key: "isReset",
        width: 120,
      },
      {
        title: "操作",
        key: "action",
        width: 100,
        align: "center",
        fixed: "right",
      },
    ],
  },
  total: 0,
});

const getPage = () => {
  let postData = builderFilter();
  loadding.value = true;
  api.projectNofityLog.pageNotifyLog(postData).then(res => {
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
const getProjectLogPage = ()=>{
  let postData = builderFilter();
  loadding.value = true;
  api.projectNofityLog.projectLogPage(postData).then(res => {
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
}
const builderFilter = () => {
  let postData = {
    desc: ["createTime"],
    pageNo: filterForm.pageNo,
    pageSize: filterForm.pageSize,
    params: {},
    likeParams: {},
  };
  let likeParams = ["projectName","projectNo","customerName","customerNo"];
  likeParams.forEach(key => {
    if (filterForm[key]) {
      if(tabIndex.value == 1){
        if(key == 'projectName' || key == 'customerName'){
          postData.likeParams.recordName = filterForm[key];
        }
        if (key == 'projectNo' || key == 'customerNo') {
          postData.likeParams.recordCode = filterForm[key];
        }
      }else{
        postData.likeParams[key] = filterForm[key];
      }
    }
  });
  let params = ["notifyType","recordType"];
 
    params.forEach(key => {
      if (filterForm[key] != -1) {
        postData.params[key] = filterForm[key];
      }
    });
  return postData;
};
const changePage = ()=>{
  if (tabIndex.value ==0) {
    getPage();
  }else{
    getProjectLogPage()
  }
}
const filterForm = reactive({
  pageNo: 1,
  pageSize: 10,
});

const filterFormRef = ref(null);
const filterSubmit = () => {
  filterForm.pageNo = 1;
  if (tabIndex.value ==0) {
    getPage();
  }else{
    getProjectLogPage()
  }
 
};
const reset = () => {
  filterFormRef.value.resetFields();
  filterForm.notifyType = -1;
  filterForm.recordType = -1;
  filterSubmit();
};

const restore = async id => {
  Modal.confirm({
    title: "恢复",
    content: "是否确认恢复项目？",
    onOk() {
      store.spinChange(1);
      api.projectNofityLog.deleteNotifyLog(id).then(res => {
        store.spinChange(-1);
        if (res.code == 200) {
          message.success(res.msg);
          getPage();
        } else {
          message.error(res.msg);
        }
      });
    },
  });
};

const projectClientRecovery = async row => {
  Modal.confirm({
    title: "恢复",
    content: `是否确认恢复该${row.recordType == 'PROJECT_DELETE' ? '项目' : '客户'}？`,
    onOk() {
      store.spinChange(1);
      api.projectNofityLog.projectLogReset({id:row.id}).then(res => {
        store.spinChange(-1);
        if (res.code == 200) {
          message.success(res.msg);
          getProjectLogPage();
        }
      });
    },
  });
};

</script>
<style scoped lang="less">
.menu_tab {
  display: flex;
  .tab_item {
    font-size: 16px;
    padding: 6px 16px;
    border-radius: 6px 6px 0 0;
    background-color: #fffaf0;
    color: @primary-color;
    margin-right: 4px;
    cursor: pointer;
    box-shadow: 0 -4px 4px rgba(249, 156, 52, 0.1) inset;
    position: relative;
    overflow: hidden;
    span {
      z-index: 2;
      position: relative;
      transition: all 0.3s;
    }
    &:hover {
      opacity: 0.85;
    }
    &::after {
      content: "";
      height: 0;
      width: 100%;
      background-color: @primary-color;
      border-radius: 1px;
      position: absolute;
      bottom: 0;
      left: 0;
      transition: all 0.3s;
      z-index: 1;
    }
  }
  .tab_item_active {
    span {
      color: #fff;
    }
    &::after {
      height: 100%;
    }
  }
  .tab_item_disabled {
    cursor: not-allowed;
    background-color: #ccc;
    color: #555;

    &:hover {
      opacity: 1;
    }
  }
}
</style>
