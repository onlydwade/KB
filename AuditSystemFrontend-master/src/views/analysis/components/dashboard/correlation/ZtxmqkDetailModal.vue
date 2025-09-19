
<template>
    <a-modal v-model:visible="visible" title="详情" destroyOnClose :width="1660" footer="">
        <div class="filter-nav">
            <a-tabs v-model:activeKey="tabKey" @change="filterSubmit">
                <a-tab-pane key="1" v-if="filterForm.level == 1" tab="大区" ></a-tab-pane>
                <a-tab-pane key="2" tab="单位" ></a-tab-pane>
            </a-tabs>
        </div>
        <div class="text-right">
                <a-button  style="margin-top: 10px;margin-right: 60px;margin-bottom: 10px;"  @click="dataExport">导出</a-button>
         </div>
        <a-table :loading="loadding" :columns="(tabKey == 1 && filterForm.level == 1? data.columns : data.columnsOther
            )" :dataSource="data.list" :pagination="false">
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'companyName'">
                        <a @click="filterSubmit('2',record.recordId)">{{ record.companyName}}</a>
                    </template>
                    <template v-if="column.key === 'waiProjectTotal'">
                        {{ parseFormatNum(record.waiProjectTotal) }}
                    </template >
                    <template v-if="column.key === 'newWaiProjectTotal'">
                        {{ parseFormatNum(record.newWaiProjectTotal) }}
                    </template >
                    <template v-if="column.key === 'xzzhsr'">
                        {{ parseFormatNum(record.xzzhsr) }}
                    </template >
                    <template v-if="column.key === 'xzzje'">
                        {{ parseFormatNum(record.xzzje) }}
                    </template >
                    <template v-if="column.key === 'xzndzje'">
                        {{ parseFormatNum(record.xzndzje) }}
                    </template >
                    <template v-if="column.key === 'xqzje'">
                        {{ parseFormatNum(record.xqzje) }}
                    </template >
                    <template v-if="column.key === 'xqndzje'">
                        {{ parseFormatNum(record.xqndzje) }}
                    </template >
                </template>
            <!-- <template #bodyCell="{ column,record }">
                <template v-if="column.key === 'regionName'">
                    <div>
                        <p v-if="record.regionName">
                            {{ record.regionName }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>

                <template v-if="column.key === 'projectTotal'">
                    <div>
                        <p v-if="record.projectTotal">
                            {{ record.projectTotal }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'waiProjectTotal'">
                    <div>
                        <p v-if="record.waiProjectTotal">
                            {{ record.waiProjectTotal }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'signProjectTotal'">
                    <div>
                        <p v-if="record.signProjectTotal">
                            {{ record.signProjectTotal }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'newWaiProjectTotal'">
                    <div>
                        <p v-if="record.newWaiProjectTotal">
                            {{ record.newWaiProjectTotal }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'xzzhsr'">
                    <div>
                        <p v-if="record.xzzhsr">
                            {{ record.xzzhsr }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'xzzje'">
                    <div>
                        <p v-if="record.xzzje">
                            {{ record.xzzje }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'xzndzje'">
                    <div>
                        <p v-if="record.xzndzje">
                            {{ record.xzndzje }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'signRenewalProjectTotal'">
                    <div>
                        <p v-if="record.signRenewalProjectTotal">
                            {{ record.signRenewalProjectTotal }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'xqzje'">
                    <div>
                        <p v-if="record.xqzje">
                            {{ record.xqzje }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'xqndzje'">
                    <div>
                        <p v-if="record.xqndzje">
                            {{ record.xqndzje }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>

                
                <template v-if="column.key === 'regionName'">
                    <div>
                        <p v-if="record.regionName">
                            {{ record.regionName }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>

                <template v-if="column.key === 'companyName'">
                    <div>
                        <p v-if="record.companyName">
                            {{ record.companyName }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'projectTotal'">
                    <div>
                        <p v-if="record.projectTotal">
                            {{ record.projectTotal }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'waiProjectTotal'">
                    <div>
                        <p v-if="record.waiProjectTotal">
                            {{ record.waiProjectTotal }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'signProjectTotal'">
                    <div>
                        <p v-if="record.signProjectTotal">
                            {{ record.signProjectTotal }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'xzzhsr'">
                    <div>
                        <p v-if="record.xzzhsr">
                            {{ record.xzzhsr }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'xzzje'">
                    <div>
                        <p v-if="record.xzzje">
                            {{ record.xzzje }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'xzndzje'">
                    <div>
                        <p v-if="record.xzndzje">
                            {{ record.xzndzje }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'signRenewalProjectTotal'">
                    <div>
                        <p v-if="record.signRenewalProjectTotal">
                            {{ record.signRenewalProjectTotal }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'xqzje'">
                    <div>
                        <p v-if="record.xqzje">
                            {{ record.xqzje }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'xqndzje'">
                    <div>
                        <p v-if="record.xqndzje">
                            {{ record.xqndzje }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
            </template> -->
        </a-table>
        <div class="pagination_box" style="margin-top: 20px;">
            <a-pagination showSizeChanger show-quick-jumper v-model:current="filterForm.pageNo"
                v-model:pageSize="filterForm.pageSize" :show-total="total => `共 ${total} 条数据`" size="small"
                @change="getPage" @showSizeChange="filterForm.pageNo = 1" :total="data.total" />
        </div>
    </a-modal>
</template>

<script setup>
import api from '@/api/index';
import { dataToFile,parseFormatNum } from '@/utils/tools';
import { ref } from 'vue';
const props = defineProps({
    dateType:{
        type    : String,
        default : 'year',
    },
    dateVal:{
        type    : String,
        default : null,
    },
    level:{
        type    : Number,
        default : null,
    },
    deptId:{
        type    : Number,
        default : null,
    },
})
const emit = defineEmits(['success'])
const visible = ref(false);
const formData = ref({});
const formRef = ref(null);
const disabled = ref(true);
const tabKey = ref('1');
const loadding = ref(false);
const filterForm = reactive({
    pageNo: 1,
    pageSize: 5,
    dateVal:null,
    deptId:null,
    deptOldId:null,
    level:null
});
const data = reactive({
    list: [],
    columns: [
        {
            title: '大区',
            // dataIndex: 'companyName',
            key: 'companyName',
            width:150,
        },
        {
            title: '在管项目总数',
            dataIndex: 'projectTotal',
            width:150
        },
        {
            title: '在管项目面积总数',    
            // dataIndex: 'waiProjectTotal'
            key: 'waiProjectTotal',
            width:200
        },
        {
            title: '新签项目总数',
            dataIndex: 'signProjectTotal',
            width:150
            
        },
        {
            title: '当年新增面积总数',
            // dataIndex: 'newWaiProjectTotal'
            key: 'newWaiProjectTotal',
            width:160
        },
        {
            title: '当年新增合同转化收入',
            // dataIndex: 'xzzhsr'
            key: 'xzzhsr',
            width:190
        },
        {
            title: '新增合同金额',    
            // dataIndex: 'xzzje',
            key: 'xzzje',
            width:150
            
        },
        {
            title: '新增年度合同金额',
            // dataIndex: 'xzndzje'
            key: 'xzndzje',
            width:160
        },
        {
            title: '续签项目总数',
            dataIndex: 'signRenewalProjectTotal',
            width:150
           
        },
        {
            title: '续签合同总金额',
            // dataIndex: 'xqzje'
            key: 'xqzje',
            width:150
        },
        {
            title: '续签合同年度金额',    
            // dataIndex: 'xqndzje'
            key: 'xqndzje',
            width:160
        }
    ],
    columnsOther: [
        {
            title: '大区',
            dataIndex: 'regionName',
            width:150
        },
        {
            title: '单位',
            dataIndex: 'companyName',
            width:180
        },
        {
            title: '在管项目总数',
            dataIndex: 'projectTotal',
            width:150
          
        },
        {
            title: '在管项目面积总数',    
            // dataIndex: 'waiProjectTotal'
            key: 'waiProjectTotal',
            width:160   
        },
        {
            title: '新签项目总数',
            dataIndex: 'signProjectTotal',
            width:150
        },
        {
            title: '当年新增合同转化收入',
            // dataIndex: 'xzzhsr'
            key: 'xzzhsr',
            width:200
        },
        {
            title: '新增合同金额',    
            // dataIndex: 'xzzje',
            key: 'xzzje',
            width:150
            
        },
        {
            title: '新增年度合同金额',
            // dataIndex: 'xzndzje'
            key: 'xzndzje',
            width:180
        },
        {
            title: '续签项目总数',
            dataIndex: 'signRenewalProjectTotal',
            width:150
        },
        {
            title: '续签合同总金额',
            // dataIndex: 'xqzje'
            key: 'xqzje',
            width:150
        },
        {
            title: '续签合同年度金额',    
            // dataIndex: 'xqndzje'
            key: 'xqndzje',
            width:180
        }
    ],
    total: 0,
});
const filterSubmit = (key,recordId) => {
    if (key&&recordId&&key==2) {
        tabKey.value = key;
        filterForm.deptOldId = filterForm.deptId;
        filterForm.deptId = recordId;
        filterForm.pageSize = 5;
    }else if(key==1&&filterForm.deptOldId){
         filterForm.deptId = filterForm.deptOldId;
    }
    data.list = [];
    data.total = 0;
    filterForm.pageNo = 1;
    filterForm.pageSize = 5;
    getPage();
};


const getPage = () => {
     loadding.value = true;
     let postData = {
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize,
        recordId: filterForm.deptId,
        currYear: filterForm.dateVal
    }
    //大区
    if (tabKey.value == 1) {
        postData = {
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize,
        recordId: filterForm.deptId,
        currYear: filterForm.dateVal,
        tabKey: tabKey.value
        }
    } 
    //单位
    else if (tabKey.value == 2) {
        postData = {
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize,
        recordId: filterForm.deptId,
        currYear: filterForm.dateVal,
        tabKey: tabKey.value
            
    }
    }else {
        //其他为空
        data.list = [];
        return;
    }
    api.analysis.getProjectSituationThreeDetails(postData).then(res => {
            if (res.code == 200) {
                data.list = res.data.records;
                data.total = res.data.total;
                loadding.value = false;
            }
    })
};

//详情导出
const dataExport = () => {
    loadding.value = true;
    let  postData = {
        pageNo: filterForm.pageNo,
        pageSize: filterForm.pageSize,
        recordId: filterForm.deptId,
        currYear: filterForm.dateVal,
        tabKey: tabKey.value
    };
    api.analysis.projectSituationThreeDetailsExport(postData).then(res => {
        loadding.value = false;
        let timestamp = new Date().getTime();
        dataToFile(res, "整体项目情况-" + timestamp + ".xlsx");
    });
};

//接收传递的参数
const open = (level,deptId,dateVal) => {
    if (level == 1) {
        //注意数组会被typeof判断为"object"
        if(typeof deptId == 'object'){
            filterForm.deptId = deptId[0];
        }else{
            filterForm.deptId = deptId;
        }
        tabKey.value = '1';
    }else{
        filterForm.deptId = deptId;
        tabKey.value = '2';
    }
    filterForm.dateVal = dateVal;
    filterForm.level = level;
    filterForm.pageNo= 1,
    filterForm.pageSize = 5,
    visible.value = true;
    getPage();
};

onMounted(() => {
    getPage();
})

defineExpose({ open })
</script>
<style scoped lang="less"></style>