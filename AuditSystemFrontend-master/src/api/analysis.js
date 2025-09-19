import request from '@/utils/request.js'
const baseUrl = '/report';
const apiFun  = {

  // 数据看板-省市项目分布
  projectCity(currLevel,recordId,currYear,zaiguan,code){
    return request({url:baseUrl+`/dataBoard/getCity/${currLevel}/${recordId}/${currYear}/${zaiguan}/${code}`,method:'get'});
  },
  // 数据看板-项目业态占比
  getProjectYETAI(currLevel,recordId,currYear,type ,zhaiguan){
    return request({url:baseUrl+`/dataBoard/getProjectYETAI/${currLevel}/${recordId}/${currYear}/${type}/${zhaiguan}`,method:'get'});
  },
  // 数据看板-实际签约情况
  getActualSigning(currLevel,recordId,currYear){
    return request({url:baseUrl+`/dataBoard/getActualSigning/${currLevel}/${recordId}/${currYear}`,method:'get'});
  },
  //数据看板-签约趋势及排名
  getSigning(currLevel,recordId,currYear){
    return request({url:baseUrl+`/dataBoard/getSigning/${currLevel}/${recordId}/${currYear}`,method:'get'});
  },
  //数据看板-扩展模式占比
  getExpansionMode(currLevel,recordId,currYear,type){
    return request({url:baseUrl+`/dataBoard/getExpansionMode/${currLevel}/${recordId}/${currYear}/${type}`,method:'get'});
  },
  //数据看板-业绩达成情况
  getAchievements(currLevel,recordId,currYear){
    return request({url:baseUrl+`/dataBoard/getAchievementsThree/${currLevel}/${recordId}/${currYear}`,method:'get'});
  },
  //数据看板-投标情况
  getBidding(currLevel,recordId,currYear,zgType,tbType,zbType){
    return request({url:baseUrl+`/dataBoard/getBidding/${currLevel}/${recordId}/${currYear}/${zgType}/${tbType}/${zbType}`,method:'get'});
  },
  //数据看板-漏斗分析
  getFunnelAnalysis(currLevel,recordId,currYear){
    return request({url:baseUrl+`/dataBoard/getFunnelAnalysis/${currLevel}/${recordId}/${currYear}`,method:'get'});
  },
  //数据看板-项目整体情况
  getProjectSituation(currLevel,recordId,currYear){
    return request({url:baseUrl+`/dataBoard/getProjectSituationThree/${currLevel}/${recordId}/${currYear}`,method:'get'});
  },
  //数据看板-项目整体情况-详情
  getProjectSituationThreeDetails(data) {
    return request({url: baseUrl + `/dataBoard/getProjectSituationThreeDetails`,
          method: "post",
          data: data,
      });
  },
 //数据看板-项目整体情况-详情-导出
 projectSituationThreeDetailsExport(data){
  return request({url:baseUrl+`/dataBoard/projectSituationThreeDetailsExport`,method:'post',
  data:data,responseType:'arraybuffer',filePath:true});
},

 //数据看板-业绩完成情况-详情
 getAchievementsDetails(data) {
  return request({url: baseUrl + `/dataBoard/getAchievementsDetails`,
        method: "post",
        data: data,
    });
},
//数据看板-业绩完成情况-详情-导出
achievementsDetailsExport(data){
return request({url:baseUrl+`/dataBoard/achievementsDetailsExport`,method:'post',
data:data,responseType:'arraybuffer',filePath:true});
},

  //项目投标费用台帐
  projectBidFeeRegistrationPage(data){
    return request({url:baseUrl+`/projectBidFeeRegistration/page`,method:'post',data:data});
  },
  //项目投标费用台帐导出
  projectBidFeeRegistrationExport(data){
    return request({url:baseUrl+`/projectBidFeeRegistration/export`,method:'post',data:data,responseType:'arraybuffer',filePath:true});
  },
  //数据看板-项目整体情况
  getProjectSituationDept(currLevel,recordId,currYear){
    return request({url:baseUrl+`/dataBoard/getProjectSituationDept/${currLevel}/${recordId}/${currYear}`,method:'get'});
  },
}
export default apiFun;
