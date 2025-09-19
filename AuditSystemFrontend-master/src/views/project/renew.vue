<template>
  <div class="page-project content-inner">
    <div class="bread_box">
      <a-breadcrumb>
        <a-breadcrumb-item><a href="#/index">首页</a></a-breadcrumb-item>
        <a-breadcrumb-item>项目库</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <PageFilter v-model="filterForm" @submit="filterSubmit" @dataExport="dataExport"
      @exportProjectAchievement="exportProjectAchievement" />
    <div class="content-box_full">
      <Title title="续签/重新投标">
        <template #right>
          <a-space>
            <!-- <NodeConfig /> -->
            <!-- <a-button type="primary" @click="router.push('/innerPage/projectEdit')" v-permission="['biz:project:add']">
              <template #icon>
                <plus-outlined />
              </template>
              新建项目
            </a-button> -->
          </a-space>
        </template>
      </Title>
      <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">
        <template #bodyCell="{ column, text, record, index }">
          <template v-if="column.key === 'projectNo'">
            <router-link target="_blank" :to="'/innerPage/projectInfo?id=' + record.id" class="color-link">
              {{ record.projectNo }}
            </router-link>
          </template>
          <template v-if="column.key === 'projectName'">
            {{ record.projectName }}
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
          <template v-if="column.key === 'sourceName'">
            <router-link v-if="record.relevanceProjectId != null"
              :to="'/innerPage/projectInfo?id=' + record.relevanceProjectId" class="color-link">
              {{ record.sourceName }}
            </router-link>
            <span v-else>{{ record.sourceName || '-' }}</span>
          </template>

          <template v-if="column.key === 'stepName'">            
            <span>{{ record.stepName || "-" }}</span>
          </template>

          <template v-if="column.key === 'serviceContent'">
            <div class="flex_box">
              <EllipsisTooltip class="flex_full"
                :content="(record.serviceContentStr || '') + (record.serviceContentOther ? (',' + record.serviceContentOther) : '')" />
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
          v-model:pageSize="filterForm.pageSize" :show-total="total => `共 ${total} 条数据`" size="small" @change="getPage"
          @showSizeChange="filterForm.pageNo = 1" :total="data.total" />
      </div>
    </div>

    <BaseHandle ref="baseHandleRef" @success="updateRow" />
    <ChangeBelonger ref="changeBelongerRef" @success="updateRow" />
    <StopDrawer ref="stopDrawerRef" @success="updateRow" />
  </div>
</template>
<script setup>
import api from '@/api/index';
import { message, Modal } from 'ant-design-vue';
import PageFilter from './components/PageFilter2.vue'
import NodeConfig from './components/NodeConfig.vue'
import BaseHandle from './components/BaseHandle.vue'
import ChangeBelonger from './components/ChangeBelonger.vue'
import StopDrawer from './components/StopDrawer.vue'
import { dataToFile, hasPermission, dateFormat } from '@/utils/tools';
import { mainStore } from '@/store';
const store = mainStore();
const router = useRouter();

const loadding = ref(false);
const filterForm = reactive({
  pageNo: 1,
  pageSize: 10,
  serviceStatus: [
    "YI_DAO_QI"
  ],
  processState: [
    "0"
  ],
  expandServiceStatus: ['WEI_JIE_XIANG'],
  expandProcessMode: [
    "1",
    "2",
  ],
  expandTwoServiceStatus: ['ZAI_GUAN', 'YI_ZHONG_ZHI', 'YI_FEI_ZHI', 'YI_WAN_JIE', 'YI_TUI_CHANG', 'CLOSE_FILE', 'WEI_ZHONG_BIAO'],
  expandTwoProcessMode: [
    "1", "2"
  ]
})
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
      title: '大区',
      dataIndex: 'regionName',
      width: 140,
      ellipsis: true,
    },
    {
      title: '归属单位',
      dataIndex: 'companyName',
      width: 200,
      ellipsis: true,
    },
    {
      title: '项目编号',
      key: 'projectNo',
      width: 150,
    },
    {
      title: '项目名称',
      key: 'projectName',
      width: 300,
    },
    {
      title: '关联客户',
      key: 'customerName',
      width: 180,
    },
    {
      title: '项目状态',
      key: 'serviceStatus',
      width: 130,
    },
    {
      title: "完成节点",
      key: "stepName",
      width: 130,
    },
    {
      title: '是否有效',
      dataIndex: 'expireStr',
      width: 180,
    },
    {
      title: '项目类型',
      dataIndex: 'projectTypeStr',
      width: 150,
    },
    {
      title: '业务板块',
      dataIndex: 'businessSegmentsStr',
      width: 150,
    },
    {
      title: '业态',
      key: 'businessTypeStr',
      width: 120,
    },
    {
      title: '服务内容',
      key: 'serviceContent',
      width: 180,
    },
    {
      title: '项目优先级',
      dataIndex: 'projectLevelStr',
      width: 150,
    },
    {
      title: '项目预估金额(元)',
      dataIndex: 'bidingBudget',
      width: 150,
    },
    {
      title: '是否为续签项目',
      dataIndex: 'inStockStr',
      width: 120,
    },
    {
      title: '归属人',
      key: 'attributorUser',
      width: 80,
    },
    {
      title: '拓展模式',
      dataIndex: 'expansionModeStr',
      width: 150,
    },
    {
      title: '合作模式',
      key: 'cooperationTypeStr',
      width: 120,
    },
    {
      title: '是否招标',
      key: 'bidedStr',
      width: 80,
    },
    {
      title: '招标类型',
      dataIndex: 'bidingModeStr',
      width: 120,
    },
    {
      title: '合作方式',
      dataIndex: 'cooperationModeStr',
      width: 120,
    },
    {
      title: '项目来源',
      key: 'sourceName',
      width: 120,
    },
    {
      title: '建筑面积 (㎡)',
      dataIndex: 'constructionArea',
      width: 120,
    },
    {
      title: '签约日期',
      dataIndex: 'signTime',
      width: 140,
      customRender: ({ text }) => {
        return dateFormat(text, 'YYYY-MM-DD')
      }
    },
    {
      title: '服务开始日期',
      dataIndex: 'serviceBeginTime',
      width: 140,
      customRender: ({ text }) => {
        return dateFormat(text, 'YYYY-MM-DD')
      }
    },
    {
      title: '合同到期日期',
      dataIndex: 'serviceEndTime',
      width: 140,
      customRender: ({ text }) => {
        return dateFormat(text, 'YYYY-MM-DD')
      }
    },
    {
      title: '拟服务期限(月)',
      dataIndex: 'proposedServicePeriod',
      width: 170,
    },
    {
      title: '合同总金额(元)',
      dataIndex: 'contractAmount',
      width: 200,
    },
    {
      title: '合同年度金额(元)',
      dataIndex: 'contractAnnualAmount',
      width: 200,
    },
    {
      title: '当年转化收入(元)',
      dataIndex: 'annualConversionAmount',
      width: 200,
    },
    {
      title: '省份',
      dataIndex: 'provinceName',
      width: 120,
    },
    {
      title: '城市',
      dataIndex: 'cityName',
      width: 120,
    },
    {
      title: '区/县',
      dataIndex: 'areaName',
      width: 120,
    },
    {
      title: '街道',
      dataIndex: 'streetName',
      width: 120,
    },
    {
      title: '详细地址',
      dataIndex: 'address',
      width: 120,
      ellipsis: true,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      width: 120,
      ellipsis: true,
    },
    {
      title: '业绩确认时间',
      dataIndex: 'performanceConfirmTime',
      width: 180,
    },
    {
      title: '最新跟进时间',
      dataIndex: 'followTime',
      width: 180,
    },
    {
      title: '失效原因',
      dataIndex: 'expireReason',
      width: 180,
      ellipsis: true,
    },
    {
      title: '失效时间',
      dataIndex: 'expireTime',
      width: 180,
    },
    {
      title: '创建人',
      dataIndex: ['createUser', 'realname'],
      width: 180,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 180,
    },
    {
      title: '最后修改人',
      dataIndex: ['updateUser', 'realname'],
      width: 180,
    },
    {
      title: '最后修改时间',
      dataIndex: 'updateTime',
      width: 180,
    },
    {
      title: '操作',
      key: 'action',
      width: 240,
      fixed: 'right'
    },

  ],
  total: 0,
})
const getPage = () => {
  let postData = builderFilter();
  loadding.value = true;
  api.project.projectRenewBidPage(postData).then(res => {
    if (res.code == 200) {
      data.list = res.data.records;
      data.total = res.data.total;
      //如果后端返回的页数小于点击的页面则设置默认从第一页重新开始请求
      if (res.data.pages != 0 && res.data.pages < filterForm.pageNo) {
        filterSubmit();
      }
    }
    loadding.value = false;
  })
}
const filterSubmit = () => {
  filterForm.pageNo = 1;
  getPage();
}
const dataExport = () => {
  let postData = builderFilter();
  store.spinChange(1);
  api.project.renewBidPageExport(postData).then(res => {
    store.spinChange(-1);
    let timestamp = (new Date).getTime();
    dataToFile(res, '项目-' + timestamp + '.xlsx');
  })
}
const exportProjectAchievement = () => {
  let postData = builderFilter();
  store.spinChange(1);
  api.project.exportProjectAchievementTwo(postData).then(res => {
    store.spinChange(-1);
    let timestamp = (new Date).getTime();
    dataToFile(res, '业绩台账-' + timestamp + '.xlsx');
  })
}
const builderFilter = () => {
  let postData = {
    isRenew: "1",
    desc: ['createTime'],
    pageNo: filterForm.pageNo,
    pageSize: filterForm.pageSize,
    params: {},
    geParams: {},
    leParams: {},
    inParams: {},
    likeParams: {}
  }
  // if(filterForm.moreFilter){
  let params = ['projectLevel', 'bided', 'attributorUserId',
    'provinceCode', 'cityCode', 'areaCode', 'streetCode', 'customerId', 'inStock', 'expire'];
  let likeParams = ['projectName', 'projectNo']
  let inParams = ['serviceStatus', 'businessType', 'companyId', "expandCompanyId", 'expansionMode', 'projectType', 'processState', 'expandServiceStatus', 'expandProcessMode','expandTwoServiceStatus','expandTwoProcessMode','finishStepId']
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
    if (filterForm['businessType'] && filterForm['businessType'].length) {
      postData.inParams['businessType'] = filterForm['businessType'];
    }

    if (filterForm[key] && filterForm[key].length > 0) {
      postData.inParams[key] = filterForm[key];
    }
  });
  // }

  if (!filterForm.moreFilter && filterForm.searchKey) {
    postData.content = filterForm.searchKey;
    postData.contentColumns = ['projectName', 'projectNo'];
  }
  if (filterForm.year) {
    postData.geParams.updateStatusDate = filterForm.year + '-01-01 00:00:00';
    postData.leParams.updateStatusDate = filterForm.year + '-12-31 23:59:59';
  }
  return postData;
}

const actions = (record, index) => {
  return [

    {
      text: '查看',
      show: record.serviceStatus != 'YI_DAO_QI',
      click: () => {
        let routeUrl = router.resolve({
          path: "/innerPage/projectInfo",
          query: { id: record.id }
        })
        window.open(routeUrl.href, '_blank');
        // router.push('/innerPage/projectInfo?id=' + record.id);
      }
    },
    {
      text: '撤销',
      show: (record.serviceStatus == 'YI_TUI_CHANG' || record.serviceStatus == 'YI_WAN_JIE' || record.serviceStatus == 'CLOSE_FILE')  && hasPermission(['biz:projectExtension:rollback']),
      click: () => {
        handleComponent('rollback', record, index)
      }
    },
    {
      text: '变更归属人',
      show: record.roleKeys.includes(2) && record.serviceStatus != 'YI_DAO_QI' && record.expire != 'YI_SHI_XIAO' && hasPermission(['biz:project:changeAttributor']),
      click: () => {
        handleComponent('changeBelonger', record, index);
      }
    },
    {
      text: '克隆',
      show: hasPermission(['biz:project:clone']) && record.serviceStatus != 'YI_DAO_QI',
      click: () => {
        handleComponent('changeClone', record, index);
      }
    },
    {
      text: '编辑基础信息',
      show: record.roleKeys.includes(1) && record.serviceStatus != 'YI_DAO_QI' && record.expire != "YI_SHI_XIAO" && !["ZAI_GUAN", "YI_ZHONG_ZHI"].includes(record.serviceStatus) && (hasPermission(["biz:project:update"]) || [record.createUserId, record.attributorUserId].includes(store.userInfo.userId)),
      click: () => {
        router.push('/innerPage/projectEdit?id=' + record.id);
      }
    },
    {
      text: '文档管理',
      show: record.serviceStatus != 'YI_DAO_QI',
      click: () => {
        router.push(`/innerPage/projectInfo?id=${record.id}&code=xmwd`);
      }
    },
    {
      text: '续签',
      show: (record.show == true || record.attributorOrCreateShow == true || hasPermission(['biz:projectExtension:renew'])) && record.serviceStatus == 'YI_DAO_QI' && !(record.processMode = '3' && ([1, 2].includes(record.processState))),//&& [0, 3].includes(record.processState),
      click: () => {
        handleComponent('renewal', record, index)
      }
    },
    {
      text: '重新投标',
      show: (record.show == true || record.attributorOrCreateShow == true || hasPermission(['biz:projectExtension:newBid'])) && record.serviceStatus == 'YI_DAO_QI' && !(record.processMode = '3' && ([1, 2].includes(record.processState))),//&& [0, 3].includes(record.processState),
      click: () => {
        handleComponent('rebid', record, index)
      }
    },
    // {
    //   text: '终止项目',
    //   show: record.expire != 'YI_SHI_XIAO' && record.serviceStatus != 'YI_DAO_QI' && ([record.createUserId, record.attributorUserId].includes(store.userInfo.userId) || hasPermission(['biz:project:changeClose'])),
    //   click: () => {
    //     handleComponent('close', record, index);
    //   }
    // },
    {
      text: '退场',
      show: (record.show == true || record.principalShow == true || hasPermission(['biz:projectExtension:exit'])) && record.serviceStatus == 'YI_DAO_QI' && !(record.processMode = '3' && ([1, 2].includes(record.processState))),
      click: () => {
        router.push(`/innerPage/extensionInfo?id=${record.id}&code=thxmxx`)
      }
    },
    // {
    //   text: '标记失效',
    //   show: record.expire != 'YI_SHI_XIAO'&& record.serviceStatus != 'YI_DAO_QI'  && ([record.createUserId, record.attributorUserId].includes(store.userInfo.userId) || hasPermission(['biz:project:changeExpire'])),
    //   click: () => {
    //     handleComponent('expire', record, index);
    //   }
    // },
    {
      text: '完结',
      show: (record.show == true || record.attributorOrCreateShow == true || hasPermission(['biz:project:finish'])) && record.serviceStatus == 'YI_DAO_QI' && !(record.processMode == '3' && ([1, 2].includes(record.processState))),
      click: () => {
        handleComponent('finish', record, index);
      }
    }
  ];
}

const baseHandleRef = ref(null);
const changeBelongerRef = ref(null);
const stopDrawerRef = ref(null);
const handleComponent = (component, row, index) => {
  switch (component) {
    case 'changeBelonger':  // 变更归属人
      changeBelongerRef.value.open(row, index);
      break
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
    case 'rebid': //重新投标
      Modal.confirm({
        title: '项目重新投标确认',
        content: '请问是否确定重新投标"' + row.projectName + '"项目',
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
    case 'close': //终止
      stopDrawerRef.value.open(row, index);
      break
    case 'expire'://标记失效
      baseHandleRef.value.open(row, 'expire', index);
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
    case 'changeClone':
      Modal.confirm({
        title: '是否确认克隆',
        content: '是否确认克隆名称为"' + row.projectName + '"的数据项？',
        onOk() {
          api.project.projectClone({ id: row.id }).then((res) => {
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
  // if(res&&res.id&&index>-1){
  //     data.list[index] = res;
  // }else{
  getPage();
  // }
}

onMounted(() => {
  getPage();
})
onActivated(() => {
  getPage();
})
</script>
<style scoped lang="less"></style>
