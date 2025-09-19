package com.bytefinger.toutuo.biz.projectextension.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.biz.project.bo.ProjectAchievementBO;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtension;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectTotalDetailVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectTotalVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 拓后管理续签状态
 * 
 * @author ycj
 * @email 
 * @date 2023-03-09
 */
@Mapper
public interface ProjectExtensionMapper extends BaseMapper<ProjectExtension> {
    Page<Project> list(IPage<Project> page, @Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Page<Project> extensionList(IPage<Project> page, @Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Page<Project> projectList(IPage<Project> page,@Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Page<Project> projectPageList(IPage<Project> page,@Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);


    Page<Project> renewBidList(IPage<Project> page,@Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    //在管项目数
    List<ProjectTotalDetailVo> projectTotal(@Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    IPage<ProjectAchievementBO> listTwo(IPage<ProjectAchievementBO> page, @Param(Constants.WRAPPER) Wrapper<ProjectAchievementBO> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

}
