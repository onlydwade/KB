<template>
    <div class="page-common content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a  href="#/index">首页</a></a-breadcrumb-item>
                <a-breadcrumb-item>系统管理</a-breadcrumb-item>
                <a-breadcrumb-item>物业项目</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <div class="filter-box">
            <a-form :label-col="{ style: { width: '100px' } }">
                <a-row>
                    <a-col :span="12">
                        <a-form-item label="项目名称">
                            <a-input allowClear v-model:value="filterForm.realtyName" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12" class="text-right">
                        <a-space>
                            <a-button type="primary" @click="filterSubmit">查询</a-button>
                        </a-space>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <div class="content-box_full">
            <Title title="物业项目列表">
                <template #right>
                    <a-button type="primary" @click="edit(null,{})">
                        <template #icon>
                            <plus-outlined />
                        </template>
                        添加物业项目
                    </a-button>
                </template>
            </Title>
            <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">  
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'action'">
                        <a-button type="text" class="color-primary" size="small" @click="edit(record.id,record)">编辑</a-button>
                        <a-button type="text" class="color-primary" size="small" @click="handleDelete(record)">删除</a-button>
                    </template>
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
        
        <a-modal v-model:visible="editData.visible" :title="(editData.type==-1?'添加':'编辑')+'物业项目'" @ok="editSubmit" :width="560">
            <a-form
            ref="formRef"
            layout="vertical"
            :model="editData.data">
                <a-form-item label="项目名称" required name="realtyName">
                    <a-input v-model:value="editData.data.realtyName" placeholder="请输入" />
                </a-form-item>
                <a-form-item label="项目编码" name="realtyCode">
                    <a-input v-model:value="editData.data.realtyCode" placeholder="请输入" />
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
    
</template>
<script setup>
import api               from '@/api/index';
import { mainStore }     from '@/store';
import { message,Modal } from 'ant-design-vue';
const store = mainStore();

onMounted(() => {
    getPage();
})
const loadding    = ref(false);
const filterForm = reactive({
    pageNo     : 1,
    pageSize   : 10,
    realtyName : '',
})
const data = reactive({
    list    : [],
    columns : [
        {
            title     : '项目名称',
            dataIndex : 'realtyName',
            width     : 120,
        },
        {
            title     : '项目编码',
            dataIndex : 'realtyCode',
            width     : 120,
        },
        {
            title     : '创建人',
            dataIndex : ['createUser','realname'],
            width     : 180,
        },
        {
            title     : '创建时间',
            dataIndex : 'createTime',
            width     : 180,
        },
        {
            title : '操作',
            key   : 'action',
            width : 150,
            fixed : 'right',
        },
    ],
    total : 0,
})
const getPage = ()=>{
    let postData = {
        desc          : ['createTime'],
        pageNo        : filterForm.pageNo,
        pageSize      : filterForm.pageSize,
        content       : filterForm.realtyName,
        contentColumn : 'realtyName',
        params        : {}
    }
    loadding.value = true;
    api.common.realtyPage(postData).then(res=>{
        if(res.code==200){
            data.list  = res.data.records;
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
const filterSubmit = ()=>{
    filterForm.pageNo = 1;
    getPage();
}

//编辑添加
const editData = reactive({
    visible : false,
    type    : -1,
    data    : {},
})
const formRef  = ref(null);
const edit     = async (parentId,row)=>{
    store.spinChange(1);
    editData.data    = JSON.parse(JSON.stringify(row));
    editData.type    = row.id || -1;
    editData.visible = true;
    store.spinChange(-1);
    try {
        formRef.value.resetFields();
    } catch (e) {
        
    }
}
const editSubmit = ()=>{
    formRef.value.validateFields().then(values=>{
        let apiAttr = editData.type==-1?'realtyAdd':'realtyEdit';
        api.common[apiAttr](editData.data).then(res=>{
            if(res.code==200){
                getPage();
                message.success('操作成功');
                editData.visible = false;
            }
        })
    })
}
const handleDelete = (row)=>{
    Modal.confirm({
        title: '操作确认',
        content: '是否确认删除"' + row.realtyName + '"？',
        onOk() {
            api.common.realtyDel(row.id).then(res=>{
                if(res.code==200){
                    getPage();
                    message.success('删除成功');
                }
            })
        }
    });
}
</script>
<style scoped lang="less">
</style>