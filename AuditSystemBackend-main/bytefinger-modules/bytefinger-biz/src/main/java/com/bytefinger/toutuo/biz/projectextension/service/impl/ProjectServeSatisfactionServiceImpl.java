package com.bytefinger.toutuo.biz.projectextension.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectServeSatisfaction;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectServeSatisfactionMapper;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectServeSatisfactionService;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class ProjectServeSatisfactionServiceImpl extends BizService<ProjectServeSatisfactionMapper, ProjectServeSatisfaction> implements IProjectServeSatisfactionService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectServeSatisfaction> add(ProjectServeSatisfaction projectServeSatisfaction) {
        projectServeSatisfaction.setDeleted(0);
        return R.ok(super.addExtension4Log(projectServeSatisfaction,projectServeSatisfaction.getProjectId()));
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectServeSatisfaction> update(ProjectServeSatisfaction projectServeSatisfaction) {
        return R.ok(super.updateExtension4Log(projectServeSatisfaction,projectServeSatisfaction.getProjectId()));
    }

    @Override
    public R deleteById(Long id) {
        ProjectServeSatisfaction projectServeSatisfaction = this.getById(id);
//        //  逻辑删除
//        super.removeById(id);
//        // 记录风险状态修改
//        operLogService.changeLog(ProjectRiskManagement.class, id, "风险被删除", null, null);
        super.deleteExtension(projectServeSatisfaction,null,projectServeSatisfaction.getProjectId());
        return R.ok();
    }

}