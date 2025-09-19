<template>
    <a-form layout="vertical" :model="formData" ref="formRef">
        <div class="padding_box">
            <a-alert v-if="!companyId" message="基础信息提交保存后再完善“公司章程文件”,“股权”，“成员”，“项目”等信息" closable type="warning" />
            <a-collapse ghost expandIconPosition="right" v-model:activeKey="collapseKey" class="crl-collapse">
                <a-collapse-panel key="base">
                    <template #header>
                        <h5 class="title_single">基础信息</h5>
                        <span class="color-danger">*</span>
                    </template>
                    <a-row :gutter="24">
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="企业名称" name="name">
                                <a-input :disabled="disabled" allowClear v-model:value="formData.name" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="省市区" name="areaCode">
                                <AddressSelect :disabled="disabled" @change="addressChange"
                                    v-model:provinceCode="formData.provinceCode" v-model:cityCode="formData.cityCode"
                                    v-model:areaCode="formData.areaCode" mode="area" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="所属街道" name="streetCode">
                                <a-select :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                    v-model:value="formData.streetCode" class="w_full"
                                    :placeholder="formData.areaCode ? '请选择街道' : '请先选择省市区'" :options="streetList">
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="注册地址" name="incorporationAddress">
                                <a-input :disabled="disabled" allowClear v-model:value="formData.incorporationAddress"
                                    placeholder="请输入" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="企业人数(合同员工)" name="personnel">
                                <a-input-number :disabled="disabled" v-model:value="formData.personnel" :min="0"
                                    class="w_full" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="注册资本（万元）" name="registeredCapital">
                                <a-input-number :disabled="disabled" v-model:value="formData.registeredCapital" :min="0"
                                    class="w_full" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="行业分类" name="industryType">
                                <a-select :disabled="disabled" :getPopupContainer="trigger => trigger.parentNode"
                                    v-model:value="formData.industryType" class="w_full" placeholder="请选择"
                                    :options="dict.options('HANG_YE_LEI_XING')">
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="成立日期" name="incorporationTime">
                                <a-date-picker :getPopupContainer="trigger => trigger.parentNode" :disabled="disabled"
                                    v-model:value="formData.incorporationTime" class="w_full"
                                    valueFormat="YYYY-MM-DD 00:00:00" format="YYYY-MM-DD" placeholder="请选择" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="统一社会信用代码" name="companyNo"
                                :rules="[{ required: true, validator: companyNoRule }]">
                                <a-input :disabled="disabled" allowClear v-model:value="formData.companyNo" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="法定代表人" name="legalRepresentative">
                                <a-input :disabled="disabled" allowClear v-model:value="formData.legalRepresentative" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="董事长/执行董事" name="chairman">
                                <a-input :disabled="disabled" allowClear v-model:value="formData.chairman" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="是否属于中小企业" name="smallCompany">
                                <a-select :getPopupContainer="trigger => trigger.parentNode" :disabled="disabled"
                                    v-model:value="formData.smallCompany" class="w_full" placeholder="请选择"
                                    :options="dict.options('SHI_FOU')">
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="是否属于高新技术企业" name="hightechCompany">
                                <a-select :getPopupContainer="trigger => trigger.parentNode" :disabled="disabled"
                                    v-model:value="formData.hightechCompany" class="w_full" placeholder="请选择"
                                    :options="dict.options('SHI_FOU')">
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="是否享受国家财税政策" name="taxPolicy">
                                <a-select :getPopupContainer="trigger => trigger.parentNode" :disabled="disabled"
                                    v-model:value="formData.taxPolicy" class="w_full" placeholder="请选择"
                                    :options="dict.options('SHI_FOU')">
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="投资类型" name="investmentType">
                                <a-select :getPopupContainer="trigger => trigger.parentNode" :disabled="disabled"
                                    v-model:value="formData.investmentType" class="w_full" placeholder="请选择"
                                    :options="dict.options('TOU_ZI_LEI_XING')">
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="持股比例（%）" name="shareholdingRatio">
                                <a-input-number :disabled="disabled" v-model:value="formData.shareholdingRatio" :min="0"
                                    :max="100" class="w_full" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-form-item label="企业概况" name="history">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea"
                            v-model:value="formData.history" placeholder="请输入(100字以内)" show-count :maxlength="100" />
                    </a-form-item>
                    <a-form-item label="经营范围" name="managementRange">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea"
                            v-model:value="formData.managementRange" placeholder="请输入(1000字以内)" show-count
                            :maxlength="1000" />
                    </a-form-item>
                    <a-form-item required label="主营业务" name="mainBusiness">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea"
                            v-model:value="formData.mainBusiness" placeholder="请输入(100字以内)" show-count :maxlength="100" />
                    </a-form-item>
                    <a-form-item label="核心竞争力描述" name="coreCompetitiveness">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea"
                            v-model:value="formData.coreCompetitiveness" placeholder="请输入(100字以内)" show-count
                            :maxlength="100" />
                    </a-form-item>
                    <a-form-item label="重点关注" name="importantFollow">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea"
                            v-model:value="formData.importantFollow" placeholder="请输入(100字以内)" show-count
                            :maxlength="100" />
                    </a-form-item>
                    <a-form-item label="备注" name="remark">
                        <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea"
                            v-model:value="formData.remark" placeholder="请输入(100字以内)" show-count :maxlength="100" />
                    </a-form-item>
                </a-collapse-panel>

                <!-- <template v-if="companyId"> -->
                <a-collapse-panel key="wenjian">
                    <template #header>
                        <h5 class="title_single">章程及工商执照上传
                            &nbsp;&nbsp;
                            <span class="color-danger">*</span>
                            &nbsp;&nbsp;
                            <a-form-item name="fileOk" :rules="{ required: true, message: '( 请上传必传文件后提交!!! )', }">
                            </a-form-item>
                        </h5>
                    </template>
                    <companyDocuments :projectId="projectId" v-model="formData.fileOk" :companyId="companyId"
                        ref="companyDocumentsRef" :readOnly="disabled" :key="companyId" />
                </a-collapse-panel>

                <a-collapse-panel key="guquan">
                    <template #header>
                        <h5 class="title_single">股权信息
                            <span class="color-danger">*</span>
                        </h5>
                    </template>
                    <Shareholder :projectId="projectId" ref="shareholderRef" :companyId="companyId" :readOnly="disabled"
                        :columnsType="1" />
                </a-collapse-panel>


                <a-collapse-panel key="gaoguan">
                    <template #header>
                        <h5 class="title_single">董监高信息
                            <span class="color-danger">*</span>
                        </h5>
                    </template>
                    <Executives ref="executivesRef" :projectId="projectId" :companyId="companyId" :readOnly="disabled"
                        :columnsType="1" />
                </a-collapse-panel>


                <a-collapse-panel key="team">
                    <template #header>
                        <h5 class="title_single">项目成员
                            <span class="color-danger">*</span>
                            <span class="color-danger" style="margin-left: 10px;">注：财务对接人必填</span>
                        </h5>
                    </template>
                    <Team ref="teamRef" :projectId="projectId" type="TOU" :readOnly="disabled" />
                </a-collapse-panel>


                <a-collapse-panel key="pool">
                    <template #header>
                        <h5 class="title_single">子项目</h5>
                    </template>
                    <SubProject :projectId="projectId" :companyId="companyId" :readOnly="disabled" />
                </a-collapse-panel>
                <!-- </template> -->
            </a-collapse>
        </div>
    </a-form>
</template>
<script setup>
import api from '@/api/index';
import { message } from 'ant-design-vue';
import { useCityStore } from '@/store/city';
import { useDictStore } from '@/store/dict';
import { formDataFill, checkArrKeys } from '@/utils/tools';
import companyDocuments from './companyDocuments.vue';
import Shareholder from './Shareholder.vue';
import Executives from './Executives.vue'
import Team from './Team.vue'
import SubProject from './SubProject.vue'
const dict = useDictStore();
const cityStore = useCityStore();
const emit = defineEmits(['update:submitLoading', 'success'])
const props = defineProps({
    projectId: {
        type: Number,
        default: 0,
    },
    companyId: {
        type: Number,
        default: 0,
    },
    disabled: {
        type: Boolean,
        default: false,
    },
    projectType: {
        type: String,
        default: '',
    },
    data: {
        type: Object,
        default: {}
    },
})
const executivesRef = ref(null)
const shareholderRef = ref(null)
const teamRef = ref(null)
const formData = ref({});
const collapseKey = ref(['base', 'wenjian', 'guquan', 'gaoguan', 'team']);

const getInfo = () => {
    api.project.correlationGet(props.companyId, 'projectCompany').then(res => {
        if (res.code == 200) {
            formData.value = res.data;

            formData.value.smallCompany = formData.value.smallCompany == 1 ? 'SHI' : 'FOU';
            formData.value.hightechCompany = formData.value.hightechCompany == 1 ? 'SHI' : 'FOU';
            formData.value.taxPolicy = formData.value.taxPolicy == 1 ? 'SHI' : 'FOU';
            if (res.data.areaCode) {
                getStreet(res.data.areaCode);
            }
        }
    })
}
// const formDataDefault = {
//     name:'',  //企业名称
//     areaCode:'', //省市区
//     streetCode:'', //所属街道
//     incorporationAddress:'' ,//注册地址
//     personnel:'',//企业人数(合同员工)
//     registeredCapital:'',//注册资本（万元）
//     incorporationTime:'',//成立日期
//     companyNo:'',//统一社会信用代码
//     legalRepresentative:'',//法定代表人
// }
const projectInfo = () => {
    api.project.projectInfo(props.projectId).then(res => {
        if (res.code == 200) {
            formData.value.name = res.data.targetCompanyName;
            formData.value.areaCode = res.data.areaCode;
            formData.value.provinceCode = res.data.provinceCode;
            formData.value.cityCode = res.data.cityCode;
            formData.value.streetCode = res.data.streetCode;
            formData.value.incorporationAddress = res.data.address;
            formData.value.personnel = res.data.targetPersonnel;
            formData.value.registeredCapital = res.data.targetRegisteredCapital;
            formData.value.incorporationTime = res.data.targetIncorporationTime;
            formData.value.companyNo = res.data.targetCompanyNo;
            formData.value.legalRepresentative = res.data.targetLegalRepresentative;
            if (res.data.areaCode) {
                getStreet(res.data.areaCode);
            }

        }
    })
}
onMounted(() => {
    if (props.companyId) {
        getInfo();
    } else {
        projectInfo()
    }
})

const formRef = ref(null);
const companyDocumentsRef = ref(null)
const projectCompanyDocumentInfo = ref({ ////章程及工商执照上传所有文档信息
    charterDocumentIds: [],///盖章版章程上传文件列表，以文档ID为传递，多个文档多个ID组成数组
    businessLicenseDocumentIds: [],//工商执照上传文件列表，以文档ID为传递，多个文档多个ID组成数组
    otherDocumentIds: [],////其他文件上传文件列表，以文档ID为传递，多个文档多个ID组成数组
})

const submit = () => {
    formRef.value.validateFields().then(vRes => {
        if (props.projectType === 'GU_QUAN_HE_ZUO_XIANG_MU') {
            companyDocumentsRef.value.tableList.forEach(item => {
                if (item.operName.indexOf('盖章版章程') > -1 && item.projectDocumentList.length > 0) {
                    projectCompanyDocumentInfo.value.charterDocumentIds = item.projectDocumentList.map(t => { return t.id })
                } else if (item.operName.indexOf('工商执照') > -1 && item.projectDocumentList.length > 0) {
                    projectCompanyDocumentInfo.value.businessLicenseDocumentIds = item.projectDocumentList.map(t => { return t.id })
                } else if (item.operName.indexOf('其他文件') > -1 && item.projectDocumentList.length > 0) {//其他
                    projectCompanyDocumentInfo.value.otherDocumentIds = item.projectDocumentList.map(t => { return t.id })
                }
            })
            if (shareholderRef.value.list.length == 0) {
                message.warning('股权信息不能为空！');
                return
            } else {
                let shareholderKeys = ['name', 'type', 'companyNo', 'currencyType', 'rate', 'investmentAmount', 'investmentEndTime',
                    'actualInvestmentAmount']
                if (checkArrKeys(shareholderRef.value.list, shareholderKeys)) {
                    message.warning('股权信息的必填字段未填写,请先编辑填写！');
                    return
                }
            }
            if (executivesRef.value.list.length == 0) {
                message.warning('董监高信息不能为空！');
                return
            } else {
                let executiveKeys = ['name', 'positionStr', 'personnelAttributionStr']
                if (checkArrKeys(executivesRef.value.list, executiveKeys)) {
                    message.warning('董监高信息的必填字段未填写,请先编辑填写！');
                    return
                }
            }
            const filteredTeams = teamRef.value.list.filter(team => team.roleKey == 'CAI_WU_FU_ZE_REN');
            if (filteredTeams.length == 0) {
                message.warning('财务对接人不能为空！');
                return
            }
        }
        let apiAttr = props.companyId ? 'correlationEdit' : 'correlationAdd';
        formData.value.projectId = props.projectId
        formData.value.projectCompanyDocumentInfo = projectCompanyDocumentInfo.value
        api.project[apiAttr](formData.value, 'projectCompany').then(res => {
            if (res.code == 200) {
                emit('success', res.data);
            }
        })
    }).catch(err => {
        message.warning('请完善必填信息！');
    })
}
defineExpose({ submit })


const addressChange = (areaCode) => {
    if (areaCode) {
        getStreet(areaCode);
    }
    formData.value.streetCode = null;
    formData.value.streetZipcode = null;
}
const streetList = ref([]);
const getStreet = (areaCode) => {
    api.common.listStreetByArea(areaCode).then(res => {
        if (res.code == 200) {
            streetList.value = res.data.map(item => {
                return {
                    label: item.name + " (" + item.zipCode + ")",
                    value: item.areaCode,
                    zipCode: item.zipCode,
                }
            });
        }
    })
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
:deep(.ant-collapse-item) {
    border: 1px solid #ddd;
    margin-top: 16px;
    margin-bottom: 16px !important;
    box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
}
</style>
