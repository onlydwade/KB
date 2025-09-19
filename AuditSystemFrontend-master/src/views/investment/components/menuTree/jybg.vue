
<template>
    <div class="menu_inner">
        <Title title="经营报告">
            <template #right>
                <a-space>
                    <a-input allowClear v-model:value="filterForm.report_name" placeholder="请输入报告名称" />
                    <a-date-picker
                    v-model:value="filterForm.year"
                    picker="year"
                    :disabled-date="disabledDate"
                    style="width:180px;"
                    valueFormat="YYYY"
                    format="YYYY"
                    placeholder="请选择报告所属年份"/>

                    <a-select
                        v-model:value="filterForm.cycleType"
                        class="flex_full"
                        placeholder="请选择"
                        style="width:100px;"
                        @change="()=>{
                            filterForm.cycle = null
                        }"
                        :options="dict.options('BAO_GAO_ZHOU_QI_LEI_XING')">
                    </a-select>
                    <a-input  disabled v-if="filterForm.cycleType=='DIAO_YAN_NIAN_DU'" style="width:150px;" :value="filterForm.year" />
                    <a-select
                        v-else
                        v-model:value="filterForm.cycle"
                        style="width:150px;"
                        placeholder="请选择"
                        :options="dict.optionsAll(filterForm.cycleType)">
                    </a-select>

                    <a-button @click="filterSubmit">
                        查询
                    </a-button>
                    <a-button @click="reset">重置</a-button>
                    <a-button type="primary" @click="drawer('ADD')" v-permissionInvestment="['biz:projectCompany:add']">
                        上传报告
                    </a-button>
                </a-space>
            </template>
        </Title>
        <a-table :columns="data.columns" :loading="loadding" :data-source="data.list" rowKey="id"  :pagination="false" :scroll="{ x: '100%' }">
            <template #bodyCell="{ column,text,record,index }">
                <template v-if="column.key === 'sort'">
                    {{(filterForm.pageNo-1)*filterForm.pageSize + index +1}}
                </template>
                <template v-if="column.key === 'cycle'">
                    {{record.cycleTypeStr}}-{{record.cycleStr}}
                </template>
                <template v-if="column.key === 'documentTemplateList'">
                    <!-- <template v-if="record.reportMethod!=='XIAN_SHANG_JI_LU'" v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                        <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                    </template> -->
                    <a-popconfirm placement="topLeft">
                              <template #icon>
                                  <template v-for="(item,tindex) in (record.documentTemplateList || [])" :key="index+'_'+tindex">
                                      <FileItem v-for="(file,findex) in (item.projectCompanyDocumentList || [])" :key="index+'_'+tindex+'_'+findex" :fileData="file.docmentObject" :readOnly="true" />
                                  </template>
                              </template>
                              <template #cancelButton>
                              </template>
                              <template #okButton>
                              </template>
                              <a-bottom type="text" class="color-link" v-if="hasFile(record.documentTemplateList)">查看文件</a-bottom>
                    </a-popconfirm>
                </template>
                <template v-if="column.key === 'action'">
                    <actionBtn :actions="actions(record,index)"/>
                </template>
                <EllipsisTooltip v-if="column.ellipsis" :content="text"/>
            </template>
        </a-table>
        <div class="pagination_table">
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
    <operationalDrawer ref="drawerRef" :companyId="companyId" :menuId="menuInfo.id" @success="getPage"/>
    <pdfPreView :formData="formData"  ref="pdfPreViewRef"></pdfPreView>
</template>
<script setup>
import api                 from '@/api/index';
import operationalDrawer   from '../operationalDrawer.vue';
import pdfPreView          from '../pdfPreView.vue';
import { hasPermission,dateFormat , hasFile}      from '@/utils/tools';
import { message , Modal } from 'ant-design-vue';
import { useDictStore }    from '@/store/dict';
const dict  = useDictStore();

import { useRecordFileDel } from '../recordFileDel';
const {
    delRecordFile
} = useRecordFileDel();

const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    }
})

const loadding   = ref(false);
const filterForm = reactive({
    pageNo    : 1,
    pageSize  : 10,
})
const data = reactive({
    list : [],
    columns : [
        {
            title    : '序号',
            key      : 'sort',
            width    : 80,
        },
        {
            title     : '报告名称',
            dataIndex : 'name',
            width     : 170,
            ellipsis  : true,
        },
        {
            title     : '报告所属年份',
            dataIndex : 'year',
            width     : 170,
        },
        {
            title : '报告期',
            key   : 'cycle',
            width : 170,
        },
        {
            title     : '报告方式',
            dataIndex : 'reportMethodStr',
            width     : 170,
        },
        {
            title : '文件',
            key   : 'documentTemplateList',
            width : 120,
        },
        {
            title     : '创建人',
            dataIndex : ['createUser','realname'],
            width     : 180,
        },
        {
            title        : '创建时间',
            dataIndex    : 'createTime',
            width        : 180,
        },
        {
            title : '操作',
            key   : 'action',
            width : 200,
            fixed : 'right'
        },
    ],
    total : 0,
})
const getPage = ()=>{
    let postData = {
        desc     : ['createTime'],
        pageNo   : filterForm.pageNo,
        pageSize : filterForm.pageSize,
        params   : {
          'report.companyId' : props.companyId
        },
        likeParams : {}
    }
    let params     = ['year','cycleType','cycle'];
    let likeParams = ['report_name'];
    params.forEach(key => {
        if(filterForm[key]&&filterForm[key]!=-1){
            postData.params[key] = filterForm[key];
        }
    });
    likeParams.forEach(key => {
        if(filterForm[key]){
            if(key.includes('_')){
                postData.likeParams[key.replace('_','.')] = filterForm[key];
            }else{
                postData.likeParams[key] = filterForm[key];
            }
        }
    });

    loadding.value = true;
    api.investment.correlationPage(postData,'companyOperateReport',props.menuInfo.id).then(res=>{
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
const reset = ()=>{
    filterForm.report_name = null;
    filterForm.year        = null;
    filterForm.cycleType   = null;
    filterForm.cycle       = null;
    filterSubmit();
}

onMounted(() => {
    filterSubmit();
})
const actions = (record,index)=>{
    return [
        {
            text  : '查看详情',
            show  : true,
            click : ()=>{
                drawer('SHOW',record);
            }
        },
        {
            text  : '预览',
            show  : true,
            click : ()=>{
              pdfOpen(record);
            }
        },
        {
            text  : '编辑',
            show  : hasPermission(['biz:projectCompany:edit'],'investment'),
            click : ()=>{
                drawer('EDIT',record);
            }
        },
    ];
}
const del = (data)=>{
    Modal.confirm({
        title   : '操作确认',
        content : '是否确认删除该条经营报告？',
        async onOk() {
            if(data.documentTemplateList&&data.documentTemplateList.length>0){
                await delRecordFile(data.documentTemplateList);
            }
            api.investment.correlationDel(data.id,'companyOperateReport').then(res=>{
                if(res.code==200){
                    getPage();
                }
            })
        }
    });
}
//drawer
const companyName = inject('getAutoParams')('name');
const drawerRef = ref(null);
const drawer    = (type,record)=>{
    drawerRef.value.open(type,record || {},companyName.value);
}

const disabledDate = (current) => {
  return current && current > new Date();
};
const pdfPreViewRef = ref(null)
const formData = ref({})
const pdfOpen = (record) => {
  formData.value = record
  pdfPreViewRef.value.open()
}
</script>
<style scoped lang="less">

</style>
