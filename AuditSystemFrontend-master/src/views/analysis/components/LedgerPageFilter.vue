
<template>
  <div class="filter-box">
      <a-form :label-col="{ style: { width: '110px' } }" ref="formRef" :model="formData">
          <a-row :class="{'show_more_col':formData.moreFilter}">
              <a-col :span="6">
                  <a-form-item label="项目所属年份" name="year">
                      <a-date-picker
                      v-model:value="formData.year"
                      class="w_full"
                      picker="year"
                      valueFormat="YYYY"/>
                  </a-form-item>
              </a-col>
              <a-col :span="6" class="more_col">
                  <a-form-item label="大区" name="regionId">
                      <DeptSelect v-model="formData.regionId" :noRoot="true"/>
                  </a-form-item>
              </a-col>
              <a-col :span="6" class="more_col">
                  <a-form-item label="归属单位" name="companyId">
                      <DeptSelect v-model="formData.companyId" :noRoot="true"/>
                  </a-form-item>
              </a-col>
              <a-col :span="6" class="more_col">
                  <a-form-item label="项目名称" name="projectName">
                      <a-input allowClear v-model:value="formData.projectName" placeholder="请输入" />
                  </a-form-item>
              </a-col>
              <a-col :span="6" class="more_col">
                  <a-form-item label="项目编号" name="projectNo">
                      <a-input allowClear v-model:value="formData.projectNo" placeholder="请输入" />
                  </a-form-item>
              </a-col>
              <a-col :span="6" class="more_col">
                  <a-form-item label="费用名称" name="freeType">
                    <a-select allowClear
                        v-model:value="formData.freeType"
                        class="w_full"
                        placeholder="请选择"
                        :options="dict.optionsAll('TOU_BIAO_FEI_YONG_LEI_XING')">
                    </a-select>
                  </a-form-item>
              </a-col>
              <a-col :span="6" class="more_col">
                  <a-form-item label="缴纳单位" name="paymentCompany">
                      <a-input allowClear v-model:value="formData.paymentCompany" placeholder="请输入" />
                  </a-form-item>
              </a-col>
              <a-col :span="6" class="more_col">
                  <a-form-item label="欠款单位" name="arrearageCompany">
                      <a-input allowClear v-model:value="formData.arrearageCompany" placeholder="请输入" />
                  </a-form-item>
              </a-col>
              <a-col :span="6" class="more_col">
                  <a-form-item label="收款单位" name="payeeCompany">
                      <a-input allowClear v-model:value="formData.payeeCompany" placeholder="请输入" />
                  </a-form-item>
              </a-col>
              <a-col :span="6" class="more_col">
                  <a-form-item label="欠款状态" name="recoverStatus">
                      <a-select allowClear
                        v-model:value="formData.recoverStatus"
                        class="w_full"
                        placeholder="请选择"
                        :options="dict.optionsAll('QIAN_KUAN_ZHUANG_TAI')">
                      </a-select>
                  </a-form-item>
              </a-col>
              <a-col :span="6" class="more_col">
                  <a-form-item label="回款状态" name="deduct">
                      <a-select allowClear
                        v-model:value="formData.deduct"
                        class="w_full"
                        placeholder="请选择"
                        :options="dict.optionsAll('HUI_KUAN_ZHUANG_TAI')">
                      </a-select>
                  </a-form-item>
              </a-col>
              <a-col :span="formData.moreFilter?24:18" class="text-right">
                  <a-form-item name="searchKey">
                      <a-space>
                          <a-input class="key_input" allowClear v-model:value="formData.searchKey" placeholder="可输入项目名称、编号、备注等字段进行搜索" style="width:360px;"/>
                          <a-button type="primary" @click="filterSubmit">查询</a-button>
                          <a-button @click="reset">重置</a-button>
                          <a-button @click="dataExport" v-permission="['biz:project:export']">导出</a-button>
                          <a-button type="text" @click="formData.moreFilter=!formData.moreFilter">{{formData.moreFilter?'收起':'展开'}}</a-button>
                      </a-space>
                  </a-form-item>
              </a-col>
          </a-row>
      </a-form>
  </div>
</template>
<script setup>
import api              from '@/api/index';
import moment           from 'moment'
import { mainStore }    from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict  = useDictStore();
const emit  = defineEmits(['update:modelValue','submit','dataExport'])
const props = defineProps({
  modelValue      : {
      type    : Object,
      default : {},
  }
})
const formData = computed({
  get : () => {
      let data           = props.modelValue;
      data.year          = ref(data.year || moment(new Date()).format('YYYY'));
      data.serviceStatus = data.serviceStatus || '-1';
      data.moreFilter    = false;
      return data;
  },
  set : (val) => emit('update:modelValue',val)
});

const formRef = ref(null);
const filterSubmit   = ()=>{
  emit('submit')
}
const reset = (data)=>{
  formRef.value.resetFields();
  formData.value.provinceCode = null;
  formData.value.cityCode     = null;
  formData.value.year         = moment(new Date()).format('YYYY');
  filterSubmit();
}
const dataExport = ()=>{
  emit('dataExport');
}
onMounted(() => {
})


const addressChange = (areaCode)=>{
  if(areaCode){
      getStreet(areaCode);
  }
  formData.value.streetCode    = null;
  formData.value.streetZipcode = null;
}
const streetList = ref([]);
const getStreet = (areaCode)=>{
  api.common.listStreetByArea(areaCode).then(res=>{
      if(res.code==200){
          streetList.value = res.data.map(item=>{
              return {
                  label   : item.name+" ("+item.zipCode+")",
                  value   : item.areaCode,
                  zipCode : item.zipCode,
              }
          });
      }
  })
}
</script>
<style scoped lang="less">
</style>
