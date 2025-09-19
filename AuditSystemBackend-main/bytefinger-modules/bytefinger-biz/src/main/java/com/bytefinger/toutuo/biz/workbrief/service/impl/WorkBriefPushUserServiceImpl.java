package com.bytefinger.toutuo.biz.workbrief.service.impl;

import com.bytefinger.common.core.service.ThisService;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkBriefPushUser;
import com.bytefinger.toutuo.biz.workbrief.dto.WorkPushUserDto;
import com.bytefinger.toutuo.biz.workbrief.mapper.WorkBriefPushUserMapper;
import com.bytefinger.toutuo.biz.workbrief.service.IWorkBriefPushUserService;
import com.bytefinger.toutuo.common.service.BizService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class WorkBriefPushUserServiceImpl extends BizService<WorkBriefPushUserMapper, WorkBriefPushUser> implements IWorkBriefPushUserService, ThisService<WorkBriefPushUserServiceImpl> {

    @Override
    public void deleteByBriefId(Long briefId) {
        this.baseMapper.deleteByBriefId(briefId);
    }

    /** 根据工作简报id查找推送对象 */
    @Override
    public List<WorkPushUserDto> getListByBriefId(List<Long> ids) {
        return this.baseMapper.getListByBriefId(ids);
    }
}
