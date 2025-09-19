import api from "@/api/index";
import { message, Modal } from "ant-design-vue";
import { deepClone } from "@/utils/tools";
export function useCorrelation(recordId, key, filter) {
  const formDetailRef = ref([]);
  const list = ref([]);
  //  console.log("formDetailRef.detailList;11")
  //  console.log(formDetailRef.value.detailList)
  //  console.log("formDetailRef.detailList;")
  //  console.log(formDetailRef.detailList)
  //  list.value= detailList;
  const getList = (detailList) => {
    // debugger
    // if(detailList!=null){
    //     list.value= formDetailRef.detailList
    // }else{
    //     list.value=[];
    // }
    list.value = detailList;
    // debugger
    // if(recordId!=null){
    //     // const res  = await api.customer.customerContactsGet(recordId,key);
    //     // if(res.code==200){
    //     //     if(filter&&filter.id){
    //     //         list.value = (res.data || []).filter(item=>{
    //     //             return !filter.id || item[filter.attr]==filter.id;
    //     //         });
    //     //     }else{
    //     //         list.value = res.data || [];
    //     //     }
    //     // }
    //     list.value= []// formRef.customerFollowLogDetailList;
    // }
  };

  const addVisible = ref(false);
  const addSubmit = () => {
    validate(async () => {
      console.log(list.value);
      // list.value.push({...editData['add']});
      list.value.push(editData["add"]);
      addCancel();
    }, "add");
  };
  const validate = (callBack) => {
    formDetailRef.value
      .validateFields()
      .then((vRes) => {
        callBack && callBack();
      })
      .catch((err) => {
        message.warning("请完善信息！");
      });
  };
  const addCancel = () => {
    editData["add"] = {};
    addVisible.value = false;
  };
  const editData = reactive({
    add: {},
  });
  const edit = (row, index) => {
    editData[index] = JSON.parse(JSON.stringify(row));
  };
  const editSubmit = async (row, index) => {
    message.success("操作成功");
    list.value.splice(index, 1, editData[index]);
    editCancel(row, index);
  };
  const editCancel = (row, index) => {
    delete editData[index];
  };

  const del = (row, index) => {
    if (row.id) {
      Modal.confirm({
        title: "操作确认",
        content: "是否确认删除该条数据？",
        onOk() {
          list.value.splice(index, 1);
        },
      });
    } else {
      list.value.splice(index, 1);
    }
  };
  const clone = (row, index) => {
    editData.add = deepClone(row);
    delete editData.add.id;
    addVisible.value = true;
  };
  return {
    formDetailRef,
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
    clone,
  };
}
