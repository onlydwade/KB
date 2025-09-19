
<template>
    <div class="menu_inner">
        <a-form ref="formRef" layout="vertical" :model="formData">
            <div class="padding_box">
                <Title>
                    <template #title>
                        项目信息
                        <span class="color-danger"> * </span>
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="项目名称">
                            <a-input disabled :value="formData.projectName" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="归属单位">
                            <a-input disabled :value="formData.companyName" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="报价金额(元)" name="bidingAmount">
                            <a-input-number :disabled="disabled" v-model:value="formData.bidingAmount" :min="0"
                                class="w_full" :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                :addon-after="amountUnit(formData.bidingAmount)" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item required label="测算利润率(%)" name="calculateProfitRate">
                            <a-input-number :disabled="disabled" v-model:value="formData.calculateProfitRate" :min="0"
                                class="w_full" />
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row :gutter="24">
                    <a-col :span="24">
                        <a-form-item label="优先级" name="projectLevel">
                            <a-radio-group v-model:value="formData.projectLevel" :disabled="disabled">
                                <a-row v-for="(item, index) in dict.options('XIANG_MU_YOU_XIAN_JI')" :key="index">
                                    <a-col :span="24">
                                        <a-radio :value="item.value">
                                            {{ item.label }}
                                        </a-radio>
                                    </a-col>
                                    <a-col v-if="projectLevelTexts[item.value]" :span="24" class="radio_desc">
                                        {{ projectLevelTexts[item.value] }}
                                    </a-col>
                                </a-row>
                                <a-form-item v-if="formData.projectLevel == 'ZHONG_DIAN_GUAN_ZHU_XIANG_MU'"
                                    :rules="{ required: formData.projectLevel == 'ZHONG_DIAN_GUAN_ZHU_XIANG_MU', message: '请选择重点关注项目相关问题', }"
                                    name="keyFocusProject">
                                    <a-checkbox-group class="checkbox_group" :disabled="disabled"
                                        v-model:value="formData.keyFocusProject">
                                        <a-row :gutter="10">
                                            <a-col v-for="(item, index) in dict.options('ZHONG_DIAN_GUAN_ZHU_XIANG_MU')"
                                                :key="index" :span="6">
                                                <a-checkbox :value="item.value">{{ item.label }}</a-checkbox>
                                            </a-col>
                                        </a-row>
                                        <a-form-item style="margin-top: 10px;"
                                            v-if="formData.keyFocusProject && formData.keyFocusProject.includes('6')"
                                            :rules="{ required: formData.keyFocusProject && formData.keyFocusProject.includes('6'), message: '请输入其他特殊情况', }"
                                            name="otherDescription">
                                            <a-input :disabled="disabled" v-model:value="formData.otherDescription" />
                                        </a-form-item>
                                    </a-checkbox-group>
                                </a-form-item>

                            </a-radio-group>
                        </a-form-item>
                    </a-col>
                </a-row>
                <Title title="附件上传">
                    <a-form-item name="fileOk" :rules="{ required: true, message: '( 请上传必传文件后提交!!! )', }">
                    </a-form-item>
                </Title>
                <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval" v-if="formData.id" :menuId="menuId"
                    :projectId="projectId" :readOnly="disabled" v-model="formData.fileOk" ref="documentsRef" />
            </div>
        </a-form>
        <ProjectActions  :projectLevel="formData.projectLevel" @submit="submit"  :menuInfo="menuInfo" offLineTitle="已线下审批，上传会签文件标记完成">
            <a-button size="large" @click="submit(2)"
                v-if="menuInfo.oaApproval != 1 && menuInfo.status == 0 && [0, 9].includes(menuInfo.approvalStatus) && !disabled">标记完成</a-button>
            <a-button size="large" @click="save" v-if="[5].includes(menuInfo.approvalStatus)   || !disabled">暂存</a-button>
        </ProjectActions>
    </div>
</template>
<script setup>
import api from '@/api/index';
import { message } from 'ant-design-vue';
import { useMenuTree } from './menu';
import { amountUnit } from '@/utils/tools'

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
const formAttrs = computed(() => {
    return ['id', 'bidingAmount', 'projectLevel', 'keyFocusProject','calculateProfitRate' ,'otherDescription']
});
const projectLevelTexts = {
    'PU_TONG_XIANG_MU': '单位组织评审',
    'TUO_YUN_YI_TI_HUA_XIANG_MU': '大区组织评审',
    'ZHONG_DIAN_GUAN_ZHU_XIANG_MU':'总部评审'
}
const {
    formRef,
    documentsRef,
    formData,
    getInfo,
    save,
    disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs.value);

const submit = (type, temp) => {
//

    formRef.value.validateFields().then(vRes => {
        if (type == 1 && documentsRef.value) {
            let offlineStatus = documentsRef.value.getOfflineStatus();
            if (offlineStatus != 'success') {
                message.warning(offlineStatus);
                return;
            }
        }
        api.project.changeBidingAmount(props.projectId, formData.value.bidingAmount).then(res => {
            if (res.code == 200) {
                save(type, temp)
            }
        })
    }).catch(err => {
        message.warning('请完善信息！');
    })
}

onMounted(() => {
    getInfo((res) => {
        formData.value.keyFocusProject = res.keyFocusProject?.split(',') || [];
    });
})
</script>
<style scoped lang="less">
.radio_desc {
    margin: 5px 5px 20px 30px;
    font-size: 14px;
    color: #bbbbbb;
}

.checkbox_group {
    padding: 10px 20px;
}
</style>
