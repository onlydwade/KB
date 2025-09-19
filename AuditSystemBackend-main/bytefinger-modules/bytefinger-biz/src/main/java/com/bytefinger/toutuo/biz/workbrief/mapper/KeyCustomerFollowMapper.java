package com.bytefinger.toutuo.biz.workbrief.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.customer.domain.vo.CustomerVO;
import com.bytefinger.toutuo.biz.workbrief.domain.KeyCustomerFollow;
import com.bytefinger.toutuo.biz.workbrief.vo.KeyCustomerInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 重点客户跟进情况 Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2024-01-30
 */
@Mapper
public interface KeyCustomerFollowMapper extends BaseMapper<KeyCustomerFollow> {

    /**
     * 根据工作简报id删除重点客户跟进情况-逻辑删除
     *
     * @param briefId
     * @return
     */
    void deleteByBriefId(@Param("briefId") Long briefId);

    /**
     * 最近2周重点客户
     *
     * @return
     */
    List<CustomerVO> getList(@Param("startTime") String startTime, @Param("endTime")String endTime);

    /**
     * 根据重点客户id查询工作摘要、跟进状态、拜访人信息
     *
     * @return
     */
    List<KeyCustomerInfoVo> getListByCustomerId(@Param("customerId") Long customerId,@Param("startTime") String startTime, @Param("endTime")String endTime);

}
