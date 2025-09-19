<template>
    <a-tree-select
        v-model:value="deptId"
        show-search
        style="width: 100%"
        :disabled="disabled"
        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
        placeholder="请选择"
        allow-clear
        selectable
        filterTreeNode
        treeNodeFilterProp="deptName"
        :dropdownMatchSelectWidth="false"
        tree-default-expand-all
        :dropdownStyle = '{
            whiteSpace: "nowrap"
        }'
        @select="select"
        :field-names="{
            children: 'children',
            label: 'deptName',
            value: 'deptId',
        }"
        :tree-data="leaf?store.getDeptLeafSelectList:noRoot?store.getDeptWithOutRootList:store.deptTree">
    </a-tree-select>
</template>
<script setup>
import api           from '@/api/index';
import { mainStore } from '@/store';
const store = mainStore();

const props = defineProps({
    modelValue : {
        type    : [Number,String],
        default : null,
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
    get : () => Number(props.modelValue) || null,
    set : (val) => {
        emit('update:modelValue',val)
    }
}) 
const select = (val,node)=>{
    emit('change',val,node)
}   
</script>
<style scoped lang="less">

</style>
