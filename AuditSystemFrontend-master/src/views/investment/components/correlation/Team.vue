
<template>
    <a-form
    ref="formRef"
    layout="vertical"
    :model="editData">
        <a-table :columns="columns" :data-source="list" :pagination="false" :scroll="{ x: '100%' }">
            <template #headerCell="{ column }">
                <template v-if="column.required">
                    {{column.title}}
                    <span class="color-danger" v-if="!readOnly">*</span>
                </template>
            </template>
            <template #emptyText>
                暂无数据，请添加
            </template>
            <template #bodyCell="{ column, text, record,index}">
                <a-form-item :required="column.required" :name="editData[index]?[index,column.editType]:''" v-if="column.editType=='userId'">
                    <UserSelect v-if="editData[index]" v-model="editData[index][column.editType]" @change="userChange($event,editData[index])" :users="editData[index].user"/>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.editType]:''" v-if="column.editType=='deptName'">
                    <a-input disabled v-if="editData[index]" v-model:value="editData[index][column.editType]" />
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.editType]:''" v-if="column.editType=='roleType'">
                    <a-select
                        v-if="editData[index]"
                        v-model:value="editData[index][column.editType]"
                        @change="()=>{
                            editData[index].roleKey = null;
                        }"
                        class="w_full"
                        placeholder="请选择"
                       :options="roleTypeDict">
                    </a-select>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.editType]:''" v-if="column.editType=='roleKey'">
                    <a-select
                        v-if="editData[index]"
                        v-model:value="editData[index][column.editType]"
                        class="w_full"
                        placeholder="请选择"
                       :options="dict.options(editData[index].roleType)">
                    </a-select>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <template v-if="column.key === 'action'">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="edit(record,index)" v-permissionInvestment="['biz:projectCompany:edit']">编辑</a-button>
                        <a-button type="text" class="color-primary" v-if="!readOnly && (type!='TUO' || list.length>1)" size="small" @click="del(record,index)" v-permissionInvestment="['biz:projectCompany:delete']">删除</a-button>
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="clone(record,index)" v-permissionInvestment="['biz:projectCompany:add']">克隆</a-button>
                    </template>
                    <a-space v-else>
                        <a-button type="primary" shape="circle" @click="editSubmit(record,index)" size="small">
                            <template #icon><check-outlined /></template>
                        </a-button>
                        <a-button shape="circle" @click="editCancel(record,index)" size="small">
                            <template #icon><close-outlined /></template>
                        </a-button>
                    </a-space>
                </template>
            </template>
            <template #summary v-if="!readOnly&&addVisible">
                <a-table-summary>
                    <a-table-summary-row>
                        <template v-for="(item,index) in columns" :key="index">
                            <a-table-summary-cell v-if="item.key != 'action'">
                                <a-form-item :required="item.required" :name="['add',item.editType]">
                                    <UserSelect v-if="item.editType=='userId'" v-model="editData['add'][item.editType]"  @change="userChange($event,editData['add'])"  :users="editData['add'].user"/>
                                    <a-input disabled v-if="item.editType=='deptName'" v-model:value="editData['add'][item.editType]" />
                                    <a-select
                                        v-if="item.editType=='roleType'"
                                        v-model:value="editData['add'][item.editType]"
                                        class="w_full"
                                        @change="()=>{
                                            editData['add'].roleKey = null;
                                        }"
                                        placeholder="请选择"
                                       :options="roleTypeDict">
                                    </a-select>
                                    <a-select
                                        v-if="item.editType=='roleKey'"
                                        v-model:value="editData['add'][item.editType]"
                                        class="w_full"
                                        placeholder="请选择"
                                       :options="dict.options(editData['add'].roleType)">
                                    </a-select>
                                </a-form-item>
                            </a-table-summary-cell>
                        </template>
                        <a-table-summary-cell :index="3">
                            <a-space>
                                <a-button type="primary" shape="circle" @click="addSubmit" size="small">
                                    <template #icon><check-outlined /></template>
                                </a-button>
                                <a-button shape="circle" @click="addCancel" size="small">
                                    <template #icon><close-outlined /></template>
                                </a-button>
                            </a-space>
                        </a-table-summary-cell>
                    </a-table-summary-row>
                </a-table-summary>
            </template>
        </a-table>
    </a-form>
    <div class="add_btn" @click="addVisible=true" v-if="!readOnly&&!addVisible" v-permissionInvestment="['biz:projectCompany:add']">
        <plus-circle-outlined style="margin-right:8px;"/>
        新增
    </div>
</template>
<script setup>
import api                from '@/api/index';
import { useCorrelation } from '@/views/project/components/correlation/correlation';
import { useDictStore }   from '@/store/dict';
import { watchArray }     from '@vueuse/core'
const dict  = useDictStore();
const emit  = defineEmits(['update:modelValue']);
const props = defineProps({
    modelValue : {
        type    : String,
        default : null,
    },
    projectId : Number,
    readOnly : {
        type    : Boolean,
        default : false,
    },
    type : {
        type    : String,
        default : 'TOU',
    },
})
const {
    formRef,
    list,
    getList,
    addVisible,
    addSubmit,
    addCancel,
    editData,
    edit,
    editSubmit,
    editCancel,
    del,
    clone
} = useCorrelation(props.projectId,'projectTeam');
watchArray(list, (newList) => {
    emit('update:modelValue',newList.length>0?'finished':null);
},{immediate:true,deep: true})

const roleTypeDict = computed(()=>{
    return dict.options('XIANG_MU_JUE_SE_LEI_XING').filter(item=>{
        return item.value.startsWith(props.type);
    })
})
const columns = [
    {
        title     : '成员名称',
        dataIndex : ['user','realname'],
        required  : true,
        editType  : 'userId'
    },
    {
        title     : '部门',
        dataIndex : 'deptName',
        editType  : 'deptName'
    },
    {
        title     : '分类',
        dataIndex : 'roleTypeStr',
        editType  : 'roleType'
    },
    {
        title     : '角色',
        dataIndex : 'roleKeyStr',
        editType  : 'roleKey'
    },
]
const userChange = (dept,obj)=>{
    if(dept&&dept.deptId){
        obj.deptId   = dept.deptId;
        obj.deptName = dept.deptName;
    }
    // api.common.getUser(dept.deptId).then(res=>{
    //     if(res.code==200){
    //
    //     }
    // })
}
onMounted(() => {
    if(!props.readOnly){
        columns.push(
            {
                title : '操作',
                key   : 'action',
                width : 180,
                fixed : 'right'
            }
        )
    }
    getList();
})
</script>
<style scoped lang="less">
.add_btn{
    display         : flex;
    align-items     : center;
    justify-content : center;
    font-size       : 16px;
    cursor          : pointer;
    margin-top      : 8px;
    height          : 48px;
    // box-shadow      : 0 0 8px rgb(0 21 41 / 8%);
    border:1px solid #eee;
    border-radius   : 4px;

    &:hover{
        color            : @primary-color;
        background-color : #fffaf0;
    }
}
</style>
