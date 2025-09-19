
<template>
  <div class="menu_inner">
    <div class="card_box">
      <van-cell-group :title="`三会审批信息`">
        <van-cell title="审批发起人" :value="(formData.approvalUser || {}).realname || '-' " />
        <van-cell title="审批发起日期" :value="dateFormat(formData.createTime,'YYYY-MM-DD') || nowDate" />
        <van-cell title="企业名称" :value="formData.companyName || '-'"/>
        <van-cell title="会议标题" :value="formData.title || '-'"/>
        <van-cell title="会议类型" :value="getDictionariesVal(dict.options('SAN_HUI_HUI_YI_LEI_XING'),formData.type,'value') || '-'"/>
        <van-cell title="内容描述" :value="formData.content || '-'"/>
      </van-cell-group>
    </div>
    <companyFileList  :companyId="companyId" :menuId="53" :recordId="formData.id" />
  </div>
</template>
<script setup>
import api              from '@/api/index';
import moment           from 'moment'
import { message }      from 'ant-design-vue';
import { mainStore }    from '@/store';
import { useDictStore } from '@/store/dict';
import { getDictionariesVal,dateFormat} from '@/utils/tools';
import companyFileList from "./companyFileList.vue";
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

const formRef     = ref(null);
const formData    = ref({});
const documentRef = ref(null);
const odBtnRef    = ref(null);
const disabled    = computed(()=>false);
const companyName = inject('getAutoParams')('companyName');
const getInfo     = ()=>{
  api.investment.correlationGet(props.recordId,'ompanyHighLevelMeetingApproval').then(res=>{
      if(res.code==200){
        formData.value = res.data || {};
      }
  })
}
onMounted(() => {
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
