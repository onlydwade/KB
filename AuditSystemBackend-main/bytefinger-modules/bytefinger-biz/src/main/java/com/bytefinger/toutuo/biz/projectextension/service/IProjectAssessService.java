package com.bytefinger.toutuo.biz.projectextension.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyExecutives;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectAssess;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectAssessVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryVo;
import org.springframework.util.CollectionUtils;


import java.util.List;
import java.util.Map;

/**
 * 项目评估
 *
 * @author ycj
 * @email 
 * @date 2023-03-16 10:18:21
 */
public interface IProjectAssessService extends IService<ProjectAssess> {
    IPage<ProjectAssess> page(QueryPage queryPage,Long stepMenuId);

    List<ProjectAssessVo> listPage(List<Project> projects);


    ProjectAssess get(Long stepMenuId, Long id);
    /**
     * 查询全部数据
     * 拼装用户信息
     *
     * @param queryPage
     * @return
     */
    IPage<Project> projectPage(QueryPage queryPage);


    /**
     * 新增评估
     *
     * @param projectAssess
     * @return
     */
    AjaxResult add(ProjectAssess projectAssess);

    /**
     * 修改评估
     *
     * @param projectAssess
     * @return
     */
    AjaxResult update(ProjectAssess projectAssess);

    /**
     * 删除评估
     *
     * @param id
     * @return
     */
    AjaxResult deleteById(Long id);
}

