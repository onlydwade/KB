
<template>
    <a-button type="text" :class="{'color-primary':!item.disabled}" size="small" v-for="(item,index) in outside" :disabled="item.disabled" :key="index" @click="handle(item)">{{item.text}}</a-button>
    <template v-if="inside.length>0">
        <a-dropdown placement="bottomRight">
            <a-button type="text" class="color-primary" size="small">
                更多<DownOutlined/>
            </a-button>
            <template #overlay>
                <a-menu>
                    <a-menu-item v-for="(item,index) in inside" :key="index" :disabled="item.disabled" @click="handle(item)">{{item.text}}</a-menu-item>
                </a-menu>
            </template>
        </a-dropdown>
    </template>
</template>
<script setup>
const props = defineProps({
    actions:{
        type    : Array,
        default : [],
    },
    max:{   //单行最大个数
        type    : Number,
        default : 3
    },
})
const outside = computed(()=>{
    let list  =  props.actions.filter((item)=>{
        return !!item.show;
    })
    return list.length>props.max?list.slice(0,props.max-1):list;
})
const inside = computed(()=>{
    let list  =  props.actions.filter((item)=>{
        return !!item.show;
    })
    return list.length>props.max?list.slice(props.max-1):[];
})
const handle = (row)=>{
    if(row.click){
        row.click();
    }
}
</script>
<style scoped lang="less">
</style>
