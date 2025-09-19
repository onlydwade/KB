
<template>
    <div class="menu_inner">
        <a-tabs v-model:activeKey="pageType" @change="filterSubmit">
            <a-tab-pane key="FENG_XIAN_YU_JING" tab="风险预警"/>
            <a-tab-pane key="FENG_XIAN_CHU_LI" tab="风险处理"/>
            <template #rightExtra>
                <a-space>
                    <template v-if="pageType=='FENG_XIAN_CHU_LI'">
                        <a-input allowClear v-model:value="filterForm.approval_name" placeholder="您可输入风险名称搜索" />
                        <a-button @click="filterSubmit">查询</a-button>
                    </template>
                    <a-button type="primary" @click="drawer('ADD')" v-permissionInvestment="['biz:projectCompany:add']">
                        <template #icon>
                            <plus-outlined />
                        </template>
                        新建
                    </a-button>
                </a-space>
            </template>
        </a-tabs>
        <a-table :columns="data[pageType]" :loading="loadding" :data-source="data.list" rowKey="id" :pagination="false" :scroll="{ x: '100%' }">
            <template #bodyCell="{ column,text,record,index }">
                <template v-if="column.key === 'sort'">
                    {{(filterForm.pageNo-1)*filterForm.pageSize + index +1}}
                </template>
                <template v-if="column.key === 'processApprovalUrl'">
                    <OaLink :link="record.processApprovalUrl"/>
                </template>
                <template v-if="column.key === 'relieveApprovalUrl'">
                    <OaLink :link="record.relieveApprovalUrl"/>
                </template>
                <template v-if="column.key === 'action'">
                    <actionBtn :actions="actions(record,index)"/>
                </template>
                <template v-if="column.key === 'expired'">
                  <div v-if="record.expired" class="status_box">
                    <span class="dot" :style="'background-color:#f5222d'"></span>
                    <span class="name text_no_warp">
                              {{ record.expired }}
                          </span>
                    <slot></slot>
                  </div>
                </template>
                <EllipsisTooltip v-if="column.ellipsis" :content="text"/>
            </template>
        </a-table>
        <div class="pagination_table">
            <a-pagination showSizeChanger show-quick-jumper
                v-model:current="filterForm.pageNo"
                v-model:pageSize="filterForm.pageSize"
                :show-total="total => `共 ${total} 条数据`"
                size="small"
                @change="getPage"
                @showSizeChange="filterForm.pageNo=1"
                :total="data.total" />
        </div>
    </div>
    <riskWarningDrawer ref="drawerYuJinRef" :companyId="companyId" :menuId="menuInfo.id" @success="getPage"/>
    <riskHandleDrawer ref="drawerChuLiRef" :companyId="companyId" :menuId="menuInfo.id" @success="getPage"/>
</template>
<script setup>
import api                 from '@/api/index';
import riskWarningDrawer   from '../riskWarningDrawer.vue'
import riskHandleDrawer    from '../riskHandleDrawer.vue'
import { hasPermission,dateFormat }      from '@/utils/tools';
import { message , Modal } from 'ant-design-vue';
const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    }
})
const pageType   = ref('FENG_XIAN_YU_JING');
const loadding   = ref(false);
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})
const oaStatus = ref({
    0:'待发起审批',
    1:'审批中',
    2:'审批通过',
    3:'已驳回',
    4:'已废弃',
    5:'待确认',
    8:'线下审批通过',
    9:'无需审批',
})
const data = reactive({
    list                : [],
    'FENG_XIAN_YU_JING' : [
        {
            title     : '风险节点名称',
            dataIndex : 'node',
            width     : 180,
            ellipsis  : true,
        },
        {
            title        : '里程碑',
            dataIndex    : 'milepostTime',
            width        : 140,
            customRender : ({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title        : '提醒时间',
            dataIndex    : 'reminderTime',
            width        : 140,
            customRender : ({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title     : '提醒内容',
            dataIndex       : 'content',
            width     : 250,
        },
        {
          title     : '状态',
          key       : 'expired',
          width     : 140
        },
        {
            title     : '创建人',
            dataIndex : ['createUser','realname'],
            width     : 180,
        },
        {
            title     : '创建时间',
            dataIndex : 'createTime',
            width     : 170,
            customRender:({text})=>{
                return dateFormat(text,'YYYY-MM-DD HH:mm')
            }
        },
        {
            title : '操作',
            key   : 'action',
            width : 120,
            fixed : 'right'
        },
    ],
    'FENG_XIAN_CHU_LI' : [
        {
            title    : '序号',
            key      : 'sort',
            width    : 80,
        },
        {
            title     : '风险名称',
            dataIndex : 'name',
            width     : 180,
            ellipsis  : true,
        },
        {
            title     : '风险类型',
            dataIndex : 'typeStr',
            width     : 180,
        },
        {
            title     : '风险状态',
            dataIndex : 'processApprovalStatus',
            width     : 180,
            customRender : ({text})=>{
                return oaStatus.value[text] || '草稿'
            }
        },
        {
            title     : '风险处理发起人',
            dataIndex : ['processApprovalUser','realname'],
            width     : 180,
        },
        {
            title        : '风险处理发起日期',
            dataIndex    : 'processApprovalSendTime',
            width        : 160,
            customRender : ({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title : '风险处理OA审批流',
            key   : 'processApprovalUrl',
            width : 200,
        },
        {
            title     : '风险解除发起人',
            dataIndex : ['relieveApprovalUser','realname'],
            width     : 180,
        },
        {
            title        : '风险解除发起日期',
            dataIndex    : 'relieveApprovalSendTime',
            width        : 160,
            customRender : ({text})=>{
                return dateFormat(text,'YYYY-MM-DD')
            }
        },
        {
            title : '风险解除审批流',
            key   : 'relieveApprovalUrl',
            width : 200,
        },
        {
            title : '操作',
            key   : 'action',
            width : 240,
            fixed : 'right'
        },
    ],
    total : 0,
})
const apiKey = computed(()=>pageType.value=='FENG_XIAN_CHU_LI'?'companyrisk':'companyRiskInspection');
const getPage = ()=>{
    let filterKey = pageType.value=='FENG_XIAN_CHU_LI'?'approval':'inspection';
    let postData = {
        desc     : ['createTime'],
        pageNo   : filterForm.pageNo,
        pageSize : filterForm.pageSize,
    }
    if (pageType.value=='FENG_XIAN_CHU_LI'){
      postData.params={"approval.companyId":props.companyId}
    }else {
      postData.params={"inspection.companyId":props.companyId}
    }

    loadding.value = true;
    if(pageType.value=='FENG_XIAN_CHU_LI' && filterForm.approval_name){
        postData.likeParams = {
            'approval.name' : filterForm.approval_name
        }
    }
    api.investment.correlationPage(postData,apiKey.value).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
const filterSubmit = ()=>{
    data.list         = [];
    filterForm.pageNo = 1;
    getPage();
}
const actions = (record,index)=>{
    return [
        {
            text  : '查看',
            show  : pageType.value=='FENG_XIAN_CHU_LI',
            click : ()=>{
                drawer('SHOW',record);
            }
        },
        {
            text  : '编辑',
            show  : hasPermission(['biz:projectCompany:edit'],'investment'),
            click : ()=>{
                drawer('EDIT',record);
            }
        },
        {
            text  : '删除',
            show  : pageType.value=='FENG_XIAN_YU_JING' && hasPermission(['biz:projectCompany:delete'],'investment'),
            click : ()=>{
                del(record);
            }
        },
        {
            text  : '风险解除审批',
            show  : pageType.value=='FENG_XIAN_CHU_LI' && !record.relieveApprovalStatus && hasPermission(['biz:projectCompany:edit'],'investment'),
            click : ()=>{
                drawer('HANDLE',record);
            }
        },
    ];
}
onMounted(() => {
    filterSubmit();
})

const del = (data)=>{
    Modal.confirm({
        title   : '操作确认',
        content : '是否确认删除该条数据？',
        onOk() {
            api.investment.correlationDel(data.id,apiKey.value).then(res=>{
                if(res.code==200){
                    getPage();
                }
            })
        }
    });
}
//drawer
const companyName    = inject('getAutoParams')('name');
const drawerYuJinRef = ref(null);
const drawerChuLiRef = ref(null);
const drawer         = (editType,record)=>{
    if(pageType.value=='FENG_XIAN_YU_JING'){
        drawerYuJinRef.value.open(editType,record || {},companyName.value);
    }
    if(pageType.value=='FENG_XIAN_CHU_LI'){
        drawerChuLiRef.value.open(editType,record || {},companyName.value);
    }
}
</script>
<style scoped lang="less">
.status_box {
  display     : inline-flex;
  align-items : center;
  .dot{
    width         : 8px;
    height        : 8px;
    border-radius : 50%;
    margin-right  : 8px;
  }
}
</style>
