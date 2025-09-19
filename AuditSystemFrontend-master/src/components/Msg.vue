
<template>
<a-drawer
    :width="560"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
    <div class="message_box">
        <a-spin :spinning="loadding">
            <a-list item-layout="horizontal" :data-source="data.list">
                <template #renderItem="{ item }">
                    <a-list-item>
                        <a-list-item-meta>
                            <template #title>
                                {{ item.title }}
                            </template>
                            <template #description>
                                <div v-if="item.messageType=='APPROVAL'">
                                    <p v-if="resoutParse(item.message).approvalStatus">
                                        审批状态:{{resoutParse(item.message).approvalStatus}}
                                    </p>
                                    <p v-if="resoutParse(item.message).submitUserName">
                                        审批发起人:{{resoutParse(item.message).submitUserName}}
                                    </p>
                                    <p v-if="resoutParse(item.message).submitTime">
                                        审批发起时间:{{resoutParse(item.message).submitTime}}
                                    </p>
                                    <p v-if="resoutParse(item.message).approvalUrl">
                                        <a class="color-link" @click="toOaLink(resoutParse(item.message).approvalUrl)">
                                            OA审批详情>>
                                        </a>
                                    </p>
                                </div>
                                <div v-else>
                                    {{item.message}}
                                </div>
                                <br>
                                {{item.createTime}}
                            </template>
                        </a-list-item-meta>
                        <template #actions>
                            <a class="color-link" @click="showInfo(item)" v-if="!!linkMode[item.module]">查看</a>
                            <a-tooltip title="标记为已读" v-if="item.openStatus==0">
                                <a class="color-link" @click="msgMark(item.id,item)">已读</a>
                            </a-tooltip>
                            <span class="color-gray" v-if="item.openStatus!=0">已读</span>
                        </template>
                        <span class="dot" v-if="item.openStatus==0"></span>
                    </a-list-item>
                </template>
            </a-list>
        </a-spin>
    </div>
    <template #title>
        <span style="margin-right:16px;">消息通知</span>
        <a-checkbox v-model:checked="data.onlyunread" @change="filterSubmit">仅查看未读</a-checkbox>
    </template>
    <template #extra>
        <a-tooltip title="标记全部消息为已读">
            <a class="color-link" @click="msgMark(0,{})">全部标记已读</a>
        </a-tooltip>
    </template>
    <template #footer>
        <div class="pagination_box">
            <a-pagination showSizeChanger
                v-model:current="data.pageNo"
                v-model:pageSize="data.pageSize"
                :show-total="total => `共 ${total} 条数据`"
                size="small"
                @change="getPage"
                @showSizeChange="data.pageNo==1"
                :total="data.total" />
        </div>
    </template>
</a-drawer>
</template>
<script setup>
import api         from '@/api/index';
import { message } from 'ant-design-vue';
import { mainStore } from '@/store';
const store = mainStore();
const emit        = defineEmits(['countChange'])
const router      = useRouter();
const visible     = ref(false);
const loadding    = ref(false);
const handleClose = ()=>{
    visible.value = false;
}
const data = reactive({
    list       : [],
    total      : 0,
    pageNo     : 1,
    pageSize   : 20,
    onlyunread : false,
})
const getPage = ()=>{
    let postData = {
        desc     : ['createTime'],
        pageNo   : data.pageNo,
        pageSize : data.pageSize,
        params   : {}
    }
    if(data.onlyunread){
        postData.params.openStatus = 0;
    }
    loadding.value = true;
    api.sys.messagePage(postData).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
const filterSubmit = ()=>{
    data.pageNo = 1;
    getPage();
}
const open = (data,index)=>{
    getPage();
    visible.value = true;
}
defineExpose({open})

const msgMark = (id,item)=>{
    let ids = [];
    if(id)ids.push(id);
    api.sys.messageMark(ids).then(res=>{
        if(res.code==200){
            // message.success('操作成功');
            if(id==0){
                getPage();
            }else{
                item.openStatus = 1;
            }
            emit('countChange');
        }
    })
}

const linkMode = {
    Lead        : (id)=>`/innerPage/leadInfo?id=${id}`,
    Opportunity : (id)=>`/innerPage/opportunityInfo?id=${id}`,
    Project     : (id)=>`/innerPage/projectInfo?id=${id}`,
    Train       : (id)=>`/operation/train?id=${id}`,
    Meeting     : (id)=>`/operation/meeting?id=${id}`,
    COMPANY_INFO     : (id)=>`/innerPage/subsidiaryInfo?id=${id}`,
}
const showInfo = (item)=>{
    visible.value = false;
    if(item.openStatus==0)msgMark(item.id,item);
    router.push(linkMode[item.module](item.moduleId));
}

const toOaLink = (link)=>{
    api.common.getSsoToken({
        mobile:store.userInfo.phonenumber
    }).then(res=>{
        if(res.code==200 && res.data){
            window.open(link+'&access_token='+res.data);
        }
    })
}
const resoutParse = (str)=>{
    try {
        return JSON.parse(str);
    } catch (e) {
        return {
            message : str || '-'
        }
    }
}
</script>
<style scoped lang="less">
.message_box{
    margin: -24px;
    :deep(.ant-list-item){
        padding: 24px;
        position: relative;
        &:hover{
            background-color: #f0f2f5;
        }
        .dot{
            width            : 8px;
            height           : 8px;
            border-radius    : 50%;
            background-color : @error-color;
            position         : absolute;
            top              : 16px;
            right            : 16px;
        }
    }
}
</style>
