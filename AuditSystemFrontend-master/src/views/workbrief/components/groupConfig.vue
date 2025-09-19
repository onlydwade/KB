
<template>
    <a-button style="margin-left:10px;" @click="open" v-permission="['biz:workbrief:groupConfig']">
        群组设置
    </a-button>
    <a-modal v-model:visible="visible" title="群组配置" :width="700" destroyOnClose :body-style="bodystyle">
        <div style="width: 100%;height: 50px;">
            <a-form :label-col="{ style: { width: '10px' } }" ref="formRef" :model="filterForm">
                <a-row :class="'show_more_col'">
                    <a-col :span="12" class="text-right">
                        <a-form-item name="title">
                            <a-space>
                                <a-input style="width:300px;" allowClear v-model:value="filterForm.title"
                                    placeholder="可输入群组名称" />
                                <a-button type="primary" @click="filterSubmit">查询</a-button>
                                <a-button @click="reset">重置</a-button>
                                <a-button @click="addGroup">添加</a-button>
                            </a-space>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <div style="width: 100%;">
            <FullTable style="height: 500px;" :loadding="loadding" :columns="data.columns" :dataSource="data.list">
                <template #bodyCell="{ column, text, record, index }">
                    <template v-if="column.key === 'action'">
                        <actionBtn :actions="actions(record, index)" />
                    </template>
                </template>
            </FullTable>
        </div>
        <div style="width: 100%;height: 50px;" class="pagination_box">
            <a-pagination showSizeChanger show-quick-jumper v-model:current="filterForm.pageNo"
                v-model:pageSize="filterForm.pageSize" :show-total="(total) => `共 ${total} 条数据`" size="small"
                @change="getPage" @showSizeChange="filterForm.pageNo = 1" :total="data.total" />
        </div>

        <template #footer>
            <a-button @click="visible = false">
                关闭
            </a-button>
        </template>
    </a-modal>

    <a-modal v-model:visible="visibleSave" :width="660" destroyOnClose>

        <a-form :label-col="{ style: { width: '110px' } }" ref="aformRef" :model="saveForm">
            <a-row :gutter="16">
                <a-col :span="16">
                    <a-form-item label="群组名称" required name="groupName">
                        <a-input v-model:value="saveForm.groupName" placeholder="请输入" />
                    </a-form-item>
                </a-col>
                <a-col :span="16">
                    <a-form-item label="群组类型" required>
                        <!-- <a-input v-model:value="saveForm.groupType" placeholder="请输入" /> -->
                        <a-select ref="select" value="1">
                            <a-select-option value="1">工作简报</a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="16">
                    <a-form-item required label="群组用户" name="groupUserList">
                        <UserListSelect :disabled="false" :options="options" mode="multiple" ref="userList"
                            v-model:modelValue="saveForm.groupUserList" @change="userSelect" />
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>

        <template #footer>
            <a-button @click="saveGroup">
                保存
            </a-button>
            <a-button @click="visibleSave = false">
                关闭
            </a-button>
        </template>
    </a-modal>
</template>``
<script setup>
import api from '@/api/index';
import { message, Modal } from 'ant-design-vue';
import { mainStore } from '@/store';
import { useDictStore } from '@/store/dict';
import { ref } from 'vue';
//import { debug } from 'console';
const dict = useDictStore();
const store = mainStore();
const userList = ref(null)
const visible = ref(false);
const visibleSave = ref(false);
const aformRef = ref(null);
const data = reactive({
    list: [],
    columns: [
        {
            title: "群组名称",
            dataIndex: "groupName",
            width: 100,
            ellipsis: true,
        },
        {
            title: "业务类型",
            dataIndex: "recordType",
            width: 100,
            ellipsis: true,
        },
        {
            title: "用户",
            dataIndex: "userNames",
            width: 100,
            ellipsis: true,
        }, {
            title: "操作",
            key: "action",
            width: 100,
            fixed: "right",
        },
    ],
    total: 0,
});
const actions = (record, index) => {
    return [
        {
            text: '编辑',
            show: true,
            click: () => {
                update(record, index);
            }
        },
        {
            text: '删除',
            show: true,
            click: () => {
                deletegroup(record, index);
            }
        },
    ];
}
const filterForm = reactive({
    pageNo: 1,
    pageSize: 10,
    title: ''
});

const bodystyle = {
    height: '650px',
    width: '690px',
    overflow: 'hidden',
    overflowY: 'scroll',
}

const saveForm = reactive({
    id: null,
    groupUserList: [],
    groupName: ''
});

const addGroup = () => {
    visibleSave.value = true;
    saveForm.id = null;
    saveForm.groupName = '';
    saveForm.groupUserList = [];
}
const update = (record, index) => {
    visibleSave.value = true;
    saveForm.groupName = '';
    saveForm.groupUserList = [];

    api.workBrief.getGroup(record.id).then((res) => {
        if (res.code == 200) {
            saveForm.id = res.data.data.id;
            saveForm.groupName = res.data.data.groupName;
            saveForm.groupUserList = res.data.data.groupUserIdList;
            userList.value.searchUser(null, (saveForm.groupUserList || []))
        }
        store.spinChange(-1);
    });
}
const deletegroup = (record, index) => {

    Modal.confirm({
        title: "是否确认删除",
        content: '是否确认删除群组名称为"' + record.groupName + '"的数据项？',
        onOk() {
            api.workBrief.deleteGroup(record.id).then((res) => {

                if (res.code == 200) {
                    getGroupList();
                    if (!res.data.success) {
                        message.error(res.data.msg);
                    }
                }
                store.spinChange(-1);
            });

        },
    });


}


const userSelect = async (user) => {

}

const open = () => {
    store.spinChange(1);
    visible.value = true;
    getGroupList();
}

const saveGroup = () => {

    console.log('群组用户', saveForm.groupUserList);

    aformRef.value.validateFields().then(vRes => {
        let postData = {};

        postData.groupUserIdList = saveForm.groupUserList;
        postData.groupName = saveForm.groupName;
        postData.id = saveForm.id;

        api.workBrief.saveGroup(postData).then((res) => {
            if (res.code == 200) {

                if (!res.data.success) {
                    message.error(res.data.msg);
                } else {
                    getGroupList();
                    message.success("保存成功！");
                }

            }
            store.spinChange(-1);
            visibleSave.value = false;
        });
    }).catch(err => {
        collapseKey.value = ['base', 'zhaobiao'];
        message.warning('请完善信息！');
    })
}

const getPage = () => {
    getGroupList();
}


const getGroupList = async (callBack) => {
    let postData = {};
    postData.pageNo = filterForm.pageNo;
    postData.pageSize = filterForm.pageSize;
    //postData.groupName = filterForm.title;
    postData.likeParams = {};
    postData.likeParams.groupName = filterForm.title;

    api.workBrief.getGroupWorkList(postData).then((res) => {
        if (res.code == 200) {
            data.list = res.data.records;
            data.total = res.data.total;

            if (res.data.pages != 0 && res.data.pages < filterForm.pageNo) {
                filterSubmit();
            }
        }
        store.spinChange(-1);
    });
}
const filterSubmit = () => {
    filterForm.pageNo = 1;
    getGroupList();
}
const reset = () => {
    filterForm.pageNo = 1;
    filterForm.title = '';
    getGroupList();
}


</script>
<style scoped lang="less">
.subtitle {
    font-size: 14px;
    font-weight: normal;
}
</style>
