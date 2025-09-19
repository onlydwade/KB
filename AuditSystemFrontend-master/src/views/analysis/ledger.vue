<template>
    <div class="page-project content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a href="#/index">首页</a></a-breadcrumb-item>
                <a-breadcrumb-item>投标费用台账</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <LedgerPageFilter v-model="filterForm" @submit="filterSubmit" @dataExport="dataExport" />
        <div class="content-box_full">
            <Title title="投标费用台账">
            </Title>
            <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">
                <template #bodyCell="{ column, text, record, index }">
                    <template v-if="column.key === 'projectNo'">
                        <router-link :to="'/innerPage/projectInfo?id=' + record.projectId + '&to=xmxx'" class="color-link">
                            {{ record.projectNo }}
                        </router-link>
                    </template>
                    <template v-if="column.key === 'freeType'">
                        <span v-if="record.freeType == 'TOU_BIAO_FEI_YONG_QI_TA'">{{ record.freeTypeOther }}</span>
                        <span v-else>{{ record.freeTypeStr }}</span>
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
import api from "@/api/index";
import LedgerPageFilter from "./components/LedgerPageFilter.vue";
import { parseFormatNum, dataToFile, dateFormat } from "@/utils/tools";
import { mainStore } from "@/store";

const store = mainStore();
const router = useRouter();

const loadding = ref(false);
const filterForm = reactive({
    pageNo: 1,
    pageSize: 10,
});
const data = reactive({
    list: [],
    columns: [
        {
            title: "大区",
            dataIndex: "regionName",
            width: 100,
            ellipsis: true,
            fixed: 'left',
        },
        {
            title: "归属单位",
            dataIndex: "companyName",
            width: 200,
            ellipsis: true,
            fixed: 'left',
        },
        {
            title: "项目编号",
            key: "projectNo",
            width: 150,
            fixed: 'left',
        },
        {
            title: "项目名称",
            dataIndex: "projectName",
            width: 200,
            ellipsis: true,
            fixed: 'left',
        },
        {
            title: "费用名称",
            key: "freeType",
            width: 120,
            fixed: 'left',
        },
        {
            title: "费用金额（元）",
            dataIndex: "freeAmount",
            align: 'right',
            width: 150,
            customRender: (text) => {
                return parseFormatNum(text.text, 2);
            }
        },
        {
            title: '支付时间',
            dataIndex: 'paymentTime',
            required: true,
            width: 180,
            customRender: (text) => {
                return dateFormat(text.text, 'YYYY-MM-DD');
            }
        },
        {
            title: "实际缴纳单位",
            dataIndex: "paymentCompany",
            width: 200,
            ellipsis: true,
        },
        {
            title: "收款单位",
            dataIndex: "payeeCompany",
            width: 200,
            ellipsis: true,
        },
        {
            title: "欠款单位",
            dataIndex: "arrearageCompany",
            width: 200,
            ellipsis: true,
        },

        {
            title: "欠款状态",
            dataIndex: "recoverStatusStr",
            width: 100,
        },
        {
            title: "回款时间",
            dataIndex: "recoverTime",
            width: 180,
            customRender: (text) => {
                return dateFormat(text.text, 'YYYY-MM-DD');
            }
        },
        {
            title: "回款状态",
            dataIndex: "deductStr",
            width: 100,
        },
        {
            title: "回款金额（元）",
            dataIndex: "deductAmount",
            align: 'right',
            customRender: (text) => {
                return parseFormatNum(text.text, 2);
            },
            width: 150,
        },
        {
            title: "备注",
            dataIndex: "remark",
            width: 200,
        },
        {
            title: "登记人",
            dataIndex: "createUserName",
            width: 150,
        },
        {
            title: "登记时间",
            dataIndex: "createTime",
            width: 180,
        },
    ],
    total: 0,
});
const getPage = () => {
    let postData = builderFilter();
    loadding.value = true;
    api.analysis.projectBidFeeRegistrationPage(postData).then(res => {
        if (res.code == 200) {
            data.list = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    });
};
const filterSubmit = () => {
    filterForm.pageNo = 1;
    getPage();
};
const dataExport = () => {
    let postData = builderFilter();
    store.spinChange(1);
    api.analysis.projectBidFeeRegistrationExport(postData).then(res => {
        store.spinChange(-1);
        let timestamp = new Date().getTime();
        dataToFile(res, "项目投标费用台帐-" + timestamp + ".xlsx");
    });
};
const builderFilter = () => {
    let postData = {
        desc: ["createTime"],
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize,
        params: {},
        geParams: {},
        leParams: {},
        inParams: {},
        likeParams: {},
    };
    if (filterForm.moreFilter) {
        let params = [
            "regionId",
            "companyId",
            "freeType",
            "recoverStatus",
            "deduct",
        ];
        let likeParams = ["projectName", "projectNo", "paymentCompany", "arrearageCompany", "payeeCompany"];

        params.forEach(key => {
            if (filterForm[key] && filterForm[key] != -1) {
                postData.params[key] = filterForm[key];
            }
        });
        likeParams.forEach(key => {
            if (filterForm[key]) {
                postData.likeParams[key] = filterForm[key];
            }
        });
    }

    if (!filterForm.moreFilter && filterForm.searchKey) {
        postData.content = filterForm.searchKey;
        postData.contentColumns = ["projectName", "projectNo", "remark"];
    }
    if (filterForm.year) {
        postData.geParams.createTime = filterForm.year + "-01-01 00:00:00";
        postData.leParams.createTime = filterForm.year + "-12-31 23:59:59";
    }
    return postData;
};
onMounted(() => {
    getPage();
});
onActivated(() => {
    getPage();
});
</script>
<style scoped lang="less"></style>
