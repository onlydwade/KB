
<template>
    <div class="status_box">
        <span class="dot" :style="'background-color:'+(types[status] || {}).color"></span>
        <span class="name text_no_warp">
            {{(types[status] || {}).name}}
        </span>
        <slot></slot>
    </div>
</template>
<script setup>
    const props = defineProps({
        projectStatus: {
            type    : String,
            default : '',
        }
    })
    const status = computed(()=>{
        return props.projectStatus || 'LI_XIANG_ZHONG';
    })
    const types = {
        'LI_XIANG_ZHONG':{
            name  : '立项中',
            color : "#f99c34"
        },
        'TUO_ZHAN_ZHONG':{
            name  : '拓展中',
            color : "#f99c34"
        },
        'TUO_ZHAN_CHENG_GONG':{
            name  : '拓展成功',
            color : "#409EFF"
        },
        'ZAI_GUAN':{
            name  : '在管',
            color : "#52c41a"
        },
        'YI_ZHONG_ZHI':{
            name  : '已终止',
            color : "#f5222d"
        },
        'TOU_BIAO_ZHONG':{
            name  : '投标中',
            color : "#f99c34"
        },
        'YI_ZHONG_BIAO':{
            name  : '已中标',
            color : "#52c41a"
        },
        'ZHONG_BIAO':{
            name  : '已中标',
            color : "#52c41a"
        },
        'WEI_ZHONG_BIAO':{
            name  : '未中标',
            color : "#f5222d"
        },
        'JIN_DIAO_ZHONG':{
            name  : '尽调中',
            color : "#409EFF"
        },
        'PING_SHEN_ZHONG':{
            name  : '评审中',
            color : "#409EFF"
        },
        'WANG_CHENG_QIAN_YUE':{
            name  : '完成签约',
            color : "#52c41a"
        },
        'YI_DAO_QI':{
            name  : '已到期',
            color : "#f5222d"
        },
        'YI_WAN_JIE':{
            name  : '已完结',
            color : "#f5222d"
        },
        'YI_FEI_ZHI':{
            name  : '已废止',
            color : "#DCDFE6"
        },
        'YI_TUI_CHANG':{
            name  : '已退场',
            color : "#F522EC"
        },
        'CLOSE_FILE':{
            name  : '已归档',
            color : "#f5222d"
        },

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
