<template>
  <div class="filter-box">
    <a-form :label-col="{ style: { width: '110px' } }" ref="formRef" :model="formData">
      <a-row :class="{ show_more_col: formData.moreFilter }">
        <a-col :span="6">
          <a-form-item label="项目所属年份" name="year">
            <a-date-picker v-model:value="formData.year" class="w_full" picker="year" valueFormat="YYYY" />
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
          <a-form-item label="项目优先级" name="projectLevel">
            <a-select v-model:value="formData.projectLevel" class="w_full" placeholder="请选择"
              :options="dict.optionsAll('XIANG_MU_YOU_XIAN_JI')">
            </a-select>
          </a-form-item>
        </a-col>
        <!-- <a-col :span="6" class="more_col">
                    <a-form-item label="大区" name="regionId">
                        <DeptSelect v-model="formData.regionId"/>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="归属单位" name="companyId">
                        <DeptSelect v-model="formData.companyId"/>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="项目状态" name="serviceStatus">
                        <a-select
                          v-model:value="formData.serviceStatus"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('XIANG_MU_ZHUANG_TAI')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="项目类型" name="projectType">
                        <a-select
                          v-model:value="formData.projectType"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('XIANG_MU_LEI_XING')">
                        </a-select>
                    </a-form-item>
                </a-col> -->
        <a-col :span="6" class="more_col">
          <a-form-item label="拓展模式" name="expansionMode">
            <a-select v-model:value="formData.expansionMode" class="w_full" placeholder="请选择"
              :options="dict.optionsAll('TUO_ZHAN_MO_SHI')">
            </a-select>
          </a-form-item>
        </a-col>
        <!-- <a-col :span="6" class="more_col">
                    <a-form-item label="一级业态" name="bided">
                        <a-select
                          v-model:value="formData.bided"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('SHI_FOU')">
                        </a-select>
                    </a-form-item>
                </a-col> -->
        <a-col :span="6" class="more_col">
          <a-form-item label="业态" name="businessType">
            <!-- <a-select
                          v-model:value="formData.businessType"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('XIANG_MU_YE_TAI')">
                        </a-select> -->
            <MultipleSelect v-model="formData.businessType" :multiple="true" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="服务内容" name="serviceContent">
            <!-- <a-input allowClear v-model:value="formData.serviceContent" placeholder="请输入" /> -->
            <MultipleDict v-model="formData.serviceContent" dictKey="FU_WU_NEI_RONG" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="合同是否到期" name="contractIsNotExpire">
            <!-- <a-select
                          v-model:value="formData.contractIsNotExpire"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('SHI_FOU')">
                        </a-select> -->
            <a-select v-model:value="formData.contractIsNotExpire" class="w_full" placeholder="请选择"
              :options="contractIsNotExpire">
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="合同到期时间" name="serviceEndTime">
            <a-date-picker :disabled="disabled" :getPopupContainer="(trigger) => trigger.parentNode"
              v-model:value="formData.serviceEndTime" class="w_full" valueFormat="YYYY-MM-DD" format="YYYY-MM-DD"
              placeholder="请选择" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="处理方式" name="processMode">
            <!-- <a-select
                          v-model:value="formData.processMode"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('XIANG_MU_YE_TAI')">
                        </a-select> -->
            <a-select v-model:value="formData.processMode"   mode="multiple"   max-tag-count="responsive" allowClear  class="w_full" placeholder="请选择" :options="processMode">
            </a-select>
          </a-form-item>
        </a-col>

        <a-col :span="12" class="more_col">
          <a-form-item label="省市区" name="areaCode">
            <AddressSelect @change="addressChange" v-model:provinceCode="formData.provinceCode"
              v-model:cityCode="formData.cityCode" v-model:areaCode="formData.areaCode" mode="area" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="街道" name="streetCode">
            <a-select :getPopupContainer="(trigger) => trigger.parentNode" v-model:value="formData.streetCode"
              class="w_full" :placeholder="formData.areaCode ? '请选择街道' : '请先选择省市区'" :options="streetList">
            </a-select>
          </a-form-item>
        </a-col>
        <!-- <a-col :span="6" class="more_col">
          <a-form-item label="所属大区" name="regionId">
            <DeptSelects v-model="formData.regionId" :noRoot="true"/>
          </a-form-item>
        </a-col> -->
        <a-col :span="6" class="more_col">
          <a-form-item label="归属单位" name="companyId">
            <!-- <a-select :getPopupContainer="trigger => trigger.parentNode"
                          v-model:value="formData.streetCode"
                          class="w_full"
                          :placeholder="formData.areaCode?'请选择街道':'请先选择省市区'"
                          :options="streetList">
                        </a-select> -->
            <DeptSelects v-model="formData.companyId" :noRoot="true" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="运营团队" name="operationTeamId">
            <DeptSelects :disabled="disabled" v-model="formData.operationTeamId" :noRoot="true" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="拓后负责人" name="principalId">
            <UserSelect :disabled="disabled" v-model="formData.principalId" :users="formData.principal" />
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="项目归属人" name="attributorUserId">
            <UserSelect v-model="formData.attributorUserId" />
          </a-form-item>
        </a-col>
        <!-- <a-col :span="6" class="more_col">
          <a-form-item label="关联新建项目" name="relevanceProjectNo">
            <a-input allowClear v-model:value="formData.relevanceProjectNo" placeholder="请输入" />
          </a-form-item>
        </a-col> -->
        <a-col :span="6" class="more_col">
          <a-form-item label="是否已承接查验" name="checkState">
            <a-select v-model:value="formData.checkState" class="w_full" placeholder="请选择"
              :options="dict.optionsAll('SHI_FOU')">
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="是否已评估" name="assessIsNot">
            <a-select v-model:value="formData.assessIsNot" class="w_full" placeholder="请选择"
              :options="dict.optionsAll('SHI_FOU')">
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6" class="more_col"></a-col>
        <a-col :span="6" class="more_col">
          <a-form-item label="状态" name="serviceStatus">
            <a-select v-model:value="formData.serviceStatus" class="w_full" :disabled="true" placeholder="请选择" :options="serviceStatus">
            </a-select>
          </a-form-item>
        </a-col>

        <a-divider v-if="formData.moreFilter" style="margin: 5px 0px; font-size: 13px;" orientation="left">或者</a-divider>
        <a-col :span="6" class="more_col">
                    <a-form-item label="项目状态" name="expandServiceStatus">
                        <a-select
                          mode="multiple"
                          max-tag-count="responsive"
                          v-model:value="formData.expandServiceStatus"
                          class="w_full"
                          placeholder="请选择"
                          :disabled="true"
                          :options="expandServiceStatus"                          
                    >
                        </a-select>
                    </a-form-item>
        </a-col>        
        <a-col :span="6" class="more_col">
          <a-form-item label="处理状态" name="processState">
            <!-- <a-select
                          v-model:value="formData.processState"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('XIANG_MU_YE_TAI')">
                        </a-select> -->
                        <a-select v-model:value="formData.processState" class="w_full" placeholder="请选择" :options="processState"
                          mode="multiple" max-tag-count="responsive"   allowClear  >
            </a-select>
          </a-form-item>
        </a-col>
        <a-divider v-if="formData.moreFilter" style="margin: 5px 0px; font-size: 13px;" orientation="left">或者</a-divider>
        <a-col :span="12" class="more_col">
                    <a-form-item label="年内状态变为" name="expandTwoServiceStatus">
                        <a-select
                          mode="multiple"
                          max-tag-count="responsive"
                          v-model:value="formData.expandTwoServiceStatus"
                          class="w_full"
                          placeholder="请选择"
                          allowClear
                          :options="expandTwoServiceStatus"
                          >
                        </a-select>
                    </a-form-item>
        </a-col>



        <a-col :span="formData.moreFilter ? 24 : 18" class="text-right">
          <a-form-item name="searchKey">
            <a-space>
              <a-input class="key_input" allowClear v-model:value="formData.searchKey" placeholder="可输入项目名称、编号、备注等字段进行搜索"
                style="width: 360px" />
              <a-button type="primary" @click="filterSubmit">查询</a-button>
              <a-button @click="reset">重置</a-button>
              <a-button @click="dataExport" v-permission="['biz:project:export']">导出</a-button>
              <a-button type="text" @click="formData.moreFilter = !formData.moreFilter">
                {{ formData.moreFilter ? "收起" : "展开" }}
              </a-button>
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
const emit = defineEmits(["update:modelValue", "submit", "dataExport"]);
const disabled = reactive(false)
const props = defineProps({
  modelValue: {
    type: Object,
    default: {},
  },
});
const serviceStatus = reactive([
  {
    label: "全部",
    value: "",
  },
  {
    label: "在管",
    value: "ZAI_GUAN",
  },
  {
    label: "已到期",
    value: "YI_DAO_QI",
  },
]);
//项目第一项状态
const expandServiceStatus = reactive([
  {
    label: "已到期",
    value: "YI_DAO_QI",
  }
]
);

//项目第二项状态
const expandTwoServiceStatus = reactive([
  {
    label: "已退场",
    value: "YI_TUI_CHANG",
  },
  {
    label: "已完结",
    value: "YI_WAN_JIE",
  },
]
);

const processState = reactive([
  {
    label: "审批中",
    value: "1",
  },
  {
    label: "处理被驳回",
    value: "3",
  },
  {
    label: "未处理",
    value: "0",
  },
  {
    label: "已处理",
    value: "2",
  },
]);
const processMode = reactive([
  // {
  //   label: "全部",
  //   value: "",
  // },
  {
    label: "续签",
    value: "1",
  },
  {
    label: "重新投标",
    value: "2",
  },
  {
    label: "退场",
    value: "3",
  },
]);
const contractIsNotExpire = reactive([
  {
    label: "全部",
    value: "",
  },
  {
    label: "未到期",
    value: "未到期",
  },
  {
    label: "已到期",
    value: "已到期",
  },
  {
    label: "即将到期",
    value: "即将到期",
  },
]);
const formData = computed({
  get: () => {
    let data = props.modelValue;
    data.year = ref(data.year || moment(new Date()).format("YYYY"));
        data.year = ref(data.year || moment(new Date()).format("YYYY"));
    // data.serviceStatus = data.serviceStatus || '-1';
    data.moreFilter = false;
    return data;
  },
  set: (val) => emit("update:modelValue", val),
});

const formRef = ref(null);
const filterSubmit = () => {
  emit("submit");
};
const reset = (data) => {
  formRef.value.resetFields();
  formData.value.provinceCode = null;
  formData.value.cityCode = null;
  formData.value.year =  moment(new Date()).format("YYYY");
  formData.value.businessType = null;
  formData.value.regionId = [];
  formData.value.companyId = [];
  formData.value.operationTeamId = [];
  formData.value.serviceContent = [];
  formData.value.processState = [];
  formData.value.processMode = [];
  formData.value.expandTwoServiceStatus = [];
  filterSubmit();
};
const dataExport = () => {
  emit("dataExport");
};
onMounted(() => { });

const addressChange = (areaCode) => {
  if (areaCode) {
    getStreet(areaCode);
  }
  formData.value.streetCode = null;
  formData.value.streetZipcode = null;
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
<style scoped lang="less"></style>
