<template>
    <div class="content-box">
        <Title title="我的待办" />
        <div class="filter-nav">
            <a-tabs v-model:activeKey="tabKey" @change="filterSubmit">
                <!-- <a-tab-pane key="1" tab="审批结果通知"></a-tab-pane> -->                
                <a-tab-pane key="2" tab="待办通知"></a-tab-pane>
                <a-tab-pane key="3" tab="项目通告"></a-tab-pane>
                <a-tab-pane key="4" tab="工作简报"></a-tab-pane>                
                <a-tab-pane v-if="hasPermission(['biz:notifyLog:list'])" key="5" tab="项目丢盘情况监管"></a-tab-pane>
            </a-tabs>
            <div v-if="tabKey != 1 && tabKey != 2"
                style="display: flex; justify-content: space-between; align-items: center;">
                <div style="display: flex; align-items: center;">
                    <p v-if="tabKey == 3" style="margin-top: 15px;">归属单位：</p>
                    <DeptSelects :maxTagCount="3" v-if="tabKey == 3" label="归属单位" style="width: 550px; margin-top: 15px;"
                        v-model="companyId" :noRoot="true" />
                    <a-button v-if="tabKey == 3" style="margin-top: 15px;margin-left: 30px;" type="primary"
                        @click="getList(companyId)">查询</a-button>
                </div>
                <a-button v-permission="['biz:notifyLog:export']"  style="margin-top: 15px;" type="primary" @click="exportData">导出</a-button>
            </div>
        </div>
        <FullTable :loading="loadding" :columns="(tabKey == 1 ? data.columns :
            tabKey == 3 ? data.columnsProject :
                tabKey == 4 ? data.columnsWorkBrief :
                    tabKey == 5 ? data.columnsEndProject :
                        data.columnsOther)" :dataSource="data.list">
            <template #bodyCell="{ column, text, record }">

                <template v-if="column.key === 'aptitle'">
                    <a class="flex_box color-link" @click="toOaLink(resoutParse(record.message).approvalUrl, record)">
                        <span class="dot" v-if="record.openStatus == 0"></span>
                        <EllipsisTooltip class="flex_full" :content="record.title" />
                    </a>
                </template>

                <template v-if="column.key === 'title'">
                    <div class="flex_box">
                        <span class="dot" v-if="record.openStatus == 0"></span>
                        <EllipsisTooltip class="flex_full" :content="record.title" />
                    </div>
                </template>
                <template v-if="column.key === 'message'">
                    <a class="flex_box color-link" @click="showInfo(record)">
                        <EllipsisTooltip class="flex_full" :content="record.message" />
                    </a>
                </template>

                <template v-if="column.key === 'approvalStatus'">
                    <div>
                        <p v-if="resoutParse(record.message).approvalStatus">
                            {{ resoutParse(record.message).approvalStatus }}
                        </p>
                        <p v-else>-</p>
                    </div>
                </template>
                <template v-if="column.key === 'submitUserName'">
                    <div>
                        <p v-if="resoutParse(record.message).submitUserName">
                            {{ resoutParse(record.message).submitUserName }}
                        </p>
                        <p v-else>-</p>
                    </div>
                </template>
                <template v-if="column.key === 'submitTime'">
                    <div>
                        <p v-if="resoutParse(record.message).submitTime">
                            {{ resoutParse(record.message).submitTime }}
                        </p>
                        <p v-else>-</p>
                    </div>
                </template>
                <template v-if="column.key === 'apaction'">
                    <a-button type="link" @click="toOaLink(resoutParse(record.message).approvalUrl, record)">详情</a-button>
                </template>
                <template v-if="column.key === 'action'">
                    <a-button type="link" @click="showInfo(record)">详情</a-button>
                </template>
                <template v-if="column.key === 'apactionProject'">
                    <a-button v-permission="['biz:notifyLog:revoke']" type="link" @click="revoke(record)">撤销</a-button>
                </template>

                <!-- 项目通告-项目名称 -->
                <!-- <template v-if="column.key === 'projectName'">
                    <router-link :to="'/innerPage/projectInfo?id=' + record.id" class="color-link">
                        {{ record.projectName }}
                    </router-link>
                </template> -->
                <!-- 工作简报-内容 -->
                <template v-if="column.key === 'workBriefTitle'">
                    <!-- <router-link :to="'/innerPage/workBriefEdit?id=' + record.id + '&isView=1'" class="color-link">
                        {{ record.title }}
                    </router-link> -->
                    <router-link :to="''" @click="handleClick(record.id)" class="color-link">
                        {{ record.title }}
                    </router-link>
                </template>
                <!-- 项目丢盘情况监管-项目名称 -->
                <template v-if="column.key === 'endProjectName'">
                    <router-link :to="'/innerPage/projectInfo?id=' + record.id" class="color-link">
                        {{ record.projectName }}
                    </router-link>
                </template>

                <EllipsisTooltip v-if="column.ellipsis" :content="text" />
            </template>
        </FullTable>
        <div class="pagination_box">
            <a-pagination showSizeChanger show-quick-jumper v-model:current="filterForm.pageNo"
                v-model:pageSize="filterForm.pageSize" :show-total="total => `共 ${total} 条数据`" size="small"
                @change="getPage" @showSizeChange="filterForm.pageNo = 1" :total="data.total" />
        </div>
        <a-drawer :width="520" :title="report.title" placement="right" layout="vertical" :visible="visible"
            @close="visible = false">
            <workReport :report="report" />
        </a-drawer>
    </div>
</template>
<script setup>
import api from '@/api/index';
import { mainStore } from '@/store';
import { dataToFile,TpWatermark,hasPermission } from "@/utils/tools";
import { message, Modal } from 'ant-design-vue';
const store = mainStore();
const router = useRouter();
const loadding = ref(false);
const tabKey = ref('2');
const visible = ref(false);
const report = ref({})
const companyId = ref([]);
const filterForm = reactive({
    pageNo: 1,
    pageSize: 10,
});

const modeName = {
    'Project': '项目',
    'Customer': '客户',
    'ProjectExtexsion': '项目干预',
    'ToutuoOperate': '项目评估',
}
const data = reactive({
    list: [],
    columns: [
        {
            title: '审批内容',
            key: 'aptitle',
            width: 250,
        },
        {
            title: '审批结果',
            key: 'approvalStatus',
            width: 100,
        },
        {
            title: '发起人',
            key: 'submitUserName',
            width: 180,
        },
        {
            title: '发起时间',
            key: 'submitTime',
            width: 180,
        },
        {
            title: '操作',
            key: 'apaction',
            width: 100,
            align: 'center',
            fixed: 'right'
        },
    ],
    columnsOther: [

        {
            title: '类型',
            key: 'title',
            width: 200,
        },
        {
            title: '内容',
            key: 'message',
            width: 250,
        },
        {
            title: '通知时间',
            dataIndex: 'createTime',
            width: 180,
        },
        {
            title: '操作',
            key: 'action',
            width: 100,
            align: 'center',
            fixed: 'right'
        },
    ],
    columnsProject: [
        {
            title: '归属单位',
            dataIndex: 'deptName',
            width: 250,
        },
        {
            title: '项目名称',
            dataIndex: 'projectName',
            key: 'projectName',
            width: 300,
        },
        {
            title: '通告类型',
            dataIndex: 'notifyTypeName',
            width: 180,
        },
        {
            title: '项目负责人',
            dataIndex: 'attributorUserName',
            width: 180,
        },
        {
            title: '逾期天数',
            dataIndex: 'overdueTime',
            width: 100,
        },
        {
            title: '发布时间',
            dataIndex: 'pushTime',
            width: 180,
        },
        {
            title: '立项时间',
            dataIndex: 'lxTime',
            width: 180,
        },
        {
            title: '操作',
            key: 'apactionProject',
            width: 100,
            align: 'center',
            fixed: 'right'
        },
    ],
    columnsWorkBrief: [
        {
            title: '汇报单位',
            dataIndex: 'deptName',
            width: 200,
        },
        {
            title: '工作简报标题',
            dataIndex: 'title',
            key: 'workBriefTitle',
            width: 200,
        },
        {
            title: '汇报人',
            dataIndex: 'createUserName',
            width: 180,
        },
        {
            title: '简报发送时间',
            dataIndex: 'pushTime',
            width: 200,
        },
    ],
    columnsEndProject: [
        {
            title: '归属单位',
            dataIndex: 'deptName',
            width: 250,
        },
        {
            title: '项目名称',
            dataIndex: 'projectName',
            key: 'endProjectName',
            width: 300,
        },
        {
            title: '项目状态',
            dataIndex: 'serviceStatusName',
            width: 100,
        },
        {
            title: '合同金额(元)',
            dataIndex: 'contractAmount',
            width: 150,
        },
        {
            title: '服务期限(月)',
            dataIndex: 'proposedServicePeriod',
            width: 150,
        },
        {
            title: '处理时间',
            dataIndex: 'updateTime',
            width: 200,
        },
        {
            title: '处理人',
            dataIndex: 'updateUserName',
            width: 180,
        },
    ],
    total: 0,
});

const getPage = () => {
    loadding.value = true;
    store.spinChange(1);
    let postData = {
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize
    }
    if (tabKey.value == 3) {
        postData = {
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize,
        inParams: {
                companyIds: companyId.value
            }
        }
 
        api.project.pageNotify(postData).then(res => {
            if (res.code == 200) {
                data.list = res.data.records;
                data.total = res.data.total;
                loadding.value = false;
                store.spinChange(-1);
            }
        })
    } else if (tabKey.value == 4) {
        api.workBrief.workBriefPageNotify(postData).then(res => {
            if (res.code == 200) {
                data.list = res.data.records;
                data.total = res.data.total;
                loadding.value = false;
                store.spinChange(-1);
            }
        })
    } else if (tabKey.value == 5) {
        api.project.pageEndNotify(postData).then(res => {
            if (res.code == 200) {
                data.list = res.data.records;
                data.total = res.data.total;
                loadding.value = false;
                store.spinChange(-1);
            }
        })
    } else {
        let postData = {
            desc: ['createTime'],
            pageNo: filterForm.pageNo,
            pageSize: filterForm.pageSize,
            params: {
                messageType: tabKey.value == 1 ? 'APPROVAL' : 'DEFAULT'
            }
        }
        api.sys.messagePage(postData).then(res => {
            if (res.code == 200) {
                data.list = res.data.records;
                data.total = res.data.total;
                loadding.value = false;
                store.spinChange(-1);
            }
        })
    }

}
const filterSubmit = () => {
    filterForm.pageNo = 1;
    data.list = [];
    data.total = 0;
    getPage();
}

onMounted(() => {
    getPage();
})
onActivated(() => {
    getPage();
})

const linkMode = {
    Project: (id) => `/innerPage/projectInfo?id=${id}`,
    Customer: (id) => `/innerPage/customerInfo?id=${id}`,
    ProjectExtexsion: (moduleId) => `/innerPage/extensionInfo?id=${moduleId}`,
    ToutuoOperate: (moduleId) => `/innerPage/extensionInfo?id=${moduleId}`,

}
const showInfo = (item) => {
    if (item.openStatus == 0) {
        toHandle(item.id, linkMode[item.module](item.moduleId))
    } else {
        router.push(linkMode[item.module](item.moduleId));
    }
}
const bus = inject('bus');
const toHandle = (id, path) => {
    api.sys.messageMark([id]).then(res => {
        if (res.code == 200) {
            bus.emit('msgCountChange');
            router.push(path);
        }
    })
}

const toOaLink = (link, item) => {
    api.common.getSsoToken({
        mobile: store.userInfo.phonenumber
    }).then(res => {
        if (res.code == 200 && res.data) {
            if (item.openStatus == 0) {
                api.sys.messageMark([item.id]).then(resr => {
                    if (resr.code == 200) {
                        bus.emit('msgCountChange');
                    }
                    window.open(link + '&access_token=' + res.data);
                })
            } else {
                window.open(link + '&access_token=' + res.data);
            }
        }
    })
}
const resoutParse = (str) => {
    try {
        return JSON.parse(str);
    } catch (e) {
        return {
            message: str || '-'
        }
    }
}

const handleClick = (id) => {
    api.workBrief.workBriefView(id).then(res => {
        if (res.code == 200) {
            report.value = res.data
            visible.value = true
        }
    })
}

const exportData = () => {
    let postData = {
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize
    }
    if (tabKey.value == 3) {
        let postData1 = {
            pageNo: filterForm.pageNo,
            pageSize: filterForm.pageSize,
            inParams: {
                companyIds: companyId.value
            }
        }
        store.spinChange(1);
        api.project.exportNotify(postData1).then(res => {
            store.spinChange(-1);
            let timestamp = new Date().getTime();
            dataToFile(res, "项目通告-" + timestamp + ".xlsx");
        });
    } else if (tabKey.value == 4) {
        store.spinChange(1);
        api.workBrief.workBriefNotifyExport(postData).then(res => {
            store.spinChange(-1);
            let timestamp = new Date().getTime();
            dataToFile(res, "工作简报-" + timestamp + ".xlsx");
        });
    } else if (tabKey.value == 5) {
        store.spinChange(1);
        api.project.exportEndNotify(postData).then(res => {
            store.spinChange(-1);
            let timestamp = new Date().getTime();
            dataToFile(res, "项目丢盘情况监管-" + timestamp + ".xlsx");
        });
    } else {
    }
}

const revoke = (item) => {
    Modal.confirm({
        title: '撤销',
        content: '请问是否确定撤销该项目',
        onOk() {
            store.spinChange(1);
            item.projectId = item.id;
            delete item.id;
            api.projectNofityLog.saveNotifyLog(item).then(res => {
                store.spinChange(-1);
                if (res.code == 200) {
                    message.success(res.msg);
                    getPage();
                }
            });
        }
    })
}

const getList = ($companyId) => {
    let postData = {
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize,
        inParams: {
            companyIds: companyId.value
        }
    }
    store.spinChange(1);
    api.project.pageNotify(postData).then(res => {
        store.spinChange(-1);
        if (res.code == 200) {
            data.list = res.data.records;
            data.total = res.data.total;
        }
    })
}

</script>
<style scoped lang="less">
.content-box {
    width: 0;

    .dot {
        width: 6px;
        height: 6px;
        background-color: @error-color;
        border-radius: 50%;
        margin-right: 8px;
    }
}
</style>
