import request from '@/utils/request.js'
const baseUrl = '/biz';
const apiFun = {

    workBriefinitTitle() {
        return request({ url: baseUrl + `/workBrief/initTitle`, method: 'get'});
    },
    workBriefStaging(data) {
        return request({ url: baseUrl + `/workBrief/staging`, method: 'post', data: data });
    },
    workBriefSave(data) {
        return request({ url: baseUrl + `/workBrief/save`, method: 'post', data: data });
    },
    workBriefDel(id) {
        return request({ url: baseUrl + `/workBrief/delete/${id}`, method: 'delete'});
    },
    workBriefInfo(id) {
        return request({ url: baseUrl + `/workBrief/view/${id}`, method: 'get' });
    },
    workBriefPage(data) {
        return request({ url: baseUrl + `/workBrief/page`, method: 'post', data: data });
    },
    workBriefView(id) {
        return request({ url: `${baseUrl}/workBrief/view/${id}`, method: 'get' });
    },
    workBriefViewRead(id,read) {
        return request({ url: `${baseUrl}/workBrief/viewRead?id=${id}&read=${read}`, method: 'get' });
    },
    winBidderGetList(data){
        return request({url:baseUrl+`/recentProject/winBidder/getList`,method:'post',data:data});
    },
    undertakingGetList(data){
        return request({url:baseUrl+`/recentProject/undertaking/getList`,method:'post',data:data});
    },
    keyProjectGetList(data){
        return request({url:baseUrl+`/keyCustomer/follow/getList`,method:'post',data:data});
    },
    getListByCustomerId(id) {
        return request({ url: `${baseUrl}/keyCustomer/follow/getListByCustomerId/${id}`, method: 'get' });
    },
    getGroupWorkList(data) {
        return request({ url: baseUrl + `/groupInfo/page`, method: 'post', data: data });
    },
    saveGroup(data) {
        return request({ url: baseUrl + `/groupInfo/save`, method: 'post', data: data });
    },
    getGroup(id) {
        return request({ url: baseUrl + `/groupInfo/get?id=`+id, method: 'get'});
    },
    deleteGroup(id) {
        return request({ url: baseUrl + `/groupInfo/delete?id=`+id, method: 'get'});
    },
    getcheckOa() {
        return request({ url: baseUrl + `/workBrief/getcheckOa`, method: 'get'});
    },
    setcheckOa(checkValue) {
        return request({ url: baseUrl + `/workBrief/setcheckOa?checkValue=`+checkValue, method: 'get'});
    },
    workBriefPageNotify(data) {
        return request({ url: baseUrl + `/workBrief/pageNotify`, method: 'post', data: data });
    },
    workBriefNotifyExport(data){
        return request({url:baseUrl+`/workBrief/exportNotify`,method:'post',data:data,responseType:'arraybuffer',filePath:true});
    },
}
export default apiFun;

