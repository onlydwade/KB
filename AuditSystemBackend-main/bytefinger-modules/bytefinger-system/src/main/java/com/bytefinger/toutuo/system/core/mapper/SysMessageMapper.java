package com.bytefinger.toutuo.system.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.api.system.core.domain.SysMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统个人消息 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-10-10
 */
@Mapper
public interface SysMessageMapper extends BaseMapper<SysMessage> {

}
