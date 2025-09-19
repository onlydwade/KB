<template>
    <div class="tag_box">
        <template v-for="(tag,index) in keyArr" :key="tag">
            <a-tooltip v-if="tag.length > 10" :title="tag">
                <a-tag  size="large" class="tags_item" color="orange" :closable="!readOnly"  @close="handleClose(index)">
                    {{ `${tag.slice(0, 10)}...` }}
                </a-tag>
            </a-tooltip>
            <a-tag v-else size="large" class="tags_item" color="orange" :closable="!readOnly"  @close="handleClose(index)">
                {{ tag }}
            </a-tag>
        </template>
        <a-select :getPopupContainer="trigger => trigger.parentNode"
            ref="inputRef"
            class="tags_input"
            v-model:value="inputValue"
            v-if="inputVisible"
            mode="tags"
            defaultOpen
            autofocus
            placeholder="可快速选择或自定义输入"
            :options="options"
            @change="handleInputConfirm">
        </a-select>
        <a-button v-else @click="showInput"  class="tags_btn" type="dashed" danger v-if="!readOnly">
            +添加
        </a-button>
    </div>
</template>
<script setup>
    import api       from '@/api/index';
    import { watch } from "vue";
    const emit  = defineEmits(['update:modelValue','change']);
    const props = defineProps({
        modelValue      : {
            type    : String,
            default : '',
        },
        valType      : {
            type    : String,
            default : 'string',
        },
        readOnly:{
            type    : Boolean,
            default : false,
        },
        autoAdd:{
            type    : Boolean,
            default : false,
        },
        type:{
            type    : Number,
            default : 1,        //1线索  2商机   3项目  4客户
        }
    })
    const inputVisible = ref(false);
    const inputRef     = ref(null);
    const inputValue   = ref([]);
    const keyArr = computed({
        get : () =>props.valType=='string'?props.modelValue?props.modelValue.split(','):[]:(props.modelValue || []),
        set : (val) => {
            valChange(val)
        }
    })
    
    const handleClose = (index) => {
        keyArr.value.splice(index, 1);
        valChange(keyArr.value);
    }
    const showInput  = () => {
        getOptions();
        inputVisible.value = true;
        nextTick(() => {
            inputRef.value.focus();
        })
    }
    const handleInputConfirm = () => {
        let str = inputValue.value.toString();
        if (str.length>0 && !keyArr.value.includes(str)) {
            keyArr.value = keyArr.value.concat(str);
            if(props.autoAdd){
                addKeyWord(str,options.value.length+1);
            }
        }
        inputVisible.value = false
        inputValue.value   = []
    }
    const addKeyWord = (str,sorts)=>{
        let postData = {
            keywordContent : str,
            keywordType    : props.type,
            sorts          : sorts
        }
        api.common.keywordsAdd(postData).then(res=>{
            if(res.code==200){
                options.value.push({
                    value:str
                })
            }
        })
    }
    const valChange = (val)=>{
        let newVal = props.valType=='string'?val.join(','):val;
        emit('update:modelValue',newVal);
        emit('change',newVal);
    }
    const options    = ref([]);
    const getOptions = ()=>{
        if(options.value.length==0){
            api.common.keywords(props.type).then(res=>{
                if(res.code==200){
                    options.value = (res.data || []).map(item=>{
                        return {
                            value : item.keywordContent
                        }
                    });
                }
            })
        }
    }
</script>
<style scoped lang="less">
.tag_box{
    display       : flex;
    flex-wrap     : wrap;
    align-items   : center;
    margin-bottom : -8px;
    flex          : 1;
}
.tags_item{
    margin-right  : 8px;
    margin-bottom : 8px;
    line-height   : 30px;
    :deep(.anticon-close){
        font-size: 14px;
    }
}
.tags_btn{
    margin-right  : 8px;
    margin-bottom : 8px;
}
.tags_input{
    margin-right  : 8px;
    margin-bottom : 8px;
    min-width     : 200px;
    max-width     : 250px;
}
</style>
