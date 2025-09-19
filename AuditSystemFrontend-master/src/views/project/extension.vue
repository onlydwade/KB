<template>
  <div class="page-project content-inner">
    <div class="bread_box">
      <a-breadcrumb>
        <a-breadcrumb-item><a href="#/index">首页</a></a-breadcrumb-item>
        <a-breadcrumb-item>项目库</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <PageFilter v-model="filterForm" @submit="filterSubmit" @dataExport="dataExport" />
    <div class="content-box_full">
      <Title title="在管项目列表">
        <!-- <template #right>
          <a-space>
            <NodeConfig />
            <a-button type="primary" @click="router.push('/innerPage/projectEdit')" v-permission="['biz:project:add']">
              <template #icon>
                <plus-outlined />
              </template>
              新建项目
            </a-button>
          </a-space>
        </template> -->
      </Title>
      <div>
        <a-row :gutter="24" style="margin-top: 12px;">
          <a-col :span="12">
            <div class="dashboard_inner">
              <div class="container_main" style="background-color: rgba(255, 250, 240, 1);">
                <div class="container_font">
                  <div class="numValue">拓后单一项目</div>
                  <div class="numValue" style="text-align: center;margin-top: 5px;">{{ formData.extensionProjectTotal || 0
                  }} 个</div>
                </div>
                <div class="container_font2">
                  <div class="titleValue">
                    <span>已承接查验：</span> <span class="pl26" @click="screenPage('checkState', 'SHI')">
                      {{ formData.checkTotal || 0 }} 个</span>
                    <span>已评估：</span> <span class="pl26" @click="screenPage('assessIsNot', 'SHI')">
                      {{ formData.assessTotal || 0 }} 个</span>
                    <span>已退场：</span> <span class="pl26" @click="screenPage('extension.processMode', '3')">
                      {{ formData.exitTotal || 0 }} 个</span>
                  </div>
                </div>
              </div>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="dashboard_inner">
              <div class="container_main" style="background-color: rgba(255, 250, 240, 1);">
                <div class="container_font">
                  <div class="numValue">续签或重新投标</div>
                  <div class="numValue" style="text-align: center;margin-top: 5px;">
                    {{ formData.renewOrNewBidTotal || 0 }} 个</div>
                </div>
                <div class="container_font2">
                  <div class="titleValue">
                    <span>已续签：</span> <span class="pl26" @click="screenPage('extension.processMode', '1')">
                      {{ formData.renewTotal || 0 }} 个
                    </span>
                    <span>已重新投标：</span> <span class="pl26" @click="screenPage('extension.processMode', '2')">
                      {{ formData.newBidTotal || 0 }} 个
                    </span>
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
            <router-link
              :to="'/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&show=' + record.principalShow"
              class="color-link">
              {{ record.projectNo }}
            </router-link>
          </template>
          <template v-if="column.key === 'projectName'">
            <router-link
              :to="'/innerPage/extensionInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&show=' + record.principalShow"
              class="color-link">
              <EllipsisTooltip class="flex_full" :content="record.projectName" />
            </router-link>
          </template>
          <template v-if="column.key === 'serviceStatus'">
            <projectStatus :projectStatus="record.serviceStatus" />
          </template>
          <template v-if="column.key === 'customerName'">
            <router-link v-if="record.customer" :to="'/innerPage/customerInfo?id=' + record.customerId"
              class="color-link">
              {{ record.customer.name || '-' }}
            </router-link>
            <span v-else>-</span>
          </template>
          <template v-if="column.key === 'serviceContent'">
            <div class="flex_box">
              <EllipsisTooltip class="flex_full"
                :content="(record.serviceContentStr || '') + (record.serviceContentOther ? `-${record.serviceContentOther}` : '')" />
            </div>
          </template>
          <template v-if="column.key === 'bidedStr'">
            <span v-if="record.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">-</span>
            <span v-else>{{ record.bidedStr }}</span>
          </template>
          <template v-if="column.key === 'cooperationTypeStr'">
            <span>{{ record.cooperationTypeStr }}</span>
            <span v-if="(record.cooperationType || '').includes('QI_TA') &&
              record.cooperationTypeOther
              ">-{{ record.cooperationTypeOther }}</span>
          </template>
          <template v-if="column.key === 'principal'">
            <!-- {{record.principal ? record.principal.realname:'-'}} -->
            <UserBox :data="record.principal || {}" single />
          </template>
          <template v-if="column.key === 'attributorUser'">
            <UserBox :data="record.attributorUser || {}" single />
          </template>
          <template v-if="column.key === 'serviceEndTime'">
            {{ dateFormat(record.serviceEndTime, 'YYYY-MM-DD') }}
          </template>
          <template v-if="column.key === 'manageAssessDeadline'">
            {{ dateFormat(record.manageAssessDeadline, 'YYYY-MM-DD') }}
          </template>
          <template v-if="column.key === 'disposeTime'">
            {{ dateFormat(record.disposeTime, 'YYYY-MM-DD') }}
          </template>
          <template v-if="column.key === 'updateUser'">
            <span v-if="record.processState == 0 && record.processMode == null">{{ '-' }}</span>
            <UserBox v-else :data="record.updateUser || {}" single />
          </template>
          <template v-if="column.key === 'contractExpireTime'">
            <!-- <ExpireTime :expireTime="record.contractExpireTime"></ExpireTime> -->
            <span v-if="record.processState == 2 || [1, 2, 3].includes(record.processMode)"></span>
            <span v-else>
              <ExpireTimes :expireTime="record.contractExpireTime" :contractIsNotExpire="record.contractIsNotExpire" />
            </span>
          </template>
          <template v-if="column.key === 'renewal'">
            <span>{{ record.renewal == 'FOU' ? '否' : '是' }}</span>
          </template>
          <template v-if="column.key === 'processState'">
            <span>{{ record.processState == 0 ? '未处理' : record.processState == 1 ? '审批中' : record.processState == 2 ?
              '已处理' : record.processState == 3 ? '处理被驳回' : '' }}</span>
          </template>
          <template v-if="column.key === 'processMode'">
            <span>{{ record.processMode == 0 ? '' : record.processMode == 1 ? '续签' : record.processMode == 2 ? '重新投标' :
              record.processMode == 3 ? '退场' : '' }}</span>
          </template>
          <template v-if="column.key === 'approvalSponsorTime'">
            <span>{{ record.processMode == 3 ? record.approvalSponsorTime : '-' }}</span>
          </template>

          <template v-if="column.key === 'assessIsNot'">
            <span>{{ record.assessIsNot == 'FOU' ? '否' : '是' }}</span>
          </template>
          <template v-if="column.key === 'checkState'">
            <span>{{ record.checkState == 'FOU' ? '否' : '是' }}</span>
          </template>

          <template v-if="column.key === 'relevanceProjectNo'">
            <router-link :to="'/innerPage/projectInfo?id=' + record.relevanceProjectId + '&to=xmxx'" class="color-link">
              {{ record.relevanceProjectNo }}
            </router-link>
          </template>
          <template v-if="column.key === 'approvalProcessOA'">
            <EllipsisTooltip class="color-link" @click="toOaLink(record.approvalProcessOA)" :content="record.approvalProcessOA
              " />
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

    <!-- <BaseHandle ref="baseHandleRef" @success="updateRow" /> -->
    <!-- <ChangeBelonger ref="changeBelongerRef" @success="updateRow" /> -->
    <!-- <StopDrawer ref="stopDrawerRef" @success="updateRow" /> -->
  </div>
</template>
<script setup>
import api from '@/api/index'
// import { message, Modal } from 'ant-design-vue'
import PageFilter from './components/extensFilter.vue'
// import NodeConfig                     from './components/NodeConfig.vue'
// import BaseHandle                     from './components/BaseHandle.vue'
// import ChangeBelonger                 from './components/ChangeBelonger.vue'
// import StopDrawer                     from './components/StopDrawer.vue'
import { dataToFile, hasPermission, dateFormat } from '@/utils/tools'
import { mainStore } from '@/store'
const store = mainStore()
const router = useRouter()

const loadding = ref(false)
const formData = ref({})
const filterForm = reactive({
  // processState: [
  //   "0",
  //   "1",
  //   // '2',
  //   "3"   
  // ],
  // processMode: [
  //   "1",
  //   "2",
  //   "3"   
  // ],
  pageNo: 1,
  pageSize: 10
})
const data = reactive({
  list: [],
  columns: [
    {
      title: '归属单位',
      dataIndex: 'companyName',
      width: 200,
      ellipsis: true
    },
    {
      title: '项目编号',
      key: 'projectNo',
      width: 150
    },
    {
      title: '项目名称',
      key: 'projectName',
      width: 200
    },
    {
      title: '项目优先级',
      dataIndex: 'projectLevelStr',
      width: 150
    },
    {
      title: '拓展模式',
      dataIndex: 'expansionModeStr',
      width: 150
    },
    {
      title: '业态',
      dataIndex: 'businessTypeStr',
      width: 120
    },
    {
      title: '服务内容',
      key: 'serviceContent',
      width: 120
    },
    {
      title: '拓后负责人',
      key: 'principal',
      width: 120
    },
    {
      title: '是否已承接查验',
      key: 'checkState',
      width: 150
    },
    {
      title: '是否已评估',
      key: 'assessIsNot',
      width: 100
    },
    {
      title: '项目状态',
      key: 'serviceStatus',
      width: 120
    },
    {
      title: '合同是否到期',
      dataIndex: 'contractIsNotExpire',
      width: 150
    },
    {
      title: '合同到期倒计时',
      key: 'contractExpireTime',
      width: 150
    },
    {
      title: '合同到期日期',
      key: 'serviceEndTime',
      width: 120
    },
    {
      title: '经营评估期限',
      key: 'manageAssessDeadline',
      width: 120
    },
    // {
    //     title     : '项目预估金额()元)',
    //     dataIndex : 'bidingBudget',
    //     width     : 150,
    // },
    {
      title: '处理状态',
      key: 'processState',
      width: 120
    },
    {
      title: '处理方式',
      key: 'processMode',
      width: 120
    },
    {
      title: '处理日期',
      key: 'disposeTime',
      width: 120
    },
    {
      title: '处理人',
      dataIndex: 'processPerson',
      width: 180
    },
    {
      title: '关联新建项目',
      key: 'relevanceProjectNo',
      width: 180
    },
    {
      title: '退场日期',
      key: 'approvalSponsorTime',
      width: 200
    },
    {
      title: '退场描述',
      dataIndex: 'exitDescription',
      width: 200,
      ellipsis: true

    },
    {
      title: '对应OA审批流程',
      key: 'approvalProcessOA',
      width: 300
    },
    // {
    //     title     : '签约日期',
    //     dataIndex : 'signTime',
    //     width     : 140,
    //     customRender:({text})=>{
    //         return dateFormat(text,'YYYY-MM-DD')
    //     }
    // },
    {
      title: '所属大区',
      dataIndex: 'regionName',
      width: 170
    },
    {
      title: '项目归属人',
      key: 'attributorUser',
      width: 200
    },
    {
      title: '运营团队',
      dataIndex: 'operationTeamName',
      width: 200
    },
    {
      title: '是否为续签项目',
      dataIndex: 'inStockStr',
      width: 200
    },
    {
      title: '省份',
      dataIndex: 'provinceName',
      width: 120
    },
    {
      title: '城市',
      dataIndex: 'cityName',
      width: 120
    },
    {
      title: '区/县',
      dataIndex: 'areaName',
      width: 120
    },
    {
      title: '街道',
      dataIndex: 'streetName',
      width: 120
    },
    {
      title: '最新跟进时间',
      dataIndex: 'followTime',
      width: 180
    },
    {
      title: '操作',
      key: 'action',
      width: 280,
      fixed: 'right'
    }
  ],
  total: 0
})
const getPage = () => {
  let postData = builderFilter()
  loadding.value = true
  api.project.projectExtensionPageAfter(postData).then((res) => {
    if (res.code == 200) {
      data.list = res.data.records
      data.total = res.data.total
      //如果后端返回的页数小于点击的页面则设置默认从第一页重新开始请求
      if(res.data.pages!=0&&res.data.pages<filterForm.pageNo){
        filterSubmit();
      }
    }
    loadding.value = false
  })
}
const screenPage = (key, val) => {
  let postData = {
    asc: ['serviceEndTime'],
    pageNo: filterForm.pageNo,
    pageSize: filterForm.pageSize,
    params: {},
    geParams: {},
    leParams: {},
    inParams: { 
    },
    likeParams: {}
  }
  postData.params[key] = val
  loadding.value = true
  api.project.projectExtensionPageAfter(postData).then((res) => {
    if (res.code == 200) {
      data.list = res.data.records
      data.total = res.data.total
    }
    loadding.value = false
  })
}
const projectExtensionSum = () => {
  let postData = builderFilter()
  api.project.projectExtensionTotal(postData).then((res) => {
    if (res.code == 200) {
      formData.value = res.data
    }
  })
}
const filterSubmit = () => {
  filterForm.pageNo = 1
  getPage()
}
const dataExport = () => {
  let postData = builderFilter()
  store.spinChange(1)
  api.project.projectTHExport(postData).then((res) => {
    store.spinChange(-1)
    let timestamp = new Date().getTime()
    dataToFile(res, '项目-' + timestamp + '.xlsx')
  })
}
const builderFilter = () => {
  let postData = {
    // desc: ['createTime'],
    asc: ['serviceEndTime'],
    pageNo: filterForm.pageNo,
    pageSize: filterForm.pageSize,
    params: {},
    geParams: {},
    leParams: {},
    inParams: {    
    // 'extension.processState': [
    // "0",
    // "1",
    // // "2",
    // "3"   
    // ],
    // 'extension.processMode': [
    //   "1",
    //   "2",
    //   "3"   
    // ],
    },
    likeParams: {}
  }
  if (filterForm.moreFilter || filterForm.moreFilter2) {
    let params = [
      'projectLevel',
      // 'regionId',
      // 'companyId',
      'serviceStatus',
      'projectType',
      'expansionMode',
      'bided',
      // 'businessType',
      'attributorUserId',
      'provinceCode',
      'cityCode',
      'areaCode',
      'streetCode',
      'customerId',
      'expire',
      // 'serviceContent',
      'contractIsNotExpire',
      // 'serviceEndTime',
      // 'operationTeamId',
      'principalId',
      'checkState',
      'assessIsNot',
      // 'processState',
      // 'processMode',
      'relevanceProjectNo'
    ]
    let likeParams = ['projectName', 'projectNo']
    let inParams = ['businessType', 'serviceContent', 'regionId', 'companyId', 'operationTeamId', 'processState' , 'processMode']
    let geParams = ['serviceEndTime']
    let leParams = ['serviceEndTime']

    params.forEach((key) => {
      if (filterForm[key] && filterForm[key] != -1 && key != 'processState' && key != 'processMode') {
        postData.params[key] = filterForm[key]
      }
      // if (filterForm['processMode'] && filterForm['processMode'] != -1) {
      //   postData.params['extension.processMode'] = filterForm['processMode']
      // }
      // if (filterForm['processState'] && filterForm['processState'] != -1) {
      //   postData.params['extension.processState'] = filterForm['processState']
      // }
    })
    likeParams.forEach((key) => {
      if (filterForm[key]) {
        postData.likeParams[key] = filterForm[key]
      }
    })
    inParams.forEach((key) => {
      if (filterForm['serviceContent'] && filterForm['serviceContent'].length > 0) {
        postData.inParams['serviceContent'] = filterForm['serviceContent']
      }
      if (filterForm['businessType'] && filterForm['businessType'].length > 0) {
        postData.inParams['businessType'] = filterForm['businessType']
      }
      if (filterForm['processMode'] && filterForm['processMode'].length > 0) {
        postData.inParams['extension.processMode'] = filterForm['processMode']
      }else{
        postData.inParams['extension.processMode'] = undefined;
      }
      if (filterForm['processState'] && filterForm['processState'].length > 0) {
        postData.inParams['extension.processState'] = filterForm['processState']
      }else{
        postData.inParams['extension.processState'] =undefined;
      }  
      if (filterForm[key] && filterForm[key].length > 0 && key != 'processState' && key != 'processMode') {
        postData.inParams[key] = filterForm[key]
      }
    });
    geParams.forEach((key) => {
      if (filterForm[key]) {
        postData.geParams[key] = filterForm[key] + ' 00:00:00';
      }
    });
    leParams.forEach((key) => {
      if (filterForm[key]) {
        postData.leParams[key] = filterForm[key] + ' 23:59:59';
      }
    })
  }

  if (!filterForm.moreFilter && filterForm.searchKey) {
    postData.content = filterForm.searchKey
    postData.contentColumns = ['projectName', 'projectNo', 'remark']
  }
  if (filterForm.year) {
    postData.geParams.performanceConfirmTime = filterForm.year + '-01-01 00:00:00'
    postData.leParams.performanceConfirmTime = filterForm.year + '-12-31 23:59:59'
  }
  return postData
}

const actions = (record, index) => {
  return [
    {
      text: '运营数据更新',
      show: record.principalShow == true || hasPermission(['biz:projectExtension:operation']),
      click: () => {
        router.push('/innerPage/operationInfo?id=' + record.id + '&businessTypeStr=' + record.businessTypeStr + '&companyName=' + record.companyName + '&projectName=' + record.projectName + '&show=' + record.show)
      }
    },
    {
      text: '退场',
      show: (record.principalShow == true || hasPermission(['biz:projectExtension:exit']))&&  record.serviceStatus == 'YI_DAO_QI'  && !(record.processMode=='3' && ([1, 2].includes(record.processState)))  ,
      click: () => {
        router.push('/innerPage/extensionInfo?id=' + record.id + '&code=thxmxx' + '&show=' + record.principalShow)
      }
    },
    {
      text: '拓前信息回顾',
      show: record.principalShow == true || (hasPermission(['biz:projectExtension:review'])),
      click: () => {
        router.push('/innerPage/projectInfo?id=' + record.id + '&code=xmxx')
      }
    }
  ]
}

// const baseHandleRef = ref(null)
// const changeBelongerRef = ref(null)
// const stopDrawerRef = ref(null)
// const handleComponent = (component, row, index) => {
//   if (component == 'renewal') {
//     //续签
//     Modal.confirm({
//       title: '项目发起续签确认',
//       content: '请问是否确定发起"' + row.projectName + '"的续签流程',
//       onOk() {
//         api.project.projectExtensionRenew(row.id).then((res) => {
//           if (res.code == 200) {
//             message.success('操作成功')
//             updateRow(res.data, index)
//           }
//         })
//       }
//     })
//   }
//   if (component == 'rebid') {
//     Modal.confirm({
//       title: '项目重新投标确认',
//       content: '请问是否确定重新投标"' + row.projectName + '"项目',
//       onOk() {
//         api.project.projectExtensionNewBid(row.id).then((res) => {
//           if (res.code == 200) {
//             message.success('操作成功')
//             updateRow(res.data, index)
//           }
//         })
//       }
//     })
//   }
//   if (component == 'changeBelonger') {
//     //变更归属人
//     changeBelongerRef.value.open(row, index)
//   }
//   if (component == 'close') {
//     //终止
//     stopDrawerRef.value.open(row, index)
//   }
//   if (component == 'expire') {
//     //标记失效
//     baseHandleRef.value.open(row, 'expire', index)
//   }
// }
const updateRow = (res, index) => {
  // if(res&&res.id&&index>-1){
  //     data.list[index] = res;
  // }else{
  getPage()
  // }
}
const toOaLink = (link) => {
  api.common.getSsoToken({
    mobile: store.userInfo.phonenumber
  }).then(res => {
    if (res.code == 200 && res.data) {
      window.open(link + '&access_token=' + res.data);
    }
  })
}
onMounted(() => {
  getPage()
  projectExtensionSum()
})
onActivated(() => {
  getPage()
  projectExtensionSum()
})
</script>
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

    .pl25 {
      margin-right: 10px;
    }

    .pl26 {
      margin-right: 30px;
      cursor: pointer;

      &:hover {
        color: #f99c34;
      }
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
</style>
