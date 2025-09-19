<template>
  <a-form ref="formRef" layout="vertical" :model="editData">
      <a-table :columns="columns" :data-source="list" :pagination="false" :scroll="{ x: '100%' }">
          <template #headerCell="{ column }">
              <template v-if="column.required">
                  {{ column.title }}
                  <span class="color-danger">*</span>
              </template>
          </template>

          <template #emptyText>
              暂无数据，请添加
          </template>
          <template #bodyCell="{ column, text, record, index }">
              <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.editType] : ''"
                  v-if="column.editType == 'expandCompanyId'">
                  <DeptSelect :noRoot="true" v-if="editData[index]" v-model="editData[index][column.editType]" />
                  <DeptSelect :noRoot="true" v-else disabled v-model="record[column.editType]" />
                  <!-- <template v-else>
                      {{ text }}
                  </template> -->
              </a-form-item>

              <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.dataIndex] : ''"
                  v-if="column.editType == 'number'">
                  <a-input-number v-if="editData[index]" v-model:value="editData[index][column.dataIndex]" :min="0"
                      :max="100" class="w_full" placeholder="请输入" />
                  <template v-else>
                      {{ text }} %
                  </template>
              </a-form-item>
              <a-form-item label="" :required="column.required" :name="editData[index] ? [index, column.dataIndex] : ''"
                  v-if="column.editType == 'text'">
                  <a-input v-if="editData[index]" allowClear v-model:value="editData[index][column.dataIndex]"
                      placeholder="请输入" />
                  <template v-else>
                      {{ text }}
                  </template>
              </a-form-item>
              <template v-if="column.key === 'action'">
                  <template v-if="!editData[index]">
                      <a-button type="text" class="color-primary" v-if="!readOnly" size="small"
                          @click="edit(record, index)">编辑</a-button>
                      <a-button type="text" class="color-primary" v-if="!readOnly" size="small"
                          @click="del(record, index)">删除</a-button>
                      <a-button type="text" class="color-primary" v-if="!readOnly" size="small"
                          @click="clone(record, index)">克隆</a-button>
                  </template>
                  <a-space v-else>
                      <a-button type="primary" shape="circle" @click="editSubmit(record, index)" size="small">
                          <template #icon><check-outlined /></template>
                      </a-button>
                      <a-button shape="circle" @click="editCancel(record, index)" size="small">
                          <template #icon><close-outlined /></template>
                      </a-button>
                  </a-space>
              </template>
          </template>
          <template #summary v-if="!readOnly && addVisible">
              <a-table-summary>
                  <a-table-summary-row>
                      <template v-for="(item, index) in columns" :key="index">
                          <a-table-summary-cell v-if="item.key != 'action'">
                              <a-form-item :required="item.required" label=""
                                  :name="['add', item.editType == 'expandCompanyId' ? item.editType : item.dataIndex]">
                                  <DeptSelect :noRoot="true" v-if="item.editType == 'expandCompanyId'"
                                      v-model="editData['add'][item.editType]" />
                                  <a-input-number v-if="item.editType == 'number'" addon-after="%"
                                      v-model:value="editData['add'][item.dataIndex]" :min="0" :max="100" class="w_full"
                                      placeholder="请输入" />
                                  <a-input v-if="item.editType == 'text'" allowClear
                                      v-model:value="editData['add'][item.dataIndex]" placeholder="请输入" />
                              </a-form-item>
                          </a-table-summary-cell>
                      </template>
                      <a-table-summary-cell :index="2">
                          <a-space>
                              <a-button type="primary" shape="circle" @click="addSubmit" size="small">
                                  <template #icon><check-outlined /></template>
                              </a-button>
                              <a-button shape="circle" @click="addCancel" size="small">
                                  <template #icon><close-outlined /></template>
                              </a-button>
                          </a-space>
                      </a-table-summary-cell>
                  </a-table-summary-row>
              </a-table-summary>
          </template>
      </a-table>
  </a-form>
  <div class="add_btn" @click="addVisible = true" v-if="!readOnly && !addVisible">
      <plus-circle-outlined style="margin-right:8px;" />
      新增
  </div>
</template>
<script setup>
// import api from '@/api/index';
import { useCorrelation } from './EnterCorrelation';
// import { useDictStore } from '@/store/dict';
import { watchArray } from '@vueuse/core'
// const dict = useDictStore();
const props = defineProps({
  modelValue: {
      type: String,
      default: null,
  },
  dataList: {
      type: Array,
      default: [],
  },
  projectId: Number,
  readOnly: {
      type: Boolean,
      default: false,
  },
})
const emit = defineEmits(['update:modelValue','update:dataList'])
const {
  formRef,
  list,
  getList,
  addVisible,
  addSubmit,
  addCancel,
  editData,
  edit,
  editSubmit,
  editCancel,
  del,
  clone
} = useCorrelation(props.projectId, 'projectAchievement');
watchArray(list, (newList) => {
  emit('update:modelValue', newList.length > 0 ? 'finished' : null);
  emit('update:dataList',list.value);
}, { immediate: true, deep: true })
//监听props.modelValue 变化 赋值给list
watch(() => props.dataList, (newVal) => {
  if (newVal.length > 0) {
    newVal.forEach(item => {
      item.deleted = 0
    })
    list.value = newVal;
  }
  
})
const columns = [
  {
      title: '拓展单位',
      dataIndex: 'expandCompanyName',
      required: true,
      editType: 'expandCompanyId'
  },
  {
      title: '业绩分配比例',
      dataIndex: 'assignmentRate',
      required: true,
      editType: 'number'
  },
  {
      title: '业绩分配描述',
      dataIndex: 'assignmentDesc',
      editType: 'text'
  },
]

onMounted(() => {
  if (!props.readOnly) {
      columns.push(
          {
              title: '操作',
              key: 'action',
              width: 180,
              fixed: 'right'
          }
      )
  }
  getList();
})
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
</style>
