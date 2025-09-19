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
                        <a-form-item required label="工作简报标题" name="title">
                            <a-input :disabled="isView" allowClear v-model:value="formData.title" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="发送用户对象" name="pushUserList">
                            <UserGroupListSelect :disabled="isView" :options="options" mode="multiple" ref="userList"
                                v-model:modelValue="formData.pushUserList" @change="userSelect" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="汇报单位" name="deptId">
                            <DeptSelectBuMen :disabled="isView" v-model="formData.deptId" :noRoot="true"
                                @change="deptChange" />
                        </a-form-item>
                    </a-col>

                </a-row>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="简报主题色" prop="theme" name="theme"
                            :rules="[{ required: true, message: '请选择简报主题色' }]">
                            <a-select :disabled="isView" v-model:value="formData.theme" placeholder="请选择" allow-clear>
                                <a-select-option value="#FFA042">方案一</a-select-option>
                                <a-select-option value="#2894FF">方案二</a-select-option>
                                <a-select-option value="#f54949">方案三</a-select-option>
                            </a-select>
                        </a-form-item>
                        <a-button size="small" type="primary" class="theme_one" @click="themeChange('#FFA042')">
                            <div></div>
                        </a-button>
                        <a-button size="small" type="primary" class="theme_two" @click="themeChange('#2894FF')">
                            <div></div>
                        </a-button>
                        <a-button size="small" type="primary" class="theme_three" @click="themeChange('#f54949')">
                            <div></div>
                        </a-button>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="所属年份" name="year">
                            <a-date-picker :disabled="isView" v-model:value="formData.year" class="w_full" picker="year"
                                valueFormat="YYYY" @change="dateChange" />
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-collapse-panel>
            <a-collapse-panel key="expand">
                <template #header>
                    <h5 class="title_single">市场拓展数据</h5>
                    <span class="color-danger">*</span>
                </template>
                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="新签拓展合同(份)" name="contractCount">
                            <a-input :disabled="isView" allowClear v-model:value="formData.contractCount"
                                placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="新增合同总金额(万)" name="totalAmount">
                            <a-input :disabled="isView" allowClear v-model:value="formData.totalAmount" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="新增合同年度金额(万)" name="totalYearAmount">
                            <a-input :disabled="isView" allowClear v-model:value="formData.totalYearAmount"
                                placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                </a-row>

                <a-row :gutter="24">
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="续签拓展合同(份)" name="renewalContractCount">
                            <a-input :disabled="isView" allowClear v-model:value="formData.renewalContractCount"
                                placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="续签合同总金额(万)" name="renewalTotalAmount">
                            <a-input :disabled="isView" allowClear v-model:value="formData.renewalTotalAmount" placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                    <a-col :xxl="6" :lg="8" :sm="12">
                        <a-form-item required label="续签合同年度金额(万)" name="renewalTotalYearAmount">
                            <a-input :disabled="isView" allowClear v-model:value="formData.renewalTotalYearAmount"
                                placeholder="请输入" />
                        </a-form-item>
                    </a-col>
                </a-row>

            </a-collapse-panel>
            <a-collapse-panel key="winbidder">
                <template #header>
                    <h5 class="title_single">近期项目中标情况</h5>
                    <!-- <span class="color-danger">*</span> -->
                </template>
                <Winbidder ref="winbidder" :id="briefId" :deptId="formData.deptId" :readOnly="isView" />
            </a-collapse-panel>
            <a-collapse-panel key="undertaking">
                <template #header>
                    <h5 class="title_single">近期项目承接情况</h5>
                    <!-- <span class="color-danger">*</span> -->
                </template>
                <Undertaking ref="undertaking" :id="briefId" :deptId="formData.deptId" :readOnly="isView" />
            </a-collapse-panel>
            <a-collapse-panel key="keycustomer">
                <template #header>
                    <h5 class="title_single">重点客户跟进情况</h5>
                    <span class="color-danger">*</span>
                </template>
                <KeyCustomer ref="keycustomer" :id="briefId" :readOnly="isView" />
            </a-collapse-panel>
        </a-collapse>
        <FooterBar>
            <!-- <a-button size="large" @click="router.back()">取消</a-button>
            <a-button size="large" class="preview" @click="preview" :loading="submitLoading">预览</a-button>
            <a-button size="large" type="primary" @click="staging" :loading="submitLoading" v-if="!isView">暂存</a-button>
            <a-button size="large" type="primary" @click="submit" :loading="submitLoading" v-if="!isView">提交</a-button> -->
        </FooterBar>
        <a-drawer :width="520" :title="formData.title" placement="right" :visible="visible" @close="visible = false">
            <workReport :report="formData" />
        </a-drawer>


        <WorkBriefActions @submit="submit" @preview="preview" @staging="staging" :menuInfo="workInfo" ref="actionRef">

        </WorkBriefActions>

    </a-form>
</template>
<script setup>
import api from '@/api/index';
import { message } from 'ant-design-vue';
import Undertaking from './Undertaking.vue';
import Winbidder from './Winbidder.vue';
import KeyCustomer from './KeyCustomer.vue';
import { mainStore } from '@/store';
import { useDictStore } from '@/store/dict';
import { ref } from 'vue';
import { formDataFill, dateFormat, TpWatermark, isMobile, handleTree } from "@/utils/tools";
const bus = inject("bus");
const router = useRouter();
const route = useRoute();
const briefId = ref(Number(route.query.id || 0));
const visible = ref(false)
const emit = defineEmits(['update:submitLoading', 'success'])
const props = defineProps({
    submitLoading: {
        type: Boolean,
        default: false,
    },
    id: {
        type: Number,
        default: 0,
    }
})
const workInfo = {
    type: Object,
    default: {},
}
const childRef = ref('actionRef')
const formData = ref({});
const collapseKey = ref(['base', 'expand', 'winbidder', 'undertaking', 'keycustomer']);
const winbidder = ref(null)
const undertaking = ref(null)
const keycustomer = ref(null)
const userList = ref(null)
const store = mainStore();
const dict = useDictStore();
const isView = ref(Number(route.query.isView || ''))
const getInfo = () => {
    api.workBrief.workBriefInfo(briefId.value).then(res => {
        if (res.code == 200) {
            formData.value = res.data;
            workInfo.approveStatus = res.data.approveStatus;
            winbidder.value.list = res.data.winBidderList || []
            undertaking.value.list = res.data.undertakingList || []
            keycustomer.value.list = res.data.customerFollowList || []
            userList.value.searchUser(null, (formData.value.pushUserList || []))
        }
    })
}
const companyId = ref(Number(formData.deptId || 100));

onBeforeMount(() => {

    var id = route.query.id;
    var token = route.query.access_token;
    //isMobile()
    if (isMobile()) {
        if (token == undefined) {
            router.push("/mobile/workReport?read=1&id=" + id);
        } else {
            router.push("/mobile/workReport?read=1&id=" + id + "&access_token=" + token);
        }
    } else {
        if (route.query.access_token) {
            sessionStorage.setItem('backPath', route.fullPath.replace('&access_token=' + route.query.access_token, ''))
            window.location.href = GLOBAL_PATH.tokenAuth + route.query.access_token;
            return;
        }
    }

    console.log('Component is about to be created.');
})

onMounted(() => {

    getdepTInfo();
    //18d1d8239b978f959e9b6ce4bce853ab
    workInfo.id = briefId.value;
    workInfo.templateId = '18d1d8239b978f959e9b6ce4bce853ab';
    workInfo.isView = isView.value;
    getcheckOa();
    if (briefId.value != 0) {
        getInfo();
    }
    else {
        initTitle();
        formData.value.year = new Date().getFullYear().toString();
        // api.analysis.getProjectSituation(1, 100, formData.value.year).then(res => {
        //     if (res.code === 200) {
        //         formData.value.contractCount = res.data.signProjectTotal;
        //         formData.value.totalAmount = res.data.contractActual;
        //         formData.value.totalYearAmount = res.data.contractActualYear;
        //     }
        // })
    }
})

const getdepTInfo = () => {
    getLoginUser();
    getDict();
    getDept();
    getPost();
}

const getLoginUser = () => {
    api.common.loginUser().then((res) => {
        if (res.code == 200) {
            store.setUserInfo(res);
            TpWatermark(res.user.realname || res.user.userName, '200', '300', '-20', 'black', '18', '.06');
        }
    })
}
const getDict = () => {
    api.sys.dictList().then(res => {
        if (res.code == 200) {
            dict.setDict(res.data || []);
        }
    })
}
const getDept = () => {
    api.sys.deptList().then(res => {
        if (res.code == 200) {
            store.setDeptTree(handleTree(res.data, "deptId"));
        }
    })
}
const getPost = () => {
    api.sys.postList().then(res => {
        if (res.code == 200) {
            store.setPostList(res.data);
        }
    })
}



const getcheckOa = () => {
    api.workBrief.getcheckOa().then(res => {
        if (res.code == 200) {
            workInfo.checkoa = res.data;
        }
    })
}

const formRef = ref(null);

const staging = (type, temp) => {
    emit('update:submitLoading', true);
    formData.value.winBidderList = winbidder.value.list;
    formData.value.undertakingList = undertaking.value.list;
    formData.value.customerFollowList = keycustomer.value.list;
    debugger;
    api.workBrief.workBriefStaging(formData.value).then(res => {
        if (res.data.code == 200) {
            message.success(res.msg);
            router.push("/workbrief");
            // workInfo.id = res.data.data.id;
            // briefId.value = res.data.data.id;
        } else {
            message.warning(res.data.msg);
        }
    })
}

// 0发起新oa审批，-1重新发起oa审批，2直接提交

const submit = (type, temp) => {

    formRef.value.validateFields().then(vRes => {
        emit('update:submitLoading', true);
        formData.value.winBidderList = winbidder.value.list;
        formData.value.undertakingList = undertaking.value.list;
        formData.value.customerFollowList = keycustomer.value.list;
        formData.value.submitType = type;

        api.workBrief.workBriefSave(formData.value).then(res => {

            if (res.data.code == 200) {
                message.success(res.msg);
                //router.back();
                //oaStart(type, temp);
                workInfo.id = res.data.data.id;
                briefId.value = res.data.data.id;
                if (type == 0 || type == -1) {
                    oaStart(type, temp);
                } else {
                    router.push("/workbrief");
                }
                //debugger;
                if (route.query == undefined || route.query.id == undefined) {
                    router.push("/workbrief");
                }

            } else {
                message.warning(res.data.msg);
            }
        })
    }).catch(err => {
        collapseKey.value = ['base'];
        message.warning('请完善必填信息！');
    })
}

const reflashPage = () => {
    if (route.query.id == undefined) {
        router.push("/innerPage/workBriefEdit?id=" + res.data.data.id);
        location.reload();
    }
}

const oaStart = (type, temp) => {

    let postData = {
        subject: formData.value.title + "-数据简报",
        recordId: briefId.value,
        subRecordId: null,
        templateId: workInfo.templateId,
        moduleName: "WorkBrief",
        approvalStatus: 5,
        stepCode: null,
        mainProcess: 1,
        detailUrl: `${window.location.origin}/#/workBriefOaEdit?id=${briefId.value}&isView=1`,
        //detailUrl: `${window.location.origin}/#/mobile/workReport?id=${briefId.value}&read=1`,
        //https://tt-sit.gem-flower.com/#/mobile/workReport?id=%s&read=1
    };
    let apiKey = "oaAdd";
    if (type == -1) {
        apiKey = "oaUpdate";
        postData.id = temp.oaId;
    } else {
        postData.submitTime = dateFormat(new Date());
        postData.createTime = dateFormat(new Date());
        postData.submitUserId = temp.userId;
    }
    api.common[apiKey](postData).then(async (res) => {
        if (res.code == 200) {
            message.success("操作成功！");
            if (type != 2) {
                bus.emit('workgetlist');
            }
            if (temp.mainProcess && menuInfo.value.status == 0) {
                await api.project.stepStatusUpdate({
                    projectId: projectId,
                    stepMenuId: menuInfo.value.id,
                    approvalStatus: 5,
                });
            }

        }
    });
};

const userSelect = async (user) => {
    if (formData.value.companyId) {
        return;
    }
    let res = null;
    let role = {};
    if (user.deptId) {
        let res = await api.sys.deptInfo(user.deptId);
        if (res.code == 200 && res.data) {
            role = res.data || {};
        }
    } else {
        res = await api.sys.userDeptPostList(user.value);
        if (res.data && res.data.length > 0) {
            role = res.data[0];
        }
    }
    if (role.deptId) {
        if (role.deptType == 'CENG_JI') {
            formData.value.companyId = role.deptId;
        } else {
            formData.value.companyId = role.parentId;
        }
        let obj = await api.sys.regionInfo(role.deptId);
        if (obj.code == 200 && obj.data) {
            formData.value.regionId = obj.data.deptId;
        }
    }
}

const deptChange = (val, node) => {
    formData.value.regionId = getSetRegionId(node.deptType, val, node.parentId);
    formData.value.deptLevel = node.level;
    api.analysis.getProjectSituation(node.level, node.deptId, formData.value.year).then(res => {
        if (res.code === 200) {
            formData.value.contractCount = res.data.signProjectTotal;
            formData.value.totalAmount = fixedFour(res.data.xzzje);
            formData.value.totalYearAmount = fixedFour(res.data.xzndzje);

            formData.value.renewalContractCount = res.data.signRenewalProjectTotal;
            formData.value.renewalTotalAmount = fixedFour(res.data.xqzje);
            formData.value.renewalTotalYearAmount = fixedFour(res.data.xqndzje);
        }
    })
    const filterForm = reactive({
        companyId: node.deptId
    });
    let postData = {
        desc: ["createTime"],
        params: {},
    };
    let params = [
        "companyId"
    ];
    params.forEach((key) => {
        if (filterForm[key] && filterForm[key] != -1) {
            postData.params[key] = filterForm[key];
        }
    });
    api.workBrief.winBidderGetList(postData).then(res => {
        if (res.code === 200) {
            winbidder.value.list = res.data;
        }
    })
    api.workBrief.undertakingGetList(postData).then(res => {
        if (res.code === 200) {
            undertaking.value.list = res.data;
        }
    })
}

const dateChange = () => {
    if (formData.value.deptId) {
        api.analysis.getProjectSituation(formData.value.deptLevel, formData.value.deptId, formData.value.year).then(res => {
            if (res.code === 200) {
                formData.value.contractCount = res.data.signProjectTotal;
                formData.value.totalAmount = fixedFour(res.data.xzzje);
                formData.value.totalYearAmount = fixedFour(res.data.xzndzje);             

                formData.value.renewalContractCount = res.data.signRenewalProjectTotal;
                formData.value.renewalTotalAmount = fixedFour(res.data.xqzje);
                formData.value.renewalTotalYearAmount = fixedFour(res.data.xqndzje);                
            }
        })
    }
}

const fixedFour = (dataone) => {
    debugger;
    return (dataone / 10000).toFixed(4);
}

const getSetRegionId = async (deptType, deptId, parentId) => {
    if (deptType == 'CENG_JI') {
        formData.value.companyId = deptId;
    } else {
        formData.value.companyId = parentId;
    }
    let obj = await api.sys.regionInfo(deptId);
    if (obj.code == 200 && obj.data) {
        formData.value.regionId = obj.data.deptId;
    }
}

const themeChange = (color) => {
    if (isView.value != 1) {
        formData.value.theme = color
    }
}

const initTitle = () => {
    api.workBrief.workBriefinitTitle().then(res => {
        if (res.code == 200) {
            formData.value.title = res.data;
        }
    })
}

const preview = () => {
    visible.value = true
}

defineExpose({ formData, submit, preview, staging })

</script>
<style scoped lang="less">
.preview {
    background-color: #409eff;
    color: white;
}

.theme_one {
    background-color: #FFA042;
    border-color: #FFA042;
    color: white;
    margin-left: 3cap;
    width: 30px;
    /* 设置按钮宽度，使其呈正方形 */
    height: 30px;
    /* 设置按钮高度，使其呈正方形 */
}

.theme_two {
    background-color: #2894FF;
    border-color: #2894FF;
    color: white;
    margin-left: 3cap;
    width: 30px;
    /* 设置按钮宽度，使其呈正方形 */
    height: 30px;
    /* 设置按钮高度，使其呈正方形 */
}

.theme_three {
    background-color: #f54949;
    border-color: #f54949;
    color: white;
    margin-left: 3cap;
    width: 30px;
    /* 设置按钮宽度，使其呈正方形 */
    height: 30px;
    /* 设置按钮高度，使其呈正方形 */
}
</style>