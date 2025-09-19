package com.bytefinger.toutuo.biz.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.customer.domain.CustomerContacts;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 联系人表 Mapper 接口
 * </p>
 *
 * @author chengwei
 * @since 2022-10-25
 */
@Mapper
public interface CustomerContactsMapper extends BaseMapper<CustomerContacts> {

}
