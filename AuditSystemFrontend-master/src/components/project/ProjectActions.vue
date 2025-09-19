
<template>
    <FooterBarL style="min-height:72px;" v-if="!terminationProcess">
        <a-button v-if="menuInfo.status == 1 && [0, 9].includes(menuInfo.approvalStatus)" size="large"
            disabled>该流程节点已完成</a-button>
        <template v-for="(item, index) in oaTemp" :key="item.id">
            <OaBtn :temp="item" @submit="submit" :disabled="disabled" :menuInfo="menuInfo"></OaBtn>
        </template>

        <template v-if="menuInfo.status == 0">
            <a-button
                v-if="!disabled && [0, 1, 3, 4, 10].includes(menuInfo.approvalStatus) && menuInfo.oaApproval == 1 && menuInfo.offlineApproval == 1 "
                size="large" @click="submit(1)">{{ offLineTitle }}</a-button>
        </template>
        <template v-if="menuInfo.status == 1">
            <a-button v-if="menuInfo.approvalStatus == 8" size="large" disabled>{{ offLineTitle.includes('NC审批') ? '已NC审批通过'
                : '已线下审批通过' }}</a-button>
        </template>
        <slot></slot>
        &nbsp;
        <template #right>
            <slot name="right"></slot>
        </template>
    </FooterBarL>
</template>
<script setup>
import api from '@/api/index';
import { message } from 'ant-design-vue';
import { mainStore } from '@/store';
import { computed } from 'vue';
const store = mainStore();
const props = defineProps({
    title: {
        type: String,
        default: '发起OA审批'
    },
    offLineTitle: {
        type: String,
        default: '已线下审批，标记完成'
    },
    menuInfo: {
        type: Object,
        default: {},
    },
    projectLevel: {
        type: String,
        default: ''
    }
})
const roleKeys = inject('getAutoParams')('roleKeys');
const serviceStatus = inject('getAutoParams')('serviceStatus');
const expire = inject('getAutoParams')('expire');
const terminationProcess = computed(() => {
    return expire.value == 'YI_SHI_XIAO' || ['YI_ZHONG_ZHI'].includes(serviceStatus.value)
})
const disabled = computed(() => {
    // return !roleKeys.value.includes(1) ||  props.menuInfo.status==1 || [1,2,5,8].includes(props.menuInfo.approvalStatus) || ['ZAI_GUAN','YI_ZHONG_ZHI'].includes(serviceStatus.value) || expire.value=='YI_SHI_XIAO';
    // return !roleKeys.value.includes(1) || ['ZAI_GUAN','YI_ZHONG_ZHI'].includes(serviceStatus.value) || expire.value=='YI_SHI_XIAO';
    return !roleKeys.value.includes(1) || props.menuInfo.status == 1 || ['ZAI_GUAN', 'YI_ZHONG_ZHI'].includes(serviceStatus.value) || expire.value == 'YI_SHI_XIAO';
})
const emit = defineEmits(['submit']);
const submit = (type, temp) => {            //type 0 线上OA审批  1 线下审批   2 提交保存 校验必填   3跳过 已填保存不校验必填
    if (type == 0) {
        // message.warning('线上审批暂未开启！！');
        // return;
    }
    emit('submit', type, temp);
}

const projectType = inject('getAutoParams')('projectType');
const oaTemp = ref([]);
const projectInfo = inject('getAutoParams')();
let pLevel = 1
const getOaTemp = () => {
    api.common.oaList(projectType.value, props.menuInfo.id).then(res => {
        if (res.code == 200) {
            // console.log(res.data.filter(item=>{
            //       let level = item.level==0 || (item.level==1 && pLevel<3) || (item.level==2 && pLevel>2);
            //       let operate = item.operate==0 || (item.operate==1 && projectInfo.value.operationTeamId==projectInfo.value.companyId) || (item.operate==2 && projectInfo.value.operationTeamId!=projectInfo.value.companyId)
            //       return item.stepMenuId == props.menuInfo.id && item.projectType==projectType.value && level && operate;
            //   }),66666666666666,projectInfo.value)
          console.log(res.data)
            oaTemp.value = res.data.filter(item => {
                let level = item.level == 0 || (item.level == 1 && pLevel < 3) || (item.level == 2 && pLevel > 2);
                let operate ;
                //单一投标项目不再区分归属单位和运营团队是否一致
                if(projectType.value === 'DAN_YI_TOU_BIAO_XIANG_MU'){
                  operate = item.operate == 0 || item.operate == 1
                }else {
                  operate = item.operate == 0 || (item.operate == 1 && projectInfo.value.operationTeamId == projectInfo.value.companyId) || (item.operate == 2 && projectInfo.value.operationTeamId != projectInfo.value.companyId)
                }
              // 项目评审特殊处理
                let projectLevel = props.menuInfo.id === 12 ? item.expire === props.projectLevel : true
                let isShow = item.stepMenuId == props.menuInfo.id && item.projectType == projectType.value  && operate && level && projectLevel;
                //项目废止OA按钮特殊处理，因为已废止之后会出现两个OA审批按钮，一个是结项的，一个是废止的，
                //所以需要判断当前状态是否为已废止，如果是已废止则需要过滤掉结项的OA审批按钮。
                if (props.menuInfo.id == 40&&serviceStatus.value == 'YI_FEI_ZHI') {
                    isShow = false;
                    if(item.shortName.includes('废止')&&item.projectType == projectType.value){
                        isShow = true;
                    }
                }
                return isShow;
            })
            console.log(oaTemp.value)
        }
    })
}
const getList = async (TeamId) => {
    pLevel = 1
    let res = await api.sys.deptInfo(projectInfo.value.companyId);
    if (res.code == 200 && res.data) {
        pLevel = res.data.level || 1;
    }
    if (TeamId) {
        projectInfo.value.operationTeamId = TeamId
    }
    getOaTemp()
}

watch(() => props.projectLevel, (newValue, oldValue) => {
    if (newValue != oldValue && props.menuInfo.id === 12) {
        getOaTemp()
    }
})

onMounted(() => {
    console.log(props.menuInfo)
    if (props.menuInfo.oaApproval == 1) {
        getList();
    }
})
defineExpose({ getList })
</script>
<style scoped lang="less"></style>
