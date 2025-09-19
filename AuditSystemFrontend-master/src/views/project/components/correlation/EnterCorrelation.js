import api                 from '@/api/index';
import { message , Modal } from 'ant-design-vue';
import { deepClone }       from '@/utils/tools';
export function useCorrelation(projectId,key,filter){
    const formRef = ref(null);
    const list    = ref([]);
  
  
    const getList = async ()=>{
      // const res  = await api.project.correlationList(projectId,key);
      // if(res.code==200){
      //     if(filter&&filter.id){
      //         list.value = (res.data || []).filter(item=>{
      //             return !filter.id || item[filter.attr]==filter.id;
      //         });
      //     }else{
      //         list.value = res.data || [];
      //     }
      // }

   }
    const addVisible = ref(false);
    const addSubmit  = ()=>{
        validate(async ()=>{
            if(key=='projectTeam' || key=='projectTeamOutside'){
                editData['add'].recordId = projectId;
            }else{
                editData['add'].projectId = projectId;
            }
            if(filter&&filter.id){
                editData['add'][filter.attr] = filter.id;
            }
            if(editData['add'].currencyType==2&& editData['add'].remark==null){
              return  message.warning("出资方式选非货币则备注项必须填写");
            }
            list.value.push(editData['add']);
            editData['add']  = {};
            addVisible.value = false;
        })
    }
    const validate = (callBack)=>{
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
        validate(async ()=>{
            if(editData[index].currencyType==2 && (editData[index].remark==null||editData[index].remark=='')){
              return  message.warning("出资方式选非货币则备注项必须填写");
            }
           list.value[index] = editData[index];
           delete editData[index];
        })
    }
    const editCancel = (row,index)=>{
        delete editData[index];
    }

    const del = (row,index)=>{
        if(row.id){
            Modal.confirm({
                title   : '操作确认',
                content : '是否确认删除该条数据？',
                onOk() {
                  list.value.splice(index,1);
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
