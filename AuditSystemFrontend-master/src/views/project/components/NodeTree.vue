<template>
    <a-tree
        v-model:checkedKeys="checkedKeys"
        checkable
        :field-names="{
            children: 'children',
            title: 'name',
            key: 'id',
        }"
        @check="checkedChange"
        :tree-data="menuList">
    </a-tree>
</template>
<script setup>
    import { handleTree } from '@/utils/tools';
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
    })
    const checkedKeys = ref([]);
    onMounted(() => {
        removeParentId();
    })
    const removeParentId = ()=>{
        let arr = [];
        function doArr(list){
            list.forEach((item, i) => {
                if(!item.children || item.children.length==0){
                    if(props.modelValue.includes(item.id)){
                        arr.push(item.id);
                    }
                }else{
                    doArr(item.children);
                }
            });
        }
        doArr(props.menuList);
        checkedKeys.value = arr;
    }
    const checkedChange = (selectedKeys,info)=>{
        let allSelect = selectedKeys.concat(info.halfCheckedKeys);
        emit('update:modelValue',allSelect);
    }
</script>
<style scoped lang="less">
.tag_box{
    display       : flex;
    flex-wrap     : wrap;
    margin-bottom : -10px;
    flex          : 1;
}
.tags_item{
    margin-right  : 10px;
    margin-bottom : 10px;
}
.tags_input{
    display : inline-block;
    width   : auto;
}
</style>
