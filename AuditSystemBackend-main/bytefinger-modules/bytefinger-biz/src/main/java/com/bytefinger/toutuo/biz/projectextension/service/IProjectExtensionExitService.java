package com.bytefinger.toutuo.biz.projectextension.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtensionExit;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectIntervene;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectExtensionExitVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectInterveneVo;

import java.util.List;
import java.util.Map;

/**
 * 拓后项目退场
 *
 * @author ycj
 * @email 
 * @date 2023-03-27 16:23:19
 */
public interface IProjectExtensionExitService extends IService<ProjectExtensionExit> {

    ProjectExtensionExit get(Long stepMenuId,Long projectId);

    /**
     * 查询拓后项目退场
     *
     * @param queryWrapper
     * @return
     */
    public List<ProjectExtensionExit> listQuery(LambdaQueryWrapper<ProjectExtensionExit> queryWrapper);

    /**
     * 新增拓后项目退场
     *
     * @param projectExtensionExit
     * @return
     */
    R<ProjectExtensionExit> add(ProjectExtensionExit projectExtensionExit);

    /**
     * 修改拓后项目退场
     *
     * @param projectExtensionExit
     * @return
     */
    R<ProjectExtensionExit> update(ProjectExtensionExit projectExtensionExit);

    //    /**
//     * 删除项目干预
//     *
//     * @param id
//     * @return
//     */
//    R deleteById(Long id);
    IPage<Project> projectPage(QueryPage queryPage);


    List<ProjectExtensionExitVo> listPage(List<Project> projects);

}

