<template>
    <div class="header">
        <div class="header-left">
            <div class="header_btn" @click="collapseChage">
                <menu-unfold-outlined style="fontSize:24px" v-if="store.collapse"/>
                <menu-fold-outlined style="fontSize:24px" v-else />
            </div>
        </div>
        <div class="header-right">
            <span style="margin-top: 20px;opacity: 0.25;">系统版本：V20231211</span>
            <a-dropdown placement="bottomLeft">
                <div class="header_btn" v-if="store.deptPosts.length>0">
                    <span class="name">
                        <span v-if="store.deptPost.parentId!=0 && store.deptPost.parentName">{{store.deptPost.parentName}}-</span>
                        {{store.deptPost.deptName}}-{{store.deptPost.postName}}</span>
                    <DownOutlined style="fontSize:14px;marginLeft:3px;" />
                </div>
                <template #overlay>
                    <a-menu>
                        <a-menu-item 
                        v-for="(item) in store.deptPosts" :key="item.orderNum" 
                        @click="changeRole(item)">
                            <span :class="(item.deptId==store.deptPost.deptId&&item.postId==store.deptPost.postId)?'color-primary':'color-text'" >
                                <span v-if="item.parentId!=0 && item.parentName">{{item.parentName}}-</span>
                                {{item.deptName}}-{{item.postName}}
                            </span>
                        </a-menu-item>
                    </a-menu>
                </template>
            </a-dropdown>
            <div class="header_btn" @click="showMsg">
                <a-badge :count="msgCount">
                    <sound-outlined  style="fontSize:24px"/>
                </a-badge>
            </div>
            <a-dropdown placement="bottomRight">
                <div class="header_btn">
                    <UserBox :data="{userId:store.userInfo.userId, realname:store.userInfo.realname}" single :showName="false" />
                </div>
                <template #overlay>
                    <a-menu>
                        <!-- <a-menu-item key="account">个人中心</a-menu-item>
                        <a-menu-divider /> -->
                        <a-menu-item key="logout" @click="loginOut">退出登录</a-menu-item>
                    </a-menu>
                </template>
            </a-dropdown>
        </div>
        
        <Msg ref="msgRef" @countChange="getMsgCount"/>
    </div>
</template>
<script setup>
import api                           from '@/api/index';
import { createRouter,removeRouter } from '@/utils/tools';
import {mainStore}                   from '@/store/index';
const store           = mainStore();
const router          = useRouter();
const refreshUserRole = inject('refreshUserRole');
const homePageReload  = inject('homePageReload');
// 侧边栏折叠
const collapseChage   = () => {
    store.handleCollapse();
};
onMounted(() => {
    if (document.body.clientWidth < 1300) {
        collapseChage();
    }
    getMsgCount();
    
    bus.on('msgCountChange', getMsgCount);
});
onUnmounted(() => {
    bus.off('msgCountChange',getMsgCount);
})
const loginOut = async () => {
    let res = await api.common.logOut();
    store.clearToken();
    sessionStorage.setItem('backPath',window.location.href.replace(window.location.origin+'/#',''))
    // if(GLOBAL_PATH.logOut){
    //     window.location.href = GLOBAL_PATH.logOut;
    // }else{
    //     router.push('/flower');
    // }
}
const changeRole = (item)=>{
    store.spinChange(1);
    router.push('/index');
    api.common.switchAuth({deptId:item.deptId,postId:item.postId}).then(async (res)=>{
        if(res.code==200){
            store.setToken(res.data.access_token);
            
            refreshUserRole();
            homePageReload();
            // store.changeRole(item);
            let getMenu = await api.sys.menuGetByAccess();
            if(getMenu.code==200){
                removeRouter(router);
                store.setMenuRouters(getMenu.data);
                createRouter(router,getMenu.data);
            }
        }
        store.spinChange(-1);
    })
}
const bus = inject('bus');
const msgRef      = ref(null);
const msgCount    = ref(0);
const getMsgCount = ()=>{
    api.sys.messageCount().then(res=>{
        if(res.code==200){
            msgCount.value = res.data || 0;
        }
    })
}
const showMsg = ()=>{
    msgRef.value.open();
}
</script>
<style scoped lang="less">
    .header{
        background-color : #fff;
        display          : flex;
        justify-content  : space-between;
        align-items      : center;
        height           : 60px;
        box-sizing       : border-box;
        box-shadow       : 0 1px 4px rgb(0 21 41 / 8%);
        z-index          : 11;
        position         : relative;
        .header_btn{
            display     : flex;
            align-items : center;
            padding     : 0 16px;
            cursor      : pointer;
            height      : 60px;
            &:hover{
                opacity          : 0.75;
                background-color : #f7f7f7;
            }
            .name{
                margin-left: 10px;
            }
        }
        .header-left{
            display : flex;
        }
        .header-right{
            display : flex;
            padding : 0 10px;
        }        
    }
</style>
