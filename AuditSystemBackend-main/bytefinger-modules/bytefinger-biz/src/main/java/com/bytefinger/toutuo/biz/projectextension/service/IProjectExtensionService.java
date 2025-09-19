package com.bytefinger.toutuo.biz.projectextension.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCorrelation;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtension;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectStepExpansionMenu;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryTotalVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectTotalVo;
import com.bytefinger.toutuo.biz.projectstep.domain.ProjectStepMenu;

import java.util.List;

/**
 * 拓后管理续签状态
 *
 * @author ycj
 * @email 
 * @date 2023-03-09
 */
public interface IProjectExtensionService extends IService<ProjectExtension> {

    /**
     * 查询全部数据
     * 拼装用户信息
     *
     * @param projects
     * @return
     */
    List<ProjectQueryVo> page(List<Project> projects);

    List<ProjectQueryVo> details(Project project);

    /**
     * 续签和重新投标列表-加上归属人和拓后负责人
     * 拼装用户信息
     *
     * @param projects
     * @return
     */
    List<ProjectQueryVo> renewBidPage(List<Project> projects);

    List<ProjectStepExpansionMenu>  findProjectEXtensionStepMenuBy();

   IPage<Project> projectPage(QueryPage queryPage);

   List<Project> projectExpandQueryVos(List<Project> projects);

   IPage<Project> extensionProjectPage(QueryPage queryPage);

    IPage<Project> renewBidPage(QueryPage queryPage);

   String syncOperationProject(Long id);

   ProjectQueryTotalVo total(QueryPage queryPage);

    void renew(Long id);

    void newBid(Long id);


    IPage pageTwo(QueryPage queryPage);
    /**
     * 查询在管项目统计
     * 拼装用户信息
     *
     * @param queryPage
     * @return
     */
    ProjectTotalVo projectTotal(QueryPage queryPage);

    void cloneProjectFile(Long projectId, Long projectAddDataId);

    ProjectExtension add(Long projectId, Integer processState, Integer processMode, Project projectAddData);

    R<Project> saveRenewal(Project project);

    R<Object> rollbackProject(Long id);
}

