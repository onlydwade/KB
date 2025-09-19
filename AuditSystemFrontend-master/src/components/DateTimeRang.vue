
<template>
    <a-range-picker :getPopupContainer="trigger => trigger.parentNode"
    v-model:value="timeRang" 
    :show-time="showTime"
    class="w_full"
    :valueFormat="valueFormat"
    :format="format" />
</template>
<script setup>
const props = defineProps({
    startTime : {
        type    : String,
        default : '',
    },
    endTime : {
        type    : String,
        default : '',
    },
    showTime   : {
        type    : Boolean,
        default : true,
    }
})
const emit   = defineEmits(['update:startTime','update:endTime']);
const valueFormat = computed(()=>{
    return 'YYYY-MM-DD' + (props.showTime?' HH:mm:ss':'');
})
const format = computed(()=>{
    return 'YYYY-MM-DD' + (props.showTime?' HH:mm':'');
})
const timeRang = computed({
    get : () => [props.startTime,props.endTime],
    set : (val) => {
        let times = val || ['',''];
        emit('update:startTime',times[0]);
        emit('update:endTime',times[1]);
    }
})    
</script>
<style scoped lang="less">

</style>
