
<template>
  <div class="menu_inner">
      <FileList  :menuId="menuId" :projectId="projectId" />
  </div>
</template>
<script setup>
import { useMenuTree }  from './menu';
import { useDictStore } from '@/store/dict';
import TeamYd from "./components/TeamYd.vue";
const dict  = useDictStore();
const props = defineProps({
  projectId: {
      type    : Number,
      default : 0,
  },
  menuId:{
      type    : Number,
      default : 0,
  },
  menuInfo : {
      type    : Object,
      default : {},
  }
})

const formAttrs = ['id','ddMode','ddTeamType','ddAmount'];
const {
  formRef,
  documentsRef,
  formData,
  getInfo,
  submit,
  save,
  disabled
} = useMenuTree(props.projectId,toRefs(props).menuInfo,formAttrs,false);
onMounted(() => {
  getInfo();
})
</script>
<style scoped lang="less">
.menu_inner {
  :v-deep .van-cell__value {
    color: #000;
  }
  :v-deep .van-cell-group__title {
    color: #000;
    font-weight: bold;
  }
  .card_box{
    margin-bottom: 20px;
  }
  .bm{
    margin: 10px;
  }
}
</style>
