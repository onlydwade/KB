<template>
  <div class="dashboard_box">
      <a-spin :spinning="loadding">
          <Title title="实际签约情况"></Title>
          <div class="dashboard_inner">
              <RkEcharts
                ref="refEchart2"
                height="400px"
                class="chart"
                :option="option"
              />
          </div>
      </a-spin>
  </div>
</template>
<script setup>
import api from '@/api/index';
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
const loadding = ref(true);
const option   = ref({
  grid: {
      left: 100,
      top: 20,
      right: 0,
      bottom: 50
  },
  tooltip: {
      trigger: 'item'
  },
  legend: {
      orient: 'horizontal', //图例的显示方式  默认横向显示 horizontal  vertical
      bottom: 0,
      padding: [0, 80, 0, 80]
  },
  xAxis: {
      type: 'category',
      show: true,
      data: [], //内容
      axisTick: {
          show: false
      },
      axisLine: {
          lineStyle: {
              color: '#E2E8EC',
              width: 0.5
          }
      },
      axisLabel: {
          color: '#999EA5'
      }
  },
  yAxis: {
      type: 'value',
      axisLabel: {
          color: '#999EA5'
      },
      splitLine: {
          lineStyle: {
              type: 'dashed',
              color: '#E2E8EC',
              width: 0.5
          }
      }
  },
  series: [
      {
          name: '合同总金额',
          data: [],
          type: 'bar',
          barMaxWidth: 20,
          itemStyle: {
              color: 'rgba(249, 156, 52, 1)' //----单独设置某一个图例的颜色
          }
          // 柱条的背景样式设置
          // backgroundStyle: {
          //   color: 'rgba(249, 156, 52, 1)' // 柱条的颜色
          // }
      },
      {
          name: '合同年度金额',
          type: 'bar',
          barMaxWidth: 20,
          data: [],
          itemStyle: {
              color: 'rgba(255, 207, 135, 1)' //----单独设置某一个图例的颜色
          }
      },
      {
          name: '当年转化收入',
          type: 'bar',
          barMaxWidth: 20,
          data: [],
          itemStyle: {
              color: 'rgba(250, 204, 20, 1)' //----单独设置某一个图例的颜色
          }
      }
  ]
})
const resData = reactive({
  data: {}
})
const refEchart2 = ref()
const updateChartData = (data) => {
  /*
      chartData.xAxisLabel = data.map(item => item.month)
      chartData.availableAmountData = data.map(item => item.availableAmount)
      chartData.targetAmount = data.map(item => item.targetAmount)
      chartData.actualAmount = data.map(item => item.actualAmount)
  */
  if (data) {
      option.value.xAxis.data = data.map(item => item.date)
      option.value.series[0].data = data.map(item => item.contractAmount)
      option.value.series[1].data = data.map(item => item.contractAnnualAmount)
      option.value.series[2].data = data.map(item => item.annualConversionAmount)
  } else {
      option.value.xAxis.data = []
      option.value.series[0].data = []
      option.value.series[1].data = []
      option.value.series[2].data = []
  }
  refEchart2.value.updateChart()
}
const getData = ()=>{
  loadding.value = true;
  api.analysis.getActualSigning(props.level,props.deptId,props.dateVal).then(res => {
      if (res.code === 200){
          resData.data = res.data
          updateChartData(resData.data)
          loadding.value = false
      } else {
          updateChartData(resData.data)
          loadding.value = false
      }
  })
}
watch([()=>props.dateType,()=>props.dateVal,()=>props.level,()=>props.deptId], (val) => {
  if(props.dateType&&props.dateVal&&props.level&&props.deptId){
      getData();
  }
},{immediate:true})
</script>
<style scoped lang="less">

</style>
