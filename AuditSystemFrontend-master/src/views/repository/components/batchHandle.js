import api            from '@/api/index';
import { message }    from 'ant-design-vue';
import { dataToFile } from '@/utils/tools';
import { mainStore }  from '@/store';
const store = mainStore();
export function useBatchHandle(type,options){   //默认page   暂未开发其他拓展
    const configs = options || {
        dataKey     : 'id',
        fileListKey : 'projectCompanyDocumentList'
    }
    const data    = reactive({
        list : [],
        total : 0,
    })
    
    //批量操作
    const batchDownLoad = (record,operation)=>{
        let downData = [];
        console.log(operation)
        let arr = operation?[record]:data.list;
        arr.forEach((item, i) => {
            if(batchIds.value.includes(item[configs.dataKey]) || operation){
                if(item.docmentObject){
                    let fileObj = JSON.parse(item.docmentObject);
                    downData.push({
                        name    : fileObj.name,
                        ossPath : fileObj.ossPath,
                        url     : fileObj.url
                    })
                }else{
                    (item.documentTemplateList || []).forEach((fileList, k) => {
                        (fileList[configs.fileListKey] || []).forEach(file=>{
                            let fileObj = JSON.parse(file.docmentObject);
                            downData.push({
                                name    : fileObj.name,
                                ossPath : fileObj.ossPath,
                                url     : fileObj.url
                            })
                        })
                    });
                }
                
            }
        });
        if(downData.length==0){
            message.warning('抱歉，所选数据中未包含可下载文件！');
            return;
        }
        store.spinChange(1);
        if(downData.length==1){
            api.common.downloadFile({
                ossPath : downData[0].ossPath,
                name    : downData[0].name
            }).then(res=>{
                store.spinChange(-1);
                dataToFile(res,downData[0].name);
                batchIds.value = [];
            })
            return;
        }
        api.common.downloadBatch(downData).then(res=>{
            store.spinChange(-1);
            dataToFile(res,(new Date()).getTime()+'.zip');
            batchIds.value = [];
        })
    }
    const batchIds = ref([]);
    const rowSelection = ref({
        checkStrictly   : false,
        selectedRowKeys : batchIds,
        onChange        : (selectedRowKeys, selectedRows) => {
            batchIds.value = selectedRowKeys;
        },
    });
    return {
        data,
        batchIds,
        rowSelection,
        batchDownLoad,
    }
}
