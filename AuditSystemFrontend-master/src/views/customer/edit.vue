<template>
    <div class="content-inner">
        <a-page-header :ghost="false"
            :title="customerId?customerName:'新增客户'"
            :breadcrumb="{ routes }">
        </a-page-header>
        <AScrollbar style="margin:-16px -16px 0 -16px;">
            <div class="padding_box">
                <EditForm ref="editFormRef" v-model:submitLoading="submitLoading" @success="submitSuccess"  :companyName="companyName" :isQlm="isQlm" :customerId="customerId"/>
            </div>
        </AScrollbar>
        <FooterBar>
            <a-button size="large" @click="router.back()">取消</a-button>
            <a-button size="large" type="primary" @click="submit" :loading="submitLoading">提交</a-button>
        </FooterBar>
    </div>
</template>
<script setup>
import api         from '@/api/index';
import { message } from 'ant-design-vue';
import EditForm    from './components/EditForm.vue'
const router     = useRouter();
const route      = useRoute();
const customerId = ref(Number(route.query.id || 0))
const companyName = ref(route.query.companyName)
const isQlm = ref(Number(route.query.isQlm || 0))
const routes     = [
    {
        path           : 'customer',
        breadcrumbName : '客户管理',
    },
    {
        breadcrumbName : '客户编辑',
    },
];

const submitLoading = ref(false);
const editFormRef   = ref(null);
const customerName = computed(()=>{
    return ((editFormRef.value || {}).formData || {}).customerName;
})
const submit        = ()=>{
    editFormRef.value.submit();
}
const submitSuccess = ()=>{
    router.back()
    message.success('操作成功');
}
</script>
<style scoped lang="less">

</style>