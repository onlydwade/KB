<template>
    <a-textarea allowClear :rows="3" type="textarea" v-model:value="addressStr" placeholder="粘贴地址，智能识别，如不准确请手动填写" />
</template>
<script setup>
import api              from '@/api/index';
import { throttle }     from '@/utils/tools';
import { useCityStore } from '@/store/city';
const cityStore = useCityStore();
const props     = defineProps({
    type:{
        type    : String,
        default : 'area'
    },
    provinceCode : {
        type    : [Number,String],
        default : null,
    },
    cityCode : {
        type    : [Number,String],
        default : null,
    },
    areaCode : {
        type    : [Number,String],
        default : null,
    },
    streetCode : {
        type    : [Number,String],
        default : null,
    },
    streetZipcode : {
        type    : [Number,String],
        default : null,
    },
    address : {
        type    : String,
        default : null,
    },
})
const emit       = defineEmits(['update:provinceCode','update:cityCode','update:areaCode','update:streetCode','update:streetZipcode','update:address','change']);
const addressStr = ref('');
const tree       = computed(()=>{
    return cityStore.getCity(props.type);
})
watch(addressStr,(newVal,oldVal)=>{
    AiAddress(newVal);
})
const AiAddress = (str)=>{
    const reg = /.+?(省|市|自治区|自治州|县|区)/g;
    let arr   = str.match(reg) || [];
    if(arr.length==0){
        return;
    }
    //重复去重 比如xx区 xx社区  取第一个
    let lastWord = '';
    for (let i = 0, len = arr.length; i < len; i++) { 
        if(arr[i]){
            let l = arr[i].substr(-1);
            if(lastWord.indexOf(l)==-1){
                lastWord += l;
            }else{
                arr.splice(i, 1);
                i--;
            }
        }
    }
    let moreAddress = str.replace(arr.join(''),'');
    let ads   = {};
    function doArr(list,paths){
        for (var i = 0; i < list.length; i++) {
            let matchIndex = -1;
            for (var k = 0; k < arr.length; k++) {
                if(list[i].name==arr[k]){
                    matchIndex = k;
                    break;
                }
            }
            if(list[i].subAreaList&&list[i].subAreaList.length>0){
                let newPath = [...paths,i];
                doArr(list[i].subAreaList,newPath);
            }
            if(matchIndex>-1){
                if(paths.length>0){
                    getParentByindex(tree.value,paths,0);
                }
                ads[list[i].level] = list[i].areaCode;
                arr.splice(0,(matchIndex+1));
                break;
            }
        }
    }
    function doArrDesc(list,index){
        for (var i = 0; i < list.length; i++) {
            if(list[i].name==arr[index]){
                ads[index] = list[i].areaCode;
                doArrDesc(list[i].subAreaList,index+1);
                break;
            }
        }
    }
    function getParentByindex(areaList,paths,index){
        if(index<paths.length){
            const pIndex = paths[index];
            ads[index]   = areaList[pIndex].areaCode;
            getParentByindex(areaList[pIndex].subAreaList || [],paths,index+1);
        }
    }
    if(arr.length<3){
        doArr(tree.value,[]);
    }else{
        doArrDesc(tree.value,0);
    }
    
    
    codeEntry(ads,moreAddress);
}
const codeEntry = (ads,moreAddress)=>{
    
    if(!ads[0]){
        return;
    }
    if(props.provinceCode!=ads[0]){
        emit('update:provinceCode',ads[0]);
        emit('update:streetCode',null);
        emit('update:streetZipcode',null);
    }
    if(!ads[1]){
        emit('update:cityCode',null);
        return;
    }
    if(props.cityCode!=ads[1]){
        emit('update:cityCode',ads[1]);
        emit('update:streetCode',null);
        emit('update:streetZipcode',null);
    }
    if(!ads[2]){
        emit('update:areaCode',null);
        return;
    }
    if(props.areaCode!=ads[2]){
        emit('update:areaCode',ads[2]);
        emit('change',ads[2]);
        if(moreAddress){
            getStreet(ads[2],moreAddress)
        }
    }
}


const streetList = ref([]);
const getStreet  = (areaCode,moreAddress)=>{
    const reg = /.+?(街道办|街道|镇)/g;
    let arr   = moreAddress.match(reg) || [];
    if(arr.length==0){
        return;
    }
    api.common.listStreetByArea(areaCode).then(res=>{
        if(res.code==200){
            let hasStreet = false;
            res.data.forEach((item, i) => {
                if(item.name.indexOf(arr[0])>-1){
                    hasStreet = true;
                    emit('update:streetCode',item.areaCode);
                    emit('update:streetZipcode',item.zipCode);
                }
            });
            if(hasStreet){
                let address = moreAddress.replace(arr.join(''),'');
                emit('update:address',address);
            }else{
                emit('update:address',moreAddress);
            }
        }
    })
}
</script>
<style scoped lang="less">

</style>
