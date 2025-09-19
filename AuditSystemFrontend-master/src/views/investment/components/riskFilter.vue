
<template>
    <div class="filter-box">
        <a-form :label-col="{ style: { width: '90px' } }" ref="formRef" :model="formData">
            <a-row>
                <a-col :span="6">
                    <a-form-item label="企业名称" name="company_name">
                        <a-input allowClear v-model:value="formData['company_name']" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" v-if="mode=='FENG_XIAN_YU_JING'">
                    <a-form-item label="风险名称" name="node">
                        <a-input allowClear v-model:value="formData['node']" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" v-if="mode=='FENG_XIAN_CHU_LI'">
                    <a-form-item label="风险名称" name="approval_name">
                        <a-input allowClear v-model:value="formData['approval_name']" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="12" class="text-right">
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
    mode:{
        type    : String,
        default : 'FENG_XIAN_YU_JING',
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
