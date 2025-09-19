
<template>
    <div class="filter-box">
        <a-form :label-col="{ style: { width: '110px' } }" ref="formRef" :model="formData">
            <a-row>
                <a-col :span="8">
                    <a-form-item label="企业名称" name="company_name">
                        <a-input allowClear v-model:value="formData.company_name" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6">
                    <a-form-item label="报告所属年份" name="year">
                        <a-date-picker
                        v-model:value="formData.year"
                        picker="year"
                        :disabled-date="disabledDate"
                        class="w_full"
                        valueFormat="YYYY"
                        format="YYYY"/>
                    </a-form-item>
                </a-col>
                <a-col :span="10">
                    <a-form-item label="报告期" name="cycleType">
                        <div class="flex_box">
                            <a-select
                                v-model:value="formData.cycleType"
                                class="flex_full"
                                placeholder="请选择"
                                @change="()=>{
                                    formData.cycle = null
                                }"
                                :options="dict.optionsAll('BAO_GAO_ZHOU_QI_LEI_XING')">
                            </a-select>
                            <a-form-item-rest>
                                <a-input class="flex_full" disabled v-if="formData.cycleType=='DIAO_YAN_NIAN_DU'" :value="formData.year" />
                                <a-select
                                    v-else
                                    v-model:value="formData.cycle"
                                    class="flex_full"
                                    style="margin-left:4px;"
                                    placeholder="请选择"
                                    :options="dict.optionsAll(formData.cycleType)">
                                </a-select>

                            </a-form-item-rest>
                        </div>
                    </a-form-item>
                </a-col>
                <a-col :span="8">
                    <a-form-item label="报告名称" name="report_name">
                        <a-input allowClear v-model:value="formData.report_name" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="16" class="text-right">
                    <a-form-item name="searchKey">
                        <a-space>
                            <a-button type="primary" @click="filterSubmit">查询</a-button>
                            <a-button @click="reset">重置</a-button>
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
    formData.value.cycle = null;
    filterSubmit();
}
const dataExport = ()=>{
    emit('dataExport');
}
onMounted(() => {
})

const disabledDate = (current) => {
  return current && current > new Date();
};
</script>
<style scoped lang="less">
</style>
