package com.bytefinger.toutuo.biz.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.toutuo.biz.project.domain.ProjectAchievement;
import com.bytefinger.toutuo.biz.project.domain.ProjectBacklogAchievement;
import com.bytefinger.toutuo.biz.project.mapper.ProjectAchievementMapper;
import com.bytefinger.toutuo.biz.project.mapper.ProjectBacklogAchievementMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectAchievementService;
import com.bytefinger.toutuo.biz.project.service.IProjectBacklogAchievementService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 项目业绩分配 服务实现类
 * </p>
 *
 * @author Jone
 * @since 2023-01-31
 */
@Service
public class ProjectBacklogAchievementServiceImpl extends ServiceImpl<ProjectBacklogAchievementMapper, ProjectBacklogAchievement> implements IProjectBacklogAchievementService {

    @DataFill
    @Override
    public List<ProjectBacklogAchievement> list(Long projectId) {
        return super.list(Wrappers.<ProjectBacklogAchievement>lambdaQuery().eq(ProjectBacklogAchievement::getProjectId, projectId).orderByDesc(ProjectBacklogAchievement::getCreateTime));
    }

}
