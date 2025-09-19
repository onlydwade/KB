
<template>
    <a-affix :offset-bottom="0" v-if="affixVisible">
        <slot name="top"></slot>
        <div class="footer_bar">
            <div class="left">
                <a-space :size="16">
                    <slot></slot>
                </a-space>
            </div>
            <div class="right">
                <slot name="right"></slot>
            </div>
        </div>
    </a-affix>
</template>
<script setup>
    const props = defineProps({
    })
    
    const affixVisible = ref(false);
    onMounted(() => {
        setTimeout(()=>{
            affixVisible.value = true;
        },500)
    })
    
</script>
<style scoped lang="less">

</style>
