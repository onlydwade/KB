<template>
  <a-table :columns="columns" :data-source="list" :pagination="false" :scroll="{ y: '100%' }">
    <template #emptyText>暂无数据，请添加</template>
    <template #bodyCell="{ column, text, record, index }">
      <template v-if="column.editType == 'input'">
        <a-input
          v-if="editData[index]"
          v-model:value="editData[index][column.dataIndex]"
          :maxlength="50"
        />
        <template v-else>
          {{ text }}
        </template>
      </template>
      <template v-if="column.key === 'action'">
        <template v-if="!editData[index]">
          <a-button type="text" class="color-del" :disabled="record.readonly"   v-permission="['biz:performance:config:del']" size="small" @click="del(record, index)">
            删除
          </a-button>
          <a-button type="text" class="color-del"   v-permission="['biz:performance:config:del']" size="small" @click="edit(record, index)">
            修改
          </a-button>
          <a-button type="text" class="color-primary"  size="small"  v-permission="['biz:performance:level']" @click="setPresentationLevel(record, index)">
            设置指标展示层级
          </a-button>
        </template>
        <a-space v-else>
          <a-button type="primary" shape="circle" @click="editSubmit(record, index)">
            <template #icon><check-outlined /></template>
          </a-button>
          <a-button shape="circle" @click="editCancel(record, index)">
            <template #icon><close-outlined /></template>
          </a-button>
        </a-space>
      </template>
    </template>
    <template #summary v-if="!readyOnly && addVisible">
      <a-table-summary>
        <a-table-summary-row>
          <template v-for="(item, index) in columns" :key="index">
            <a-table-summary-cell v-if="item.key != 'action'">
              <a-input
                v-if="item.editType == 'input'"
                v-model:value="editData['add'][item.dataIndex]"
              />
            </a-table-summary-cell>
          </template>
          <a-table-summary-cell :index="7">
            <a-space>
              <a-button type="primary" shape="circle" @click="addSubmit">
                <template #icon><check-outlined /></template>
              </a-button>
              <a-button shape="circle" @click="addCancel">
                <template #icon><close-outlined /></template>
              </a-button>
            </a-space>
          </a-table-summary-cell>
        </a-table-summary-row>
      </a-table-summary>
    </template>
  </a-table>
  <div class="add_btn" @click="add" v-if="!readyOnly && !addVisible">
    <plus-circle-outlined style="margin-right: 8px" />
    新增
  </div>
  <PresentationLevelDrawer ref="PresentationLevelDrawerRef" />
</template>
<script setup>
import api from "@/api/index";
import { message, Modal } from "ant-design-vue";
import PresentationLevelDrawer      from './PresentationLevelDrawer.vue';
const props = defineProps({
  modelValue: {
    type: Array,
    default: [],
  },
  competitorId: {
    //有值实时编辑修改，无值则为上级组件一次提交
    type: Number,
    default: 0,
  },
  readyOnly: {
    type: Boolean,
    default: false,
  },
  list: {
    type: Array,
    default: [],
  },
});

const emits = defineEmits(["submitData"]);

const columns = [
  {
    title: "指标类型",
    dataIndex: "fieldName",
    width: 150,
    editType: "input",
  },
  {
    title: "单位",
    dataIndex: "fieldUnitName",
    width: 150,
    editType: "input",
  },
];

const addVisible = ref(false);
const add = () => {
  addVisible.value = true;
};
const addSubmit = async () => {
  if (!editData["add"].fieldName) {
    message.error("请输入指标类型");
    return;
  }
  if (!editData["add"].fieldUnitName) {
    message.error("请输入指单位");
    return;
  }

  const sameNameDatas = props.list.filter(l=>{
    return l.fieldName == editData["add"].fieldName
  });

  if(sameNameDatas.length > 0){
    message.error("已经有相同的指标类型，请删除后再加!");
    return;
  }

  editData["add"].fieldKey = "CUSTOM" + Date.now();
  editData["add"].sorts = props.list.length + 1;
  editData["add"].readonly = false;
  editData["add"].required = false;
  editData["add"].desc = "";
  editData["add"].isNew = true;

  props.list.push(editData["add"]);
  editData["add"] = {};
  addVisible.value = false;
  emits("submitData", props.list);
};
const addCancel = () => {
  editData["add"] = {};
  addVisible.value = false;
  emits("submitData", props.list);
};
const editData = reactive({
  add: {},
});
const PresentationLevelDrawerRef =ref(null)
const setPresentationLevel = (row, index) => {
  PresentationLevelDrawerRef.value.open(row)
};
const editSubmit = async (row, index) => {
  if (!editData[index].fieldName) {
    message.error("请输入指标类型");
    return;
  }
  if (!editData[index].fieldUnitName) {
    message.error("请输入单位");
    return;
  }

  // if (row.id) {
  //   let res = await api.competitor.competitorContactEdit(editData[index]);
  //   if (res.code == 200) {
  //     message.success("操作成功");
  //   } else {
  //     return;
  //   }
  // }
  Object.assign(props.list[index], editData[index]);
  delete editData[index];
  emits("submitData", props.list);
};
const editCancel = (row, index) => {
  delete editData[index];
  emits("submitData", props.list);
};
//编辑
const edit = (row, index) => {
  editData[index] = JSON.parse(JSON.stringify(row));
}

const del = (row, index) => {
  if (!row.isNew) {
    Modal.confirm({
      title: "操作确认",
      content: '是否确认删指标类型《"' + row.fieldName + '"》？',
      onOk() {
        api.performance.ifCanDelIndicator(row.fieldKey).then(res => {
          if (res.code == 200 && !res.data) {
            props.list.splice(index, 1);
          }
          else {
            message.warning('已有用户填报了业绩数据,无法删除！');
          }
        });
      },
    });
  } else {
    props.list.splice(index, 1);
  }
};
onMounted(() => {
  if (!props.readyOnly) {
    columns.push({
      title: "操作",
      key: "action",
      width: 180,
    });
  }
});
</script>
<style scoped lang="less">
.add_btn {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  cursor: pointer;
  margin-top: 8px;
  height: 48px;
  // box-shadow      : 0 0 8px rgb(0 21 41 / 8%);
  border: 1px solid #eee;
  border-radius: 4px;

  &:hover {
    color: @primary-color;
    background-color: #fffaf0;
  }
}
.color-del{
  color: @primary-color;
  &[disabled] {
    color:  #989797;
  }
}
</style>
