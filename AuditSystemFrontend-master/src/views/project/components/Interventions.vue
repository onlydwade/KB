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
     
      <!-- <a-space :size="16" v-if="type == 'SHOW'">
        <a-button size="large" @click="handleClose">关闭</a-button>
      </a-space> -->
      <a-space :size="16">
        <a-tag v-if="(type == 'EDIT' || type == 'SHOW') && formData.interveneState !=null" class="status" :style="{backgroundColor:statesColor(formData.interveneState)}">
              {{interveneStatePlan(formData.interveneState)}}
        </a-tag>
        <a-tag v-if="type == 'EDIT' || type == 'SHOW'" class="status" :style="{backgroundColor:statesColor(props.data.value.interveneState)}">
              {{interveneStatePlan(props.data.value.interveneState)}}
        </a-tag>
        <a-button size="large" @click="handleClose">关闭</a-button>
        <a-button size="large" v-if="type == 'EDIT'" @click="handleStaging">暂存</a-button>
        <a-button size="large" type="primary"  v-if="type == 'ADD'&& hasPermission(['biz:projectIntervene:release']) || type == 'ADD'&&showStr == 'true'" @click="handleOk(0)">确认下达</a-button>
        <a-button size="large" type="primary"  v-if="((type == 'EDIT') && interveneState ==0  && hasPermission(['biz:projectIntervene:feedback'])) || ((type == 'EDIT') && interveneState ==0 &&showStr == 'true')" @click="handleOk(1)">确认反馈</a-button>
        <!-- <a-button size="large" type="primary" v-if="type == 'SHOW' && interveneState == 0" @click="handleOk(2)" v-permission="['biz:projectIntervene:feedback']">确认执行反馈</a-button> -->
        <a-button size="large" type="primary"  v-if="type == 'EDIT' && interveneState ==2 && hasPermission(['biz:projectIntervene:noFeedback']) || (type == 'EDIT' && interveneState ==2 &&showStr == 'true')" @click="handleRest(1)">重新反馈执行情况</a-button>
        <a-button size="large" type="danger"   v-if="type == 'SHOW' && interveneState ==1 && examineLevel ==0 && hasPermission(['biz:projectExtension:isNotPass']) || type == 'SHOW' && interveneState ==1 && examineLevel ==1 && hasPermission(['biz:projectExtension:isNotPassTwo'])"  @click="handleIsNoPass(2)">不通过</a-button>
        <a-button size="large" type="primary"  v-if="type == 'SHOW' && interveneState ==1 && examineLevel ==0 && hasPermission(['biz:projectExtension:isNotPass']) || type == 'SHOW' && interveneState ==1 && examineLevel ==1 && hasPermission(['biz:projectExtension:isNotPassTwo'])" @click="handleIsNoPass(3)">通过</a-button>
      </a-space>
    </template>
    <a-form layout="vertical" :model="formData" ref="formRef">
      <a-page-header :ghost="false"
            :breadcrumb="{ routes }">
            <template #footer>
                <a-tabs v-model:activeKey="tabKey"  @change="tabChange">
                    <a-tab-pane key="1" tab="执行情况"  v-if="type == 'EDIT' || type == 'SHOW'"/>
                    <a-tab-pane key="2" tab="干预方案"  v-if="type == 'EDIT' || type == 'SHOW'"/>
                    <a-tab-pane key="3" tab="评估详情"  v-if="type == 'EDIT' || type == 'SHOW'"/>
                </a-tabs>
            </template>
        </a-page-header>
        <div class="content-box_full" v-if="tabKey==1">
          <Title class="titlebar">
            <template #title>
              执行反馈
            </template>
          </Title>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="方案实施人">
                <UserSelect v-model:value="formData.schemeUserId" :users="formData.schemeUser" :disabled="disabled || type == 'EDIT'"/>
                <!-- <a-input
                  v-model:value="formData.assessTitle"
                  :disabled="disabled"
                  placeholder="请输入"
                /> -->
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="干预实施期限">
                <!-- <a-input
                  v-model:value="formData.interveneDeadline"
                  :disabled="disabled"
                  placeholder="请输入"
                /> -->
                <a-date-picker
                  v-model:value="formData.interveneDeadline"
                  :disabled="disabled || type == 'EDIT'"
                  class="w_full"
                  valueFormat="YYYY-MM-DD"
                  format="YYYY-MM-DD"
                  placeholder="请选择"
                />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="执行情况">
                <a-textarea allowClear   :disabled="type == 'SHOW'" show-count :maxlength="500" :rows="5" type="textarea" v-model:value="formData.executeCondition" placeholder="请输入(500字以内)" />
              </a-form-item>
            </a-col>
          </a-row>
          <Title title="文件上传">
            <a-form-item name="fileOk" 
                    :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
            </a-form-item>
            </Title>
            <ProjectTreeDocumentsList  :recordId="formData.id"  :menuId="menuId" :projectId="projectId" v-model="formData.fileOk" ref="documentsRef" :readOnly="type == 'SHOW'"/>
        </div>
        <div class="content-box_full" v-if="tabKey==2">
          <Title class="titlebar">
            <template #title>
              基础信息
            </template>
          </Title>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="干预方案名称">
                <a-input
                  v-model:value="formData.interveneSchemeName"
                  :disabled="disabled  || type == 'EDIT'"
                  placeholder="请输入"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item required label="对应评估内容">
                <!-- <a-input
                  v-model:value="formData.assessTitle"
                  :disabled="disabled  || type == 'EDIT'"
                  placeholder="请输入"
                /> -->
                <a-select
                          v-model:value="formData.assessId"
                          :disabled="disabled  || type == 'EDIT'"
                          class="w_full"
                          placeholder="请选择"
                          :options="postAssess">
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="方案实施人">
                <!-- <a-input
                  v-model:value="formData.schemeUserId"
                  :disabled="disabled"
                  placeholder="请输入"
                /> -->
                <UserSelect v-model:value="formData.schemeUserId" :users="formData.schemeUser" :disabled="disabled  || type == 'EDIT'"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="干预实施期限">
                <!-- <a-input
                  v-model:value="formData.interveneDeadline"
                  :disabled="disabled"
                  placeholder="请输入"
                /> -->
                <a-date-picker
                  v-model:value="formData.interveneDeadline"
                  :disabled="disabled  || type == 'EDIT'"
                  class="w_full"
                  valueFormat="YYYY-MM-DD"
                  format="YYYY-MM-DD"
                  placeholder="请选择"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <Title class="titlebar">
            <template #title>
              干预方案
            </template>
          </Title>
          <a-row :gutter="24">
            <a-col :span="24">
              <a-form-item label="经营反馈">
                <a-textarea allowClear  :disabled="disabled  || type == 'EDIT'" show-count :maxlength="500" :rows="5" type="textarea" v-model:value="formData.manageIntervene" placeholder="请输入(500字以内)" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="风险干预">
                <a-textarea allowClear  :disabled="disabled  || type == 'EDIT'" show-count :maxlength="500" :rows="5" type="textarea" v-model:value="formData.riskIntervene" placeholder="请输入(500字以内)" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="对客服务干预">
                <a-textarea allowClear  :disabled="disabled  || type == 'EDIT'" show-count :maxlength="500" :rows="5" type="textarea" v-model:value="formData.customerServiceIntervene" placeholder="请输入(500字以内)" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="品质干预">
                <a-textarea allowClear  :disabled="disabled  || type == 'EDIT'" show-count :maxlength="500" :rows="5" type="textarea" v-model:value="formData.qualityIntervene" placeholder="请输入(500字以内)" />
              </a-form-item>
            </a-col>
          </a-row>

        </div>
        <div class="content-box_full" v-if="tabKey==3">
          <Title class="titlebar">
            <template #title>
              基础信息
            </template>
          </Title>
          <a-row :gutter="24">
            <a-col :xxl="6" :lg="8" :sm="12">
              <a-form-item label="评估标题" >
                <a-input
                  v-model:value="formData2.assessTitle"
                  :disabled="disabled || type == 'EDIT'"
                  placeholder="请输入"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="评估年份">
                <a-date-picker
                  v-model:value="formData2.assessYear"
                  :disabled="disabled || type == 'EDIT'"
                  class="w_full"
                  picker="year"
                  valueFormat="YYYY"
                  @change="yearChange"
                />
              </a-form-item>
            </a-col>
            <a-col :span="4">
              <a-form-item label="评估阶段">
                <a-select
                  v-model:value="formData2.assessStage"
                  class="w_full"
                  placeholder="请选择"
                  :disabled="disabled || type == 'EDIT'"
                  @change="timeChange"
                >
                  <a-select-option value="4">年度</a-select-option>
                  <a-select-option value="3">半季度</a-select-option>
                  <a-select-option value="2">季度</a-select-option>
                  <a-select-option value="1">月度</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="6" style="padding-top: 30px;">
              <a-form-item>
                <a-select
                  v-model:value="formData2.assessStageDetails"
                  class="w_full"
                  placeholder="请选择"
                  :disabled="disabled || type == 'EDIT'"
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
                label="是否需下达干预方案"
                name="transmitState"
              >
                <a-select
                  v-model:value="formData2.transmitState"
                  :disabled="disabled || type == 'EDIT'"
                  class="w_full"
                  placeholder="请选择"
                >
                  <a-select-option :value="1">是</a-select-option>
                  <a-select-option :value="0">否</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="干预下达期限" name="transmitDeadline">
                <a-date-picker
                  v-model:value="formData2.transmitDeadline"
                  :disabled="disabled || type == 'EDIT'"
                  class="w_full"
                  valueFormat="YYYY-MM-DD"
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
              :columns="data.columns"
              :dataSource="dataSource"
              :pagination="false"
              :scroll="{ x: '100%' }"
            >
              <template #emptyText> 暂无数据，请添加 </template>
              <template #bodyCell="{ column, text, record, index }">
                <a-form-item
                  v-if="['riskDescribe','improveSuggest'].includes(column.dataIndex)"
                >
                  <a-input
                    v-if="editData[record.label]"
                    v-model:value="editData[record.label][column.dataIndex]"
                  />
                  <template v-else>
                    {{ text }}
                  </template>
                </a-form-item>
                <template v-if="column.key === 'action'">
                  <template v-if="!editData[record.label]">
                    <a-button
                      type="text"
                      class="color-primary"
                      v-if="!readOnly"
                      size="small"
                      @click="edit(record.label)"
                      :disabled="disabled || type == 'EDIT'"
                      >编辑</a-button
                    >
                  </template>
                  <a-space v-else>
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
            >
            </a-form-item>
          </Title>
          <ProjectTreeDocumentsList v-if="formData.id"  :recordId="assessIdUp"  :menuId="49" :projectId="projectId" v-model="formData.fileOk" ref="documentsRef" :readOnly="true"/>
      </div>
    </a-form>
  </a-drawer>
</template>
<script setup>
import api from '@/api/index'
import moment from "moment";
import { message } from 'ant-design-vue'
import { mainStore } from '@/store'
import { cloneDeep } from "lodash-es";
import { useDictStore } from '@/store/dict'
import { hasPermission } from '@/utils/tools';
const dict = useDictStore()
const store = mainStore()
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
  },
  data: {
    type: String,
    default: {}
  },
  showStr : Boolean
})

const emit = defineEmits(['success'])
const visible = ref(false)
const type = ref('ADD')
const interveneState = ref('')
const examineLevel = ref('')

const disabled = computed(() => type.value == 'SHOW')
const title = ref('')
const formData = ref({})
const formData2 = ref({})
const formRef = ref(null)
const submitLoading = ref(false)
const documentRef = ref(null)
const assessIdUp = ref(null)
const assessId = ref(null)
const loadding  = ref(false);
const router     = useRouter();
const route      = useRoute();
const tabKey     = ref('2');
const urlReplace = ref(false);
const displayStatus = ref(false);
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
const tabChange  = (key)=>{
    urlReplace.value = true;
    let query        = {...route.query,tab:key};
    router.replace({query:query});
    if (key == 3) {
      assessIdUp.value = props.data.value.assessId
      esInfo(props.data.value)
    } else if (key == 1 || key == 2) {
      projectInterveneGet(props.data.value)
    }
}
// 接收SecondMenu.vue空的函数-更新节点状态进度
const statusProgress = inject('statusProgress')
const handleStaging =() =>{
  let apiKey = 'projectInterveneEdit'
        formData.value.projectId = props.projectId
        api.project[apiKey](formData.value, 'projectIntervene').then(
          async (res) => {
            if (res.code == 200) {
              emit('success')
              message.success('操作成功')
            }
            submitLoading.value = false
          }
        )
}
const handleRest = (state) => {
  handleOk(state)
}
const handleOk = (state) => {
  if(state ==2){
    displayStatus.value = true
    return;
  }
  formRef.value
    .validateFields()
    .then((vRes) => {
      if(type.value == 'ADD'&&(formData.value.assessId ==null || formData.value.assessId == undefined || formData.value.assessId =='')){
        return message.warning('请选择对应评估内容')
      }
      let apiKey = type.value == 'ADD' ? 'projectInterveneAdd' : 'projectInterveneEdit'
      if(state == 0 ){
        formData.value.interveneState = 0
      }else if(state == 1 ){
        formData.value.interveneState = 1
      }
      formData.value.projectId = props.projectId
      api.project[apiKey](formData.value, 'projectIntervene').then(
        async (res) => {
          if (res.code == 200) {
            // if (type.value == 'ADD') {
            //   let uploadResult = await documentRef.value.batchUpLoad(
            //     res.data.id
            //   )
            // }
            emit('success')
            message.success('操作成功')
            visible.value = false
            // displayStatus.value = false
          }else{
            message.erroe(res.msg)
          }
          submitLoading.value = false
        }
      )
    })
    .catch((err) => {
      message.warning('请完善必填信息！')
    })
}
// 通过or不通过
const handleIsNoPass= (state) => {
  let apiKey = 'projectInterveneIsNotPass'
  let postData ={
    interveneState:state,
    id: assessId.value
  }
  api.project[apiKey](postData, 'projectIntervene').then(
        async (res) => {
          if (res.code == 200) {
            let state = res.data.data.interveneState
            emit('success')
            message.success('操作成功')
            if(state == 3){
              projectStepExpansionInfo()
            }
            visible.value = false
          }
          submitLoading.value = false
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
            message.erroe(res.msg)
          }
      })
}
const handleClose = () => {
  visible.value = false
  // displayStatus.value = false
}
const titleObj = { ADD: '新增', EDIT: '修改', SHOW: '查看' }
const open = (key, data, dfaultType) => {
  type.value = key
  examineLevel.value = data.examineLevel
  interveneState.value = data.interveneState
  try {
    if(key !== 'ADD'){
      if(tabKey.value =='1' || tabKey.value =='2'){
        projectInterveneGet(data)
      }else if(tabKey.value =='3'){
        esInfo(data)
      }
      
    }
  } catch (e) {
    type.value = 'ADD'
    formData.value = {}
  }
  title.value = type.value =='ADD' ? '干预下达' :'干预执行'
  if (key == 'ADD') {
    tabKey.value='2'
    formData.value.type = dfaultType
    dataSource.value = null
    formData.value = {}
    selectOptions.value=null
  }
  assessList()
  visible.value = true
}

const data = reactive({
    list: [],
    columns : [
    {
        title     : '风险类型',
        dataIndex : 'label',
        width: 150
    },
    {
        title     : '风险描述',
        dataIndex : 'riskDescribe',
        width: 150
    },
    {
        title     : '改进建议',
        dataIndex : 'improveSuggest',
        width: 150
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
// 评估详情
const esInfo = (data)=>{
  loadding.value = true;
  if(data.assessId){
  api.project.projectAssessInfo('projectAssess',49,data.assessId).then(res=>{
        if(res.code==200){
          timeChange(res.data.assessStage)
          formData2.value = JSON.parse(JSON.stringify(res.data))
          dataSource.value = res.data.projectAssessSuggestVos;
        }
        loadding.value = false;
    })
  }else{
    return message.error('无对应评估内容，暂无详情')
  }
}

const projectInterveneGet = (data)=>{
  api.project.projectInterveneInfo('projectIntervene',props.menuId,data.id).then(res=>{
        if(res.code==200){
          formData.value = JSON.parse(JSON.stringify(res.data))
          formData.value.assessId = res.data.assessId
          assessId.value = data.id
        }
        loadding.value = false;
    })

}
const selectOptions = ref([])
const timeChange = (val) => {
  formData2.value.assessStageDetails = null
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
    formData2.value.assessStageDetails = formData2.value.assessYear
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
const postAssess = ref([]);
const assessList  = ()=>{
            let postData={
              projectId:props.projectId
            }
            api.project.projectAssessList(postData).then(res=>{
                if(res.code==200){
                  postAssess.value = res.data.map((item)=>{
                        return {
                            value : item.id,
                            label : item.assessTitle,
                        }
                    });
                }
            })
    }
defineExpose({ open })
</script>
<style scoped lang="less">
.titlebar {
  margin: 16px -16px;
  &:first-child {
    margin-top: -16px;
  }
}
.status{
    color: white;
    border-radius: 40px;
    width: 100px;
    text-align: center;
    height: 26px;
    line-height: 26px;
    position: fixed;
    top: 24px;
    right: 766px;
}
</style>
