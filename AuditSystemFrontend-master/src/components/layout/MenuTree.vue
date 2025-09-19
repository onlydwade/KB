<template>
    <a-tree v-model:checkedKeys="checkedKeys" checkable :field-names="{
        children: 'children',
        title: 'menuName',
        key: 'menuId',
    }" @check="checkedChange" :tree-data="menuTree">
    </a-tree>
</template>
<script setup>
import { handleTree } from '@/utils/tools';
import { watch } from "vue";
const emit = defineEmits(['update:modelValue']);
const props = defineProps({
    modelValue: {
        type: Array,
        default: [],
    },
    menuList: {
        type: Array,
        default: [],
    },
})
const checkedKeys = ref([]);
const menuTree = computed(() => {
    return handleTree(props.menuList, "menuId", 'parentId', 'children', 'hidden');
});
onMounted(() => {
    let arr = []
    props.menuList.forEach((item, i) => {
        if (!arr.includes(item.parentId) && item.parentId != 0) {
            arr.push(item.parentId);
        }
    });
    let checkeds = [];
    props.modelValue.forEach((menuId, i) => {
        if (!arr.includes(menuId)) {
            checkeds.push(menuId);
        }
    });
    checkedKeys.value = checkeds;
})

const checkedChange = (selectedKeys, info) => {
    let allSelect = selectedKeys.concat(info.halfCheckedKeys);
    emit('update:modelValue', allSelect);
}
</script>
<style scoped lang="less">
.tag_box {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: -10px;
    flex: 1;
}

.tags_item {
    margin-right: 10px;
    margin-bottom: 10px;
}

.tags_input {
    display: inline-block;
    width: auto;
}
</style>
