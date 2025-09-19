<template>
  <div class="card_box" v-if="list.length">
    <div class="title">
     文件上传
    </div>
    <div class="list_box" v-for="(item,index) in list" :key="index">
      <a-row>
        <a-col :span="18">
          <div class="name">{{ item.name }}</div>
          <div class="time">{{ dateFormat(item.time,'YYYY-MM-DD HH:mm:ss') }}</div>
        </a-col>
        <a-col :span="6" align="middle" class="btn">
          <span  class="color-primary" @click="fileDownload(item)">点击下载</span>
        </a-col>
      </a-row>
    </div>
  </div>
</template>
<script setup>
import api            from '@/api/index';
import { message } from 'ant-design-vue';
import { dataToFile,hasPermission,dateFormat } from '@/utils/tools';
const props = defineProps({
  list: {
    type: Array,
    default: [],
  },
});
const fileDownload = (data)=>{
    let postData = {
        ossPath : data.ossPath,
        name    : data.name
    }
    if(isMobile){
        api.common.getFilePath(postData).then(res=>{
            if(res.code==200){
                // window.open(res.data,'_blank');

                // 创建a标签
                const link    = document.createElement('a');
                link.href     = res.data;
                link.download = postData.name;
                // 将a标签添加到DOM中并触发点击事件
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }
        })
    }else{
        api.common.downloadFile(postData).then(res=>{
            dataToFile(res,postData.name);
        })
    }
}
const fileDownloadTip = ()=>{
    message.warning('暂无下载/查看权限');
}
onMounted(() => {
 
})
</script>
<style lang="less" scoped>
.card_box {
  margin: 20px 0;
  padding: 10px;
}
.title {
  color: #000;
  font-weight: bold;
  line-height: 40px;
}
.list_box {
  border: 1px solid #f0f2f5;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 8px;
  .name{
    font-size: 16px;
    
  }
  .time{
    color: #969799;
  }
}
.btn{
  display: flex;
  margin: auto;
}
</style>
