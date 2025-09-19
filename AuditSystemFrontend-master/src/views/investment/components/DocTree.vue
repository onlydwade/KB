
<template>
    <div class="left_tree">
        <a-tree
            v-if="treeData.length>0"
            showLine
            defaultExpandAll
            v-model:selectedKeys="docId"
            selectable
            :field-names="{
                children: 'children',
                title: 'name',
                key: 'id',
            }"
            :tree-data="treeData">
          </a-tree>
    </div>
</template>
<script setup>
    const emit  = defineEmits(['update:modelValue','change'])
    const props = defineProps({
        modelValue      : {
            type    : Number,
            default : null,
        },
        treeData:{
            type    : Array,
            default : [],
        }
    })
    onMounted(() => {
    })
    const docId = computed({
        get : () => props.modelValue?[props.modelValue]:[],
        set : (val) => {
            emit('update:modelValue',...val);
            emit('change',...val);
        }
    })
</script>
<style scoped lang="less">
.left_tree{
    padding-right : 16px;
    min-width     : 200px;
    :deep(.ant-tree .ant-tree-node-content-wrapper.ant-tree-node-selected){
        background-color : rgba(0,0,0,0);
        color            : @primary-color;
        font-weight      : bold;
        position: relative;
        &::after{
            content          : '';
            position         : absolute;
            width            : 100%;
            height           : 2px;
            background-color : @primary-color;
            bottom           : 0;
            left             : 0;
            border-radius    : 1px;
        }
    }
    :deep(.ant-tree-title){
        word-break: keep-all;
        white-space: nowrap;
    }
}
</style>
