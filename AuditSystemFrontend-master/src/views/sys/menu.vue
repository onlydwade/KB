<template>
    <div class="page-common content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a  href="#/index">首页</a></a-breadcrumb-item>
                <a-breadcrumb-item>系统管理</a-breadcrumb-item>
                <a-breadcrumb-item>菜单管理</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <div class="content-box_full">
            <Title title="菜单管理">
                <template #right>
                    <a-button type="primary" @click="edit(null,{})" v-permission="['system:menu:add']">
                        <template #icon>
                            <plus-outlined />
                        </template>
                        添加主菜单
                    </a-button>
                </template>
            </Title>
            <FullTable :columns="columns" :dataSource="menuList" rowKey="key">
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'menuType'">
                        <a-tag v-if="record.menuType=='M'" color="success">目录</a-tag>
                        <a-tag v-if="record.menuType=='C'" color="warning">菜单</a-tag>
                        <a-tag v-if="record.menuType=='F'" color="error">权限</a-tag>
                    </template>
                    <template v-if="column.key === 'action'">
                        <a-button type="text" class="color-primary" size="small" @click="edit(record.menuId,{})" v-permission="['system:menu:add']">添加子菜单</a-button>
                        <a-button type="text" class="color-primary" size="small" @click="edit(record.menuId,record)" v-permission="['system:menu:update']">编辑</a-button>
                        <a-button type="text" class="color-primary" size="small" v-if="(record.canDel == 0)" @click="handleDelete(record)" v-permission="['system:menu:delete']">删除</a-button>
                    </template>
                </template>
            </FullTable>
        </div>
        
        <a-modal v-model:visible="editData.visible" :title="(editData.type==-1?'添加':'编辑')+'菜单'" @ok="editSubmit" :width="760">
            <a-form
            ref="formRef"
            :model="editData.data"
            :label-col="{ style: { width: '90px' } }">
                <a-row :gutter="16">
                    <a-col :span="12">
                        <a-form-item label="上级菜单">
                            <a-tree-select :getPopupContainer="trigger => trigger.parentNode"
                                v-model:value="editData.data.parentId"
                                show-search
                                style="width: 100%"
                                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                                placeholder="不选择表示主目录"
                                allow-clear
                                tree-default-expand-all
                                :field-names="{
                                    children: 'children',
                                    label: 'menuName',
                                    value: 'menuId',
                                }"
                                :tree-data="menuList">
                              </a-tree-select>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="菜单类型" required  name="menuType">
                            <a-radio-group v-model:value="editData.data.menuType">
                                <a-radio value="M">目录</a-radio>
                                <a-radio value="C">菜单</a-radio>
                                <a-radio value="F">权限</a-radio>
                            </a-radio-group>
                        </a-form-item>
                    </a-col>
                    
                    <a-col :span="12">
                        <a-form-item label="菜单名称" required name="menuName">
                            <a-input v-model:value="editData.data.menuName" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="图标名称">
                            <a-input v-model:value="editData.data.icon" :disabled="!!editData.data.parentId&&editData.data.parentId>0" placeholder="参考图标库输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="路由地址">
                            <a-input v-model:value="editData.data.path" :disabled="editData.data.menuType=='F'" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="组件路径">
                            <a-input v-model:value="editData.data.component" :disabled="editData.data.menuType!='C'" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="权限标识">
                            <a-input v-model:value="editData.data.perms" :disabled="editData.data.menuType!='F'" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="排序号" required name="orderNum">
                            <a-input-number v-model:value="editData.data.orderNum" placeholder="请输入序号" :precision="0" :min="0" class="w_full"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12" v-if="editData.data.menuType!='F'">
                        <a-form-item label="是否展示" name="visible">
                            <a-switch v-model:checked="editData.data.visible" checked-children="展示" checkedValue="0" un-checked-children="隐藏" unCheckedValue="1" />
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
        </a-modal>
    </div>
    
</template>
<script setup>
import api               from '@/api/index';
import { mainStore }     from '@/store';
import { handleTree }    from '@/utils/tools';
import moment            from 'moment'
import { message,Modal } from 'ant-design-vue';
const store = mainStore();

onMounted(() => {
    getMenu();
})
const columns = ref([
    {
        title     : '菜单名称',
        dataIndex : 'menuName',
        width     : 220,
    },
    {
        title : '类型',
        key   : 'menuType',
        width : 60,
    },
    {
        title     : '序号',
        dataIndex : 'orderNum',
        width     : 80,
    },
    // {
    //     title     : '路由地址',
    //     dataIndex : 'path',
    //     width     : 180,
    // },
    // {
    //     title     : '组件路径',
    //     dataIndex : 'component',
    //     width     : 180,
    // },
    {
        title     : '权限标识',
        dataIndex : 'perms',
        width     : 120,
    },
    // {
    //     title        : '创建时间',
    //     dataIndex    : 'createTime',
    //     width        : 180,
    // },
    {
        title : '操作',
        key   : 'action',
        width : 210,
        fixed : 'right'
    },
])
const menuList = ref([]);
const getMenu = ()=>{
    api.sys.menuList().then(res=>{
        if(res.code==200){
            menuList.value = handleTree(res.data,"menuId");
        }
    })
}

//编辑添加
const editData = reactive({
    visible : false,
    type    : -1,
    data    : {},
})
const formRef = ref(null);
const edit = async (parentId,row)=>{
    editData.data = JSON.parse(JSON.stringify(row));
    if(row.menuId){
        let info = await api.sys.menuInfo(row.menuId);
        if(info.code==200){
            editData.data = info.data;
        }
        editData.data.parentId = editData.data.parentId || null;
    }else{
        editData.data.menuType = 'C';
        editData.data.parentId = parentId;
        editData.data.isFrame  = 1;
        editData.data.visible  = '0';
    }
    editData.type    = row.menuId || -1;
    editData.visible = true;
    try {
        formRef.value.resetFields();
    } catch (e) {
        
    }
}
const editSubmit = ()=>{
    formRef.value.validateFields().then(values=>{
        let apiAttr = editData.type==-1?'menuAdd':'menuEdit';
        api.sys[apiAttr](editData.data).then(res=>{
            if(res.code==200){
                getMenu();
                message.success('操作成功');
                editData.visible = false;
            }
        })
    })
}
const handleDelete = (row)=>{
    Modal.confirm({
        title   : '操作确认',
        content : '是否确认删除名称为"' + row.menuName + '"的数据项？',
        onOk() {
            api.sys.menuDel(row.menuId).then(res=>{
                if(res.code==200){
                    getMenu();
                    message.success('删除成功');
                }
            })
        }
    });
}
</script>
<style scoped lang="less">
</style>