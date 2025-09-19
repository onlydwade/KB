package com.bytefinger.toutuo.biz.operlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.operlog.domain.ProjectExtensionOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 业务数据变更记录表 Mapper 接口
 * </p>
 *
 * @author ycj
 * @since 2023-04-03
 */
@Mapper
public interface ProjectExtensionOperLogMapper extends BaseMapper<ProjectExtensionOperLog> {

}
