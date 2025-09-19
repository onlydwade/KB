package com.bytefinger.toutuo.biz.workbrief.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.biz.customer.domain.vo.CustomerVO;
import com.bytefinger.toutuo.biz.workbrief.domain.KeyCustomerFollow;
import com.bytefinger.toutuo.biz.workbrief.vo.KeyCustomerInfoVo;

import java.util.List;


/**
 * <p>
 * 重点客户跟进情况 服务类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
public interface IKeyCustomerFollowService extends IService<KeyCustomerFollow> {

    /**
     * 根据工作简报id删除重点客户跟进情况-逻辑删除
     *
     * @param briefId
     * @return
     */
    void deleteByBriefId(Long briefId);

    /**
     * 最近2周重点客户跟进情况
     * @return
     */
    List<CustomerVO> getList();

    /**
     * 根据重点客户id查询工作摘要、跟进状态、拜访人信息
     * @return
     */
    List<KeyCustomerInfoVo> getListByCustomerId(Long customerId);

}
