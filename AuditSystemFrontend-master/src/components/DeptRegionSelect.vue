<template>
    <a-tree-select v-model:value="deptId" show-search style="width: 100%" :disabled="disabled"
        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }" placeholder="请选择" allow-clear selectable filterTreeNode
        treeNodeFilterProp="deptName" :dropdownMatchSelectWidth="false" tree-default-expand-all :dropdownStyle='{
            whiteSpace: "nowrap"
        }' @select="select" :field-names="{
    children: 'children',
    label: 'deptName',
    value: 'deptId',
}" :tree-data="regionTreeA">
    </a-tree-select>
</template>
<script setup>
import api from '@/api/index';
import { mainStore } from '@/store';
import { ref, computed } from 'vue';
const store = mainStore();
let regionTree = ref([]);

onMounted(() => {

})

//只需要大区选择
const regionTreeA = computed(() => {
    var regionTreeList = [];
    var treeList = JSON.parse(JSON.stringify(props.leaf ? store.getDeptLeafSelect : props.noRoot ? store.getDeptWithOutRoot : store.deptTree));
    if (treeList.length == 0) {
        return regionTreeList;
    } else {
        regionTreeList = JSON.parse(JSON.stringify(treeList));
        if (treeList[0].children.length > 0) {            
            regionTreeList[0].children = [];
            for (var i = 0; i < treeList[0].children.length; i++) {
                treeList[0].children[i].children = [];
                regionTreeList[0].children.push(treeList[0].children[i]);
            }
        }
    }

    return regionTreeList;
})

const props = defineProps({
    modelValue: {
        type: [Number, String],
        default: null,
    },
    disabled: {
        type: Boolean,
        default: false,
    },
    leaf: {
        type: Boolean,
        default: false,
    },
    noRoot: {
        type: Boolean,
        default: false,
    }
})
const emit = defineEmits(['update:modelValue', 'change']);
const deptId = computed({
    get: () => Number(props.modelValue) || null,
    set: (val) => {
        emit('update:modelValue', val)
    }
})
const select = (val, node) => {
    emit('change', val, node)
}   
</script>
<style scoped lang="less"></style>
