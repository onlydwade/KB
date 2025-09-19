
<template>
    <a-space :size="16" v-if="oaReady">
        <div v-if="checkoachild == 1">
            <a-button v-if="oaNewest.approvalStatus == 5" size="large" :type="btnType" disabled>审批待确认</a-button>
            <a-button v-if="oaNewest.approvalStatus == 1" size="large" :type="btnType" disabled>审批中...</a-button>
            <a-button v-if="oaNewest.approvalStatus == 2 || approveStatus == 100" size="large" :type="btnType" disabled>审批通过</a-button>

            <template v-if="hasPermission(['biz:workbrief:createOa']) && myProject">
                <a-button :loading="loadding"
                    v-if="(!oaNewest.approvalStatus || oaNewest.approvalStatus == 0) && isView != 1" size="large"
                    :type="btnType" @click="startOa(0)">发起OA审批</a-button>
                <a-button :loading="loadding" v-if="oaNewest.approvalStatus == 10 && isView != 1" size="large"
                    :type="btnType" @click="startOa(0)">发起OA审批已删除，重新发起</a-button>
                <a-button :loading="loadding" v-if="oaNewest.approvalStatus == 3 && isView != 1" size="large"
                    :type="btnType" @click="startOa(-1)">发起OA审批已驳回，再次发起</a-button>
                <a-button :loading="loadding" v-if="oaNewest.approvalStatus == 4 && isView != 1" size="large"
                    :type="btnType" @click="startOa(0)">发起OA审批已废弃，重新发起</a-button>
            </template>
            <template v-else>
                <a-button size="large" :type="btnType" disabled
                    v-if="![1, 2, 5].includes(oaNewest.approvalStatus)">无“发起OA审批”权限</a-button>
            </template>
            <a-popconfirm title="" style="z-index:10" v-model:visible="createNewOa">
                <template #icon>
                    <div style="font-size:16px;padding-bottom:16px;">
                        确认OA审批发起人(可搜索切换)：
                    </div>
                    <UserSelect v-model="oaUserId" :users="oaUser" style="width:280px;" size="large" />
                </template>
                <template #okButton>
                    <a-button :loading="loadding" size="large" type="primary" @click="startOa(0)">确认提交</a-button>
                </template>
                <template #cancelButton>
                    <a-button @click="createNewOa = false" size="large">取消</a-button>
                </template>
                <a-button v-if="oaNewest.approvalStatus && isView != 1" size="large" :type="btnType"
                    v-permission="['biz:workbrief:createNewOa']">发起OA审批(新)</a-button>
            </a-popconfirm>
            <a-popconfirm title="" placement="topLeft" v-if="oaNewest.id">
                <template #icon>
                    <div class="" style="width:780px;margin:0 -16px">
                        <Title :title="'[数据简报] 最新审批信息'"></Title>
                        <div style="padding:16px;">
                            <a-descriptions :column="1">
                                <a-descriptions-item label="审批编号">{{ oaNewest.approvalNo || '-' }}</a-descriptions-item>
                                <a-descriptions-item label="审批状态">{{ status[oaNewest.approvalStatus]
                                }}</a-descriptions-item>
                                <!-- <a-descriptions-item label="审批结果">{{resoutParse(oaNewest.approvalResult)}}</a-descriptions-item> -->
                                <a-descriptions-item label="OA审批详情" v-if="oaNewest.approvalUrl">
                                    <a class="color-link" @click="toOaLink(oaNewest.approvalUrl)">
                                        {{ oaNewest.approvalUrl }}
                                    </a>
                                </a-descriptions-item>
                            </a-descriptions>
                        </div>
                        <Title :title="'[数据简报] 审批记录'"></Title>
                        <div class="" style="padding:16px;">
                            <a-table :columns="columns" :data-source="oaList" :pagination="false"
                                :scroll="{ x: '100%', y: 360 }" bordered>
                                <template #bodyCell="{ column, record }">
                                    <template v-if="column.key === 'approvalStatus'">
                                        {{ status[record.approvalStatus] }}
                                    </template>
                                    <!-- <template v-if="column.key === 'approvalResult'">
                                    {{resoutParse(record.approvalResult)}}
                                </template> -->
                                    <template v-if="column.key === 'action'">
                                        <a class="color-link" @click="toOaLink(record.approvalUrl)"
                                            v-if="record.approvalUrl">
                                            查看OA详情
                                        </a>
                                    </template>
                                </template>
                            </a-table>
                        </div>
                    </div>

                </template>
                <template #cancelButton>
                </template>
                <template #okButton>
                </template>
                <a-button size="large" v-if="oaNewest.id" class="arrow_left_btn">
                    <span class="arrow_before"></span>
                    <template #icon><more-outlined style="color:#f99c34" /></template>
                    审批详情
                </a-button>
            </a-popconfirm>

            <a-divider v-if="approveStatus != 100 && approveStatus != 2 && isView != 1" type="vertical"
                style="width: 2px; background-color: #f99c34" />
        </div>
        <a-button size="large" @click="clickback()">返回</a-button>
        <a-button size="large" class="preview" @click="previewA" v-permission="['biz:workbrief:preview']"
            :loading="submitLoading">预览</a-button>
        <a-button size="large" type="primary" @click="stagingA" v-permission="['biz:workbrief:save']"
            :loading="submitLoading"
            v-if="((oaNewest.approvalStatus && (oaNewest.approvalStatus == 0 || oaNewest.approvalStatus == 3 || oaNewest.approvalStatus == 4 || oaNewest.approvalStatus == 5 || oaNewest.approvalStatus == 10)) || !oaNewest.approvalStatus) && isView != 1">暂存</a-button>
        <a-button size="large" type="primary" @click="submitA" v-permission="['biz:workbrief:submit']"
            :loading="submitLoading"
            v-if="checkoachild != 1 && approveStatus != 100 && approveStatus != 2 && isView != 1">提交</a-button>
    </a-space>
</template>
<script setup>
import api from '@/api/index';
import { message } from 'ant-design-vue';
import { hasPermission } from '@/utils/tools';
import { mainStore } from '@/store';
const router = useRouter();
const store = mainStore();
const props = defineProps({
    disabled: {
        type: Boolean,
        default: false
    },
    menuInfo: {
        type: Object,
        default: {},
    },
    temp: {
        type: Object,
        default: {},
    }
})
const status = ref({
    0: '待发起审批',
    1: '审批中',
    2: '审批通过',
    3: '已驳回',
    4: '已废弃',
    5: '待确认',
    8: '线下审批通过',
    9: '无需审批',
})
const bus = inject('bus');
const emit = defineEmits(['submit', 'preview', 'staging']);
const projectId = 20;
const checkoachild = ref(1);

const attributorUserId = null;
const createUserId = null;

const previewA = (type) => {
    emit('preview');
}
const stagingA = (type) => {
    emit('staging');
}
const submitA = (type) => {
    emit('submit', 2);
}

const myProject = computed(() => {

    return true;
})
const btnType = computed(() => {
    return 'primary';
})
const oaList = ref([]);
const columns = ref([
    {
        title: '审批编号',
        dataIndex: 'approvalNo',
        width: 320,
        ellipsis: true,
    },
    {
        title: '审批状态',
        key: 'approvalStatus',
        width: 120,
    },
    {
        title: '发起时间',
        dataIndex: 'createTime',
        width: 180,
    },
    {
        title: '发起人',
        dataIndex: ['submitUser', 'realname'],
        width: 180,
    },
    {
        title: '操作',
        key: 'action',
        width: 120,
        fixed: 'right'
    },
])
const oaVisible = ref(false);
const oaReady = ref(false);
const approveStatus = ref(0);
const isView = ref(0);
const oaNewest = computed(() => {
    return oaList.value.length > 0 ? oaList.value[0] : {};
});
const projectType = null;
const getList = (type) => {
    let postData = {
        desc: ['createTime'],
        pageNo: 1,
        pageSize: 500,
        params: {
            recordId: props.menuInfo.id,
            templateId: props.menuInfo.templateId
        }
    }

    if (props.menuInfo.id == null || props.menuInfo.id == 0) {
        oaReady.value = true;
        return;
    }

    api.common.oaPage(postData).then(res => {
        if (res.code == 200) {
            oaList.value = res.data.records || []
            oaReady.value = true;
            if (type != 'first' && oaList.value.length > 0 && oaList.value[0].approvalUrl) {
                toOaLink(oaList.value[0].approvalUrl);
            }
            setTimeout(() => {
                loadding.value = false;
            }, 1000)
            createNewOa.value = false;
        }
    })
}

const clickback = () => {
    router.push("/workbrief");
}

onMounted(() => {

    //alert(props.menuInfo.checkoa);

    checkoachild.value = props.menuInfo.checkoa;
    approveStatus.value = props.menuInfo.approveStatus;
    //console.log(props.menuInfo.isView);
    isView.value = props.menuInfo.isView;
    getList('first');
    bus.on('workgetlist', getList);
    //isInTeam();
    oaUserId.value = store.userInfo.userId;
    oaUser.value = {
        userId: store.userInfo.userId,
        realname: store.userInfo.realname
    }
})
onUnmounted(() => {
    bus.off('workgetlist', getList);
})

const createNewOa = ref(false);
const oaUserId = ref(0);
const oaUser = ref({});
const inTeam = ref(false);
const isInTeam = () => {
    api.project.inTeam(projectId.value).then(res => {
        if (res.code == 200) {
            inTeam.value = res.data || false;
        }
    })
}

const loadding = ref(false);
const startOa = (type) => {
    if (loadding.value) {
        return;
    }
    //loadding.value = true;
    //emit('submit');
    emit('submit', type, { ...props.temp, oaId: oaNewest.value.id, userId: oaUserId.value || store.userInfo.userId });
    //getList();    
}

defineExpose({ getList })


const toOaLink = (link) => {
    api.common.getSsoToken({
        mobile: store.userInfo.phonenumber
    }).then(res => {
        if (res.code == 200 && res.data) {
            window.open(link + '&access_token=' + res.data);
        }
    })
}
const resoutParse = (str) => {
    try {
        return JSON.parse(str).docStatusName || '-'
    } catch (e) {
        return str || '-'
    }
}
</script>
<style scoped lang="less">
:deep(.arrow_left_btn) {
    position: relative;
    margin-left: -4px;
    margin-right: -4px;

    &::after,
    .arrow_before {
        border: solid transparent;
        content: ' ';
        height: 0;
        top: 0;
        bottom: 0;
        margin: auto;
        right: 100%;
        position: absolute;
        width: 0px;
    }

    &::after {
        border-width: 6px;
        border-right-color: #fff !important;
        z-index: 50;
    }

    .arrow_before {
        border-width: 7px;
        border-right-color: #ccc;
        opacity: 0.8;
        z-index: 49;
    }

    &:hover,
    &:focus {
        .arrow_before {
            border-width: 8px;
            border-right-color: @primary-color;
        }
    }
}
</style>
