<template>
 <div class="page_content" v-if="list.length>0">
  <Title class="titlebar" title="本报告期出现重大事项情况"></Title>
    <a-row :gutter="24" class="row_box">
      <a-col :span="6">类型</a-col>
      <a-col :span="6">事项明细</a-col>
      <a-col :span="6">事项描述</a-col>
      <a-col :span="6">处理建议</a-col>
    </a-row>
    <a-row :gutter="24" class="list_box" v-for="(item,index) in list" :key="index">
      <a-col :span="6">{{ typeStr[item.type] }}</a-col>
      <a-col :span="6">{{ item.detail }}</a-col>
      <a-col :span="6">{{ item.description }}</a-col>
      <a-col :span="6">{{ item.processPropose }}</a-col>
    </a-row>
  </div>
</template>
<script setup>
import api                from '@/api/index';
import { useCorrelation } from './correlation';
import { useDictStore }   from '@/store/dict';
import { watchArray }     from '@vueuse/core';

const dict  = useDictStore();
const emit  = defineEmits(['update:modelValue']);
const props = defineProps({
  modelValue : {
      type    : Array,
      default : [],
  },
  companyId : {
      type    : Number,
      default : 0,
  },
  reportId:{
      type    : Number,
      default : 0,
  },
  readOnly : {
      type    : Boolean,
      default : false,
  },
})
const {
  formRef,
  list,
  getList,
  addVisible,
  addSubmit,
  addCancel,
  editData,
  edit,
  editSubmit,
  editCancel,
  del,
  clone
} = useCorrelation(props.companyId,'companyOperateReportItem',{attr:'reportId',id:props.reportId},0,props.reportId?false:true);
const typeStr = {
  'ZHONG_DA_LI_HAO':'重大利好事项',
  'ZHONG_DA__BU_LI':'重大不利事项',
  'ZHONG_XIN':'中性事项',
}
watchArray(list, (newList) => {
  emit('update:modelValue',newList);
},{deep: true})

onMounted(() => {
  console.log('22',dict.options('ZHONG_DA_SHI_XIANG_LEI_XING'))
  if(props.reportId){
      getList();
  }
})
</script>
<style scoped lang="less">
.page_content{
  .row_box{
    background: #f5f5f5;
    line-height: 50px;
    margin-bottom: 2px;
  }
  .list_box{
    line-height: 40px;
    border-bottom: 1px solid #f0f0f0;
  }
  .list_box:hover{
      background: #f5f5f5;
  }
  .titlebar{
    margin : 16px -16px;
    &:first-child{
        margin-top : -16px;
    }
}
}
</style>
