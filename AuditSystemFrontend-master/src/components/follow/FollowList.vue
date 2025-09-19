
<template>
    <Title title="追踪动态"></Title>
    <div class="follow_content">
        <a-form
        ref="formRef"
        layout="vertical"
        v-if="!readOnly"
        :model="formData">
            <div class="mention_box">
                <a-form-item label="追踪内容描述" required name="followContent">
                    <a-mentions :getPopupContainer="trigger => trigger.parentNode"
                        v-model:value="formData.followContent"
                        rows="3"
                        placeholder="请输入追踪内容">
                    </a-mentions>
                </a-form-item>  
                <a-spin :spinning="uploading">
                    <div class="file_box">
                        <FileItem v-for="(item,index) in formData.followDocument" :key="index" :fileData="item" @fileDel="fileDel(index)"/>
                    </div>
                    <div class="control_box">
                        <a-upload
                            name="file"
                            :multiple="true"
                            :headers = "{
                                'Authorization':'Bearer '+ token
                            }"
                            :showUploadList="false"
                            :action="uploadPath"
                            @change="handleChange($event,index)">
                            <a-space>
                                <a-button type="link">
                                    <template #icon><picture-outlined style="fontSize:24px"/></template>
                                </a-button>
                                <a-button type="link">
                                    <template #icon><cloud-upload-outlined style="fontSize:24px"/></template>
                                </a-button>
                            </a-space>
                        </a-upload>
                        <a-button type="primary" size="large" shape="round" @click="followSubmit">发布</a-button>
                    </div>
                </a-spin>
            </div> 
        </a-form>

        <a-steps progress-dot direction="vertical" :current="-1">
            <a-step v-for="(item,index) in data.list" :key="index">
                <template #title>
                    <div class="step_date">
                        {{dateFormat(item.followTime,'YYYY-MM-DD')}}
                    </div>
                </template>
                <template #description>
                    <div class="step_desc">
                        <div class="step_bar">
                            <a-space class="params">
                                {{dateFormat(item.followTime,'HH:mm:ss')}}
                                <a-divider type="vertical" />
                                {{(item.createUser || {}).realname}}
                            </a-space>
                            <a-button v-if="!readOnly" type="text" class="color-primary" size="small" @click="edit(item,index)">编辑</a-button>
                            <a-button v-if="!readOnly" type="text" class="color-primary" size="small" @click="del(item,index)">删除</a-button>
                        </div>
                        <p class="follow_text">
                            {{item.followContent}}
                        </p>
                        <div class="file_inner">
                            <FileItem v-for="(item,fileIndex) in JSON.parse(item.followDocument || '[]')" readOnly :key="fileIndex" :fileData="item"/>
                        </div>
                    </div>
                    
                </template>
            </a-step>
        </a-steps>
         <a-empty v-if="data.list.length==0" description="暂无跟进记录" />
    </div>
    <div class="pagination_box">
        <a-pagination showSizeChanger show-quick-jumper
            v-model:current="data.pageNo"
            v-model:pageSize="data.pageSize"
            :show-total="total => `共 ${total} 条数据`"
            size="small"
            @change="getList"
            @showSizeChange="data.pageNo=1"
            :total="data.total" />
    </div>
    
    <FollowEdit ref="editRef" @success="getList" :menuId="menuId"/>
</template>
<script setup>
import api               from '@/api/index';
import { message,Modal } from 'ant-design-vue';
const props = defineProps({
    recordId:{
        type    : Number,
        default : 0,
    },
    moduleName:{
        type    : String,
        default : 'Project',
    },
    readOnly:{
        type    : Boolean,
        default : false,
    },
    
    menuId:{
        type    : Number,
        default : 0,
    }
})

const loadding = ref(false);
const data     = reactive({
    pageNo   : 1,
    pageSize : 20,
    total    : 0,
    list     : []
})
const getList    = async ()=>{
    let postData = {
        desc          : ['updateTime'],
        pageNo        : data.pageNo,
        pageSize      : data.pageSize,
        params        : {}
    }
    loadding.value = true;
    let res        = await api.common.followPage(props.moduleName,props.recordId,postData);
    
    if(res.code==200){
        data.list  = res.data.records;
        data.total = res.data.total;
    }
    loadding.value  = false;
}


const formData = reactive({
    followContent  : '',
    followDocument : [],
});
const formRef      = ref(null);
const followSubmit = ()=>{
    formRef.value.validateFields().then(async (vRes)=>{
        let postData = {
            recordId       : props.recordId,
            modelName      : props.moduleName,
            followType     : formData.followType,
            followContent  : formData.followContent,
            followDocument : JSON.stringify(formData.followDocument)
        }
        let res = await api.common.followAdd(postData);
        if(res&&res.code==200){
            getList();
            message.success('操作成功');
            
            formData.followType = null;
            formData.followContent  = '';
            formData.followDocument = [];
        }
    })
}

const editRef = ref(null);
const edit    = (item,index)=>{
    editRef.value.open(item);
}

const del = (item,index)=>{
    debugger
    Modal.confirm({
        title   : '操作确认',
        content : '确认删除此追踪记录?',
        onOk() {            
            api.common.deleteByIds(props.moduleName,props.recordId,item.id).then(res=>{
                if(res.code==200){
                    message.success('操作成功');
                    getList();
                }
            })
        }
    });
}

const uploadPath   = GLOBAL_PATH.api + '/system/upload/follow';
const token        = localStorage.getItem('token');
const uploading    = ref(false);
const handleChange = (info,index)=>{
    const status   = info.file.status;
    const response = info.file.response;
    if(status==='uploading'){
        uploading.value = true;
    }
    if (status === 'done') {
        uploading.value = false;
        if(response&&response.success){
            response.data.time = Date.now();
            let ext = response.data.name.substring(response.data.name.lastIndexOf(".")+1);
            response.data.ext = ext;
            if(props.menuId){
                response.data.stepMenuId         = props.menuId;
                response.data.documentTemplateId = 1038;
            }
            formData.followDocument.push(response.data);
        }else{
            message.error(`上传失败.`);
        }
    } else if (status === 'error') {
        uploading.value = false;
        message.error(`${info.file.name} 上传失败.`);
    }
    if(response&&response.code&&response.code==500){
        message.error(response.msg);
    }
}
const fileDel  = (index,parent)=>{
    if(parent){
        parent.splice(index,1);
    }else{
        formData.followDocument.splice(index,1);
    }
}
onMounted(() => {
    getList();
})

</script>
<style scoped lang="less">
.follow_content{
    padding:16px;
    .step_date{
        background-color : #aaa;
        color            : #fff;
        padding          : 4px 8px;
        line-height      : 20px;
        border-radius    : 14px;
        font-size        : 14px;
    }
    .step_desc{
        background-color : #f0f2f5;
        border-radius    : 4px;
        padding          : 16px;
        margin-top       : 8px;
        max-width        : 680px;
        margin-bottom    : 16px;
        .step_bar{
            display:flex;
        }
        .params{
            color : @text-color-secondary;
            flex  : 1;
        }
        .follow_text{
            font-size  : 16px;
            color      : @text-color;
        }
        
    }
    :deep(.ant-steps-item-container){
        cursor: auto!important;
    }
    
    .mention_box{
        margin-bottom : 24px;
        margin-left   : -16px;
        margin-right  : -16px;
        padding       : 0 16px 24px 16px;
        box-shadow    : 0 4px 4px rgb(0 21 41 / 4%);
        .control_box{
            margin-top: 8px;
        }
    }
    .file_box{
        max-width : 800px;
        margin-top : -16px;
    }
    .control_box{
        display         : flex;
        justify-content : space-between;
        align-items     : center;
    }
}
</style>
