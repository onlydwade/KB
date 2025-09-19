<template>
  <a-table
    :columns="columns"
    :loading="loadding"
    :data-source="tableList"
    v-if="tableList.length > 0"
    rowKey="id"
    :pagination="false"
    :scroll="{ x: '100%' }"
  >
    <template #bodyCell="{ column, record, index }">
      <template v-if="column.key === 'operName'">
        <span class="color-danger" v-if="record.required == 1">*</span>
        {{ record.operName }}
      </template>
      <template v-if="column.key === 'status'">
        <check-circle-outlined v-if="record.projectDocumentList.length > 0" class="color-success" />
        <clock-circle-outlined v-else class="color-gray" />
      </template>
      <template v-if="column.key === 'projectDocumentList'">
        <a-spin :spinning="uploading == index">
          <a-upload-dragger
            :class="{ 'upload-disabled': record.disabled !== 0 }"
            v-if="!readOnly"
            name="file"
            :multiple="true"
            :accept="accept"
            :headers="{
              Authorization: 'Bearer ' + token,
            }"
            :disabled="record.disabled !== 0"
            :showUploadList="false"
            :action="uploadPath"
            @change="handleChange($event, index)"
          >
            <p class="ant-upload-drag-icon">
              <inbox-outlined></inbox-outlined>
            </p>
            <template v-if="record.disabled === 0">
              <p class="ant-upload-text">点击或将文件拖拽到这里上传</p>
              <p class="ant-upload-hint" style="font-size: 11px">支持：{{ accept }}</p>
            </template>
            <p v-else class="ant-upload-text">无需上传，自动带入</p>
          </a-upload-dragger>
          <slot></slot>
          <FileItem
            v-for="(item, index) in record.projectDocumentList"
            :readOnly="readOnly"
            :key="index"
            :fileData="item.documentObject"
            @fileDel="fileDel(index, record.projectDocumentList, item)"
          />
        </a-spin>
      </template>
    </template>
  </a-table>
</template>
<script setup>
import api from "@/api/index";
import { message } from "ant-design-vue";
import { mainStore } from "@/store";
const store = mainStore();
const props = defineProps({
  modelValue: {
    type: String,
    default: null,
  },
  FileList: {
    type: Array,
    default: () => [],
  },
  projectId: {
    type: Number,
    default: 0,
  },
  accept: {
    type: String,
    default: ".jpg, .jpeg, .png, .gif, .rar, .zip, .doc, .docx, .pdf, .xls, .xlsx, .xlsm",
  },
  readOnly: {
    type: Boolean,
    default: false,
  },
});
const emit = defineEmits(["update:modelValue", "uploadResult", "update:FileList"]);

const uploadPath = GLOBAL_PATH.api + "/system/upload/project";
const token = localStorage.getItem("token");

const columns = [
  {
    title: "名称",
    key: "operName",
  },
  {
    title: "状态",
    key: "status",
    align: "center",
    width: 100,
  },
  {
    title: "文件上传",
    key: "projectDocumentList",
    width: 480,
  },
];
const loadding = ref(false);
const tableList = ref([]);
const uploading = ref(-1);
watch(
  () => props.FileList,
  (newVal, oldVal) => {
    if (tableList.value.length > 0 && newVal.length > 0) {
      newVal.forEach(newItem => {
        tableList.value.forEach(tableItem => {
          if (newItem.documentTemplateId === tableItem.id) {
            // 如果 projectDocumentList 为空，初始化为空数组
            if (!tableItem.projectDocumentList) {
              tableItem.projectDocumentList = [];
            }
            // 检查项目文档列表中是否已经存在该条目
            const exists = tableItem.projectDocumentList.some(item => item.id === newItem.id);

            if (!exists) {
              // 将 newItem 插入到匹配的 tableItem 的 projectDocumentList 中
              tableItem.projectDocumentList.push(newItem);
            }
          }
        });
      });
    }
    getUploadResult();
    console.log("🚀 ~ watch ~ tableList:", tableList.value);
  }
);

const handleChange = (info, index) => {
  const status = info.file.status;
  const response = info.file.response;
  if (status === "uploading") {
    uploading.value = index;
  }
  if (status === "done") {
    uploading.value = -1;
    if (response && response.success) {
      response.data.time = Date.now();
      addDocument(response.data, index);
    }
  } else if (status === "error") {
    uploading.value = -1;
    message.error(`${info.file.name} 上传失败.`);
  }
  if (response && response.code && response.code == 500) {
    message.error(response.msg);
  }
};
const getFileList = () => {
  let fileList = [];
  (tableList.value || []).forEach(item => {
    item.projectDocumentList.forEach(file => {
      fileList.push(file);
    });
  });
  emit("update:FileList", fileList);
};
const addDocument = (fileData, index) => {
  let ext = fileData.name.substring(fileData.name.lastIndexOf(".") + 1);
  let postData = {
    documentTemplateId: tableList.value[index].id,
    documentObject: JSON.stringify(fileData),
    documentExt: ext,
    documentName: fileData.name.replace("." + ext, ""),
  };
  if (props.recordId) {
    postData.recordId = props.recordId;
    api.project.addExpansionDocument(postData).then(res => {
      if (res.code == 200) {
        tableList.value[index].projectDocumentList.push(res.data);
        getUploadResult();
      }
    });
  } else {
    tableList.value[index].projectDocumentList.push(postData);
    // emit("update:FileList", tableList.value[index].projectDocumentList);
    getFileList();
    getUploadResult();
  }
};

const fileDel = (index, list, item) => {
  list.splice(index, 1);
  getFileList();
  getUploadResult();
};
const getProjectDocument = projectId => {
  loadding.value = true;
  api.project.getProjectDocument(projectId).then(res => {
    if (res.code == 200) {
      tableList.value = (res.data || []).map(item => {
        item.projectDocumentList = item.projectDocumentList || [];
        return item;
      });
    }
    loadding.value = false;
  });
};
onMounted(() => {
  getProjectDocument();
});
const uploadResult = ref(false);
const getUploadResult = type => {
  let done = true;
  for (var i = 0; i < tableList.value.length; i++) {
    let item = tableList.value[i];
    if ((item.projectDocumentList || []).length == 0 && !!item.required) {
      done = false;
      break;
    }
  }
  if (type != "init") {
    if (uploadResult.value != done) {
      uploadResult.value = done;
      emit("uploadResult", done);
    }
  }
  console.log("🚀 ~ api.project.getProjectDocument ~ tableList.value:", tableList.value);
  emit("update:modelValue", done ? "upload_finish" : null);
};

const batchUpLoad = recordId => {
  store.spinChange(1);
  return new Promise(function (resolve, reject) {
    let promises = [];
    tableList.value.forEach((item, i) => {
      item.projectDocumentList.forEach((file, k) => {
        file.recordId = recordId;
        // let pro = api.project.addExpansionDocument(file).then(res=>{
        //     if(res.code==200){
        //         tableList.value[i].projectDocumentList[k] = res.data;
        //     }
        // })
        tableList.value[i].projectDocumentList[k] = res.data;
        promises.push(pro);
      });
    });
    if (promises.length > 0) {
      Promise.all(promises)
        .then(posts => {
          store.spinChange(-1);
          resolve("upload_success");
        })
        .catch(reason => {
          store.spinChange(-1);
          reject("upload_filed");
        });
    } else {
      store.spinChange(-1);
      resolve("upload_none");
    }
  });
};
defineExpose({ batchUpLoad });
</script>
<style scoped lang="less"></style>
