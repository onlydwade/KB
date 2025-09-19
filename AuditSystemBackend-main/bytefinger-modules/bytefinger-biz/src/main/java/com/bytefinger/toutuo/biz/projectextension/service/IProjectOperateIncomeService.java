package com.bytefinger.toutuo.biz.projectextension.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectOperateIncome;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCorrelationVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectOperateIncomeVo;

import java.util.List;
import java.util.Map;

/**
 * 经营管理-项目收支
 *
 * @author ycj
 * @email 
 * @date 2023-03-17 14:23:41
 */
public interface IProjectOperateIncomeService extends IService<ProjectOperateIncome> {

    List<ProjectOperateIncome> list(Long projectId,Long stepMenuId);

    /**
     * 新增经营管理-项目收支
     *
     * @param projectOperateIncome
     * @return
     */
    R<ProjectOperateIncome> add(ProjectOperateIncome projectOperateIncome);

    /**
     * 修改经营管理-项目收支
     *
     * @param projectOperateIncome
     * @return
     */
    R<ProjectOperateIncome> update(ProjectOperateIncome projectOperateIncome);

    /**
     * 删除经营管理-项目收支
     *
     * @param id
     * @return
     */
    R deleteById(Long id);

}

