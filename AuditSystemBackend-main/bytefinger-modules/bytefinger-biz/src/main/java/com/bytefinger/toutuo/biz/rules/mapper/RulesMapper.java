package com.bytefinger.toutuo.biz.rules.mapper;

import com.bytefinger.toutuo.biz.rules.domain.Rules;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 规则管理 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-11-01
 */
@Mapper
public interface RulesMapper extends BaseMapper<Rules> {

}
