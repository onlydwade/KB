<template>
    <div class="content-inner">
        <a-page-header :ghost="false" :breadcrumb="{ routes }">
            <template #title>
                <EllipsisTooltip style="width:800px" class="flex_full" :content="infoData.projectName" />
            </template>
            <template #extra>
                <a-button size="large" @click="router.back()">返回</a-button>
                <a-button size="large" @click="callbackProjectStep"
                    v-if="hasPermission(['biz:projectStep:callbackProjectStep'])">回退</a-button>
                <a-button size="large" @click="handleComponent('expire')"
                    v-if="infoData.expire != 'YI_SHI_XIAO' && ([infoData.createUserId, infoData.attributorUserId].includes(store.userInfo.userId) || hasPermission(['biz:project:changeExpire']))">标记失效</a-button>
                <a-button size="large" @click="handleComponent('valid')"
                    v-if="infoData.expire != 'YOU_XIAO' && ([infoData.createUserId, infoData.attributorUserId].includes(store.userInfo.userId) || hasPermission(['biz:project:changeValid']))">标记有效</a-button>
                <!-- <a-button size="large" v-if="infoData.expire!='YI_SHI_XIAO' && ([infoData.createUserId,infoData.attributorUserId].includes(store.userInfo.userId) || hasPermission(['biz:project:changeClose']))" @click="handleComponent('close')">终止项目</a-button> -->
                <a-button size="large"
                    v-if="(infoData.roleKeys.includes(1) && infoData.expire != 'YI_SHI_XIAO' && hasPermission(['biz:project:update']) && !['ZAI_GUAN', 'YI_ZHONG_ZHI'].includes(infoData.serviceStatus) || infoData.roleKeys.includes(1) && infoData.expire != 'YI_SHI_XIAO' && !['ZAI_GUAN', 'YI_ZHONG_ZHI'].includes(infoData.serviceStatus) && ([infoData.createUserId, infoData.attributorUserId].includes(store.userInfo.userId)))||isAdmin"
                    @click="router.push('/innerPage/projectEdit?id=' + projectId)">编辑基础信息</a-button>
                <a-button size="large"
                    v-if="infoData.roleKeys.includes(2) && infoData.expire != 'YI_SHI_XIAO' && hasPermission(['biz:project:changeAttributor'])"
                    @click="handleComponent('changeBelonger')">变更归属人</a-button>
            </template>
            <div class="header_info">
                <div class="header_description">
                    <a-descriptions size="small" :column="{ xxl: 4, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 }">
                        <a-descriptions-item label="项目编号">{{ infoData.projectNo || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="关键词">
                            {{ infoData.keywords }}
                        </a-descriptions-item>
                        <a-descriptions-item label="合作模式" v-if="infoData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">
                            {{ infoData.cooperationTypeStr || '' }}
                            <span v-if="infoData.cooperationTypeOther">,{{ infoData.cooperationTypeOther }}</span>
                        </a-descriptions-item>
                        <a-descriptions-item label="拓展模式" v-else>{{ infoData.expansionModeStr || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="归属单位">{{ infoData.companyName || '-' }}</a-descriptions-item>
                        <a-descriptions-item label="归属人">
                            <UserBox :data="infoData.attributorUser || {}" single descIn />
                        </a-descriptions-item>
                    </a-descriptions>
                </div>
                <div class="extra">
                    <div :style="{ display: 'flex', width: 'max-content', justifyContent: 'flex-end', textAlign: 'right' }">
                        <a-statistic title="是否有效" :value="infoData.expireStr" :style="{ marginRight: '32px', }" />
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
                    :class="{ 'step_item_success': stepAcitive > index, 'step_item_on': stepCurrent == index, 'step_item_ing': stepAcitive == index, 'step_item_disabled': item.disabled }"
                    @click="stepChange(index, item)">
                    <div class="percent_box">
                        <file-search-outlined v-if="item.code == 'jcxx'" :style="{ fontSize: '20px', color: '#fff' }" />
                        <span v-else>
                            {{ item.percent }}%
                        </span>
                    </div>
                    <div class="title">{{ (item.code == 'yjqr' && infoData.inStock == 'SHI') ? '续签确认' : item.name }}</div>
                </div>
            </template>
        </div>
        <component :is="components[componentCode]" :projectId="projectId" :menuList="menuData"
            :projectType="infoData.projectType"></component>
        <BaseHandle ref="baseHandleRef" @success="updateInfo" />
        <ChangeBelonger ref="changeBelongerRef" @success="updateInfo" />
        <StopDrawer ref="stopDrawerRef" @success="updateInfo" />
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
import { mainStore } from '@/store';
const store = mainStore();
const dict = useDictStore();
const router = useRouter();
const route = useRoute();
const projectId = ref(Number(route.query.id || 0))
const to = ref(route.query.to || route.query.code || 'xmxx');
const infoData = ref({ roleKeys: [] });


const isAdmin = computed(() => {
    const postId = import.meta.env.VITE_ADMINISTRATOR_POST_ID;
    if (parseInt(postId) === store.deptPost.postId) {
      return true;
    }
  });

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
        breadcrumbName: '项目库',
    },
    {
        breadcrumbName: '项目查看',
    },
]
const getInfo = (callBack) => {
    api.project.projectInfo(projectId.value).then(res => {
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
    api.project.treeStepByProject(projectId.value).then(res => {
        if (res.code == 200) {
            let arrs = [0, 0, 0];
            pageTree.value = res.data.map((item, k) => {
                item.percent = 0;
                let done = 0;
                item.children.forEach((subItem, i) => {
                    if (item.code == 'jcxx') {
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
                if (item.percent == 100 || item.code == 'jcxx') {
                    stepAcitive.value = k + 1;
                }
                //异步注册加载组件
                if (handle != 'refresh') {
                    components[item.code] = defineAsyncComponent(modules['./components/menuTree/SecondMenu.vue']);
                }
                if (infoData.value.serviceStatus == 'WEI_ZHONG_BIAO' && (item.code == 'yjqr' || item.code == 'thyj')) {
                    item.disabled = true;
                }
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
        let comp = await api.project.stepStatusUpdate(postData);
        callBack && callBack(comp);
    }
    getInfo();
    getPageTree('refresh');
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
const callbackProjectStepId = ref(0)
const components = {};
const componentCode = computed(() => {
    return (pageTree.value[stepCurrent.value] || {}).code;
});
const stepChange = (current, itme) => {
    callbackProjectStepId.value = itme.id
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
    if (component == 'close') {     //终止
        stopDrawerRef.value.open(infoData.value);
    }
    if (component == 'changeBelonger') {     //变更归属人
        changeBelongerRef.value.open(infoData.value);
    }
    if (component == "expire") {
        //标记失效
        baseHandleRef.value.open(infoData.value, "expire");
    }
    if (component == "valid") {
        // 标记有效
        Modal.confirm({
            title: '操作确认',
            content: '确认标记项目为有效?',
            onOk() {
                api.project.projectValid(infoData.value).then(res => {
                    if (res.code == 200) {
                        message.success('操作成功');
                        getInfo(() => {
                            getPageTree('refresh');
                        });
                    }
                })
            }
        });
    }
}
const updateInfo = (res) => {
    // if(res&&res.id){
    //     infoData.value = res;
    // }else{
    getInfo();
    // }
}
const callbackProjectStep = () => {
    Modal.confirm({
        title: '操作确认',
        content: '确认回退到【' + pageTree.value[stepCurrent.value].name + '】节点?',
        onOk() {
            let data = {
                projectId: projectId.value,
                stepMenuId: pageTree.value[stepCurrent.value].id
            }
            api.project.callbackProjectStep(data).then(res => {
                if (res.code == 200) {
                    message.success('操作成功');
                    getInfo(() => {
                        getPageTree('refresh');
                    });
                }
            })
        }
    });

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
        line-height: 56px;
        font-size: 18px;
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
}</style>
