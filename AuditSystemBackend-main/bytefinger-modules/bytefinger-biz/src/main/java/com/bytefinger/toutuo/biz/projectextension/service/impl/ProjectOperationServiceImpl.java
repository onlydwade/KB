package com.bytefinger.toutuo.biz.projectextension.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.enums.ProjectStatus;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectOperation;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectOperationMapper;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectOperationService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCollectionRateVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectOperationVo;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectOperationServiceImpl extends BizService<ProjectOperationMapper, ProjectOperation> implements IProjectOperationService {
    @Autowired
    private IProjectService projectService;




    @Override
    public ProjectOperationVo get(Long id){
        LambdaQueryWrapper<ProjectOperation> queryWrapper = new LambdaQueryWrapper<ProjectOperation>();
        queryWrapper.eq(ProjectOperation::getProjectId, id);
        Project project = projectService.getById(id);
        ProjectOperation projectOperation =  this.getOne(queryWrapper);
        ProjectOperationVo projectOperationVo =new ProjectOperationVo();
        if (ObjectUtil.isNotEmpty(projectOperation)){
            BeanUtils.copyProperties(projectOperation, projectOperationVo);
        }
        if (ObjectUtil.isNotEmpty(project)){
            projectOperationVo.setConstructionArea(project.getConstructionArea());
        }
        return projectOperationVo;
    }
    /**
     * 立项收缴率查询
     * @return 结果
     */
    @Override
    public ProjectCollectionRateVo collectionRateMessage(Long id){
        ProjectCollectionRateVo projectCollectionRateVo = new ProjectCollectionRateVo();
        Project project = projectService.getById(id);
        BeanUtils.copyProperties(project, projectCollectionRateVo);
        return projectCollectionRateVo;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectOperation> add(ProjectOperation projectOperation) {

        return R.ok(super.addExtension4Log(projectOperation,projectOperation.getProjectId()));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectOperation> update(ProjectOperation projectOperation) {
        return R.ok(super.updateExtension4Log(projectOperation,projectOperation.getProjectId()));
    }

    @Override
    public R deleteById(Long id) {
        ProjectOperation projectOperation = this.getById(id);

        super.deleteExtension(projectOperation,null,projectOperation.getProjectId());
        return R.ok();
    }
}