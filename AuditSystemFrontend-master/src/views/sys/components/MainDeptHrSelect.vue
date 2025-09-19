<template>
<a-spin :spinning="loadding">
    <a-tree-select
        v-model:value="checkedKeys"
        show-search
        allow-clear
        tree-checkable
        style="width: 100%"
        placeholder="请选择"
        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
        tree-default-expand-all
        filterTreeNode
        maxTagCount="responsive"
        treeNodeFilterProp="desc2"
        :field-names="{
            children: 'childs',
            label: 'desc2',
            value: 'desc1',
        }"
        :tree-data="menuList">
    </a-tree-select>
</a-spin>
</template>
<script setup>
    const emit  = defineEmits(['update:modelValue']);
    const props = defineProps({
        modelValue      : {
            type    : Array,
            default : [],
        },
        menuList      : {
            type    : Array,
            default : [],
        },
        loadding:{
            type    : Boolean,
            default : false,
        }
    })
    const checkedKeys = computed({
        get : () => props.modelValue,
        set : (val) => emit('update:modelValue',val)
    })

    onMounted(() => {
    })
</script>
<style scoped lang="less">
</style>
