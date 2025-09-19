
<template>
    <a-drawer :maskClosable="false" 
    :width="880"
    :title="userName+'-角色关联'"
    destroyOnClose
    :bodyStyle="{backgroundColor:'#f0f2f5'}"
    @close="handleClose"
    :visible="visible">
        <template #extra>
            <a-space :size="16">
                <a-button size="large" @click="handleClose">关闭</a-button>
            </a-space>
        </template>
        <div class="content-box">
            <Title :title="userName+' - 角色列表'">
                <template #right>
                    <a-button type="primary" @click="edit({})" v-permission="['system:userDeptPost:addDeptPost']">
                        <template #icon>
                            <plus-outlined />
                        </template>
                        添加角色
                    </a-button>
                </template>
            </Title>
            <FullTable :columns="columns" :dataSource="roleList">
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'status'">
                        <a-tag v-if="record.status==0" color="success">启用中</a-tag>
                        <a-tag v-if="record.status==1" color="warning">已禁用</a-tag>
                    </template>
                    <template v-if="column.key === 'action'">
                        <!-- <a-button type="text" class="color-primary" size="small" @click="edit(record)">编辑</a-button> -->
                        <a-button type="text" class="color-primary" size="small" @click="handleDelete(record)" v-permission="['system:userDeptPost:deleteDeptPost']">删除</a-button>
                    </template>
                </template>
            </FullTable>
        </div>
        
    </a-drawer>
    
    <a-modal v-model:visible="editData.visible" :title="userName+'-'+(editData.type==-1?'添加':'编辑')+'角色'" @ok="editSubmit" :width="560">
        <a-form
        ref="formRef"
        :model="editData.data"
        :label-col="{ style: { width: '90px' } }">
            <a-form-item label="选择组织">
                <a-tree-select :getPopupContainer="trigger => trigger.parentNode"
                    v-model:value="editData.data.deptId"
                    show-search
                    class="w_full"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    placeholder="请选择系统组织"
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
            <a-form-item label="选择角色">
                <a-select :getPopupContainer="trigger => trigger.parentNode"
                  show-search
                  v-model:value="editData.data.postId"
                  class="w_full"
                  :options="postList">
                </a-select>
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script setup>
    import api         from '@/api/index';
    import { message,Modal } from 'ant-design-vue';
    import { handleTree }    from '@/utils/tools';
    const emit     = defineEmits(['success'])
    const props = defineProps({
        deptList:{
            type    : Array,
            default : [],
        }
    })
    const visible  = ref(false);
    
    const columns = ref([
        {
            title     : '部门',
            dataIndex : 'deptName',
            width     : 220,
        },
        {
            title     : '角色数据权限',
            dataIndex : 'postName',
            width     : 220,
        },
        {
            title : '操作',
            key   : 'action',
            width : 60,
            fixed : 'right'
        },
    ])
    const userId      = ref(null);
    const userName    = ref('');
    const deptId      = ref(null);
    const handleClose = ()=>{
        visible.value = false;
    }
    const open = (data,index)=>{
        userId.value   = data.userId;
        deptId.value   = data.deptId;
        userName.value = data.realname || data.nickName;
        visible.value  = true;
        getRoleList();
        getPost();
        // getDept();
    }
    defineExpose({open})
    
    const roleList    = ref([]);
    const getRoleList = ()=>{
        api.sys.userDeptPostList(userId.value).then(res=>{
            if(res.code==200){
                roleList.value = res.data;
            }
        })
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
    
    // const deptList = ref([]);
    // const getDept  = ()=>{
    //     if(deptList.value.length==0){
    //         api.sys.deptList().then(res=>{
    //             if(res.code==200){
    //                 deptList.value = handleTree(res.data,"deptId");
    //             }
    //         })
    //     }
    // }
    
    
    //编辑添加
    const editData = reactive({
        visible : false,
        type    : -1,
        data    : {},
    })
    const formRef  = ref(null);
    const menuList = ref([]);
    const edit     = (row)=>{
        editData.type    = row.userId || -1;
        editData.data = {
            deptId  : row.deptId || (deptId.value || null),
            postId  : row.postId || null,
        }
        if(editData.type==-1){
            editData.data.userIds = [userId.value];
        }else{
            editData.data.userId = userId.value;
        }
        editData.visible = true;
        try {
            formRef.value.resetFields();
        } catch (e) {
            
        }
    }
    const editSubmit = ()=>{
        formRef.value.validateFields().then(values=>{
            let apiAttr = editData.type==-1?'userDeptPostAdd':'userDeptPostEdit';
            api.sys[apiAttr](editData.data).then(res=>{
                if(res.code==200){
                    getRoleList();
                    message.success('操作成功');
                    editData.visible = false;
                }
            })
        })
    }
    const handleDelete = (row)=>{
        Modal.confirm({
            title: '操作确认',
            content: '是否确认删除该角色？',
            onOk() {
                let postData = {
                    userIds : [userId.value],
                    deptId  : row.deptId,
                    postId  : row.postId,
                }
                api.sys.userDeptPostDel(postData).then(res=>{
                    if(res.code==200){
                        getRoleList();
                        message.success('删除成功');
                    }
                })
            }
        });
    }
</script>
<style scoped lang="less">
.content-box{
    height         : 100%;
    display        : flex;
    flex-direction : column;
}
</style>
