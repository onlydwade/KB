<template>
    <div class="page-common content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a  href="#/index">首页</a></a-breadcrumb-item>
                <a-breadcrumb-item>系统管理</a-breadcrumb-item>
                <a-breadcrumb-item>角色管理</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <div class="filter-box">
            <a-form :label-col="{ style: { width: '100px' } }" ref="filterFormRef" :model="filterForm">
                <a-row>
                    <a-col :span="8">
                        <a-form-item label="角色名称" name="postName">
                            <a-input allowClear v-model:value="filterForm.postName" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="6">
                        <a-form-item label="状态" name="status">
                            <a-select
                                v-model:value="filterForm.status"
                                class="w_full"
                                placeholder="请选择"
                               :options="[{label:'全部',value:-1},{label:'启用中',value:0},{label:'禁用中',value:1}]">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :span="10" class="text-right">
                        <a-space>
                            <a-button type="primary" @click="filterSubmit">查询</a-button>
                            <a-button @click="reset">重置</a-button>
                        </a-space>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <div class="content-box_full">
            <Title title="角色列表">
                <template #right>
                    <a-button type="primary" @click="edit(null,{})" v-permission="['system:post:add']">
                        <template #icon>
                            <plus-outlined />
                        </template>
                        添加角色
                    </a-button>
                </template>
            </Title>
            <FullTable :columns="columns" :dataSource="postList" rowKey="postId">
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'status'">
                        <span  v-if="record.status==0" class="color-success">启用中</span>
                        <span  v-if="record.status==1" class="color-gray">已禁用</span>
                    </template>
                    <template v-if="column.key === 'action'">
                        <a-button type="text" class="color-primary" size="small" @click="edit(record.postId,record)" v-permission="['system:post:update']">编辑</a-button>
                        <a-button type="text" class="color-primary" size="small" @click="handleDelete(record)" v-permission="['system:post:delete']">删除</a-button>
                    </template>
                </template>
            </FullTable>
        </div>
        
        <a-modal v-model:visible="editData.visible" :title="(editData.type==-1?'添加':'编辑')+'角色'" @ok="editSubmit" :width="680">
            <a-form
            ref="formRef"
            :model="editData.data"
            :label-col="{ style: { width: '100px' } }">
                <a-row :gutter="16">
                    <a-col :span="12">
                        <a-form-item label="角色名称" required name="postName">
                            <a-input v-model:value="editData.data.postName" placeholder="请输入" show-count :maxlength="30" />
                        </a-form-item>
                    </a-col>
                    <!-- <a-col :span="12">
                        <a-form-item label="角色编码" required name="postName">
                            <a-input v-model:value="editData.data.postCode" placeholder="请输入" />
                        </a-form-item>
                    </a-col> -->
                    <a-col :span="12">
                        <a-form-item label="状态" name="status">
                            <a-switch v-model:checked="editData.data.status" checked-children="启用" checkedValue="0" un-checked-children="禁用" unCheckedValue="1" />
                        </a-form-item>
                    </a-col>
                    <!-- <a-col :span="12">
                        <a-form-item label="排序号" name="postSort">
                            <a-input-number v-model:value="editData.data.postSort" placeholder="请输入序号" :precision="0" :min="0" class="w_full"/>
                        </a-form-item>
                    </a-col> -->
                </a-row>
                <!-- <a-form-item label="角色说明">
                    <a-textarea allowClear :rows="2" type="textarea" v-model:value="editData.data.remark" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
                </a-form-item> -->
                <a-form-item label="数据权限">
                    <a-radio-group v-model:value="editData.data.dataRoleId" name="dataRoleId">
                        <a-radio v-for="(item,index) in dataPermissions" :key="index" :value="item.menuId">
                            {{item.menuName}}
                        </a-radio>
                    </a-radio-group>
                </a-form-item>    
                <a-row :gutter="16">
                    <a-col :span="12">
                        <a-form-item label="赢标宝账号" name="type">
                            <a-input v-model:value="editData.data.ybbUser" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row :gutter="16">
                    <a-col :span="12">
                        <a-form-item label="业务角色标识" name="type">
                            <a-input v-model:value="editData.data.type" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                </a-row>
                
                <a-form-item label="角色权限" v-if="editData.visible">
                    <MenuTree v-model="editData.data.menuIds" :menuList="menuList" />
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
    
</template>
<script setup>
import api               from '@/api/index';
import { mainStore }     from '@/store';
import moment            from 'moment'
import { message,Modal } from 'ant-design-vue';
const store = mainStore();

onMounted(() => {
    getPost();
    getDataPermission();
})
const columns = ref([
    {
        title     : '角色名称',
        dataIndex : 'postName',
    },
    // {
    //     title     : '角色说明',
    //     dataIndex : 'remark',
    // },
    {
        title : '角色状态',
        key   : 'status',
        width : 120,
    },
    {
        title     : '创建人',
        dataIndex : 'createBy',
        width     : 180,
    },
    {
        title        : '创建时间',
        dataIndex    : 'createTime',
        width        : 180,
    },
    {
        title : '操作',
        key   : 'action',
        width : 120,
        fixed : 'right'
    },
])

const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
    status   : -1,
})
const postList      = ref([]);
const filterFormRef = ref(null);
const getPost       = ()=>{
    api.sys.postListAll().then(res=>{
        if(res.code==200){
            postList.value = res.data.filter(item=>{
                return (filterForm.status==-1 || filterForm.status==item.status) && (item.postName.includes(filterForm.postName) || !filterForm.postName)
            });
        }
    })
}
const filterSubmit = ()=>{
    filterForm.pageNo = 1;
    getPost();
}
const reset = ()=>{
    filterFormRef.value.resetFields();
    filterSubmit();
}

const dataPermissions = ref([]);
const getDataPermission = ()=>{
    api.sys.dataPermission().then(res=>{
        if(res.code==200){
            dataPermissions.value = res.data || [];
        }
    })
}

//编辑添加
const editData = reactive({
    visible : false,
    type    : -1,
    data    : {
        menuIds : []
    },
})
const formRef  = ref(null);
const menuList = ref([]);
const edit     = async (parentId,row)=>{
    const isAdmin = sessionStorage.getItem('isAdmin') || 0;
    if(isAdmin!=1){
        message.error('当前账号不支持编辑角色');
        return;
    }
    store.spinChange(1);
    if(menuList.value.length==0){   //是否获取菜单数据
        let menuRes = await api.sys.menuList();
        if(menuRes.code==200){
            menuList.value = menuRes.data;
        }
    }
    
    editData.data          = JSON.parse(JSON.stringify(row));
    if(row.postId){
        let info = await api.sys.postInfo(row.postId);
        if(info.code==200){
            editData.data = info.data;
        }
        editData.data.menuIds = editData.data.menuIds?editData.data.menuIds:[];
    }else{
        editData.data.status         = '0';
        editData.data.dataRoleId     = 0;
        editData.data.menuIds        = [];
        editData.data.postSort       = 1;
    }    
    editData.type    = row.postId || -1;
    editData.visible = true;
    store.spinChange(-1);
    try {
        formRef.value.resetFields();
    } catch (e) {
        
    }
}
const editSubmit = ()=>{
    formRef.value.validateFields().then(values=>{
        if(editData.data.dataRoleId==0) {
            message.error('请选择数据权限');
            return false;
        } else {
            let apiAttr = editData.type==-1?'postAdd':'postEdit';
            api.sys[apiAttr](editData.data).then(res=>{
                if(res.code==200){
                    getPost();
                    message.success('操作成功');
                    editData.visible = false;
                }
            })
        }
    })
}
const handleDelete = (row)=>{
    const isAdmin = sessionStorage.getItem('isAdmin') || 0;
    if(isAdmin!=1){
        message.error('当前账号不支持编辑角色');
        return;
    }
    
    Modal.confirm({
        title: '操作确认',
        content: '是否确认删除名称为"' + row.postName + '"的角色？',
        onOk() {
            api.sys.postDel(row.postId).then(res=>{
                if(res.code==200){
                    getPost();
                    message.success('删除成功');
                }
            })
        }
    });
}

</script>
<style scoped lang="less">
</style>