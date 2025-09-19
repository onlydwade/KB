<template>
</template>

<script setup>
import { useRoute } from 'vue-router';
import api from '@/api/index';
const route = useRoute()
const router = useRouter();

onMounted(() => {
    checkDeviceAndRedirect();
})

const checkDeviceAndRedirect = () => {
    const userAgent = window.navigator.userAgent;
    const isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(userAgent);
    debugger;
    if (route.query.access_token) {
        sessionStorage.setItem('backPath', route.fullPath.replace('&access_token=' + route.query.access_token, ''))
        window.location.href = GLOBAL_PATH.tokenAuth + route.query.access_token;
        return;
    }

    // 获取完整的路由路径
    const fullPath = route.fullPath;

    // 使用 indexOf 和 substr 方法分离出查询参数部分
    const queryStringStart = fullPath.indexOf('?');
    let queryString = '';

    if (queryStringStart !== -1) {
        // 如果存在查询参数，则提取出 ? 后面的内容
        queryString = fullPath.substr(queryStringStart + 1);
    }

    console.log(queryString)

    if (isMobile) {
        // 如果访问的是手机，则跳转到手机指定页面
        router.push('/mobile/backlogInfo?' + queryString);
    } else {
        // 如果访问的是PC，则跳转到PC指定页面
        router.push('/backlogOaView?' + queryString);
    }
}


</script>

<style scoped></style>
