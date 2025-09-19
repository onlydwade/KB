
<template>
    <div class="menu_inner">
        <Title title="经营报告">
            <template #right>
                <a-space>
                    <a-button @click="showAsync" v-permissionInvestment="['biz:projectCompany:financeSync']">
                        手动同步
                    </a-button>
                    <a-button type="primary" @click="drawer('ADD')" v-permissionInvestment="['biz:projectCompany:financeAdd']">
                        录入财务信息
                    </a-button>
                    <a-tooltip placement="bottomRight">
                        <template #title>若子公司数据未录入财务系统，可点击录入财务信息按钮进行财务数据的手动录入</template>
                        <question-circle-filled style="fontSize:20px"/>
                    </a-tooltip>
                </a-space>
            </template>
        </Title>
        <a-descriptions :column="column" style="margin-bottom:24px;">
            <a-descriptions-item label="企业名称">{{infoData.name || '-'}}</a-descriptions-item>
            <a-descriptions-item label="持股比例（%）">{{infoData.shareholdingRatio || '-'}}</a-descriptions-item>
            <a-descriptions-item label="实缴资金（元）">{{infoData.paidCapital || '-'}}</a-descriptions-item>

            <a-descriptions-item label="报告周期">{{dateFormat(reportData.beginTime,'YYYY-MM') || '-'}} - {{dateFormat(reportData.endTime,'YYYY-MM') || '-'}}</a-descriptions-item>
            <a-descriptions-item label="是否已审计">
                <a-radio-group v-model:value="reportData.audit" name="radioGroup" @change="auditChange">
                    <a-radio value="SHI">是</a-radio>
                    <a-radio value="FOU">否</a-radio>
                </a-radio-group>
            </a-descriptions-item>
        </a-descriptions>

        <cwxxItem :menuId="57" :companyId="companyId" :reportData="reportData"></cwxxItem>
        <cwxxItem :menuId="58" :companyId="companyId" :reportData="reportData"></cwxxItem>
        <cwxxItem :menuId="59" :companyId="companyId" :reportData="reportData"></cwxxItem>
    </div>
    <cwxxDrawer ref="drawerRef" :companyId="companyId" :menuId="menuInfo.id" @success="getData"/>

    <a-modal v-model:visible="visible" title="手动同步" @ok="handleOk">
        <template #footer>
            <a-button key="back" @click="visible=false">取消</a-button>
            <a-button key="submit" type="primary" @click="handleOk" :loading="syncLoading">同步</a-button>
        </template>
        <a-row justify="center"  style="padding:10px 0;">
          <a-col :span="15" style="padding:10px 0;">
            <a-range-picker
            v-model:value="monthRang"
            valueFormat="YYYY-MM-DD 00:00:00"
            format="YYYY-MM"
            picker="month"
            :placeholder="['同步周期','月份范围']"/>
          </a-col>
         <a-col :span="15" style="padding:10px 0;">
          <a-select
             v-model:value="reportFormType"
             class="w_full"
             placeholder="请选择"
             :options="reportList">
          </a-select>
         </a-col>
        </a-row>
    </a-modal>

</template>
<script setup>
import api            from '@/api/index';
import cwxxDrawer     from '../correlation/cwxxDrawer.vue';
import cwxxItem       from '../correlation/cwxxItem.vue';
import { dateFormat } from '@/utils/tools';
import { message }    from 'ant-design-vue';
import { ref } from 'vue';
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
const column     = { xxl: 3, xl: 3, lg: 3, md: 3, sm: 2, xs: 1 };
const infoData   = inject('getAutoParams')();
const reportData = ref({});
const bus        = inject('bus');
const syncLoading = ref(false)
const getData    = (type)=>{
    let postData = {
        desc     : ['createTime'],
        pageNo   : 1,
        pageSize : 1,
        params   : {
            companyId : props.companyId
        }
    }
    api.investment.correlationPage(postData,'companyFinance').then(res=>{
        if(res.code==200){
            reportData.value = res.data.records[0] || {};
            if(reportData.value.id){
                getLastInfo();
            }

            if(type!='firstIn'){
                bus.emit('reloadPageData');
            }
        }
    })
}
const summary = ref({})
const getLastInfo = ()=>{
    api.investment.financeTotal(props.companyId).then(res=>{
        if(res.code==200){
            reportData.value.totalAssets = res.data.totalAssets;
            reportData.value.totalProfit = res.data.totalProfit;
            reportData.value.netProfit   = res.data.netProfit;
            reportData.value.netAssets   = res.data.netAssets;
        }
    })
}
const auditChange = ()=>{
    api.investment.correlationEdit(reportData.value,'companyFinance');
}
onMounted(() => {
    getData('firstIn');
})

const visible   = ref(false);
const monthRang = ref([]);
const reportFormType = ref('gb')
const reportList = ref([
  {label:'企业报表数据',value:'gb'},
  {label:'合并报表数据',value:'hb'}
])
const showAsync = ()=>{
    monthRang.value = [];
    visible.value   = true;
}
const handleOk  = ()=>{
    if(monthRang.value.length==0){
        message.warning('请选择同步月份范围！');
        return;
    }
    syncLoading.value = true
    api.investment.financeSync({
        companyId : props.companyId,
        beginTime : monthRang.value[0],
        endTime   : monthRang.value[1],
        ver       : reportFormType.value
    }).then(res=>{
        if(res.code==200){
            message.success('操作成功');
            visible.value = false;
            getData();
        }
        syncLoading.value = false
    })
}

//drawer
const drawerRef = ref(null);
const drawer    = ()=>{
    drawerRef.value.open();
}
</script>
<style scoped lang="less">

</style>
