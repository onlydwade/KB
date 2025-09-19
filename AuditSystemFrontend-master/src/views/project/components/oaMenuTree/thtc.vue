
<template>
  <div class="menu_inner">
    <div class="card_box">
      <van-cell-group title="退场管理信息">
                <van-cell title="主题" :value="formData.title || '-'"/>
                <van-cell title="项目名称" :value="formData.projectName || '-'"/>
                <van-cell title="申请日期" :value="dateFormat(formData.approvalSendTime,'YYYY-MM-DD') || '-'"/>
                <van-cell title="申请人归属" :value="getApprovalAffiliation(formData.approvalAffiliation)"/>
                <van-cell title="申请人" :value="(formData.approvalUser || {}).realname || '-'"/>
                <van-cell title="联系电话" :value="formData.relationPhone || '-'"/>
                <van-cell title="所属公司" :value="formData.companyName || '-'"/>
                <van-cell title="所属部门" :value="formData.businessDeptName || '-'"/>
                <van-cell title="是否涉及金额" :value="formData.whetherInvolveMoney==1?'是':formData.whetherInvolveMoney==2?'否' : '-'"/>
                <van-cell title="涉及金额" :value="parseFormatNum(formData.involveMoney) || '-'"/>
                <van-cell title="金额大写" :value="parseFormatNum(formData.majusculeMoney) || '-'"/>
                <van-cell title="退出描述" :value="formData.content || '-'"/>
        </van-cell-group>
    </div>
      <FileList  :menuId="menuId" :projectId="projectId" :recordId="-1"/>
  </div>
  </template>
  <script setup>
  import api           from '@/api/index';
  import { message }   from 'ant-design-vue';
  import { mainStore } from '@/store';
  import { parseFormatNum ,dateFormat} from '@/utils/tools'
  const store = mainStore();
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
      }
  })
  const route     = useRoute();
  const projectId = ref(Number(route.query.id || 0))
  const documentsRef    = ref(null);
  const formData        = ref({});
  function getApprovalAffiliation(val) {
    if(val=='1'){
      return '物业公司总部'
    }
    if(val=='2'){
      return '地区公司'
    }
    if(val=='3'){
      return '物业项目'
    }
    return '-'
  }
  const type        = ref('ADD');
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
              type.value     = 'EDIT';
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
              type.value                    = 'ADD';
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
const disabled = ref(false);
const getOaStatus = (res)=>{
    console.log('res',res)
    disabled.value = res;
}
  onMounted(() => {
      getInfo();
  })
  </script>
  <style scoped lang="less">
  .menu_inner{
    :v-deep .van-cell__value {
    color: #000;
  }
  :v-deep .van-cell-group__title {
    color: #000;
    font-weight: bold;
  }
  .card_box{
    margin-bottom: 20px;
  }
  }

  </style>
