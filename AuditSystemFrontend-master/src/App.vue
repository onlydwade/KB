<template>
    <a-config-provider :locale="zhCN">
        <router-view />
    </a-config-provider>
    <div class="spin_box" v-show="store.spin>0">
        <a-spin :spinning="store.spin>0" size="large"></a-spin>
    </div>
</template>
<script setup>
import { mainStore } from '@/store';
import 'dayjs/locale/zh-cn';
import zhCN          from 'ant-design-vue/lib/locale-provider/zh_CN';
import { useRouter } from "vue-router";
const store  = mainStore();
const router = useRouter();
</script>
<style>
.spin_box{
    position         :fixed;
    z-index          : 100000;
    width            : 100%;
    height           : 100%;
    top              : 0;
    left             : 0;
    display          : flex;
    align-items      : center;
    justify-content  : center;
    background-color : rgba(0,0,0,0.1);
}
</style>
