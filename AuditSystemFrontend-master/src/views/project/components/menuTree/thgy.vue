
<template>
    <div class="menu_inner padding_box">
      <Title title="干预记录">
          <template #right>
            <a-button v-if="(hasPermission(['biz:projectIntervene:skip']))"  @click="drawerInfo()">
                  标记跳过此环节
              </a-button>
              <a-button v-if="(hasPermission(['biz:projectIntervene:addTransmit']))" type="primary" @click="drawer('ADD')">
                  下达干预方案
              </a-button>
          </template>
      </Title>
      <a-table :columns="data.columns" :loading="loadding" :data-source="data.list" rowKey="id"  :pagination="false" :scroll="{ x: '100%' }">
          <template #bodyCell="{ column,text,record,index }">
              <template v-if="column.key === 'sort'">
                  {{(filterForm.pageNo-1)*filterForm.pageSize + index +1}}
              </template>
              <template v-if="column.key === 'interveneState'">
                  <a-tag class="status" :style="{backgroundColor:statesColor(record.interveneState)}">
                    {{interveneStatePlan(record.interveneState)}}
                  </a-tag>
              </template>
              <template v-if="column.key === 'assessState'">
                  {{assessStage(record.projectAssess?record.projectAssess.assessState:'')}}
              </template>
              <!-- 方案下达期限 -->
              <template v-if="column.key === 'transmitDeadline'">
                  {{record.projectAssess?dateFormat(record.projectAssess.transmitDeadline,'YYYY-MM-DD'):''}}
              </template>
              <!-- 方案下达日期 -->
              <template v-if="column.key === 'createTime'">
                  {{dateFormat(record.createTime,'YYYY-MM-DD')}}
              </template>
              <!-- 干预实施期限 -->
              <template v-if="column.key === 'interveneDeadline'">
                  {{dateFormat(record.interveneDeadline,'YYYY-MM-DD')}}
              </template>
              <!-- 实施反馈日期 -->
              <template v-if="column.key === 'feedbackTime'">
                  {{dateFormat(record.feedbackTime,'YYYY-MM-DD')}}
              </template>
              <!-- 关闭日期 -->
              <template v-if="column.key === 'closeTime'">
                  {{dateFormat(record.closeTime,'YYYY-MM-DD')}}
              </template>
              <template v-if="column.key === 'schemeIsnotEmbodiment'">
                  {{schemeIsnotStage(record.schemeIsnotEmbodiment)}}
              </template>
              
              <!-- <template v-if="column.key === 'action'">
                  <actionBtn :actions="actions(record,index)"/>
              </template> -->
              <template v-if="column.key === 'action'">
                    <a-button type="text" class="color-primary" size="small" v-if="(hasPermission(['biz:projectIntervene:show']))" @click="drawer('SHOW',record)">查看</a-button>
                    <a-button type="text" class="color-primary" size="small" v-if="(record.interveneState==0&&hasPermission(['biz:projectIntervene:feedback'])&&showStr == 'false') || (record.interveneState==0&&showStr == 'true')"  @click="drawer('EDIT',record)">反馈执行情况</a-button>
                    <a-button type="text" class="color-primary" size="small" v-if="(record.interveneState==2&&hasPermission(['biz:projectIntervene:noFeedback'])&&showStr == 'false') || (record.interveneState==2&&showStr == 'true')" @click="drawer('EDIT',record)">重新反馈执行情况</a-button>
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
  
  <!-- <XmfkDrawer ref="drawerRef" :companyId="companyId" :menuId="menuInfo.id" @success="getPage"/> -->
  <Interventions ref="drawerRef" :showStr="showStr" :projectId="projectId" :companyId="companyId" :menuId="menuInfo.id" @success="getPage" :data="recordData" />
</template>
<script setup>
import api                 from '@/api/index';
// import XmfkDrawer          from '../correlation/XmfkDrawer.vue';
import Interventions  from '../../components/Interventions.vue'
import { dateFormat }      from '@/utils/tools';
import { message , Modal } from 'ant-design-vue';
import { hasPermission}   from '@/utils/tools';
const emit = defineEmits(['ok1'])
const props = defineProps({
  projectId : {
      type    : Number,
      default : 0,
  },
  companyId: {
      type    : Number,
      default : 0,
  },
  menuInfo : {
      type    : Object,
      default : {},
  }
})

const loadding   = ref(false);
const route     = useRoute();
const showStr = ref(route.query.show)
const projectId = ref(Number(route.query.id || 0));
const filterForm = reactive({
  pageNo   : 1,
  pageSize : 10,
})
const interveneStatePlan = (val) => {
    if(val === 0) {
        return '干预待执行'
    }
    if(val === 1) {
        return '待检查'
    }
    if(val === 2) {
        return '执行不通过'
    }
    if(val === 3) {
        return '已完成'
    }
}
const data = reactive({
  list : [],
  columns : [
      {
          title     : '干预方案',
          dataIndex : 'interveneSchemeName',
          required  : true,
          width     : 180
      },
      {
          title     : '对应评估记录标题',
          dataIndex : ['projectAssess','assessTitle'],
          width     : 200
      },
      {
          title     : '评估人',
          dataIndex : ['projectAssess','assessUserName'],
          width     : 180
      },
      {
          title     : '评估阶段',
          key       : 'assessState',
          width     : 180,
      },
      {
          title     : '方案下达期限',
          key       : 'transmitDeadline',
          width     : 180
      },
      {
          title     : '方案下达人',
          dataIndex : ['createUser','realname'],
          width     : 180
      },
      {
          title     : '方案下达日期',
          key       : 'createTime',
          width     : 180,
      },
      {
          title     : '干预实施期限',
          key       : 'interveneDeadline',
          width     : 180
      },
      {
          title     : '方案是否被实施',
          key       : 'schemeIsnotEmbodiment',
          width     : 180
      },
      {
          title     : '方案实施人',
          dataIndex : ['schemeUser','realname'],
          width     : 180
      },
      {
          title     : '干预执行状态',
          key       : 'interveneState',
          width     : 180
      },
      {
          title     : '实施反馈日期',
          key       : 'feedbackTime',
          width     : 180
      },
      {
          title     : '关闭日期',
          key       : 'closeTime',
          width     : 180
      },
      {
          title : '操作',
          key   : 'action',
          width : 250,
          fixed : 'right'
      },
  ],
  total : 0,
})
const getPage = ()=>{
  let postData = {
      desc     : ['createTime'],
      pageNo   : filterForm.pageNo,
      pageSize : filterForm.pageSize,
      params   : {
          projectId : projectId.value
      }
  }
  loadding.value = true;
  api.project.projectIntervenePage(postData,'projectIntervene',props.menuInfo.id).then(res=>{
      if(res.code==200){
          data.list  = res.data.records;
          data.total = res.data.total;
      }
      loadding.value = false;
  })
}
const summary = ref({});
const filterSubmit = ()=>{
  filterForm.pageNo = 1;
  getPage();
}
onMounted(() => {
  filterSubmit();
})
// const actions = (record,index)=>{
//   return [
//       {
//           text  : '查看',
//           show  : true,
//           click : ()=>{
//               drawer('SHOW',record);
//           }
//       },
//       {
//           text  : '反馈执行情况',
//           show  : true,
//           click : ()=>{
//               drawer('EDIT',record);
//           }
//       },
//       {
//           text  : '删除',
//           show  : true,
//           click : ()=>{
//               del(record);
//           }
//       },
//   ];
// }
const del = (data)=>{
  Modal.confirm({
      title   : '操作确认',
      content : '是否确认删除该条评估记录？',
      onOk() {
          api.project.projectAssessDel(data.id).then(res=>{
              if(res.code==200){
                  getPage();
              }else{
                message.erroe(res.msg);
              }
          })
      }
  });
}
// 跳过
const drawerInfo =()=>{
    Modal.confirm({
      title   : '操作确认',
      content : '是否确认跳过此环节？',
      onOk() {
        let postData = {
            projectId : props.projectId,
            stepMenuId : props.menuInfo.id,
            status : 1
    }
    api.project.projectStepExpansion(postData).then(res=>{
          if(res.code==200){
            getPage()
            emit('ok1')
          }else{
            message.erroe(res.msg);
          }
      })
      }
  });
}
//drawer
const drawerRef = ref(null);
const recordData = []
const drawer    = (type,record)=>{
  let dfaultType = data.total==0
  recordData.value = record
  //?'SHOU_CI_CHU_ZI':'ZHUI_JIA_SHI_JIAO_ZHU_CE_ZI_BEN';
  drawerRef.value.open(type,record || {});
}
const assessStage = (val) => {
    if(val === '') {
        return ''
    }
    if(val === 1) {
        return '月度'
    }
    if(val === 2) {
        return '季度'
    }
    if(val === 3) {
        return '半季度'
    }
    if(val === 4) {
        return '年度'
    }
}
const schemeIsnotStage = (val) => {
    if(val === 0) {
        return '否'
    }
    if(val === 2) {
        return '是'
    }
}

const statesColor = (val) =>{
    let colorStr = ""
    switch(val) {
        case 0:
            colorStr= '#f99c34'
            break;
        case 1:
            colorStr= '#02a7f0'
            break;
        case 2:
            colorStr= '#d9001b'
            break;
        case 3:
            colorStr= '#63a103'
            break;
        default:
            colorStr= ''
    }
    return colorStr
}
</script>

<style scoped lang="less">
.status{
    color: white;
    border-radius: 40px;
    width: 60%;
    text-align: center;
}
.ant-tag {
    border: 0;
}
</style>
