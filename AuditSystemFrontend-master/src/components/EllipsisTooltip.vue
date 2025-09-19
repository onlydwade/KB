<template>
    <div class="ellipsis_box">
        <a-tooltip v-if="isShowTooltip" destroyTooltipOnHide placement="top" trigger="hover" overlayClassName="table-class" v-model:visible="isShow">
            <template #title>
                <span>{{ content }}</span>
            </template>
            <div class="content" @mouseleave="mouseleave">
                <span ref="contentRef">
                    {{ content }}
                </span>
            </div>
        </a-tooltip>
        <div v-else-if="mode==1 && processState !=null" class="content" @mouseenter="mouseenter">
            <span ref="contentRef">
                {{ content }}
                <a-tag class="status" :style="{backgroundColor:statesColor(processState)}">
                    {{processStatePlan(processState)}}
                </a-tag>
            </span>
        </div>
        <div v-else class="content" @mouseenter="mouseenter">
            <span ref="contentRef">
                {{ content }}

            </span>
        </div>
    </div>
</template>
<script setup>
const props = defineProps({
    content: {
        type    : String,
        default : "",
    },
    mode: {
        type    : Number,
        default : 0,
    },
    processState: {
        type    : Number,
        default : 0,
    }
})
const isShow        = ref(true);
const contentRef    = ref();
const isShowTooltip = ref(false);
const processStatePlan = (val) => {
    if(val === 0) {
        return '未处理'
    }
    if(val === 1) {
        return '审批中'
    }
    if(val === 2) {
        return '已处理'
    }
    if(val === 3) {
        return '处理被驳回'
    }
}
const statesColor = (val) =>{
    let colorStr = ""
    switch(val) {
        case 0:
            colorStr= '#f99c34'
            break;
        case 1:
            colorStr= '#02a7f0'
            break;
        case 2:
            colorStr= '#d9001b'
            break;
        case 3:
            colorStr= '#63a103'
            break;
        default:
            colorStr= ''
    }
    return colorStr
}
const mouseleave    = () => {
    // isShowTooltip.value = false
};
const mouseenter = () => {
    // 关键代码逻辑
    if (contentRef.value.parentNode.offsetWidth >= contentRef.value.offsetWidth) {
        isShowTooltip.value = false;
    } else {
        isShowTooltip.value = true;
    }
};
</script>

<style>
.ellipsis_box{
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
.content {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
.status{
    color: white;
    border-radius: 40px;
    width: 100px;
    text-align: center;
    height: 26px;
    line-height: 26px;
}
</style>