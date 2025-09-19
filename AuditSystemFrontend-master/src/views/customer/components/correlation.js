import api                 from '@/api/index';
import { message , Modal } from 'ant-design-vue';
import { deepClone }       from '@/utils/tools';
export function useCorrelation(customerId,key,filter){
    const formRef = ref(null);
    const list = ref([]);
    const getList = async ()=>{
        if(customerId!=null&& customerId!=0 ){
            const res  = await api.customer.customerContactsGet(customerId,key);
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

    }
 


    const addVisible = ref(false);
    const addSubmit  = ()=>{
        validate(async ()=>{
            list.value.push(editData['add'] );
            addCancel()
        },'add')
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
        message.success('操作成功');
        list.value.splice(index,1,editData[index]);
        editCancel (row,index)
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
