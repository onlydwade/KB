
<template>
    <div class="follow_box" v-if="type==1&&days>14">
        <span class="dot" :style="'background-color:'+color"></span>
        {{days}} 天未跟踪
    </div>
    <span class="follow_btn" v-if="type==2&&days>14" :style="'background-color:'+color">
        {{days}} 天未跟踪
    </span>
</template>
<script setup>
    import moment from 'moment';
    const props = defineProps({
        type: {
            type    : Number,
            default : 1,
        },
        followTime: {
            type    : String,
            default : '',
        }
    })
    const color = ref('#f99c34');
    const days = computed(()=>{
        let dayDiff = moment(new Date()).diff(moment(props.followTime), 'days');
        if(dayDiff>14){
            color.value = '#f5222d';
        }
        return dayDiff;
    })
</script>
<style scoped lang="less">
.follow_box {
    display     : inline-flex;
    align-items : center;
    .dot{
        width         : 8px;
        height        : 8px;
        border-radius : 50%;
        margin-right  : 8px;
    }
}
.follow_btn{
    color         : #fff;
    font-size     : 14px;
    font-weight   : normal;
    padding       : 4px 16px;
    line-height   : 20px;
    border-radius : 14px;
}
</style>
