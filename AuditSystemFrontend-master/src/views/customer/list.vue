<template>
    <div class="page-customer content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a  href="#/index">首页</a></a-breadcrumb-item>
                <a-breadcrumb-item>客户管理</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <PageFilter v-model="filterForm" @submit="filterSubmit" @dataExport="dataExport" @cooperationExport="cooperationExport" />
        <div class="content-box_full">
            <Title title="客户列表">
                <template #right>
                  <a-button  @click="openCustomerModal" v-permission="['biz:customer:batchDelete']" style="margin-right: 10px;border:1px solid #F99C34;color:#F99C34;">
                    客户删除
                  </a-button>
                    <a-button type="primary" @click="router.push('/innerPage/customerEdit')" v-permission="['biz:customer:add']">
                        <template #icon>
                            <plus-outlined />
                        </template>
                        新建客户
                    </a-button>
                </template>
            </Title>
            <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">
                <template #bodyCell="{ column,text,record,index }">
                    <template v-if="column.key === 'customerNo'">
                        <router-link :to="'/innerPage/customerInfo?id='+record.id" class="color-link">
                            {{record.customerNo}}
                        </router-link>
                    </template>
                    <template v-if="column.key === 'followUser'">
                        <UserBox :data="record.followUserVO || {}" single/>
                    </template>
                    <template v-if="column.key === 'maintenanceUser'">
                        <UserBox :data="record.maintenanceUserVO || {}" single/>
                    </template>
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
        
        <BaseHandle ref="baseHandleRef" @success="updateRow" />
        <CustomerModalList  ref="customerModalListRef" @success="updateRow"/>
    </div>
</template>
<script setup>
import api               from '@/api/index';
import { message,Modal } from 'ant-design-vue';
import PageFilter        from './components/PageFilter.vue'
import BaseHandle        from './components/BaseHandle.vue'
import CustomerModalList        from './components/CustomerModalList.vue'

import { dataToFile , hasPermission }    from '@/utils/tools';
import { mainStore }     from '@/store';
const store      = mainStore();
const router     = useRouter();
const loadding   = ref(false);
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
})
const data = reactive({
    list    : [],
    columns : [
        {
            title     : '客户编码',
            key       : 'customerNo',
            width     : 170,
        },
        {
            title     : '客户全称',
            dataIndex : 'customerName',
            width     : 200,
            ellipsis  : true,
        },
        {
            title     : '跟进类型',
            dataIndex : 'customerLevelStr',
            width     : 120,
        },
        {
            title     : '客户类型',
            dataIndex : 'customerTypeStr',
            width     : 120,
        },
        {
            title     : '企业类型',
            dataIndex : 'companyTypeStr',
            width     : 120,
        },
        {
            title     : '合作类型',
            dataIndex : 'cooperationTypeStr',
            width     : 120,
        },
        {
            title     : '所属行业',
            dataIndex : 'customerIndustryStr',
            width     : 120,
        },
        {
            title     : '客户标签',
            dataIndex : 'keywords',
            width     : 150,
            ellipsis  : true,
        },
        {
            title : '客户跟进人',
            key   : 'followUser',
            width : 180,
        },
        {
            title : '信息维护人',
            key   : 'maintenanceUser',
            width : 180,
        },
        {
            title     : '客户所属省份',
            dataIndex : 'provinceName',
            width     : 120,
        },
        {
            title     : '客户所属城市',
            dataIndex : 'cityName',
            width     : 120,
        },
        {
            title     : '客户所属区/县',
            dataIndex : 'areaName',
            width     : 120,
        },
        {
            title     : '客户详细地址',
            dataIndex : 'address',
            width     : 150,
            ellipsis  : true,
        },
        {
            title     : '统一社会信用代码',
            dataIndex : 'customerCompanyNo',
            width     : 200,
        },
        {
            title     : '备注',
            dataIndex : 'remark',
            width     : 150,
            ellipsis  : true,
        },
        {
            title     : '创建人',
            dataIndex : ['createUser','realname'],
            width     : 180,
        },
        {
            title        : '创建时间',
            dataIndex    : 'createTime',
            width        : 180,
        },
        {
            title     : '最后修改人',
            dataIndex : ['updateUser','realname'],
            width     : 180,
        },
        {
            title        : '最后修改时间',
            dataIndex    : 'updateTime',
            width        : 180,
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
const getPage = ()=>{
    let postData   = builderFilter();
    loadding.value = true;
    api.customer.customerPage(postData).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
                  //如果后端返回的页数小于点击的页面则设置默认从第一页重新开始请求
            if (res.data.pages != 0 && res.data.pages < filterForm.pageNo) {
                filterSubmit();
            }
        }
        loadding.value = false;
    })
}
const filterSubmit = ()=>{
    filterForm.pageNo = 1;
    getPage();
}

const dataExport = ()=>{
    let postData = builderFilter();
    store.spinChange(1);
    api.customer.customerExport(postData).then(res=>{
        store.spinChange(-1);
        let timestamp = (new Date).getTime();
        dataToFile(res,'客户-'+timestamp+'.xlsx');
    })
}
const cooperationExport = ()=>{
    let postData = builderFilter();
    store.spinChange(1);
    api.customer.cooperationExport(postData).then(res=>{
        store.spinChange(-1);
        let timestamp = (new Date).getTime();
        dataToFile(res,'客户战略合作-'+timestamp+'.xlsx');
    })
}

const builderFilter = ()=>{
    let postData = {
        desc       : ['createTime'],
        pageNo     : filterForm.pageNo,
        pageSize   : filterForm.pageSize,
        params     : {},
        geParams   : {},
        leParams   : {},
        inParams   : {},
        likeParams : {}
    }
    if(filterForm.moreFilter){
        let params     = ['followUserId','maintenanceUserId','customerType','customerLevel','customerIndustry','provinceCode','cityCode','areaCode','cooperationType'];    
        let likeParams = ['customerName','customerNo','address','keywords'];
        params.forEach(key => {
            if(filterForm[key]&&filterForm[key]!=-1){
                postData.params[key] = filterForm[key];
            }
        });
        likeParams.forEach(key => {
            if(filterForm[key]){
                postData.likeParams[key] = filterForm[key];
            }
        });
    }
    if(!filterForm.moreFilter && filterForm.searchKey){
        postData.content        = filterForm.searchKey;
        postData.contentColumns = ['customerName', 'customerNo', 'customerCompanyNo'];
    }
    return postData;
}

const actions = (record,index)=>{
    return [
        {
            text  : '认领',
            show  : !record.followUserId && hasPermission(['biz:customer:claim']),
            click : ()=>{
                handleComponent('claim',record,index);
            }
        },
        {
            text  : '跟踪',
            show  : [record.maintenanceUserId,record.createUserId,record.followUserId].includes(store.userInfo.userId) ||  hasPermission(['biz:customer:follow']),
            click : ()=>{
                router.push('/innerPage/customerInfo?id='+record.id+'&tab=3');
            }
        },
        {
            text  : '申请战略合作',
            show  : [record.maintenanceUserId,record.createUserId,record.followUserId].includes(store.userInfo.userId) ||  hasPermission(['biz:customer:cooperation']),
            click : ()=>{
                router.push('/innerPage/customerInfo?id='+record.id+'&tab=5');
            }
        },
        {
            text  : '编辑',
            show  : [record.maintenanceUserId,record.createUserId,record.followUserId].includes(store.userInfo.userId) ||  hasPermission(['biz:customer:update']),
            click : ()=>{
                router.push('/innerPage/customerEdit?id='+record.id);
            }
        },
        {
            text  : '删除',
            show  : [record.maintenanceUserId,record.createUserId,record.followUserId].includes(store.userInfo.userId) ||  hasPermission(['biz:customer:delete']),
            click : ()=>{
                // handleComponent('delete',record,index);
                Modal.confirm({
                  title: "操作确认",
                  content: "是否确认删除该客户？",
                  async onOk() {
                    api.customer.deleteCustomer({ids: [record.id]}).then(res => {
                      if (res.code == 200) {
                        message.success("删除成功");
                        getPage();
                      }
                    });
                  },
                });
            }
        },
    ];
}
const baseHandleRef     = ref(null);
const handleComponent   = (component,row,index)=>{
    if(component=='delete'){     //删除
        baseHandleRef.value.open(row,'delete',index);
    }
    if(component=='claim'){     //认领
        Modal.confirm({
            title   : '操作确认',
            content : '确认认领此客户?',
            onOk() {
                let postData = {
                    id           : row.id,
                    followUserId : store.userInfo.userId
                }
                api.customer.customerClaim(postData).then(res=>{
                    if(res.code==200){
                        message.success('操作成功');
                        updateRow(res.data,index);
                    }
                })
            }
        });
    }
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
const customerModalListRef = ref(null)
const openCustomerModal = ()=>{
    customerModalListRef.value.open();
}
onMounted(() => {
    getPage();
})
onActivated(() => {
    getPage();
})
</script>
<script>export default{name:'homeCustomer'}</script>
<style scoped lang="less">
</style>