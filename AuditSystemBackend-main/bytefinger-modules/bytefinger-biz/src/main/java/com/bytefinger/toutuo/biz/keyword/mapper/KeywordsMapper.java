package com.bytefinger.toutuo.biz.keyword.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.keyword.domain.Keywords;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 业务关键词 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-10-19
 */
@Mapper
public interface KeywordsMapper extends BaseMapper<Keywords> {

}
