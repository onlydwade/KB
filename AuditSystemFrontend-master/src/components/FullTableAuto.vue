
<template>
    <div class="full_table" ref="el">
        <slot></slot>
    </div>
</template>
<script setup>
import { useElementSize } from '@vueuse/core'
const el         = ref(null)
const { height } = useElementSize(el)
const props      = defineProps({
    headerH      : {
        type    : Number,
        default : 55,
    }
})
const emit     = defineEmits(['tableReady']);
const tableHeight = ref(200);
watch(()=>height.value,(val)=>{
    let h             = height.value-props.headerH;
    tableHeight.value = h
    emit('tableReady',tableHeight.value);
},{immediate:true})
</script>
<style scoped lang="less">
</style>
