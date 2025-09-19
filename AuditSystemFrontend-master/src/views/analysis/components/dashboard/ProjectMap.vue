<template>
  <div class="dashboard_box">
      <a-spin :spinning="loadding">
          <!-- <Title :title="province.code?`${province.name}项目城市分布`:'全国项目城市分布'" :subTitle="province.code?'点击右侧返回按钮按省份查看':'点击省份查看省明细'"> -->
          <Title :title="province.code?`${province.name}项目城市分布`:'全国项目城市分布'">
              <template #left >
                  <a-space>
                      <a-select @change="zgTypeChange" v-model:value="zgType" button-style="solid"  style="margin-left: 30px;width: 100px;">
                          <a-select-option :value="1">全部在管</a-select-option>
                          <a-select-option :value="2">当年新增</a-select-option>
                      </a-select>
                  </a-space>
              </template>
              <template #right>
                  <a-space>
                      <a-radio-group
                      v-model:value="areaType"
                      @change="areaTypeChange"
                      button-style="solid">
                          <a-radio-button :value="1">按省查看</a-radio-button>
                          <a-radio-button :value="2">按市查看</a-radio-button>
                      </a-radio-group>
                      <span class="color-link" v-if="province.code" @click="toChina">
                          返回上级
                          <rollback-outlined style="fontSize:20px"/>
                      </span>
                  </a-space>
              </template>
          </Title>
          <div class="dashboard_inner">
              <div class="map_box">
                  <div class="map_container" ref="mapRef"></div>
              </div>
              <div class="rank_box">
                  <h5 class="title">
                      项目分布排名
                  </h5>
                  <ScrollBox>
                      <div class="scroll-main">
                          <div class="rank_item" v-for="(item,index) in rankList" :key="index">
                              <span class="sort" :class="{'sort_active':index<3}">{{index+1}}</span>
                              <span class="name">
                                  <EllipsisTooltip  :content="(item.parentName+item.name)"/>
                              </span>
                              <span class="num">{{item.value}}个</span>
                          </div>
                      </div>
                  </ScrollBox>
              </div>
          </div>
      </a-spin>
  </div>
</template>
<script setup>
import axios        from 'axios';
import api          from '@/api/index';
import {throttle}   from '@/utils/tools';
import { message }    from 'ant-design-vue';
import * as echarts from 'echarts'
import mapJson from '@/assets/json/mapData.json'
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

const areaType       = ref(1);
const zgType       = ref(1);
const areaTypeChange = ()=>{
  getData(true);
}
const zgTypeChange = ()=>{
    getData(true);
}

const province = reactive({
  code : '',
  name : ''
})

const rankList = ref([]);
const getData  = (unRefresh)=>{
  loadding.value = true;
  api.analysis.projectCity(props.level,props.deptId,props.dateVal,zgType.value,(areaType.value==1?0:(province.code || 360000))).then(res=>{
      if(res.code==200){
          let arrSort = (res.data || []).sort((a,b)=>{
              return b.projectCount - a.projectCount;
          });
          rankList.value = arrSort.map(item=>{
              return {
                  value      : item.projectCount,
                  adcode     : item.areaCode,
                  name       : item.areaName,
                  parentName : item.parentName || ''
              }
          });
          if(!unRefresh){
              createdMap();
          }
      }
      loadding.value = false;
  })
}
const toChina = ()=>{
  province.code  = '';
  province.name  = '';
  areaType.value = 1;
  getData();
}

watch([()=>props.dateType,()=>props.dateVal,()=>props.level,()=>props.deptId], (val) => {
  if(props.dateType&&props.dateVal&&props.level&&props.deptId){
      getData();
  }
},{immediate:true})

const mapRef     = ref(null);
const myChart    = ref(null);
const createdMap = async ()=>{
  if(myChart.value){
      myChart.value.clear();
  }
  myChart.value = echarts.init(mapRef.value);
  /* let apiPath   = 'https://geo.datav.aliyun.com/areas_v3/bound/'+toAreaCode(province.code)+'_full.json';
  let mapJson   = await axios.get(apiPath); */
  echarts.registerMap("map",mapJson);
  let option = {
      tooltip: {
          trigger            : 'item',
          showDelay          : 0,
          transitionDuration : 0.2
      },
      series : [
          {
              name     : '项目数:',
              type     : 'map',
              map      : 'map',
              label    : {
                  show: false
              },
              zoom       : 1.2,
              roam       : true,
              scaleLimit : {
                min : 0.5,
                max : 6,
              },
              data : rankList.value
          }
      ],
      visualMap: {
          show   : true,
          x      : 'left',
          y      : 'bottom',
          pieces : [
              {
                  lt    : 5,
                  label : "0-5个",
                  color : "#ffd768"
              }, {
                  gt    : 5,
                  lt    : 10,
                  label : "5-10个",
                  color : "#ffb95e"
              }, {
                  gt    : 10,
                  lt    : 15,
                  label : "10-15个",
                  color : "#f99c34"
              }, {
                  gt    : 15,
                  label : "15个以上",
                  color : "#d47b22"
              }
          ],
          color: ['#5475f5', '#9feaa5', '#85daef','#74e2ca', '#e6ac53', '#9fb5ea']
      }
  }
  myChart.value.setOption(option);
  myChart.value.off("click");
  myChart.value.on("click", (params) => {
      message.warning('暂不支持按查看具体省份分布明细！！！');
      return;

      if(province.code){
          return;
      }
      if(params.dataIndex >= rankList.value.length){
          message.warning('该省份暂无项目分布！');
          return;
      }
      let select     = rankList.value[params.dataIndex];
      province.code  = select.adcode;
      province.name  = select.name;
      areaType.value = 2;
      getData();
  });
}
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


//数据处理
const codeMap = {
  '7013135772653':'810000',
  '7112407077174':'820000',
  '7212684281636':'710000',
}
const toAreaCode = (code)=>{
  if(!code){
      return 100000;
  }
  if(code%1000000==0){
      return code/1000000;
  }
  return codeMap[code];
}
/* const mapJson = ()=>{
  return {}
} */
</script>
<style scoped lang="less">
.dashboard_inner{
  display : flex;
  height  : 100%;
  .map_box{
      width        : 0;
      flex         : 1.3;
      margin-right : 16px;
  }
  .map_container{
      width  : 100%;
      height : 100%;
  }
  .rank_box{
      width            : 0;
      flex             : 1;
      background-color : #fffaf0;
      border-radius    : 8px;
      display          : flex;
      flex-direction   : column;
      .title{
          font-size : 16px;
          padding   : 12px;
      }
  }
}
.scroll-main{
  padding: 0 10px;
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
      background-color : @primary-color;
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
