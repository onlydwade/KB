package com.bytefinger.toutuo.biz.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.customer.domain.Customer;

/**
 * <p>
 * 业务客户表 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-25
 */
public interface ICustomerService extends IService<Customer> {

    /**
     * 新增客户
     *
     * @param customer
     * @return
     */
    R<Customer> add(Customer customer);

    String getCustomerNo();

    void addKeywords(Long id);

    /**
     * 修改客户
     *
     * @param customer
     * @return
     */
    R<Customer> update(Customer customer);

    /**
     * 认领客户
     *
     * @param customer
     * @return
     */
    R<Customer> claim(Customer customer);
    /**
     * 删除客户
     *
     * @param customerId
     * @param remarks
     * @return
     */
    R deleteById(Long customerId, String remarks);

    /**
     * 查询客户
     *
     * @param queryPage
     * @return
     */
    IPage page(QueryPage queryPage);

    /*
    * 批量删除客户
    * */
    R deleteByIds(Long[] ids);

}
