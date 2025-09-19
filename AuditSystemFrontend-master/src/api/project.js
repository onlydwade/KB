import request from "@/utils/request.js";
const baseUrl = "/biz";
const apiFun = {
    //项目基础信息
    projectEdit(data) {
        return request({
            url: baseUrl + `/project/update`,
            method: "put",
            data: data,
        });
    },
    projectAdd(data) {
        return request({
            url: baseUrl + `/project/add`,
            method: "put",
            data: data,
        });
    },
    saveRenewal(data) {
        return request({
            url: baseUrl + `/projectExtension/saveRenewal`,
            method: "put",
            data: data,
        });
    },
    projectInfo(id) {
        return request({ url: baseUrl + `/project/get/${id}`, method: "get" });
    },
    deleteProject(data) {
      return request({
          url: baseUrl + `/project/deleteProject`,
          method: "post",
          data: data,
      });
    },
    //客户信息-关联项目
    projectPageFromCustomer(data) {
        return request({
            url: baseUrl + `/project/projectPageFromCustomer`,
            method: "post",
            data: data,
        });
    },

    projectPage(data) {
        return request({
            url: baseUrl + `/project/page`,
            method: "post",
            data: data,
        });
    },
    projectPageAll(data) {
      return request({
          url: baseUrl + `/project/pageAll`,
          method: "post",
          data: data,
      });
  },
    getStepList() {
        return request({
            url: baseUrl + `/project/getProjectStepMenuList`,
            method: "get",            
        });
    },
    getProjectDocument() {
        return request({
            url: baseUrl + `/projectBacklog/projectDocument/list`,
            method: "get",            
        });
    },
    projectExport(data) {
        return request({
            url: baseUrl + `/project/export`,
            // url: baseUrl + '/projectExtension/renewBidPageExport',
            method: "post",
            data: data,
            responseType: "arraybuffer",
            filePath: true,
        });
    },
    renewBidPageExport(data) {
        return request({
            // url: baseUrl + `/project/export`,
            url: baseUrl + '/projectExtension/renewBidPageExport',
            method: "post",
            data: data,
            responseType: "arraybuffer",
            filePath: true,
        });
    },
    //业绩台账导出
    exportProjectAchievement(data) {
        return request({
            url: baseUrl + `/project/exportProjectAchievement`,
            method: "post",
            data: data,
            responseType: "arraybuffer",
            filePath: true,
        });
    },
    exportProjectAchievementTwo(data) {
        return request({
            url: baseUrl + `/project/exportProjectAchievementTwo`,
            method: "post",
            data: data,
            responseType: "arraybuffer",
            filePath: true,
        });
    },   
    getOpenMarkList(data) {
        return request({
            url: baseUrl + `/project/getOpenMarkList`,
            method: "post",
            data: data,
            responseType: "arraybuffer",
            filePath: true,
        });        
    }, 
    // 拓后运营-在管项目库导出
    projectTHExport(data) {
        return request({
            // url: baseUrl + `/projectExtension/export`,
            url: baseUrl + `/projectExtension/extensionExport`,
            method: "post",
            data: data,
            responseType: "arraybuffer",
            filePath: true,
        });
    },

    // 拓前运营-在管项目库导出
    projectTQxport(data) {
        return request({
            url: baseUrl + `/projectExtension/export`,
            //url: baseUrl + `/projectExtension/extensionExport`,
            method: "post",
            data: data,
            responseType: "arraybuffer",
            filePath: true,
        });
    },
    projectClose(data) {
        return request({
            url: baseUrl + `/project/changeClose`,
            method: "put",
            data: data,
        });
    },
    projectExpire(data) {
        return request({
            url: baseUrl + `/project/changeExpire`,
            method: "put",
            data: data,
        });
    },
    projectValid(data) {
        return request({
            url: baseUrl + `/project/changeValid`,
            method: "put",
            data: data,
        });
    },
    // 克隆
    projectClone(data) {
        return request({
            url: baseUrl + `/project/clone`,
            method: "put",
            data: data,
        });
    },
    //变更归属人
    changeAttributor(projectId, dispatchUserId) {
        return request({
            url: baseUrl + `/project/changeAttributor/${projectId}/${dispatchUserId}`,
            method: "put",
        });
    },
    //录入最终报价
    changeBidingAmount(projectId, bidingAmount) {
        return request({
            url: baseUrl + `/project/changeBidingAmount/${projectId}/${bidingAmount}`,
            method: "put",
        });
    },
    //只计算金额接口，不更新项目表和业绩数据
    getAnnualConversionAmountCalculate(data) {
        return request({
            url: baseUrl + `/project/getAnnualConversionAmountCalculate`,
            method: "post",
            data: data,
        });
    },
    //计算当年转化金额
    annualConversionAmountCalculateNew(data) {
        return request({
            url: baseUrl + `/project/annualConversionAmountCalculateNew`,
            method: "post",
            data: data,
        });
    },
    //录入最终报价
    annualConversionAmountCalculate(data) {
        return request({
            url: baseUrl + `/project/annualConversionAmountCalculate`,
            method: "post",
            data: data,
        });
    },
     //补录项目-录入最终报价
     backlogAnnualConversionAmountCalculate(data) {
      return request({
          url: baseUrl + `/projectBacklog/annualConversionAmountCalculateNew`,
          method: "post",
          data: data,
      });
    },
    // 项目节点
    treeStepAll() {
        return request({ url: baseUrl + `/projectStepMenu/tree`, method: "get" });
    },
    treeStepUpdate(data) {
        return request({
            url: baseUrl + `/projectStepMenu/update`,
            method: "put",
            data: data,
        });
    },
    treeStepByProject(projectId) {
        return request({
            url: baseUrl + `/projectStepMenuConfig/treeByProjectId/${projectId}`,
            method: "get",
        });
    },
    treeStepByType(projectType) {
        return request({
            url: baseUrl + `/projectStepMenuConfig/treeProjectType/${projectType}`,
            method: "get",
        });
    },
    listStepByType(projectType) {
        return request({
            url: baseUrl + `/projectStepMenuConfig/listByProjectType/${projectType}`,
            method: "get",
        });
    },
    treeStepMenuUpdate(data) {
        return request({
            url: baseUrl + `/projectStepMenuConfig/update`,
            method: "put",
            data: data,
        });
    },
    callbackProjectStep(data) {
        return request({
            url: baseUrl + `/projectStep/callbackProjectStep`,
            method: "post",
            data: data,
        });
    },

    //修改项目步骤完成状态
    stepStatusUpdate(data) {
        return request({
            url: baseUrl + `/projectStep/update`,
            method: "put",
            data: data,
        });
    },
    //是否团队成员
    inTeam(id) {
        return request({ url: baseUrl + `/projectTeam/get/${id}`, method: "get" });
    },
    //项目文档树
    treeDocumentByType(projectType) {
        return request({
            url: baseUrl + `/projectStepMenuConfig/listByProjectTypeTwo/${projectType}`,
            method: "get",
        });
    },
    documentList(projectId, stepMenuId) {
        return request({
            url: baseUrl + `/projectDocument/list/${projectId}/${stepMenuId}`,
            method: "get",
        });
    },
    addDocument(data) {
        return request({
            url: baseUrl + `/projectDocument/add`,
            method: "put",
            data: data,
        });
    },
    delDocument(id) {
        return request({
            url: baseUrl + `/projectDocument/delete/${id}`,
            method: "delete",
        });
    },
    documentPageTwo(data) {
        return request({
            url: baseUrl + `/projectDocument/pageTwo`,
            method: "post",
            data: data,
        });
    },
    //项目关联详细增删改查
    correlationAdd(data, key) {
        return request({ url: baseUrl + `/${key}/add`, method: "put", data: data });
    },
    correlationEdit(data, key) {
        return request({
            url: baseUrl + `/${key}/update`,
            method: "put",
            data: data,
        });
    },
    correlationGet(id, key) {
        return request({ url: baseUrl + `/${key}/get/${id}`, method: "get" });
    },
    correlationDel(id, key) {
        return request({ url: baseUrl + `/${key}/delete/${id}`, method: "delete" });
    },
    correlationList(projectId, key) {
        return request({
            url: baseUrl + `/${key}/list/${projectId}`,
            method: "get",
        });
    },
    correlationPage(data, key) {
        return request({ url: baseUrl + `/${key}/page`, method: "post", data });
    },
    closureList(projectId) {
        return request({
            url: baseUrl + `/project/getClosureInfo/${projectId}`,
            method: "get",
        });
    },
    // 扩展管理-在管项目库统计
    expandProjectTotal(data) {
        return request({
            url: baseUrl + `/projectExtension/projectTotal`,
            method: "post",
            data,
        });
    },
    // 扩展管理-在管项目库-同步到拓后运营列表
    syncOperationProject(projectId) {
        return request({
            url: baseUrl + `/projectExtension/syncOperationProject/${projectId}`,
            method: "get",
        });
    },
    // 操作回退
    rollbackProject(projectId) {
        return request({
            url: baseUrl + `/projectExtension/rollbackProject/${projectId}`,
            method: "get",
        });
    },
    // 扩展管理-续签/重新投标
    projectRenewBidPage(data) {
        return request({
            url: baseUrl + `/projectExtension/renewBidPage`,
            method: "post",
            data,
        });
    },
    // 拓后管理-拓后管理
    treeByProjectId(projectId) {
        return request({
            url: baseUrl + `/projectExtension/treeByProjectId/${projectId}`,
            method: "get",
        });
    },
    // 扩展管理-在管项目库列表
    projectExtensionPage(data) {
        return request({
            url: baseUrl + `/projectExtension/page`,
            method: "post",
            data,
        });
    },
        // 扩展管理-项目补录列表
        projectBacklogList(data) {
          return request({
              url: baseUrl + `/projectBacklog/list`,
              method: "post",
              data,
          });
       },
       // 扩展管理-项目补录详情
       projectBacklogInfo(id) {
          return request({
              url: baseUrl + `/projectBacklog/info/${id}`,
              method: "get",
          });
       },
       // 扩展管理-项目补录新增
       projectBacklogAdd(data) {
          return request({
              url: baseUrl + `/projectBacklog/add`,
              method: "post",
              data: data,
          });
       },
         // 扩展管理-项目补录提交
         projectBacklogSubmit(data) {
          return request({
              url: baseUrl + `/projectBacklog/submit`,
              method: "post",
              data: data,
          });
       },
       // 扩展管理-项目补录修改
       projectBacklogUpdate(data) {
          return request({
              url: baseUrl + `/projectBacklog/update`,
              method: "post",
              data: data,
          });
       },
       getCheckOaBacklog() {
        return request({ url: baseUrl + `/projectBacklog/getcheckOa`, method: 'get'});
       },
      setCheckOaBacklog(checkValue) {
        return request({ url: baseUrl + `/projectBacklog/setcheckOa?checkValue=`+checkValue, method: 'get'});
      },
    // 拓后管理-拓后管理列表
    projectExtensionPageAfter(data) {
        return request({
            url: baseUrl + `/projectExtension/extensionPage`,
            method: "post",
            data,
        });
    },
    // 拓后管理-续签
    projectExtensionRenew(id) {
        return request({
            url: baseUrl + `/projectExtension/renew/${id}`,
            method: "get",
        });
    },
    // 拓后管理-重新投标
    projectExtensionNewBid(id) {
        return request({
            url: baseUrl + `/projectExtension/newBid/${id}`,
            method: "get",
        });
    },
    // 拓后管理-项目信息-详情
    projectExtensionInfo(id) {
        return request({
            url: baseUrl + `/projectExtension/get/${id}`,
            method: "get",
        });
    },
    // 拓后管理-在管项目库统计
    projectExtensionTotal(data) {
        return request({
            url: baseUrl + `/projectExtension/total`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-运营管理-基础数据
    projectOperationInfo(id) {
        return request({
            url: baseUrl + `/projectOperation/get/${id}`,
            method: "get",
        });
    },
    // 拓后管理-运营管理-新增
    projectOperationAdd(data) {
        return request({
            url: baseUrl + `/projectOperation/add`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-运营管理-编辑
    projectOperationEdit(data) {
        return request({
            url: baseUrl + `/projectOperation/update`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-运营管理-关联项目列表
    projectCorrelationList(id) {
        return request({
            url: baseUrl + `/projectCorrelation/get/${id}`,
            method: "get",
        });
    },
    // 拓后管理-运营管理-关联项目新增
    projectCorrelationAdd(data) {
        return request({
            url: baseUrl + `/projectCorrelation/add`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-运营管理-关联项目编辑
    projectCorrelationEdit(data) {
        return request({
            url: baseUrl + `/projectCorrelation/update`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-运营管理-关联项目删除
    // projectCorrelationDel(data){
    //     return request({url:baseUrl+`/projectCorrelation/delete/${id}`,method:'post',data:data});
    // },
    projectCorrelationDel(id) {
        return request({
            url: baseUrl + `/projectCorrelation/delete/${id}`,
            method: "delete",
        });
    },
    // 拓后管理-运营管理-经营指标信息列表
    projectMgmtIndicatorsList(id) {
        return request({
            url: baseUrl + `/projectManagementIndicators/list/${id}`,
            method: "get",
        });
    },
    // 拓后管理-运营管理-经营指标信息新增
    projectMgmtIndicatorsAdd(data) {
        return request({
            url: baseUrl + `/projectManagementIndicators/add`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-运营管理-经营指标信息编辑
    projectMgmtIndicatorsEdit(data) {
        return request({
            url: baseUrl + `/projectManagementIndicators/update`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-运营管理-经营指标信息删除
    projectMgmtIndicatorsDel(id) {
        return request({
            url: baseUrl + `/projectManagementIndicators/delete/${id}`,
            method: "delete",
        });
    },
    //  拓后管理-运营管理-服务满意度信息列表
    projectServeSatisList(data) {
        return request({
            url: baseUrl + `/projectServeSatisfaction/list`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-运营管理-服务满意度信息新增
    projectServeSatisAdd(data) {
        return request({
            url: baseUrl + `/projectServeSatisfaction/add`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-运营管理-服务满意度信息编辑
    projectServeSatisEdit(data) {
        return request({
            url: baseUrl + `/projectServeSatisfaction/update`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-运营管理-服务满意度信息删除
    projectServeSatisDel(id) {
        return request({
            url: baseUrl + `/projectServeSatisfaction/delete/${id}`,
            method: "delete",
        });
    },
    // 拓后管理-运营管理-立项收缴率列表
    projectOperationList(id) {
        return request({
            url: baseUrl + `/projectOperation/collectionRateMessage/${id}`,
            method: "get",
        });
    },
    // 拓后管理-项目评估列表
    projectAssessPage(data, key, menuId) {
        if (menuId) {
            return request({
                url: baseUrl + `/${key}/page/${menuId}`,
                method: "post",
                data: data,
            });
        } else {
            return request({
                url: baseUrl + `/${key}/page`,
                method: "post",
                data: data,
            });
        }
    },
    // 拓后运营-项目评估详情
    projectAssessGet(key, menuId, id) {
        return request({
            url: baseUrl + `/${key}/get/${menuId}/${id}`,
            method: "get",
        });
    },
    // 拓后运营-项目评估新增
    projectAssessAdd(data, key) {
        return request({
            url: baseUrl + `/${key}/add`,
            method: "post",
            data: data,
        });
    },
    // 拓后运营-项目评估编辑
    projectAssessEdit(data, key) {
        return request({
            url: baseUrl + `/${key}/update`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-项目评估删除
    projectAssessDel(id) {
        return request({
            url: baseUrl + `/projectAssess/delete/${id}`,
            method: "delete",
        });
    },
    // 拓后管理-项目运营-经营管理列表
    projectOperateincomeList(id, menuId) {
        return request({
            url: baseUrl + `/projectOperateincome/list/${id}/${menuId}`,
            method: "get",
        });
    },
    // 拓后管理-项目运营-经营管理新增
    operateincomeAdd(data) {
        return request({
            url: baseUrl + `/projectOperateincome/add`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-项目运营-经营管理编辑
    operateincomeEdit(data) {
        return request({
            url: baseUrl + `/projectOperateincome/update`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-项目运营-经营管理删除
    operateincomeDel(id) {
        return request({
            url: baseUrl + `/projectOperateincome/delete/${id}`,
            method: "delete",
        });
    },
    // 拓后运营-承接查验列表
    projectCheckPage(data) {
        return request({
            url: baseUrl + `/projectCheck/page`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-承接查验详情
    projectCheckInfo(id) {
        return request({ url: baseUrl + `/projectCheck/get/${id}`, method: "get" });
    },
    // 拓后运营-承接查验节点新增
    projectChecktAdd(data, key) {
        return request({
            url: baseUrl + `/${key}/add`,
            method: "post",
            data: data,
        });
    },
    // 拓后运营-承接查验节点编辑
    projectCheckEdit(data, key) {
        return request({
            url: baseUrl + `/${key}/update`,
            method: "post",
            data: data,
        });
    },
    // 拓后运营-项目评估列表
    projectAssessListPage(data) {
        return request({
            url: baseUrl + `/projectAssess/listPage`,
            method: "post",
            data: data,
        });
    },
    // 拓后运营-项目干预标题匹配评估(项目干预用）
    projectAssessList(data) {
        return request({
            url: baseUrl + `/projectAssess/list`,
            method: "post",
            data: data,
        });
    },
    // 拓后运营-项目干预列表
    projectInterveneListPage(data) {
        return request({
            url: baseUrl + `/projectIntervene/listPage`,
            method: "post",
            data: data,
        });
    },
    // 拓后运营-退场管理列表
    projectExtensionExitListPage(data) {
        return request({
            url: baseUrl + `/projectExtensionExit/listPage`,
            method: "post",
            data: data,
        });
    },
    //附件
    expansionDocumentList(companyId, stepMenuId, recordId) {
        return request({
            url: baseUrl +
                `/projectExpansionDocument/list/${companyId}/${stepMenuId}/${
          recordId || 0
        }`,
            method: "get",
        });
    },
    // 附件添加
    addExpansionDocument(data) {
        return request({
            url: baseUrl + `/projectExpansionDocument/add`,
            method: "put",
            data: data,
        });
    },
    // 附件删除
    delExpansionDocument(id) {
        return request({
            url: baseUrl + `/projectExpansionDocument/delete/${id}`,
            method: "delete",
        });
    },
    // 拓后运营-项目干预列表
    projectIntervenePage(data, key, menuId) {
        if (menuId) {
            return request({
                url: baseUrl + `/${key}/page/${menuId}`,
                method: "post",
                data: data,
            });
        } else {
            return request({
                url: baseUrl + `/${key}/page`,
                method: "post",
                data: data,
            });
        }
    },
    // 拓后管理-项目干预详情
    projectInterveneInfo(key, menuId, id) {
        return request({
            url: baseUrl + `/${key}/get/${menuId}/${id}`,
            method: "get",
        });
    },
    // 拓后运营-项目干预节点-评估详情
    projectAssessInfo(key, menuId, id) {
        return request({
            url: baseUrl + `/${key}/get/${menuId}/${id}`,
            method: "get",
        });
    },
    // 拓后运营-项目干预节点反馈执行情况
    projectInterveneAdd(data, key) {
        return request({
            url: baseUrl + `/${key}/addTransmit`,
            method: "post",
            data: data,
        });
    },
    // 拓后运营-项目干预节点反馈执行情况
    projectInterveneEdit(data, key) {
        return request({
            url: baseUrl + `/${key}/update`,
            method: "post",
            data: data,
        });
    },
    // 拓后运营-项目干预节点通过or不通过
    projectInterveneIsNotPass(data, key) {
        return request({
            url: baseUrl + `/${key}/isNotPass`,
            method: "post",
            data: data,
        });
    },
    // 拓后运营-退场管理节点详情
    projectExtensionExitInfo(key, menuId, id) {
        return request({
            url: baseUrl + `/${key}/get/${menuId}/${id}`,
            method: "get",
        });
    },
    // 拓后运营-退场管理节点新增
    projectExtensionExitAdd(data, key) {
        return request({
            url: baseUrl + `/${key}/add`,
            method: "post",
            data: data,
        });
    },
    // 拓后运营-退场管理节点编辑
    projectExtensionExitEdit(data, key) {
        return request({
            url: baseUrl + `/${key}/update`,
            method: "post",
            data: data,
        });
    },
    // 修改Or新增-项目步骤完成状态
    projectStepExpansion(data) {
        return request({
            url: baseUrl + `/projectStepExpansion/update`,
            method: "put",
            data: data,
        });
    },
    // 拓后管理-项目运营-风险管理列表
    projectRiskManagementList(id, menuId) {
        return request({
            url: baseUrl + `/projectRiskManagement/list/${id}/${menuId}`,
            method: "get",
        });
    },
    // 拓后管理-项目运营-风险管理新增
    projectRiskManagementAdd(data) {
        return request({
            url: baseUrl + `/projectRiskManagement/add`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-项目运营-风险管理编辑
    projectRiskManagementEdit(data) {
        return request({
            url: baseUrl + `/projectRiskManagement/update`,
            method: "post",
            data: data,
        });
    },
    // 拓后管理-项目运营-风险管理删除
    projectRiskManagementDel(id) {
        return request({
            url: baseUrl + `/projectRiskManagement/delete/${id}`,
            method: "delete",
        });
    },
    // 首页-项目通告
    pageNotify(data) {
        return request({
            url: baseUrl + `/project/pageNotify`,
            method: "post",
            data: data,
        });
    },
    // 首页-项目丢盘情况监管
    pageEndNotify(data) {
        return request({
            url: baseUrl + `/project/pageEndNotify`,
            method: "post",
            data: data,
        });
    },
    exportNotify(data){
        return request({url:baseUrl+`/project/exportNotify`,method:'post',data:data,responseType:'arraybuffer',filePath:true});
    },
    exportEndNotify(data){
        return request({url:baseUrl+`/project/exportEndNotify`,method:'post',data:data,responseType:'arraybuffer',filePath:true});
    },
    offlinePass(data) {
        return request({ url: baseUrl + `/projectExtensionExit/exitOfflinePass`, method: "post",data:data });
    },
    storage(data) {
        return request({ url: baseUrl + `/projectExtensionExit/storage`, method: "post",data:data  });
    },
    getAchievementBacklogList(projectId) {
        return request({ url: baseUrl + `/projectBacklog/getAchievementList/${projectId}`, method: "get" });
    },
    projectDuplicateCheck(data) {
        return request({ url: baseUrl + `/project/projectDuplicateCheck`, method: "post",data:data  });
    },
};
export default apiFun;
