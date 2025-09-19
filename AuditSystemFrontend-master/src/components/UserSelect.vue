
<template>
    <a-select
      v-model:value="userIds"
      :mode="mode"
      class="w_full"
      allowClear
      :placeholder="'请'+(dataSource=='all'?'搜索':'')+'选择'"
      showSearch
      :filterOption="dataSource!='all'"
      optionFilterProp="label"
      :options="options"
      @change="change"
      @search="fetchUser">
      <template #option="item">
          <div class="user_item">
              {{item.label}} 
              <span class="color-info" style="font-size:12px;font-weight:normal">
                  {{item.deptName}}
              </span>
          </div>
      </template>
      <template #notFoundContent>
            <a-spin v-if="fetching" size="small" />
            <div class="empty" v-else>
                {{dataSource=='all'?'按姓名搜索后选择':'无数据'}}
            </div>
      </template>
    </a-select>
</template>
<script setup>
import api from '@/api/index';
const props = defineProps({
    modelValue : {
        type    : [Number,Array],
        default : null,
    },
    mode       : {
        type    : String,
        default : '',
    },
    users:{
        type    : [Object,Array],
        default : [],
    },
    dataSource:{
        type    : String,
        default : 'all',
    }
})
const emit    = defineEmits(['update:modelValue','change']);
const userIds = computed({
    get : () => props.modelValue || (props.mode?[]:null),
    set : (val) => {
        emit('update:modelValue',val)
    }
})
const options   = ref([]);
const fetching  = ref(false);
const timerFun  = ref(null);
const fetchUser = (name)=>{
    window.clearTimeout(timerFun.value);
    if(name){
        timerFun.value = setTimeout(()=>{
           searchUser(name);
       },500)
    }
}
const searchUser = (name)=>{
    fetching.value = true;
    optionsDefault();
    let postData = {
        pageNo        : 1,
        pageSize      : 500,
        content       : name,
        contentColumn : 'realname',
        params        : {}
    }
    api.sys.userPage(postData).then(res=>{
        if(res.code==200){
            (res.data.records || []).forEach((item, i) => {
                let user = {
                    label    : item.realname,
                    value    : item.userId,
                    deptId   : item.deptId,
                    deptName : item.deptName,
                    companyName : item.companyName
                }
                if(!options.value.includes(user)){
                    options.value.push(user);
                }
            });
            fetching.value = false;
        }
    })
}
watch(() => props.users,
    (newVal, oldVal) => {
        optionsDefault();
    },
    {deep: true}
)
const optionsDefault = ()=>{
    if(!props.mode){
        if(props.users&&props.users.userId){
            options.value = [{
                label : props.users.realname,
                value : props.users.userId,
            }]
        }else{
            options.value = []
        }
    }else{
        options.value = (props.users || []).map(item=>{
            return {
                label : item.realname,
                value : item.userId
            }
        })
    }
}

const change = (val,option)=>{
    emit('change',option)
}
onMounted(() => {
    optionsDefault();
});
    
</script>
<style scoped lang="less">
.user_box{
    display     : inline-flex;
    align-items : center;
    .avatar +.avatar{
        margin-left : 4px;
    }
}
.empty{
    text-align:center;
}
.user_item{
    display: flex;
    justify-content:space-between;
    flex-wrap: wrap;
}
</style>
