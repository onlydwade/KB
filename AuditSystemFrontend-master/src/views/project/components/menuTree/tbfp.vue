<template>
    <div class="menu_inner">
        <a-form ref="formRef" layout="vertical" :model="formData">
            <div class="padding_box">
                <Title>
                    <template #title>
                        中标信息
                        <span class="color-danger"> * </span>
                    </template>
                </Title>
                <BidedCompanys v-if="formData.id" :projectId="projectId" @change="bidedChange" :readOnly="disabled" />

                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="8">
                        <a-form-item required label="未中标/已中标原因" name="bidedRemark" style="margin-top:32px;">
                            <KeyWords :readOnly="disabled || !formData.bidedResult"
                                v-if="formData.bidedResult == 'WEI_ZHONG_BIAO'" v-model="formData.bidedRemark"
                                :type="6" />
                            <KeyWords :readOnly="disabled || !formData.bidedResult" v-else
                                v-model="formData.bidedRemark" :type="7" />
                            <span v-if="!formData.bidedResult">请先选择中标单位</span>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="8" v-if="formData.bidedResult === 'YI_ZHONG_BIAO'">
                        <a-form-item required label="预计服务年限（年）- 如:1年" name="expectServiceYears" style="margin-top:32px;">
                          <a-input-number v-model:value="formData.expectServiceYears" :min="0" class="w_full" />
                        </a-form-item>
                    </a-col>
                </a-row>

                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="8">
                        <a-form-item required label="投标结果" name="bidedResult">
                            <a-select disabled :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.bidedResult" class="w_full" placeholder="请选择"
                                :options="dict.options('TOU_BIAO_JIE_GUO')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="8" v-if="formData.bidedResult === 'YI_ZHONG_BIAO'">
                        <a-form-item label="中标金额（元）" name="bidedAmount">
                            <a-input-number disabled v-model:value="formData.bidedAmount" :min="0" class="w_full"
                                :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                :addon-after="amountUnit(formData.bidedAmount)" />
                        </a-form-item>
                    </a-col>
                </a-row>

                <a-row v-if="formData.bidedResult == 'WEI_ZHONG_BIAO'" :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="8">
                        <a-form-item required label="未中标原因" name="notSelectedReason">
                            <a-select :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.notSelectedReason" class="w_full" placeholder="请选择"
                                :options="dict.options('WEI_ZHONG_BIAO_YUAN_YIN')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                </a-row>

                <a-row v-if="formData.notSelectedReason == 'TOU_BIAO_WU_XIAO'" :gutter="24">
                    <a-col :span="24">
                        <a-form-item :required="formData.notSelectedReason == 'TOU_BIAO_WU_XIAO'" label="投标无效原因"
                            name="invalidBidReason">
                            <a-checkbox-group :disabled="disabled" v-model:value="formData.invalidBidReason">
                                <a-row>
                                    <a-col v-for="(item, index) in dict.options('TOU_BIAO_WU_XIAO_YUAN_YIN')"
                                        :key="index" :span="4">
                                        <a-checkbox :value="item.value">{{ item.label }}</a-checkbox>
                                    </a-col>
                                </a-row>
                            </a-checkbox-group>
                        </a-form-item>
                        <a-form-item v-if="formData.invalidBidReason.includes('12')"
                            :rules="{ required: formData.invalidBidReason.includes('12'), message: '请输入其他问题' }"
                            name="otherInvalidBidBeason">
                            <a-input :disabled="disabled" v-model:value="formData.otherInvalidBidBeason" />
                        </a-form-item>
                    </a-col>
                </a-row>

                <Title title="中标通知书或评分结果文件上传">
                    <span class="color-danger"> * </span>
                    <a-form-item name="fileOk" :rules="{ required: true, message: '( 请上传必传文件后提交!!! )', }">
                    </a-form-item>
                </Title>
                <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval" v-if="formData.id" :menuId="menuId"
                    :projectId="projectId" v-model="formData.fileOk" ref="documentsRef" :readOnly="disabled"
                    :requiredIds="formData.bidedResult == 'YI_ZHONG_BIAO' ? [7] : []" />
            </div>
        </a-form>
        <ProjectActions @submit="submit" :menuInfo="menuInfo">
            <a-button size="large" @click="save"
                v-if="(menuInfo.status == 0 && [0, 5, 9].includes(menuInfo.approvalStatus) )|| !disabled">暂存</a-button>
            <!-- <a-button size="large" @click="submit(2)" type="primary"
                v-if="menuInfo.status == 0 && [0, 9].includes(menuInfo.approvalStatus) && !disabled">提交保存</a-button> -->
        </ProjectActions>
    </div>
</template>
<script setup>
import BidedCompanys from '../correlation/BidedCompanys.vue'
import { useMenuTree } from './menu';
import { amountUnit } from '@/utils/tools';
import { useDictStore } from '@/store/dict';
const dict = useDictStore();
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

const formAttrs = ['id', 'bidedResult', 'expectServiceYears', 'bidedAmount', 'bidedRemark', 'serviceStatus', 'notSelectedReason', 'invalidBidReason', 'otherInvalidBidBeason'];
const {
    formRef,
    documentsRef,
    formData,
    getInfo,
    submit,
    save,
    disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs, true);
onMounted(() => {
    getInfo((res) => {
        formData.value.invalidBidReason = res.invalidBidReason?.split(',') || [];
    });
})
watch(() => [formData.value.bidedResult], () => {
    if (formData.value.bidedResult !== 'WEI_ZHONG_BIAO') {
        formData.value.notSelectedReason = null
        formData.value.invalidBidReason = []
        formData.value.otherInvalidBidBeason = ''
    }
}, { deep: true })

const bidedChange = (bid) => {
    formData.value.bidedResult = bid.type == 1 ? 'YI_ZHONG_BIAO' : 'WEI_ZHONG_BIAO';
    // formData.value.serviceStatus = formData.value.bidedResult;
    formData.value.bidedAmount = bid.bidingAmount;
}
</script>
<style scoped lang="less"></style>
