
<template>
  <div class="menu_inner">
    <div class="card_box">
      <van-cell-group title="交接信息填写">
        <van-cell title="投后负责人" :value="(formData.principal || {}).realname || '-'" />
        <van-cell title="交接日期" :value="dateFormat(formData.handoverDate, 'YYYY-MM-DD') || '-'" />
        <van-cell title="业务所属部门" :value="formData.businessDeptName || '-'" />
      </van-cell-group>
    </div>
    <div class="card_box" v-if="list.length">
      <van-cell-group title="风险检查点">
        <van-cell-group v-for="(item, idx) in list" :key="idx">
          <van-cell title="节点名称" :value="item.node  || '-' " />
          <van-cell title="里程碑" :value="dateFormat(item.milepostTime, 'YYYY-MM-DD') || '-'" />
          <van-cell title="提醒时间" :value="dateFormat(item.reminderTime, 'YYYY-MM-DD') || '-'" />
          <van-cell title="提醒内容" :value="item.remark || '-'" />
        </van-cell-group>
      </van-cell-group>
    </div>
    <FileList :menuId="menuId" :projectId="projectId"
      :stepLink="{ 1031: { stepMenuId: 23, id: 1025 }, 1032: { stepMenuId: 23, id: 1026 } }" />
  </div>
</template>
<script setup>
import { useMenuTree } from './menu';
import { useDictStore } from '@/store/dict';
import { parseFormatNum, dateFormat } from '@/utils/tools';
import {useCorrelation} from "@/views/project/components/correlation/correlation";
const dict = useDictStore();
const props = defineProps({
  projectId: {
    type: Number,
    default: 0,
  },
  menuId: {
    type: Number,
    default: 0,
  },
  menuInfo: {
    type: Object,
    default: {},
  }
})

const formAttrs = ['id', 'ddMode', 'ddTeamType', 'ddAmount'];
const {
  formRef,
  documentsRef,
  formData,
  getInfo,
  submit,
  save,
  disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs, false);

const {
  list,
  getList
} = useCorrelation(props.projectId, 'projectRiskInspection');

onMounted(() => {
  getInfo();
  getList();
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

  .card_box {
    margin-bottom: 20px;
  }

  .bm {
    margin: 10px;
  }
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
