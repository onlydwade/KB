
<template>
    <div class="status_box">
        <span class="dot" :style="'background-color:'+types[status].color"></span>
        <span class="name text_no_warp">
            {{types[status].name}}
        </span>
        <slot></slot>
    </div>
</template>
<script setup>
    const props = defineProps({
        msgStatus: {
            type    : Number,
            default : 1,
        }
    })
    const status = computed(()=>{
        return props.opportunityStatus || 1;
    })
    const types = {
        1:{
            name  : '',
            color : "#FF0000"
        }
    }
</script>
<style scoped lang="less">
.status_box {
    display     : inline-flex;
    align-items : center;
    .dot{
        width         : 8px;
        height        : 8px;
        border-radius : 50%;
        margin-right  : 8px;
    }
}
</style>
