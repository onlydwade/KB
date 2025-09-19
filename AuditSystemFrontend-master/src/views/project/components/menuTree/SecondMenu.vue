
<template>
    <div class="second_menu">
        <div class="menu_tab" v-if="!menuHidden">
            <div class="tab_item"
            v-for="(item,index) in menuList"
            :key="index"
            :class="{'tab_item_active':tabIndex==index,'tab_item_disabled':(index>0&&menuList[index-1].status!=1&&treeTabs[0]!=0)}"
            @click="tabChange(index)">
                <span>
                    {{(item.code=='yjqrsp'&&inStock=='SHI')?'续签确认审批':item.name}}
                    <template v-if="!item.uncheck">
                        <check-circle-outlined v-if="item.status==1" :class="{'color-success':tabIndex!=index}"/>
                        <clock-circle-outlined v-else :class="{'color-gray':tabIndex!=index}" />
                    </template>
                </span>
            </div>
        </div>
        <div class="second-content">
            <component :is="components[componentCode]" :projectId="projectId" :menuId="menuId" :menuHidden="menuHidden" :menuInfo="menuInfo" :projectType="projectType" @ok1="ok1" :projectInfo="projectInfo"></component>
        </div>
    </div>
</template>
<script setup>
import LoadingComponent from '@/components/LoadingComponent.vue'
const emit = defineEmits(['ok'])
const props = defineProps({
    projectId: {
        type    : Number,
        default : 0,
    },
    menuList:{
        type    : Array,
        default : [],
    },

    menuHidden:{
        type    : Boolean,
        default : false,
    },
    projectType:{
      type    : String,
      default : '',
    },
    projectInfo:{
      type    : Object,
      default : {},
    },
})
onMounted(() => {
    initialize();
})
const modules       = import.meta.glob("./**/*.vue");
const inStock       = inject('getAutoParams')('inStock')
const treeTabs      = inject('treeTabs');
const treeTabChange = inject('treeTabChange');
const initialize    = ()=>{
    props.menuList.forEach((item, i) => {
        let path = ''
        if(item.children&&item.children.length>0){
            path = './ThirdMenu.vue';
        }else{
            path = './'+item.code+'.vue';
        }
        components[item.code] = defineAsyncComponent({
            loader           : modules[path],
            // loader :  () => import('@/components/LoadingComponent.vue'),
            loadingComponent : LoadingComponent,
        });
    });
    tabChange(treeTabs.value[1],'firstIn');
}
const tabIndex      = ref(0);
const components    = {}
const componentCode = ref('');
const menuId        = ref(null);
const tabChange     = (current,first)=>{
    if(first!='firstIn'&&current>0&&props.menuList[current-1].status!=1&&treeTabs.value[0]!=0){
        return;
    }
    tabIndex.value      = current;
    componentCode.value = props.menuList[current].code;
    menuId.value        = props.menuList[current].id;
    if(first!='firstIn'){
        treeTabChange({index:1,value:current});
        treeTabChange({index:2,value:0});
    }

    //替换路由code
    if(!props.menuList[current].children || props.menuList[current].children.length==0){
        replaceRouter(componentCode.value);
    }
}
const menuInfo = computed(()=>{
    return props.menuList[tabIndex.value] || {};
})


const router        = useRouter();
const route         = useRoute();
const replaceRouter = (code)=>{
    let query = {...route.query,to:code};
    delete query.code;
    router.replace({query:query});
}
const ok1=(val)=>{
    emit('ok')
}
const statusProgress = function(value) {
    emit('ok')
}
provide('statusProgress', statusProgress)
provide('replaceRouter',replaceRouter);
</script>
<style scoped lang="less">
.menu_tab{
    display : flex;
    .tab_item{
        font-size        : 16px;
        padding          : 8px 16px;
        border-radius    : 4px 4px 0 0;
        background-color : #fffaf0;
        color            : @primary-color;
        margin-right     : 8px;
        cursor           : pointer;
        box-shadow       : 0 -4px 4px rgba(249,156,52,0.1) inset;
        position         : relative;
        overflow         : hidden;
        span{
            z-index    : 2;
            position   : relative;
            transition : all 0.3s;
        }
        &:hover{
            opacity: 0.85;
        }
        &::after{
            content          : '';
            height           : 0;
            width            : 100%;
            background-color : @primary-color;
            border-radius    : 1px;
            position         : absolute;
            bottom           : 0;
            left             : 0;
            transition       : all 0.3s;
            z-index          : 1;
        }
    }
    .tab_item_active{
        span{
            color            : #fff;
        }
        &::after{
            height : 100%;
        }
    }
    .tab_item_disabled{
        cursor           : not-allowed;
        background-color : #ccc;
        color            : #555;

        &:hover{
            opacity: 1;
        }
    }
}
.second-content{
    background-color : #fff;
    border-radius    : 0 4px 4px 4px;
    flex             : 1;
    height: 0;
    display        : flex;
    flex-direction : column;
    flex           : 1;
}
.second_menu{
    display        : flex;
    flex-direction : column;
    flex           : 1;
}
</style>
