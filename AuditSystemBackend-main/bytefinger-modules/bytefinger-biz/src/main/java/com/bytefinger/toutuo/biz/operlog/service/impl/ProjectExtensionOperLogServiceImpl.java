package com.bytefinger.toutuo.biz.operlog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.toutuo.biz.operlog.domain.OperLog;
import com.bytefinger.toutuo.biz.operlog.domain.ProjectExtensionOperLog;
import com.bytefinger.toutuo.biz.operlog.domain.vo.UpdateLog;
import com.bytefinger.toutuo.biz.operlog.mapper.OperLogMapper;
import com.bytefinger.toutuo.biz.operlog.mapper.ProjectExtensionOperLogMapper;
import com.bytefinger.toutuo.biz.operlog.service.IOperLogService;
import com.bytefinger.toutuo.biz.operlog.service.IProjectExtensionOperLogService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务数据变更记录表 服务实现类
 * </p>
 *
 * @author ycj
 * @since 2023-04-03
 */
@Service
@AllArgsConstructor
public class ProjectExtensionOperLogServiceImpl extends ServiceImpl<ProjectExtensionOperLogMapper, ProjectExtensionOperLog> implements IProjectExtensionOperLogService {

    private final RemoteUserService remoteUserService;

    @Override
    public void ruleLogBySystem(String modelName, Long dataId, UpdateLog updateLog,Long projectId) {
        ProjectExtensionOperLog operLog = new ProjectExtensionOperLog();
        operLog.setRecordId(dataId);
        operLog.setModuleName(modelName);
        operLog.setUpdateUserId(1L);
        operLog.setUpdateUserName("系统");
        operLog.setProjectId(projectId);
        operLog.setUpdateLog(JSON.toJSONString(updateLog));
        this.save(operLog);
    }

    @Override
    public void changeLog(String modelName, Long dataId, String log,Long projectId) {
        ProjectExtensionOperLog operLog = new ProjectExtensionOperLog();
        operLog.setRecordId(dataId);
        operLog.setModuleName(modelName);
        operLog.setUpdateDeptId(SecurityUtils.getLoginUser().getDeptId());
        operLog.setUpdatePostId(SecurityUtils.getLoginUser().getPostId());
        operLog.setUpdateLog(JSON.toJSONString(Lists.newArrayList(UpdateLog.builder().fieldName(log).build())));
        operLog.setProjectId(projectId);
        this.save(operLog);
    }

    @Override
    public void changeLog(Class clazz, Long dataId, String fieldName, String valueBefore, String valueAfter,Long projectId) {
        try {
            ProjectExtensionOperLog operLog = new ProjectExtensionOperLog();
            operLog.setRecordId(dataId);
            operLog.setModuleName(clazz.getSimpleName());
            operLog.setUpdateDeptId(SecurityUtils.getLoginUser().getDeptId());
            operLog.setUpdatePostId(SecurityUtils.getLoginUser().getPostId());
            operLog.setProjectId(projectId);
            operLog.setUpdateLog(JSON.toJSONString(Lists.newArrayList(UpdateLog.builder()
                    .fieldName(fieldName)
                    .valueBefore(null == valueBefore ? "" : valueBefore)
                    .valueAfter(null == valueAfter ? "" : valueAfter)
                    .build())));

            this.save(operLog);

        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<ProjectExtensionOperLog> listByModuleAndRecordId(Long projectId) {
        List<ProjectExtensionOperLog> olList = super.list(Wrappers.<ProjectExtensionOperLog>lambdaQuery()
                .eq(ProjectExtensionOperLog::getProjectId, projectId)
                .orderByDesc(ProjectExtensionOperLog::getUpdateTime));

        List<Long> userIds = olList.stream().map(ProjectExtensionOperLog::getUpdateUserId).collect(Collectors.toList());

        // 拼装数据
        R<List<UserVO>> result = remoteUserService.list(userIds, SecurityConstants.INNER);
        if (result.isSuccess()) {
            for (ProjectExtensionOperLog operLog : olList) {
                operLog.setUpdateUser(
                        result.getData().stream().filter(u -> u.getUserId().equals(operLog.getUpdateUserId())).findFirst().orElse(null)
                );
            }
        }

        return olList;
    }

    @DataFill
    @Override
    public IPage<ProjectExtensionOperLog> pageData(IPage<ProjectExtensionOperLog> page, Wrapper<ProjectExtensionOperLog> queryWrapper) {
        return super.page(page, queryWrapper);
    }

}
