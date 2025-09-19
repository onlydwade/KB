
<template>
    <div class="menu_inner">
        <a-form ref="formRef" layout="vertical" :model="formData">
            <div class="padding_box">
                <Title>
                    <template #title>
                        合同信息录入
                        <span class="color-danger"> * </span>
                        <a-button type="link" v-if="formData.relevanceProjectId"
                            @click="openPrevProject">查看上个版本合同信息</a-button>
                    </template>
                </Title>
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
                        <a-form-item required label="乙方单位名称" name="secondParty">
                            <a-input :disabled="disabled" v-model:value="formData.secondParty"
                                placeholder="请输入" />
                        </a-form-item>
                    </a-col>
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
                      <a-form-item label="业务板块" name="businessSegmentsStr">
                        <a-input disabled v-model:value="formData.businessSegmentsStr" />
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
                                :disabled="formData.renewBid" class="w_full" placeholder="请选择" :options="dict.options('SHI_FOU')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col v-if="formData.inStock === 'SHI'" :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="是否有增量业绩确认">
                            <a-select :getPopupContainer="trigger => trigger.parentNode" :disabled="disabled"
                                v-model:value="formData.isPerformanceIncrement" class="w_full" placeholder="请选择"
                                :options="dict.options('SHI_FOU')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                </a-row>

                <template v-if="formData.inStock === 'SHI'&& formData.isPerformanceIncrement === 'SHI'">
                    <Title style="margin-top:32px;">
                        <template #title>
                            增量业绩确认信息
                        </template>
                        <!-- <a-form-item name="correlation" :rules="{ required: true, message: '( 请完善业绩分配后提交!!! )', }">
                    </a-form-item> -->
                    </Title>
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
                            <a-form-item required label="当年转化金额(元) ">
                                <a-input-number v-model:value="formData.annualConversionAmounts" :min="0" class="w_full"
                                    disabled placeholder="自动计算"
                                    :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                    :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                    :addon-after="amountUnit(formData.annualConversionAmounts)" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                </template>

                <Title style="margin-top:32px;">
                    <template #title>
                        业绩分配
                        <span class="color-danger"> * </span>
                    </template>
                    <a-form-item name="correlation" :rules="{ required: true, message: '( 请完善业绩分配后提交!!! )', }">
                    </a-form-item>
                </Title>
                <Achievement v-if="formData.id" :projectId="projectId" v-model="formData.correlation"
                    :readOnly="disabled" />

                <Title title="附件上传">
                    <a-form-item name="fileOk" :rules="{ required: true, message: '( 请上传必传文件后提交!!! )', }">
                    </a-form-item>
                </Title>
                <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval" v-if="formData.id" :menuId="menuId"
                    :projectId="projectId" v-model="formData.fileOk" ref="documentsRef" :readOnly="disabled" />

            </div>
        </a-form>
        <ProjectActions @submit="submit" :menuInfo="menuInfo">
            <a-button size="large" @click="submit(2)"
                v-if="menuInfo.oaApproval != 1 && menuInfo.status == 0 && [0, 9].includes(menuInfo.approvalStatus) && !disabled">标记完成</a-button>
            <a-button size="large" @click="save"
                v-if="(menuInfo.status == 0 && [0, 5, 9].includes(menuInfo.approvalStatus)) || !disabled">暂存</a-button>
        </ProjectActions>
    </div>
    <YjqrspModal ref="prevProjectModalRef" />
</template>
<script setup>
import api from '@/api/index';
import { amountUnit, throttle } from '@/utils/tools';
import Achievement from '../correlation/Achievement.vue';
import YjqrspModal from '../correlation/YjqrspModal.vue';
import { useMenuTree } from './menu';
import { useDictStore } from '@/store/dict';
const dict = useDictStore();
const prevProjectModalRef = ref(null);
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
const formAttrs = ['id', 'firstResponsibleCompany', 'contractAmount', 'contractAnnualAmount', 'annualConversionAmount',  'signTime', 'serviceBeginTime', 'serviceEndTime', 'proposedServicePeriod', 'serviceContent', 'serviceContentOther', 'constructionArea','inStock', 'isPerformanceIncrement', 'contractAmounts', 'contractAnnualAmounts', 'annualConversionAmounts','secondParty'];
const {
    formRef,
    documentsRef,
    formData,
    getInfo,
    submit,
    save,
    disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs);

onMounted(() => {
    getInfo((res) => {
        formData.value.firstResponsibleCompany = res.firstResponsibleCompany || res.bidingCompany;
        formData.value.annualConversionAmount = res.annualConversionAmount || null;
        formData.value.calculateProfitRate = res.calculateProfitRate|| null;
        formData.value.contractAmounts = parseFloat(res.contractAmounts) || null;
        formData.value.contractAnnualAmounts = parseFloat(res.contractAnnualAmounts) || null;
        formData.value.annualConversionAmounts = parseFloat(res.annualConversionAmounts) || null;
    });
})

watch(() => [formData.value.proposedServicePeriod, formData.value.contractAmount, formData.value.contractAnnualAmount, formData.value.signTime, formData.value.serviceBeginTime, formData.value.serviceEndTime, formData.value.contractAmounts, formData.value.contractAnnualAmounts], () => {
    if (formData.value.proposedServicePeriod && (formData.value.signTime || formData.value.serviceBeginTime || formData.value.serviceEndTime)) {
        if ((formData.value.contractAmounts || formData.value.contractAnnualAmounts)&&(formData.value.inStock=='SHI' &&formData.value.isPerformanceIncrement=='SHI') ) {
            //增量为是，需要计算增量金额
            getAnnualConversionAmountCalculate('SHI');   
        }
        if (formData.value.contractAmount || formData.value.contractAnnualAmount) {
            //如果为否，需要清除增量业绩数据
            if(formData.value.inStock=='FOU'||formData.value.isPerformanceIncrement=='FOU'){
                formData.value.isPerformanceIncrement='FOU';
                formData.value.contractAmounts=0;
                formData.value.contractAnnualAmounts=0;
                formData.value.annualConversionAmounts=0;
            }
            //无论续签是还是否，都要计算当年转化金额 (元)
            getAnnualConversionAmountCalculate('FOU');
        }
    }
}, { deep: true })
const getAnnualConversionAmountCalculate = throttle((isIncrement) => {
    const arr = isIncrement === 'SHI' ? ['id', 'proposedServicePeriod', 'contractAmounts', 'contractAnnualAmounts', 'signTime', 'serviceBeginTime', 'serviceEndTime', 'projectType','inStock','isPerformanceIncrement','performanceConfirmTime'] : ['id', 'proposedServicePeriod', 'contractAmount', 'contractAnnualAmount', 'signTime', 'serviceBeginTime', 'serviceEndTime', 'projectType','inStock','isPerformanceIncrement','contractAmounts', 'contractAnnualAmounts','annualConversionAmounts','performanceConfirmTime']
    const postData = arr.reduce((prev, key) => ({ ...prev, [key]: formData.value[key] }), {
        isIncrement,
        annualConversionAmount: 0
    })
    api.project.annualConversionAmountCalculateNew(postData).then(res => {
        if (res.code == 200) {
            if (isIncrement === 'SHI') {
                formData.value.annualConversionAmounts = parseFloat(res.data) || null;
            } else {
                formData.value.annualConversionAmount = res.data || null;
            }
        }
    })
}, 20)

const openPrevProject = () => {
    prevProjectModalRef.value.open(formData.value.relevanceProjectId, toRefs(props).menuInfo.value.offlineApproval || 0, props.menuId)
}
</script>
<style scoped lang="less"></style>
