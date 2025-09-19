
<template>
    <div class="title_box">
        <div class="left text_no_warp">
            <span class="title text_no_warp">
                <span v-if="title">{{title}}</span>
                <slot name="title" v-else></slot>
            </span>
            <span v-if="subTitle" class="sub_title">{{subTitle}}</span>
            <slot></slot>
            <slot name="left"></slot>
        </div>
        <slot name="right"></slot>
    </div>
</template>
<script setup>
    const props = defineProps({
        title    : String,
        subTitle : String
    })

</script>
<style scoped lang="less">
.title_box {
    padding         : 16px;
    // height          : 36px;
    padding-bottom  : 0;
    box-sizing      : border-box;
    display         : flex;
    justify-content : flex-end;
    align-items     : center;
    .left{
        flex        : 1;
        display     : flex;
        align-items : center;
    }
    .title{
        font-size    : 16px;
        line-height  : 20px;
        font-weight  : bold;
        color        : @heading-color;
        position     : relative;
        padding-left : 10px;
        margin-right : 10px;
        &::before{
            content:'';
            width            : 3px;
            height           : 16px;
            background-color : @primary-color;
            border-radius    : 1.5px;
            position         : absolute;
            top              : 0;
            left             : 0;
            bottom           : 0;
            margin           : auto;
        }
    }
    .sub_title{
        font-size   : 14px;
        line-height : 20px;
        color       : @text-color;
    }
}
</style>
