
<template>
    <Title title="拜访情况"></Title>
    <div class="follow_content">
        <a-form ref="formRef" layout="vertical" v-if="!readOnly" :model="formData">
            <div class="mention_box">
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="拜访日期" name="visitTime">
                            <a-date-picker :disabled="disabled" :getPopupContainer="(trigger) => trigger.parentNode"
                                v-model:value="formData.visitTime" class="w_full" valueFormat="YYYY-MM-DD"
                                format="YYYY-MM-DD" placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="拜访人员" name="visitUserName">
                            <a-input allowClear v-model:value="formData.visitUserName" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="拜访方式" name="visitType">
                            <a-select v-model:value="formData.visitType" class="w_full" placeholder="请选择"
                                :options="dict.options('BAI_FANG_FANG_SHI')" allowClear>
                            </a-select>
                        </a-form-item>
                    </a-col>
                </a-row>
                <FollowDetail v-model:value="detailList" :recordId="props.recordId"></FollowDetail>
            </div>
            <div style="float:right"><a-button type="primary" size="large" shape="round" @click="followSubmit">发布</a-button>
            </div>
        </a-form>
        <a-steps progress-dot direction="vertical" :current="-1">
            <a-step v-for="(item, index) in data.list" :key="index">
                <!-- <template #title>
                    <div class="step_date">

                    </div>
                </template> -->
                <template #description>
                    <div class="step_desc">
                        <div class="step_bar">
                            <a-space class="params">
                                {{ dateFormat(item.visitTime, 'YYYY-MM-DD') }}
                                <a-divider type="vertical" />
                                {{ item.visitUserName }}
                                <!-- {{dateFormat(item.visitTime,' HH:mm:ss')}} -->
                                <a-divider type="vertical" />
                                <!-- {{(item.createUser || {}).realname}} -->
                                {{ item.visitTypeStr }}
                            </a-space>
                            <a-button v-if="!readOnly" type="text" class="color-primary" size="small"
                                @click="edit(item, index)">编辑</a-button>
                            <a-button v-if="!readOnly" type="text" class="color-primary" size="small"
                                @click="del(item, index)">删除</a-button>
                        </div>
                        <p class="follow_text" v-for="(detail, fileIndex) in item.customerFollowLogDetailList" readOnly
                            :key="fileIndex" :fileData="detail">
                            <span class="color-primary">{{ fileIndex + 1 }}</span>
                            {{ detail.workSummary }}
                            <a-divider type="vertical" />
                            <span class="color-success"
                                v-if="detail.taskStatus == 'CHI_XUN_GEN_JIN'">{{ detail.taskStatusStr }}</span>
                            <span class="color-primary"
                                v-else-if="detail.taskStatus == 'TING_ZHI'">{{ detail.taskStatusStr }}</span>
                            <span class="color-danger" v-else>{{ detail.taskStatusStr }}</span>
                            <br />
                            {{ detail.followStatus }}
                            <br />
                            负责人: {{ detail.head }}
                            <a-divider type="vertical" />
                            专班: {{ detail.teamEstablish }}
                        </p>

                        <!-- <div>
                            <FollowDetailView v-for="(detail,fileIndex) in item.customerFollowLogDetailList  " readOnly :key="fileIndex" :fileData="detail"/>
                        </div>   -->
                    </div>

                </template>
            </a-step>
        </a-steps>
        <a-empty v-if="data.list.length == 0" description="暂无跟进记录" />
    </div>
    <div class="pagination_box">
        <a-pagination showSizeChanger show-quick-jumper v-model:current="data.pageNo" v-model:pageSize="data.pageSize"
            :show-total="total => `共 ${total} 条数据`" size="small" @change="getList" @showSizeChange="data.pageNo = 1"
            :total="data.total" />
    </div>
    <!--
    <FollowEdit ref="editRef" @success="getList" :menuId="menuId"/> -->
</template>
<script setup>
import api from '@/api/index';
import { message, Modal } from 'ant-design-vue';
import { mainStore } from "@/store";
import { useDictStore } from "@/store/dict";
import FollowDetail from './FollowDetail.vue'
import { watchArray } from '@vueuse/core'
const store = mainStore();
const dict = useDictStore();
const props = defineProps({
    recordId: {
        type: Number,
        default: 0,
    },
    moduleName: {
        type: String,
        default: 'Project',
    },
    readOnly: {
        type: Boolean,
        default: false,
    },

    menuId: {
        type: Number,
        default: 0,
    }
})

const loadding = ref(false);
const detailList = ref([])

const data = reactive({
    pageNo: 1,
    pageSize: 20,
    total: 0,
    customerFollowLogDetailList: [],
    list: []
})
const getList = async () => {
    let postData = {
        desc: ['updateTime'],
        pageNo: data.pageNo,
        pageSize: data.pageSize,
        params: {}
    }
    loadding.value = true;
    let res = await api.common.followList(props.moduleName, props.recordId, postData);
    if (res.code == 200) {
        data.list = res.data.records;
        data.total = res.data.total;
    }
    loadding.value = false;
    // detailList.value = []
}

const formData = reactive({
    id: null,
    followContent: '',
    visitTime: '',
    visitType: '',
    visitUserName: '',
    followDocument: [],
    customerFollowLogDetailList: [],
});
const formRef = ref(null);
const followSubmit = () => {
    if (detailList.value.length == 0) {
        message.warning('请填写工作进展！');
        return;
    }

    formRef.value.validateFields().then(async (vRes) => {
        let postData = {
            id: formData.id,
            recordId: props.recordId,
            modelName: props.moduleName,
            followType: formData.followType,
            followContent: formData.followContent,
            visitTime: formData.visitTime,
            visitType: formData.visitType,
            visitUserName: formData.visitUserName,
            customerFollowLogDetailList: detailList.value,
        }

        let res = await api.common.followAddCustomerLog(postData);
        if (res && res.code == 200) {
            getList();
            message.success('操作成功');
            formData.id = null;
            formData.followType = null;
            formData.followContent = '';
            formData.visitTime = '';
            formData.visitType = '';
            formData.visitUserName = '';
            formData.followDocument = [];
            formData.customerFollowLogDetailList = [];
            detailList.value = []
        }
    })
}

const editRef = ref(null);
const edit = (item, index) => {
    // formData.value=item
    formData.customerFollowLogDetailList = item.customerFollowLogDetailList
    formData.visitTime = item.visitTime;
    formData.visitType = item.visitType;
    formData.visitUserName = item.visitUserName;
    formData.id = item.id;
    nextTick(() => {
        detailList.value = item.customerFollowLogDetailList
    })

    console.log(detailList.value);
    console.log("detailList");
    //editRef.value.open(item);
}

const del = (item, index) => {
    debugger
    Modal.confirm({
        title: '操作确认',
        content: '确认删除此客户追踪记录?',
        onOk() {
            api.common.deleteCustomerLogByIds(props.moduleName, props.recordId, item.id).then(res => {
                if (res.code == 200) {
                    message.success('操作成功');
                    getList();
                }
            })
        }
    });
}

const uploadPath = GLOBAL_PATH.api + '/system/upload/follow';
const token = localStorage.getItem('token');
const uploading = ref(false);
const handleChange = (info, index) => {
    const status = info.file.status;
    const response = info.file.response;
    if (status === 'uploading') {
        uploading.value = true;
    }
    if (status === 'done') {
        uploading.value = false;
        if (response && response.success) {
            response.data.time = Date.now();
            let ext = response.data.name.substring(response.data.name.lastIndexOf(".") + 1);
            response.data.ext = ext;
            if (props.menuId) {
                response.data.stepMenuId = props.menuId;
                response.data.documentTemplateId = 1038;
            }
            formData.followDocument.push(response.data);
        } else {
            message.error(`上传失败.`);
        }
    } else if (status === 'error') {
        uploading.value = false;
        message.error(`${info.file.name} 上传失败.`);
    }
    if (response && response.code && response.code == 500) {
        message.error(response.msg);
    }
}
const fileDel = (index, parent) => {
    if (parent) {
        parent.splice(index, 1);
    } else {
        formData.followDocument.splice(index, 1);
    }
}
onMounted(() => {
    getdepTInfo();
    getList();
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

</script>
<style scoped lang="less">
.follow_content {
    padding: 16px;

    .step_date {
        background-color: #aaa;
        color: #fff;
        padding: 4px 8px;
        line-height: 20px;
        border-radius: 14px;
        font-size: 14px;
    }

    .step_desc {
        background-color: #f0f2f5;
        border-radius: 4px;
        padding: 16px;
        margin-top: 8px;
        // max-width        : 680px;
        margin-bottom: 16px;

        .step_bar {
            display: flex;
        }

        .params {
            color: @text-color-secondary;
            flex: 1;
        }

        .follow_text {
            font-size: 16px;
            color: @text-color;
        }

    }

    :deep(.ant-steps-item-container) {
        cursor: auto !important;
    }

    .mention_box {
        margin-bottom: 24px;
        margin-left: -16px;
        margin-right: -16px;
        padding: 0 16px 24px 16px;
        box-shadow: 0 4px 4px rgb(0 21 41 / 4%);

        .control_box {
            margin-top: 8px;
        }
    }

    .file_box {
        max-width: 800px;
        margin-top: -16px;
    }

    .control_box {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
}</style>
