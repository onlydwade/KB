
<template>
    <div class="footer_bar" :class="{'footer_bar_fixed':fixed}">
        <div class="left">
            <slot name="left"></slot>
        </div>
        <div class="right">
            <a-space :size="16">
                <slot></slot>
            </a-space>
        </div>
    </div>
    <div class="footer_bar_placeholder" v-if="placeholder"></div>
</template>
<script setup>
    const props = defineProps({
        fixed:{
            type    : Boolean,
            default : false,
        },
        placeholder:{
            type    : Boolean,
            default : false,
        },
    })
</script>
<style scoped lang="less">

</style>
