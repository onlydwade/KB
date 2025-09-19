<template>
    <div class="page-home">
        <Sidebar v-if="homePageAlive == 1"></Sidebar>
        <div class="main-page" v-if="homePageAlive == 1">
            <Header></Header>
            <router-view v-slot="{ Component }" class="content-main">
                <keep-alive :include="store.keepAlive">
                    <component :is="Component" />
                </keep-alive>
            </router-view>
        </div>
        <div class="main-page" v-else-if="homePageAlive == 2">
            <a-result status="403" title="登录失败" sub-title="当前账号无访问权限，请联系管理员处理后重新登录...">
                <template #extra>
                    <a-button type="primary" href="#/flower">重新登录</a-button>
                </template>
            </a-result>
        </div>
        <div class="spin_box" v-else>
            <a-spin></a-spin>
        </div>
    </div>
</template>
<script setup>
import api from '@/api/index';
import { handleTree, TpWatermark, RemoveTpWatermark } from '@/utils/tools';
import { mainStore } from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict = useDictStore();
const route = useRoute();
onMounted(() => {
    getLoginUser(() => {
        getDict();
        getDept();
        getDeptList();
        getPost();
    });
})
onUnmounted(() => {
    RemoveTpWatermark();
})
const getLoginUser = (callback) => {
    api.common.loginUser().then((res) => {
        if (res.code == 200) {
            store.setUserInfo(res);
            TpWatermark(res.user.realname || res.user.userName, '200', '300', '-20', 'black', '18', '.06');
            callback && callback();
            homePageAlive.value = 1;
        } else {
            homePageAlive.value = 2;
        }
    }).catch(() => {
        homePageAlive.value = 2;
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
    api.sys.listSingerType().then(res => {
        if (res.code == 200) {
            store.setDeptTree(handleTree(res.data, "deptId"));
        }
    })
}
const getDeptList = () => {
    api.sys.listLevelDeptType().then(res => {
        if (res.code == 200) {
            store.setDeptTree(handleTree(res.data, "deptId"));
        }
    })
}
const getHrOrg = () => {
    api.sys.getlistSingerType().then(res => {
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

const homePageAlive = ref(0);        //0加载中    1加载成功   2异常
const reload = () => {
    homePageAlive.value = 0;
    nextTick(() => {
        homePageAlive.value = 1;
    })
}
provide('refreshUserRole', getLoginUser);
provide('homePageReload', reload);
provide('refreshDeptTree', getDept);
provide('refreshDeptTreeList', getDeptList);
</script>
<style scoped lang="less">
.page-home {
    display: flex;
    height: 100vh;
}

.main-page {
    flex: 1;
    display: flex;
    flex-direction: column;
    width: 0;

    .content-main {
        flex: 1;
        height: 0;
        overflow: auto;
    }
}
</style>
