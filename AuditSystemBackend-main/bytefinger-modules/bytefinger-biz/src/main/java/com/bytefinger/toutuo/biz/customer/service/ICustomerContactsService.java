package com.bytefinger.toutuo.biz.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.customer.domain.CustomerContacts;

import java.util.List;

/**
 * <p>
 * 联系人表 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-25
 */
public interface ICustomerContactsService extends IService<CustomerContacts> {

    /**
     * 新增客户
     *
     * @param list
     * @return
     */
    R<CustomerContacts> add(CustomerContacts list);


    /**
     * 修改客户
     *
     * @param customer
     * @return
     */
    R<CustomerContacts> update(CustomerContacts customer);

    List<CustomerContacts> getList(String customerId);
    /**
     * 查询客户
     *
     * @param queryPage
     * @return
     */
    IPage page(QueryPage queryPage);

}
