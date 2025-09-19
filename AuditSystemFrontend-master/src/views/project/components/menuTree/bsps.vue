<template>
    <div class="menu_inner">
        <a-form ref="formRef" layout="vertical" :model="formData">
            <div class="padding_box">
                <Title>
                    <template #title>
                        项目信息
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="所属大区">
                            <a-input disabled :value="formData.regionName" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="归属单位">
                            <a-input disabled :value="formData.companyName" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="项目名称">
                            <a-input disabled :value="formData.projectName" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="拓展模式">
                            <a-input disabled :value="formData.expansionModeStr" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="业态">
                            <a-input disabled :value="formData.businessTypeStr" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="开标时间">
                            <a-input disabled :value="dateFormat(formData.bidingOpentime, 'YYYY-MM-DD')" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                      <a-form-item label="参标单位">
                        <a-input disabled :value="formData.bidPartInCompany" placeholder="" />
                      </a-form-item>
                    </a-col>
                </a-row>

                <Title>
                    <template #title>
                        选择相关标书形式
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="投标类型" name="bidType">
                            <a-select :disabled="disabled" v-model:value="formData.bidType" class="w_full" placeholder="请选择"
                                :options="dict.options('TOU_BIAO_LEI_XING')" @change="onBidTypeChange" ref="bidTypeRef">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12" v-if="formData.bidType == 'ZHI_ZHI_BIAO'">
                        <a-form-item required label="申请类型" name="applicationType">
                            <a-select :disabled="disabled" v-model:value="formData.applicationType" class="w_full"
                                placeholder="请选择" :options="dict.options('SHEN_QING_LEI_XING')" ref="applicationTypeRef">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="关联用印类型" name="relatedSealType">
                            <a-select :disabled="disabled" v-model:value="formData.relatedSealType" class="w_full"
                                placeholder="请选择" :options="filteredrelatedSealTypeList" :showArrow="true"
                                :mode='multiple' ref="relatedSealTypeRef">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12" v-if="formData.bidType == 'DIAN_ZHI_BIAO'">
                        <a-form-item required label="是否涉及公章借用" name="isSealBorrowing">
                            <a-select :disabled="disabled" v-model:value="formData.isSealBorrowing" class="w_full"
                                      placeholder="请选择" :options="dict.options('SHI_FOU')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12" v-if="formData.bidType == 'ZHI_ZHI_BIAO' && (formData.relatedSealType === '1' || formData.relatedSealType === '2')">
                        <a-form-item required label="借用日期" name="borrowingTime">
                          <a-date-picker :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                         v-model:value="formData.borrowingTime" class="w_full" valueFormat="YYYY-MM-DD 23:59:59"
                                         format="YYYY-MM-DD" placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12" v-if="formData.bidType == 'ZHI_ZHI_BIAO' && (formData.relatedSealType === '1' || formData.relatedSealType === '2')">
                        <a-form-item required label="归还日期" name="returnTime">
                          <a-date-picker :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                         v-model:value="formData.returnTime" class="w_full" valueFormat="YYYY-MM-DD 23:59:59"
                                         format="YYYY-MM-DD" placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12" v-if="formData.bidType == 'DIAN_ZHI_BIAO' && formData.isSealBorrowing == 'SHI'">
                      <a-form-item required label="用印类型" name="applicationType">
                        <a-select :disabled="disabled" v-model:value="formData.applicationType" class="w_full"
                                  placeholder="请选择" :options="dict.options('SHEN_QING_LEI_XING')" ref="applicationTypeRef">
                        </a-select>
                      </a-form-item>
                    </a-col>
                </a-row>


                <!-- <Title>
                    <template #title>
                        借公章信息
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="取章人选择" name="bidType">
                            <a-input   placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12" v-if="formData.bidType == 'ZHI_ZHI_BIAO'">
                        <a-form-item required label="取章人" name="applicationType">
                            <a-input   placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="取章人联系方式" name="relatedSealType">
                            <a-input   placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="印章名称" name="relatedSealType">
                            <a-input   placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="印章类型" name="relatedSealType">
                            <a-input   placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="计划借出时间" name="relatedSealType">
                            <a-input   placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="计划归还时间" name="relatedSealType">
                            <a-input   placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item  label="用印事由" name="relatedSealType">
                            <a-input   placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item  label="借出备注" name="relatedSealType">
                            <a-input   placeholder="" />
                        </a-form-item>
                    </a-col>
                </a-row> -->


                <Title title="文件上传（上传招标及投标文件）">
                    <a-form-item name="fileOk" :rules="{ required: true, message: '( 请上传必传文件后提交!!! )', }">
                    </a-form-item>
                </Title>
                <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval" v-if="formData.id" :menuId="menuId"
                    :projectId="projectId" v-model="formData.fileOk" ref="documentsRef" :readOnly="disabled" />
            </div>
        </a-form>

        <ProjectActions @submit="submit" :menuInfo="menuInfo">

            <a-button size="large" @click="save"
                v-if="(menuInfo.status == 0 && [0, 5, 9].includes(menuInfo.approvalStatus) )|| !disabled">暂存</a-button>
        </ProjectActions>
    </div>
</template>


<script setup>
import { useMenuTree } from './menu';
import { watch, computed, ref } from 'vue';
import { useDictStore } from '@/store/dict';
const dict = useDictStore();
const bidTypeRef = ref(null)
const applicationTypeRef = ref(null)
const relatedSealTypeRef = ref(null)
const route = useRoute();
const projectId = ref(Number(route.query.id || 0))

const filteredrelatedSealTypeList = computed(() => {
    const items = dict.options(formData.value.bidType === 'ZHI_ZHI_BIAO' ? 'GUAN_LIAN_YONG_YIN_LEI_XING' : 'GUAN_LIAN_YONG_YIN_LEI_XING_DIAN_ZI')
    console.log(items);
    return items
})
const selectMode = computed(() => {
    //return formData.value.bidType === 'ZHI_ZHI_BIAO' ? 'multiple' : 'default';
    return formData.value.bidType === 'default';
});

const props = defineProps({
    projectId: {
        type: Number,
        default: 0,
    },
    menuId: {
        type: Number,
        default: 0,
    },
    menuInfo: {
        type: Object,
        default: {},
    }
})

const formAttrs = ['regionName', 'companyName', 'projectName', 'expansionModeStr', 'businessTypeStr', 'id', 'bidedResult', "bidedAmount", "bidedRemark", "serviceStatus", 'bidType', 'applicationType', 'relatedSealType','isSealBorrowing','bidingOpentime','borrowingTime','returnTime','bidPartInCompany'];
const {
    formRef,
    documentsRef,
    formData,
    getInfo,
    submit,
    save,
    disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs, true);

const onBidTypeChange = (value) => {
    if (value !== 'ZHI_ZHI_BIAO') {
        formData.value.applicationType = ''; //清空申请类型
        formData.value.relatedSealType = ''; // 清空关联用印类型
    } else {
        formData.value.relatedSealType = ''; // 清空关联用印类型
    }
}
// watch(() => [formData.value.bidType], () => {
//     if (!disabled.value) {
//         if (formData.value.bidType !== 'ZHI_ZHI_BIAO') {
//             formData.value.applicationType = ''; // 清空申请类型
//             formData.value.relatedSealType = []; // 清空关联用印类型
//         } else {
//             formData.value.applicationType = ''; // 清空申请类型
//             formData.value.relatedSealType = []; // 清空关联用印类型
//         }
//     }
// }, { deep: true });

onMounted(() => {
    getInfo((res) => {
        formData.value.relatedSealType = res.relatedSealType || '';
    });
})

</script>

<style scoped lang="less"></style>