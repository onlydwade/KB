
<template>
    <div class="filter-box">
        <a-form :label-col="{ style: { width: '90px' } }" ref="formRef" :model="formData">
            <a-row :gutter="16">
                <a-col :span="5" v-if="subType=='Project'">
                    <a-form-item name="name">
                        <a-input allowClear v-model:value="formData.name" placeholder="项目名称查询" />
                    </a-form-item>
                </a-col>
                <a-col :span="5" v-if="subType=='Company'">
                    <a-form-item name="name">
                        <a-input allowClear v-model:value="formData.name" placeholder="企业名称查询" />
                    </a-form-item>
                </a-col>
                <template v-if="subType=='Company' && [52,53].includes(menuId)">
                    <a-col :span="4">
                        <a-form-item name="type">
                            <a-select
                              v-model:value="formData.type"
                              class="w_full"
                              placeholder="会议类型"
                              :options="dict.optionsAll('SAN_HUI_HUI_YI_LEI_XING')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :span="4" v-if="subType=='Company' && menuId==52">
                        <a-form-item name="documentType">
                            <a-select
                              v-model:value="formData.documentType"
                              class="w_full"
                              placeholder="文件类型"
                              :options="dict.optionsAll('HUI_YI_WEN_DANG_LEI_XING')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :span="5">
                        <a-form-item name="title">
                            <a-input allowClear v-model:value="formData.title" placeholder="会议标题查询" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-item label="" name="createTime">
                            <a-range-picker 
                                v-model:value="formData.createTime"
                                valueFormat="YYYY-MM-DD"
                                format="YYYY-MM-DD"
                                class="w_full"
                                :placeholder="['发起时间','范围筛选']" />
                        </a-form-item>
                    </a-col>
                </template>
                <template v-else>
                    <a-col :span="4">
                        <a-form-item name="stepMenuName">
                            <a-input allowClear v-model:value="formData.stepMenuName" placeholder="节点名称查询" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="5">
                        <a-form-item name="documentName">
                            <a-input allowClear v-model:value="formData.documentName" placeholder="文件名称查询" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-item label="" name="createTime">
                            <a-range-picker 
                                v-model:value="formData.createTime"
                                valueFormat="YYYY-MM-DD"
                                format="YYYY-MM-DD"
                                class="flex_full"
                                :placeholder="['上传时间','范围筛选']" />
                        </a-form-item>
                    </a-col>
                </template>
                <a-col :span="(subType=='Company' && menuId==52)?24:4" class="text-right">
                    <a-form-item>
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
import { useDictStore } from '@/store/dict';
const dict  = useDictStore();
const emit  = defineEmits(['update:modelValue','submit'])
const props = defineProps({
    modelValue      : {
        type    : Object,
        default : {},
    },
    subType:{
        type    : String,
        default : 'Project'
    },
    menuId:{
        type    : Number,
        default : 0
    }
})
const formData = computed({
    get : () => props.modelValue,
    set : (val) => emit('update:modelValue',val)
});
const formRef      = ref(null);
const filterSubmit = ()=>{
    emit('submit')
}
const reset = (data)=>{
    formRef.value.resetFields();
    filterSubmit();
}
onMounted(() => {
})
</script>
<style scoped lang="less">
</style>
