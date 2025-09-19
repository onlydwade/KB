<template>
  <div class="page_content" v-if="list.length>0">
    <a-row style="margin-bottom: 10px;">
      <a-col>
     公司董事会人员及变化情况:
      </a-col>
    </a-row>
    <a-row :gutter="24" class="row_box">
      <a-col :span="6">董事名称</a-col>
      <a-col :span="6">任期</a-col>
      <a-col :span="6">任职情况</a-col>
      <a-col :span="6">类型/职位</a-col>
    </a-row>
    <a-row :gutter="24" class="list_box" v-for="(item,index) in list" :key="index">
      <a-col :span="6">{{ item.name }}</a-col>
      <a-col :span="6">{{ item.remark }}</a-col>
      <a-col :span="6">{{ item.termStatus }}</a-col>
      <a-col :span="6">{{ item.position }}</a-col>
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
} = useCorrelation(props.companyId,'companyOperateReportExecutives',{attr:'reportId',id:props.reportId},0,props.reportId?false:true);

watchArray(list, (newList) => {
  emit('update:modelValue',newList);
},{deep: true})

onMounted(() => {
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
}
</style>
