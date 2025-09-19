import request from '@/utils/request.js'
const baseUrl = '/biz';
const apiFun = {

    oaSetTodoDone(id) {
        return request({ url: `${baseUrl}/oa/setTodoDone/${id}`, method: 'get' });
    },
}
export default apiFun;

