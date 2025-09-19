
<template>
    <div class="menu_inner">
        <a-form
        ref="formRef"
        layout="vertical"
        :model="formData">
            <div class="padding_box">
                <Title>
                    <template #title>
                        交接信息填写
                        <span class="color-danger"> * </span>
                    </template>
                </Title>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="投后负责人" name="principalId">
                            <UserSelect :disabled="disabled" v-model="formData.principalId" :users="formData.principal"/>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="交接日期" name="handoverDate">
                            <a-date-picker :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="formData.handoverDate"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="业务所属部门" name="businessDeptName">
                            <a-input :disabled="disabled" v-model:value="formData.businessDeptName" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                </a-row>
                <Title>
                    <template #title>
                        风险检查点 
                    </template>
                </Title>
                <RiskCheck :projectId="projectId" :readOnly="disabled"/>
            </div>
        </a-form>

        <ProjectActions @submit="submit" :menuInfo="menuInfo">
            <a-button size="large" @click="save" v-if="(menuInfo.status==0&&[0,9].includes(menuInfo.approvalStatus))||!disabled">暂存</a-button>
        </ProjectActions>
    </div>
</template>
<script setup>
import RiskCheck from '../correlation/RiskCheck.vue'
import { useMenuTree } from './menu';
const props = defineProps({
    projectId: {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    }
})
const formAttrs = ['id','principalId','principal','handoverDate','businessDeptName'];
const {
    formRef,
    formData,
    getInfo,
    submit,
    save,
    disabled
} = useMenuTree(props.projectId,toRefs(props).menuInfo,formAttrs,true);
onMounted(() => {
    getInfo();
})
</script>
<style scoped lang="less">
</style>
