import api                 from '@/api/index';
import { message , Modal } from 'ant-design-vue';
import { deepClone }       from '@/utils/tools';
export function useCorrelation(customerId,key,filter){
    const formRef = ref(null);
    const list = ref([]);
    const proOptions = ref([])
    const columns = ref([
        {
            title: '承接项目',
            dataIndex: 'projectName',
            required: true,
            editType: 'text'
        },    
        {
            title: '归属单位',
            dataIndex: 'deptName',
            required: true,
            editType: 'text'
        },
        {
            title: '合同金额(万)',
            dataIndex: 'contractAmount',
            required: true,
            editType: 'text'
        },
        {
            title: '服务年限(年)',
            dataIndex: 'year',
            required: true,
            editType: 'text'
        },
    
        {
            title: '合同年度金额(万)',
            dataIndex: 'contractAnnualAmount',
            required: true,
            editType: 'text'
        },
    ])
    const getList = async ()=>{
          let postData = {
            desc: ["createTime"],
            params: {},
          };
        const res  = await api.workBrief.undertakingGetList(postData);
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

            debugger;
            var name = editData['add'].projectName[0];

            for(var i =0;i<proOptions._value.length;i++){
                if(proOptions._value[i].projectId == editData['add'].projectId){
                    name = proOptions._value[i].projectName;
                }
            }
            editData['add'].projectName = name;
            
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
        columns[0].editType = 'text'
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
        columns
    }

}
