package com.bytefinger.toutuo.biz.workbrief.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.workbrief.domain.RecentProjectUndertaking;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * <p>
 * 近期项目承接情况 服务类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
public interface IRecentProjectUndertakingService extends IService<RecentProjectUndertaking> {

    /**
     * 根据工作简报id删除近期项目承接情况-逻辑删除
     *
     * @param briefId
     * @return
     */
    void deleteByBriefId(Long briefId);

    /**
     * 根据工作简报id查询近期项目承接情况
     *
     * @param briefId
     * @return
     */
    List<RecentProjectUndertaking> getListByBriefId(Long briefId);

    /**
     * 查询最近2周承接的项目数据
     *
     * @return
     */
    List<RecentProjectUndertaking> getList(@RequestBody QueryPage queryPage);

}
