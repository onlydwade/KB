<template>
  <div>
    <a-modal
      v-model:visible="visible"
      :width="800"
      title="指标展示层级"
      placement="right"
      @close="handleClose"
      @ok="handleOk"
    >
      <a-form layout="vertical" :model="formData" ref="formRef" class="custom-class">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="指标类型" name="fieldName">
              <a-input
                allowClear
                v-model:value="formData.fieldName"
                disabled
                placeholder="请输入"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="单位" name="fieldUnitName">
              <a-input
                allowClear
                v-model:value="formData.fieldUnitName"
                disabled
                placeholder="请输入"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item required label="指标展示层级" name="appraise">
              <div class="tree_box">
                <a-tree
                v-model:checkedKeys="checkedKeys"
                checkable
                :field-names="{
                  children: 'children',
                  title: 'name',
                  key: 'id',
                }"
                @check="checkedChange"
                :tree-data="treeData"
              ></a-tree>
              </div>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup>
import api            from '@/api/index';
import { message } from "ant-design-vue";
import setConfigList from "./setConfigList.vue";
const visible = ref(false);
const formData = ref({});
const formRef = ref(null);
const checkedKeys = ref([]);
const selectKeys = ref([]);
const treeData = ref([])
const handleClose = () => {
  visible.value = false;
};
const handleOk = () => {
  if((selectKeys.value == null || selectKeys.value.length == 0) && (checkedKeys.value == null || checkedKeys.value.length == 0) ){
    message.error("请勾选配置!");
    return;
  }
  if(selectKeys.value == null || selectKeys.value.length == 0){
    selectKeys.value = checkedKeys.value;
  }
  api.performance.setIndicatorDisplayLevel(formData.value.fieldKey,selectKeys.value).then(res=>{
    if(res.code==200){
      visible.value = false;
    }
  });
};
const open = (row) => {
  formData.value = {...row}
  visible.value = true;
  selectKeys.value = [];
  api.performance.getIndicatorDisplayLevel(formData.value.fieldKey).then(res=>{
    if(res.code==200){
    if(res.data){
        checkedKeys.value   = res.data;
      }else{
        checkedKeys.value = [];
      }
  }
  });

};
const checkedChange = (selectedKeys,info) =>{
  selectKeys.value = selectedKeys;

}
const getDept  = async ()=>{
  let res = await api.performance.indicatorDisplayLevelTree();
  if(res.code==200){
    if(res.data){
        treeData.value   = getDeptWithOutRoot([res.data]);
      }else{
        treeData.value = [];
      }
  }

}
const getDeptWithOutRoot = (tree)=>{
  let arr = [];
  function doArr(treeData,parentChild){
    if (treeData){
      treeData.forEach((item, i) => {
        let obj = {
          ...item,
          children : []
        }

        if(item.deptType === 'CENG_JI' ){
          doArr(item.children,obj.children);
          parentChild.push(obj)
        }
      });
    }
  }
  doArr(tree,arr);
  return arr;
}
onMounted(() => {
    getDept();
})
defineExpose({ open });
</script>
<style lang="less" scoped>
.custom-class {
  padding: 10px 20px;
}
.tree_box{
  max-height: 200px;
  overflow-y:auto ;
}
</style>
