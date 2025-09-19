<template>
    <div class="dashboard_box">
        <a-spin :spinning="loadding">
            <Title title="漏斗分析"></Title>
            <div class="dashboard_inner">
              <RkEcharts
                ref="refEchart6"
                height="280px"
                class="chart"
                :option="option"
              />
            </div>
        </a-spin>
    </div>
</template>
<script setup>
import api from '@/api/index';
import { getPercentage } from '@/utils/tools'
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
const loadding = ref(false);
const refEchart6 = ref(null);
const chartData = ref([])
const option = ref({})
const getData = ()=>{
    loadding.value = true;
    loadding.value = true;
    api.analysis.getFunnelAnalysis(props.level,props.deptId,props.dateVal).then(res => {
        if (res.code === 200 ){
          let obj = res.data.data
          let total = res.data.total
          let arr = []
          for (let key in obj) {
              console.log(obj);
              let k ={}
              if(key=='xmxxzl'){
                  k['name']='项目信息总量'
                  k['pct'] = getPercentage(obj.xmxxzl,total)
              }
              if(key=='xmgjzl'){
                k['name']='项目跟进总量'
                k['pct'] =getPercentage(obj.xmgjzl ,total)
              }
              if(key=='cgxmzl'){
                  k['name']='成功项目总量'
                  k['pct'] =getPercentage(obj.cgxmzl,total)
              }

              k['value'] =obj[key]
              arr.push(k)
          }
          option.value =  {
                  tooltip: {
                    trigger: 'item',
                    formatter: (params) => {
                      return `${params.data.name} : ${params.data.value} , ${params.data.pct} %`;
                    },
                  },
                  color: ['#ffcc7c','#f97810','#f99c34'],
                  series: [
                    {
                      name: 'Funnel',
                      type: 'funnel',
                      left: '10%',
                      top: 60,
                      bottom: 60,
                      width: '60%',
                      minSize: '30%',
                      maxSize: '100%',
                      label: {
                        show: true,
                        position: 'inside',
                        color:'#fff',
                        fontSize:'16px',
                        formatter: (params) => {
                          return `${params.data.pct} %`;
                        },
                      },
                      itemStyle: {
                        borderColor: '#fff',
                        borderWidth: 1
                      },
                      emphasis: {
                        label: {
                          fontSize: 20
                        }
                      },
                      data:arr
                    },
                    {
                      name: 'Funnel',
                      type: 'funnel',
                      left: '10%',
                      top: 60,
                      bottom: 60,
                      width: '60%',
                      minSize: '30%',
                      maxSize: '100%',
                      label: {
                        // show: true,
                        // position: 'inside',
                        formatter: '{b} {c}',
                      },
                      itemStyle: {
                        borderColor: '#fff',
                        borderWidth: 1
                      },
                      emphasis: {
                        label: {
                          fontSize: 20
                        }
                      },
                      data:arr
                    }
                  ]
          }
        refEchart6.value.updateChart()
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

</style>
