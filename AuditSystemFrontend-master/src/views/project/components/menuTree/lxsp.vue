<template>
    <div class="menu_inner">
        <a-form ref="formRef" layout="vertical" :model="formData">
            <div class="padding_box">
                <Title>
                    <template #title>
                        项目基础信息
                        <!-- <span class="color-danger"> * </span> -->
                    </template>
                </Title>
                <a-row :gutter="24">

                  <template v-if="checkList">
                    <a-col :xxl="5" :lg="7" :sm="11">
                      <a-form-item label="项目名称">
                        <a-input disabled :value="formData.projectName" placeholder="" />
                      </a-form-item>
                    </a-col>
                    <a-col :xxl="1" :lg="1" :sm="1" >
                      <a-form-item label=" " >
                        <a-popover title="相似项目">
                          <template #content>
                            <a-list item-layout="horizontal" :data-source="checkList">
                              <template #renderItem="{ item }">
                                <a-list-item>{{ '【' + item.projectNo + '】,【' + item.projectName +'】,【' + item.companyName+'】,【' + item.createUser.realname + '】'}}</a-list-item>
                              </template>
                            </a-list>
                          </template>
                          <img  style="width: 32px;height:32px;align-items: center;" class="gth" src="/images/icon-gth.png" alt="" />
                        </a-popover>
                      </a-form-item>
                    </a-col>
                  </template>
                  <template v-else>
                    <a-col :xxl="6" :lg="8" :sm="12">
                      <a-form-item label="项目名称">
                        <a-input disabled :value="formData.projectName" placeholder="" />
                      </a-form-item>
                    </a-col>
                  </template>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="所属大区">
                            <a-input disabled :value="formData.regionName" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="归属单位">
                            <a-input disabled :value="formData.companyName" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="归属人">
                            <a-input disabled :value="(formData.attributorUser || {}).realname" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="项目优先级">
                            <a-input disabled :value="formData.projectLevelStr || ''" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                      <a-form-item label="业务板块" name="businessSegments">
                        <a-select disabled :getPopupContainer="trigger => trigger.parentNode"
                                  v-model:value="formData.businessSegments"
                                  class="w_full"
                                  placeholder="请选择"
                                  :options="dict.options('YE_WU_BAN_KUAI')">
                        </a-select>
                      </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="拓展模式">
                            <a-input disabled :value="formData.expansionModeStr || ''" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <template v-if="formData.projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="项目类型">
                                <a-input disabled :value="formData.projectTypeStr || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="目标公司名称">
                                <a-input disabled :value="formData.targetCompanyName || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="目标公司社会统一信用代码">
                                <a-input disabled :value="formData.targetCompanyNo || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="目标公司法定代表人">
                                <a-input disabled :value="formData.targetLegalRepresentative || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="目标公司类型">
                                <a-input disabled :value="formData.targetCompanyTypeStr || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="目标公司注册资本(万元)">
                                <a-input disabled :value="formData.targetRegisteredCapital" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="目标公司所属业态">
                                <a-input disabled :value="formData.targetBusinessTypeStr || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="目标公司成立日期">
                                <a-input disabled :value="formData.targetIncorporationTime" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="合作人员规模">
                                <a-input disabled :value="formData.targetPersonnel" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="合作模式">
                                <a-input disabled
                                    :value="(formData.cooperationTypeStr || '') + (formData.cooperationTypeOther || '')"
                                    placeholder="" />
                            </a-form-item>
                        </a-col>
                    </template>
                    <template v-else>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="是否招标">
                                <a-input disabled :value="formData.bidedStr || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="合作方式">
                                <a-input disabled :value="formData.cooperationModeStr || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="是否为续签项目">
                                <a-input disabled :value="formData.inStockStr || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="业态">
                                <a-input disabled :value="formData.businessTypeStr || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="服务内容">
                                <a-input disabled :value="formData.serviceContentStr || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12" v-if="(formData.serviceContent || '').includes('QI_TA')">
                            <a-form-item label="其它服务内容" name="serviceContentOther">
                                <a-input disabled  v-model:value="formData.serviceContentOther" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="关联客户">
                                <a-input disabled :value="(formData.customer || {}).name" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="建筑面积（m²）">
                                <a-input disabled :value="formData.constructionArea" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="预计进场时间">
                                <a-input disabled :value="dateFormat(formData.approachTime, 'YYYY-MM-DD')"
                                    placeholder="" />
                            </a-form-item>
                        </a-col>
                    </template>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="省份/城市/区/县">
                            <AddressSelect disabled v-model:provinceCode="formData.provinceCode"
                                v-model:cityCode="formData.cityCode" v-model:areaCode="formData.areaCode" mode="area" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="街道">
                            <a-input disabled :value="(formData.streetName || '')" placeholder="" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="详细地址">
                            <a-input disabled :value="formData.address" placeholder="" />
                        </a-form-item>
                    </a-col>

                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item label="项目预估金额（元）">
                            <a-input-number disabled :value="formData.bidingBudget" :min="0" class="w_full"
                                :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                :addon-after="amountUnit(formData.bidingBudget)" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="12" :lg="8" :sm="12">
                        <a-form-item label="项目来源">
                            <a-input disabled :value="formData.sourceName" placeholder="" />
                        </a-form-item>
                    </a-col>
                </a-row>
                <template v-if="projectType == 'GU_QUAN_HE_ZUO_XIANG_MU'">
                    <Title title="股权信息"></Title>
                    <Shareholder :projectId="projectId" :readOnly="true" />

                    <Title title="董监高信息"></Title>
                    <Executives :projectId="projectId" :readOnly="true" />

                    <Title title="目标公司项目信息"></Title>
                    <Pool :projectId="projectId" :readOnly="true" />
                </template>

                <template v-if="projectType == 'DAN_YI_TOU_BIAO_XIANG_MU'">
                    <Title title="投标报名"></Title>
                    <a-row :gutter="24">
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item required label="参标单位" name="bidPartInCompany">
                                <!-- <a-input :disabled="disabled" v-model:value="formData.bidPartInCompany" placeholder="请输入" /> -->
                                <a-select allowClear :disabled="disabled" show-search
                                    v-model:value="formData.bidPartInCompany" class="w_full" placeholder="请选择"
                                    @change="cbOrgChange" :options="cbOrgList">
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="参标主体（人力组织）" name="hrOrg">
                                <!-- <MainDeptSelect :loadding="dataLoadding" v-model="editData.hrOrg" :menuList="mainDeptHrTree" /> -->
                                <!-- <DeptSelect :disabled="disabled"   :noRoot="true" v-model="formData.hrOrg" /> -->
                                <a-tree-select :getPopupContainer="trigger => trigger.parentNode" disabled
                                    v-model:value="formData.hrOrg" show-search allow-clear style="width: 100%"
                                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }" tree-default-expand-all
                                    filterTreeNode treeNodeFilterProp="desc2" :field-names="{
            children: 'childs',
            label: 'desc2',
            value: 'code',
        }" :tree-data="mainDeptHrTree">
                                </a-tree-select>
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="标书购买金额（元）" name="tenderAmount">
                                <a-input-number :disabled="disabled" v-model:value="formData.tenderAmount" :min="0"
                                    class="w_full"
                                    :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                    :parser="value => value.replace(/\\s?|(,*)/g, '')"
                                    :addon-after="amountUnit(formData.tenderAmount)" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="是否推送报销系统" name="reimbursementStr">
                                <a-input disabled :value="formData.reimbursementStr" placeholder="" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item  label="招标编号" required name="bidingNo">
                                <a-input disabled :value="formData.bidingNo" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="招标类型">
                                <a-input disabled :value="formData.bidingModeStr || ''" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="发布日期">
                                <a-input disabled :value="dateFormat(formData.bidingPublishtime, 'YYYY-MM-DD')"
                                    placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="招标单位">
                                <a-input disabled :value="formData.bidingCompany" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="招标单位联系人">
                                <a-input disabled :value="formData.bidingCompanyContact" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="招标单位联系方式">
                                <a-input disabled :value="formData.bidingCompanyPhone" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="招标代理机构">
                                <a-input disabled :value="formData.bidingAgency" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="代理机构联系人">
                                <a-input disabled :value="formData.bidingAgencyContact" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="代理机构联系方式">
                                <a-input disabled :value="formData.bidingAgencyPhone" placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="投标截止时间" required name="bidingEndtime">
                                <a-input disabled :value="dateFormat(formData.bidingEndtime, 'YYYY-MM-DD')"
                                    placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                          <a-form-item label="开标时间" required name="bidingOpentime">
                                <a-input disabled :value="dateFormat(formData.bidingOpentime, 'YYYY-MM-DD')"
                                    placeholder="" />
                            </a-form-item>
                        </a-col>
                        <a-col :xxl="6" :lg="8" :sm="12">
                            <a-form-item label="招标网址">
                                <a-input disabled :value="formData.bidingWebsite" placeholder="" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                </template>
                <Title title="文件上传">
                    <a-form-item name="fileOk" :rules="{ required: true, message: '( 请上传必传文件后提交!!! )', }">
                    </a-form-item>
                </Title>
                <ProjectTreeDocuments :offlineApproval="menuInfo.offlineApproval" :menuId="menuId"
                    :projectId="projectId" v-model="formData.fileOk" ref="documentsRef" :readOnly="disabled" />
                <template v-if="projectType == 'DAN_YI_TOU_BIAO_XIANG_MU'">
                    <Title title="碧拓报名" style="margin-top:32px;"></Title>
                    <a-form-item required label="报名已通过" name="btApply"
                        :rules="[{ required: true, validator: btApplyRule }]">
                        <a-switch v-model:checked="formData.btApply" :disabled="disabled" checkedValue="SHI"
                            checked-children="是" unCheckedValue="FOU" un-checked-children="否" />
                    </a-form-item>
                </template>
            </div>
        </a-form>
        <ProjectActions @submit="submit" :menuInfo="menuInfo" ref="actionRef">
            <a-button size="large" @click="submit(2)"
                v-if="menuInfo.oaApproval != 1 && menuInfo.status == 0 && [0, 9].includes(menuInfo.approvalStatus) && !disabled">标记完成</a-button>
            <a-button size="large" @click="save"
                v-if="(menuInfo.status == 0 && [0, 5, 9].includes(menuInfo.approvalStatus) && projectType == 'DAN_YI_TOU_BIAO_XIANG_MU') || !disabled">暂存</a-button>
        </ProjectActions>
    </div>
</template>
<script setup>
import { amountUnit } from '@/utils/tools';
import { useMenuTree } from './menu';
import { useDictStore } from '@/store/dict';
import { message } from 'ant-design-vue';
import { mainStore } from '@/store';
// import MainDeptSelect    from '../../../sys/components/MainDeptSelect.vue';
import api from '@/api/index';
import Shareholder from '../correlation/Shareholder.vue';
import Executives from '../correlation/Executives.vue'
import Pool from '../correlation/Pool.vue'
const store = mainStore();
const dict = useDictStore();
const props = defineProps({
    projectId: {
        type: Number,
        default: 0,
    },
    menuId: {
        type: Number,
        default: 0,
    },
    menuInfo: {
        type: Object,
        default: {},
    }
})
const cbOrgList = ref([]);
const checkList = ref([]);

const mainDeptHrTree = ref([]);
const projectType = inject('getAutoParams')('projectType');
const formAttrs = computed(() => {
    return projectType.value == 'DAN_YI_TOU_BIAO_XIANG_MU' ? ['id', 'bidPartInCompany', 'tenderAmount', 'btApply', 'hrOrg'] : null;
});
const {
    formRef,
    documentsRef,
    formData,
    getInfo,
    // submit,
    save,
    disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs.value);
onMounted(() => {
    console.log('menuInfo', props.menuInfo)
    getInfo();
    getHrOrg();
    getCBOrgList();
})

onUpdated(() => {
  getCheckList();
});

const getCheckList = () => {
  api.project.projectDuplicateCheck(formData.value).then(res=>{
    if(res.code==200){
      checkList.value = res.data
    }
  })
}

const submit = (type, temp) => {
    //
    formRef.value.validateFields().then(vRes => {
        if (type == 1 && documentsRef.value) {
            let offlineStatus = documentsRef.value.getOfflineStatus();
            if (offlineStatus != 'success') {
                message.warning(offlineStatus);
                return;
            }
        }
        save(type, temp)
        // api.project.changeBidingAmount(props.projectId, formData.value.bidingAmount).then(res => {
        //     if (res.code == 200) {
        //         save(type, temp)
        //     }
        // })
    }).catch(err => {
        message.warning('请完善信息！');
    })
}
const getCBOrgList = () => {
    api.sys.getCBOrgList().then(res => {
        if (res.code == 200) {
            cbOrgList.value = res.data.map(item => {
                return {
                    label: item,
                    value: item,
                }
            });
        }
    })
}
const cbOrgChange = (val) => {
    api.sys.getHrOrgIdByOrgName(val).then(res => {
        if (res.code == 200) {
            formData.value.hrOrg = res.data
        }
    })
}
const getHrOrg = () => {
    api.sys.hrOrgTree().then(res => {
        if (res.code == 200) {

            mainDeptHrTree.value = res.data//deptArrayHR;
        }
    })
}

const btApplyRule = (rule, value, callback) => {
    if (rule.required && value != 'SHI') {
        return new Promise((resolve, reject) => {
            reject('勾选此项后方可继续操作！')
        });
    }
    return Promise.resolve();
}
</script>
<style scoped lang="less">
// .menu_inner {}
</style>
