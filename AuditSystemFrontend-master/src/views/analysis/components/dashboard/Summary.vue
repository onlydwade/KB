<template>
     <div class="dashboard_box">
        <a-spin :spinning="loadding">
            <Title title="整体项目情况">
                <template #left >
                    <a-space>
                        <a v-if="visible" @click="openZtxmqkDetailModal">详情</a>
                    </a-space>
                </template>
            </Title>
            <div class="dashboard_inner">
                <div class="container_main">
                  <div class="box_color1 container_font">
                    <div class="numValue">{{ amountFormat(chartData.projectTotal) }}</div>
                    <div class="titleValue">在管项目总数</div>
                  </div>
                  <div class="box_color1 container_font">
                    <div class="numValue">{{ amountFormat(chartData.waiProjectTotal) }} ㎡</div>
                    <div class="titleValue">在管项目面积总数</div>
                  </div>
                    <div class="box_color2 container_font">
                        <div class="numValue">¥{{ parseFormatNum(chartData.xzzhsr) }} </div>
                        <div class="titleValue">当年新增合同转化收入</div>
                    </div>
                    <div class="box_color2 container_font">
                        <div class="numValue">{{ parseFormatNum(chartData.newWaiProjectTotal) }} ㎡</div>
                        <div class="titleValue">当年新增面积总数</div>
                    </div>
                  <div class="box_color3 container_font">
                    <div class="numValue">{{ amountFormat(chartData.signProjectTotal) }}</div>
                    <div class="titleValue">新签项目总数</div>
                  </div>
                    <div class="box_color3 container_font">
                        <div class="numValue">{{ amountFormat(chartData.signRenewalProjectTotal) }}</div>
                        <div class="titleValue">续签项目总数</div>
                    </div>
                    <div class="box_color6 container_font">
                        <div class="numValue">¥{{ parseFormatNum(chartData.xzzje) }}</div>
                        <div class="titleValue">新增合同总金额</div>
                    </div>
                  <div class="box_color6 container_font">
                    <div class="numValue">¥{{ parseFormatNum(chartData.xqzje) }}</div>
                    <div class="titleValue">续签合同总金额</div>
                  </div>

                  <div class="box_color6 container_font">
                    <div class="numValue">¥{{ parseFormatNum(chartData.xzndzje) }}</div>
                    <div class="titleValue">新增合同年度金额</div>
                  </div>

                  <div class="box_color6 container_font">
                    <div class="numValue">¥{{ parseFormatNum(chartData.xqndzje) }}</div>
                    <div class="titleValue">续签合同年度金额</div>
                  </div>
                </div>
            </div>
        </a-spin>
    </div>
    <ZtxmqkDetailModal  ref="ztxmqkDetailModalRer" />
</template>
<script setup>
import api                from '@/api/index';
import { parseFormatNum,amountFormat,numFixed } from '@/utils/tools'
import ZtxmqkDetailModal from './correlation/ZtxmqkDetailModal.vue';
const ztxmqkDetailModalRer = ref(null);
const props = defineProps({
    dateType:{
        type    : String,
        default : 'year',
    },
    dateVal:{
        type    : String,
        default : null,
    },
    level:{
        type    : Number,
        default : null,
    },
    deptId:{
        type    : Number,
        default : null,
    },
})
var visible = false;
const chartData = ref({})
const loadding   = ref(true);
const openZtxmqkDetailModal = () => {
     ztxmqkDetailModalRer.value.open(props.level,props.deptId,props.dateVal)
};
const getData          = ()=>{
    loadding.value = true;
    api.analysis.getProjectSituation(props.level,props.deptId,props.dateVal).then(res => {
        if (res.code === 200 ){
          chartData.value = res.data
        }
        loadding.value = false
    })

}
watch([()=>props.dateType,()=>props.dateVal,()=>props.level,()=>props.deptId], (val) => {
    if(props.dateType&&props.dateVal&&props.level&&props.deptId){
        getData();
        if(props.level<=2){
            visible = true;
        }else{
            visible = false;
        }
    }
},{immediate:true})
</script>
<style scoped lang="less">
.container_main {
    display   : flex;
    flex-wrap : wrap;
    .container_font {
        margin        : 8px 4px;
        padding-left  : 26px;
        padding       : 12px;
        width         : 48%;
        border-radius : 10px;
        color         : #ffffff;
        .numValue {
            font-size   : 28px;
            font-weight : 700;
            line-height : 35px;
        }
        .titleValue {
            font-size   : 14px;
            font-weight : 400;
            line-height : 35px;
        }
    }
    .box_color1 {
        background-color:#f99c34;
    }
    .box_color2 {
        background-color:#d47b22;
    }
    .box_color3 {
        background-color:#fec03d;
    }
    .box_color4 {
        background-color:#ffa943;
    }
    .box_color5 {
        background-color:#ffb95e;
    }
    .box_color6 {
        background-color:#ff9032;
    }
    .box_color7 {
        background-color:#f99c34;
    }
    .box_color8 {
        background-color:#f99c34;
    }
}
</style>
