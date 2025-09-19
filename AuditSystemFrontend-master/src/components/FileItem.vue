
<template>
    <div class="file_item">
        <div class="file_item_content">
            <paper-clip-outlined />
            <a-tooltip title="下载查看" placement="top">
              <span class="name" @click="fileDownload">{{parseData.name}}</span>
            </a-tooltip>
<!--              <template v-if="hasPermission(['biz:file:downloadSingle'])">-->
<!--                  <a-tooltip title="下载查看" placement="top">-->
<!--                      <span class="name" @click="fileDownload">{{parseData.name}}</span>-->
<!--                  </a-tooltip>-->
<!--              </template>-->
<!--              <template v-else>-->
<!--                  <a-tooltip title="暂无下载/查看权限" placement="top">-->
<!--                      <span class="name" @click="fileDownloadTip">{{parseData.name}}</span>-->
<!--                  </a-tooltip>-->
<!--              </template>-->

            <span class="time" v-if="parseData.time">{{dateFormat(parseData.time)}}</span>
            <a-tooltip title="删除" placement="top" v-if="(!readOnly && canDel==0) || canDel==1">
                <close-outlined class="i_btn" @click="fileDel"/>
            </a-tooltip>
        </div>
    </div>
</template>
<script setup>
import api            from '@/api/index';
import { message } from 'ant-design-vue';
import { dataToFile,hasPermission } from '@/utils/tools';
import { mainStore }  from '@/store';
const store = mainStore();
const props = defineProps({
    fileData:{
        type    : [Object,String],
        default : {},
    },
    readOnly:{
        type    : Boolean,
        default : false,
    },
    canDel:{
        type    : Number,
        default : 0,
    }
})
const parseData = computed(()=>{
    return typeof(props.fileData)=='string'?JSON.parse(props.fileData):props.fileData;
})

const fileDownload = ()=>{
    let postData = {
        ossPath : parseData.value.ossPath,
        name    : parseData.value.name
    }
    if(isMobile){
        api.common.getFilePath(postData).then(res=>{
            if(res.code==200){
                // window.open(res.data,'_blank');

                // 创建a标签
                const link    = document.createElement('a');
                link.href     = res.data;
                link.download = parseData.value.name;
                // 将a标签添加到DOM中并触发点击事件
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }
        })
    }else{
        api.common.downloadFile(postData).then(res=>{
            dataToFile(res,parseData.value.name);
        })
    }
}

const filePreview = ()=>{
    window.open(GLOBAL_PATH.oss + parseData.value.ossPath);
}
const fileDownloadTip = ()=>{
    message.warning('暂无下载/查看权限');
}
const emit = defineEmits(['fileDel'])
const fileDel = ()=>{
    emit('fileDel');
}
</script>
<style scoped lang="less">
.file_item_content{
    display       : flex;
    align-items   : center;
    padding       : 4px;
    border-radius : 4px;

    &:hover{
        background-color: #f0f2f5;
    }
    .name{
        color       : @primary-color;
        cursor      : pointer;
        padding     : 0 4px;
        word-break  : break-all;
        word-wrap   : break-word;
        white-space : pre-wrap;

        &:hover{
            text-decoration: underline;
        }
    }
    .time{
        padding     : 0 4px;
        word-break  : break-all;
        word-wrap   : break-word;
        white-space : pre-wrap;
        font-size   : 13px;
    }
    .i_btn{
        cursor      : pointer;
        // font-size   : 20px;
        margin-left : 8px;

        &:hover{
            color: @error-color;
        }
    }
}
</style>
