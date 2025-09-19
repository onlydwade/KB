
<template>
<div class="follow_box" :style="'color:'+color" v-if="expireTime">
    <span class="dot" :style="'background-color:'+color">
        <span class="animation" :style="'background-color:'+color"></span>
    </span>
    {{dayStr}}
</div>
</template>
<script setup>
import moment from 'moment';
const props = defineProps({
    type: {
        type: Number,
        default: 1,
    },
    expireTime: {
        type: String,
        default: '',
    }
})
const color = ref('#f99c34');

const dayStr = computed(() => {
    let dayDiff = moment(moment(props.expireTime)).diff(new Date(), 'days');
    let str = '';
    if (dayDiff < 10) {
        color.value = '#f5222d';
    }
    if (dayDiff < 0) {
        str = "已到期" + Math.abs(dayDiff) + "天";
    } else if (dayDiff == 0) {
        str = "今日到期";
    } else {
        str = "剩余" + Math.abs(dayDiff) + "天到期";
    }
    return str;
})
</script>
<style scoped lang="less">
.follow_box {
    display: inline-flex;
    align-items: center;
    .dot {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        margin-right: 8px;
        position: relative;
        .animation{
            position                    : absolute;
            border-radius               : 50%;
            width                       : 150%;
            height                      : 150%;
            top                         : -25%;
            left                        : -25%;
            background-color            : rgba(0,0,0,0);
            -webkit-animation           : twinkling 1s infinite ease-in-out;
            animation                   : twinkling 1s infinite ease-in-out;
            -webkit-animation-fill-mode : both;
            animation-fill-mode         : both;
        }
    }
}
.follow_btn {
    color: #fff;
    font-size: 14px;
    font-weight: normal;
    padding: 4px 16px;
    line-height: 20px;
    border-radius: 14px;
}
@-webkit-keyframes twinkling {
    0% {
        opacity: 0.2;
        filter:alpha(opacity=20);
        -webkit-transform: scale(1);
    }
    50% {
        opacity: 0.5;
        filter:alpha(opacity=50);
        -webkit-transform: scale(1.12);
    }
    100% {
        opacity: 0.2;
        filter:alpha(opacity=20);
        -webkit-transform: scale(1);
    }
}
@keyframes twinkling {
    0% {
        opacity: 0.2;
        filter:alpha(opacity=20);
        -webkit-transform: scale(1);
    }
    50% {
        opacity: 0.5;
        filter:alpha(opacity=50);
        -webkit-transform: scale(1.12);
    }
    100% {
        opacity: 0.2;
        filter:alpha(opacity=20);
        -webkit-transform: scale(1);
    }
}
</style>
