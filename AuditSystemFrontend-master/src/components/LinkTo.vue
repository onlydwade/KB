
<template>
    <a class="color-link" @click="linkTo">
        <slot></slot>
    </a>
</template>
<script setup>
import api           from '@/api/index';
import { message }   from 'ant-design-vue';
import { mainStore } from '@/store';
const store  = mainStore();
const router = useRouter();
const props  = defineProps({
    scope : {
        type    : Boolean,
        default : true,
    },
    modelName : {
        type    : String,
        default : '',
    },
    dataId : {
        type    : Number,
        default : 0,
    },
    link : {
        type    : String,
        default : '',
    },
    
})
const linkTo = async ()=>{
    router.push(props.link);
    return;
    
    if(props.modelName){
        if(!props.link || !props.dataId)return;
        let res    = await api[props.modelName].dataScope(props.dataId);
        if(res.code==200){
            let scoped = res.data;
            if(!scoped){
                message.warning('您暂无该数据访问权限！');
                return;
            }
        }else{
            return;
        }
    }
    router.push(props.link);
    
}
</script>
<style scoped lang="less">
</style>
