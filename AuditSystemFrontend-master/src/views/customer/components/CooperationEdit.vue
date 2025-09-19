
<template>
        <div class="content-inner">
        <a-page-header :ghost="false"
            title='战略合作协议'>
        </a-page-header>
        </div>
    <a-form layout="vertical" :model="formData" ref="formRef"> 
                <div   class="content-box_full">
        <a-collapse ghost expandIconPosition="right" v-model:activeKey="collapseKey" class="crl-collapse">
            <a-collapse-panel key="base">
                <template #header>
                    <h5 class="title_single">基础信息</h5>
                    <span class="color-danger">*</span>
                </template>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="签约甲方" required  name="partyA">
                            <a-input allowClear v-model:value="formData.partyA" placeholder="请输入" />
                        </a-form-item>  
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="签约乙方" required  name="partyB">
                            <a-input allowClear v-model:value="formData.partyB" placeholder="请输入" />
                        </a-form-item>  
                    </a-col> 

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="签约时间" required name="signDate">    
                            <a-date-picker 
                                v-model:value="formData.signDate"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="期限（年）" required name="termYear">
                            <a-input-number :disabled="disabled" v-model:value="formData.termYear" :min="0"
                                class="w_full" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="开始时间" required name="startDate">    
                            <a-date-picker 
                                v-model:value="formData.startDate"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="结束时间" required name="endDate">    
                            <a-date-picker 
                                v-model:value="formData.endDate"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item label="协议内容" required name="content">
                            <KeyWords v-model="formData.content" :type="4" />
                    </a-form-item>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12" style="float:right;">
                    <a-button  style="float:right;" size="large"  @click="save()" type="primary">保存</a-button>
                </a-col>
            </a-row>
        </a-collapse-panel>
        </a-collapse>        


        <Title title="合作协议及证明上传"></Title>
        <FullTable :columns="data.columns" :dataSource="data.list">
            <template #bodyCell="{ column,record }">
                <template v-if="column.key === 'name'">
                    <span class="color-danger" v-if="record.required">*</span>
                    {{record.name}}
                </template>
                <template v-if="column.key === 'status'">
                    <check-circle-outlined v-if="record[record.attr].length>0" class="color-success"/>
                    <clock-circle-outlined v-else  class="color-gray" />
                </template>
                <template v-if="column.key === 'upload'">
                    <Upload v-model="record[record.attr]" :limit="record.limit" @change="fileUploadChange($event,record)" :readOnly="readOnly" :canDel="canDel"/>
                </template>
            </template>
        </FullTable>
    </div>
    </a-form>
</template>
<script setup>
import api         from '@/api/index';
import { message } from 'ant-design-vue';
import {hasPermission} from '@/utils/tools';
const router     = useRouter();
const route = useRoute();
const props = defineProps({
    recordId:{
        type    : Number,
        default : 0,
    },
    readOnly:{
        type    : Boolean,
        default : false,
    }
})
const cooperationId    = ref(Number(route.query.recordId || 0));
const customerId    = ref(Number(route.query.customerId || 0));
const formData    = ref({});
const formRef    = ref(null);
const collapseKey = ref(['base']);
const listRef = ref(null) 
const data     = reactive({
    list    : [],
    columns : [
        {
            title : '名称',
            key   : 'name',
            width : 150,
        },
        {
            title : '上传状态',
            key   : 'status',
            width : 80,
        },
        {
            title : '文件上传',
            key   : 'upload',
            width : 180,
        },
    ],
})
const canDel = computed(()=>{
    if(hasPermission(['biz:customer:cooperationFileDel'])){
        return 1;
    }else{
        return 2
    }
    
})
const baseData = [
    {name:'合作协议（盖章版）上传',required:true,attr:'cooperationAgreementDocument',limit:1},
    {name:'专项会议纪要上传',   required:false,attr:'meetingMinutesDocument',limit:0},
    {name:'其他文件上传',      required:false,attr:'otherDocument',limit:0},
]
const getInfo  = ()=>{     
 
    api.customer.cooperationGet(cooperationId.value).then(res=>{
        if(res.code==200){
            let resData = res.data || {};
            if(resData.id){
                cooperationId.value = resData.id;
                formData.value=res.data;
            }

            data.list           = [];
            baseData.forEach((item, i) => {
                let obj = {
                    name     : item.name,
                    required : item.required,
                    attr     : item.attr,
                    limit    : item.limit
                }
                obj[item.attr] = JSON.parse(resData[item.attr] || '[]');
                data.list.push(obj);
            });
        }
    })
}


const save = (val,record)=>{
    formRef.value.validateFields().then(vRes=>{
            let postData = {
            partyA:formData.value.partyA,
            partyB:formData.value.partyB,
            signDate:formData.value.signDate,
            startDate:formData.value.startDate,
            endDate:formData.value.endDate,
            content:formData.value.content,
            termYear:formData.value.termYear,
            id         : cooperationId.value,
            customerId : customerId.value
            }
        data.list.forEach((item, i) => {
            postData[item.attr] = item[item.attr].length>0?JSON.stringify(item[item.attr]):'[]';
        });
        if(postData["cooperationAgreementDocument"].length<=3){
            message.warning('请上传合作协议（盖章版）文件！');
            return;
        }
        let apiKey = cooperationId.value?'cooperationEdit':'cooperationAdd';
        api.customer[apiKey](postData).then(res=>{
            if(res.code==200){
                cooperationId.value = res.data.id;
                message.success('操作成功');
                router.back();
            }
        }) 
    }).catch(err=>{
        collapseKey.value = ['base'];
        message.warning('请完善必填信息！');
    })


}

const fileUploadChange = (val,record)=>{
    data.list.forEach((item, i) => {
        postData[item.attr] = item[item.attr].length>0?JSON.stringify(item[item.attr]):'[]';
    });
    
     
}
onMounted(() => {    
        getInfo();
})

</script>
<style scoped lang="less">
.content-box{

}
</style>
