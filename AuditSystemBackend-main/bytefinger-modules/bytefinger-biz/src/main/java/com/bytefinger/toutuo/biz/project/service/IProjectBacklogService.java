package com.bytefinger.toutuo.biz.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.project.domain.ProjectBacklog;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目补录
 *
 */
public interface IProjectBacklogService extends IService<ProjectBacklog> {

    IPage<ProjectBacklog> projectBacklogPage(QueryPage queryPage);

    R<Object> add(ProjectBacklog projectBacklog);

    R<Object> submit(ProjectBacklog projectBacklog);

    R<Object> update(ProjectBacklog projectBacklog);

    Integer getCheckOa();

    void setCheckOa(Integer checkValue);

    void oaApprovalPass(Long projectId, Integer approvalStatus);

    void setYtProjetList(List<ProjectBacklog> projectList);

    BigDecimal calculateAnnualConversion(BigDecimal contractAmount, Integer servicePeriod, Date serviceBeginTime, Date serviceEndTime, Date performanceConfirmTime);

    Boolean isAdministrators();
}

