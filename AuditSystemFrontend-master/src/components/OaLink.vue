
<template>
    <a class="color-link" @click="linkTo">
        {{link || '-'}}
    </a>
</template>
<script setup>
import api           from '@/api/index';
import { mainStore } from '@/store';
const store  = mainStore();
const props  = defineProps({
    link : {
        type    : String,
        default : '',
    },
})
const linkTo = async ()=>{
    if(!props.link){
        return;
    }
    api.common.getSsoToken({
        mobile:store.userInfo.phonenumber
    }).then(res=>{
        if(res.code==200 && res.data){
            window.open(props.link+'&access_token='+res.data);
        }
    })
}
</script>
<style scoped lang="less">
</style>
