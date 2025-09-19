import api                 from '@/api/index';
import { message , Modal } from 'ant-design-vue';
import { deepClone }       from '@/utils/tools';
export function useCorrelation(customerId,key,filter){
    const formRef = ref(null);
    const list = ref([]);
    const proOptions = ref([])
    const summaryOptions = ref([])
    const columns = ref([
        {
            title: '重点客户',
            dataIndex: 'customerName',
            required: true,
            editType: 'select',
            key: 'customerName',
        },    
        {
            title: '任务摘要',
            dataIndex: 'summary',
            required: true,
            editType: 'select',
            key:'summary'
        },
        {
            title: '项目类型',
            dataIndex: 'projectType',
            required: true,
            editType: 'select',
            key: 'projectType'
        },
        {
            title: '负责人',
            dataIndex: 'visitUserName',
            required: true,
            editType: 'text'
        },
        {
            title: '专班建立',
            dataIndex: 'teamsBuild',
            required: true,
            editType: 'text'
        },
        {
            title: '状态跟进',
            dataIndex: 'followStatus',
            required: true,
            editType: 'text'
        },

    ])
    const getList = async ()=>{
        const res  = await api.workBrief.keyProjectGetList();
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
        columns.value[0].editType = 'text';
        columns.value[1].editType = 'text';
        editData[index] = JSON.parse(JSON.stringify(row));
    }
    const moveUp = (row,index)=>{        
        if(index == 0){
           return;
        }
        let upJson = JSON.parse(JSON.stringify(list.value[index-1]));
        let downJson = JSON.parse(JSON.stringify(row));
        
        list.value[index - 1] = downJson;
        list.value[index] = upJson;
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
        moveUp,
        editSubmit,
        editCancel,
        del,
        clone,
        proOptions,
        summaryOptions,
        columns
    }

}
