
<template>
  <FooterBarL style="min-height:72px;">        
      <ProjectEnterOaBtn :temp="workItem" @submit="submit"  @preview="preview" @staging="staging"  :menuInfo="menuInfo"></ProjectEnterOaBtn>
  </FooterBarL>
</template>
<script setup>
import api from '@/api/index';
import { message } from 'ant-design-vue';
import { mainStore } from '@/store';
import { computed } from 'vue';
const store = mainStore();
const props = defineProps({
  title: {
      type: String,
      default: '发起OA审批'
  },
  offLineTitle: {
      type: String,
      default: '已线下审批，标记完成'
  },
  menuInfo: {
      type: Object,
      default: {},
  },
  projectLevel: {
      type: String,
      default: ''
  },
  workItem:{
      type: Object,
      default: {}
  }
})

// const disabled = computed(() => {    
//     return !roleKeys.value.includes(1);
// })
const emit = defineEmits(['submit','preview','staging']);
const submit = (type, temp) => {
  if (type == 0) {
    
  }
  emit('submit', type, temp);
}
const preview = (type, temp) => {
  if (type == 0) {
    
  }
  emit('preview', type, temp);
}
const staging = (type, temp) => {
  if (type == 0) {
    
  }
  emit('staging', type, temp);
}

watch(() => props.projectLevel, (newValue, oldValue) => {

})

onMounted(() => {
})
defineExpose({ 
})
</script>
<style scoped lang="less"></style>
