import request from '@/utils/request.js'
const baseUrl = '/biz';
const apiFun  = {

    stepTree(){
        return request({url:baseUrl+`/companyStep/tree`,method:'get'});
    },

    //文件
    documentAdd(data){
        return request({url:baseUrl+`/companyDocument/add`,method:'put',data:data});
    },
    documentDel(id){
        return request({url:baseUrl+`/companyDocument/delete/${id}`,method:'delete'});
    },
    documentList(companyId,stepMenuId,recordId){
        return request({url:baseUrl+`/companyDocument/list/${companyId}/${stepMenuId}/${(recordId || 0)}`,method:'get'});
    },
    documentPage(data){
        return request({url:baseUrl+`/companyDocument/page`,method:'post',data:data});
    },
    projectCompanyExport(data){
        return request({url:baseUrl+`/projectCompany/export`,method:'post',data:data,responseType:'arraybuffer',filePath:true});
    },

    correlationAdd(data,key){
        return request({url:baseUrl+`/${key}/add`,method:'put',data:data});
    },
    correlationEdit(data,key){
        return request({url:baseUrl+`/${key}/update`,method:'put',data:data});
    },
    correlationGet(id,key){
        return request({url:baseUrl+`/${key}/get/${id}`,method:'get'});
    },
    correlationDel(id,key){
        return request({url:baseUrl+`/${key}/delete/${id}`,method:'delete'});
    },
    correlationList(key){
        return request({url:baseUrl+`/${key}/list`,method:'get'});
    },
    correlationPage(data,key,menuId){
        if(menuId){
            return request({url:baseUrl+`/${key}/page/${menuId}`,method:'post',data:data});
        }else{
            return request({url:baseUrl+`/${key}/page`,method:'post',data:data});
        }
    },
    correlationProcess(data,key){
        return request({url:baseUrl+`/${key}/changeProcess`,method:'put',data:data});
    },

    //财务同步
    financeSync(data){
        return request({url:baseUrl+`/companyFinance/sync`,method:'post',data:data});
    },
    financeTotal(companyId){
        return request({url:baseUrl+`/companyFinance/totalByCompanyId/${companyId}`,method:'post'});
    },
    
    //是否在团队中
    inProject(projectId){
        return request({url:baseUrl+`/projectTeam/exist/${projectId}`,method:'get'});
    },

    //付款记录
    companyPaymentTotal(companyId){
        return request({url:baseUrl+`/companyPayment/total/${companyId}`,method:'get'});
    },
}
export default apiFun;

