package com.bytefinger.toutuo.biz.operlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.operlog.domain.OperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 业务数据变更记录表 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-10-12
 */
@Mapper
public interface OperLogMapper extends BaseMapper<OperLog> {

}
