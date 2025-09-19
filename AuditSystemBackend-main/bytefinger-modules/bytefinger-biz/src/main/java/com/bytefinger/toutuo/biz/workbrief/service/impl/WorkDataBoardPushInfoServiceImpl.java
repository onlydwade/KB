package com.bytefinger.toutuo.biz.workbrief.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.service.ThisService;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.SysDept;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.biz.user.service.ISysDeptService;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import com.bytefinger.toutuo.biz.workbrief.domain.SendDataBoard;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkDataBoardPushInfo;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkDataBoardPushUser;
import com.bytefinger.toutuo.biz.workbrief.dto.WorkPushUserDto;
import com.bytefinger.toutuo.biz.workbrief.mapper.WorkDataBoardPushInfoMapper;
import com.bytefinger.toutuo.biz.workbrief.service.IGroupInfoService;
import com.bytefinger.toutuo.biz.workbrief.service.ISendDataBoardService;
import com.bytefinger.toutuo.biz.workbrief.service.IWorkDataBoardPushInfoService;
import com.bytefinger.toutuo.biz.workbrief.service.IWorkDataBoardPushUserService;
import com.bytefinger.toutuo.common.service.BizService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class WorkDataBoardPushInfoServiceImpl extends BizService<WorkDataBoardPushInfoMapper, WorkDataBoardPushInfo> implements IWorkDataBoardPushInfoService, ThisService<WorkDataBoardPushInfoServiceImpl> {

    @Autowired
    IGroupInfoService groupInfoService;

    @Autowired
    IWorkDataBoardPushUserService iWorkDataBoardPushUserService;

    @Autowired
    ISendDataBoardService sendDataBoardService;

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    ISysDeptService sysDeptService;

    @Override
    public void sendDataBoard(SendDataBoard sendDataBoard) {

        Set<String> pushUserList = new HashSet<>();
        //分组的用户
        List<String> groupIds = sendDataBoard.getPushUserList().stream().filter(o -> o.contains("group_")).collect(Collectors.toList());
        List<Long> groupUserIds = groupInfoService.getUserIdsByGroupIds(groupIds);
        groupUserIds.forEach(id -> pushUserList.add(id.toString()));


        //没分组的用户
        List<String> userIds = sendDataBoard.getPushUserList().stream().filter(o -> !o.contains("group_")).collect(Collectors.toList());
        pushUserList.addAll(userIds);

        WorkDataBoardPushInfo workDataBoardPushInfo = new WorkDataBoardPushInfo();
        workDataBoardPushInfo.setDeptId(sendDataBoard.getDeptId().toString());
        workDataBoardPushInfo.setYear(sendDataBoard.getYear().toString());
        this.save(workDataBoardPushInfo);

        List<SysUser> users = sysUserService.list(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, pushUserList));

        for (String userId:pushUserList) {

            WorkDataBoardPushUser workDataBoardPushUser = new WorkDataBoardPushUser();
            workDataBoardPushUser.setDataId(workDataBoardPushInfo.getId());
            workDataBoardPushUser.setUserId(userId);

            List<SysUser> itemUsers = users.stream().filter(m -> m.getUserId().toString().equals(userId)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(itemUsers))
                workDataBoardPushUser.setUserName(itemUsers.get(0).getRealname());

            workDataBoardPushUser.setPushStatus(1);
            iWorkDataBoardPushUserService.save(workDataBoardPushUser);

        }
        sendDataBoardService.sendTodo(pushUserList, sendDataBoard.getYear(), sendDataBoard.getDeptId());
    }

    @Override
    public List<WorkDataBoardPushInfo> getSendList() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<WorkDataBoardPushInfo> workDataBoardPushInfoList = this.list(Wrappers.<WorkDataBoardPushInfo>lambdaQuery().in(WorkDataBoardPushInfo::getCreateUserId, loginUser.getUserid()).orderByDesc(WorkDataBoardPushInfo::getCreateTime));
        if (CollectionUtils.isEmpty(workDataBoardPushInfoList))
            return new ArrayList<>();

        List<SysDept> deptList = sysDeptService.list(Wrappers.<SysDept>lambdaQuery().in(SysDept::getDeptId, workDataBoardPushInfoList.stream().map(WorkDataBoardPushInfo::getDeptId).collect(Collectors.toList())));

        for (int i = 0; i < workDataBoardPushInfoList.size(); i++) {
            List<WorkDataBoardPushUser> userList = iWorkDataBoardPushUserService.list(Wrappers.<WorkDataBoardPushUser>lambdaQuery().in(WorkDataBoardPushUser::getDataId, workDataBoardPushInfoList.get(i).getId()));
            workDataBoardPushInfoList.get(i).setPushUserList(userList);

            int finalI = i;
            List<SysDept> itemDept = deptList.stream().filter(m -> m.getDeptId().toString().equals(workDataBoardPushInfoList.get(finalI).getDeptId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(itemDept)) {
                workDataBoardPushInfoList.get(i).setDeptName(itemDept.get(0).getDeptName());
            }

        }
        return workDataBoardPushInfoList;
    }

}
