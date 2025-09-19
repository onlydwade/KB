<template>
  <div class="sidebar" :class="{ sidebar_closed: store.collapse }">
    <a class="logo_box" href="#/dashboard">
      <img class="logo" src="/images/logo.png" alt="" />
      <h5 class="name" v-if="!store.collapse">宝石花投拓管理平台</h5>
    </a>
    <div class="menu_box">
      <AScrollbar>
        <a-menu mode="inline" :inlineIndent="34" :inline-collapsed="store.collapse" v-model:openKeys="openKeys"
          v-model:selectedKeys="selectedKeys" @click="menuClick">
          <template v-for="(item, index) in store.getMenu">
            <SubMenu v-if="item.children && item.children.length > 0" :data="item" :key="item.key" />
            <a-menu-item v-else :key="item.key">
              <template #icon v-if="item.icon">
                <i :class="'iconfont icon-' + item.icon"></i>
              </template>
              {{ item.title }}
            </a-menu-item>
          </template>
        </a-menu>
      </AScrollbar>
    </div>
  </div>
</template>

<script setup>
import api from '@/api/index';
import { mainStore } from "@/store";
import { onBeforeRouteUpdate } from "vue-router";
const store = mainStore();
const router = useRouter();
const route = useRoute();

const selectedKeys = ref([]);
const openKeys = ref([]);
onMounted(() => {
  routePathToKey(route.path);
});
onBeforeRouteUpdate(to => {
  routePathToKey(to.path);
});
const routePathToKey = path => {
  if (path.includes("innerPage/")) {
    return;
  }
  let pathArr = path.split("/");
  if (pathArr[0] == "") {
    pathArr.splice(0, 1);
  }
  openKeys.value = pathArr;
  selectedKeys.value = [pathArr[pathArr.length - 1]];
};
// const menuList = ref([])
// const getMenu = ()=>{
//     const menu = JSON.parse(localStorage.getItem('menu') || '[]');
//     let json = [{title : '首页',key:'index',icon:'index'}];
//     function doArr(arr,target,parentPath){
//         arr.forEach((item, i) => {
//             let obj = {
//                 title : item.meta.title,
//                 icon  : (item.meta.icon&&item.meta.icon!='#')?item.meta.icon:null,
//                 key   : item.path.replace(parentPath+'/',''),
//             }
//             if(item.children&&item.children.length>0){
//                 obj.children = [];
//                 doArr(item.children,obj.children,item.path);
//                 target.push(obj);
//             }else{
//                 target.push(obj);
//             }
//         });
//     }
//     doArr(menu,json,'');
//     menuList.value = json;
// }
// getMenu();
let keys = ["_manage", "_license", "_contract", "_politics", "_tender",];
const menuClick = data => {
  console.log(data)
  const mode = import.meta.env.MODE;
  const url = mode == 'pro' ? 'https://bid.gem-flower.com' : 'https://bid-test.gem-flower.com'
  if (keys.includes(data.key)) {
    api.common.getSsoToken({
      mobile: store.userInfo.phonenumber
    }).then(res => {
      if (res.code == 200 && res.data) {
        window.open(`${url}/#/ttLogin?token=${res.data}&url=${data.key}`);
      }
    })
    return
  }
  let fullPath = "";
  data.keyPath.forEach((item, i) => {
    fullPath += "/" + item;
  });
  router.push(fullPath);
};
</script>

<style lang="less">
.sidebar {
  background-color: #fff;
  width: 210px;
  flex: 0 0 210px;
  height: 100%;
  box-shadow: 2px 0 8px 0 rgb(29 35 41 / 5%);
  transition: all 0.3s;
  z-index: 12;
  position: relative;

  .logo_box {
    box-sizing: border-box;
    height: 60px;
    display: flex;
    align-items: center;
    padding-left: 16px;
    width: 100%;
    background-color: #fff;
    box-shadow: 1px 1px 4px rgb(0 21 41 / 8%);
    z-index: 10;
    position: relative;
    overflow: hidden;

    .logo {
      height: 26px;
    }

    .name {
      margin: 0;
      font-size: 15px;
      margin-left: 10px;
      word-break: keep-all;
      color: #6f5f59;
      font-weight: normal;
    }
  }

  .iconfont {
    font-size: 22px !important;
    position: relative;
    left: -4px;
  }

  .ant-menu {
    border-right: none;
  }

  .ant-menu-inline {
    border-right: none;
  }
}

.menu_box {
  box-sizing: border-box;
  height: calc(100vh - 60px);
  overflow: hidden;
}

.sidebar_closed {
  width: 80px;
  flex: 0 0 80px;

  .logo_box {
    padding-left: 30px;
  }
}
</style>
