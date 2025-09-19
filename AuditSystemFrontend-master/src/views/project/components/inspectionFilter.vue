<template>
  <div class="filter-box">
    <a-form
      :label-col="{ style: { width: '120px' } }"
      ref="formRef"
      :model="formData"
    >
      <a-row :class="{ show_more_col: formData.moreFilter }">
        <a-col :span="6" class="more_col">
          <a-form-item label="所属年份" name="year">
            <a-date-picker
              v-model:value="formData.year"
              class="w_full"
              picker="year"
              valueFormat="YYYY"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="项目名称" name="projectName">
            <a-input
              allowClear
              v-model:value="formData.projectName"
              placeholder="请输入"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="项目编号" name="projectNo">
            <a-input
              allowClear
              v-model:value="formData.projectNo"
              placeholder="请输入"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="项目优先级" name="projectLevel">
            <a-select
              v-model:value="formData.projectLevel"
              class="w_full"
              placeholder="请选择"
              :options="dict.optionsAll('XIANG_MU_YOU_XIAN_JI')"
            >
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="状态" name="serviceStatus">
            <a-select
              v-model:value="formData.serviceStatus"
              class="w_full"
              placeholder="请选择"
              :options="serviceStatus"
            >
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="拓展模式" name="expansionMode">
            <a-select
              v-model:value="formData.expansionMode"
              class="w_full"
              placeholder="请选择"
              :options="dict.optionsAll('TUO_ZHAN_MO_SHI')"
            >
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="业态" name="businessType">
            <!-- <a-select
              v-model:value="formData.businessType"
              class="w_full"
              placeholder="请选择"
              :options="dict.optionsAll('XIANG_MU_YE_TAI')"
            >
            </a-select> -->
            <MultipleSelect v-model="formData.businessType" :multiple="true" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="服务内容" name="serviceContent">
            <MultipleDict
              v-model="formData.serviceContent"
              dictKey="FU_WU_NEI_RONG"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="合同到期日期" name="serviceEndTime">
            <a-date-picker
              allowClear
              v-model:value="formData.serviceEndTime"
              class="w_full"
              valueFormat="YYYY-MM-DD"
              format="YYYY-MM-DD"
              placeholder="请选择"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="是否已承接查验" name="checkState">
            <!-- <a-select
              v-model:value="formData.checkState"
              class="w_full"
              placeholder="请选择"
              :options="dict.optionsAll('SHI_FOU')"
              allowClear
            >
            </a-select> -->
            <a-select
              v-model:value="formData.checkState"
              class="w_full"
              placeholder="请选择"
              :options="options"
              allowClear
            >
            </a-select>
          </a-form-item>
        </a-col>
        <!-- <a-col :span="6" class="more_col">
          <a-form-item label="省份" name="provinceCode">
            <a-input
              allowClear
              v-model:value="formData.provinceCode"
              placeholder="请输入"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="城市" name="cityName">
            <a-input
              allowClear
              v-model:value="formData.cityName"
              placeholder="请输入"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="区/县" name="areaName">
            <a-input
              allowClear
              v-model:value="formData.areaName"
              placeholder="请输入"
            />
          </a-form-item>
        </a-col> -->
        <a-col :span="12" class="more_col">
          <a-form-item label="省市区" name="areaCode">
            <AddressSelect
              @change="addressChange"
              v-model:provinceCode="formData.provinceCode"
              v-model:cityCode="formData.cityCode"
              v-model:areaCode="formData.areaCode"
              mode="area"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="街道" name="streetCode">
            <a-select
              :getPopupContainer="(trigger) => trigger.parentNode"
              v-model:value="formData.streetCode"
              class="w_full"
              :placeholder="formData.areaCode ? '请选择街道' : '请先选择省市区'"
              :options="streetList"
            >
            </a-select>
          </a-form-item>
        </a-col>
        <!-- <a-col :span="6" class="more_col">
          <a-form-item label="所属大区" name="regionId">
            <DeptSelects :disabled="disabled" v-model="formData.regionId" :noRoot="true" />
          </a-form-item>
        </a-col> -->
        <a-col :span="6" class="more_col">
          <a-form-item label="归属单位" name="companyId">
            <DeptSelects :disabled="disabled" v-model="formData.companyId" :noRoot="true"/>
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="运营团队" name="operationTeamId">
            <DeptSelects
              :disabled="disabled"
              v-model="formData.operationTeamId"
              :noRoot="true"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="拓后负责人" name="principalId">
            <UserSelect
              :disabled="disabled"
              v-model="formData.principalId"
              :users="formData.principal"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="项目归属人" name="attributorUserId">
            <UserSelect v-model="formData.attributorUserId" />
          </a-form-item>
        </a-col>
        <a-col :span="formData.moreFilter ? 18 : 12" class="text-right">
          <a-form-item name="searchKey">
            <a-space>
              <a-button type="primary" @click="filterSubmit">查询</a-button>
              <a-button @click="reset">重置</a-button>
              <a-button
                type="text"
                @click="formData.moreFilter = !formData.moreFilter"
                >{{ formData.moreFilter ? "收起" : "展开" }}</a-button
              >
            </a-space>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </div>
</template>

<script setup>
import api from "@/api/index";
import moment from "moment";
import { mainStore } from "@/store";
import { useDictStore } from "@/store/dict";
const store = mainStore();
const dict = useDictStore();
const emit = defineEmits(["update:modelValue", "submit"]);
const props = defineProps({
  modelValue: {
    type: Object,
    default: {},
  },
});
const serviceStatus = reactive([
    {
        label:'全部',
        value:''
    },
    {
        label:'在管',
        value:'ZAI_GUAN'
    },
    {
        label:'已到期',
        value:'YI_DAO_QI'
    },
])
const options = reactive([
  {
    value:'',
    label:'全部'
  },
  {
    value:'SHI',
    label:'是'
  },
  {
    value:'FOU',
    label:'否'
  }
])
const formData = computed({
  get: () => {
    let data = props.modelValue;
    // data.year = ref(data.year || moment(new Date()).format("YYYY"));
    data.moreFilter = false;
    return data;
  },
  set: (value) => emit("update:modelValue", value),
});
const formRef = ref("");
const filterSubmit = () => {
  emit("submit");
};
const reset = (data) => {
  formRef.value.resetFields();
  // formData.value.year = moment(new Date()).format("YYYY");
  formData.value.provinceCode = null;
  formData.value.cityCode = null;
  formData.value.areaCode = null;
  streetList.value = null;
  formData.value.businessType  = null;
  formData.value.regionId = []
  formData.value.companyId = []
  formData.value.operationTeamId = []
  formData.value.serviceContent = []
  filterSubmit();
};
const addressChange = (areaCode) => {
  if (areaCode) {
    getStreet(areaCode);
  }
};
const streetList = ref([]);
const getStreet = (areaCode) => {
  api.common.listStreetByArea(areaCode).then((res) => {
    if (res.code == 200) {
      streetList.value = res.data.map((item) => {
        return {
          label: item.name + " (" + item.zipCode + ")",
          value: item.areaCode,
          zipCode: item.zipCode,
        };
      });
    }
  });
};
</script>



