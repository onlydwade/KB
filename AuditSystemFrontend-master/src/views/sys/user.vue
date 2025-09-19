<template>
    <div class="page-common content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a  href="#/index">首页</a></a-breadcrumb-item>
                <a-breadcrumb-item>系统管理</a-breadcrumb-item>
                <a-breadcrumb-item>用户管理</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <div class="left_filter_content">
            <DeptTree v-model="filterForm.deptId" @change="filterSubmit" title="系统业务架构" :deptList="deptList"/>
            <div class="right_content">
                <div class="filter-box">
                    <a-form :label-col="{ style: { width: '100px' } }"  ref="filterFormRef" :model="filterForm">
                        <a-row>
                            <a-col :span="8">
                                <a-form-item label="用户姓名" name="realname">
                                    <a-input allowClear v-model:value="filterForm.realname" placeholder="请输入" />
                                </a-form-item>
                            </a-col>
                            <a-col :span="16" class="text-right">
                                <a-space>
                                    <a-button type="primary" @click="filterSubmit">查询</a-button>
                                    <a-button @click="reset">重置</a-button>
                                </a-space>
                            </a-col>
                        </a-row>
                    </a-form>
                </div>
                <div class="content-box_full">
                    <Title title="用户管理">
                        <template #right>
                            <a-button :disabled="batchIds.length==0" @click="batchPostSet" v-permission="['system:userDeptPost:addDeptPost']">
                                批量关联系统角色
                            </a-button>
                        </template>
                    </Title>
                    <FullTableAuto @tableReady="tableReady">
                        <a-table :columns="data.columns" :loading="loadding" :data-source="data.list" rowKey="userId" :row-selection="rowSelection"  :pagination="false" :scroll="{x:'100%',y: tableHeight }">    
                            <template #bodyCell="{ column, record,text }">
                                <template v-if="column.key === 'avatar'">
                                    <a-avatar :size="32" src="/images/logo.png"></a-avatar>
                                </template>
                                <template v-if="column.key === 'status'">
                                    <a-tag v-if="record.status==0" color="success">启用中</a-tag>
                                    <a-tag v-if="record.status==1" color="warning">已禁用</a-tag>
                                </template>
                                <template v-if="column.key === 'action'">
                                    <a-button type="text" class="color-primary" size="small" @click="edit(record.userId,record)" v-permission="['system:user:update']">编辑</a-button>
                                    <!-- <a-button type="text" class="color-primary" size="small" @click="handleDelete(record)">删除</a-button> -->
                                    <a-button type="text" class="color-primary" size="small" @click="handleComponent('deptPost',record,index)">关联角色</a-button>
                                </template>
                                <EllipsisTooltip v-if="column.ellipsis" :content="text"/>
                            </template>
                        </a-table>
                    </FullTableAuto>
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
            </div>
        </div>
        
        <a-modal v-model:visible="editData.visible" :title="(editData.type==-1?'添加':'编辑')+'用户'" @ok="editSubmit" :width="860">
            <a-form
            ref="formRef"
            layout="vertical"
            :model="editData.data">
                <a-row :gutter="16">
                    <a-col :span="8">
                        <a-form-item label="用户姓名" required name="realname">
                            <a-input v-model:value="editData.data.realname" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="8">
                        <a-form-item label="平台账号" required name="userName">
                            <a-input disabled :value="editData.data.userName"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="8">
                        <a-form-item label="手机号" required name="phonenumber">
                            <a-input disabled :value="editData.data.phonenumber"/>
                        </a-form-item>
                    </a-col>
                    
                    <!-- <a-col :span="12">
                        <a-form-item label="登录密码" name="password" :required="editData.type==-1">
                            <a-input v-model:value="editData.data.password" :placeholder="editData.type==-1?'请输入':'不填表示不修改'" />
                        </a-form-item>
                    </a-col> -->
                    <a-col :span="16">
                        <a-form-item label="系统内挂靠组织" required  name="deptId">
                            <a-tree-select :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="editData.data.deptId"
                                show-search
                                class="w_full"
                                allow-clear
                                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                                placeholder="请选择挂靠组织"
                                tree-default-expand-all
                                filterTreeNode
                                treeNodeFilterProp="deptName"
                                :field-names="{
                                    children: 'children',
                                    label: 'deptName',
                                    value: 'deptId',
                                }"
                                :tree-data="deptList">
                              </a-tree-select>
                        </a-form-item>
                    </a-col>
                    <a-col :span="8">
                        <a-form-item label="状态"  name="status">
                            <a-switch v-model:checked="editData.data.status" checked-children="启用" checkedValue="0" un-checked-children="禁用" unCheckedValue="1" />
                        </a-form-item>
                    </a-col>
                    <!-- <a-col :span="12">
                        <a-form-item label="联系电话" name="phonenumber">
                            <a-input v-model:value="editData.data.phonenumber" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="邮箱" name="email">
                            <a-input v-model:value="editData.data.email" placeholder="请输入" />
                        </a-form-item>
                    </a-col> -->
                </a-row>
                <a-form-item label="备注" name="remark">
                    <a-textarea allowClear :rows="3" type="textarea" v-model:value="editData.data.remark" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                </a-form-item>
            </a-form>
        </a-modal>
        
        <UserPost ref="userPostRef" @success="updateRow" :deptList="deptList"/>
        
        <a-modal v-model:visible="batchPost.visible" title="批量设置角色" @ok="batchPostSubmit" :width="560">
            <a-form
            ref="postFormRef"
            :model="batchPost.data"
            :label-col="{ style: { width: '90px' } }">
                <a-form-item label="选择部门" required name="deptId">
                    <a-tree-select :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="batchPost.data.deptId"
                        show-search
                        class="w_full"
                        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                        placeholder="请选择公司部门"
                        allow-clear
                        tree-default-expand-all
                        filterTreeNode
                        treeNodeFilterProp="deptName"
                        :field-names="{
                            children: 'children',
                            label: 'deptName',
                            value: 'deptId',
                        }"
                        :tree-data="deptList">
                      </a-tree-select>
                </a-form-item>
                <a-form-item label="选择角色" required name="postId">
                    <a-select :getPopupContainer="trigger => trigger.parentNode"
                      show-search
                      v-model:value="batchPost.data.postId"
                      class="w_full"
                      :options="postList">
                    </a-select>
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
    
</template>
<script setup>
import api               from '@/api/index';
import { handleTree }    from '@/utils/tools';
import { mainStore }     from '@/store';
import moment            from 'moment'
import { message , Modal } from 'ant-design-vue';
import DeptTree    from './components/DeptTree.vue'
import UserPost    from './components/UserPost.vue'

const store = mainStore();

const deptList = ref([]);
const getDept  = ()=>{
    if(deptList.value.length==0){
        api.sys.deptList().then(res=>{
            if(res.code==200){
                deptList.value = handleTree(res.data,"deptId");
            }
        })
    }
}

const postList = ref([]);
const getPost  = ()=>{
    if(postList.value.length==0){
        api.sys.postList().then(res=>{
            if(res.code==200){
                postList.value = res.data.map((item)=>{
                    return {
                        value : item.postId,
                        label : item.postName,
                    }
                });
            }
        })
    }
}
onMounted(() => {
    getPage();
    getDept();
    getPost();
})
const loadding   = ref(false);
const moreFilter = ref(false);
const tableHeight       = ref(0);
const tableReady = (h)=>{
    tableHeight.value = h;
}
const filterForm = reactive({
    pageNo   : 1,
    pageSize : 10,
    
    deptId   : null,
    realname : '',
})
const data = reactive({
    list : [],
    columns:[
        {
            title : '头像',
            key   : 'avatar',
            width : 50,
        },
        {
            title     : '姓名',
            dataIndex : 'realname',
            width     : 250,
        },
        {
            title     : '手机号',
            dataIndex : 'phonenumber',
            width     : 180,
        },
        {
            title     : '所属系统业务架构',
            dataIndex : 'deptName',
            width     : 180,
        },
        {
            title     : '所属公司组织',
            dataIndex : 'mainDeptName',
            width     : 180,
        },
        {
            title : '状态',
            key   : 'status',
            width : 120,
        },
        {
            title     : '备注',
            dataIndex : 'remark',
            width     : 150,
            ellipsis  : true,
        },
        {
            title : '操作',
            key   : 'action',
            width : 160,
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
        content       : filterForm.realname,
        contentColumn : 'realname',
        params        : {}
    }
    if(filterForm.deptId){
        postData.params.deptId = filterForm.deptId;
    }
    loadding.value = true;
    api.sys.userPage(postData).then(res=>{
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
const filterFormRef = ref(null);
const reset         = ()=>{
    filterFormRef.value.resetFields();
    filterSubmit();
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
    editData.data = JSON.parse(JSON.stringify(row));
    if(row.userId){
        let info = await api.sys.userInfo(row.userId);
        if(info.code==200){
            editData.data = info.data;
        }
        editData.data.password = null;
    }else{
        editData.data.status  = '0';
        editData.data.delFlag = '0';
        editData.data.deptId  = filterForm.deptId;
    }
    editData.data.deptId = editData.data.deptId==0?null:editData.data.deptId;
    
    editData.type    = row.userId || -1;
    editData.visible = true;
    store.spinChange(-1);
    try {
        formRef.value.resetFields();
    } catch (e) {
        
    }
}
const editSubmit = ()=>{
    formRef.value.validateFields().then(values=>{
        let apiAttr = editData.type==-1?'userAdd':'userEdit';
        api.sys[apiAttr](editData.data).then(res=>{
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
        content: '是否确认删除"' + row.userName + '"用户？',
        onOk() {
            api.sys.userDel(row.userId).then(res=>{
                if(res.code==200){
                    getPage();
                    message.success('删除成功');
                    
                    let idIndex = batchIds.value.indexOf(row.userId);
                    if(idIndex>-1){
                        batchIds.value.splice(idIndex,1)
                    }
                }
            })
        }
    });
}


const userPostRef = ref(null);
const handleComponent   = (component,row,index)=>{
    if(component=='deptPost'){
        userPostRef.value.open(row,index);
    }
}
const updateRow = (res,index)=>{
    
}

//批量操作
const batchIds = ref([]);
const rowSelection = ref({
    checkStrictly   : false,
    selectedRowKeys : batchIds,
    onChange        : (selectedRowKeys, selectedRows) => {
        batchIds.value = selectedRowKeys;
    },
});

const batchDel = ()=>{
    Modal.confirm({
        title: '操作确认',
        content: '是否确认批量删除已选中用户？',
        onOk() {
            api.sys.userDel(batchIds.value.join(',')).then(res=>{
                if(res.code==200){
                    filterSubmit();
                    message.success('删除成功');
                    batchIds.value = [];
                }
            })
        }
    });
}
const batchPost = reactive({
    visible : false,
    data    : {}
})
const batchPostSet = ()=>{
    batchPost.data.userIds = batchIds.value;
    batchPost.visible      = true;
}
const postFormRef  = ref(null);
const batchPostSubmit = ()=>{
    postFormRef.value.validateFields().then(values=>{
        api.sys.userDeptPostAdd(batchPost.data).then(res=>{
            if(res.code==200){
                message.success('操作成功');
                batchPost.visible = false;
                batchIds.value = [];
            }
        })
    })
}
</script>
<style scoped lang="less">
.left_filter_content{
    flex    : 1;
    display : flex;
    height  : 0;
    .right_content{
        flex           : 1;
        width          : 0;
        height         : 100%;
        display        : flex;
        flex-direction : column;
    }
}
</style>