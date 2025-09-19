<template>
    <div class="content-inner">
        <a-page-header :ghost="false"
            :breadcrumb="{ routes }">
            <template #title>
                <EllipsisTooltip style="width:500px" class="flex_full" :content="companyId?formData.projectName:'新增项目'"/>
            </template>
        </a-page-header>
        <AScrollbar style="margin:-16px -16px 0 -16px;">
            <div class="padding_box">
                <a-form layout="vertical" :model="formData" ref="formRef">
                    <a-collapse ghost expandIconPosition="right" v-model:activeKey="collapseKey" class="crl-collapse">
                        <a-collapse-panel key="base">
                            <template #header>
                                <h5 class="title_single">基础信息</h5>
                                <span class="color-danger">*</span>
                            </template>
                            <a-row :gutter="24">
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="企业名称" name="name">
                                        <a-input allowClear v-model:value="formData.name"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="省市区" name="areaCode">
                                        <AddressSelect
                                            @change="addressChange"
                                            v-model:provinceCode="formData.provinceCode"
                                            v-model:cityCode="formData.cityCode"
                                            v-model:areaCode="formData.areaCode" mode="area"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="所属街道" name="streetCode">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                          v-model:value="formData.streetCode"
                                          class="w_full"
                                          :placeholder="formData.areaCode?'请选择街道':'请先选择省市区'"
                                          :options="streetList">
                                        </a-select>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="注册地址" name="incorporationAddress">
                                        <a-input allowClear v-model:value="formData.incorporationAddress" placeholder="请输入"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item label="企业人数(合同员工)" name="personnel">
                                        <a-input-number v-model:value="formData.personnel" :min="0" class="w_full"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="注册资本（万元）" name="registeredCapital">
                                        <a-input-number v-model:value="formData.registeredCapital" :min="0" class="w_full" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="行业分类" name="industryType">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                          v-model:value="formData.industryType"
                                          class="w_full"
                                          placeholder="请选择"
                                          :options="dict.options('HANG_YE_LEI_XING')">
                                        </a-select>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="成立日期" name="incorporationTime">
                                        <a-date-picker :getPopupContainer="trigger => trigger.parentNode"
                                            v-model:value="formData.incorporationTime"
                                            class="w_full"
                                            valueFormat="YYYY-MM-DD 00:00:00"
                                            format="YYYY-MM-DD"
                                            placeholder="请选择" />
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="统一社会信用代码" name="companyNo"
                                    :rules="[{required:true,validator:companyNoRule}]">
                                        <a-input allowClear v-model:value="formData.companyNo"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="法定代表人" name="legalRepresentative">
                                        <a-input allowClear v-model:value="formData.legalRepresentative"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="董事长/执行董事" name="chairman">
                                        <a-input allowClear v-model:value="formData.chairman"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="是否属于中小企业" name="smallCompany">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                          v-model:value="formData.smallCompany"
                                          class="w_full"
                                          placeholder="请选择"
                                          :options="dict.options('SHI_FOU')">
                                        </a-select>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="是否属于高新技术企业" name="hightechCompany">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                          v-model:value="formData.hightechCompany"
                                          class="w_full"
                                          placeholder="请选择"
                                          :options="dict.options('SHI_FOU')">
                                        </a-select>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="是否享受国家财税政策" name="taxPolicy">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                          v-model:value="formData.taxPolicy"
                                          class="w_full"
                                          placeholder="请选择"
                                          :options="dict.options('SHI_FOU')">
                                        </a-select>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="投资类型" name="investmentType">
                                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                                          v-model:value="formData.investmentType"
                                          class="w_full"
                                          placeholder="请选择"
                                         :options="dict.options('TOU_ZI_LEI_XING')">
                                        </a-select>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="持股比例（%）" name="shareholdingRatio">
                                        <a-input-number v-model:value="formData.shareholdingRatio" :min="0" :max="100" class="w_full"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="投后负责人" name="principalId">
                                        <UserSelect v-model="formData.principalId" :users="formData.principal"/>
                                    </a-form-item>
                                </a-col>
                                <a-col :xxl="6" :lg="8" :sm="12">
                                    <a-form-item required label="业务所属部门" name="businessDeptName">
                                        <!-- <DeptSelect v-model="formData.businessDeptId"/> -->
                                        <a-input allowClear v-model:value="formData.businessDeptName" placeholder="请输入"/>
                                    </a-form-item>
                                </a-col>
                            </a-row>
                            <a-form-item label="企业概况" name="history">
                                <a-textarea allowClear :rows="3" type="textarea" v-model:value="formData.history" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                            </a-form-item>
                            <a-form-item label="经营范围" name="managementRange">
                                <a-textarea allowClear :rows="3" type="textarea" v-model:value="formData.managementRange" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                            </a-form-item>
                            <a-form-item required label="主营业务" name="mainBusiness">
                                <a-textarea allowClear :rows="3" type="textarea" v-model:value="formData.mainBusiness" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                            </a-form-item>
                            <a-form-item label="核心竞争力描述" name="coreCompetitiveness">
                                <a-textarea allowClear :rows="3" type="textarea" v-model:value="formData.coreCompetitiveness" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                            </a-form-item>
                            <a-form-item label="重点关注" name="importantFollow">
                                <a-textarea allowClear :rows="3" type="textarea" v-model:value="formData.importantFollow" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                            </a-form-item>
                            <a-form-item label="备注" name="remark">
                                <a-textarea allowClear :rows="3" type="textarea" v-model:value="formData.remark" placeholder="请输入(100字以内)" show-count :maxlength="100"/>
                            </a-form-item>
                        </a-collapse-panel>

                        <a-collapse-panel key="gaoguan">
                            <template #header>
                                <h5 class="title_single">董监高信息</h5>
                            </template>
                            <Executives :companyId="companyId"/>
                        </a-collapse-panel>


                        <a-collapse-panel key="zhangchen">
                            <template #header>
                                <h5 class="title_single">章程及工商执照上传</h5>
                            </template>
                            <CompanyDocuments :companyId="companyId" :menuId="37" :recordId="companyId" type="GONGSHAN_XINXI">
                                <template #linkDoc_0>
                                    <FileZhangchen :projectId="formData.projectId" v-if="formData.projectId" />
                                </template>
                                <template #linkDoc_1>
                                    <CompanyFileLink :companyId="companyId" :documentTemplateId="2"/>
                                </template>
                                <template #linkDoc_2>
                                    <CompanyFileLink :companyId="companyId" :documentTemplateId="3"/>
                                </template>
                            </CompanyDocuments>
                        </a-collapse-panel>

                        <a-collapse-panel key="subProject">
                            <template #header>
                                <h5 class="title_single">子项目</h5>
                            </template>
                            <SubProject :companyId="companyId"/>
                        </a-collapse-panel>

                        <a-collapse-panel key="xinxipinzhen">
                            <template #header>
                                <h5 class="title_single">信息变更凭证上传
                                    <a-form-item name="fileOk"
                                    :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                                    </a-form-item>
                                </h5>
                            </template>
                            <CompanyDocuments  v-model="formData.fileOk" :companyId="companyId" :menuId="37" :recordId="companyId" type="XINXI_BIANGEN"/>
                        </a-collapse-panel>
                    </a-collapse>
                </a-form>
            </div>
        </AScrollbar>
        <FooterBar>
            <a-button size="large" @click="router.back()">取消</a-button>
            <a-button
                size="large"
                type="primary"
                @click="submit"
                :loading="submitLoading" :disabled="!hasPermission(['biz:projectCompany:edit'],'investment')">提交</a-button>
        </FooterBar>
    </div>
</template>
<script setup>
import api                                   from '@/api/index';
import Executives                            from './components/correlation/Executives.vue'
import SubProject                            from './components/correlation/SubProject.vue'
import FileZhangchen                         from './components/FileZhangchen.vue'
import {amountUnit , numFixed,hasPermission} from '@/utils/tools';
import { Modal,message }                     from 'ant-design-vue';
import { useDictStore }                      from '@/store/dict';
import { mainStore }                         from '@/store';
const store     = mainStore();
const dict      = useDictStore();
const routes    = [
    {
        breadcrumbName : '投后管理',
        path           : '/investment'
    },
    {
        breadcrumbName: '子公司信息编辑',
    },
];
const router    = useRouter();
const route     = useRoute();
const companyId = ref(Number(route.query.id || 0))

const collapseKey    = ref(['base','zhaobiao','xinxipinzhen']);
const formData       = ref({roleKeys:[]});
const formRef        = ref(null);
const submitLoading  = ref(false);
const homePageReload = inject('homePageReload');
const submit         = ()=>{
    formRef.value.validateFields().then(vRes=>{
        submitLoading.value = true;
        api.investment.correlationEdit(formData.value,'projectCompany').then(res=>{
            submitLoading.value = false;
            if(res.code==200){
                router.back()
                message.success('操作成功');
            }
        })
    }).catch(err=>{
        collapseKey.value = ['base'];
        message.warning('请完善信息！');
    })
}

const getInfo  = async ()=>{
    let res = await api.investment.correlationGet(companyId.value,'projectCompany');
    if(res.code==200){
        formData.value = res.data;
        if(res.data.areaCode){
            getStreet(res.data.areaCode);
        }
    }
}
onMounted(() => {
    if(companyId.value){
        getInfo();
    }else{
    }
})

const addressChange = (areaCode)=>{
    if(areaCode){
        getStreet(areaCode);
    }
    formData.value.streetCode    = null;
    formData.value.streetZipcode = null;
}
const streetList = ref([]);
const getStreet = (areaCode)=>{
    api.common.listStreetByArea(areaCode).then(res=>{
        if(res.code==200){
            streetList.value = res.data.map(item=>{
                return {
                    label   : item.name+" ("+item.zipCode+")",
                    value   : item.areaCode,
                    zipCode : item.zipCode,
                }
            });
        }
    })
}

const userSelect = async (user)=>{
    if(formData.value.companyId){
        return;
    }
    formData.value.companyId = store.deptPost.deptId;
}
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
