<template>
    <a-modal v-model:visible="visible" title="发送数据看板" destroyOnClose :width="1160" footer="">
        <a-form layout="vertical" :model="formData" ref="formRef">
            <a-collapse ghost expandIconPosition="right" v-model:activeKey="collapseKey" class="crl-collapse">

                <a-collapse-panel key="base">
                    <template #header>
                        <h5 class="title_single">发送数据看板信息</h5>
                        <span class="color-danger">*</span>
                    </template>
                    <a-row :gutter="24">
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="发送对象" name="pushUserList">
                                <UserGroupListSelect :disabled="isView" :options="options" mode="multiple"
                                    ref="userList" v-model:modelValue="formData.pushUserList" @change="userSelect" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="数据层级" name="deptId">
                                <a-tree-select v-model:value="formData.deptId" show-search
                                    :dropdown-style="{ maxHeight: '500px', overflow: 'auto' }" placeholder="请选择查询主体"
                                    tree-default-expand-all treeNodeFilterProp="name" :dropdownStyle='{
        whiteSpace: "nowrap"
    }' @select="deptSelect" :field-names="{
        children: 'children',
        label: 'name',
        value: 'id',
    }" :tree-data="formData.tree">
                                </a-tree-select>
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="年份" name="dateVal">
                                <a-date-picker :allowClear="false" v-model:value="formData.dateVal"
                                    :picker="formData.dateType" :valueFormat="formData.dateFormat"
                                    :format="formData.dateFormat" :disabledDate="formData.disabledDate" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-radio-button @click="openSendTodoModal"
                                style="background-color: #f99c34;color: white;">发送</a-radio-button>
                        </a-col>
                    </a-row>
                </a-collapse-panel>

                <a-collapse-panel key="his">
                    <template #header>
                        <h5 class="title_single">历史发送数据看板信息</h5>
                        <span class="color-danger">*</span>
                    </template>
                    <a-row :gutter="24">
                        <div style="margin-top: 10px;width: 100%;" v-for="item in historyList">
                            <div>发送时间： {{ item.createTime }}</div>
                            <div>数据层级： {{ item.deptName }}</div>
                            <div>发送对象：
                                <span v-for="(aitem,index) in item.pushUserList">
                                    {{ aitem.userName }}
                                    <span v-if="index < item.pushUserList.length -1 ">、</span>
                                </span>
                            </div>
                        </div>
                    </a-row>
                </a-collapse-panel>
            </a-collapse>
        </a-form>
    </a-modal>
</template>

<script setup>
import api from '@/api/index';
import { message } from 'ant-design-vue';
import { amountUnit, parseFormatNum, dataToFile } from '@/utils/tools';
import moment from 'moment'

import { ref } from 'vue';
import { useDictStore } from '@/store/dict';
const dict = useDictStore();

const formData = reactive({
    pushUserList: null,
    deptId: null,
    level: null,
    tree: [],
    dateType: 'year',
    dateVal: moment(new Date()).format('YYYY'),
    dateFormat: computed(() => {
        return formData.dateType == 'month' ? 'YYYY-MM' : 'YYYY'
    }),
    disabledDate: (current) => {
        return current && current > moment().endOf('day')
    }
});

const historyList = ref([]);

const emit = defineEmits(['success'])
const visible = ref(false);
const formRef = ref(null);
const disabled = ref(true);
const loadding = ref(false);
const userList = ref(null)
const collapseKey = ref(['base', 'his'])


const deptSelect = (val, option) => {
    formData.deptId = option.id;
    formData.level = option.level;
}

const getDeptWithOutRoot = (tree) => {
    let arr = [];
    function doArr(treeData, parentChild) {
        if (treeData) {
            treeData.forEach((item, i) => {
                let obj = {
                    ...item,
                    children: []
                }

                if (item.deptType === 'CENG_JI' || (item.children && item.children.length > 0)) {
                    doArr(item.children, obj.children);
                    parentChild.push(obj)
                }
            });
        }
    }
    doArr(tree, arr);
    return arr;
}

const getDept = async () => {
    let res = await api.performance.actualInTree();
    if (res.code == 200 && res.data) {
        let tree = [];
        tree = getDeptWithOutRoot([res.data]);
        formData.tree = tree;
        formData.deptId = tree[0].id;
        formData.level = tree[0].level;
    }
}

//接收传递的参数
const open = (level, deptId, dateVal) => {
    console.log("aslkdjflsdkjflkdsj")
    visible.value = true;

};

const openSendTodoModal = () => {

    let postData = {}

    formRef.value.validateFields().then(async (vRes) => {

        postData.pushUserList = formData.pushUserList
        postData.year = formData.dateVal
        postData.deptId = formData.deptId

        api.performance.sendDataBoardTodo(postData).then(res => {
            if (res.code == 200) {
                console.log(res)
                getDataBoardTodoInfo();
            }
        })

        formData.pushUserList = []
        message.success("发送成功");
    }).catch(err => {
        message.warning('请完善必填信息！');
    })
}

const getDataBoardTodoInfo = () => {

    api.performance.getDataBoardTodo().then(res => {
        if (res.code == 200) {
            console.log(res)
            historyList.value = res.data;
        }
    })


}


defineExpose({ open })

onMounted(() => {
    getDataBoardTodoInfo();
    getDept();
})

</script>
<style scoped lang="less"></style>
