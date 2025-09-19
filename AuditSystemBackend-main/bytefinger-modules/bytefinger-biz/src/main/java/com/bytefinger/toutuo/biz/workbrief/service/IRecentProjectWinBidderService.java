package com.bytefinger.toutuo.biz.workbrief.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.workbrief.domain.RecentProjectWinBidder;

import java.util.List;


/**
 * <p>
 * 近期项目中标情况 服务类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
public interface IRecentProjectWinBidderService extends IService<RecentProjectWinBidder> {


    /**
     * 根据工作简报id删除近期项目中标情况-逻辑删除
     *
     * @param briefId
     * @return
     */
    void deleteByBriefId(Long briefId);

    /**
     * 根据工作简报id查询近期项目中标情况
     *
     * @param briefId
     * @return
     */
    List<RecentProjectWinBidder> getListByBriefId(Long briefId);

    /**
     * 查询最近2周中标的项目数据
     *
     * @return
     */
    List<RecentProjectWinBidder> getList(QueryPage queryPage);
}
