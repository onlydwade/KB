
<template>
    <div class="content-box_full">
        <Title title="基础数据"></Title>
        <div class="description_box">
            <a-descriptions :column="{ xs: 8 }">
                <a-descriptions-item label="总户数">{{ infoData.househoidSum || '-'}}</a-descriptions-item>
                <a-descriptions-item label="占地面积（m²）">{{ infoData.floorSpace ||  '-'}}</a-descriptions-item>
                <a-descriptions-item label="建筑面积（m²）">{{ infoData.constructionArea ||  '-'}}</a-descriptions-item>
            </a-descriptions>
        </div>
        <div class="menu_inner padding_box">
        <Title title="关联项目"></Title>
            <a-table :columns="projectColumns" :data-source="projectList" :pagination="false" :scroll="{ x: '100%',y:360 }" bordered>
                <template #bodyCell="{ column,text,record,index }">
                    <template v-if="column.key === 'projectName'">
                        <router-link :to="'/innerPage/extensionInfo?id='+record.projectId" class="color-link">
                            {{record.projectName}}
                        </router-link>
                    </template>
                    <!-- <EllipsisTooltip v-if="column.ellipsis" :content="text"/> -->
                </template>
            </a-table>
        </div>
        <div class="menu_inner padding_box">
            <Title title="经营指标信息"></Title>
            <a-table :columns="columnsBusiness" :data-source="businessList" :pagination="false" :scroll="{ x: '100%',y:360 }" bordered>
            </a-table>
        </div>
        <div class="menu_inner padding_box">
            <Title title="立项收缴率指标信息"></Title>
            <a-table :columns="columnsApproval" :data-source="approvalList" :pagination="false" :scroll="{ x: '100%',y:360 }" bordered>
                <template #bodyCell="{ column,text,record,index }">
                    <template v-if="column.key === 'id'">
                        {{'立项收缴率指标（%）'}}
                        </template>
                    <!-- <EllipsisTooltip v-if="column.ellipsis" :content="text"/> -->
                </template>
            </a-table>
        </div>
        <div class="menu_inner padding_box">
            <Title title="服务满意度"></Title>
            <a-table :columns="columnsContract" :data-source="contractList" :pagination="false" :scroll="{ x: '100%',y:360 }" bordered>
            </a-table>
            <a-table :columns="columnsCustome" :data-source="custometList" :pagination="false" :scroll="{ x: '100%',y:360 }" bordered>
            </a-table>
        </div>
    </div>
</template>
<script setup>
import api from '@/api/index';
const infoData  = ref({roleKeys:[]});
// const router      = useRouter();
const route       = useRoute();
const projectId   = ref(Number(route.query.id || 0))
const props = defineProps({
    recordId:{
        type    : Number,
        default : false,
    }
})
const loadding = ref(false);
const projectList  = ref([]);
const projectColumns = ref([
        {
            title     : '关联项目名称',
            key       : 'projectName'
        }
]);
const businessList  = ref([]);
const columnsBusiness = ref([
        {
            title     : '可收费类型',
            dataIndex : 'chargeType',
            width     : 150
        },
        {
            title     : '收费单价（元/m）',
            dataIndex : 'chargePrice',
            width     : 150
        },
        {
            title     : '收费面积（元/m）',
            dataIndex : 'quantity',
            width     : 150
        },
        {
            title     : '饱和收入（元）',
            dataIndex : 'amount',
            width     : 150
        }
]);
const approvalList  = ref([]);
const columnsApproval = ref([
        {
            title     : '指标',
            key       : 'id',
            width     : 150
        },
        {
            title     : '第一年(%)',
            dataIndex : 'collectionRateFirst',
            width     : 150
        },
        {
            title     : '第二年(%)',
            dataIndex : 'collectionRateSecond',
            width     : 150
        },
        {
            title     : '第三年(%)',
            dataIndex : 'collectionRateThird',
            width     : 150
        }
]);
const contractList  = ref([]);
const columnsContract = ref([
{
            title     : '年份',
            dataIndex : 'year',
            width     : 150
        },
        {
            title     : '合同相对方满意度',
            dataIndex : 'satisfactionExplain',
            width     : 150
        }
])
const custometList  = ref([]);
const columnsCustome = ref([
{
            title     : '年份',
            dataIndex : 'year',
            width     : 150
        },
        {
            title     : '业主服务满意度',
            dataIndex : 'satisfactionExplain',
            width     : 150
        }
])

// 关联项目
const getPage = ()=>{
    loadding.value = true;
    api.project.projectCorrelationList(projectId.value).then(res=>{
        if(res.code==200){
            projectList.value  = res.data;
        }
        loadding.value = false;
    })
}
//经营指标信息
const getMgmtPage = ()=>{
    loadding.value = true;
    api.project.projectMgmtIndicatorsList(projectId.value).then(res=>{
        if(res.code==200){
            businessList.value  = res.data;
        }
        loadding.value = false;
    })
}
//立项收缴率指标信息
const getOperationPage = ()=>{
    loadding.value = true;
    api.project.projectOperationList(projectId.value).then(res=>{
        if(res.code==200){
            approvalList.value  = res.data;
            console.log(res.data)
        }
        loadding.value = false;
    })
}
// 合同服务满意度
const postServeSatisList = ()=>{
    loadding.value = true;
    let postData = {
        projectId : projectId.value,
        satisfactionType: 1
    }
    api.project.projectServeSatisList(postData).then(res=>{
        if(res.code==200){
            contractList.value  = res.data;
        }
        loadding.value = false;
    })
    let postData2 = {
        projectId : projectId.value,
        satisfactionType: 2
    }
    api.project.projectServeSatisList(postData2).then(res=>{
        if(res.code==200){
            custometList.value  = res.data;
        }
        loadding.value = false;
    })
}


const getInfo  = (callBack)=>{
    api.project.projectOperationInfo(projectId.value).then(res=>{
        if(res.code==200){
            infoData.value = res.data;
            callBack && callBack();
        }
    })
}

onMounted(() => {
    getPage();
    getInfo();
    getMgmtPage();
    getOperationPage();
    postServeSatisList();
})
</script>
<style scoped lang="less">
.description_box{
    margin         : 16px;
    padding-bottom : 24px;
    margin-bottom  : 24px;
    border-bottom  : 1px solid @border-color-base;
    &:last-child{
        border-bottom  : none;
        // padding-bottom : 0;
        margin-bottom  : 0;
    }
}
</style>
