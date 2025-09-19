
<template>
    <div class="third_tab">
        <div class="left_tab" v-if="!menuHidden">
            <div class="tab_item" 
            :class="{'tab_item_active':tabIndex==index}" 
            v-for="(item,index) in menuInfo.children" :key="index" 
            @click="tabChange(index)">
                {{item.name}}
                <span>
                    <check-circle-outlined v-if="item.status==1" :class="{'color-success':tabIndex!=index}"/>
                    <clock-circle-outlined v-else :class="{'color-gray':tabIndex!=index}"/>
                </span>
            </div>
        </div>
        <div class="third-content">
            <component :is="components[componentCode]" :projectId="projectId" :menuId="menuId" :menuInfo="menuInfo"></component>
        </div>
    </div>
</template>
<script setup>
import LoadingComponent from '@/components/LoadingComponent.vue'
const props = defineProps({
    projectId: {
        type    : Number,
        default : 0,
    },
    menuInfo:{
        type    : Object,
        default : {},
    },
    menuHidden:{
        type    : Boolean,
        default : false,
    }
})
onMounted(() => {
    initialize();
})
const treeTabs      = inject('treeTabs');
const treeTabChange = inject('treeTabChange');
const modules       = import.meta.glob("./**/*.vue");
const initialize    = ()=>{
    props.menuInfo.children.forEach((item, i) => {
        let path = ''
        if(item.children&&item.children.length>0){
            path = './ThirdMenu.vue';   //FouthMenu //理论上无此层级
        }else{
            path = './'+item.code+'.vue';
        }
        components[item.code] = defineAsyncComponent({
            loader           : modules[path],
            loadingComponent : LoadingComponent,
        });
    });
    tabChange(treeTabs.value[2],'firstIn');
}
const tabIndex      = ref(0);
const components    = {}
const componentCode = ref('');
const menuId        = ref(null);
const replaceRouter = inject('replaceRouter');
const tabChange     = (current,first)=>{
    tabIndex.value      = current;
    componentCode.value = props.menuInfo.children[current].code;
    menuId.value        = props.menuInfo.children[current].id;
    if(first!='firstIn'){
        treeTabChange({index:2,value:current});
    }
    replaceRouter(componentCode.value);
}
const menuInfo = computed(()=>{
    return props.menuInfo.children[tabIndex.value] || {};
})
</script>
<style scoped lang="less">
.third_tab{
    display          : flex;
}
.left_tab{
    max-width    : 360px;
    padding      : 16px;
    margin-right : 4px;
    .tab_item{
        font-size        : 14px;
        padding          : 8px 16px;
        line-height      : 24px;
        text-align       : center;
        background-color : #f0f2f5;
        margin-bottom    : 16px;
        border-radius    : 8px;
        cursor           : pointer;
        position         : relative;
        display          : flex;
        justify-content  : space-between;
        span{
            margin-left: 8px;
        }
        
        &:hover{
            opacity: 0.85;
        }
        &::after{
            content          : '';
            height           : 2px;
            width            : 0;
            background-color : @primary-color;
            border-radius    : 1px;
            position         : absolute;
            bottom           : 0;
            left             : 50%;
            transform        : translateX(-50%);
            transition       : all 0.3s;
        }
    }
    .tab_item_active{
        background-color : #fffaf0;
        color            : @primary-color;
        font-weight      : bold;
        &::after{
            width : 80%;
        }
    }
}
.third-content{
    flex          : 1;
    border-radius : 0 4px 4px 0;
    box-shadow    : -4px 0 4px rgba(0,0,0,0.05);
}
</style>
