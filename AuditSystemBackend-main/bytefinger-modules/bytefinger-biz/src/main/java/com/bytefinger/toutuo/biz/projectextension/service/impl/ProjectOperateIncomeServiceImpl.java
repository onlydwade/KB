package com.bytefinger.toutuo.biz.projectextension.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCorrelation;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectOperateIncome;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectOperateIncomeMapper;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectOperateIncomeService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCorrelationVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectOperateIncomeVo;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class ProjectOperateIncomeServiceImpl extends BizService<ProjectOperateIncomeMapper, ProjectOperateIncome> implements IProjectOperateIncomeService {

    @Override
    @DataFill
    public List<ProjectOperateIncome> list(Long projectId,Long stepMenuId) {
        //List<ProjectOperateIncome> projectCorrelationVos = new ArrayList<>();
        LambdaQueryWrapper<ProjectOperateIncome> queryWrapper = new LambdaQueryWrapper<ProjectOperateIncome>();
        queryWrapper.eq(ProjectOperateIncome::getProjectId, projectId);
        queryWrapper.eq(ProjectOperateIncome::getDeleted, 0);
        List<ProjectOperateIncome> projectOperateIncomes = this.list(queryWrapper);

        super.dataProjectFillDcoument(stepMenuId, Lists.newArrayList(projectOperateIncomes),-1);
        return projectOperateIncomes;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectOperateIncome> add(ProjectOperateIncome projectOperateIncome) {
        projectOperateIncome.setDeleted(0);
        return R.ok(super.addExtension4Log(projectOperateIncome,projectOperateIncome.getProjectId()));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectOperateIncome> update(ProjectOperateIncome projectOperateIncome) {
        return R.ok(super.updateExtension4Log(projectOperateIncome,projectOperateIncome.getProjectId()));
    }

    @Override
    public R deleteById(Long id) {
        ProjectOperateIncome projectOperateIncome = this.getById(id);
//        //  逻辑删除
//        super.removeById(id);
//        // 记录风险状态修改
//        operLogService.changeLog(ProjectRiskManagement.class, id, "经营管理被删除", null, null);
        super.deleteExtension(projectOperateIncome,null,projectOperateIncome.getProjectId());
        return R.ok();
    }
}