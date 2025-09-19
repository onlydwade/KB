
<template>
    <div class="filter-box">
        <a-form :label-col="{ style: { width: '110px' } }" ref="formRef" :model="formData">
            <a-row :class="{ 'show_more_col': formData.moreFilter }">
                <a-col :span="6" class="more_col">
                    <a-form-item label="是否续签" name="inStock">
                        <a-select v-model:value="formData.inStock" class="w_full" placeholder="请选择"
                            :options="dict.optionsAll('SHI_FOU')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="归属单位" name="companyId">
                        <!-- <DeptSelects v-model="formData.companyId" :noRoot="true" /> -->                        
                        <a-tree-select
                          v-model:value="formData.companyId"
                          @change="companyIdChage()"
                          show-search
                          tree-checkable                          
                          :dropdown-style="{ maxHeight: '200px', overflow: 'auto' }"
                          placeholder="请选择查询主体"
                          tree-default-expand-all
                          treeNodeFilterProp="name"
                          treeCheckStrictly
                          :dropdownStyle = '{
                             whiteSpace: "nowrap"
                          }'
                           @select="deptSelect"
                          :field-names="{
                              children: 'children',
                              label: 'name',
                              value: 'id',
                          }"
                          :tree-data="treeData.list"
                      >
                      </a-tree-select>

                    </a-form-item>
                </a-col>
                 <a-col :span="6" class="more_col">
                    <a-form-item label="拓展单位" name="expandCompanyId">
                        <a-tree-select
                          v-model:value="formData.expandCompanyId"
                          @change="expandCompanyIdChage()"
                          show-search
                          tree-checkable                          
                          :dropdown-style="{ maxHeight: '200px', overflow: 'auto' }"
                          placeholder="请选择查询主体"
                          tree-default-expand-all
                          treeNodeFilterProp="name"
                          treeCheckStrictly
                          :dropdownStyle = '{
                             whiteSpace: "nowrap"
                          }'
                           @select="deptSelect"
                          :field-names="{
                              children: 'children',
                              label: 'name',
                              value: 'id',
                          }"
                          :tree-data="treeData.list"
                      >
                      </a-tree-select>
                    </a-form-item>
                </a-col>

              
                <a-col :span="6">
                    <a-form-item label="项目所属年份" name="year">
                        <a-date-picker   :allowClear="false" v-model:value="formData.year" class="w_full" picker="year" valueFormat="YYYY" />
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="续签或重新投标" name="isRenewalTender">
                        <a-select v-model:value="formData.isRenewalTender" class="w_full" placeholder="请选择"
                            :options="dict.optionsAll('SHI_FOU')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="6" class="more_col">
                    <a-form-item label="多个拓展单位" name="hasExpandCompanys">
                        <a-select v-model:value="formData.hasExpandCompanys" class="w_full" placeholder="请选择"
                            :options="dict.optionsAll('SHI_FOU')">
                        </a-select>
                    </a-form-item>
                </a-col>
 

                <a-col :span="formData.moreFilter ? 24 : 18" class="text-right">
                    <a-form-item name="searchKey">
                        <a-space> 
                            <a-button v-permission="['biz:actualIn:list']" type="primary" @click="filterSubmit">查询</a-button>
                            <a-button @click="reset">重置</a-button>
                            <a-button v-permission="['biz:actualIn:export']" @click="dataExport">导出</a-button>
                    
                            <a-button type="text" @click="unfold">
                                {{ formData.moreFilter ? '收起' : '展开' }}
                            </a-button>
                        </a-space>
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
    </div>
</template>
<script setup>
import api from '@/api/index';
import moment from 'moment'
import { mainStore } from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict = useDictStore();
const emit = defineEmits(['update:modelValue', 'submit', 'dataExport'])
const props = defineProps({
    modelValue: {
        type: Object,
        default: {},
    }
})

const deptSelect = (val,option)=>{
    treeData.treeIds = option.id;
    treeData.level  = option.level;
}

const treeData = reactive({
    loadding : false,
    treeIds  : 100,
    list     : [],
    level  : 1,
});

//项目第一项状态
const serviceStatus = reactive([
     
]
);
//补上从枚举读取的状态
const getServiceStatusDic = () => {
    let dictData = dict.options('XIANG_MU_ZHUANG_TAI')
    for (let i = 0; i < dictData.length; i++) {
        const item = dictData[i];
        let label = item.label;
        let value = item.value;
        serviceStatus.push({
            label: label,
            value: value,
        })
    }
}; 
const formData = computed({
    get: () => {
        getServiceStatusDic();
        let data = props.modelValue;
        data.year = ref(data.year || moment(new Date()).format('YYYY'));
        if (data.modelName) {
            data.year = null;
        }
        data.inStock = '-1';
        data.isRenewalTender = '-1';
        return data;
    },
    set: (val) => emit('update:modelValue', val)
});

const formRef = ref(null);
const filterSubmit = () => {
    console.log(formData.value.companyId);
    console.log(formData.value.expandCompanyId);

    emit('submit')
}
const bool = ref(true)
const reset = (data) => {
    formRef.value.resetFields();
    bool.value = false
    nextTick(() => {
        bool.value = true
    })
    formData.value.provinceCode = null;
    formData.value.cityCode = null;
    formData.value.year = moment(new Date()).format('YYYY');
    if (data.modelName) {
        formData.value.year = null;
    }
    // formData.value.companyId = [];
    formData.value.companyId = [{"value": treeData.list[0].id}];
    formData.value.expandCompanyId = [];

    formData.value.pageNo = 1;
    formData.value.pageSize = 10;
    formData.value.inStock = '-1';
    formData.value.isRenewalTender = '-1';


    filterSubmit();
}
const dataExport = () => {
    emit('dataExport');
}

const unfold = () => {
    formData.value.moreFilter = !formData.value.moreFilter  
}
onMounted(() => {
    getTree();
})

const getTree = async ()=>{
    treeData.loadding = true;
    let res = await api.performance.actualInTree();
    treeData.loadding = false;
    if(res.code==200&&res.data){

        let tree = [];
        tree = getDeptWithOutRoot([res.data]);
        // if(res.data.children && res.data.children.length>0){
        //     tree = getDeptWithOutRoot([res.data]);
        // }else{
        //     let dept = await api.sys.deptInfo(res.data.parentId);
        //     tree = [{
        //         id       : dept.data.deptId,
        //         name     : dept.data.deptName,
        //         level    : dept.data.level,
        //         parentId : dept.data.parentId,
        //         children : []
        //     }];
        // }

         treeData.list = tree;
        // treeData.treeIds = tree[0].id;        
        // treeData.level = tree[0].level;
        formData.value.companyId = [{"value":tree[0].id}];
        formData.value.expandCompanyId = [{"value":tree[0].id}];
        filterSubmit();
    }
}

const companyIdChage=()=>{
    if((formData.value.companyId==null||formData.value.companyId.length==0)&& (formData.value.expandCompanyId==null||formData.value.expandCompanyId.length==0) ){
        formData.value.expandCompanyId = [{"value":treeData.list[0].id}];
    }
}
const expandCompanyIdChage=()=>{
    if((formData.value.companyId==null||formData.value.companyId.length==0)&& (formData.value.expandCompanyId==null||formData.value.expandCompanyId.length==0) ){
        formData.value.companyId = [{"value":treeData.list[0].id}];
    }
}

const getDeptWithOutRoot = (tree)=>{
    let arr = [];
    function doArr(treeData,parentChild){
      if (treeData){
        treeData.forEach((item, i) => {
          let obj = {
            ...item,
            children : []
          }

          if(item.deptType === 'CENG_JI'|| (item.children&&item.children.length>0)){
            doArr(item.children,obj.children);
            parentChild.push(obj)
          }
        });
      }
    }
    doArr(tree,arr);
    return arr;
}
</script>
<style scoped lang="less"></style>
