
<template>
    <div class="menu_inner" v-if="companyList">
        <CompanyForm @success="saveSuccess" :projectId="projectId" ref="companyFormRef" v-if="companyList.length==0" :disabled="disabled" 
        :data="formData" :projectType="projectType"/>
        <div class="padding_box"  v-else>
            <a-row :gutter="[24,24]" justify="start" wrap>
                <a-col :xxl="6" :lg="8" :sm="12" v-if="!disabled&&companyList.length<1">
                    <div class="cj_item cj_add" @click="edit({},-1)">
                        <plus-circle-outlined style="margin-bottom:8px;font-size:32px"/>
                        添加其它企业
                    </div>
                </a-col>
                <a-col :xxl="6" :lg="8" :sm="12" v-for="(item,index) in companyList" :key="index">
                    <div class="cj_item">
                        <div class="cj_header">
                            <span class="version color-primary">{{item.name}}</span>
                        </div>
                        <div class="cj_body">
                            <div class="cj_more color-text">
                                {{item.companyNo || '-'}}
                            </div>
                            <div class="cj_more">
                                创建于 {{dateFormat(item.createTime,'YYYY-MM-DD HH:mm')}}
                            </div>
                        </div>
                        <div class="cj_footer" v-if="!disabled">
                            <span class="btn" @click="del(item,index)">删除</span>
                            <a-divider type="vertical" />
                            <span class="btn"  @click="edit(item,index)">编辑</span>
                        </div>
                        <div class="cj_footer" v-else>
                            <span class="btn"  @click="edit(item,-2)">查看</span>
                        </div>
                    </div>
                </a-col>
            </a-row>
        </div>
        <FooterBarL v-if="companyList.length==0">
            <a-button size="large" type="primary" @click="save">提交保存</a-button>
        </FooterBarL>
        <ProjectActions v-else @submit="submit" :menuInfo="menuInfo">
            <a-button size="large" @click="submit(2)" v-if="menuInfo.oaApproval!=1&&menuInfo.status==0&&[0,9].includes(menuInfo.approvalStatus)&&!disabled">标记完成</a-button>
        </ProjectActions>

        <a-drawer
            width="80%"
            :title="'工商企业信息'+(companyId==0?'添加':handleIndex==-2?'查看':'编辑')"
            destroyOnClose
            @close="handleClose"
            :visible="visible">
            <template #extra>
                <a-space :size="16" v-if="handleIndex==-2">
                    <a-button size="large" @click="handleClose">关闭</a-button>
                </a-space>
                <a-space :size="16" v-else>
                    <a-button size="large" @click="handleClose">取消</a-button>
                    <a-button size="large" type="primary" @click="handleOk" :disabled="disabled">提交保存</a-button>
                </a-space>
            </template>
            <CompanyForm style="margin:-24px;" :disabled="disabled" v-if="visible" @success="saveSuccess2" :projectId="projectId" ref="companyFormRef2" :companyId="companyId" :projectType="projectType" :visible="visible"/>
        </a-drawer>
    </div>
</template>
<script setup>
import api                 from '@/api/index';
import { useMenuTree }     from './menu';
import { message , Modal } from 'ant-design-vue';
import CompanyForm         from '../correlation/CompanyForm.vue'
const props = defineProps({
    projectId: {
        type    : Number,
        default : 0,
    },
    menuId:{
        type    : Number,
        default : 0,
    },
    menuInfo:{
        type    : Object,
        default : {},
    },
    projectType:{
      type    : String,
      default : '',
    },
})
const {
    submit,
    disabled,
    formData
} = useMenuTree(props.projectId,toRefs(props).menuInfo);

const companyFormRef = ref(null);
const save           = ()=>{
    companyFormRef.value.submit();
}
const saveSuccess = ()=>{
    // submit(3);
    getList();
}

const companyList = ref(null);
const getList = ()=>{
    api.project.correlationList(props.projectId,'projectCompany').then(res=>{
        if(res.code==200){
            companyList.value = res.data || [];
        }
    })
}
onMounted(() => {
    getList();
})


const visible         = ref(false);
const handleIndex     = ref(0);
const companyId       = ref(0);
const companyFormRef2 = ref(null);
const handleClose     = ()=>{
    visible.value = false;
}
const edit = (data,index)=>{
    handleIndex.value = index;
    companyId.value   = data.id || 0;
    visible.value     = true;
}
const handleOk = ()=>{
    companyFormRef2.value.submit();
}
const saveSuccess2 = ()=>{
    getList();
    visible.value     = false;
}
const del = (data,index)=>{
    Modal.confirm({
        title   : '操作确认',
        content : '是否确认删除该条企业工商信息？',
        onOk() {
            api.project.correlationDel(data.id,'projectCompany').then(res=>{
                if(res.code==200){
                    companyList.value.splice(index,1);
                    if(companyList.value.length==0){
                        // submit(4);
                    }
                }
            })
        }
    });
}
</script>
<style scoped lang="less">
.menu_inner{
    // background-color : #f5f5f5;
    // flex             : 1;
}
.cj_item{
    border         : 1px solid #ddd;
    border-radius  : 8px;
    height         : 100%;
    display        : flex;
    flex-direction : column;
    &:hover{
        box-shadow: 0 0 8px rgba(0,0,0,0.1);
    }
    .cj_header{
        display     : flex;
        align-items : center;
        padding: 16px;
        padding-bottom: 0;
        .version{
            font-size: 16px;
            flex: 1;
        }
    }
    .cj_body{
        padding : 8px 16px;
        flex    : 1;
    }
    .cj_more{
        display         : flex;
        align-items     : center;
        justify-content : space-between;
        color: @text-color-secondary;
        padding: 8px 0;
    }
    .cj_footer{
        display         : flex;
        align-items     : center;
        justify-content : space-around;
        border-top      : 1px solid #ddd;
        .btn{
            line-height : 40px;
            cursor      : pointer;
            text-align  : center;
            flex        : 1;
            font-size   : 16px;
            color       : @primary-color;

            &:hover{
                background-color: #f0f2f5;
            }
        }
        .btn_disabled{
            color  : @text-color-secondary;
            cursor : not-allowed;
            opacity: 0.6;
            &:hover{
                background-color: #fff;
            }
        }
    }
}
.cj_add{
    display         : flex;
    justify-content : center;
    align-items     : center;
    flex-direction  : column;
    font-size       : 18px;
    min-height      : 160px;
    cursor          : pointer;

    &:hover{
        color: @primary-color;
    }
}
</style>
