<template>
    <div class="content-box_full">
        <a-page-header :ghost="false"
            :breadcrumb="{ routes }">
            <template #title>
                <EllipsisTooltip style="width:500px" class="flex_full" :content="projectId?formData.projectName:'新增项目'"/>
            </template>
        </a-page-header>
        <AScrollbar style="margin:-16px -16px 0 -16px;">
            <div class="padding_box">
                <a-form layout="vertical" :model="formData" ref="formRef">
                    <a-collapse ghost expandIconPosition="right" v-model:activeKey="collapseKey" class="crl-collapse">
                        <a-collapse-panel key="base">
                            <template #header>
                                <h5 class="title_single">基础信息</h5>
                                <span class="color-danger">*</span>
                            </template>
                            <a-row :gutter="24">
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="总户数">
                                        <a-input-number placeholder="请输入" v-model:value="formData.househoidSum" :min="0" class="w_full"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="占地面积（m²）" name="floorSpace">
                                        <a-input  placeholder="请输入" allowClear v-model:value="formData.floorSpace"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="建筑面积（m²）" name="constructionArea">
                                        <a-input  placeholder="请输入" disabled allowClear v-model:value="formData.constructionArea"/>
                                    </a-form-item>
                                </a-col>
                                </a-row>
                                </a-collapse-panel>
                    </a-collapse>
                    <div class="menu_inner padding_box">
                        <OperationEdit type="TUO" :projectId="projectId" v-model="correlation" :readOnly="disabled" :businessTypeStr="businessTypeStr" :companyName="companyName" :projectName="projectName"/>
                    </div>
                    <div class="menu_inner padding_box">
                        <Risk :projectId="projectId" type="TUO" v-model="correlation" :readOnly="disabled"/>
                    </div>
                    <div class="menu_inner padding_box">
                        <Title title="立项收缴率指标信息"></Title>
                        <FullTable :columns="data.columnsApproval" :loading="loadding" :data-source="data.list">
                            <template #bodyCell="{ column,text,record,index }">
                                <template v-if="column.key === 'id'">
                                    {{'立项收缴率指标（%）'}}
                            </template>
                </template>
                        </FullTable>
                    </div>
                    <div class="menu_inner padding_box" style="margin-top: 100px;">
                        <ServiceSatis :projectId="projectId" type="TUO" v-model="correlation" :readOnly="disabled"/>
                        <ContractSatis :projectId="projectId" type="TUO" v-model="correlation" :readOnly="disabled"/>
                    </div>
                </a-form>
            </div>
        </AScrollbar>
        <FooterBar>
            <a-button size="large" @click="router.back()">取消</a-button>
            <a-button
                size="large"
                type="primary"
                @click="submit"
                :loading="submitLoading">提交</a-button>
        </FooterBar>
    </div>
</template>
<script setup>
import api                     from '@/api/index';
import {amountUnit , numFixed} from '@/utils/tools';
import { Modal,message }       from 'ant-design-vue';
import { useDictStore }        from '@/store/dict';
import Shareholder             from './components/correlation/Shareholder.vue';
import Executives              from './components/correlation/Executives.vue'
import Pool                    from './components/correlation/Pool.vue'
import { mainStore }           from '@/store';
import OperationEdit from './components/correlation/OperationEdit.vue'
import Risk from './components/correlation/Risk.vue'
import ServiceSatis from './components/correlation/ServiceSatis.vue'
import ContractSatis from './components/correlation/ContractSatis.vue'


const store     = mainStore();
const dict      = useDictStore();
const router    = useRouter();
const route     = useRoute();
const projectId = ref(Number(route.query.id || 0))
const businessTypeStr = ref(String(route.query.businessTypeStr || 0))
const companyName = ref(String(route.query.companyName || 0))
const projectName = ref(String(route.query.projectName || 0))
const routes    = [
    {
        path           : 'expand',
        breadcrumbName : '拓后运营',
    },
    {
        breadcrumbName: '运营数据信息',
    },
];
const collapseKey    = ref(['base','zhaobiao']);
const formData       = ref({roleKeys:[]});
const formRef        = ref(null);
const submitLoading  = ref(false);
const homePageReload = inject('homePageReload');
const loadding = ref(false);
const data     = reactive({
    list    : [],
    columnsApproval: [
    {
            title     : '指标',
            key       : 'id',
            width     : 150
        },
        {
            title     : '第一年(%)',
            dataIndex : 'collectionRateFirst',
            width     : 150,
            ellipsis  : true,
        },
        {
            title     : '第二年(%)',
            dataIndex : 'collectionRateSecond',
            width     : 150,
            ellipsis  : true,
        },
        {
            title     : '第三年(%)',
            dataIndex : 'collectionRateThird',
            width     : 150,
            ellipsis  : true,
        }
    ],
})
const submit         = ()=>{
    formRef.value.validateFields().then(vRes=>{
        submitLoading.value = true;
        formData.value.projectId = projectId.value
        let apiAttr = formData.value.id ?'projectOperationEdit':'projectOperationAdd';
        api.project[apiAttr](formData.value).then(res=>{
            submitLoading.value = false;
            if(res.code==200){
                    router.back()
                    message.success('操作成功');
            }
        })
    }).catch(err=>{
        collapseKey.value = ['base','zhaobiao'];
        message.warning('请完善信息！');
    })
}

const getInfo  = async ()=>{
    let res = await api.project.projectOperationInfo(projectId.value);
    if(res.code==200){
        formData.value = res.data;
    }
}
//立项收缴率指标信息
const getOperationPage = ()=>{
    loadding.value = true;
    api.project.projectOperationList(projectId.value).then(res=>{
        if(res.code==200){
            data.list  = res.data;
            console.log(res.data)
        }
        loadding.value = false;
    })
}
onMounted(() => {
    if(projectId.value){
        getInfo();
        getOperationPage();
    }
})


</script>
<style scoped lang="less">
</style>
