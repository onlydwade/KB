import request from '@/utils/request.js'
const baseUrl = '/biz';
const apiFun  = {
    //预算收入
    budgetInTree(){
        return request({url:baseUrl+`/budgetIn/tree`,method:'get'});
    },
    budgetInLock(recordId,currYear){
        return request({url:baseUrl+`/budgetIn/locked/${recordId}/${currYear}`,method:'get'});
    },
    budgetInUnlocked(recordId,currYear){
        return request({url:baseUrl+`/budgetIn/unlocked/${recordId}/${currYear}`,method:'get'});
    },
    budgetInGet(level,recordId,currYear){
        return request({url:baseUrl+`/budgetIn/get/${level}/${recordId}/${currYear}`,method:'get'});
    },
    budgetInGetWithSub(level,recordId,currYear){
        return request({url:baseUrl+`/budgetIn/getBySub/${level}/${recordId}/${currYear}`,method:'get'});
    },
    budgetInUpdate(level,recordId,currYear,data){
        return request({url:baseUrl+`/budgetIn/update/${level}/${recordId}/${currYear}`,method:'put',data:data});
    },

    //获取考核指标统计方式配置
    getConfigList(currYear){
        return request({url:baseUrl+`/budgetIn/getConfigList/${currYear}`,method:'get'});
    },
    //保存统计方式配置信息
    saveConfigList(data){
        return request({url:baseUrl+`/budgetIn/saveConfigList`,method:'post',data:data});
    },
    //计算
    calcPerformanceAllocationData(currYear,projectId){
        return request({url:baseUrl+`/budgetIn/calcPerformanceAllocationData/${currYear}/${projectId}`,method:'get'});
    },
    //计算
    calcPerformanceAllocationDataAll(currYear){
        return request({url:baseUrl+`/budgetIn/calcPerformanceAllocationDataAll/${currYear}`,method:'get'});
    },

    //目标指标配置
    getTargetIndicatorConfig(currYear){
        return request({url:baseUrl+`/budgetIn/getTargetIndicatorConfig/${currYear}`,method:'get'});
    },
    //保存-目标指标配置
    saveTargetIndicatorConfig(data){
        return request({url:baseUrl+`/budgetIn/saveTargetIndicatorConfig`,method:'post',data:data});
    },
    //获取目标指标数据
    getTargetIndicatorData(currYear){
        return request({url:baseUrl+`/budgetIn/getTargetIndicatorData/${currYear}`,method:'get'});
    },
    //保存-目标指标数据
    saveTargetIndicatorData(data){
        return request({url:baseUrl+`/budgetIn/saveTargetIndicatorData`,method:'post',data:data});
    },

    //获取目标指标数据
    getTargetIndicatorDataList(currYear,recordId){
        return request({url:baseUrl+`/budgetIn/getTargetIndicatorDataList/${currYear}/${recordId}`,method:'get'});
    },
    //保存-目标指标数据
    saveTargetIndicatorDataList(data){
        return request({url:baseUrl+`/budgetIn/saveTargetIndicatorDataList`,method:'post',data:data});
    },
     //保存-目标指标
     saveTargetIndicator(data){
        return request({url:baseUrl+`/budgetIn/saveTargetIndicator`,method:'post',data:data});
    },
    //锁定
    budgetInLockIndicator(recordId,currYear){
        return request({url:baseUrl+`/budgetIn/lockedIndicator/${recordId}/${currYear}`,method:'get'});
    },
    //解锁
    budgetInUnlockedIndicator(recordId,currYear){
        return request({url:baseUrl+`/budgetIn/unlockedIndicator/${recordId}/${currYear}`,method:'get'});
    },


    //预算支出
    budgetOutTree(){
        return request({url:baseUrl+`/budgetOut/tree`,method:'get'});
    },
    budgetOutLock(recordId,currYear){
        return request({url:baseUrl+`/budgetOut/locked/${recordId}/${currYear}`,method:'get'});
    },
    budgetOutUnlocked(recordId,currYear){
        return request({url:baseUrl+`/budgetOut/unlocked/${recordId}/${currYear}`,method:'get'});
    },
    budgetOutGet(level,recordId,currYear){
        return request({url:baseUrl+`/budgetOut/get/${level}/${recordId}/${currYear}`,method:'get'});
    },
    budgetOutGetWithSub(level,recordId,currYear){
        return request({url:baseUrl+`/budgetOut/getBySub/${level}/${recordId}/${currYear}`,method:'get'});
    },
    budgetOutUpdate(level,recordId,currYear,data){
        return request({url:baseUrl+`/budgetOut/update/${level}/${recordId}/${currYear}`,method:'put',data:data});
    },
    budgetOutNote(){
        return request({url:baseUrl+`/budgetOut/note`,method:'get'});
    },

    //实际签约和业绩动态
    actualInTree(){
        return request({url:baseUrl+`/actualIn/tree`,method:'get'});
    },
    actualInTreeAll(){
        return request({url:baseUrl+`/actualIn/treeAll`,method:'get'});
    },
    actualInSubmitTotal(data){
        return request({url:baseUrl+`/actualIn/submitTotal`,method:'post',data:data});
    },
    actualInPage(data){
        return request({url:baseUrl+`/actualIn/page`,method:'post',data:data});
    },
    actualInTotal(data){
        return request({url:baseUrl+`/actualIn/total`,method:'post',data:data});
    },
    actualInAchievementList(data){
        return request({url:baseUrl+`/actualIn/achievementListThree`,method:'post',data:data});
    },

    actualInExport(data){
        return request({url:baseUrl+`/actualIn/export`,method:'post',data:data,responseType:'arraybuffer',filePath:true});
    },
    actualInAchievementExport(data){
        return request({url:baseUrl+`/actualIn/exportAchievementListThree`,method:'post',data:data,responseType:'arraybuffer',filePath:true});
    },
    //实际签约汇总-报表
    actualInSummaryPage(data){
        return request({url:baseUrl+`/actualIn/summary/page`,method:'post',data:data});
    },
    //实际签约汇总-报表导出
    actualInSummaryExport(data){
        return request({url:baseUrl+`/actualIn/summary/export`,method:'post',data:data,responseType:'arraybuffer',filePath:true});
    },
    getItemConfig(){
        return request({url:baseUrl+`/budgetIn/GetItemConfig`,method:'get'})
    },
    setItemConfig(data){
        return request({url:baseUrl+`/budgetIn/SetItemConfig`,method:'post',data:data})
    },
    indicatorDisplayLevelTree(){
        return request({url:baseUrl+`/budgetIn/IndicatorDisplayLevelTree`,method:'get'})
    },
    getIndicatorDisplayLevel(fieldKey){
        return request({url:baseUrl+`/budgetIn/GetIndicatorDisplayLevel/${fieldKey}`,method:'get'})
    },
    setIndicatorDisplayLevel(fieldKey ,data){
        return request({url:baseUrl+`/budgetIn/SetIndicatorDisplayLevel/${fieldKey}`,method:'post',data:data})
    },
    ifCanDelIndicator(fieldKey){
        return request({url:baseUrl+`/budgetIn/ifCanDelIndicator/${fieldKey}`,method:'get'})
    },
    sendDataBoardTodo(data){
        return request({url:baseUrl+`/dataBoard/sendTodo`,method: 'post', data: data});
    },
    getDataBoardTodo(){
        return request({url:baseUrl+`/dataBoard/getTodo`,method: 'get'});
    },
    setDataBoardTodo(id){
        return request({url:baseUrl+`/oa/setTodoDone/${id}`,method: 'get'});
    },
}
export default apiFun;

