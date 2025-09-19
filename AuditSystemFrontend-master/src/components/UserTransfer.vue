
<template>
    <a-transfer
        v-model:target-keys="targetKeys"
        class="tree-transfer"
        :one-way="true"
        :data-source="dataSource"
        :render="item => item.title"
        :show-select-all="false">
        <template #children="{ direction, selectedKeys, onItemSelect }">
            <div class="tree_box" v-if="direction === 'left'">
                <div class="search_box">
                    <a-input-search allowClear v-model:value="searchValue" placeholder="关键词筛选" />
                </div>
                
                <a-tree
                  v-if="treeData.length>0 && !changeStatus"
                  checkable
                  show-search
                  defaultExpandAll
                  :checked-keys="[...selectedKeys, ...targetKeys]"
                  :tree-data="treeData"
                  @check="
                    (_, props) => {
                      onChecked(props, [...targetKeys, ...targetKeys], onItemSelect);
                    }
                  "
                  @select="
                    (_, props) => {
                      onChecked(props, [...selectedKeys, ...targetKeys], onItemSelect);
                    }
                  "
                />
            </div>
            
        </template>
        <template #notFoundContent>
            <span>没数据</span>
        </template>
    </a-transfer>
</template>
<script setup>
import api           from '@/api/index';
import { mainStore } from '@/store';
const store = mainStore();

const props = defineProps({
    modelValue : {
        type    : Array,
        default : [],
    },
    sourceType : {
        type    : Number,
        default : 1,
    },
    disabled   : {
        type    : Boolean,
        default : false,
    }
})
const emit   = defineEmits(['update:modelValue']);
const targetKeys = computed({
    get : () => props.modelValue,
    set : (val) => emit('update:modelValue',val)
}) 

const dataSource1 = ref([]);
const dataSource2 = ref([]);
const dataSource  = computed(()=>{
    let arr = [];
    function flatten(list){
        list.forEach((item, i) => {
            if(item.children&&item.children.length>0){
                flatten(item.children);
            }else{
                arr.push({
                    key   : item.key,
                    title : item.title
                })
            }
        });
    }
    flatten(props.sourceType==1?dataSource1.value:dataSource2.value);
    return arr;
});

const searchValue = ref('');
const treeData = computed(()=>{
    let list = props.sourceType==1?dataSource1.value:dataSource2.value;
    if(!searchValue.value){
        return list;
    }else{
        return filterTree(JSON.parse(JSON.stringify(list)),searchValue.value);
    }
});

const filterTree = (nodes, query)=> {
  // 条件就是节点的title过滤关键字
  let predicate = function (node) {
    if (node.title.includes(query)||targetKeys.value.includes(node.key)) {
      return true;
    } else {
      return false;
    }
  };

  // 结束递归的条件
  if (!(nodes && nodes.length)) {
    return [];
  }
  let newChildren = [];
  for (let node of nodes) {
    //一、带父节点     以下两个条件任何一个成立，当前节点都应该加入到新子节点集中
    // 1. 子孙节点中存在符合条件的，即 subs 数组中有值
    // 2. 自己本身符合条件
    let subs = filterTree(node.children, query);
    if (predicate(node)) {
      newChildren.push(node);
    } else if (subs && subs.length) {
      node.children = subs;
      newChildren.push(node);
    }
  }
  return newChildren.length ? newChildren : [];
}

const onChecked = (e, checkedKeys, onItemSelect) => {
    const {eventKey} = e.node;
    function doSelect(tree,checked){
        if(tree.children&&tree.children.length>0){
            tree.children.forEach((item, i) => {
                doSelect(item,checked);
            });
        }else{
            onItemSelect(tree.key,checked);
        }
    }
    doSelect(e.node,!e.node.checked);
};
const isChecked = (selectedKeys, eventKey)=> {
  return selectedKeys.indexOf(eventKey) !== -1;
}

const changeStatus = ref(false);
watch(()=>props.sourceType,()=>{
    changeStatus.value = true;
    getSource();
    nextTick(()=>{
        changeStatus.value = false;
    })
})
const getSource = ()=>{
    if(props.sourceType==1&&dataSource1.value.length==0){
        api.sys.deptUserTree().then(res=>{
            if(res.code==200){
                let tree = [];
                function doArr(list,parent){
                    let hasUser = false;
                    list.forEach((item, i) => {
                        let push = false;
                        let obj = {
                            key      : 'tree_'+item.id,
                            title    : item.name,
                            children : [],
                        }
                        if(item.children&&item.children.length>0){
                            push = doArr(item.children,obj.children);
                        }
                        if(item.userVOList&&item.userVOList.length>0){
                            push    = true;
                            item.userVOList.forEach(user => {
                                obj.children.push({
                                    key      : user.userId,
                                    title    : user.realname,
                                })
                            });
                        }
                        if(push){
                            hasUser = true;
                            parent.push(obj);
                        }
                    });
                    return hasUser;
                }
                doArr(res.data || [],tree);
                dataSource1.value = tree;
            }
        })
    }
    if(props.sourceType==2&&dataSource2.value.length==0){
        api.sys.postUserTree().then(res=>{
            if(res.code==200){
                let tree = [];
                function doArr(list,parent){
                    list.forEach((item, i) => {
                        let hasUser = false;
                        let obj = {
                            key      : 'tree_'+item.postId,
                            title    : item.postName,
                            children : [],
                        }
                        if(item.postUserVOList&&item.postUserVOList.length>0){
                            hasUser = true;
                            item.postUserVOList.forEach(user => {
                                obj.children.push({
                                    key      : user.userId,
                                    title    : user.realName,
                                })
                            });
                        }
                        if(hasUser){
                            parent.push(obj);
                        }
                    });
                }
                doArr(res.data || [],tree);
                dataSource2.value = tree;
            }
        })
    }
}
onMounted(() => {
    getSource();
})
</script>
<style scoped lang="less">
.tree-transfer{
    :deep(.ant-transfer-list-body-customize-wrapper){
        height   : 500px;
        overflow : auto;
    }
}
.tree_box{
    height         : 100%;
    display        : flex;
    flex-direction : column;
    .search_box{
        padding: 8px;
    }
    :deep(.ant-tree){
        flex     : 1;
        height   : 0;
        overflow : auto;
    }
}
</style>
