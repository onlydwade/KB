
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
                <a-form-item :required="column.required" :name="editData[index]?[index,['GU_DONG','GUAN_LIAN_GONG_SI'].includes(editData[index].relevanceType)?'name':'sonCompanyId']:''" v-if="column.editType=='sonCompany'">
                    <template v-if="editData[index]">
                        <a-input v-if="['GU_DONG','GUAN_LIAN_GONG_SI'].includes(editData[index].relevanceType)" allowClear v-model:value="editData[index].name" placeholder="请输入"/>
                        <a-select
                          v-else
                          v-model:value="editData[index].sonCompanyId"
                          class="w_full"
                          allowClear
                          placeholder="请搜索选择"
                          showSearch
                          @select="(val,option)=>{
                            compChange(val,option,index);
                          }"
                          :filter-option="false"
                          :options="listData[index] || []"
                          :fieldNames="{
                              label:'name',
                              value:'id'
                          }"
                          @search="fetchData($event,index)">
                          <template #notFoundContent>
                                <a-spin v-if="fetching" size="small" />
                                <div class="empty" v-else>
                                    按关键词搜索后选择
                                </div>
                          </template>
                        </a-select>
                    </template>
                    <template v-else>
                        <span v-if="['GU_DONG','GUAN_LIAN_GONG_SI'].includes(record.relevanceType)">
                            {{record.name}}
                        </span>
                        <router-link v-else :to="'/innerPage/subsidiaryInfo?id='+record.sonCompanyId" class="color-link">
                            {{record.sonCompanyName || '-'}}
                        </router-link>
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='companyNo'"
                    :rules="[{required:true,validator:companyNoRule}]">
                    <a-input v-if="editData[index]" allowClear v-model:value="editData[index][column.dataIndex]" placeholder="请输入"/>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <a-form-item :required="column.required" :name="editData[index]?[index,column.dataIndex]:''" v-if="column.editType=='text'">
                    <a-input v-if="editData[index]" allowClear v-model:value="editData[index][column.dataIndex]" placeholder="请输入"/>
                    <template v-else>
                        {{ text }}
                    </template>
                </a-form-item>
                <template v-if="column.key === 'action' && !record.parent">
                    <template v-if="!editData[index]">
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="editBefore(record,index)" v-permissionInvestment="['biz:projectCompany:edit']">编辑</a-button>
                        <a-button type="text" class="color-primary" v-if="!readOnly" size="small" @click="del(record,index)" v-permissionInvestment="['biz:projectCompany:delete']">删除</a-button>
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
                                <a-form-item :required="item.required" v-if="item.editType=='relevanceType'" :name="['add','relevanceType']">
                                    <a-select
                                        v-model:value="editData['add'][item.editType]"
                                        class="w_full"
                                        placeholder="请选择"
                                       :options="dict.options('ZI_XIANG_MU_GUAN_LIAN_LEI_XING')">
                                    </a-select>
                                </a-form-item>
                                <a-form-item :required="item.required" v-if="item.editType=='sonCompany'" :name="['add',['GU_DONG','GUAN_LIAN_GONG_SI'].includes(editData['add'].relevanceType)?'name':'sonCompanyId']">
                                    <a-input v-if="['GU_DONG','GUAN_LIAN_GONG_SI'].includes(editData['add'].relevanceType)" allowClear v-model:value="editData['add'].name" placeholder="请输入"/>
                                    <a-select
                                      v-else
                                      v-model:value="editData['add'].sonCompanyId"
                                      class="w_full"
                                      allowClear
                                      placeholder="请搜索选择"
                                      showSearch
                                      @select="(val,option)=>{
                                        compChange(val,option,'add');
                                      }"
                                      :filter-option="false"
                                      :options="listData['add'] || []"
                                      :fieldNames="{
                                          label:'name',
                                          value:'id'
                                      }"
                                      @search="fetchData($event,'add')">
                                      <template #notFoundContent>
                                            <a-spin v-if="fetching" size="small" />
                                            <div class="empty" v-else>
                                                按关键词搜索后选择
                                            </div>
                                      </template>
                                    </a-select>
                                </a-form-item>
                                <a-form-item v-if="item.editType=='companyNo'" :required="item.required" :name="['add',item.dataIndex]" :rules="[{required:true,validator:companyNoRule}]">
                                    <a-input allowClear v-model:value="editData['add'][item.dataIndex]" placeholder="请输入"/>
                                </a-form-item>
                                <a-form-item :required="item.required" v-if="item.editType=='text'" :name="['add',item.dataIndex]">
                                    <a-input  v-model:value="editData['add'][item.dataIndex]" placeholder="请输入"/>
                                </a-form-item>
                            </a-table-summary-cell>
                        </template>
                        <a-table-summary-cell :index="2">
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
    <div class="add_btn" @click="addVisible=true" v-if="!readOnly&&!addVisible"  v-permissionInvestment="['biz:projectCompany:add']">
        <plus-circle-outlined style="margin-right:8px;"/>
        新增
    </div>
</template>
<script setup>
import api                from '@/api/index';
import { dateFormat}      from '@/utils/tools';
import { useCorrelation } from './correlation';
import { useDictStore }   from '@/store/dict';
import { watchArray }     from '@vueuse/core'
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
} = useCorrelation(props.companyId,'projectCompanyItem',false,0,false,[{name:'关联企业',key:'sonCompanyId'},{name:'统一信用代码',key:'companyNo'}]);
watchArray(list, (newList) => {
    emit('update:modelValue',newList.length>0?'finished':null);
},{immediate:true,deep: true})


const columns = [
    {
        title     : '关联类型',
        dataIndex : 'relevanceTypeStr',
        width     : 170,
        required  : true,
        editType  : 'relevanceType'
    },
    {
        title    : '关联企业名称',
        key      : 'sonCompanyName',
        required : true,
        width    : 250,
        editType : 'sonCompany'
    },
    {
        title     : '统一社会信用代码',
        dataIndex : 'companyNo',
        width     : 200,
        editType  : 'companyNo'
    },
    {
        title     : '备注',
        dataIndex : 'remark',
        width     : 150,
        editType  : 'text'
    },
]

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

const editBefore = (record,index)=>{
    listData.value[index] = [{
        name : record.sonCompanyName,
        id   : record.sonCompanyId
    }]
    edit(record,index);
}

const listData = ref({});
const fetching  = ref(false);
const timerFun  = ref(null);
const fetchData = (name,key)=>{
    window.clearTimeout(timerFun.value);
    if(name){
        timerFun.value = setTimeout(()=>{
           searchData(name,key);
       },500)
    }
}
const searchData = async (name,key)=>{
    fetching.value = true;
    let postData = {
        pageNo        : 1,
        pageSize      : 500,
        // content       : name,
        // contentColumn : '',
        params        : {},
        likeParams: { 'company.name': name },
    }
    let res = await api.project.correlationPage(postData,'projectCompany');
    if(res.code==200){
        listData.value[key] = res.data.records.filter(item=>{
            return item.id!=props.companyId;
        });
    }
    fetching.value = false;
}

const compChange = async (data,options,key)=>{
    if(!options.projectId){
        return;
    }
    let res = await api.project.projectInfo(options.projectId);
    if(res.code==200){
        editData[key].companyNo         = options.companyNo;
        editData[key].sonCompanyName    = options.name;
        editData[key].investmentType    = options.investmentType;
        editData[key].investmentTypeStr = options.investmentTypeStr;
        editData[key].incorporationTime = options.incorporationTime;

        editData[key].dept      = res.data.businessDeptName;
        editData[key].principal = (res.data.principal || {}).realname;
    }

}

const companyNoRule = (rule, value, callback) => {
  const regex = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;
  if (rule.required && !regex.test(value)) {
    return new Promise((resolve, reject) => {
      reject('请输入有效社会信用代码(18位)');
    });
  }
  return Promise.resolve();
};
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
