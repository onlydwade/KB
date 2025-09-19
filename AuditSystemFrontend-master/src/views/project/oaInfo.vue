<template>
    <div class="content-inner">
        <component :is="components[componentCode]" :projectId="projectId" :menuHidden="true" :menuList="menuData">
        </component>
    </div>
</template>
<script setup>
import api from '@/api/index';
import { handleTree, TpWatermark, RemoveTpWatermark, isMobile } from '@/utils/tools';
import { mainStore } from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict = useDictStore();
const router = useRouter();
const route = useRoute();
const projectId = ref(Number(route.query.id || 0))
const stepCurrent = ref(0);
const to = ref(route.query.to || 'xmxx');
const infoData = ref({ roleKeys: [] });
const getInfo = (callBack) => {
    api.project.projectInfo(projectId.value).then(res => {
        if (res.code == 200) {
            infoData.value = res.data;
            callBack && callBack();
        }
    })
}

const pageTree = ref([]);
const modules = import.meta.glob("./**/*.vue");
const getPageTree = (handle) => {
    api.project.treeStepByProject(projectId.value).then(res => {
        if (res.code == 200) {
            let arrs = [0, 0, 0];
            pageTree.value = res.data.map((item, k) => {
                item.children.forEach((subItem, i) => {
                    if (subItem.children && subItem.children.length > 0) {
                        subItem.children.forEach((thirdItem, x) => {
                            if (thirdItem.code == to.value) {
                                arrs = [k, i, x]
                            }
                        });
                    } else {
                        if (subItem.code == to.value) {
                            arrs = [k, i, 0]
                        }
                    }
                });
                //异步注册加载组件
                if (handle != 'refresh' && isMobile()) {
                    components[item.code] = defineAsyncComponent(modules['./components/oaMenuTree/SecondMenu.vue']);
                } else {
                    components[item.code] = defineAsyncComponent(modules['./components/menuTree/SecondMenu.vue']);
                }
                return item;
            });
            if (handle != 'refresh') {
                treeTabs.value = arrs;
                stepCurrent.value = arrs[0];
            }
        }
    })
}
const refreshTree = async (stepMenuId, status, callBack) => {
    if (stepMenuId) {
        let postData = {
            projectId: projectId.value,
            stepMenuId: stepMenuId,
            status: status
        }
        let comp = await api.project.stepStatusUpdate(postData);
    }
    getInfo();
    getPageTree('refresh');
    callBack && callBack();
}

const treeTabs = ref([0, 0, 0]);
const treeTabChange = (data) => {
    treeTabs.value[data.index] = data.value;
}
provide('treeTabs', treeTabs);
provide('treeTabChange', treeTabChange);
provide('refreshMenuTree', refreshTree);
const getAutoParams = (params) => {
    return computed(() => {
        return params ? infoData.value[params] : infoData.value;
    })
}
provide('getAutoParams', getAutoParams);

onMounted(() => {
    if (route.query.access_token) {
        sessionStorage.setItem('backPath', route.fullPath.replace('&access_token=' + route.query.access_token, ''))
        window.location.href = GLOBAL_PATH.tokenAuth + route.query.access_token;
        return;
    }
    getInfo(() => {
        getPageTree();

        getLoginUser();
        getDict();
        getDept();
        getPost();
    });
})

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



const components = {};
const componentCode = computed(() => {
    return (pageTree.value[stepCurrent.value] || {}).code;
});
const stepChange = (current) => {
    treeTabs.value[0] = current;
    treeTabs.value[1] = 0;
}
const menuData = computed(() => {
    return (pageTree.value[stepCurrent.value] || {}).children;
})

const baseHandleRef = ref(null);
const changeBelongerRef = ref(null);
const handleComponent = (component) => {
    if (component == 'stop') {     //放弃
        baseHandleRef.value.open(infoData.value, 'stop');
    }
    if (component == 'changeBelonger') {     //变更归属人
        changeBelongerRef.value.open(infoData.value);
    }
}
const updateInfo = (res) => {
    if (res && res.id) {
        infoData.value = res;
    } else {
        getInfo();
    }
}
</script>
<style scoped lang="less">
.content-box {
    padding: 16px 0;
}
</style>