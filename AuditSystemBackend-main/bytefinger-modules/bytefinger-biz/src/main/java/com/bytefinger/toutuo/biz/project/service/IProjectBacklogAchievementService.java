package com.bytefinger.toutuo.biz.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.biz.project.domain.ProjectAchievement;
import com.bytefinger.toutuo.biz.project.domain.ProjectBacklogAchievement;

import java.util.List;

/**
 * <p>
 * 项目业绩分配 补录 服务类
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
public interface IProjectBacklogAchievementService extends IService<ProjectBacklogAchievement> {

    List<ProjectBacklogAchievement> list(Long projectId);
}
