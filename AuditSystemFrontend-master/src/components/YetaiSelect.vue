<template>
  <a-cascader
    :getPopupContainer="
      (trigger) =>
        trigger.parentNode.parentNode.parentNode.parentNode.parentNode
    "
    v-model:value="yetaiIds"
    :options="selectOptions"
    :show-search="{ yetaiFilter }"
    class="w_full"
    :fieldNames="{
      label: 'label',
      value: 'value',
      children: 'children',
    }"
    placeholder="请选择"
    :multiple="multiple"
    allowClear
  />
</template>
<script setup>
import api from "@/api/index";
const props = defineProps({
  modelValue: {
    type: String,
    default: null,
  },
  multiple: {
    type: Boolean,
    default: null,
  },
});

const selectOptions = ref([]);
const getDic = () => {
  api.common.getYeTaiDic().then((res) => {
    // console.log("res", res.data);
    selectOptions.value = res.data;
  });
};
const multiple = ref(props.multiple);
const emit = defineEmits(["update:modelValue"]);
const yetaiIds = computed({
  get: () => {
    let arr = [];
    // 编辑回显
    console.log("modelValue", props);
    if (props.modelValue !== null && props.multiple == false) {
      let arr = getTreeIds(selectOptions.value, props.modelValue);
      // console.log("arr", arr);
      return arr;
    }
    if (props.modelValue == null) {
      let arr = null;
      return arr;
    }
    return;
  },
  set: (val) => {
    // console.log("777", val);
    emit("update:modelValue", val[1]);
  },
});
onMounted(() => {
  getDic();
});
// 过滤--直接搜索选项并选择
const yetaiFilter = (inputValue, path) => {
  return path.some(
    (option) =>
      option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1
  );
};
// 根据子级value查找其父级value
const getTreeIds = (tree, nodeId, config) => {
  let {
    children = "children",
    value = "value",
    label = "label",
  } = config || {};
  const toFlatArray = (tree, parentId) => {
    return tree.reduce((t, _) => {
      const child = _[children];
      return [
        ...t,
        parentId ? { ..._, parentId } : _,
        ...(child && child.length ? toFlatArray(child, _[value]) : []),
      ];
    }, []);
  };
  const getIds = (flatArray) => {
    let ids = [nodeId];
    let child = flatArray.find((_) => _[value] === nodeId);
    while (child && child.parentId) {
      ids = [child.parentId, ...ids];
      child = flatArray.find((_) => _[value] === child.parentId);
    }
    return ids;
  };
  return getIds(toFlatArray(tree));
};
</script>

