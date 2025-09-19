
<template>
    <div class="filter-box">
        <a-form :label-col="{ style: { width: '110px' } }" ref="formRef" :model="formData">
            <a-row :class="{'show_more_col':formData.moreFilter}" :gutter="10">
                <a-col :span="6">
                    <a-form-item label="企业名称" name="company_name">
                        <a-input allowClear v-model:value="formData.company_name" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="公司编号" name="company_companyBizNo">
                        <a-input allowClear v-model:value="formData.company_companyBizNo" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6">
                    <a-form-item label="投后负责人" name="company_principalId">
                        <UserSelect v-model="formData.company_principalId"/>
                    </a-form-item>
                </a-col>
                <a-col :span="6">
                    <a-form-item label="状态" name="company_serviceStatus">
                        <a-select
                          v-model:value="formData.company_serviceStatus"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('TOU_HOU_ZHUANG_TAI')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="投资类型" name="company_investmentType">
                        <a-select
                          v-model:value="formData.company_investmentType"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('TOU_ZI_LEI_XING')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="成立日期" name="company_incorporationTime">
                        <a-range-picker
                            v-model:value="formData.company_incorporationTime"
                            class="w_full"
                            valueFormat="YYYY-MM-DD 00:00:00"
                            format="YYYY-MM-DD"/>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="社会信用码" name="company_companyNo">
                        <a-input allowClear v-model:value="formData.company_companyNo" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="投前项目名称" name="company_projectName">
                        <a-input allowClear v-model:value="formData.company_projectName" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="投前项目归属人" name="company_attributorUserId">
                        <UserSelect v-model="formData.company_attributorUserId"/>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="省份" name="company_provinceCode">
                        <AddressSelect v-model:provinceCode="formData.company_provinceCode" mode="province"/>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="持股比例" name="company_shareholdingRatio">
                        <a-slider v-model:value="formData.company_shareholdingRatio" range :marks="{0:'0%',33:'33%',51:'51%',66:'66%',100:'100%'}"></a-slider>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="是否实缴" name="company_paidCapitalStatus">
                        <a-select
                          v-model:value="formData.company_paidCapitalStatus"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('SHI_FOU')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="formData.moreFilter?24:6" class="text-right">
                    <a-form-item name="searchKey">
                        <a-space>
                            <a-button type="primary" @click="filterSubmit">查询</a-button>
                            <a-button @click="reset">重置</a-button>
                            <a-button @click="dataExport" v-permission="['biz:projectCompany:export']">导出</a-button>
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
        let data  = props.modelValue;
        data.shareholdingRatio = [0,100]
        return data;
    },
    set : (val) => emit('update:modelValue',val)
});

const formRef = ref(null);
const filterSubmit   = ()=>{
  // console.log('111111',formData.value)
    emit('submit')
}
const reset = (data)=>{
    formRef.value.resetFields();
    formData.value.company_shareholdingRatio = [0,0]
    // console.log('rrrrrrrrr',formData.value)
    filterSubmit();
}
const dataExport = ()=>{
    emit('dataExport');
}
onMounted(() => {
})
</script>
<style scoped lang="less">
</style>
