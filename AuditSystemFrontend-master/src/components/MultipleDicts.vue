
<template>
    <a-select
      :disabled="disabled"
      :getPopupContainer="trigger => trigger.parentNode.parentNode"
      mode="multiple"
      v-model:value="dictArrs"
      class="w_full"
      placeholder="请选择"
      :showSearch="false"
      :options="dict.options(dictKey)">
    </a-select>
</template>
<script setup>
import { useDictStore } from '@/store/dict';
const dict  = useDictStore();
const props = defineProps({
    modelValue : {
        type    : String,
        default : null,
    },
    dictKey:{
        type    : String,
        default : '',
    },
    disabled   : {
        type    : Boolean,
        default : false,
    }
})
const emit     = defineEmits(['update:modelValue']);
const dictArrs = computed({
    get:()=>{
        return  props.modelValue?props.modelValue.split(','):[];
    },
    set : (val) => {
        emit('update:modelValue',val.join(','))    
    }
});
</script>
<style scoped lang="less">

</style>
