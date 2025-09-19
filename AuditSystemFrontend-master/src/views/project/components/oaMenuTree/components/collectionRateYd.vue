<template>
  <div class="card_box">
    <div class="title">立项收缴率指标</div>
    <div class="list_box">
      <a-row>
        <a-col :span="18">
          <div class="name">第一年</div>
        </a-col>
        <a-col :span="6">
          <div class="name"> {{ formData.collectionRateFirst}} %</div>
        </a-col>
      </a-row>
    </div>
    <div class="list_box">
      <a-row>
        <a-col :span="18">
          <div class="name">第二年</div>
        </a-col>
        <a-col :span="6">
          <div class="name"> {{formData.collectionRateSecond }} %</div>
        </a-col>
      </a-row>
    </div>
    <div class="list_box">
      <a-row>
       <a-col :span="18">
          <div class="name">第三年</div>
        </a-col>
        <a-col :span="6">
          <div class="name"> {{ formData.collectionRateThird }} %</div>
        </a-col>
      </a-row>
    </div>
  </div>
</template>
<script setup>
import api from "@/api/index";
import { amountUnit , parseFormatNum}  from '@/utils/tools';
import { mainStore } from '@/store';
const store = mainStore();
const props = defineProps({
  formData: {
    type: Object,
    default: {},
  },
});
</script>
<style lang="less" scoped>
.card_box {
  margin: 20px 0;
  padding: 10px;
}
.title {
  color: #000;
  font-weight: bold;
  line-height: 40px;
}
.list_box {
  // border: 1px solid #f0f2f5;
  background: #fffaf0;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 8px;
  .name {
    font-size: 15px;
  }
  .simple {
    line-height: 30px;
    color: #969799;
  }
  .total{
    color: #f99c34;
  }
}
</style>
