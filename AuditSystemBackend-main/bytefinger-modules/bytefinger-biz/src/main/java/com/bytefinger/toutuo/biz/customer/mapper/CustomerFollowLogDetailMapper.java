package com.bytefinger.toutuo.biz.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.customer.domain.CustomerContacts;
import com.bytefinger.toutuo.biz.customer.domain.CustomerFollowLogDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 客户动态明细表 Mapper 接口
 * </p>
 *
 * @author chengwei
 * @since 2022-10-25
 */
@Mapper
public interface CustomerFollowLogDetailMapper extends BaseMapper<CustomerFollowLogDetail> {

}
