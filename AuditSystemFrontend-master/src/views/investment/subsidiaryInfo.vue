<template>
    <div class="subsidiary_info content-inner" v-if="loadReady">
        <a-page-header :ghost="false"
            :breadcrumb="{ routes }">
            <template #title>
                <EllipsisTooltip style="width:500px" class="flex_full" :content="infoData.name"/>
            </template>
            <template #extra>
                <a-button size="large" @click="router.back()">返回</a-button>
                <a-button size="large" @click="router.push('/innerPage/projectInfo?code=lxsp&id='+infoData.projectId)">投资进程回顾</a-button>
                <a-button size="large" @click="stepChange(13)" v-permissionInvestment="['biz:projectCompany:changeExit']">项目退出</a-button>
                <a-button size="large" type="primary" v-permissionInvestment="['biz:projectCompany:edit']" @click="router.push('/innerPage/subsidiaryEdit?id='+companyId)">企业工商信息变更</a-button>
            </template>
            <div class="header_info">
                <div class="header_description">
                    <a-descriptions size="small" :column="{ xxl: 3, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                        <a-descriptions-item label="公司编号">{{infoData.companyBizNo || '-'}}</a-descriptions-item>
                        <a-descriptions-item label="业务所属部门">{{infoData.businessDeptName || '-'}}</a-descriptions-item>
                        <a-descriptions-item label="注册资本（万元）">{{infoData.registeredCapital || '-'}}</a-descriptions-item>
                        <a-descriptions-item label="成立日期">{{infoData.incorporationTime || '-'}}</a-descriptions-item>
                        <a-descriptions-item label="所在地">{{infoData.provinceName}}/{{infoData.cityName}}/{{infoData.areaName}}/</a-descriptions-item>
                        <a-descriptions-item label="财务对接人">
                            <UserBox v-if="infoData.financialHandoverUser !=null" :data="infoData.financialHandoverUser" single descIn/>
                            <UserBox v-else :data="infoData.financialHandoverUser || infoData.principal || {}" single descIn/>
                        </a-descriptions-item>
                        <a-descriptions-item label="投前项目名称">
                            <router-link :to="'/innerPage/projectInfo?id='+infoData.projectId" class="color-link">
                                {{infoData.projectName}}
                            </router-link>
                        </a-descriptions-item>
                        <a-descriptions-item label="投前项目归属人">
                            <UserBox :data="infoData.attributorUser || {}" single descIn/>
                        </a-descriptions-item>
                        <a-descriptions-item label="是否实缴">{{infoData.paidCapitalStatusStr || '-'}}</a-descriptions-item>
                    </a-descriptions>
                </div>
                <div class="extra">
                    <div :style="{display: 'flex',width: 'max-content',justifyContent: 'flex-end',textAlign:'right'}">
                        <a-statistic title="投资类型" :value="infoData.investmentTypeStr" :style="{marginRight: '32px',}"/>
                        <a-statistic title="投后状态" :value="infoData.serviceStatusStr || ' '" />
                    </div>
                </div>
            </div>
        </a-page-header>
        <a-tabs v-model:activeKey="stepCurrent" type="card" @change="stepChange" class="auto_tabs">
            <a-tab-pane v-for="(item,index) in pageTree" :key="index" :tab="item.name"></a-tab-pane>
        </a-tabs>
        <div class="step_content">
            <component :is="components[componentCode]" :companyId="companyId" :menuInfo="menuInfo" :existProjectTeam="existProjectTeam"></component>
        </div>
    </div>
    <LoadingComponent v-else/>
</template>
<script setup>
import LoadingComponent        from '@/components/LoadingComponent.vue'
import api                     from '@/api/index';
import { onBeforeRouteUpdate } from 'vue-router';
import { message , Modal }     from 'ant-design-vue';
import { mainStore }           from '@/store';
const store  = mainStore();
const routes = [
    {
        breadcrumbName : '投后管理',
        path           : '/investment'
    },
    {
        breadcrumbName: '子公司详情',
    },
]
const router    = useRouter();
const route     = useRoute();
const companyId = ref(Number(route.query.id || 0))
const to        = ref(route.query.code  || 'qyxx');
const infoData  = ref({roleKeys:[]});

onBeforeRouteUpdate((to) => {
    let id = Number(to.query.id || 0);
    if(id!=companyId.value){
        companyId.value = id;
        getInfo(()=>{
            getTree();
        });
    }
});

const getInfo  = (callBack)=>{
    api.investment.correlationGet(companyId.value,'projectCompany').then(res=>{
        if(res.code==200){
            infoData.value = res.data;
            callBack && callBack();
        }
    })
}
const getAutoParams = (params)=>{
    return computed(()=>{
        return params?infoData.value[params]:infoData.value;
    })
}
provide('getAutoParams',getAutoParams);

onMounted(() => {
    getInfo(()=>{
        getTree();

        getPermission();

        inProject(infoData.value.projectId)
    });

})
const existProjectTeam = ref(false)
const inProject = (projectId)=>{
  api.investment.inProject(projectId).then(res=>{
    if(res.code==200){
      existProjectTeam.value = res.data || false;
    }
  })
}
const loadReady = ref(false);
const getPermission = ()=>{
    loadReady.value = false;
    api.sys.projectPermission(infoData.value.projectId).then(res=>{
        store.setBusinessPermissions(res.data,'investment');
        loadReady.value = true;
    })
}
provide('getPermission',getPermission);

const pageTree    = ref([]);
const stepCurrent = ref(0);
const modules     = import.meta.glob("./**/*.vue");
const getTree     = ()=>{
    api.investment.stepTree().then(res=>{
        if(res.code==200){
            pageTree.value = res.data;

            pageTree.value.forEach((item, i) => {
                let path = './components/menuTree/'+item.code+'.vue';
                if(item.code==to.value){
                    stepCurrent.value = i;
                }
                components[item.code] = defineAsyncComponent({
                    loader           : modules[path],
                    loadingComponent : LoadingComponent,
                });
            });
            stepChange(stepCurrent.value);
        }
    })
}
const components    = {};
const componentCode = computed(()=>{
    return (pageTree.value[stepCurrent.value] || {}).code;
});
const stepChange    = (current)=>{
    stepCurrent.value = current;
    replaceRouter((pageTree.value[current] || {}).code);
}
const menuInfo = computed(()=>{
    return pageTree.value[stepCurrent.value] || {};
})
const replaceRouter = (code)=>{
    let query = {...route.query,code:code};
    router.replace({query:query});
}
</script>
<style scoped lang="less">
.auto_tabs{
    overflow:visible;
    :deep(.ant-tabs-nav){
        margin-bottom: -1px;
        margin-left: -1px;

        .ant-tabs-nav-operations{
            background-color: #fff;
            border-radius: 4px 4px 0 0;
        }
        .ant-tabs-tab{
            background-color : #fffaf0;
            box-shadow       : 0 -4px 4px rgba(249,156,52,0.1) inset;
            position         : relative;
            overflow         : hidden;
            color            : @primary-color;
            .ant-tabs-tab-btn{
                z-index    : 2;
                position   : relative;
                transition : all 0.3s;
            }
            &:hover{
                opacity: 0.85;
            }
            &::after{
                content          : '';
                height           : 0;
                width            : 100%;
                background-color : @primary-color;
                border-radius    : 1px;
                position         : absolute;
                bottom           : 0;
                left             : 0;
                transition       : all 0.3s;
                z-index          : 1;
            }
        }
        .ant-tabs-tab-active{

            .ant-tabs-tab-btn{
                color : #fff;
            }
            &::after{
                height : 100%;
            }
        }
    }
}
.header_info{
    display         : flex;
    justify-content : space-between;
}

.step_content{
    background-color : #fff;
    border-radius    : 0 4px 4px 4px;
    display          : flex;
    flex-direction   : column;
    flex             : 1;

    :deep(.menu_inner){
        padding : 16px;
    }
    :deep(.pagination_table){
        text-align : right;
        padding    : 16px 0;
    }
}
</style>
