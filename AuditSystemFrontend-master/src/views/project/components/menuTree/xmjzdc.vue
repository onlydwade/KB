<template>
    <div class="menu_inner">
        <a-form ref="formRef" layout="vertical" :model="formData">
            <div class="padding_box">
                <Title>
                    <template #title>
                        尽调基本信息
                        <span class="color-danger"> * </span>
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="尽调形式" name="ddMode">
                            <a-select :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.ddMode" class="w_full"
                                :options="dict.options('JIN_DIAO_XING_SHI')">
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="尽调内容" required name="dueDiligenceContent">
                            <a-select @change="handleChangeA"  v-model:value="dueDiligenceContentList" :options="[{ label: '项目尽调', value: '项目尽调' },
        { label: '公司尽调', value: '公司尽调' }]" mode="tags">
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="尽调方式" required name="dueDiligenceMethod">
                            <a-select @change="handleChange"  v-model:value="dueDiligenceMethodList"
                                :options="[{ label: '内部', value: '内部' }, { label: '第三方', value: '第三方' }]" mode="tags">
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="踏勘团队" name="ddTeamType">
                            <a-select :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.ddTeamType" class="w_full"
                                :options="dict.options('TA_KAN_TUAN_DUI')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="费用预算（元）" name="ddAmount">
                            <a-input-number :disabled="disabled" v-model:value="formData.ddAmount" :min="0"
                                class="w_full" :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                :addon-after="amountUnit(formData.ddAmount)" />
                        </a-form-item>
                    </a-col>
                </a-row>

                <Title>
                    <template #title>
                        尽调人员确认
                        <span class="color-danger"> * </span>
                    </template>
                    <a-form-item name="teamOk" :rules="{ required: true, message: '( 请完善尽调人员后提交!!! )', }">
                    </a-form-item>
                </Title>
                <Team v-if="formData.id" :projectId="projectId" type="TOU" v-model="formData.teamOk"
                    :readOnly="disabled" />

                <Title style="margin-top:32px;">
                    <template #title>
                        其他审批（选择性发起）
                    </template>
                </Title>
                <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval" :menuId="menuId"
                    :projectId="projectId" ref="documentsRef" :readOnly="disabled" />
            </div>
        </a-form>

        <ProjectActions @submit="submitAgent" :menuInfo="menuInfo" title="发起尽调审批">
            <a-button size="large" @click="save"
                v-if="menuInfo.status == 0 && [0, 5, 9].includes(menuInfo.approvalStatus) && !disabled">暂存</a-button>
            <a-button size="large" @click="submit(3)"
                v-if="menuInfo.status == 0 && [0, 9].includes(menuInfo.approvalStatus) && !disabled">跳过此环节</a-button>
        </ProjectActions>
    </div>
</template>
<script setup>
import Team from '../correlation/Team.vue'
import { message } from 'ant-design-vue';
import { amountUnit } from '@/utils/tools';
import { useMenuTree } from './menu';
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

let dueDiligenceMethodList = ref([])
let dueDiligenceContentList = ref([])

const handleChange = (value) => {
    if (value.length == 0) { formData.value.dueDiligenceMethod ="";return; }
    var str = "";
    if(value.length >1) {
        str = value[1];
    }else{
        str = value[0];
    }
    formData.value.dueDiligenceMethod = str;
    dueDiligenceMethodList.value = [];
    dueDiligenceMethodList.value.push(str);
}
const handleChangeA = (value) => {
    if (value.length == 0) { formData.value.dueDiligenceContent ="";return; }
    var str = "";
    if(value.length >1) {
        str = value[1];
    }else{
        str = value[0];
    }
    formData.value.dueDiligenceContent = str;
    dueDiligenceContentList.value = [];
    dueDiligenceContentList.value.push(str);
}

const formAttrs = ['id', 'ddMode', 'ddTeamType', 'ddAmount', 'dueDiligenceContent', 'dueDiligenceMethod'];
const {
    formRef,
    documentsRef,
    formData,
    getInfo,
    submit,
    save,
    disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs, true);

const submitAgent = (type, temp) => {
    if (temp && temp.templateId == '1867210e0bbb3bcdf67c4c748429593d') {
        let result = documentsRef.value.getAssignStatus(19);
        if (result != 'success') {
            message.warning(result);
            return;
        }
    }
    submit(type, temp);
}
onMounted(() => {
    getInfo((res) => {
        if (formData.value.dueDiligenceMethod!= null && formData.value.dueDiligenceMethod != "") {
            dueDiligenceMethodList.value.push(formData.value.dueDiligenceMethod);
        }
        if (formData.value.dueDiligenceContent!= null && formData.value.dueDiligenceContent != "") {
            dueDiligenceContentList.value.push(formData.value.dueDiligenceContent);
        }
    });
})
</script>
<style scoped lang="less"></style>
