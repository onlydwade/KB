import request from '@/utils/request.js'
const baseUrl = '/biz';
const apiFun  = {
    
    //档案管理
    archivesPage(data){
        return request({url:baseUrl+`/projectDocument/page`,method:'post',data:data});
    },
}
export default apiFun;

