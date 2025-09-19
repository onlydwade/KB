<template>
  <div class="card_box" v-for="(item,index) in listData" :key="index" v-show="item.projectCompanyDocumentList.length">
    <div class="title">
      {{ item.operName }}
    </div>
    <div class="list_box" v-for="(t,idx) in item.projectCompanyDocumentList" :key="idx">
      <a-row>
        <a-col :span="18">
          <div class="name"><EllipsisTooltip :content="t.docmentObject.name " /></div>
          <div class="time">{{ t.createTime }}</div>
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
import { mainStore } from '@/store';
import { useRoute } from 'vue-router'
const route = useRoute()
const store  = mainStore();
const props = defineProps({
  modelValue : {
        type    : String,
        default : null,
    },
    menuId: {
        type    : Number,
        default : 0,
    },
    companyId:{
        type    : Number,
        default : 0,
    },
    recordId:{
        type    : Number,
        default : 0,
    },
    
    type:{
        type    : String,
        default : '',
    },
    readOnly: {
        type    : Boolean,
        default : false,
    },
});
const loadding = ref(false)
const listData = ref([])
const getList   = ()=>{
    loadding.value = true;
    let recordId = route.path =='/companyOaInfo' ? route.query.id:props.recordId
      api.investment.documentList(props.companyId,props.menuId,recordId).then(async res=>{
        if(res.code==200){
          if(res.code==200){
            listData.value = res.data.filter(item=>{
              item.projectCompanyDocumentList.forEach(t=>{
              t.docmentObject=JSON.parse(t.docmentObject)
            })
                return !props.type || item.type == props.type;
            });
        }
        }
        loadding.value = false;
    })
    
    
}
const fileDownload = (data)=>{
    let postData = {
        ossPath : data.ossPath,
        name    : data.name
    }
    api.common.downloadFile(postData).then(res=>{
            dataToFile(res,postData.name);
    })
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
