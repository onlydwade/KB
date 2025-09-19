
<template>
  <div class="menu_inner">
      <!-- <a-tabs v-model:activeKey="handleType" v-if="showHandle">
          <a-tab-pane key="FENG_XIAN_JIE_CHU" tab="风险解除"/>
          <a-tab-pane key="FENG_XIAN_CHU_LI" tab="风险处理"/>
      </a-tabs> -->
      <div class="card_box">
      <van-cell-group :title="`风险${handleType=='FENG_XIAN_JIE_CHU'?'解除':'处理'}信息`">
        <van-cell title="审批发起人" :value="(formData.processApprovalUser || {}).realname || '-' " />
        <van-cell title="审批发起日期" :value="dateFormat(formData.processApprovalSendTime,'YYYY-MM-DD') || nowDate" />
        <van-cell title="企业名称" :value="formData.companyName || '-'"  v-if="companyId"/>
       <div v-if="handleType=='FENG_XIAN_CHU_LI'">
        <van-cell title="风险节点名称" :value="formData.name || '-'" />
        <van-cell title="风险提出人" :value="formData.presenter || '-'" />
        <van-cell title="风险类型" :value="formData.typeStr || '-'" />
       </div>
       <van-cell title="风险解除描述" :value="formData.schemeContent || '-'"   v-if="handleType=='FENG_XIAN_JIE_CHU'"/>
       <van-cell title="风险描述" :value="formData.content || '-'"   v-if="handleType=='FENG_XIAN_CHU_LI'"/>
       <van-cell title="风险解决方案" :value="formData.scheme || '-'"  v-if="handleType=='FENG_XIAN_CHU_LI'"/>
      </van-cell-group>
    </div>
    <companyFileList v-if="handleType=='FENG_XIAN_JIE_CHU'" type="JIE_CHU" :companyId="companyIdEnd" :menuId="46" :recordId="formData.id" />
    <companyFileList v-if="handleType=='FENG_XIAN_CHU_LI'" type="CHU_LI" :companyId="companyIdEnd" :menuId="46" :recordId="formData.id" />
  </div>
</template>
<script setup>
import api              from '@/api/index';
import moment           from 'moment'
import { message }      from 'ant-design-vue';
import { mainStore }    from '@/store';
import { useDictStore } from '@/store/dict';
import companyFileList from "./companyFileList.vue";
import { parseFormatNum ,dateFormat} from '@/utils/tools'
const store = mainStore();
const dict  = useDictStore();
const props = defineProps({
  companyId: {
      type    : Number,
      default : 0,
  },
  recordId : {
      type    : Number,
      default : 0,
  },
  mode:{
      type    : String,
      default : ''
  }
})

const companyIdEnd = computed(()=>{
  return props.companyId || formData.value.companyId;
})
const disabled    = computed(()=>[1,2,5].includes(formData.value.processApprovalStatus) || props.mode=='COMPANY_FX_JIE_CHU');
const disabled2   = computed(()=>[1,2,5].includes(formData.value.relieveApprovalStatus) || props.mode=='COMPANY_FX_CHU_LI');
const title       = ref('');
const formData    = ref({});
const formRef     = ref(null);
const documentRef = ref(null);
const handleType = ref('FENG_XIAN_JIE_CHU');
const showHandle = computed(()=>{
  return formData.value.relieveApprovalStatus || formData.value.relieveApprovalStatus==0  || props.mode=='COMPANY_FX_JIE_CHU';
})

const companyName = inject('getAutoParams')('companyName');
const getInfo     = ()=>{
  api.investment.correlationGet(props.recordId,'companyrisk').then(res=>{
      if(res.code==200){
          formData.value = res.data || {};
      }
  })
}
onMounted(() => {
  handleType.value = props.mode=='COMPANY_FX_CHU_LI'?'FENG_XIAN_CHU_LI':'FENG_XIAN_JIE_CHU';
  getInfo();
})

const nowDate = ref(moment(new Date()).format('YYYY-MM-DD'));
</script>
<style scoped lang="less">
.menu_inner {
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
  .bm{
    margin: 10px;
  }
}
</style>
