
<template>
    <a-button @click="open" v-permission="['biz:projectStepMenu:update']">
        项目流程节点配置
    </a-button>
    <a-modal v-model:visible="visible" :width="660" destroyOnClose>
        <template #title>
            <span>流程节点配置</span>
            <span class="color-warning subtitle">
                (完成一个节点配置后请先保存后再切换其它配置节点)
            </span>
        </template>
        <a-tabs v-model:activeKey="projectType" animated @change="projectTypeChange" style="margin-top:-16px;">
            <a-tab-pane v-for="(item,index) in dict.options('XIANG_MU_LEI_XING')" :key="item.value" :tab="item.label"></a-tab-pane>
            
            <template #rightExtra>
                <a-button v-if="projectType=='OA_CONFIG'" type="primary">
                    <template #icon><setting-outlined /></template>
                    流程节点审批配置
                </a-button>
                <a-button v-else @click="projectType='OA_CONFIG'">流程节点审批配置</a-button>
            </template>
        </a-tabs>
        <NodeTreeOa v-if="projectType=='OA_CONFIG'" :menuList="menuList"/>
        <div style="height:432px;margin:-16px;" v-else>
            <AScrollbar>
                <div class="padding_box">
                    <NodeTree v-model="menuIds" v-if="menuList.length>0&&showTree" :menuList="menuList"/>
                </div>
            </AScrollbar>
        </div>
        <template #footer>
            <a-button @click="visible=false">
                关闭
            </a-button>
            <a-button @click="save(true)" type="primary" v-if="projectType!='OA_CONFIG'">
                保存
            </a-button>
            <a-button @click="save(false)" type="danger" v-if="projectType!='OA_CONFIG'">
                保存并关闭
            </a-button>
        </template>
    </a-modal>
</template>
<script setup>
import api              from '@/api/index';
import { message }      from 'ant-design-vue';
import { mainStore }    from '@/store';
import NodeTree         from './NodeTree.vue'
import NodeTreeOa       from './NodeTreeOa.vue'
import { useDictStore } from '@/store/dict';
const dict  = useDictStore();
const store = mainStore();

const visible     = ref(false);
const projectType = ref('DAN_YI_FEI_TOU_BIAO_XIANG_MU');

const menuIds     = ref([]);
const save        = (close)=>{
    if(projectType.value=='OA_CONFIG'){
        
    }else{
        let postData = {
            projectType : projectType.value,
            stepMenuIds : menuIds.value
        }
        api.project.treeStepMenuUpdate(postData).then(res=>{
            if(res.code==200){
                message.success('操作成功');
                visible.value = close;
            }
        })
    }
}
const open = ()=>{
    store.spinChange(1);
    getMenu(async ()=>{
        if(menuList.value.length==0){   //是否获取菜单数据
            let menuRes = await api.project.treeStepAll();
            if(menuRes.code==200){
                menuList.value = menuRes.data;
            }
        }
        store.spinChange(-1);
        visible.value = true;
    });
    
}
const showTree = ref(true);
const menuList = ref([]);
const getMenu  = async (callBack)=>{
    let res = await api.project.listStepByType(projectType.value);
    if(res.code==200){
        menuIds.value = res.data.map(item=>{
            return item.stepMenuId;
        });
    }
    if(callBack){
        callBack();
    }
}
const projectTypeChange = ()=>{
    if(projectType.value=='OA_CONFIG'){
        
    }else{
        showTree.value = false;
        getMenu(()=>{
            showTree.value = true;
        });
    }
}
</script>
<style scoped lang="less">
.subtitle{
    font-size   : 14px;
    font-weight : normal;
}
</style>
