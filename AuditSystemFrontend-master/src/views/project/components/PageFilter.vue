<template>
    <div class="filter-box">
        <a-form :label-col="{ style: { width: '110px' } }" ref="formRef" :model="formData">
            <a-row :class="{ 'show_more_col': formData.moreFilter }">
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

                <a-col :span="12" class="more_col">
                    <a-form-item label="归属单位" name="companyId">
                        <DeptSelects v-model="formData.companyId" :noRoot="true" />
                    </a-form-item>
                </a-col>


                <a-col :span="12" class="more_col">
                    <a-form-item label="拓展单位" name="expandCompanyId">
                        <DeptSelects v-model="formData.expandCompanyId" :noRoot="true" />
                    </a-form-item>
                </a-col>


                <a-col :span="6" class="more_col">
                    <a-form-item label="项目类型" name="projectType">
                        <a-select mode="multiple" max-tag-count="responsive" v-model:value="formData.projectType"
                            class="w_full" placeholder="请选择" allowClear :options="dict.options('XIANG_MU_LEI_XING')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="拓展模式" name="expansionMode">
                        <a-select v-model:value="formData.expansionMode" mode="multiple" max-tag-count="responsive"
                            class="w_full" placeholder="请选择" allowClear :options="dict.options('TUO_ZHAN_MO_SHI')">
                        </a-select>
                    </a-form-item>
                </a-col>


                <a-col :span="6" class="more_col">
                    <a-form-item label="是否招标" name="bided">
                        <a-select v-model:value="formData.bided" class="w_full" placeholder="请选择"
                            :options="dict.optionsAll('SHI_FOU')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="业态" name="businessType">
                        <MultipleSelect v-model="formData.businessType" :multiple="true" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="归属人" name="attributorUserId">
                        <UserSelect v-model="formData.attributorUserId" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="是否续签" name="inStock">
                        <a-select v-model:value="formData.inStock" class="w_full" placeholder="请选择"
                            :options="dict.optionsAll('SHI_FOU')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="12" class="more_col">
                    <a-form-item label="省市区" name="areaCode">
                        <AddressSelect v-if="bool" v-model:provinceCode="formData.provinceCode"
                            v-model:cityCode="formData.cityCode" v-model:areaCode="formData.areaCode" mode="area"
                            :single="true" ref="AddressSelectRef" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="关联客户" name="customerId">
                        <DataSelect v-model="formData.customerId" modeName="customer" :options="{
            key: 'id',
            label: 'customerName'
        }" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="是否有效" name="expire">
                        <a-select v-model:value="formData.expire" class="w_full" placeholder="请选择"
                            :options="dict.optionsAll('XIANG_MU_SHI_XIAO_ZHUANG_TAI')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="项目来源" name="sourceName">
                        <a-input allowClear v-model:value="formData.sourceName" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="完成节点" name="finishStepId">
                        <a-select v-model:value="formData.finishStepId" mode="multiple" max-tag-count="responsive"
                        class="w_full" placeholder="请选择" allowClear :options="stepList">
                        </a-select>
                    </a-form-item>                    
                </a-col>


                <a-col :span="6" class="more_col">
                    <a-form-item label="项目状态" name="serviceStatus">
                        <a-select mode="multiple" max-tag-count="responsive" v-model:value="formData.serviceStatus"
                            class="w_full" placeholder="请选择" allowClear :options="serviceStatus">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="续签或重新投标" name="isRenewalTender">
                        <a-select v-model:value="formData.isRenewalTender" class="w_full" placeholder="请选择"
                            :options="dict.optionsAll('SHI_FOU')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-divider v-if="formData.moreFilter" style="margin: 5px 0px; font-size: 13px;"
                    orientation="left">或者</a-divider>
                <a-col :span="12" class="more_col">
                    <a-form-item label="年内状态变为" name="expandServiceStatus">
                        <a-select mode="multiple" max-tag-count="responsive"
                            v-model:value="formData.expandServiceStatus" class="w_full" placeholder="请选择" allowClear
                            :options="expandServiceStatus" @change="selectOption">
                        </a-select>
                    </a-form-item>
                </a-col>

                <a-col :span="formData.moreFilter ? 24 : 18" class="text-right">
                    <a-form-item name="searchKey">
                        <a-space>
                            <a-input class="key_input" allowClear v-model:value="formData.searchKey"
                                placeholder="可输入项目名称、编号字段进行搜索" style="width:360px;" />
                            <a-button type="primary" @click="filterSubmit">查询</a-button>
                            <a-button @click="reset">重置</a-button>
                            <a-button @click="dataExport" v-permission="['biz:project:export']">导出</a-button>
                            <a-button @click="exportProjectAchievement"
                                v-permission="['biz:project:export']">投标台账导出</a-button>
                            <a-button type="text" @click="unfold">
                                {{ formData.moreFilter ? '收起' : '展开' }}
                            </a-button>
                        </a-space>
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
    </div>
</template>
<script setup>
import api from '@/api/index';
import moment from 'moment'
import { mainStore } from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict = useDictStore();
const stepList = ref([]);
const emit = defineEmits(['update:modelValue', 'submit', 'dataExport', 'exportProjectAchievement'])
const props = defineProps({
    modelValue: {
        type: Object,
        default: {},
    }
})

//项目第一项状态
const serviceStatus = reactive([
    {
        label: "全部未结项",
        value: "WEI_JIE_XIANG",
    }
]
);
//补上从枚举读取的状态
const getServiceStatusDic = () => {
    let dictData = dict.options('XIANG_MU_ZHUANG_TAI')
    for (let i = 0; i < dictData.length; i++) {
        const item = dictData[i];
        let label = item.label;
        let value = item.value;
        serviceStatus.push({
            label: label,
            value: value,
        })
    }
};
//项目第一项状态
const expandServiceStatus = reactive([
    {
        label: "在管",
        value: "ZAI_GUAN",
    },
    {
        label: "已终止",
        value: "YI_ZHONG_ZHI",
    },
    {
        label: "已废止",
        value: "YI_FEI_ZHI",
    },
    {
        label: "已完结",
        value: "YI_WAN_JIE",
    },
    {
        label: "已退场",
        value: "YI_TUI_CHANG",
    },
    {
        label: "已归档",
        value: "CLOSE_FILE",
    },
    {
        label: "未中标",
        value: "WEI_ZHONG_BIAO",
    }
]
);
const formData = computed({
    get: () => {
        getServiceStatusDic();
        let data = props.modelValue;
        data.year = ref(data.year || moment(new Date()).format('YYYY'));
        if (data.modelName) {
            data.year = null;
        }
        data.expire = 'YOU_XIAO';
        data.inStock = '-1';
        data.expandServiceStatus = ['ZAI_GUAN', 'YI_ZHONG_ZHI', 'YI_FEI_ZHI', 'YI_WAN_JIE', 'YI_TUI_CHANG', 'CLOSE_FILE', 'WEI_ZHONG_BIAO'];
        data.isRenewalTender = 'FOU';
        data.finishStepId = [];
        return data;
    },
    set: (val) => emit('update:modelValue', val)
});

const formRef = ref(null);
const filterSubmit = () => {
    emit('submit')
}
const bool = ref(true)
const reset = (data) => {
    formRef.value.resetFields();
    bool.value = false
    nextTick(() => {
        bool.value = true
    })
    formData.value.provinceCode = null;
    formData.value.cityCode = null;
    formData.value.year = moment(new Date()).format('YYYY');
    if (data.modelName) {
        formData.value.year = null;
    }
    //formData.value.serviceStatus = [];
    //formData.value.expandServiceStatus = [];
    formData.value.businessType = null;
    formData.value.companyId = [];
    formData.value.expandCompanyId = [];
    formData.value.expansionMode = [];
    formData.value.finishStepId = [];
    formData.value.projectType = [];
    //formData.value.isRenewalTender = '-1';

    formData.value.pageNo = 1;
    formData.value.pageSize = 10;
    formData.value.serviceStatus = ["WEI_JIE_XIANG"];
    formData.value.expire = 'YOU_XIAO';
    formData.value.inStock = '-1';
    formData.value.expandServiceStatus = ['ZAI_GUAN', 'YI_ZHONG_ZHI', 'YI_FEI_ZHI', 'YI_WAN_JIE', 'YI_TUI_CHANG', 'CLOSE_FILE', 'WEI_ZHONG_BIAO'];
    formData.value.isRenewalTender = 'FOU';


    filterSubmit();
}
const dataExport = () => {
    emit('dataExport');
}
const exportProjectAchievement = () => {
    emit('exportProjectAchievement')
}
const unfold = () => {
    formData.value.moreFilter = !formData.value.moreFilter
    if (formData.value.searchKey && formData.value.moreFilter) {
        if (formData.value.searchKey.toLowerCase().includes("xm"))
            formData.value.projectNo = formData.value.searchKey
        else
            formData.value.projectName = formData.value.searchKey
    }
    //formData.value.searchKey = ''
}

const getStepList = () => {
    api.project.getStepList().then((res) => {
    if (res.code == 200) {        
        stepList.value = res.data;
    }    
  });
}
onMounted(() => {
    getStepList();
})
</script>
<style scoped lang="less"></style>
