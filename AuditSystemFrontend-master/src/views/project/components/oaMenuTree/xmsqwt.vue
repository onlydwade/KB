<template>
    <div class="menu_inner">
        <div class="card_box">
            <van-cell-group title=" 授权委托申请（选择性发起）">
                <van-cell title="签约主体" :value="formData.signDeptName || '-'" />
                <van-cell title="收费主体" :value="formData.chargeDeptName || '-'" />
                <van-cell title="运营主体" :value="formData.operationDeptName || '-'" />
                <van-cell title="是否涉及成立公司" :value="formData.isBranchOfficeStr || '-'" />
                <van-cell title="项目代管单位" :value="formData.projectManagementUnit || '-'" />
                <van-cell title="授权主体" :value="formData.authorizedEntity || '-'" />
                <van-cell title="授权内容" :value=" getAuthorizationContent(formData.authorizationContent)" />
                <van-cell title="情况说明" :value="formData.conditionExplain || '-'" />
            </van-cell-group>
            <FileList :menuId="menuId" :projectId="projectId" />
        </div>
    </div>
</template>

<script setup>
import { useMenuTree } from './menu';
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

const formAttrs = ['id', 'signDeptId', 'chargeDeptId', 'operationDeptId', 'conditionExplain'];
const {
    formRef,
    documentsRef,
    formData,
    getInfo,
    submit,
    save,
    disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs, false);
onMounted(() => {
    getInfo();
})

const getAuthorizationContent = (code) => {
    if ( code ==null|| code ==undefined||code =="") {  return ""}
    var str = "";
    let list  =JSON.parse(code)
    if (list.length >= 0) {
        str = list.join(",");
    } 
     
 return str;
}

</script>

<style scoped lang="less">
.menu_inner {
    :v-deep .van-cell__value {
        color: #000;
    }

    :v-deep .van-cell-group__title {
        color: #000;
        font-weight: bold;
    }

    .card_box {
        margin-bottom: 20px;
    }

    .bm {
        margin: 10px;
    }
}</style>