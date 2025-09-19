package com.bytefinger.toutuo.biz.projectextension.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCorrelation;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtension;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCorrelationVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryVo;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenu;

import java.util.List;

/**
 * 拓后运营项目关联
 *
 * @author ycj
 * @email 
 * @date 2023-03-09
 */
public interface IProjectCorrelationService extends IService<ProjectCorrelation> {
    List<ProjectCorrelationVo> get(Long id);

    ProjectCorrelation add(ProjectCorrelation projectCorrelation);

    ProjectCorrelation update(ProjectCorrelation projectCorrelation);

    /**
     * 删除风险
     *
     * @param id
     * @return
     */
    R deleteById(Long id);


    List<Project> projectRelevance(ProjectCorrelation projectCorrelation);
}

