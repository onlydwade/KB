
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
                <a-form-item :required="column.required" :name="editData[index]?[index,column.editType]:''" v-if="column.editType=='personnelAttribution'">
                    <a-select
                        v-if="editData[index]"
                        v-model:value="editData[index][column.editType]"
                        class="w_full"
                        @change="()=>{
                            editData[index]['name'] = ''
                            editData[index]['attributionCompany'] = ''
                        }"
                        placeholder="请选择"
                       :options="dict.options('REN_YUAN_GUI_SHU')">
                    </a-select>
                    <template v-else>
                        {{text}}
                    </template>
                </a-form-item>

                <a-form-item :required="column.required" :name="editData[index]?[index,column.editType]:''" v-if="column.editType=='position'">
                    <a-select
                        v-if="editData[index]"
                        v-model:value="editData[index][column.editType]"
                        class="w_full"
                        placeholder="请选择"
                       :options="dict.options('DONG_JIAN_GAO_ZHI_WEI')">
                    </a-select>
                    <template v-else>
                        {{text}}
                    </template>
                </a-form-item>

                <a-form-item :required="column.required" :name="editData[index]?[index,column.editType]:''" v-if="column.editType=='name'">
                    <template v-if="editData[index]">
                        <UserNameSelect v-if="editData[index]['personnelAttribution']=='BEN_GONG_SI'" v-model="editData[index][column.editType]"/>
                        <a-input v-else allowClear v-model:value="editData[index][column.editType]" placeholder="请输入"/>
                    </template>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='text'">
                    <a-input v-if="editData[index]" :disabled="column.dataIndex=='attributionCompany' && editData[index]['personnelAttribution'] =='BEN_GONG_SI' " allowClear v-model:value="editData[index][column.dataIndex]" placeholder="请输入"/>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>

                <template v-if="column.key === 'documentTemplateList'">
                    <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                        <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                    </template>
                </template>
                <template v-if="column.key === 'action'">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="edit(record,index)" v-permissionInvestment="['biz:projectCompany:edit']">编辑</a-button>
                        <UploadBtn v-if="!readOnly&&tempId!=0" @change="uploadChange($event,record.id)">
                            <a-button type="text" class="color-primary" size="small" v-permissionInvestment="['biz:projectCompany:edit']">上传文件</a-button>
                        </UploadBtn>
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="del(record,index)" v-permissionInvestment="['biz:projectCompany:delete']">删除</a-button>

                        <a-button type="text" class="color-primary" v-if="!readOnly&&tempId==0" size="small" @click="clone(record,index)" v-permissionInvestment="['biz:projectCompany:add']">克隆</a-button>
                    </template>
                    <a-space v-else>
                        <a-button type="primary" shape="circle" @click="submitReg(index,record)" size="small">
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
                            <a-table-summary-cell v-if="item.key != 'action' && item.colSpan!=0">
                                <a-form-item :required="item.required" :name="['add',['personnelAttribution','position'].includes(item.editType)?item.editType:item.dataIndex]">
                                    <a-select
                                        v-if="item.editType=='personnelAttribution'"
                                        v-model:value="editData['add'][item.editType]"
                                        class="w_full"
                                        @change="()=>{
                                            editData['add']['name'] = ''
                                            editData['add']['attributionCompany'] = ''
                                        }"
                                        placeholder="请选择"
                                       :options="dict.options('REN_YUAN_GUI_SHU')">
                                    </a-select>
                                    <a-select
                                        v-if="item.editType=='position'"
                                        v-model:value="editData['add'][item.editType]"
                                        class="w_full"
                                        placeholder="请选择"
                                       :options="dict.options('DONG_JIAN_GAO_ZHI_WEI')">
                                    </a-select>

                                    <template v-if="item.editType=='name'">
                                        <UserNameSelect v-if="editData['add']['personnelAttribution']=='BEN_GONG_SI'" v-model="editData['add'][item.editType]"/>
                                        <a-input v-else allowClear v-model:value="editData['add'][item.editType]" placeholder="请输入"/>
                                    </template>

                                    <a-input v-if="item.editType=='text'" :disabled="item.dataIndex=='attributionCompany' && editData['add']['personnelAttribution'] =='BEN_GONG_SI' " allowClear v-model:value="editData['add'][item.dataIndex]" placeholder="请输入"/>
                                </a-form-item>
                            </a-table-summary-cell>
                        </template>
                        <a-table-summary-cell :index="6">
                            <a-space>
                                <a-button type="primary" shape="circle" @click="submitReg('add')" size="small">
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
    <div class="add_btn" @click="addVisible=true" v-if="!readOnly&&!addVisible"  v-permissionInvestment="['biz:projectCompany:add']">
        <plus-circle-outlined style="margin-right:8px;"/>
        新增
    </div>
</template>
<script setup>
import api                from '@/api/index';
import { message }        from 'ant-design-vue';
import { useCorrelation } from './correlation';
import { useDictStore }   from '@/store/dict';
import { watchArray }     from '@vueuse/core'
import UserNameSelect     from './UserNameSelect.vue'
const dict  = useDictStore();
const props = defineProps({
    modelValue : {
        type    : String,
        default : null,
    },
    companyId : {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 0,
    },
    tempId:{
        type    : Number,
        default : 0,
    },
    readOnly : {
        type    : Boolean,
        default : false,
    },
})
const emit = defineEmits(['update:modelValue'])
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
} = useCorrelation(props.companyId,'projectCompanyExecutives',{},props.menuId);
watchArray(list, (newList) => {
    emit('update:modelValue',newList.length>0?'finished':null);
},{immediate:true,deep: true})

const submitReg = (key,record)=>{
    if(editData[key].personnelAttribution=='WAI_BU_GU_DONG'&&!editData[key].attributionCompany){
        message.error('外部股东，必须输入归属企业名称！');
        return;
    }
    if(key=='add'){
        addSubmit();
    }else{
        editSubmit(record,key);
    }
}

const columns = [
    {
        title     : '人员归属',
        dataIndex : 'personnelAttributionStr',
        required  : true,
        width     : 180,
        editType  : 'personnelAttribution'
    },
    {
        title     : '姓名',
        dataIndex : 'name',
        required  : true,
        width     : 180,
        editType  : 'name'
    },
    {
        title     : '职务',
        dataIndex : 'positionStr',
        required  : true,
        width     : 180,
        editType  : 'position'
    },
    {
        title     : '归属企业',
        dataIndex : 'attributionCompany',
        width     : 200,
        editType  : 'text'
    },
    {
        title   : '相关附件',
        key     : 'documentTemplateList',
        width   : 360,
    },
    {
        title     : '备注',
        dataIndex : 'remark',
        width     : 200,
        editType  : 'text'
    },
]

onMounted(() => {
    if(!props.readOnly){
        columns.push(
            {
                title : '操作',
                key   : 'action',
                width : 200,
                fixed : 'right'
            }
        )
    }
    if(!props.tempId){
        columns.splice(4,1);
    }
    getList();
})

const uploadChange = (fileData,recordId)=>{
    let ext = fileData.name.substring(fileData.name.lastIndexOf(".")+1);
    let postData = {
        companyId          : props.companyId,
        stepMenuId         : props.menuId,
        documentTemplateId : props.tempId,
        docmentObject      : JSON.stringify(fileData),
        documentExt        : ext,
        documentName       : fileData.name.replace('.'+ext,''),
        recordId : recordId,
    }
    api.investment.documentAdd(postData).then(res=>{
        if(res.code==200){
            getList();
        }
    })
}
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
