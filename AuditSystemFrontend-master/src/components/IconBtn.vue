
<template>
    <a-tooltip
    :title="tips"
    :placement="placement">
        <slot></slot>
    </a-tooltip>
</template>
<script setup>
    const props = defineProps({
        title     : String,
        type      : {
            type    : String,
            default : 'primary',
        },
        placement : {
            type    : String,
            default : 'bottom',
        },
    })
    
    const emit = defineEmits(['click'])
    const clickBtn = (e)=>{
        if (props.disabled == true) {
            e.stopPropagation()
            e.preventDefault()
            return
        }
        emit('click', e);
    }
</script>
<style scoped lang="less">

</style>
