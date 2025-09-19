<template>
    <a-tree-select
        v-model:value="deptId"
        show-search
        allow-clear
        tree-checkable
        style="width: 100%"
        placeholder="请选择"
        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
        tree-default-expand-all
        treeNodeFilterProp="deptName"
        treeCheckStrictly
        filterTreeNode
        :dropdownStyle = '{
            whiteSpace: "nowrap"
        }'
        @select="select"
        :field-names="{
            children: 'children',
            label: 'deptName',
            value: 'deptId',
        }"
        :tree-data="leaf?store.getDeptLeafSelect:noRoot?store.getDeptWithOutRoot:store.deptTree">
    </a-tree-select>
</template>
<script setup>
import api           from '@/api/index';
import { mainStore } from '@/store';
const store = mainStore();

const props = defineProps({
    modelValue : {
        type    : [Array,Number,String],
        default : [],
    },
    disabled   : {
        type    : Boolean,
        default : false,
    },
    leaf : {
        type    : Boolean,
        default : false,
    },
    noRoot:{
        type    : Boolean,
        default : false,
    }
})
const emit   = defineEmits(['update:modelValue','change']);
const deptId = computed({
    get : () => props.modelValue,
    set : (val) => {
        emit('update:modelValue',val.map(item=>{
            return item.value
        }))
    }
}) 
const select = (val,node)=>{
    emit('change',val,node)
}   
</script>
<style scoped lang="less">

</style>
