<template>
  <div class="dashboard_box">
      <a-spin :spinning="loadding">
          <Title title="投标情况">
              <template #left >
                  <a-space>
                      <span style="margin-left: 10px;">显示维度</span>
                      <a-select @change="zgTypeChange" v-model:value="zgType" button-style="solid"  style="width: 80px;">
                          <a-select-option :value="1">全部</a-select-option>
                          <a-select-option :value="2">在管</a-select-option>
                          <a-select-option :value="3">新拓</a-select-option>
                      </a-select>
                      <span style="margin-left: 10px;">拓展模式</span>
                      <a-select @change="tbTypeChange" v-model:value="tbType" button-style="solid"  style="width: 120px;">
                          <a-select-option :value="1">全部</a-select-option>
                          <a-select-option :value="2">外部投标</a-select-option>
                          <a-select-option :value="3">中石油投标</a-select-option>
                      </a-select>
                      <span style="margin-left: 10px;">招标类型</span>
                      <a-select @change="zbTypeChange" v-model:value="zbType" button-style="solid"  style="width: 120px;">
                          <a-select-option :value="1">全部</a-select-option>
                          <a-select-option :value="2">公开招标</a-select-option>
                          <a-select-option :value="3">邀请招标</a-select-option>
                          <a-select-option :value="4">竞争性谈判</a-select-option>
                          <a-select-option :value="5">单一来源</a-select-option>
                          <a-select-option :value="6">询价</a-select-option>
                      </a-select>
                  </a-space>
              </template>
<!--              <template #right>-->
<!--                  <a-space style="font-size: 12px;">-->
<!--                      <a-radio-group-->
<!--                          v-model:value="performanceType"-->
<!--                          @change="updateChartData(performanceType)"-->
<!--                          button-style="solid"-->
<!--                          size="small"-->
<!--                      >-->
<!--                          <a-radio-button value="wai">外部投标</a-radio-button>-->
<!--                          <a-radio-button value="zhong">中石油投标</a-radio-button>-->
<!--                          <a-radio-button value="all">全部</a-radio-button>-->
<!--                      </a-radio-group>-->
<!--                  </a-space>-->
<!--              </template>-->
          </Title>
          <div class="dashboard_inner">
            <a-row justify="center">
              <a-col :span="18" class="title">
                投标成功率
              </a-col>
              <a-col :span="18" class="percentage">
                {{completionRate}}%
              </a-col>
            </a-row>
            <a-row justify="center" >
              <a-col :span="14">
                <a-progress :percent="completionRate" status="active" strokeWidth="25" strokeColor="#ff8a00" :showInfo="false" style="line-height: 30px;"/>
              </a-col>
              <a-col :span="6" class="num"><span>{{ zhongbiao }}/{{ total }}</span></a-col>
            </a-row>
            <a-row justify="center" style="margin-top: 30px;">
              <a-col :span="18"  align="center" class="tp">中标合同总金额：￥{{  parseFormatNum(actualAmount,2)}}</a-col>
            </a-row>
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
const refEchart5 = ref()
const zgType       = ref(1);
const tbType       = ref(1);
const zbType       = ref(1);
const zbTypeChange =()=>{
    getData()
}
const zgTypeChange =()=>{
    getData()
}
const tbTypeChange =()=>{
    getData()
}
const loadding = ref(true);
const resData = reactive({
  data: {}
})

const chartData = reactive({
  budgetaryIncome: { fieldValue: 0 },
  actualIncome: { value: 0 },
  completeRate: 0
})
const option  = ref({

})

const performanceType = ref('all')
const total = ref(0)
const bugetAmount = ref(0)
const actualAmount = ref(0)
const zhongbiao = ref(0)
const completionRate =  ref(0)  // 完成率
const updateChartData = () => {
  let data = resData.data
    actualAmount.value = data.contractAmount
    total.value = data.total
    zhongbiao.value = data.zhongbiao
    if(data.total !== 0){
        completionRate.value = numFixed((data.zhongbiao  / data.total ) * 100,2)
    }else {
        completionRate.value = 0
    }
        // for (let key in data) {
        //   console.log(data[key].buget,performanceType.value);
        //   if(key==performanceType.value){
        //     actual.value = data[key].actual
        //     bugetAmount.value  = data[key].bugetAmount
        //     actualAmount.value  = data[key].actualAmount
        //     buget.value  = data[key].buget
        //     if(actual.value&& buget.value){
        //       completionRate.value  =numFixed((actual.value  / buget.value ) * 100,2)
        //     }else{
        //       completionRate.value  = 0
        //     }
        //   }
        // }

}
const getData = ()=>{
  loadding.value = true;
    api.analysis.getBidding(props.level,props.deptId,props.dateVal,zgType.value,tbType.value,zbType.value).then(res => {
      loadding.value = false
        if (res.code === 200 ){
            resData.data = res.data
            updateChartData()
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
.dashboard_inner{
  margin-top: 30px;
  .title{
    font-size: 16px;
    color: #adadad;
    padding-left: 10px;
  }
  .percentage{
    font-size: 20px;
    color: #ff8a00;
    padding-left: 10px;
    font-weight: bold;
  }
  .num{
    font-size: 20px;
    color: #ff8a00;
    padding-left: 20px;
  }
  .tp{
    font-size: 18px;
  }
}
</style>
