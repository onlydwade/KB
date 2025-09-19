<template>
    <div class="menu_inner">

        <div class="card_box">
            <van-cell-group>
            <div class="title">我方</div>
            <div class="list_box_my" v-for="(item, idx) in mylist" :key="idx">
                <a-row  v-if="item.type == 1">
                    <a-col :span="18">
                    <div class="name">{{ item.company}}</div>
                    </a-col>
                    <a-col :span="6" v-if="item.win">   
                    <div style="float: right;color: #f99c34;;" >
                        {{item.win?'中标':'未中标'}}</div>
                    </a-col> 
                    <a-col :span="6" v-else>
                    <div class="name" style="float: right;" >
                        {{item.win?'中标':'未中标'}}</div>
                    </a-col>   
     
                    <a-col :span="24">
                    <div class="name">投标报价:{{ item.bidingAmount }}(元)</div>
                    </a-col>
                    <a-col :span="12">
                    <div class="name">排名：{{ item.sorts }}</div>
                    </a-col>
                    <a-col :span="12">
                    <div class="name">得分：{{ item.score }}</div>
                    </a-col> 
                </a-row>
            </div>
            <van-divider />
            <div class="title">竞方</div>
            <div class="list_box_my" v-for="(item, idx) in list" :key="idx">
                <a-row  v-if="item.type != 1">
                    <a-col :span="18">
                    <div class="name">{{ item.company}}</div>
                    </a-col>
                    <a-col :span="6" v-if="item.win">   
                    <div style="float: right;color: #f99c34;" >
                        {{item.win?'中标':'未中标'}}</div>
                    </a-col> 
                    <a-col :span="6" v-else>
                        <div class="name" style="float: right;" >
                        {{item.win?'中标':'未中标'}}</div>
                    </a-col>  
                    <a-col :span="24">
                    <div class="name">投标报价:{{ item.bidingAmount }}(元)</div>
                    </a-col>
                    <a-col :span="12">
                    <div class="name">排名：{{ item.sorts }}</div>
                    </a-col>
                    <a-col :span="12">
                    <div class="name">得分：{{ item.score }}</div>
                    </a-col> 
                </a-row>
            </div>
            <van-divider />
        </van-cell-group>    
            <div class="title">中标信息</div>            
            <van-cell-group>
                <van-cell title="未中标/已中标原因" :value="formData.bidedRemark"/>
                <van-cell title="预计服务年限（年）" :value="formData.expectServiceYears" v-if="formData.bidedResult == 'YI_ZHONG_BIAO'"/>
                <van-cell title="投标结果" :value="formData.bidedResult=='WEI_ZHONG_BIAO'?'未中标':'中标'" />
                <van-cell title="中标金额（元）" :value="formData.bidedAmount" v-if="formData.bidedResult == 'YI_ZHONG_BIAO'"/>
            </van-cell-group>    
            <van-cell-group v-if="formData.bidedResult == 'WEI_ZHONG_BIAO'" >                
                <van-cell title="未中标原因" :value="formData.notSelectedReasonStr"/>   
                <a-row v-if="formData.notSelectedReason == 'TOU_BIAO_WU_XIAO'" :gutter="24" >
                    <a-col :span="24">
                        <a-form-item :required="formData.notSelectedReason == 'TOU_BIAO_WU_XIAO'" label="投标无效原因"
                            name="invalidBidReason">
                            <a-checkbox-group disabled v-model:value="formData.invalidBidReason">
                                <a-row  v-for="(item, index) in dict.options('TOU_BIAO_WU_XIAO_YUAN_YIN')" :key="index">
                                    <a-checkbox  v-if="item.value&&formData.invalidBidReason.includes(item.value)"  :value="item.value">{{ item.label }}
                                        <span v-if="item.value=='12'">：{{formData.otherInvalidBidBeason}}</span> 
                                    </a-checkbox>                
                                </a-row>
                            </a-checkbox-group>
                        </a-form-item>
                       
                      
                    </a-col>
                </a-row>            
            </van-cell-group>     
           
        </div>
 
     <div >
        <FileList :menuId="menuId" :projectId="projectId" />
    </div>
    </div>
</template>
<script setup>
import { useMenuTree } from './menu';
// import { SHIFOUFormat, parseFormatNum, dateFormat, getNodeById } from '@/utils/tools';
import api from '@/api/index';
import { amountUnit } from '@/utils/tools';
import { useDictStore } from '@/store/dict';
const dict = useDictStore();
const props = defineProps({
    projectId: {
        type: Number,
        default: 0,
    },
    menuId: {
        type: Number,
        default: 0,
    },
    menuInfo: {
        type: Object,
        default: {},
    }
})

const formAttrs = ['id', 'bidedResult', 'expectServiceYears' , 'bidedAmount', 'bidedRemark', 'serviceStatus', 'notSelectedReason','notSelectedReasonStr', 'invalidBidReason', 'otherInvalidBidBeason'];
const {
    formRef,
    documentsRef,
    formData,
    getInfo,
    submit,
    save,
    disabled
} = useMenuTree(props.projectId, toRefs(props).menuInfo, formAttrs, true);
onMounted(() => {
    getInfo((res) => {
        formData.value.invalidBidReason = res.invalidBidReason?.split(',') || [];
    });

    getList();
})
watch(() => [formData.value.bidedResult], () => {
    if (formData.value.bidedResult !== 'WEI_ZHONG_BIAO') {
        formData.value.notSelectedReason = null
        formData.value.invalidBidReason = []
        formData.value.otherInvalidBidBeason = ''
    }
}, { deep: true })

const list    = ref([]);
const mylist    = ref([]);
const getList = async ()=>{
        const res  = await api.project.correlationList(props.projectId, 'projectBid');
        if(res.code==200){  
            if(res.data ){
                res.data.forEach((item) => { 
                if(item.type==1){
                    mylist.value.push(item)
                }else{
                    list.value.push(item)
                }
             });         
            }
        }
    }
 
</script>
<style scoped lang="less">

.card_box {
  margin: 20px 0;
  padding: 10px;
}
.title {
  color: #000;
  font-weight: bold;
  line-height: 40px;
}

.list_box_my {
  // border: 1px solid #f0f2f5;
//   background: #fffaf0;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 8px;
.item_my{
    background: #f99c34;
}
  .name {
    font-size: 15px;
  }

  .simple {
    line-height: 30px;
    color: #969799;
  }
}


.list_box {
  // border: 1px solid #f0f2f5;
//   background: #fffaf0;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 8px;
.item_my{
    background: #f99c34;
}
  .name {
    font-size: 15px;
  }

  .simple {
    line-height: 30px;
    color: #969799;
  }
}

.menu_inner {
    :v-deep .van-cell__value {
        color: #000;
    }

    :v-deep .van-cell-group__title {
        color: #000;
        font-weight: bold;
    }

    .card_box {
        margin-bottom: 20px;
    }

    .bm {
        margin: 10px;
    }
}</style>