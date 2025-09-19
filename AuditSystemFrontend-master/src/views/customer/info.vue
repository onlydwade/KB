<template>
    <div class="content-inner">
        <a-page-header :ghost="false" :title="infoData.customerName" :breadcrumb="{ routes }">
            <template #extra>
                <a-button size="large" @click="router.back()">返回</a-button>
                <a-button size="large" @click="handleComponent('delete')"
                    v-if="[infoData.createUserId, infoData.followUserId].includes(store.userInfo.userId) || hasPermission(['biz:customer:delete'])">删除</a-button>
                <a-button size="large" @click="handleComponent('claim')"
                    v-if="!infoData.followUserId && hasPermission(['biz:customer:claim'])">认领</a-button>
                <a-button size="large" type="primary" @click="router.push('/innerPage/customerEdit?id=' + customerId)"
                    v-if="[infoData.createUserId, infoData.followUserId].includes(store.userInfo.userId) || hasPermission(['biz:customer:update'])">编辑</a-button>
            </template>
            <template #footer>
                <a-tabs v-model:activeKey="tabKey" @change="tabChange">
                    <a-tab-pane key="1" tab="详细信息" />
                    <a-tab-pane key="2" tab="关联项目" />
                    <a-tab-pane key="3" tab="追踪动态" />
                    <a-tab-pane key="5" tab="战略合作" />
                    <a-tab-pane key="4" tab="变更记录" />
                </a-tabs>
            </template>
        </a-page-header>
        <div class="content-box_full" v-if="tabKey == 1">
            <AScrollbar>
                <Title title="基础信息"></Title>
                <div class="description_box">
                    <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                        <a-descriptions-item label="客户编码">{{ infoData.customerNo || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="客户全称">{{ infoData.customerName || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="客户来源">{{ infoData.sourceStr || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="合作类型">{{ infoData.cooperationTypeStr || '-' }}</a-descriptions-item>                        
                       
                        <a-descriptions-item label="跟进类型">{{ infoData.customerLevelStr || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="企业类型">{{ infoData.companyTypeStr || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="所属行业">{{ infoData.customerIndustryStr || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="客户类型">{{ infoData.customerTypeStr || '-' }}</a-descriptions-item>

                        <a-descriptions-item label="统一社会信用代码">{{ infoData.customerCompanyNo || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="跟进人">
                            <UserBox :data="infoData.followUserVO || {}" single descIn />
                        </a-descriptions-item>
                        <a-descriptions-item label="信息维护人">
                            <UserBox :data="infoData.maintenanceUserVO || {}" single descIn />
                        </a-descriptions-item>
                        <a-descriptions-item label="客户标签">
                            <KeyWords v-model="infoData.keywords" readOnly />
                        </a-descriptions-item>

                        <a-descriptions-item label="是否为简报客户">{{ infoData.workBriefStr || '-' }}</a-descriptions-item>                        
                        <a-descriptions-item label="客户所属省份">{{ infoData.provinceName || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="客户所属城市">{{ infoData.cityName || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="客户所属区/县">{{ infoData.areaName || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="客户详细地址">{{ infoData.address || '-' }}</a-descriptions-item>     
                        <br/>            
                        <br/>   
                        <br/>   
                        <!-- <a-descriptions-item></a-descriptions-item>           -->
                        <a-descriptions-item label="备注">{{ infoData.remark || '-' }}</a-descriptions-item>
                    </a-descriptions>
                </div>
                <Title title="企业信息"></Title>
                <div class="description_box">
                    <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                        <a-descriptions-item label="法人代表">{{ infoData.legalEntity  || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="注册资本">{{ infoData.registeredCapital  || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="人员规模">{{ infoData.personnelSize  || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="成立时间">{{ infoData.establishmentDate  || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="公司官网">{{ infoData.website  || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="注册地址">{{ infoData.registeredAddress  || '-' }}</a-descriptions-item>
                        <br>                        <br>
                        <a-descriptions-item label="公司简介">{{ infoData.companyIntroduction  || '-' }}</a-descriptions-item>
                    </a-descriptions>
                </div> 
                <Title title="联系人"></Title>
                <Contacts ref="listRef"  :customerId="customerId"  :readOnly="true"/>
                <Title title="系统信息"></Title>
                <div class="description_box">
                    <a-descriptions :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                        <a-descriptions-item label="创建人">{{ (infoData.createUser || {}).realname ||
                            '-' }}</a-descriptions-item>
                        <a-descriptions-item label="创建时间">{{ infoData.createTime || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="最后修改人">{{ (infoData.updateUser || {}).realname ||
                            '-' }}</a-descriptions-item>
                        <a-descriptions-item label="最后修改时间">{{ infoData.updateTime || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="最新跟进时间">{{ infoData.followTime || '-' }}</a-descriptions-item>
                    </a-descriptions>
                </div>
            </AScrollbar>
        </div>

        <!-- <LinkData :recordId="customerId" v-if="tabKey == 2" /> -->
        <customerProjectList :recordId="customerId" v-if="tabKey == 2" />
        <div class="content-box_full" v-if="tabKey == 3">
            <AScrollbar>
                <FollowCustomerList :recordId="customerId" moduleName="Customer"
                    :readOnly="![infoData.maintenanceUserId,infoData.createUserId, infoData.followUserId].includes(store.userInfo.userId) && !hasPermission(['biz:customer:follow'])" />
            </AScrollbar>
        </div>
        <div class="content-box_full" v-if="tabKey == 4">
            <AScrollbar>
                <OperLog :recordId="customerId" moduleName="Customer" />
            </AScrollbar>
        </div>
        <CooperationList :customerId="customerId" v-if="tabKey == 5"
            :readOnly="![infoData.maintenanceUserId,infoData.createUserId, infoData.followUserId].includes(store.userInfo.userId) && !hasPermission(['biz:customer:cooperation'])" />
        <BaseHandle ref="baseHandleRef" @success="updateInfo" />


    </div>
</template>
<script setup>
import api from '@/api/index';
import { onBeforeRouteUpdate } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import BaseHandle from './components/BaseHandle.vue'
// import LinkData from './components/LinkData.vue'
import customerProjectList from '../project/customerProjectlist.vue'
import Contacts         from './components/Contacts.vue';
import Cooperation from './components/Cooperation.vue'
import CooperationList from './components/cooperationList.vue'
import { hasPermission } from '@/utils/tools';
import { mainStore } from '@/store';
const store = mainStore();
const router = useRouter();
const route = useRoute();
const customerId = ref(Number(route.query.id || 0))
const tabKey = ref(route.query.tab || '1');
onBeforeRouteUpdate((to) => {
    if (urlReplace.value) {
        urlReplace.value = false;
        return;
    }
    customerId.value = Number(to.query.id || 0);
    tabKey.value = to.query.tab || '1';
    getInfo();
});

const routes = [
    {
        path: 'customer',
        breadcrumbName: '客户管理',
    },
    {
        breadcrumbName: '客户查看',
    },
];

const infoData = ref({});
const getInfo = () => {
    api.customer.customerInfo(customerId.value).then(res => {
        if (res.code == 200) {
            infoData.value = res.data;
        }
    })
}
onMounted(() => {
    getdepTInfo();
    getInfo();
})

const getdepTInfo = () => {
    getLoginUser();
    getDict();
    getDept();
    getPost();
}

const getLoginUser = () => {
    api.common.loginUser().then((res) => {
        if (res.code == 200) {
            store.setUserInfo(res);
            TpWatermark(res.user.realname || res.user.userName, '200', '300', '-20', 'black', '18', '.06');
        }
    })
}
const getDict = () => {
    api.sys.dictList().then(res => {
        if (res.code == 200) {
            dict.setDict(res.data || []);
        }
    })
}
const getDept = () => {
    api.sys.deptList().then(res => {
        if (res.code == 200) {
            store.setDeptTree(handleTree(res.data, "deptId"));
        }
    })
}
const getPost = () => {
    api.sys.postList().then(res => {
        if (res.code == 200) {
            store.setPostList(res.data);
        }
    })
}

const getAutoParams = (params) => {
    return computed(() => {
        return params ? infoData.value[params] : infoData.value;
    })
}
provide('getAutoParams', getAutoParams);

const urlReplace = ref(false);
const tabChange = (key) => {
    urlReplace.value = true;
    let query = { ...route.query, tab: key };
    router.replace({ query: query });
    if(key==1){
        getInfo();
    }
}

const baseHandleRef = ref(null);
const handleComponent = (component) => {
    if (component == 'delete') {
        baseHandleRef.value.open(infoData.value, 'delete');
    }
    if (component == 'claim') {     //认领
        Modal.confirm({
            title: '操作确认',
            content: '确认认领此客户?',
            onOk() {
                let postData = {
                    id: infoData.value.id,
                    followUserId: store.userInfo.userId
                }
                api.customer.customerClaim(postData).then(res => {
                    if (res.code == 200) {
                        message.success('操作成功');
                        updateRow(res.data);
                    }
                })
            }
        });
    }
}
const updateInfo = (res, index, type) => {
    if (type == 'delete') {
        router.back();
    } else {
        getInfo();
    }
}
</script>
<style scoped lang="less">
.description_box {
    margin: 16px;
    padding-bottom: 24px;
    margin-bottom: 24px;
    border-bottom: 1px solid @border-color-base;

    &:last-child {
        border-bottom: none;
        margin-bottom: 0;
    }
}
</style>