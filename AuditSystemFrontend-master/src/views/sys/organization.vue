<template>
    <div class="page-common content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a  href="#/index">首页</a></a-breadcrumb-item>
                <a-breadcrumb-item>系统管理</a-breadcrumb-item>
                <a-breadcrumb-item>组织架构</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <div class="content-box_full">
            <Title title="组织架构列表">
            </Title>
            <FullTable :columns="columns" :dataSource="deptList" :defaultExpandedRowKeys="[100]" rowKey="key">
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'levelType'">
                        {{(record.deptType === 'CENG_JI')?'层级':'部门'}}
                    </template>
                    <template v-if="column.key === 'mainDept'">
                        <div class="flex_box">
                            <EllipsisTooltip class="flex_full" :content="mainDeptsStr(record.mainDepts)"/>
                        </div>
                    </template>
                    <template v-if="column.key === 'action'">
                        <a-button type="text" class="color-primary" size="small" @click="edit(record.deptId,record)" v-permission="['system:dept:update']">编辑</a-button>
                        <a-button type="text" v-if="record.level>1" class="color-primary" size="small" @click="handleDelete(record)" v-permission="['system:dept:delete']">删除</a-button>
                        <a-button type="text" class="color-primary" size="small" @click="edit(record.deptId,{})" v-permission="['system:dept:add']">添加子节点</a-button>
                    </template>
                </template>
            </FullTable>
        </div>

        <a-modal v-model:visible="editData.visible" :title="(editData.type==-1?'添加子':'编辑')+'节点'" @ok="editSubmit" :width="660">
            <a-form
            ref="formRef"
            :model="editData.data"
            :label-col="{ style: { width: '120px' } }">
                <a-form-item label="节点名称" required name="deptName">
                    <a-input v-model:value="editData.data.deptName" placeholder="请输入"  show-count :maxlength="30" :disabled="editData.data.level==1"/>
                </a-form-item>
                <a-form-item label="上级节点" required name="parentId" v-if="editData.type==-1 || editData.data.level!=1">
                    <a-tree-select :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="editData.data.parentId"
                        show-search
                        allow-clear
                        style="width: 100%"
                        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
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
                <a-form-item label="排序号" required name="orderNum">
                    <a-input-number v-model:value="editData.data.orderNum" placeholder="请输入序号" :precision="0" :min="0" class="w_full" :disabled="editData.data.level==1"/>
                </a-form-item>
                <a-form-item label="关联公司组织" required name="mainDeptIds">
                    <MainDeptSelect :loadding="dataLoadding" v-model="editData.data.mainDeptIds" :menuList="mainDeptTree" />
                </a-form-item>
                <a-form-item label="所属人力单位组织"  name="hrOrg">  
                    <a-tree-select :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="editData.data.hrOrg"
                        show-search
                        allow-clear
                        style="width: 100%"
                        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                        tree-default-expand-all
                        filterTreeNode
                        treeNodeFilterProp="desc2"
                        :field-names="{     
                        children: 'childs',
                            label: 'desc2',
                            value: 'code',
                        }"
                        :tree-data="mainDeptHrTree">
                    </a-tree-select>
                </a-form-item>
                <a-form-item label="类型" required name="deptType">
                  <a-select v-model:value="editData.data.deptType"  class="w_full"  placeholder="请选择">
                    <a-select-option value="CENG_JI">层级</a-select-option>
                    <a-select-option value="BU_MEN">部门</a-select-option>
                  </a-select>
                </a-form-item>
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
import MainDeptSelect    from './components/MainDeptSelect.vue';
const store = mainStore();

onMounted(() => {
    getDept();
    getHrOrg();
})
const columns = ref([
    {
        title     : '系统业务架构',
        dataIndex : 'deptName',
        width     : 300,
    },
    {
        title : '类型',
        key   : 'levelType',
        width : 100,
    },
    {
        title : '关联公司组织',
        key   : 'mainDept',
        width : 300,
    },
    {
        title     : '创建人',
        dataIndex : 'createBy',
        width     : 150,
    },
    {
        title     : '创建时间',
        dataIndex : 'createTime',
        width     : 180,
    },
    {
        title : '操作',
        key   : 'action',
        width : 220,
        fixed : 'right'
    },
])
const deptList = ref([]);
const getDept = ()=>{
    api.sys.deptList().then(res=>{
        if(res.code==200){
            deptList.value = handleTree(res.data,"deptId");
        }
    })
}
const refreshDeptTree = inject('refreshDeptTree');
const refreshDeptTreeList = inject('refreshDeptTreeList');

//编辑添加
const mainDeptTree = ref([]);
const mainDeptHrTree = ref([]);
const editData     = reactive({
    visible : false,
    type    : -1,
    data    : {},
})
const formRef = ref(null);
const dataLoadding = ref(false);
const edit = async (parentId,row)=>{
    editData.data          = JSON.parse(JSON.stringify(row));
    if(row.deptId){
        // let info = await api.sys.deptInfo(row.deptId);
        // if(info.code==200){
        //     editData.data = info.data;
        // }
        // editData.data.parentId = editData.data.parentId || null;

        editData.data.children    = [];
        editData.data.mainDeptIds = editData.data.mainDepts.map(item=>{
            return item.desc1;
        })
    }else{
        editData.data.parentId = parentId;
        editData.data.delFlag  = 0;
    }
    editData.type    = row.deptId || -1;
    editData.visible = true;
    try {
        formRef.value.resetFields();
    } catch (e) {

    }

    if(mainDeptTree.value.length==0){
        dataLoadding.value = true;
        let info = await api.sys.deptMainTree();
        if(info.code==200){
            let deptArray = [];
            function doArr(arr,parent){
                arr.forEach((item, i) => {
                    let obj = {
                        desc1    : item.desc1,
                        desc2    : item.desc2,
                        // disabled : !item.optional
                    }
                    if(item.childs&&item.childs.length>0){
                        obj.childs = [];
                        if(item.optional){
                            obj.desc1 += '_M'
                            obj.childs.push({
                                desc1    : item.desc1,
                                desc2    : item.desc2,
                            })
                        }
                        doArr(item.childs,obj.childs);
                    }
                    parent.push(obj)
                });
            }
            doArr(info.data || [],deptArray);
            mainDeptTree.value = deptArray;
            dataLoadding.value = false;
        }
    }  
}


const getHrOrg = ()=>{ 
    api.sys.hrOrgTree().then(res => {
        if (res.code == 200) { 
            mainDeptHrTree.value = res.data;
        }
    })  
}

const editSubmit = ()=>{
    formRef.value.validateFields().then(values=>{
        let apiAttr = editData.type==-1?'deptAdd':'deptEdit';
        api.sys[apiAttr](editData.data).then(res=>{
            if(res.code==200){
                getDept();
                message.success('操作成功');
                editData.visible = false;
                refreshDeptTree();
                refreshDeptTreeList();
            }
        })
    })
}
const handleDelete = (row)=>{
    Modal.confirm({
        title: '操作确认',
        content: '是否确认删除名称为"' + row.deptName + '"的节点？',
        onOk() {
            api.sys.deptDel(row.deptId).then(res=>{
                if(res.code==200){
                    getDept();
                    message.success('删除成功');
                }
            })
        }
    });
}

const mainDeptsStr = (depts)=>{
    return (depts || []).map(item=>{
        return item.desc2;
    }).join(',');
}
</script>
<style scoped lang="less">
</style>
