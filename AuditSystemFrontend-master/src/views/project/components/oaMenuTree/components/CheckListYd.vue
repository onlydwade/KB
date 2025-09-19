<template>
  <a-popover title="相似项目" v-if="checkList">
    <template #content>
      <a-list style="width: 90vw;" item-layout="horizontal" :data-source="checkList">
        <template #renderItem="{ item }">
          <a-list-item>{{ '【' + item.projectNo + '】,【' + item.projectName +'】,【' + item.companyName+'】,【' + item.createUser.realname + '】'}}</a-list-item>
        </template>
      </a-list>
    </template>
    <img  style="width: 26px;height:26px;align-items: center;" class="gth" src="/images/icon-gth.png" alt="" />
  </a-popover>
</template>
<script setup>
import api from "@/api/index";
import { getNodeById } from '@/utils/tools';
import { mainStore } from '@/store';
import {onMounted, onUpdated} from "vue";
const store = mainStore();
const props = defineProps({
  data: {
    type: Object,
    default: () => ({}),
    required: true
  }
});
const loadding = ref(false);
const checkList = ref([]);

const getCheckList = () => {
  loadding.value = true;
  // 延迟2秒后执行操作
  setTimeout(() => {
    api.project.projectDuplicateCheck(
        props.data
    ).then(res=>{
      if(res.code==200){
        checkList.value = res.data
      }
      loadding.value = false;
    })
  }, 2000);
}

onMounted(() => {
  getCheckList();
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
}</style>
