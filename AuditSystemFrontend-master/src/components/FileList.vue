<template>
  <div class="card_box" v-for="(item,index) in listData" :key="index">
    <div class="title">
      {{ item.operName }}
    </div>
    <div class="list_box" v-for="(t,idx) in item.projectDocumentList" :key="idx">
      <a-row>
        <a-col :span="18">
          <div class="name"><EllipsisTooltip :content="t.docmentObject.name " /></div>
          <div class="time">{{dateFormat(t.docmentObject.time,'YYYY-MM-DD HH:mm:ss') }}</div>
        </a-col>
        <a-col :span="6" align="middle" class="btn">
          <span  class="color-primary" @click="fileDownload(t.docmentObject)">点击下载</span>
          <!-- <span  class="color-primary" @click="fileDownloadTip()" v-else>暂无权限</span> -->
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
  title: {
    type: String,
    default: "上传文件",
  },
  list: {
    type: Array,
    default: [],
  },
  projectId: {
        type    : Number,
        default : 0,
  },
  menuId: {
        type    : Number,
        default : 0,
  },
  stepLink:{
        type    : Object,
        default : {},

  },
  recordId: {
        type    : Number,
        default : 0,
  },
});
const loadding = ref(false)
const listData = ref([])
const getList   = ()=>{
    if(!props.menuId){
        return;
    }
    loadding.value = true;
    if(props.recordId==-1){
      api.project.expansionDocumentList(props.projectId,props.menuId,props.recordId).then(res=>{
        if(res.code==200){
          listData.value = res.data.filter(m=>{
            m.projectDocumentList = m.projectCompanyDocumentList
            m.projectDocumentList.forEach(t=>{
              t.docmentObject=JSON.parse(t.docmentObject)
            })
            return m.projectDocumentList.length>0
          })
        }
        loadding.value = false;
    })
    }else{
      api.project.documentList(props.projectId,props.menuId).then(async res=>{
        if(res.code==200){
          let arr = []
          for(const item of res.data) {
            if(props.stepLink[item.id]){
                    let linkRes = await api.project.documentList(props.projectId,props.stepLink[item.id].stepMenuId);
                    linkRes.data.forEach((linkItem, k) => {
                        if(linkItem.id==props.stepLink[item.id].id){
                          arr.push(linkItem)
                        }
                    });
            }
          }
          listData.value = [...res.data,...arr].filter(m=>{
            m.projectDocumentList.forEach(t=>{
              t.docmentObject=JSON.parse(t.docmentObject)
            })
            return m.projectDocumentList.length>0
          })
        }
        loadding.value = false;
    })
    }
    
}
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
watch(
    () => props.menuId,(newValue, oldValue) => {
        getList();
    }
)
onMounted(() => {
    getList();
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
