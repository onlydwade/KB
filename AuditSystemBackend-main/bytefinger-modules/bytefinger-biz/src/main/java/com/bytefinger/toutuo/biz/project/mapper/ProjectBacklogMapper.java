package com.bytefinger.toutuo.biz.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.biz.project.domain.ProjectBacklog;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExpansionDocument;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 项目 补录
 *
 */
@Mapper
public interface ProjectBacklogMapper extends BaseMapper<ProjectBacklog> {

    Page<ProjectBacklog> list(IPage<ProjectBacklog> page, @Param(Constants.WRAPPER) Wrapper<ProjectBacklog> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Integer getGroupConfig();

    void setGroupConfig(@Param("configValue") Integer configValue);

    List<String> getUserRoleCodes(@Param("userId") Long userId);

    String getPostCode(@Param("postId") Long postId);

}
