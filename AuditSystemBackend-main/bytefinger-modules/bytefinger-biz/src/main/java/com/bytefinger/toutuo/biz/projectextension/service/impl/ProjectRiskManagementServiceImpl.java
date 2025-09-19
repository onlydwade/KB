package com.bytefinger.toutuo.biz.projectextension.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectRiskManagementMapper;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectRiskManagementService;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProjectRiskManagementServiceImpl extends BizService<ProjectRiskManagementMapper, ProjectRiskManagement> implements IProjectRiskManagementService {
    /**
     * 查询全部数据
     * 拼装用户信息
     * @param projectId
     * @param stepMenuId
     * @return
     */
    @DataFill
    @Override
    public List<ProjectRiskManagement> listQuery(Long projectId,Long stepMenuId) {
        LambdaQueryWrapper<ProjectRiskManagement> queryWrapper = new LambdaQueryWrapper<ProjectRiskManagement>();
        queryWrapper.eq(ProjectRiskManagement::getProjectId, projectId);
        queryWrapper.eq(ProjectRiskManagement::getDeleted, 0);
        List<ProjectRiskManagement>  projectRiskManagements = this.list(queryWrapper);
        //忽略附件id
        super.dataProjectFillDcoument(stepMenuId, Lists.newArrayList(projectRiskManagements),-1);
        return projectRiskManagements;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult add(ProjectRiskManagement projectRiskManagement) {
        List<ProjectRiskManagement> projectRiskManagements = super.list(Wrappers.<ProjectRiskManagement>lambdaQuery().eq(ProjectRiskManagement::getProjectId,projectRiskManagement.getProjectId()).eq(ProjectRiskManagement::getRiskName, projectRiskManagement.getRiskName()).eq(ProjectRiskManagement::getDeleted, 0));
        if (projectRiskManagements.size() > 0) {
            return R.fail("该风险已经存在").toAjaxResult();
        }
        projectRiskManagement.setDeleted(0);
        return R.ok(super.addExtension4Log(projectRiskManagement,projectRiskManagement.getProjectId())).toAjaxResult();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult update(ProjectRiskManagement projectRiskManagement) {
        List<ProjectRiskManagement> projectRiskManagements = super.list(Wrappers.<ProjectRiskManagement>lambdaQuery().eq(ProjectRiskManagement::getProjectId,projectRiskManagement.getProjectId()).eq(ProjectRiskManagement::getRiskName, projectRiskManagement.getRiskName()).eq(ProjectRiskManagement::getDeleted, 0).notIn(ProjectRiskManagement::getId, Lists.newArrayList(projectRiskManagement.getId())));
        if (projectRiskManagements.size() > 0) {
            return R.fail("该风险已经存在").toAjaxResult();
        }
        return R.ok(super.updateExtension4Log(projectRiskManagement,projectRiskManagement.getProjectId())).toAjaxResult();
    }

    @Override
    public R deleteById(Long id) {
        ProjectRiskManagement projectRiskManagement = this.getById(id);
//        //  逻辑删除
//        super.removeById(id);
//        // 记录风险状态修改
//        operLogService.changeLog(ProjectRiskManagement.class, id, "风险被删除", null, null);
        super.deleteExtension(projectRiskManagement,null,projectRiskManagement.getProjectId());
        return R.ok();
    }
}