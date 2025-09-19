
<template>
    <div class="filter-box">
        <a-form :label-col="{ style: { width: '90px' } }" ref="formRef" :model="formData">
            <a-row>
                <a-col :span="6">
                    <a-form-item label="公司编号" name="company_companyBizNo">
                        <a-input allowClear v-model:value="formData.company_companyBizNo" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6">
                    <a-form-item label="企业名称" name="company_name">
                        <a-input allowClear v-model:value="formData.company_name" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6">
                    <a-form-item label="会议类型" name="type">
                        <a-select
                          v-model:value="formData.type"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('SAN_HUI_HUI_YI_LEI_XING')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" v-if="type=='DOCUMENT_RECORD'">
                    <a-form-item label="文件类型" name="documentType">
                        <a-select
                          v-model:value="formData.documentType"
                          class="w_full"
                          placeholder="请选择"
                          :options="dict.optionsAll('HUI_YI_WEN_DANG_LEI_XING')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6">
                    <a-form-item label="会议标题" name="title">
                        <a-input allowClear v-model:value="formData.title" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" v-if="type=='DOCUMENT_RECORD'">
                    <a-form-item label="会议时间" name="meeting_meetingTime">
                        <a-range-picker
                            v-model:value="formData.meeting_meetingTime"
                            valueFormat="YYYY-MM-DD"
                            format="YYYY-MM-DD"
                            class="w_full"
                            :placeholder="['会议时间','范围筛选']" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" v-else>
                    <a-form-item label="会议时间" name="approval_meetingTime">
                      <a-range-picker
                          v-model:value="formData.approval_meetingTime"
                          valueFormat="YYYY-MM-DD"
                          format="YYYY-MM-DD"
                          class="w_full"
                          :placeholder="['会议时间','范围筛选']" />
                    </a-form-item>
                </a-col>

                <a-col :span="type=='DOCUMENT_RECORD'?12:18" class="text-right">
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
    },
    type:{
        type    : String,
        default : '',
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
