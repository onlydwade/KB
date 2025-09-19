<template>
    <div class="oa_header">
        <div class="name">
            项目流程节点
        </div>
        <div class="switch_box">
            OA审批
        </div>
        <div class="switch_box">
            线下审批
        </div>
    </div>
    <div style="height:395px;margin:0 -16px -16px -16px">
        <AScrollbar>
            <div class="padding_box">
                <a-tree
                v-if="treeData.length>0"
                defaultExpandAll
                blockNode
                showLine
                :selectable="false"
                :field-names="{
                    children : 'children',
                    title    : 'name',
                    key      : 'id',
                }"
                :tree-data="treeData">
                <template #title="{dataRef}">
                    <div class="tree_node">
                        <div class="name">
                            <EllipsisTooltip class="flex_full" :content="dataRef.name"/>
                        </div>
                        <template v-if="!dataRef.children || dataRef.children.length==0">
                            <div class="switch_box">
                                <a-switch v-model:checked="dataRef.oaApproval" @change="oaChange(dataRef)" :checkedValue="1" checked-children="开启" :unCheckedValue="0" un-checked-children="关闭" />
                            </div>
                            <div class="switch_box">
                                <a-switch v-if="dataRef.oaApproval==1" v-model:checked="dataRef.offlineApproval" @change="oaChange(dataRef)" :checkedValue="1" checked-children="开启" :unCheckedValue="0" un-checked-children="关闭" />
                            </div>
                        </template>
                    </div>
                </template>
              </a-tree>
            </div>
        </AScrollbar>
    </div>
</template>
<script setup>
import api            from '@/api/index';
import { handleTree } from '@/utils/tools';
import { watchArray } from '@vueuse/core'
const props = defineProps({
    menuList      : {
        type    : Array,
        default : [],
    },
})
const treeData = ref([]);
watchArray(props.menuList, (newList) => {
    treeData.value = newList.filter(item=>{
        return item.code!='jcxx'&&item.code!='tdzj';
    });
},{immediate:true,deep: true})

const oaChange = (item)=>{
    let postData = {
        id              : item.id,
        oaApproval      : item.oaApproval,
        offlineApproval : item.offlineApproval,
    }
    if(item.oaApproval==0){
        item.offlineApproval     = 0;
        postData.offlineApproval = 0;
    }
    api.project.treeStepUpdate(postData);
}
onMounted(() => {
})
</script>
<style scoped lang="less">
.tree_node{
    display        : flex;
    width          : 100%;
    margin-bottom : 8px;
    position       : relative;
    
    &::after{
        content    : '';
        position   : absolute;
        height     : 0;
        width      : 100%;
        top        : 0;
        bottom     : 0;
        left       : 0;
        margin     : auto;
        border-top : 1px dashed #eee;
        z-index    : 1;
    }
    .name{
        flex             : 1;
        width            : 0;
        z-index          : 2;
        :deep(.content){
            span{
                background-color : #fff;
                padding-right    : 8px;
            }
        }
    }
    .switch_box{
        z-index          : 3;
        background-color : #fff;
    }
}
.oa_header{
    display          : flex;
    background-color : #f0f2f5;
    padding          : 8px 16px;
    margin           : -17px -16px 0 -16px;
    font-weight      : bold;
    .name{
        flex  : 1;
        width : 0;
    }
}
.switch_box{
    width            : 80px;
    text-align       : center;
}
</style>
