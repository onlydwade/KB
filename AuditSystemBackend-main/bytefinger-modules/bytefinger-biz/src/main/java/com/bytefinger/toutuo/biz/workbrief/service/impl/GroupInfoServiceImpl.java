package com.bytefinger.toutuo.biz.workbrief.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.service.ThisService;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.toutuo.biz.workbrief.domain.GroupInfo;
import com.bytefinger.toutuo.biz.workbrief.domain.GroupUser;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkBriefPushUser;
import com.bytefinger.toutuo.biz.workbrief.dto.GroupPushDto;
import com.bytefinger.toutuo.biz.workbrief.dto.GroupUserDto;
import com.bytefinger.toutuo.biz.workbrief.mapper.GroupInfoMapper;
import com.bytefinger.toutuo.biz.workbrief.mapper.GroupUserMapper;
import com.bytefinger.toutuo.biz.workbrief.service.IGroupInfoService;
import com.bytefinger.toutuo.biz.workbrief.service.IWorkBriefPushUserService;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class GroupInfoServiceImpl extends BizService<GroupInfoMapper, GroupInfo> implements IGroupInfoService, ThisService<GroupInfoServiceImpl> {

    private final GroupInfoMapper groupInfoMapper;

    private final GroupUserMapper groupUserMapper;

    @Autowired
    private IWorkBriefPushUserService iWorkBriefPushUserService;

    @DataFill
    @Override
    public IPage<GroupInfo> page(QueryPage queryPage) {

        Page<GroupInfo> pageResult = groupInfoMapper.list(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());

        if (!CollectionUtils.isEmpty(pageResult.getRecords())) {
            List<Long> groupIds = pageResult.getRecords().stream().map(GroupInfo::getId).collect(Collectors.toList());

            List<GroupUserDto> groupUserDtoList = groupInfoMapper.groupUserList(groupIds);
            Map<Long, List<GroupUserDto>> mapGroupUserDto = groupUserDtoList.stream().collect(Collectors.groupingBy(GroupUserDto::getGroupId));

            pageResult.getRecords().forEach(o -> {
                List<GroupUserDto> itemUserList = mapGroupUserDto.get(o.getId());
                if (!CollectionUtils.isEmpty(itemUserList))
                    o.setUserNames(itemUserList.stream().map(GroupUserDto::getRealname).collect(Collectors.joining(",")));

                o.setRecordType("工作简报");
            });
        }
        return pageResult;
    }

    @Override
    public R<GroupInfo> saveInfo(GroupInfo groupInfo) {
        try {
            Boolean checkNameResult = checkName(groupInfo);
            if (!checkNameResult) {
                return R.fail("群组名称不能重复");
            }
            if (groupInfo.getId() == null) {
                groupInfoMapper.insert(groupInfo);
            } else {
                groupInfoMapper.updateById(groupInfo);
            }
            saveGroupUserList(groupInfo.getId(), groupInfo.getGroupUserIdList());
            return R.ok(groupInfo);
        } catch (Exception ex) {
            return R.fail(ex.getMessage());
        }
    }

    public Boolean checkName(GroupInfo groupInfo) {

        LambdaQueryWrapper<GroupInfo> pushWq = new LambdaQueryWrapper<>();
        pushWq.eq(GroupInfo::getGroupName, groupInfo.getGroupName());
        List<GroupInfo> pushList = this.list(pushWq);

        if (groupInfo.getId() == null) {
            if (!CollectionUtils.isEmpty(pushList))
                return false;
        } else {
            if (!CollectionUtils.isEmpty(pushList)) {
                if (!pushList.get(0).getId().equals(groupInfo.getId()))
                    return false;
            }
        }
        return true;
    }

    public void saveGroupUserList(Long id, List<Long> groupUserList) {
        List<GroupUser> oldGroupUserList = groupUserMapper.selectList(Wrappers.<GroupUser>lambdaQuery().eq(GroupUser::getGroupId, id));
        if (!CollectionUtils.isEmpty(oldGroupUserList)) {
            groupUserMapper.deleteBatchIds(oldGroupUserList.stream().map(GroupUser::getId).collect(Collectors.toList()));
        }
        if (CollectionUtils.isEmpty(groupUserList))
            return;

        groupUserList.forEach(o -> {
            GroupUser item = new GroupUser();
            item.setGroupId(id);
            item.setUserId(o);
            groupUserMapper.insert(item);
        });
    }

    @Override
    public R<String> deleteById(Long id) {

        try {
            GroupInfo groupInfo = groupInfoMapper.selectById(id);
            if (groupInfo == null)
                return R.ok();

            //List<Long> userIds = new ArrayList<>(ids.stream().filter(o -> !o.contains("group_")).map(Long::parseLong).collect(Collectors.toList()));


            LambdaQueryWrapper<WorkBriefPushUser> pushUserWq = new LambdaQueryWrapper<>();
            pushUserWq.eq(WorkBriefPushUser::getUserId, "group_" + id.toString());
            pushUserWq.eq(WorkBriefPushUser::getDeleted, 0);
            List<WorkBriefPushUser> pushUserList = iWorkBriefPushUserService.list(pushUserWq);
            if (!CollectionUtils.isEmpty(pushUserList)) {
                return R.fail("群组已被使用，不允许删除");
            }

            groupInfoMapper.deleteById(id);
            return null;
        } catch (Exception ex) {
            return R.fail(ex.getMessage());
        }
    }

    @Override
    public R<GroupInfo> getGroupById(Long id) {
        GroupInfo groupInfo = groupInfoMapper.selectById(id);
        if (groupInfo == null)
            return R.fail("未找到群组信息");

        List<GroupUser> groupUserList = groupUserMapper.selectList(Wrappers.<GroupUser>lambdaQuery().eq(GroupUser::getGroupId, id));
        if (!CollectionUtils.isEmpty(groupUserList)) {
            groupInfo.setGroupUserList(groupUserList);
            groupInfo.setGroupUserIdList(groupUserList.stream().map(GroupUser::getUserId).collect(Collectors.toList()));
        }
        return R.ok(groupInfo);
    }

    public List<GroupPushDto> getGroupPush(List<String> groupIds, String content) {
        if (StringUtils.isEmpty(content) && CollectionUtils.isEmpty(groupIds))
            return Lists.newArrayList();

        List<Long> ids = new ArrayList<>();
        groupIds.forEach(o -> {
            ids.add(Long.parseLong(o.replace("group_", "")));
        });

        List<GroupInfo> groupUserList = new ArrayList<>();

        if (StringUtils.isNotEmpty(content))
            groupUserList = groupInfoMapper.selectList(Wrappers.<GroupInfo>lambdaQuery().like(GroupInfo::getGroupName, content));

        if (!CollectionUtils.isEmpty(ids))
            groupUserList = groupInfoMapper.selectList(Wrappers.<GroupInfo>lambdaQuery().in(GroupInfo::getId, ids));

        List<GroupPushDto> result = new ArrayList<>();

        groupUserList.forEach(o -> {
            GroupPushDto item = new GroupPushDto();
            item.setUserId("group_" + o.getId().toString());
            item.setRealname(o.getGroupName());
            result.add(item);
        });

        return result;
    }

    public List<Long> getUserIdsByGroupIds(List<String> groupIds) {
        if (CollectionUtils.isEmpty(groupIds))
            return Lists.newArrayList();

        List<Long> ids = new ArrayList<>();
        groupIds.forEach(o -> {
            ids.add(Long.parseLong(o.replace("group_", "")));
        });

        List<GroupUser> groupUserList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(ids))
            groupUserList = groupUserMapper.selectList(Wrappers.<GroupUser>lambdaQuery().in(GroupUser::getGroupId, ids));

        return groupUserList.stream().map(GroupUser::getUserId).collect(Collectors.toList());
    }

}
