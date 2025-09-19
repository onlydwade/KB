package com.bytefinger.toutuo.common.aop.fill.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bytefinger.toutuo.common.enums.DataRole;
import com.bytefinger.toutuo.common.service.FillDataService;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.exception.ServiceException;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.auth.AuthUtil;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 1、切面方法执行前，设置用户数据权限
 * 2、切面方法执行后，设置用户操作权限 + 数据回写
 *
 * @author patrick
 */
@Aspect
@Component
@AllArgsConstructor
@Slf4j
public class FillAspect {

    private final FillDataService fillDataService;

    private static final String WEI_DU_KEY = "weiDu";

    /**
     * 设置用户数据权限
     *
     * @param point
     * @param dataFill
     */
    @Before(value = "@annotation(dataFill)")
    public void doBefore(JoinPoint point, DataFill dataFill) {
        Object[] args = point.getArgs();
        if (null != args && args.length > 0) {
            for (Object params : args) {
                if (null != params && params instanceof QueryPage) {
                    QueryPage queryPage = (QueryPage) params;

                    String dataRole = null;

                    // 前端传入的
                    if (queryPage.getParams().containsKey(WEI_DU_KEY)) {
                        Object weiDu = queryPage.getParams().get(WEI_DU_KEY);
                        if (null != weiDu && weiDu instanceof String) {
                            dataRole = weiDu.toString();
                        }
                        queryPage.getParams().remove(WEI_DU_KEY);
                    }
                    log.info("****************************************dataRole01:{}",dataRole);
                    // 验证是否有查看下属数据权限
                    if (null != dataRole) {
                        if (ArrayUtil.contains(new String[]{DataRole.SHOW_SUB_GUI_SHU_JI_WO_XIA_JI_DE.getCode(), DataRole.SHOW_SUB_XIA_JI_CAN_YU_DE.getCode()}, dataRole)) {
                            // 无此权限
                            if (!AuthUtil.hasPermi(DataRole.SHOW_SUB.getRoleKey()) && !AuthUtil.hasPermi(DataRole.SHOW_ALL.getRoleKey())) {
                                throw new ServiceException("请求数据不正确");
                            }
                        }
                    } else {
                        if (AuthUtil.hasPermi(DataRole.SHOW_ALL.getRoleKey())) {
                            dataRole = DataRole.SHOW_ALL.getCode();
                        } else if (AuthUtil.hasPermi(DataRole.SHOW_SUB.getRoleKey())) {
                            dataRole = DataRole.SHOW_SUB.getCode();
                        } else {
                            dataRole = DataRole.SHOW_ME.getCode();
                        }
                    }
                    log.info("****************************************dataRole01:{}",dataRole);

                    // 设置用户相关数据
                    queryPage.getDbParams().put("dataRole", dataRole);
                    queryPage.getDbParams().put("userId", SecurityUtils.getUserId());
                    queryPage.getDbParams().put("deptId", SecurityUtils.getLoginUser().getDeptId());
                    queryPage.getDbParams().put("postId", SecurityUtils.getLoginUser().getPostId());
                    queryPage.getDbParams().put("deptIds", CollUtil.isNotEmpty(SecurityUtils.getLoginUser().getSubDeptIds()) ? SecurityUtils.getLoginUser().getSubDeptIds() : Arrays.asList(new Long[]{-1L}));
                }
            }
        }
    }

    /**
     * 切面方法执行后，设置用户操作权限 + 数据回写
     * 1、获取返回值
     * 2、查询返回值中是否有指定属性
     * 3、获取指定属性数据
     * 4、批量查询数据
     * 5、反写数据
     *
     * @param point
     * @param dataFill
     */
    @AfterReturning(pointcut = "@annotation(dataFill)", returning = "result")
    public void doAfterReturning(JoinPoint point, DataFill dataFill, Object result) {
        if (null != result) {
            if (result instanceof IPage) {
                fillDataService.filler(((IPage) result).getRecords());
            } else if (result instanceof List) {
                fillDataService.filler((List) result);
            } else if (result instanceof BaseEntity) {
                fillDataService.filler(Lists.newArrayList((BaseEntity) result));
            } else if (result instanceof R) {
                Object data = ((R) result).getData();
                if (null != data) {
                    if (data instanceof List) {
                        fillDataService.filler((List) data);
                    } else if (data instanceof BaseEntity) {
                        fillDataService.filler(Lists.newArrayList((BaseEntity) result));
                    }
                }
            }
        }
    }

}
