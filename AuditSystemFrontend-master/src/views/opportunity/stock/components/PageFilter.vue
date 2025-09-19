<template>
  <div class="filter-box">
    <a-form :label-col="{ style: { width: '110px' } }" ref="formRef" :model="formData">
      <a-row :class="{ show_more_col: moreFilter }">
        <a-col :span="6" class="more_col">
          <a-form-item label="项目名称" name="keyWord">
            <a-input allowClear v-model:value="formData.keyWord" placeholder="请输入" />
          </a-form-item>
        </a-col>

        <a-col :span="6" class="more_col">
          <a-form-item label="业务省份" name="province">
            <a-select v-model:value="formData.province" class="w_full" placeholder="请选择" mode="multiple"
              :options="provinces"   @change="provinceChange" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="业务城市" name="city">
            <a-select v-model:value="formData.city" class="w_full" placeholder="请选择" mode="multiple" :options="cities"
              :disabled="!formData.province || formData.province.length === 0" />
          </a-form-item>
        </a-col>

        <a-col :span="6" class="more_col">
          <a-form-item label="物业业态" name="propertyType">
            <a-select v-model:value="formData.propertyType" class="w_full" placeholder="请选择" mode="multiple"
              :options="dict.options('QLM_Property_Type')" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="服务类型" name="projectLevel">
            <a-select v-model:value="formData.industryClass" class="w_full" placeholder="请选择" mode="multiple"
              :options="dict.options('QLM_Industry_Class')" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="业主单位类型" name="tenderFirstCategoryList">
            <a-select v-model:value="formData.tenderFirstCategoryList" class="w_full" placeholder="请选择" mode="multiple"
              :options="dict.options('QLM_Tender_First_Category')" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="合同到期时间" name="expireDateTime">
            <a-range-picker valueFormat="YYYY-MM-DD"
                            format="YYYY-MM-DD"
                            class="w_full"
                             v-model:value="formData.expireDateTime" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="合同金额区间" name="amount">
            <a-input-group>
              <a-row :gutter="8">
                <a-col :span="11">
                  <a-input v-model:value="formData.startAmount" allowClear placeholder="请输入" />
                </a-col>
                <a-col :span="2">
                  至
                </a-col>
                <a-col :span="11">
                  <a-input v-model:value="formData.endAmount" allowClear placeholder="请输入" />
                </a-col>
              </a-row>
            </a-input-group>
          </a-form-item>
        </a-col>

        <a-col :span="6" class="more_col">
          <a-form-item label="招标方式" name="bidingTypeList">
            <a-select v-model:value="formData.bidingTypeList" class="w_full" placeholder="请选择" mode="multiple"
              :options="dict.options('QLM_Biding_Type')" />
          </a-form-item>
        </a-col>

        <a-col :span="moreFilter ? 24 : 24" class="text-right">
          <a-form-item name="keyWord">
            <a-space>
              <a-input class="key_input" allowClear v-model:value="formData.keyWord" placeholder="请输入项目名称进行搜索"
                style="width: 360px" />
              <a-button type="primary" @click="filterSubmit">查询</a-button>
              <a-button @click="reset">重置</a-button>
              <a-button type="text" @click="moreFilter = !moreFilter">
                {{ moreFilter ? "收起" : "展开" }}
              </a-button>
            </a-space>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </div>
</template>
<script setup>
import { useDictStore } from "@/store/dict";
import api from "@/api/index";
const dict = useDictStore();
const emit = defineEmits(["update:modelValue", "submit"]);
const props = defineProps({
  modelValue: {
    type: Object,
    default: {},
  },
});
const moreFilter = ref(false)
const formRef = ref();
const provinces = ref([])
const cities = ref([])
const formData = computed({
  get: () => {
    return props.modelValue;
  },
  set: (val) => emit("update:modelValue", val),
});
const filterSubmit = () => {
  emit("submit", formData.value);
};
const loadding = ref(false);
const provinceChange = (e) => {
  formData.value.province
  loadCity()
}

const loadProvince = () => {
  api.opportunity.getQlmAreaList('Province').then(res => {
    if (res.code === 200) {
      provinces.value = res.data.map(n => {
        return { value: n.id, label: n.name }
      })
    }
  })
}
const loadCity = () => {
  loadding.value = false
  api.opportunity.getQlmAreaListByParentIds(formData.value.province).then(res => {
    if (res.code === 200) {
      cities.value= res.data.map(n => {
            return { value: n.id, label: n.name }
          })      
    }
    loadding.value = true
})
}

const onload = () => {
  loadProvince()
}

onload()
const reset = () => {
  formData.value = {    
    pageNo     : 1,
    pageSize   : 10
  }
  filterSubmit()
};

</script>
<style scoped lang="less"></style>
  