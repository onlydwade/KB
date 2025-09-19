package com.bytefinger.toutuo.board.aop;

import cn.hutool.core.collection.CollUtil;
import com.bytefinger.common.security.auth.AuthUtil;
import com.bytefinger.common.security.utils.SecurityUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GetDataRole {
    public   Map<String, Object> dataRole(){
        Map<String, Object> dbParams = new HashMap<>();
        String dataRole = null;
        if (AuthUtil.hasPermi("biz:data:show:all")) {
            dataRole = "SHOW_ALL";
        } else if (AuthUtil.hasPermi("biz:data:show:sub")) {
            dataRole = "SHOW_SUB";
        } else if(AuthUtil.hasPermi("biz:data:show:me")){
            dataRole = "SHOW_ME";
        }
        dbParams.put("dataRole", dataRole);
        dbParams.put("userId", SecurityUtils.getUserId());
        dbParams.put("deptId", SecurityUtils.getLoginUser().getDeptId());
        dbParams.put("postId", SecurityUtils.getLoginUser().getPostId());
        dbParams.put("deptIds", CollUtil.isNotEmpty(SecurityUtils.getLoginUser().getSubDeptIds()) ? SecurityUtils.getLoginUser().getSubDeptIds() : Arrays.asList(new Long[]{-1L}));
        return dbParams;
    }
}
