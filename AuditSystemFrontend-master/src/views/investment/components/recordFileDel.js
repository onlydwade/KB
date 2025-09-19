import api                         from '@/api/index';
import { message  }                from 'ant-design-vue';
export function useRecordFileDel(){
    const delRecordFile = (fileList)=>{
        return new Promise(function(resolve, reject) {
            let promises = [];
            fileList.forEach((item, i) => {
                item.projectCompanyDocumentList.forEach((file, k) => {
                    let delFun  = api.investment.documentDel(file.id)
                    promises.push(delFun)
                });
            });
            Promise.all(promises).then(posts=>{
                resolve('del_success');
            }).catch(reason=>{
                reject('del_filed');
            });
        });
    }
    return {
        delRecordFile
    }
}