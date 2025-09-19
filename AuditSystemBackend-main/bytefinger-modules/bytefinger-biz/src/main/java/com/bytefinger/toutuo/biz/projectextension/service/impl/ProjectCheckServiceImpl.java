package com.bytefinger.toutuo.biz.projectextension.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.enums.ShiFou;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectTeam;
import com.bytefinger.toutuo.biz.project.mapper.ProjectTeamMapper;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCheck;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectCheckMapper;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectCheckService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCheckVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryVo;
import com.bytefinger.toutuo.common.service.BizService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProjectCheckServiceImpl extends BizService<ProjectCheckMapper, ProjectCheck> implements IProjectCheckService {

    @Autowired
    private ProjectCheckMapper projectCheckMapper;

    private final ProjectTeamMapper projectTeamMapper;

    @DataFill
    @Override
    public IPage<Project> projectPage(QueryPage queryPage) {
        Map map = queryPage.getParams();
//        if (ObjectUtils.isNotEmpty(map.get("checkState"))){
//            Object checkState = map.get("checkState");
//
//            queryPage.getParams().remove("checkState");
//            List<ProjectCheck> list = this.list();
//            if (checkState.equals(ShiFou.FOU.getCode())){
//                queryPage.getNotInParams().put("Id",list.stream().map(v->v.getProjectId()).collect(Collectors.toList()));
//            }else {
//                queryPage.getInParams().put("Id",list.stream().map(v->v.getProjectId()).collect(Collectors.toList()));
//            }
//        }
        String checkState = "checkState";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(checkState))) {
            queryPage.getDbParams().put("checkState", queryPage.getParams().get(checkState));
            queryPage.getParams().remove(checkState);
        }
        String serviceContent = "serviceContent";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceContent))) {
            queryPage.getDbParams().put("serviceContent", queryPage.getInParams().get(serviceContent));
            queryPage.getInParams().remove(serviceContent);
        }
        return projectCheckMapper.list(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());
    }
    /**
     * 承接查验详情
     * @return 结果
     */
    @Override
    public ProjectCheckVo get(Long projectId) {
        ProjectCheckVo projectCheckVo = new ProjectCheckVo();
        //在管项目查询 and 排除当前项目
        LambdaQueryWrapper<ProjectCheck> queryWrapper = new LambdaQueryWrapper<ProjectCheck>();
        queryWrapper.eq(ProjectCheck::getProjectId, projectId);
        ProjectCheck projectChecks = this.getOne(queryWrapper);

        if (ObjectUtil.isNotEmpty(projectChecks)){
            BeanUtils.copyProperties(projectChecks, projectCheckVo);
        }
        return projectCheckVo;
    }

    @Override
    public List<ProjectQueryVo> page(List<Project> projects) {
        //项管基础信息
//        Page<Project> list = projectMapper.list(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());
        List<ProjectQueryVo> projectQueryVos = new ArrayList<>();
        List<Long> projectIds = projects.stream().map(pro -> pro.getId()).collect(Collectors.toList());
        LambdaQueryWrapper<ProjectCheck> wrapperCheck = new LambdaQueryWrapper<ProjectCheck>();
        wrapperCheck.in(ProjectCheck::getProjectId, projectIds);
        wrapperCheck.notIn(ProjectCheck::getTemporary, 0);
        List<ProjectCheck> projectChecks = this.list(wrapperCheck);
        // IPage<ProjectQueryVo> projectQueryVoIPage = new Page<>();
        for (Project project : projects){
            ProjectQueryVo projectQueryVo = new ProjectQueryVo();
            BeanUtils.copyProperties(project, projectQueryVo);
            //当前登入人是否是项目归属人、拓后负责人、项目团队成员、创建人
            //项目团队成员
            List<ProjectTeam> projectTeamList =  projectTeamMapper.selectList(Wrappers.<ProjectTeam>lambdaQuery().eq(ProjectTeam::getRecordId, project.getId()).eq(ProjectTeam::getUserId, SecurityUtils.getUserId()));
            if((ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(projectTeamList) && projectTeamList.size()>0)){
                projectQueryVo.setShow(true);
            }else {
                projectQueryVo.setShow(false);
            }
            projectQueryVo.setCheckState(ObjectUtils.isNotEmpty(projectChecks) ? (projectChecks.stream().filter(check -> check.getProjectId().equals(project.getId())).count() > 0 ? ShiFou.SHI.getCode() : ShiFou.FOU.getCode()) : ShiFou.FOU.getCode());
            projectQueryVos.add(projectQueryVo);
        }
        return projectQueryVos;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectCheck> add(ProjectCheck projectCheck) {

        return R.ok(super.addExtension4Log(projectCheck,projectCheck.getProjectId()));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectCheck> update(ProjectCheck projectCheck) {

        return R.ok(super.updateExtension4Log(projectCheck,projectCheck.getProjectId()));
    }
}