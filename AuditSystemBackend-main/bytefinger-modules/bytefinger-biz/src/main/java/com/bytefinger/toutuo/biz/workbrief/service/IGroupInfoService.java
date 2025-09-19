package com.bytefinger.toutuo.biz.workbrief.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.oa.domain.OaApproval;
import com.bytefinger.toutuo.biz.workbrief.domain.GroupInfo;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkBrief;
import com.bytefinger.toutuo.biz.workbrief.dto.GroupPushDto;
import com.bytefinger.toutuo.biz.workbrief.dto.WorkBriefDto;
import com.bytefinger.toutuo.biz.workbrief.vo.WorkBriefSaveVo;

import java.util.List;


/**
 * <p>
 * 工作简报 服务类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
public interface IGroupInfoService extends IService<GroupInfo> {

    IPage<GroupInfo> page(QueryPage queryPage);

    R<GroupInfo> saveInfo(GroupInfo groupInfo);

    R<GroupInfo> getGroupById(Long id);

    R<String> deleteById(Long id);

    List<GroupPushDto> getGroupPush(List<String> groupIds,String content);

    List<Long> getUserIdsByGroupIds(List<String> groupIds);
}
