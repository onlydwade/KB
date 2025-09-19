
<template>
    <a-modal v-model:visible="visible" title="详情" destroyOnClose :width="1160" footer="">
        <div class="text-right">
                <a-button  style="margin-top: 10px;margin-right: 60px;margin-bottom: 10px;"   @click="dataExport">导出</a-button>
         </div>
        <a-table :loading="loadding" :columns="data.columns"
            :dataSource="data.list" :pagination="false" 
            :row-key="record => record.key"
            @expand="getInnerData"
            :expandedRowKeys="expandedRowKeys"
            >
            <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'ZDTZHTJE'">
                        {{parseFormatNum(record.ZDTZHTJE) }}
                    </template>
                    <template v-if="column.key === 'HTZJE'">
                        {{parseFormatNum(record.HTZJE) }}
                    </template>
                    <template v-if="column.key === 'HTNDJE'">
                        {{parseFormatNum(record.HTNDJE) }}
                    </template >
                </template>
        <!-- <template #expandedRowRender>
            <a-table :columns="innerColumns.columns" :data-source="innerColumns.list" :pagination="false" >
            </a-table>
        </template> -->
            <!-- <template #bodyCell="{ column,record }">
                <template v-if="column.key === 'a1'">
                    <div>
                        <p v-if="record.a1">
                            {{ record.a1 }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>

                <template v-if="column.key === 'a2'">
                    <div>
                        <p v-if="record.a2">
                            {{ record.a2 }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'a3'">
                    <div>
                        <p v-if="record.a3">
                            {{ record.a3 }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'a4'">
                    <div>
                        <p v-if="record.a4">
                            {{ record.a4 }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'a5'">
                    <div>
                        <p v-if="record.a5">
                            {{ record.a5 }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
                <template v-if="column.key === 'a6'">
                    <div>
                        <p v-if="record.a6">
                            {{ record.a6 }}
                        </p>
                        <p v-else>/</p>
                    </div>
                </template>
            </template> -->
        </a-table>
        <!-- <div class="pagination_box" style="margin-top: 20px;">
            <a-pagination showSizeChanger show-quick-jumper v-model:current="filterForm.pageNo"
                v-model:pageSize="filterForm.pageSize" :show-total="total => `共 ${total} 条数据`" size="small"
                @change="getPage" @showSizeChange="filterForm.pageNo = 1" :total="data.total" />
        </div> -->
    </a-modal>
</template>

<script setup>
import api from '@/api/index';
import { amountUnit,parseFormatNum,dataToFile } from '@/utils/tools';
import { ref } from 'vue';
import { useDictStore } from '@/store/dict';
const radioStrList = ['HTZJE','HTNDJE','ZDTZHTJE','YXXXTBL','XMBLL'];
const dict = useDictStore();
const filterForm = reactive({
    dateVal:null,
    deptId:null,
    deptOldId:null,
    level:null
});
const emit = defineEmits(['success'])
const visible = ref(false);
const formData = ref({});
const formRef = ref(null);
const disabled = ref(true);
const loadding = ref(false);
const expandedRowKeys = ref([]);

//动态列
const innerColumns = reactive({
    list: [],
    columns: []
});

//固定列
const innerColumns2 = reactive({
    list: [],
    columns: [
        {
            title: '层级',
            dataIndex: 'companyName',
            key: 'companyName'
        }]
});

//综合列
const data = reactive({
    list: [],
    columns: [
        // {
        //     title: '序号',
        //     dataIndex: 'num',
        //     key: 'num'
        // },
        // {
        //     title: '层级',
        //     dataIndex: 'companyName',
        //     key: 'companyName'
        // },
        // {
        //     title: '大区主导扩展量',    
        //     dataIndex: 'zdtzhtje',
        //     key: 'zdtzhtje'
        // },
        // {
        //     title: '扩展合同总金额',
        //     dataIndex: 'htzje',
        //     key: 'htzje'
        // },
        // {
        //     title: '新增扩展合同年度金额',
        //     dataIndex: 'htndje',
        //     key: 'htndje'
        // },
        // {
        //     title: '有效信息填报量111',
        //     dataIndex: 'yxxxtbl',
        //     key: 'yxxxtbl'
        // },
        // {
        //     title: '有效信息填报量',
        //     dataIndex: 'yxxxtbl',
        //     key: 'yxxxtbl'
        // },
       
    ],
    total: 0,
});


const getPage = () => {
    if (filterForm.deptId==null||filterForm.dateVal==null||filterForm.level==null) {
        data.list = [];
        return;
    }
    loadding.value = true;
     let postData = {
        recordId: filterForm.deptId,
        currYear: filterForm.dateVal,
        currLevel: filterForm.level
    }

        
    api.analysis.getAchievementsDetails(postData).then(res => {
            if (res.code == 200) {
                data.list = res.data;
                console.log(data.list);
                loadding.value = false;
            }
    })

}

 /*
    * 功能：点击某行加号，加载子表格数据
    * 参数：expanded：是否展开 
    * 参数：record：点击行的数据
    */
const getInnerData = (expanded, record) => {
    // 只展开一行
    if (expandedRowKeys.value.length > 0) { //进这个判断说明当前已经有展开的了
            //返回某个指定的字符串值在字符串中首次出现的位置，下标为0
                let index = expandedRowKeys.value.indexOf(record.key);
                if (index > -1) { //如果出现则截取这个id,1d到1相当于0，针对重复点击一个
                expandedRowKeys.value.splice(index, 1);
                } else {
                //如果没出现则截取所有id,添加点击id，0到1，针对已经有一个展开，点另一个会进入判断
                expandedRowKeys.value.splice(0, expandedRowKeys.value.length);
                expandedRowKeys.value.push(record.key);
                }
            } else {
            //数组长度小于0，说明都没展开，第一次点击，id添加到数组，数组有谁的id谁就展开
                expandedRowKeys.value.push(record.key);  
    }
    if (expanded) {
        loadding.value = true;
        let postData = {
            recordId: record.recordId,
            currYear: filterForm.dateVal,
            currLevel: filterForm.level
        }
        api.analysis.getAchievementsDetails(postData).then(res => {
                    if (res.code == 200) {
                        var dataList = data.list;
                        for (var i = 0; i < dataList.length; i++) {
                            dataList[i].children =  res.data;
                        }
                        data.list = dataList;
                        loadding.value = false;
                    }
            })

    }
}

//导出
const dataExport = ()=>{
    loadding.value = true;
    let  postData = {
        recordId: filterForm.deptId,
        currYear: filterForm.dateVal,
        currLevel: filterForm.level
    };
    api.analysis.achievementsDetailsExport(postData).then(res => {
        loadding.value = false;
        let timestamp = new Date().getTime();
        dataToFile(res, "业绩达成情况-" + timestamp + ".xlsx");
    });
}

//接收传递的参数
const open = (level,deptId,dateVal) => {
    innerColumns.columns = [];
    //清空所有展开行
    expandedRowKeys.value = [];  
   //注意数组会被typeof判断为"object"
   if(typeof deptId == 'object'){
            filterForm.deptId = deptId[0];
    }else{
            filterForm.deptId = deptId;
    }
    filterForm.dateVal = dateVal;
    if(typeof level == 'object'){
            filterForm.level = level[0];
    }else{
            filterForm.level = level;
    };
    visible.value = true;
    api.analysis.getAchievements(filterForm.level,filterForm.deptId,filterForm.dateVal).then(res => {
        if (res.code === 200 ){
            debugger
            for (let i = 0; i < res.data.length; i++) {
                res.data[i].dataIndex = res.data[i].key;
                res.data[i].title =  res.data[i].name+res.data[i].unit;
                if (radioStrList.includes(res.data[i].key)) {
                    innerColumns.columns.push(res.data[i]);
                }
            }
            data.columns = [... innerColumns2.columns, ...innerColumns.columns]; 
            getPage();
        }
    })
};

onMounted(() => {
    getPage();
})
defineExpose({ open })
</script>
<style scoped lang="less"></style>