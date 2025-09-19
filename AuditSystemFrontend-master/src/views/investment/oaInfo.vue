<template>
    <div class="oa_info">
        <component :is="components.pageLoader" :companyId="infoData.companyId" :recordId="recordId" :mode="mode"></component>
    </div>
</template>
<script setup>
import LoadingComponent                               from '@/components/LoadingComponent.vue'
import api                                            from '@/api/index';
import { message , Modal }                            from 'ant-design-vue';
import { handleTree,hasPermission,TpWatermark,RemoveTpWatermark,isMobile} from '@/utils/tools';
import { mainStore }                                  from '@/store';
import { useDictStore }                               from '@/store/dict';
const dict   = useDictStore();
const store  = mainStore();
const router = useRouter();
const route  = useRoute();

const modeObjs = {
    'COMPANY_FX_CHU_LI' : {
        key  : 'companyrisk',
    },
    'COMPANY_FX_JIE_CHU':{
        key:'companyrisk',
    },
    'COMPANY_SAN_HUI_SHEN_PI':{
        key:'ompanyHighLevelMeetingApproval'
    },
    'COMPANY_PROJECT_STOP':{
        key:'companyExit'
    }
}
const modeObjsYd = {
    'COMPANY_FX_CHU_LI' : {
        key  : 'companyriskYd',
    },
    'COMPANY_FX_JIE_CHU':{
        key:'companyriskYd',
    },
    'COMPANY_SAN_HUI_SHEN_PI':{
        key:'ompanyHighLevelMeetingApprovalYd'
    },
    'COMPANY_PROJECT_STOP':{
        key:'companyExitYd'
    }
}
const recordId = ref(Number(route.query.id || 0))
const mode     = ref(route.query.mode  || 'COMPANY_FX_CHU_LI');

const infoData = ref({roleKeys:[]});
const getInfo  = (callBack)=>{
    api.investment.correlationGet(recordId.value,modeObjs[mode.value].key).then(res=>{
        if(res.code==200){
            infoData.value = res.data;
            callBack && callBack();
        }
    })
}
const getAutoParams = (params)=>{
    return computed(()=>{
        return params?infoData.value[params]:infoData.value;
    })
}
provide('getAutoParams',getAutoParams);

const components = {};
const modules    = import.meta.glob("./**/*.vue");
onMounted(() => {
    if(route.query.access_token){
        sessionStorage.setItem('backPath',route.fullPath.replace('&access_token='+route.query.access_token,''))
        window.location.href = GLOBAL_PATH.tokenAuth+route.query.access_token;
        return;
    }
    
    let oaCode            = isMobile() ?   modeObjsYd[mode.value].key : modeObjs[mode.value].key;
    let path              = './components/oa/'+oaCode+'.vue';
    components.pageLoader = defineAsyncComponent({
        loader           : modules[path],
        loadingComponent : LoadingComponent,
    });
    getInfo(()=>{
        getLoginUser();
        getDict();
        getDept();
        getPost();
    });
})

const getLoginUser = ()=>{
    api.common.loginUser().then((res)=>{
        if(res.code==200){
            store.setUserInfo(res);
            TpWatermark(res.user.realname || res.user.userName,'200','300','-20','black','18','.06');
        }
    })
}
const getDict = ()=>{
    api.sys.dictList().then(res=>{
        if(res.code==200){
            dict.setDict(res.data || []);
        }
    })
}
const getDept = ()=>{
    api.sys.deptList().then(res=>{
        if(res.code==200){
            store.setDeptTree(handleTree(res.data,"deptId"));
        }
    })
}
const getPost = ()=>{
    api.sys.postList().then(res=>{
        if(res.code==200){
            store.setPostList(res.data);
        }
    })
}
</script>
<style scoped lang="less">
.oa_info{
    height         : 100%;
    display        : flex;
    flex-direction : column;
    
    :deep(.menu_inner){
        flex             : 1;
        height           : 0;
        // background-color : #fff;
        overflow         : auto;
        padding          : 16px;
        box-sizing       : border-box;
    }
}
</style>