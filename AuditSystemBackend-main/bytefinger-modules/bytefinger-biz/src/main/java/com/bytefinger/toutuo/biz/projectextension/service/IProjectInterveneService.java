package com.bytefinger.toutuo.biz.projectextension.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectAssess;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectIntervene;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectAssessVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectInterveneVo;

import java.util.List;
import java.util.Map;

/**
 * 拓后运营项目干预
 *
 * @author ycj
 * @email
 * @date 2023-03-21 16:14:17
 */
public interface IProjectInterveneService extends IService<ProjectIntervene> {
    IPage<ProjectIntervene> page(QueryPage queryPage, Long stepMenuId);

    /**
     * 查询项目干预
     *
     * @param queryWrapper
     * @return
     */
    public List<ProjectIntervene> listQuery(LambdaQueryWrapper<ProjectIntervene> queryWrapper);

    /**
     * 新增项目干预
     *
     * @param projectIntervene
     * @return
     */
    AjaxResult add(ProjectIntervene projectIntervene);

    R<ProjectIntervene> isNotPass(ProjectIntervene projectIntervene);

    /**
     * 修改项目干预
     *
     * @param projectIntervene
     * @return
     */
    AjaxResult update(ProjectIntervene projectIntervene);

    //    /**
//     * 删除项目干预
//     *
//     * @param id
//     * @return
//     */
//    R deleteById(Long id);
    IPage<Project> projectPage(QueryPage queryPage);


    List<ProjectInterveneVo> listPage(List<Project> projects);

    ProjectIntervene get(Long stepMenuId, Long id);
}

