
<template>
    <div class="menu_inner">
        <a-form
        ref="formRef"
        layout="vertical"
        :model="formData">
            <div class="padding_box">
                <Title>
                    <template #title>
                        基本信息
                        <span class="color-danger"> * </span>
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="承接查验状态" name="checkState">
                            <a-select
                                v-model:value="formData.checkState"
                                class="w_full"
                                placeholder="请选择"
                                :options="checkStateList">
                                </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="开始时间" name="startTime">
                            <a-date-picker  :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.startTime"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="完成时间" name="endTime">
                            <a-date-picker  :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.endTime"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="责任人" name="personChargeName">
                            <a-input  v-model:value="formData.personChargeName" autocomplete="off" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                </a-row>
                <Title>
                    <template #title>
                        销项管理
                        <span class="color-danger"> * </span>
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="发现问题数量" name="problemNum">
                            <a-input  v-model:value="formData.problemNum" autocomplete="off" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="已整改数量" name="rectifyFinishedNum">
                            <a-input  v-model:value="formData.rectifyFinishedNum" autocomplete="off" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="待整改数量" name="rectifyAwaitNum">
                            <a-input  v-model:value="formData.rectifyAwaitNum" autocomplete="off" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="整改期限" name="rectifyDeadline">
                            <a-date-picker  :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.rectifyDeadline"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                </a-row>
                <Title title="上传文件">
                    <a-form-item name="fileOk1" 
                    :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                    </a-form-item>
                </Title>
                <ProjectTreeDocumentsList v-if="isBool" :proType="'ZHENGGAI_QINGDAN'"  :recordId="-1"  :menuId="menuId" :projectId="projectId" v-model="formData.fileOk1" ref="documentsRef" />                
                <Title title="交接管理">
                    <a-form-item name="fileOk2" 
                    :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                    </a-form-item>
                </Title>
                <ProjectTreeDocumentsList v-if="isBool" :proType="'JIAOJIE_GUANLI'"  :recordId="-1"  :menuId="menuId" :projectId="projectId" v-model="formData.fileOk2" ref="documentsRef"/>
                <Title title="资料管理">
                    <a-form-item name="fileOk3" 
                    :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                    </a-form-item>
                </Title>
                <ProjectTreeDocumentsList v-if="isBool" :proType="'ZILIAO_GUANLI'"  :recordId="-1"  :menuId="menuId" :projectId="projectId" v-model="formData.fileOk3" ref="documentsRef"/>
                <!-- <Title style="margin-top:32px;" title="业绩分配">
                    <a-form-item name="correlation" 
                    :rules="{required: true,message: '( 请完善业绩分配后提交!!! )',}">
                    </a-form-item>
                </Title> -->
                <!-- <Achievement :projectId="projectId" v-model="formData.correlation" :readOnly="disabled"/> -->
            </div>
        </a-form>
        <!-- <ProjectActions @submit="submit" :menuInfo="menuInfo">
            <a-button size="large" @click="submit(2)">保存</a-button>
            <a-button size="large" @click="save">暂存</a-button>
        </ProjectActions> -->
        <FooterBarL style="min-height:72px;" v-if="(hasPermission(['biz:projectCheck:add'])) || showStr == 'true'">
            <a-button size="large" v-if="(hasPermission(['biz:projectCheck:add'])) || showStr == 'true'" type="primary" @click="hanleOk">保存</a-button>
            <a-button size="large" v-if="(hasPermission(['biz:projectCheck:add'])) || showStr == 'true'" @click="hanleSubmit">暂存</a-button>
        </FooterBarL>
    </div>
</template>
<script setup>
import { amountUnit }  from '@/utils/tools';
import Achievement     from '../correlation/Achievement.vue';
// import { useMenuTree } from './menuList';
import api           from '@/api/index';
import { message }   from 'ant-design-vue';
import { hasPermission}   from '@/utils/tools';
const props = defineProps({
    projectId : {
        type    : Number,
        default : 0,
    },
    menuId : {
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    }
})
const checkStateList = reactive([
    {
        label:'未完成',
        value:0
    },
    {
        label:'进行中',
        value:1
    },
    {
        label:'已完成',
        value:2
    }
])
// const formAttrs = ['id','firstResponsibleCompany','contractAmount','contractAnnualAmount','calculateProfitRate','signTime','serviceBeginTime','serviceEndTime','proposedServicePeriod','serviceContent','serviceContentOther','constructionArea'];
// const {
//     formRef,
//     documentsRef,
//     formData,
//     save,
//     disabled
// } = useMenuTree(props.projectId,toRefs(props).menuInfo,formAttrs);
const isBool = ref(true)
const route       = useRoute();
const projectId   = ref(Number(route.query.id || 0))
const formRef         = ref(null);
const documentsRef    = ref(null);
const formData        = ref({});
const type        = ref('ADD');
const router      = useRouter();
const showStr = ref(route.query.show)
const emit = defineEmits(['ok1'])
const hanleOk = ()=>{
  console.log('222',formData.value)  
      formRef.value.validateFields().then(vRes=>{  
        console.log(formData.value.id)
          let apiKey = formData.value.id==null?'projectChecktAdd':'projectCheckEdit';
          formData.value.companyId = props.companyId;
          formData.value.projectId = props.projectId;
          formData.value.temporary = 1;
          api.project[apiKey](formData.value,'projectCheck').then(async res=>{
              if(res.code==200){
                //   if(formData.value.id==''){
                    //   let uploadResult = await documentsRef.value.batchUpLoad(res.data.id);
                //   }
                  message.success('操作成功');
                  getInfo();
                  isBool.value = false
                  nextTick(()=>{
                    isBool.value = true
                  })
                  projectStepExpansionInfo()
                //   router.back()
              }else{
                message.error(res.msg);
              }
          })
      }).catch(err=>{
          message.warning('请完善必填信息！');
      })
  }
  const hanleSubmit = ()=>{
          let apiKey = formData.value.id==null?'projectChecktAdd':'projectCheckEdit';
          formData.value.companyId = props.companyId;
          formData.value.projectId = props.projectId;
          formData.value.temporary = 0;
          api.project[apiKey](formData.value,'projectCheck').then(async res=>{
              if(res.code==200){
                  message.success('暂存成功');
                  getInfo();
                  isBool.value = false
                  nextTick(()=>{
                    isBool.value = true
                  })
                //   router.back()
              }else{
                message.error(res.msg);
              }
          })
  }
  
const getInfo = ()=>{
        api.project.projectCheckInfo(projectId.value).then(res=>{
          if(res.code==200){
              formData.value = res.data;
          }else{
          }
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
            emit('ok1')
          }else{
          }
      })
}
onMounted(() => {
    getInfo()
})
</script>
<style scoped lang="less">
</style>
