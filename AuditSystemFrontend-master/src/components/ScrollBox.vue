
<template>
    <div class="scroll-box">
        <div class="scroll-inner">
            <AScrollbar>
                <slot></slot>
            </AScrollbar>
        </div>
    </div>
</template>
<script setup>
</script>
<style scoped lang="less">
.scroll-box{
    position   : relative;
    flex       : 1;
    min-width  : 100%;
    height     : 100%;
    box-sizing : border-box;
    .scroll-inner{
        position   : absolute;
        width      : 100%;
        height     : 100%;
        top        : 0;
        left       : 0;
        box-sizing : border-box;
    }
    &:last-child{
        margin-bottom: 16px;
    }
}
</style>
