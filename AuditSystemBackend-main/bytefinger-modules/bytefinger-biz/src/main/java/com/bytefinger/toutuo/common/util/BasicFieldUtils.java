package com.bytefinger.toutuo.common.util;

import com.bytefinger.common.core.utils.SpringUtils;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.toutuo.api.biz.user.domain.SysDept;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import com.bytefinger.toutuo.biz.customer.service.ICustomerService;
import com.bytefinger.toutuo.biz.user.service.ISysDeptService;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;

/***
 * @Description: 基础字段id转name工具类
 * @Param:
 * @Return:
 * @Author: Jone Ying
 * @Date: 2023/2/4
**/
public class BasicFieldUtils {



    /**
     * 获取用户姓名
     *
     */
    public static String getRealName(Long userId) {
        SysUser user = SpringUtils.getBean(ISysUserService.class).getById(userId);
        if(null == user){
            return "";
        }
        return user.getRealname();
    }

    /**
     * 获取部门名称
     *
     */
    public static String getDeptName(Long deptId) {
        SysDept dept = SpringUtils.getBean(ISysDeptService.class).getById(deptId);
        if(null == dept){
            return "";
        }
        return dept.getDeptName();
    }

    private static String getCustomerName(Long customerId) {
        Customer customer = SpringUtils.getBean(ICustomerService.class).getById(customerId);
        if(null == customer){
            return "";
        }
        return customer.getCustomerName();
    }

    public static String getName(Excel.DataType dataType, Object o) {
        if(Excel.DataType.USER.name().equals(dataType.name())){
            return getRealName((Long) o);
        }else if(Excel.DataType.DEPT.name().equals(dataType.name())){
            return getDeptName((Long) o);
        }else if(Excel.DataType.CUSTOMER.name().equals(dataType.name())){
            return getCustomerName((Long) o);
        }
        return null;
    }


}