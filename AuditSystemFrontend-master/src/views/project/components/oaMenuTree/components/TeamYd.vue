<template>
  <div class="card_box" v-if="list.length">
    <div class="title">尽调人员确认</div>
    <div class="list_box" v-for="(item, idx) in list" :key="idx">
      <a-row>
        <a-col :span="18">
          <div class="name">{{ item.user.realname }} | {{ item.deptName }}</div>
        </a-col>
        <a-col :span="18" >
          <div class="simple">{{ item.roleTypeStr }} -{{ item.roleKeyStr }}</div>
        </a-col>
      </a-row>
    </div>
  </div>
</template>
<script setup>
import api from "@/api/index";
import { message } from "ant-design-vue";
const props = defineProps({
  projectId: {
    type: Number,
    default: 0,
  },
});
const loadding = ref(false);
const list = ref([]);
const getList =  () => {
  loadding.value = true;
  api.project.correlationList(props.projectId,'projectTeam').then(res => {
    if (res.code == 200) {
        list.value = res.data || [];
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
}
</style>
