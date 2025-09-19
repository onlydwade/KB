
<template>
    <a-form ref="formRef" layout="vertical" :model="editData">
        <!-- <a-table :columns="columns" :data-source="list" :pagination="false" :scroll="{ x: '100%' }">
            <template #emptyText>
                暂无数据，请添加
            </template>
            <template #bodyCell="{ column, text, record,index}">
            <template v-if="column.key === 'action'">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary"  size="small" @click="clone(record,index)">查看</a-button>
                        <a-button type="text" class="color-primary"  size="small" @click="edit(record,index)">编辑</a-button>
                        <a-button type="text" class="color-primary"  size="small" @click="del(record,index)">删除</a-button>

                    </template>
                </template>
                </template>

        </a-table> -->
        <div class="content-box_full">
            <Title title="评估记录"></Title>
            <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">
                <template #bodyCell="{ column, text, record, index }">
                    <template v-if="column.key === 'companyCode'">
                        <router-link :to="'/innerPage/subsidiaryInfo?id=' + record.id" class="color-link">
                            {{ record.companyCode }}
                        </router-link>
                    </template>
                    <template v-if="column.key === 'companyName'">
                        <router-link :to="'/innerPage/subsidiaryInfo?id=' + record.id" class="color-link flex_box">
                            <EllipsisTooltip class="flex_full" :content="record.companyName" />
                        </router-link>
                    </template>
                    <template v-if="column.key === 'projectName'">
                        <router-link :to="'/innerPage/projectInfo?id=' + record.projectId" class="color-link flex_box">
                            <EllipsisTooltip class="flex_full" :content="record.projectName" />
                        </router-link>
                    </template>
                    <template v-if="column.key === 'action'">
                        <a-button type="text" class="color-primary" size="small" @click="view(record, index)">查看</a-button>
                        <a-button type="text" class="color-primary" size="small" @click="edit(record, index)">编辑</a-button>
                        <a-button type="text" class="color-primary" size="small" @click="del(record, index)">删除</a-button>
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
    </a-form>
    <EstimateDrawer ref="estimateDrawerRef" @success="updateRow" />
</template>
<script setup>
import api from '@/api/index';
import { useCorrelation } from './evaluation';
import { useDictStore } from '@/store/dict';
import { watchArray } from '@vueuse/core'
import EstimateDrawer from '../../components/EstimateDrawer.vue'
import { message, Modal } from 'ant-design-vue';
// const store  = mainStore();
const route = useRoute();

const loadding = ref(false);
const estimateDrawerRef = ref(null);
const filterForm = reactive({
    pageNo: 1,
    pageSize: 10,
})
const data = reactive({
    list: [],
    columns: [
        {
            title: '评估日期',
            // dataIndex : ['user','realname'],
            dataIndex: 'assessTime',
            required: true,
            editType: 'assessTime'
        },
        {
            title: '经营评估期限',
            dataIndex: 'assessDeadline',
            editType: 'assessDeadline'
        },
        {
            title: '评估人',
            dataIndex: 'assessUserName',
            editType: 'assessUserName'
        },
        {
            title: '评估标题',
            dataIndex: 'assessTitle',
            editType: 'assessTitle'
        },
        {
            title: '评估年份',
            dataIndex: 'assessYear',
            editType: 'assessYear'
        },
        {
            title: '评估阶段',
            dataIndex: 'assessStage',
            editType: 'assessStage'
        },
        {
            title: '状态',
            dataIndex: 'assessState',
            editType: 'assessState'
        },
        {
            title: '是否需下达干预方案',
            dataIndex: 'transmitState',
            editType: 'transmitState'
        },
        {
            title: '方案下达期限',
            dataIndex: 'transmitDeadline',
            editType: 'transmitDeadline'
        },
        {
            title: '是否已下达干预方案',
            dataIndex: 'transmitAlreadyState',
            editType: 'transmitAlreadyState'
        },
        {
            title: '操作',
            key: 'action',
            width: 200,
            fixed: 'right'
        },

    ],
    total: 0,
})
const getPage = () => {
    let postData = {
        desc: ['createTime'],
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize,
        id: route.query.id,
        params: {}
    }
    loadding.value = true;
    api.project.projectAssessPage(postData).then(res => {
        if (res.code == 200) {
            data.list = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
const edit = (row, index) => {
    estimateDrawerRef.value.open(row, index);
}
const view = (row, index) => {
    estimateDrawerRef.value.open(row, index);
}
const del = (row, index) => {
    if (row.id) {
        Modal.confirm({
            title: '操作确认',
            content: '是否确认删除该条数据？',
            onOk() {
                api.project.correlationDel(row.id, key).then(res => {
                    if (res.code == 200) {
                        list.value.splice(index, 1);
                    }
                })
            }
        });
    }
}
onMounted(() => {
    getPage();
})
</script>
<style scoped lang="less">
.add_btn {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    cursor: pointer;
    margin-top: 8px;
    height: 48px;
    // box-shadow      : 0 0 8px rgb(0 21 41 / 8%);
    border: 1px solid #eee;
    border-radius: 4px;

    &:hover {
        color: @primary-color;
        background-color: #fffaf0;
    }
}
</style>
