import request from '@/utils/request.js'
const baseUrl = '/biz';
const apiFun  = {
    
    //客户基础信息
    customerAdd(data){
        return request({url:baseUrl+`/customer/add`,method:'put',data:data});
    },
    customerEdit(data){
        return request({url:baseUrl+`/customer/update`,method:'put',data:data});
    },
    customerClaim(data){
        return request({url:baseUrl+`/customer/claim`,method:'put',data:data});
    },
    customerDel(data){
        return request({url:baseUrl+`/customer/delete`,method:'delete',data:data});
    },
    customerInfo(customerId){
        return request({url:baseUrl+`/customer/get/${customerId}`,method:'get'});
    },
    customerPage(data){
        return request({url:baseUrl+`/customer/page`,method:'post',data:data});
    },
    customerExport(data){
        return request({url:baseUrl+`/customer/export`,method:'post',data:data,responseType:'arraybuffer',filePath:true});
    },
    deleteCustomer(data){
      return request({url:baseUrl+`/customer/deleteCustomer`,method:'post',data:data});
    },
    //战略合作
    cooperationAdd(data){
        return request({url:baseUrl+`/customerStrategicCooperation/add`,method:'put',data:data});
    },
    cooperationEdit(data){
        return request({url:baseUrl+`/customerStrategicCooperation/update`,method:'put',data:data});
    },
    cooperationDel(id){
        return request({
            url: baseUrl + `/customerStrategicCooperation/delete/${id}`,
            method: "delete",
        });
    },
    cooperationGet(customerId){
        return request({url:baseUrl+`/customerStrategicCooperation/get/${customerId}`,method:'get'});
    },
    cooperationPage(data){
        return request({url:baseUrl+`/customerStrategicCooperation/page`,method:'post',data:data});
    },
    cooperationExport(data){
        return request({url:baseUrl+`/customerStrategicCooperation/export`,method:'post',data:data,responseType:'arraybuffer',filePath:true});
    },    

    //商机库
    businessOpportunityGet(customerId){
        return request({url:baseUrl+`/businessOpportunity/get/${customerId}`,method:'get'});
    },
    businessOpportunityPage(data){
        return request({url:baseUrl+`/businessOpportunity/page`,method:'post',data:data});
    },
    businessOpportunityList(data){
        return request({url:baseUrl+`/businessOpportunity/list`,method:'get',data:data});
    },

    //实时商机列表
    getQlmRealTimeList(data){
        return request({url:baseUrl+`/businessOpportunity/getQlmRealTimeList`,method:'post',data:data});
    },
    //存量商机列表
    getQlmStockList(data){
        return request({url:baseUrl+`/businessOpportunity/getQlmStockList`,method:'post',data:data});
    },

    //实时商机明细
    getQlmRealTimeDetail(opportunityId,lockStatus){
        return request({url:baseUrl+`/businessOpportunity/getQlmRealTimeDetail/${opportunityId}/${lockStatus}`,method:'get'});
    },
    // 存量商机
    getQlmStockDetail(opportunityId,lockStatus){
        return request({url:baseUrl+`/businessOpportunity/getQlmStockDetail/${opportunityId}/${lockStatus}`,method:'get'});
    },
    //千里马token
    getQlmToken(dataId){
        return request({url:baseUrl+`/businessOpportunity/getQlmToken/${dataId}`,method:'get'});
    },
    //业主单位是否解锁
    getQlmIsUnLockCompany(company){
        return request({url:baseUrl+`/businessOpportunity/isUnLock/${company}`,method:'get'});
    },

    getUnlockData(){
        return request({url:baseUrl+`/businessOpportunity/getUnlockData`,method:'get'});
    },
    //业主单位
    getQlmCompany(name){
        return request({url:baseUrl+`/businessOpportunity/getQlmCompany/${name}`,method:'get'});
    },
    saveQlmCompanyCustomer(name){
        return request({url:baseUrl+`/businessOpportunity/saveQlmCompanyCustomer/${name}`,method:'get'});
    },
    //联系人
    customerContactsGet(customerId){
        return request({url:baseUrl+`/customerContacts/get/${customerId}`,method:'get'});
    },
    customerContactsPage(data){
        return request({url:baseUrl+`/customerContacts/page`,method:'post',data:data});
    },
}
export default apiFun;

