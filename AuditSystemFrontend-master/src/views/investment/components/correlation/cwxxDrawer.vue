<template>
    <a-drawer :maskClosable="false"
    :width="880"
    :title="title"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
        <template #extra>
            <a-space :size="16">
                <a-button size="large" @click="handleClose">取消</a-button>
                <a-button size="large" type="primary" @click="handleOk">保存</a-button>
            </a-space>
        </template>
        <a-form layout="vertical" :model="formData" ref="formRef">
            <Title class="titlebar">
                <template #title>
                    信息填写
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item required label="是否已审计" name="audit">
                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                            v-model:value="formData.audit"
                            class="w_full"
                            placeholder="请选择"
                            :options="dict.options('SHI_FOU')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="报告周期" name="months">
                        <a-range-picker
                            picker="month"
                            v-model:value="formData.months"
                            valueFormat="YYYY-MM-DD 00:00:00"
                            format="YYYY-MM"
                            class="w_full"
                            :placeholder="['开始月份','结束月份']" />
                    </a-form-item>
                </a-col>
            </a-row>
            <Title class="titlebar">
                <template #title>
                    资产负债信息
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item required label="总资产（万元）" name="totalAssets">
                        <a-input-number v-model:value="formData.totalAssets" :min="0" class="w_full"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="净资产（万元）" name="netAssets">
                        <a-input-number v-model:value="formData.netAssets" :min="0" class="w_full"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"/>
                    </a-form-item>
                </a-col>
            </a-row>
            <CompanyDocuments v-if="visible" ref="documentRef" :companyId="companyId" :menuId="57"/>

            <Title class="titlebar">
                <template #title>
                    利润信息
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item required label="利润总额（万元）" name="totalProfit">
                        <a-input-number v-model:value="formData.totalProfit" :min="0" class="w_full"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="净利润（万元）" name="netProfit">
                        <a-input-number v-model:value="formData.netProfit" :min="0" class="w_full"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item required label="营业收入本年累计" name="accumulatedOperating">
                    <a-input-number v-model:value="formData.accumulatedOperating" :min="0" class="w_full"
                                    :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                    :parser="value => value.replace(/\\s?|(,*)/g, '')"/>
                  </a-form-item>
                </a-col>
            </a-row>
            <CompanyDocuments v-if="visible" ref="documentRef2" :companyId="companyId" :menuId="58"/>

            <Title class="titlebar">
                <template #title>
                    现金流信息
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item required label="期初现金与现金等价物余额（万元）" name="beginCashFlow">
                        <a-input-number v-model:value="formData.beginCashFlow" :min="0" class="w_full"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="期末现金与现金等价物余额（万元）" name="endCashFlow">
                        <a-input-number v-model:value="formData.endCashFlow" :min="0" class="w_full"
                        :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                        :parser="value => value.replace(/\\s?|(,*)/g, '')"/>
                    </a-form-item>
                </a-col>
            </a-row>
            <CompanyDocuments v-if="visible" ref="documentRef3" :companyId="companyId" :menuId="59"/>
        </a-form>
    </a-drawer>
</template>
<script setup>
import api              from '@/api/index';
import { message  }     from 'ant-design-vue';
import {amountUnit}     from '@/utils/tools';
import { mainStore }    from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict  = useDictStore();
const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 43,
    }
})

const emit         = defineEmits(['success'])
const visible      = ref(false);
const title        = ref('');
const formData     = ref({});
const formRef      = ref(null);
const documentRef  = ref(null);
const documentRef2 = ref(null);
const documentRef3 = ref(null);
const handleOk     = ()=>{
    formRef.value.validateFields().then(async vRes=>{
        formData.value.companyId = props.companyId;
        formData.value.beginTime = formData.value.months[0];
        formData.value.endTime   = formData.value.months[1];

        store.spinChange(1);

        let res = await api.investment.correlationAdd(formData.value,'companyFinance');
        formData.value.financeId = res.data.id;

        let arr = [{menuId:57,key:'companyFinanceAssets'},{menuId:58,key:'companyFinanceProfit'},{menuId:59,key:'companyFinanceCashFlow'}];
        for (var i = 0; i < arr.length; i++) {
            await readySubmit(arr[i]);
        }
        store.spinChange(0);
        emit('success');
        message.success('操作成功');
        visible.value = false;
    }).catch(err=>{
        store.spinChange(0);
        message.warning('请完善必填信息！');
    })
}
const readySubmit = (obj)=>{
    return new Promise((resolve, reject)=>{
        api.investment.correlationAdd(formData.value,obj.key).then(async res=>{
            if(res.code==200){
                if(obj.menuId==57){
                    let uploadResult = await documentRef.value.batchUpLoad(res.data.id);
                }
                if(obj.menuId==58){
                    let uploadResult = await documentRef2.value.batchUpLoad(res.data.id);
                }
                if(obj.menuId==59){
                    let uploadResult = await documentRef3.value.batchUpLoad(res.data.id);
                }
                resolve();
            }else{
                store.spinChange(-1);
                reject('异常');
            }
        }).catch(()=>{
            store.spinChange(-1);
            reject('异常');
        })
    })
}
const handleClose = ()=>{
    visible.value = false;
}
const open     = ()=>{
    formData.value = {};
    title.value    = '财务信息录入';
    visible.value  = true;
}
defineExpose({open})
</script>
<style scoped lang="less">
.titlebar{
    margin : 16px -16px;
    &:first-child{
        margin-top : -16px;
    }
}
</style>
