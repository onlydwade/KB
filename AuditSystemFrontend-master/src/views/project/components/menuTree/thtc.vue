
<template>
  <div class="menu_inner">
      <a-form
      ref="formRef"
      layout="vertical"
      :model="formData">
      <div class="padding_box">
          <Title>
              <template #title>
                请示内容
                  <span class="color-danger"> * </span>
              </template>
          </Title>
          <a-row :gutter="24">
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item required label="主题" name="title">
              <a-input
                v-model:value="formData.title"
                placeholder="请输入"
              />
            </a-form-item>
          </a-col>
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item
              label="项目名称"
            >
              <a-input
                :disabled="true"
                v-model:value="formData.projectName"
                placeholder="请输入"
              />
            </a-form-item>
          </a-col>
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item required label="申请日期" name="approvalSendTime">
              <a-date-picker
                :getPopupContainer="(trigger) => trigger.parentNode"
                v-model:value="formData.approvalSendTime"
                class="w_full"
                valueFormat="YYYY-MM-DD 00:00:00"
                format="YYYY-MM-DD"
                placeholder="请选择"
              />
            </a-form-item>
          </a-col>
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item
              required
              label="申请人归属"
              name="approvalAffiliation"
            >
              <a-select
                v-model:value="formData.approvalAffiliation"
                class="w_full"
                placeholder="请选择"
                :options="[
                  { label: '物业公司总部', value: '1' },
                  { label: '地区公司', value: '2' },
                  { label: '物业项目', value: '3' }
                ]"
              >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item
              required
              label="申请人"
              name="approvalUserId"
            >
              <UserSelect v-model:value="formData.approvalUserId" :users="formData.approvalUser"  @change="onChange" />
            </a-form-item>
          </a-col>
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item
              required
              label="联系电话"
              name="relationPhone"
            >
              <a-input
                v-model:value="formData.relationPhone"
                placeholder="请输入"
              />
            </a-form-item>
          </a-col>
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item
              label="所属公司"
            >
              <a-input
                disabled
                v-model:value="formData.companyName"
                placeholder="请输入"
              />
            </a-form-item>
          </a-col>
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item
              label="所属部门"
            >
              <a-input
                disabled
                v-model:value="formData.businessDeptName"
                placeholder="请输入"
              />
            </a-form-item>
          </a-col>
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item
              required
              label="是否涉及金额"
              name="whetherInvolveMoney"
            >
              <a-select
                v-model:value="formData.whetherInvolveMoney"
                class="w_full"
                placeholder="请选择"
                :options="[
                  { label: '是', value: 1 },
                  { label: '否', value: 2 }
                ]"
              >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item
              required
              label="涉及金额"
              name="involveMoney"
            >
              <a-input-number
                v-model:value="formData.involveMoney"
                :min="0"
                class="w_full"
                :formatter="
                  (value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                "
                :parser="(value) => value.replace(/\\s?|(,*)/g, '')"
                :addon-after="amountUnit(formData.involveMoney)"
              />
            </a-form-item>
          </a-col>
          <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item
              required
              label="金额大写"
              name="majusculeMoney"
            >
              <a-input
                v-model:value="formData.majusculeMoney"
                :min="0"
                class="w_full"
              />
            </a-form-item>
          </a-col>
          <!-- <a-col :xxl="8" :lg="8" :sm="12">
            <a-form-item
              label="当年转化金额(单位：元)"
              name="annualConversionAmount"
            >
              <a-input-number
                :value="formData.annualConversionAmount"
                :min="0"
                class="w_full"
                placeholder="自动计算"
                :formatter="
                  (value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                "
                :parser="(value) => value.replace(/\\s?|(,*)/g, '')"
                :addon-after="amountUnit(formData.annualConversionAmount)"
              />
            </a-form-item>
          </a-col> -->
          </a-row>
          <a-form-item label="退出描述">
              <a-textarea  allowClear :rows="3" type="textarea" v-model:value="formData.content" placeholder="请输入(500字以内)" show-count :maxlength="500"/>
          </a-form-item>

          <Title class="titlebar" title="文件上传">
              <a-form-item name="fileOk">
              </a-form-item>
          </Title>
          <ProjectTreeDocumentsList  :recordId="-1" :companyId="companyId"  :menuId="menuId" :projectId="projectId" v-model="formData.fileOk" ref="documentsRef"/>
          </div>
        </a-form>
  </div>
  <FooterBarL>
    <StopOaBtn :showStr="showStr" @submit="handleOk" :projectId="projectId" :oaTempId="oaTempId"  :menuInfo="menuInfo" :approvalStatus="approvalStatus"  place="topLeft" @getOaStatus="getOaStatus" :projectInfo="projectInfo"></StopOaBtn>
      <!-- <a-button size="large" type="primary" v-if="formData.approvalUrl == null" v-permission="['biz:projectExtension:createOa']"  @click="handleOk">发起OA审批</a-button>
      <a-button size="large" type="primary" v-if="formData.approvalUrl !=null" v-permission="['	biz:projectExtension:reOa']" @click="handleOk">重新发起OA审批</a-button>
      <a-button size="large" type="primary" v-if="formData.approvalUrl !=null" v-permission="['biz:projectExtension:createNewOa']" @click="handleOk">创建新OA流程审批</a-button> -->
    <a-divider type="vertical" v-if="projectInfo.processState!==2" style="width: 2px; background-color: #f99c34"/>
    <a-button size="large" v-if="projectInfo.processState!==2" v-permission="['biz:projectExtension:exitStora']" @click="handleStorage">暂存</a-button>
  </FooterBarL>
</template>
  <script setup>
  import api           from '@/api/index';
  import { message ,Modal}   from 'ant-design-vue';
  import { mainStore } from '@/store';
  const router = useRouter();
  import { amountUnit } from '@/utils/tools'
  import Achievement from '../correlation/Achievement.vue'
  import { useMenuTree } from './menu';
  import StopOaBtn     from '../StopOaBtnTh.vue';
  const bus      = inject('bus');
  const store = mainStore();
  const emit = defineEmits(['ok1'])
  const props = defineProps({
      companyId: {
          type    : Number,
          default : 0,
      },
      menuInfo : {
          type    : Object,
          default : {},
      },
      menuId: {
          type    : Number,
          default : 0,
      },
      projectInfo:{
        type    : Object,
        default : {},
      },
  })
  const route     = useRoute();
  const projectId = ref(Number(route.query.id || 0))
  const formRef         = ref(null);
  const documentsRef    = ref(null);
  const formData        = ref({});
  const oaTempId = ref('1869c78375cc1506b0da03d45778aa0e');
  const showStr = ref(route.query.show)
  const oaStart     = (type,temp)=>{
    formRef.value.validateFields().then(vRes=>{
        let postData = {
            id             : temp.oaId,
            subject        : formData.value.projectName+'-'+props.menuInfo.name,
            recordId       : projectId.value,
            subRecordId    : props.menuInfo.id,
            templateId     : '1869c78375cc1506b0da03d45778aa0e',
            moduleName     : 'ProjectExtension',
            approvalStatus : 5,
            // stepCode       : props.menuInfo.code,
            subModuleName  : 'ProjectExtensionExit',
            submitUserId  : store.userInfo.userId,
            mainProcess    : true,
            detailUrl      : window.location.origin+'/#/extensionOaInfo?id='+projectId.value+'&to='+props.menuInfo.code
        }
        let apiKey = type== -1?'oaUpdate':'oaAdd';
        api.common[apiKey](postData).then(async res=>{
            if(res.code==200){
               const approvalStatus = res.data.approvalStatus
               const approvalUrl =  res.data.approvalUrl
                message.success('操作成功！');
                bus.emit('oaHasSubmit');
                handleSubmit(approvalStatus,approvalUrl)
            }
        })
      }).catch(err=>{
          message.warning('请完善必填信息！');
      })
    }

  const offlinePass = (type,temp) => {
    formRef.value.validateFields().then(vRes=>{
      formData.value.projectId = projectId.value;
      api.project.offlinePass(formData.value).then(res => {
        if (res.code == 200) {
          message.success('操作成功！');
          router.push('/project/walkOff');
        }
      })
    }).catch(err=>{
      message.warning('请完善必填信息！');
    })
  }
  //暂存
  const handleStorage = ()=>{
    formData.value.projectId = projectId.value;
    api.project.storage(formData.value).then(res => {
      if(res.code==200){
        message.success('操作成功');
        getInfo();
      }
    })
  }

  const handleOk = (type,temp)=>{
      //oaStart(type,temp);
      Modal.confirm({
        title   : '操作确认',
        content : '是否确认退场？',
        onOk() {
          offlinePass(type,temp);  //退场改为线下审批
        }
      });
  }
  const handleSubmit = (approvalStatus,approvalUrl)=>{
    // formRef.value.validateFields().then(vRes=>{
          let apiKey = formData.value.id==null?'projectExtensionExitAdd':'projectExtensionExitEdit';
          formData.value.projectId = projectId.value;
          formData.value.temporary = 1;
          formData.value.approvalStatus = approvalStatus;
          formData.value.approvalUrl = approvalUrl;
          api.project[apiKey](formData.value,'projectExtensionExit').then(async res=>{
              if(res.code==200){
                  // if(type.value=='ADD'){
                  //     let uploadResult = await documentsRef.value.batchUpLoad(res.data.id);
                  // }
                  message.success('操作成功');

                  // projectStepExpansionInfo()
                  getInfo();
              }
          })
      // }).catch(err=>{
      //     message.warning('请完善必填信息！');
      // })
  }

  const handleStorag = ()=>{
    let apiKey = formData.value.id==null?'projectExtensionExitAdd':'projectExtensionExitEdit';
          formData.value.projectId = projectId.value;
          api.project[apiKey](formData.value,'projectExtensionExit').then(async res=>{
              if(res.code==200){
                  message.success('操作成功');
                  getInfo();
              }
          })
  }

  // const type        = ref('ADD');
  const companyName = inject('getAutoParams')('name');
  const getInfo     = ()=>{
      let postData = {
          pageNo   : 1,
          pageSize : 1,
          params   : {
              companyId : props.companyId
          }
      }
      api.project.projectExtensionExitInfo('projectExtensionExit',props.menuId,projectId.value).then(res=>{
          if(res.code==200){
              // type.value     = 'EDIT';
              formData.value = res.data;
              if (!formData.value.approvalUserId){
                  formData.value.approvalUserId = store.userInfo.userId;
                  getUserPage(store.userInfo.userId)
                  formData.value.approvalUser   = {
                      userId   : store.userInfo.userId,
                      realname : store.userInfo.realname
                  }
              }else{
                getUserPage(formData.value.approvalUserId)
              }
          }else{
              // type.value                    = 'ADD';
              formData.value.approvalUserId = store.userInfo.userId;
              formData.value.approvalUser   = {
                  userId   : store.userInfo.userId,
                  realname : store.userInfo.realname
              }
              formData.value.approvalContent = '项目退出'
          }
      })
  }
  const getUserPage = (userId) =>{
    api.sys.projectExtensionUserPage({params : {userId: userId}}).then(res=>{
        if(res.code==200){
           if(res.data.records.length){
            formData.value.companyName = res.data.records[0].companyName
            formData.value.businessDeptName = res.data.records[0].deptName
           }else{
            formData.value.companyName = ''
            formData.value.businessDeptName = ''
           }
        }
    })
  }
  const projectStepExpansionInfo = ()=>{
    let postData = {
            projectId : projectId.value,
            stepMenuId : props.menuId,
            status : 1
    }
    api.project.projectStepExpansion(postData).then(res=>{
          if(res.code==200){
          }else{
            message.erroe(res.msg)
          }
      })
}
const disabled = ref(false);
const getOaStatus = (res)=>{
    console.log('res',res)
    disabled.value = res;
}
const onChange = (data) =>{
  console.log('22',data)
  if(data){
    getUserPage(data.value)
  }else{
    formData.value.companyName= ''
    formData.value.businessDeptName= ''
  }
}
  onMounted(() => {
      getInfo();
  })
  </script>
  <style scoped lang="less">

  </style>
