<template>
    <div class="page-customer content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a href="#/index">首页</a></a-breadcrumb-item>
                <a-breadcrumb-item>工作简报</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <PageFilter v-model="filterForm" @submit="filterSubmit" @dataExport="dataExport" />
        <div class="content-box_full">
            <Title title="工作简报">
                <template #right>
                    <a-button type="primary" v-permission="['biz:workbrief:add']" @click="router.push('/innerPage/workBriefEdit')">
                        <template #icon>
                            <plus-outlined />
                        </template>
                        新建
                    </a-button>

                    <a-button style="margin-left: 10px;" v-permission="['biz:workbrief:checkConfig']" type="primary" @click="getcheckOa()">
                        <template #icon>
                            <EditOutlined />
                        </template>
                        审批设置
                    </a-button>
                    <!-- <a-button style="margin-left:10px;" type="primary" @click="">
                        <template #icon>
                            <EditOutlined />
                        </template>
                        群组设置
                    </a-button> -->
                    <div>
                        <GroupConfig />
                    </div>
                    
                </template>
            </Title>

            <a-modal v-model:visible="visible" :width="660" :height="1000" destroyOnClose>
                <template #title>
                    <span>流程配置</span>
                </template>
                <a-switch v-model:checked="checkoa" :checkedValue="1" checked-children="开启" :unCheckedValue="0"
                    un-checked-children="关闭" />
                <template #footer>
                    <a-button @click="visible = false">
                        关闭
                    </a-button>
                    <a-button type="primary" @click="setcheckOa()">
                        保存
                    </a-button>
                </template>
            </a-modal>

            <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">
                <template #bodyCell="{ column, text, record, index }">
                    <template v-if="column.key === 'code'">
                        <router-link :to="'/innerPage/workBriefEdit?id=' + record.id + '&isView=1'" class="color-link">
                            {{ record.code }}
                        </router-link>
                    </template>
                    <template v-if="column.key === 'action'">
                        <actionBtn :actions="actions(record, index)" />
                    </template>
                    <template v-if="column.key === 'pushUserName'">
                        <a-popover placement="right">
                            <template #content>
                                <p class="pushUserNamelist" v-for="el in record.pushUserList" :key="el.id">
                                    {{ el.userName }}
                                    <check-circle-filled :style="{ color: el.pushStatus === 2 ? '#53FF53' : '#8E8E8E' }" />
                                </p>
                            </template>
                            <div>{{ record.pushUserName }}</div>
                        </a-popover>
                    </template>
                    <EllipsisTooltip v-if="column.ellipsis" :content="text" />
                </template>
            </FullTable>
            <div class="pagination_box">
                <a-pagination showSizeChanger show-quick-jumper v-model:current="filterForm.pageNo"
                    v-model:pageSize="filterForm.pageSize" :show-total="total => `共 ${total} 条数据`" size="small"
                    @change="getPage" @showSizeChange="filterForm.pageNo = 1" :total="data.total" />
            </div>
        </div>
    </div>
</template>
<script setup>
import api from '@/api/index';
import { message, Modal } from 'ant-design-vue';
import PageFilter from './components/PageFilter.vue';
import { dataToFile, hasPermission } from '@/utils/tools';
import { mainStore } from '@/store';
import GroupConfig from './components/groupConfig.vue';

const store = mainStore();
const router = useRouter();
const loadding = ref(false);
const filterForm = reactive({
    pageNo: 1,
    pageSize: 10,
})
const data = reactive({
    list: [],
    columns: [
        {
            title: '简报编码',
            key: 'code',
            width: 170,
        },
        {
            title: '简报标题',
            dataIndex: 'title',
            width: 200,
            ellipsis: true,
        },
        {
            title: '推送对象',
            // dataIndex : 'pushUserName',
            width: 170,
            key: 'pushUserName',
            // ellipsis  : true,
        },
        {
            title: '所属部门',
            dataIndex: 'deptName',
            width: 170,
        },
        {
            title: '审批状态',
            dataIndex: 'approveStatusName',
            width: 120,
        },
        {
            title: '是否推送',
            dataIndex: 'isPush',
            width: 100,
        },
        {
            title: '推送时间',
            dataIndex: 'pushTime',
            width: 170,
        },
        {
            title: '创建人',
            dataIndex: 'createUserName',
            width: 120,
        },
        {
            title: '创建时间',
            dataIndex: 'createTime',
            width: 180,
        },
        {
            title: '操作',
            key: 'action',
            width: 240,
            fixed: 'right'
        },
    ],
    total: 0,
})
const visible = ref(false);
const checkoa = ref(1);
const getPage = () => {
    let postData = builderFilter();
    loadding.value = true;
    api.workBrief.workBriefPage(postData).then(res => {
        if (res.code == 200) {
            data.list = res.data.records;
            data.total = res.data.total;
            //如果后端返回的页数小于点击的页面则设置默认从第一页重新开始请求
            if (res.data.pages != 0 && res.data.pages < filterForm.pageNo) {
                filterSubmit();
            }
        }
        loadding.value = false;
    })
}
const filterSubmit = () => {
    filterForm.pageNo = 1;
    getPage();
}

const dataExport = () => {
    let postData = builderFilter();
    store.spinChange(1);
    api.customer.customerExport(postData).then(res => {
        store.spinChange(-1);
        let timestamp = (new Date).getTime();
        dataToFile(res, '客户-' + timestamp + '.xlsx');
    })
}
const builderFilter = () => {
    let postData = {
        desc: ['createTime'],
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize,
        params: {},
        geParams: {},
        leParams: {},
        inParams: {},
        likeParams: {}
    }
    let likeParams = ['title'];
    likeParams.forEach(key => {
        if (filterForm[key]) {
            postData.likeParams[key] = filterForm[key];
        }
    });
    return postData;
}

const actions = (record, index) => {
    return [
        {
            text: '编辑',
            show: (record.approveStatus === 0 || record.approveStatus === 3 || record.approveStatus === 5|| record.approveStatus === 4|| record.approveStatus === 10 || record.approveStatus == null) && hasPermission(["biz:workbrief:update"]),
            click: () => {
                router.push('/innerPage/workBriefEdit?id=' + record.id);
            }
        },
        {
            text: '删除',
            show: (record.approveStatus === 0 || record.approveStatus === 3 || record.approveStatus === 5|| record.approveStatus === 4|| record.approveStatus === 10 || record.approveStatus == null) && hasPermission(["biz:workbrief:delete"]),
            click: () => {
                handleComponent('delete', record, index);
            }
        },
    ];
}
const baseHandleRef = ref(null);
const handleComponent = (component, row, index) => {
    if (component == 'delete') {     //删除
        Modal.confirm({
            title: '操作确认',
            content: '确认删除此工作简报?',
            onOk() {
                api.workBrief.workBriefDel(row.id).then(res => {
                    if (res.code == 200) {
                        message.success('操作成功');
                        getPage();
                    }
                })
            }
        });
    }
}

const updateRow = (res, index, type) => {
    if (type == 'delete') {
        getPage();
    } else {
        if (res && res.id) {
            data.list[index] = res;
        } else {
            getPage();
        }
    }
}

const getcheckOa = () => {
    api.workBrief.getcheckOa().then(res => {
        if (res.code == 200) {
            visible.value = true;
            checkoa.value = res.data;
        }
    })
}

const setcheckOa = () => {
    api.workBrief.setcheckOa(checkoa.value).then(res => {
        if (res.code == 200) {
            message.success('操作成功');
            visible.value = false;
        }
    })
}

onMounted(() => {
    getPage();
})
onActivated(() => {
    getPage();
})
</script>
<style scoped lang="less">
.pushUserNamelist {
    padding-top: 3px;
    padding-bottom: 3px;
}
</style>