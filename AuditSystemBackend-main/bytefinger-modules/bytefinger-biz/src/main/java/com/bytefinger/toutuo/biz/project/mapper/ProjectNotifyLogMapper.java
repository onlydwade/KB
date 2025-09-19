package com.bytefinger.toutuo.biz.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectNotifyLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 通告撤销日志 Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2023-01-31
 */
@Mapper
public interface ProjectNotifyLogMapper extends BaseMapper<ProjectNotifyLog> {

    /**
     * 分页查询通告撤销日志
     *
     * @param page 分页参数
     * @param wrapper 条件
     * @param params 参数
     * @return
     */
    IPage<ProjectNotifyLog> page(IPage<ProjectNotifyLog> page, @Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);
}
