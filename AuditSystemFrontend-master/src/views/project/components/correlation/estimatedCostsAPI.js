import { message, Modal } from "ant-design-vue";
import { deepClone } from "@/utils/tools";
const ADD_KEY = "add";
export function useCorrelation(projectId, fields) {
  const formRef = ref(null);
  const list = ref([]);
  const initList = (data) => {
    list.value = data || [];
  };
  const addVisible = ref(false);

  const addSubmit = () => {
    validate(async () => {
      if (fields && fields.dataIndex && editData[ADD_KEY][fields.dataIndex]) {
        const checked = list.value.some(
          (item) =>
            item[fields.dataIndex] === editData[ADD_KEY][fields.dataIndex]
        );
        if (checked) {
          message.error(
            `【${fields.title}】字段已存在相同数据，该字段不能有重复数据！`
          );
          return;
        }
      }
      editData[ADD_KEY].projectId = projectId;

      list.value.push({ ...editData[ADD_KEY] });
      editData[ADD_KEY] = {};
      addVisible.value = false;
    });
  };
  const validate = (callBack) => {
    formRef.value
      .validateFields()
      .then((vRes) => {
        callBack && callBack();
      })
      .catch((err) => {
        message.warning("请完善信息！");
      });
  };
  const addCancel = () => {
    editData[ADD_KEY] = {};
    addVisible.value = false;
  };
  const editData = reactive({
    add: {},
  });
  const edit = (row, index) => {
    editData[index] = JSON.parse(JSON.stringify(row));
  };
  const editSubmit = async (row, index) => {
    validate(async () => {
      if (fields && fields.dataIndex && editData[index][fields.dataIndex]) {
        const checked = list.value.some(
          (item, idx) =>
            idx !== index &&
            item[fields.dataIndex] === editData[index][fields.dataIndex]
        );
        if (checked) {
          message.error(
            `【${fields.title}字段已存在相同数据，该字段不能有重复数据！`
          );
          return;
        }
      }
      list.value[index] = { ...editData[index] };
      delete editData[index];
    });
  };
  const editCancel = (row, index) => {
    delete editData[index];
  };

  const del = (row, index) => {
    // if (row.id) {
    //   Modal.confirm({
    //     title: "操作确认",
    //     content: "是否确认删除该条数据？",
    //     onOk() {
    //       list.value.splice(index, 1);
    //     },
    //   });
    // } else {
    list.value.splice(index, 1);
    // }
  };
  const clone = (row, index) => {
    editData.add = deepClone(row);
    delete editData.add.id;
    addVisible.value = true;
  };
  return {
    formRef,
    list,
    initList,
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
