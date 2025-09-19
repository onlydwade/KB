<template>
    <a-drawer :maskClosable="false"
    :width="1024"
    :title="title"
    destroyOnClose
    @close="handleClose"
    :visible="visible">
        <template #extra>
            <a-space :size="16" v-if="type=='SHOW'">
                <a-button size="large" @click="handleClose">关闭窗口</a-button>
            </a-space>
            <a-space :size="16" v-else>
                <a-button size="large" @click="handleClose">取消</a-button>
                <a-button size="large" type="primary" @click="handleOk">保存</a-button>
            </a-space>
        </template>
        <a-form layout="vertical" :model="formData" ref="formRef">
            <Title class="titlebar">
                <template #title>
                    信息填写
                    <span class="color-danger"> * </span>
                </template>
            </Title>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item label="企业名称" name="companyName" v-if="companyId || type!='ADD'">
                        <a-input disabled allowClear :value="formData.companyName"/>
                    </a-form-item>
                    <a-form-item required label="企业名称" name="companyId" v-else>
                        <DataSelect
                            v-model="formData.companyId"
                            modeName="company"
                            :options="{
                                key   : 'id',
                                label : 'name'
                            }"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="会议标题" name="title">
                        <a-input :disabled="disabled" allowClear v-model:value="formData.title"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="记录人" name="takeId">
                        <UserSelect v-model="formData.takeId" :users="formData.take" :disabled="disabled"/>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item required label="会议类型" name="type">
                        <a-select :getPopupContainer="trigger => trigger.parentNode"
                            :disabled="disabled"
                            v-model:value="formData.type"
                            class="w_full"
                            placeholder="请选择"
                            :options="dict.options('SAN_HUI_HUI_YI_LEI_XING')">
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item required label="会议时间" name="meetingTime">
                    <a-date-picker v-model:value="formData.meetingTime"
                                   :disabled="disabled"
                                   show-time
                                   placeholder="会议时间"
                                   class="w_full"
                                   valueFormat="YYYY-MM-DD HH:mm:ss"
                                   format="YYYY-MM-DD HH:mm:ss" />
                  </a-form-item>
                </a-col>
            </a-row>
            <a-form-item label="内容描述" name="content">
                <a-textarea :disabled="disabled" allowClear :rows="3" type="textarea" v-model:value="formData.content" placeholder="请输入(200字以内)" show-count :maxlength="200"/>
            </a-form-item>
            <template v-if="companyIdEnd">
                <Title class="titlebar" title="文件上传">
                    <a-form-item name="fileOk"
                    :rules="{required: true,message: '( 请上传必传文件后提交!!! )',}">
                    </a-form-item>
                </Title>
                <CompanyDocuments v-model="formData.fileOk" :type="docType" v-if="visible" ref="documentRef" :companyId="companyIdEnd" :menuId="menuId" :recordId="formData.id" :readOnly="disabled"/>
            </template>
            <a-alert :show-icon="false" banner v-else>
                <template #message>
                    选择企业后完善更多信息！！！
                </template>
            </a-alert>
        </a-form>
    </a-drawer>
</template>
<script setup>
import api              from '@/api/index';
import { message  }     from 'ant-design-vue';
import { mainStore }    from '@/store';
import { useDictStore } from '@/store/dict';
const store = mainStore();
const dict = useDictStore();

const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 53,
    }
})

const companyIdEnd = computed(()=>{
    return props.companyId || formData.value.companyId;
})
const emit          = defineEmits(['success'])
const visible       = ref(false);
const type          = ref('ADD');
const disabled      = computed(()=>type.value=='SHOW');
const title         = ref('');
const formData      = ref({});
const formRef       = ref(null);
const documentRef   = ref(null);
const handleOk      = ()=>{
    formRef.value.validateFields().then(async vRes=>{
        formData.value.companyId = companyIdEnd.value;
        if(type.value=='ADD'){
            let addNum = 0;
            for (var i = 0; i < dict.options('HUI_YI_WEN_DANG_LEI_XING').length; i++) {
                let type = dict.options('HUI_YI_WEN_DANG_LEI_XING')[i].value;
                if(documentRef.value.getTypeDocNum(type)>0){
                    addNum++;
                    await readySubmit(type);
                }
            }
            if(addNum==0){
                message.error('添加“三会”记录请至少上传一种文档类型文件！');
                return;
            }
        }else{
            let res = await api.investment.correlationEdit(formData.value,'ompanyHighLevelMeeting');
            if(res.code!=200){
                return;
            }
        }
        emit('success');
        message.success('操作成功');
        visible.value = false;

    })
}
const readySubmit = (type)=>{
    return new Promise((resolve, reject)=>{
        let postData          = JSON.parse(JSON.stringify(formData.value));
        postData.documentType = type;
        api.investment.correlationAdd(postData,'ompanyHighLevelMeeting').then(async res=>{
            if(res.code==200){
                let uploadResult = await documentRef.value.batchUpLoad(res.data.id,type);
                resolve();
            }else{
                reject('异常');
            }
        }).catch(()=>{
            reject('异常');
        })
    })
}

const handleClose = ()=>{
    visible.value = false;
}
const titleObj = {ADD:'新建',EDIT:'修改',SHOW:'查看'};
const docType  = ref(null);
const open     = (key,data,companyName)=>{
    type.value    = key;
    try {
        formData.value = JSON.parse(JSON.stringify(data));
    } catch (e) {
        type.value     = 'ADD';
        formData.value = {};
    }
    title.value   =  titleObj[type.value] + ' “三会” 记录';
    if(key=='ADD'){
        formData.value.takeId = store.userInfo.userId;
        formData.value.take = {
            userId   : store.userInfo.userId,
            realname : store.userInfo.realname
        }

        if(companyName){
            formData.value.companyName  = companyName;
        }
    }
    docType.value = data.documentType || null;
    visible.value = true;
}
defineExpose({open})

</script>
<style scoped lang="less">
.titlebar{
    margin : 16px -16px;
    &:first-child{
        margin-top : -16px;
    }
}
</style>
