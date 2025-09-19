import request from '@/utils/request.js'
import requestsso from '@/utils/requestsso.js'
const baseUrl = '';
const apiFun = {

    //登录
    login(data) {
        return request({ url: baseUrl + `/auth/login`, method: 'post', data: data });
    },
    ssoLogin(data, params, authorization) {
        return requestsso({ url: GLOBAL_PATH.ssoUrl + '/bsh-unify-api/oauth/token', method: 'post', params: params, data: data, authorization: authorization });
        // return requestsso({url:'/bsh-unify-api/oauth/token',method:'post',params:params,data:data,authorization:authorization});
    },
    ddLogin(params) {
        return request({ url: baseUrl + `/auth/login/ding/getTokenByDingCode`, method: 'get', params });
    },
    getSsoToken(params) {
        return request({ url: baseUrl + `/auth/login/oauth2/getSsoToken`, method: 'get', params: params });
    },
    loginUser() {
        return request({ url: baseUrl + `/biz/user/getInfo`, method: 'get' });
    },
    getUser(userId) {
        return request({ url: baseUrl + `/biz/user/get/${userId}`, method: 'get' });
    },
    switchAuth(data) {
        return request({ url: baseUrl + `/auth/switchAuth`, method: 'post', data: data });
    },
    logOut() {
        return request({ url: baseUrl + '/auth/logout', method: 'delete' });
    },

    downloadFile(data) {
        return request({ url: baseUrl + `/system/download2`, method: 'post', data: data, responseType: 'arraybuffer', filePath: true });
    },
    downloadBatch(data) {
        return request({ url: baseUrl + `/system/downForZip`, method: 'post', data: data, responseType: 'arraybuffer', filePath: true });
    },
    getFilePath(data) {
        return request({ url: baseUrl + `/system/generatePresignedUrl`, method: 'post', data: data });
    },
    uploadFile(modelName) {
        return request({ url: baseUrl + `/system/upload/${modelName}`, method: 'post' });
    },

    //关键词
    keywords(type) {
        return request({ url: baseUrl + `/biz/keyword/list/${type}`, method: 'get' });
    },
    keywordsAdd(data) {
        return request({ url: baseUrl + `/biz/keyword/add`, method: 'put', data: data });
    },

    listProvinceCity() {
        return request({ url: baseUrl + `/system/cnarea/listProvinceCity`, method: 'get' });
    },
    listProvinceCityArea() {
        return request({ url: baseUrl + `/system/cnarea/listProvinceCityArea`, method: 'get' });
    },
    listStreetByArea(parentCode) {
        return request({ url: baseUrl + `/system/cnarea/listStreet/${parentCode}`, method: 'get' });
    },
    // 拓展管理-业态枚举
    getYeTaiDic() {
        return request({ url: baseUrl + `/system/dict/getYeTaiDic`, method: 'get' });
    },

    //变更记录
    operLogInfo(moduleName, id) {
        return request({ url: baseUrl + `/biz/operLog/list/${moduleName}/${id}`, method: 'get' });
    },
    operLogPage(moduleName, recordId, data) {
        return request({ url: baseUrl + `/biz/operLog/page/${moduleName}/${recordId}`, method: 'post', data: data });
    },
    extensionOperLog(recordId, data) {
        return request({ url: baseUrl + `/biz/projectExtensionOperLog/page/${recordId}`, method: 'post', data: data });
    },

    //追踪记录
    followAdd(data) {
        return request({ url: baseUrl + `/biz/followLog/add`, method: 'put', data: data });
    },
    followAddCustomerLog(data) {
        return request({ url: baseUrl + `/biz/followLog/addCustomerLog`, method: 'put', data: data });
    },
    followEdit(data) {
        return request({ url: baseUrl + `/biz/followLog/update`, method: 'put', data: data });
    },
    followDel(moduleName, recordId, id) {
        return request({ url: baseUrl + `/biz/followLog/delete/${moduleName}/${recordId}/${id}`, method: 'delete' });
    },
    deleteCustomerLogByIds(moduleName, recordId, id) {
        return request({ url: baseUrl + `/biz/followLog/deleteCustomerLogByIds/${moduleName}/${recordId}/${id}`, method: 'delete' });
    },
    followInfo(moduleName, id) {
        return request({ url: baseUrl + `/biz/followLog/get/${moduleName}/${id}`, method: 'get' });
    },
    followPage(moduleName, recordId, data) {
        return request({ url: baseUrl + `/biz/followLog/page/${moduleName}/${recordId}`, method: 'post', data: data });
    },
    followList(moduleName, recordId, data) {
        return request({ url: baseUrl + `/biz/followLog/followList/${moduleName}/${recordId}`, method: 'post', data: data });
    },
    //规则管理
    rulesAdd(data) {
        return request({ url: baseUrl + `/biz/rules/add`, method: 'put', data: data });
    },
    rulesEdit(data) {
        return request({ url: baseUrl + `/biz/rules/update`, method: 'put', data: data });
    },
    rulesInfo(id) {
        return request({ url: baseUrl + `/biz/rules/get/${id}`, method: 'get' });
    },
    rulesDel(id) {
        return request({ url: baseUrl + `/biz/rules/delete/${id}`, method: 'delete' });
    },
    rulesPage(data) {
        return request({ url: baseUrl + `/biz/rules/page`, method: 'post', data: data });
    },
    rulesEnums() {
        return request({ url: baseUrl + `/biz/rules/rulesEnums`, method: 'get' });
    },
    ruleTask() {
        return request({ url: baseUrl + `/biz/rules/ruleTask`, method: 'get' });
    },

    // 审核
    approvalPage(data, type) {
        if (type === 1) {
            return request({ url: baseUrl + `/biz/approval/pageCommit`, method: 'post', data: data });
        } else {
            return request({ url: baseUrl + `/biz/approval/pageApproval`, method: 'post', data: data });
        }
    },
    approvalCommonPage(data) {
        return request({ url: baseUrl + `/biz/approval/page`, method: 'post', data: data });
    },

    //oa审批
    oaList(projectType, stepMenuId) {
        return request({ url: baseUrl + `/biz/oaApprovalTemplate/list/${projectType}/${stepMenuId}`, method: 'get' });
    },
    oaAdd(data) {
        return request({ url: baseUrl + `/biz/oaApproval/add`, method: 'put', data: data });
    },
    oaUpdate(data) {
        return request({ url: baseUrl + `/biz/oaApproval/update`, method: 'put', data: data });
    },
    oaGet(moduleName, recordId, subRecordId) {
        return request({ url: baseUrl + `/biz/oaApproval/get/${moduleName}/${recordId}/${subRecordId}`, method: 'get' });
    },
    oaPage(data) {
        return request({ url: baseUrl + `/biz/oaApproval/page`, method: 'post', data: data });
    },
    getDingCorpId() {
        return request({ url: baseUrl + '/auth/login/ding/getDingCorpId' })
    }

}
export default apiFun;

