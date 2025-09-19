
<template>
    <div class="filter-box">
        <a-form :label-col="{ style: { width: '110px' } }" ref="formRef" :model="formData">
            <a-row :class="{'show_more_col':formData.moreFilter}">
                <a-col :span="6" class="more_col">
                    <a-form-item label="客户跟进人" name="followUserId">
                        <UserSelect v-model="formData.followUserId"/>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="信息维护人" name="maintenanceUserId">
                        <UserSelect v-model="formData.maintenanceUserId"/>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="客户全称" name="customerName">
                        <a-input allowClear v-model:value="formData.customerName" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="客户编码" name="customerNo">
                        <a-input allowClear v-model:value="formData.customerNo" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="客户类型" name="customerType">
                        <a-select
                          v-model:value="formData.customerType"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('KE_HU_FEN_LEI')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="跟进类型" name="customerLevel">
                        <a-select
                          v-model:value="formData.customerLevel"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('KE_HU_DENG_JI')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="所属行业" name="customerIndustry">
                        <a-select
                          v-model:value="formData.customerIndustry"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('SUO_SHU_XING_YE')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="所属省市区" name="areaCode">
                        <AddressSelect
                            v-model:provinceCode="formData.provinceCode"
                            v-model:cityCode="formData.cityCode"
                            v-model:areaCode="formData.areaCode" mode="area"/>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="详细地址" name="address">
                        <a-input allowClear v-model:value="formData.address" placeholder="请输入"/>
                    </a-form-item>
                </a-col>
                
                <a-col :span="6" class="more_col">
                    <a-form-item label="客户标签" name="keywords">
                        <a-input allowClear v-model:value="formData.keywords" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="合作类型" name="cooperationType">
                        <a-select
                          v-model:value="formData.cooperationType"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('HE_ZUO_LEI_XING')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="formData.moreFilter?24:24" class="text-right">
                    <a-form-item name="searchKey">
                        <a-space>
                            <a-input class="key_input" allowClear v-model:value="formData.searchKey" placeholder="可输入客户全称、统一社会信用代码字段搜索" style="width:360px;"/>
                            <a-button type="primary" @click="filterSubmit">查询</a-button>
                            <a-button @click="reset">重置</a-button>
                            <a-button @click="dataExport" v-permission="['biz:customer:export']">导出</a-button>
                            <a-button @click="cooperationExport" v-permission="['biz:customer:cooperationExport']">战略协议台账导出</a-button>
                            <a-button type="text" @click="formData.moreFilter=!formData.moreFilter">{{formData.moreFilter?'收起':'展开'}}</a-button>
                        </a-space>
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
    </div>
</template>
<script setup>
import { mainStore }    from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict  = useDictStore();
const emit  = defineEmits(['update:modelValue','submit','dataExport','cooperationExport'])
const props = defineProps({
    modelValue      : {
        type    : Object,
        default : {},
    }
})
const formData = computed({
    get : ()=>{
        let data = props.modelValue;
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
    filterSubmit();
}
const dataExport = ()=>{
    emit('dataExport');
}
const cooperationExport = ()=>{
    emit('cooperationExport');
}
onMounted(() => {
})
</script>
<style scoped lang="less">
</style>
