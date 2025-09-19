<template>
  <a-drawer
    :maskClosable="false"
    :width="1024"
    :title="title"
    destroyOnClose
    @close="handleClose"
    :visible="visible"
  >
    <template #extra>
      <a-space :size="16" v-if="type == 'SHOW'">
        <a-button size="large" @click="handleClose">关闭</a-button>
      </a-space>
      <a-space :size="16" v-else>
        <a-button size="large" @click="handleClose">取消</a-button>
        <a-button size="large" v-if="type == 'ADD'"  @click="handleStaging">暂存</a-button>
        <a-button size="large" type="primary" @click="handleOk">保存</a-button>
      </a-space>
    </template>
    <a-form layout="vertical" :model="formData" ref="formRef">
      <Title class="titlebar">
        <template #title>
          基础信息
          <span class="color-danger"> * </span>
        </template>
      </Title>
      <a-row :gutter="24">
        <a-col :xxl="6" :lg="8" :sm="12">
          <a-form-item required label="评估标题" name="assessTitle">
            <a-input
              v-model:value="formData.assessTitle"
              :disabled="disabled"
              placeholder="请输入"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item   required label="评估年份" name="assessYear">
            <a-date-picker
              v-model:value="formData.assessYear"
              :disabled="disabled"
              class="w_full"
              picker="year"
              valueFormat="YYYY"
              @change="yearChange"
            />
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item   required label="评估阶段"  name="assessStage">
            <a-select
              v-model:value="formData.assessStage"
              class="w_full"
              placeholder="请选择"
              :disabled="disabled"
              @change="timeChange"
            >
              <a-select-option value="4">年度</a-select-option>
              <a-select-option value="3">半季度</a-select-option>
              <a-select-option value="2">季度</a-select-option>
              <a-select-option value="1">月度</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6" style="padding-top: 30px;"  >
          <a-form-item  required name="assessStageDetails">
            <a-select
              v-model:value="formData.assessStageDetails"
              class="w_full"
              placeholder="请选择"
              :disabled="disabled"
              :options="selectOptions"
            >
              <!-- <a-select-option value="4">年度</a-select-option>
              <a-select-option value="3">半季度</a-select-option>
              <a-select-option value="2">季度</a-select-option>
              <a-select-option value="1">月度</a-select-option> -->
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            required
            label="是否需下达干预方案"
            name="transmitState"
          >
            <a-select
              v-model:value="formData.transmitState"
              :disabled="disabled"
              class="w_full"
              placeholder="请选择"
              @change="ruleChange"
            >
              <a-select-option :value="1">是</a-select-option>
              <a-select-option :value="0">否</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item :required="formData.transmitState==1 ?true:false" label="干预下达期限" name="transmitDeadline">
            <a-date-picker
              v-model:value="formData.transmitDeadline"
              :disabled="disabled"
              class="w_full"
              valueFormat="YYYY-MM-DD 00:00:00"
              format="YYYY-MM-DD"
              placeholder="请选择"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <Title>
        <template #title> 风险评估 </template>
      </Title>
      <a-form-item name="terminationDocumentArr">
        <a-table
          :columns="tableData.columns"
          :dataSource="dataSource"
          :pagination="false"
          :scroll="{ x: '100%' }"
        >
          <template #emptyText> 暂无数据，请添加 </template>
          <template #bodyCell="{ column, text, record, index }">
            <template v-if="['riskDescribe','improveSuggest'].includes(column.dataIndex)">
                <a-input
                  v-if="editData[record.label]"
                  v-model:value="editData[record.label][column.dataIndex]"
                />
                <template v-else> {{ text }}</template>
            </template>
            <template v-else-if="column.key === 'action'">
              <template v-if="!editData[record.label]">
                <a-button
                  type="text"
                  class="color-primary"
                  size="small"
                  @click="edit(record.label)"
                  :disabled="disabled"
                  >编辑</a-button
                >
              </template>
              <a-space v-else-if="editData[record.label]">
                <a-button
                  type="primary"
                  shape="circle"
                  @click="editSubmit(record.label)"
                  size="small"
                >
                  <template #icon><check-outlined /></template>
                </a-button>
                <a-button
                  shape="circle"
                  @click="editCancel(record.label)"
                  size="small"
                >
                  <template #icon><close-outlined /></template>
                </a-button>
              </a-space>
            </template>
          </template>
        </a-table>
      </a-form-item>

      <Title class="titlebar" title="文件上传">
        <a-form-item
          name="fileOk"
          :rules="{ required: true, message: '( 请上传必传文件后提交!!! )' }"
        >
        </a-form-item>
      </Title>
      <!-- <ProjectTreeDocumentsList
        v-model="formData.fileOk"
        v-if="visible"
        ref="documentRef"
        :companyId="companyId"
        :menuId="menuId"
        :recordId="formData.id"
        :readOnly="disabled"
      /> -->
      <ProjectTreeDocumentsList :key="componentKey" :recordId="formData.id"  :menuId="menuId" :projectId="projectId" v-model="formData.fileOk" ref="documentRef" :readOnly="disabled"/>
    </a-form>
  </a-drawer>
</template>
<script setup>
import api from '@/api/index'
import moment from "moment";
import { message } from 'ant-design-vue'
import { mainStore } from '@/store'
const store = mainStore()
import { useDictStore } from '@/store/dict'
import { cloneDeep } from "lodash-es";
const dict = useDictStore()

const props = defineProps({
  projectId : {
        type    : Number,
        default : 0,
  },
  companyId: {
    type: Number,
    default: 0
  },
  menuId: {
    type: Number,
    default: 43
  }
})

const emit = defineEmits(['success'])
const visible = ref(false)
const type = ref('ADD')
const disabled = computed(() => type.value == 'SHOW')
const title = ref('')
const formData = ref({})
const formRef = ref(null)
const submitLoading = ref(false)
const documentRef = ref(null)
const loadding  = ref(false);
// 接收SecondMenu.vue空的函数-更新节点状态进度
const statusProgress = inject('statusProgress')
const handleStaging =() =>{
  let apiKey = 'projectAssessAdd'
      formData.value.projectId = props.projectId
      formData.value.companyId = props.companyId
      formData.value.projectAssessSuggestVos = dataSource.value
        formData.value.assessState = 0
        api.project[apiKey](formData.value, 'projectAssess').then(
          async (res) => {
            if (res.code == 200) {
              documentRef.value.batchUpLoad(
                  res.data.id
                )
              emit('success')
              message.success('操作成功')
              componentKey.value++
              visible.value = false
            }
            submitLoading.value = false
          }
        )
}
const componentKey =ref(0)
const handleOk = () => {
  
  formRef.value
    .validateFields()
    .then((vRes) => {
      let apiKey = type.value == 'ADD' ? 'projectAssessAdd' : 'projectAssessEdit'
      formData.value.projectId = props.projectId
      formData.value.companyId = props.companyId
      formData.value.projectAssessSuggestVos = dataSource.value
      if(type.value == 'ADD'){
        formData.value.assessState = 1
      }
      api.project[apiKey](formData.value, 'projectAssess').then(
        async (res) => {
          if (res.code == 200) {
            if (type.value == 'ADD') {
               documentRef.value.batchUpLoad(
                res.data.id
              )
            }
            emit('success')
            message.success('操作成功')
            projectStepExpansionInfo()
            componentKey.value++
            visible.value = false
          }
          submitLoading.value = false
        }
      )
    })
    .catch((err) => {
      message.warning('请完善必填信息！')
    })
}

const handleClose = () => {
  visible.value = false
}
const titleObj = { ADD: '新增', EDIT: '修改', SHOW: '查看' }
const open = (key, data, dfaultType) => {
  type.value = key
  try {
    if(key !== 'ADD'){
    formData.value = JSON.parse(JSON.stringify(data))
      esInfo(data)
      timeChange(data.assessStage)
    }
  } catch (e) {
    type.value = 'ADD'
    formData.value = {}
  }
  title.value = titleObj[type.value] + '经营评估'
  if (key == 'ADD') {
    formData.value.type = dfaultType
    dataSource.value = []
    // 重置风险评估列表
    tableData.list.forEach(item=>{
        item.riskDescribe = null
        item.improveSuggest = null
    })
    dataSource.value = tableData.list
    formData.value = {}
    selectOptions.value=null
    // formData.value.processStatus = 'FOU'
  }

  visible.value = true
}
// 是否干预下达方案，否时下达期限非必填，是时下达期限为必填
const required = ref(true) 
const ruleChange = (val) => {
  if (val == 0) {
    required.value = false
  } else {
    required.value = true
  }
}
const tableData = reactive({
    list: [
      {
        label:'法律风险',
        riskDescribe:'',
        improveSuggest:''
      },
      {
        label:'隐患风险',
        riskDescribe:'',
        improveSuggest:''
      },
      {
        label:'合同风险',
        riskDescribe:'',
        improveSuggest:''
      },
    ],
    columns : [
    {
        title     : '风险类型',
        dataIndex : 'label',
        width: 150
    },
    {
        title     : '风险描述',
        dataIndex : 'riskDescribe',
        width: 150,
        editType: 'deptName',
    },
    {
        title     : '改进建议',
        dataIndex : 'improveSuggest',
        width: 150,
        editType: 'deptName',
    },
    {
        title : '操作',
        key   : 'action',
        width : 200,
        fixed : 'right'
    },
  ]
})
const dataSource = ref([])
const esInfo = (data) => {
  loadding.value = true;
  api.project
    .projectAssessGet("projectAssess", props.menuId, data.id)
    .then((res) => {
      if (res.code == 200) {
        formData.value = JSON.parse(JSON.stringify(res.data));
        // tableData.list = res.data.projectAssessSuggestVos;
        // dataSource.value = res.data.projectAssessSuggestVos;
        // console.log("000", tableData.list);
        if (res.data.projectAssessSuggestVos==null) {
          dataSource.value = tableData.list
        } else {
          dataSource.value = res.data.projectAssessSuggestVos;
        }
      }
      loadding.value = false;
    })
}
const projectStepExpansionInfo = ()=>{
    let postData = {
            projectId : props.projectId,
            stepMenuId : props.menuId,
            status : 1
    }
    api.project.projectStepExpansion(postData).then(res=>{
          if(res.code==200){
            statusProgress()
          }else{
          }
      })
}
// 评估阶段下拉框
const selectOptions = ref([])
const timeChange = (val) => {
  formData.value.assessStageDetails = null
  selectOptions.value = []
  if (val === '1') {
    selectOptions.value = dict.optionsAll('DIAO_YAN_YUE_DU')
    selectOptions.value.shift()
    // dict.optionsAll('DIAO_YAN_YUE_DU').map(i=>{
    //   if(i.disabled == false) {
    //     selectOptions.value.push({
    //       label:i.label,
    //       value:i.value
    //     })
    //   }
    // })
  }
  if (val === '2') {
    selectOptions.value = dict.optionsAll('DIAO_YAN_JI_DU')
    selectOptions.value.shift()
  }
  if (val === '3') {
    selectOptions.value = dict.optionsAll('DIAO_YAN_BAN_NIAN_DU')
    selectOptions.value.shift()
  }
  if (val === '4') {
    // formData.value.assessStageDetails = moment(new Date()).format("YYYY");
    // 评估阶段选择年度,评估年份与评估阶段控制一致
    // formData.value.assessYear = formData.value.assessStageDetails
    formData.value.assessStageDetails = formData.value.assessYear
  }
}
const yearChange = (val) => {
  if(!val){
    formData.value.assessStage = ''
    formData.value.assessStageDetails = ''
  }
  // 评估阶段为年度，更改评估年份，评估阶段年度与之一致
  if (formData.value.assessStage == 4) {
    formData.value.assessStageDetails = val
  }
}
const editData = reactive({});
const edit = (key) => {
  editData[key] = cloneDeep(
    dataSource.value.filter((item) => key === item.label)[0]
  );
};
const editSubmit = (key) => {
  Object.assign(
    dataSource.value.filter((item) => key === item.label)[0],
    editData[key]
  );
  delete editData[key];
};
const editCancel = (key) => {
  delete editData[key];
};
defineExpose({ open });
</script>
<style scoped lang="less">
.titlebar {
  margin: 16px -16px;
  &:first-child {
    margin-top: -16px;
  }
}
</style>
