package com.bytefinger.toutuo.biz.rules.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.rules.domain.Rules;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 规则管理 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-11-01
 */
public interface IRulesService extends IService<Rules> {

    /**
     * 新增规则
     *
     * @param rules
     * @return
     */
    Rules add(Rules rules);

    /**
     * 修改规则
     *
     * @param rules
     * @return
     */
    Rules update(Rules rules);

    /**
     * 分页
     *
     * @param queryPage
     * @return
     */
    IPage page(QueryPage queryPage);

    /**
     * 规则枚举
     *
     * @return
     */
    Map<String, Object> rulesEnums();

    /**
     * 规则任务执行器
     */
    void ruleTask();
}
