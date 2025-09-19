import api                 from '@/api/index';
import { message , Modal } from 'ant-design-vue';
import { deepClone }       from '@/utils/tools';
export function useCorrelation(projectId,key,filter){
    const formRef = ref(null);
    const list    = ref([]);
    const getList = async ()=>{
        const res  = await api.project.correlationList(projectId,key);
        if(res.code==200){
            if(filter&&filter.id){
                list.value = (res.data || []).filter(item=>{
                    return !filter.id || item[filter.attr]==filter.id;
                });
            }else{
                list.value = res.data || [];
            }
        }
    }
    const addVisible = ref(false);
    const addSubmit  = ()=>{
        validate(async ()=>{
            if(key=='projectTeam'){
                editData['add'].recordId = projectId;
            }else{
                editData['add'].projectId = projectId;
            }
            if(filter&&filter.id){
                editData['add'][filter.attr] = filter.id;
            }
            let res = await api.project.correlationAdd(editData['add'],key);
            if(res.code==200){
                message.success('操作成功');
            }else{
                return;
            }
            getList();
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
        let res = await api.project.correlationEdit(editData[index],key);
        if(res.code==200){
            message.success('操作成功');
            getList();
            delete editData[index];
        }
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
                    api.project.correlationDel(row.id,key).then(res=>{
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