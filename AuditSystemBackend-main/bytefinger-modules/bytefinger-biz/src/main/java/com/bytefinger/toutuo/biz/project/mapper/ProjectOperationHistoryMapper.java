package com.bytefinger.toutuo.biz.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectNotifyLog;
import com.bytefinger.toutuo.biz.project.domain.ProjectOperationHistory;
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
public interface ProjectOperationHistoryMapper extends BaseMapper<ProjectOperationHistory> {


}
