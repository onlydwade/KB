import api from "@/api/index";
import { message } from "ant-design-vue";
import { formDataFill, dateFormat } from "@/utils/tools";
import { mainStore } from "@/store";
const store = mainStore();
export function useMenuTree(projectId, menuInfo, formAttrs, fill) {
  //formAttrs  空数组保存全部    null 不保存跳过    fill true 根据formAttrs 反显  否则全部
  const formRef = ref(null);
  const documentsRef = ref(null);
  const formData = ref({});
  const refreshMenuTree = inject("refreshMenuTree");
  const roleKeys = inject("getAutoParams")("roleKeys");
  const serviceStatus = inject("getAutoParams")("serviceStatus");
  const expire = inject("getAutoParams")("expire");
  const disabled = computed(() => {
    return (
      !roleKeys.value.includes(1) ||
      menuInfo.value.status == 1 ||
      [1, 2, 5, 8].includes(menuInfo.value.approvalStatus) ||
      ["ZAI_GUAN", "YI_ZHONG_ZHI"].includes(serviceStatus.value) ||
      expire.value == "YI_SHI_XIAO"
    );
  });
  const submit = (type, temp) => {
    if (formRef.value && type !== 3 && type !== 4) {
      formRef.value
        .validateFields()
        .then((vRes) => {
          offLineReg(type, temp);
        })
        .catch((err) => {
          message.warning("请完善信息！");
        });
    } else {
      offLineReg(type, temp);
    }
  };
  const offLineReg = (type, temp) => {
    //线下审批  必传文件校验
    if (type == 1 && documentsRef.value) {
      let offlineStatus = documentsRef.value.getOfflineStatus();
      if (offlineStatus != "success") {
        message.warning(offlineStatus);
        return;
      }
    }
  };

  const bus = inject("bus");
  const projectName = inject("getAutoParams")("projectName");
  const oaStart = (type, temp) => {
    let postData = {
      subject: projectName.value + "-" + menuInfo.value.name,
      recordId: projectId,
      subRecordId: menuInfo.value.id,
      templateId: temp.templateId,
      moduleName: "Project",
      approvalStatus: 5,
      stepCode: menuInfo.value.code,
      mainProcess: temp.mainProcess,
      detailUrl:
        window.location.origin +
        "/#/projectOaInfo?id=" +
        projectId +
        "&to=" +
        menuInfo.value.code,
    };
    let apiKey = "oaAdd";
    if (type == -1) {
      apiKey = "oaUpdate";
      postData.id = temp.oaId;
    } else {
      postData.submitTime = dateFormat(new Date());
      postData.createTime = dateFormat(new Date());
      postData.submitDeptId = store.deptPost.deptId || store.userInfo.deptId;
      postData.submitUserId = store.userInfo.userId;
      postData.submitUser = {
        id: store.userInfo.userId,
        realname: store.userInfo.realname,
      };
    }
    api.common[apiKey](postData).then(async (res) => {
      if (res.code == 200) {
        message.success("操作成功！");
        if (temp.mainProcess && menuInfo.value.status == 0) {
          await api.project.stepStatusUpdate({
            projectId: projectId,
            stepMenuId: menuInfo.value.id,
            approvalStatus: 5,
          });
        }
        refreshMenuTree();
        bus.emit("oaHasSubmit");
      }
    });
  };
  return {
    formRef,
    documentsRef,
    formData,
    submit,
    refreshMenuTree,
    disabled,
  };
}
