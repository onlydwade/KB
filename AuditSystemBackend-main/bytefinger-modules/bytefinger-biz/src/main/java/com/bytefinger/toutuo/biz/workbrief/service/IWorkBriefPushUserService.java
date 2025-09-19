package com.bytefinger.toutuo.biz.workbrief.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkBriefPushUser;
import com.bytefinger.toutuo.biz.workbrief.dto.WorkPushUserDto;

import java.util.List;


/**
 * <p>
 * 工作简报推送对象 服务类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
public interface IWorkBriefPushUserService extends IService<WorkBriefPushUser> {

    /**
     * 根据工作简报id删除工作简报推送对象-逻辑删除
     *
     * @param briefId
     * @return
     */
    void deleteByBriefId(Long briefId);

    /**
     * 根据工作简报id查找推送对象
     *
     * @param ids
     * @return
     */
    List<WorkPushUserDto> getListByBriefId(List<Long> ids);

}
