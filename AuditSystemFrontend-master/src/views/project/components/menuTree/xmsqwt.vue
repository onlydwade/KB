<template>
    <div class="menu_inner">
        <a-form ref="formRef" layout="vertical" :model="formData">
            <div class="padding_box">
                <Title>
                    <template #title>
                        授权委托申请（选择性发起）
                        <span class="color-danger"> * </span>
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="签约主体" name="signDeptId">
                            <DeptSelect :disabled="disabled" :noRoot="true" v-model="formData.signDeptId" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="收费主体" name="chargeDeptId">
                            <DeptSelect :disabled="disabled" :noRoot="true" v-model="formData.chargeDeptId" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="运营主体" name="operationDeptId">
                            <DeptSelect :disabled="disabled" :noRoot="true" v-model="formData.operationDeptId" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="是否涉及成立分公司" name="isBranchOffice">
                            <a-select :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.isBranchOffice" class="w_full" placeholder="请选择"
                                :options="dict.options('SHI_FOU')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col v-if="formData.isBranchOffice === 'SHI'" :xxl="6" :lg="8" :sm="12">
                        <a-form-item :required="formData.isBranchOffice === 'SHI'" label="项目代管单位"
                            name="projectManagementUnit">
                            <a-input :disabled="disabled" v-model:value="formData.projectManagementUnit"
                                placeholder="请输入" />
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="授权主体" required name="authorizedEntity">
                            <a-select @change="handleChangeA" v-model:value="authorizedEntityList" :options="[{ label: '总部委托分公司', value: '总部委托分公司' },
        { label: '总部委托合资公司/子公司', value: '总部委托合资公司/子公司' }, { label: '分公司委托合资公司/子公司', value: '分公司委托合资公司/子公司' }
            , { label: '子公司/合资公司委托分公司  ', value: '子公司/合资公司委托分公司  ' }]"
                                mode="tags">
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="授权内容" required name="authorizationContent">
                            <a-select @change="handleChange" v-model:value="authorizationContentList"
                                :options="[{ label: '签约授权', value: '签约授权' }, { label: '收款授权', value: '收款授权' }, { label: '运营授权', value: '运营授权' }]"
                                mode="tags">
                            </a-select>
                        </a-form-item>
                    </a-col>
                </a-row>
                <!-- <a-form-item required label="盖章类别" name="stampCategory">
                    <a-space style="font-size: 12px;">
                        <a-radio-group :disabled="disabled" v-model:value="formData.stampCategory" size="small">
                            <a-radio value="companyStamp">加盖公司公章、法人章</a-radio>
                        </a-radio-group>
                    </a-space>
                </a-form-item> -->
                <a-form-item required label="情况说明" name="conditionExplain">
                    <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea"
                        v-model:value="formData.conditionExplain" placeholder="请输入(100字以内)" show-count
                        :maxlength="100" />
                </a-form-item>

                <Title
                    v-if="(formData.id && (formData.signDeptId != formData.chargeDeptId || formData.signDeptId != formData.operationDeptId)) || (formData.signDeptId == null && formData.chargeDeptId == null && formData.operationDeptId == null)"
                    title="附件上传">
                    <a-form-item name="fileOk" :rules="{ required: true, message: '( 请上传必传文件后提交!!! )', }">
                    </a-form-item>
                </Title>
                <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval"
                    v-if="(formData.id && (formData.signDeptId != formData.chargeDeptId || formData.signDeptId != formData.operationDeptId)) || (formData.signDeptId == null && formData.chargeDeptId == null && formData.operationDeptId == null)"
                    :menuId="menuId" :projectId="projectId" v-model="formData.fileOk" ref="documentsRef"
                    :readOnly="disabled" />
            </div>
        </a-form>
        <ProjectActions
            v-if="(formData.id && (formData.signDeptId != formData.chargeDeptId || formData.signDeptId != formData.operationDeptId)) || (formData.signDeptId == null && formData.chargeDeptId == null && formData.operationDeptId == null)"
            @submit="submitAgent" :menuInfo="menuInfo" offLineTitle="已线下签署，上传盖章版委托书标记完成">
            <a-button size="large" @click="save"
                v-if="menuInfo.status == 0 && [0, 5, 9].includes(menuInfo.approvalStatus) || !disabled">暂存</a-button>
            <!-- <a-button size="large" @click="submit(3)"
                v-if="(formData.signDeptId!=null&&formData.signDeptId== formData.chargeDeptId&& formData.signDeptId== formData.operationDeptId)  && menuInfo.status == 0 && [0, 9].includes(menuInfo.approvalStatus) && !disabled">跳过此环节</a-button> -->
        </ProjectActions>

        <a-button style="margin-left: 20px;" size="large" @click="save"
            v-show="(menuInfo.status == 0 && [0, 5, 9].includes(menuInfo.approvalStatus) ) || !disabled">暂存</a-button>
        <a-button size="large" style="margin-left: 20px;" @click="submit(3)"
            v-show="(formData.signDeptId != null && formData.signDeptId == formData.chargeDeptId && formData.signDeptId == formData.operationDeptId) && menuInfo.status == 0 && [0, 9].includes(menuInfo.approvalStatus) && !disabled">跳过此环节</a-button>
    </div>
</template>
<script setup>
import { useMenuTree } from './menu';
import { message } from 'ant-design-vue';

import { useDictStore } from '@/store/dict';
import { ref } from 'vue';
const dict = useDictStore();
let authorizationContentList = ref([])
let authorizedEntityList = ref([])

const handleChange = (value) => {
    if (value.length == 0) { formData.value.authorizationContent ="";return; }
    var str = "";
    if (value.length > 1) {
        str = value[1];
    } else {
        str = value[0];
    }
    formData.value.authorizationContent = JSON.stringify(value);
    // authorizationContentList.value = [];
    // authorizationContentList.value.push(str);
}
const handleChangeA = (value) => {
    if (value.length == 0) { formData.value.authorizedEntity ="";return; }
    var str = "";
    if (value.length > 1) {
        str = value[1];
    } else {
        str = value[0];
    }
    formData.value.authorizedEntity = str;
    authorizedEntityList.value = [];
    authorizedEntityList.value.push(str);
}


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

const formAttrs = ['id', 'signDeptId', 'chargeDeptId', 'operationDeptId', 'isBranchOffice', 'projectManagementUnit', 'conditionExplain', 'authorizedEntity', 'authorizationContent'];
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
        console.log(formData.value.authorizationContent);
        if (formData.value.authorizationContent != null && formData.value.authorizationContent != "") {
            authorizationContentList.value = JSON.parse(formData.value.authorizationContent);
        }
        if (formData.value.authorizedEntity != null && formData.value.authorizedEntity != "") {
            authorizedEntityList.value.push(formData.value.authorizedEntity);
        }
    });
})
const submitAgent = (type, temp) => {
    if (type == 0 || type == -1) {
        let result = documentsRef.value.getAssignStatus(10);
        if (result != 'success') {
            message.warning(result);
            return;
        }
    }
    submit(type, temp);
}
</script>
<style scoped lang="less"></style>
