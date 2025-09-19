import request from '@/utils/request.js'
const baseUrl = '/biz';
const apiFun = {

    oaMessageLogView(id) {
        return request({ url: `${baseUrl}/oaLog/view/${id}`, method: 'get' });
    },
    oaMessageLogDone(id) {
        return request({ url: `${baseUrl}/oaLog/done/${id}`, method: 'get' });
    },
}
export default apiFun;

