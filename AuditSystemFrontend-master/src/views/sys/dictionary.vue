<template>
    <div class="page-common content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a  href="#/index">首页</a></a-breadcrumb-item>
                <a-breadcrumb-item>系统管理</a-breadcrumb-item>
                <a-breadcrumb-item>字典管理</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <div class="filter-box">
            <a-form :label-col="{ style: { width: '100px' } }" ref="filterFormRef" :model="filterForm">
                <a-row>
                    <a-col :span="8">
                        <a-form-item label="字典名称" name="dictName">
                            <a-input allowClear v-model:value="filterForm.dictName" placeholder="请输入" />
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
            <a-row>
                <a-col :span="12">
                  <Title title="字典列表" style="padding-bottom: 10px;">
                  <template #right>
                        <a-button type="primary" @click="edit(0,{})" v-permission="['system:dictionary:add']">
                            <template #icon>
                                <plus-outlined />
                            </template>
                            新增字典
                        </a-button>
                    </template>
                </Title>
               
                  <a-table  rowKey="dictId" :pagination="true"  :row-selection="rowSelection"  :columns="columns" :dataSource="typeList" :scroll="{x:'100%',y:tableHeight}" >
                    <!-- <template #bodyCell="{ column,record,index }">
                        <template v-if="column.key === 'dictName'">
                            <div  @click="hanleClick(record,index)">{{record.dictName}}</div>
                        </template>
                        <template v-if="column.key === 'dictType'">
                            <div  @click="hanleClick(record,index)">{{record.dictType}}</div>
                        </template>
                        <template v-if="column.key === 'remark'">
                            <div  @click="hanleClick(record,index)">{{record.remark}}</div>
                        </template>
                    </template> -->
                </a-table>
              
                </a-col>
                <a-col :span="12">
                <Title title="" :style="{paddingBottom:hasPermission('system:dictionary:add') ? '10px':'30px'}" >
                    <template #right>
                        <a-button type="primary" @click="edit(1,{})" v-permission="['system:dictionary:add']">
                            <template #icon>
                                <plus-outlined />
                            </template>
                            新增枚举
                        </a-button>
                    </template>
                </Title>
              
                  <a-table  rowKey="postId" :pagination="true"   :columns="columns2" :dataSource="dataList" :scroll="{x:'100%',y:tableHeight}" >
                  </a-table>
              
                </a-col>
            </a-row>
        </div>
        
        <a-modal v-model:visible="editData.visible" :title="(editData.type==-1?'新增':'编辑')+'枚举'" @ok="editSubmit" :width="500">
            <a-form
            ref="formRef"
            :model="editData.data"
            :label-col="{ style: { width: '90px' } }">
                <a-row :gutter="16">
                    <a-col :span="24">
                        <a-form-item label="枚举名称" required name="dictLabel">
                            <a-input v-model:value="editData.data.dictLabel" placeholder="请输入" show-count :maxlength="30" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-item label="枚举值" required name="dictValue">
                            <a-input v-model:value="editData.data.dictValue" placeholder="请输入" show-count :maxlength="30" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-item label="枚举类型">
                            <a-input v-model:value="editData.data.dictType" placeholder="请输入" disabled show-count :maxlength="30" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-item label="排序" required name="dictSort">
                            <a-input v-model:value="editData.data.dictSort" placeholder="请输入" show-count :maxlength="30" />
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
        </a-modal>
        <a-modal v-model:visible="dictVisible" title="新增字典" @ok="DictSubmit" :width="500">
            <a-form
            ref="formRef"
            :model="dictData"
            :label-col="{ style: { width: '90px' } }">
                 <a-row :gutter="16">
                    <a-col :span="24">
                        <a-form-item label="字典名称" required name="dictName">
                            <a-input v-model:value="dictData.dictName" placeholder="请输入" show-count :maxlength="30" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-item label="字典键值" required name="dictType">
                            <a-input v-model:value="dictData.dictType" placeholder="请输入" show-count :maxlength="30" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-item label="备注">
                            <a-input v-model:value="dictData.remark" placeholder="请输入"  show-count :maxlength="30" />
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
import moment            from 'moment'
import { message,Modal } from 'ant-design-vue';
import {hasPermission} from '@/utils/tools';
import { ref } from "vue";
const store = mainStore();
const tableHeight       = ref(( window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight)-422);
onMounted(() => {
    getPost();
})
const columns = ref([
    {
        title     : '字典名称',
        dataIndex : 'dictName',
        width : 180,
    },
    {
        title : '字典键值',
        dataIndex   : 'dictType',
        width : 120,
    },
    {
        title     : '备注',
        dataIndex : 'remark',
        width     : 180,
    }
])
const columns2 = ref([
    {
        title     : '枚举名称',
        dataIndex : 'dictLabel',
        width : 180,
    },
    {
        title     : '枚举值',
        dataIndex : 'dictValue',
        width : 180,
    },
    {
        title : '排序',
        dataIndex   : 'dictSort',
        width : 120,
    }
])
const filterForm = reactive({
    dictName :''
})
const dictVisible = ref(false)
const dictData = ref({}) 
const typeList      = ref([]);
const dataList      = ref([]);
const filterFormRef = ref(null);
const getPost       = ()=>{
    api.sys.getEditTypeList(filterForm.dictName).then(res=>{
        console.log(filterForm.dictName)
        if(res.code==200){
            typeList.value = res.data
        }
    })
}
const getData       = (dictType)=>{
    api.sys.getDataListByType(dictType?dictType:editData.data.dictType).then(res=>{
        if(res.code==200){
            dataList.value = res.data
        }
    })
}
const filterSubmit = ()=>{
    getPost();
}
const reset = ()=>{
    filterFormRef.value.resetFields();
    filterSubmit();
}

const dataPermissions = ref([]);

//编辑添加
const editData = reactive({
    visible : false,
    type    : -1,
    data    : {
    },
})
const formRef  = ref(null);
const rowSelection=computed(()=>{
    return{
	        type:'radio', //设置成单选按钮
            onChange : (selectedRowKeys, selectedRows) => {
                console.log(selectedRows[0].dictType)
                editData.data.dictType = selectedRows[0].dictType
                getData(selectedRows[0].dictType)
        },
    }
    
})
const edit     =  (type)=>{
   if(type){
    editData.visible = true;
   }else{
    dictVisible.value = true
   }
    try {
        formRef.value.resetFields();
    } catch (e) {
        
    }
}
//枚举
const editSubmit = ()=>{
    formRef.value.validateFields().then(values=>{
            let apiAttr ='addDataInfo';
            api.sys[apiAttr](editData.data).then(res=>{
                if(res.data==''){
                    getData();
                    message.success('操作成功');
                    editData.visible = false;
                }else{
                    message.error(res.data);
                }
            })
    })
}
//字典
const  DictSubmit = ()=>{
    formRef.value.validateFields().then(values=>{
            let apiAttr ='addDictType';
            api.sys[apiAttr](dictData.value).then(res=>{
                if(res.code==200){
                    getPost()
                    message.success('操作成功');
                    dictVisible.value = false;
                }else{
                    message.error(res.data);
                }
            })
    })
}

</script>
<style scoped lang="less">

</style>