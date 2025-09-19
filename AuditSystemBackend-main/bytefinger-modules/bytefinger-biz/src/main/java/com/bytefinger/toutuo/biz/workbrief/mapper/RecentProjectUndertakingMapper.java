package com.bytefinger.toutuo.biz.workbrief.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.workbrief.domain.RecentProjectUndertaking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 近期项目承接情况 Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2024-01-30
 */
@Mapper
public interface RecentProjectUndertakingMapper extends BaseMapper<RecentProjectUndertaking> {

    /**
     * 根据工作简报id删除近期项目承接情况-逻辑删除
     *
     * @param briefId
     * @return
     */
    void deleteByBriefId(@Param("briefId")Long briefId);

    /**
     * 根据工作简报id查询近期项目承接情况
     *
     * @param briefId
     * @return
     */
    List<RecentProjectUndertaking> getListByBriefId(@Param("briefId")Long briefId);

    /**
     * 最近2周承接的项目数据
     *
     * @return
     */
    List<RecentProjectUndertaking> getList(@Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params, @Param("startTime") String startTime, @Param("endTime")String endTime,@Param("companyIds")List<Long> companyIds);

}
