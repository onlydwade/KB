package com.bytefinger.toutuo.biz.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 业务客户表 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-10-25
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    /*
    * 物理删除客户数据
    * */
    void deleteByIds( @Param("ids") List<Long> ids);

    void add(@Param("cu") Customer customer);

}
