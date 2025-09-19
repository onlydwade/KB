<template>
  <div class="card_box" v-if="list.length">
    <div class="title">经营指标信息</div>
    <div class="list_box" v-for="(item, idx) in list" :key="idx">
      <a-row>
        <a-col :span="12">
          <div class="name">{{ item.chargeType}}</div>
        </a-col>
        <a-col :span="12">
          <div class="name"> ￥{{ parseFormatNum(item.amount,2) }}</div>
        </a-col>
        <a-col :span="20" >
          <div class="simple">{{ parseFormatNum(item.chargePrice,2) }}元/㎡ X {{ parseFormatNum(item.quantity,2) }}㎡</div>
        </a-col>
      </a-row>
    </div>
    <div class="list_box">
     <a-row>
      <a-col :span="12">
          <div class="total">合计</div>
        </a-col>
        <a-col :span="12">
          <div class="total"> ￥{{ parseFormatNum(total,2) }}</div>
        </a-col>
     </a-row>
    </div>
  </div>
</template>
<script setup>
import api from "@/api/index";
import { getNodeById }  from '@/utils/tools';
import { amountUnit , parseFormatNum}  from '@/utils/tools';
import { mainStore } from '@/store';
const store = mainStore();
const props = defineProps({
  projectId: {
    type: Number,
    default: 0,
  },
});
const loadding = ref(false);
const list = ref([]);
const total = ref(0)
const getList =  () => {
  loadding.value = true;
  api.project.correlationList(props.projectId,'projectManagementIndicators').then(res => {
    if (res.code == 200) {
        list.value = res.data || [];
        list.value.forEach(item=>{
          total.value +=item.amount 
        })
    }
    loadding.value = false;
  });
};
watch(
  () => props.menuId,
  (newValue, oldValue) => {
    getList();
  }
);
onMounted(() => {
  getList();
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
