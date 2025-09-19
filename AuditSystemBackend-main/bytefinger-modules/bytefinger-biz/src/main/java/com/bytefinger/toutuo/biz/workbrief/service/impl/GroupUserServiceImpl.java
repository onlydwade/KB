package com.bytefinger.toutuo.biz.workbrief.service.impl;

import com.bytefinger.common.core.service.ThisService;
import com.bytefinger.toutuo.biz.workbrief.domain.GroupUser;
import com.bytefinger.toutuo.biz.workbrief.mapper.GroupUserMapper;
import com.bytefinger.toutuo.biz.workbrief.service.IGroupUserService;
import com.bytefinger.toutuo.common.service.BizService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作简报推送对象 服务实现类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
@Service
@AllArgsConstructor
public class GroupUserServiceImpl extends BizService<GroupUserMapper, GroupUser> implements IGroupUserService, ThisService<GroupUserServiceImpl> {

    
}
