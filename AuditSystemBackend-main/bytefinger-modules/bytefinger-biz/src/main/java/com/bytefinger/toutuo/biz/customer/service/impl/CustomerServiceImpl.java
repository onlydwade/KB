package com.bytefinger.toutuo.biz.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import com.bytefinger.toutuo.biz.customer.domain.CustomerContacts;
import com.bytefinger.toutuo.biz.customer.domain.CustomerDelete;
import com.bytefinger.toutuo.biz.customer.mapper.CustomerDeleteMapper;
import com.bytefinger.toutuo.biz.customer.mapper.CustomerMapper;
import com.bytefinger.toutuo.biz.customer.service.ICustomerContactsService;
import com.bytefinger.toutuo.biz.customer.service.ICustomerService;
import com.bytefinger.toutuo.biz.operlog.service.IOperLogService;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectCompanyLog;
import com.bytefinger.toutuo.biz.project.mapper.ProjectMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectCompanyLogService;
import com.bytefinger.toutuo.common.service.BizService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务客户表 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-25
 */
@Service
@AllArgsConstructor
public class CustomerServiceImpl extends BizService<CustomerMapper, Customer> implements ICustomerService {

    private final IOperLogService operLogService;

    private final ProjectMapper projectMapper;

    private final CustomerDeleteMapper customerDeleteMapper;
    private final IProjectCompanyLogService projectCompanyLogService;


    private final ICustomerContactsService customerContactsService;
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Customer> add(Customer customer) {
//        if(customer.getCustomerContactsList().size() == 0){
//            return R.fail("联系人为空");
//        }
        if (super.count(Wrappers.<Customer>lambdaQuery().eq(Customer::getCustomerCompanyNo, customer.getCustomerCompanyNo())) > 0) {
            return R.fail("客户数据已经存在");
        }
        customer.setCustomerNo(super.getNo());

        Customer cust= super.add4Log(customer);
        if( CollectionUtils.isNotEmpty(customer.getCustomerContactsList()) && customer.getCustomerContactsList().size() >0){
            List<CustomerContacts>  list=customer.getCustomerContactsList();
            list.forEach(t-> {
                t.setCustomerId(cust.getId().toString());
            });
            customerContactsService.saveBatch(list);
        }

        return R.ok(cust);
    }

    @Override
    public String getCustomerNo() {
        return super.getNo();
    }

    @Override
    public void addKeywords(Long id) {
        String keyword = "战略合作";
        Customer customer = getById(id);
        String keywords = customer.getKeywords();
        boolean notBlank = StringUtils.isNotBlank(keywords);
        if(notBlank && keywords.contains(keyword)){
            return;
        }
        customer.setKeywords(notBlank ? keywords + "," + keyword : keyword);
        updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Customer> update(Customer customer) {
//        if (super.count(Wrappers.<Customer>lambdaQuery()
//                .eq(Customer::getCustomerCompanyNo, customer.getCustomerCompanyNo())
//                .ne(Customer::getId, customer.getId())) > 0) {
//            return R.fail("客户数据已经存在");
//        }
        Customer cust= super.update4Log(customer);
        //如果编辑时删除所有的联系人数据，也需删除。所以用先删除后插入：原有加密的不允许删除，非加密的先删后增保存
        List<CustomerContacts>  list=customer.getCustomerContactsList();
        if(CollectionUtils.isEmpty(list) || list.size()==0){
            customerContactsService.remove(Wrappers.<CustomerContacts>lambdaQuery().eq(CustomerContacts::getCustomerId, cust.getId().toString()));
            return R.ok(cust);
        }
        //找到加密的数据，不处理
        customerContactsService.remove(Wrappers.<CustomerContacts>lambdaQuery().ne(CustomerContacts::getContacts,"***").eq(CustomerContacts::getCustomerId, cust.getId().toString()));
        list =list.stream().filter(t->!t.getContacts().equals("***")).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(list) && list.size()>0){
            list.forEach(t-> {
                t.setCustomerId(cust.getId().toString());
            });
            customerContactsService.saveBatch(list);
        }

        return R.ok(cust);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<Customer> claim(Customer customer) {
        Customer cust= super.update4Log(customer);
        return R.ok(cust);
    }

    @Override
    public R deleteById(Long customerId, String remarks) {
        Customer customer = super.getById(customerId);

        // 关键项目无法删除
        Integer projectCount = projectMapper.selectCount(Wrappers.<Project>lambdaQuery().eq(Project::getCustomerId, customerId));
        if (projectCount > 0) {
            return R.fail("有关联的项目，无法删除客户");
        }
        //删除联系人
        customerContactsService.remove(Wrappers.<CustomerContacts>lambdaQuery().eq(CustomerContacts::getCustomerId, customer.getId().toString()));

        //  逻辑删除
        super.removeById(customer.getId());

        // 记录客户状态修改
        operLogService.changeLog(Customer.class, customerId, "客户被删除", null, remarks);
        return R.ok();
    }

    /**
     * 查询全部数据
     * 拼装用户信息
     *
     * @param queryPage
     * @return
     */
    @DataFill
    @Override
    public IPage page(QueryPage queryPage) {
        return page(queryPage.toPage(), queryPage.getWrapper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteByIds(Long[] ids) {
        ArrayList<CustomerDelete> customerDeleteArrayList = new ArrayList<>();
        ArrayList<ProjectCompanyLog> projectCompanyLogs = new ArrayList<>();
        for (Customer customer : this.list(new LambdaQueryWrapper<Customer>().in(Customer::getId, ids))) {
            CustomerDelete customerDelete = new CustomerDelete();
            customerDelete.setCustomerId(customer.getId());
            customerDelete.setCustomerName(customer.getCustomerName());
            customerDelete.setCustomerNo(customer.getCustomerNo());
            customerDelete.setCustomerCompanyNo(customer.getCustomerCompanyNo());
            customerDelete.setCustomerType(customer.getCustomerType());
            customerDelete.setCooperationType(customer.getCooperationType());
            customerDelete.setCustomerIndustry(customer.getCustomerIndustry());
            customerDelete.setCustomerLevel(customer.getCustomerLevel());
            customerDelete.setProvinceCode(customer.getProvinceCode());
            customerDelete.setCityCode(customer.getCityCode());
            customerDelete.setAreaCode(customer.getAreaCode());
            customerDelete.setAddress(customer.getAddress());
            customerDelete.setRemark(customer.getRemark());
            customerDelete.setKeywords(customer.getKeywords());
            customerDelete.setLegalEntity(customer.getLegalEntity());
            customerDelete.setRegisteredCapital(customer.getRegisteredCapital());
            customerDelete.setRegisteredAddress(customer.getRegisteredAddress());
            customerDelete.setPersonnelSize(customer.getPersonnelSize());
            customerDelete.setEstablishmentDate(customer.getEstablishmentDate());
            customerDelete.setWebsite(customer.getWebsite());
            customerDelete.setCompanyIntroduction(customer.getCompanyIntroduction());
            customerDelete.setCreateUserId(customer.getCreateUserId());
            customerDelete.setCreateTime(customer.getCreateTime());
            customerDelete.setUpdateUserId(customer.getUpdateUserId());
            customerDelete.setUpdateTime(customer.getUpdateTime());
            customerDelete.setFollowTime(customer.getFollowTime());
            customerDelete.setDeleted(customer.getDeleted());
            customerDelete.setFollowUserId(customer.getFollowUserId());
            customerDelete.setOwnerUserId(customer.getOwnerUserId());
            customerDelete.setMaintenanceUserId(customer.getMaintenanceUserId());
            customerDelete.setSource(customer.getSource());
            customerDelete.setCompanyType(customer.getCompanyType());
            customerDelete.setWorkBrief(customer.getWorkBrief());
            customerDeleteArrayList.add(customerDelete);
            //删除日志
            ProjectCompanyLog projectCompanyLog = new ProjectCompanyLog();
            projectCompanyLog.setCreateTime(new Date());
            projectCompanyLog.setCreateUserId(SecurityUtils.getLoginUser().getUserid());
            projectCompanyLog.setRecordId(customer.getId());
            projectCompanyLog.setRecordCode(customer.getCustomerCompanyNo());
            projectCompanyLog.setRecordName(customer.getCustomerName());
            projectCompanyLog.setRecordType("COMPANY_DELETE");
            projectCompanyLog.setRecordTypeName("客户删除");
            projectCompanyLog.setIsReset(0);
            projectCompanyLogs.add(projectCompanyLog);
        }
        //删除客户
        List<Long> list = Arrays.asList(ids);
        this.baseMapper.deleteByIds(list);

        //数据恢复数据插入
        customerDeleteArrayList.forEach(e ->{
            customerDeleteMapper.insert(e);
        });
        //添加项目客户删除日志
        projectCompanyLogService.saveBatch(projectCompanyLogs);
        return R.ok();
    }

}
