<template>
  <div class="dashboard_box">
      <a-spin :spinning="loadding">
          <Title title="项目业态占比">
              <template #left >
                  <a-space>
                      <a-select @change="zgTypeChange" v-model:value="zgType" button-style="solid"  style="width: 180px;">
                          <a-select-option :value="1">在管业态分析</a-select-option>
                          <a-select-option :value="2">当年拓展业态分析</a-select-option>
                      </a-select>
                  </a-space>
              </template>
            <template #right>
                  <a-space style="font-size: 12px;">
                      <a-radio-group
                          v-model:value="levelType"
                          @change="changeRadio"
                          button-style="solid"
                          size="small"
                      >
                          <a-radio-button value="level_1">一级业态</a-radio-button>
                          <a-radio-button value="level_2">二级业态</a-radio-button>
                      </a-radio-group>
                  </a-space>
              </template>
          </Title>
          <div class="dashboard_inner">
              <RkEcharts
                ref="refEchart3"
                height="640px"
                id="chart3"
                :option="option"
              />
          </div>
      </a-spin>
  </div>
</template>
<script setup>
import api from '@/api/index';
import * as echarts from 'echarts'
import {throttle}   from '@/utils/tools';
import { parseFormatNum,amountFormatTow , getPercentage} from '@/utils/tools'
import { message } from 'ant-design-vue';
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
const levelType = ref('level_1')
const loadding = ref(false);
const zgType = ref(1);
const chartData = ref([
  // {
  //     value: 0,
  //     name: '住宅',
  // },
  // {
  //     value: 0,
  //     name: '工业',
  // },
  // {
  //     value: 0,
  //     name: '办公',
  // },
  // {
  //     value: 0,
  //     name: '学校',
  // },
  // {
  //     value: 0,
  //     name: '医院',
  // },
  // {
  //     value: 0,
  //     name: '军队',
  // },
  // {
  //     value: 0,
  //     name: '城市服务',
  // },
  // {
  //     value: 0,
  //     name: '酒店',
  // },
  // {
  //     value: 0,
  //     name: ' 产业园区',
  // },
  // {
  //     value: 0,
  //     name: '其他',
  // }
])
const colorList =[
'rgb(250,171,83,1)',
'rgb(147,205,223,1)',
'rgb(238,206,148,1)',
'rgb(144,176,50,1)',
'rgb(186,135,224,1)'
]
// let formatNumber = (num) => num.toString().replace(/(?=(\B)(\d{3})+$)/g, ',');
const option   = ref({})
const contractAmountSum = ref(0)
const refEchart3 = ref(null)
const myChart    = ref(null);
const resizeHandler = throttle(() => {
  if (myChart.value) {
      myChart.value.resize();
  }
},200);
onMounted(() => {
  window.addEventListener("resize", resizeHandler);
})
onBeforeUnmount(() => {
  window.removeEventListener("resize", resizeHandler);
});
const zgTypeChange =()=>{
    if(levelType.value==='level_1'){
        getData('XIANG_MU_YE_TAI');
    }else{
        getData('XIANG_MU_YE_TAI2');
    }
}
const getData = (type)=>{
  loadding.value = true;
  api.analysis.getProjectYETAI(props.level,props.deptId,props.dateVal,type,zgType.value).then(res => {
      if (res.code === 200 ){
        contractAmountSum.value = res.data.contractAmountSum
          loadding.value = false
          updateChartData(res.data.projectYETAI,res.data.contractAmountSum)

      }
  })
}



const updateChartData = (data) => {
    chartData.value= data.map(item=>{
      let obj ={}
      obj.name = item.label
      obj.value = item.contractAmount
      obj.percentage = item.percentage
      obj.key = item.value
      return obj
    })
    option.value = {
      title: {
          text:`{name|合同总金额}\n{val|￥${parseFormatNum(contractAmountSum.value)}}`,
          top: "center",
          left: "20%",
          textAlign: "center",
          textStyle: {
            rich: {
              name: {
                color: '#aaaaaa',
                lineHeight: 40,
                fontSize:16
            },
            val: {
                fontSize:16
            },
            },

          }
        },
      tooltip: {
          trigger: 'item'
      },
      //图例数据解析
      legend: {
          type: 'scroll',
          //图例  标注各种颜色代表的模块
          orient: 'vertical', //图例的显示方式  默认横向显示 horizontal  vertical
          right: '0%', //控制图例出现的距离  默认左上角
          top: 20,
          left: "45%", //控制图例的位置
          itemWidth: 10, //图例颜色块的宽度和高度
          itemHeight: 10,
          icon: 'circle', // 图例前的图标为圆点
          itemGap: 20, //图例之间的间距
          borderWidth: 0, // 图例边框线宽
          padding: [0, 0, 0, 0],
          formatter: (name) => {
              let obj = "";
              chartData.value.forEach((item) => {
                //格式化图例文本，支持字符串模板和回调函数两种形式。
                if (item.name === name) {
                  obj = item
                  return;
                }
              });
              return `{name|${ echarts.format.truncateText(name, 80, '14px Microsoft Yahei', '…')}}  {b|${getPercentage(obj.value,contractAmountSum.value)} %}  {m|￥ ${parseFormatNum(obj.value)}}`;
            },
            textStyle: {
              //图例中文字的样式
              fontSize: 12,
              padding: [0, 0, 0, 4] ,// 修改文字和图标距离
              rich: {
              b: {
                  width:70,
                  color: '#aaaaaa',
                  lineHeight: 20,
                  fontSize:12,
              },
              name:{
                width:80,
                lineHeight: 20,
                fontSize:12,
              }
            }
          },
          tooltip: {
            show: true
          }
      },
      series: [
          {
              type: 'pie',
              radius: ['40%', '30%'],
              center: ['20%', '50%'],
              avoidLabelOverlap: false,
              itemStyle: {
                  borderRadius: 5,
                  borderColor: '#fff',
                  borderWidth: 2
              },
              label: {
                  show: false,
                  position: 'center'
              },
              // emphasis: {
              //     label: {
              //         show: true,
              //         fontSize: 20
              //     }
              // },
              labelLine: {
                  show: false
              },
              data: chartData,
              // color:colorList
          }
      ]
    }
    refEchart3.value.updateChart()
      myChart.value = echarts.init(document.getElementById('chart3'));
      myChart.value.off("legendselectchanged");
      myChart.value.on("legendselectchanged", (params) => {
        // console.log("🚀 ~ file: ProjectYetai.vue:245 ~ myChart.value.on ~ params:", params)
        myChart.value.setOption({
            legend: { selected: { [params.name]: true } },
        });
        if( levelType.value  != 'level_1') return
         levelType.value = 'level_2'
        chartData.value.forEach(item => {
          if(item.name==params.name){
            getData(item.key);
            return
          }
        });
      });

}

const changeRadio =(e)=>{
  if(e.target.value=='level_1'){
    getData('XIANG_MU_YE_TAI');
  }else{
    message.warning('请点击以下节点业态名称查看二级业态数据')
    levelType.value = 'level_1'
  }
}

watch([()=>props.dateType,()=>props.dateVal,()=>props.level,()=>props.deptId], (val) => {
  if(props.dateType&&props.dateVal&&props.level&&props.deptId){
      getData('XIANG_MU_YE_TAI');
  }
},{immediate:true})

</script>
<style scoped lang="less">
.dashboard_box{
}
</style>
