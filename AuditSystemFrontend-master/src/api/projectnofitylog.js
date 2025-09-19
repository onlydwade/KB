import request from "@/utils/request.js";
const baseUrl = "/biz";
const apiFun = {
    pageNotifyLog(data) {
        return request({ url: baseUrl + `/projectNotifyLog/page`, method: 'post', data: data });
    },
    saveNotifyLog(data){
        return request({url:baseUrl+`/projectNotifyLog/save`,method: 'post', data: data });
    },
    deleteNotifyLog(id) {
        return request({ url: baseUrl + `/projectNotifyLog/delete/${id}`, method: 'delete'});
    },
    projectLogPage(data){
      return request({url:baseUrl+`/projectLog/page`,method:'post',data:data});
    },
    projectLogReset(data){
      return request({url:baseUrl+`/projectLog/reset`,method:'post',data:data});
    },
    
};
export default apiFun;