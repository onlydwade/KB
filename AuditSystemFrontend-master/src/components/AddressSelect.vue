
<template>
    <a-cascader :getPopupContainer="trigger => trigger.parentNode.parentNode.parentNode.parentNode.parentNode"
    v-model:value="addressIds" 
    :options="cityStore.getCity(mode)" 
    autoComplete="off"
    :change-on-select="single"
    :show-search="{ addressFilter }"
    @change="addressChange" 
    class="w_full"
    :fieldNames = "{
        label: 'name',
        value: 'areaCode',
        children: 'subAreaList'
    }"
    :placeholder="placeholder" />
</template>
<script setup>
import api              from '@/api/index';
import { useCityStore } from '@/store/city';
const cityStore = useCityStore();
const props     = defineProps({
    provinceCode : {
        type    : [Number,String],
        default : '',
    },
    provinceName : {
        type    : String,
        default : null,
    },
    cityCode : {
        type    : [Number,String],
        default : '',
    },
    cityName : {
        type    : String,
        default : null,
    },
    areaCode : {
        type    : [Number,String],
        default : '',
    },
    mode:{
        type    : String,
        default : 'area',
    },
    single:{
        type:   Boolean,
        default: false
    }
})
const placeholder = computed(()=>{
    return '请选择' + (props.mode=='area'?'省市区':props.mode=='city'?'省市':'省')
})
const emit       = defineEmits(['update:provinceCode','update:cityCode','update:areaCode','update:provinceName','update:cityName','change']);
const addressIds = computed({
    get : () => {
      
        if(!props.single) {
          let ids = []
          if(props.provinceCode){
              ids = [Number(props.provinceCode)];
              if((props.mode=='city' || props.mode=='area')&&props.cityCode){
                  ids.push(props.cityCode);
                  if(props.mode=='area'&&props.areaCode){
                      ids.push(props.areaCode);
                  }
              }
          }
          return ids;
        }
       
    },
    set : (val) => {}
})
onMounted(() => {
    cityStore.geTreeData(props.mode=='area'?'area':'city');
});

const addressChange = (val,selectedOptions)=>{
  if(props.single){
    addressIds.value = val
    if(val&&val.length==1){
      emit('update:provinceCode',val[0]);
    }else if(val.length==2){
      emit('update:cityCode',val[1])
    }else if(val.length==3){
      emit('update:areaCode',val[2]);
    }else{
          emit('update:provinceCode','');
          emit('update:cityCode','');
          emit('update:areaCode','');
          emit('change','');
    }
  }else{
    if(val&&val.length>0){
        emit('update:provinceCode',val[0]);
        emit('update:provinceName',selectedOptions[0].name);
        if(val.length>1){
            emit('update:cityCode',val[1]);
            emit('update:cityName',selectedOptions[1].name);
        }
        if(props.mode=='area'&&val.length>2){
            emit('update:areaCode',val[2]);
            emit('change',val[2]);
        }
    }else{
        emit('update:provinceCode','');
        emit('update:provinceName','');
        emit('update:cityCode','');
        emit('update:cityName','');
        if(props.mode=='area'){
            emit('update:areaCode','');
            emit('change','');
        }
    }
  }
}
const addressFilter = (inputValue, path) => {
    return path.some(option => option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1);
}
</script>
<style scoped lang="less">
</style>
