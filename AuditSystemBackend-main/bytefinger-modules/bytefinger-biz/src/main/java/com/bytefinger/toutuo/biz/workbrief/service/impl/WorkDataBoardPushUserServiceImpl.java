package com.bytefinger.toutuo.biz.workbrief.service.impl;

import com.bytefinger.common.core.service.ThisService;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkDataBoardPushUser;
import com.bytefinger.toutuo.biz.workbrief.dto.WorkPushUserDto;
import com.bytefinger.toutuo.biz.workbrief.mapper.WorkDataBoardPushUserMapper;
import com.bytefinger.toutuo.biz.workbrief.service.IWorkDataBoardPushUserService;
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
public class WorkDataBoardPushUserServiceImpl extends BizService<WorkDataBoardPushUserMapper, WorkDataBoardPushUser> implements IWorkDataBoardPushUserService, ThisService<WorkDataBoardPushUserServiceImpl> {


}
