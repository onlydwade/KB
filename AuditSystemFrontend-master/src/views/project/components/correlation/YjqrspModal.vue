
<template>
    <a-modal v-model:visible="visible" title="上个合同业绩确认信息" destroyOnClose :width="1160" footer="">
        <a-form v-if="!!formData?.id" ref="formRef" layout="vertical" :model="formData">
            <Title style="margin-bottom:32px;" title="合同信息录入">
            </Title>
            <div class="padding_box">
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="甲方单位名称" name="firstResponsibleCompany">
                            <a-input :disabled="disabled" v-model:value="formData.firstResponsibleCompany"
                                placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="合同总金额（元）" name="contractAmount">
                            <a-input-number :disabled="disabled" v-model:value="formData.contractAmount" :min="0"
                                class="w_full" :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                :addon-after="amountUnit(formData.contractAmount)" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="合同年度金额（元）" name="contractAnnualAmount">
                            <a-input-number :disabled="disabled" v-model:value="formData.contractAnnualAmount" :min="0"
                                class="w_full" :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                :addon-after="amountUnit(formData.contractAnnualAmount)" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="当年转化金额 (元)">
                            <a-input-number :value="formData.annualConversionAmount" :min="0" class="w_full" disabled
                                placeholder="自动计算" :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                :addon-after="amountUnit(formData.annualConversionAmount)" />
                        </a-form-item>
                    </a-col>
                    <!-- <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="测算利润率(%)" name="calculateProfitRate">
                            <a-input-number :disabled="disabled" v-model:value="formData.calculateProfitRate" :min="0"
                                class="w_full" />
                        </a-form-item>
                    </a-col> -->
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="签约日期" name="signTime">
                            <a-date-picker :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.signTime" class="w_full" valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD" placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="服务开始日期" name="serviceBeginTime">
                            <a-date-picker :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.serviceBeginTime" class="w_full" valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD" placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="合同到期日期" name="serviceEndTime">
                            <a-date-picker :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.serviceEndTime" class="w_full" valueFormat="YYYY-MM-DD 23:59:59"
                                format="YYYY-MM-DD" placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="拟服务期限（月）" name="proposedServicePeriod">
                            <a-input-number :disabled="disabled" v-model:value="formData.proposedServicePeriod" :min="0"
                                class="w_full" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="服务内容" name="serviceContent">
                            <MultipleDicts :disabled="disabled" v-model="formData.serviceContent"
                                dictKey="FU_WU_NEI_RONG" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12" v-if="(formData.serviceContent || '').includes('QI_TA')">
                        <a-form-item label="其它服务内容" name="serviceContentOther">
                            <a-input :disabled="disabled" v-model:value="formData.serviceContentOther"
                                placeholder="请输入其它服务内容" />
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="建筑面积（㎡）" name="constructionArea">
                            <a-input-number :disabled="disabled" v-model:value="formData.constructionArea" :min="0"
                                class="w_full" :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                :parser="value => value.replace(/\\s?|(,*)/g, '')" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="拓展模式" name="expansionModeStr">
                            <a-input disabled v-model:value="formData.expansionModeStr" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="业态" name="businessTypeStr">
                            <a-input disabled v-model:value="formData.businessTypeStr" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="是否为续签项目">
                            <a-select :getPopupContainer="trigger => trigger.parentNode" v-model:value="formData.inStock"
                                disabled class="w_full" placeholder="请选择" :options="dict.options('SHI_FOU')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="是否有增量业绩确认">
                            <a-select :getPopupContainer="trigger => trigger.parentNode" :disabled="disabled"
                                v-model:value="formData.isPerformanceIncrement" class="w_full" placeholder="请选择"
                                :options="dict.options('SHI_FOU')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                </a-row>
            </div>

            <template v-if="formData.isPerformanceIncrement === 'SHI'">
                <Title style="margin-top:32px;" title="增量业绩确认信息"></Title>
                <div class="padding_box">
                    <a-row :gutter="24">
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="合同总金额(元) " name="contractAmounts">
                                <a-input-number :disabled="disabled" v-model:value="formData.contractAmounts" :min="0"
                                    placeholder="请输入" class="w_full"
                                    :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                    :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                    :addon-after="amountUnit(formData.contractAmounts)" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="合同年度金额(元) " name="contractAnnualAmounts">
                                <a-input-number :disabled="disabled" v-model:value="formData.contractAnnualAmounts"
                                    placeholder="请输入" :min="0" class="w_full"
                                    :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                    :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                    :addon-after="amountUnit(formData.contractAnnualAmounts)" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="当年转化收(元) " name="annualConversionAmounts">
                                <a-input-number v-model:value="formData.annualConversionAmounts" :min="0" class="w_full"
                                    disabled placeholder="自动计算"
                                    :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                    :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                    :addon-after="amountUnit(formData.annualConversionAmounts)" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                </div>
            </template>

            <Title style="margin-top:32px;" title="业绩分配"> </Title>
            <div class="padding_box">
                <Achievement v-if="formData.id" :projectId="formData.id" v-model="formData.correlation" readOnly />
            </div>

            <Title title="附件上传"></Title>
            <div class="padding_box">
                <ProjectTreeDocuments :offlineApproval="formData.offlineApproval" v-if="formData.id"
                    :menuId="formData.menuId" :projectId="formData.id" v-model="formData.fileOk" ref="documentsRef"
                    readOnly />
            </div>
        </a-form>
    </a-modal>
</template>

<script setup>
import api from '@/api/index';
import { amountUnit } from '@/utils/tools';
import { ref } from 'vue';
import Achievement from './Achievement.vue';
import { useDictStore } from '@/store/dict';
const dict = useDictStore();

const emit = defineEmits(['success'])
const visible = ref(false);
const formData = ref({});
const formRef = ref(null);
const disabled = ref(true);

const open = (id, offlineApproval, menuId) => {
    if (!id) {
        return
    }
    api.project.projectInfo(id).then((res) => {
        if (res.code == 200) {
            formData.value = { ...res.data, offlineApproval, id, menuId };
            visible.value = true;
            // nextTick(() => {
            //     formRef.value.resetFields();
            // })
        }
    });
}

defineExpose({ open })
</script>
<style scoped lang="less"></style>
