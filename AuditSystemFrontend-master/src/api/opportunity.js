import request from '@/utils/request.js'
const baseUrl = '/biz';
const apiFun = {
    getQlmAreaList(type) {
        return request({ url: `${baseUrl}/businessOpportunity/getQlmAreaList/${type}`, method: 'get' });
    },
    getQlmRealTimeDetail(id, lockStatus) {
        return request({ url: `${baseUrl}/businessOpportunity/getQlmRealTimeDetail/${id}/${lockStatus}`, method: 'get' });
    },
    getQlmAreaListByParentId(parentId) {
        return request({ url: `${baseUrl}/businessOpportunity/getQlmAreaListByParentId/${parentId}`, method: 'get' });
    },
    
    getQlmAreaListByParentIds(parentIds) {
        return request({ url: `${baseUrl}/businessOpportunity/getQlmAreaListByParentIds`, method: 'post' , data: parentIds });
    },
    getQlmRealTimeList(data) {
        return request({ url: baseUrl + `/businessOpportunity/getQlmRealTimeList`, method: 'post', data: data });
    },
}
export default apiFun;

