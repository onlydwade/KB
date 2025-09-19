<template>
    <div class="content-box_full">
        <a-form layout="vertical" :model="formData" ref="formRef">
            <a-form-item>
                <h1 style="font-size: 18px;margin-top: 25px; margin-left: 25px;">【投拓管理系统消息通知】{{ formData.title }}</h1>
            </a-form-item>
            <a-form-item>
                <a-row :gutter="24">
                    <h2 style="font-size: 13px;color: grey;margin-top: 0px; margin-left: 45px;">{{ formData.module }}</h2>
                    <h2 style="font-size: 13px;color: grey;margin-top: 0px; margin-left: 25px;">{{ formData.createTime }}
                    </h2>
                </a-row>
            </a-form-item>
            <a-divider style="margin-top: -25px;margin-left: 25px;" />
            <a-form-item>
                <div style="font-size: 12px;margin-left: 35px;margin-top: 15px;">
                    <h2 style="color: #6c6b6b;">{{ formData.content }}</h2>
                </div>
            </a-form-item>
            <!-- <FooterBarL style="min-height:72px;position: fixed; ">
                <a-space :size="16">
                    <a-button v-if="formData.status == 1" type="primary" size="large" @click="handle"
                        style="margin-left: 20px;">去处理</a-button>
                    <a-button v-if="formData.status == 1" type="primary" size="large" @click="done">设为已办</a-button>
                </a-space>
            </FooterBarL> -->

            <div style="position: fixed; bottom: 0; left: 0; width: 100%; padding: 30px;  background-color: #fff;">
                <a-space :size="16">
                    <a-button v-if="formData.status == 1 && !isDingTalkEnv()" type="primary" size="large" @click="handle"
                        style="margin-left: 20px;">去处理</a-button>
                    <a-button v-if="formData.status == 1 && formData.moduleType == 1" type="primary" size="large" @click="done">设为已办</a-button>
                </a-space>
            </div>
        </a-form>
    </div>
</template>
<script setup>
import api from '@/api/index';
import { message } from 'ant-design-vue';
import { mainStore } from '@/store';
import { isDingTalkEnv } from '@/utils/dingtalk-jsapi'
const store = mainStore();
const router = useRouter();
const route = useRoute();
const formData = ref({});

onMounted(() => {
    if (route.query.access_token) {
        sessionStorage.setItem('backPath', route.fullPath.replace('&access_token=' + route.query.access_token, ''))
        window.location.href = GLOBAL_PATH.tokenAuth + route.query.access_token;
        return;
    }
    getInfo();
})

const getInfo = () => {
    api.oaMessageLog.oaMessageLogView(route.query.id).then(res => {
        if (res.code == 200) {
            formData.value = res.data;
        }
    });
}

const handle = () => {
    store.spinChange(1);
    // 项目
    if (formData.value.moduleType == 1) {
        router.push('/projectInfo?id=' + formData.value.serviceId);
    }
    // 客户
    else if (formData.value.moduleType == 3) {
        router.push('/customerInfo?id=' + formData.value.serviceId + '&tab=3');
    }
    store.spinChange(-1);
}

const done = () => {
    // 设为已办
    api.oaInfo.oaSetTodoDone(route.query.id).then(res => {
        // 不管oa那边发没发成功，我们这边的系统oa记录也要置为已办
        if (res.code == 200) {
            api.oaMessageLog.oaMessageLogDone(route.query.id).then(res => {
                if (res.code == 200) {
                    message.success('操作成功');
                    getInfo();
                } else {
                    message.error('操作失败：' + res.msg);
                }
            });
        } else {
            message.error('操作失败：' + res.msg);
        }
    });
}

</script>
<style scoped lang="less"></style>