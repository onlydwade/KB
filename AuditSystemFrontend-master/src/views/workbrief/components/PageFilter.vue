
<template>
    <div class="filter-box">
        <a-form :label-col="{ style: { width: '110px' } }" ref="formRef" :model="formData">
            <a-row :class="{ 'show_more_col': formData.moreFilter }">
                <a-col :span="formData.moreFilter ? 12 : 24" class="text-right">
                    <a-form-item name="title">
                        <a-space>
                            <a-input class="key_input" allowClear v-model:value="formData.title" placeholder="可输入工作简报标题字段搜索"
                                style="width:360px;" />
                            <a-button type="primary" @click="filterSubmit">查询</a-button>
                            <a-button @click="reset">重置</a-button>
                            <!-- <a-button type="text" @click="formData.moreFilter=!formData.moreFilter">{{formData.moreFilter?'收起':'展开'}}</a-button> -->
                        </a-space>
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
    </div>
</template>
<script setup>
const emit = defineEmits(['submit'])
const props = defineProps({
    modelValue: {
        type: Object,
        default: {},
    }
})
const formData = computed({
    get: () => {
        let data = props.modelValue;
        return data;
    },
    set: (val) => emit('update:modelValue', val)
});
const formRef = ref(null);
const filterSubmit = () => {
    emit('submit')
}
const reset = (data) => {
    formRef.value.resetFields();
    formData.value.provinceCode = null;
    formData.value.cityCode = null;
    filterSubmit();
}
const dataExport = () => {
    emit('dataExport');
}
onMounted(() => {
})
</script>
<style scoped lang="less"></style>
