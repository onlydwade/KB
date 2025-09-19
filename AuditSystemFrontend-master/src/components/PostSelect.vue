
<template>
    <a-select :getPopupContainer="trigger => trigger.parentNode"
      v-model:value="postId"
      class="w_full"
      :disabled="disabled"
      :options="store.postList"
      placeholder="请选择"
      :field-names="{
          label: 'postName',
          value: 'postId',
      }">
    </a-select>
</template>
<script setup>
import api           from '@/api/index';
import { mainStore } from '@/store';
const store = mainStore();

const props = defineProps({
    modelValue : {
        type    : [Number,String],
        default : null,
    },
    disabled   : {
        type    : Boolean,
        default : false,
    }
})
const emit   = defineEmits(['update:modelValue']);
const postId = computed({
    get : () => Number(props.modelValue) || null,
    set : (val) => emit('update:modelValue',val)
})    
</script>
<style scoped lang="less">

</style>
