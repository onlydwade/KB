
<template>
    <a-select :getPopupContainer="trigger => trigger.parentNode"
    v-model:value="tags"
    :mode="mode"
    class="w_full"
    :maxTagCount="maxTagCount"
    :disabled="disabled"
    :placeholder="mode=='tags'?'请输入添加，可添加多个':'请选择，可多选'"
    :options="options">
    </a-select>
</template>
<script setup>
import api           from '@/api/index';
import { mainStore } from '@/store';
const store = mainStore();

const props = defineProps({
    modelValue : {
        type    : String,
        default : '',
    },
    disabled   : {
        type    : Boolean,
        default : false,
    },
    mode:{
        type    : String,
        default : 'tags',
    },
    maxTagCount:{
        type    : Number,
        default : null,
    },
    options:{
        type    : Array,
        default : [],
    }
})
const emit   = defineEmits(['update:modelValue']);
const tags = computed({
    get : () => {
        if(props.modelValue){
            return props.modelValue.split(',');
        }else{
            return [];
        }
    },
    set : (val) => {
        emit('update:modelValue',val.join(','))
    }
})
</script>
<style scoped lang="less">

</style>
