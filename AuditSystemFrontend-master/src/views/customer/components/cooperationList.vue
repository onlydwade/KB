<template>
    <div class="page-customer content-inner">
       
        <div class="content-box_full">
            <Title title="战略合作列表">
                <template #right>
                    <a-button type="primary" @click="openModal()"  >
                        <template #icon>
                            <plus-outlined />
                        </template>
                        新增
                    </a-button>
                </template>
            </Title>
            <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">
                <template #bodyCell="{ column,text,record,index }">

                    <template v-if="column.key === 'action'">
                        <actionBtn :actions="actions(record,index)"/>
                    </template>
                    <EllipsisTooltip v-if="column.ellipsis" :content="text"/>
                </template>
            </FullTable>
            <div class="pagination_box">
                <a-pagination showSizeChanger show-quick-jumper
                    v-model:current="filterForm.pageNo"
                    v-model:pageSize="filterForm.pageSize"
                    :show-total="total => `共 ${total} 条数据`"
                    size="small"
                    @change="getPage"
                    @showSizeChange="filterForm.pageNo=1"
                    :total="data.total" />
            </div>
        </div>
        <CooperationList :customerId="customerId" v-if="tabKey == 5"
            :readOnly="![infoData.createUserId, infoData.followUserId].includes(store.userInfo.userId) && !hasPermission(['biz:customer:cooperation'])" />

    </div>
</template>
<script setup>
import api               from '@/api/index';
import { message,Modal } from 'ant-design-vue';
import { dataToFile , hasPermission }    from '@/utils/tools';
// import cooperationEdit from './CooperationEdit.vue';
import { mainStore }     from '@/store';
const store      = mainStore();
const router     = useRouter();
const visible     = ref(false);
const formData    = ref({});
const loadding   = ref(false);
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})
const data = reactive({
    list    : [],
    columns : [
        {
            title     : '签约甲方',
            dataIndex : 'partyA',
            width     : 170,
        },
        {
            title     : '签约乙方',
            dataIndex : 'partyB',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '签订时间',
            dataIndex : 'signDate',
            width     : 120,
        },
        {
            title     : '期限（年）',
            dataIndex : 'termYear',
            width     : 120,
        },
        {
            title     : '开始日期',
            dataIndex : 'startDate',
            width     : 120,
        },
        {
            title     : '结束日期',
            dataIndex : 'endDate',
            width     : 120,
            // customRender:({text})=>{
            //     return dateFormat(text,'YYYY-MM-DD')
            // }
        },
        {
            title     : '协议内容',
            dataIndex : 'content',
            width     : 150,
            ellipsis  : true,
        },
        {
            title : '操作',
            key   : 'action',
            width : 240,
            fixed : 'right'
        },
    ],
    total : 0,
})
const props = defineProps({
    customerId:{
        type    : Number,
        default : 0,
    },
    readOnly:{
        type    : Boolean,
        default : false,
    }
})

const getPage = ()=>{
    let postData   = builderFilter();
    loadding.value = true;
    api.customer.cooperationPage(postData).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
 
const builderFilter = ()=>{
    //debugger    
    let postData = {
        desc       : ['createTime'],
        pageNo     : filterForm.pageNo,
        pageSize   : filterForm.pageSize,
        params     : {    "customerId":props.customerId},
        geParams   : {},
        leParams   : {},
        inParams   : {},
        likeParams : {}
    }

    return postData;
}

const actions = (record,index)=>{
    return [        
        {
            text  : '编辑',
            show  :  true ,
            click : ()=>{
                //openModal();
                router.push('/innerPage/cooperation?customerId='+props.customerId +'&recordId='+record.id);
            }
        },
        {
            text  : '删除',
            show  :  true,
            click : ()=>{
                handleDelete(record);
            }
        },
    ];
}
// const baseHandleRef     = ref(null);
// const handleComponent   = (component,row,index)=>{
//     if(component=='delete'){     //删除
//         baseHandleRef.value.open(row,'delete',index);
//     }   
// }

const openModal =  ()=>{
    router.push('/innerPage/cooperation?customerId='+props.customerId )
    //visible.value = true;
}
 

const handleDelete = (row)=>{
        Modal.confirm({
            title: '操作确认',
            content: '是否确认删除？',
            onOk() { 
                api.customer.cooperationDel(row.id).then(res=>{
                    if(res.code==200){
                        getPage();
                        message.success('删除成功');
                    }
                })
            }
        });
    }

const updateRow = (res,index,type)=>{
    if(type=='delete'){
        getPage();
    }else{
        if(res&&res.id){
            data.list[index] = res;
        }else{
            getPage();
        }
    }
}

onMounted(() => {
    getPage();
})
onActivated(() => {
    getPage();
})
</script>
<!-- <script>export default{name:'cooperation'}</script> -->
<style scoped lang="less">
</style>