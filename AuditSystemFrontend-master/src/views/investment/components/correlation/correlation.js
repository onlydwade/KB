import api                 from '@/api/index';
import { message , Modal } from 'ant-design-vue';
import { deepClone }       from '@/utils/tools';
export function useCorrelation(companyId,key,filter,menuId,local,repeatKeys){  //local  不提交
    const formRef = ref(null);
    const list    = ref([]);
    const getList = async ()=>{
        
        let postData = {
            pageNo   : 1,
            pageSize : 1000,
            params   : {
                companyId : companyId
            }
        }
        if(filter&&filter.id){
            postData.params[filter.attr]=filter.id;
        }
        const res  = await api.investment.correlationPage(postData,key,menuId);
        if(res.code==200){
            list.value = res.data.records || [];
        }
        // if(key=='projectCompanyItem'){
        //     postData.params = {};
        //     postData.params.sonCompanyId = companyId;
        //     const resAdd  = await api.investment.correlationPage(postData,key,menuId);
        //     if(resAdd.code==200){
        //         let arr = (resAdd.data.records || []).map(item=>({
        //             id               : item.id,
        //             parent           : true,
        //             relevanceTypeStr : (item.relevanceType=='ZI_GONG_SI'?'父级公司':'祖级公司'),
        //             sonCompanyId     : item.companyId,
        //             sonCompanyName   : item.companyName,
        //         }))
        //         list.value = list.value.concat(arr);
        //     }
        // }
    }
    const addVisible = ref(false);
    const addSubmit  = ()=>{
        validate(async ()=>{
            editData['add'].companyId = companyId;
            if(filter&&filter.id){
                editData['add'][filter.attr] = filter.id;
            }
            if(local){
                list.value.push(editData['add']);
            }else{
                let res = await api.investment.correlationAdd(editData['add'],key);
                if(res.code==200){
                    message.success('操作成功');
                }else{
                    return;
                }
                getList();
            }
            editData['add']  = {};
            addVisible.value = false;
        },'add')
    }
    const validate = (callBack,key)=>{
        let repeatTip = '';
        if(editData[key].currencyType==2&& editData[key].remark==null){
          return  message.warning("出资方式选非货币则备注项必须填写");
        }
        if(repeatKeys&&repeatKeys.length>0){
            for (var i = 0; i < repeatKeys.length; i++) {
                let rKey = repeatKeys[i].key;
                if(list.value.some((item,k) => editData[key][rKey] && item[rKey] == editData[key][rKey] && k!=key)){
                    repeatTip = repeatKeys[i].name +'数据重复，请检查！'
                    break;
                }
            }
        }
        if(repeatTip){
            message.warning(repeatTip);
            return;
        }

        let rateSum = 0
        if(key === 'add'){
            list.value.forEach(e => {
                rateSum = rateSum + e.rate
            });
            
        }else{
            for(let i=0;i<list.value.length;i++){
                if(i != key){
                    rateSum = rateSum + list.value[i].rate
                }
            }
        }
        rateSum = rateSum + editData[key].rate
        if(rateSum>100){
            message.warning("持股比例总和不能超过100");
            return;
        }

        formRef.value.validateFields().then(vRes=>{
            callBack && callBack();
        }).catch(err=>{
            message.warning('请完善信息！');
        })
    }
    const addCancel = ()=>{
        editData['add']  = {};
        addVisible.value = false;
    }
    const editData = reactive({
        'add' : {},
    })
    const edit = (row,index)=>{
        editData[index] = JSON.parse(JSON.stringify(row));
    }
    const editSubmit = async (row,index)=>{
      if(editData[index].currencyType==2 && (editData[index].remark==null||editData[index].remark=='')){
        return  message.warning("出资方式选非货币则备注项必须填写");
      }
        validate(async ()=>{
            if(local){
                list.value[index] = JSON.parse(JSON.stringify(editData[index]));
            }else{
                let res = await api.investment.correlationEdit(editData[index],key);
                if(res.code==200){
                    message.success('操作成功');
                    getList();
                }else{
                    return;
                }
            }
            delete editData[index];
        },index)
    }
    const editCancel = (row,index)=>{
        delete editData[index];
    }
    
    const delRecordFile = (fileList)=>{
        return new Promise(function(resolve, reject) {
            let promises = [];
            fileList.forEach((item, i) => {
                item.projectCompanyDocumentList.forEach((file, k) => {
                    let delFun  = api.investment.documentDel(file.id)
                    promises.push(delFun)
                });
            });
            Promise.all(promises).then(posts=>{
                resolve('del_success');
            }).catch(reason=>{
                reject('del_filed');
            });
        });
    }
    const del = (row,index)=>{
        if(row.id){
            Modal.confirm({
                title   : '操作确认',
                content : '是否确认删除该条数据？',
                async onOk() {
                    if(row.documentTemplateList&&row.documentTemplateList.length>0){
                        await delRecordFile(row.documentTemplateList);
                    }
                    api.investment.correlationDel(row.id,key).then(res=>{
                        if(res.code==200){
                            list.value.splice(index,1);
                        }
                    })
                }
            });
        }else{
            list.value.splice(index,1);
        }
    }
    const clone = (row,index)=>{
        editData.add     = deepClone(row);
        delete editData.add.id;
        addVisible.value = true;
    }
    return {
        formRef,
        list,
        getList,
        addVisible,
        addSubmit,
        addCancel,
        editData,
        edit,
        editSubmit,
        editCancel,
        del,
        clone
    }
}
