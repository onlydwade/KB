import request from '@/utils/request.js'
const baseUrl = '';
const apiFun  = {

    //菜单管理
    menuAdd(data){
        return request({url:baseUrl+`/biz/menu/add`,method:'put',data:data});
    },
    menuEdit(data){
        return request({url:baseUrl+`/biz/menu/update`,method:'put',data:data});
    },
    menuDel(menuId){
        return request({url:baseUrl+`/biz/menu/delete/${menuId}`,method:'delete'});
    },
    menuInfo(menuId){
        return request({url:baseUrl+`/biz/menu/get/${menuId}`,method:'get'});
    },
    menuGetByAccess(){
        return request({url:baseUrl+`/biz/menu/getRouters`,method:'get'});
    },
    menuList(){
        return request({url:baseUrl+`/biz/menu/list`,method:'get'});
    },
    menuTreeByPost(){
        return request({url:baseUrl+`/biz/menu/postMenuTreeselect`,method:'get'});
    },
    menuTree(){
        return request({url:baseUrl+`/biz/menu/treeselect`,method:'get'});
    },
    dataPermission(){
        return request({url:baseUrl+`/biz/menu/dataPermission`,method:'get'});
    },

    //部门管理
    deptAdd(data){
        return request({url:baseUrl+`/biz/dept/add`,method:'put',data:data});
    },
    deptEdit(data){
        return request({url:baseUrl+`/biz/dept/update`,method:'put',data:data});
    },
    deptDel(deptId){
        return request({url:baseUrl+`/biz/dept/delete/${deptId}`,method:'delete'});
    },
    deptInfo(deptId){
        return request({url:baseUrl+`/biz/dept/get/${deptId}`,method:'get'});
    },
    regionInfo(deptId){
      return request({url:baseUrl+`/biz/dept/region/${deptId}`,method:'get'});
    },
    deptList(){
        return request({url:baseUrl+`/biz/dept/list`,method:'get'});
    },
    listSingerType(){
        return request({url:baseUrl+`/biz/dept/listSingerType`,method:'get'});
    },   
    listLevelDeptType(){
        return request({url:baseUrl+`/biz/dept/listLevelDeptType`,method:'get'});
    },  
    getlistSingerType(){
        return request({url:baseUrl+`/biz/dept/getlistSingerType`,method:'get'});
    },
    deptExclude(deptId){
        return request({url:baseUrl+`/biz/dept/list/exclude/${deptId}`,method:'get'});
    },
    //字典
    deptMainTree(){
        return request({url:baseUrl+`/biz/mainDept/tree`,method:'get'});
    },
    //人力组织树
    hrOrgTree(){
        return request({url:baseUrl+`/biz/mainDept/hrOrg/tree`,method:'get'});
    },
    getCBOrgList(){
        return request({url:baseUrl+`/biz/mainDept/getCBOrgList`,method:'get'});
    },    
    getHrOrgIdByOrgName(orgName){
        return request({url:baseUrl+`/biz/mainDept/hrOrg/${orgName}`,method:'get'});
    },
    //岗位管理
    postAdd(data){
        return request({url:baseUrl+`/biz/post/add`,method:'put',data:data});
    },
    postEdit(data){
        return request({url:baseUrl+`/biz/post/update`,method:'put',data:data});
    },
    postDel(postId){
        return request({url:baseUrl+`/biz/post/delete/${postId}`,method:'delete'});
    },
    postInfo(postId){
        return request({url:baseUrl+`/biz/post/get/${postId}`,method:'get'});
    },
    postList(){
        return request({url:baseUrl+`/biz/post/list`,method:'get'});
    },
    postListAll(){
        return request({url:baseUrl+`/biz/post/listAll`,method:'get'});
    },
    projectPermission(projectId){
        return request({url:baseUrl+`/biz/post/list/${projectId}`,method:'get'});
    },

    //用户管理
    userAdd(data){
        return request({url:baseUrl+`/biz/user/add`,method:'put',data:data});
    },
    userEdit(data){
        return request({url:baseUrl+`/biz/user/update`,method:'put',data:data});
    },
    userResetPwd(data){
        return request({url:baseUrl+`/biz/user/resetPwd`,method:'put',data:data});
    },
    userDel(userId){
        return request({url:baseUrl+`/biz/user/delete/${userId}`,method:'delete'});
    },
    userInfo(userId){
        return request({url:baseUrl+`/biz/user/get/${userId}`,method:'get'});
    },
    userPage(data){
        return request({url:baseUrl+`/biz/user/page`,method:'post',data:data});
    },
    userGroupPage(data){
        return request({url:baseUrl+`/biz/user/groupPage`,method:'post',data:data});
    },
    projectExtensionUserPage(data){
      return request({url:baseUrl+`/biz/projectExtension/userPage`,method:'post',data:data});
    },
    deptUserTree(){
        return request({url:baseUrl+`/biz/user/currDeptTreeTwo`,method:'get'});
    },
    postUserTree(){
        return request({url:baseUrl+`/biz/user/currPostTree`,method:'get'});
    },

    //用户部门岗位管理
    userDeptAdd(data){
        return request({url:baseUrl+`/biz/userDeptPost/addDept`,method:'put',data:data});
    },
    userDeptPostAdd(data){
        return request({url:baseUrl+`/biz/userDeptPost/addDeptPost`,method:'put',data:data});
    },
    userDeptPostDel(data){
        return request({url:baseUrl+`/biz/userDeptPost/deleteDeptPost`,method:'delete',data:data});
    },
    userDeptPostInfo(userId,deptId,postId){
        return request({url:baseUrl+`/biz/userDeptPost/get/${userId}/${deptId}/${postId}`,method:'get'});
    },
    userDeptPostList(userId){
        return request({url:baseUrl+`/biz/userDeptPost​/list​/${userId}`,method:'get'});
    },
    userDeptPostEdit(data){
        return request({url:baseUrl+`/biz/userDeptPost/update`,method:'put',data:data});
    },

    //通知公告
    messageCount(){
        return request({url:baseUrl+`/system/message/countMessage`,method:'get'});
    },
    messageMark(data){
        return request({url:baseUrl+`/system/message/markMessage`,method:'post',data:data});
    },
    messageInfo(id){
        return request({url:baseUrl+`/system/message/get/${id}`,method:'get'});
    },
    messageOpen(id){
        return request({url:baseUrl+`/system/message/open/${id}`,method:'get'});
    },
    messagePage(data){
        return request({url:baseUrl+`/system/message/page`,method:'post',data:data});
    },

    noticeAdd(data){
        return request({url:baseUrl+`/system/notice/add`,method:'put',data:data});
    },
    noticeEdit(data){
        return request({url:baseUrl+`/system/notice/update`,method:'put',data:data});
    },
    noticeDel(id){
        return request({url:baseUrl+`/system/notice/delete/${id}`,method:'delete'});
    },
    noticeInfo(id){
        return request({url:baseUrl+`/system/notice/get/${id}`,method:'get'});
    },
    noticePage(data){
        return request({url:baseUrl+`/system/notice/page`,method:'post',data:data});
    },

    //字典
    dictList(){
        return request({url:baseUrl+`/system/dict/list`,method:'get'});
    },
    //新增字典
    addDictType(data){
      return request({url:baseUrl+`/system/dict/addDictType`,method:'post',data:data});
    },
    // 字典表管理左侧table查询
    getEditTypeList(type){
        return request({url:baseUrl+`/system/dict/getEditTypeList`+'?dictName='+ type,method:'get'});
    },
    // 字典表管理左侧table查询
    getDataListByType(type){
        return request({url:baseUrl+`/system/dict/getDataListByType`+'?dictType='+ type,method:'get'});
    },
    // 字典表管理左侧table查询
    addDataInfo(data){
        return request({url:baseUrl+`/system/dict/addDataInfo`,method:'post',data:data});
    },
    // 项目
    projectList(data){
        return request({url:baseUrl+`/biz/projectCorrelation/projectRelevance`,method:'post',data:data});
    }

}
export default apiFun;

