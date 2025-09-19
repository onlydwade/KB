<template>
  <div class="container">
    <div class="header">
      <div class="filter">
        <div class="role">
          <div @click="showRolePicker = true" v-if="store.deptPosts.length > 0">
            <i class="ri-user-3-fill" />
            <div class="name">{{ postName }}</div>
            <i class="ri-arrow-down-s-line" />
          </div>
          <div :class="{ disabled: notChange }"  @click="showActualInPicker = true">
            <i class="ri-stack-fill" />
            <div class="name">{{ actualIn.name }}</div>
            <i class="ri-arrow-down-s-line" />
          </div>
        </div>
        <div :class="{ disabled: notChange }" class="date" @click="showDatePicker = true"><i class="ri-calendar-2-line" /> {{ year }}年
          <i class="ri-arrow-down-s-line" />
        </div>
      </div>

      <div class="statistics " style="text-align: center;">
        <div class=" " style="width: 50%;">
          <div class="bg" style="display: flex;flex-wrap: wrap;padding-top: 10px;padding-bottom: 10px;margin: 5px;margin-right: 2.5px;">
            <div class="" style="width: 50%">
              <div >
                <div class="value" style="font-size: 0.8rem">{{ header.projectTotal }}</div>
                <div class="name" style="font-size: 0.8rem">在管项目总数</div>
              </div>
            </div>
            <div class="" style="width: 50%">
              <div>
                <div class="value">{{ header.waiProjectTotal }}<span class="unit">km²</span></div>
                <div class="name" >在管面积</div>
              </div>
            </div>
          </div>
        </div>
        <div class=" " style="width: 50%;">
          <div class="bg" style="display: flex;flex-wrap: wrap;padding-top: 10px;padding-bottom: 10px;margin: 5px;margin-left: 2.5px;">
            <div class="" style="width: 50%">
              <div >
                <div class="value" >{{ header.newWaiProjectTotal }}<span class="unit">km²</span></div>
                <div class="name" >当年新增面积</div>
              </div>
            </div>
            <div class="" style="width: 50%">
              <div>
                <div class="value">{{ header.xzzhsr }}<span class="unit">万</span></div>
                <div class="name" >当年转化收入</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class=" bg" style="margin: 5px;">
        <div class="statistics" style="padding-top: 10px;padding-bottom: 10px;">
          <div class="child row2-child ">
            <div class="value">{{ header.signProjectTotal }}</div>
            <div class="name">新签项目</div>
          </div>
          <div class="child row2-child ">
            <div class="value">{{ header.xzzje }}<span class="unit">万</span></div>
            <div class="name">新增总金额</div>
          </div>
          <div class="child row2-child ">
            <div class="value">{{ header.xzndzje }}<span class="unit">万</span></div>
            <div class="name">新增年度金额</div>
          </div>
        </div>
      </div>
      <div class=" bg" style="margin: 5px;">
        <div class="statistics" style="padding-top: 10px;padding-bottom: 10px;">
          <div class="child row2-child ">
            <div class="value">{{ header.signRenewalProjectTotal }}</div>
            <div class="name">续签项目</div>
          </div>
          <div class="child row2-child ">
            <div class="value">{{ header.xqzje }}<span class="unit">万</span></div>
            <div class="name">续签总金额</div>
          </div>
          <div class="child row2-child ">
            <div class="value">{{ header.xqndzje }}<span class="unit">万</span></div>
            <div class="name">续签年度金额</div>
          </div>
        </div>
      </div>
    </div>
    <div class="performance group">
      <div class="top">
        业绩达成情况
      </div>
      <div class="typeDiv">
        <div  v-for="(item,index) in radioList"  :class="{ actived: performanceType === item.key }" @click="updatePerformanceGauge(item.key)">{{item.name}}</div>
      </div>
      <div class="chart" ref="performanceRef"></div>
      <div class="data">
        <div>目标值：<span class="value">{{ mbz }}</span></div>
        <div>实际值：<span class="value">{{ sjz }}</span></div>
      </div>
    </div>
    <div class="expand group">
      <div class="top">
        占比统计
        <span>单位：万元</span>
      </div>
      <div class="typeRadio">
        <div :class="{ actived: zbType === 'TUO_ZHAN_MO_SHI' }" @click="loadExpandPie">拓展模式统计</div>
        <div :class="{ actived: zbType === 'YE_WU_BAN_KUAI' }" @click="loadExpandPie">业务板块统计</div>
      </div>
      <div class="middle">
        <div class="chart" ref="expandRef"></div>
        <div class="list">
          <div class="item" v-for="  item, index   in   tuozhan  " >
            <i class="point ri-checkbox-blank-circle-fill" :style="{ color: morphologyColor[index] }" />
            {{ item.name }}:<span class="count">{{ item.value }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="morphology group">
      <div class="top">
        项目业态占比
        <span>
          <div>单位：万元</div>
          <div v-if='isSubMorphology' class="back" @click="loadMorphologyPie">
            <van-icon name="share" />
          </div>
        </span>
      </div>
      <div class="typeRadio">
        <div :class="{ actived: zgType === 1 }" @click="changeZgType(1)">在管业态分析</div>
        <div :class="{ actived: zgType === 2 }" @click="changeZgType(2)">当年拓展业态分析</div>
      </div>
      <div class="middle">
        <div class="chart" ref="morphologyRef"></div>
        <div class="list">
          <div class="item" v-for="  item, index   in   morphology  " @click="toSubMorphology(item)">
            <i class="point ri-checkbox-blank-circle-fill" :style="{ color: morphologyColor[index] }" />
            {{ item.name }}:<span class="count">{{ item.value }}</span>
          </div>
        </div>
      </div>
    </div>


    <div class="distribution group">
      <div class="top">
        全国项目分布
        <div>
          <span class="item" :class="{ actived: areaType === 1 }" @click="changeAreaType(1)">按省</span>
          <span class="item" :class="{ actived: areaType === 2 }" @click="changeAreaType(2)">按市</span>
        </div>

      </div>
      <div class="typeRadio">
        <div :class="{ actived: mapType === 1 }" @click="changeMapType(1)">全部在管</div>
        <div :class="{ actived: mapType === 2 }" @click="changeMapType(2)">当年新增</div>
      </div>
      <div class="chart" ref="mapRef"></div>
      <template v-if="projectCountRank.length > 0">
        <div class="no1">
          <img class="icon" src="@/assets/images/no_1.png" />
          {{ projectCountRank[0]?.parentName }}
          {{ projectCountRank[0]?.name }}
          <span class="count">{{ projectCountRank[0]?.projectCount }}</span>
        </div>
        <div class="top5">
          <div v-for="item, index in projectCountRank" class="item">
            <div class="name">
              <div class="no">{{ index + 1 }}</div>
              <div class="province">
                {{ item.parentName }}
                {{ item.name }}
              </div>
              <div class="count">{{ item.projectCount }}</div>
            </div>
            <van-progress :show-pivot="false" color="linear-gradient(to right, #e5e5e5, #1879fa)"
                          :percentage="(item.projectCount / projectCountRank[0].projectCount) * 100" />
          </div>
        </div>
      </template>
    </div>
    <div class="tender group">
      <div class="top">
        投标情况
        <span>单位：万元</span>
      </div>
      <div class="typeSelect">
        <a-select @change="xswdTypeChange" v-model:value="xswdType" button-style="solid"  >
          <a-select-option :value="1">显示维度</a-select-option>
          <a-select-option :value="2">在管</a-select-option>
          <a-select-option :value="3">新拓</a-select-option>
        </a-select>
        <a-select @change="tzmsTypeChange" v-model:value="tzmsType" button-style="solid" >
          <a-select-option :value="1">拓展模式</a-select-option>
          <a-select-option :value="2">外部投标</a-select-option>
          <a-select-option :value="3">中石油投标</a-select-option>
        </a-select>
        <a-select @change="zblxTypeChange" v-model:value="zblxType" button-style="solid" >
          <a-select-option :value="1">招标类型</a-select-option>
          <a-select-option :value="2">公开招标</a-select-option>
          <a-select-option :value="3">邀请招标</a-select-option>
          <a-select-option :value="4">竞争性谈判</a-select-option>
          <a-select-option :value="5">单一来源</a-select-option>
          <a-select-option :value="6">询价</a-select-option>
        </a-select>
      </div>

      <div class="chart">
        <van-circle v-model:current-rate="tenderRate" :stroke-width="100" size="80%"
          :color="{ '0%': '#b2d4e7', '100%': '#74bae1', }" layer-color="#ededed" :text="`${completionRate}%`" />
      </div>
      <div class="data" >
        <div style="margin: 0 auto;">中标合同总金额：<span class="value">{{ (actualAmount/ 10000).toFixed(2) }}</span></div>
      </div>
    </div>
    <div class="funnel group">
      <div class="top">
        漏斗分析
      </div>
      <div class="chart" ref="funnelRef"></div>
    </div>
    <div class="agency group">
      <div class="top">
        实际签约情况
        <span>单位：万元</span>
      </div>
      <div class="chart" ref="agencyRef"></div>
    </div>
<!--    <div class="trend group">-->
<!--      <div class="top">-->
<!--        签约趋势及排名-->
<!--        <span>单位：万元</span>-->
<!--      </div>-->
<!--      <div class="chart" ref="trendRef"></div>-->
<!--      <div class="top5">-->
<!--        <div v-for="  item, index   in   cityRanking  " class="item">-->
<!--          <div class="name">-->
<!--            <div class="no">{{ index + 1 }}</div>-->
<!--            <div class="province">{{ item.deptName }}</div>-->
<!--            <div class="count">{{ (item.total / 10000).toFixed(2) }}</div>-->
<!--          </div>-->
<!--          <van-progress :show-pivot="false" color="linear-gradient(to right, #e5e5e5, #1879fa)"-->
<!--            :percentage="(item.total / cityRanking[0].total) * 100" />-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
  </div>
  <van-action-sheet v-model:show="showRolePicker" :close-on-click-overlay="false">
    <van-picker v-model="pickerRole" :columns="roles" @confirm="rolePickerConfirm" @cancel="rolePickerCancel" />
  </van-action-sheet>
  <van-action-sheet v-model:show="showDatePicker" :close-on-click-overlay="false">
    <van-date-picker v-model="pickerYear" title="选择年" :columns-type="['year']" @confirm="datePickerConfirm"
      @cancel="datePickerCancel" />
  </van-action-sheet>
  <van-action-sheet v-model:show="showActualInPicker" :close-on-click-overlay="false">
    <div class="handle">
      <div @click="actualInPickerCancel">取消</div>
      <div @click="actualInPickerConfirm">确定</div>
    </div>
    <div class="actualIn">
      <div class="parent">
        <div v-for="item in actualInList" @click="actualInPicker(item, 0)"
          :class="{ selected: item.id === pickerActualIn[0]?.id }">
          {{ item.name }}
        </div>
      </div>
      <div class="children" v-if="pickerActualIn[0]?.children?.length > 0">
        <div v-for="item in pickerActualIn[0].children" :class="{ selected: item.id === pickerActualIn[1]?.id }"
          @click="actualInPicker(item, 1)">
          {{ item.name }}</div>
      </div>
    </div>
  </van-action-sheet>
</template>
<script setup>
import { nextTick } from 'vue';
import * as echarts from 'echarts'
import mapJson from '@/assets/json/mapData.json'
import api from '@/api/index';
import { mainStore } from '@/store/index';
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const store = mainStore();

const mapRef = ref()
const morphologyRef = ref()
const expandRef = ref()
const performanceRef = ref()
const funnelRef = ref()
const agencyRef = ref()
const trendRef = ref()

const mapChart = ref()
const morphologyPieChart = ref()
const expandPieChart = ref()
const performanceGaugeChart = ref()
const funnelGaugeChart = ref()
const agencyGaugeChart = ref()
const trendGaugeChart = ref()

const postName = computed(() => {
  return `${store.deptPost.parentName}-${store.deptPost.deptName}-${store.deptPost.postName}`
})
const roles = computed(() => {
  return store.deptPosts.map(n => {
    return {
      text: `${n.parentName}-${n.deptName}-${n.postName}`,
      value: n.postId,
      data: n
    }
  })
})
const showRolePicker = ref(false)
const pickerRole = ref([store.deptPost.postId])

const showActualInPicker = ref(false)
const actualInList = ref([])
const actualIn = ref({})
const pickerActualIn = ref([])

const showDatePicker = ref(false)
const year = ref(new Date().getFullYear())
const pickerYear = ref([`${new Date().getFullYear()}`])

const areaType = ref(1)

const mapType = ref(1)

const header = ref({
  projectTotal: 0,
  waiProjectTotal: 0,
  newWaiProjectTotal: 0,
  signProjectTotal: 0,
  signRenewalProjectTotal: 0,
  xzzhsr: "0.00",
  xzzje: "0.00",
  xqzje: "0.00",
  xzndzje: "0.00",
  xqndzje: "0.00"
})

const projectCountRank = ref([])
const morphology = ref([])
const morphologyColor = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de']
const isSubMorphology = ref(false)

const performanceData = ref({})

const tenderType = ref('all')
const tenderRate = ref(0)
const tenderData = ref({})

const route = useRoute()
const routeYear = ref(route.query.year)
const routeDeptId = ref(route.query.deptId)
const routeTodoId = ref(route.query.todoId)
let notChange = false

const cityRanking = ref([])

const tuozhan = ref([])

const rolePickerConfirm = (item) => {
  const data = item.selectedOptions[0].data
  showRolePicker.value = false
  api.common.switchAuth({ deptId: data.deptId, postId: data.postId })
    .then((res) => {
      if (res.code == 200) {
        store.setToken(res.data.access_token)
        store.changeRole(data);
        loadData()
      }
    })
}

const rolePickerCancel = () => {
  showRolePicker.value = false
}

const datePickerConfirm = () => {
  showDatePicker.value = false
  year.value = pickerYear.value[0]
  loadData()
}

const datePickerCancel = () => {
  showDatePicker.value = false
  pickerYear.value[0] = year.value
}

const actualInPicker = (item, level) => {
  if (level === 0 && item.parentId === 0)
    pickerActualIn.value = [item]
  if (level === 1 && pickerActualIn.value[level]?.id === item.id)
    delete pickerActualIn.value[level]
  else
    pickerActualIn.value[level] = item
}

const actualInPickerCancel = () => {
  showActualInPicker.value = false
  if (actualIn.value.parentId === 0) {
    pickerActualIn.value = [actualIn.value]
  } else {
    const parent = actualInList.value.find(n => n.id === actualIn.value.parentId)
    pickerActualIn.value = [parent, actualIn.value]
  }
}

const actualInPickerConfirm = () => {
  showActualInPicker.value = false
  actualIn.value = pickerActualIn.value[1] || pickerActualIn.value[0]
  loadData()
}
//地图
const createMap = () => {
  mapChart.value = echarts.init(mapRef.value);
  echarts.registerMap("map", mapJson);
  let option = {
    tooltip: {
      trigger: 'item',
      showDelay: 0,
      transitionDuration: 0.2
    },
    series: [
      {
        name: '项目数:',
        type: 'map',
        map: 'map',
        label: {
          show: false
        },
        zoom: 1.2,
        roam: true,
        scaleLimit: {
          min: 0.5,
          max: 6,
        },
        data: []
      }
    ],
    visualMap: {
      show: true,
      x: 'left',
      y: 'bottom',
      pieces: [
        {
          lt: 5,
          label: "0-5个",
          color: "#BAE7FF"
        }, {
          gt: 5,
          lt: 10,
          label: "5-10个",
          color: "#69C0FF"
        }, {
          gt: 10,
          lt: 15,
          label: "10-15个",
          color: "#1890FF"
        }, {
          gt: 15,
          label: "15个以上",
          color: "#004599"
        }
      ],
      color: ['#004599', '#1890FF', '#69C0FF', '#BAE7FF']
    }
  }
  mapChart.value.setOption(option);

  loadMap()
}

//业态
const createMorphologyPie = () => {
  morphologyPieChart.value = echarts.init(morphologyRef.value);
  let option = {
    color: morphologyColor,
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: ['35%', '50%'],
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          //formatter: '{price|￥{c}}\n{rate|{b}\n{d}%}',
          formatter: '{d}%',
          offset: [0, -10],
          lineHeight: 15,
          rich: {
              price: {
                  fontWeight: 'bold'
              },
              time: {
                  fontSize: 8,
                  color: '#999'
              }
          }
        },
        labelLine: {
          length: 2,
          length2: 0
        },
        labelLayout: function (params) {
          const isLeft = params.labelRect.x < expandPieChart.value.getWidth() / 2;
          const points = params.labelLinePoints;
          // Update the end point.
          points[2][0] = isLeft
              ? params.labelRect.x
              : params.labelRect.x + params.labelRect.width;
          return {
              labelLinePoints: points
          };
        },
        data: []
      }
    ],
    graphic: [
      {
        type: 'group',
        left: 'center',
        top: 'middle',
        children: [
          {
            type: 'text',
            left: 'center',
            top: 'middle',
            style: {
              text: '合同总金额\n\n\n\n',
              fontSize: '0.6rem',
              fill: '#8b8b8b',
              textAlign: 'center'
            }
          },
          {
            type: 'text',
            left: 'center',
            top: 'middle',
            style: {
              text: '0',
              fontSize: '0.8rem',
              textAlign: 'center'
            }
          }
        ]
      }
    ]
  }
  morphologyPieChart.value.setOption(option);
  loadMorphologyPie()
}
//拓展
const createExpandPie = () => {
  expandPieChart.value = echarts.init(expandRef.value);
  let option = {
    color: ['#2a90fa', '#3ac0c1', '#48c062', '#f8cd3c'],
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: '45%',
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          //formatter: '{price|￥{c}}\n{rate|{b}\n{d}%}',
          formatter: '{d}%',
          offset: [0, -10],
          lineHeight: 15,
          rich: {
            price: {
              fontWeight: 'bold'
            },
            time: {
              fontSize: 8,
              color: '#999'
            }
          }
        },
        labelLine: {
          length: 2,
          length2: 0
        },
        labelLayout: function (params) {
          const isLeft = params.labelRect.x < expandPieChart.value.getWidth() / 2;
          const points = params.labelLinePoints;
          // Update the end point.
          points[2][0] = isLeft
            ? params.labelRect.x
            : params.labelRect.x + params.labelRect.width;
          return {
            labelLinePoints: points
          };
        },
        data: [
          { value: 0, name: '外部项目' },
          { value: 0, name: '中石油项目' }
        ]
      }
    ]
  }
  expandPieChart.value.setOption(option);
  loadExpandPie()
}
//业绩
const createPerformanceGauge = () => {
  performanceGaugeChart.value = echarts.init(performanceRef.value);
  let option = {
    series: [
      {
        name: 'Pressure',
        type: 'gauge',
        min: 0,
        max: 100,
        splitNumber: 4,
        radius: '100%', // 仪表盘半径，可以是百分比或像素值
        center: ['50%', '60%'],
        progress: {
          show: true,
          width: 15,
          itemStyle: {
            color: '#dce5ec'
          }
        },
        axisLine: {
          lineStyle: {
            width: 15,
            color: [
              [1, '#ebebeb']
            ]
          }
        },
        detail: {
          valueAnimation: true,
          formatter: '{value}%',
          fontSize: 20,
        },
        pointer: {
          icon: 'path://M2.9,0.7L2.9,0.7c1.4,0,2.6,1.2,2.6,2.6v115c0,1.4-1.2,2.6-2.6,2.6l0,0c-1.4,0-2.6-1.2-2.6-2.6V3.3C0.3,1.9,1.4,0.7,2.9,0.7z',
          width: 6,
          length: '60%',
          itemStyle: {
            color: '#2a90fa'
          }
        },
        anchor: {
          show: true,
          showAbove: true,
          size: 20,
          itemStyle: {
            borderWidth: 6,
            borderColor: '#2a90fa'
          }
        },
        axisTick: {
          show: false,
          distance: -30,
          lineStyle: {
            color: [[1, '#ebebeb']]
          }
        },
        splitLine: {
          show: false,
          distance: -30,
          lineStyle: {
            color: '#ebebeb'
          }
        },
        axisLabel: {
          show: true, // 是否轴线显示标签。
          distance: 40,
          fontSize: 15,
          fontWeight: 'bold',
          color: '#a0bacf',
          formatter: function (value) {
            if (value >= 80) {
              return '优';
            } else if (value >= 60) {
              return '良';
            } else if (value >= 40) {
              return '中';
            } else if (value >= 20) {
              return '差';
            }
            return '0';
          }
        },
        data: [
          {
            value: 0,
            name: '完成率',
            title: {
              offsetCenter: ['0%', '40%']
            },
            detail: {
              offsetCenter: ['0%', '60%']
            }
          }
        ]
      }
    ]
  };

  performanceGaugeChart.value.setOption(option);
  loadPerformanceGauge()
}
//投标
const createTenderGauge = () => {
  loadTenderGauge()
}
//漏斗
const createFunnelGauge = () => {
  funnelGaugeChart.value = echarts.init(funnelRef.value);
  let option = {
    color: ['#266abe', '#3ba1ff','#22c7ff'],
    series: [
      {
        name: 'Funnel',
        type: 'funnel',
        top: '10%',
        left: '10%',
        width: '80%',
        height: '80%',
        minSize: '50%',
        maxSize: '100%',
        sort: 'descending',
        label: {
          show: true,
          position: 'inside',
          formatter: '{b}:{c}\n\n{d}%',
          fontSize: '15px',
          fontWeight: 400,
          color: '#ffffff'
        },
        data: [
          { value: 0, name: '项目总量' },
          { value: 0, name: '成功项目' }
        ]
      }
    ]
  };

  funnelGaugeChart.value.setOption(option);
  loadFunnelGauge()
}
//签约
const createAgencyGauge = () => {
  agencyGaugeChart.value = echarts.init(agencyRef.value);
  let option = {
    color: ['#a334cc', '#cf7980', '#e8922f'],
    grid: {
      top: 20,
      left: 15,
      right: 15,
      bottom: 50,
    },
    legend: {
      bottom: '1%',
      left: 'center',
    },
    tooltip: {
      trigger: 'item'
    },
    xAxis: {
      type: 'category',
      data: [],
      axisLine: {
        show: false // 隐藏 x 轴的线
      },
      axisTick: {
        show: false, // 隐藏 x 轴的刻度点
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        show: false, // 隐藏 y 轴的刻度值
      },
      splitLine: {
        lineStyle: {
          type: 'dashed', // 设置网格线为虚线
        },
      },
    },
    series: [
      {
        name: '合同总金额',
        data: [],
        type: 'line',
        symbolSize: 10,
        smooth: true,
      },
      {
        name: '合同年度金额',
        data: [],
        type: 'line',
        symbolSize: 10,
        smooth: true
      },
      {
        name: '当年转化收入',
        data: [],
        type: 'line',
        symbolSize: 10,
        smooth: true
      }
    ]
  }

  agencyGaugeChart.value.setOption(option);
  loadAgencyGauge()
}
//趋势
const createTrendGauge = () => {
  trendGaugeChart.value = echarts.init(trendRef.value);
  let option = {
    grid: {
      top: 20,
      left: 15,
      right: 15,
      bottom: 30,
    },
    tooltip: {
      show: true,
      trigger: 'axis',
      triggerOn: 'click',
      formatter: '{c}'
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: [],
      axisLine: {
        show: false // 隐藏 x 轴的线
      },
      axisTick: {
        show: false, // 隐藏 x 轴的刻度点
      }
    },
    yAxis: {
      show: false,
      type: 'value'
    },
    series: [
      {
        data: [],
        symbolSize: 10,
        type: 'line',
        color: ['#277deb'],
        areaStyle: {
          color: {
            type: 'linear',
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: '#6da8f0' }, // 起始颜色
              { offset: 1, color: '#ffffff' } // 结束颜色
            ]
          }
        }
      }
    ]
  }

  trendGaugeChart.value.setOption(option);
  loadTrendGauge()
}

const loadHeader = () => {
  api.analysis.getProjectSituation(actualIn.value.level, actualIn.value.id, year.value).then(res => {
    if (res.code === 200) {

      header.value = {
        projectTotal: (res.data.projectTotal),
        waiProjectTotal: (res.data.waiProjectTotal / 1000).toFixed(2),
        newWaiProjectTotal: (res.data.newWaiProjectTotal / 1000).toFixed(2),
        signProjectTotal: (res.data.signProjectTotal),
        signRenewalProjectTotal: (res.data.signRenewalProjectTotal),
        xzzhsr: (res.data.xzzhsr/ 10000).toFixed(2),
        xzzje: (res.data.xzzje/ 10000).toFixed(2),
        xqzje: (res.data.xqzje/ 10000).toFixed(2),
        xzndzje: (res.data.xzndzje/ 10000).toFixed(2),
        xqndzje: (res.data.xqndzje/ 10000).toFixed(2)
      }
    }
  })
}
const loadActualIn = async () => {
  await api.performance.actualInTreeAll().then(res => {
    if (res.code === 200) {
      const children = [...res.data.children]
      res.data.children = []
      const data = [
        res.data,
        ...children
      ]
      actualInList.value = data

      actualIn.value = data[0]

      if(isNotEmpty(routeDeptId)){
          notChange = true
        actualIn.value = findObjectById(routeDeptId.value,data)
      }

      if(isNotEmpty(routeYear)){
          notChange = true
        year.value = routeYear.value
      }

      pickerActualIn.value = [actualIn.value]

    }
  })
}
const changeAreaType = (type) => {
  areaType.value = type
  loadMap()
}
const changeMapType = (type) => {
  mapType.value = type
  loadMap()
}
//地图
const loadMap = () => {
  api.analysis.projectCity(actualIn.value.level, actualIn.value.id, year.value, mapType.value,(areaType.value == 1 ? 0 : 360000)).then(res => {
    const arrSort = (res.data || []).sort((a, b) => {
      return b.projectCount - a.projectCount;
    });
    const data = arrSort.map(item => {
      return {
        value: item.projectCount,
        adcode: item.areaCode,
        name: item.areaName,
        parentName: item.parentName || '',
        projectCount: item.projectCount
      }
    })

    projectCountRank.value = data.slice(0, 5);

    if (areaType.value == 1) {
      mapChart.value.setOption({
        series: [{
          data
        }]
      })
    }
  })
}

const zgType = ref(1)

const changeZgType = (type) => {
  zgType.value = type
  loadMorphologyPie()
}
//业态
const loadMorphologyPie = () => {
  api.analysis.getProjectYETAI(actualIn.value.level, actualIn.value.id, year.value, 'XIANG_MU_YE_TAI',zgType.value).then(res => {
    if (res.code === 200) {
      isSubMorphology.value = false
      morphology.value = res.data.projectYETAI.map(item => {
        return {
          name: item.label,
          value: (item.contractAmount / 10000).toFixed(2),
          percentage: item.percentage,
          key: item.value
        }
      })

      morphologyPieChart.value.setOption({
        series: [{
          data: morphology.value
        }],
        graphic: [
          {
            children: [
              {},
              {
                style: {
                  text: `${(res.data.contractAmountSum / 10000).toFixed(2)}`,
                }
              }
            ]
          }
        ]
      })
    }
  })
}

const toSubMorphology = (item) => {
  if (isSubMorphology)
    return

  api.analysis.getProjectYETAI(actualIn.value.level, actualIn.value.id, year.value, item.key).then(res => {
    if (res.code === 200) {
      isSubMorphology.value = true
      morphology.value = res.data.projectYETAI.map(item => {
        return {
          name: item.label,
          value: (item.contractAmount / 10000).toFixed(2),
          percentage: item.percentage,
          key: item.value
        }
      })

      morphologyPieChart.value.setOption({
        series: [{
          data: morphology.value
        }],
        graphic: [
          {
            children: [
              {},
              {
                style: {
                  text: `\n\n${(res.data.contractAmountSum / 10000).toFixed(2)}`,
                }
              }
            ]
          }
        ]
      })
    }
  })
}

const zbType = ref('TUO_ZHAN_MO_SHI')

//拓展
const loadExpandPie = () => {
  api.analysis.getExpansionMode(actualIn.value.level, actualIn.value.id, year.value,zbType.value).then(res => {
    if (res.code === 200) {
      let obj = res.data

      const data = []

      for (let key in obj) {
        console.log(key);
        if(obj[key].mode==='WAI_BU_CHENG_BIAO'){
          obj[key]['name']='外部承接'
        }
        if(obj[key].mode==='WAI_BU_TOU_BIAO'){
          obj[key]['name']='外部投标'
        }
        if(obj[key].mode==='ZHONG_SHI_YOU_CHENG_BIAO'){
          obj[key]['name']='中石油承接'
        }
        if(obj[key].mode==='ZHONG_SHI_YOU_TOU_BIAO'){
          obj[key]['name']='中石油投标'
        }
        obj[key]['value'] =(obj[key].contractAmount / 10000).toFixed(2)

        data.push(obj[key])
      }

      tuozhan.value = data

      expandPieChart.value.setOption({
        series: [
          {
            data
          }
        ]
      });
    }
  })
}

let radioList = [];
const radioStrList = ['HTZJE','HTNDJE','ZDTZHTJE','YXXXTBL','XMBLL'];
const mbz = ref(0) //目标值
const sjz = ref(0) // 实际值
const performanceType = ref('HTZJE')
let returnData = {data:{}}

//业绩
const loadPerformanceGauge = () => {
  api.analysis.getAchievements(actualIn.value.level, actualIn.value.id, year.value).then(res => {
    if (res.code === 200) {
      returnData.data = res.data

      radioList = []
      returnData.data.forEach((mp, i) => {
        if (radioStrList.includes(mp.key)) {
          radioList.push(mp)
        }
      })

      updatePerformanceGauge(performanceType.value)
    }
  })
}

const updatePerformanceGauge = (key) => {

  performanceType.value = key

  let data = returnData.data

  data.forEach((mp, i) => {

    if(mp.key == performanceType.value ) {
      mbz.value = mp.mb
      sjz.value  = mp.sj
      const rate = mbz.value === 0 ? 100 : parseFloat((sjz.value  / mbz.value * 100).toFixed(2))

      performanceGaugeChart.value.setOption({
        series: [
          {
            data: [
              {
                value: rate > 100 ? 100 : rate,
                name: '完成率',
                title: {
                  offsetCenter: ['0%', '40%']
                },
                detail: {
                  offsetCenter: ['0%', '60%']
                }
              }
            ]
          }
        ]
      })
    }
  })

}

const xswdType       = ref(1);
const tzmsType       = ref(1);
const zblxType       = ref(1);

const xswdTypeChange = () => {
  loadTenderGauge()
}
const tzmsTypeChange = () => {
  loadTenderGauge()
}
const zblxTypeChange = () => {
  loadTenderGauge()
}

//投标
const loadTenderGauge = () => {
  api.analysis.getBidding(actualIn.value.level, actualIn.value.id, year.value,xswdType.value,tzmsType.value,zblxType.value).then(res => {
    if (res.code === 200) {
      resData.data = res.data
      updateTender()
    }
  })
}
const resData = reactive({
  data: {}
})
const actualAmount = ref(0)
const zhongbiao = ref(0)
const completionRate =  ref(0)  // 完成率
const total = ref(0)

const updateTender = () => {
  let data = resData.data
  actualAmount.value = data.contractAmount
  total.value = data.total
  zhongbiao.value = data.zhongbiao
  if(data.total !== 0){
    completionRate.value = parseFloat((data.zhongbiao / data.total * 100).toFixed(2))
  }else {
    completionRate.value = 0
  }

}

//漏斗
const loadFunnelGauge = () => {
  api.analysis.getFunnelAnalysis(actualIn.value.level, actualIn.value.id, year.value).then(res => {
    if (res.code === 200) {

      let total = res.data.total

      let obj = res.data.data

      const data = [
        {
          name: '项目信息总量',
          value: obj.xmxxzl
        },
        {
          name: '项目跟进总量',
          value: obj.xmgjzl
        },
        {
          name: '成功项目总量',
          value: obj.cgxmzl
        }
      ]

      let option = {
        series: [{
          label: {
            formatter: (params) => {
              let rate = 0;
              if (params.data.value  !== 0 && total !== 0) {
                rate =  (params.data.value / total * 100).toFixed(2)
              }
              return `${params.data.name}:${params.data.value}\n\n${rate}%`
            }
          },
          data
        }]
      };

      funnelGaugeChart.value.setOption(option);
    }
  })
}

//签约
const loadAgencyGauge = () => {
  api.analysis.getActualSigning(actualIn.value.level, actualIn.value.id, year.value).then(res => {
    if (res.code === 200) {
      const date = res.data.map(n => n.date)
      const contractAmount = res.data.map(n => (n.contractAmount / 10000).toFixed(2))
      const contractAnnualAmount = res.data.map(n => (n.contractAnnualAmount / 10000).toFixed(2))
      const annualConversionAmount = res.data.map(n => (n.annualConversionAmount / 10000).toFixed(2))

      agencyGaugeChart.value.setOption({
        xAxis: {
          data: date,
        },
        series: [
          {
            data: contractAmount,
          },
          {
            data: contractAnnualAmount,
          },
          {
            data: annualConversionAmount,
          }
        ]
      })
    }
  })
}

//趋势
const loadTrendGauge = () => {
  api.analysis.getSigning(actualIn.value.level, actualIn.value.id, year.value).then(res => {
    if (res.code === 200) {
      cityRanking.value = res.data.cityRanking

      const date = res.data.salesVolume.map(n => n.month)
      const data = res.data.salesVolume.map(n => (n.value / 10000).toFixed(2))

      trendGaugeChart.value.setOption({
        xAxis: {
          data: date,
        },
        series: [
          {
            data
          }
        ]
      })
    }
  })
}

const findObjectById = (id, list)  =>{
  for (let i = 0; i < list.length; i++) {
    if (list[i].id == id) {
      // 如果找到，返回该对象
      return list[i];
    }
    if (list[i].children && list[i].children.length > 0) {
      // 递归查找子对象
      const found = findObjectById(id, list[i].children);
      if (found) {
        // 如果在子对象中找到，返回该对象
        return found;
      }
    }
  }
  // 如果没有找到，返回null或undefined
  return null;
};

// 一个计算属性或方法来检查 值 是否为空或空字符串
const isNotEmpty = (data) => {
    return data.value !== null && data.value !== '' && data.value !== undefined;
};

const onload = async () => {
  await nextTick()
  await loadActualIn()
  loadHeader()
  createMap()
  createMorphologyPie()
  createExpandPie()
  createPerformanceGauge()
  createTenderGauge()
  createFunnelGauge()
  createAgencyGauge()
  createTrendGauge()
}

const loadData = () => {
  loadHeader()
  loadMap()
  loadMorphologyPie()
  loadExpandPie()
  loadPerformanceGauge()
  loadTenderGauge()
  loadFunnelGauge()
  loadAgencyGauge()
  loadTrendGauge()
}


onload()

</script>
<style scoped lang="less">
.container {
  background-color: #f3f9ff;

  .header {
    background-image: url('@/assets/images/statistics_bg.png');
    background-position: 20% 80%; /* 图片居中 */
    background-repeat: no-repeat;
    background-size: 150% 100%;
    color: #ffffff;
    display: flex;
    flex-direction: column;
    padding-bottom: 10px;

    .disabled {
      pointer-events: none;
    }

      .filter {
      display: flex;
      justify-content: space-between;
      flex-wrap: wrap;
      margin: 1rem 0.5rem 0rem;
      font-size: 1rem;

      &>div {

        &.role {
          flex: 1;
          overflow-x: hidden;

          &>div {
            display: flex;
            margin-bottom: 0.5rem;

            .name {
              text-wrap: nowrap;
              text-overflow: ellipsis;
              overflow-x: hidden;
            }


          }
        }

        &.date {
          text-align: right;
          width: 7rem;
          flex-shrink: 0;
        }
      }
    }

    .bg{
      background-color: rgba(255, 255, 255, 0.3); /* 白色透明背景 */
      color: white; /* 文字颜色为白色 */
      border-radius: 10px; /* 圆角 */
    }

    .statistics {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between; /* 在每一行中均匀分布元素 */
      width: 100%;

      .child {
        text-align: center;
      }


      .row1-child {
        flex-basis: calc(50% - 15px); /* 计算每个子元素宽度，减去间隙 */
      }


      .row2-child {
        flex-basis: calc(33.33%); /* 计算每个子元素宽度，减去间隙 */
      }

      /* 为了确保第二行和第三行在第一行下方开始 */
      .container :nth-child(4n + 5) {
        margin-top: 20px;
      }


        .value {
          font-weight: bold;
          font-size: 0.8rem;

          .unit {
            font-size: 0.6rem;
          }
        }

        .name {
          margin-top: 0.1rem;
          font-size: 0.8rem;
        }

    }
  }

  .group {
    margin: 0.5rem 0rem;
    background-color: #ffffff;
    padding: 1rem 0rem;

    .top {
      font-size: 1rem;
      font-weight: bold;
      color: #000000;
      display: flex;
      justify-content: space-between;
      align-items: top;
      padding: 0rem 0.5rem;

      span {
        font-weight: 400;

        .back {
          text-align: right;

          .van-icon {
            margin-top: 0.5rem;
            transform: scaleX(-1);
            color: #3ba1ff;
            font-size: 1.5rem;
          }
        }
      }
    }

    .typeRadio {
      display: flex;
      justify-content: center;
      width: 90%;
      margin: 1rem auto 0rem;
      overflow: hidden;

      div {
        padding: 0.3rem 0rem;
        width: 33.33%;
        text-align: center;
        border: 0.1rem solid #3ba1ff;

        &:first-child {
          border-right: none;
          border-top-left-radius: 10rem;
          border-bottom-left-radius: 10rem;
        }

        &:last-child {
          border-left: none;
          border-top-right-radius: 10rem;
          border-bottom-right-radius: 10rem;
        }

        &.actived {
          color: #ffffff;
          background-color: #3ba1ff;
        }
      }
    }

    .typeDiv {
      display: flex;
      justify-content: center;
      width: 90%;
      margin: 1rem auto 0rem;
      overflow: hidden;

      div {
        padding: 0.3rem 0rem;
        width: 33.33%;
        text-align: center;
        border: 0.1rem solid #3ba1ff;

        &.actived {
          color: #ffffff;
          background-color: #3ba1ff;
        }
      }
    }

    .typeSelect {
      display: flex;
      justify-content: center;
      width: 90%;
      margin: 1rem auto 0rem;
      overflow: hidden;

      div{
        padding: 0.3rem 0rem;
        width: 33.33%;
        text-align: center;
      }

      .ant-select-selector{
        border: 0.1rem solid #3ba1ff;
      }

    }

    .data {
      display: flex;
      justify-content: space-between;
      text-align: center;

      div {
        width: 50%;

        .value {
          font-size: 1rem;
          font-weight: bold;
        }
      }
    }


    .top5 {
      .item {
        padding: 0rem 1rem 0.5rem;

        .name {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin: 0.5rem 0rem 0rem;

          .no {
            width: 2rem;
            height: 1.5rem;
            line-height: 1.5rem;
            text-align: center;
            font-weight: bold;
            color: #777777;
            background-color: #ededed;
          }

          .province {
            flex: 1;
            margin-left: 2rem;
          }

          .count {
            color: #1779fa;
            font-size: 1.3rem;
            font-weight: bold;
          }
        }

        &:nth-child(-n+3) {
          .name {
            .no {
              color: #ffffff;
              background-color: #faa654;
            }
          }
        }
      }
    }

    &.distribution {

      .top {
        .item {
          font-weight: bold;
          margin: 0rem 0.3rem;

          &.actived {
            border-bottom: 0.2rem solid #1779fa;
          }
        }
      }

      .chart {
        height: 80vw;
      }

      .no1 {
        border: 0.4rem solid #daecfe;
        border-radius: 100rem;
        padding: 0.1rem 0.5rem;
        display: flex;
        justify-content: center;
        align-items: center;
        width: 75%;
        margin: 1rem auto;
        font-size: 1rem;
        font-weight: bold;

        .icon {
          width: 2rem;
          margin-right: 0.5rem;
        }

        .count {
          font-size: 2rem;
          margin-left: 0.5rem;
          color: #1779fa;
        }
      }
    }

    &.morphology {

      .middle {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .chart {
          flex: 1;
          height: 50vw;
        }

        .list {
          width: 37vw;
          color: #8c8c8c;

          .item {
            display: flex;
            align-items: center;
            margin-left: -1rem;

            .point {
              font-size: 0.5rem;
              margin-right: 0.5rem;
            }

            .count {
              font-weight: bold;
              color: #000000;
            }
          }
        }
      }
    }

    &.expand {

      .middle {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .chart {
          flex: 1;
          height: 50vw;
        }

        .list {
          width: 37vw;
          color: #8c8c8c;

          .item {
            display: flex;
            align-items: center;
            margin-left: -1rem;

            .point {
              font-size: 0.5rem;
              margin-right: 0.5rem;
            }

            .count {
              font-weight: bold;
              color: #000000;
            }
          }
        }
      }
    }

    &.performance {
      .chart {
        height: 60vw;
      }
    }

    &.tender {
      .chart {
        height: 60vw;
        display: flex;
        justify-content: center;
        align-items: center;

        :deep(.van-circle) {
          .van-circle__text {
            font-size: 2rem;
            font-weight: unset;

            &::before {
              display: block;
              font-size: 1.3rem;
              color: #8c8c8c;
              content: "投标成功率";
              white-space: pre-wrap;
              margin-bottom: 1rem;
            }
          }
        }
      }
    }

    &.funnel {
      .chart {
        height: 50vw;
      }
    }

    &.agency {
      .chart {
        height: 60vw;
      }
    }

    &.trend {
      .chart {
        height: 50vw;
        border-bottom: 0.1rem solid #d7d8da;
        margin: 0rem 0.5rem;
      }
    }
  }
}

.handle {
  display: flex;
  justify-content: space-between;
  font-size: 18px;
  color: #4498f9;
  padding: 10px 15px;
}

.actualIn {
  display: flex;
  height: 350px;

  &>div {
    font-weight: bold;
    width: 100%;
    background-color: #e6ebf5;


    &.children {
      overflow-y: auto;
      background-color: #ffffff;
    }

    div {
      padding: 10px 15px;

      &.selected {
        background-color: #ffffff;
        color: #4498f9;
        border-left: 5px solid #4498f9;
      }
    }
  }
}

</style>
