<template>
    <div class="page-common content-inner">
        <div class="bread_box">
            <a-breadcrumb>
                <a-breadcrumb-item><a  href="#/index">首页</a></a-breadcrumb-item>
                <a-breadcrumb-item>系统管理</a-breadcrumb-item>
                <a-breadcrumb-item>规则管理</a-breadcrumb-item>
            </a-breadcrumb>
        </div>
        <div class="filter-box">
            <a-form :label-col="{ style: { width: '100px' } }" ref="filterFormRef" :model="filterForm">
                <a-row>
                    <a-col :span="8">
                        <a-form-item label="规则名称" name="ruleName">
                            <a-input allowClear v-model:value="filterForm.ruleName" placeholder="请输入" />
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
            <Title title="规则列表">
                <template #right>
                    <a-button type="primary" @click="edit(-1,{})" v-permission="['system:rules:add']">
                        <template #icon>
                            <plus-outlined />
                        </template>
                        添加规则
                    </a-button>
                </template>
            </Title>
            <FullTable :loadding="loadding" :columns="data.columns" :dataSource="data.list">  
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'ruleStatus'">
                        <span  v-if="record.ruleStatus==1" class="color-success">启用中</span>
                        <span  v-if="record.ruleStatus==0" class="color-gray">已禁用</span>
                    </template>
                    <template v-if="column.key === 'action'">
                        <a-button type="text" class="color-primary" size="small" @click="edit(record.id,record)" v-permission="['system:rules:update']">编辑</a-button>
                        <a-button type="text" class="color-primary" size="small" @click="handleDelete(record)" v-permission="['system:rules:delete']">删除</a-button>
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
        
        <a-drawer :maskClosable="false"  
            :visible="editData.visible" 
            :title="(editData.type==-1?'添加':'编辑')+'规则'" 
            @close="editClose"
            :width="960">
            <template #extra>
                <a-space :size="16">
                    <a-button size="large" @click="editClose">取消</a-button>
                    <a-button size="large" type="primary" @click="editSubmit" :loading="submitLoading">保存</a-button>
                </a-space>
            </template>
            <div class="drawer_content">
                <a-form
                ref="formRef"
                layout="vertical"
                :model="editData.data">
                    <Title class="titlebar">
                        <template #title>
                            基础配置
                            <span class="color-danger"> * </span>
                        </template>
                    </Title>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item required label="规则名称" name="ruleName">
                                <a-input allowClear v-model:value="editData.data.ruleName"/>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item required label="规则对象类型" name="modeName">
                                <a-select :getPopupContainer="trigger => trigger.parentNode"
                                  v-model:value="editData.data.modeName"
                                  class="w_full"
                                  placeholder="请选择"
                                  @change="change"
                                  :options="ruleDict">
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item required label="规则优先级" name="ruleLevel">
                                <a-input-number v-model:value="editData.data.ruleLevel" placeholder="请输入数字，1为最高优先级" :min="0" class="w_full" />
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="状态" name="ruleStatus">
                                <a-switch v-model:checked="editData.data.ruleStatus"  checked-children="启用" :checkedValue="1" un-checked-children="禁用" :unCheckedValue="0" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-divider />
                    <Title class="titlebar">
                        <template #title>
                            触发条件
                            <span class="color-danger"> * </span>
                        </template>
                        <template #right>
                            <a-radio-group v-model:value="editData.data.conditionType">
                                <a-radio-button :value="0">符合全部条件</a-radio-button>
                                <a-radio-button :value="1">符合任意条件</a-radio-button>
                            </a-radio-group>
                        </template>
                    </Title>
                    <ConditionList 
                      v-model="editData.data.conditionList" 
                      v-model:validateField="validateFieldCondition"
                      :ruleDict="ruleDictCondition" :modeName="editData.data.modeName"/>
                    <a-divider />
                    <Title class="titlebar">
                        <template #title>
                            动作
                            <span class="color-danger"> * </span>
                        </template>
                    </Title>
                    <ActionList 
                    v-model="editData.data.actionList" 
                    v-model:validateField="validateFieldAction"
                    :ruleValue="ruleDict.value" :ruleDict="ruleDictAction" :modeName="editData.data.modeName"/>
                </a-form>
            </div>
        </a-drawer>
    </div>
    
</template>
<script setup>
import api               from '@/api/index';
import { message,Modal } from 'ant-design-vue';
import { mainStore }     from '@/store';
import { useDictStore }  from '@/store/dict';
import ConditionList     from './components/ConditionList.vue';
import ActionList        from './components/ActionList.vue';
const dict  = useDictStore();
const store = mainStore();

onMounted(() => {
    getPage();
})
const loadding    = ref(false);
const filterForm = reactive({
    pageNo     : 1,
    pageSize   : 10,
    ruleName : '',
})
const data = reactive({
    list    : [],
    columns : [
        {
            title     : '规则名称',
            dataIndex : 'ruleName',
            width     : 220,
        },
        {
            title       : '优先级',
            dataIndex   : 'ruleLevel',
            width       : 50,
        },
        {
            title : '状态',
            key   : 'ruleStatus',
            width : 120,
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
            width : 120,
            fixed : 'right',
        },
    ],
    total : 0,
})
const getPage = ()=>{
    let postData = {
        desc          : ['ruleLevel'],
        pageNo        : filterForm.pageNo,
        pageSize      : filterForm.pageSize,
        content       : filterForm.ruleName,
        contentColumn : 'ruleName',
        params        : {}
    }
    loadding.value = true;
    api.common.rulesPage(postData).then(res=>{
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
const submitLoading = ref(false);
const formRef       = ref(null);
const edit          = async (parentId,row)=>{
    store.spinChange(1);
    editData.data    = JSON.parse(JSON.stringify(row));
    if(row.id){
        
        // let info = await api.common.rulesInfo(row.id);
        // if(info.code==200){
        //     editData.data = info.data;
        // }
    }else{
        editData.data.ruleStatus    = 1;
        editData.data.conditionType = 0;
    }
    editData.data.conditionList = editData.data.condition?JSON.parse(editData.data.condition):[];
    editData.data.actionList    = editData.data.action?JSON.parse(editData.data.action):[];
    
    editData.type    = row.id || -1;
    
    getDict(()=>{
        editData.visible = true;
        if(editData.type==-1&&ruleDict.value.length==1){
            editData.data.modeName = ruleDict.value[0].value;
        }
        store.spinChange(-1);
        try {
            formRef.value.resetFields();
        } catch (e) {
            
        }
    });
    
}
const validateFieldCondition = ref(true) //触发条件表单验证
const validateFieldAction = ref(true) //动作表单验证
const editSubmit = ()=>{
  console.log(editData.data,'22')
    formRef.value.validateFields().then(values=>{
        if(!validateFieldCondition.value) return message.warning('请完善表单信息')
        if(!validateFieldAction.value) return message.warning('请完善表单信息!')
        submitLoading.value       = true;
        let apiAttr               = editData.type==-1?'rulesAdd':'rulesEdit';
        api.common[apiAttr](editData.data).then(res=>{
            if(res.code==200){
                getPage();
                message.success('操作成功');
                editData.visible    = false;
            }
            submitLoading.value = false;
        })
    })
}
const editClose = ()=>{
    editData.visible = false;
}
const handleDelete = (row)=>{
    Modal.confirm({
        title   : '操作确认',
        content : '是否确认删除"' + row.ruleName + '"？',
        onOk() {
            api.common.rulesDel(row.id).then(res=>{
                if(res.code==200){
                    getPage();
                    message.success('删除成功');
                }
            })
        }
    });
}

const ruleDict          = ref([]);
const ruleDictAction    = ref({});
const ruleDictCondition = ref({});
const getDict           = (callBack)=>{
    if(ruleDict.value.length>0){
        if(callBack){callBack()}
        return;
    }
    api.common.rulesEnums().then(res=>{
        if(res.code==200){
            res.data.condition.forEach((item, i) => {
                ruleDict.value.push({
                    label  : item.name,
                    value  : item.code,
                    status : item.status
                })
            });
            //tree 数据扁平对象化
            // if(modeName.value == 'XIANG_MU'){
            //     var newStr = res.data.action.filter(o=>{
            //         if(o.code != 'XIAO_XI_TONG_ZHI_TOUTUO') return o
            //     })
            // }else{
            //     var newStr = res.data.action.filter(o=>{
            //         if(o.code == 'XIAO_XI_TONG_ZHI_TOUTUO') return o
            //     })
            // }
            console.log('newStr',res.data.action)
            ruleDictAction.value    = dictDispose(res.data.action || [],'GUI_ZE_GUAN_LI_DONG_ZUO');
            ruleDictCondition.value = dictDispose(res.data.condition || []);
        }
        if(callBack){callBack()}1
    })
}
const dictDispose = (arr,enums)=>{
    
    let obj = {}
    if(enums){
        obj[enums] = [];
    }
    function doArr(a,key){
        
        a.forEach((item, i) => {
            
            if(key){
                obj[key].push({
                    label  : item.name,
                    value  : item.code,
                    status : item.status,
                    groupName : item.groupName
                })
            }
            if(item.children&&item.children.length>0){
                obj[item.code] = [];
                doArr(item.children,item.code);
            }
        });
    }
    doArr(arr,enums);
    return obj;
}
const modeName       = ref('');
const change = (val,option)=>{
    editData.data.modeName = val
    console.log(editData.data.modeName)
}
</script>
<style scoped lang="less">
.drawer_content{
    margin-top: -16px;
    .titlebar{
        margin : 16px -16px;
        &:first-child{
            margin-top : 0;
        }
    }
    :deep(.ant-collapse-header){
        padding-top    : 16px!important;
        padding-bottom : 16px!important;
    }
}
</style>