<template>
  <div class="dashboard_box">
      <a-spin :spinning="loadding">
          <Title title="业绩达成情况">
              <template #left >
                  <a-space>
                    <a v-if="visible" @click="openYjdcqkDetailModal">详情</a>
                  </a-space>
              </template>
              <template #right>
                  <a-space style="font-size: 12px;">
                      <a-radio-group
                          v-model:value="performanceType"
                          @change="changeRadio"
                          button-style="solid"
                          size="small"
                          v-for="(item,index) in radioList"
                          :key="index"
                      >
                          <a-radio-button :value=item.key>{{item.name}}</a-radio-button>
                      </a-radio-group>
                  </a-space>
              </template>
          </Title>
          <div class="dashboard_inner" style="display: flex;">
             <div style="width: 70%;">
              <RkEcharts
                  ref="refEchart2"
                  height="300px"
                  class="chart"
                  :option="option"
              />
             </div>
             <div  style="width: 30%;display: flex">
                <ul class="ul-style" v-if="performanceType=='YXXXTBL' || performanceType=='XMBLL'">
                  <li><p>
                      目标值：{{targetValue }}
                  </p></li>
                  <li><p>
                      实际值：{{actualValue}}
                  </p></li>
                </ul>
                <ul class="ul-style" v-else>
                  <li><p>
                      目标值：¥{{parseFormatNum(targetValue) }}
                  </p></li>
                  <li><p>
                      实际值：¥{{ parseFormatNum(actualValue) }}
                  </p></li>
                </ul>
             </div>
          </div>

      </a-spin>
  </div>
  <YjdcqkDetailModal ref="yjdcqkDetailModalRef" />
</template>
<script setup>
import api from '@/api/index';
import { parseFormatNum,amountFormatTow,numFixed } from '@/utils/tools'
import { reactive } from 'vue';
import YjdcqkDetailModal from './correlation/YjdcqkDetailModal.vue';
const yjdcqkDetailModalRef = ref(null);

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
const refEchart2 = ref()
const loadding = ref(true);
const resData = reactive({
  data: {}
})
const targetValue = ref(0) //目标值
const actualValue = ref(0) // 实际值
const completionRate =  ref(0)  // 完成率
const performanceType = ref('HTZJE')
const radioStrList = ['HTZJE','HTNDJE','ZDTZHTJE','YXXXTBL','XMBLL'];
let radioList = [];
var visible = false;
const openYjdcqkDetailModal = () => {
    yjdcqkDetailModalRef.value.open(props.level,props.deptId,props.dateVal)
};
const option  = ref({
  tooltip: {
      trigger: 'item',
      valueFormatter: function (value) {
          return `${getValue(value)} %`
      }
  },
  grid: {
    top: 0,

  },
  series: [
      {
          type: 'gauge',
          min: 0,
          max: 1,
          splitNumber: 4,
          center: ['60%', '50%'] ,
          anchor: {
              show: true,
              showAbove: true,
              size: 18,
              itemStyle: {
                  color: '#FAC858'
              }
          },
          pointer: {
              icon: 'path://M2.9,0.7L2.9,0.7c1.4,0,2.6,1.2,2.6,2.6v115c0,1.4-1.2,2.6-2.6,2.6l0,0c-1.4,0-2.6-1.2-2.6-2.6V3.3C0.3,1.9,1.4,0.7,2.9,0.7z',
              width: 8,
              length: '80%',
              offsetCenter: [0, '8%']
          },
          progress: {
              show: true,
              overlap: true,
              roundCap: true
          },
          axisLine: {
              show: true,
              roundCap: true
          },
          axisLabel: {
              show: true, // 是否轴线显示标签。
              distance: 25, // 标签与刻度线的距离。
              formatter: '{value} %', // 刻度标签的内容格式器，支持字符串模板和回调函数两种形式。
              formatter: function (value) {
                  if (value >= 0.8) {
                      return '优'
                  } else if (value >= 0.6) {
                      return '良'
                  } else if (value >= 0.4) {
                      return '中'
                  } else if (value >= 0.2) {
                      return '差'
                  }
                  return '0'
              }
          },

          data: [
              {
                  value:0,
                  name: '完成率',
                  itemStyle: {
                      // 数据项的指针样式。
                      color: 'rgb(250,170,81,1)', // 图形的颜色。
                      borderWidth: 1 // 描边线宽。为 0 时无描边。
                      // borderColor: '#eee'
                  }
              }
          ],
          title: {
              fontSize: 14,
              offsetCenter: [0, '72%']
          },
          axisTick: {
              // 刻度样式。
              show: false, // 是否显示刻度。
              splitNumber: 0, // 分隔线之间分割的刻度数。默认 5
              distance: -30, // 分隔线与轴线的距离。
              length: 8, // 刻度线长。支持相对半径的百分比。
              lineStyle: {
                  // { color , width , type , dashOffset , cap , join , miterLimit , shadowBlur , shadowColor , shadowOffsetX , shadowOffsetY , opacity }
                  color: '#fff',
                  width: 2
              }
          },
          detail: {
              // 仪表盘详情，用于显示数据。
              show: true, // 是否显示详情。
              valueAnimation: true, // 是否开启标签的数字动画。
              formatter: '{value}', // 格式化函数或者字符串
              color: 'auto', // 文本颜色，默认取数值所在的区间的颜色。
              offsetCenter: [0, '100%'], // 相对于仪表盘中心的偏移位置，数组第一项是水平方向的偏移，第二项是垂直方向的偏移。可以是绝对的数值，也可以是相对于仪表盘半径的百分比。
              // 字体相关样式
              fontSize: 20,
              backgroundColor: '',
              borderRadius: 3,
              width: 40,
              height: 14,
              color: '#000000',
              formatter: function (value) {
                return `${getValue(value)} %`
              }
          }

      }
  ]
})
function getValue (val){
  return numFixed(val*100,2)
}

const updateChartData = () => {
    let data = resData.data

    data.forEach((mp, i) => {

        if(mp.key === performanceType.value ){
            targetValue.value = mp.mb
            actualValue.value  = mp.sj
            console.log(actualValue.value )
            console.log(targetValue.value )


            if(actualValue.value  === undefined || targetValue.value === undefined || actualValue.value === 0 || targetValue.value === 0){
              completionRate.value  = 0
            }else{
              completionRate.value  = (actualValue.value  / targetValue.value )
            }
            option.value.series[0].data[0].value =   completionRate.value
        }

    })

    if(radioList.length === 0){
        targetValue.value = 0
        actualValue.value  = 0
        option.value.series[0].data[0].value = 0
    }
    refEchart2.value.updateChart()

}
const changeRadio = () =>{
  updateChartData()
}
const getData = ()=>{
  loadding.value = true;
    api.analysis.getAchievements(props.level,props.deptId,props.dateVal).then(res => {
        if (res.code === 200 ){
            resData.data = res.data

            radioList = []
            resData.data .forEach((mp, i) => {
                if (radioStrList.includes(mp.key)) {
                    radioList.push(mp)
                }
            })
            updateChartData()
        }
        if(props.level<=2){
            visible = true;
        }else{
            visible = false;
        }
        loadding.value = false
    })

}

watch([()=>props.dateType,()=>props.dateVal,()=>props.level,()=>props.deptId], (val) => {
  if(props.dateType&&props.dateVal&&props.level&&props.deptId){
      getData();
  }
},{immediate:true})
</script>

<style scoped lang="less">
.ul-style {
  margin: auto 10px;
  padding-bottom: 16px;
  font-size: 18px;
  li {
    list-style-type: disc;
    color: #F99C34;
    p { color: rgba(0, 0, 0, 0.7); }
  }
}
</style>
