<template>
  <div class="echarts" :style="`height:${height};width:${width};`" />
</template>

<script>
import * as echarts from 'echarts'
export default {
  props: {
    option: {
      type: Object,
      default: () => {}
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '100%'
    },
    clickEvent: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      reOption: null,
      myChart: null
    }
  },
  watch: {
    option(newVal, oldVal) {
      if (this.myChart) {
        if (newVal) {
          this.reOption = newVal
          // this.resetToolbar()
          toRaw(this.myChart).setOption(this.reOption, true)
          window.addEventListener('resize', this.myChart.resize)
        } else {
          this.reOption = oldVal
          // this.resetToolbar()
          toRaw(this.myChart).setOption(this.reOption, true)
          window.addEventListener('resize', this.myChart.resize)
        }
      } else {
        this.drawEcharts()
      }
      this.myChart.resize()
    }
  },
  created() {
    this.reOption = this.option
  },
  mounted() {
    this.drawEcharts()
    window.addEventListener('resize', this.myChart.resize)
  },
  beforeDestroy() {
    if (this.myChart.dispose) {
      this.myChart.dispose()
    }
    window.removeEventListener('resize', this.myChart.resize)
  },
  methods: {
    drawEcharts() {
      const el = this.$el
      this.myChart = echarts.init(el, '')
      // this.resetToolbar()
      toRaw(this.myChart).setOption(this.reOption)
      this.myChart.on('click', (params) => {
        if (!this.clickEvent) {
          return
        }
        this.clickEvent(params)
      })
    },
    resize() {
      this.myChart.resize()
    },
    setOption() {
      // this.resetToolbar()
      toRaw(this.myChart).setOption(this.reOption)
    },
    GetLength(str) {
      // /<summary>获得字符串实际长度，中文2，英文1</summary>
      // /<param name="str">要获得长度的字符串</param>
      let realLength = 0
      const len = str.length
      let charCode = -1
      for (let i = 0; i < len; i++) {
        charCode = str.charCodeAt(i)
        if (charCode >= 0 && charCode <= 128) realLength += 1
        else realLength += 2
      }
      return realLength
    },
    updateChart() {
      // 暴露更新图表方法，可能需要优化
      if (this.myChart) {
        this.reOption = this.option
        // this.resetToolbar()
        toRaw(this.myChart).setOption(this.reOption, true)
        window.addEventListener('resize', this.myChart.resize)
      } else {
        this.drawEcharts()
      }
      this.myChart.resize()
    }
    // breakwrapStr(str, len) {
    //   const strLen = this.GetLength(str)
    //   const strCut = ''

    //   // for (let i = 0; i < strLen; i++) {
    //   //   const a = str.charAt(i)
    //   // }
    //   if (strLen < len) { return str }
    // },
    // resetToolbar() {
    //   // toolbar 重置为UI设计的要求
    //   this.reOption.tooltip.formatter = function(params) {
    //     let result = ''
    //     params.forEach(function(item) {
    //       let bgcolor = ''
    //       if (typeof (item.color) === 'string') {
    //         bgcolor = item.color
    //       } else {
    //         bgcolor = '#1054DB'
    //       }
    //       // 长字符换行，待优化
    //       // const max = 16
    //       // let breawrapStr = ''
    //       // if (this.GetLength(item.axisValueLabel) > max) {
    //       //   breawrapStr += item.axisValueLabel.substring(0, max)
    //       // }

    //       if (item.componentIndex === 0) {
    //         result += '<div class="tooltip-title" >' + item.axisValueLabel + '</div>'
    //       }
    //       const dotHtml =
    //               '<span class="tooltip-dot" style="background-color:' + bgcolor + '"></span>'
    //       result += '<div class="tool-item"><span>' + dotHtml + '<span class="tooltip-name">' + item.seriesName + '</span></span> <span class="tooltip-value">' + item.data + ' </span></div>'
    //     })
    //     return result
    //   }
    // }
  }
}
</script>
