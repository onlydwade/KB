<template>
    <a-form layout="vertical" :model="formData" ref="formRef">
        <a-collapse ghost expandIconPosition="right" v-model:activeKey="collapseKey" class="crl-collapse">
            <a-collapse-panel key="base">
                <template #header>
                    <h5 class="title_single">基础信息</h5>
                    <span class="color-danger">*</span>
                </template>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="客户全称" name="customerName">
                            <a-input allowClear v-model:value="formData.customerName"/>
                            <p class="form_item_extra">对于企业客户，请准确区分企业总部/分公司/某地办事处等</p>
                        </a-form-item>
                    </a-col>
   
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="客户来源"   name="source">
                            <a-select disabled :getPopupContainer="trigger => trigger.parentNode"
                              v-model:value="formData.source"
                              class="w_full"
                              placeholder="请选择"
                              :options="dict.options('KE_HU_LAI_YUAN')">
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="企业类型" required name="companyType">
                            <a-select :getPopupContainer="trigger => trigger.parentNode"
                              v-model:value="formData.companyType"
                              class="w_full"
                              placeholder="请选择"
                              :options="dict.options('QI_YE_LEI_XING')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item  label="跟进类型" name="customerLevel">
                            <a-select :getPopupContainer="trigger => trigger.parentNode"
                              v-model:value="formData.customerLevel"
                              class="w_full"
                              placeholder="请选择"
                              :options="dict.options('KE_HU_DENG_JI')">
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="合作类型" name="cooperationType">
                            <!-- <a-select :getPopupContainer="trigger => trigger.parentNode"
                              v-model:value="formData.cooperationType"
                              class="w_full"  
                              placeholder="请选择"
                              :options="dict.options('HE_ZUO_LEI_XING')">
                            </a-select> -->
                            <MultipleDicts v-model="formData.cooperationType" dictKey="HE_ZUO_LEI_XING"/>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="所属行业" name="customerIndustry">
                            <a-select :getPopupContainer="trigger => trigger.parentNode"
                              v-model:value="formData.customerIndustry"
                              class="w_full"
                              placeholder="请选择"
                              :options="dict.options('SUO_SHU_XING_YE')">
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="客户类型" name="customerType">
                            <a-select :getPopupContainer="trigger => trigger.parentNode"
                              v-model:value="formData.customerType"
                              class="w_full"
                              placeholder="请选择"
                              :options="dict.options('KE_HU_FEN_LEI')">
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item  label="统一社会信用代码" name="customerCompanyNo"
                        :rules="[{required:formData.customerType=='QI_YE_JI_KE_HU',validator:companyNoRule}]">
                            <a-input allowClear v-model:value="formData.customerCompanyNo" placeholder="请输入"/>
                        </a-form-item>
                    </a-col>



                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="客户跟进人"  name="followUserId">
                            <UserSelect v-model="formData.followUserId" :users="formData.followUserVO"/>
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="信息维护人" name="maintenanceUserId">
                            <UserSelect v-model="formData.maintenanceUserId" :users="formData.maintenanceUserVO"/>
                        </a-form-item>
                    </a-col>   
<!--  
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="单位负责人" name="ownerUserId">
                            <UserSelect v-model="formData.ownerUserId" :users="formData.ownerUserVO"/>
                        </a-form-item>
                    </a-col> -->
                    <a-col :xxl="6" :lg="8" :sm="12">
                    <a-form-item label="客户标签" name="keywords">
                        <KeyWords v-model="formData.keywords" :type="4" :autoAdd="true"/>
                    </a-form-item> 
                    </a-col>           
                    <a-col :xxl="6" :lg="8" :sm="12" >
                        <a-form-item label="是否为简报客户" name="workBrief"  style="margin-left:10px;"  > 
                            <a-switch v-model:checked="formData.workBrief"  :disabled="!hasPermission(['biz:customer:workBrief'])" checkedValue="SHI" checked-children="是" unCheckedValue="FOU" un-checked-children="否" />
                        </a-form-item>   
                    </a-col>
                   
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="客户所属省市区县" name="areaCode">
                            <AddressSelect 
                                v-model:provinceCode="formData.provinceCode"
                                v-model:cityCode="formData.cityCode"
                                v-model:areaCode="formData.areaCode" mode="area"/>
                                <p class="form_item_extra">对于企业类客户，请填写客户全称对应的所在省市区</p>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="客户详细地址" name="address">
                            <a-input allowClear v-model:value="formData.address" placeholder="请输入"/>
                        </a-form-item>
                    </a-col>
  
                </a-row>
                   
                <a-form-item label="备注" name="remark">
                    <a-textarea allowClear :rows="3" type="textarea" v-model:value="formData.remark" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
                </a-form-item>

         
              
             </a-collapse-panel>
        </a-collapse>
        <a-collapse ghost expandIconPosition="right" v-model:activeKey="collapseKeycompany" class="crl-collapse">
            <a-collapse-panel key="company">
                <template #header>
                    <h5 class="title_single">企业信息</h5>
                    <!-- <span class="color-danger">*</span> -->
                </template>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="法人代表" name="legalEntity">
                            <a-input  maxlength="200" allowClear v-model:value="formData.legalEntity" placeholder="请输入"/>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="注册资本" name="registeredCapital">
                            <a-input  maxlength="200" allowClear v-model:value="formData.registeredCapital" placeholder="请输入"/>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="人员规模" name="personnelSize">
                            <a-input  maxlength="200" allowClear v-model:value="formData.personnelSize" placeholder="请输入"/>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="成立时间" name="establishmentDate">    
                            <a-date-picker 
                                v-model:value="formData.establishmentDate"
                                class="w_full"
                                valueFormat="YYYY-MM-DD 00:00:00"
                                format="YYYY-MM-DD"
                                placeholder="请选择" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="公司官网" name="website">
                            <a-input  maxlength="200" allowClear v-model:value="formData.website" placeholder="请输入"/>
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="18" :lg="12" :sm="18">
                        <a-form-item label="注册地址" name="registeredAddress">
                            <a-input  maxlength="200" allowClear v-model:value="formData.registeredAddress" placeholder="请输入"/>
                        </a-form-item>
                    </a-col> 
                </a-row>
                <a-form-item label="公司简介" name="companyIntroduction">
                            <a-textarea allowClear :rows="3" type="textarea" v-model:value="formData.companyIntroduction" placeholder="请输入(3000字以内)" show-count :maxlength="3000"/>
                        </a-form-item>
            </a-collapse-panel>
        </a-collapse>
        <a-collapse ghost expandIconPosition="right" v-model:activeKey="collapseKeycontact" class="crl-collapse">
            <a-collapse-panel key="contact">
                <template #header>
                    <h5 class="title_single">联系人</h5>
                </template>
                <Contacts ref="listRef"  :customerId="customerId" />
            </a-collapse-panel>
        </a-collapse>
    </a-form>
</template>
<script setup>
import api              from '@/api/index';
import { message }      from 'ant-design-vue';
import { useCityStore } from '@/store/city';
import { useDictStore } from '@/store/dict';
import Contacts         from './Contacts.vue';
import {hasPermission} from '@/utils/tools';
const dict      = useDictStore();
const cityStore = useCityStore();
const emit      = defineEmits(['update:submitLoading','success'])
const props     = defineProps({
    submitLoading : {
        type    : Boolean,
        default : false,
    },    
    companyName : {
        type    : String,
        default : "",
    },
    isQlm : {
        type    : Number,
        default : 0,
    },
    customerId : {
        type    : Number,
        default : 0,
    },
})
const formData    = ref({
    source: "ZI_JIAN"
});
const collapseKey = ref(['base']);
const collapseKeycompany = ref(['company']);
const collapseKeycontact = ref(['contact']);
const listRef = ref(null)
const getInfo  = ()=>{
    api.customer.customerInfo(props.customerId).then(res=>{
        if(res.code==200){
            formData.value            = res.data;
            formData.value.addressIds = [];
            if(res.data.provinceCode&&res.data.cityCode){
                formData.value.addressIds = [res.data.provinceCode,res.data.cityCode];
            }
        }
    })
}
//获取千里马客户信息
const getQlmInfo  = ()=>{
    debugger
    api.customer.getQlmCompany(props.companyName).then(res=>{
        if(res.code==200||res.code==500){
            //message.success('解锁成功');             
            formData.value            = res.data;
            formData.value.source="YING_BIAO_BAO";
            formData.value.customerName=props.companyName
        }
    })
}

onMounted(() => {
    if(props.isQlm>0){
        getQlmInfo()
    }else{
        if(props.customerId){
            getInfo();
        }
    }
})
const list = ref(null);
const formRef = ref(null);
const submit  = ()=>{
    formRef.value.validateFields().then(vRes=>{
        emit('update:submitLoading',true);
        formData.value.customerContactsList= listRef.value.list;
        let apiAttr = props.customerId?'customerEdit':'customerAdd';
        api.customer[apiAttr](formData.value).then(res=>{
            emit('update:submitLoading',false);
            if(res.code==200){
                emit('success',res.data);
            }
        })
    }).catch(err=>{
        collapseKey.value = ['base'];
        message.warning('请完善必填信息！');
    })
}
defineExpose({formData,submit})

 

const companyNoRule = (rule, value, callback)=>{
    if(rule.required && (value || '').length!=18){
        return new Promise((resolve, reject) => {
            reject('请输入有效社会信用代码(18位)')
        });
    }
    return Promise.resolve();
}
</script>
<style scoped lang="less">
</style>