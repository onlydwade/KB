<template>
  <a-spin :spinning="loading">

    <div v-if="!isMobileDevice">
      <a-button style="margin-bottom: 20px;" size="large" type="primary" @click="getPdf" :loading="loading">
        导出pdf
      </a-button>
    </div>

    <div class="pdf-preview" id="pdfDom">
      <div class="container">
        <div class="header">
          <div class="date">
            <div>{{ report.createTime }}</div>
            <div>{{ report.deptName }}</div>
          </div>
          <!-- 数字大于1万 单位是万，数字大于亿 单位是亿 -->
          <div class="amount">


            <div class="item">
              <div>{{ report.contractCount }}</div>
              <div style="font-size: 0.8rem;">新签拓展合同（份）</div>
            </div>
            <div class="item">
              <div v-if="report.totalAmount > 10000">{{ (report.totalAmount / 10000).toFixed(2) }}亿</div>
              <div v-else-if="report.totalAmount > 0">{{ (report.totalAmount / 1).toFixed(2) }}万</div>
              <div v-else>{{ report.totalAmount }}元</div>
              <div style="font-size: 0.8rem;">新签合同总金额</div>
            </div>
            <div class="item">
              <div v-if="report.totalYearAmount > 10000">{{ (report.totalYearAmount / 10000).toFixed(2) }}亿</div>
              <div v-else-if="report.totalYearAmount > 0">{{ (report.totalYearAmount / 1).toFixed(2) }}万</div>
              <div v-else>{{ report.totalYearAmount }}元</div>
              <div style="font-size: 0.8rem;">新签合同年度金额</div>
            </div>
          </div>

          <div class="amount">
            <div class="item">
              <div>{{ report.renewalContractCount }}</div>
              <div style="font-size: 0.8rem;">续签拓展合同（份）</div>
            </div>
            <div class="item">
              <div v-if="report.renewalTotalAmount > 10000">{{ (report.renewalTotalAmount / 10000).toFixed(2) }}亿</div>
              <div v-else-if="report.renewalTotalAmount > 0">{{ (report.renewalTotalAmount / 1).toFixed(2) }}万</div>
              <div v-else>{{ report.renewalTotalAmount }}元</div>
              <div style="font-size: 0.8rem;">续签合同总金额</div>
            </div>
            <div class="item">
              <div v-if="report.renewalTotalYearAmount > 10000">{{ (report.renewalTotalYearAmount / 10000).toFixed(2)
                }}亿</div>
              <div v-else-if="report.renewalTotalYearAmount > 0">{{ (report.renewalTotalYearAmount / 1).toFixed(2) }}万
              </div>
              <div v-else>{{ report.renewalTotalYearAmount }}元</div>
              <div style="font-size: 0.8rem;">续签合同年度金额</div>
            </div>
          </div>
        </div>




        <div class="content" v-if="report?.winBidderList?.length > 0">
          <div class="title">
            <div class="icon">
              <div class="a1"></div>
              <div class="a2"></div>
            </div>
            近期项目中标情况
          </div>
          <div v-for="item in report.winBidderList" :key="item" class="item">
            <div class="title">
              <div style="white-space: normal;   overflow-wrap: break-word;  word-wrap: break-word;">{{ item.projectName
                }}</div>
            </div>
            <div class="company">归属单位：<span class="name">{{ item.deptName }}</span></div>
            <div class="data">
              <div>
                <div class="value amount">{{ item.bidedAmount ?? 0 }}</div>
                <div>中标金额（万）</div>
              </div>
              <div>
                <div class="value">{{ item.year ?? 0 }}</div>
                <div>服务年限（年）</div>
              </div>
            </div>
          </div>
        </div>
        <div class="content" v-if="report?.undertakingList?.length > 0">
          <div class="title">
            <div class="icon">
              <div class="a1"></div>
              <div class="a2"></div>
            </div>
            近期项目承接情况
          </div>
          <div v-for="item in report.undertakingList" :key="item" class="item">
            <div class="title">
              <div style="white-space: normal;   overflow-wrap: break-word;  word-wrap: break-word;">{{ item.projectName
                }}</div>
            </div>
            <div class="company">归属单位：<span class="name">{{ item.deptName }}</span></div>
            <div class="data">
              <div>
                <div class="value amount">{{ item.contractAmount ?? 0 }}</div>
                <div>合同金额（万）</div>
              </div>
              <div>
                <div class="value">{{ item.year ?? 0 }}</div>
                <div>服务年限（年）</div>
              </div>
              <div>
                <div class="value">{{ item.contractAnnualAmount ?? 0 }}</div>
                <div>合同年度金额（万）</div>
              </div>
            </div>
          </div>
        </div>
        <div class="content">
          <div class="title">
            <div class="icon">
              <div class="a1"></div>
              <div class="a2"></div>
            </div>
            重点项目跟进情况
          </div>
          <div v-for="item in report.customerFollowList" :key="item" class="item">
            <div class="title">
              <div style="white-space: normal;   overflow-wrap: break-word;  word-wrap: break-word;">{{
    item.customerName }}</div>
              <div :class="`t${item.projectType}`">{{ item.projectTypeName }}</div>
            </div>
            <div class="info">
              <div>
                <div class="head">任务摘要：</div>
                <div class="value">{{ item.summary }}</div>
              </div>
              <div>
                <div class="head">专班建立：</div>
                <div class="value">{{ item.teamsBuild }}</div>
              </div>
              <div>
                <div class="head">跟进状态：</div>
                <div class="value">{{ item.followStatus }}</div>
              </div>
            </div>
            <div class="responsible">
              <div class="head">负责人：</div>
              <div class="value">{{ item.visitUserName }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </a-spin>
</template>
<script setup>
import { computed } from 'vue';
import htmlToPdf from '@/utils/htmlToPdf';
import { isMobile } from "@/utils/tools";

const isMobileDevice = isMobile()
const loading = ref(false)

const props = defineProps({
  report: {}
})

const getPdf = () => {
  loading.value = true;
  nextTick(() => {

    // 调用htmlToPdf工具函数
    htmlToPdf.getPdf('工作简报', false, 'pdfDom');

    // 定时器模拟按钮loading动画的时间
    setTimeout(() => {
      loading.value = false;
    }, 1000);
  })
}

const theme = ref(props.report.theme)
const subTheme = computed(() => theme.value + '55')
const themeBg = computed(() => theme.value + '10')

</script>
<style lang="less" scoped>
.container {
  min-height: 100%;
  display: flex;
  flex-direction: column;
  background-color: v-bind(themeBg);
  overflow-y: auto;

  .header {
    padding: 1rem 1rem 0rem;
    background: linear-gradient(to bottom, v-bind(theme), #ffffff);

    .date {
      display: flex;
      justify-content: space-between;

      div {
        color: #ffffff;
        font-size: 1.2rem;
        font-weight: bold;
        text-shadow: 1px 1px 4px #00000055;
        margin-bottom: 0.5rem;
      }
    }

    .amount {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      background-color: #ffffff;
      padding: 1.5rem 0rem;
      border-radius: 0.5rem;

      .item {
        text-align: center;
        flex: 1;
        width: 33.33%;

        div {
          font-size: 1.5rem;

          &:first-child {
            color: v-bind(theme);
            font-weight: bold;
          }

          &:last-child {
            font-size: 0.9rem;
          }
        }
      }
    }
  }

  .content {
    padding: 0.5rem 1rem 0rem;

    &>.title {
      display: flex;
      align-items: center;
      font-size: 1rem;
      color: #000000;

      &>div {
        display: flex;
        margin-right: 0.5rem;

        &>div {
          height: 1rem;
          width: 1rem;

          &.a1 {
            background-color: v-bind(theme);
            border-radius: 100rem;
          }

          &.a2 {
            background-color: v-bind(subTheme);
            border-radius: 100rem;
            margin-left: -0.4rem;
          }
        }
      }
    }

    .item {
      background-color: #ffffff;
      margin: 0.5rem 0rem;
      border-radius: 0.5rem;
      box-shadow: 1px 1px 5px #00000055;

      .title {
        display: flex;
        justify-content: space-between;
        padding: 0.5rem 0rem 0.5rem 0.5rem;
        border-bottom: 1px solid #e9e9e9;
        text-shadow: 1px 1px 4px #00000055;
        font-size: 1rem;

        div {
          &:nth-child(1) {
            flex: 1;
            text-overflow: ellipsis;
            text-wrap: nowrap;
            overflow-x: hidden;
          }

          &:nth-child(2) {
            display: flex;
            align-items: center;
            font-size: 0.8rem;
            font-weight: bold;
            color: #ffffff;
            padding: 0rem 1rem 0rem 2rem;
            border-radius: 100rem 0rem 0rem 100rem;

            &.t1 {
              background: #9fac61;
            }

            &.t2 {
              background: #eb8a8a;
            }

            &.t3 {
              background: #005b8d;
            }

            &.t4 {
              background: #bfa5a4;
            }
          }
        }
      }

      .company {
        padding: 0.5rem;
        color: #808080;

        .name {
          color: #000000;
          text-shadow: 1px 1px 4px #00000055;
        }
      }

      .data {
        padding: 0.5rem 0rem 1rem;
        display: flex;

        &>div {
          flex: 1;
          text-align: center;

          .value {
            margin-bottom: 0.3rem;
            font-size: 1.3rem;
            font-weight: bold;
            text-shadow: 1px 1px 4px #00000055;
            white-space: normal;
            word-break: break-all;
            overflow: hidden;

            &.amount {
              color: v-bind(theme);
            }
          }
        }
      }

      .info {
        padding: 1rem;

        &>div {
          display: flex;
          flex-wrap: wrap;
          margin-top: 0.3rem;
        }
      }

      .responsible {
        display: flex;
        flex-wrap: wrap;
        padding: 0.5rem 1rem;
        border-top: 1px solid #e9e9e9;
      }

      .head {
        width: 5rem;
        color: #808080;
      }

      .value {
        flex: 1;
        color: #000000;
        text-shadow: 1px 1px 4px #00000055;
        white-space: normal;
        word-break: break-all;
        overflow: hidden;
      }
    }
  }
}
</style>