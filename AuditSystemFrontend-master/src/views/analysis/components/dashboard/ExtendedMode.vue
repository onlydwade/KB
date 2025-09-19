<template>
  <div class="dashboard_box">
      <a-spin :spinning="loadding">
          <Title title="占比统计">
            <template #left >
              <a-space>
                <a-select @change="getData" v-model:value="type" button-style="solid"  style="width: 180px;">
                  <a-select-option :value="'TUO_ZHAN_MO_SHI'">拓展模式统计</a-select-option>
                  <a-select-option :value="'YE_WU_BAN_KUAI'">业务板块统计</a-select-option>
                </a-select>
              </a-space>
            </template>
          </Title>
          <div class="dashboard_inner">
            <RkEcharts
                ref="refEchart4"
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
import { parseFormatNum,amountFormat,numFixed } from '@/utils/tools'

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
const refEchart4 = ref()
const type = ref('TUO_ZHAN_MO_SHI');
const chartData = ref([])
const option = ref({})
const getData = ()=>{
  loadding.value = true;
  api.analysis.getExpansionMode(props.level,props.deptId,props.dateVal,type.value).then(res => {
        if (res.code === 200 ){
          let obj = res.data
          let arr = []
          for (let key in obj) {
              console.log(key);
              obj[key]['name'] = obj[key].name
              obj[key]['value'] =obj[key].contractAmount
              arr.push(obj[key])
          }
        option.value ={
                tooltip: {
                  trigger: 'item',
                },
                tooltip: {
                  trigger: 'item',
                    formatter: '{b}:  {d}%'
                },
                legend: {
                  orient: 'horizontal',
                  bottom: '5%',
                  icon: 'circle',
                  formatter: (name) => {
                        let obj = "";
                       arr.forEach((item) => {
                          //格式化图例文本，支持字符串模板和回调函数两种形式。
                          if (item.name === name) {
                            obj = item
                            return;
                          }
                        });
                        return `${name}:  ￥${parseFormatNum(obj.value,2)}`;
                  },
                },
                color: ['#ffddab','#fbba71','#ff9223','#fb7e17'],
                series: [
                  {
                    type: 'pie',
                    radius: '60%',
                    center: ['50%', '40%'] ,
                    label:{
                      formatter: '{b}:   {d}%'
                    },
                    data:arr,
                    emphasis: {
                      itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                      }
                    }
                  }
                ]
        }
        refEchart4.value.updateChart()
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
