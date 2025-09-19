package com.bytefinger.toutuo.system.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.api.system.log.domain.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 操作日志记录 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {

}
