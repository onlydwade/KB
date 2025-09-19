import api                         from '@/api/index';
import { message  }                from 'ant-design-vue';
import { formDataFill,dateFormat } from '@/utils/tools';
import { mainStore }               from '@/store';
const store     = mainStore();
export function useMenuTree(companyId,menuInfo,formAttrs,fill){     //formAttrs  空数组保存全部    null 不保存跳过    fill true 根据formAttrs 反显  否则全部
    const formRef         = ref(null);
    const documentsRef    = ref(null);
    const formData        = ref({});
    const refreshMenuTree = inject('refreshMenuTree');
    const roleKeys        = inject('getAutoParams')('roleKeys'); 
    const serviceStatus   = inject('getAutoParams')('serviceStatus'); 
    const expire          = inject('getAutoParams')('expire'); 
    const disabled        = computed(()=>{
        return !roleKeys.value.includes(1) ||  menuInfo.value.status==1 || [1,2,5,8].includes(menuInfo.value.approvalStatus) || ['ZAI_GUAN','YI_ZHONG_ZHI'].includes(serviceStatus.value) || expire.value=='YI_SHI_XIAO';
    })
    const getInfo         = (callBack)=>{
        api.project.projectInfo(companyId).then(res=>{
            if(res.code==200){
                if(fill){
                    res.data && formDataFill(formData.value,formAttrs,res.data);
                }else{
                    formData.value = res.data;
                }
                callBack&&callBack(res.data);
            }
        })
    }
    const save = async (type,temp)=>{
        let postData = {};
        if(formAttrs){
            if(formAttrs.length==0){
                postData = formData.value;
            }else{
                formAttrs.forEach(item => {
                    postData[item] = formData.value[item];
                });
            }
            let res = await api.project.projectEdit(postData);
            if(res.code!=200){
                return;
            }
        }
        switch (type) {
            case -1:
                //重新发起线上OA审批 
                oaStart(type,temp);
            break;
            case 0:
                //线上OA审批 
                oaStart(type,temp);
            break;
            case 1:
                message.success('操作成功！');
                refreshMenuTree(menuInfo.value.id,1,null,8);
            break;
            case 2:
                //提交保存 校验必填 
            case 3:
                //跳过 已填信息保存 改变节点状态  
                message.success('操作成功！');
                refreshMenuTree(menuInfo.value.id,1);
            break;
            case 4:
                //标记未完成
                refreshMenuTree(menuInfo.value.id,0);
            break;
            default:
            //其它 暂存已填信息保存 不改节点状态 
            message.success('操作成功！');
        }
    }
    const submit = (type,temp)=>{
        if(formRef.value&&type!==3&&type!==4){
            formRef.value.validateFields().then(vRes=>{
                offLineReg(type,temp)
            }).catch(err=>{
                message.warning('请完善信息！');
            })
        }else{
            offLineReg(type,temp);
        }
    }
    const offLineReg = (type,temp)=>{
        //线下审批  必传文件校验
        if(type==1&&documentsRef.value){
            let offlineStatus = documentsRef.value.getOfflineStatus();
            if(offlineStatus!='success'){
                message.warning(offlineStatus);
                return;
            }
        }
        save(type,temp);
    }
    
    const bus         = inject('bus');
    const projectName = inject('getAutoParams')('projectName'); 
    const oaStart     = (type,temp)=>{
        let postData = {
            subject        : projectName.value+'-'+menuInfo.value.name,
            recordId       : companyId,
            subRecordId    : menuInfo.value.id,
            templateId     : temp.templateId,
            moduleName     : 'Project',
            approvalStatus : 5,
            stepCode       : menuInfo.value.code,
            mainProcess    : temp.mainProcess,
            detailUrl      : window.location.origin+'/#/projectOaInfo?id='+companyId+'&to='+menuInfo.value.code
        }
        let apiKey = 'oaAdd';
        if(type==-1){
            apiKey      = 'oaUpdate';
            postData.id = temp.oaId;
        }else{
            postData.submitTime   = dateFormat(new Date());
            postData.createTime   = dateFormat(new Date())
            postData.submitDeptId = store.deptPost.deptId || store.userInfo.deptId;
            postData.submitUserId = store.userInfo.userId;
            postData.submitUser = {
                id       : store.userInfo.userId,
                realname : store.userInfo.realname
            };
        }
        api.common[apiKey](postData).then(async res=>{
            if(res.code==200){
                message.success('操作成功！');
                if(temp.mainProcess && menuInfo.value.status==0){
                    await api.project.stepStatusUpdate({
                        companyId      : companyId,
                        stepMenuId     : menuInfo.value.id,
                        approvalStatus : 5,
                    });
                }
                refreshMenuTree();
                bus.emit('oaHasSubmit');
            }
        })
    }
    return {
        formRef,
        documentsRef,
        formData,
        getInfo,
        save,
        submit,
        refreshMenuTree,
        disabled
    }
}