<template>
  <div class="dashboard_box">
    <a-spin :spinning="loadding">
      <Title title="签约趋势及排名" v-if="resData.data.cityRanking.length>0"></Title>
      <Title title="签约趋势" v-else></Title>
      <a-row>
        <a-col :span="16" v-if="resData.data.cityRanking.length>0">
          <RkEcharts ref="refEchart2" class="chart" height="400px" :option="option" />
        </a-col>
        <a-col :span="24" v-else>
          <RkEcharts ref="refEchart2" class="chart" height="400px" :option="option" />
        </a-col>
        <a-col :span="8" v-if="resData.data.cityRanking.length>0">
          <div class="dashboard_inner_cs">
            <div class="rank_box">
              <h5 class="title">
                <span style="padding-left: 20px">排名情况</span>
              </h5>
              <ScrollBox>
                <div class="scroll-main">
                  <div
                    class="rank_item"
                    v-for="(item, index) in resData.data.cityRanking"
                    :key="item.deptId"
                  >
                    <span class="sort" :class="index < 3 ?'sort_active':''">{{ index + 1 }}</span>
                    <span class="name">
                      <EllipsisTooltip :content="item.deptName" />
                    </span>
                    <span class="num">￥{{ parseFormatNum(item.total,2) }}</span>
                  </div>
                </div>
              </ScrollBox>
            </div>
          </div>
        </a-col>
      </a-row>
      <div></div>
    </a-spin>
  </div>
</template>
<script setup>
import { parseFormatNum,amountFormatTow} from '@/utils/tools'
import api from "@/api/index";
const props = defineProps({
  dateType: {
    type: String,
    default: "year",
  },
  dateVal: {
    type: String,
    default: null,
  },
  level: {
    type: Number,
    default: null,
  },
  deptId: {
    type: Number,
    default: null,
  },
});
const loadding = ref(true);
const option = ref({
  title: {},
  grid: {
    left: 100,
    top: 20,
    right: 0,
    bottom: 50,
  },
  tooltip: {
    trigger: "item",
  },
  legend: {
    orient: "horizontal", //图例的显示方式  默认横向显示 horizontal  vertical
    bottom: -3,
    padding: [0, 80, 0, 80],
    show: false,
  },
  xAxis: {
    type: "category",
    show: true,
    data: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"], //内容
    axisTick: {
      show: false,
    },
    axisLine: {
      lineStyle: {
        color: "#E2E8EC",
        width: 0.5,
      },
    },
    axisLabel: {
      color: "#999EA5",
    },
  },
  yAxis: {
    type: "value",
    axisLabel: {
      color: "#999EA5",
    },
    splitLine: {
      lineStyle: {
        type: "dashed",
        color: "#E2E8EC",
        width: 0.5,
      },
    },
  },
  series: [
    {
      name: "项目签约金额(含税)",
      data: [], // [200, 200, 150, 80, 70, 110, 130, 56, 34, 67, 13, 50],
      type: "bar",
      barMaxWidth: 20,
      itemStyle: {
        color: "rgba(249, 156, 52, 1)", //----单独设置某一个图例的颜色
      },
      // 柱条的背景样式设置
      // backgroundStyle: {
      //   color: 'rgba(249, 156, 52, 1)' // 柱条的颜色
      // }
    },
  ],
});
const tabKey = ref("salesVolume");
const refEchart2 = ref();
const resData = reactive({
  data: {
    salesVolume: [],
    achievement: [],
    conversionRate: [],
    cityRanking: [],
  },
});
const tabChange = () => {
    const data = resData.data
    if (data && data[tabKey.value]) {
        option.value.series[0].data = data[tabKey.value].map(item => item.value)
    } else {
        option.value.series[0].data = []
    }
    refEchart2.value.updateChart()
}
const getData  = ()=>{
    loadding.value = true;
    api.analysis.getSigning(props.level,props.deptId,props.dateVal).then(res => {
        if (res.code === 200 ){
            resData.data = res.data
            tabChange()
        } else {
            tabChange()
        }
        loadding.value = false
    })
}
watch(
  [() => props.dateType, () => props.dateVal, () => props.level, () => props.deptId],
  val => {
    if (props.dateType && props.dateVal && props.level && props.deptId) {
      getData();
    }
  },
  { immediate: true }
);
</script>
<style scoped lang="less">
.dashboard_box {
  :deep(.ant-tabs-nav-wrap) {
    padding: 0 16px;
    .ant-tabs-tab {
      padding-top: 16px;
      padding-bottom: 16px;
    }
  }
}
.title_t {
  padding-left: 16px;
  font-size: 20;
  font-weight: bold;
}
.dashboard_inner_cs{
    display : flex;
    height  : 100%;
    // padding: 0 !important;
    .scroll-main{
      padding: 10px 20px;
    }
    .rank_box{
        width            : 0;
        flex             : 1;
        background-color : #fff;
        border-radius    : 8px;
        display          : flex;
        flex-direction   : column;
        .title{
            font-size : 16px;
            // padding   : 12px;
        }
    }
}
.rank_item{
    display       : flex;
    align-items   : center;
    margin-bottom : 10px;
    .sort{
        height           : 26px;
        width            : 26px;
        background-color : #eee;
        text-align       : center;
        line-height      : 26px;
        border-radius    : 50%;
        margin-right     : 8px;
    }
    .sort_active{
        background-color : #314659;
        color            : #fff;
    }
    .sort_active1{
        background-color : #f99c34;
        color            : #fff;
    }
    .sort_active2{
        background-color : green;
        color            : #fff;
    }
    .name{
        flex  : 1;
        width : 0;
    }
    .num{
        margin-left: 8px;
    }
}
</style>
