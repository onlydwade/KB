<template>
    <div class="content-inner">
        <a-page-header :ghost="false" :breadcrumb="{ routes }">
            <template #title>
                <EllipsisTooltip style="width:500px" class="flex_full" :content="infoData.projectName" :mode="1"
                    :processState="infoData.processState" />
            </template>
            <template #extra>
                <a-button size="large" @click="router.back()">返回</a-button>
                <a-button size="large"
                    v-if="(infoData.principalShow == true || hasPermission(['biz:projectExtension:review']))"
                    @click="router.push('/innerPage/projectInfo?id=' + projectId)">拓前信息回顾</a-button>
                <!-- <a-button size="large"
                    v-if="(infoData.principalShow == true || hasPermission(['biz:projectExtension:operation'])) && infoData.isSyncOperation !== 'SHI'"
                    @click="handleComponent('syncOper')">同步到运营</a-button> -->
                <a-button size="large"
                    v-if="(infoData.principalShow == true || hasPermission(['biz:projectExtension:operation'])) && infoData.isSyncOperation === 'SHI'"
                    @click="router.push('/innerPage/operationInfo?id=' + projectId + '&businessTypeStr=' + businessTypeStr + '&companyName=' + companyName + '&projectName=' + projectName)">运营数据更新</a-button>
                <!-- <a-button size="large"
                    v-if="(hasPermission(['biz:projectExtension:renew']) || infoData.attributorOrCreateShow == true) && [0, 3].includes(infoData.processState)"
                    @click="handleComponent('renewal')">续签</a-button>
                <a-button size="large"
                    v-if="(hasPermission(['biz:projectExtension:newBid']) || infoData.attributorOrCreateShow == true) && [0, 3].includes(infoData.processState)"
                    @click="handleComponent('rebid')">重新招标</a-button> -->
                <!-- <a-button size="large"
                    v-if="(infoData.principalShow == true && infoData.expire != 'YI_SHI_XIAO' && infoData.serviceStatus == 'YI_DAO_QI')"
                    @click="handleComponent('finish')">完结</a-button> -->
                <a-button size="large"
                    v-if="(infoData.principalShow == true || hasPermission(['biz:projectExtension:exit'])) && [0, 3].includes(infoData.processState) && infoData.isSyncOperation === 'SHI'"
                    @click="router.push('/innerPage/extensionInfo?id=' + projectId + '&code=thxmxx')">退场</a-button>

            </template>
            <div class="header_info">
                <div class="header_description">
                    <a-descriptions size="small" :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                        <a-descriptions-item label="项目编号">{{ infoData.projectNo || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="关键词">
                            {{ infoData.keywords || '-' }}
                        </a-descriptions-item>
                        <a-descriptions-item label="合作模式" v-if="infoData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">
                            {{ infoData.cooperationTypeStr || '' }}
                            <span v-if="infoData.cooperationTypeOther">,{{ infoData.cooperationTypeOther }}</span>
                        </a-descriptions-item>
                        <a-descriptions-item label="拓展模式" v-else>
                            {{ infoData.expansionModeStr || '-' }}
                        </a-descriptions-item>
                        <a-descriptions-item label="是否投标">
                            {{ infoData.projectType == 'DAN_YI_TOU_BIAO_XIANG_MU' ? '是' : '否' }}
                        </a-descriptions-item>
                        <a-descriptions-item label="运营团队">{{ infoData.operationTeamName || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="合同到期日期">{{ infoData.serviceEndTime || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="拓后负责人">
                            <UserBox :data="infoData.principal || {}" single descIn />
                        </a-descriptions-item>
                        <a-descriptions-item label="处理方式">
                            {{ infoData.processMode == 0 ? '未续签' : infoData.processMode == 1 ?
                                '续签' : infoData.processMode == 2 ? '重新投标' : infoData.processMode == 3 ? '退场' :
                                    '-' }}
                        </a-descriptions-item>
                    </a-descriptions>
                </div>
                <div class="extra">
                    <div :style="{ display: 'flex', width: 'max-content', justifyContent: 'flex-end', textAlign: 'right' }">
                        <a-statistic title="状态" :value="infoData.serviceStatusStr" :style="{ marginRight: '32px', }" />
                        <a-statistic title="优先级" :value="infoData.projectLevelStr || ' '" />
                    </div>
                </div>
            </div>
        </a-page-header>
        <div class="project_step">
            <!-- <a-steps v-model:current="stepCurrent" @change="stepChange">
                <template v-for="(item,index) in pageTree">
                    <a-step v-if="item.code=='jcxx'" :key="item.id" :title="item.name">
                        <template #icon>
                            <div class="percent_box">
                                100%
                            </div>
                        </template>
                    </a-step>
                    <a-step v-else :key="item.id" :title="(item.code=='yjqr'&&infoData.inStock=='SHI')?'续签确认':item.name">
                        <template #icon>
                            <div class="percent_box">
                                {{item.percent}}%
                            </div>
                        </template>
                    </a-step>
                </template>
            </a-steps> -->
            <div class="progress_box">
                <div class="progress_box_bg">
                    <div class="progress_box_progress" :style="'width:' + progress"></div>
                </div>
            </div>
            <template v-for="(item, index) in pageTree" :key="item.id">
                <div class="step_item"
                    :class="{ 'step_item_success': stepAcitive > index, 'step_item_on': stepCurrent == index, 'step_item_ing': stepAcitive == index, }"
                    @click="stepChange(index)">
                    <div class="percent_box">
                        <file-search-outlined v-if="item.code == 'thxmxx'" :style="{ fontSize: '20px', color: '#fff' }" />
                        <UnorderedListOutlined v-if="item.code == 'thyy'" />
                        <span v-else>
                            <template v-if="item.percent == 100">
                                <CheckOutlined />
                            </template>
                            <template v-else>
                                <!-- {{item.percent}}% -->
                                <clock-circle-outlined />
                            </template>
                        </span>
                    </div>
                    <div class="title">{{ (item.code == 'yjqr' && infoData.inStock == 'SHI') ? '续签确认' : item.name }}</div>
                </div>
            </template>
        </div>
        <component :is="components[componentCode]" :projectId="projectId" :menuList="menuData" @ok="getPageTree('refresh')"
            :projectInfo="infoData"></component>
        <BaseHandle ref="baseHandleRef" @success="getInfo" />
        <ChangeBelonger ref="changeBelongerRef" @success="getInfo" />
        <StopDrawer ref="stopDrawerRef" @success="getInfo" />
    </div>
</template>
<script setup>
import api from '@/api/index';
import { onBeforeRouteUpdate } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import { hasPermission } from '@/utils/tools';
import BaseHandle from './components/BaseHandle.vue'
import ChangeBelonger from './components/ChangeBelonger.vue'
import StopDrawer from './components/StopDrawer.vue'
import { useDictStore } from '@/store/dict';
const dict = useDictStore();
const router = useRouter();
const route = useRoute();
const projectId = ref(Number(route.query.id || 0))
const businessTypeStr = ref(String(route.query.businessTypeStr || 0))
const companyName = ref(String(route.query.companyName || 0))
const projectName = ref(String(route.query.projectName || 0))
const to = ref(route.query.to || route.query.code || 'xmxx');
const infoData = ref({ roleKeys: [] });
onBeforeRouteUpdate((to) => {
    let id = Number(to.query.id || 0);
    if (id != projectId.value) {
        projectId.value = id;
        to.value = to.query.code || 'xmxx';
        getInfo(() => {
            getPageTree();
        });
    } else {
        if (to.query.code) {
            to.value = to.query.code;
            getPageTree();
        }
    }
});
const routes = [
    {
        path: 'expand',
        breadcrumbName: '在管项目库',
    },
    {
        breadcrumbName: '项目查看',
    },
]
const getInfo = (callBack) => {
    api.project.projectExtensionInfo(projectId.value).then(res => {
        if (res.code == 200) {
            infoData.value = res.data;
            callBack && callBack();
        }
    })
}

const stepCurrent = ref(0);
const stepAcitive = ref(1);
const pageTree = ref([]);
const progress = computed(() => {
    return Math.min(stepAcitive.value * 100 / (pageTree.value.length - 1), 100) + '%';
})
const modules = import.meta.glob("./**/*.vue");
const getPageTree = (handle) => {
    api.project.treeByProjectId(projectId.value).then(res => {
        if (res.code == 200) {
            let arrs = [0, 0, 0];
            pageTree.value = res.data.map((item, k) => {
                item.percent = 0;
                let done = 0;
                item.children.forEach((subItem, i) => {
                    if (item.code == 'thxmxx') {
                        subItem.uncheck = true;
                    }
                    if (subItem.children && subItem.children.length > 0) {
                        let subDone = 0;
                        subItem.children.forEach((thirdItem, x) => {
                            if (thirdItem.status) {
                                subDone += Math.ceil(100 / subItem.children.length);
                            }
                            if (thirdItem.code == to.value) {
                                arrs = [k, i, x]
                            }
                            thirdItem.approvalStatus = thirdItem.approvalStatus || 0;
                        });
                        done += subDone;
                    } else {
                        if (subItem.status) {
                            done += 100;
                        }
                        if (subItem.code == to.value) {
                            arrs = [k, i, 0]
                        }
                    }
                    subItem.approvalStatus = subItem.approvalStatus || 0;
                });
                item.percent = Math.ceil(done / item.children.length);
                item.percent = item.percent > 100 ? 100 : item.percent;

                item.approvalStatus = item.approvalStatus || 0;
                if (item.percent == 100 || item.code == 'thxmyy') {
                    stepAcitive.value = k + 1;
                }
                //异步注册加载组件
                if (handle != 'refresh') {
                    components[item.code] = defineAsyncComponent(modules['./components/menuTree/SecondMenu.vue']);
                }
                // if(infoData.value.serviceStatus=='WEI_ZHONG_BIAO'&&(item.code=='yjqr' || item.code=='thyj')){
                //     item.disabled = true;
                // }
                return item;
            });

            if (handle != 'refresh') {
                treeTabs.value = arrs;
                stepCurrent.value = arrs[0];
            }
        }
    })
}

const refreshTree = async (stepMenuId, status, callBack, approvalStatus) => {
    if (stepMenuId) {
        let postData = {
            projectId: projectId.value,
            stepMenuId: stepMenuId,
            status: status,
        }
        if (approvalStatus == 8) {
            postData.approvalStatus = approvalStatus
        }
        // let comp = await api.project.stepStatusUpdate(postData);
    }
    getInfo();
    getPageTree('refresh');
    callBack && callBack();
}

const treeTabs = ref([0, 0, 0]);
const treeTabChange = (data) => {
    treeTabs.value[data.index] = data.value;
}
provide('treeTabs', treeTabs);
provide('treeTabChange', treeTabChange);
provide('refreshMenuTree', refreshTree);
const getAutoParams = (params) => {
    return computed(() => {
        return params ? infoData.value[params] : infoData.value;
    })
}
provide('getAutoParams', getAutoParams);

onMounted(() => {
    getInfo(() => {
        getPageTree();
    });
})

const components = {};
const componentCode = computed(() => {
    return (pageTree.value[stepCurrent.value] || {}).code;
});
const stepChange = (current) => {
    if (current > stepAcitive.value || pageTree.value[current].disabled) {
        // message.warning('请先完成前方流程！')
        return;
    }
    stepCurrent.value = current;
    treeTabs.value[0] = current;
    treeTabs.value[1] = 0;
}
const menuData = computed(() => {
    return (pageTree.value[stepCurrent.value] || {}).children;
})

const baseHandleRef = ref(null);
const changeBelongerRef = ref(null);
const stopDrawerRef = ref(null);
const handleComponent = (component) => {
    switch (component) {
        case 'close':   // 终止
            stopDrawerRef.value.open(infoData.value);
            break
        case 'changeBelonger':   // 变更归属人
            changeBelongerRef.value.open(infoData.value);
            break
        case 'rebid':   // 重新招标
            Modal.confirm({
                title: '项目重新投标确认',
                content: '请问是否确定重新投标',
                onOk() {
                    // api.project.projectExtensionNewBid(projectId.value).then(res => {
                    //     if (res.code == 200) {
                    //         message.success('操作成功');
                    //     }
                    // })

                    router.push(`/innerPage/renewalEdit?id=${projectId.value}&type=2`)
                }
            })
            break
        case 'renewal': // 续签
            Modal.confirm({
                title: '项目发起续签确认',
                content: '请问是否确定发起续签流程',
                onOk() {
                    // api.project.projectExtensionRenew(projectId.value).then(res => {
                    //     if (res.code == 200) {
                    //         message.success('操作成功');
                    //     }
                    // })
                    router.push(`/innerPage/renewalEdit?id=${projectId.value}&type=1`)
                }
            });
            break
        case 'finish':
            Modal.confirm({
                title: '项目完结确认',
                content: `当前项目到期时间为${dateFormat(infoData.value.serviceEndTime, 'YYYY-MM-DD')}，是否确定需要完结？`,
                onOk() {
                    api.project.projectEdit({ id: projectId.value, serviceStatus: 'YI_WAN_JIE' }).then((res) => {
                        if (res.code == 200) {
                            message.success('操作成功')
                        }
                    })
                }
            })
            break
        case 'syncOper':
            Modal.confirm({
                title: '项目同步拓后运营确认',
                content: '请问是否确定同步项目到拓后运营？',
                onOk() {
                    api.project.syncOperationProject(row.id).then((res) => {
                        if (res.code == 200) {
                            message.success('操作成功')
                        }
                    })
                }
            })
            break
        default:

    }
}
</script>
<style scoped lang="less">
.content-box {
    padding: 16px 0;
}

// .project_step{
//     background-color : #fff;
//     border-radius    : 4px;
//     padding          : 16px;
//     margin-bottom    : 16px;
//     .percent_box{
//         width            : 56px;
//         height           : 56px;
//         text-align       : center;
//         background-color : #999;
//         border-radius    : 50%;
//         color            : #fff;
//         line-height      : 56px;
//         font-size        : 18px;
//         transition       : all 0.3s;
//     }
//     :deep(.ant-steps-item-container){
//         display     : flex;
//         align-items : center;
//         &:hover{
//             .percent_box{
//                 background-color :@primary-color;
//                 opacity: 0.5;
//             }
//         }
//     }
//     :deep(.ant-steps-item-active){
//         .percent_box{
//             background-color : @primary-color;
//             opacity          : 1!important;
//         }
//     }
// }
.header_info {
    display: flex;
    justify-content: space-between;
}

.project_step {
    display: flex;
    justify-content: space-between;
    background-color: #fff;
    border-radius: 4px;
    padding: 0 16px;
    margin-bottom: 16px;
    position: relative;

    .percent_box {
        width: 56px;
        height: 56px;
        text-align: center;
        background-color: #999;
        border-radius: 50%;
        color: #fff;
        line-height: 58px;
        font-size: 20px;
        transition: all 0.3s;
    }

    .step_item {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        cursor: not-allowed;
        padding: 16px 0;
        transition: all 0.3s;
        border-left: 8px solid #fff;
        border-right: 8px solid #fff;
        background-color: #fff;
        z-index: 10;
        position: relative;

        .percent_box {
            opacity: 0.8;
        }

        .title {
            text-align: center;
            font-size: 14px;
            line-height: 16px;
            white-space: nowrap;
            margin-top: 4px;
        }
    }

    .step_item_success {
        cursor: pointer;

        .percent_box {
            background-color: @primary-color;
        }

        &:hover {
            .percent_box {
                opacity: 1;
            }
        }
    }

    .step_item_ing {
        cursor: pointer;

        .percent_box {
            background-color: @error-color;
        }

        &:hover {
            .percent_box {
                opacity: 1;
            }
        }
    }

    .step_item_on {
        padding: 0;

        .percent_box {
            opacity: 1;
            width: 76px;
            height: 76px;
            line-height: 76px;
        }

        .title {
            color: @primary-color;
        }
    }

    .step_item_disabled {
        cursor: not-allowed;

        .percent_box {
            background-color: #999;
            color: #fff;
            opacity: 0.8;
            color: #fff;
        }

        .title {
            color: @text-color;
        }

        &:hover {
            .percent_box {
                opacity: 0.8;
            }
        }
    }

    .progress_box {
        height: 2px;
        z-index: 1;
        position: absolute;
        width: 100%;
        box-sizing: border-box;
        padding: 0 24px;
        top: 50%;
        left: 0;
        margin-top: -10px;

        .progress_box_bg {
            background-color: #f0f2f5;
            height: 2px;
            border-radius: 1px;
            position: relative;
        }

        .progress_box_progress {
            background-color: @primary-color;
            height: 2px;
            border-radius: 1px;
            position: absolute;
            left: 0;
            top: 0;
            width: 0%;
        }
    }
}
</style>
