
<template>
    <div class="left_filter">
        <Title v-if="title" :title="title"></Title>
        <AScrollbar>
            <div class="content-inner">
                <a-tree
                    v-if="deptList.length>0"
                    showLine
                    v-model:expandedKeys="expandedKeys"
                    v-model:selectedKeys="deptId"
                    selectable
                    :field-names="{
                        children: 'children',
                        title: 'deptName',
                        key: 'deptId',
                    }"
                    :tree-data="deptList">
                  </a-tree>
            </div>
        </AScrollbar>
    </div>
</template>
<script setup>
    import api            from '@/api/index';
    import { handleTree } from '@/utils/tools';
    const emit  = defineEmits(['update:modelValue','change'])
    const props = defineProps({
        modelValue      : {
            type    : Number,
            default : null,
        },
        title      : {
            type    : String,
            default : '',
        },
        deptList:{
            type    : Array,
            default : [],
        }
    })
    onMounted(() => {
    })
    const deptId = computed({
        get : () => [props.modelValue],
        set : (val) => {
            emit('update:modelValue',...val);
            emit('change',...val);
        }
    })
    const expandedKeys = ref([]);
    watch(() => props.deptList,
        (newVal, oldVal) => {
            if(newVal.length>0){
                expandedKeys.value = [(props.deptList[0] || {}).deptId];
            }
        },
        {deep: true}
    )
    
    // const deptList     = ref([]);
    // const getDept      = ()=>{
    //     api.sys.deptList().then(res=>{
    //         if(res.code==200&&res.data.length>0){
    //             expandedKeys.value = [res.data[0].deptId];
    //             deptList.value     = handleTree(res.data,"deptId");
    //         }
    //     })
    // }
</script>
<style scoped lang="less">
.left_filter{
    height           : 100%;
    box-sizing       : border-box;
    background-color : #fff;
    border-radius    : 4px;
    margin-right     : 16px;
    width            : 250px;
    display          : flex;
    flex-direction   : column;
    :deep(.ant-tree .ant-tree-node-content-wrapper.ant-tree-node-selected){
        background-color : rgba(0,0,0,0);
        color            : @primary-color;
        font-weight      : bold;
        position         : relative;
        
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
}
</style>
