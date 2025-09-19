package com.bytefinger.toutuo.biz.projectextension.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 承接查验
 * 
 * @author ycj
 * @email 
 * @date 2023-03-15 14:44:28
 */
@Mapper
public interface ProjectCheckMapper extends BaseMapper<ProjectCheck> {
    Page<Project> list(IPage<Project> page, @Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);
}
