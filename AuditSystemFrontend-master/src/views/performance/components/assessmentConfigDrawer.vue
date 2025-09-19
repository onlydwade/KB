<template>
  <div>
    <a-drawer
      v-model:visible="visible"
      :width="800"
      class="custom-class"
      title="考核指标配置"
      placement="right"
      @close="handleClose"
    >
      <template #extra>
        <a-space :size="16">
          <a-button size="large" @click="handleClose">取消</a-button>
          <a-button size="large" type="primary" @click="handleOk" :loading="submitLoading">
            确认
          </a-button>
        </a-space>
      </template>
      <setConfigList ref="setConfigListRef" :list="list" @submitData="getUpdate"/>
    </a-drawer>
  </div>
</template>
<script setup>
import api from "@/api/index";
import setConfigList      from './setConfigList.vue';
const emit  = defineEmits(['success'])
    const visible = ref(false);
    const list = ref([])
    const handleClose = () =>{
      visible.value= false
    }
    const getUpdate = (data) =>{
      list.value = data;
    }
    const handleOk = () =>{
      api.performance.setItemConfig(list.value).then(res=>{
        visible.value= false;
        emit('success');
        // location.reload();
      })
      console.log(list);
    }
    const open = () =>{
       visible.value= true
       api.performance.getItemConfig().then(res=>{
        if(res.code==200 && res.data){
            list.value = res.data;
        }
    })
    }
    defineExpose({open})
</script>
