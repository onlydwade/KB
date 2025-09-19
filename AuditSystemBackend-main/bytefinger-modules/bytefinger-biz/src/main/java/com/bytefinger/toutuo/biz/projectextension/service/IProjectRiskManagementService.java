package com.bytefinger.toutuo.biz.projectextension.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * 拓后运营-风险管理
 *
 * @author ycj
 * @email 
 * @date 2023-03-20 15:05:12
 */
public interface IProjectRiskManagementService extends IService<ProjectRiskManagement> {
    /**
     * 查询风险
     *
     * @param stepMenuId
     * @return
     */
    public List<ProjectRiskManagement> listQuery(Long projectId,Long stepMenuId);

    /**
     * 新增风险
     *
     * @param projectRiskManagement
     * @return
     */
    AjaxResult add(ProjectRiskManagement projectRiskManagement);

    /**
     * 修改风险
     *
     * @param projectRiskManagement
     * @return
     */
    AjaxResult update(ProjectRiskManagement projectRiskManagement);

    /**
     * 删除风险
     *
     * @param id
     * @return
     */
    R deleteById(Long id);
}

