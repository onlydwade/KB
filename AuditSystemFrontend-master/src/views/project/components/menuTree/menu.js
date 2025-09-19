import api from "@/api/index";
import { message } from "ant-design-vue";
import { formDataFill, dateFormat } from "@/utils/tools";
import { mainStore } from "@/store";
const store = mainStore();
const checkboxGroupkeys = [
  "invalidBidReason",
  "keyFocusProject",
  // "relatedSealType",
];
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
    //如果当前状态是已废止直接禁用所有表单参数
    if ("YI_FEI_ZHI"==serviceStatus.value) {
      return true;
    }
    const postId = import.meta.env.VITE_ADMINISTRATOR_POST_ID;
    if (parseInt(postId) === store.deptPost.postId) {
      return false;
    }
    
    return (
      !roleKeys.value.includes(1) ||
      menuInfo.value.status == 1 ||
      [1, 2, 8].includes(menuInfo.value.approvalStatus) ||
      ["ZAI_GUAN", "YI_ZHONG_ZHI"].includes(serviceStatus.value) ||
      expire.value == "YI_SHI_XIAO"
    );
  });

  const getInfo = (callBack) => {
    api.project.projectInfo(projectId).then((res) => {
      if (res.code == 200) {
        if (fill) {
          res.data && formDataFill(formData.value, formAttrs, res.data);
        } else {
          formData.value = res.data;
        }
        callBack && callBack(res.data);
      }
    });
  };
  const save = async (type, temp) => {
    let postData = {};
    if (formAttrs) {
      if (formAttrs.length == 0) {
        postData = formData.value;
      } else {
        formAttrs.forEach((item) => {
          postData[item] = formData.value[item];
        });
      }
      // 复选框转换字段类型
      checkboxGroupkeys.forEach((key) => {
        if (postData[key]) {
          if (
            key == "relatedSealType" &&
            postData["bidType"] == "DIAN_ZHI_BIAO"
          ) {
            //电子标单选，纸质标多选
          } else {
            postData[key] = postData[key].join(",");
          }
        }
      });
      if(type==5){
        postData['serviceStatus']='YI_FEI_ZHI'
      }
      let res = await api.project.projectEdit(postData);
      if (res.code != 200) {
        return;
      }
    }
    switch (type) {
      case -1:
        //重新发起线上OA审批
        oaStart(type, temp);
        break;
      case 0:
        //线上OA审批
        oaStart(type, temp);
        break;
      case 1:
        message.success("操作成功！");
        refreshMenuTree(menuInfo.value.id, 1, null, 8);
        break;
      case 2:
      //提交保存 校验必填
      case 3:
        //跳过 已填信息保存 改变节点状态
        refreshMenuTree(menuInfo.value.id, 1, (comp) => {
          console.log("eeee", comp);
          if (comp.code == 200) {
            message.success("操作成功！");
          }
        });
        break;
      case 4:
        //标记未完成
        refreshMenuTree(menuInfo.value.id, 0);
        break;
      case 5:
        //项目废止（测算及方案不通过）
        message.success("操作成功！");
        //刷新
        location.reload();
        break;
      default:
        //其它 暂存已填信息保存 不改节点状态
        message.success("操作成功！");
    }
  };
  const submit = (type, temp) => {
    if (formRef.value && type !== 3 && type !== 4) {
      formRef.value
        .validateFields()
        .then((vRes) => {
          offLineReg(type, temp);
        })
        .catch((err) => {
          console.log(err);
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
    save(type, temp);
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
      detailUrl: `${window.location.origin}/#/projectOaInfo?id=${projectId}&to=${menuInfo.value.code}`,
    };
    let apiKey = "oaAdd";
    if (type == -1) {
      apiKey = "oaUpdate";
      postData.id = temp.oaId;
    } else {
      postData.submitTime = dateFormat(new Date());
      postData.createTime = dateFormat(new Date());
      postData.submitUserId = temp.userId;
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
    getInfo,
    save,
    submit,
    refreshMenuTree,
    disabled,
  };
}
