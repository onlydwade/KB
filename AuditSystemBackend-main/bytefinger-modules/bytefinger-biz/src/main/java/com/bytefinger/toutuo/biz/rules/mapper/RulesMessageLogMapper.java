package com.bytefinger.toutuo.biz.rules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.rules.domain.RulesMessageLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 规则发送消息记录 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-11-22
 */
@Mapper
public interface RulesMessageLogMapper extends BaseMapper<RulesMessageLog> {

}
