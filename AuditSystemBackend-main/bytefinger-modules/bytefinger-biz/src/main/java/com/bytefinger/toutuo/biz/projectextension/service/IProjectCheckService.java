package com.bytefinger.toutuo.biz.projectextension.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCheck;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCheckVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectOperationVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 承接查验
 *
 * @author ycj
 * @email 
 * @date 2023-03-15 14:44:28
 */
public interface IProjectCheckService extends IService<ProjectCheck> {
    ProjectCheckVo get(Long id);

    List<ProjectQueryVo> page(List<Project> projects);

    public IPage<Project> projectPage(QueryPage queryPage);

    /**
     * 新增承接查验
     *
     * @param projectCheck
     * @return
     */
    R<ProjectCheck> add(ProjectCheck projectCheck);

    /**
     * 修改承接查验
     *
     * @param projectCheck
     * @return
     */
    R<ProjectCheck> update(ProjectCheck projectCheck);

}

