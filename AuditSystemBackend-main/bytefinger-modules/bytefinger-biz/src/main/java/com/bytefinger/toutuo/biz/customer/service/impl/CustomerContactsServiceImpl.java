package com.bytefinger.toutuo.biz.customer.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.auth.AuthUtil;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.SysUserDeptPostInfoDTO;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import com.bytefinger.toutuo.biz.customer.domain.CustomerContacts;
import com.bytefinger.toutuo.biz.customer.mapper.CustomerContactsMapper;
import com.bytefinger.toutuo.biz.customer.mapper.CustomerMapper;
import com.bytefinger.toutuo.biz.customer.service.ICustomerContactsService;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.user.service.ISysPermissionService;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import com.bytefinger.toutuo.common.enums.DataRole;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/**
 * <p>
 * 联系人 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-25
 */
@Service
@AllArgsConstructor
public class CustomerContactsServiceImpl extends BizService<CustomerContactsMapper, CustomerContacts> implements ICustomerContactsService {

    private final CustomerMapper customerMapper;
    private final ISysUserService userService;
    private final ISysPermissionService permissionService;
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<CustomerContacts> add(CustomerContacts CustomerContacts) {
        return R.ok(super.add4Log(CustomerContacts));
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<CustomerContacts> update(CustomerContacts CustomerContacts) {
        return R.ok(super.update4Log(CustomerContacts));
    }

    @Override
    public List<CustomerContacts> getList(String customerId) {
        //管理员，创建人和跟进人
        SysUser user = userService.getById(SecurityUtils.getUserId());
        Customer customer =customerMapper.selectById(customerId);
        //是否勾选菜单“联系人加密”
        LoginUser loginUser = SecurityUtils.getLoginUser();
        boolean hasPermission=false;
//        if(!user.isAdmin()){
//            SysUserDeptPostInfoDTO currSysUserDeptPostInfo = this.permissionService.getDeptPost(loginUser.getSysUser(), loginUser.getDeptId(), loginUser.getPostId());
//            Set<String>  permissions= this.permissionService.getMenuPermission(user, currSysUserDeptPostInfo.getPostId());
//
//            if(permissions.contains("biz:customer:contacts")){
//                hasPermission=true;
//            }
//        }
        //没有跟进人,不加密
        if( Objects.isNull(customer.getFollowUserId()) ){
            hasPermission=true;
        }
        //如果时全部，可以查看
        if ( !hasPermission && AuthUtil.hasPermi(DataRole.SHOW_ALL.getRoleKey())) {
            hasPermission=true;
        }
        //如果是本人/部门及以下。判断是否在以下层级
        if ( !hasPermission &&(AuthUtil.hasPermi(DataRole.SHOW_ME.getRoleKey()) || AuthUtil.hasPermi(DataRole.SHOW_SUB.getRoleKey()) ) ) {
            if(Objects.nonNull(customer.getFollowUserId())){
                SysUser followUser= userService.getById(customer.getFollowUserId());
                if( Objects.nonNull(followUser)&& loginUser.getSubDeptIds().contains(followUser.getDeptId()) ){
                    hasPermission=true;
                }
                //没有跟进人
                if( Objects.isNull(followUser) ){
                    hasPermission=true;
                }
                //跟进人-没有所属系统业务架构
                if( Objects.nonNull(followUser)&& 0== followUser.getDeptId() ){
                    hasPermission=true;
                }
            }
        }
        List<CustomerContacts> customerContactsList=  baseMapper.selectList(Wrappers.<CustomerContacts>lambdaQuery().eq(CustomerContacts::getCustomerId, customerId));
        if(!(hasPermission||  user.isAdmin()|| SecurityUtils.getUserId().equals(customer.getCreateUserId())  ||  SecurityUtils.getUserId().equals(customer.getFollowUserId()) ||  SecurityUtils.getUserId().equals(customer.getMaintenanceUserId() )) ){
            customerContactsList.forEach(t -> {
                t.setContacts("***");
                t.setPhone("***");
                t.setOfficePhone("***");
                t.setPhoneAddr("***");
                t.setContacts("***");
            });
        }
        return customerContactsList;
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

}
