
<template>
  <div class="menu_inner">
    <div class="card_box">
      <van-cell-group :title="`项目退出信息`">
        <van-cell title="企业名称" :value="formData.companyName || '-'"/>
        <van-cell title="申请内容" :value="formData.approvalContent || '-' " />
        <van-cell title="发起人" :value="(formData.approvalUser || {}).realname || '-' " />
        <van-cell title="发起日期" :value="dateFormat(formData.approvalSendTime,'YYYY-MM-DD') || '-'" />
        <van-cell title="项目退出日期" :value="dateFormat(formData.exitTime,'YYYY-MM-DD') || '-'"/>
        <van-cell title="退出描述" :value="formData.content || '-'" />
      </van-cell-group>
    </div>
    <companyFileList :recordId="-1" :companyId="companyId" :menuId="50" />
  </div>
</template>
<script setup>
import api           from '@/api/index';
import { message }   from 'ant-design-vue';
import { mainStore } from '@/store';
import { parseFormatNum ,dateFormat} from '@/utils/tools'
import companyFileList from "./companyFileList.vue";
const store = mainStore();
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
  api.investment.correlationGet(props.recordId,'companyExit').then(res=>{
      if(res.code==200){
          formData.value = res.data || {};
      }
  })
}
onMounted(() => {
  getInfo();
})
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
