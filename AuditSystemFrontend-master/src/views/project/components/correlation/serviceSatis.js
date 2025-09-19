import api                 from '@/api/index';
import { message , Modal } from 'ant-design-vue';
import { deepClone }       from '@/utils/tools';
export function useCorrelation(projectId,key,filter){
    const formRef = ref(null);
    const list    = ref([]);
    const getList = async ()=>{
        let postData = {
            projectId : projectId,
            satisfactionType: 1
        }
        const res  = await api.project.projectServeSatisList(postData);
        if(res.code==200){
            list.value = (res.data || [])
        }else{
            message.success('查询失败');
        }
    }
    const addVisible = ref(false);
    const addSubmit  = ()=>{
        validate(async ()=>{
            if(key=='operation'){
                editData['add'].projectId = projectId;
                editData['add'].satisfactionType = 1;
            }else{
                editData['add'].projectId = projectId;
            }
            if(filter&&filter.id){
                editData['add'][filter.attr] = filter.id;
            }
            let res = await api.project.projectServeSatisAdd(editData['add'],key);
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
        let res = await api.project.projectServeSatisEdit(editData[index],key);
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
                    api.project.projectServeSatisDel(row.id,key).then(res=>{
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