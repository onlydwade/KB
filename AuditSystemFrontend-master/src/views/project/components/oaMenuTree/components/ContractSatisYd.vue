<template>
  <div class="card_box">
    <div class="title">{{ title }}</div>
    <div v-if="list?.length" class="list_box" v-for="(item, idx) in list" :key="idx">
      <a-row>
        <a-col :span="12">
          <div class="name">{{ item.subject }}</div>
        </a-col>
        <a-col :span="12">
          <div class="name"> ￥{{ parseFormatNum(item.cost, 2) }}</div>
        </a-col>
        <a-col :span="12">
          <div class="name"> {{ item.remark }}</div>
        </a-col>
      </a-row>
    </div>
    <!-- <div class="list_box">
      <a-row>
        <a-col :span="12">
          <div class="total">合计</div>
        </a-col>
        <a-col :span="12">
          <div class="total"> ￥{{ parseFormatNum(total, 2) }}</div>
        </a-col>
      </a-row>
    </div> -->
  </div>
</template>
<script setup>
// import api from "@/api/index";
// import { getNodeById } from '@/utils/tools';
import { parseFormatNum } from '@/utils/tools';
// import { mainStore } from '@/store';
// const store = mainStore();
const props = defineProps({
  projectId: {
    type: Number,
    default: 0,
  },
  title: {
    type: String,
    default: ''
  },
  list: {
    type: Array,
    default: []
  }
});
// watch(() => props.list, (val) => {
//   console.log(val)
// });
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

  .total {
    color: #f99c34;
  }
}
</style>
