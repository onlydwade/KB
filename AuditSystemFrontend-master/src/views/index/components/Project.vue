<template>
    <div class="content-box_full">
        <Title title="我跟进中的项目">
        </Title>
        <div class="filter-nav" style="margin-top:10px">
            <a-input-search allowClear placeholder="输入搜索" v-model:value="filterForm.searchVal" @search="filterSubmit" size="large"/>
        </div>
        <AScrollbar>
            <a-spin :spinning="loadding">
                <div class="padding_box">
                    <div class="project_item" v-for="item in data.list" :key="item.id" @click="router.push('/innerPage/projectInfo?id='+item.id)">
                        <span class="title">{{item.projectName}}</span>
                        <right-outlined style="fontSize:14px;marginLeft:3px;"/>
                    </div>
                    <template v-if="!loadding&&data.total>6">
                        <div class="load_more" v-if="data.list.length<data.total">
                            <a-button @click="loadMore">加载更多</a-button>
                        </div>
                        <a-divider v-else plain>
                            <span class="color-info">暂无更多数据</span>
                        </a-divider>
                    </template>
                    <a-empty v-if="!loadding&&data.list.length==0"/>
                </div>
            </a-spin>
        </AScrollbar>
    </div>
</template>
<script setup>
import api              from '@/api/index';
import { mainStore }    from '@/store';
const store      = mainStore();
const loadding   = ref(false);
const router     = useRouter();
const filterForm = reactive({
    pageNo    : 1,
    pageSize  : 10,
    searchVal : '',
})
const data = reactive({
    list  : [],
    total : 0,
})
const getPage = ()=>{
    let postData = {
        desc     : ['createTime'],
        pageNo   : filterForm.pageNo,
        pageSize : filterForm.pageSize,
        params   : {
            weiDu  : 'SHOW_ME',
            expire : 'YOU_XIAO',
            
            // attributorUserId : store.userInfo.userId
        },
        inParams : {
            serviceStatus:['LI_XIANG_ZHONG','TUO_ZHAN_ZHONG','TUO_ZHAN_CHENG_GONG','TOU_BIAO_ZHONG','YI_ZHONG_BIAO','JIN_DIAO_ZHONG','PING_SHEN_ZHONG','WANG_CHENG_QIAN_YUE']
        },
        likeParams : {
            projectName : filterForm.searchVal
        }
    }
    loadding.value = true;
    api.project.projectPage(postData).then(res=>{
        if(filterForm.pageNo==1){
            data.list = [];
        }
        if(res.code==200){
            data.list  = data.list.concat(res.data.records);
            data.total = res.data.total;
        }
        loadding.value = false;
    })
}
const filterSubmit = ()=>{
    filterForm.pageNo = 1;
    getPage();
}
const loadMore = ()=>{
    filterForm.pageNo += 1;
    getPage();
}
onMounted(() => {
    getPage();
})

</script>
<style scoped lang="less">
.project_item{
    margin-bottom    : 16px;
    border-radius    : 4px;
    padding          : 10px;
    display          : flex;
    align-items      : center;
    border           : 1px solid #fff2d9;
    cursor           : pointer;
    background-color : #fffaf0;
    color            : @primary-color;
    .title{
        flex               : 1;
        width              : 0;
        display            : -webkit-box;
        -webkit-box-orient : vertical;
        -webkit-line-clamp : 2;
        overflow           : hidden;
    }
    &:hover{
        border : 1px solid @primary-color;
        background-color: @primary-color;
        color  : #fff;
    }
}
.load_more{
    display: flex;
    justify-content: center;
}
</style>
