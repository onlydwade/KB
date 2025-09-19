
<template>
    <a-tooltip :title="(showName?nameStr:'')" placement="top">
        <div class="user_box" :style="'margin-top:'+(descIn?'-4px':'0')">
            <a-avatar class="avatar" :style="'background-color:'+userData.color" v-if="single" :src="userData.avatar">
                {{firstName(data.realname)}}
            </a-avatar>
            <a-avatar class="avatar" :style="'background-color:'+item.color" v-else v-for="(item,index) in userData" :key="index" :src="item.avatar">
                {{firstName(item.realname)}}
            </a-avatar>
        </div>
    </a-tooltip>
</template>
<script setup>
    const props = defineProps({
        data : {
            type : [Object,Array],
        },
        single : {
            type    : Boolean,
            default : false,
        },
        showName : {
            type    : Boolean,
            default : true,
        },
        descIn : {
            type    : Boolean,
            default : false,
        },
    })
    const userData = computed(()=>{
        if(props.data){
            if(props.single){
                if(props.data.userId) {
                    props.data.color = colorRand(props.data.userId||props.data.id);
                    return props.data;
                }
            }else if(props.data.length > 0) {
                props.data.forEach((item, i) => {
                    item.color = colorRand(item.userId||item.id);
                });
                return props.data;
            }
        }
        return [];
    })
    const nameStr = computed(()=>{
        let str = '';
        if(props.single){
            str = props.data.realname;
        }else{
            props.data.forEach((item, i) => {
                if(i>0){
                    str += ',';
                }
                str += item.realname;
            });
        }
        return str || '无名';
    })
    const firstName = (name)=>{
        if(name){
            return name.slice(-2);
        }else{
            return '无';
        }
    }
    
    const colorList = [
        '#f2870b',
        '#3e79fd',
        '#04d4b6',
        '#04d4b6',
    ]
    const colorRand = (id)=>{
        // const random = Math.floor(Math.random()*colorList.length);
        // return colorList[random];
        id = id || 0;
        return colorList[(id%colorList.length)];
    }
</script>
<style scoped lang="less">
.user_box{
    display     : inline-flex;
    align-items : center;
    .avatar +.avatar{
        margin-left : 4px;
    }
}
</style>
